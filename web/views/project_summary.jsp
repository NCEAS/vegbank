
@stdvegbankget_jspdeclarations@

<html>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8" />
@defaultHeadToken@
 
<TITLE>View VegBank Data: projects - Summary</TITLE>
<link rel="stylesheet" href="@stylesheet@" type="text/css" />
</HEAD>
<body>  
 @vegbank_header_html_normal@ 
  @possibly_center@  
<h2>View VegBank Projects</h2>
<!--Get standard declaration of rowClass as string: -->
        <% String rowClass = "evenrow"; %>
        <vegbank:get id="project" select="project" beanName="map" pager="true" />
<!--Where statement removed from preceding: -->
<logic:empty name="project-BEANLIST">
<p>  Sorry, no projects found.</p>
</logic:empty>
<logic:notEmpty name="project-BEANLIST">
<table class="leftrightborders" cellpadding="2">
<tr>
<th>More</th>
                  <%@ include file="autogen/project_summary_head.jsp" %></tr>
<logic:iterate id="onerowofproject" name="project-BEANLIST">
<tr class="@nextcolorclass@">
<td class="sizetiny">
<a href="@get_link@detail/project/@subst_lt@bean:write name='onerowofproject' property='project_id' /@subst_gt@">
                            Details
                            </a>
</td>
                       <%@ include file="autogen/project_summary_data.jsp" %>
                       </tr>
<bean:define id="project_pk" name="onerowofproject" property="project_id" />
<!--Insert a nested get statement here:
   example:   

<vegbank:get id="related_table" select="related_table" beanName="map" pager="false" perPage="-1" where="where_project_pk" wparam="project_pk" />-->
</logic:iterate>
</table>
</logic:notEmpty>
<br />
<vegbank:pager />
</body></html>
          @vegbank_footer_html_tworow@
