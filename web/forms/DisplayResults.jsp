<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<!-- 
*   '$RCSfile: DisplayResults.jsp,v $'
*     Purpose: web page for displaying the plots stored in vegbank
*   Copyright: 2000 Regents of the University of California and the
*               National Center for Ecological Analysis and Synthesis
*     Authors: @author@
*
*    '$Author: anderson $'
*      '$Date: 2004-06-29 06:53:26 $'
*  '$Revision: 1.22 $'
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
<head>@defaultHeadToken@
<link rel="STYLESHEET" href="@stylesheet@" type="text/css" />
  <title>
  VEGBANK - Display Plots Summary
  </title>
  </head>
  <script src="/vegbank/includes/utils.js"></script>
  <script language="javascript">
  function postAction(actionName) {
	  document.theform.action = actionName;
	  document.theform.submit();
  }
  </script>


  <body onLoad="refreshHighlight('selectedPlots')">

  @vegbank_header_html_normal@

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
  <logic:equal value="1" name="PlotsResultsSize">
    <span class="category"><font color="red">
      There was one match to your search criteria:<br/>
    </font></span>
  </logic:equal>
  <logic:notEqual name="PlotsResultsSize" value="1">

    <span class="category">
      <font color="red">
	<bean:write name="PlotResultsSize"/> plot<logic:notEqual name="PlotResultsSize" value="1">s</logic:notEqual> matched your search criteria<br/>
      </font>
    </span>
  
      <!-- SOME NOTES ABOUT THE USE OF ICONS-->
      <br/>
      <span class="intro">Available Reports:</span>
		&nbsp; &nbsp; &nbsp; &nbsp;
      <span class="item">
        <img src="@image_server@report_sm.gif"></img>=Summary 
		&nbsp; &nbsp; &nbsp; &nbsp;
        <img src="@image_server@comprehensive_sm.gif"></img>=Comprehensive
      </span>
	  <br/> &nbsp;

      <form name="theform" action="" method="post">

	 <span class="item">Choose plots from the search results below, then...</span>
	 <br/>
		<input type="button" value="Download Selected Plots" onClick="postAction('DownLoadManager.do')"/> 
		&nbsp; &nbsp; &nbsp; 
		<input type="button" value="Request Access from Plot Owner" onClick="postAction('LoadPlotQuery.do')"/> 
		&nbsp; &nbsp; &nbsp; 
		<input type="button" value="Add Selected Plots to Dataset" onClick="postAction('DatasetAppend.do')"/> 
	   <br/>&nbsp;

           <!-- set up a table -->
           <table cellspacing="0" cellpadding="1">
	     <tr><td>&nbsp; &nbsp; &nbsp; &nbsp; </td>
	       <td bgcolor="#000000">

       <table width="700" cellspacing="0" cellpadding="0">


	 <tr bgcolor="#336633" align="left" valign="middle">
	   <th align="center" nowrap> SEARCH<br>RESULTS </th>
	   <th align="center" valign="center" nowrap> Accession Code </th>
	   <th align="center" valign="center" nowrap> Author's<br>Plot Name </th>
	   <th align="center" valign="center" nowrap> Latitude  </th>
	   <th align="center" valign="center" nowrap> Longitude  </th>
	 </tr>

	 <tr bgcolor="#333333" align="center">
	   <td class="whitetext" nowrap>
	   	select +<a class="whitetext" href="javascript:checkAll('selectedPlots')">all</a>
		&nbsp; 
		-<a class="whitetext" href="javascript:clearAll('selectedPlots')">none</a>
	   </td>
	   <td colspan="20"> &nbsp; </td>
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
	    <th width="20%" class="<%= rowClass %>" align="center" nowrap>
	     
	      <!-- THE LINK TO THE SUMMARY-->
             <a href="@datarequestservlet@?requestDataType=vegPlot&amp;resultType=summary&amp;queryType=simple&amp;accessionCode=<bean:write property="accessionCode" name="row"/>" title="summary report"><img align="center" border="0" 
	     	src="@image_server@report_sm.gif" alt="Summary view"></img></a>

	      <!-- THE LINK TO THE COMPREHENSIVE REPORT-->
	      <a href="@datarequestservlet@?requestDataType=vegPlot&amp;resultType=full&amp;queryType=simple&amp;accessionCode=<bean:write property="accessionCode" name="row"/>" title="comprehensive report"><img align="center" border="0" 
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

	    <td align="left" valign="middle">
	        <span class="category">
			 	&nbsp; &nbsp;
	          <bean:write name="row" property="accessionCode"/>   
	        </span>
         </td>		 
	    <td align="left" valign="middle">
	        <span class="item">
	          <bean:write name="row" property="authorObservationCode"/>   
	        </span>
         </td>		 
	    <td align="center" valign="middle">
	        <span class="item">
	          <bean:write name="row" property="latitude" format="#.0"/>   
	        </span>
         </td>		 
	    <td align="center" valign="middle">
	        <span class="item">
	          <bean:write name="row" property="longitude" format="#.0"/>   
	        </span>
         </td>		 
	  </tr>
	  <tr bgcolor="#666666">
         <td colspan="20"><img src="transparent.gif" height="1" width="1"></td>		 
	  </tr>
  
	</logic:iterate>

	 <tr bgcolor="#333333" align="center">
	   <td class="whitetext" nowrap>
	   	select +<a class="whitetext" href="javascript:checkAll('selectedPlots')">all</a>
		&nbsp; 
		-<a class="whitetext" href="javascript:clearAll('selectedPlots')">none</a><a name="bottom"></a>
	   </td>
	   <td colspan="20"> &nbsp; </td>
	 </tr>


	 <tr bgcolor="#336633">
	   <td colspan="20">&nbsp; </td>
	 </tr>
      </table>
	  </td></tr>
      </table>

	<br/>
	 <span class="item">Choose plots from the search results above, then...</span>
	<br/>
	<input type="button" value="Download Selected Plots" onClick="postAction('DownLoadManager.do')"/>
		&nbsp; &nbsp; &nbsp; 
	<input type="button" value="Request Access from Plot Owner" onClick="postAction('LoadPlotQuery.do')"/> 
		&nbsp; &nbsp; &nbsp; 
	<input type="button" value="Add Selected Plots to Dataset" onClick="postAction('DatasetAppend.do')"/> 
	<br/>&nbsp;

     </form>      
  </logic:notEqual>
  
  </font>
  </span>
  
  <script language="javascript">refreshHighlight('plotName');</script>
  <!-- VEGBANK FOOTER -->
  @vegbank_footer_html_tworow@
  </body>
</html>
