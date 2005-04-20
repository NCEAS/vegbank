@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@
<!-- $Id: userdataset_summary.jsp,v 1.1 2005-04-20 21:43:59 mlee Exp $ -->
<!-- purpose : show user's datasets, either all of them (mode=all in URL) or only certain ones:

  mode=ac is to show accessioncodes
  mode=id is to show by primary key of dataset(s) 
 BOTH of the above are comma separated, and currently accessionCodes need to be surrounded by single quotes -->
 

 
<TITLE>View Your VegBank Datasets - Summary</TITLE>

  
 @webpage_masthead_html@ 
  @possibly_center@  
<br/>  
<h1>View Your VegBank Datasets - Summary</h1>
<br/>
<!-- get user ID -->
<%@ include file="includeviews/sub_getwebuserid.jsp" %>

<!-- see what mode to be in- what scope of datasets to show -->
<bean:parameter id="mode" name="mode" value="all" /> <!-- default value of all --> 

<!-- only allow datasets that user IS owner of (if private, and those that are public) -->

<!-- <h1>Temp: You are user: <%= strWebUserId %>  </h1> -->

<!-- the actual wparam here, if any -->
<bean:parameter id="urlwparam" name="wparam" value=""/>

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


<%@ include file="includeviews/sub_userdataset_summary.jsp" %>

<br>



          @webpage_footer_html@
