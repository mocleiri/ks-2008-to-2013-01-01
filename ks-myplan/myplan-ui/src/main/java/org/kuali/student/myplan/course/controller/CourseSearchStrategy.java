package org.kuali.student.myplan.course.controller;

import org.apache.log4j.Logger;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.student.common.search.dto.*;
import org.kuali.student.core.enumerationmanagement.dto.EnumeratedValueInfo;
import org.kuali.student.core.enumerationmanagement.service.EnumerationManagementService;
import org.kuali.student.lum.lu.service.LuService;
import org.kuali.student.lum.lu.service.LuServiceConstants;
import org.kuali.student.myplan.course.form.CourseSearchForm;
import org.kuali.student.myplan.course.util.CourseSearchConstants;

import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class CourseSearchStrategy {
    private final Logger logger = Logger.getLogger(CourseSearchStrategy.class);

    private transient EnumerationManagementService enumService;

    private transient LuService luService;


    protected synchronized EnumerationManagementService getEnumerationService() {
        if (this.enumService == null) {
            this.enumService = (EnumerationManagementService) GlobalResourceLoader
                    .getService(new QName(CourseSearchConstants.ENUM_SERVICE_NAMESPACE, "EnumerationManagementService"));
        }
        return this.enumService;
    }

    public HashMap<String, String> fetchCourseDivisions() {
        HashMap<String, String> map = new HashMap<String, String>();
        try {
            SearchRequest request = new SearchRequest("myplan.distinct.clu.divisions");

            SearchResult result = getLuService().search(request);

            for (SearchResultRow row : result.getRows()) {
                for (SearchResultCell cell : row.getCells()) {
                    String division = cell.getValue();
                    // Store both trimmed and original, because source data
                    // is sometimes space padded.
                    String key = division.trim().replaceAll("\\s+", "");
                    map.put(key, division);
                }
            }
        } catch (Exception e) {
            // TODO: Handle this exception better
            e.printStackTrace();
        }
        return map;
    }


    // TODO: Fetch these from the enumeration service, ala CourseDetailsInquiryViewHelperServiceImpl.initializeCampusLocations
    public final static String NO_CAMPUS = "-1";


    public void addCampusParams(ArrayList<SearchRequest> requests, CourseSearchForm form) {
        String str = form.getCampusSelect();
        String[] results = null;
        if (!str.equalsIgnoreCase("")) {
            results = str.split(",");
        }

        List<EnumeratedValueInfo> enumeratedValueInfoList = null;
        try {

            enumeratedValueInfoList = getEnumerationService().getEnumeratedValues("kuali.lu.campusLocation", null, null, null);
        } catch (Exception e) {
            logger.error("No Values for campuses found", e);
        }
        String[] campus = new String[enumeratedValueInfoList.size() - 1];
        for (int k = 0; k < campus.length; k++) {
            campus[k] = NO_CAMPUS;
        }
        if (results != null) {
            for (int i = 0; i < results.length; i++) {
                for (EnumeratedValueInfo enumeratedValueInfo : enumeratedValueInfoList) {
                    if (results[i].equalsIgnoreCase(enumeratedValueInfo.getCode())) {
                        campus[i] = results[i];
                        break;
                    }
                }
            }
        }
        //  Add the individual term items.
        for (SearchRequest request : requests) {
            for (int j = 0; j < campus.length; j++) {
                int count = j + 1;
                String campusKey = "campus" + count;
                request.addParam(campusKey, campus[j]);
            }
        }
    }

    /**
     * @param divisionMap for reference
     * @param query       initial query
     * @param divisions   matches found
     * @return query string, minus matches found
     */
    public String extractDivisions(HashMap<String, String> divisionMap, String query, List<String> divisions) {
        boolean match = true;
        while (match) {
            match = false;
            // Retokenize after each division found is removed
            // Remove extra spaces to normalize input
            query = query.trim().replaceAll("\\s+", " ");
            List<QueryTokenizer.Token> tokens = QueryTokenizer.tokenize(query);
            List<String> list = QueryTokenizer.toStringList(tokens);
            List<String> pairs = TokenPairs.toPairs(list);
            TokenPairs.sortedLongestFirst(pairs);

            Iterator<String> i = pairs.iterator();
            while (match == false && i.hasNext()) {
                String pair = i.next();

                String key = pair.replace(" ", "");
                if (divisionMap.containsKey(key)) {
                    String division = divisionMap.get(key);
                    divisions.add(division);
                    query = query.replace(pair, "");
                    match = true;
                }
            }
        }
        return query;
    }

    public void addDivisionSearches(List<String> divisions, List<String> codes, List<String> levels, List<SearchRequest> requests) {
        for (String division : divisions) {
            boolean needDivisionQuery = true;

            for (String code : codes) {
                needDivisionQuery = false;
                SearchRequest request = new SearchRequest("myplan.lu.search.divisionAndCode");
                request.addParam("division", division);
                request.addParam("code", code);
                requests.add(request);
            }

            for (String level : levels) {
                needDivisionQuery = false;

                // Converts "1XX" to "100"
                level = level.substring(0, 1) + "00";

                SearchRequest request = new SearchRequest("myplan.lu.search.divisionAndLevel");
                request.addParam("division", division);
                request.addParam("level", level);
                requests.add(request);
            }

            if (needDivisionQuery) {
                SearchRequest request = new SearchRequest("myplan.lu.search.division");
                request.addParam("division", division);
                requests.add(request);
            }
        }
    }

    public void addFullTextSearches(String query, List<SearchRequest> requests) {
        List<QueryTokenizer.Token> tokens = QueryTokenizer.tokenize(query);

        for (QueryTokenizer.Token token : tokens) {
            String queryText = null;
            switch (token.rule) {
                case WORD:
                    queryText = token.value;
                    break;
                case QUOTED:
                    queryText = token.value;
                    queryText = queryText.substring(1, queryText.length() - 1);
                    break;
                default:
                    break;
            }
            SearchRequest request = new SearchRequest("myplan.lu.search.fulltext");
            request.addParam("queryText", queryText);
            requests.add(request);
        }
    }

    public List<SearchRequest> queryToRequests(CourseSearchForm form)
            throws Exception {
        String query = form.getSearchQuery().toUpperCase();

        List<String> levels = QueryTokenizer.extractCourseLevels(query);
        for (String level : levels) {
            query = query.replace(level, "");
        }
        List<String> codes = QueryTokenizer.extractCourseCodes(query);
        for (String code : codes) {
            query = query.replace(code, "");
        }

        HashMap<String, String> divisionMap = fetchCourseDivisions();

        ArrayList<String> divisions = new ArrayList<String>();
        query = extractDivisions(divisionMap, query, divisions);


        ArrayList<SearchRequest> requests = new ArrayList<SearchRequest>();

        // Order is important, more exact search results appear at top of list
        addDivisionSearches(divisions, codes, levels, requests);

        addFullTextSearches(query, requests);

        addCampusParams(requests, form);

        return requests;
    }



    //Note: here I am using r1 LuService implementation!!!
    protected LuService getLuService() {
        if (luService == null) {
            luService = (LuService) GlobalResourceLoader.getService(new QName(LuServiceConstants.LU_NAMESPACE, "LuService"));
        }
        return this.luService;
    }

    public void setLuService(LuService luService) {
        this.luService = luService;
    }

    public EnumerationManagementService getEnumService() {
        return enumService;
    }

    public void setEnumService(EnumerationManagementService enumService) {
        this.enumService = enumService;
    }
}
