<!-- this is a sub form that should be included in another .jsp that gets the datasets needed.  This form displays the contents of the dataset -->
   <%
		       //**************************************************************************************
		       //  Set up alternating row colors
		       //**************************************************************************************
		       String rowClass = "evenrow";
    %>

<logic:empty name="BEANLIST">
                <p>Sorry, no Datasets found!</p>
          </logic:empty>
<logic:notEmpty name="BEANLIST"><!-- set up table -->

<logic:iterate id="onerow" name="BEANLIST"><!-- iterate over all records in set : new table for each -->
<table class="leftright" cellpadding="3"><!--each field, only write when HAS contents-->

<tr class='@nextcolorclass@'><th colspan="2">Dataset :
<bean:write name="onerow" property="datasetname"/></th>
</tr>


<tr class='@nextcolorclass@'><td class="datalabel">Dataset Owner</td>
<td><bean:write name="onerow" property="usr_id_transl"/>&nbsp;</td>
</tr>




<logic:notEmpty name="onerow" property="datasetdescription">
<tr class='@nextcolorclass@'><td class="datalabel">Dataset Description</td>
<td class="sizetiny"><bean:write name="onerow" property="datasetdescription"/>&nbsp;</td>
</tr>
</logic:notEmpty>
<logic:notEmpty name="onerow" property="datasettype">
<tr class='@nextcolorclass@'><td class="datalabel">Dataset Type</td>
<td><bean:write name="onerow" property="datasettype"/>&nbsp;</td>
</tr>
</logic:notEmpty>
<logic:notEmpty name="onerow" property="datasetsharing">
<tr class='@nextcolorclass@'><td class="datalabel">Dataset Sharing</td>
<td><bean:write name="onerow" property="datasetsharing"/>&nbsp;</td>
</tr>
</logic:notEmpty>

<logic:notEmpty name="onerow" property="datasetstart">
<tr class='@nextcolorclass@'><td class="datalabel">Dataset Start</td>
<td><bean:write name="onerow" property="datasetstart"/>&nbsp;</td>
</tr>
</logic:notEmpty>
<logic:notEmpty name="onerow" property="datasetstop">
<tr class='@nextcolorclass@'><td class="datalabel">Dataset Stop</td>
<td><bean:write name="onerow" property="datasetstop"/>&nbsp;</td>
</tr>
</logic:notEmpty>
<logic:notEmpty name="onerow" property="accessioncode">
<tr class='@nextcolorclass@'><td class="datalabel"> Accession Code</td>
<td class="sizetiny"><bean:write name="onerow" property="accessioncode"/>&nbsp;</td>
</tr>
</logic:notEmpty>
<!-- show dataset items -->
<TR><TD colspan="2">
Items in this dataset:
<bean:define id="nestId" name="onerow" property="userdataset_id" />
 
<vegbank:get id="userdatasetitem" select="userdatasetitem" beanName="map" 
  where="where_userdataset_pk" wparam="nestId" perPage="-1"/>
   <logic:empty name="userdatasetitem-BEANLIST">
                Sorry, no userDatasetItem(s) are available in the database!
   </logic:empty>

<logic:notEmpty name="userdatasetitem-BEANLIST">
<table class="leftright" cellpadding="2" >
<tr>
<th>Accession Code</th>
<th>Item Type</th>
<th>Notes</th>
</tr>
<logic:iterate id="oneitem" name="userdatasetitem-BEANLIST">
<tr class='@nextcolorclass@'>
<td class="sizetiny"><bean:write name="oneitem" property="itemaccessioncode"/>&nbsp;</td>
<td><bean:write name="oneitem" property="itemtype"/>&nbsp;</td>
<td><bean:write name="oneitem" property="notes"/>&nbsp;</td>
</tr>
</logic:iterate>
</table>
</logic:notEmpty>
</TD></TR>

</table>



</logic:iterate>
</logic:notEmpty>