@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@
 
<TITLE>denorm PLANT concept</TITLE>
 
 @webpage_masthead_html@ 
 

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
<vegbank:denorm update="dnrm_plantconcept_plantname" />
<tr>
  <td>dnrm_plantconcept_plantname</td>
  <td class="numeric"><bean:write name="dnrm_plantconcept_plantname-RESULTS" ignore="true"/></td>
  <td><logic:empty name="dnrm_plantconcept_plantname-RESULTS"><span class="error">ERROR!  please check the log</span></logic:empty></td>
</tr>
<vegbank:denorm update="dnrm_plantusage_plantname" />
<tr>
  <td>dnrm_plantusage_plantname</td>
  <td class="numeric"><bean:write name="dnrm_plantusage_plantname-RESULTS" ignore="true"/></td>
  <td><logic:empty name="dnrm_plantusage_plantname-RESULTS"><span class="error">ERROR!  please check the log</span></logic:empty></td>
</tr>
<vegbank:denorm update="dnrm_plantusage_plantconceptid" />
<tr>
  <td>dnrm_plantusage_plantconceptid</td>
  <td class="numeric"><bean:write name="dnrm_plantusage_plantconceptid-RESULTS" ignore="true"/></td>
  <td><logic:empty name="dnrm_plantusage_plantconceptid-RESULTS"><span class="error">ERROR!  please check the log</span></logic:empty></td>
</tr>
<vegbank:denorm update="dnrm_plantusage_partyid" />
<tr>
  <td>dnrm_plantusage_partyid</td>
  <td class="numeric"><bean:write name="dnrm_plantusage_partyid-RESULTS" ignore="true"/></td>
  <td><logic:empty name="dnrm_plantusage_partyid-RESULTS"><span class="error">ERROR!  please check the log</span></logic:empty></td>
</tr>

</table>


@webpage_footer_html@


