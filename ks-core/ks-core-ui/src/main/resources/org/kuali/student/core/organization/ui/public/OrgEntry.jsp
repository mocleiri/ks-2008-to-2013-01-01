<%--

    Copyright 2010 The Kuali Foundation Licensed under the
    Educational Community License, Version 2.0 (the "License"); you may
    not use this file except in compliance with the License. You may
    obtain a copy of the License at

    http://www.osedu.org/licenses/ECL-2.0

    Unless required by applicable law or agreed to in writing,
    software distributed under the License is distributed on an "AS IS"
    BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
    or implied. See the License for the specific language governing
    permissions and limitations under the License.

--%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page import="org.kuali.student.common.ui.server.messages.MessageRPCPreloader"%>

<html>
	<head>
	
		<!--                                           -->
		<!-- Any title is fine                         -->
		<!--                                           -->
		<title>Kuali Student: Organization</title>
 				 
		<!--                                           -->
		<!-- Use normal html, such as style            -->
		<!--                                           -->
		<style>
		</style>


    	<!--                                           -->
    	<!-- This script loads the Visualization API.  -->
	    <!--                                           -->
		<script type="text/javascript" src="http://www.google.com/jsapi"></script>


	
		<!--                                           -->
		<!-- This script loads your compiled module.   -->
		<!-- If you add any GWT meta tags, they must   -->
		<!-- be added before this line.                -->
		<!--                                           -->
<%
 MessageRPCPreloader messageRPCPreloader = new MessageRPCPreloader();
 String messageData = messageRPCPreloader.getMessagesByGroupsEncodingString("en",new String[]{"common","org","validation"});
%>

<script type="text/javascript">
 var i18nMessages = '<%= messageData %>';
</script>	
		

	</head>

	<!--                                           -->
	<!-- The body can have arbitrary html, or      -->
	<!-- you can leave the body empty if you want  -->
	<!-- to create a completely dynamic ui         -->
	<!--                                           -->
	<body>

		<!-- OPTIONAL: include this if you want history support -->
		<iframe src="javascript:''" id="__gwt_historyFrame" style="position:absolute;width:0;height:0;border:0"></iframe>

		<div id="applicationPanel" style="height: 100%; width: 100%; overflow: auto">
			<div id="loadingSpinner">
				<img src="images/loading.gif" />
				<b>Loading...</b>
			</div>
			<script language='javascript' src='org.kuali.student.core.organization.ui.OrgEntry.nocache.js'></script>
			<div id="orgPositionsTable"></div>
			<div id="orgTreeMemberTable"></div>
		</div>
	</body>
</html>
