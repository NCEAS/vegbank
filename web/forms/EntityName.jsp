@webpage_top_html@
@stdvegbankget_jspdeclarations@
@webpage_head_html@
<link rel="STYLESHEET" href="@stylesheet@" type="text/css" />
<title>VEGBANK - Entity Name Search</title>
@webpage_masthead_html@


  

<html:form action="EntityName.do" method="get">


  <!-- SECOND TABLE -->
  <table align="left" border="0" width="90%" cellspacing="0" cellpadding="0">

    <tr>
      <td colspan="2" bgcolor="white">
	<img align="center" border="0" height="144" src="@image_server@owlogoBev.jpg" alt="Veg plots logo "> 
      </td>
      <td align="left" valign="middle">
	<table border="0" cellpadding="5">
	  <tr>
	    <td align="left" valign="bottom">
	      <font face="Helvetica,Arial,Verdana" size="6" color="#23238E">Entity Name Search Form</font>
	      <br/>
	    </td>
	  </tr>
	</table>
      </td>
    </tr>

    <!-- Instructions Row -->
    <tr>
      <!-- LEFT MARGIN -->
      <td width="10%"  bgcolor="white" align="left" valign="top"></td>
      <td width="5%"  bgcolor="white" align="left" valign="top"></td>
      
      <td align="left">

	<table border="0" align="center">
	  
	  <tr valign="top">
	    <td align="left" colspan="2" valign="center">
	      <font color="#23238E" face="Helvetica,Arial,Verdana" size="2">
	      <b>This tool is used to find VegBank's discrete names for rocks, plants and communities.</b>
		  <br/><br/>

	      </font> 
	    </td>
	  </tr>
	  </tbody>
	</table>
      </td>
    </tr>

    <!-- ERROR DISPLAY -->
    <tr>
      <td colspan="3">
	<html:errors/>
      </td>
    </tr>
    

    <tr>
      <td>&nbsp;</td>
      <td colspan="2">
		Please enter an entity name and choose its type. 	
		<br>
		<html:text property="searchText" size="20"/>
		<html:select property="entityType">
			<html:option value="1">plant concept
			<html:option value="2">community concept
			<html:option value="3">contributing party
		
		<html:submit value="search"/>&nbsp;&nbsp;

      </td>
    </tr>
    
	<logic:present name="searchResults">
    <tr>
      <td>&nbsp;</td>
      <td colspan="2">
	  <br/>
	  <h3>SEARCH RESULTS:</h3>
	  </td>
    </tr>

    <tr>
      <td>&nbsp;</td>
      <td colspan="2">
	  <ul>
		<logic:iterate id="result" name="searchResults">
			<li>
			<logic:present name="result"><bean:write name="result"/></logic:present>
			</li> 
		</logic:iterate>
		</ul>
	  </td>
    </tr>
	</logic:present>

	<logic:notPresent name="searchResults">
    <tr>
      <td>&nbsp;</td>
      <td colspan="2">
	  - present link to alphabetical listing of plants, communities and parties
	  </td>
    </tr>
	</logic:notPresent>
	    

      <tr><td colspan="2">&nbsp;</td></tr>
      <tr>
	<td colspan="3">
	  <!-- VEGBANK FOOTER -->
	  @webpage_footer_html@
	</td>
      </tr>
    </table>
    
    
  
