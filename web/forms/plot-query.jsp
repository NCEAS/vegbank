@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@

<title>
VegBank - Advanced Plot Search
</title>
@webpage_masthead_html@
  
  <html:form action="/PlotQuery" method="get">
 
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
	
      
    <!-- Find Based on Plot Attributes -->
    
      <h3>Find Plots based on Plot Attributes</h3>
    
	<!-- ELEVATION, slope, aspect -->
	
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
	  <tr><!-- ELEVATION --> 
	    <td>&nbsp;</td>
	    <td colspan="2" class="item" align="center">from 
			<bean:write name="bean" property="curMinElevation"/> to 
			<bean:write name="bean" property="curMaxElevation"/> meters</td>
	    <td>&nbsp;</td>
	    <td>&nbsp;</td>
	  </tr>
	  <tr>
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
	  <tr><!-- Slope Aspect -->
	    <td>&nbsp;</td>
	    <td colspan="2" class="item" align="center">from <bean:write name="bean" property="curMinSlopeAspect"/> to <bean:write name="bean" property="curMaxSlopeAspect"/> degrees</td>
	    <td>&nbsp;</td>
	    <td>&nbsp;</td>
	  </tr>
	  <tr>
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
	  <tr><!-- Slope Gradient -->
	    <td>&nbsp;</td>
		<td colspan="2" class="item" align="center">from <bean:write name="bean" property="curMinSlopeGradient"/> to <bean:write name="bean" property="curMaxSlopeGradient"/> degrees</td>
		<td>&nbsp;</td>
	    <td>&nbsp;</td>
	  </tr>
	  <tr>
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
	<!-- closed list fields -->
	
	      <h4>Picklist fields:</h4> 
	    <p class="instructions">
	      Please select values for VegBank fields that are constrained to limited vocabulary.  You do not need to select values for all fields.  Select "--ANY--" to ignore the field in the query.
	    </p>
	    <p>
	      Rock Type<br/>
	      <html:select property="rockType" size="6" multiple="true">
		<option value="ANY" selected>--ANY--</option>
		<html:options property="rockTypes"/>
		<option value="IS NOT NULL">--NOT NULL--</option>
		<option value="IS NULL">--NULL--</option>
	      </html:select>
	    </p>
	    
	    <p>
	      Surficial Deposits<br/>
	      <html:select property="surficialDeposit" size="6" multiple="true">
	        <option value="ANY" selected>--ANY--</option>
		 <html:options property="surficialDeposits"/>
		<option value="IS NOT NULL">--NOT NULL--</option>
		<option value="IS NULL">--NULL--</option>
	      </html:select> 
	    </p>
	    <p>
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
	    <p>Topo Position<br />
	      <html:select property="topoPosition" size="6" multiple="true">
		<option value="ANY" selected>--ANY--</option>                    
		<html:options property="topoPositions"/>
		<option value="IS NOT NULL">--NOT NULL--</option>
		<option value="IS NULL">--NULL--</option>
	      </html:select> 
	    </p>
	    <!-- This is an open list i.e. not supported yet -->
	    <p>Landform<br />
	      <html:select property="landForm" size="6" multiple="true">
		<option value="ANY" selected>--ANY--</option>                    
		<option>alluvial fan</option><option>alluvial flat</option><option>alluvial terrace</option><option>backshore terrace</option><option>backwater</option><option>badlands</option><option>bajada</option><option>bald</option><option>bank</option><option>bar</option><option>barrier beach</option><option>barrier flat</option><option>barrier island(s)</option><option>barrier reef</option><option>basin</option><option>basin floor</option><option>bay</option><option>bayou</option><option>beach</option><option>beach ridge</option><option>bench</option><option>blowout</option><option>bottomlands</option><option>butte</option><option>caldera</option><option>canyon</option><option>carolina bay</option><option>channel</option><option>chenier</option><option>chenier plain</option><option>cirque</option><option>cirque floor</option><option>cirque headwall</option><option>cliff</option><option>coast</option><option>coastal plain</option><option>col</option><option>collapse sinkhole</option><option>colluvial shoulder</option><option>colluvial slope</option><option>cove</option><option>cuesta</option><option>debris slide</option><option>delta</option><option>delta plain</option><option>depositional levee</option><option>depositional stream terrace</option><option>depression</option><option>desert pavement</option><option>dike</option><option>doline</option><option>dome</option><option>drainage</option><option>drainage channel (undifferentiated)</option><option>draw</option><option>drumlin</option><option>dune (undifferentiated)</option><option>dune field</option><option>earth flow</option><option>earth hummock</option><option>eroded bench</option><option>eroding stream channel system</option><option>erosional stream terrace</option><option>escarpment</option><option>esker</option><option>estuary</option><option>exogenous dome</option><option>fan piedmont</option><option>fault scarp</option><option>fault terrace</option><option>fissure</option><option>fissure vent</option><option>flood plain</option><option>fluvial</option><option>foothills</option><option>foredune</option><option>frost creep slope</option><option>frost mound</option><option>frost scar</option><option>gap</option><option>glaciated uplands</option><option>glacier</option><option>gorge</option><option>graben</option><option>ground moraine</option><option>gulch</option><option>hanging valley</option><option>headland</option><option>highland</option><option>hills</option><option>hillslope bedrock outcrop</option><option>hogback</option><option>hoodoo</option><option>hummock</option><option>inlet</option><option>inselberg</option><option>interdune flat</option><option>interfluve</option><option>island</option><option>kame</option><option>kame moraine</option><option>kame terrace</option><option>karst</option><option>karst tower</option><option>karst window</option><option>kegel karst</option><option>kettle</option><option>kettled outwash plain</option><option>knob</option><option>knoll</option><option>lagoon</option><option>lake</option><option>lake bed</option><option>lake plain</option><option>lake terrace</option><option>lateral moraine</option><option>lateral scarp (undifferentiated)</option><option>lava flow (undifferentiated)</option><option>ledge</option><option>levee</option><option>loess deposit (undifferentiated)</option><option>longshore bar</option><option>lowland</option><option>marine terrace (undifferentiated)</option><option>meander belt</option><option>meander scar</option><option>mesa</option><option>mid slope</option><option>mima mound</option><option>monadnock</option><option>moraine (undifferentiated)</option><option>mound</option><option>mountain valley</option><option>mountain(s)</option><option>mountain-valley fan</option><option>mud flat</option><option>noseslope</option><option>outwash fan</option><option>outwash plain</option><option>outwash terrace</option><option>oxbow</option><option>patterned ground (undifferentiated)</option><option>peat dome</option><option>periglacial boulderfield</option><option>piedmont</option><option>pimple mounds</option><option>pingo</option><option>pinnacle</option><option>plain</option><option>plateau</option><option>playa</option><option>polygon (high-centered)</option><option>polygon (low-centered)</option><option>pothole</option><option>raised beach</option><option>raised estuary</option><option>raised mudflat</option><option>raised tidal flat</option><option>ravine</option><option>relict coastline</option><option>ridge</option><option>ridge and valley</option><option>ridgetop bedrock outcrop</option><option>rift valley</option><option>rim</option><option>riverbed</option><option>rock fall avalanche</option><option>saddle</option><option>sag pond</option><option>sandhills</option><option>scarp</option><option>scarp slope</option><option>scour</option><option>scoured basin</option><option>sea cliff</option><option>seep</option><option>shoal</option><option>shoreline</option><option>sinkhole (undifferentiated)</option><option>slide</option><option>slope</option><option>slough</option><option>slump and topple prone slope</option><option>slump pond</option><option>soil creep slope</option><option>solution sinkhole</option><option>spit</option><option>splay</option><option>stone circle</option><option>stone stripe</option><option>stream terrace (undifferentiated)</option><option>streambed</option><option>subjacent karst collapse sinkhole</option><option>subsidence sinkhole</option><option>swale</option><option>talus</option><option>tarn</option><option>tidal flat</option><option>tidal gut</option><option>till plain</option><option>toe slope</option><option>toe zone (undifferentiated)</option><option>transverse dune</option><option>trench</option><option>trough</option><option>valley</option><option>valley floor</option><option>wave-built terrace</option><option>wave-cut platform</option>
		<option value="IS NOT NULL">--NOT NULL--</option>
		<option value="IS NULL">--NULL--</option>
	      </html:select> 
	    </p>
	
	    
	 
    <!-- FIND BASED ON SAMPLING METHODS -->
  
	<h3>Find Plots based on Sampling Methods</h3>
    
	
	  <!-- sampling methodology -->
	
	      <h4>Plot Date / Size:</h4> 
	    <p class="instructions">
	      Enter date ranges or plot size ranges that apply to plots of interest.  
	      Enter dates in the format: 
	      "M-D-YYYY" where M is the number of the month 
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
	  <tr><!-- date --> 
	    <td>&nbsp;</td>
		<td colspan="2" class="item" align="center">from 
            <dt:format pattern="MM-dd-yyyy">
                <dt:parse pattern="yyyy-MM-dd hh:mm:ss-SS">
                    <bean:write name="bean" property="curMinObsStartDate"/>
                </dt:parse>
            </dt:format>
            to
            <dt:format pattern="MM-dd-yyyy">
                <dt:parse pattern="yyyy-MM-dd hh:mm:ss-SS">
                    <bean:write name="bean" property="curMaxObsEndDate"/>
                </dt:parse>
            </dt:format>
        </td>
		<td>&nbsp;</td>
	    <td>&nbsp;</td>
	  </tr>
	  <tr>
	    <td>Date Sampled <br/> <span class="instructions">M-D-YYYY</span></td>
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
	  <tr><!-- date2 --> 
	    <td>&nbsp;</td>
		<td colspan="2" class="item" align="center">from 
            <dt:format pattern="MM-dd-yyyy">
                <dt:parse pattern="yyyy-MM-dd hh:mm:ss-SS">
                    <bean:write name="bean" property="curMinDateEntered"/>
                </dt:parse>
            </dt:format>
            to
            <dt:format pattern="MM-dd-yyyy">
                <dt:parse pattern="yyyy-MM-dd hh:mm:ss-SS">
                    <bean:write name="bean" property="curMaxDateEntered"/>
                </dt:parse>
            </dt:format>
         </td>
		<td>&nbsp;</td>
	    <td>&nbsp;</td>
	  </tr>
	  <tr>
	    <td>Date Entered into VegBank <br/> <span class="instructions">M-D-YYYY</span></td>
	    <td>
	      <html:text property="minDateEntered" size="20"/>
	    </td>
	    <td>
	      <html:text property="maxDateEntered" size="20"/>
	    </td>
	    <td class="units">date</td>
	    <td><span class="item">(required)</span></td>
	  </tr>
	  <tr><!-- plot size -->
	    <td>&nbsp;</td>
		<td colspan="2" class="item" align="center">from 
			<bean:write name="bean" property="curMinArea"/> to <bean:write name="bean" property="curMaxArea"/> square meters</td>
		<td>&nbsp;</td>
	    <td>&nbsp;</td>
	  </tr>
	  <tr>
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
	
	<!-- sampling methodology : MORE -->
	
	      <h4>Methods<!-- and People-->:</h4> 
	   <p class="instructions">
	      Enter method(s) <!--or people--> that apply to plots of interest.
	   </p>
	<p>Cover Method Name:<br/>
	   
	      	<html:select property="coverMethodType" size="6" multiple="true">
		        <option value="ANY" selected>--ANY--</option>
		        <html:options property="coverMethodNames"/>
		        <option value="IS NOT NULL">--NOT NULL--</option>
		        <option value="IS NULL">--NULL--</option>
	        </html:select>
	    </p>
	    
	      <p>Stratum Method Name:<br/>
	      
	      	<html:select property="stratumMethodName" size="6" multiple="true">
		        <option value="ANY" selected>--ANY--</option>
		        <html:options property="stratumMethodNames"/>
		        <option value="IS NOT NULL">--NOT NULL--</option>
		        <option value="IS NULL">--NULL--</option>
	        </html:select>
	      </p>
	    
	    
	   
	    
	      <p>Project Name:<br/>
	     
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
	    
	  <!-- FIND USING VEGATATION -->
      
    
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
