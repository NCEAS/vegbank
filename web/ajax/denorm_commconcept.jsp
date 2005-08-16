@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@
 
<TITLE>denorm commconcept</TITLE>
 
 @webpage_masthead_html@ 
 

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
  <!--td--><!--logic:empty name="&-RESULTS"--><!--span class="error">ERROR!  please check the log</span--><!--/logic:empty--><!--/td-->
<!--/tr-->


<vegbank:denorm update="dnrm_commconcept_commname" />
<tr>
  <td>commconcept.commname</td>
  <td class="numeric"><bean:write name="dnrm_commconcept_commname-RESULTS" ignore="true"/></td>
  <td><logic:empty name="dnrm_commconcept_commname-RESULTS"><span class="error">ERROR!  please check the log</span></logic:empty></td>
</tr>

<vegbank:denorm update="dnrm_commusage_commname" />
<tr>
  <td>dnrm_commusage_commname</td>
  <td class="numeric"><bean:write name="dnrm_commusage_commname-RESULTS" ignore="true"/></td>
  <td><logic:empty name="dnrm_commusage_commname-RESULTS"><span class="error">ERROR!  please check the log</span></logic:empty></td>
</tr>
<vegbank:denorm update="dnrm_commusage_commconceptid" />
<tr>
  <td>dnrm_commusage_commconceptid</td>
  <td class="numeric"><bean:write name="dnrm_commusage_commconceptid-RESULTS" ignore="true"/></td>
  <td><logic:empty name="dnrm_commusage_commconceptid-RESULTS"><span class="error">ERROR!  please check the log</span></logic:empty></td>
</tr>
<vegbank:denorm update="dnrm_commusage_partyid" />
<tr>
  <td>dnrm_commusage_partyid</td>
  <td class="numeric"><bean:write name="dnrm_commusage_partyid-RESULTS" ignore="true"/></td>
  <td><logic:empty name="dnrm_commusage_partyid-RESULTS"><span class="error">ERROR!  please check the log</span></logic:empty></td>
</tr>


</table>



@webpage_footer_html@


