<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<HTML>
<!-- 
*   '$RCSfile: AddCoverMethod.jsp,v $'
*   Purpose: Add a new reference to vegbank
*   Copyright: 2000 Regents of the University of California and the
*               National Center for Ecological Analysis and Synthesis
*   Authors: @author@
*
*  '$Author: farrell $'
*  '$Date: 2003-05-12 23:12:03 $'
*  '$Revision: 1.2 $'
*
*
-->
<head>

<title>Add A Cover Method Form</title>
<link rel="stylesheet" href="@stylesheet@" type="text/css"/> 
  <style type="text/css">
  .oddrow { background-color : #FFFFCC }
  .evenrow {background-color : #FFFFFF }
  </style>  
  <meta http-equiv="Content-Type" content="text/html; charset=">
  </head>

  <body>

  <!--xxx -->
  @vegbank_header_html_normal@ 
  <!--xxx -->
  
  <br/>

  <html:errors/>

  <h2>Cover Method Form -- add a new <a href="/vegbank/dbdictionary/dd~table~covermethod~type~tableview.html">Cover method</a></h2>
  <blockquote><p>Note that you do not need to add the full amount of Cover Indexes.  Only add the number of indexes that are appropriate for the method you are adding.</br>
  <font color="red">*</font>Indicates a required field.</p>
  </blockquote>

  <html:form action="/AddCoverMethod" onsubmit="return validateAddCoverMethodForm(this)" >

  <table class="formEntry">
    <tr>
      <td class="formLbl">
	<p><span class="category">
	<a href="/vegbank/dbdictionary/dd~table~covermethod~field~covertype~type~tableview.html">Cover Method Name</a>:
	<font color="red">*</font></span>
	</p>
      </td>
      <td><html:text property="coverMethod.coverType" maxlength="30" size="30" /></td>
    </tr>
    
    <tr>
      <td colspan="2">
	<table>
	  <tr>
	    <td class="formLbl">reference:</td>
	    <td> 

	      <bean:define id="list" name="AddCoverMethodForm" property="references" type="java.util.Vector"/>
	      <html:select property="coverMethod.reference_ID">
        <option value="-1">--select a reference--</option>
	      <html:options collection="list" labelProperty="title" property="id"/>
	      </html:select>
	    </td>
	  </tr>
	  <tr>
	    <td>
	      &nbsp;
	    </td>
	    <td>
	      <a href="/vegbank/forms/AddReference.jsp">Add a new reference</a>
	    </td>
	  </tr>
	</table>
      </td>
    </tr>
    <tr>
      <td colspan="2">
	<p>
	<span class="category">
	<a href="/vegbank/dbdictionary/dd~table~coverindex~type~tableview.html">Cover Indexes</a> for this Cover Method:
	</span>
	</p>
      </td>
    </tr>
    <tr>
      <td>&nbsp;</td>
      <td>

	<table class="formEntry">
	  <tr class="grey">
	    <td rowspan="2">
	      <p><span class="category">num</span></p>
	    </td>
	    <td rowspan="2" class="formLbl">
	      <p>
	      <span class="category">
	      <a href="/vegbank/dbdictionary/dd~table~coverindex~field~covercode~type~tableview.html">Cover Code</a><font color="red">*</font>
	      </span>
	      </p>
	    </td>
	    <td colspan="2" class="formLbl"><p><span class="category">Cover Class Range</span></p></td>
	    <td rowspan="2" class="formLbl">
	      <p>
	      <span class="category">
	      <a href="/vegbank/dbdictionary/dd~table~coverindex~field~coverpercent~type~tableview.html">Cover Class Percent</a>
	      <font color="red">*</font>
	      </span><br />
	      <span class="item">estimated midpoint of class</span>
	      </p>
	    </td>
	    <td rowspan="2" class="formLbl">
	      <p>
	      <span class="category"><a href="/vegbank/dbdictionary/dd~table~coverindex~field~indexdescription~type~tableview.html">Index Description</a>
	      </span>
	      </p>
	    </td>
	  </tr>
	  <tr class="grey">
	    <td class="formLbl">
	      <p><span class="category"><a href="/vegbank/dbdictionary/dd~table~coverindex~field~lowerlimit~type~tableview.html">lower %</a></span></p>
	    </td>
	    <td class="formLbl">
	      <p><span class="category"><a href="/vegbank/dbdictionary/dd~table~coverindex~field~upperlimit~type~tableview.html">upper %</a></span></p>
	    </td>
	  </tr>

	  <%
	  for (int i=0; i<20 ; i++)
	  {
	  %>
	  <tr>
	    <td class="oddrow"><p><%= i+1 %></p></td>

	    <td><html:text property='<%= "coverCode[" + i + "]" %>' maxlength="10" size="10" /></td>
	    <td><html:text property='<%= "lowerLimit[" + i + "]" %>' maxlength="10" size="10" /></td>
	    <td><html:text property='<%= "upperLimit[" + i + "]" %>' maxlength="10" size="10" /></td>
	    <td><html:text property='<%= "coverPercent[" + i + "]" %>' maxlength="10" size="10" /></td>
	    <td><html:text property='<%= "indexDescription[" + i + "]" %>' maxlength="10" size="10" /></td>

	  </tr>
	  <%
	  }
	  %>
	  

	</table>
      </td>
    </tr>
  </table>

  <html:submit property="submit" value="--add this cover method to VegBank--" />
  
  </html:form>

  <!-- VEGBANK FOOTER -->
  @vegbank_footer_html_tworow@
  <!-- END FOOTER -->

  </body>
  </html>
