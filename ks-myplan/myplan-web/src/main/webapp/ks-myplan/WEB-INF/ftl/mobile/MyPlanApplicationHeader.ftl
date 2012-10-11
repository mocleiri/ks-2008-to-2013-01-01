<#macro myplan_app_header element>
<div id="mobileHeader" data-role="header">
    <h1 style="text-align:left; margin:.6em 30% .8em .6em">MyPlan</h1>
    <a href="#globalNav" data-rel="popup" data-icon="menu" data-iconpos="notext" data-theme="d" data-shadow="false" data-iconshadow="false" data-position-to="origin" class="myplan-mobile-menu ui-btn-right">Menu</a>
    <div data-role="popup" id="globalNav" data-theme="a" data-disabled="false" data-corners="true" data-transition="slidedown">
        <ul data-role="listview" data-inset="true" style="min-width:210px;" data-theme="b">
            <li data-role="divider" data-theme="d">Navigation</li>
            <li data-corners="false" data-shadow="false" data-icon="false" data-theme="a"><a href="#">Course Search</a></li>
            <li data-corners="false" data-shadow="false" data-icon="false" data-theme="a"><a href="/student/myplan/lookup?methodToCall=search&viewId=M.SavedCoursesSummary-LookupView">Bookmarks</a></li>
        </ul>
    </div>
</div>
</#macro>