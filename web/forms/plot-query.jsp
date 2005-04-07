@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@

<title>
VegBank - Advanced Plot Search
</title>
@webpage_masthead_html@

<% if ( request.getQueryString() == null ) { %>
<!-- redirect user to default if no query parameters passed. -->
  <script type="text/javascript">
    window.location="@plotquery_page@?show_0=on&show_G=on&show_H=on" ;
  </script>

<%  }  %>
  
 
 <!-- get min max now, no longer use struts action! -->
 <vegbank:get id="minmaxbean" select="plotandobservationminmax" beanName="map" 
   pager="false" perPage="-1" where="empty" wparam="" />
 
 
	      <h1>Advanced Plot Search</h1>
	   
    <!-- Instructions Row -->
   <p class="instructions">
		   This form can be used to find plots in VegBank. 
		 
	      Each section allows querying of different types of attributes.  Leave
	      fields blank to ignore these fields in the query.  Make sure you select whether the query
	      should match ALL or ANY criteria you specify at the <a href="#typeOfQuery">end of this form</a>.
		
	      For more information about this form, see the <a href="@help-for-plot-query-href@">help section</a>.
		 
	    </p>
    <!-- ERROR DISPLAY -->
    <p class="error">
	<html:errors/>
      </p>
    
<!-- start custom -->
  <!-- using parameters like show_0 - show_Z -->
  <!-- values of params don't matter.  Just checks to see if they are there -->
  
    <!-- using a table for formatting, I know it's bad, but it works and I don't know how to otherwise -->
   <h2>Fields you wish to query:</h2>
   <p class="instructions">This section is OPTIONAL.  Scroll down or <a href="#plotqueryfields">click here</a> to start 
   your plot query from this page. 
   
   You can check or uncheck boxes below and press the "change fields shown" button to change the fields 
   shown on this form.   </p>
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
    <form name="changeCriteriaShown" action="@plotquery_page@#plotqueryfields"> <!-- action is self -->
    <input type="hidden" name="hidelist" value="true" />
    <table class="noborders" >
    <tr class="grey"><th>Shown Fields:</th><th>Additional Fields Available:</th></tr>
  
    <tr class="grey"><td valign="top">
      
      
      <logic:present parameter="show_0"><input type="checkbox" name="show_0" checked="checked" />State and Country<br/></logic:present>
	  <logic:present parameter="show_2"><input type="checkbox" name="show_2" checked="checked" />Elevation<br/></logic:present>
	  <logic:present parameter="show_3"><input type="checkbox" name="show_3" checked="checked" />Aspect<br/></logic:present>
	  <logic:present parameter="show_4"><input type="checkbox" name="show_4" checked="checked" />Slope Gradient<br/></logic:present>
	  <logic:present parameter="show_5"><input type="checkbox" name="show_5" checked="checked" />Rock Type<br/></logic:present>
	  <logic:present parameter="show_6"><input type="checkbox" name="show_6" checked="checked" />Surficial Deposits<br/></logic:present>
	  <logic:present parameter="show_7"><input type="checkbox" name="show_7" checked="checked" />Hydrologic Regime<br/></logic:present>
	  <logic:present parameter="show_8"><input type="checkbox" name="show_8" checked="checked" />Topographic Position<br/></logic:present>
	  <logic:present parameter="show_9"><input type="checkbox" name="show_9" checked="checked" />Landform Type<br/></logic:present>
	  <logic:present parameter="show_A"><input type="checkbox" name="show_A" checked="checked" />Date Sampled<br/></logic:present>
	  <logic:present parameter="show_B"><input type="checkbox" name="show_B" checked="checked" />Date Entered<br/></logic:present>
	  <logic:present parameter="show_C"><input type="checkbox" name="show_C" checked="checked" />Plots Size<br/></logic:present>
	  <logic:present parameter="show_D"><input type="checkbox" name="show_D" checked="checked" />Cover Method<br/></logic:present>
	  <logic:present parameter="show_E"><input type="checkbox" name="show_E" checked="checked" />Stratum Method<br/></logic:present>
	  <logic:present parameter="show_F"><input type="checkbox" name="show_F" checked="checked" />Project<br/></logic:present>
	  <logic:present parameter="show_G"><input type="checkbox" name="show_G" checked="checked" />Plants within the Plot<br/></logic:present>
      <logic:present parameter="show_H"><input type="checkbox" name="show_H" checked="checked" />Community Classification<br/></logic:present>
    </td>
    <td  valign="top">
      
      <% int intAdditionalWritten = 1 ; %>
      <logic:notPresent parameter="show_0"><input type="checkbox" name="show_0"  />State and Country<br/><% intAdditionalWritten = 2 ; %></logic:notPresent>
	  <logic:notPresent parameter="show_2"><input type="checkbox" name="show_2"  />Elevation<br/><% intAdditionalWritten = 2 ; %></logic:notPresent>
	  <logic:notPresent parameter="show_3"><input type="checkbox" name="show_3"  />Aspect<br/><% intAdditionalWritten = 2 ; %></logic:notPresent>
	  <logic:notPresent parameter="show_4"><input type="checkbox" name="show_4"  />Slope Gradient<br/><% intAdditionalWritten = 2 ; %></logic:notPresent>
	  <logic:notPresent parameter="show_5"><input type="checkbox" name="show_5"  />Rock Type<br/><% intAdditionalWritten = 2 ; %></logic:notPresent>
	  <logic:notPresent parameter="show_6"><input type="checkbox" name="show_6"  />Surficial Deposits<br/><% intAdditionalWritten = 2 ; %></logic:notPresent>
	  <logic:notPresent parameter="show_7"><input type="checkbox" name="show_7"  />Hydrologic Regime<br/><% intAdditionalWritten = 2 ; %></logic:notPresent>
	  <logic:notPresent parameter="show_8"><input type="checkbox" name="show_8"  />Topographic Position<br/><% intAdditionalWritten = 2 ; %></logic:notPresent>
	  <logic:notPresent parameter="show_9"><input type="checkbox" name="show_9"  />Landform Type<br/><% intAdditionalWritten = 2 ; %></logic:notPresent>
	  <logic:notPresent parameter="show_A"><input type="checkbox" name="show_A"  />Date Sampled<br/><% intAdditionalWritten = 2 ; %></logic:notPresent>
	  <logic:notPresent parameter="show_B"><input type="checkbox" name="show_B"  />Date Entered<br/><% intAdditionalWritten = 2 ; %></logic:notPresent>
	  <logic:notPresent parameter="show_C"><input type="checkbox" name="show_C"  />Plots Size<br/><% intAdditionalWritten = 2 ; %></logic:notPresent>
	  <logic:notPresent parameter="show_D"><input type="checkbox" name="show_D"  />Cover Method<br/><% intAdditionalWritten = 2 ; %></logic:notPresent>
	  <logic:notPresent parameter="show_E"><input type="checkbox" name="show_E"  />Stratum Method<br/><% intAdditionalWritten = 2 ; %></logic:notPresent>
	  <logic:notPresent parameter="show_F"><input type="checkbox" name="show_F"  />Project<br/><% intAdditionalWritten = 2 ; %></logic:notPresent>
	  <logic:notPresent parameter="show_G"><input type="checkbox" name="show_G"  />Plants within the Plot<br/><% intAdditionalWritten = 2 ; %></logic:notPresent>
	  <logic:notPresent parameter="show_H"><input type="checkbox" name="show_H"  />Community Classification<br/><% intAdditionalWritten = 2 ; %></logic:notPresent>
	  <% if ( intAdditionalWritten == 1 ) { %>
	    -none-
	  <% } %>
	  

    </td>
    </tr>
    
   <tr class="grey"><td colspan="2"> 
    <input type="submit" value="change fields shown" /> Please note that clicking this button will reset the plot-query form below. <br/ >
    Tip: You can bookmark this page to return to it with the current fields shown.
    </td></tr>
      <tr  class="grey"><td colspan="2">
		  <script type="text/javascript" language="JavaScript">
		  <!-- 
		  var numelements = document.forms.changeCriteriaShown.elements.length;
		  function SetCheckboxes(value) {
		      var item;
		      for (var i=0 ; i<numelements ; i++) {
		          item = document.forms.changeCriteriaShown.elements[i];
		          item.checked = value;
		      }
		  }
		  -->
		</script> 
		  <input value="Check All" onclick="SetCheckboxes(true);" type="button" /> | <input value="Uncheck All" onclick="SetCheckboxes(false);" type="button" /> 
  </td></tr>
    </table>
    </form>
    </div>
   <!-- 
     DEFINE the values of showCrit parameter:
   0 is state, country   \
   2 is elevation  \
   3 is aspect      \
   4 is slope       /
   5 rock type   \
   6 surificial   \
   7 hydrologic    \
   8 topoPos        \
   9 Landform       /
   A date sampled \
   B date entered  \
   C plots size    /
   D coverMethod \
   E stratumMethod \
   F project       /
   G plots by Veg (all 5)
   H plots by comm (all 4)
   
   -->
   
   <!-- the real form -->
   
   <h2><a name="plotqueryfields"></a>Plot Query:</h2>
   
   <html:form action="/PlotQuery" method="get">
   
          <!-- copy params in case there is an error, you'll need these params to show these on 
               the next screen: -->
        
          <logic:present parameter="show_0"><input type="hidden" name="show_0" value="on" /></logic:present>
		  <logic:present parameter="show_2"><input type="hidden" name="show_2" value="on" /></logic:present>
		  <logic:present parameter="show_3"><input type="hidden" name="show_3" value="on" /></logic:present>
		  <logic:present parameter="show_4"><input type="hidden" name="show_4" value="on" /></logic:present>
		  <logic:present parameter="show_5"><input type="hidden" name="show_5" value="on" /></logic:present>
		  <logic:present parameter="show_6"><input type="hidden" name="show_6" value="on" /></logic:present>
		  <logic:present parameter="show_7"><input type="hidden" name="show_7" value="on" /></logic:present>
		  <logic:present parameter="show_8"><input type="hidden" name="show_8" value="on" /></logic:present>
		  <logic:present parameter="show_9"><input type="hidden" name="show_9" value="on" /></logic:present>
		  <logic:present parameter="show_A"><input type="hidden" name="show_A" value="on" /></logic:present>
		  <logic:present parameter="show_B"><input type="hidden" name="show_B" value="on" /></logic:present>
		  <logic:present parameter="show_C"><input type="hidden" name="show_C" value="on" /></logic:present>
		  <logic:present parameter="show_D"><input type="hidden" name="show_D" value="on" /></logic:present>
		  <logic:present parameter="show_E"><input type="hidden" name="show_E" value="on" /></logic:present>
		  <logic:present parameter="show_F"><input type="hidden" name="show_F" value="on" /></logic:present>
		  <logic:present parameter="show_G"><input type="hidden" name="show_G" value="on" /></logic:present>
	      <logic:present parameter="show_H"><input type="hidden" name="show_H" value="on" /></logic:present>
          <logic:equal parameter="hidelist" value="true"><input type="hidden" name="hidelist" value="true" /></logic:equal>
   
   
    <bean:define id="hideCurr" value="show" />
    <logic:notPresent parameter="show_0">
        <bean:define id="hideCurr" value="hidden" />
    </logic:notPresent>
    
    <div class='<bean:write name="hideCurr" />'>
    <!-- Header Location -->
    
	<h3>Find Plots based on Location</h3>
      
    
	      <h4>Country, State:</h4>
	    <p class="instructions">
	      Please select the state/province and/or country in which the plot was sampled. <br />
	      Note that you may select more than one value at a time.  To select multiple choices, hold down the ctrl key and then select each state/province/country you want to query.
	    </p>
	 
		<p class="item">
	      Country (plot count)<br/>
	      <html:select property="countries" size="3" multiple="true">
		      <option value="ANY">--ANY--</option>
       	
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
		      
		      
	      </html:select>
	    </p>	 
	    <p class="item">
	      State (plot count)<br/>
	      <html:select property="state" size="6" multiple="true">
		<option value="ANY">--ANY--</option>
  	   
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
   
		<option value="IS NOT NULL">--NOT NULL--</option>
		<option value="IS NULL">--NULL--</option>
	      </html:select>
	    </p>
     
	  
	<hr/>
	</div>
      
    
    
	<!-- ELEVATION, slope, aspect -->
	 
	 <bean:define id="hideCurr" value="show" />
	    <logic:notPresent parameter="show_2">
	      <logic:notPresent parameter="show_3">
	        <logic:notPresent parameter="show_4">
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
	    <th>Include Nulls?</th>
	  </tr>

	
	  <!-- elev -->
	  <bean:define id="hideCurr" value="show" />
		    <logic:notPresent parameter="show_2">
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
	      <html:text property="minElevation" size="20"/>
	    </td>
	    <td>
	      <html:text property="maxElevation" size="20"/>
	    </td>
	    <td class="units">meters</td>
	    <td>
	      <html:checkbox property="allowNullElevation" />
	    </td>
	  </tr>

	  <bean:define id="hideCurr" value="show" />
		    <logic:notPresent parameter="show_3">
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
	      <html:text property="minSlopeAspect" size="20"/>
	    </td>
	    <td>
	      <html:text property="maxSlopeAspect" size="20"/>
	    </td>
	    <td  class="units">degrees</td>
	    <td>
	      <input name="allowNullSlopeAspect" type="checkbox">
	    </td>
	  </tr>
	  <bean:define id="hideCurr" value="show" />
	  		    <logic:notPresent parameter="show_4">
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
	      <html:text property="minSlopeGradient" size="20"/>
	    </td>
	    <td>
	      <html:text property="maxSlopeGradient" size="20"/>
	    </td>
	    <td  class="units">degrees</td>
	    <td>
	      <input name="allowNullSlopeGradient" type="checkbox">
	    </td>
	  </tr>
		  
	</table>
	<hr/>
	
	</div><!-- ranges -->
	<!-- closed list fields -->
	 <bean:define id="hideCurr" value="show" />
		    <logic:notPresent parameter="show_5">
		      <logic:notPresent parameter="show_6">
		        <logic:notPresent parameter="show_7">
		          <logic:notPresent parameter="show_8">
		            <logic:notPresent parameter="show_9">
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
		    <logic:notPresent parameter="show_5">
	     	          <bean:define id="hideCurr" value="hidden" />
		    </logic:notPresent> 

	    <p class='<bean:write name="hideCurr" />'>
	      Rock Type<br/>
	      <html:select property="rockType" size="6" multiple="true">
		<option value="ANY" selected>--ANY--</option>
		<html:options property="rockTypes"/>
		<option value="IS NOT NULL">--NOT NULL--</option>
		<option value="IS NULL">--NULL--</option>
	      </html:select>
	    </p>
	    
		 <bean:define id="hideCurr" value="show" />
			    <logic:notPresent parameter="show_6">
		     	          <bean:define id="hideCurr" value="hidden" />
		    </logic:notPresent> 
	    
	    <p class='<bean:write name="hideCurr" />'>
	      Surficial Deposits<br/>
	      <html:select property="surficialDeposit" size="6" multiple="true">
	        <option value="ANY" selected>--ANY--</option>
		 <html:options property="surficialDeposits"/>
		<option value="IS NOT NULL">--NOT NULL--</option>
		<option value="IS NULL">--NULL--</option>
	      </html:select> 
	    </p>
	    
	    	 <bean:define id="hideCurr" value="show" />
				    <logic:notPresent parameter="show_7">
			     	          <bean:define id="hideCurr" value="hidden" />
		    </logic:notPresent> 
	    
	    <p class='<bean:write name="hideCurr" />'>
	      Hydrologic Regime<br />
	      <html:select property="hydrologicRegime" size="6" multiple="true">
	        <option value="ANY" selected>--ANY--</option>
		<html:options property="hydrologicRegimes"/>
		<option value="IS NOT NULL">--NOT NULL--</option>
		<option value="IS NULL">--NULL--</option>
	      </html:select> 
	    </p>
	    
	  
	<!--  <hr/> -->
	
	    <!-- picklist values to select -->    
	    	 <bean:define id="hideCurr" value="show" />
				    <logic:notPresent parameter="show_8">
			     	          <bean:define id="hideCurr" value="hidden" />
		    </logic:notPresent> 
	    
	    <p class='<bean:write name="hideCurr" />'>Topo Position<br />
	      <html:select property="topoPosition" size="6" multiple="true">
		<option value="ANY" selected>--ANY--</option>                    
		<html:options property="topoPositions"/>
		<option value="IS NOT NULL">--NOT NULL--</option>
		<option value="IS NULL">--NULL--</option>
	      </html:select> 
	    </p>
	    
	    	 <bean:define id="hideCurr" value="show" />
				    <logic:notPresent parameter="show_9">
			     	          <bean:define id="hideCurr" value="hidden" />
		    </logic:notPresent> 
	    
	    <!-- is an open list, FYI -->
	    <p class='<bean:write name="hideCurr" />'>Landform<br />
	      <html:select property="landForm" size="6" multiple="true">
		<option value="ANY" selected>--ANY--</option>                    
		<option>alluvial fan</option><option>alluvial flat</option><option>alluvial terrace</option><option>backshore terrace</option><option>backwater</option><option>badlands</option><option>bajada</option><option>bald</option><option>bank</option><option>bar</option><option>barrier beach</option><option>barrier flat</option><option>barrier island(s)</option><option>barrier reef</option><option>basin</option><option>basin floor</option><option>bay</option><option>bayou</option><option>beach</option><option>beach ridge</option><option>bench</option><option>blowout</option><option>bottomlands</option><option>butte</option><option>caldera</option><option>canyon</option><option>carolina bay</option><option>channel</option><option>chenier</option><option>chenier plain</option><option>cirque</option><option>cirque floor</option><option>cirque headwall</option><option>cliff</option><option>coast</option><option>coastal plain</option><option>col</option><option>collapse sinkhole</option><option>colluvial shoulder</option><option>colluvial slope</option><option>cove</option><option>cuesta</option><option>debris slide</option><option>delta</option><option>delta plain</option><option>depositional levee</option><option>depositional stream terrace</option><option>depression</option><option>desert pavement</option><option>dike</option><option>doline</option><option>dome</option><option>drainage</option><option>drainage channel (undifferentiated)</option><option>draw</option><option>drumlin</option><option>dune (undifferentiated)</option><option>dune field</option><option>earth flow</option><option>earth hummock</option><option>eroded bench</option><option>eroding stream channel system</option><option>erosional stream terrace</option><option>escarpment</option><option>esker</option><option>estuary</option><option>exogenous dome</option><option>fan piedmont</option><option>fault scarp</option><option>fault terrace</option><option>fissure</option><option>fissure vent</option><option>flood plain</option><option>fluvial</option><option>foothills</option><option>foredune</option><option>frost creep slope</option><option>frost mound</option><option>frost scar</option><option>gap</option><option>glaciated uplands</option><option>glacier</option><option>gorge</option><option>graben</option><option>ground moraine</option><option>gulch</option><option>hanging valley</option><option>headland</option><option>highland</option><option>hills</option><option>hillslope bedrock outcrop</option><option>hogback</option><option>hoodoo</option><option>hummock</option><option>inlet</option><option>inselberg</option><option>interdune flat</option><option>interfluve</option><option>island</option><option>kame</option><option>kame moraine</option><option>kame terrace</option><option>karst</option><option>karst tower</option><option>karst window</option><option>kegel karst</option><option>kettle</option><option>kettled outwash plain</option><option>knob</option><option>knoll</option><option>lagoon</option><option>lake</option><option>lake bed</option><option>lake plain</option><option>lake terrace</option><option>lateral moraine</option><option>lateral scarp (undifferentiated)</option><option>lava flow (undifferentiated)</option><option>ledge</option><option>levee</option><option>loess deposit (undifferentiated)</option><option>longshore bar</option><option>lowland</option><option>marine terrace (undifferentiated)</option><option>meander belt</option><option>meander scar</option><option>mesa</option><option>mid slope</option><option>mima mound</option><option>monadnock</option><option>moraine (undifferentiated)</option><option>mound</option><option>mountain valley</option><option>mountain(s)</option><option>mountain-valley fan</option><option>mud flat</option><option>noseslope</option><option>outwash fan</option><option>outwash plain</option><option>outwash terrace</option><option>oxbow</option><option>patterned ground (undifferentiated)</option><option>peat dome</option><option>periglacial boulderfield</option><option>piedmont</option><option>pimple mounds</option><option>pingo</option><option>pinnacle</option><option>plain</option><option>plateau</option><option>playa</option><option>polygon (high-centered)</option><option>polygon (low-centered)</option><option>pothole</option><option>raised beach</option><option>raised estuary</option><option>raised mudflat</option><option>raised tidal flat</option><option>ravine</option><option>relict coastline</option><option>ridge</option><option>ridge and valley</option><option>ridgetop bedrock outcrop</option><option>rift valley</option><option>rim</option><option>riverbed</option><option>rock fall avalanche</option><option>saddle</option><option>sag pond</option><option>sandhills</option><option>scarp</option><option>scarp slope</option><option>scour</option><option>scoured basin</option><option>sea cliff</option><option>seep</option><option>shoal</option><option>shoreline</option><option>sinkhole (undifferentiated)</option><option>slide</option><option>slope</option><option>slough</option><option>slump and topple prone slope</option><option>slump pond</option><option>soil creep slope</option><option>solution sinkhole</option><option>spit</option><option>splay</option><option>stone circle</option><option>stone stripe</option><option>stream terrace (undifferentiated)</option><option>streambed</option><option>subjacent karst collapse sinkhole</option><option>subsidence sinkhole</option><option>swale</option><option>talus</option><option>tarn</option><option>tidal flat</option><option>tidal gut</option><option>till plain</option><option>toe slope</option><option>toe zone (undifferentiated)</option><option>transverse dune</option><option>trench</option><option>trough</option><option>valley</option><option>valley floor</option><option>wave-built terrace</option><option>wave-cut platform</option>
		<option value="IS NOT NULL">--NOT NULL--</option>
		<option value="IS NULL">--NULL--</option>
	      </html:select> 
	    </p>
	
	    </div><!-- 5 to 9 -->
	 
    
    
    <!-- FIND BASED ON SAMPLING METHODS -->
    <bean:define id="hideCurr" value="show" />
  		    <logic:notPresent parameter="show_A">
  		      <logic:notPresent parameter="show_B">
  		        <logic:notPresent parameter="show_C">
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
	    <th>Include Nulls?</th>
	  </tr>
	   <bean:define id="hideCurr" value="show" />
	    		    <logic:notPresent parameter="show_A">
	    		              <bean:define id="hideCurr" value="hidden" />
         	        </logic:notPresent> 
	  
	  <tr class='<bean:write name="hideCurr" />'><!-- date --> 
	    <td>&nbsp;</td>
		<td colspan="2" class="item" align="center">from 
            <dt:format pattern="MM/dd/yyyy">
                <dt:parse pattern="yyyy-MM-dd hh:mm:ss-SS">
                    <bean:write name="minmaxbean-BEAN" property="curminobsstartdate"/>
                </dt:parse>
            </dt:format>
            to
            <dt:format pattern="MM/dd/yyyy">
                <dt:parse pattern="yyyy-MM-dd hh:mm:ss-SS">
                    <bean:write name="minmaxbean-BEAN" property="curmaxobsenddate"/>
                </dt:parse>
            </dt:format>
        </td>
		<td>&nbsp;</td>
	    <td>&nbsp;</td>
	  </tr>
	  <tr class='<bean:write name="hideCurr" />'>
	    <td>Date Sampled <br/> <span class="instructions">M/D/YYYY</span></td>
	    <td>
	      <html:text property="minObsStartDate" size="20"/>
	    </td>
	    <td>
	      <html:text property="maxObsEndDate" size="20"/>
	    </td>
	    <td class="units">date</td>
	    <td>
	      <html:checkbox property="allowNullObsDate"/>
	    </td>
	  </tr>
	  
	  <bean:define id="hideCurr" value="show" />
	  	    		    <logic:notPresent parameter="show_B">
	  	    		              <bean:define id="hideCurr" value="hidden" />
         	        </logic:notPresent> 
	  <tr class='<bean:write name="hideCurr" />'><!-- date2 --> 
	    <td>&nbsp;</td>
		<td colspan="2" class="item" align="center">from 
            <dt:format pattern="MM/dd/yyyy">
                <dt:parse pattern="yyyy-MM-dd hh:mm:ss-SS"><bean:write name="minmaxbean-BEAN" property="curmindateentered"/></dt:parse>
            </dt:format>
            to
            <dt:format pattern="MM/dd/yyyy">
                <dt:parse pattern="yyyy-MM-dd hh:mm:ss.SS-z"><bean:write name="minmaxbean-BEAN" property="curmaxdateentered"/></dt:parse>
            </dt:format>
         </td>
		<td>&nbsp;</td>
	    <td>&nbsp;</td>
	  </tr>
	  <tr class='<bean:write name="hideCurr" />'>
	    <td>Date Entered into VegBank <br/> <span class="instructions">M/D/YYYY</span></td>
	    <td>
	      <html:text property="minDateEntered" size="20"/>
	    </td>
	    <td>
	      <html:text property="maxDateEntered" size="20"/>
	    </td>
	    <td class="units">date</td>
	    <td><span class="item">(required)</span></td>
	  </tr>
	 
	 <bean:define id="hideCurr" value="show" />
	 	    		    <logic:notPresent parameter="show_C">
	 	    		              <bean:define id="hideCurr" value="hidden" />
         	        </logic:notPresent> 
	  <tr class='<bean:write name="hideCurr" />'><!-- plot size -->
	    <td>&nbsp;</td>
		<td colspan="2" class="item" align="center">from 
			<bean:write name="minmaxbean-BEAN" property="curminarea"/> to <bean:write name="minmaxbean-BEAN" property="curmaxarea"/> square meters</td>
		<td>&nbsp;</td>
	    <td>&nbsp;</td>
	  </tr>
	  <tr class='<bean:write name="hideCurr" />'>
	    <td>Plot Size</td>
	    <td>
	      <html:text property="minPlotArea" size="20"/>
	    </td>
	    <td>
	      <html:text property="maxPlotArea" size="20"/>
	    </td>
	    <td class="units">square meters</td>
	    <td>
	      <html:checkbox property="allowNullPlotArea"/>
	    </td>
	  </tr>
	  
	</table>
	<hr/> 
	
	</DIV> <!--ABC -->
	
	
	  <bean:define id="hideCurr" value="show" />
	  		    <logic:notPresent parameter="show_D">
	  		      <logic:notPresent parameter="show_E">
	  		        <logic:notPresent parameter="show_F">
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
		  		    <logic:notPresent parameter="show_D">
		  		             <bean:define id="hideCurr" value="hidden" />
       	  	        </logic:notPresent>
	<p class='<bean:write name="hideCurr" />'>Cover Method Name:<br/>
	   
	      	<html:select property="coverMethodType" size="6" multiple="true">
		        <option value="ANY" selected>--ANY--</option>
		        <html:options property="coverMethodNames"/>
		        <option value="IS NOT NULL">--NOT NULL--</option>
		        <option value="IS NULL">--NULL--</option>
	        </html:select>
	    </p>
	    <bean:define id="hideCurr" value="show" />
				  		    <logic:notPresent parameter="show_E">
				  		             <bean:define id="hideCurr" value="hidden" />
       	  	        </logic:notPresent>
	      <p class='<bean:write name="hideCurr" />'>Stratum Method Name:<br/>
	      
	      	<html:select property="stratumMethodName" size="6" multiple="true">
		        <option value="ANY" selected>--ANY--</option>
		        <html:options property="stratumMethodNames"/>
		        <option value="IS NOT NULL">--NOT NULL--</option>
		        <option value="IS NULL">--NULL--</option>
	        </html:select>
	      </p>
	    
	    
	   
	    <bean:define id="hideCurr" value="show" />
				  		    <logic:notPresent parameter="show_F">
				  		             <bean:define id="hideCurr" value="hidden" />
       	  	        </logic:notPresent>
	      <p class='<bean:write name="hideCurr" />'>Project Name:<br/>
	     
	      	<html:select property="projectName" size="6" multiple="true">
		        <option value="ANY" selected>--ANY--</option>
		        <!--xx html:options property="projectNames"/-->
		        <vegbank:get id="project" select="project" beanName="map" pager="false" perPage="-1" />
		        <logic:iterate id="onerowofproject" name="project-BEANLIST">
		          <option value='<bean:write name="onerowofproject" property="projectname" />'><bean:write name="onerowofproject" property="projectname" /> (<bean:write name="onerowofproject" property="countobs" />)</option>
		        </logic:iterate>
		        
		        <option value="IS NOT NULL">--NOT NULL--</option>
		        <option value="IS NULL">--NULL--</option>
	        </html:select>
	
        </p>
	  
	  </DIV>
	  
	  <!-- FIND USING VEGATATION -->
       <bean:define id="hideCurr" value="show" />
	  	  		         <logic:notPresent parameter="show_G">
	  	  	     	          <bean:define id="hideCurr" value="hidden" />
	  	  		        </logic:notPresent>
	  	  		     
	  	  	    
	    <DIV id="groupG" class='<bean:write name="hideCurr" />'>
    
	<h3>Find Plots based on Vegetation</h3>
	
	  <!-- PLANT TAXON -->
	  	<h4>Plant Taxa:</h4> 
	      <p class="instructions">
		Please enter names for plants that you wish to query.  You may also include
		criteria about other attributes that apply to that plant.  Plots will be returned that match ALL criteria for a row. 
		Plots will be returned that match all rows, or any row, based on the settings
		for <a href="#typeOfQuery">"Type of Query" setting at the end of this form.</a>
		<br />                <font color="red"><b>Use % for the wildcard.</b></font>
	      </p>
	   
<table>
  <tr>
    <th rowspan="2">Row</th>
    <th rowspan="2">Plant Name <a target="_blank" href="@forms_link@PlantQuery.jsp">search</a></th>
    <th colspan="2">Cover (%)</th>
    
  </tr>
  <tr>
    <th>Min</th>
    <th>Max</th>
    
  </tr>
  <%
  for (int i=0; i<5 ; i++)
  {
  %>
  <tr>
    <td><span class="item"><%= i+1 %></span></td>    
    <td><html:text property='<%= "plantName[" + i + "]" %>' size="30"/></td>
    <td><html:text property='<%= "minTaxonCover[" + i + "]" %>' size="5"/></td>
    <td><html:text property='<%= "maxTaxonCover[" + i + "]" %>' size="5"/></td>
   
        
  </tr>
  <%
  }
  %>
</table>
<hr/> 
      </DIV>
      <bean:define id="hideCurr" value="show" />
	  	  	  		         <logic:notPresent parameter="show_H">
	  	  	  	     	          <bean:define id="hideCurr" value="hidden" />
	  	  	  		        </logic:notPresent>
	  	  	  		     
	  	  	  	    
	    <DIV id="groupH" class='<bean:write name="hideCurr" />'>
      
      <!-- FIND USING COMMUNITIES -->
      
	<h3>Find Plots based on Community Classfication</h3>
	
    
	  <!-- VEG COMMUNITY -->
		<h4>Vegetation Community:</h4> 
	      <p class="instructions">
		Use this section to query for plots that have been assigned to a community based on the criteria you specify here.  
		This section functions much like the plant section above.
		Plots will be returned that match ALL criteria for a row.  Plots will be returned that match all rows, or any row, based on the settings
		for <a href="#typeOfQuery">"Type of Query" setting at the end of this form.</a>
		<br /><font color="red"><b>Use % for the wildcard.</b></font>
	      </p>
	  
	  <table border="0" cellspacing="1" cellpadding="1">
	    <tr>
	      <th rowspan="2">Row</th>
	      <th rowspan="2">Community Name <a target="_blank" href="@forms_link@CommQuery.jsp">search</a></th>
	      <!-- TODO:
	      <th rowspan="2">Fit</th>
	      <th rowspan="2">Confidence</th>
	      -->
	      <th colspan="2">Date Classified</th>
	      <!--
	      <th rowspan="2">Name of Person Classifying</th>
	      -->
	    </tr>
	    <tr>
	      <th>Min</th>
	      <th>Max</th>
	    </tr>
	    
	      <%
	      for (int i=0; i<4 ; i++)
	      {
	      %>
	      <tr>
		<td><span class="item"><%= i+1 %></span></td>    
		<td><html:text property='<%= "commName[" + i + "]" %>' size="30"/></td>
		
	      <td><html:text property='<%= "maxCommStartDate[" + i + "]" %>' size="10"/></td>
	      <td><html:text property='<%= "minCommStopDate[" + i + "]" %>' size="10"/></td>
	    
	      </tr>
	      <%
	      }
	      %>
	    </table>
        </DIV>
	    <!-- SUBMIT THE FORM -->
	      <h3><a name="typeOfQuery" > </a>Submit Query to VegBank</h3>
	        <h4>Type of Query:</h4> 
		<p class="instructions">
		      Plots can be selected that <b>match all</b> the above criteria (AND)
		      or that <b>match any</b> of the above criteria (OR). <br />
		      
		    </p>
		  
		
		      <p>
		      <html:radio property="conjunction" value=" AND "/>match ALL criteria<br/>
		      <html:radio property="conjunction" value=" OR "/>match ANY criteria<br />
		      </p>
		      <small>
		      <html:submit value="search"/>&nbsp;&nbsp;
		      <html:reset value="reset"/>
		      </small>
		   
	
     
    
	    
      
    </html:form>      
    
  @webpage_footer_html@
