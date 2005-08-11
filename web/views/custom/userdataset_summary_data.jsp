<td class="userdataset_datasetname"><logic:notEmpty property="datasetname" name="onerowofuserdataset"><span class="largefield">
<bean:write name="onerowofuserdataset" property="datasetname"/></span>
</logic:notEmpty>
<logic:empty property="datasetname" name="onerowofuserdataset">
<logic:notEqual value="true" parameter="textoutput">&nbsp;</logic:notEqual>
</logic:empty></td><td class="userdataset_datasetstart">
<logic:notEmpty property="datasetstart_datetrunc" name="onerowofuserdataset"><span title="<bean:write name='onerowofuserdataset' property='datasetstart' />">
          @subst_lt@dt:format pattern="dd-MMM-yyyy"@subst_gt@
          @subst_lt@dt:parse pattern="yyyy-MM-dd"@subst_gt@
        <bean:write name="onerowofuserdataset" property="datasetstart_datetrunc"/>
           @subst_lt@/dt:parse@subst_gt@
           @subst_lt@/dt:format@subst_gt@
         </span>
</logic:notEmpty>
<logic:empty property="datasetstart" name="onerowofuserdataset">
<logic:notEqual value="true" parameter="textoutput">&nbsp;</logic:notEqual>
</logic:empty></td><td class="userdataset_datasettype">
<logic:notEmpty property="datasettype" name="onerowofuserdataset"><span>
<bean:write name="onerowofuserdataset" property="datasettype"/></span>
</logic:notEmpty>
<logic:empty property="datasettype" name="onerowofuserdataset">
<logic:notEqual value="true" parameter="textoutput">&nbsp;</logic:notEqual>
</logic:empty></td>
<!-- show dataset items -->
<TD class="largefield" rowspan="2" valign="top" nowrap="nowrap">
<bean:define id="ud_id" name="onerowofuserdataset" property="userdataset_id" />
 
<vegbank:get id="userdatasetitem" select="userdatasetitem_counts" beanName="map" 
  where="where_userdataset_pk" wparam="ud_id" perPage="-1" />
   <logic:empty name="userdatasetitem-BEANLIST">
                Empty dataset.
   </logic:empty>

<logic:notEmpty name="userdatasetitem-BEANLIST">

<logic:iterate id="onerowofuserdatasetitem" name="userdatasetitem-BEANLIST">
<!-- need to lowercase the name of the view: -->
<bean:define id="viewtolink" type="java.lang.String" name="onerowofuserdatasetitem" property="itemtable" />
<bean:define id="wheresuffix" value="" />
<logic:equal name="viewtolink" value="observation">
  <bean:define id="wheresuffix" value="_obs" /><!-- special where needed -->
</logic:equal>

<logic:equal name="viewtolink" value="plot">
  <!-- if it's really plot, instead use observation -->
  <bean:define id="viewtolink" type="java.lang.String" value="observation" /> 
  <bean:define id="wheresuffix" value="_plot" /><!-- special where needed -->
</logic:equal>



<!-- go to summary view if > 4  items, else detail -->
<bean:define id="viewtype" value="summary" />
<logic:lessThan name="onerowofuserdatasetitem" property="countudi" value="5">
  <bean:define id="viewtype" value="detail" />
</logic:lessThan>

<a href='@get_link@<bean:write name="viewtype" />/<%= viewtolink.toLowerCase() %>/<bean:write name="onerowofuserdataset" property="accessioncode" />?where=where_inuserdataset_ac<bean:write name="wheresuffix" />'>
<bean:write name="onerowofuserdatasetitem" property="itemtable" /> (<bean:write name="onerowofuserdatasetitem" property="countudi" />)</a><br/>

</logic:iterate>

</logic:notEmpty>
</TD>