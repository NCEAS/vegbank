<%@ page contentType="text/html;charset=UTF-8" language="java" 
  import="org.apache.commons.beanutils.RowSetDynaClass" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

  <!-- format the word record to be plural if not 1 record -->
  <bean:size id="QueryResultsSize" name="QueryResults" property="rows" />
  <logic:equal value="1" name="QueryResultsSize">
    <span class="category"><font color="red">
      There was one plant that matched your search criteria:<br/>
    </font></span>
  </logic:equal>
  <logic:notEqual name="QueryResultsSize" value="1">
    <span class="category">
      <font color="red">
	<bean:write name="QueryResultsSize"/> plants matched your search criteria:<br/>
      </font>
    </span>
  </logic:notEqual>

<!-- set up a table -->
<table cellspacing="0" cellpadding="1">
	<tr>
		<td>&nbsp; &nbsp; &nbsp; &nbsp; </td>
		<td bgcolor="#000000">

       <table width="650" cellspacing="0" cellpadding="0">


	 <tr bgcolor="#336633" align="left" valign="top">
	   <th align="center" nowrap rowspan="1"> SEARCH<br>RESULTS </th>
<!-- MTL: manually setting these properties, so that I can arrange order and such here            <logic:iterate id="heading" name="QueryResults" property="dynaProperties"  indexId="dynaPropertyId">

<%
RowSetDynaClass queryResults = (RowSetDynaClass) request.getAttribute("QueryResults");
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
           </logic:iterate> -->
          <th align="center" valign="left" nowrap>
                   Scientific Name
          </th>
          <th align="center" valign="left" nowrap>
                   English Common Name
          </th>
          <th align="center" valign="left" nowrap>
                   Code
          </th>

           
	 </tr>


	 <%
	 //**************************************************************************************
	 //  Set up alternating row colors
	 //**************************************************************************************
	 boolean toggle = true;
	 String rowClass, marginBgColor;
	
	 %>
	
	 <logic:iterate id="row" name="QueryResults" property="rows" >

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
	    <th width="20%" bgcolor="<%= marginBgColor %>" align="center" nowrap rowspan="2">
	      <html:link page="/GenericDispatcher.do?command=RetrieveVBModelBean&jsp=GenericDisplay.jsp&rootEntity=PlantConcept" title="summary report" paramId="accessionCode" paramName="row" paramId="accessionCode" paramProperty="accessioncode">
                <img align="center" src="@image_server@report_sm.gif" alt="Summary view"></img>
              </html:link>
              <html:link page="/DisplayEntity.do?resultType=rawXML" paramId="accessionCode" paramName="row" paramProperty="accessioncode" title="view raw XML">
                <img align="center" border="0" src="@image_server@xml_icon.gif" alt="Raw XML view"></img>
              </html:link>
              
              <!-- USDA link to plant --><!-- only if code isn't empty -->
        <logic:empty name="row" property="code">
         <!-- <img align="center" border="2" src="@image_server@leaficon.gif" alt="No USDA Plants page available" ></img> -->
       </logic:empty>
    <logic:notEmpty name="row" property="code">

      <html:link href="http://plants.usda.gov/cgi_bin/plant_search.cgi?mode=Symbol&go=go" paramId="keywordquery" paramName="row" paramProperty="code" title="View USDA Plants page" target="_new">
                <img align="center" border="0" src="@image_server@leaficon.gif" alt="View USDA Plants page"></img>
       </html:link>
    </logic:notEmpty>
    
        
            </th>
	     
   <!-- MTL: manually setting these properties, so that I can arrange order and such here          <logic:iterate id="column" name="QueryResults" property="dynaProperties" indexId="dynaPropertyId"> 
<%
// Apologies for this hack, could not find a syntatically neat way to do this, 
// I want to get the name of the current column to use to access the property 
// of the dynabean. The Dynabean is in the request and the dynaPropertyId is 
// incremented on iteration of the logic:iterate loop
String columnName  
  = ( (RowSetDynaClass) request.getAttribute("QueryResults")).getDynaProperties()[dynaPropertyId.intValue()].getName();

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
            </logic:iterate> -->
            
     <td align="left" valign="middle">
       <logic:empty name="row" property="scientific name">
         <span class="itemsmaller">[n/a]</span>
       </logic:empty>
       <logic:notEmpty name="row" property="scientific name">
         <span class="item">
           <bean:write	name="row" property="scientific name" />
         </span>
       </logic:notEmpty>
     </td>
     
      <td align="left" valign="middle">
     <logic:empty name="row" property="english common name">
         <span class="itemsmaller">[n/a]</span>
       </logic:empty>
    <logic:notEmpty name="row" property="english common name">
       <span class="item">
          <bean:write	name="row" property="english common name" />
       </span>
     </logic:notEmpty>  
     </td>
     
        <td align="left" valign="middle">
         <logic:empty name="row" property="code">
         <span class="itemsmaller">[n/a]</span>
       </logic:empty>
    <logic:notEmpty name="row" property="code">

       <span class="item">
          <bean:write	name="row" property="code" />
       </span>
    </logic:notEmpty>
     </td>
     
	  </tr>
	  <tr class="<%= rowClass %>">
	    <td colspan="20" align="right"><!-- accessionCode --><span title="This is the VegBank plant concept accession code." class="itemsmaller"><bean:write name="row" property="accessioncode" /></span></td>
	  </tr>
	 
	  <tr bgcolor="#666666">
         <td colspan="20"><img src="transparent.gif" height="1" width="1"></td>		 
	  </tr>
	</logic:iterate>

	 <tr bgcolor="#336633">
	   <td colspan="20">&nbsp; </td>
	 </tr>
	 <tr bgcolor="#336633">
	   <td colspan="20" align="right" class="grey"><span class="itemsmaller">Note: VegBank Accession Codes appear below each plant.  These uniquely identify each plant concept.</span></td>
	 </tr>
      </table>
      
		</td>
	</tr>
</table> 