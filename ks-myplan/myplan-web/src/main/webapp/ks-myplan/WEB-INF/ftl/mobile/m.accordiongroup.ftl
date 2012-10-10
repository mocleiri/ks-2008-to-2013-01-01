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
    Mobile Accordion Group.

    The header is rendered outside the div so that an accordion like layout can be generated

 -->

<#macro uif_accordion_group group>

    <#if group.header.headerLevel?has_content>
        <#local headerOpenTag="<${group.header.headerLevel} ${style!} ${styleClass!}>"/>
        <#local headerCloseTag="</${group.header.headerLevel}>"/>
    </#if>

    <#if group.header.headerLevel?has_content && group.header.headerText?has_content && group.header.headerText != '&nbsp;'>
            ${headerOpenTag}<span class="uif-headerText-span">${group.header.headerText}</span>${headerCloseTag}
    </#if>

    <@div component=group>

        <#if group.disclosure.render>
            <div id="${group.id}_disclosureContent" class="uif-disclosureContent">
        </#if>

        <#-- invoke layout manager -->
        <#include "${group.layoutManager.template}" parse=true/>

        <#local macroInvokeSrc="<" + "@${group.layoutManager.templateName} items=group.items"/>
        <#local macroInvokeSrc="${macroInvokeSrc} manager=group.layoutManager container=group/>"/>
        <#local macroInvoke = macroInvokeSrc?interpret>

        <@macroInvoke />



        <@template component=group.footer/>

        <#if group.disclosure.render>
            <@template component=group.disclosure parent=group/>
            </div>
        </#if>

    </@div>

</#macro>