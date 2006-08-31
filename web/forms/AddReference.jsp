@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@
<!-- 
*   '$RCSfile: AddReference.jsp,v $'
*   Purpose: Add a new reference to vegbank
*   Copyright: 2000 Regents of the University of California and the
*               National Center for Ecological Analysis and Synthesis
*   Authors: @author@
*
*  '$Author: mlee $'
*  '$Date: 2006-08-31 05:00:21 $'
*  '$Revision: 1.14 $'
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
* Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA*
-->



<TITLE>Add A Reference Form</TITLE>

 


  <style type="text/css">
  .oddrow { background-color : #FFFFCC }
  .evenrow {background-color : #FFFFFF }
  .headerrow  {background-color : #DDDDDD }
  </style>  
  
  
  

  
  @webpage_masthead_html@ 
  

  <br/>

  <html:errors/>

  <h2>Reference Form -- add a new <a href="/dd/reference">Reference</a></h2>

  <html:form action="/AddReference" onsubmit="return validateAddReferenceForm(this)" >



  
  <!-- table that has the whole package in its one row -->
  <p>Note that you do not need to add the full amount of Alternate Identifiers or Reference Contributors.  Only add those appropriate for the reference you are adding.</p>
  <p><font color="red">*</font>Indicates a required field.</p>
  <p>Click a field label or table name to see more about that field in VegBank's <a href="@datadictionary-index@">data dictionary</a>.  Some fields are described in more detail just below the field.</p>
  <table class="formEntry" cellpadding="5" >
    <tr>
      <!-- Left hand column -->
      <td width="55%" valign="top"><p>  




	<table class="formEntry" cellspacing="0">

	  <!-- one row of data entry -->
	  <tr class="oddrow">
	    <!-- label -->
	    <td class="formLbl"><p><a href="/ddfull/reference/shortname">Short Name</a><font color="red">*</font>:</p></td>
	    <!-- input -->
	    <td>
	      <p>
	        <html:text property="reference.shortname" size="40"/>
	      </p>
	    </td>
	  </tr>




	  <!-- explanation -->
	  <tr class="oddrow"><td colspan="2"><p><span class="item">(a name that is used to quickly identify this reference)</span></p></td></tr>
	  <!-- end row -->

	  <tr>
	    <td class="formLbl"><p><a href="/ddfull/reference/referencetype">Type of Reference:</a></p></td>
	    <td>
        <p>
          <html:select property="reference.referencetype">
            <option value="-1" selected="yes">--pick a referenceType--</option>
            <html:options property="reference.referencetypePickList"/>
          </html:select>
	      </p>
      </td>

	    </tr>

	    <tr class="oddrow">
	      <td class="formLbl"><p><a href="/ddfull/reference/title">Reference Title</a><font color="red">*</font>:</p></td>
	      <td>
		<p>
		<html:text property="reference.title" size="40"/>
		</p>
	      </td>
	    </tr>
	    <tr class="oddrow">
	      <td colspan="2"><p><span class="item">(Official title from the author)</span></p></td>
	    </tr>

	    <tr>
	      <td class="formLbl"><p>
		<a href="/ddfull/reference/titlesuperior">
		Title of Superior Reference:
		</a></p></td>
		<td>
		  <p>
		  <html:text property="reference.titlesuperior" size="40"/>
		  </p>
		</td>
	      </tr>
	      <tr>
		<td colspan="2"><p><span class="item">(This applies if this reference is contained by some other reference, for example if the reference is a chapter, the title of the book)</span></p></td>
	      </tr>

	      <tr class="oddrow">
		<td class="formLbl"><p>
		  <a href="/ddfull/reference/pubdate">
		  Date of Publication:
		  </a></p>
		</td>
		<td>
		  <p>
		  <html:text property="reference.pubdate" size="40"/>
		  </p>
		</td>
	      </tr>

	      <tr>
		<td class="formLbl"><p>
		  <a href="/ddfull/reference/accessdate">
		  Date Accessed:
		  </a></p>
		</td>
		<td>
		  <p>
		  <html:text property="reference.accessdate" size="40"/>
		  </p>
		</td>
	      </tr>
	      <tr>
		<td colspan="2"><p><span class="item">
		  (Date the reference was accessed, useful for references which may change after publication, 
		  e.g. website, database)
		  </p></span>
		</td>
	      </tr>
	      
	      <tr class="oddrow">
		<td class="formLbl"><p>
		  <a href="/ddfull/reference/conferencedate">
		  Date of Conference:
		  </a></p>
		</td>
		<td>
		  <p>
		  <html:text property="reference.conferencedate" size="40"/>
		  </p>
		</td>
	      </tr>

	      <tr>
		<td class="formLbl"><p>
		  <a href="/ddfull/reference/referencejournal_id">
		  Journal:
		  </a></p>
		</td>
		<td>
		  <p>
		  <bean:define id="list" name="AddReferenceForm" property="journals" type="java.util.Collection"/>
		  <html:select property="reference.referencejournal_id">
		    <option value="-1" selected>--pick a journal--</option>
		    <html:options collection="list" labelProperty="name" property="id"/>
		  </html:select>
		  <br/>
		  <!-- link to add new journal to the list -->
		  <a href="@forms_link@AddJournal.jsp">Add a new Journal to Vegbank</a>
		</p>
	      </td>
	    </tr>
	    <tr>
	      <td colspan="2"><p><span class="item">
		If the journal that applies to your reference is not on the list, please press the "add new journal" button to add the journal to our list.
		</p></span>
	      </td>
	    </tr>

	    <tr class="oddrow">
	      <td class="formLbl"><p>
		<a href="/ddfull/reference/volume">
		Volume:
		</a></p>
	      </td>
	      <td>
		<p>
		<html:text property="reference.volume" size="40"/>
		</p>
	      </td>
	    </tr>

	    <tr>
	      <td class="formLbl"><p>
		<a href="/ddfull/reference/issue">
		Issue:
		</a></p>
	      </td>
	      <td>
		<p>
		<html:text property="reference.issue" size="40"/>
		</p>
	      </td>
	    </tr>

	    <tr class="oddrow">
	      <td class="formLbl"><p>
		<a href="/ddfull/reference/pagerange">
		Page Range:
		</a></p>
	      </td>
	      <td>
		<p>
		<html:text property="reference.pagerange" size="40"/>
		</p>
	      </td>
	    </tr>

	    <tr>
	      <td class="formLbl"><p>
		<a href="/ddfull/reference/totalpages">
		Total Number of Pages:
		</a></p>
	      </td>
	      <td>
		<p>
		<html:text property="reference.totalpages" size="40"/>
		</p>
	      </td>
	    </tr>

	    <tr class="oddrow"><td class="formLbl"><p>
	      <a href="/ddfull/reference/publisher">
	      Name of Publisher:
	      </a></p></td>
	      <td>
		<p>
		<html:text property="reference.publisher" size="40"/>
		</p>
	      </td>
	    </tr>

	    <tr><td class="formLbl"><p>
	      <a href="/ddfull/reference/publicationplace">
	      Publisher Location:
	      </a></p></td>
	      <td>
		<p>
	        <html:text property="reference.publicationplace" size="40"/>
		</p>
	      </td>
	    </tr>

	    <tr class="oddrow"><td class="formLbl"><p>
	      <a href="/ddfull/reference/isbn">
	      isbn:
	      </a></p></td>
	      <td>
		<p>
		<html:text property="reference.isbn" size="40"/>
		</p>
	      </td>
	    </tr>
	    <tr class="oddrow">
	      <td colspan="2"><p><span class="item">
		International Standard Book Number
		</p></span>
	      </td>
	    </tr>

	    <tr><td class="formLbl"><p>
	      <a href="/ddfull/reference/edition">
	      Edition:
	      </a></p></td>
	      <td>
		<p>
		<html:text property="reference.edition" size="40"/>
		</p>
	      </td>
	    </tr>

	    <tr class="oddrow"><td class="formLbl"><p>
	      <a href="/ddfull/reference/numberofvolumes">
	      Number of Volumes:
	      </a></p></td>
	      <td>
		<p>
		<html:text property="reference.numberofvolumes" size="40"/>
		</p>
	      </td>
	    </tr>

	    <tr><td class="formLbl"><p>
	      <a href="/ddfull/reference/chapternumber">
	      Chapter:
	      </a></p></td>
	      <td>
		<p>
		<html:text property="reference.chapternumber" size="40"/>
		</p>
	      </td>
	    </tr>

	    <tr class="oddrow"><td class="formLbl"><p>
	      <a href="/ddfull/reference/reportnumber">
	      Report Number:
	      </a></p></td>
	      <td>
		<p>
		<html:text property="reference.reportnumber" size="40"/>
		</p>
	      </td>
	    </tr>
	    <tr class="oddrow">
	      <td colspan="2"><p><span class="item">
		If this reference is a report, the unique number assigned to the report by issuing institution.
		</p></span>
	      </td>
	    </tr>

	    <tr><td class="formLbl"><p>
	      <a href="/ddfull/reference/communicationtype">
	      Type of Personal Communication:
	      </a></p></td>
	      <td>
		<p>
		<html:text property="reference.communicationtype" size="40"/>
		</p>
	      </td>
	    </tr>
	    <tr>
	      <td colspan="2"><p><span class="item">
		Could be an email, letter, memo, transcript of conversation either hardcopy or online.
		</p></span>
	      </td>
	    </tr>

	    <tr class="oddrow"><td class="formLbl"><p>
	      <a href="/ddfull/reference/degree">
	      Thesis Degree:
	      </a></p></td>
	      <td>
		<p>
		<html:text property="reference.degree" size="40"/>
		</p>
	      </td>
	    </tr>
	    <tr class="oddrow">
	      <td colspan="2"><p><span class="item">
		If this reference is a thesis, this field is used to describe the name or degree level 
		for which the thesis was completed.
		</p></span>
	      </td>
	    </tr>

	    <tr><td class="formLbl"><p>
	      <a href="/ddfull/reference/url">
	      URL:
	      </a></p></td>
	      <td>
		<p>
		<html:text property="reference.url" size="40"/>
		</p>
	      </td>
	    </tr>
	    <tr>
	      <td colspan="2"><p><span class="item">
		(Uniform Resource Locator) from which this resource can be downloaded or additional information can be obtained.
		</p></span>
	      </td>
	    </tr>

	    <tr class="oddrow"><td class="formLbl"><p>
	      <a href="/ddfull/reference/doi">DOI:
	      </a></p></td>
	      <td>
		<p>
		<html:text property="reference.doi" size="40"/>
		</p>
	      </td>
	    </tr>
	    <tr class="oddrow">
	      <td colspan="2"><p><span class="item">
		Digital Object Identifier
		</p></span>
	      </td>
	    </tr>

	    <tr><td class="formLbl"><p>
	      <a href="/ddfull/reference/additionalinfo">Additional Information:
	      </a></p></td>
	      <td>
		<p>
		<html:textarea property="reference.additionalinfo" rows="4" cols="40"/>
		</p>
	      </td>
	    </tr>
	    <tr>
	      <td colspan="2"><p><span class="item">
		Any information that is not captured by the other reference fields.
		</p></span>
	      </td>
	    </tr>

	  </table>






	
	
	<br/>
	<br/>

	<b>
	<a href="/dd/referencealtident">Alternate Identifiers for this Reference:</a></b>
	<br/>
	<span class="item">
	Additional identifiers that allow references to be specified.  You must specify a system that is uniquely yours, such as a URL for
	your data management system.  For example: "http://metacat.somewhere.org/photos/album941" or "mailto://joe@address.com/books/classics"
	<br/>
	<b>You need not add any alternate identifiers, but if you do add them, both system and identifier must be filled in. </b>
	Please only add alternate identifiers that you plan on using with the VegBank system.
	</span>


	<!-- table to contain referenceAltIdent -->
	<table border="0" cellpadding="0" cellspacing="0">
	  <tr class="headerrow">
	    <th><p>num</p></th>
	    <th>
	      <p>
	      <a href="/ddfull/referencealtident/system">system</a>
	      <font color="red">*</font>
	      <p>
	    </th>
	    <th>
	      <p>
	      <a href="/ddfull/referencealtident/identifier">identifier</a>
	      <font color="red">*</font>
	      </p>
	    </th>
	  </tr>
	  <%
	  for (int i=0; i<5 ; i++)
	  {
	  %>
	  <tr>
	    <td class="oddrow"><p><%= i+1 %></p></td>
	    
	    <td>
	      <p>
	      <html:text property='<%= "system[" + i + "]" %>' size="35"/>
	      </p>
	    </td>
	    <td>
	      <p>
	      <html:text property='<%= "identifier[" + i + "]" %>' size="35"/>
	      </p>
	    </td>
	  </tr>
	  <%
	  }
	  %>
	</table>
	<!-- End of Alternative Identifiers table -->
	
	<br/>
	<br/>
	



	<p><b>
	<a href="/dd/referencecontributor">
	Authors and other Reference Contributors:
	</a></b>
	<br />
	<span class="item">
	Authors and other parties who contribute to a reference should be specified here, using the "order" field
	to indicate the order in which the authors should appear with the reference.  <br />
	For each party, use "LastName, GivenName(s)" format, for example (without quotes) : "Smith, Reid P." <br />
	"Role" is used to document how the party contributed to the reference.<br />
	<b>Please add only the authors and reference contributors necessary for this reference.  You do not need to fill in all rows.</b>
	</span>
	</p>

	<!-- ReferenceContributors and Reference Party Form -->
	<table border="0" cellpadding="0" cellspacing="0">
	  <tr class="headerrow">
	    <th><p><a href="/ddfull/referencecontributor/position">order</a></p></th>
	    <th><p><a href="/ddfull/referencecontributor/referenceparty_id">Party Name</a><font color="red">*</font></p></th>
	    <th><p><a href="/ddfull/referencecontributor/roletype">role</a></p></th>
	  </tr>
	<%
	  for (int i=0; i<10 ; i++)
	  {
	%>
	<tr>
	  <td class="oddrow"><p><%= i + 1 %></p></td> 
	  <td>
	    <p>
	    <html:text property='<%= "party[" + i + "]" %>' size="35"/>

	    </p>
	  </td>
    <td>
	    <p>
          <html:select property='<%= "roletype[" + i + "]" %>'>
            <option value="-1">--please choose a role--</option>
            <html:options property="roleTypes"/> 
          </html:select>
		    </p>
	  </td>
	</tr>
	<%
	}
	%>
	
	</table>
	<!-- End ReferenceContributors and Reference Party Form -->	
	
      </td>
      <!-- End Right hand column -->
    </tr>
  </table>

  <html:submit property="submit" value="--add this reference to VegBank--" />

  </html:form>



  <!-- VEGBANK FOOTER -->
  
  <!-- END FOOTER -->
 
  
  @webpage_footer_html@
