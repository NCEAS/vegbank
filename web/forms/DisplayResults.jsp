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
*    '$Author: farrell $'
*      '$Date: 2003-10-27 19:58:18 $'
*  '$Revision: 1.9 $'
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
<head>
<link rel="STYLESHEET" href="@stylesheet@" type="text/css" />
  <title>
  VEGBANK - Display Plots Summary
  </title>
  </head>

  <body>
  @vegbank_header_html_normal@


  <script src="/vegbank/includes/utils.js"></script>

  <!-- format the word plot to be plural if not 1 plot -->

  <bean:size id="PlotResultsSize" name="PlotsResults"/>
  <logic:equal value="1" name="PlotsResultsSize">
    <span class="category"><font color="red">
      There was one match to your search criteria:<br/>
    </font></span>
  </logic:equal>
  <logic:notEqual name="PlotsResultsSize" value="1">

    <span class="category"><font color="red">
      
	<bean:write name="PlotResultsSize"/> plot<logic:notEqual name="PlotResultsSize" value="1">s</logic:notEqual> matched your search criteria<br/>
    </font></span>
  
      <!-- SOME NOTES ABOUT THE USE OF ICONS-->
      <br/>
      <span class="intro">Available Reports:
        <img src="/vegbank/images/report_sm.gif"></img>=Summary
        <img src="/vegbank/images/comprehensive_sm.gif"></img>=Comprehensive
        <img src="/vegbank/images/small_globe.gif"></img>=Location 
        <img src="/vegbank/images/xml_icon.gif"></img>=Raw XML
      </span>

      <!-- set up the form which is required by netscape 4.x browsers -->
      <form name="listform" action="@viewdataservlet@" method="post">

	 <span class="item">Choose plots to download from the search results below.</span>
	<br/>
       <input type="submit" name="downLoadAction" value="Continue to Download Wizard" /> 
	<br/>&nbsp;
       <!-- set up a table -->
    <table cellspacing="0" cellpadding="1">
	<tr><td>&nbsp; &nbsp; &nbsp; &nbsp; </td>
	<td bgcolor="#000000">
    <table width="650" cellspacing="0" cellpadding="0">


	 <tr bgcolor="#336633" align="left" valign="top">
	   <th align="center" nowrap> SEARCH<br>RESULTS </th>
	   <th align="center" valign="center" nowrap> Accession Code </th>
	   <th align="center" valign="center" nowrap> Author's Plot Name </th>
	   <th align="center" valign="center" nowrap> Latitude  </th>
	   <th align="center" valign="center" nowrap> Longitude  </th>
	 </tr>

	 <tr bgcolor="#333333" align="center">
	   <td class="whitetext" nowrap>
	   	select +<a class="whitetext" href="#" onclick="checkAll('plotName')">all</a>
		&nbsp; 
		-<a class="whitetext" href="#" onclick="clearAll('plotName')">none</a>
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
	    <td width="20%" bgcolor="<%= marginBgColor %>" align="center" nowrap>
	      
	      <!-- THE LINK TO THE SUMMARY-->
             <a href="/vegbank/servlet/DataRequestServlet?requestDataFormatType=html&amp;clientType=browser&amp;requestDataType=vegPlot&amp;resultType=summary&amp;queryType=simple&amp;plotId=<bean:write property="plotId" name="row"/>" title="summary report"><img align="center" border="0" 
	     	src="/vegbank/images/report_sm.gif" alt="Summary view"></img></a>

	      <!-- THE LINK TO THE COMPREHENSIVE REPORT-->
	      <a href="/vegbank/servlet/DataRequestServlet?requestDataFormatType=html&amp;clientType=browser&amp;requestDataType=vegPlot&amp;resultType=full&amp;queryType=simple&amp;plotId=<bean:write property="plotId" name="row"/>" title="comprehensive report"><img align="center" border="0" 
	      	src="/vegbank/images/comprehensive_sm.gif" alt="Comprehensive view"></img></a>
	      
	      <!-- THE LINK TO THE LOCATION -->
	      <a href="/mapplotter/servlet/mapplotter?action=mapsinglecoordinate&amp;longitude=<bean:write name="row" property="longitude"/>&amp;latitude=<bean:write name="row" property="latitude"/>" title="location report"><img align="center" border="0" 
	      	src="/vegbank/images/small_globe.gif" alt="Location"></img></a>
	      
	      <!-- THE LINK TO THE RAW XML-->
              <html:link page="/DisplayPlot.do?resultType=rawXML" paramId="plotId" paramName="row" paramProperty="plotId" title="view raw XML">
                <img align="center" border="0" src="/vegbank/images/xml_icon.gif" alt="Raw XML view"></img>
              </html:link>

		<!-- Checkbox for download -->
		<br/>
              <input name="plotName" type="checkbox" value='<bean:write property="plotId" name="row"/>' 
			  	 onclick="toggle(this)" id='<bean:write property="plotId" name="row"/>'>
                <span class="itemlabel" style="cursor:hand">
					<label for='<bean:write property="plotId" name="row"/>'>download</label></span>
              </input>

	    </td>

	    <td align="center" valign="middle">
	        <span class="category">
	          <bean:write name="row" property="vegbankAccessionNumber"/>   
	        </span>
         </td>		 
	    <td align="center" valign="middle">
	        <span class="item">
	          <bean:write name="row" property="authorObservationCode"/>   
	        </span>
         </td>		 
	    <td align="center" valign="middle">
	        <span class="item">
	          <bean:write name="row" property="latitude"/>   
	        </span>
         </td>		 
	    <td align="center" valign="middle">
	        <span class="item">
	          <bean:write name="row" property="longitude"/>   
	        </span>
         </td>		 
	  </tr>
	  <tr bgcolor="#666666">
         <td colspan="20"><img src="transparent.gif" height="1" width="1"></td>		 
	  </tr>
  
	</logic:iterate>

	 <tr bgcolor="#333333" align="center">
	   <td class="whitetext" nowrap>
	   	select +<a class="whitetext" href="#bottom" onclick="checkAll('plotName')">all</a>
		&nbsp; 
		-<a class="whitetext" href="#bottom" onclick="clearAll('plotName')">none</a><a name="bottom"></a>
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
	 <span class="item">Choose plots to download from the search results above.</span>
	<br/>
       <input type="submit" name="downLoadAction" value="Continue to Download Wizard" /> 
	<br/>&nbsp;
      
  </logic:notEqual>
  
  </font>
  </span>
  
  <script language="javascript">refreshHighlight('plotName');</script>
  <!-- VEGBANK FOOTER -->
  @vegbank_footer_html_tworow@
  </body>
</html>
