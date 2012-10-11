<#macro myplan_app_header element>
<div id="mobileHeader" data-role="header">
    <h1 style="text-align:left; margin:.6em 30% .8em .6em">MyPlan</h1>
    <a href="#globalNav" data-rel="popup" data-icon="menu" data-iconpos="notext" data-theme="d" data-shadow="false" data-iconshadow="false">Menu</a>
    <div data-role="popup" id="globalNav" data-theme="a" data-disabled="false" data-corners="true" data-transition="slidedown" data-position-to="origin">
                <ul data-role="listview" data-inset="true" style="min-width:210px;" data-theme="b">
                    <li data-corners="false" data-shadow="false" data-iconshadow="true" data-wrapperels="div" data-iconpos="right" data-theme="b"><a href="#" class="ui-link-inherit">Course Search</a></li>
                    <li data-corners="false" data-shadow="false" data-iconshadow="true" data-wrapperels="div" data-iconpos="right" data-theme="b"><a href="methods.html" class="ui-link-inherit">Bookmarks</a></li>
                </ul>
        </div>
</div>
</#macro>