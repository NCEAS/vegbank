<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<script language="javascript" >
	function validateLogonForm()
	{
		
		return true;
	}
</script>

<font color="blue"><html:errors /></font><br/>
     Login <br/>
	 <!-- html form -->
   <html:form action="/logon.do" onsubmit="return validateLogonForm()">
	 <!-- input text field for the number -->
     <html:text property="username" /><br/>
	 <html:password property="password" /><br/>
	 <!-- button -->

     <html:submit property="login" value="Login" />
  </html:form>
