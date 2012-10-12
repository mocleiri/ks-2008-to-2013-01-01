jQuery(document).ready(function () {
    jQuery("head").append('<meta name="viewport" content="width=device-width, initial-scale=1">');
    //jQuery("#Uif-Application").attr("data-role","page");

    if(jQuery('#searchForCourses').length > 0) {
        jQuery('#searchForCourses').click(function(e) {
            e.preventDefault(); searchForCourses('mobile_course_search_results'); ;
        });
    }
});


function searchForCourses(id) {
    var sQuery = jQuery("input[name='searchQuery']").val();
    var sTerm = 'any';
    var aCampus = ['306','310','323'];
    var url = '/student/myplan/course/search?queryText=' + escape(sQuery) + '&termParam=' + sTerm + '&campusParam=' + aCampus + '&time=' + new Date().getTime();
    jQuery.getJSON(url, function(data) {
        var items = [];
        jQuery("#" + id + " .uif-boxLayout").html('');
        jQuery.each(data.aaData, function(index, value) {
            var link = jQuery(value[1]).wrapInner('<p class="ui-li-desc" />').prepend('<h3 class="ui-li-heading">'+value[0] + '</h3>').attr({
                "class":"ui-link-inherit",
                "style":"padding-top:5px; padding-bottom:5px;"
            });
            var li = jQuery('<li />').attr({
                "data-corners": "false",
                "data-shadow": "false",
                "data-iconshadow": "true",
                "data-wrapperels":"div",
                "data-icon":"arrow-r",
                "data-iconpos":"right",
                "data-theme":"a",
                "class":"ui-btn ui-btn-icon-right ui-li-has-arrow ui-li ui-btn-up-a"
            }).html('<div class="ui-btn-inner ui-li"><div class="ui-btn-text">'+link.wrap("<div>").parent().clone().html().replace("CourseDetails-InquiryView","M.CourseDetails-InquiryView")+'</div><span class="ui-icon ui-icon-arrow-r ui-icon-shadow">&nbsp;</span></div>');
            items.push(li.wrap("<div>").parent().clone().html());
        });

        jQuery('<ul />').attr({
            "data-role":"listview",
            "data-theme":"a",
            "class":"listview",
            "data-inset":"false"
        }).html(items.join('')).prepend('<li data-role="list-divider" role="heading" class="ui-li ui-li-divider ui-bar-d ui-li-has-count">Showing Results for \''+sQuery+'\'<span class="ui-li-count ui-btn-up-a ui-btn-corner-all">'+data.aaData.length+'</span></li>').appendTo('#' + id + ' .uif-boxLayout');
        //jQuery('#' + id + ' ul').listview('refresh');
    });
}