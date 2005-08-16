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
<vegbank:denorm update="dnrm_comminterpretation_commname" />
<tr>
  <td>dnrm_comminterpretation_commname</td>
  <td class="numeric"><bean:write name="dnrm_comminterpretation_commname-RESULTS" ignore="true"/></td>
  <td><logic:empty name="dnrm_comminterpretation_commname-RESULTS"><span class="error">ERROR!  please check the log</span></logic:empty></td>
</tr>
<vegbank:denorm update="dnrm_plot_country" />
<tr>
  <td>dnrm_plot_country</td>
  <td class="numeric"><bean:write name="dnrm_plot_country-RESULTS" ignore="true"/></td>
  <td><logic:empty name="dnrm_plot_country-RESULTS"><span class="error">ERROR!  please check the log</span></logic:empty></td>
</tr>
<vegbank:denorm update="dnrm_plot_state" />
<tr>
  <td>dnrm_plot_state</td>
  <td class="numeric"><bean:write name="dnrm_plot_state-RESULTS" ignore="true"/></td>
  <td><logic:empty name="dnrm_plot_state-RESULTS"><span class="error">ERROR!  please check the log</span></logic:empty></td>
</tr>
<vegbank:denorm update="dnrm_stratum_stratummethod" />
<tr>
  <td>dnrm_stratum_stratummethod</td>
  <td class="numeric"><bean:write name="dnrm_stratum_stratummethod-RESULTS" ignore="true"/></td>
  <td><logic:empty name="dnrm_stratum_stratummethod-RESULTS"><span class="error">ERROR!  please check the log</span></logic:empty></td>
</tr>
<vegbank:denorm update="dnrm_stratum_stratumname" />
<tr>
  <td>dnrm_stratum_stratumname</td>
  <td class="numeric"><bean:write name="dnrm_stratum_stratumname-RESULTS" ignore="true"/></td>
  <td><logic:empty name="dnrm_stratum_stratumname-RESULTS"><span class="error">ERROR!  please check the log</span></logic:empty></td>
</tr>
<vegbank:denorm update="dnrm_taximp_stratumheight" />
<tr>
  <td>dnrm_taximp_stratumheight</td>
  <td class="numeric"><bean:write name="dnrm_taximp_stratumheight-RESULTS" ignore="true"/></td>
  <td><logic:empty name="dnrm_taximp_stratumheight-RESULTS"><span class="error">ERROR!  please check the log</span></logic:empty></td>
</tr>
<vegbank:denorm update="dnrm_taximp_stratumbase" />
<tr>
  <td>dnrm_taximp_stratumbase</td>
  <td class="numeric"><bean:write name="dnrm_taximp_stratumbase-RESULTS" ignore="true"/></td>
  <td><logic:empty name="dnrm_taximp_stratumbase-RESULTS"><span class="error">ERROR!  please check the log</span></logic:empty></td>
</tr>
<vegbank:denorm update="dnrm_plot_embargo" />
<tr>
  <td>dnrm_plot_embargo</td>
  <td class="numeric"><bean:write name="dnrm_plot_embargo-RESULTS" ignore="true"/></td>
  <td><logic:empty name="dnrm_plot_embargo-RESULTS"><span class="error">ERROR!  please check the log</span></logic:empty></td>
</tr>
<vegbank:denorm update="dnrm_observation_embargo" />
<tr>
  <td>dnrm_observation_embargo</td>
  <td class="numeric"><bean:write name="dnrm_observation_embargo-RESULTS" ignore="true"/></td>
  <td><logic:empty name="dnrm_observation_embargo-RESULTS"><span class="error">ERROR!  please check the log</span></logic:empty></td>
</tr>
<vegbank:denorm update="dnrm_disturbanceobs_embargo" />
<tr>
  <td>dnrm_disturbanceobs_embargo</td>
  <td class="numeric"><bean:write name="dnrm_disturbanceobs_embargo-RESULTS" ignore="true"/></td>
  <td><logic:empty name="dnrm_disturbanceobs_embargo-RESULTS"><span class="error">ERROR!  please check the log</span></logic:empty></td>
</tr>
<vegbank:denorm update="dnrm_soilobs_embargo" />
<tr>
  <td>dnrm_soilobs_embargo</td>
  <td class="numeric"><bean:write name="dnrm_soilobs_embargo-RESULTS" ignore="true"/></td>
  <td><logic:empty name="dnrm_soilobs_embargo-RESULTS"><span class="error">ERROR!  please check the log</span></logic:empty></td>
</tr>
<vegbank:denorm update="dnrm_commclass_embargo" />
<tr>
  <td>dnrm_commclass_embargo</td>
  <td class="numeric"><bean:write name="dnrm_commclass_embargo-RESULTS" ignore="true"/></td>
  <td><logic:empty name="dnrm_commclass_embargo-RESULTS"><span class="error">ERROR!  please check the log</span></logic:empty></td>
</tr>
<vegbank:denorm update="dnrm_classcontributor_embargo" />
<tr>
  <td>dnrm_classcontributor_embargo</td>
  <td class="numeric"><bean:write name="dnrm_classcontributor_embargo-RESULTS" ignore="true"/></td>
  <td><logic:empty name="dnrm_classcontributor_embargo-RESULTS"><span class="error">ERROR!  please check the log</span></logic:empty></td>
</tr>
<vegbank:denorm update="dnrm_comminterpretation_embargo" />
<tr>
  <td>dnrm_comminterpretation_embargo</td>
  <td class="numeric"><bean:write name="dnrm_comminterpretation_embargo-RESULTS" ignore="true"/></td>
  <td><logic:empty name="dnrm_comminterpretation_embargo-RESULTS"><span class="error">ERROR!  please check the log</span></logic:empty></td>
</tr>
<vegbank:denorm update="dnrm_taxonobservation_embargo" />
<tr>
  <td>dnrm_taxonobservation_embargo</td>
  <td class="numeric"><bean:write name="dnrm_taxonobservation_embargo-RESULTS" ignore="true"/></td>
  <td><logic:empty name="dnrm_taxonobservation_embargo-RESULTS"><span class="error">ERROR!  please check the log</span></logic:empty></td>
</tr>
<vegbank:denorm update="dnrm_taxonimportance_embargo" />
<tr>
  <td>dnrm_taxonimportance_embargo</td>
  <td class="numeric"><bean:write name="dnrm_taxonimportance_embargo-RESULTS" ignore="true"/></td>
  <td><logic:empty name="dnrm_taxonimportance_embargo-RESULTS"><span class="error">ERROR!  please check the log</span></logic:empty></td>
</tr>

<vegbank:denorm update="dnrm_taxoninterpretation_embargo" />
<tr>
  <td>dnrm_taxoninterpretation_embargo</td>
  <td class="numeric"><bean:write name="dnrm_taxoninterpretation_embargo-RESULTS" ignore="true"/></td>
  <td><logic:empty name="dnrm_taxoninterpretation_embargo-RESULTS"><span class="error">ERROR!  please check the log</span></logic:empty></td>
</tr>
<vegbank:denorm update="dnrm_taxonalt_embargo" />
<tr>
  <td>dnrm_taxonalt_embargo</td>
  <td class="numeric"><bean:write name="dnrm_taxonalt_embargo-RESULTS" ignore="true"/></td>
  <td><logic:empty name="dnrm_taxonalt_embargo-RESULTS"><span class="error">ERROR!  please check the log</span></logic:empty></td>
</tr>
<vegbank:denorm update="dnrm_plantconcept_obscount" />
<tr>
  <td>dnrm_plantconcept_obscount</td>
  <td class="numeric"><bean:write name="dnrm_plantconcept_obscount-RESULTS" ignore="true"/></td>
  <td><logic:empty name="dnrm_plantconcept_obscount-RESULTS"><span class="error">ERROR!  please check the log</span></logic:empty></td>
</tr>
<vegbank:denorm update="dnrm_commconcept_obscount" />
<tr>
  <td>dnrm_commconcept_obscount</td>
  <td class="numeric"><bean:write name="dnrm_commconcept_obscount-RESULTS" ignore="true"/></td>
  <td><logic:empty name="dnrm_commconcept_obscount-RESULTS"><span class="error">ERROR!  please check the log</span></logic:empty></td>
</tr>
<vegbank:denorm update="dnrm_project_obscount" />
<tr>
  <td>dnrm_project_obscount</td>
  <td class="numeric"><bean:write name="dnrm_project_obscount-RESULTS" ignore="true"/></td>
  <td><logic:empty name="dnrm_project_obscount-RESULTS"><span class="error">ERROR!  please check the log</span></logic:empty></td>
</tr>
<vegbank:denorm update="dnrm_party_obscount" />
<tr>
  <td>dnrm_party_obscount</td>
  <td class="numeric"><bean:write name="dnrm_party_obscount-RESULTS" ignore="true"/></td>
  <td><logic:empty name="dnrm_party_obscount-RESULTS"><span class="error">ERROR!  please check the log</span></logic:empty></td>
</tr>
<vegbank:denorm update="dnrm_namedplace_obscount" />
<tr>
  <td>dnrm_namedplace_obscount</td>
  <td class="numeric"><bean:write name="dnrm_namedplace_obscount-RESULTS" ignore="true"/></td>
  <td><logic:empty name="dnrm_namedplace_obscount-RESULTS"><span class="error">ERROR!  please check the log</span></logic:empty></td>
</tr>
<vegbank:denorm update="dnrm_taximportance_covercode_allnull" />
<tr>
  <td>dnrm_taximportance_covercode_allnull</td>
  <td class="numeric"><bean:write name="dnrm_taximportance_covercode_allnull-RESULTS" ignore="true"/></td>
  <td><logic:empty name="dnrm_taximportance_covercode_allnull-RESULTS"><span class="error">ERROR!  please check the log</span></logic:empty></td>
</tr>
<vegbank:denorm update="dnrm_taximportance_covercode_exact" />
<tr>
  <td>dnrm_taximportance_covercode_exact</td>
  <td class="numeric"><bean:write name="dnrm_taximportance_covercode_exact-RESULTS" ignore="true"/></td>
  <td><logic:empty name="dnrm_taximportance_covercode_exact-RESULTS"><span class="error">ERROR!  please check the log</span></logic:empty></td>
</tr>
<vegbank:denorm update="dnrm_taximportance_covercode_range" />
<tr>
  <td>dnrm_taximportance_covercode_range</td>
  <td class="numeric"><bean:write name="dnrm_taximportance_covercode_range-RESULTS" ignore="true"/></td>
  <td><logic:empty name="dnrm_taximportance_covercode_range-RESULTS"><span class="error">ERROR!  please check the log</span></logic:empty></td>
</tr>
<vegbank:denorm update="dnrm_taxobs_origpcid" />
<tr>
  <td>dnrm_taxobs_origpcid</td>
  <td class="numeric"><bean:write name="dnrm_taxobs_origpcid-RESULTS" ignore="true"/></td>
  <td><logic:empty name="dnrm_taxobs_origpcid-RESULTS"><span class="error">ERROR!  please check the log</span></logic:empty></td>
</tr>
<vegbank:denorm update="dnrm_taxonobs_origsna" />
<tr>
  <td>dnrm_taxonobs_origsna</td>
  <td class="numeric"><bean:write name="dnrm_taxonobs_origsna-RESULTS" ignore="true"/></td>
  <td><logic:empty name="dnrm_taxonobs_origsna-RESULTS"><span class="error">ERROR!  please check the log</span></logic:empty></td>
</tr>
<vegbank:denorm update="dnrm_taxonobs_origsn" />
<tr>
  <td>dnrm_taxonobs_origsn</td>
  <td class="numeric"><bean:write name="dnrm_taxonobs_origsn-RESULTS" ignore="true"/></td>
  <td><logic:empty name="dnrm_taxonobs_origsn-RESULTS"><span class="error">ERROR!  please check the log</span></logic:empty></td>
</tr>
<vegbank:denorm update="dnrm_taxonobs_origcode" />
<tr>
  <td>dnrm_taxonobs_origcode</td>
  <td class="numeric"><bean:write name="dnrm_taxonobs_origcode-RESULTS" ignore="true"/></td>
  <td><logic:empty name="dnrm_taxonobs_origcode-RESULTS"><span class="error">ERROR!  please check the log</span></logic:empty></td>
</tr>
<vegbank:denorm update="dnrm_taxonobs_origcommon" />
<tr>
  <td>dnrm_taxonobs_origcommon</td>
  <td class="numeric"><bean:write name="dnrm_taxonobs_origcommon-RESULTS" ignore="true"/></td>
  <td><logic:empty name="dnrm_taxonobs_origcommon-RESULTS"><span class="error">ERROR!  please check the log</span></logic:empty></td>
</tr>
<vegbank:denorm update="dnrm_taxobs_currpcid" />
<tr>
  <td>dnrm_taxobs_currpcid</td>
  <td class="numeric"><bean:write name="dnrm_taxobs_currpcid-RESULTS" ignore="true"/></td>
  <td><logic:empty name="dnrm_taxobs_currpcid-RESULTS"><span class="error">ERROR!  please check the log</span></logic:empty></td>
</tr>
<vegbank:denorm update="dnrm_taxobs_currsna" />
<tr>
  <td>dnrm_taxobs_currsna</td>
  <td class="numeric"><bean:write name="dnrm_taxobs_currsna-RESULTS" ignore="true"/></td>
  <td><logic:empty name="dnrm_taxobs_currsna-RESULTS"><span class="error">ERROR!  please check the log</span></logic:empty></td>
</tr>
<vegbank:denorm update="dnrm_taxobs_currsn" />
<tr>
  <td>dnrm_taxobs_currsn</td>
  <td class="numeric"><bean:write name="dnrm_taxobs_currsn-RESULTS" ignore="true"/></td>
  <td><logic:empty name="dnrm_taxobs_currsn-RESULTS"><span class="error">ERROR!  please check the log</span></logic:empty></td>
</tr>
<vegbank:denorm update="dnrm_taxobs_currcode" />
<tr>
  <td>dnrm_taxobs_currcode</td>
  <td class="numeric"><bean:write name="dnrm_taxobs_currcode-RESULTS" ignore="true"/></td>
  <td><logic:empty name="dnrm_taxobs_currcode-RESULTS"><span class="error">ERROR!  please check the log</span></logic:empty></td>
</tr>
<vegbank:denorm update="dnrm_taxobs_currcommon" />
<tr>
  <td>dnrm_taxobs_currcommon</td>
  <td class="numeric"><bean:write name="dnrm_taxobs_currcommon-RESULTS" ignore="true"/></td>
  <td><logic:empty name="dnrm_taxobs_currcommon-RESULTS"><span class="error">ERROR!  please check the log</span></logic:empty></td>
</tr>
<vegbank:denorm update="dnrm_obs_commint_1" />
<tr>
  <td>dnrm_obs_commint_1</td>
  <td class="numeric"><bean:write name="dnrm_obs_commint_1-RESULTS" ignore="true"/></td>
  <td><logic:empty name="dnrm_obs_commint_1-RESULTS"><span class="error">ERROR!  please check the log</span></logic:empty></td>
</tr>
<vegbank:denorm update="dnrm_obs_commint_2" />
<tr>
  <td>dnrm_obs_commint_2</td>
  <td class="numeric"><bean:write name="dnrm_obs_commint_2-RESULTS" ignore="true"/></td>
  <td><logic:empty name="dnrm_obs_commint_2-RESULTS"><span class="error">ERROR!  please check the log</span></logic:empty></td>
</tr>
<vegbank:denorm update="dnrm_obs_commint_3" />
<tr>
  <td>dnrm_obs_commint_3</td>
  <td class="numeric"><bean:write name="dnrm_obs_commint_3-RESULTS" ignore="true"/></td>
  <td><logic:empty name="dnrm_obs_commint_3-RESULTS"><span class="error">ERROR!  please check the log</span></logic:empty></td>
</tr>
<vegbank:denorm update="dnrm_obs_commint_4" />
<tr>
  <td>dnrm_obs_commint_4</td>
  <td class="numeric"><bean:write name="dnrm_obs_commint_4-RESULTS" ignore="true"/></td>
  <td><logic:empty name="dnrm_obs_commint_4-RESULTS"><span class="error">ERROR!  please check the log</span></logic:empty></td>
</tr>
<vegbank:denorm update="dnrm_obs_commint_5" />
<tr>
  <td>dnrm_obs_commint_5</td>
  <td class="numeric"><bean:write name="dnrm_obs_commint_5-RESULTS" ignore="true"/></td>
  <td><logic:empty name="dnrm_obs_commint_5-RESULTS"><span class="error">ERROR!  please check the log</span></logic:empty></td>
</tr>
<vegbank:denorm update="dnrm_obs_commint_6" />
<tr>
  <td>dnrm_obs_commint_6</td>
  <td class="numeric"><bean:write name="dnrm_obs_commint_6-RESULTS" ignore="true"/></td>
  <td><logic:empty name="dnrm_obs_commint_6-RESULTS"><span class="error">ERROR!  please check the log</span></logic:empty></td>
</tr>
<vegbank:denorm update="dnrm_obs_commint_7" />
<tr>
  <td>dnrm_obs_commint_7</td>
  <td class="numeric"><bean:write name="dnrm_obs_commint_7-RESULTS" ignore="true"/></td>
  <td><logic:empty name="dnrm_obs_commint_7-RESULTS"><span class="error">ERROR!  please check the log</span></logic:empty></td>
</tr>
<vegbank:denorm update="dnrm_obs_commint_8" />
<tr>
  <td>dnrm_obs_commint_8</td>
  <td class="numeric"><bean:write name="dnrm_obs_commint_8-RESULTS" ignore="true"/></td>
  <td><logic:empty name="dnrm_obs_commint_8-RESULTS"><span class="error">ERROR!  please check the log</span></logic:empty></td>
</tr>
<vegbank:denorm update="dnrm_obs_commint_9" />
<tr>
  <td>dnrm_obs_commint_9</td>
  <td class="numeric"><bean:write name="dnrm_obs_commint_9-RESULTS" ignore="true"/></td>
  <td><logic:empty name="dnrm_obs_commint_9-RESULTS"><span class="error">ERROR!  please check the log</span></logic:empty></td>
</tr>
<vegbank:denorm update="dnrm_obs_commint_10" />
<tr>
  <td>dnrm_obs_commint_10</td>
  <td class="numeric"><bean:write name="dnrm_obs_commint_10-RESULTS" ignore="true"/></td>
  <td><logic:empty name="dnrm_obs_commint_10-RESULTS"><span class="error">ERROR!  please check the log</span></logic:empty></td>
</tr>
<vegbank:denorm update="dnrm_obs_commint_11" />
<tr>
  <td>dnrm_obs_commint_11</td>
  <td class="numeric"><bean:write name="dnrm_obs_commint_11-RESULTS" ignore="true"/></td>
  <td><logic:empty name="dnrm_obs_commint_11-RESULTS"><span class="error">ERROR!  please check the log</span></logic:empty></td>
</tr>
<vegbank:denorm update="dnrm_obs_commint_12" />
<tr>
  <td>dnrm_obs_commint_12</td>
  <td class="numeric"><bean:write name="dnrm_obs_commint_12-RESULTS" ignore="true"/></td>
  <td><logic:empty name="dnrm_obs_commint_12-RESULTS"><span class="error">ERROR!  please check the log</span></logic:empty></td>
</tr>
<vegbank:denorm update="dnrm_obs_commint_13" />
<tr>
  <td>dnrm_obs_commint_13</td>
  <td class="numeric"><bean:write name="dnrm_obs_commint_13-RESULTS" ignore="true"/></td>
  <td><logic:empty name="dnrm_obs_commint_13-RESULTS"><span class="error">ERROR!  please check the log</span></logic:empty></td>
</tr>
<vegbank:denorm update="dnrm_obs_commint_14" />
<tr>
  <td>dnrm_obs_commint_14</td>
  <td class="numeric"><bean:write name="dnrm_obs_commint_14-RESULTS" ignore="true"/></td>
  <td><logic:empty name="dnrm_obs_commint_14-RESULTS"><span class="error">ERROR!  please check the log</span></logic:empty></td>
</tr>
<vegbank:denorm update="dnrm_obs_commint_15" />
<tr>
  <td>dnrm_obs_commint_15</td>
  <td class="numeric"><bean:write name="dnrm_obs_commint_15-RESULTS" ignore="true"/></td>
  <td><logic:empty name="dnrm_obs_commint_15-RESULTS"><span class="error">ERROR!  please check the log</span></logic:empty></td>
</tr>
<vegbank:denorm update="dnrm_obs_commint_16" />
<tr>
  <td>dnrm_obs_commint_16</td>
  <td class="numeric"><bean:write name="dnrm_obs_commint_16-RESULTS" ignore="true"/></td>
  <td><logic:empty name="dnrm_obs_commint_16-RESULTS"><span class="error">ERROR!  please check the log</span></logic:empty></td>
</tr>
<vegbank:denorm update="dnrm_obs_commint_17" />
<tr>
  <td>dnrm_obs_commint_17</td>
  <td class="numeric"><bean:write name="dnrm_obs_commint_17-RESULTS" ignore="true"/></td>
  <td><logic:empty name="dnrm_obs_commint_17-RESULTS"><span class="error">ERROR!  please check the log</span></logic:empty></td>
</tr>
<vegbank:denorm update="dnrm_obs_commint_18" />
<tr>
  <td>dnrm_obs_commint_18</td>
  <td class="numeric"><bean:write name="dnrm_obs_commint_18-RESULTS" ignore="true"/></td>
  <td><logic:empty name="dnrm_obs_commint_18-RESULTS"><span class="error">ERROR!  please check the log</span></logic:empty></td>
</tr>
<vegbank:denorm update="dnrm_obs_commint_19" />
<tr>
  <td>dnrm_obs_commint_19</td>
  <td class="numeric"><bean:write name="dnrm_obs_commint_19-RESULTS" ignore="true"/></td>
  <td><logic:empty name="dnrm_obs_commint_19-RESULTS"><span class="error">ERROR!  please check the log</span></logic:empty></td>
</tr>
<vegbank:denorm update="dnrm_obs_commint_20" />
<tr>
  <td>dnrm_obs_commint_20</td>
  <td class="numeric"><bean:write name="dnrm_obs_commint_20-RESULTS" ignore="true"/></td>
  <td><logic:empty name="dnrm_obs_commint_20-RESULTS"><span class="error">ERROR!  please check the log</span></logic:empty></td>
</tr>
<vegbank:denorm update="dnrm_obs_commint_21" />
<tr>
  <td>dnrm_obs_commint_21</td>
  <td class="numeric"><bean:write name="dnrm_obs_commint_21-RESULTS" ignore="true"/></td>
  <td><logic:empty name="dnrm_obs_commint_21-RESULTS"><span class="error">ERROR!  please check the log</span></logic:empty></td>
</tr>



</table>

@webpage_footer_html@


