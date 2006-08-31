
@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@
<!--
*   '$RCSfile: AddParty.jsp,v $'
*   Purpose: Add a new reference to vegbank
*   Copyright: 2000 Regents of the University of California and the
*               National Center for Ecological Analysis and Synthesis
*   Authors: @author@
*
*  '$Author: mlee $'
*  '$Date: 2006-08-31 05:00:21 $'
*  '$Revision: 1.12 $'
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



<title>Add A Party Form</title>


  

  <style type="text/css">
  .oddrow { background-color : #FFFFCC }
  .evenrow {background-color : #FFFFFF }
  .headerrow  {background-color : #DDDDDD }
  </style>  
  
  

  

  
  @webpage_masthead_html@
  

  <br/>

  <html:errors/>


  <h2>Party Form -- add a new Party</h2>
  <p><span class="item">Use this form to add a new Party to VegBank.</span></p>
  <p><span class="item">Click a field label or table name to see more about that field in VegBank's <a href="@datadictionary-index@">data dictionary</a>.  Some fields are described in more detail just below the field.<br/>

  <font color="red">*</font>Indicates a required field.</p>

  <html:form action="/AddParty" onsubmit="return validateAddPartyMethodForm(this)">

  <!-- table that has the whole package in its one row -->
  <table  class="formEntry" cellpadding="5">
    <tr>
      <td width="45%" valign="top">  

	<!-- Party Primary info table -->

	<table class="formEntry">

	  <tr>
	    <td class="formLbl">
	      <p><a href="/ddfull/party/salutation">Salutation:</a></p>
	    </td>
	    <td>
	      <html:text property="party.salutation" size="40"/>
	    </td>
	  </tr>

	  <tr>
	    <td class="formLbl">
	      <p><a href="/ddfull/party/salutation">First Name:</a></p> 
	    </td>
	    <td>
	      <html:text property="party.givenname" size="40"/>
	    </td>
	  </tr>

	  <tr>
	    <td class="formLbl">
	      <p><a href="/ddfull/party/middlename">Middle Name:</a></p> 
	    </td>
	    <td>
	      <html:text property="party.middlename" size="40"/>
	    </td>
	  </tr>

	  <tr>
	    <td class="formLbl">
	      <p>
	      <a href="/ddfull/party/surname">Last Name:</a>
	      <font color="red">*</font>
	      </p> 
	    </td>
	    <td>
	      <html:text property="party.surname" size="40"/>
	    </td>
	  </tr>

	  <tr>
	    <td class="formLbl">
	      <p>
	      <a href="/ddfull/party/organizationname">Organization:</a></p> 
	    </td>
	    <td>
	      <html:text property="party.organizationname" size="40"/>
	    </td>
	  </tr>

	  <tr>
	    <td class="formLbl">
	      <p><a href="/ddfull/party/contactinstructions">Contact Instructions:</a></p> 
	    </td>
	    <td>
	      <html:textarea property="party.contactinstructions" rows="3" cols="40"/>
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
	<b>Party <a href="/dd/telephone">Telephone Numbers</a>:</b>
	<br/>
	<span class="item">Note that you do not need to add any telephone numbers for a new party.  If you add telephone numbers, both the "phone number" and "phone type" are required fields.</span>
	</p>


	<!-- Telephone Table -->
	<table border="0" cellpadding="0" cellspacing="0">
	  <tr class="headerrow">
	    <th><p>num</p></th>
	    <th><p><a href="/ddfull/telephone/phonenumber">phone number</a><font color="red">*</font></p></th>
	    <th><p><a href="/ddfull/telephone/phonetype">phone type</a><font color="red">*</font></p></th>
	  </tr>
	  
	  <!-- Telephone Control -->
	  <%
	  for (int i=0; i<4 ; i++)
	  {
	  %>

	  <tr>
	    <td class="oddrow"><%= i + 1 %></td>
	    <td>
        <html:text property='<%= "phoneNumber[" + i + "]" %>' size="30" maxlength="30"/>
      </td>
	    <td> 
	      <html:select property='<%= "phoneType[" + i + "]" %>'>
		      <option value="-1" selected="yes">--choose type--</option>
          <html:options property="phoneTypes"/>
	      </html:select>
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
	<a href="/dd/address">Addresses</a>:</span>

	<br/>
	
	<table border="0" cellpadding="0" cellspacing="0">

	  <!-- The address control -->
	  <tr class="oddrow">
	    <td>&nbsp;</td>
	    <td nowrap><p>
	      <a href="/ddfull/address/orgposition"> 
	      Organization Position</a>:</p>
	    </td> 
	    <td>
	      <html:text property="address.orgposition" size="35"/>
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
	    <a href="/ddfull/address/email"> 
	    Email</a>:</p>
	  </td> 
	  <td>
	    <html:text property="address.email" size="35"/>
	  </td>
	</tr>
	<tr class="oddrow">
	  <td>&nbsp;</td>
	  <td nowrap><p>
	    <a href="/ddfull/address/deliverypoint"> 
	    Street Address</a>:</p>
	  </td> 
	  <td>
	    <html:text property="address.deliverypoint" size="35"/>
	  </td>
	</tr>
	<tr class="oddrow">
	  <td>&nbsp;</td>
	  <td nowrap>
	    <p><a href="/ddfull/address/city"> City</a>:</p>
	  </td> 
	  <td>
	    <html:text property="address.city" size="35"/>
	  </td>
	</tr>
	<tr class="oddrow">
	  <td>&nbsp;</td>
	  <td nowrap>
	    <p><a href="/ddfull/address/administrativearea">State/Province</a>:</p>
	  </td> 
	  <td>
	    <html:text property="address.administrativearea" size="35"/>
	  </td>
	</tr>
	<tr class="oddrow">
	  <td>&nbsp;</td>
	  <td nowrap><p>
	    <a href="/ddfull/address/postalcode"> 
	    ZIP/Postal Code</a>:</p>
	  </td> 
	  <td>
	    <html:text property="address.postalcode" size="35"/>
	  </td>
	</tr>
	<tr class="oddrow">
	  <td>&nbsp;</td>
	  <td nowrap>
	    <p><a href="/ddfull/address/country"> 
	    Country</a>:</p>
	  </td> 
	  <td>
	    <html:text property="address.country" size="35"/>
	  </td>
	</tr>
	
	<tr class="oddrow">
	  <td>&nbsp;</td>
	  <td nowrap>
	    <p><a href="/ddfull/address/addressstartdate"> 
	    Start Date for Address</a>:</p>
	  </td> 
	  <td>
	    <html:text property="address.addressstartdate" size="35"/>
	  </td>
	</tr>

  <tr class="oddrow">
    <td>&nbsp;</td>
    <td nowrap><p>
      <a href="/ddfull/address/currentflag"> 
      Address is current?</a></p>
    </td> 
    <td>
      <html:checkbox property="currentFlag" value="true"/>
    </td>
  </tr>

    </table>
    <!-- big surrounding table -->



    
  </td>
</tr>
</table>

  <html:submit property="submit" value="--add this party to VegBank--" />

</html:form>

<!-- VEGBANK FOOTER -->

<!-- END FOOTER -->



@webpage_footer_html@
