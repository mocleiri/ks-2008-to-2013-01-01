var oTable;
var oFacets = new Object();

// Override to redefine course search table width
function ksapCourseSearchTableWidth() {
	return 548;
}

// Override to redefine course search columns
function ksapCourseSearchColumns() {
	return [ {
		'bSortable' : true,
		'bSearchable' : true,
		'sTitle' : 'Code',
		'sClass' : 'myplan-text-nowrap sortable',
		'sWidth' : '73px',
		'sType' : 'string'
	}, {
		'bSortable' : true,
		'bSearchable' : true,
		'sTitle' : 'Course Name',
		'sClass' : 'sortable',
		'sWidth' : '170px'
	}, {
		'bSortable' : false,
		'bSearchable' : true,
		'sTitle' : 'Credits',
		'sWidth' : '34px'
	}, {
		'bSortable' : false,
		'bSearchable' : true,
		'sTitle' : 'Terms Offered',
		'sClass' : 'myplan-data-list',
		'sWidth' : '76px'
	}, {
		'bSortable' : false,
		'bSearchable' : true,
		'sTitle' : 'Gen Edu Req',
		'sWidth' : '66px'
	}, {
		'bSortable' : false,
		'bSearchable' : false,
		'sTitle' : '',
		'sClass' : 'myplan-status-column',
		'sWidth' : '69px'
	} ];
}

/**
 * Perform a course search.
 * 
 * @param id
 *            The ID of the course search DataTables table element.
 * @param parentId
 *            The ID of the course search panel div element. This element will
 *            be hidden while the initial search is taking place.
 */
function searchForCourses(id, parentId) {
	var results = jQuery("#" + parentId); // course_search_results_panel
	results.fadeOut("fast");
	showLoading("Searching. Please wait...");
	var sQuery = jQuery("input[name='searchQuery']").val();
	var sTerm = jQuery("select[name='searchTerm'] option:selected").val();
	var aCampus = new Array();
	jQuery.each(jQuery("input[name='campusSelect']:checked"), function() {
		aCampus.push(jQuery(this).val());
	});
	fnLoadFacets(sQuery, sTerm, aCampus);
	oTable = jQuery("#" + id)
			.dataTable(
					{
						aLengthMenu : [ 20, 50, 100 ],
						aaSorting : [],
						aoColumns : ksapCourseSearchColumns(),
						bAutoWidth : false,
						bDeferRender : true,
						bDestroy : true,
						bJQueryUI : true,
						bProcessing : false,
						bScrollCollapse : true,
						bServerSide : true,
						bSortClasses : false,
						bStateSave : false,
						iCookieDuration : 600,
						iDisplayLength : 20,
						fnDrawCallback : function() {
							if (Math
									.ceil((this.fnSettings().fnRecordsDisplay())
											/ this.fnSettings()._iDisplayLength) > 1) {
								jQuery(".dataTables_paginate .ui-button").not(
										".first, .last").show();
							} else {
								jQuery(".dataTables_paginate .ui-button")
										.hide();
							}
							if (this.fnSettings()._iDisplayStart != 0
									&& jQuery("#" + parentId).height() > jQuery(
											window).height()) {
								var targetOffset = jQuery("#" + parentId)
										.offset().top;
								jQuery('html,body').animate({
									scrollTop : targetOffset
								}, 250);
							}
						},
						fnInitComplete : function(oSettings, json) {
							oTable.fnDraw();
							hideLoading();
							results.fadeIn("fast");
							results.find("table#" + id).width(
									ksapCourseSearchTableWidth());
						},
						fnServerData : function(sSource, aoData, fnCallback) {
							jQuery
									.ajax({
										dataType : 'json',
										type : "GET",
										url : sSource,
										data : aoData,
										success : fnCallback,
										error : function(jqXHR, textStatus,
												errorThrown) {
											hideLoading();
											showGrowl(textStatus + " "
													+ errorThrown,
													"Search Error");
										},
										statusCode : {
											500 : function() {
												showGrowl(
														"500 Internal Server Error",
														"Fatal Error");
											}
										}
									});
						},
						oLanguage : {
							"sEmptyTable" : '<div class="myplan-course-search-empty"><p class="fl-font-size-130">We couldn&#39;t find anything matching your search.</p><p>A few suggestions:</p><ul><li>Check your spelling</li><li>Try a more general search (Any quarter, ENGL 1xx)</li><li>Use at least 3 characters</li></ul></div>',
							"sInfo" : "Showing _START_-_END_ of _TOTAL_ results",
							"sInfoEmpty" : "0 results found",
							"sInfoFiltered" : "",
							"sLengthMenu" : "Show _MENU_",
							"sZeroRecords" : "0 results found"
						},
						sAjaxSource : 'course/search?queryText='
								+ escape(sQuery) + '&termParam=' + sTerm
								+ '&campusParam=' + aCampus + '&time='
								+ new Date().getTime(),
						sCookiePrefix : "myplan_",
						sDom : "ilrtSp",
						sPaginationType : "full_numbers"
					});
}

/**
 * Load initial facet state from the server based on new search criteria.
 * 
 * @param sQuery
 *            Search criteria.
 * @param sTerm
 *            Term selection.
 * @param aCampus
 *            Campus selections.
 */
function fnLoadFacets(sQuery, sTerm, aCampus) {
	showLoading("Loading", jQuery("#course_search_results_facets"));
	jQuery
			.ajax({
				dataType : 'json',
				type : "GET",
				url : 'course/facetValues?queryText=' + escape(sQuery)
						+ '&termParam=' + sTerm + '&campusParam=' + aCampus,
				success : function(data, textStatus, jqXHR) {
					oFacets = data;
					jQuery(
							".myplan-facets-group .uif-disclosureContent .uif-boxLayout")
							.each(function() {
								jQuery(this).empty();
							});
					if (oFacets.aFacetState.length > 0)
						jQuery.publish("GENERATE_FACETS");
					hideLoading(jQuery("#course_search_results_facets"));
				},
				error : function(jqXHR, textStatus, errorThrown) {
					hideLoading(jQuery("#course_search_results_facets"));
					showGrowl(textStatus + " " + errorThrown, "Search Error");
				},
				statusCode : {
					500 : function() {
						showGrowl("500 Internal Server Error", "Fatal Error");
					}
				}
			});
}

/**
 * Send a facet click event to the server, and receive an updated facet state in
 * oFacets.
 * 
 * @param sFilter
 *            The facet key that was clicked.
 * @param i
 *            The facet column index.
 * @param e
 *            The JS click event.
 */
function fnClickFacet(sFilter, i, e) {
	stopEvent(e);
	jQuery.ajax({
		dataType : 'json',
		type : "GET",
		url : 'course/facetValues?queryText=' + escape(oFacets.sQuery)
				+ '&termParam=' + oFacets.sTerm + '&campusParam='
				+ oFacets.aCampus + '&fclick=' + sFilter + '&fcol=' + i,
		success : function(data, textStatus, jqXHR) {
			oFacets = data;
			if (oFacets.aFacetState.length > 0)
				jQuery.publish("UPDATE_FACETS");
			if (sFilter === 'All')
				oTable.fnFilter('', i, true, false);
			else {
				// Build filter regex query
				var oData = oFacets.aFacetState[i];
				var aSelections = [];
				for ( var key in oData)
					if (oData.hasOwnProperty(key)
							&& oData[key].checked === true)
						aSelections.push(";" + key + ";");
				oTable.fnFilter(aSelections.join('|'), i, true, false);
			}
		},
		error : function(jqXHR, textStatus, errorThrown) {
			showGrowl(textStatus + " " + errorThrown, "Search Error");
		},
		statusCode : {
			500 : function() {
				showGrowl("500 Internal Server Error", "Fatal Error");
			}
		}
	});
}

/**
 * GENERATE_FACETS event handler.
 * 
 * <p>
 * This event is sent to all facet groups after receiving initial facet state
 * from the server in conjunction with a search.
 * </p>
 * 
 * @param i
 *            The facet columns index.
 * @param obj
 *            A handle to the facet group div element.
 * @param sorter
 *            Function to use for sorting facet keys.
 */
function fnGenerateFacetGroup(i, obj) {
	var oData = oFacets.aFacetState[i];
	var jFacets = obj.find(".uif-disclosureContent .uif-boxLayout");
	var bOne = false; // exactly one facet value
	var bMore = false; // more than one facet value
	for (key in oData)
		if (bMore)
			continue;
		else if (oData.hasOwnProperty(key))
			bMore = !(bOne = !bOne);
	if (bMore) {
		jFacets.append(jQuery('<div class="all"><ul /></div>'));
		var jAll = jQuery('<li />').attr("title", "All")
				.addClass("all checked").html('<a href="#">All</a>').click(
						function(e) {
							fnClickFacet('All', i, e);
						});
		jFacets.find(".all ul").append(jAll);
	}
	jFacets.append(jQuery('<div class="facets"><ul /></div>'));
	var ful = jFacets.find(".facets ul");
	for (key in oData) {
		var jItem = jQuery('<li />').data("facetkey", key).html(
				'<a href="#">' + key + '</a><span>(' + oData[key].count
						+ ')</span>').click(function(e) {
			fnClickFacet(jQuery(this).data("facetkey"), i, e);
		});
		if (bOne)
			jItem.addClass("static");
		ful.append(jItem);
	}
}

/**
 * UPDATE_FACETS event handler.
 * 
 * <p>
 * This event is sent to all facet groups after receiving updated facet state
 * from the server.
 * </p>
 * 
 * @param i
 *            The facet column index.
 * @param obj
 *            A handle to the facet group div element.
 */
function fnUpdateFacetList(i, obj) {
	// Update the style on the 'All' facet option (checked if none in the group
	// are selected, not checked if any are selected)
	var bAll = true;
	var oData = oFacets.aFacetState[i];
	for ( var key in oData)
		if (!bAll)
			continue;
		else if (oData.hasOwnProperty(key) && !oData[key].checked)
			bAll = false;
	if (bAll)
		obj.find("ul li.all").addClass("checked");
	else
		obj.find("ul li.all").removeClass("checked");
	// Update the style (checked/not checked) on facet links and the count view
	obj.find("li").not(".all").each(function() {
		var key = jQuery(this).data("facetkey");
		if (oData.hasOwnProperty(key)) {
			var oFcb = oData[key];
			if (!bAll && oFcb.checked)
				jQuery(this).addClass("checked");
			else
				jQuery(this).removeClass("checked");
			jQuery(this).find("span").text("(" + oFcb.count + ")");
		} else
			jQuery(this).find("span").text("(0)");
	});
}
