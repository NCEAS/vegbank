<HTML>
<!-- 
  *   '$RCSfile: AddStratumMethod.jsp,v $'
  *     Purpose: Adding a new Stratum Method 
  *   Copyright: 2000 Regents of the University of California and the
  *               National Center for Ecological Analysis and Synthesis
  *     Authors: @author@
  *
  *    '$Author: farrell $'
  *      '$Date: 2003-05-01 18:35:06 $'
  *  '$Revision: 1.1 $'
  *
  *
  -->

<HEAD>

<TITLE>Add A Stratum Method Form</TITLE>
<link rel="stylesheet" href="@stylesheet@" type="text/css">
<style type="text/css">
.oddrow { background-color : #FFFFCC }
.evenrow {background-color : #FFFFFF }
.headerrow  {background-color : #DDDDDD }
</style> 
<meta http-equiv="Content-Type" content="text/html; charset=">
</HEAD>
<BODY>

<!--xxx -->
@vegbank_header_html_normal@ 
<!--xxx-->

<br>
<h2>Stratum Method Form -- add a new stratum method</h2>

<form action="/vegbank/servlet/VegbankController" method="post">
  <input type="hidden" name="command" value="AddStratumMethod">
  
  <table class="formEntry">
    <tr>
      <td class="formLbl">stratum Method Name:<font color="red">*</font></td>
      <td>
        <input name="stratumMethod.stratumMethodName" maxlength="30" size="30" />
      </td>
    </tr>
    <tr>
      <td class="formLbl">stratum Method Description:<font color="red">*</font></td>
      <td> 
        <textarea name="stratumMethod.stratumMethodDescription" cols="60"></textarea>
      </td>
    </tr>
   
    <tr>
      <td colspan="2">
        <%@ include file="/includes/AddSelectReference.jsp" %>
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
          <tr>
            <td class="oddrow">1</td>
            <td><input name="stratumType.stratumIndex" maxlength="10" size="10" /></td>
            <td><input name="stratumType.stratumName" maxlength="30" size="30" /></td>
            <td><input name="stratumType.stratumDescription" size="60" /></td>
          </tr>
          <tr>
            <td class="evenrow">2</td><td><input name="stratumType.stratumIndex" maxlength="10" size="10" /></td>
            <td><input name="stratumType.stratumName" maxlength="30" size="30" /></td>
            <td><input name="stratumType.stratumDescription" size="60" /></td>
          </tr>
  <tr><td class="oddrow">3</td><td><input name="stratumType.stratumIndex" maxlength="10" size="10" /></td>
  <td><input name="stratumType.stratumName" maxlength="30" size="30" /></td>
  <td><input name="stratumType.stratumDescription" size="60" /></td>
  </tr>
    <tr><td class="evenrow">4</td><td><input name="stratumType.stratumIndex" maxlength="10" size="10" /></td>
  <td><input name="stratumType.stratumName" maxlength="30" size="30" /></td>
  <td><input name="stratumType.stratumDescription" size="60" /></td>
  </tr>
    <tr><td class="oddrow">5</td><td><input name="stratumType.stratumIndex" maxlength="10" size="10" /></td>
    <td><input name="stratumType.stratumName" maxlength="30" size="30" /></td>
    <td><input name="stratumType.stratumDescription" size="60" /></td>
  </tr>
    <tr><td class="evenrow">6</td><td><input name="stratumType.stratumIndex" maxlength="10" size="10" /></td>
    <td><input name="stratumType.stratumName" maxlength="30" size="30" /></td>
    <td><input name="stratumType.stratumDescription" size="60" /></td>
  </tr>
    <tr><td class="oddrow">7</td><td><input name="stratumType.stratumIndex" maxlength="10" size="10" /></td>
    <td><input name="stratumType.stratumName" maxlength="30" size="30" /></td>
    <td><input name="stratumType.stratumDescription" size="60" /></td>
  </tr>
    <tr><td class="evenrow">8</td><td><input name="stratumType.stratumIndex" maxlength="10" size="10" /></td>
    <td><input name="stratumType.stratumName" maxlength="30" size="30" /></td>
    <td><input name="stratumType.stratumDescription" size="60" /></td>
  </tr>
    <tr><td class="oddrow">9</td><td><input name="stratumType.stratumIndex" maxlength="10" size="10" /></td>
    <td><input name="stratumType.stratumName" maxlength="30" size="30" /></td>
    <td><input name="stratumType.stratumDescription" size="60" /></td>
  </tr>
    <tr><td class="evenrow">10</td><td><input name="stratumType.stratumIndex" maxlength="10" size="10" /></td>
    <td><input name="stratumType.stratumName" maxlength="30" size="30" /></td>
    <td><input name="stratumType.stratumDescription" size="60" /></td>
  </tr>
   <tr><td class="oddrow">11</td><td><input name="stratumType.stratumIndex" maxlength="10" size="10" /></td>
  <td><input name="stratumType.stratumName" maxlength="30" size="30" /></td>
  <td><input name="stratumType.stratumDescription" size="60" /></td>
  </tr>
  <tr><td class="evenrow">12</td><td><input name="stratumType.stratumIndex" maxlength="10" size="10" /></td>
  <td><input name="stratumType.stratumName" maxlength="30" size="30" /></td>
  <td><input name="stratumType.stratumDescription" size="60" /></td>
  </tr>
  <tr><td class="oddrow">13</td><td><input name="stratumType.stratumIndex" maxlength="10" size="10" /></td>
  <td><input name="stratumType.stratumName" maxlength="30" size="30" /></td>
  <td><input name="stratumType.stratumDescription" size="60" /></td>
  </tr>
    <tr><td class="evenrow">14</td><td><input name="stratumType.stratumIndex" maxlength="10" size="10" /></td>
  <td><input name="stratumType.stratumName" maxlength="30" size="30" /></td>
  <td><input name="stratumType.stratumDescription" size="60" /></td>
  </tr>
    
          <tr>
            <td class="oddrow">15</td><td><input name="stratumType.stratumIndex" maxlength="10" size="10" /></td>
            <td><input name="stratumType.stratumName" maxlength="30" size="30" /></td>
            <td><input name="stratumType.stratumDescription" size="60" /></td>
          </tr>


        </table>
    </td>
  </tr>
  
</table>
  </td></tr>
  </table>
<input type="submit" name="commit_changes" value="--add this stratum method to VegBank--" />
<p>Note that you do not need to add the full amount of Stratum Types.  Only add the number of types that are appropriate for the method you are adding.</p>
<p><font color="red">*</font>Indicates a required field.</p>
<p>Click <a href="/vegbank/dbdictionary/dd-index.html">here</a> for VegBank's data dictionary for more information on fields and tables.</p>
</form>

<!-- VEGBANK FOOTER -->
<!-- xxx -->
@vegbank_footer_html_tworow@ 
<!-- xxx -->
</BODY>
</HTML>


