

<!-- show dataset items -->
<TD class="smallfield" rowspan="2" valign="top" nowrap="nowrap">
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