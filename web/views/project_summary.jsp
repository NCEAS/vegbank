@stdvegbankget_jspdeclarations@


<HEAD   >
<META http-equiv="Content-Type" content="text/html; charset=UTF-16">

@defaultHeadToken@
 
<TITLE>View Data in VegBank : project(s) : Summary</TITLE>
<link rel="stylesheet" href="@stylesheet@" type="text/css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</HEAD>
<body   >@vegbank_header_html_normal@  <br><h2>View VegBank Projects</h2><% String rowClass = "evenrow"; %><vegbank:get id="project" select="project" beanName="map" where="where_project_pk" pager="true"/>
<logic:empty name="project-BEANLIST">
<p>  Sorry, no projects found.</p>
</logic:empty>
<logic:notEmpty name="project-BEANLIST">
<table class="leftrightborders" cellpadding="2">
<tr>
<th>More</th>
<th>Project Name</th>
<th>Project Description</th>
<th>Start Date</th>
<th>Stop Date</th>
</tr>
<logic:iterate id="onerow" name="project-BEANLIST">
<tr class="@nextcolorclass@">
<td class="sizetiny"><a href="@get_link@detail/project/<bean:write name='onerow' property='project_id' />">
                            Details
                            </a></td>
<td class="sizetiny">
<bean:write name="onerow" property="projectname"/>&nbsp;</td><td class="sizetiny">
<bean:write name="onerow" property="projectdescription"/>&nbsp;</td><td>
<bean:write name="onerow" property="startdate"/>&nbsp;</td><td>
<bean:write name="onerow" property="stopdate"/>&nbsp;</td></tr>
</logic:iterate>
</table>
</logic:notEmpty><br><vegbank:pager/>
          @vegbank_footer_html_tworow@
</body>
