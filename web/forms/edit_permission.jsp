@webpage_top_html@
  @stdvegbankget_jspdeclarations@
<jsp:useBean id="bean" class="org.vegbank.ui.struts.UserProfileForm"/>
  @webpage_head_html@
<!-- 
  * '$RCSfile: edit_permission.jsp,v $'
  * Purpose: 
  * Copyright: 2000 Regents of the University of California and the
  *               National Center for Ecological Analysis and Synthesis
  * Authors: @author@
  *
  * '$Author: berkley $'
  * '$Date: 2006-06-15 20:10:45 $'
  * '$Revision: 1.1 $'
  *
  *
  -->
  


<TITLE>Edit User Permissions</TITLE>

<script type="text/javascript">
function getHelpPageId() {
  return "update-user-permission";
}

</script>





@webpage_masthead_html@

<br />
<br />
<h2 align="center" class="vegbank">Edit User Permissions</h2>

<html:errors/>

<html:form method="get" action="/ChangePermission.do">
 	<bean:define id="beanUsrId"><bean:write name="webuser" property="userid"/></bean:define>
 	<input type="hidden" name="usrId" value="<bean:write name="beanUsrId"/>"/>
    <table bgcolor="#CCCCCC" cellpadding="1" cellspacing="1" border="0" >
    <tr bgcolor="#EBF3F8"> 
      <td align="center">Personal Information</td> 
      <td align="center">Permission</td>
    </tr>
    <tr bgcolor="#FEFEFE">
      <td>
        <p>
          <bean:write name="webuser" property="givenname"/> 
          <bean:write name="webuser" property="surname"/>
        </p>
      </td>
      <td>
        <!--<html:text name="upform" property="webuser.permissiontype" size="3" maxlength="3"/>-->
        <input name="permissiontype" type="text" size="3" maxlength="3" 
          value="<bean:write name="webuser" property="permissiontype"/>"/>
      </td>
    </tr>
    </table>
    <html:submit value="Update permission"/>
	</html:form>

@webpage_footer_html@

