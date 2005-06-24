@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@
<!-- $Id: userdataset_detail.jsp,v 1.14 2005-06-24 01:18:12 mlee Exp $ -->
<!-- purpose : show user's datasets.  THIS WILL NOT show datasets for a user
   other than the current user, nor will it show a dataset for someone who isn't logged in-->
<!-- we should prompt users to log in to access this page -->
 
<TITLE>View Your VegBank Datasets - Detail</TITLE>

  
 @webpage_masthead_html@ 
  @possibly_center@  
  
<h1>View Your VegBank Datasets - Detail</h1>
<!-- get user ID -->
<%@ include file="includeviews/sub_getwebuserid.jsp" %>

<!-- see what mode to be in- what scope of datasets to show -->
<bean:parameter id="mode" name="mode" value="id" /> <!-- default value of id --> 

<!-- only allow datasets that user IS owner of (if private, and those that are public) -->

<!-- <h1>Temp: You are user: <%= strWebUserId %>  </h1> -->

<!-- the actual wparam here, if any -->
<bean:parameter id="urlwparam" name="wparam" value=""/>

<logic:empty name="urlparam">
  <bean:define id="mode" value="all" /><!-- if no param, then show all per user -->
</logic:empty>

<!-- test to see if wparam starts with a character, if so is ac -->
<logic:match name="urlwparam" value=".">
  <bean:define id="mode" value="ac" />
</logic:match>

<%

String paramDelim = Utility.PARAM_DELIM ;
%>

 <!-- show all of a user's datasets -->
<logic:equal name="mode" value="all">
  <vegbank:get id="userdataset" select="userdataset" beanName="map" 
  where="where_usrpk" wparam="<%= strWebUserId %>" pager="true"/>
     <!-- where="where usr_id={0} -->
</logic:equal>

 <!-- show by ID, but still only for a particular user -->
 <!-- % String[] arr = new String[2] ; 
 arr[0] = strWebUser;
 arr[1] = urlwparam;
 
 %-->
 
<logic:equal name="mode" value="id">
  <vegbank:get id="userdataset" select="userdataset" beanName="map" 
  where="where_usrpk_dsid" wparam="<%= strWebUserId + paramDelim + urlwparam %>" pager="true"/> <!--  -->
<!-- where_usrpk_dsid=where usr_id={0} AND accessionCode={1} -->
</logic:equal>

<logic:equal name="mode" value="ac">

  <vegbank:get id="userdataset" select="userdataset" beanName="map" 
    where="where_usrpk_ac" wparam="<%= strWebUserId + paramDelim + urlwparam %>" pager="true"/>
 <!-- where_usrpk_ac=where usr_id={0} AND usr_id={1} -->

</logic:equal>


<%@ include file="includeviews/sub_userdataset_detail.jsp" %>

<br />



          @webpage_footer_html@
