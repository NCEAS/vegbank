<!-- This is only a jsp snipit -->
<!-- start logon form Include -->

<html:form action="/Logon" focus="username"
         onsubmit="return validateLogonForm(this);">
<table border="0" width="100%" height="135">

  <!-- Title -->
  <tr>
    <td colSpan="2" align="center">
      <div align=left><br/>
        <font face="Georgia, Times New Roman, Times, serif" color="#531100" size="4">
          <b>Login to VegBank</b>
        </font> 
      </div>
    </td>

  <!-- Username -->
  <tr>
    <td align="left">
      <font face="Georgia, Times New Roman, Times, serif" color="#531100">
        e-Mail:
      </font>
    </td>
    <td align="left">
      <html:text property="username" size="25" maxlength="50"/>
    </td>
  </tr>
  
  <!-- Password -->
  <tr>
    <td align="left">
      <font face="Georgia, Times New Roman, Times, serif" color="#531100">
        Password:
      <font>
    </td>
    <td align="left">
      <html:password property="password" size="25" maxlength="18"
                    redisplay="false"/>
    </td>
  </tr>

  <!-- Form Buttons -->
  <tr>
    <td>&nbsp;</td>
    <td>
      <html:submit value="Login"/>
    </td>
  </tr>
  <tr>
    <td></td>
    <!-- Some usefull links -->
    <td>
		<p>
        &raquo; <a href="@forms_link@RegisterNewUser.jsp">Not yet a user? <b>Register</b> here.</a>
        <br/>
        &raquo; <a href="/vegbank/Logon.do?username=GUESTUSER@VEGBANK.ORG&password=nopassword">Use vegbank as a guest? Login here.</a>
        <br/>
        &raquo; <a href="@forms_link@EmailPassword.jsp">Lost your password?</a>
      </p>
    </td>
  </tr>

</table>

</html:form>

<html:javascript formName="LogonForm"
        dynamicJavascript="true"
         staticJavascript="false"/>
<script language="Javascript1.1" src="staticJavascript.jsp"></script>

<!-- End logon form Include -->
