@webpage_top_html@
@stdvegbankget_jspdeclarations@
@webpage_head_html@
@defaultHeadToken@

<title>Interpret a Taxon -- choose taxon on plot</title>
  @webpage_masthead_html@ 


  <h2>Interpret a Taxon from Plot</h2>

<logic:messagesPresent message="true">
	<ul>
	<html:messages id="msg" message="true">
		<li><bean:write name="msg"/></li>
	
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
	interpret...
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
  @webpage_footer_html@
  <!-- END FOOTER -->

  
  
