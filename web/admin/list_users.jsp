

<!-- 
  *   '$Id: list_users.jsp,v 1.2 2006-06-09 19:59:03 berkley Exp $ '
  *     Purpose: web form to submit vegbank cerification request
  *   Copyright: 2000 Regents of the University of California and the
  *               National Center for Ecological Analysis and Synthesis
  *     Authors: @author@
  *
  *    '$Author: berkley $'
  *      '$Date: 2006-06-09 19:59:03 $'
  *  '$Revision: 1.2 $'
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

	<!--<bean:define id="sortbyId" value="usercertification_id"/>
	<bean:define id="sortbyReq" value="requested_cert_level"/>
	<bean:define id="sortbyStatus" value="certificationstatus"/>-->

    <tr> 
      <th align="center">User</th>
      <th align="center">Permissions</th>
      <th align="center">Organization</th>
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
