<!--Where statement removed from preceding: -->
<logic:empty name="browseparty-BEANLIST">
<p>  Sorry, no parties found (error!)</p>
</logic:empty>
<logic:notEmpty name="browseparty-BEANLIST">
<table class="leftrightborders" cellpadding="2">
<tr>
<!-- don't show party name if already shown on master view -->
<logic:notEqual name="partyShown" value="yes">
<th>Party</th>
</logic:notEqual>
          <th>Total Plots</th>
          <th>Observation Contribution Plots</th>
          <th>Classified Plots</th>
          <th>Project Contribution Plots</th>
                
                  
 </tr>

<logic:iterate id="onerowofbrowseparty" name="browseparty-BEANLIST">

<tr class="@nextcolorclass@">
<!-- don't show party name if already shown on master view -->
<logic:notEqual name="partyShown" value="yes">
<td>
  <a href='@get_link@std/party/<bean:write name="onerowofbrowseparty" property="party_id" />'><bean:write name="onerowofbrowseparty" property="party_id_transl" /></a>
</td>
</logic:notEqual>
<td class="numeric">
  <logic:notEmpty name="onerowofbrowseparty" property="countallcontrib">
    <a href='@get_link@simple/observation/<bean:write name="onerowofbrowseparty" property="party_id" />?where=where_obs_allparty' ><bean:write name="onerowofbrowseparty" property="countallcontrib" /></a>
  </logic:notEmpty>
  <logic:empty name="onerowofbrowseparty" property="countallcontrib">
    0
  </logic:empty>
</td>

<td class="numeric">
  <logic:notEmpty name="onerowofbrowseparty" property="countobscontrib">
    <a href='@get_link@simple/observation/<bean:write name="onerowofbrowseparty" property="party_id" />?where=where_obs_obscontrib' ><bean:write name="onerowofbrowseparty" property="countobscontrib" /></a>
  </logic:notEmpty>
  <logic:empty name="onerowofbrowseparty" property="countobscontrib">
    0
  </logic:empty>
</td>

<td class="numeric">
  <logic:notEmpty name="onerowofbrowseparty" property="countclasscontrib">
    <a href='@get_link@simple/observation/<bean:write name="onerowofbrowseparty" property="party_id" />?where=where_obs_classcontrib' ><bean:write name="onerowofbrowseparty" property="countclasscontrib" /></a>
  </logic:notEmpty>
  <logic:empty name="onerowofbrowseparty" property="countclasscontrib">
    0
  </logic:empty>
</td>

<td class="numeric">
  <logic:notEmpty name="onerowofbrowseparty" property="countprojectcontrib">
    <a href='@get_link@simple/observation/<bean:write name="onerowofbrowseparty" property="party_id" />?where=where_obs_projcontrib' ><bean:write name="onerowofbrowseparty" property="countprojectcontrib" /></a>
  </logic:notEmpty>
  <logic:empty name="onerowofbrowseparty" property="countprojectcontrib">
    0
  </logic:empty>
</td>


</logic:iterate>
</table>
</logic:notEmpty>