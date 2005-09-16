@stdvegbankget_jspdeclarations@


<h2>denorm PLANT concept</h2>
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

<!-- null is default, else can specify denormtype=all on URL -->


<hr/>
<table class="thinlines sortable" id="thisdenormtable" >
<tr>
  <th>dnrm_{Table}_{Field}</th>
  <th>records updated (0 is NOT an error)</th>
  <th>Errors?</th>
</tr>
<vegbank:denorm update="dnrm-plantconcept-301-plantname" />
<tr>
  <td>dnrm-plantconcept-301-plantname</td>
  <td class="numeric"><bean:write name="dnrm-plantconcept-301-plantname-RESULTS" ignore="true"/></td>
  <td><logic:lessThan value="0" name="dnrm-plantconcept-301-plantname-RESULTS"><span class="error">ERROR!  please check the log</span></logic:lessThan></td>
</tr>
<vegbank:denorm update="dnrm-plantusage-1-plantname" />
<tr>
  <td>dnrm-plantusage-1-plantname</td>
  <td class="numeric"><bean:write name="dnrm-plantusage-1-plantname-RESULTS" ignore="true"/></td>
  <td><logic:lessThan value="0" name="dnrm-plantusage-1-plantname-RESULTS"><span class="error">ERROR!  please check the log</span></logic:lessThan></td>
</tr>
<vegbank:denorm update="dnrm-plantusage-601-plantconceptid" />
<tr>
  <td>dnrm-plantusage-601-plantconceptid</td>
  <td class="numeric"><bean:write name="dnrm-plantusage-601-plantconceptid-RESULTS" ignore="true"/></td>
  <td><logic:lessThan value="0" name="dnrm-plantusage-601-plantconceptid-RESULTS"><span class="error">ERROR!  please check the log</span></logic:lessThan></td>
</tr>
<vegbank:denorm update="dnrm-plantusage-901-partyid" />
<tr>
  <td>dnrm-plantusage-901-partyid</td>
  <td class="numeric"><bean:write name="dnrm-plantusage-901-partyid-RESULTS" ignore="true"/></td>
  <td><logic:lessThan value="0" name="dnrm-plantusage-901-partyid-RESULTS"><span class="error">ERROR!  please check the log</span></logic:lessThan></td>
</tr>

</table>




