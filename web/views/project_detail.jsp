
@stdvegbankget_jspdeclarations@

<html>
<HEAD>
@defaultHeadToken@
 
<TITLE>View VegBank Data: projects - Detail</TITLE>
<link rel="stylesheet" href="@stylesheet@" type="text/css" />
</HEAD>
<body>@vegbank_header_html_normal@  <br />
<h2>View VegBank Projects</h2>
<!--Get standard declaration of rowClass as string: -->
        <% String rowClass = "evenrow"; %>
        <vegbank:get id="project" select="project" beanName="map" pager="true" />
<!--Where statement removed from preceding: -->
<logic:empty name="project-BEANLIST">
<p>  Sorry, no projects found.</p>
</logic:empty>
<logic:notEmpty name="project-BEANLIST">
<logic:iterate id="onerowofproject" name="project-BEANLIST">
<!-- iterate over all records in set : new table for each -->
<table class="leftrightborders" cellpadding="0">
        <%@ include file="autogen/project_detail_data.jsp" %>
        <bean:define id="project_pk" name="onerowofproject" property="project_id" />
<!--Insert a nested get statement here:
   example:   

<vegbank:get id="related_table" select="related_table" beanName="map" pager="false" perPage="-1" where="where_project_pk" wparam="project_pk" />-->
<tr><th colspan="2">projectContributors:</th></tr>
<TR><TD COLSPAN="2">
<vegbank:get id="projectcontributor" select="projectcontributor" beanName="map" pager="false" where="where_project_pk" wparam="project_pk" perPage="-1" />
<logic:empty name="projectcontributor-BEANLIST">
<p class="@nextcolorclass@">  Sorry, no projectcontributors found.</p>
</logic:empty>
<logic:notEmpty name="projectcontributor-BEANLIST">
<table class="leftrightborders" cellpadding="2" >
<tr>
<%@ include file="autogen/projectcontributor_summary_head.jsp" %>
</tr>
<logic:iterate id="onerowofprojectcontributor" name="projectcontributor-BEANLIST">
<tr class="@nextcolorclass@">
<%@ include file="autogen/projectcontributor_summary_data.jsp" %>
</tr>
</logic:iterate>
</table>
</logic:notEmpty>
</TD></TR>
</table>
<p>&nbsp;</p>
</logic:iterate>
</logic:notEmpty>
<br />
<vegbank:pager />
</body></html>
          @vegbank_footer_html_tworow@

