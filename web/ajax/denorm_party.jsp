
  @stdvegbankget_jspdeclarations@

   

<p>denorm party</p>
<p>If no errors are presented on this page, it should have worked ok. </p>

<!-- look for wparam, if so, then do ac -->
<p> Will run the default, which is to update null denorm fields, unless another message appears here: </p>
<logic:present parameter="wparam">
  <bean:define id="denormtype" value="ac" type="java.lang.String" />
  <bean:parameter id="tempac" name="wparam" value="ERROR!notfound" />
  <p>Updating for only one accession code: <a href="@cite_link@<bean:write name='tempac' />"><bean:write name='tempac' /></a></p>
</logic:present>
<!-- null is default, else can specify denormtype=all on URL -->
<logic:equal parameter="denormtype" value="all">
  <p>Updating ALL records.</p>
</logic:equal>


<hr/>
<table border="1" class="thinlines sortable" id="thisdenormtable" >
<tr>
  <th>dnrm_{Table}_{Field}</th>
  <th>records updated (0 is NOT an error)</th>
  <th>Errors?</th>
</tr>
<vegbank:denorm update="dnrm_party_partypublic" />
<tr>
  <td>dnrm_party_partypublic</td>
  <td class="numeric"><bean:write name="dnrm_party_partypublic-RESULTS" ignore="true"/></td>
  <td><logic:lessThan value="0" name="dnrm_party_partypublic-RESULTS"><span class="error">ERROR!  please check the log</span></logic:lessThan></td>
</tr>

</table>



