@stdvegbankget_jspdeclarations@


<HEAD   >
@defaultHeadToken@
 
<TITLE>View Data in VegBank : Dataset(s)</TITLE>
<link rel="stylesheet" href="@stylesheet@" type="text/css">
<meta http-equiv="Content-Type" content="text/html; charset=">
</HEAD>
<body   >@vegbank_header_html_normal@  <br><h2>View VegBank Datasets</h2>
<!-- get user ID -->
<% 
Long localPgUserId = (Long)(request.getSession().getAttribute("USER")); 
String strUserId = "0";

if ( localPgUserId==null )
  { strUserId = "-1" ;}
  else strUserId = localPgUserId.toString();

%>
<!-- <h1>Temp: You are user: <%= strUserId %> </h1> -->
<vegbank:get select="userdataset" beanName="map" where="where_usr_pk" wparam="<%= strUserId %>"/>
<logic:empty name="BEANLIST">
                Sorry, you have no Datasets in the database!
          </logic:empty>
<logic:notEmpty name="BEANLIST"><!-- set up table -->

<logic:iterate id="onerow" name="BEANLIST"><!-- iterate over all records in set : new table for each -->
<table border="1" cellpadding="0" cellspacing="0" class="item"><!--each field, only write when HAS contents-->
<logic:notEmpty name="onerow" property="datasetname">
<tr><td><!--label:--><p><span class="category">Dataset Name</span></p></td>
<td><p><span class="item"><bean:write name="onerow" property="datasetname"/>&nbsp;</span></p></td>
</tr>
</logic:notEmpty>
<logic:notEmpty name="onerow" property="datasetdescription">
<tr><td><!--label:--><p><span class="category">Dataset Description</span></p></td>
<td><p><span class="item"><bean:write name="onerow" property="datasetdescription"/>&nbsp;</span></p></td>
</tr>
</logic:notEmpty>
<logic:notEmpty name="onerow" property="datasettype">
<tr><td><!--label:--><p><span class="category">Dataset Type</span></p></td>
<td><p><span class="item"><bean:write name="onerow" property="datasettype"/>&nbsp;</span></p></td>
</tr>
</logic:notEmpty>
<logic:notEmpty name="onerow" property="datasetsharing">
<tr><td><!--label:--><p><span class="category">Dataset Sharing</span></p></td>
<td><p><span class="item"><bean:write name="onerow" property="datasetsharing"/>&nbsp;</span></p></td>
</tr>
</logic:notEmpty>

<logic:notEmpty name="onerow" property="datasetstart">
<tr><td><!--label:--><p><span class="category">Dataset Start</span></p></td>
<td><p><span class="item"><bean:write name="onerow" property="datasetstart"/>&nbsp;</span></p></td>
</tr>
</logic:notEmpty>
<logic:notEmpty name="onerow" property="datasetstop">
<tr><td><!--label:--><p><span class="category">Dataset Stop</span></p></td>
<td><p><span class="item"><bean:write name="onerow" property="datasetstop"/>&nbsp;</span></p></td>
</tr>
</logic:notEmpty>
<logic:notEmpty name="onerow" property="accessioncode">
<tr><td><!--label:--><p><span class="category"> Accession Code</span></p></td>
<td><p><span class="item"><bean:write name="onerow" property="accessioncode"/>&nbsp;</span></p></td>
</tr>
</logic:notEmpty>
<!-- show dataset items -->
<TR><TD colspan="2">
<bean:define id="nestId" name="onerow" property="userdataset_id" toScope="request"/>
Items in this dataset:

<vegbank:get select="userdatasetitem" beanName="map" where="where_userdataset_pk" wparam="<%= (String)nestId %>" perPage="-1"/>
<logic:empty name="BEANLIST">
                Sorry, no userDatasetItem(s) are available in the database!
          </logic:empty>
<logic:notEmpty name="BEANLIST">
<table border="1" cellpadding="0" cellspacing="0">
<tr>
<th>Accession Code</th>
<th>Item Type</th>
<th>Notes</th>
</tr>
<logic:iterate id="onerow" name="BEANLIST">
<tr>
<td><p><span class="item"><bean:write name="onerow" property="itemaccessioncode"/>&nbsp;</span></p></td>
<td><p><span class="item"><bean:write name="onerow" property="itemtype"/>&nbsp;</span></p></td>
<td><p><span class="item"><bean:write name="onerow" property="notes"/>&nbsp;</span></p></td>
</tr>
</logic:iterate>
</table>
</logic:notEmpty>
</TD></TR>

</table>



</logic:iterate>
</logic:notEmpty><br>
          @vegbank_footer_html_tworow@