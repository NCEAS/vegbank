

@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@
<!-- 
*   '$RCSfile: AddCoverMethod.jsp,v $'
*   Purpose: Add a new reference to vegbank
*   Copyright: 2000 Regents of the University of California and the
*               National Center for Ecological Analysis and Synthesis
*   Authors: @author@
*
*  '$Author: mlee $'
*  '$Date: 2006-08-31 05:00:21 $'
*  '$Revision: 1.14 $'
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


<title>Add A Cover Method Form</title>
 
  <style type="text/css">
  .oddrow { background-color : #FFFFCC }
  .evenrow {background-color : #FFFFFF }
  </style>  
  
  

  

  
  @webpage_masthead_html@ 
  
  
  <br/>

  <html:errors/>

  <h2>Cover Method Form -- add a new <a href="/dd/covermethod">Cover method</a></h2>
  <blockquote>
  <p>Notes: (1) We encourage use of established cover methods whenever possible so 
as to maximize the consistency among plot records. <br />(2) The number of cover 
codes varies between methods; it is not necessary to fill out all of the lines 
in the form. <br />(3) Cover class ranges should not overlap, although the upper 
limit of one class should generally equal the lower limit of the next, and the 
absolute range should generally be 0-100. <br />(4) Some traditional cover methods 
have one or more low values without  numerical cover limits.  For these it is 
nonetheless REQUIRED to assign at least a midpoint value; the traditional 
definition can be provided in the Index Description. <br />(5) The midpoint is used 
in all between plot comparisons and for conversions between methods in 
aggregating plots for analysis.  There is no a priori correct definition of 
the midpoint.  As a default the mean of the upper and lower limits is 
acceptable, although this does embed a bias in that cover classes generally 
tend to have more observations from the lower half of the range.  A geometric 
mean might be better, though there is at this time no theoretically 
compelling reason for such a selection. The choice is left to the author of 
the method.
  <br />
  <font color="red">*</font>Indicates a required field.</p>

  </blockquote>

  <html:form action="/AddCoverMethod" onsubmit="return validateAddCoverMethodForm(this)" >

  <table class="formEntry">
    <tr>
      <td class="formLbl">
	<p><span class="category">
	<a href="/ddfull/covermethod/covertype">Cover Method Name</a>:
	<font color="red">*</font></span>
	</p>
      </td>
      <td><html:text property="coverMethod.covertype" maxlength="30" size="30" /></td>
    </tr>
    
    <tr>
      <td colspan="2">
	<table>
	  <tr>
	    <td class="formLbl">reference:</td>
	    <td> 

	      <bean:define id="list" name="AddCoverMethodForm" property="references" type="java.util.Vector"/>
	      <html:select property="coverMethod.reference_id">
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
	      <a href="@forms_link@AddReference.jsp">Add a new reference</a>
	    </td>
	  </tr>
	</table>
      </td>
    </tr>
    <tr>
      <td colspan="2">
	<p>
	<span class="category">
	<a href="/dd/coverindex">Cover Indexes</a> for this Cover Method:
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
	      <a href="/ddfull/coverindex/covercode">Cover Code</a><font color="red">*</font>
	      </span>
	      </p>
	    </td>
	    <td colspan="2" class="formLbl"><p><span class="category">Cover Class Range</span></p></td>
	    <td rowspan="2" class="formLbl">
	      <p>
	      <span class="category">
	      <a href="/ddfull/coverindex/coverpercent">Cover Class Percent</a>
	      <font color="red">*</font>
	      </span><br />
	      <span class="item">estimated midpoint of class</span>
	      </p>
	    </td>
	    <td rowspan="2" class="formLbl">
	      <p>
	      <span class="category"><a href="/ddfull/coverindex/indexdescription">Index Description</a>
	      </span>
	      </p>
	    </td>
	  </tr>
	  <tr class="grey">
	    <td class="formLbl">
	      <p><span class="category"><a href="/ddfull/coverindex/lowerlimit">lower %</a></span></p>
	    </td>
	    <td class="formLbl">
	      <p><span class="category"><a href="/ddfull/coverindex/upperlimit">upper %</a></span></p>
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
	    <td><html:text property='<%= "indexDescription[" + i + "]" %>' maxlength="60" size="60" /></td>

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
  
  <!-- END FOOTER -->

  
  @webpage_footer_html@
