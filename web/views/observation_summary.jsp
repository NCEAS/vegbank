@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@
  @ajax_js_include@
  @datacart_js_include@

  
   <%
                       //**************************************************************************************
                       //  Set up alternating row colors
                       //**************************************************************************************
                       String rowClass = "evenrow";
     int inttemp=0 ; 
     String howManyPlantsComplex="3"; // this many plants if complex query
     String howManyPlantsDefault="5"; // this many plants if default query (overrides above)
    %>



<title>VegBank Plot Query Results</title>



@webpage_masthead_html@
<!-- If nothing is set to be shown, then show some things anyway. -->
<bean:define id="hasshowparams" value="false" />
<logic:present parameter="show_statecountry"><bean:define id="show_statecountry" value="show" /><bean:define id="hasshowparams" value="true" /></logic:present>
<logic:present parameter="show_elev"><bean:define id="show_elev" value="show" /><bean:define id="hasshowparams" value="true" /></logic:present>
<logic:present parameter="show_aspect"><bean:define id="show_aspect" value="show" /><bean:define id="hasshowparams" value="true" /></logic:present>
<logic:present parameter="show_slope"><bean:define id="show_slope" value="show" /><bean:define id="hasshowparams" value="true" /></logic:present>
<logic:present parameter="show_rocktype"><bean:define id="show_rocktype" value="show" /><bean:define id="hasshowparams" value="true" /></logic:present>
<logic:present parameter="show_surficial"><bean:define id="show_surficial" value="show" /><bean:define id="hasshowparams" value="true" /></logic:present>
<logic:present parameter="show_hydrologic"><bean:define id="show_hydrologic" value="show" /><bean:define id="hasshowparams" value="true" /></logic:present>
<logic:present parameter="show_topo"><bean:define id="show_topo" value="show" /><bean:define id="hasshowparams" value="true" /></logic:present>
<logic:present parameter="show_landform"><bean:define id="show_landform" value="show" /><bean:define id="hasshowparams" value="true" /></logic:present>
<logic:present parameter="show_datesampled"><bean:define id="show_datesampled" value="show" /><bean:define id="hasshowparams" value="true" /></logic:present>
<logic:present parameter="show_dateentered"><bean:define id="show_dateentered" value="show" /><bean:define id="hasshowparams" value="true" /></logic:present>
<logic:present parameter="show_area"><bean:define id="show_area" value="show" /><bean:define id="hasshowparams" value="true" /></logic:present>
<logic:present parameter="show_covermeth"><bean:define id="show_covermeth" value="show" /><bean:define id="hasshowparams" value="true" /></logic:present>
<logic:present parameter="show_stratummeth"><bean:define id="show_stratummeth" value="show" /><bean:define id="hasshowparams" value="true" /></logic:present>
<logic:present parameter="show_project"><bean:define id="show_project" value="show" /><bean:define id="hasshowparams" value="true" /></logic:present>
<logic:present parameter="show_plants"><bean:define id="show_plants" value="show" /><bean:define id="hasshowparams" value="true" /></logic:present>
<logic:present parameter="show_comms"><bean:define id="show_comms" value="show" /><bean:define id="hasshowparams" value="true" /></logic:present>
<logic:equal name="hasshowparams" value="false">
  <!-- set default to show here : -->
  <!-- didn't have any params, so defining the defaults here -->
  <bean:define id="show_plants" value="show" />
  <bean:define id="show_comms" value="show" />
  <bean:define id="show_statecountry" value="show" />
  <% howManyPlantsComplex =  howManyPlantsDefault ; %>
  
  
</logic:equal>


 @possibly_center@  
  <h2>VegBank Plots</h2>
  <div id="tut_plotcriteriamessages">
  <!-- special case for linking from NatureServeExplorer : -->
<logic:equal parameter="where" value="where_observation_nsuid">
  <p>Welcome to VegBank.  You've clicked a link from <a href="http://natureserve.org/explorer">NatureServe Explorer</a>
  requesting to see the plots in VegBank that our users have linked to a particular community, which is: <br/>

    <vegbank:get id="commconcept" select="commconcept" where="where_commconcept_nsuid" 
     beanName="map" pager="false" xwhereEnable="false" perPage="-1"/>

    <logic:notEmpty name="commconcept-BEANLIST">
      <logic:iterate id="onerowofcommconcept" name="commconcept-BEANLIST">
        <a href='@get_link@std/commconcept/<bean:write name="onerowofcommconcept" property="commconcept_id" />'><bean:write name="onerowofcommconcept" property="commname" /></a>
      </logic:iterate>
    </logic:notEmpty>
    <logic:empty name="commconcept-BEANLIST">
      Sorry, VegBank has no record of the NatureServe community requested: 
      <bean:parameter id="beanwparam" name="wparam" />
      <bean:write name="beanwparam" />
    </logic:empty>
  </p>
</logic:equal>

<!-- tell the user what was searched for, if criteriaAsText is passed here : -->
<logic:present parameter="criteriaAsText">
  <bean:parameter id="bean_criteriaAsText" name="criteriaAsText" />
  <logic:notEmpty name="bean_criteriaAsText">
    <p class="psmall">You searched for plots: <bean:write name="bean_criteriaAsText" /></p>


  </logic:notEmpty>
</logic:present>  
</div>


<vegbank:get id="plotobs" select="plotandobservation" whereNumeric="where_observation_pk" 
    whereNonNumeric="where_observation_ac" beanName="map" pager="true" xwhereEnable="true" 
    allowOrderBy="true" />  <!-- save="plot-search-results" /-->


<vegbank:pager />
<div style="clear:both; display: block;">
                                                                                                                                                                                                  
   <table class="noborder">
    <tr>
    <!-- add all results -->
    <td>
        <a href="javascript:addAllResults('observation')"
            title="add all results to datacart" class="nobg"><img src="/vegbank/images/cart_star_on_blue2.gif" border="0" /></a>
    </td>
    <td>
        <a href="<vegbank:changeParam paramName='delta' paramValue='findadd-observationaccessioncode' absolute='true' />&lr=true&showQuery=true"
            title="add all results to datacart">add all plot query results</a> to datacart, &nbsp; &nbsp;
    </td>
                                                                                                                                                                                                  
    <!-- add page -->
    <td>
        <a href="javascript:addAllOnPage()" title="add all on page to datacart" class="nobg"><img src="/vegbank/images/cart_add_one.gif" border="0" /></a>
    </td>
    <td>
        <a href="javascript:addAllOnPage()" title="add all on page">add plots on page</a> to datacart, &nbsp; &nbsp;
    </td>
                                                                                                                                                                                                  
    <!-- drop page -->
    <td>
        <a href="javascript:dropAllOnPage()" title="drop all on page from datacart" class="nobg"><img src="/vegbank/images/cart_drop_one.gif" border="0" /></a>
    </td>
    <td>
        <a href="javascript:dropAllOnPage()" title="drop all on page">drop plots on page</a> from datacart
    </td>
    </tr>
                                                                                                                                                                                                  
  </table>
</div>


<%@ include file="../includes/setup_rowindex.jsp" %>

<logic:empty name="plotobs-BEANLIST">
                Sorry, no plot-observations are available.
          </logic:empty>
<logic:notEmpty name="plotobs-BEANLIST"><!-- set up table -->

    <logic:equal parameter="delta" value="findadd-observationaccessioncode">
        <vegbank:datacart delta="findadd:observation:observation:observation_id:observationaccessioncode" deltaItems="getQuery" display="false" />
    </logic:equal>


<form action="" method="GET" id="cartable">
<TABLE width="100%" class="leftrightborders" cellpadding="2">
<tr>
<th valign="bottom" align="center" nowrap="nowrap">Add/Drop</th>

<th valign="bottom" align="center">
  <table class="horizborders">
    <tr><td class="horizborders">Author Code</td></tr>
    <!-- the following is a bit of a hack.  The class will be hidden, unless show_statecountry is something, in 
    this case "show", and the class "showhidden" isn't anything, so displays normally.  MTL May 6, 2005 -->
    <tr class='horizborders <bean:write name="show_statecountry" ignore="true" />hidden'>
      <td>Plot Location</td></tr>
  </table>
</th>
<th class='<bean:write name="show_elev" ignore="true" /><bean:write name="show_slope" ignore="true" /><bean:write name="show_aspect" ignore="true" />hidden' valign="bottom" align="center">
  <table class="horizborders">
    <tr><td class='horizborders <bean:write name="show_elev" ignore="true" />hidden'>Elev</td></tr>
    <tr><td class='horizborders <bean:write name="show_slope" ignore="true" />hidden'>Slope</td></tr>
    <tr><td class='horizborders <bean:write name="show_aspect" ignore="true" />hidden'>Aspect</td></tr>
  </table>
</th>

<th valign="bottom" align="center" class='<bean:write name="show_rocktype" ignore="true" /><bean:write name="show_surficial" ignore="true" /><bean:write name="show_hydrologic" ignore="true" /><bean:write name="show_topo" ignore="true" /><bean:write name="show_landform" ignore="true" />hidden'>
  <table class="horizborders">
    <tr><td class='horizborders <bean:write name="show_rocktype" ignore="true" />hidden'>Rock Type</td></tr>
    <tr><td class='horizborders <bean:write name="show_surficial" ignore="true" />hidden'>Surficial Deposits</td></tr>
    <tr><td class='horizborders <bean:write name="show_hydrologic" ignore="true" />hidden'>Hydrologic Regime</td></tr>
    <tr><td class='horizborders <bean:write name="show_topo" ignore="true" />hidden'>Topo Position</td></tr>
    <tr><td class='horizborders <bean:write name="show_landform" ignore="true" />hidden'>Landform</td></tr>
  </table>
</th>

<th valign="bottom" align="center" class='<bean:write name="show_datesampled" ignore="true" /><bean:write name="show_dateentered" ignore="true" /><bean:write name="show_area" ignore="true" />hidden'>
  <table class="horizborders">
    <tr><td class='<bean:write name="show_datesampled" ignore="true" />hidden'>Sampled</td></tr>
    <tr><td class='<bean:write name="show_dateentered" ignore="true" />hidden'>Entered</td></tr>
    <tr><td class='<bean:write name="show_area" ignore="true" />hidden'>Size</td></tr>
  </table>
</th>
<th valign="bottom" align="center" class='<bean:write name="show_covermeth" ignore="true" /><bean:write name="show_stratummeth" ignore="true" /><bean:write name="show_project" ignore="true" />hidden'>
  <table class="horizborders">
    <tr><td class='<bean:write name="show_covermeth" ignore="true" />hidden'>Cover Method</td></tr>
    <tr><td class='<bean:write name="show_stratummeth" ignore="true" />hidden'>Stratum Method</td></tr>
    <tr><td class='<bean:write name="show_project" ignore="true" />hidden'>Project</td></tr>
  </table>
</th>

<th valign="bottom" align="center" class='<bean:write name="show_plants" ignore="true" />hidden' nowrap="nowrap">
  <table class="horizborders">
    <tr><td>Plants Found on Plot</td></tr>
  </table>
</th>

<th valign="bottom" align="center" class='<bean:write name="show_comms" ignore="true" />hidden' width="30%">
  <table class="horizborders">
    <tr><td>Plot Communities</td></tr>
  </table>
</th>

</tr>

<logic:iterate id="onerowofobservation" name="BEANLIST">
<!-- iterate over all records in set : new table for each -->
<bean:define id="onerowofplot" name="onerowofobservation" />
<bean:define id="obsId" name="onerowofplot" property="observation_id"/>
<bean:define id="plot_pk" name="onerowofplot" property="plot_id"/>
<bean:define id="observation_pk" name="onerowofplot" property="observation_id"/>

<!-- start of plot & obs fields-->

<tr class='@nextcolorclass@' align="left">
<td align="center"><!-- that td cannot have a class, it gets overwritten -->
<bean:define id="delta_ac" name="onerowofobservation" property="observationaccessioncode" />
Plot #<%= rowIndex++ %>
<br/>
<%@ include file="../includes/datacart_checkbox.jsp" %>
</td>


<!-- plot data -->
<td class="largefield">
<strong><bean:write name="onerowofobservation" property="authorobscode" /></strong>
<br/>
<i>
<span class='<bean:write name="show_statecountry" ignore="true" />hidden'><bean:write name="onerowofobservation" property="stateprovince" />,<br/>
<bean:write name="onerowofobservation" property="country" /></span>
</i>
<br/>
&raquo; <a href='@get_link@comprehensive/observation/<bean:write name="onerowofobservation" property="observation_id" />'>plot detail</a>
</td>
<td class='largefield numeric <bean:write name="show_elev" ignore="true" /><bean:write name="show_slope" ignore="true" /><bean:write name="show_aspect" ignore="true" />hidden'>
<span class='<bean:write name="show_elev" ignore="true" />hidden'>E: <bean:write name="onerowofobservation" property="elevation" /><br/></span>
<span class='<bean:write name="show_slope" ignore="true" />hidden'>S: <bean:write name="onerowofobservation" property="slopegradient" /><br/></span>
<span class='<bean:write name="show_aspect" ignore="true" />hidden'>A: <bean:write name="onerowofobservation" property="slopeaspect" /></span>
</td>


<td class='largefield <bean:write name="show_rocktype" ignore="true" /><bean:write name="show_surficial" ignore="true" /><bean:write name="show_hydrologic" ignore="true" /><bean:write name="show_topo" ignore="true" /><bean:write name="show_landform" ignore="true" />hidden'>
<span class='<bean:write name="show_rocktype" ignore="true" />hidden'>R: <bean:write name="onerowofobservation" property="rocktype" /><br/></span>
<span class='<bean:write name="show_surficial" ignore="true" />hidden'>S: <bean:write name="onerowofobservation" property="surficialdeposits" /><br/></span>
<span class='<bean:write name="show_hydrologic" ignore="true" />hidden'>H: <bean:write name="onerowofobservation" property="hydrologicregime" /><br/></span>
<span class='<bean:write name="show_topo" ignore="true" />hidden'>T: <bean:write name="onerowofobservation" property="topoposition" /><br/></span>
<span class='<bean:write name="show_landform" ignore="true" />hidden'>L: <bean:write name="onerowofobservation" property="landform" /></span>
</td>

 
 
 
<td class='largefield numeric <bean:write name="show_datesampled" ignore="true" /><bean:write name="show_dateentered" ignore="true" /><bean:write name="show_area" ignore="true" />hidden'>
 <span title='<bean:write name="onerowofobservation" property="obsstartdate"/>' class='<bean:write name="show_datesampled" ignore="true" />hidden'>
   S: <dt:format pattern="MMM-yy">
       <dt:parse pattern="yyyy-MM-dd">
           <bean:write name="onerowofobservation" property="obsstartdate_datetrunc"/>
       </dt:parse>
   </dt:format> 
   <br/> 
 </span>
 
 <span title='<bean:write name="onerowofobservation" property="observationdateentered"/>' class='<bean:write name="show_dateentered" ignore="true" />hidden'>
  E:  <dt:format pattern="MMM-yy">
          <dt:parse pattern="yyyy-MM-dd">
              <bean:write name="onerowofobservation" property="observationdateentered_datetrunc"/>
          </dt:parse>
      </dt:format> 
   <br/>
 </span>
 <span class='<bean:write name="show_area" ignore="true" />hidden'>   
   Sz: <bean:write name="onerowofobservation" property="area" />
 </span>  
</td>

<!-- methods -->
<td nowrap="nowrap" class='largefield <bean:write name="show_covermeth" ignore="true" /><bean:write name="show_stratummeth" ignore="true" /><bean:write name="show_project" ignore="true" />hidden'>
   <!-- PLEASE trim strings to < 25 chars -->
    <bean:define id="cvr_trunc"><bean:write name="onerowofobservation" property="covermethod_id_transl" /></bean:define>
    <% if ( cvr_trunc.length() > 25 ) { cvr_trunc = cvr_trunc.substring(0,22) + "..." ; } %>
    
    <bean:define id="str_trunc"><bean:write name="onerowofobservation" property="stratummethod_id_transl" /></bean:define>
    <% if ( str_trunc.length() > 25 ) { str_trunc = str_trunc.substring(0,22) + "..." ; } %>
    
    <bean:define id="prj_trunc"><bean:write name="onerowofobservation" property="project_id_transl" /></bean:define>
    <% if ( prj_trunc.length() > 25 ) { prj_trunc = prj_trunc.substring(0,22) + "..." ; } %>
    
    
 <span class='<bean:write name="show_covermeth" ignore="true" />hidden'>   Cvr: <a href='@get_link@std/covermethod/<bean:write name="onerowofobservation" property="covermethod_id" />'><%= cvr_trunc %></a><br/></span>
 <span class='<bean:write name="show_stratummeth" ignore="true" />hidden'>   Str: <a href='@get_link@std/stratummethod/<bean:write name="onerowofobservation" property="stratummethod_id" />'><%= str_trunc %></a><br/></span>
 <span class='<bean:write name="show_project" ignore="true" />hidden'>   Prj: <a href='@get_link@std/project/<bean:write name="onerowofobservation" property="project_id" />'><%= prj_trunc %></a></span>
</td>
<!-- plants in this plot -->
 <td class='largefield <bean:write name="show_plants" ignore="true" />hidden'>
<vegbank:get id="taxonobservation" select="taxonobservation_maxcover" where="where_taxonobservation_maxcover" wparam="observation_pk"
  pager="false" perPage="-1" beanName="map" />
  <logic:empty name="taxonobservation-BEANLIST">
   No data
  </logic:empty>
  <logic:notEmpty name="taxonobservation-BEANLIST">
    
     
         <% inttemp = 0 ;%>
       
         <!-- iterate over all records in set : new table for each -->
         <logic:iterate length="<%= howManyPlantsComplex %>" id="onerowoftaxonobservation" name="taxonobservation-BEANLIST">
		   <% if (inttemp!=0) { %> <br /> <% }  %>
		   
		    <%  inttemp ++ ;  %>
		        &raquo; <a href='@get_link@std/taxonobservation/<bean:write name="onerowoftaxonobservation" property="taxonobservation_id" />'>
				  <bean:write name="onerowoftaxonobservation" property="authorplantname" />
				</a>
					<logic:notEmpty name="onerowoftaxonobservation" property="maxcover">
					  (<bean:write name="onerowoftaxonobservation" property="maxcover" />%)
					</logic:notEmpty>
         </logic:iterate><!-- tax obs -->
      
    
  </logic:notEmpty><!-- concept -->
</td>
<td class='largefield <bean:write name="show_comms" ignore="true" />hidden'>
<!-- community info -->
<vegbank:get id="comminterpretation" select="comminterpretation_withobs" beanName="map" 
  where="where_observation_pk" wparam="obsId" perPage="-1" pager="false"/>
  <logic:empty name="comminterpretation-BEANLIST">
    No data
  </logic:empty>
  <logic:notEmpty name="comminterpretation-BEANLIST">
    
      
        <logic:iterate length="3" id="onerowofcomminterpretation" name="comminterpretation-BEANLIST"><!-- iterate over all records in set : new table for each -->
          <logic:notEmpty name="onerowofcomminterpretation" property="commconcept_id">
            
            &raquo; <a href='@get_link@std/commclass/<bean:write name="onerowofcomminterpretation" property="commclass_id" />'><bean:write name="onerowofcomminterpretation" property="commconcept_id_transl" /></a>
            
          <br/></logic:notEmpty><!-- concept -->
        </logic:iterate>
      
   
  </logic:notEmpty>
 </td>
</tr>
</logic:iterate><!-- plot -->
</TABLE>
</form>
@mark_datacart_items@

<vegbank:pager />


</logic:notEmpty>

<br />
@webpage_footer_html@


