@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@

<bean:define id="searchType" value="Advanced" />
<bean:define id="simpleHide" value="show" /><!-- show by default -->
<bean:define id="simpleHideReverse" value="hidden" /><!-- hide by default -->
<bean:define id="selectMult">multiple="multiple"</bean:define>
<bean:define id="stateListSize">5</bean:define>
<bean:define id="countryListSize">3</bean:define>
<logic:present parameter="simplemode">
  <bean:define id="searchType" value="Simple" />
  <bean:define id="simpleHide" value="hidden" /><!-- hide some things. -->
  <bean:define id="simpleHideReverse" value="show" /><!-- show some only for simple, like more options -->
  <bean:define id="selectMult"></bean:define> <!-- make picklists only size 1 when simple mode -->
  <bean:define id="stateListSize">1</bean:define>
  <bean:define id="countryListSize">1</bean:define>
</logic:present>

<title>
VegBank - <bean:write name="searchType" /> Plot Search
</title>
<% int howmanytaxa = 5; %>
<% int howmanycomms = 4; %>
  <%   String alph = "abcdefghijklmnopqrstuvwxyz" ; %>
  
<script language="javascript">
function prepareForm() {
    var blnOK = validateThisForm(document.forms.plotqueryform);
    if ( blnOK ) {
      setQueryText();
    }  
    return blnOK;
}


function setQueryText() {
    /* this function sets the criteriaAsText field which the next form displays as: You searched for this and that... */
    /* var to store building string */
    text = "" ;
    strSep = " AND" ;
    /* get relevant fields into variables */
    thecountry = getValuesFromList(document.plotqueryform.xwhereParams_country_0,"value") ;
    if ( thecountry != "" &&  thecountry != null )
         {
            text = text + strSep + " in " + thecountry;
     }
    
    thestate = getValuesFromList(document.plotqueryform.xwhereParams_state_0,"value") ;
    if ( thestate != "" &&  thestate != null )
             {
                text = text + strSep + " in " + thestate ;
             }
    /* all min and max fields: */
    
        theminelevation = document.plotqueryform.xwhereParams_minelevation_0.value ;
            if ( theminelevation != "" &&  theminelevation != null )
                     {
                        text = text + strSep + " Elevation of at least " + theminelevation ;
                 }
        
        themaxelevation = document.plotqueryform.xwhereParams_maxelevation_0.value ;
             if ( themaxelevation != "" &&  themaxelevation != null )
                         {
                            text = text + strSep + " Elevation no more than " + themaxelevation ;
                     }
        theminslopeaspect = document.plotqueryform.xwhereParams_minslopeaspect_0.value ;
            if ( theminslopeaspect != "" &&  theminslopeaspect != null )
                     {
                        text = text + strSep + " Slope Aspect of at least " + theminslopeaspect ;
                 }
        
        themaxslopeaspect = document.plotqueryform.xwhereParams_maxslopeaspect_0.value ;
             if ( themaxslopeaspect != "" &&  themaxslopeaspect != null )
                         {
                            text = text + strSep + " Slope Aspect no more than " + themaxslopeaspect ;
                     }
        theminslopegradient = document.plotqueryform.xwhereParams_minslopegradient_0.value ;
            if ( theminslopegradient != "" &&  theminslopegradient != null )
                     {
                        text = text + strSep + " Slope Gradient greater than " + theminslopegradient ;
                 }
        
        themaxslopegradient = document.plotqueryform.xwhereParams_maxslopegradient_0.value ;
             if ( themaxslopegradient != "" &&  themaxslopegradient != null )
                         {
                            text = text + strSep + " Slope Gradient less than " + themaxslopegradient ;
                     }
        theminobsstartdate = document.plotqueryform.xwhereParams_minobsstartdate_0.value ;
            if ( theminobsstartdate != "" &&  theminobsstartdate != null )
                     {
                        text = text + strSep + " Sampling Date after " + theminobsstartdate ;
                 }
        
        themaxobsstartdate = document.plotqueryform.xwhereParams_maxobsstartdate_0.value ;
             if ( themaxobsstartdate != "" &&  themaxobsstartdate != null )
                         {
                            text = text + strSep + " Sampling Date before " + themaxobsstartdate ;
                     }
        themindateentered = document.plotqueryform.xwhereParams_mindateentered_0.value ;
            if ( themindateentered != "" &&  themindateentered != null )
                     {
                        text = text + strSep + " Entered into VegBank after " + themindateentered ;
                 }
        
        themaxdateentered = document.plotqueryform.xwhereParams_maxdateentered_0.value ;
             if ( themaxdateentered != "" &&  themaxdateentered != null )
                         {
                            text = text + strSep + " Entered into VegBank before " + themaxdateentered ;
                     }
        theminarea = document.plotqueryform.xwhereParams_minarea_0.value ;
            if ( theminarea != "" &&  theminarea != null )
                     {
                        text = text + strSep + " Area at least " + theminarea ;
                 }
        
        themaxarea = document.plotqueryform.xwhereParams_maxarea_0.value ;
             if ( themaxarea != "" &&  themaxarea != null )
                         {
                            text = text + strSep + " Area no more than " + themaxarea ;
                 }
    
       /* value picklists, not PK */
    
              thetopoposition = getValuesFromList(document.plotqueryform.xwhereParams_topoposition_0,"value") ;
              if ( thetopoposition != "" &&  thetopoposition != null )
                   {
                      text = text + strSep + " topoposition is " + thetopoposition;
               }
               
              thelandform = getValuesFromList(document.plotqueryform.xwhereParams_landform_0,"value") ;
              if ( thelandform != "" &&  thelandform != null )
                   {
                      text = text + strSep + " landform is " + thelandform;
               }
               
              thehydrologicregime = getValuesFromList(document.plotqueryform.xwhereParams_hydrologicregime_0,"value") ;
              if ( thehydrologicregime != "" &&  thehydrologicregime != null )
                   {
                      text = text + strSep + " hydrologicregime is " + thehydrologicregime;
               }
               
              thesurficialdeposits = getValuesFromList(document.plotqueryform.xwhereParams_surficialdeposits_0,"value") ;
              if ( thesurficialdeposits != "" &&  thesurficialdeposits != null )
                   {
                      text = text + strSep + " surficialdeposits is " + thesurficialdeposits;
               }
               
              therocktype = getValuesFromList(document.plotqueryform.xwhereParams_rocktype_0,"value") ;
              if ( therocktype != "" &&  therocktype != null )
                   {
                      text = text + strSep + " rocktype is " + therocktype;
                   }
                   
            
            /* picklists that have different values (PKs) rather than text, take text */
           
            thecovermethod = getValuesFromList(document.plotqueryform.xwhereParams_covermethod_0,"text") ;
            if ( thecovermethod != "" &&  thecovermethod != null )
                     {
                        text = text + strSep + " Cover Method is " + thecovermethod;
                 }

                thestratummethod = getValuesFromList(document.plotqueryform.xwhereParams_stratummethod_0,"text") ;
                if ( thestratummethod != "" &&  thestratummethod != null )
                     {
                        text = text + strSep + " Stratum Method is " + thestratummethod;
                 }

                theproject = getValuesFromList(document.plotqueryform.xwhereParams_project_0,"text") ;
                if ( theproject != "" &&  theproject != null )
                     {
                        text = text + strSep + " Project is " + theproject;
                 }     
           
         <%
  for (int i=0; i<howmanytaxa ; i++)
  {
  %>
    
     
            thetaxon<%= alph.substring(i,i+1)  %> =  document.plotqueryform.xwhereParams_taxon<%= alph.substring(i,i + 1) %>_2.value ;
            thetaxon<%= alph.substring(i,i+1)  %>covermin = document.plotqueryform.xwhereParams_taxon<%= alph.substring(i,i + 1) %>_0.value ;
            thetaxon<%= alph.substring(i,i+1)  %>covermax = document.plotqueryform.xwhereParams_taxon<%= alph.substring(i,i + 1) %>_1.value ;
              if ( thetaxon<%= alph.substring(i,i+1)  %> != "" &&  thetaxon<%= alph.substring(i,i+1)  %> != null )
                {
                  text = text + strSep + " Plant Name is " + thetaxon<%= alph.substring(i,i+1)  %> ;
                  if  ( thetaxon<%= alph.substring(i,i+1)  %>covermin != "" &&  thetaxon<%= alph.substring(i,i+1)  %>covermin != null )
                   {
                     text = text + " and its Cover is at least " + thetaxon<%= alph.substring(i,i+1)  %>covermin ;
                   }
                  if  ( thetaxon<%= alph.substring(i,i+1)  %>covermax != "" &&  thetaxon<%= alph.substring(i,i+1)  %>covermax != null )
                   {
                     text = text + " and its Cover not more than " + thetaxon<%= alph.substring(i,i+1)  %>covermax ;
                   }
                                      
                }
    <%
  }
  %>  
              <%
       for (int i=0; i<howmanycomms ; i++)
       {
       %>
         thecomm<%= alph.substring(i,i+1)  %> =  document.plotqueryform.xwhereParams_community<%= alph.substring(i,i + 1) %>_0.value ;
            if ( thecomm<%= alph.substring(i,i+1)  %> != "" &&  thecomm<%= alph.substring(i,i+1)  %> != null )
             {
               text = text + strSep + " Community Name is " + thecomm<%= alph.substring(i,i+1)  %> ;
             }
         <%
       }
  %>  
     
     
    text=text + "                   "; /* ensures substring will not fail */
    document.plotqueryform.criteriaAsText.value =  text.substring(strSep.length)
    
}


</script>


@webpage_masthead_html@


<!-- If nothing is set to be shown, then show some things anyway. -->
<bean:define id="hasshowparams" value="false" />
<logic:present parameter="show_statecountry"><bean:define id="show_statecountry" value="on" /><bean:define id="hasshowparams" value="true" /></logic:present>
<logic:present parameter="show_elev"><bean:define id="show_elev" value="on" /><bean:define id="hasshowparams" value="true" /></logic:present>
<logic:present parameter="show_aspect"><bean:define id="show_aspect" value="on" /><bean:define id="hasshowparams" value="true" /></logic:present>
<logic:present parameter="show_slope"><bean:define id="show_slope" value="on" /><bean:define id="hasshowparams" value="true" /></logic:present>
<logic:present parameter="show_rocktype"><bean:define id="show_rocktype" value="on" /><bean:define id="hasshowparams" value="true" /></logic:present>
<logic:present parameter="show_surficial"><bean:define id="show_surficial" value="on" /><bean:define id="hasshowparams" value="true" /></logic:present>
<logic:present parameter="show_hydrologic"><bean:define id="show_hydrologic" value="on" /><bean:define id="hasshowparams" value="true" /></logic:present>
<logic:present parameter="show_topo"><bean:define id="show_topo" value="on" /><bean:define id="hasshowparams" value="true" /></logic:present>
<logic:present parameter="show_landform"><bean:define id="show_landform" value="on" /><bean:define id="hasshowparams" value="true" /></logic:present>
<logic:present parameter="show_datesampled"><bean:define id="show_datesampled" value="on" /><bean:define id="hasshowparams" value="true" /></logic:present>
<logic:present parameter="show_dateentered"><bean:define id="show_dateentered" value="on" /><bean:define id="hasshowparams" value="true" /></logic:present>
<logic:present parameter="show_area"><bean:define id="show_area" value="on" /><bean:define id="hasshowparams" value="true" /></logic:present>
<logic:present parameter="show_covermeth"><bean:define id="show_covermeth" value="on" /><bean:define id="hasshowparams" value="true" /></logic:present>
<logic:present parameter="show_stratummeth"><bean:define id="show_stratummeth" value="on" /><bean:define id="hasshowparams" value="true" /></logic:present>
<logic:present parameter="show_project"><bean:define id="show_project" value="on" /><bean:define id="hasshowparams" value="true" /></logic:present>
<logic:present parameter="show_plants"><bean:define id="show_plants" value="on" /><bean:define id="hasshowparams" value="true" /></logic:present>
<logic:present parameter="show_comms"><bean:define id="show_comms" value="on" /><bean:define id="hasshowparams" value="true" /></logic:present>
<logic:equal name="hasshowparams" value="false">
  <!-- set default to show here : -->
  <!-- didn't have any params, so defining the defaults here -->
  <bean:define id="show_plants" value="on" />
  <bean:define id="show_comms" value="on" />
  <bean:define id="show_statecountry" value="on" />
</logic:equal>

 
 <!-- get min max now, no longer use struts action! -->
 <vegbank:get id="minmaxbean" select="plotandobservationminmax" beanName="map" 
   pager="false" perPage="-1" where="empty" wparam="" />
 
 
	      <h1><bean:write name="searchType" /> Plot Search</h1>
       <p class="psmall <bean:write name='simpleHideReverse' />">
       <a href="@plotquery_page_advanced@">more options...</a></p>
     
       <p class="psmall <bean:write name='simpleHide' />">
       <a href="@plotquery_page_simple@">simpler search...</a></p>
    <!-- Instructions Row -->
   <p class="instructions">
		   Use this form to find plots in VegBank. 
		 
	      Each section allows querying of different types of attributes.  Leave
	      fields blank to ignore them.  		
	      For more information, see the <a href="@help-for-plot-query-href@">help section</a>.
		 
	    </p>
        <!-- only show this on simple view -->
        
    <!-- ERROR DISPLAY -->
    
    
    <!-- dont do this if simple: -->
    <logic:notEqual name="searchType" value="Simple" >
<!-- start custom -->
  <!-- using parameters like show_statecountry - show_Z -->
  <!-- values of params don't matter.  Just checks to see if they are there -->
  
    <!-- using a table for formatting, I know it's bad, but it works and I don't know how to otherwise -->
   <h2>Fields you wish to query:</h2>
   <p class="instructions">This section is OPTIONAL.  Scroll down or <a href="#plotqueryfields">click here</a> to start 
   your plot query from this page. 
   
   You can check or uncheck boxes below and press the "change fields shown" button to change the fields 
   shown on this form.   <br/>
   Tip: the fields that are shown on this form will also be shown on the results page.  So, show fields
   that you want to see on the results page, even if you are not querying by those fields.</p>
   <!-- this tag defines whether to intially hide this list of criteria -->
   <bean:define id="initliststatus" value="display:block" />
   <bean:define id="inithidestatus" value="display:none" />
   <logic:equal parameter="hidelist" value="true">
     <bean:define id="initliststatus" value="display:none" />
     <bean:define id="inithidestatus" value="display:block" />
   </logic:equal>
   <div id="CriteriaHidden" style="<bean:write name='inithidestatus' />">
     <table class="noborders">
     <tr class="grey"><td>List of fields hidden.  <a href="javascript:showorhidediv('changeCriteria');showorhidediv('CriteriaHidden');">Click Here to show</a>  list of fields</td></tr></table>
   </div>
   
   <div id="changeCriteria" style="<bean:write name='initliststatus' />" >
     <a href="javascript:showorhidediv('changeCriteria');showorhidediv('CriteriaHidden');">Click Here To Hide</a> 
    the following list of fields.
    <form name="changeCriteriaShown" action="#plotqueryfields"> <!-- action is self -->
    <input type="hidden" name="hidelist" value="true" />
    <table class="noborders" >
    <tr class="grey"><th>Shown Fields:</th><th>Additional Fields Available:</th></tr>
  
    <tr class="grey"><td valign="top">
      
      
      <logic:present name="show_statecountry"><input type="checkbox" name="show_statecountry" checked="checked" />State and Country<br/></logic:present>
	  <logic:present name="show_elev"><input type="checkbox" name="show_elev" checked="checked" />Elevation<br/></logic:present>
	  <logic:present name="show_aspect"><input type="checkbox" name="show_aspect" checked="checked" />Aspect<br/></logic:present>
	  <logic:present name="show_slope"><input type="checkbox" name="show_slope" checked="checked" />Slope Gradient<br/></logic:present>
	  <logic:present name="show_rocktype"><input type="checkbox" name="show_rocktype" checked="checked" />Rock Type<br/></logic:present>
	  <logic:present name="show_surficial"><input type="checkbox" name="show_surficial" checked="checked" />Surficial Deposits<br/></logic:present>
	  <logic:present name="show_hydrologic"><input type="checkbox" name="show_hydrologic" checked="checked" />Hydrologic Regime<br/></logic:present>
	  <logic:present name="show_topo"><input type="checkbox" name="show_topo" checked="checked" />Topographic Position<br/></logic:present>
	  <logic:present name="show_landform"><input type="checkbox" name="show_landform" checked="checked" />Landform Type<br/></logic:present>
	  <logic:present name="show_datesampled"><input type="checkbox" name="show_datesampled" checked="checked" />Date Sampled<br/></logic:present>
	  <logic:present name="show_dateentered"><input type="checkbox" name="show_dateentered" checked="checked" />Date Entered<br/></logic:present>
	  <logic:present name="show_area"><input type="checkbox" name="show_area" checked="checked" />Plot Size<br/></logic:present>
	  <logic:present name="show_covermeth"><input type="checkbox" name="show_covermeth" checked="checked" />Cover Method<br/></logic:present>
	  <logic:present name="show_stratummeth"><input type="checkbox" name="show_stratummeth" checked="checked" />Stratum Method<br/></logic:present>
	  <logic:present name="show_project"><input type="checkbox" name="show_project" checked="checked" />Project<br/></logic:present>
	  <logic:present name="show_plants"><input type="checkbox" name="show_plants" checked="checked" />Plants within the Plot<br/></logic:present>
      <logic:present name="show_comms"><input type="checkbox" name="show_comms" checked="checked" />Community Classification<br/></logic:present>
    </td>
    <td  valign="top">
      
      <% int intAdditionalWritten = 1 ; %>
      <logic:notPresent name="show_statecountry"><input type="checkbox" name="show_statecountry"  />State and Country<br/><% intAdditionalWritten = 2 ; %></logic:notPresent>
	  <logic:notPresent name="show_elev"><input type="checkbox" name="show_elev"  />Elevation<br/><% intAdditionalWritten = 2 ; %></logic:notPresent>
	  <logic:notPresent name="show_aspect"><input type="checkbox" name="show_aspect"  />Aspect<br/><% intAdditionalWritten = 2 ; %></logic:notPresent>
	  <logic:notPresent name="show_slope"><input type="checkbox" name="show_slope"  />Slope Gradient<br/><% intAdditionalWritten = 2 ; %></logic:notPresent>
	  <logic:notPresent name="show_rocktype"><input type="checkbox" name="show_rocktype"  />Rock Type<br/><% intAdditionalWritten = 2 ; %></logic:notPresent>
	  <logic:notPresent name="show_surficial"><input type="checkbox" name="show_surficial"  />Surficial Deposits<br/><% intAdditionalWritten = 2 ; %></logic:notPresent>
	  <logic:notPresent name="show_hydrologic"><input type="checkbox" name="show_hydrologic"  />Hydrologic Regime<br/><% intAdditionalWritten = 2 ; %></logic:notPresent>
	  <logic:notPresent name="show_topo"><input type="checkbox" name="show_topo"  />Topographic Position<br/><% intAdditionalWritten = 2 ; %></logic:notPresent>
	  <logic:notPresent name="show_landform"><input type="checkbox" name="show_landform"  />Landform Type<br/><% intAdditionalWritten = 2 ; %></logic:notPresent>
	  <logic:notPresent name="show_datesampled"><input type="checkbox" name="show_datesampled"  />Date Sampled<br/><% intAdditionalWritten = 2 ; %></logic:notPresent>
	  <logic:notPresent name="show_dateentered"><input type="checkbox" name="show_dateentered"  />Date Entered<br/><% intAdditionalWritten = 2 ; %></logic:notPresent>
	  <logic:notPresent name="show_area"><input type="checkbox" name="show_area"  />Plot Size<br/><% intAdditionalWritten = 2 ; %></logic:notPresent>
	  <logic:notPresent name="show_covermeth"><input type="checkbox" name="show_covermeth"  />Cover Method<br/><% intAdditionalWritten = 2 ; %></logic:notPresent>
	  <logic:notPresent name="show_stratummeth"><input type="checkbox" name="show_stratummeth"  />Stratum Method<br/><% intAdditionalWritten = 2 ; %></logic:notPresent>
	  <logic:notPresent name="show_project"><input type="checkbox" name="show_project"  />Project<br/><% intAdditionalWritten = 2 ; %></logic:notPresent>
	  <logic:notPresent name="show_plants"><input type="checkbox" name="show_plants"  />Plants within the Plot<br/><% intAdditionalWritten = 2 ; %></logic:notPresent>
	  <logic:notPresent name="show_comms"><input type="checkbox" name="show_comms"  />Community Classification<br/><% intAdditionalWritten = 2 ; %></logic:notPresent>
	  <% if ( intAdditionalWritten == 1 ) { %>
	    -none-
	  <% } %>
	  

    </td>
    </tr>
    
   <tr class="grey"><td colspan="2"> 
          <script type="text/javascript" language="JavaScript">
          <!-- 
          
          function SetCheckboxes(value) {
            var numelements = document.forms.changeCriteriaShown.elements.length;
            var item;
              for (var i=0 ; i<numelements ; i++) {
                  item = document.forms.changeCriteriaShown.elements[i];
                  item.checked = value;
              }
          }
          -->
        </script> 
             <a href="javascript:SetCheckboxes(true);" >check all</a> 
          | <a href="javascript:SetCheckboxes(false);" >uncheck all</a> <br/>
   
    <input type="submit" value="change fields shown" /> Please note that clicking this button will reset the plot-query form below. <br/ >
    Tip: You can bookmark this page to return to it with the current fields shown.
    </td></tr>

    </table>
    </form>
    </div>
 
   </logic:notEqual> <!-- simple mode -->
   <!-- the real form -->
   
   <h2 class="<bean:write name='simpleHide' />"><a name="plotqueryfields"></a>Plot Query:</h2>
   
   <form name="plotqueryform" action="@views_link@observation_summary.jsp" method="post" onsubmit="javascript:return prepareForm()">
     
     <input name="where" type="hidden" value="where_simple" />
     <input name="xwhereGlue" type="hidden" value="AND" />  
     
     <input name="criteriaAsText" type="hidden" value="" />
          <!-- copy params in case there is an error, you'll need these params to show these on 
               the next screen: -->
        
          <logic:present name="show_statecountry"><input type="hidden" name="show_statecountry" value="on" /></logic:present>
		  <logic:present name="show_elev"><input type="hidden" name="show_elev" value="on" /></logic:present>
		  <logic:present name="show_aspect"><input type="hidden" name="show_aspect" value="on" /></logic:present>
		  <logic:present name="show_slope"><input type="hidden" name="show_slope" value="on" /></logic:present>
		  <logic:present name="show_rocktype"><input type="hidden" name="show_rocktype" value="on" /></logic:present>
		  <logic:present name="show_surficial"><input type="hidden" name="show_surficial" value="on" /></logic:present>
		  <logic:present name="show_hydrologic"><input type="hidden" name="show_hydrologic" value="on" /></logic:present>
		  <logic:present name="show_topo"><input type="hidden" name="show_topo" value="on" /></logic:present>
		  <logic:present name="show_landform"><input type="hidden" name="show_landform" value="on" /></logic:present>
		  <logic:present name="show_datesampled"><input type="hidden" name="show_datesampled" value="on" /></logic:present>
		  <logic:present name="show_dateentered"><input type="hidden" name="show_dateentered" value="on" /></logic:present>
		  <logic:present name="show_area"><input type="hidden" name="show_area" value="on" /></logic:present>
		  <logic:present name="show_covermeth"><input type="hidden" name="show_covermeth" value="on" /></logic:present>
		  <logic:present name="show_stratummeth"><input type="hidden" name="show_stratummeth" value="on" /></logic:present>
		  <logic:present name="show_project"><input type="hidden" name="show_project" value="on" /></logic:present>
		  <logic:present name="show_plants"><input type="hidden" name="show_plants" value="on" /></logic:present>
	      <logic:present name="show_comms"><input type="hidden" name="show_comms" value="on" /></logic:present>
          <logic:equal parameter="hidelist" value="true"><input type="hidden" name="hidelist" value="true" /></logic:equal>
   
   
    <bean:define id="hideCurr" value="show" />
    <logic:notPresent name="show_statecountry">
        <bean:define id="hideCurr" value="hidden" />
    </logic:notPresent>
    
    <div class='<bean:write name="hideCurr" />'>
    <!-- Header Location -->
    
    <h3 class="<bean:write name='simpleHide' />">Find Plots based on Location</h3>
        
          <h4>State/Province, Country:</h4>
	    <p class="instructions">
	      Choose a state, province, and/or country to find plots located there. <br />
        <span  class="<bean:write name='simpleHide' />">  Note that you may select more than one value at a time.
        To select multiple choices, hold down the ctrl or apple key and then 
        select each state/province/country you want to query. </span>
	    </p>
	    <table class="noborders"><tr><td valign="top">
         <p class="item">
                  State/Province (plot count)<br/>
                 <input type="hidden" name="xwhereParams_state_1" value="stateprovince" />
                  <input type="hidden" name="xwhereKey_state" value="xwhere_in" />
          <!-- make picklists only size 1 when simple mode -->
                <select name="xwhereParams_state_0" size="<bean:write name='stateListSize' />" <bean:write name='selectMult' />>
         
         
                    
                <option value="" selected="selected">--ANY--</option>
               
                <vegbank:get id="plotstatelist" select="plotstatelist" 
                  beanName="map" pager="false" where="empty" 
                  wparam="" perPage="-1" />
                <logic:empty name="plotstatelist-BEANLIST">
                  <option value="No states found">Error: no states found</option>
                </logic:empty>
                <logic:notEmpty name="plotstatelist-BEANLIST">
                  <logic:iterate id="onerowofplotstatelist" name="plotstatelist-BEANLIST">
                     <option value='<bean:write name="onerowofplotstatelist" property="stateprovince" />' >  
           <bean:write name="onerowofplotstatelist" property="stateprovince" />
           (<bean:write name="onerowofplotstatelist" property="countstate" />)
                      
                     </option>
                  </logic:iterate>
                </logic:notEmpty>
           
                  </select>
        </p>
        
        </td><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td><td valign="top">
        
       
		<p class="item">
	      Country (plot count)<br/>
            <input type="hidden" name="xwhereParams_country_1" value="country" />
            <input type="hidden" name="xwhereKey_country" value="xwhere_in" />
      <!-- make picklists only size 1 when simple mode -->
          <select name="xwhereParams_country_0" size="<bean:write name='countryListSize' />" <bean:write name='selectMult' />>
              <option value="" selected="selected">--ANY--</option>
       	
     <!-- 	<option value="Canada">Canada</option>
      		<option value="Mexico">Mexico</option>
      		<option value="USA">United States</option>
    -->
    
    	    <vegbank:get id="plotcountrylist" select="plotcountrylist" 
			  beanName="map" pager="false" where="empty" 
			  wparam="" perPage="-1" />
			<logic:empty name="plotcountrylist-BEANLIST">
			  <option value="No countries found">Error: no countries found</option>
			</logic:empty>
			<logic:notEmpty name="plotcountrylist-BEANLIST">
			  <logic:iterate id="onerowofplotcountrylist" name="plotcountrylist-BEANLIST">
			     <option value='<bean:write name="onerowofplotcountrylist" property="country" />' >  
	   <bean:write name="onerowofplotcountrylist" property="country" />
	   (<bean:write name="onerowofplotcountrylist" property="countcountry" />)
			      
			     </option>
			  </logic:iterate>
        </logic:notEmpty>
    
    <!--
    <option value="IS NOT NULL">--NOT NULL--</option>
		      <option value="IS NULL">--NULL--</option> -->
		      
		      
	      </select>
	    </p>	 
        </td></tr></table>
     
	  
    <hr  class="<bean:write name='simpleHide' />" />
	</div>
      
    
    
	<!-- ELEVATION, slope, aspect -->
	 
	 <bean:define id="hideCurr" value="show" />
	    <logic:notPresent name="show_elev">
	      <logic:notPresent name="show_aspect">
	        <logic:notPresent name="show_slope">
     	        <bean:define id="hideCurr" value="hidden" />
	        </logic:notPresent>
	      </logic:notPresent>
	    </logic:notPresent>  
	    
    <div id="group234" class='<bean:write name="hideCurr" />'><!-- ranges -->
	
	
	      <h4>Ranges for Fields:</h4> 
	<p class="instructions">
	      Please enter the upper and/or lower limits for each field of interest.  Note that you may enter just an upper value or a lower value.  You also do not need to enter values for all fields.
	    </p>
	  
	<table>
	  <tr>
	    <th>Field</th>
	    <th>Minimum</th>
	    <th>Maximum</th>
	    <th>Units</th>
	  </tr>

	
	  <!-- elev -->
	  <bean:define id="hideCurr" value="show" />
		    <logic:notPresent name="show_elev">
		            <bean:define id="hideCurr" value="hidden" />
		     </logic:notPresent>  
		
	  <tr  class='<bean:write name="hideCurr" />'><!-- ELEVATION --> 
	    <td>&nbsp;</td>
	    <td colspan="2" class="item" align="center">from 
			<bean:write name="minmaxbean-BEAN" property="curminelevation"/> to 
			<bean:write name="minmaxbean-BEAN" property="curmaxelevation"/> meters</td>
	    <td>&nbsp;</td>
	    <td>&nbsp;</td>
	  </tr>	
	  <tr class='<bean:write name="hideCurr" />'>
	    <td>Elevation</td>
	    <td>
          <input type="hidden" name="xwhereKey_minelevation" value="xwhere_gteq" />
          <input type="hidden" name="xwhereParams_minelevation_1" value="elevation" />
          <input name="xwhereParams_minelevation_0" class="number" size="20"/>
	    </td>
	    <td>
          <input type="hidden" name="xwhereKey_maxelevation" value="xwhere_lteq" />
          <input type="hidden" name="xwhereParams_maxelevation_1" value="elevation" />
          <input name="xwhereParams_maxelevation_0" class="number" size="20"/>
	    </td>
	    <td class="units">meters</td>
	  </tr>

	  <bean:define id="hideCurr" value="show" />
		    <logic:notPresent name="show_aspect">
		            <bean:define id="hideCurr" value="hidden" />
		     </logic:notPresent> 	  
	  
	  <tr  class='<bean:write name="hideCurr" />'><!-- Slope Aspect -->
	    <td>&nbsp;</td>
	    <td colspan="2" class="item" align="center">from <bean:write name="minmaxbean-BEAN" property="curminslopeaspect"/> to <bean:write name="minmaxbean-BEAN" property="curmaxslopeaspect"/> degrees</td>
	    <td>&nbsp;</td>
	    <td>&nbsp;</td>
	  </tr>
	  <tr class='<bean:write name="hideCurr" />'>
	    <td>Slope Aspect</td>
	    <td>
          <input type="hidden" name="xwhereKey_minslopeaspect" value="xwhere_gteq" />
          <input type="hidden" name="xwhereParams_minslopeaspect_1" value="slopeaspect" />
          <input name="xwhereParams_minslopeaspect_0" class="number" size="20"/>
	    </td>
	    <td>
          <input type="hidden" name="xwhereKey_maxslopeaspect" value="xwhere_lteq" />
          <input type="hidden" name="xwhereParams_maxslopeaspect_1" value="slopeaspect" />
          <input name="xwhereParams_maxslopeaspect_0" class="number" size="20"/>
	    </td>
	    <td  class="units">degrees</td>
	  
	  </tr>
	  <bean:define id="hideCurr" value="show" />
	  		    <logic:notPresent name="show_slope">
	  		            <bean:define id="hideCurr" value="hidden" />
	  		     </logic:notPresent> 	  
	 
	  
	
	  <tr class='<bean:write name="hideCurr" />'><!-- Slope Gradient -->
	    <td>&nbsp;</td>
		<td colspan="2" class="item" align="center">from <bean:write name="minmaxbean-BEAN" property="curminslopegradient"/> to <bean:write name="minmaxbean-BEAN" property="curmaxslopegradient"/> degrees</td>
		<td>&nbsp;</td>
	    <td>&nbsp;</td>
	  </tr>
	  <tr class='<bean:write name="hideCurr" />'>
	    <td>Slope Gradient</td>
        <td>
          <input type="hidden" name="xwhereKey_minslopegradient" value="xwhere_gteq" />
          <input type="hidden" name="xwhereParams_minslopegradient_1" value="slopegradient" />
          <input name="xwhereParams_minslopegradient_0" class="number" size="20"/>
        </td>
        <td>
          <input type="hidden" name="xwhereKey_maxslopegradient" value="xwhere_lteq" />
          <input type="hidden" name="xwhereParams_maxslopegradient_1" value="slopegradient" />
          <input name="xwhereParams_maxslopegradient_0" class="number" size="20"/>
        </td>
	    <td  class="units">degrees</td>
	   
	  </tr>
		  
	</table>
	<hr/>
	
	</div><!-- ranges -->
	<!-- closed list fields -->
	 <bean:define id="hideCurr" value="show" />
		    <logic:notPresent name="show_rocktype">
		      <logic:notPresent name="show_surficial">
		        <logic:notPresent name="show_hydrologic">
		          <logic:notPresent name="show_topo">
		            <logic:notPresent name="show_landform">
	     	          <bean:define id="hideCurr" value="hidden" />
		            </logic:notPresent>
		          </logic:notPresent>
		        </logic:notPresent>
		      </logic:notPresent>
	    </logic:notPresent> 
	    
	    <div id="group56789" class='<bean:write name="hideCurr" />'>
	
	
	      <h4>Picklist fields:</h4> 
	    <p class="instructions">
	      Please select values for VegBank fields that are constrained to limited vocabulary.  You do not need to select values for all fields.  Select "--ANY--" to ignore the field in the query.
	    </p>

	 <bean:define id="hideCurr" value="show" />
		    <logic:notPresent name="show_rocktype">
	     	          <bean:define id="hideCurr" value="hidden" />
		    </logic:notPresent> 

	    <p class='<bean:write name="hideCurr" />'>
	      Rock Type<br/>
           <input type="hidden" name="xwhereKey_rocktype" value="xwhere_in" />
          <input type="hidden" name="xwhereParams_rocktype_1" value="rocktype" />
                    
          <select name="xwhereParams_rocktype_0" size="6" multiple="multiple">
		<option value="" selected>--ANY--</option>
        @VB_INSERT_CLOSEDLIST_plot.rocktype@
		</select>
	    </p>
	    
		 <bean:define id="hideCurr" value="show" />
			    <logic:notPresent name="show_surficial">
		     	          <bean:define id="hideCurr" value="hidden" />
		    </logic:notPresent> 
	    
	    <p class='<bean:write name="hideCurr" />'>
	      Surficial Deposits<br/>
            <input type="hidden" name="xwhereKey_surficialdeposits" value="xwhere_in" />
            <input type="hidden" name="xwhereParams_surficialdeposits_1" value="surficialdeposits" />
            <select name="xwhereParams_surficialdeposits_0" size="6" multiple="multiple">
	        <option value="" selected>--ANY--</option>
         @VB_INSERT_CLOSEDLIST_plot.surficialdeposits@

	      </select> 
	    </p>
	    
	    	 <bean:define id="hideCurr" value="show" />
				    <logic:notPresent name="show_hydrologic">
			     	          <bean:define id="hideCurr" value="hidden" />
		    </logic:notPresent> 
	    
	    <p class='<bean:write name="hideCurr" />'>
	      Hydrologic Regime<br />
        <input type="hidden" name="xwhereKey_hydrologicregime" value="xwhere_in" />
        <input type="hidden" name="xwhereParams_hydrologicregime_1" value="hydrologicregime" />
        <select name="xwhereParams_hydrologicregime_0" size="6" multiple="multiple">
	        <option value="" selected>--ANY--</option>
        @VB_INSERT_CLOSEDLIST_observation.hydrologicregime@

	      </select> 
	    </p>
	    
	  
	<!--  <hr/> -->
	
	    <!-- picklist values to select -->    
	    	 <bean:define id="hideCurr" value="show" />
				    <logic:notPresent name="show_topo">
			     	          <bean:define id="hideCurr" value="hidden" />
		    </logic:notPresent> 
	    
	    <p class='<bean:write name="hideCurr" />'>Topo Position<br />
        <input type="hidden" name="xwhereKey_topoposition" value="xwhere_in" />
        <input type="hidden" name="xwhereParams_topoposition_1" value="topoposition" />
        <select name="xwhereParams_topoposition_0" size="6" multiple="multiple">
		<option value="" selected>--ANY--</option>                    
		
          @VB_INSERT_CLOSEDLIST_plot.topoposition@
	      </select> 
	    </p>
	    
	    	 <bean:define id="hideCurr" value="show" />
				    <logic:notPresent name="show_landform">
			     	          <bean:define id="hideCurr" value="hidden" />
		    </logic:notPresent> 
	    
	    <!-- is an open list, FYI -->
	    <p class='<bean:write name="hideCurr" />'>Landform<br />
         <input type="hidden" name="xwhereKey_landform" value="xwhere_in" />
         <input type="hidden" name="xwhereParams_landform_1" value="landform" />
         <select name="xwhereParams_landform_0" size="6" multiple="multiple">
		<option value="" selected>--ANY--</option>                    
		<option>alluvial fan</option><option>alluvial flat</option><option>alluvial terrace</option><option>backshore terrace</option><option>backwater</option><option>badlands</option><option>bajada</option><option>bald</option><option>bank</option><option>bar</option><option>barrier beach</option><option>barrier flat</option><option>barrier island(s)</option><option>barrier reef</option><option>basin</option><option>basin floor</option><option>bay</option><option>bayou</option><option>beach</option><option>beach ridge</option><option>bench</option><option>blowout</option><option>bottomlands</option><option>butte</option><option>caldera</option><option>canyon</option><option>carolina bay</option><option>channel</option><option>chenier</option><option>chenier plain</option><option>cirque</option><option>cirque floor</option><option>cirque headwall</option><option>cliff</option><option>coast</option><option>coastal plain</option><option>col</option><option>collapse sinkhole</option><option>colluvial shoulder</option><option>colluvial slope</option><option>cove</option><option>cuesta</option><option>debris slide</option><option>delta</option><option>delta plain</option><option>depositional levee</option><option>depositional stream terrace</option><option>depression</option><option>desert pavement</option><option>dike</option><option>doline</option><option>dome</option><option>drainage</option><option>drainage channel (undifferentiated)</option><option>draw</option><option>drumlin</option><option>dune (undifferentiated)</option><option>dune field</option><option>earth flow</option><option>earth hummock</option><option>eroded bench</option><option>eroding stream channel system</option><option>erosional stream terrace</option><option>escarpment</option><option>esker</option><option>estuary</option><option>exogenous dome</option><option>fan piedmont</option><option>fault scarp</option><option>fault terrace</option><option>fissure</option><option>fissure vent</option><option>flood plain</option><option>fluvial</option><option>foothills</option><option>foredune</option><option>frost creep slope</option><option>frost mound</option><option>frost scar</option><option>gap</option><option>glaciated uplands</option><option>glacier</option><option>gorge</option><option>graben</option><option>ground moraine</option><option>gulch</option><option>hanging valley</option><option>headland</option><option>highland</option><option>hills</option><option>hillslope bedrock outcrop</option><option>hogback</option><option>hoodoo</option><option>hummock</option><option>inlet</option><option>inselberg</option><option>interdune flat</option><option>interfluve</option><option>island</option><option>kame</option><option>kame moraine</option><option>kame terrace</option><option>karst</option><option>karst tower</option><option>karst window</option><option>kegel karst</option><option>kettle</option><option>kettled outwash plain</option><option>knob</option><option>knoll</option><option>lagoon</option><option>lake</option><option>lake bed</option><option>lake plain</option><option>lake terrace</option><option>lateral moraine</option><option>lateral scarp (undifferentiated)</option><option>lava flow (undifferentiated)</option><option>ledge</option><option>levee</option><option>loess deposit (undifferentiated)</option><option>longshore bar</option><option>lowland</option><option>marine terrace (undifferentiated)</option><option>meander belt</option><option>meander scar</option><option>mesa</option><option>mid slope</option><option>mima mound</option><option>monadnock</option><option>moraine (undifferentiated)</option><option>mound</option><option>mountain valley</option><option>mountain(s)</option><option>mountain-valley fan</option><option>mud flat</option><option>noseslope</option><option>outwash fan</option><option>outwash plain</option><option>outwash terrace</option><option>oxbow</option><option>patterned ground (undifferentiated)</option><option>peat dome</option><option>periglacial boulderfield</option><option>piedmont</option><option>pimple mounds</option><option>pingo</option><option>pinnacle</option><option>plain</option><option>plateau</option><option>playa</option><option>polygon (high-centered)</option><option>polygon (low-centered)</option><option>pothole</option><option>raised beach</option><option>raised estuary</option><option>raised mudflat</option><option>raised tidal flat</option><option>ravine</option><option>relict coastline</option><option>ridge</option><option>ridge and valley</option><option>ridgetop bedrock outcrop</option><option>rift valley</option><option>rim</option><option>riverbed</option><option>rock fall avalanche</option><option>saddle</option><option>sag pond</option><option>sandhills</option><option>scarp</option><option>scarp slope</option><option>scour</option><option>scoured basin</option><option>sea cliff</option><option>seep</option><option>shoal</option><option>shoreline</option><option>sinkhole (undifferentiated)</option><option>slide</option><option>slope</option><option>slough</option><option>slump and topple prone slope</option><option>slump pond</option><option>soil creep slope</option><option>solution sinkhole</option><option>spit</option><option>splay</option><option>stone circle</option><option>stone stripe</option><option>stream terrace (undifferentiated)</option><option>streambed</option><option>subjacent karst collapse sinkhole</option><option>subsidence sinkhole</option><option>swale</option><option>talus</option><option>tarn</option><option>tidal flat</option><option>tidal gut</option><option>till plain</option><option>toe slope</option><option>toe zone (undifferentiated)</option><option>transverse dune</option><option>trench</option><option>trough</option><option>valley</option><option>valley floor</option><option>wave-built terrace</option><option>wave-cut platform</option>

	      </select> 
	    </p>
	
	    </div><!-- 5 to 9 -->
	 
    
    
    <!-- FIND BASED ON SAMPLING METHODS -->
    <bean:define id="hideCurr" value="show" />
  		    <logic:notPresent name="show_datesampled">
  		      <logic:notPresent name="show_dateentered">
  		        <logic:notPresent name="show_area">
  	     	          <bean:define id="hideCurr" value="hidden" />
  		        </logic:notPresent>
  		      </logic:notPresent>
  	        </logic:notPresent> 
  	    
	    <DIV id="groupABC" class='<bean:write name="hideCurr" />'>
  
  
  
	<!--<h3>Find Plots based on Sampling Methods</h3>-->
    
	
	  <!-- sampling methodology -->
	
	      <h4>Plot Date / Size:</h4> 
	    <p class="instructions">
	      Enter date ranges or plot size ranges that apply to plots of interest.  
	      Enter dates in the format: 
	      "M/D/YYYY" where M is the number of the month 
	      (e.g. June is 6), D is day of the month, 
	      and YYYY is the four digit year.
	    </p>
	  
	<table>
	  <tr>
	    <th>Field</th>
	    <th>Minimum</th>
	    <th>Maximum</th>
	    <th>Units</th>
	    
	  </tr>
	   <bean:define id="hideCurr" value="show" />
	    		    <logic:notPresent name="show_datesampled">
	    		              <bean:define id="hideCurr" value="hidden" />
         	        </logic:notPresent> 
	  
	  <tr class='<bean:write name="hideCurr" />'><!-- date --> 
	    <td>&nbsp;</td>
		<td colspan="2" class="item" align="center">from 
            <dt:format pattern="MM/dd/yyyy">
                <dt:parse pattern="yyyy-MM-dd">
                    <bean:write name="minmaxbean-BEAN" property="curminobsstartdate"/>
                </dt:parse>
            </dt:format>
            to
            <dt:format pattern="MM/dd/yyyy">
                <dt:parse pattern="yyyy-MM-dd">
                    <bean:write name="minmaxbean-BEAN" property="curmaxobsstartdate"/>
                </dt:parse>
            </dt:format>
        </td>
		<td>&nbsp;</td>
	    <td>&nbsp;</td>
	  </tr>
	  <tr class='<bean:write name="hideCurr" />'>
	    <td>Date Sampled <br/> <span class="instructions">M/D/YYYY</span></td>
	    <td>
          <input type="hidden" name="xwhereKey_minobsstartdate" value="xwhere_gteq" />
          <input type="hidden" name="xwhereParams_minobsstartdate_1" value="obsstartdate" />
          <input name="xwhereParams_minobsstartdate_0" class="date" size="20"/>
	    </td>
	    <td>
          <input type="hidden" name="xwhereKey_maxobsstartdate" value="xwhere_lteq" />
          <input type="hidden" name="xwhereParams_maxobsstartdate_1" value="obsstartdate" />
          <input name="xwhereParams_maxobsstartdate_0" class="date" size="20"/>
	    </td>
	    <td class="units">date</td>
	   
	  </tr>
	  
	  <bean:define id="hideCurr" value="show" />
	  	    		    <logic:notPresent name="show_dateentered">
	  	    		              <bean:define id="hideCurr" value="hidden" />
         	        </logic:notPresent> 
	  <tr class='<bean:write name="hideCurr" />'><!-- date2 --> 
	    <td>&nbsp;</td>
		<td colspan="2" class="item" align="center">from 
            <dt:format pattern="MM/dd/yyyy">
                <dt:parse pattern="yyyy-MM-dd"><bean:write name="minmaxbean-BEAN" property="curmindateentered"/></dt:parse>
            </dt:format>
            to
            <dt:format pattern="MM/dd/yyyy">
                <dt:parse pattern="yyyy-MM-dd"><bean:write name="minmaxbean-BEAN" property="curmaxdateentered"/></dt:parse>
            </dt:format>
         </td>
		<td>&nbsp;</td>
	   
	  </tr>
	  <tr class='<bean:write name="hideCurr" />'>
	    <td>Date Entered into VegBank <br/> <span class="instructions">M/D/YYYY</span></td>
        <td>
          <input type="hidden" name="xwhereKey_mindateentered" value="xwhere_gteq" />
          <input type="hidden" name="xwhereParams_mindateentered_1" value="observation.dateentered" />
          <input name="xwhereParams_mindateentered_0" class="date" size="20"/>
        </td>
        <td>
          <input type="hidden" name="xwhereKey_maxdateentered" value="xwhere_lteq" />
          <input type="hidden" name="xwhereParams_maxdateentered_1" value="observation.dateentered" />
          <input name="xwhereParams_maxdateentered_0" class="date" size="20"/>
        </td>
	    <td class="units">date</td>
	  
	  </tr>
	 
	 <bean:define id="hideCurr" value="show" />
	 	    		    <logic:notPresent name="show_area">
	 	    		              <bean:define id="hideCurr" value="hidden" />
         	        </logic:notPresent> 
	  <tr class='<bean:write name="hideCurr" />'><!-- plot size -->
	    <td>&nbsp;</td>
		<td colspan="2" class="item" align="center">from 
			<bean:write name="minmaxbean-BEAN" property="curminarea"/> to <bean:write name="minmaxbean-BEAN" property="curmaxarea"/> square meters</td>
		<td>&nbsp;</td>
	  
	  </tr>
	  <tr class='<bean:write name="hideCurr" />'>
	    <td>Plot Size</td>
        <td>
          <input type="hidden" name="xwhereKey_minarea" value="xwhere_gteq" />
          <input type="hidden" name="xwhereParams_minarea_1" value="plot.area" />
          <input name="xwhereParams_minarea_0" class="number" size="20"/>
        </td>
        <td>
          <input type="hidden" name="xwhereKey_maxarea" value="xwhere_lteq" />
          <input type="hidden" name="xwhereParams_maxarea_1" value="plot.area" />
          <input name="xwhereParams_maxarea_0" class="number" size="20"/>
        </td>
	    <td class="units">square meters</td>
	   
	  </tr>
	  
	</table>
	<hr/> 
	
	</DIV> <!--ABC -->
	
	
	  <bean:define id="hideCurr" value="show" />
	  		    <logic:notPresent name="show_covermeth">
	  		      <logic:notPresent name="show_stratummeth">
	  		        <logic:notPresent name="show_project">
	  	     	          <bean:define id="hideCurr" value="hidden" />
	  		        </logic:notPresent>
	  		      </logic:notPresent>
	  	        </logic:notPresent> 
	  	    
	    <DIV id="groupDEF" class='<bean:write name="hideCurr" />'>
	<!-- sampling methodology : MORE -->
	
	      <h4>Methods<!-- and People-->:</h4> 
	   <p class="instructions">
	      Select method(s) <!--or people--> that apply to plots of interest.
	   </p>
	
	<bean:define id="hideCurr" value="show" />
		  		    <logic:notPresent name="show_covermeth">
		  		             <bean:define id="hideCurr" value="hidden" />
       	  	        </logic:notPresent>
	<p class='<bean:write name="hideCurr" />'>Cover Method Name:<br/>
                <input type="hidden" name="xwhereKey_covermethod" value="xwhere_in" />
                <input type="hidden" name="xwhereParams_covermethod_1" value="covermethod_id" />
                <select name="xwhereParams_covermethod_0" size="6" multiple="multiple">
        
		        <option value="" selected>--ANY--</option>
                <vegbank:get id="covermethod" select="covermethod" 
                    beanName="map" pager="false" perPage="-1" 
                    allowOrderBy="true" orderBy="xorderby_covertype_asc" />
                  <logic:iterate id="onerowofcovermethod" name="covermethod-BEANLIST">
                    <option value='<bean:write name="onerowofcovermethod" property="covermethod_id" />'><bean:write name="onerowofcovermethod" property="covertype" /> (<bean:write name="onerowofcovermethod" property="d_obscount" />)</option>
                  </logic:iterate>
              
		      </select>
	    </p>
	    <bean:define id="hideCurr" value="show" />
				  		    <logic:notPresent name="show_stratummeth">
				  		             <bean:define id="hideCurr" value="hidden" />
       	  	        </logic:notPresent>
	      <p class='<bean:write name="hideCurr" />'>Stratum Method Name:<br/>
	      
              <input type="hidden" name="xwhereKey_stratummethod" value="xwhere_in" />
                <input type="hidden" name="xwhereParams_stratummethod_1" value="stratummethod_id" />
                <select name="xwhereParams_stratummethod_0" size="6" multiple="multiple">
        
                 <option value="" selected>--ANY--</option>
                 <vegbank:get id="stratummethod" select="stratummethod" 
                     beanName="map" pager="false" perPage="-1" 
                     allowOrderBy="true" orderBy="xorderby_stratummethodname_asc" />
                   <logic:iterate id="onerowofstratummethod" name="stratummethod-BEANLIST">
                     <option value='<bean:write name="onerowofstratummethod" property="stratummethod_id" />'><bean:write name="onerowofstratummethod" property="stratummethodname" /> (<bean:write name="onerowofstratummethod" property="d_obscount" />)</option>
                  </logic:iterate>
		     </select>
	      </p>
	    
	    
	   
	    <bean:define id="hideCurr" value="show" />
				  		    <logic:notPresent name="show_project">
				  		             <bean:define id="hideCurr" value="hidden" />
       	  	        </logic:notPresent>
	      <p class='<bean:write name="hideCurr" />'>Project Name:<br/>
	     
              <input type="hidden" name="xwhereKey_project" value="xwhere_in" />
                <input type="hidden" name="xwhereParams_project_1" value="project_id" />
                <select name="xwhereParams_project_0" size="6" multiple="multiple">
        
              <option value="" selected>--ANY--</option>
		        <!--xx htmlxoptions property="projectNames"/-->
		        <vegbank:get id="project" select="project" beanName="map" pager="false" perPage="-1" 
                  allowOrderBy="true" orderBy="xorderby_projectname" />
		        <logic:iterate id="onerowofproject" name="project-BEANLIST">
		          <option value='<bean:write name="onerowofproject" property="project_id" />'><bean:write name="onerowofproject" property="projectname" /> (<bean:write name="onerowofproject" property="d_obscount" />)</option>
		        </logic:iterate>
		        
		      </select>
	
        </p>
	  
	  </DIV>
	  
	  <!-- FIND USING VEGATATION -->
       <bean:define id="hideCurr" value="show" />
	  	  		         <logic:notPresent name="show_plants">
	  	  	     	          <bean:define id="hideCurr" value="hidden" />
	  	  		        </logic:notPresent>
	  	  		     
	  	  	    
	    <DIV id="groupG" class='<bean:write name="hideCurr" />'>
    
    <h3  class="<bean:write name='simpleHide' />">Find Plots based on Vegetation</h3>
	
	  <!-- PLANT TAXON -->
	  	<h4>Plant Taxa:</h4> 
          
	      <p class="instructions">
        Enter a plant name to find plots with that plant.  <span class="<bean:write name='simpleHide' />" >  You may also include
		criteria about other attributes that apply to that plant.  Plots will be returned that match ALL criteria for a row. 
		Plots will be returned that match all rows. </span>
		<br />                <font color="red"><b>Use % for the wildcard.</b></font>  Examples: White oak, Carex%
	      </p>
	   
<table>
  <tr class="<bean:write name='simpleHide' />">
    <th rowspan="2">Row</th>
    <th rowspan="2">Plant Name <a target="_blank" href="@forms_link@PlantQuery.jsp">search</a></th>
    <th colspan="2">Cover (%)</th>
    
  </tr>
  <tr class="<bean:write name='simpleHide' />">
    <th>Min</th>
    <th>Max</th>
    
  </tr>
  

  <bean:define id="simpleHide_special">show</bean:define> 
  <!-- special bean that gets rewritten to hide latter rows -->
  
  <%
  for (int i=0; i<howmanytaxa ; i++)
  {
  %>
  <!-- if simple mode, only show one row -->
  
  <tr class="<bean:write name='simpleHide_special' />">
    <td class="<bean:write name='simpleHide' />"><span class="item"><%= i+1 %></span>
     <input type="hidden" name="xwhereKey_taxon<%= alph.substring(i,i + 1) %>" value="xwhere_taxacover" />
    </td>    
    <td><input name='<%= "xwhereParams_taxon" + alph.substring(i,i + 1) + "_2" %>' size="30"/></td>
    <td class="<bean:write name='simpleHide' />"><input class='number' name='<%= "xwhereParams_taxon" + alph.substring(i,i + 1) + "_0" %>' size="5"/></td>
    <td class="<bean:write name='simpleHide' />"><input class='number' name='<%= "xwhereParams_taxon" + alph.substring(i,i + 1) + "_1" %>' size="5"/></td>
     <logic:equal name="simpleHide" value="hidden">
       <bean:define id="simpleHide_special" value="hidden" />     
     </logic:equal> <!-- show no more rows -->
  </tr>
  <%
  }
  %>
</table>
<hr class="<bean:write name='simpleHide' />"/> 
      </DIV>
      <bean:define id="hideCurr" value="show" />
	  	  	  		         <logic:notPresent name="show_comms">
	  	  	  	     	          <bean:define id="hideCurr" value="hidden" />
	  	  	  		        </logic:notPresent>
	  	  	  		     
	  	  	  	    
	    <DIV id="groupH" class='<bean:write name="hideCurr" />'>
      
      <!-- FIND USING COMMUNITIES -->
      
    <h3 class="<bean:write name='simpleHide' />">Find Plots based on Community Classfication</h3>
	
    
	  <!-- VEG COMMUNITY -->
		<h4>Vegetation Community:</h4> 
	      <p class="instructions">
		Enter part of a community name to find plots classified to that community.
    <span class="<bean:write name='simpleHide' />" >    This section functions much like the plant section above.
		Plots will be returned that match ALL criteria for a row.  Plots will be returned that match all rows. </span>
		<br /><font color="red"><b>Use % for the wildcard.</b></font> Example: Acer%Forest
	      </p>
	  
	  <table border="0" cellspacing="1" cellpadding="1">
        <tr  class="<bean:write name='simpleHide' />">
	      <th rowspan="1">Row</th>
	      <th rowspan="1">Community Name <a target="_blank" href="@forms_link@CommQuery.jsp">search</a></th>
	      <!-- TODO:
	      <th rowspan="1">Fit</th>
	      <th rowspan="1">Confidence</th>
	      -->
	      <!--<th colspan="2">Date Classified</th> -->
	      <!--
	      <th rowspan="1">Name of Person Classifying</th>
	      -->
	    </tr>
	    <!--<tr>
	      <th>Min</th>
	      <th>Max</th>
	    </tr>-->
	    
        <bean:define id="simpleHide_special">show</bean:define> 
          <!-- special bean that gets rewritten to hide latter rows -->
  
        
	      <%
	      for (int i=0; i<howmanycomms ; i++)
	      {
	      %>
          <tr  class="<bean:write name='simpleHide_special' />">
        <td class="<bean:write name='simpleHide' />"><span class="item"><%= i+1 %></span>
     <input type="hidden" name="xwhereKey_community<%= alph.substring(i,i + 1) %>" value="xwhere_communityname" /></td>    
        <td><input name='<%= "xwhereParams_community" + alph.substring(i,i + 1) + "_0" %>' size="50"/></td>
           
           <logic:equal name="simpleHide" value="hidden">
                  <bean:define id="simpleHide_special" value="hidden" />     
           </logic:equal> <!-- show no more rows -->
	      <!--td><input name='<%= "maxCommStartDate[" + i + "]" %>' size="10"/></td>
	      <td><input name='<%= "minCommStopDate[" + i + "]" %>' size="10"/></td-->
	    
	      </tr>
	      <%
	      }
	      %>
	    </table>
        </DIV>
	    <!-- SUBMIT THE FORM -->
          <h3  class="<bean:write name='simpleHide' />">Submit Query to VegBank</h3>
	       
		      <input type="submit" value="search"/>&nbsp;&nbsp;
		      <html:reset value="reset"/>
		      
		   
	
     
    
	    
      
    </form>      
    
  @webpage_footer_html@
