

<!-- 
  *   '$Id: list_certifications.jsp,v 1.9 2005-03-15 18:59:58 mlee Exp $ '
  *     Purpose: web form to submit vegbank cerification request
  *   Copyright: 2000 Regents of the University of California and the
  *               National Center for Ecological Analysis and Synthesis
  *     Authors: @author@
  *
  *    '$Author: mlee $'
  *      '$Date: 2005-03-15 18:59:58 $'
  *  '$Revision: 1.9 $'
  *
  *
  -->
@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@


<TITLE>VegBank Certification Administration</TITLE>








@webpage_masthead_html@

        <h2 align="center" class="vegbank">Certification Administration</h2>

		<logic:messagesPresent message="true">
			<ul>
			<html:messages id="msg" message="true">
				<li><bean:write name="msg"/></li>
			</html:messages>
			</ul>
		</logic:messagesPresent>

        <p class="sizenormal">
			Below is a list of all VegBank certification applications.
			<br/>
        </p> 
	
	<!-- main table -->
	<table  border="0" cellspacing="5" cellpadding="2">
		<!--  display list containing applicant name, date, requested perm. Delim by status. -->

	<bean:define id="sortbyId" value="usercertification_id"/>
	<bean:define id="sortbyReq" value="requested_cert_level"/>
	<bean:define id="sortbyStatus" value="certificationstatus"/>

    <tr> 
      <th><html:link action="ListCertifications" paramId="sortby" paramName="sortbyId">Applicant</html:link></th>
      <th>Permissions at Time of Submission</th>
      <th><html:link  action="ListCertifications" paramId="sortby" paramName="sortbyReq">Requested Permission</html:link></th>
      <th><html:link  action="ListCertifications" paramId="sortby" paramName="sortbyStatus">Status</html:link></th>
      <th><b>ACTION</b></th>
	</tr>

	<tr>

    <logic:iterate id="certBean" name="allApps">
	<html:form method="get" action="ListCertifications.do">
		<html:hidden name="certBean" property="certId"/>
    <tr> 
		<td nowrap>
			#<bean:write name="certBean" property="certId"/>: 
			<bean:write name="certBean" property="surName"/>, 
			<bean:write name="certBean" property="givenName"/>
			<br/>
			(<html:link action="LoadUser" paramId="usrId" paramName="certBean" paramProperty="usrId" title="edit user">
				<bean:write name="certBean" property="emailAddress"/>
			</html:link>)
		</td>

		<td align="center"><bean:write name="certBean" property="currentCertLevelName"/></td>

		<td align="center"><bean:write name="certBean" property="requestedCertName"/></td>

		<td align="center"><bean:write name="certBean" property="certificationstatus"/></td>
		<td align="center" class="vegbank">
			<html:link action="ViewCertification" paramId="certId" 
						paramName="certBean" paramProperty="certId" title="view">View</html:link>
		</td>
		</html:form>
    </tr>
	</logic:iterate>
  </table>




@webpage_footer_html@
