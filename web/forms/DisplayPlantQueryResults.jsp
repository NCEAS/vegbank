<%@ page contentType="text/html;charset=UTF-8" language="java" 
  import="org.apache.commons.beanutils.RowSetDynaClass" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<!-- 
*   '$RCSfile: DisplayPlantQueryResults.jsp,v $'
*     Purpose: web page for displaying the plots stored in vegbank
*   Copyright: 2000 Regents of the University of California and the
*               National Center for Ecological Analysis and Synthesis
*     Authors: @author@
*
*    '$Author: mlee $'
*      '$Date: 2004-04-08 05:44:28 $'
*  '$Revision: 1.3 $'
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
  VEGBANK - Display Plots Plants Summary
  </title>
  </head>

  <body>

  @vegbank_header_html_normal@

  <br/>
  <logic:messagesPresent message="false">
    <ul>
      <html:messages id="error" message="false">
        <li><bean:write name="error"/></li>
      </html:messages> 
    </ul>
  </logic:messagesPresent>


  <!-- format the word plants to be plural if not 1 plant -->

  <bean:size id="PlantResultsSize" name="PlantQueryResults" property="rows" />
  <logic:equal value="1" name="PlantResultsSize">
    <span class="category"><font color="red">
      There was one match to your search criteria:<br/>
    </font></span>
  </logic:equal>
  <logic:notEqual name="PlantResultsSize" value="1">

    <span class="category">
      <font color="red">
	<bean:write name="PlantResultsSize"/> plants matched your search criteria<br/>
      </font>
    </span>
  </logic:notEqual>

      <html:form action="/DownLoadManager">

           <!-- set up a table -->
           <table cellspacing="0" cellpadding="1">
	     <tr><td>&nbsp; &nbsp; &nbsp; &nbsp; </td>
	       <td bgcolor="#000000">

       <table width="650" cellspacing="0" cellpadding="0">


	 <tr bgcolor="#336633" align="left" valign="top">
	   <th align="center" nowrap> SEARCH<br>RESULTS </th>
           <logic:iterate id="heading" name="PlantQueryResults" property="dynaProperties"  indexId="dynaPropertyId">

<%
RowSetDynaClass queryResults = (RowSetDynaClass) request.getAttribute("PlantQueryResults");
String columnName = queryResults.getDynaProperties()[dynaPropertyId.intValue()].getName(); 
// Filter out the accessioncode
if ( ! columnName.equalsIgnoreCase("accessioncode") )
{
%>
		<th align="center" valign=center nowrap>
                   <bean:write name="heading" property="name"/>
                </th>
<%
}
%>
           </logic:iterate>
	 </tr>

	 <%
	 //**************************************************************************************
	 //  Set up alternating row colors
	 //**************************************************************************************
	 boolean toggle = true;
	 String rowClass, marginBgColor;
	 %>
	
	 <logic:iterate id="row" name="PlantQueryResults" property="rows" >

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
	    <th width="20%" bgcolor="<%= marginBgColor %>" align="center" nowrap>
	      <html:link page="/GenericDispatcher.do?command=RetrieveVBModelBean&jsp=GenericDisplay.jsp&rootEntity=PlantConcept" title="summary report" paramId="accessionCode" paramName="row" paramId="accessionCode" paramProperty="accessioncode">
                <img align="center" src="@image_server@report_sm.gif" alt="Summary view"></img>
              </html:link>
              <html:link page="/DisplayEntity.do?resultType=rawXML" paramId="accessionCode" paramName="row" paramProperty="accessioncode" title="view raw XML">
                <img align="center" border="0" src="@image_server@xml_icon.gif" alt="Raw XML view"></img>
              </html:link>
              
              <!-- USDA link to plant -->
              <html:link href="http://plants.usda.gov/cgi_bin/plant_search.cgi?mode=Symbol&go=go" paramId="keywordquery" paramName="row" paramProperty="code" title="View USDA Plants page" target="_new">
                <img align="center" border="0" src="@image_server@leaficon.gif" alt="View USDA Plants page"></img>
              </html:link>
            </th>
	     
            <logic:iterate id="column" name="PlantQueryResults" property="dynaProperties" indexId="dynaPropertyId"> 
<%
// Apologies for this hack, could not find a syntatically neat way to do this, 
// I want to get the name of the current column to use to access the property 
// of the dynabean. The Dynabean is in the request and the dynaPropertyId is 
// incremented on iteration of the logic:iterate loop
String columnName  
  = ( (RowSetDynaClass) request.getAttribute("PlantQueryResults")).getDynaProperties()[dynaPropertyId.intValue()].getName();

// Filter out the accessioncode
if ( ! columnName.equalsIgnoreCase("accessioncode") )
{
%>
	      <td align="center" valign="middle">
	        <span class="item">
	          <bean:write name="row" property="<%= columnName %>"/>   
	        </span>
              </td>	
<%
}
%>	
            </logic:iterate> 
	  </tr>
	  <tr bgcolor="#666666">
         <td colspan="20"><img src="transparent.gif" height="1" width="1"></td>		 
	  </tr>
	</logic:iterate>

	 <tr bgcolor="#336633">
	   <td colspan="20">&nbsp; </td>
	 </tr>
      </table>
	  </td></tr>
      </table>

     </html:form>      
  
  </font>
  </span>
 
  <!-- VEGBANK FOOTER -->
  @vegbank_footer_html_tworow@
  </body>
</html>
