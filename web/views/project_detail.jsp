
@stdvegbankget_jspdeclarations@

<html>
<HEAD>
@defaultHeadToken@
 
<TITLE>View VegBank Data: projects - Detail</TITLE>
<link rel="stylesheet" href="@stylesheet@" type="text/css" />
</HEAD>
<body>  
 @vegbank_header_html_normal@ 
  @possibly_center@  
<h2>View VegBank Projects</h2>
<!--Get standard declaration of rowClass as string: -->
        <% String rowClass = "evenrow"; %>
        <vegbank:get id="project" select="project" beanName="map" pager="true"  xwhereEnable="true"/>
<!--Where statement removed from preceding: -->
<vegbank:pager />
<logic:empty name="project-BEANLIST">
<p>  Sorry, no projects found.</p>
</logic:empty>
<logic:notEmpty name="project-BEANLIST">
<logic:iterate id="onerowofproject" name="project-BEANLIST">
<!-- iterate over all records in set : new table for each -->
<table class="leftrightborders" cellpadding="2">
<tr><th class="major_smaller" colspan="9">Project: <bean:write name="onerowofproject" property="projectname" /></th></tr>
        <%@ include file="autogen/project_detail_data.jsp" %>
        <bean:define id="project_pk" name="onerowofproject" property="project_id" />
<!-- custom bits:
-->


<vegbank:get id="observation" select="observation_count" beanName="map" pager="false" perPage="-1" 
  where="where_project_pk" wparam="project_pk" />

<tr  class='@nextcolorclass@'><td class="datalabel">Count of Observations in this project</td>
<td>
<logic:empty name="observation-BEAN">
-none-
</logic:empty>
<logic:notEmpty name="observation-BEAN">
<bean:write name="observation-BEAN" property="count_observations" />
<logic:notEqual name="observation-BEAN" property="count_observations" value="0">
<a href="@get_link@simple/observation/<bean:write name='project_pk' />?where=where_project_pk">View observations</a>
</logic:notEqual>
</logic:notEmpty>



<!--Insert a nested get statement here:
   example:   

<vegbankget id="related_table" select="related_table" beanName="map" pager="false" perPage="-1" where="where_project_pk" wparam="project_pk" />-->

<TR><TD COLSPAN="2">
<vegbank:get id="projectcontributor" select="projectcontributor" beanName="map" pager="false" where="where_project_pk" wparam="project_pk" perPage="-1" />
<table class="leftrightborders" cellpadding="2" >
<tr><th colspan="2">Project Contributors:</th></tr>
<logic:empty name="projectcontributor-BEANLIST">
<tr><td class="@nextcolorclass@">  Sorry, no projectcontributors found.</td></tr>
</logic:empty>
<logic:notEmpty name="projectcontributor-BEANLIST">

<tr>
<%@ include file="autogen/projectcontributor_summary_head.jsp" %>
</tr>
<logic:iterate id="onerowofprojectcontributor" name="projectcontributor-BEANLIST">
<tr class="@nextcolorclass@">
<%@ include file="autogen/projectcontributor_summary_data.jsp" %>
</tr>
</logic:iterate>

</logic:notEmpty>
</table>
</TD></TR>
</table>
<p>&nbsp;</p>
</logic:iterate>
</logic:notEmpty>
<br />
<vegbank:pager />
</body></html>
          @vegbank_footer_html_tworow@

