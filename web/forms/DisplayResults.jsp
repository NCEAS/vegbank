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
*      '$Date: 2003-05-30 18:00:16 $'
*  '$Revision: 1.2 $'
*
*
-->
<head>
<link rel="STYLESHEET" href="@stylesheet@" type="text/css" />
  <title>
  VEGBANK - Display Plots Summary
  </title>
  </head>

  <body>
  @vegbank_header_html_normal@



  <!-- format the word plot to be plural if not 1 plot -->

  <bean:size id="PlotResultsSize" name="PlotsResults"/>
  <logic:equal value="1" name="PlotsResultsSize">
    <span class="category"><font color="red">
      There was one match to your search criteria:<br/>
    </font></span>
  </logic:equal>
  <logic:notEqual name="PlotsResultsSize" value="1">

    <span class="category"><font color="red">
      <bean:write name="PlotResultsSize"/> plots matched your search criteria<br/>
    </font></span>
  
      <!-- SOME NOTES ABOUT THE USE OF ICONS-->
      <br/>
      <span class="intro">Available Reports:
        <img src="/vegbank/images/report_sm.gif"></img>=Summary
        <img src="/vegbank/images/small_globe.gif"></img>=Location 
        <img src="/vegbank/images/comprehensive_sm.gif"></img>=Comprehensive
      </span>

      <!-- set up the form which is required by netscape 4.x browsers -->
      <form name="myform" action="@viewdataservlet@" method="post">

       <input type="submit" name="downLoadAction" value="Continue to Download Wizard" /> 
       <!-- set up a table -->
       <table width="800">


	 <tr colspan="1" bgcolor="#336633" align="left" valign="top">
	   <th class="tablehead"><font color="#ffff80" face="Arial, Helvetica, sans-serif" size="-1">Identification</font></th>
	 </tr>

	 <%
	 //**************************************************************************************
	 //  Set up alternating row colors
	 //**************************************************************************************
	 String bgColor = "#FFFFF";
	 %>
	
	 <logic:iterate id="row" name="PlotsResults" >

	 <%
	 //**************************************************************************************
	 //  Set up alternating row colors
	 //**************************************************************************************
	 bgColor = bgColor.equals("#FFFFF")? "#FFFFC" : "#FFFFF";
	 %>
	 
       	  <tr valign="top" bgcolor="<%= bgColor %>" >

	    <!-- First Cell-->
	    <td  width="20%" colspan="1" bgcolor="<%= bgColor %>" align="left" valign="middle">
			
	      <a>
	        <span class="category">Vegbank plot Observation accession code: </span> 	
	        <span class="itemsmall">
	          <bean:write name="row" property="vegbankAccessionNumber"/>   
	        </span>
	      </a>

	      <br/>
	      
	      <a>
	        <span class="category">Author plot observation code: </span> 
	        <span class="itemsmall">
	          <bean:write name="row" property="authorObservationCode"/>   
	        </span>
	      </a>

	      <br/>
	      
	      <!-- THE LINK TO THE SUMMARY REPORT-->
	      <a href="/mapplotter/servlet/mapplotter?action=mapsinglecoordinate&amp;longitude=<bean:write name="row" property="longitude"/>&amp;latitude=<bean:write name="row" property="latitude"/>"> 
	        <img align="center" border="0" src="/vegbank/images/small_globe.gif" alt="Location"> </img> 
	      </a>
	      &#160;
	      
	      <!-- THE LINK TO THE COMPREHENSIVE REPORT-->
	      <!--
	      <a href="/vegbank/servlet/DataRequestServlet?requestDataFormatType=html&amp;clientType=browser&amp;requestDataType=vegPlot&amp;resultType=full&amp;queryType=simple&amp;vbaccessionnumber=<bean:write property="vegbankAccessionNumber" name="row"/>"> 
	        <img align="center" border="0" src="/vegbank/images/comprehensive_sm.gif" alt="Comprehensive view"> </img> 
	      </a>
	      -->
	      <a href="/vegbank/servlet/DataRequestServlet?requestDataFormatType=html&amp;clientType=browser&amp;requestDataType=vegPlot&amp;resultType=full&amp;queryType=simple&amp;plotId=<bean:write property="plotId" name="row"/>"> 
	        <img align="center" border="0" src="/vegbank/images/comprehensive_sm.gif" alt="Comprehensive view"> </img> 
	      </a>
	      &#160;
	      
	      <!-- THE LINK TO THE SUMMARY-->
	      <!--
	      <a href="/vegbank/servlet/DataRequestServlet?requestDataFormatType=html&amp;clientType=browser&amp;requestDataType=vegPlot&amp;resultType=summary&amp;queryType=simple&amp;vbaccessionnumber=<bean:write property="vegbankAccessionNumber" name="row"/>"> 
	        <img align="center" border="0" src="/vegbank/images/report_sm.gif" alt="Summary view"> </img> 
              </a>
              -->
             <a href="/vegbank/servlet/DataRequestServlet?requestDataFormatType=html&amp;clientType=browser&amp;requestDataType=vegPlot&amp;resultType=summary&amp;queryType=simple&amp;plotId=<bean:write property="plotId" name="row"/>"> 
	        <img align="center" border="0" src="/vegbank/images/report_sm.gif" alt="Summary view"> </img> 
              </a>

	      <br/>

              <input name="plotName" type="checkbox" value="<bean:write property="plotId" name="row"/>" checked="yes"> 
                <span class="item"> <font color="#333333"> download this plot </font> </span> 
              </input>
            </td>		 
	  </tr>
	 
  
	</logic:iterate>
      </table>
      
  </logic:notEqual>
  
  </font>
  </span>
  
  <!-- VEGBANK FOOTER -->
  @vegbank_footer_html_tworow@
  </body>
</html>