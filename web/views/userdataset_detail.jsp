@stdvegbankget_jspdeclarations@
<!-- $Id: userdataset_detail.jsp,v 1.9 2005-03-15 06:39:47 anderson Exp $ -->
<!-- purpose : show user's datasets, either all of them (mode=all in URL) or only certain ones:

  mode=ac is to show accessioncodes
  mode=id is to show by primary key of dataset(s) 
 BOTH of the above are comma separated, and currently accessionCodes need to be surrounded by single quotes -->
 

<HEAD   >
@defaultHeadToken@
 
<TITLE>View Your VegBank Datasets</TITLE>
<link rel="stylesheet" href="@stylesheet@" type="text/css">
<meta http-equiv="Content-Type" content="text/html; charset=">
</HEAD>
<body>  
 @vegbank_header_html_normal@ 
  @possibly_center@  <h2>View Your VegBank Datasets</h2>
<!-- get user ID -->
<%@ include file="includeviews/sub_getwebuserid.jsp" %>

<!-- see what mode to be in-- what scope of datasets to show -->
<bean:parameter id="mode" name="mode" value="all" /> <!-- default value of all --> 

<!-- only allow datasets that user IS owner of (if private, and those that are public) -->

<!-- <h1>Temp: You are user: <  %= strWebUser %  </h1> -->

<!-- the actual wparam here, if any -->
<bean:parameter id="urlwparam" name="wparam" value=""/>

<%

String paramDelim = Utility.PARAM_DELIM ;
%>
<!-- if (mode == null) {
	mode = "all";
}

if (getType="all")  -->

 <!-- show all of a user's datasets -->
<logic:equal name="mode" value="all">
  <vegbank:get select="userdataset" beanName="map" 
  where="where_usrpk" wparam="<%= strWebUserId %>" pager="true"/>
     <!-- where="where usr_id={0} -->
</logic:equal>

 <!-- show by ID, but still only for a particular user -->
 <!-- % String[] arr = new String[2] ; 
 arr[0] = strWebUser;
 arr[1] = urlwparam;
 
 %-->
 
<logic:equal name="mode" value="id">
  <vegbank:get select="userdataset" beanName="map" 
  where="where_usrpk_dsid" wparam="<%= strWebUserId + paramDelim + urlwparam %>" pager="true"/> <!--  -->
<!-- where_usrpk_dsid=where usr_id={0} AND accessionCode={1} -->
</logic:equal>

<logic:equal name="mode" value="ac">

  <vegbank:get select="userdataset" beanName="map" 
    where="where_usrpk_ac" wparam="<%= strWebUserId + paramDelim + urlwparam %>" pager="true"/>
 <!-- where_usrpk_ac=where usr_id={0} AND usr_id={1} -->

</logic:equal>


 

 
<%@ include file="includeviews/sub_userdataset_detail.jsp" %>

<br>



          @webpage_footer_html@
