@webpage_top_html@
@stdvegbankget_jspdeclarations@
@webpage_head_html@

<TITLE>Add A Stratum Method Form</TITLE>
<style type="text/css">
.headerrow  {background-color : #DDDDDD }
</style>
@webpage_masthead_html@ 

<br>
<h2>Stratum Method Form -- add a new stratum method</h2>

<html:errors/>

<html:form action="/AddStratumMethod" onsubmit="return validateAddStratumMethodForm(this)">
<form action="/vegbank/servlet/VegbankController" method="post">
  <input type="hidden" name="command" value="AddStratumMethod">
  
  <table class="formEntry">
    <tr>
      <td class="formLbl">stratum Method Name:<font color="red">*</font></td>
      <td>
        <html:text property="stratumMethod.stratummethodname" maxlength="30" size="30"/>
      </td>
    </tr>
    <tr>
      <td class="formLbl">stratum Method Description:<font color="red">*</font></td>
      <td> 
        <html:textarea property="stratumMethod.stratummethoddescription"  cols="60"/>
      </td>
    </tr>
   
    <tr>
      <td colspan="2">
	<table>
	  <tr>
	    <td class="formLbl">reference:</td>
	    <td>
	      <bean:define id="list" name="AddStratumMethodForm" property="references" type="java.util.Vector"/>
	      <html:select property="stratumMethod.reference_id">
	        <option value="-1">--select a reference--</option>
	        <html:options collection="list" labelProperty="title" property="id"/>
	      
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
      <td>&nbsp;</td>
      <td>&nbsp;</td>
    </tr>


    <tr>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
    </tr>
  
    <tr>
      <td colspan="2">Stratum Type(s) for this Stratum Method: </td>
    </tr>
    <tr>
      <td>&nbsp;</td>
      <td>
        <table class="formEntry">
          <tr>
            <td>num</td>
            <td class="formLbl">stratum Index<font color="red">*</font></td>
            <td class="formLbl">stratum Name<font color="red">*</font></td>
            <td class="formLbl">stratum Description</td>
          </tr>

            <%
            for (int i=0; i<15 ; i++)
            {
            %>
          <tr>
            <td class="oddrow"><p><%= i+1 %></p></td>

            <td><html:text property='<%= "stratumIndex[" + i + "]" %>' maxlength="10" size="10" /></td>
            <td><html:text property='<%= "stratumName[" + i + "]" %>' maxlength="30" size="30" /></td>
            <td><html:text property='<%= "stratumDescription[" + i + "]" %>' maxlength="60" size="60" /></td>
          </tr>
          <%
          }
          %>
          <tr>
       </table>
    </td>
  </tr>
  
</table>
  </td></tr>
  </table>
<html:submit property="commit_changes" value="--add this stratum method to VegBank--" />
<p>Note that you do not need to add the full amount of Stratum Types.  Only add the number of types that are appropriate for the method you are adding.</p>
<p><font color="red">*</font>Indicates a required field.</p>
<p>Click <a href="@dbdictionary_link@dd-index.html">here</a> for VegBank's data dictionary for more information on fields and tables.</p>


<!-- VEGBANK FOOTER -->
<!-- xxx -->
@webpage_footer_html@
<!-- xxx -->




