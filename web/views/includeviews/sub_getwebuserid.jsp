<!-- purpose: call this to get the User's ID, who is logged in.  returns as string: strWebUserId -->

<% 
// $Id: sub_getwebuserid.jsp,v 1.2 2005-03-17 13:36:18 mlee Exp $


Long longWebUserId = (Long)(request.getSession().getAttribute("USER")); 
String strWebUserId = "-1";


if (longWebUserId!=null) {
    strWebUserId = longWebUserId.toString();
}

%>

<bean:define id="beanWebUserEmail" value="" type="java.lang.String"/><!-- sets to default of "" -->
<vegbank:get id="webusr" select="usr_email" where="where_usrpk" wparam="<%= strWebUserId %>" />
<logic:notEmpty name="webusr-BEANLIST">
  <logic:iterate id="onerowofwebusr" name="webusr-BEANLIST">
      <!-- gets user's email address -->
      <bean:define type="java.lang.String" id="beanWebUserEmail" name="onerowofwebusr" property="email_address" />    
  </logic:iterate>
</logic:notEmpty>