<td class="userdataset_datasetname" ><logic:notEmpty property="datasetname" name="onerowofuserdataset"><span id="<bean:write name='onerowofuserdataset' property='userdataset_id'/>-name" class="smallfield">
<bean:write name="onerowofuserdataset" property="datasetname"/></span><br/><span id="<bean:write name='onerowofuserdataset' property='userdataset_id'/>-description" class="smallfield"><bean:write name="onerowofuserdataset" property="datasetdescription"/></span>
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
<logic:notEmpty property="datasetsharing" name="onerowofuserdataset"><span id="<bean:write name='onerowofuserdataset' property='userdataset_id'/>-sharing">
<bean:write name="onerowofuserdataset" property="datasetsharing"/></span><span style="display:none" id="<bean:write name='onerowofuserdataset' property='userdataset_id'/>-accessioncode"><bean:write name='onerowofuserdataset' property='accessioncode'/></span>
</logic:notEmpty>
<logic:empty property="datasetsharing" name="onerowofuserdataset">
<logic:notEqual value="true" parameter="textoutput">&nbsp;</logic:notEqual>
</logic:empty></td><td class="userdataset_datasettype">
<logic:notEmpty property="datasettype" name="onerowofuserdataset"><span>
<bean:write name="onerowofuserdataset" property="datasettype"/></span>
</logic:notEmpty>
<logic:empty property="datasettype" name="onerowofuserdataset">
<logic:notEqual value="true" parameter="textoutput">&nbsp;</logic:notEqual>
</logic:empty></td>
