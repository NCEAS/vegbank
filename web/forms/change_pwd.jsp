@webpage_top_html@
@stdvegbankget_jspdeclarations@
@webpage_head_html@
<TITLE>Change Your Password</TITLE>
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
		&nbsp; &nbsp; &nbsp; <html:link forward="MainMenu">Cancel
	  </td>
    </tr>

  </table>
  


@webpage_footer_html@




