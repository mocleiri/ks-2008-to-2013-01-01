/**
 * Copyright 2010 The Kuali Foundation Licensed under the
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

package org.kuali.student.common.ui.client.widgets.suggestbox;

import java.util.ArrayList;
import java.util.List;

import org.kuali.student.common.ui.client.mvc.Callback;
import org.kuali.student.common.ui.client.service.SearchRpcService;
import org.kuali.student.common.ui.client.service.SearchRpcServiceAsync;
import org.kuali.student.core.assembly.data.LookupMetadata;
import org.kuali.student.core.assembly.data.LookupParamMetadata;
import org.kuali.student.core.search.dto.SearchParam;
import org.kuali.student.core.search.dto.SearchRequest;
import org.kuali.student.core.search.dto.SearchResult;
import org.kuali.student.core.search.dto.SearchResultCell;
import org.kuali.student.core.search.dto.SearchResultRow;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasText;

public class SearchSuggestOracle extends IdableSuggestOracle{
    
    private String searchTypeKey;
    private String searchIdKey;
    private String searchTextKey;
    private String resultIdKey;
    private Callback currentCallback;
    private Request pendingRequest;
    private Callback pendingCallback;
    private HasText textWidget;
    private String resultDisplayKey;
    private List<SearchParam> additionalParams = new ArrayList<SearchParam>();
    private List<IdableSuggestion> lastSuggestions = new ArrayList<IdableSuggestion>();
    
    private LookupMetadata lookupMetaData;
    private SearchRpcServiceAsync searchRpcServiceAsync = GWT.create(SearchRpcService.class);
    
    private List<org.kuali.student.common.ui.client.mvc.Callback<IdableSuggestion>> searchCompletedCallbacks = new ArrayList<org.kuali.student.common.ui.client.mvc.Callback<IdableSuggestion>>();
    
    /**
     * @deprecated
     * @param searchTypeKey the type to be search on
     * @param searchTextKey the column/key that to search on
     * @param idKey the column/key that is the primary key for this type
     */
    public SearchSuggestOracle(String searchTypeKey, String searchTextKey, String searchIdKey, String resultIdKey, String resultDisplayKey){
        this.searchTypeKey = searchTypeKey;
        this.searchTextKey = searchTextKey;
        this.searchIdKey = searchIdKey;
        this.resultIdKey = resultIdKey;
        this.resultDisplayKey = resultDisplayKey;
    }
    
    /*
     * 
     */
    public SearchSuggestOracle(LookupMetadata lookupMetadata) {
    	this.lookupMetaData = lookupMetadata;
        this.searchTypeKey = lookupMetaData.getSearchTypeId();
        
        for (LookupParamMetadata param : lookupMetadata.getParams()) {
        	if ((param.getUsage() != null) && param.getUsage().name().equals("DEFAULT")) {
        		this.searchTextKey = param.getKey();
        	}
        	//Add in any writeaccess never default values to the additional params
        	if(param.getWriteAccess().equals("NEVER")||param.getDefaultValueString()!=null||param.getDefaultValueList()!=null){
        		SearchParam searchParam = new SearchParam();
        		searchParam.setKey(param.getKey());
				if(param.getDefaultValueList()==null){
					searchParam.setValue(param.getDefaultValueString());
				}else{
					searchParam.setValue(param.getDefaultValueList());
				}
        		additionalParams.add(searchParam);
        	}
        }
        if (this.searchTextKey == null) {
        	//FIXME deal with missing default key
        	GWT.log("Cannot find searchTextKey for " + searchTypeKey, null);
        }
        
        this.searchIdKey = lookupMetadata.getSearchParamIdKey();
        this.resultIdKey = lookupMetadata.getResultReturnKey();
        this.resultDisplayKey = lookupMetadata.getResultDisplayKey();
    }

    public void setAdditionalSearchParams(List<SearchParam> params){
        additionalParams = params;
    }
    
    private Callback wrappedCallback = new Callback() {

        public void onSuggestionsReady(Request request, Response response) {
          if (textWidget.getText().equals(request.getQuery())) {
            currentCallback.onSuggestionsReady(request, response);
            pendingRequest = null;
            pendingCallback = null;
          }
          currentCallback = null;
          if (pendingCallback != null) {
            requestSuggestions(pendingRequest, pendingCallback);
            pendingRequest = null;
            pendingCallback = null;
          }
        }

    };
    
    @Override
    public void requestSuggestions(Request request, Callback callback) {
        if (currentCallback == null) {
          currentCallback = callback;
          sendRequest(request, wrappedCallback);
        } else {
          pendingRequest = request;
          pendingCallback = callback;
        }        
    }
    
    private SearchRequest buildSearchRequest(String query, String searchId) {
    	SearchRequest sr = new SearchRequest();
    	sr.setNeededTotalResults(false);
    	sr.setSearchKey(this.searchTypeKey);

		List<SearchParam> searchParams = new ArrayList<SearchParam>();
		SearchParam param1 = createParam(this.searchTextKey, query);
		searchParams.add(param1);
		
    	sr.setParams(searchParams);
    	
    	sr.getParams().addAll(additionalParams);

        return sr;
    }
    
    private SearchRequest buildSearchRequestById(String query, String searchId) {
    	SearchRequest sr = new SearchRequest();
    	sr.setNeededTotalResults(false);
    	sr.setSearchKey(this.searchTypeKey);

		List<SearchParam> searchParams = new ArrayList<SearchParam>();
		SearchParam param2 = createParam(this.searchIdKey, searchId);
		searchParams.add(param2);
		
    	sr.setParams(searchParams);
    	
    	sr.getParams().addAll(additionalParams);

        return sr;
    }
    
    private SearchParam createParam(String key, String value) {
    	SearchParam param = new SearchParam();
    	
    	if(key == null) {
			param.setKey("");
		} else {
			param.setKey(key);
		}

    	if(value == null) {
			param.setValue("");
		} else {
			param.setValue(value);
		}
    	
    	return param;
    }

    public void sendRequest(final Request request, final Callback callback){
        String query = request.getQuery().trim();
        SearchRequest searchRequest = buildSearchRequest(query, null);
        
        //case-sensitive?
        if(query.length() > 0){
        	searchRpcServiceAsync.search(searchRequest, new AsyncCallback<SearchResult>(){

            	@Override
                public void onFailure(Throwable caught) {
                    // TODO Auto-generated method stub
                }
    
                @Override
                public void onSuccess(SearchResult results) {
                    lastSuggestions = createSuggestions(results, request.getLimit());
                    Response response = new Response(lastSuggestions);
                    callback.onSuggestionsReady(request, response);
                    if (searchCompletedCallbacks != null &&
                            lastSuggestions != null && lastSuggestions.size() == 1) {
                        for (org.kuali.student.common.ui.client.mvc.Callback<IdableSuggestion> callback : searchCompletedCallbacks) {
                            callback.exec(lastSuggestions.get(0));
                        }
                    }
                }
                
                private List<IdableSuggestion> createSuggestions(SearchResult results, int limit){
                    List<IdableSuggestion> suggestionsList = new ArrayList<IdableSuggestion>();
                    String query = request.getQuery();
                    query = query.trim();
                    int count = 0;
                    if(results != null){
                        for (SearchResultRow r: results.getRows()){
                            if(count == limit){
                                break;
                            }

                            IdableSuggestion theSuggestion = new IdableSuggestion();
                            for(SearchResultCell c: r.getCells()){
                                if(c.getKey().equals(resultDisplayKey)){
                                    String itemText = c.getValue();
                                    theSuggestion.addAttr(c.getKey(), c.getValue());
                                    int index = (" " + itemText).toLowerCase().indexOf(" " + query.toLowerCase().trim());
                                    
                                    if (index < 0) {
                                        //temporary fix to stop index out of bound exception in hosted mode
//                                        continue;
                                        //Including fuzzy search results in the display list.
                                        theSuggestion.setDisplayString(itemText);
                                        theSuggestion.setReplacementString(itemText);
                                    	//FIXME handle case when search for text is not appearing within search result - should not happen (misconfiguration)
                                        continue;
                                    }
                                    
                                    String htmlString = itemText.substring(0,index) + "<b>" + itemText.substring(index, index + query.length()) + "</b>" + itemText.substring(index + query.length(), itemText.length());
                                    theSuggestion.setDisplayString(htmlString);
                                    theSuggestion.setReplacementString(itemText);

                                } else if(c.getKey().equals(resultIdKey)){
                                     theSuggestion.setId(c.getValue());
                                     theSuggestion.addAttr(c.getKey(), c.getValue());
                                } else{
                                    theSuggestion.addAttr(c.getKey(), c.getValue());
                                }
                            }
                            suggestionsList.add(theSuggestion);
                            count++;
                        }
                    }
                    return suggestionsList;
                }
            });
        }
    }

    @Override
    public boolean isDisplayStringHTML() {
        return true;
    }

/*    
    public IdableSuggestion getSuggestionById(String id) {
        IdableSuggestion suggestion = null;
        if(!(lastSuggestions.isEmpty())){
            for(IdableSuggestion is: lastSuggestions){
                if(is.getId().equals(id)){
                    suggestion = is;
                    break;
                }
            }
        }
        if(suggestion == null){
            searchOnId(id);
        }
        return suggestion;
    }*/

    @Override
    public IdableSuggestion getSuggestionByText(String text){
        IdableSuggestion suggestion = null;
        for(IdableSuggestion is: lastSuggestions){
            if(is.getReplacementString().trim().equalsIgnoreCase(text.trim())){
                suggestion = is;
                break;
            }
        }
        return suggestion;
    }
    
    public void setTextWidget(HasText widget){
        textWidget = widget;
    }

    @Override
    public void getSuggestionByIdSearch(String id, final org.kuali.student.common.ui.client.mvc.Callback<IdableSuggestion> callback) {
        SearchRequest searchRequest = buildSearchRequestById(null, id);
        searchRpcServiceAsync.search(searchRequest, new AsyncCallback<SearchResult>(){
            
            @Override
            public void onFailure(Throwable caught) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onSuccess(SearchResult results) {
                IdableSuggestion theSuggestion = null;
                if(results != null && !results.getRows().isEmpty()){
                	SearchResultRow r = results.getRows().get(0);
                    theSuggestion = new IdableSuggestion();
                    for(SearchResultCell c: r.getCells()){
                        if(c.getKey().equals(resultDisplayKey)){
                            String itemText = c.getValue();
                            theSuggestion.addAttr(c.getKey(), c.getValue());
                            theSuggestion.setDisplayString(itemText);
                            theSuggestion.setReplacementString(itemText);
                        } else if(c.getKey().equals(resultIdKey)){
                             theSuggestion.setId(c.getValue());
                             theSuggestion.addAttr(c.getKey(), c.getValue());
                        } else {
                            theSuggestion.addAttr(c.getKey(), c.getValue());
                        }
                    }
                }
                callback.exec(theSuggestion);
            }
        });
    }

    @Override
    public void addSearchCompletedCallback(org.kuali.student.common.ui.client.mvc.Callback<IdableSuggestion> callback) {
        searchCompletedCallbacks.add(callback);
    }
    
}
