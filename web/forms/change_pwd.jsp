

<!--
  * '$Id: change_pwd.jsp,v 1.7 2005-03-15 12:35:28 mlee Exp $'
  * Purpose: Display change user password form.
  * Copyright: 2000 Regents of the University of California and the
  *               National Center for Ecological Analysis and Synthesis
  * Authors: @author@
  *
  * '$Author: mlee $'
  * '$Date: 2005-03-15 12:35:28 $'
  * '$Revision: 1.7 $'
  *
  *
  -->
@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@

<!-- #BeginEditable "doctitle" -->
<TITLE>Change Your Password</TITLE>

<!-- #EndEditable -->



@webpage_masthead_html@

<br>
<br>
<h2 align="center" class="vegbank">Change Password</h2>

<html:errors/>

<html:form method="post" action="/UpdatePassword.do" >
<html:hidden property="action" value="update"/>
 	<input type="hidden" name="usrId" value="<%=request.getParameter("usrId")%>"/>

	<table width="700" border="0" cellspacing="5" cellpadding="3">
<% 
	Boolean isAdmin = (Boolean)(request.getSession().getAttribute("isAdmin"));

	if (isAdmin != null) {
		if (!isAdmin.booleanValue()) {
%>
	<tr> 
      <td colspan="2"><span class="sizesmall">Current Password: </span><br/>
          <input type="password" name="password" size="25" maxlength="100"/>
	  </td>
    </tr>
<% 
		}
	} 
%>

	
	<tr> 
      <td colspan="2"><span class="sizesmall">New Password: </span><br/>
        <input type="password" name="newpassword1" size="25" maxlength="100"/>
	  </td>
    </tr>

	<tr> 
      <td colspan="2"><span class="sizesmall">Re-type New Password: </span><br/>
        <input type="password" name="newpassword2" size="25" maxlength="100"/>
	  </td>
    </tr>

	<tr> 
      <td colspan="2">
        <html:submit value="Change My Password"/>
		&nbsp; &nbsp; &nbsp; <html:link forward="MainMenu">Cancel</html:link>
	  </td>
    </tr>

  </table>
  
</html:form>



@webpage_footer_html@


