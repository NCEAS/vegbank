<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ page import="org.vegbank.nvcrs.web.BeanManager"  %>
<%@ page import="java.util.*" %> 

<script language="javascript" >
	function validateLogonForm()
	{
		
		return true;
	}
</script>
<!--
<img src="image/title_sep_line.gif">
<font face=Arial >
<table width=630 border=0 cellpadding=0 cellspacing=0>
<tr><td width=30></td><td>
<b>Login
</td></tr>
</table >
-->
<%
	BeanManager beanManager=(BeanManager)pageContext.getAttribute("beanManager", PageContext.SESSION_SCOPE);
	if(beanManager!=null)
	{
		java.util.ArrayList list = beanManager.getErrors();
		String strErr="";
		if(list!=null && !list.isEmpty())
		{
			int num=list.size();
			for(int i=0;i<num;i++)
			{
				strErr = (String)list.get(i);
			
%>    	
<br>
<center><font face=Arial size=2>Error</font></center><br>
<center><font face=Arial size=1><%= strErr %></font></center>
<%
			}
			beanManager.clearErrors();
		}
	}
%>

<center>
<br><br>
   <html:form action="/logon.do" onsubmit="return validateLogonForm()">
     <table width=500 border=0 cellpadding=0 cellspacing=0 bgcolor="#ecedcd">
     <tr><th colspan=2 background="image/blue_back.gif"><center><font color=white>LOGIN</font></center></th>
     <tr><td>&nbsp;</td><td>&nbsp;</td></tr>
     <font size=2 >
     <tr><td align=right>Login name:</td><td align=left><html:text property="username" /></td></tr>
	 <tr><td align=right>Password:</td><td left><html:password property="password" /></td></tr>
     <tr><td>&nbsp;</td><td>&nbsp;</td></tr>
     <tr><td></td><td><html:submit property="login" value="Login" /></td></tr>
     <tr><td>&nbsp;</td><td>&nbsp;</td></tr>
     <tr><td colspan=2 align=center><font face=Arial size=2 >Forget your password? please send email to <a href="mailto:peet@unc.edu">system administrator.</a></font></td></tr>
     <tr><td colspan=2 align=center><font face=Arial size=2 >If you have not registered, please <a href="register.go">register</a></font></td></tr>
     <tr><td>&nbsp;</td><td>&nbsp;</td></tr>
     
     </table>
</html:form>
