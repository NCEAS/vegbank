@stdvegbankget_jspdeclarations@


<HEAD   >
@defaultHeadToken@
 
<TITLE>View Your VegBank Datasets</TITLE>
<link rel="stylesheet" href="@stylesheet@" type="text/css">
<meta http-equiv="Content-Type" content="text/html; charset=">
</HEAD>
<body   >@vegbank_header_html_normal@  <br><h2>View Your VegBank Datasets</h2>
<!-- get user ID -->
<% 
Long localPgUserId = (Long)(request.getSession().getAttribute("USER")); 
String strUserId = "0";

if ( localPgUserId==null )
  { strUserId = "-1" ;}
  else strUserId = localPgUserId.toString();

%>
<!-- <h1>Temp: You are user: <%= strUserId %> </h1> -->
<vegbank:get select="userdataset" beanName="map" where="where_usr_pk" wparam="<%= strUserId %>" pager="true"/>

<%@ include file="includeviews/sub_userdataset_detail.jsp" %>

<br>

          @vegbank_footer_html_tworow@