@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@

  <title>
  VEGBANK - Display Plots Summary
  </title>
  
  <script src="/vegbank/includes/utils.js"></script>
  <script language="javascript">
    function postAction(actionName) {
        document.theform.action = actionName;
        document.theform.submit();
    }

    function customOnLoad() {
        refreshHighlight('selectedPlots');
    }

  </script>
  <!--body onLoad="refreshHighlight('selectedPlots')"-->
  @webpage_masthead_html@
  
  <br/>
  <logic:messagesPresent message="false">
    <ul>
      <html:messages id="error" message="false">
        <li><bean:write name="error"/></li>
      </html:messages> 
    </ul>
  </logic:messagesPresent>
  <!-- format the word plot to be plural if not 1 plot -->
  <bean:size id="PlotResultsSize" name="PlotsResults"/>
  <p class="category bright">
 
    	<bean:write name="PlotResultsSize"/> plot<logic:notEqual name="PlotResultsSize" value="1">s</logic:notEqual> 
    	 matched your search criteria
 
  
  </p>
      <!-- SOME NOTES ABOUT THE USE OF ICONS-->
      <br/>
	<TABLE class="noborder" cellpadding="10"><TR><TD valign="top">
	<p class="category">
	<logic:notEmpty name="PlotsResults">
      Available Reports:
		</p>
      <p class="item">
        <img src="@image_server@report_sm.gif"></img>=Summary 
		&nbsp; &nbsp; &nbsp; &nbsp;
        <img src="@image_server@comprehensive_sm.gif"></img>=Comprehensive
      </p>
	
      <form name="theform" action="" method="post">
	 <p class="item">Choose plots from the search results below, then...</p>
	
	 
		<input type="button" value="Download Selected Plots" onClick="postAction('DownLoadManager.do')"/> 
		<!--
		&nbsp; &nbsp; &nbsp; 
		<input type="button" value="Request Access from Plot Owner" onClick="postAction('LoadPlotQuery.do')"/> 
		&nbsp; &nbsp; &nbsp; 
		<input type="button" value="Add Selected Plots to Dataset" onClick="postAction('DatasetAppend.do')"/> 
		-->
	 </TD><TD valign="top">
	   @newPlotQueryLink@
	 </TD></TR></TABLE>
           <!-- set up a table -->
           <table cellspacing="0" cellpadding="1">
	     <tr><td>&nbsp; &nbsp; &nbsp; &nbsp; </td>
	       <td bgcolor="#000000">
       <table cellspacing="0" cellpadding="0">
	 <tr bgcolor="#336633" align="left" valign="middle">
	   <th align="center" nowrap> SEARCH<br>RESULTS </th> <!-- black dividing bar --><td bgcolor="#000000" width="1px"></td>
	   
	   <th align="center" valign="middle" nowrap> Author's Plot Name <br /> &amp; Accession Code</th><!-- black dividing bar --><td bgcolor="#000000" width="1px"></td>
	   <th align="center" valign="middle" nowrap> Latitude &amp; <br/> Longitude</th><!-- black dividing bar --><td bgcolor="#000000" width="1px"></td>
	   <th width="8px">&nbsp;<!-- a little space before dominant species --></th>
	   <th align="left" valign="middle" nowrap>  Dominant Taxa (with % cover)  </th>
	 </tr>
	 <tr bgcolor="#333333" align="center">
	   <td class="whitetext" nowrap colspan="20" align="left">
	   	&nbsp;&nbsp;select +<a class="whitetext" href="javascript:checkAll('selectedPlots')">all</a>
		&nbsp; 
		-<a class="whitetext" href="javascript:clearAll('selectedPlots')">none</a>
	   </td>
	  
	 </tr>
	 <%
	 //**************************************************************************************
	 //  Set up alternating row colors
	 //**************************************************************************************
	 boolean toggle = true;
	 String rowClass, marginBgColor;
	 %>
	
	 <logic:iterate id="row" name="PlotsResults" >
	 <%
	 //**************************************************************************************
	 //  Set up alternating row colors
	 //**************************************************************************************
	 if (toggle) {
		 rowClass = "listRowA";
		 marginBgColor = "#CCCCCC";
	 } else {
		 rowClass = "listRowB";
		 marginBgColor = "#EEEEEE";
	 }
	 toggle = !toggle;
	 %>
	 
     <tr class="<%= rowClass %>" valign="top">
	    <!-- First Cell-->
	    <th  class="<%= rowClass %>" align="center" valign="middle" nowrap colspan="2">
	     
	      <!-- THE LINK TO THE SUMMARY-->
	      <!-- URL for getting observation via accessionCode is facilited with %27 which is URL safe version of apostrophe -->
             <a href='@get_link@simple/observation/%27<bean:write property="accessionCode" name="row"/>%27' title="summary report"><img align="center" border="0" 
	     	src="@image_server@report_sm.gif" alt="Summary view"></img></a>
	      <!-- THE LINK TO THE COMPREHENSIVE REPORT-->
	      <a href='@get_link@comprehensive/observation/%27<bean:write property="accessionCode" name="row"/>%27' title="comprehensive report"><img align="center" border="0" 
	      	src="@image_server@comprehensive_sm.gif" alt="Comprehensive view"></img></a>
	      
	      <!-- THE LINK TO THE LOCATION -->
	      <!-- Removing link to map until is works -->
		  <!--
		  a href="http://68.171.138.181:8000/mapplotter/servlet/mapplotter?action=mapsinglecoordinate&amp;longitude=<bean:write name="row" 
		  property="longitude"/>&amp;latitude=<bean:write name="row" property="latitude"/>" title="location report"><img align="center" 
		  border="0" src="@image_server@small_globe.gif" alt="Location"></img></a>
		  -->
		  <!--
	      <a href="/mapplotter/servlet/mapplotter?action=mapsinglecoordinate&amp;longitude=<bean:write name="row" 
		  	property="longitude"/>&amp;latitude=<bean:write name="row" property="latitude"/>" title="location report">
		  -->
	      
	      <!-- Checkbox for action selection -->
	      <br/>
			<input name="selectedPlots" type="checkbox" value='<bean:write property="accessionCode" name="row"/>' 
			onclick="toggle(this)" id='<bean:write property="accessionCode" name="row"/>'/>
                <span class="itemlabel" style="cursor:hand">
					<label for='<bean:write property="accessionCode" name="row"/>'>select</label></span>
	    </th>
 
	    <td align="center" valign="middle" colspan="2"> <!-- colspan 2 b/c top black bars as separators are cells -->
	        <span class="item">
	          <bean:write name="row" property="authorObservationCode"/>   
	        </span>
			<br/>
			<span class="sizetiny">
				<bean:write name="row" property="accessionCode"/>
	        </span>
         </td>		 
	    <td align="center" valign="middle" colspan="2"> <!-- colspan 2 b/c top black bars as separators are cells -->
	        <span class="item">
	          <logic:notEqual name="row" property="latitude" value="0">
	          	<logic:notEqual name="row" property="longitude" value="0">
				  <bean:write name="row" property="latitude" format="#.#"/> 
				  <br/>
				  <bean:write name="row" property="longitude" format="#.#"/>   
	          	</logic:notEqual>
	          </logic:notEqual>
	        </span>
         </td>		
		 <td>&nbsp;<!-- a little space before dominant species --></td> 
	    <td align="left" valign="middle" colspan="2"> <!-- colspan 2 b/c top black bars as separators are cells -->
	        <span class="sizetiny">
	    <!-- no top species returned -->      
	    <logic:empty name="row" property="topspp1"> 
			    None available <br/>
				Species may just be listed by stratum without overall cover values
			  </logic:empty>
	          <logic:notEmpty name="row" property="topspp1"> 
			    <bean:write name="row" property="topspp1" />
			    <br/><bean:write name="row" property="topspp2" /> 
			    <br/><bean:write name="row" property="topspp3" />
			    <br/><bean:write name="row" property="topspp4" /> 
			    <br/><bean:write name="row" property="topspp5" /> 
			  </logic:notEmpty>
			<!--    First Dominant Species will be written here (with cover %)
			    <br/>2nd Dominant Species will be written here (with cover %) 
			    <br/>3rd Dominant Species will be written here (with cover %)
			    <br/>4th Dominant Species will be written here (with cover %)
			    <br/>5th Dominant Species will be written here (with cover %) -->
	        </span>
         </td>		 
	  </tr>
	  <tr bgcolor="#666666">
         <td colspan="20"><img src="transparent.gif" height="1" width="1"></td>		 
	  </tr>
  
	</logic:iterate>
	 <tr bgcolor="#333333" align="center">
	   <td colspan="20" class="whitetext" nowrap align="left">
	   	&nbsp;&nbsp;select +<a class="whitetext" href="javascript:checkAll('selectedPlots')">all</a>
		&nbsp; 
		-<a class="whitetext" href="javascript:clearAll('selectedPlots')">none</a><a name="bottom"></a>
	   </td>
	   
	 </tr>
	 <tr bgcolor="#336633">
	   <td colspan="20">&nbsp; </td>
	 </tr>
      </table>
	  </td></tr>
      </table>
	
	<TABLE class="noborder" cellpadding="10"><TR><TD valign="top">
	
	<p class="item">Choose plots from the search results above, then...</p>
	
	<input type="button" value="Download Selected Plots" onClick="postAction('DownLoadManager.do')"/>
	<!--
		&nbsp; &nbsp; &nbsp; 
	<input type="button" value="Request Access from Plot Owner" onClick="postAction('LoadPlotQuery.do')"/> 
		&nbsp; &nbsp; &nbsp; 
	<input type="button" value="Add Selected Plots to Dataset" onClick="postAction('DatasetAppend.do')"/> 
		-->
	</TD><TD valign="top">
	@newPlotQueryLink@
	</TD></TR></TABLE>
	
     </form>      
  
  </font>
  </span>
	</logic:notEmpty>
  
  <script language="javascript">refreshHighlight('plotName');</script>
  <!-- VEGBANK FOOTER -->
  
  
@webpage_footer_html@
