

@stdvegbankget_jspdeclarations@


<HEAD   >
<META http-equiv="Content-Type" content="text/html; charset=UTF-16">

@defaultHeadToken@

<TITLE>View Data in VegBank : project(s) : Detail</TITLE>
<link rel="stylesheet" href="@stylesheet@" type="text/css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</HEAD>
<body   >@vegbank_header_html_normal@  <br><h2>View VegBank Projects</h2><% String rowClass = "evenrow"; %><vegbank:get id="project" select="project" beanName="map" pager="true"/>
<logic:empty name="project-BEANLIST">
<p>  Sorry, no projects found.</p>
</logic:empty>
<logic:notEmpty name="project-BEANLIST">
<logic:iterate id="onerow" name="project-BEANLIST"><!-- iterate over all records in set : new table for each -->
<table class="leftrightborders" cellpadding="0"><!--each field, only write when field HAS contents-->
<logic:notEmpty name="onerow" property="projectname">
<tr class="@nextcolorclass@"><td class="datalabel">Project Name</td>
<td class="sizetiny">
<bean:write name="onerow" property="projectname"/>&nbsp;</td></tr>
</logic:notEmpty>
<logic:notEmpty name="onerow" property="projectdescription">
<tr class="@nextcolorclass@"><td class="datalabel">Project Description</td>
<td class="sizetiny">
<bean:write name="onerow" property="projectdescription"/>&nbsp;</td></tr>
</logic:notEmpty>
<logic:notEmpty name="onerow" property="startdate">
<tr class="@nextcolorclass@"><td class="datalabel">Start Date</td>
<td>
<bean:write name="onerow" property="startdate"/>&nbsp;</td></tr>
</logic:notEmpty>
<logic:notEmpty name="onerow" property="stopdate">
<tr class="@nextcolorclass@"><td class="datalabel">Stop Date</td>
<td>
<bean:write name="onerow" property="stopdate"/>&nbsp;</td></tr>
</logic:notEmpty>
<logic:notEmpty name="onerow" property="accessioncode">
<tr class="@nextcolorclass@"><td class="datalabel">Accession Code</td>
<td class="sizetiny">
<bean:write name="onerow" property="accessioncode"/>&nbsp;</td></tr>
</logic:notEmpty>
</table>
<p>&nbsp;</p>
</logic:iterate>
</logic:notEmpty><br><vegbank:pager/>
          @vegbank_footer_html_tworow@
</body>
