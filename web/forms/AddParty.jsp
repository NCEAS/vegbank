<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<HTML>
<!--
*   '$RCSfile: AddParty.jsp,v $'
*   Purpose: Add a new reference to vegbank
*   Copyright: 2000 Regents of the University of California and the
*               National Center for Ecological Analysis and Synthesis
*   Authors: @author@
*
*  '$Author: farrell $'
*  '$Date: 2003-05-10 00:08:25 $'
*  '$Revision: 1.1 $'
*
*
-->

<head>

<title>Add A Party Form</title>
<!--xxx  -->
<link rel="stylesheet" href="/vegbank/includes/default.css" type="text/css">
  <!-- xxx-->

  <style type="text/css">
  .oddrow { background-color : #FFFFCC }
  .evenrow {background-color : #FFFFFF }
  .headerrow  {background-color : #DDDDDD }
  </style>  
  <meta http-equiv="Content-Type" content="text/html; charset=">
  </head>

  <body>

  <!--xxx -->
  @vegbank_header_html_normal@
  <!--xxx -->

  <br/>

  <html:errors/>


  <h2>Party Form -- add a new Party</h2>
  <p><span class="item">Use this form to add a new Party to VegBank.</span></p>
  <p><span class="item">Click a field label or table name to see more about that field in VegBank's <a href="/vegbank/dbdictionary/dd-index.html">data dictionary</a>.  Some fields are described in more detail just below the field.<br/>

  <font color="red">*</font>Indicates a required field.</p>
  <p><font color="blue">Don't know what to do about address.oganization_ID -- picklist of many parties or just let them fill in the org name and we'll see if we can find it in party?</font></p>

  <html:form action="/AddParty" onsubmit="return validateAddPartyMethodForm(this)">

  <!-- table that has the whole package in its one row -->
  <table width="799" class="formEntry" cellpadding="5">
    <tr>
      <td width="45%" valign="top">  

	<!-- Party Primary info table -->

	<table class="formEntry">

	  <tr>
	    <td class="formLbl">
	      <p><a href="/vegbank/dbdictionary/dd~table~party~field~salutation~type~tableview.html">Salutation:</a></p>
	    </td>
	    <td>
	      <html:text property="party.salutation" size="40"/>
	    </td>
	  </tr>

	  <tr>
	    <td class="formLbl">
	      <p><a href="/vegbank/dbdictionary/dd~table~party~field~salutation~type~tableview.html">First Name:</a></p> 
	    </td>
	    <td>
	      <html:text property="party.givenName" size="40"/>
	    </td>
	  </tr>

	  <tr>
	    <td class="formLbl">
	      <p><a href="/vegbank/dbdictionary/dd~table~party~field~middlename~type~tableview.html">Middle Name:</a></p> 
	    </td>
	    <td>
	      <html:text property="party.middleName" size="40"/>
	    </td>
	  </tr>

	  <tr>
	    <td class="formLbl">
	      <p>
	      <a href="/vegbank/dbdictionary/dd~table~party~field~surname~type~tableview.html">Last Name:</a>
	      <font color="red">*</font>
	      </p> 
	    </td>
	    <td>
	      <html:text property="party.surName" size="40"/>
	    </td>
	  </tr>

	  <tr>
	    <td class="formLbl">
	      <p>
	      <a href="/vegbank/dbdictionary/dd~table~party~field~organizationname~type~tableview.html">Organization:</a></p> 
	    </td>
	    <td>
	      <html:text property="party.organizationName" size="40"/>
	    </td>
	  </tr>

	  <tr>
	    <td class="formLbl">
	      <p><a href="/vegbank/dbdictionary/dd~table~party~field~contactinstructions~type~tableview.html">Contact Instructions:</a></p> 
	    </td>
	    <td>
	      <html:textarea property="party.contactInstructions" rows="3" cols="40"/>
	    </td>
	  </tr>

	  <!-- <tr><td class="formLbl">owner:</td><td> -->
	    <!-- GF: if the following = 1 (owned by me) then the owner will be the PK of the party that is the current user
	    Otherwise, the owner will be the party_ID of the same record (ie the new party owns itself) -->
	    <!-- <select name="party.owner">
	      <option value="1" selected>owned by me</option>
	      <option value="2">owned by self</option>
	    </select>
	  </td></tr> -->

	</table>


	<!-- End Party Primary info table -->
	

	<p>
	<b>Party<a href="/vegbank/dbdictionary/dd~table~telephone~type~tableview.html">Telephone Numbers</a>:</b>
	<br/>
	<span class="item">Note that you do not need to add any telephone numbers for a new party.  If you add telephone numbers, both the "phone number" and "phone type" are required fields.</span>
	</p>


	<!-- Telephone Table -->
	<table border="0" cellpadding="0" cellspacing="0">
	  <tr class="headerrow">
	    <th><p>num</p></th>
	    <th><p><a href="/vegbank/dbdictionary/dd~table~telephone~field~phonenumber~type~tableview.html">phone number</a><font color="red">*</font></p></th>
	    <th><p><a href="/vegbank/dbdictionary/dd~table~telephone~field~phonetype~type~tableview.html">phone type</a><font color="red">*</font></p></th>
	  </tr>
	  
	  <!-- Telephone Control -->
	  <%
	  for (int i=0; i<4 ; i++)
	  {
	  %>

	  <tr>
	    <td class="oddrow"><%= i + 1 %></td>
	    <td><html:text property='<%= "phoneNumber[" + i + "]" %>' size="30" maxlength="30"/></td>
	    <td> 
	      <select name="phoneType">
		<option value="-1" selected="yes">--choose type--</option>
		<option>Work</option>
		<option>Home</option>
		<option>Cell</option>
		<option>Fax</option>
		<option>Secretary</option>
		<option>Not specified</option>
	      </select>
	    </td>
	  </tr>
	  <%
	  }
	  %>

	  
	</table>
	<!-- End Telephone Table -->

	
	
      </td>
      <!-- blank column in between left and right panels -->
      <td width="10%">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
      <td width="45%" valign="top" align="left">

	<!-- table to contain addresses -->
	<p><span class="category">Party 
	<a href="/vegbank/dbdictionary/dd~table~address~type~tableview.html">Addresses</a>:</span>

	<br />
	<span class="item">
	You may specify up to two addresses for the new party you are adding (though you do not need to specify any).
	If you need more than two addresses, you can add this party with two address, then edit the party and add more addresses.
	</span>
	</p>
	
	<table border="0" cellpadding="0" cellspacing="0">

	  <!-- The addresses control -->

	  <%
	  for (int i=0; i<2 ; i++)
	  {
	  %>
	  
	  <tr>
	    <td colspan="3" class="oddrow">
	      <p><span class="category">ADDRESS <%= i + 1 %>:</span></p>
	    </td>
	  </tr>

	  

	  <tr class="oddrow">
	    <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
	    <td nowrap><p>
	      <a href="/vegbank/dbdictionary/dd~table~address~field~organization_id~type~tableview.html">  
	      Organization</a>:</p>
	    </td>
	    <td>
	      <html:text property='<%= "organization_ID[" + i + "]" %>' size="35"/>
	    </td>
	  </tr>
	  <tr class="oddrow">
	    <td>&nbsp;</td>
	    <td nowrap><p>
	      <a href="/vegbank/dbdictionary/dd~table~address~field~orgposition~type~tableview.html"> 
	      Organization Position</a>:</p>
	    </td> 
	    <td>
	      <html:text property='<%= "orgPosition[" + i + "]" %>' size="35"/>
	    </td>
	  </tr>
	  <!-- explanation -->
	  <tr class="oddrow">
	    <td>&nbsp;</td>
	    <td colspan="2">
	      <p><span class="item">(the party's position within his/her organization)</span></p>
	  </tr>
	  <tr class="oddrow"><td></td>
	  <td nowrap><p>
	    <a href="/vegbank/dbdictionary/dd~table~address~field~email~type~tableview.html"> 
	    Email</a>:</p>
	  </td> 
	  <td>
	    <html:text property='<%= "email[" + i + "]" %>' size="35"/>
	  </td>
	</tr>
	<tr class="oddrow">
	  <td>&nbsp;</td>
	  <td nowrap><p>
	    <a href="/vegbank/dbdictionary/dd~table~address~field~deliverypoint~type~tableview.html"> 
	    Street Address</a>:</p>
	  </td> 
	  <td>
	    <html:text property='<%= "deliveryPoint[" + i + "]" %>' size="35"/>
	  </td>
	</tr>
	<tr class="oddrow">
	  <td>&nbsp;</td>
	  <td nowrap>
	    <p><a href="/vegbank/dbdictionary/dd~table~address~field~city~type~tableview.html"> City</a>:</p>
	  </td> 
	  <td>
	    <html:text property='<%= "city[" + i + "]" %>' size="35"/>
	  </td>
	</tr>
	<tr class="oddrow">
	  <td>&nbsp;</td>
	  <td nowrap>
	    <p><a href="/vegbank/dbdictionary/dd~table~address~field~administrativearea~type~tableview.html">State/Province</a>:</p>
	  </td> 
	  <td>
	    <html:text property='<%= "administrativeArea[" + i + "]" %>' size="35"/>
	  </td>
	</tr>
	<tr class="oddrow">
	  <td>&nbsp;</td>
	  <td nowrap><p>
	    <a href="/vegbank/dbdictionary/dd~table~address~field~postalcode~type~tableview.html"> 
	    ZIP/Postal Code</a>:</p>
	  </td> 
	  <td>
	    <html:text property='<%= "postalCode[" + i + "]" %>' size="35"/>
	  </td>
	</tr>
	<tr class="oddrow">
	  <td>&nbsp;</td>
	  <td nowrap>
	    <p><a href="/vegbank/dbdictionary/dd~table~address~field~country~type~tableview.html"> 
	    Country</a>:</p>
	  </td> 
	  <td>
	    <html:text property='<%= "country[" + i + "]" %>' size="35"/>
	  </td>
	</tr>
	
	<tr class="oddrow">
	  <td>&nbsp;</td>
	  <td nowrap>
	    <p><a href="/vegbank/dbdictionary/dd~table~address~field~addressstartdate~type~tableview.html"> 
	    Start Date for Address</a>:</p>
	  </td> 
	  <td>
	    <html:text property='<%= "startDate[" + i + "]" %>' size="35"/>
	  </td>
	</tr>
	<%
	}
	%>

    </table>
    <!-- big surrounding table -->



    
  </td>
</tr>
</table>

  <html:submit property="submit" value="--add this party to VegBank--" />

</html:form>

<!-- VEGBANK FOOTER -->
@vegbank_footer_html_tworow@
<!-- END FOOTER -->


</body>
</html>
