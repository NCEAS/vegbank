@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@
<!-- 
*   '$RCSfile: plot-query.jsp,v $'
*     Purpose: web page querying the plots stored in vegbank
*   Copyright: 2000 Regents of the University of California and the
*               National Center for Ecological Analysis and Synthesis
*     Authors: @author@
*
*    '$Author: anderson $'
*      '$Date: 2005-03-15 13:55:41 $'
*  '$Revision: 1.36 $'
*
*
* This program is free software; you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation; either version 2 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
-->





<title>
VegBank - Advanced Plot Query
</title>




@webpage_masthead_html@

  

  <html:form action="/PlotQuery" method="get">

  <table align="left" border="0" width="90%" cellspacing="0" cellpadding="0">

    <tr>
      <td colspan="2" bgcolor="white">
	  </td>
      <td align="left" valign="middle">
	<table border="0" cellpadding="5">
	  <tr>
	    <td align="left" valign="bottom">
	      <h2>Plot Query Form</h2>
	    </td>
	  </tr>
	</table>
      </td>
    </tr>

    <!-- Instructions Row -->
    <tr>
      <!-- LEFT MARGIN -->
      <td width="10%"  bgcolor="white" align="left" valign="top"></td>
      <td width="5%"  bgcolor="white" align="left" valign="top"></td>
      
      <td align="left">
	<table border="0" width="550">
	  <tbody>
	  <tr valign="top">
	    <td align="left" colspan="2" valign="center" class="instructions">
		   This form can be used to find plots in VegBank. 

		 
	      Each section allows querying of different types of attributes.  Leave
	      fields blank to ignore these fields in the query.  Make sure you select whether the query
	      should match ALL or ANY criteria you specify at the <a href="#typeOfQuery">end of this form</a>.

		
	      For more information about this form, see the <a href="@help-for-plot-query-href@">help section</a>.
		 
	    </td>
	  </tr>
	  </tbody>
	</table>

      </td>
    </tr>

    <!-- ERROR DISPLAY -->
    <tr>
      <td colspan="3">
	<html:errors/>
      </td>
    </tr>
    
    <!-- Header Location -->
    <tr bgcolor="#FFFFCC">
      <td width="10%"></td>
      <td colspan="2">
	<h3>Find Plots based on Location</h3>
      </td>
    </tr>


    <!-- State -->
    <tr>
      <td colspan="2"></td>
      <td>
	<!-- STATE -->
	<table border="0" width="100%" bgcolor="#DFE5FA">
	  <tr>
	    <td align="left" valign="top" width="5%" colspan="2">
	      <h4>Country, State:</h4>
	    </td>
	  </tr>
	  <tr>
	    <td align="center" width="5%">
	      <img src="@image_server@icon_cat31.gif" alt="exclamation" width="15" height="15"> 
	    </td>
	    <td class="instructions">
	      Please select the state/province and/or country in which the plot was sampled. <br />
	      Note that you may select more than one value at a time.  To select multiple choices, hold down the ctrl key and then select each state/province/country you want to query.
	    </td>
	  </tr>
	</table>


	<table>
	  <tr>
	    <td width="12%">&nbsp;</td>
		<td class="itemsmall">
	      Country (plot count)<br/>
	      <html:select property="countries" size="6" multiple="true">
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
	    </td>	 

	    <td class="itemsmall">
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
	    </td>
     
	  </tr>
	</table>
	<hr/>

	
      </td>
    </tr>

    <!-- Find Based on Plot Attributes -->
    <tr bgcolor="#FFFFCC" ><td width="10%"></td><td colspan="2">
      <h3>Find Plots based on Plot Attributes</h3></td>
      <tr><td colspan="2"></td><td>
	<!-- ELEVATION, slope, aspect -->
	<table border="0" width="100%" bgcolor="#DFE5FA">

	  <tr>
	    <td align="left" valign="top" width="5%" colspan="2">
	      <h4>Ranges for Fields:</h4> 
	    </td>
	  </tr>
	  <tr>
	    <td align="center" width="5%">
	      <img src="@image_server@icon_cat31.gif" alt="exclamation"
	      width="15" height="15"> 
	    </td>
	    <td class="instructions">
	      Please enter the upper and/or lower limits for each field of interest.  Note that you may enter just an upper value or a lower value.  You also do not need to enter values for all fields.
	    </td>
	  </tr>
	</table>
	<table border="0">
	  <tr bgcolor="#DDDDDD">
	    <th><p><span class="thsmall">Field</span></p></th>
	    <th><p><span class="thsmall">Minimum</span></p></th>
	    <th><p><span class="thsmall">Maximum</span></p></th>
	    <th><p><span class="thsmall">Units</span></p></th>
	    <th><p><span class="thsmall">Include Nulls?</span></p></th>
	  </tr>
	  <tr><!-- ELEVATION --> 
	    <td>&nbsp;</td>
	    <td colspan="2" class="itemsmaller" align="center">from 
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
	      <html:checkbox property="allowNullElevation" value="false"/>
	    </td>
	  </tr>
	  <tr><!-- Slope Aspect -->
	    <td>&nbsp;</td>
	    <td colspan="2" class="itemsmaller" align="center">from <bean:write name="bean" property="curMinSlopeAspect"/> to <bean:write name="bean" property="curMaxSlopeAspect"/> degrees</td>
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
		<td colspan="2" class="itemsmaller" align="center">from <bean:write name="bean" property="curMinSlopeGradient"/> to <bean:write name="bean" property="curMaxSlopeGradient"/> degrees</td>
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
	<hr size=".5">
	<!-- closed list fields -->
	<table border="0" width="100%" bgcolor="#DFE5FA">
	  <tr>
	    <td align="left" valign="top" width="5%" colspan="2">
	      <h4>Picklist fields:</h4> 
	    </td>

	  </tr>
	  <tr>
	    <td align="center" width="5%">
	      <img src="@image_server@icon_cat31.gif" alt="exclamation"
	      width="15" height="15"> 
	    </td>
	    <td class="instructions">
	      Please select values for VegBank fields that are constrained to limited vocabulary.  You do not need to select values for all fields.  Select "--ANY--" to ignore the field in the query.
	    </td>
	  </tr>

	</table>

	<table>
	  <tr>
	    <!-- picklist values to select -->               
	    <td align="left" valign="top">
	      Rock Type<br/>
	      <html:select property="rockType" size="6" multiple="true">
		<option value="ANY" selected>--ANY--</option>
		<html:options property="rockTypes"/>
		<option value="IS NOT NULL">--NOT NULL--</option>
		<option value="IS NULL">--NULL--</option>
	      </html:select>
	    </td>
	    
	    <td align="left" valign="top">
	      Surficial Deposits<br/>
	      <html:select property="surficialDeposit" size="6" multiple="true">
	        <option value="ANY" selected>--ANY--</option>
		 <html:options property="surficialDeposits"/>
		<option value="IS NOT NULL">--NOT NULL--</option>
		<option value="IS NULL">--NULL--</option>
	      </html:select> 
	    </td>

	    <td align="left" valign="top">
	      Hydrologic Regime<br />
	      <html:select property="hydrologicRegime" size="6" multiple="true">
	        <option value="ANY" selected>--ANY--</option>
		<html:options property="hydrologicRegimes"/>
		<option value="IS NOT NULL">--NOT NULL--</option>
		<option value="IS NULL">--NULL--</option>
	      </html:select> 
	    </td>

	    
	  </tr>
	</table>
	<!--  <hr size=".5"> -->
	<table>

	  <tr>
	    <!-- picklist values to select -->               
	    <td align="left" valign="top">Topo Position<br />
	      <html:select property="topoPosition" size="6" multiple="true">
		<option value="ANY" selected>--ANY--</option>                    
		<html:options property="topoPositions"/>
		<option value="IS NOT NULL">--NOT NULL--</option>
		<option value="IS NULL">--NULL--</option>
	      </html:select> 
	    </td>
	    <!-- This is an open list i.e. not supported yet -->
	    <td align="left" valign="top">Landform<br />
	      <html:select property="landForm" size="6" multiple="true">
		<option value="ANY" selected>--ANY--</option>                    

		<option>alluvial fan</option><option>alluvial flat</option><option>alluvial terrace</option><option>backshore terrace</option><option>backwater</option><option>badlands</option><option>bajada</option><option>bald</option><option>bank</option><option>bar</option><option>barrier beach</option><option>barrier flat</option><option>barrier island(s)</option><option>barrier reef</option><option>basin</option><option>basin floor</option><option>bay</option><option>bayou</option><option>beach</option><option>beach ridge</option><option>bench</option><option>blowout</option><option>bottomlands</option><option>butte</option><option>caldera</option><option>canyon</option><option>carolina bay</option><option>channel</option><option>chenier</option><option>chenier plain</option><option>cirque</option><option>cirque floor</option><option>cirque headwall</option><option>cliff</option><option>coast</option><option>coastal plain</option><option>col</option><option>collapse sinkhole</option><option>colluvial shoulder</option><option>colluvial slope</option><option>cove</option><option>cuesta</option><option>debris slide</option><option>delta</option><option>delta plain</option><option>depositional levee</option><option>depositional stream terrace</option><option>depression</option><option>desert pavement</option><option>dike</option><option>doline</option><option>dome</option><option>drainage</option><option>drainage channel (undifferentiated)</option><option>draw</option><option>drumlin</option><option>dune (undifferentiated)</option><option>dune field</option><option>earth flow</option><option>earth hummock</option><option>eroded bench</option><option>eroding stream channel system</option><option>erosional stream terrace</option><option>escarpment</option><option>esker</option><option>estuary</option><option>exogenous dome</option><option>fan piedmont</option><option>fault scarp</option><option>fault terrace</option><option>fissure</option><option>fissure vent</option><option>flood plain</option><option>fluvial</option><option>foothills</option><option>foredune</option><option>frost creep slope</option><option>frost mound</option><option>frost scar</option><option>gap</option><option>glaciated uplands</option><option>glacier</option><option>gorge</option><option>graben</option><option>ground moraine</option><option>gulch</option><option>hanging valley</option><option>headland</option><option>highland</option><option>hills</option><option>hillslope bedrock outcrop</option><option>hogback</option><option>hoodoo</option><option>hummock</option><option>inlet</option><option>inselberg</option><option>interdune flat</option><option>interfluve</option><option>island</option><option>kame</option><option>kame moraine</option><option>kame terrace</option><option>karst</option><option>karst tower</option><option>karst window</option><option>kegel karst</option><option>kettle</option><option>kettled outwash plain</option><option>knob</option><option>knoll</option><option>lagoon</option><option>lake</option><option>lake bed</option><option>lake plain</option><option>lake terrace</option><option>lateral moraine</option><option>lateral scarp (undifferentiated)</option><option>lava flow (undifferentiated)</option><option>ledge</option><option>levee</option><option>loess deposit (undifferentiated)</option><option>longshore bar</option><option>lowland</option><option>marine terrace (undifferentiated)</option><option>meander belt</option><option>meander scar</option><option>mesa</option><option>mid slope</option><option>mima mound</option><option>monadnock</option><option>moraine (undifferentiated)</option><option>mound</option><option>mountain valley</option><option>mountain(s)</option><option>mountain-valley fan</option><option>mud flat</option><option>noseslope</option><option>outwash fan</option><option>outwash plain</option><option>outwash terrace</option><option>oxbow</option><option>patterned ground (undifferentiated)</option><option>peat dome</option><option>periglacial boulderfield</option><option>piedmont</option><option>pimple mounds</option><option>pingo</option><option>pinnacle</option><option>plain</option><option>plateau</option><option>playa</option><option>polygon (high-centered)</option><option>polygon (low-centered)</option><option>pothole</option><option>raised beach</option><option>raised estuary</option><option>raised mudflat</option><option>raised tidal flat</option><option>ravine</option><option>relict coastline</option><option>ridge</option><option>ridge and valley</option><option>ridgetop bedrock outcrop</option><option>rift valley</option><option>rim</option><option>riverbed</option><option>rock fall avalanche</option><option>saddle</option><option>sag pond</option><option>sandhills</option><option>scarp</option><option>scarp slope</option><option>scour</option><option>scoured basin</option><option>sea cliff</option><option>seep</option><option>shoal</option><option>shoreline</option><option>sinkhole (undifferentiated)</option><option>slide</option><option>slope</option><option>slough</option><option>slump and topple prone slope</option><option>slump pond</option><option>soil creep slope</option><option>solution sinkhole</option><option>spit</option><option>splay</option><option>stone circle</option><option>stone stripe</option><option>stream terrace (undifferentiated)</option><option>streambed</option><option>subjacent karst collapse sinkhole</option><option>subsidence sinkhole</option><option>swale</option><option>talus</option><option>tarn</option><option>tidal flat</option><option>tidal gut</option><option>till plain</option><option>toe slope</option><option>toe zone (undifferentiated)</option><option>transverse dune</option><option>trench</option><option>trough</option><option>valley</option><option>valley floor</option><option>wave-built terrace</option><option>wave-cut platform</option>

		<option value="IS NOT NULL">--NOT NULL--</option>
		<option value="IS NULL">--NULL--</option>
	      </html:select> 
	    </td>
	
	    
	  </tr>
	</table>
      </td>
    </tr>


    <!-- FIND BASED ON SAMPLING METHODS -->
    <tr bgcolor="#FFFFCC" >
      <td width="10%"></td>
      <td colspan="2">
	<h3>Find Plots based on Sampling Methods</h3>
      </td>
      <tr>
	<td colspan="2"></td>
	<td>
	
	  <!-- sampling methodology -->
	<table border="0" width="100%" bgcolor="#DFE5FA">
	  <tr>
	    <td align="left" valign="top" width="5%" colspan="2">
	      <h4>Plot Date / Size:</h4> 
	    </td>
	  </tr>

	  <tr>
	    <td align="center" width="5%">
	      <img src="@image_server@icon_cat31.gif" alt="exclamation"
	      width="15" height="15"> 
	    </td>
	    <td class="instructions">
	      Enter date ranges or plot size ranges that apply to plots of interest.  
	      Enter dates in the format: 
	      "M-D-YYYY" where M is the number of the month 
	      (e.g. June is 6), D is day of the month, 
	      and YYYY is the four digit year.
	    </td>
	  </tr>
	</table>

	<table border="0">
	  <tr bgcolor="#DDDDDD">
	    <th>Field</th>
	    <th>Minimum</th>
	    <th>Maximum</th>
	    <th>Units</th>
	    <th>Include Nulls?</th>
	  </tr>
	  <tr><!-- date --> 
	    <td>&nbsp;</td>
		<td colspan="2" class="itemsmaller" align="center">from 
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
		<td colspan="2" class="itemsmaller" align="center">from 

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
	    <td><span class="itemsmall">(required)</span>
	  </tr>
	  <tr><!-- plot size -->
	    <td>&nbsp;</td>
		<td colspan="2" class="itemsmaller" align="center">from 
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
	<hr size=".5"> 
	
	<!-- sampling methodology : MORE -->
	<table border="0" width="100%" bgcolor="#DFE5FA">
	  <tr>
	    <td align="left" valign="top" width="5%" colspan="2">
	      <h4>Methods<!-- and People-->:</h4> 
	    </td>

	  </tr>
	  <tr>
	    <td align="center" width="5%">
	      <img src="@image_server@icon_cat31.gif" alt="exclamation"
	      width="15" height="15"> 
	    </td>
	    <td class="instructions">
	      Enter method(s) <!--or people--> that apply to plots of interest.
	      <!--Please enter a person's name in the format: "SurName, GivenName(s)".  Example: "Jones, Pat" <br />-->
	      <font color="red"><b>Use % for the wildcard.</b>
	    </td>

	  </tr>
	</table>

	<table>
	  <tr>
	    <td>Cover Method Name:</td>
	    <td>
	      	<html:select property="coverMethodType" size="6" multiple="true">
		        <option value="ANY" selected>--ANY--</option>
		        <html:options property="coverMethodNames"/>
		        <option value="IS NOT NULL">--NOT NULL--</option>
		        <option value="IS NULL">--NULL--</option>
	        </html:select>
	    </td>
	    </tr>
	    <tr>

	      <td>Stratum Method Name:</td>
	      <td>
	      	<html:select property="stratumMethodName" size="6" multiple="true">
		        <option value="ANY" selected>--ANY--</option>
		        <html:options property="stratumMethodNames"/>
		        <option value="IS NOT NULL">--NOT NULL--</option>
		        <option value="IS NULL">--NULL--</option>
	        </html:select>
	      </td>
	    </tr>
	    
	   
	    <tr>
	      <td>Project Name:</td>
	      <td>
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
	
        </td>
	    </tr>

	  </table>
	
	</td>
      </tr>



      <!-- FIND USING VEGATATION -->
      

      <tr bgcolor="#FFFFCC" ><td width="10%"></td><td colspan="2">
	<h3>Find Plots based on Vegetation</h3></td>

	<tr><td colspan="2"></td><td>
	  <!-- PLANT TAXON -->
	  <table border="0" width="100%" bgcolor="#DFE5FA">
	    <tr>
	      <td align="left" valign="top" width="5%" colspan="2">
		<h4>Plant Taxa:</h4> 
	      </td>
	    </tr>
	    <tr>

	      <td align="center" width="5%">
		<img src="@image_server@icon_cat31.gif" alt="exclamation"
		width="15" height="15"> 
	      </td>
	      <td class="instructions">
		Please enter names for plants that you wish to query.  You may also include
		criteria about other attributes that apply to that plant.  Plots will be returned that match ALL criteria for a row. 
		Plots will be returned that match all rows, or any row, based on the settings
		for <a href="#typeOfQuery">"Type of Query" setting at the end of this form.</a>
		<br />                <font color="red"><b>Use % for the wildcard.</b>
	      </td>
	    </tr>

	    <tr>
	      <td>
	      </td>
    </tr>
  </table>

<table border="0" cellspacing="1" cellpadding="1">
  <tr bgcolor="#DDDDDD">

    <th rowspan="2">Row</th>
    <th rowspan="2">Plant Name <a target="_blank" href="@forms_link@PlantQuery.jsp">search</a></th>
    <th colspan="2">Cover (%)</th>

    
  </tr>

  <tr bgcolor="#DDDDDD">


    <th>Min</th>
    <th>Max</th>
    
  </tr>

  <%
  for (int i=0; i<5 ; i++)
  {
  %>
  <tr>

    <td><p><span class="item"><%= i+1 %></span></p></td>    
    <td><html:text property='<%= "plantName[" + i + "]" %>' size="30"/></td>
    <td><html:text property='<%= "minTaxonCover[" + i + "]" %>' size="5"/></td>
    <td><html:text property='<%= "maxTaxonCover[" + i + "]" %>' size="5"/></td>
   
        
  </tr>
  <%
  }
  %>

</table>
<hr /> 

      


      <!-- FIND USING COMMUNITIES -->
      <tr bgcolor="#FFFFCC" ><td width="10%"></td><td colspan="2">
	<h3>Find Plots based on Community Classfication</h3></td>
	<tr><td colspan="2"></td><td>
    
	  <!-- VEG COMMUNITY -->
	  <table border="0" width="100%" bgcolor="#DFE5FA">
	    <tr>
	      <td align="left" valign="top" width="5%" colspan="2">
		<h4>Vegetation Community:</h4> 
	      </td>

	    </tr>
	    <tr>
	      <td align="center" width="5%">
		<img src="@image_server@icon_cat31.gif" alt="exclamation"
		width="15" height="15"> 
	      </td>
	      <td class="instructions">
		Use this section to query for plots that have been assigned to a community based on the criteria you specify here.  
		This section functions much like the plant section above.
		Plots will be returned that match ALL criteria for a row.  Plots will be returned that match all rows, or any row, based on the settings
		for <a href="#typeOfQuery">"Type of Query" setting at the end of this form.</a>
		<br /><font color="red"><b>Use % for the wildcard.</b></font>

	      </td>
	    </tr>
	  </table>
	  <table border="0" cellspacing="1" cellpadding="1">
	    <tr bgcolor="#DDDDDD">
	      <th rowspan="2">Row</th>
	      <th rowspan="2">Community Name <a target="_blank" href="@forms_link@CommQuery.jsp">search</a></th>
	      <!-- TODO:
	      <th rowspan="2"><p><span class="thsmall">Fit</span></p></th>
	      <th rowspan="2"><p><span class="thsmall">Confidence</span></p></th>
	      -->
	      <th colspan="2">Date Classified</th>
	      <!--
	      <th rowspan="2"><p><span class="thsmall">Name of Person Classifying</span></p></th>
	      -->
	    </tr>
	    <tr bgcolor="#DDDDDD">
	      <th>Min</th>
	      <th>Max</th>
	    </tr>
	    <tr>

	      <%
	      for (int i=0; i<4 ; i++)
	      {
	      %>

	      <tr>
		<td><p><span class="item"><%= i+1 %></span></p></td>    
		<td><html:text property='<%= "commName[" + i + "]" %>' size="30"/></td>

		
	      <td><html:text property='<%= "maxCommStartDate[" + i + "]" %>' size="10"/></td>
	      <td><html:text property='<%= "minCommStopDate[" + i + "]" %>' size="10"/></td>
	    
	      </tr>
	      <%
	      }
	      %>
	    </table>

  

	    <!-- SUBMIT THE FORM -->
	    <tr bgcolor="#FFFFCC" ><td width="10%"></td><td colspan="2">
	      <h3><a name="typeOfQuery" > </a>Submit Query to VegBank</h3></td>
	      <tr><td colspan="2"></td><td>
		<table border="0" width="100%" bgcolor="#DFE5FA">
		  <tr>
		    <td align="left" valign="top" width="5%" colspan="2">
		      <h4>Type of Query:</h4> 
		    </td>
		  </tr>
		  
		  <tr>
		    <td align="center" width="5%">
		      <img src="@image_server@icon_cat31.gif" alt="exclamation"
		      width="15" height="15"> 
		    </td>
		    <td class="instructions">
		      Plots can be selected that <b>match all</b> the above criteria (AND)
		      or that <b>match any</b> of the above criteria (OR). <br />

		      
		    </td>
		  </tr>
		</table>
		<!-- table for buttons-->
		<table>

		  <tr>
		    <td align="left" colspan="2" valign="center">

		      <p>
		      <html:radio property="conjunction" value=" AND "/>match ALL criteria<br/>
		      <html:radio property="conjunction" value=" OR "/>match ANY criteria<br />
		      </p>

		      <small>
		      <html:submit value="search"/>&nbsp;&nbsp;
		      <html:reset value="reset"/>
		      </small>

		    </td>
		  </tr>
		</table>

		<!-- back one indent -->
	      </td>
	    </tr>


      
     

    

	    
      <tr><td colspan="2">&nbsp;</td></tr>
      <tr>
	<td colspan="3">
	  <!-- VEGBANK FOOTER -->
<br /><br />
	  
	</td>
      </tr>
    </table>
    </html:form>    
    
  @webpage_footer_html@
