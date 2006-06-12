

<!-- 
  *   '$Id: list_users.jsp,v 1.3 2006-06-12 17:56:59 berkley Exp $ '
  *     Purpose: web form to submit vegbank cerification request
  *   Copyright: 2000 Regents of the University of California and the
  *               National Center for Ecological Analysis and Synthesis
  *     Authors: @author@
  *
  *    '$Author: berkley $'
  *      '$Date: 2006-06-12 17:56:59 $'
  *  '$Revision: 1.3 $'
  *
  *
  -->
@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@


<TITLE>VegBank User Administration</TITLE>








@webpage_masthead_html@

        <h2 align="center" class="vegbank">User Administration</h2>

		<logic:messagesPresent message="true">
			<ul>
			<html:messages id="msg" message="true">
				<li><bean:write name="msg"/></li>
			</html:messages>
			</ul>
		</logic:messagesPresent>
    
    <!--<html:link forward="AddParty">Add a new user</html:link>-->

    <p class="sizenormal">
			Below is a list of all VegBank Users
			<br/>
    </p> 
	
	<!-- main table -->
	<table  border="0" cellspacing="5" cellpadding="2">
		<!--  display list containing applicant name, date, requested perm. Delim by status. -->

  <bean:define id="sortbyId" value="usr.usr_id"/>
	<bean:define id="sortbyPerm" value="usr.permission_type"/>
	<bean:define id="sortbyOrg" value="party.organizationname"/>

  <tr> 
    <th align="center"><html:link action="ListUsers" paramId="sortby" paramName="sortbyId">User</html:link></th>
    <th align="center"><html:link action="ListUsers" paramId="sortby" paramName="sortbyPerm">Permissions</html:link></th>
    <th align="center"><html:link action="ListUsers" paramId="sortby" paramName="sortbyOrg">Organization</html:link></th>
    <th align="center"><b>ACTION</b></th>
	</tr>

	<tr>

    <logic:iterate id="usrBean" name="usrList">
	  <html:form method="get" action="ListUsers.do">
		<html:hidden name="usrBean" property="usrId"/>
    <tr> 
		<td nowrap>
			#<bean:write name="usrBean" property="usrId"/>: 
			<bean:write name="usrBean" property="surName"/>, 
			<bean:write name="usrBean" property="givenName"/>
			<br/>
			<bean:write name="usrBean" property="emailAddress"/>
		</td>

		<td align="center"><bean:write name="usrBean" property="permissionType"/></td>

		<td align="center"><bean:write name="usrBean" property="organization"/></td>

		<td align="center" class="vegbank">
      <html:link action="LoadUser" paramId="usrId" paramName="usrBean" paramProperty="usrId" title="view user">View/Edit</html:link><br/>  
      <html:link action="DeleteUser" paramId="usrId" paramName="usrBean" paramProperty="usrId" title="delete user">Delete</html:link>
		</td>
		</html:form>
    </tr>
	</logic:iterate>
  </table>




@webpage_footer_html@
