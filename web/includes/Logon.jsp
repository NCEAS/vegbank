<!-- This is only a jsp snipit -->
<!-- start logon form Include -->

<html:form action="/Logon" focus="username">
<table border="0" width="100%" height="135">

  <!-- Title -->
  <tr>
    <td colSpan="2" class="category">
     Login to VegBank
    </td>

  <!-- Username -->
  <tr>
    <td>
        e-Mail:
    </td>
    <td>
      <html:text property="username" size="25" maxlength="50"/>
    </td>
  </tr>
  
  <!-- Password -->
  <tr>
    <td>
        Password:
    </td>
    <td>
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
    <!-- Some useful links -->
    <td>
		<p>
        &raquo; <a href="@forms_link@RegisterNewUser.jsp">Not yet a user? <b>Register</b> here.</a>
      <!--  <br/>
        &raquo; <a href="/vegbank/Logon.do?username=GUESTUSER@VEGBANK.ORG&password=nopassword">Use vegbank as a guest? Login here.</a> -->
        <br/>
        &raquo; <a href="@forms_link@EmailPassword.jsp">Lost your password?</a>
         <br/>
        &raquo; Note that you need not login for most VegBank functions.  <br/>To use VegBank without logging in, 
        click <a href="@web_context@DisplayMainMenu.do">here for the Main Menu</a>.
      </p>
    </td>
  </tr>

</table>

</html:form>

<!--html:javascript formName="LogonForm"
        dynamicJavascript="true"
         staticJavascript="false"/>

<script language="Javascript1.1" src="staticJavascript.jsp">
</script-->

<!-- End logon form Include -->
