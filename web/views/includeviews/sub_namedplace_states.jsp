 <tr><th>State/Province</th><th>Plots</th></tr>
     <logic:iterate id="onerowofnamedplace" name="namedplace-BEANLIST" >
       <!-- loop over list of states -->
       <tr class="@nextcolorclass@"><td ><bean:write name="onerowofnamedplace" property="region_name"/>&nbsp;</td>
       <td class="numeric">&nbsp;
<a href='@get_link@simple/observation/<bean:write name="onerowofnamedplace" property="namedplace_id" />?where=where_place_complex'><bean:write name="onerowofnamedplace" property="count_obs"/></a></td></tr>
     </logic:iterate>