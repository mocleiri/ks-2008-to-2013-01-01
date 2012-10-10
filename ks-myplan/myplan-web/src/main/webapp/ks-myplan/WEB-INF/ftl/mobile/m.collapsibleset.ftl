<#--

    Copyright 2005-2012 The Kuali Foundation

    Licensed under the Educational Community License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.opensource.org/licenses/ecl2.php

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

-->
<#--
    Mobile Collapsible Accordion Set:

      Used to render the collection line groups in an jQuery mobile collapisble accordion set
 -->

<#macro uif_collapsible_set items manager container>

    <div id="${manager.id}" data-role="collapsible-set" data-inset="false" data-collapsed-icon="arrow-r" data-expanded-icon="arrow-d" data-iconpos="right" data-theme="a">
        <#list manager.stackedGroups as item>
            <div data-role="collapsible">
                <@krad.template component=item/>
            </div>
        </#list>
    </div>

</#macro>