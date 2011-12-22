/**
 * Copyright 2011 The Kuali Foundation Licensed under the
 * Educational Community License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 * http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package org.kuali.student.process.poc.krms;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.joda.time.DateTime;
import org.kuali.rice.krms.api.engine.EngineResults;
import org.kuali.rice.krms.api.engine.ExecutionFlag;
import org.kuali.rice.krms.api.engine.ExecutionOptions;
import org.kuali.rice.krms.api.engine.ResultEvent;
import org.kuali.rice.krms.api.engine.SelectionCriteria;
import org.kuali.rice.krms.api.engine.TermResolver;
import org.kuali.rice.krms.api.repository.LogicalOperator;
import org.kuali.rice.krms.framework.engine.Agenda;
import org.kuali.rice.krms.framework.engine.AgendaTreeEntry;
import org.kuali.rice.krms.framework.engine.BasicAgenda;
import org.kuali.rice.krms.framework.engine.BasicAgendaTree;
import org.kuali.rice.krms.framework.engine.BasicAgendaTreeEntry;
import org.kuali.rice.krms.framework.engine.BasicContext;
import org.kuali.rice.krms.framework.engine.BasicRule;
import org.kuali.rice.krms.framework.engine.CompoundProposition;
import org.kuali.rice.krms.framework.engine.Context;
import org.kuali.rice.krms.framework.engine.ContextProvider;
import org.kuali.rice.krms.framework.engine.Proposition;
import org.kuali.rice.krms.framework.engine.ProviderBasedEngine;
import org.kuali.student.common.util.krms.ManualContextProvider;
import org.kuali.student.common.util.krms.RulesExecutionConstants;
import org.kuali.student.enrollment.acal.dto.TermInfo;
import org.kuali.student.enrollment.acal.service.AcademicCalendarService;
import org.kuali.student.process.poc.context.CourseRegistrationProcessContextInfo;
import org.kuali.student.process.poc.evaluator.ProcessEvaluator;
import org.kuali.student.process.poc.krms.proposition.MilestoneDateComparisonProposition;
import org.kuali.student.process.poc.krms.proposition.MilestoneDateComparisonProposition.DateComparisonType;
import org.kuali.student.process.poc.krms.proposition.PersonLivingProposition;
import org.kuali.student.process.poc.krms.proposition.RegistrationHoldProposition;
import org.kuali.student.process.poc.krms.proposition.SummerTermProposition;
import org.kuali.student.process.poc.util.InstructionComparator;
import org.kuali.student.r2.common.dto.ContextInfo;
import org.kuali.student.r2.common.dto.ValidationResultInfo;
import org.kuali.student.r2.common.exceptions.DoesNotExistException;
import org.kuali.student.r2.common.exceptions.InvalidParameterException;
import org.kuali.student.r2.common.exceptions.MissingParameterException;
import org.kuali.student.r2.common.exceptions.OperationFailedException;
import org.kuali.student.r2.common.exceptions.PermissionDeniedException;
import org.kuali.student.r2.common.util.constants.ExemptionServiceConstants;
import org.kuali.student.r2.common.util.constants.ProcessServiceConstants;
import org.kuali.student.r2.core.exemption.dto.ExemptionInfo;
import org.kuali.student.r2.core.exemption.infc.DateOverride;
import org.kuali.student.r2.core.exemption.service.ExemptionService;
import org.kuali.student.r2.core.hold.dto.HoldInfo;
import org.kuali.student.r2.core.hold.service.HoldService;
import org.kuali.student.r2.core.population.service.PopulationService;
import org.kuali.student.r2.core.process.dto.CheckInfo;
import org.kuali.student.r2.core.process.dto.InstructionInfo;
import org.kuali.student.r2.core.process.service.ProcessService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class to build up a Proposition tree for to evaluate checks from a Process
 *
 * @author alubbers
 */
public class KRMSProcessEvaluator implements ProcessEvaluator<CourseRegistrationProcessContextInfo> {

    public static final EXEMPTION_WAS_USED_MESSAGE_SUFFIX = " (exemption applied)"

    private AcademicCalendarService acalService;
    private ProcessService processService;
    private PopulationService populationService;
    private ExemptionService exemptionService;
    private HoldService holdService;
    private List<TermResolver<?>> termResolvers;
    private ExecutionOptions executionOptions;
    private SelectionCriteria selectionCriteria;

    public void setAcalService(AcademicCalendarService acalService) {
        this.acalService = acalService;
    }

    public void setProcessService(ProcessService processService) {
        this.processService = processService;
    }

    public void setPopulationService(PopulationService populationService) {
        this.populationService = populationService;
    }

    public void setExemptionService(ExemptionService exemptionService) {
        this.exemptionService = exemptionService;
    }

    public void setHoldService(HoldService holdService) {
        this.holdService = holdService;
    }

    @Override
    public List<ValidationResultInfo> evaluate(CourseRegistrationProcessContextInfo processContext, ContextInfo context)
            throws OperationFailedException {

        List<InstructionInfo> instructions;
        try {
            instructions = processService.getInstructionsByProcess(processContext.getProcessKey(), context);
        } catch (OperationFailedException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new OperationFailedException("unexpected", ex);
        }

        TermInfo term = null;

        if (processContext.getTermKey() != null) {
            try {
                term = acalService.getTerm(processContext.getTermKey(), context);
            } catch (OperationFailedException ex) {
                throw ex;
            } catch (Exception ex) {
                throw new OperationFailedException("unexpected", ex);
            }
        }

        List<InstructionInfo> sortedInstructions = new ArrayList<InstructionInfo>();

        // filter out Instructions that do apply to the context
        for (InstructionInfo instruction : instructions) {

            // filter out by term
            if (term != null && !instruction.getAppliedAtpTypeKeys().contains(term.getTypeKey())) {
                continue;
            }

            // filter out by applicable Population
            boolean skipInstruction = true;
            for (String popKey : instruction.getAppliedPopulationKeys()) {
                try {
                    if (populationService.isMember(processContext.getStudentId(), popKey, context)) {
                        skipInstruction = false;
                        break;
                    }
                } catch (OperationFailedException ex) {
                    throw ex;
                } catch (Exception ex) {
                    throw new OperationFailedException("unexpected", ex);
                }
            }

            // check for any direct exemptions the student may have for this check
            List<ExemptionInfo> exemptions;
            try {
                exemptions = exemptionService.getActiveExemptionsByTypeProcessAndCheckForPerson(ExemptionServiceConstants.CHECK_EXEMPTION_TYPE_KEY, processContext.getProcessKey(), instruction.getCheckKey(), processContext.getStudentId(), context);
            } catch (OperationFailedException ex) {
                throw ex;
            } catch (Exception ex) {
                throw new OperationFailedException("unexpected", ex);
            }
            if (!exemptions.isEmpty()) {
                skipInstruction = true;
            }

            if (skipInstruction) {
                continue;
            }

            sortedInstructions.add(instruction);
        }

        Collections.sort(sortedInstructions, new InstructionComparator());

        // build a list of propositions based on the sorted instructions, using a LinkedHashMap to maintain the same sorting order
        Map<Proposition, InstructionInfo> propositions = new LinkedHashMap<Proposition, InstructionInfo>(sortedInstructions.size());
        for (InstructionInfo instruction : sortedInstructions) {

            CheckInfo check;
            try {
                check = processService.getCheck(instruction.getCheckKey(), context);
            } catch (OperationFailedException ex) {
                throw ex;
            } catch (Exception ex) {
                throw new OperationFailedException("unexpected", ex);
            }

            // TODO handle sub-process?

            if (check.getTypeKey().equals(ProcessServiceConstants.HOLD_CHECK_TYPE_KEY)) {
                propositions.put(new RegistrationHoldProposition(check.getIssueKey()), instruction);
            } else if (check.getKey().equals(ProcessServiceConstants.CHECK_KEY_IS_ALIVE)) {
                propositions.put(new PersonLivingProposition(), instruction);
            } else if (check.getKey().equals(ProcessServiceConstants.CHECK_KEY_IS_NOT_SUMMER_TERM)) {
                propositions.put(new SummerTermProposition(term), instruction);
            } else if (check.getTypeKey().equals(ProcessServiceConstants.START_DATE_CHECK_TYPE_KEY)) {
                propositions.put(buildMilestoneCheckProposition(check, DateComparisonType.AFTER, processContext, context), instruction);
            } else if (check.getTypeKey().equals(ProcessServiceConstants.DEADLINE_CHECK_TYPE_KEY)) {
                propositions.put(buildMilestoneCheckProposition(check, DateComparisonType.BEFORE, processContext, context), instruction);
            } else if (check.getTypeKey().equals(ProcessServiceConstants.TIME_PERIOD_CHECK_TYPE_KEY)) {
                propositions.put(buildMilestoneCheckProposition(check, DateComparisonType.BETWEEN, processContext, context), instruction);
            }
        }

        // Build the list of known facts prior to execution
        Map<String, Object> executionFacts = buildExecutionFacts(processContext, context);

        // Combine all propositions into a CompoundProposition and evaluate the results
        EngineResults krmsResults = evaluateProposition(new CompoundProposition(LogicalOperator.AND, new ArrayList<Proposition>(propositions.keySet())), executionFacts);


        List<ValidationResultInfo> results;
        try {
            results = buildValidationResultsFromEngineResults(krmsResults, propositions, context);   
        } catch (OperationFailedException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new OperationFailedException("unexpected", ex);
        }

        return results;
    }

    private MilestoneDateComparisonProposition buildMilestoneCheckProposition(CheckInfo check, DateComparisonType comparisonType, CourseRegistrationProcessContextInfo processContext, ContextInfo context)
            throws OperationFailedException {
        List<ExemptionInfo> exemptions;
        try {
            exemptions = exemptionService.getActiveExemptionsByTypeProcessAndCheckForPerson(ExemptionServiceConstants.MILESTONE_DATE_EXEMPTION_TYPE_KEY, processContext.getProcessKey(), check.getKey(), processContext.getStudentId(), context);
        } catch (OperationFailedException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new OperationFailedException("unexpected", ex);
        }

        if (exemptions.isEmpty()) {
            return new MilestoneDateComparisonProposition(RulesExecutionConstants.CURRENT_DATE_TERM_NAME, comparisonType, check.getMilestoneTypeKey(), true, null);
        }

        // For now, assume there is only one active Exemption
        DateOverride dateOverrideInfo = exemptions.get(0).getDateOverride();

        return new MilestoneDateComparisonProposition(RulesExecutionConstants.CURRENT_DATE_TERM_NAME, comparisonType, check.getMilestoneTypeKey(), true, dateOverrideInfo);
    }

    private EngineResults evaluateProposition(Proposition proposition, Map<String, Object> executionFacts) {

        // Build the KRMS agenda and other startup objects to execute
        List<AgendaTreeEntry> treeEntries = new ArrayList<AgendaTreeEntry>(1);
        treeEntries.add(new BasicAgendaTreeEntry(new BasicRule(proposition, null)));

        Agenda agenda = new BasicAgenda(null, new BasicAgendaTree(treeEntries));

        Context context = new BasicContext(Arrays.asList(agenda), termResolvers);
        ContextProvider contextProvider = new ManualContextProvider(context);

        ProviderBasedEngine engine = new ProviderBasedEngine();
        engine.setContextProvider(contextProvider);

        if (executionOptions == null) {
            executionOptions = new ExecutionOptions();
            executionOptions.setFlag(ExecutionFlag.LOG_EXECUTION, true);
            executionOptions.setFlag(ExecutionFlag.EVALUATE_ALL_PROPOSITIONS, true);
        }

        if (selectionCriteria == null) {
            Map<String, String> contextQualifiers = Collections.singletonMap(RulesExecutionConstants.DOCTYPE_CONTEXT_QUALIFIER, RulesExecutionConstants.STUDENT_ELIGIBILITY_DOCTYPE);

            Map<String, String> empty = Collections.emptyMap();
            selectionCriteria = SelectionCriteria.createCriteria(new DateTime(), contextQualifiers, empty);
        }

        return engine.execute(selectionCriteria, executionFacts, executionOptions);
    }

    private List<ValidationResultInfo> buildValidationResultsFromEngineResults(EngineResults engineResults, Map<Proposition, InstructionInfo> propositionInstructionMap, ContextInfo context) throws InvalidParameterException, MissingParameterException, DoesNotExistException, PermissionDeniedException, OperationFailedException {
        List<ValidationResultInfo> results = new ArrayList<ValidationResultInfo>();

        // go through all the results from the Propositions, and build validation results based on any propositions that failed
        List<ResultEvent> events = engineResults.getResultsOfType(ResultEvent.PropositionEvaluated);
        for (ResultEvent e : events) {
            Proposition prop = (Proposition) e.getSource();
            InstructionInfo instruction = propositionInstructionMap.get(prop);
            ValidationResultInfo result = new ValidationResultInfo();
            String message = instruction.getMessage().getPlain();
            ExemptionAwareProposition exemptionProp = null;
            if(prop instanceof ExemptionAwareProposition) {
                exemptionProp = (ExemptionAwareProposition)prop;
                if(exemptionProp.isExemptionUsed()) {
                    message += EXEMPTION_WAS_USED_MESSAGE_SUFFIX;
                }
            }
            if (e.getResult()) {
                if(exemptionProp != null && exemptionProp.isExemptionUsed()) {
                    result.setInfo(message);
                }
                results.add(result);
            }
            else {


                if (instruction.getIsWarning()) {
                    result.setWarn(message);
                } else {
                    result.setError(message);
                }



                results.add(result);

                if (!instruction.getContinueOnFail()) {
                    break;
                }
            }
        }

        // Now check if there are any warnings from Holds that are marked as warning only
        List<String> warningHoldIds = (List<String>) engineResults.getAttribute(RulesExecutionConstants.REGISTRATION_HOLD_WARNINGS_ATTRIBUTE);

        if (warningHoldIds != null && !warningHoldIds.isEmpty()) {
            for (String holdId : warningHoldIds) {
                HoldInfo hold = holdService.getHold(holdId, context);
                ValidationResultInfo result = new ValidationResultInfo();
                result.setWarn("The following hold was found on the student's account, but set as a warning: " + hold.getDescr().getPlain());
                results.add(result);
            }
        }

        return results;
    }

    public List<ValidationResultInfo> evaluateStudentAliveRule(InstructionInfo instruction, CourseRegistrationProcessContextInfo processContext, ContextInfo context) throws InvalidParameterException, MissingParameterException, DoesNotExistException, OperationFailedException, PermissionDeniedException {
        List<ValidationResultInfo> results = new ArrayList<ValidationResultInfo>();

        // Build the list of known facts prior to execution
        Map<String, Object> executionFacts = buildExecutionFacts(processContext, context);

        // evaluate the results for just the one proposition
        Proposition prop = new PersonLivingProposition();
        EngineResults engineResults = evaluateProposition(prop, executionFacts);

        Map<Proposition, InstructionInfo> propInstructionMap = Collections.singletonMap(prop, instruction);

        return buildValidationResultsFromEngineResults(engineResults, propInstructionMap, context);
    }

    public List<ValidationResultInfo> evaluateSummerTermRule(InstructionInfo instruction, CourseRegistrationProcessContextInfo processContext, ContextInfo context) throws InvalidParameterException, MissingParameterException, DoesNotExistException, OperationFailedException, PermissionDeniedException {
        List<ValidationResultInfo> results = new ArrayList<ValidationResultInfo>();

        TermInfo term = acalService.getTerm(processContext.getTermKey(), context);

        // Build the list of known facts prior to execution
        Map<String, Object> executionFacts = buildExecutionFacts(processContext, context);

        // evaluate the results for just the one proposition
        Proposition prop = new SummerTermProposition(term);
        EngineResults engineResults = evaluateProposition(prop, executionFacts);

        Map<Proposition, InstructionInfo> propInstructionMap = Collections.singletonMap(prop, instruction);

        return buildValidationResultsFromEngineResults(engineResults, propInstructionMap, context);
    }

    private Map<String, Object> buildExecutionFacts(CourseRegistrationProcessContextInfo processContext, ContextInfo context) {
        Map<String, Object> executionFacts = new HashMap<String, Object>();
        executionFacts.put(RulesExecutionConstants.STUDENT_ID_TERM_NAME, processContext.getStudentId());
        executionFacts.put(RulesExecutionConstants.CONTEXT_INFO_TERM_NAME, context);
        if (processContext.getTermKey() != null) {
            executionFacts.put(RulesExecutionConstants.REGISTRATION_TERM_TERM_NAME, processContext.getTermKey());
        }
        return executionFacts;
    }

    public void setTermResolvers(List<TermResolver<?>> termResolvers) {
        this.termResolvers = termResolvers;
    }

    public void setExecutionOptions(ExecutionOptions executionOptions) {
        this.executionOptions = executionOptions;
    }
}
