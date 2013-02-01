<#macro myplan_app_header element>
<div id="applicationHeader">
    <div id="applicationHeading">
        <div id="applicationLogo">Kuali Student Academic Planning</div>
        <div id="applicationNavigation">
            <ul>
                <#if RequestParameters.viewId??>
                <#--Plan Page Link-->
                    <#if RequestParameters.viewId=="PlannedCourses-FormView">
                        <li class="home"><a href="plan?methodToCall=start&viewId=PlannedCourses-FormView" class="active">Plan</a>
                        </li>
                    <#else>
                        <li class="home"><a href="plan?methodToCall=start&viewId=PlannedCourses-FormView">Plan</a>
                        </li>
                    </#if>

                <#--Find a Course Page Link-->
                    <#if RequestParameters.viewId=="CourseSearch-FormView">
                        <li><a href="course?methodToCall=start&viewId=CourseSearch-FormView" class="active">Find Courses</a></li>
                    <#else>
                        <li><a href="course?methodToCall=start&viewId=CourseSearch-FormView">Find Courses</a></li>
                    </#if>

                <#--DegreeAudit Page Link-->
                    <#if RequestParameters.viewId=="DegreeAudit-FormView">
                        <li><a href="audit?methodToCall=audit&viewId=DegreeAudit-FormView" class="active">Audit Degree</a></li>
                    <#else>
                        <li><a href="audit?methodToCall=audit&viewId=DegreeAudit-FormView">Audit Degree</a></li>
                    </#if>
                    <#else >
                        <li class="home"><a href="plan?methodToCall=start&viewId=PlannedCourses-FormView&currentPage=planPage">Plan</a></li>
                        <li><a href="course?methodToCall=start&viewId=CourseSearch-FormView&currentPage=coursePage">Find Courses</a></li>
                        <li><a href="audit?methodToCall=audit&viewId=DegreeAudit-FormView&currentPage=auditPage">Audit Degree</a></li>
                </#if>
            </ul>
        </div>
    </div>
</div>
</#macro>