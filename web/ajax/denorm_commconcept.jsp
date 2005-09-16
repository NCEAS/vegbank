@stdvegbankget_jspdeclarations@

 

<h2>denorm commconcept</h2>
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
<table class="thinlines sortable" id="thisdenormtable" >
<tr>
  <th>dnrm_{Table}_{Field}</th>
  <th>records updated (0 is NOT an error)</th>
  <th>Errors?</th>
</tr>

<!-- template : -->

<!--vegbank:denorm update="&" /-->
<!--tr-->
  <!--td>&</td-->
  <!--td class="numeric"--><!--bean:write name="&-RESULTS" ignore="true"/--><!--/td-->
  <!--td--><!--logic:lessThan value="0" name="&-RESULTS"--><!--span class="error">ERROR!  please check the log</span--><!--/logic:lessThan--><!--/td-->
<!--/tr-->


<vegbank:denorm update="dnrm-commconcept-2101-commname" />
<tr>
  <td>commconcept.commname</td>
  <td class="numeric"><bean:write name="dnrm-commconcept-2101-commname-RESULTS" ignore="true"/></td>
  <td><logic:lessThan value="0" name="dnrm-commconcept-2101-commname-RESULTS"><span class="error">ERROR!  please check the log</span></logic:lessThan></td>
</tr>

<vegbank:denorm update="dnrm-commusage-1201-commname" />
<tr>
  <td>dnrm-commusage-1201-commname</td>
  <td class="numeric"><bean:write name="dnrm-commusage-1201-commname-RESULTS" ignore="true"/></td>
  <td><logic:lessThan value="0" name="dnrm-commusage-1201-commname-RESULTS"><span class="error">ERROR!  please check the log</span></logic:lessThan></td>
</tr>
<vegbank:denorm update="dnrm-commusage-1501-commconceptid" />
<tr>
  <td>dnrm-commusage-1501-commconceptid</td>
  <td class="numeric"><bean:write name="dnrm-commusage-1501-commconceptid-RESULTS" ignore="true"/></td>
  <td><logic:lessThan value="0" name="dnrm-commusage-1501-commconceptid-RESULTS"><span class="error">ERROR!  please check the log</span></logic:lessThan></td>
</tr>
<vegbank:denorm update="dnrm-commusage-1801-partyid" />
<tr>
  <td>dnrm-commusage-1801-partyid</td>
  <td class="numeric"><bean:write name="dnrm-commusage-1801-partyid-RESULTS" ignore="true"/></td>
  <td><logic:lessThan value="0" name="dnrm-commusage-1801-partyid-RESULTS"><span class="error">ERROR!  please check the log</span></logic:lessThan></td>
</tr>


</table>

