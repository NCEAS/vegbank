@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@
<!-- $Id: userdataset_detail.jsp,v 1.16 2005-07-21 22:57:12 mlee Exp $ -->
<!-- purpose : show user's datasets.  THIS WILL NOT show datasets for a user
   other than the current user, nor will it show a dataset for someone who isn't logged in-->
<!-- we should prompt users to log in to access this page -->
 
<TITLE>View Your VegBank Datasets - Detail</TITLE>

  
 <%@ include file="includeviews/inlinestyles.jsp" %> 
 @webpage_masthead_html@ 
  @possibly_center@  
  
<h1>View Your VegBank Datasets - Detail</h1>
<!-- get user ID -->
<%@ include file="includeviews/sub_getwebuserid.jsp" %>


<!-- only allow datasets that user IS owner of (if private, and those that are public) -->

<!-- <h1>Temp: You are user: <%= strWebUserId %>  </h1> -->

<!-- see what mode to be in- what scope of datasets to show -->
<bean:define id="mode" value="id" /> <!-- default value of id --> 

<!-- paginateMain default assigned here, and is used in case of single ID being passed -->
<bean:define id="paginateMain" value="false" /> 

<!-- the actual wparam here, if any -->
<logic:notPresent parameter="wparam">
  <!-- there was no wparam -->
  <bean:define id="mode" value="all" /><!-- if no param, then show all per user -->
  <bean:define id="paginateMain" value="true" />  
</logic:notPresent>

<!-- default for urlwparam written so as not to confused java scriptlets -->
<bean:define id="urlwparam" value="defaultErrorShouldBeOverwritten" /> 

<logic:present parameter="wparam">
  <bean:parameter id="urlwparam" name="wparam" value="errorEMPTYSTRINGwparamInURL"/>
  <!-- there WAS a wparam: <bean:write name="urlwparam" /> -->
  <!-- test to see if wparam has a period in it, if so is ac -->
   <logic:match name="urlwparam" value=".">
     <bean:define id="mode" value="ac" />
     <bean:define id="paginateMain" value="false" />
   </logic:match>
   
   <logic:match name="urlwparam" value=",">
   <!-- if multiple ids (indicated by comma in wparam, then paginate, else don't -->
     <bean:define id="paginateMain" value="true" />
   </logic:match> 
</logic:present>





<%
String paramDelim = Utility.PARAM_DELIM ;
%>

 
<logic:equal name="mode" value="all">
  <!-- show all of a user's datasets -->
  <vegbank:get id="userdataset" select="userdataset" beanName="map" 
  where="where_usrpk" wparam="<%= strWebUserId %>" pager="<%= paginateMain %>"/>
     <!-- where="where usr_id={0} -->
</logic:equal>

 <!-- show by ID, but still only for a particular user -->
 <!-- % String[] arr = new String[2] ; 
 arr[0] = strWebUser;
 arr[1] = urlwparam;
 
 %-->
 
<logic:equal name="mode" value="id">
  <!-- id mode: -->
  <vegbank:get id="userdataset" select="userdataset" beanName="map" 
  where="where_usrpk_dsid" wparam="<%= strWebUserId + paramDelim + urlwparam %>" pager="<%= paginateMain %>" /> <!--  -->
<!-- where_usrpk_dsid=where usr_id={0} AND accessionCode={1} -->
</logic:equal>

<logic:equal name="mode" value="ac">
  <!-- ac mode -->
  <vegbank:get id="userdataset" select="userdataset" beanName="map" 
    where="where_usrpk_ac" wparam="<%= strWebUserId + paramDelim + urlwparam %>"  pager="<%= paginateMain %>" />
 <!-- where_usrpk_ac=where usr_id={0} AND usr_id={1} -->

</logic:equal>


<%@ include file="includeviews/sub_userdataset_detail.jsp" %>

<br />



          @webpage_footer_html@
