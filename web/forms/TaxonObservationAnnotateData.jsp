
@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@
<!--
*   '$RCSfile: TaxonObservationAnnotateData.jsp,v $'
*   Purpose: View a summary of all TaxonObservations in vegbank to annotate one
*   Copyright: 2000 Regents of the University of California and the
*               National Center for Ecological Analysis and Synthesis
*   Authors: @author@
*
*  '$Author: mlee $'
*  '$Date: 2005-03-15 12:35:28 $'
*  '$Revision: 1.4 $'
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
*
-->
<script src="/vegbank/includes/utils.js"></script>


<title>Interpret a Taxon -- choose taxon on plot</title>

  
  @webpage_masthead_html@ 
  


  <h2>Interpret a Taxon from Plot</h2>

<logic:messagesPresent message="true">
	<ul>
	<html:messages id="msg" message="true">
		<li><bean:write name="msg"/></li>
	</html:messages>
	</ul>
</logic:messagesPresent>


  <logic:notEmpty name="Taxonobservation">

  <p>Please choose one of these taxa to interpret. </p>
  <blockquote>
  <table border="0" cellspacing="1" cellpadding="0"><tr bgcolor="#666666"><td>
   <table border="0" cellspacing="1" cellpadding="3">

    <tr class="listhead">
      <td width="200">Taxon Observation</td>
      <td width="110">Plant Name</td>
      <td width="100">INTERPRET?</td>
    </tr>

    <%
    //**************************************************************************************
    //  Set up alternating row colors
    //**************************************************************************************
    String bgColor = "#FFFFF";
    %>

    <logic:iterate id="tobs" name="Taxonobservation" type="org.vegbank.common.model.Taxonobservation">

    <%
    //**************************************************************************************
    //  Set up alternating row colors
    //**************************************************************************************
    bgColor = bgColor.equals("#FFFFF")? "#FFFFC" : "#FFFFF";
    %>

    <tr class="item" valign="top" bgcolor="<%= bgColor %>" >
  	<td>
		&nbsp; &nbsp; 
		<bean:write name="tobs" property="accessioncode"/>
	</td>


<!-- plant Name -->    
  <td>
  	&nbsp; &nbsp; 
	<bean:write name="tobs" property="authorplantname"/>
  </td>


<!-- interp link -->
  <td>
	&nbsp; &nbsp; 
	<html:link action="InterpretTaxonObservation" paramId="tobsAC" paramName="tobs" paramProperty="accessioncode">
	interpret...</html:link>
  </td>

    </tr>
    </logic:iterate>

  </table>
    </td></tr></table>
	</blockquote>
  </logic:notEmpty>

  <logic:empty name="Taxonobservation">
  <blockquote>
    <p>This plot does not have any taxon observations.<br>
  	<a href="javascript:history.go(-1)">Go back</a></p>
  </blockquote>
  </logic:empty>


  <br/>

  <!-- VEGBANK FOOTER -->
  
  <!-- END FOOTER -->

  
  @webpage_footer_html@
