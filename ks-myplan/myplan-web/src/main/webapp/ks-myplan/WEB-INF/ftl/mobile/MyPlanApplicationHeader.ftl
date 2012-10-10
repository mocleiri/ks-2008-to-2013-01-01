<#macro myplan_app_header element>
<div id="mobileHeader" data-role="header">
    <h1 style="text-align:left; margin:.6em 30% .8em .6em">MyPlan</h1>
    <a href="#globalNav" data-rel="popup" aria-haspopup="true" aria-owns="#globalNav" data-icon="menu" data-iconpos="notext" data-theme="d" data-shadow="false" data-iconshadow="false" class="ui-btn-right myplan-mobile-menu">Menu</a>
    <div data-role="popup" id="popupMenu" data-theme="a" class="ui-popup ui-body-a ui-overlay-shadow ui-corner-all" aria-disabled="false" data-disabled="false" data-shadow="true" data-corners="true" data-transition="none" data-position-to="origin">
                <ul data-role="listview" data-inset="true" style="min-width:210px;" data-theme="b" class="ui-listview ui-listview-inset ui-corner-all ui-shadow">
                    <li data-corners="false" data-shadow="false" data-iconshadow="true" data-wrapperels="div" data-icon="arrow-r" data-iconpos="right" data-theme="b" class="ui-btn ui-btn-icon-right ui-li-has-arrow ui-li ui-btn-up-b"><div class="ui-btn-inner ui-li"><div class="ui-btn-text"><a href="options.html" class="ui-link-inherit">Course Search</a></div><span class="ui-icon ui-icon-arrow-r ui-icon-shadow">&nbsp;</span></div></li>
                    <li data-corners="false" data-shadow="false" data-iconshadow="true" data-wrapperels="div" data-icon="arrow-r" data-iconpos="right" data-theme="b" class="ui-btn ui-btn-icon-right ui-li-has-arrow ui-li ui-btn-up-b"><div class="ui-btn-inner ui-li"><div class="ui-btn-text"><a href="methods.html" class="ui-link-inherit">Bookmarks</a></div><span class="ui-icon ui-icon-arrow-r ui-icon-shadow">&nbsp;</span></div></li>
                </ul>
        </div>

        <a href="#popupMenu" data-rel="popup" data-role="button" data-inline="true" data-corners="true" data-shadow="true" data-iconshadow="true" data-wrapperels="span" data-theme="c" aria-haspopup="true" aria-owns="#popupMenu" class="ui-btn ui-shadow ui-btn-corner-all ui-btn-inline ui-btn-up-c"><span class="ui-btn-inner ui-btn-corner-all"><span class="ui-btn-text">Menu</span></span></a>

</div>
</#macro>