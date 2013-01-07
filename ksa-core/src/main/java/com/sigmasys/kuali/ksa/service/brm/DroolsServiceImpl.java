package com.sigmasys.kuali.ksa.service.brm;

import com.sigmasys.kuali.ksa.exception.InvalidRulesException;
import com.sigmasys.kuali.ksa.model.Constants;
import com.sigmasys.kuali.ksa.model.rule.Rule;
import com.sigmasys.kuali.ksa.model.rule.RuleSet;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.*;
import org.drools.command.Command;
import org.drools.command.CommandFactory;
import org.drools.definition.KnowledgePackage;
import org.drools.io.Resource;
import org.drools.io.ResourceFactory;
import org.drools.runtime.ExecutionResults;
import org.drools.runtime.StatelessKnowledgeSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.StringReader;
import java.util.*;

/**
 * Drools implementation of BrmService
 *
 * @author Michael Ivanov
 */
@Service("droolsService")
@Transactional
@SuppressWarnings("unchecked")
public class DroolsServiceImpl implements BrmService {

    private static final Log logger = LogFactory.getLog(DroolsServiceImpl.class);


    public static final String DSL_FILE_NAME = Constants.DROOLS_CLASSPATH + "/" + "ksa.dsl";

    public static final String DROOLS_CONTEXT_NAME = "droolsContext";

    static {
        System.setProperty("drools.compiler", "JANINO");
        System.setProperty("drools.dialect.java.compiler", "JANINO");
    }

    @Autowired
    private BrmPersistenceService brmPersistenceService;

    private Resource dslResource;

    // Map of <DRL file name, KnowledgeBase> objects
    private final Map<String, KnowledgeBase> knowledgeBases = new HashMap<String, KnowledgeBase>();


    @PostConstruct
    private void postConstruct() {
        logger.info("Initializing DSL resource '" + DSL_FILE_NAME + "'");
        dslResource = ResourceFactory.newClassPathResource(DSL_FILE_NAME);
        logger.info("DSL resource '" + DSL_FILE_NAME + "' has been initialized");
    }

    private Collection<KnowledgePackage> buildKnowledgePackages(RuleSet ruleSet) {

        // Performing pre-validation
        preValidateRuleSet(ruleSet);

        // Getting rules in Drools String (DRL, DSLR, etc.) format
        String ruleSetContent = DroolsRuleBuilder.toString(ruleSet);
        logger.info("Building Drools knowledge base ->\n" + ruleSetContent);

        // Initializing RuleSet resource and resource type
        Resource rulesResource = ResourceFactory.newReaderResource(new StringReader(ruleSetContent));
        ResourceType resourceType = ResourceType.getResourceType(ruleSet.getType().getName());

        // Building the knowledge base
        KnowledgeBuilder builder = KnowledgeBuilderFactory.newKnowledgeBuilder();

        if (ResourceType.DSLR == resourceType) {
            builder.add(dslResource, ResourceType.DSL);
        }

        builder.add(rulesResource, resourceType);

        // Handling errors if there are any
        handleErrors(builder.getErrors(), ruleSet.getName());

        // Returning the constructed knowledge base packages
        return builder.getKnowledgePackages();
    }

    private synchronized KnowledgeBase getKnowledgeBase(RuleSet ruleSet) {
        try {
            KnowledgeBase knowledgeBase = knowledgeBases.get(ruleSet.getName());
            if (knowledgeBase == null) {
                Collection<KnowledgePackage> knowledgePackages = buildKnowledgePackages(ruleSet);
                knowledgeBase = KnowledgeBaseFactory.newKnowledgeBase();
                knowledgeBase.addKnowledgePackages(knowledgePackages);
                knowledgeBases.put(ruleSet.getName(), knowledgeBase);
            }
            return knowledgeBase;
        } catch (InvalidRulesException ire) {
            logger.error(ire.getMessage(), ire);
            throw ire;
        } catch (Throwable t) {
            String errMsg = "Cannot retrieve KnowledgeBase from '" + ruleSet.getName() + "'";
            logger.error(errMsg, t);
            throw new RuntimeException(errMsg, t);
        }
    }

    private void handleErrors(KnowledgeBuilderErrors errors, String ruleSetName) {
        if (errors != null && !errors.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder();
            errorMessage.append("There are problems in compiling the rules for '").append(ruleSetName).append("' \n");
            for (KnowledgeBuilderError error : errors) {
                errorMessage.append(error).append("\n");
            }
            logger.error(errorMessage);
            throw new InvalidRulesException(errorMessage.toString());
        }
    }

    private <T> T fireRules(KnowledgeBase knowledgeBase, T droolsContext, Map<String, Object> globalParams) {
        try {
            StatelessKnowledgeSession session = knowledgeBase.newStatelessKnowledgeSession();
            if (globalParams != null) {
                for (Map.Entry<String, Object> entry : globalParams.entrySet()) {
                    session.setGlobal(entry.getKey(), entry.getValue());
                }
            }
            Command command = CommandFactory.newInsert(droolsContext, DROOLS_CONTEXT_NAME);
            ExecutionResults results = session.execute(CommandFactory.newBatchExecution(Arrays.asList(command)));
            return (T) results.getValue(DROOLS_CONTEXT_NAME);
        } catch (Throwable t) {
            logger.error(t.getMessage(), t);
            throw new RuntimeException(t.getMessage(), t);
        }
    }

    private RuleSet getRuleSet(String ruleSetName) {
        RuleSet ruleSet = brmPersistenceService.getRuleSet(ruleSetName);
        if (ruleSet == null) {
            String errMsg = "Rule Set specified by name '" + ruleSetName + "' does not exist";
            logger.error(errMsg);
            throw new IllegalStateException(errMsg);
        }
        return ruleSet;
    }

    private RuleSet getRuleSet(Long ruleSetId) {
        RuleSet ruleSet = brmPersistenceService.getRuleSet(ruleSetId);
        if (ruleSet == null) {
            String errMsg = "Rule Set specified by ID = " + ruleSetId + " does not exist";
            logger.error(errMsg);
            throw new IllegalStateException(errMsg);
        }
        return ruleSet;
    }

    private void preValidateRuleSet(RuleSet ruleSet) {
        if (!StringUtils.hasText(ruleSet.getHeader())) {
            String errMsg = "Rule Set '" + ruleSet.getName() + "' must have a non-empty header";
            logger.error(errMsg);
            throw new InvalidRulesException(errMsg);
        }
        Set<Rule> rules = ruleSet.getRules();
        if (CollectionUtils.isEmpty(rules)) {
            String errMsg = "Rule Set '" + ruleSet.getName() + "' must have at least one rule";
            logger.error(errMsg);
            throw new InvalidRulesException(errMsg);
        }
        Long ruleTypeId = ruleSet.getType().getId();
        for (Rule rule : ruleSet.getRules()) {
            if (!ruleTypeId.equals(rule.getType().getId())) {
                String errMsg = "Rule Set's type '" + ruleSet.getType().getName() +
                        "' does not match Rule's type '" + rule.getType().getName() + "'";
                logger.error(errMsg);
                throw new InvalidRulesException(errMsg);
            }
        }
    }

    // ------------------------   PUBLIC METHOD DEFINITIONS -----------------------------------------------------

    @Override
    public <T> T fireRules(String ruleSetName, T brmContext, Map<String, Object> globalParams) {
        return fireRules(getKnowledgeBase(getRuleSet(ruleSetName)), brmContext, globalParams);
    }

    @Override
    public <T> T fireRules(String ruleSetName, T brmContext) {
        return fireRules(getKnowledgeBase(getRuleSet(ruleSetName)), brmContext, null);
    }

    @Override
    public <T> T fireRules(Long ruleSetId, T brmContext, Map<String, Object> globalParams) {
        return fireRules(getKnowledgeBase(getRuleSet(ruleSetId)), brmContext, globalParams);
    }

    @Override
    public <T> T fireRules(Long ruleSetId, T brmContext) {
        return fireRules(getKnowledgeBase(getRuleSet(ruleSetId)), brmContext, null);
    }

    @Override
    public void validateRuleSet(RuleSet ruleSet) {
        buildKnowledgePackages(ruleSet);
    }

    @Override
    public void refresh() {
        knowledgeBases.clear();
        postConstruct();
    }

    @Override
    public void reloadRuleSet(String ruleSetName) {
        knowledgeBases.remove(ruleSetName);
        getKnowledgeBase(getRuleSet(ruleSetName));
    }

}