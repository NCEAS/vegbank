<hr/>
<table class="thinlines sortable" id="thisdenormtable" >
<tr>
  <th>dnrm_{Table}_{Field}</th>
  <th>records updated (0 is NOT an error)</th>
  <th>Errors?</th>
</tr>



<logic:present parameter="latlong_only">
  <!-- only update lat/long in plot table -->
  <vegbank:denorm update="dnrm-plot-22201-latlong" />
  <tr>
    <td>dnrm-plot-22201-latlong</td>
    <td class="numeric"><bean:write name="dnrm-plot-22201-latlong-RESULTS" ignore="true"/></td>
    <td><logic:lessThan value="0" name="dnrm-plot-22201-latlong-RESULTS"><span class="error">ERROR!  please check the log</span></logic:lessThan></td>
  </tr>
</logic:present>

<logic:present parameter="emb_only">
  <!-- common request -->
  
  <vegbank:denorm update="dnrm-plot-4501-embargo" />
  <tr>
    <td>dnrm-plot-4501-embargo</td>
    <td class="numeric"><bean:write name="dnrm-plot-4501-embargo-RESULTS" ignore="true"/></td>
    <td><logic:lessThan value="0" name="dnrm-plot-4501-embargo-RESULTS"><span class="error">ERROR!  please check the log</span></logic:lessThan></td>
  </tr>
  <vegbank:denorm update="dnrm-observation-4801-embargo" />
  <tr>
    <td>dnrm-observation-4801-embargo</td>
    <td class="numeric"><bean:write name="dnrm-observation-4801-embargo-RESULTS" ignore="true"/></td>
    <td><logic:lessThan value="0" name="dnrm-observation-4801-embargo-RESULTS"><span class="error">ERROR!  please check the log</span></logic:lessThan></td>
  </tr>
  <vegbank:denorm update="dnrm-disturbanceobs-5101-embargo" />
  <tr>
    <td>dnrm-disturbanceobs-5101-embargo</td>
    <td class="numeric"><bean:write name="dnrm-disturbanceobs-5101-embargo-RESULTS" ignore="true"/></td>
    <td><logic:lessThan value="0" name="dnrm-disturbanceobs-5101-embargo-RESULTS"><span class="error">ERROR!  please check the log</span></logic:lessThan></td>
  </tr>
  <vegbank:denorm update="dnrm-soilobs-5401-embargo" />
  <tr>
    <td>dnrm-soilobs-5401-embargo</td>
    <td class="numeric"><bean:write name="dnrm-soilobs-5401-embargo-RESULTS" ignore="true"/></td>
    <td><logic:lessThan value="0" name="dnrm-soilobs-5401-embargo-RESULTS"><span class="error">ERROR!  please check the log</span></logic:lessThan></td>
  </tr>
  <vegbank:denorm update="dnrm-commclass-5701-embargo" />
  <tr>
    <td>dnrm-commclass-5701-embargo</td>
    <td class="numeric"><bean:write name="dnrm-commclass-5701-embargo-RESULTS" ignore="true"/></td>
    <td><logic:lessThan value="0" name="dnrm-commclass-5701-embargo-RESULTS"><span class="error">ERROR!  please check the log</span></logic:lessThan></td>
  </tr>
  <vegbank:denorm update="dnrm-classcontributor-6001-embargo" />
  <tr>
    <td>dnrm-classcontributor-6001-embargo</td>
    <td class="numeric"><bean:write name="dnrm-classcontributor-6001-embargo-RESULTS" ignore="true"/></td>
    <td><logic:lessThan value="0" name="dnrm-classcontributor-6001-embargo-RESULTS"><span class="error">ERROR!  please check the log</span></logic:lessThan></td>
  </tr>
  <vegbank:denorm update="dnrm-comminterpretation-6301-embargo" />
  <tr>
    <td>dnrm-comminterpretation-6301-embargo</td>
    <td class="numeric"><bean:write name="dnrm-comminterpretation-6301-embargo-RESULTS" ignore="true"/></td>
    <td><logic:lessThan value="0" name="dnrm-comminterpretation-6301-embargo-RESULTS"><span class="error">ERROR!  please check the log</span></logic:lessThan></td>
  </tr>
  <vegbank:denorm update="dnrm-taxonobservation-6601-embargo" />
  <tr>
    <td>dnrm-taxonobservation-6601-embargo</td>
    <td class="numeric"><bean:write name="dnrm-taxonobservation-6601-embargo-RESULTS" ignore="true"/></td>
    <td><logic:lessThan value="0" name="dnrm-taxonobservation-6601-embargo-RESULTS"><span class="error">ERROR!  please check the log</span></logic:lessThan></td>
  </tr>
  <vegbank:denorm update="dnrm-taxonimportance-6901-embargo" />
  <tr>
    <td>dnrm-taxonimportance-6901-embargo</td>
    <td class="numeric"><bean:write name="dnrm-taxonimportance-6901-embargo-RESULTS" ignore="true"/></td>
    <td><logic:lessThan value="0" name="dnrm-taxonimportance-6901-embargo-RESULTS"><span class="error">ERROR!  please check the log</span></logic:lessThan></td>
  </tr>
  
  <vegbank:denorm update="dnrm-taxoninterpretation-7801-embargo" />
  <tr>
    <td>dnrm-taxoninterpretation-7801-embargo</td>
    <td class="numeric"><bean:write name="dnrm-taxoninterpretation-7801-embargo-RESULTS" ignore="true"/></td>
    <td><logic:lessThan value="0" name="dnrm-taxoninterpretation-7801-embargo-RESULTS"><span class="error">ERROR!  please check the log</span></logic:lessThan></td>
  </tr>
  <vegbank:denorm update="dnrm-taxonalt-8101-embargo" />
  <tr>
    <td>dnrm-taxonalt-8101-embargo</td>
    <td class="numeric"><bean:write name="dnrm-taxonalt-8101-embargo-RESULTS" ignore="true"/></td>
    <td><logic:lessThan value="0" name="dnrm-taxonalt-8101-embargo-RESULTS"><span class="error">ERROR!  please check the log</span></logic:lessThan></td>
</tr>
</logic:present>

<logic:notPresent parameter="emb_only">
 <logic:notPresent parameter="latlong_only">
  <vegbank:denorm update="dnrm-comminterpretation-2401-commname" />
  <tr>
    <td>dnrm-comminterpretation-2401-commname</td>
    <td class="numeric"><bean:write name="dnrm-comminterpretation-2401-commname-RESULTS" ignore="true"/></td>
    <td><logic:lessThan value="0" name="dnrm-comminterpretation-2401-commname-RESULTS"><span class="error">ERROR!  please check the log</span></logic:lessThan></td>
  </tr>
  <vegbank:denorm update="dnrm-plot-2701-country" />
  <tr>
    <td>dnrm-plot-2701-country</td>
    <td class="numeric"><bean:write name="dnrm-plot-2701-country-RESULTS" ignore="true"/></td>
    <td><logic:lessThan value="0" name="dnrm-plot-2701-country-RESULTS"><span class="error">ERROR!  please check the log</span></logic:lessThan></td>
  </tr>
  <vegbank:denorm update="dnrm-plot-3001-state" />
  <tr>
    <td>dnrm-plot-3001-state</td>
    <td class="numeric"><bean:write name="dnrm-plot-3001-state-RESULTS" ignore="true"/></td>
    <td><logic:lessThan value="0" name="dnrm-plot-3001-state-RESULTS"><span class="error">ERROR!  please check the log</span></logic:lessThan></td>
  </tr>
  <vegbank:denorm update="dnrm-stratum-3301-stratummethod" />
  <tr>
    <td>dnrm-stratum-3301-stratummethod</td>
    <td class="numeric"><bean:write name="dnrm-stratum-3301-stratummethod-RESULTS" ignore="true"/></td>
    <td><logic:lessThan value="0" name="dnrm-stratum-3301-stratummethod-RESULTS"><span class="error">ERROR!  please check the log</span></logic:lessThan></td>
  </tr>
  <vegbank:denorm update="dnrm-stratum-3601-stratumname" />
  <tr>
    <td>dnrm-stratum-3601-stratumname</td>
    <td class="numeric"><bean:write name="dnrm-stratum-3601-stratumname-RESULTS" ignore="true"/></td>
    <td><logic:lessThan value="0" name="dnrm-stratum-3601-stratumname-RESULTS"><span class="error">ERROR!  please check the log</span></logic:lessThan></td>
  </tr>
  <vegbank:denorm update="dnrm-taxonimportance-3901-stratumheight" />
  <tr>
    <td>dnrm-taxonimportance-3901-stratumheight</td>
    <td class="numeric"><bean:write name="dnrm-taxonimportance-3901-stratumheight-RESULTS" ignore="true"/></td>
    <td><logic:lessThan value="0" name="dnrm-taxonimportance-3901-stratumheight-RESULTS"><span class="error">ERROR!  please check the log</span></logic:lessThan></td>
  </tr>
  <vegbank:denorm update="dnrm-taxonimportance-4201-stratumbase" />
  <tr>
    <td>dnrm-taxonimportance-4201-stratumbase</td>
    <td class="numeric"><bean:write name="dnrm-taxonimportance-4201-stratumbase-RESULTS" ignore="true"/></td>
    <td><logic:lessThan value="0" name="dnrm-taxonimportance-4201-stratumbase-RESULTS"><span class="error">ERROR!  please check the log</span></logic:lessThan></td>
</tr>





<vegbank:denorm update="dnrm-plantconcept-8401-obscount" />
<tr>
  <td>dnrm-plantconcept-8401-obscount</td>
  <td class="numeric"><bean:write name="dnrm-plantconcept-8401-obscount-RESULTS" ignore="true"/></td>
  <td><logic:lessThan value="0" name="dnrm-plantconcept-8401-obscount-RESULTS"><span class="error">ERROR!  please check the log</span></logic:lessThan></td>
</tr>
<vegbank:denorm update="dnrm-commconcept-8701-obscount" />
<tr>
  <td>dnrm-commconcept-8701-obscount</td>
  <td class="numeric"><bean:write name="dnrm-commconcept-8701-obscount-RESULTS" ignore="true"/></td>
  <td><logic:lessThan value="0" name="dnrm-commconcept-8701-obscount-RESULTS"><span class="error">ERROR!  please check the log</span></logic:lessThan></td>
</tr>
<vegbank:denorm update="dnrm-project-9001-obscount" />
<tr>
  <td>dnrm-project-9001-obscount</td>
  <td class="numeric"><bean:write name="dnrm-project-9001-obscount-RESULTS" ignore="true"/></td>
  <td><logic:lessThan value="0" name="dnrm-project-9001-obscount-RESULTS"><span class="error">ERROR!  please check the log</span></logic:lessThan></td>
</tr>
<vegbank:denorm update="dnrm-party-9301-obscount" />
<tr>
  <td>dnrm-party-9301-obscount</td>
  <td class="numeric"><bean:write name="dnrm-party-9301-obscount-RESULTS" ignore="true"/></td>
  <td><logic:lessThan value="0" name="dnrm-party-9301-obscount-RESULTS"><span class="error">ERROR!  please check the log</span></logic:lessThan></td>
</tr>
<vegbank:denorm update="dnrm-namedplace-9601-obscount" />
<tr>
  <td>dnrm-namedplace-9601-obscount</td>
  <td class="numeric"><bean:write name="dnrm-namedplace-9601-obscount-RESULTS" ignore="true"/></td>
  <td><logic:lessThan value="0" name="dnrm-namedplace-9601-obscount-RESULTS"><span class="error">ERROR!  please check the log</span></logic:lessThan></td>
</tr>
<tr><td colspan="3"><h4>cover codes</h4> </td></tr>
<vegbank:denorm update="dnrm-taxonimportance-9901-covercode_allnull" />
<tr>
  <td>dnrm-taxonimportance-9901-covercode_allnull</td>
  <td class="numeric"><bean:write name="dnrm-taxonimportance-9901-covercode_allnull-RESULTS" ignore="true"/></td>
  <td><logic:lessThan value="0" name="dnrm-taxonimportance-9901-covercode_allnull-RESULTS"><span class="error">ERROR!  please check the log</span></logic:lessThan></td>
</tr>
<vegbank:denorm update="dnrm-taxonimportance-10201-covercode_exact" />
<tr>
  <td>dnrm-taxonimportance-10201-covercode_exact</td>
  <td class="numeric"><bean:write name="dnrm-taxonimportance-10201-covercode_exact-RESULTS" ignore="true"/></td>
  <td><logic:lessThan value="0" name="dnrm-taxonimportance-10201-covercode_exact-RESULTS"><span class="error">ERROR!  please check the log</span></logic:lessThan></td>
</tr>
<vegbank:denorm update="dnrm-taxonimportance-10501-covercode_range" />
<tr>
  <td>dnrm-taxonimportance-10501-covercode_range</td>
  <td class="numeric"><bean:write name="dnrm-taxonimportance-10501-covercode_range-RESULTS" ignore="true"/></td>
  <td><logic:lessThan value="0" name="dnrm-taxonimportance-10501-covercode_range-RESULTS"><span class="error">ERROR!  please check the log</span></logic:lessThan></td>
</tr>
<tr><td colspan="3"><h4>For exporing text files</h4></td></tr>
<vegbank:denorm update="dnrm-taxonobservation-10801-origpcid" />
<tr>
  <td>dnrm-taxonobservation-10801-origpcid</td>
  <td class="numeric"><bean:write name="dnrm-taxonobservation-10801-origpcid-RESULTS" ignore="true"/></td>
  <td><logic:lessThan value="0" name="dnrm-taxonobservation-10801-origpcid-RESULTS"><span class="error">ERROR!  please check the log</span></logic:lessThan></td>
</tr>
<vegbank:denorm update="dnrm-taxonobservation-11101-origsna" />
<tr>
  <td>dnrm-taxonobservation-11101-origsna</td>
  <td class="numeric"><bean:write name="dnrm-taxonobservation-11101-origsna-RESULTS" ignore="true"/></td>
  <td><logic:lessThan value="0" name="dnrm-taxonobservation-11101-origsna-RESULTS"><span class="error">ERROR!  please check the log</span></logic:lessThan></td>
</tr>
<vegbank:denorm update="dnrm-taxonobservation-11401-origsn" />
<tr>
  <td>dnrm-taxonobservation-11401-origsn</td>
  <td class="numeric"><bean:write name="dnrm-taxonobservation-11401-origsn-RESULTS" ignore="true"/></td>
  <td><logic:lessThan value="0" name="dnrm-taxonobservation-11401-origsn-RESULTS"><span class="error">ERROR!  please check the log</span></logic:lessThan></td>
</tr>
<vegbank:denorm update="dnrm-taxonobservation-11701-origcode" />
<tr>
  <td>dnrm-taxonobservation-11701-origcode</td>
  <td class="numeric"><bean:write name="dnrm-taxonobservation-11701-origcode-RESULTS" ignore="true"/></td>
  <td><logic:lessThan value="0" name="dnrm-taxonobservation-11701-origcode-RESULTS"><span class="error">ERROR!  please check the log</span></logic:lessThan></td>
</tr>
<vegbank:denorm update="dnrm-taxonobservation-12001-origcommon" />
<tr>
  <td>dnrm-taxonobservation-12001-origcommon</td>
  <td class="numeric"><bean:write name="dnrm-taxonobservation-12001-origcommon-RESULTS" ignore="true"/></td>
  <td><logic:lessThan value="0" name="dnrm-taxonobservation-12001-origcommon-RESULTS"><span class="error">ERROR!  please check the log</span></logic:lessThan></td>
</tr>
<vegbank:denorm update="dnrm-taxonobservation-12301-currpcid" />
<tr>
  <td>dnrm-taxonobservation-12301-currpcid</td>
  <td class="numeric"><bean:write name="dnrm-taxonobservation-12301-currpcid-RESULTS" ignore="true"/></td>
  <td><logic:lessThan value="0" name="dnrm-taxonobservation-12301-currpcid-RESULTS"><span class="error">ERROR!  please check the log</span></logic:lessThan></td>
</tr>
<vegbank:denorm update="dnrm-taxonobservation-12601-currsna" />
<tr>
  <td>dnrm-taxonobservation-12601-currsna</td>
  <td class="numeric"><bean:write name="dnrm-taxonobservation-12601-currsna-RESULTS" ignore="true"/></td>
  <td><logic:lessThan value="0" name="dnrm-taxonobservation-12601-currsna-RESULTS"><span class="error">ERROR!  please check the log</span></logic:lessThan></td>
</tr>
<vegbank:denorm update="dnrm-taxonobservation-12901-currsn" />
<tr>
  <td>dnrm-taxonobservation-12901-currsn</td>
  <td class="numeric"><bean:write name="dnrm-taxonobservation-12901-currsn-RESULTS" ignore="true"/></td>
  <td><logic:lessThan value="0" name="dnrm-taxonobservation-12901-currsn-RESULTS"><span class="error">ERROR!  please check the log</span></logic:lessThan></td>
</tr>
<vegbank:denorm update="dnrm-taxonobservation-13201-currcode" />
<tr>
  <td>dnrm-taxonobservation-13201-currcode</td>
  <td class="numeric"><bean:write name="dnrm-taxonobservation-13201-currcode-RESULTS" ignore="true"/></td>
  <td><logic:lessThan value="0" name="dnrm-taxonobservation-13201-currcode-RESULTS"><span class="error">ERROR!  please check the log</span></logic:lessThan></td>
</tr>
<vegbank:denorm update="dnrm-taxonobservation-13501-currcommon" />
<tr>
  <td>dnrm-taxonobservation-13501-currcommon</td>
  <td class="numeric"><bean:write name="dnrm-taxonobservation-13501-currcommon-RESULTS" ignore="true"/></td>
  <td><logic:lessThan value="0" name="dnrm-taxonobservation-13501-currcommon-RESULTS"><span class="error">ERROR!  please check the log</span></logic:lessThan></td>
</tr>
<vegbank:denorm update="dnrm-observation-14101-commint_1" />
<tr>
  <td>dnrm-observation-14101-commint_1</td>
  <td class="numeric"><bean:write name="dnrm-observation-14101-commint_1-RESULTS" ignore="true"/></td>
  <td><logic:lessThan value="0" name="dnrm-observation-14101-commint_1-RESULTS"><span class="error">ERROR!  please check the log</span></logic:lessThan></td>
</tr>
<vegbank:denorm update="dnrm-observation-14401-commint_2" />
<tr>
  <td>dnrm-observation-14401-commint_2</td>
  <td class="numeric"><bean:write name="dnrm-observation-14401-commint_2-RESULTS" ignore="true"/></td>
  <td><logic:lessThan value="0" name="dnrm-observation-14401-commint_2-RESULTS"><span class="error">ERROR!  please check the log</span></logic:lessThan></td>
</tr>
<vegbank:denorm update="dnrm-observation-14701-commint_3" />
<tr>
  <td>dnrm-observation-14701-commint_3</td>
  <td class="numeric"><bean:write name="dnrm-observation-14701-commint_3-RESULTS" ignore="true"/></td>
  <td><logic:lessThan value="0" name="dnrm-observation-14701-commint_3-RESULTS"><span class="error">ERROR!  please check the log</span></logic:lessThan></td>
</tr>
<vegbank:denorm update="dnrm-observation-15001-commint_4" />
<tr>
  <td>dnrm-observation-15001-commint_4</td>
  <td class="numeric"><bean:write name="dnrm-observation-15001-commint_4-RESULTS" ignore="true"/></td>
  <td><logic:lessThan value="0" name="dnrm-observation-15001-commint_4-RESULTS"><span class="error">ERROR!  please check the log</span></logic:lessThan></td>
</tr>
<vegbank:denorm update="dnrm-observation-15301-commint_5" />
<tr>
  <td>dnrm-observation-15301-commint_5</td>
  <td class="numeric"><bean:write name="dnrm-observation-15301-commint_5-RESULTS" ignore="true"/></td>
  <td><logic:lessThan value="0" name="dnrm-observation-15301-commint_5-RESULTS"><span class="error">ERROR!  please check the log</span></logic:lessThan></td>
</tr>
<vegbank:denorm update="dnrm-observation-15601-commint_6" />
<tr>
  <td>dnrm-observation-15601-commint_6</td>
  <td class="numeric"><bean:write name="dnrm-observation-15601-commint_6-RESULTS" ignore="true"/></td>
  <td><logic:lessThan value="0" name="dnrm-observation-15601-commint_6-RESULTS"><span class="error">ERROR!  please check the log</span></logic:lessThan></td>
</tr>
<vegbank:denorm update="dnrm-observation-15901-commint_7" />
<tr>
  <td>dnrm-observation-15901-commint_7</td>
  <td class="numeric"><bean:write name="dnrm-observation-15901-commint_7-RESULTS" ignore="true"/></td>
  <td><logic:lessThan value="0" name="dnrm-observation-15901-commint_7-RESULTS"><span class="error">ERROR!  please check the log</span></logic:lessThan></td>
</tr>
<vegbank:denorm update="dnrm-observation-16201-commint_8" />
<tr>
  <td>dnrm-observation-16201-commint_8</td>
  <td class="numeric"><bean:write name="dnrm-observation-16201-commint_8-RESULTS" ignore="true"/></td>
  <td><logic:lessThan value="0" name="dnrm-observation-16201-commint_8-RESULTS"><span class="error">ERROR!  please check the log</span></logic:lessThan></td>
</tr>
<vegbank:denorm update="dnrm-observation-16501-commint_9" />
<tr>
  <td>dnrm-observation-16501-commint_9</td>
  <td class="numeric"><bean:write name="dnrm-observation-16501-commint_9-RESULTS" ignore="true"/></td>
  <td><logic:lessThan value="0" name="dnrm-observation-16501-commint_9-RESULTS"><span class="error">ERROR!  please check the log</span></logic:lessThan></td>
</tr>
<vegbank:denorm update="dnrm-observation-16801-commint_10" />
<tr>
  <td>dnrm-observation-16801-commint_10</td>
  <td class="numeric"><bean:write name="dnrm-observation-16801-commint_10-RESULTS" ignore="true"/></td>
  <td><logic:lessThan value="0" name="dnrm-observation-16801-commint_10-RESULTS"><span class="error">ERROR!  please check the log</span></logic:lessThan></td>
</tr>
<vegbank:denorm update="dnrm-observation-17101-commint_11" />
<tr>
  <td>dnrm-observation-17101-commint_11</td>
  <td class="numeric"><bean:write name="dnrm-observation-17101-commint_11-RESULTS" ignore="true"/></td>
  <td><logic:lessThan value="0" name="dnrm-observation-17101-commint_11-RESULTS"><span class="error">ERROR!  please check the log</span></logic:lessThan></td>
</tr>
<vegbank:denorm update="dnrm-observation-17401-commint_12" />
<tr>
  <td>dnrm-observation-17401-commint_12</td>
  <td class="numeric"><bean:write name="dnrm-observation-17401-commint_12-RESULTS" ignore="true"/></td>
  <td><logic:lessThan value="0" name="dnrm-observation-17401-commint_12-RESULTS"><span class="error">ERROR!  please check the log</span></logic:lessThan></td>
</tr>
<vegbank:denorm update="dnrm-observation-17701-commint_13" />
<tr>
  <td>dnrm-observation-17701-commint_13</td>
  <td class="numeric"><bean:write name="dnrm-observation-17701-commint_13-RESULTS" ignore="true"/></td>
  <td><logic:lessThan value="0" name="dnrm-observation-17701-commint_13-RESULTS"><span class="error">ERROR!  please check the log</span></logic:lessThan></td>
</tr>
<vegbank:denorm update="dnrm-observation-18001-commint_14" />
<tr>
  <td>dnrm-observation-18001-commint_14</td>
  <td class="numeric"><bean:write name="dnrm-observation-18001-commint_14-RESULTS" ignore="true"/></td>
  <td><logic:lessThan value="0" name="dnrm-observation-18001-commint_14-RESULTS"><span class="error">ERROR!  please check the log</span></logic:lessThan></td>
</tr>
<vegbank:denorm update="dnrm-observation-18301-commint_15" />
<tr>
  <td>dnrm-observation-18301-commint_15</td>
  <td class="numeric"><bean:write name="dnrm-observation-18301-commint_15-RESULTS" ignore="true"/></td>
  <td><logic:lessThan value="0" name="dnrm-observation-18301-commint_15-RESULTS"><span class="error">ERROR!  please check the log</span></logic:lessThan></td>
</tr>
<vegbank:denorm update="dnrm-observation-18601-commint_16" />
<tr>
  <td>dnrm-observation-18601-commint_16</td>
  <td class="numeric"><bean:write name="dnrm-observation-18601-commint_16-RESULTS" ignore="true"/></td>
  <td><logic:lessThan value="0" name="dnrm-observation-18601-commint_16-RESULTS"><span class="error">ERROR!  please check the log</span></logic:lessThan></td>
</tr>
<vegbank:denorm update="dnrm-observation-18901-commint_17" />
<tr>
  <td>dnrm-observation-18901-commint_17</td>
  <td class="numeric"><bean:write name="dnrm-observation-18901-commint_17-RESULTS" ignore="true"/></td>
  <td><logic:lessThan value="0" name="dnrm-observation-18901-commint_17-RESULTS"><span class="error">ERROR!  please check the log</span></logic:lessThan></td>
</tr>
<vegbank:denorm update="dnrm-observation-19201-commint_18" />
<tr>
  <td>dnrm-observation-19201-commint_18</td>
  <td class="numeric"><bean:write name="dnrm-observation-19201-commint_18-RESULTS" ignore="true"/></td>
  <td><logic:lessThan value="0" name="dnrm-observation-19201-commint_18-RESULTS"><span class="error">ERROR!  please check the log</span></logic:lessThan></td>
</tr>
<vegbank:denorm update="dnrm-observation-19501-commint_19" />
<tr>
  <td>dnrm-observation-19501-commint_19</td>
  <td class="numeric"><bean:write name="dnrm-observation-19501-commint_19-RESULTS" ignore="true"/></td>
  <td><logic:lessThan value="0" name="dnrm-observation-19501-commint_19-RESULTS"><span class="error">ERROR!  please check the log</span></logic:lessThan></td>
</tr>
<vegbank:denorm update="dnrm-observation-19801-commint_20" />
<tr>
  <td>dnrm-observation-19801-commint_20</td>
  <td class="numeric"><bean:write name="dnrm-observation-19801-commint_20-RESULTS" ignore="true"/></td>
  <td><logic:lessThan value="0" name="dnrm-observation-19801-commint_20-RESULTS"><span class="error">ERROR!  please check the log</span></logic:lessThan></td>
</tr>
<vegbank:denorm update="dnrm-observation-20101-commint_21" />
<tr>
  <td>dnrm-observation-20101-commint_21</td>
  <td class="numeric"><bean:write name="dnrm-observation-20101-commint_21-RESULTS" ignore="true"/></td>
  <td><logic:lessThan value="0" name="dnrm-observation-20101-commint_21-RESULTS"><span class="error">ERROR!  please check the log</span></logic:lessThan></td>
</tr>

<tr><td colspan="3">TOP TAXA for keyword gen:</td></tr>
<vegbank:denorm update="dnrm-observation-20401-toptaxon2" />
<tr>
  <td>dnrm-observation-20401-toptaxon2</td>
  <td class="numeric"><bean:write name="dnrm-observation-20401-toptaxon2-RESULTS" ignore="true"/></td>
  <td><logic:lessThan value="0" name="dnrm-observation-20401-toptaxon2-RESULTS"><span class="error">ERROR!  please check the log</span></logic:lessThan></td>
</tr>
<vegbank:denorm update="dnrm-observation-20701-toptaxon3" />
<tr>
  <td>dnrm-observation-20701-toptaxon3</td>
  <td class="numeric"><bean:write name="dnrm-observation-20701-toptaxon3-RESULTS" ignore="true"/></td>
  <td><logic:lessThan value="0" name="dnrm-observation-20701-toptaxon3-RESULTS"><span class="error">ERROR!  please check the log</span></logic:lessThan></td>
</tr>
<vegbank:denorm update="dnrm-observation-21001-toptaxon4" />
<tr>
  <td>dnrm-observation-21001-toptaxon4</td>
  <td class="numeric"><bean:write name="dnrm-observation-21001-toptaxon4-RESULTS" ignore="true"/></td>
  <td><logic:lessThan value="0" name="dnrm-observation-21001-toptaxon4-RESULTS"><span class="error">ERROR!  please check the log</span></logic:lessThan></td>
</tr>
<vegbank:denorm update="dnrm-observation-21301-toptaxon5" />
<tr>
  <td>dnrm-observation-21301-toptaxon5</td>
  <td class="numeric"><bean:write name="dnrm-observation-21301-toptaxon5-RESULTS" ignore="true"/></td>
  <td><logic:lessThan value="0" name="dnrm-observation-21301-toptaxon5-RESULTS"><span class="error">ERROR!  please check the log</span></logic:lessThan></td>
</tr>
<vegbank:denorm update="dnrm-observation-21601-toptaxon1" />
<tr>
  <td>dnrm-observation-21601-toptaxon1</td>
  <td class="numeric"><bean:write name="dnrm-observation-21601-toptaxon1-RESULTS" ignore="true"/></td>
  <td><logic:lessThan value="0" name="dnrm-observation-21601-toptaxon1-RESULTS"><span class="error">ERROR!  please check the log</span></logic:lessThan></td>
</tr>
 </logic:notPresent>
</logic:notPresent>
</table>
