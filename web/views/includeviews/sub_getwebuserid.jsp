<!-- purpose: call this to get the User's ID, who is logged in.  returns as string: strWebUserId -->

<% 
// $Id: sub_getwebuserid.jsp,v 1.1 2005-02-12 00:06:57 mlee Exp $


Long longWebUserId = (Long)(request.getSession().getAttribute("USER")); 
String strWebUserId = "-1";

if (longWebUserId!=null) {
    strWebUserId = longWebUserId.toString();
}

%>
