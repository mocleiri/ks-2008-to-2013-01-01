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
    Mobile Grid

 -->

<#macro m_uif_grid group>

    <#if group.styleClassesAsString?has_content>
        <#local styleClass="class=\"${group.styleClassesAsString}\""/>
    </#if>

    <#if group.style?has_content>
        <#local style="style=\"${group.style}\""/>
    </#if>

    <div id="${group.id}" ${style!} ${styleClass!}>
        <#list group.items as item>
           <@krad.template component=item/>
        </#list>
    </div>

</#macro>