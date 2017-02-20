<!-- this file is included in other dataset views to do the right vegbank:get -->

<!-- get user ID -->
<%@ include file="sub_getwebuserid.jsp" %>


<!-- only allow datasets that user IS owner of (if private, and those that are public) -->

<!-- <h1>Temp: You are user: <%= strWebUserId %>  </h1> -->

<!-- see what mode to be in- what scope of datasets to show -->

<!-- complex file.  Must consider presence of params and beans, but be careful as they
  could be defined for previous view on same page -->


<bean:define id="mode" value="id" /> <!-- default value of id --> 



<!-- paginateMain default assigned here, and is used in case of single ID being passed -->
<bean:define id="paginateMain" value="true" /> 
<bean:define id="perPageDos" value="10" />

<!-- the actual wparam here, if any -->
<logic:notPresent parameter="wparam">
  <logic:notPresent name="wparam">
    <!-- there was no wparam -->
    <bean:define id="mode" value="all" /><!-- if no param, then show all per user -->
    <bean:define id="paginateMain" value="true" /> 
    <bean:define id="perPageDos" value="10" />
  </logic:notPresent>
</logic:notPresent>

<!-- check for where param and where bean -->
<bean:parameter id="urlwhere" name="where" value="defaultErrorShouldBeOverwritten" />
<!-- actual bean takes precedence -->
<logic:present name="where">
  <bean:define id="urlwhere"><bean:write name="where" /></bean:define>
</logic:present>



<!-- default for urlwparam written so as not to confused java scriptlets -->
<bean:define id="urlwparam" value="defaultErrorShouldBeOverwritten" /> 

<logic:present parameter="wparam">
  <bean:parameter id="urlwparam" name="wparam" value="errorEMPTYSTRINGwparamInURL"/>
</logic:present>

<logic:present name="wparam"><!-- if wparam defined as bean, get it -->
  <bean:define id="urlwparam"><bean:write name="wparam" /></bean:define>
</logic:present>



<logic:notEqual name="urlwparam" value="defaultErrorShouldBeOverwritten">
  <!-- there WAS a wparam: <bean:write name="urlwparam" /> -->
  <!-- test to see if wparam has a period in it, if so is ac -->
   <logic:match name="urlwparam" value=".">
     <bean:define id="mode" value="ac" />
     <bean:define id="paginateMain" value="false" />
     <bean:define id="perPageDos" value="-1" />
   </logic:match>
   
   <logic:match name="urlwparam" value=",">
   <!-- if multiple ids (indicated by comma in wparam, then paginate, else don't -->
     <bean:define id="mode" value="id" />
     <bean:define id="paginateMain" value="true" />
     <bean:define id="perPageDos" value="10" />
   </logic:match> 
   
   <!-- list of acceptible where's is here: -->
   <logic:equal name="urlwhere" value="where_userdataset_hastable">
     <!-- this one is ok -->
     <bean:define id="mode" value="mydswithtable" />
     <bean:define id="paginateMain" value="true" />
     <bean:define id="perPageDos" value="10" />     
   </logic:equal>
   
   <!-- list of acceptible where's is here: -->
      <logic:equal name="urlwhere" value="where_userdataset_search">
        <!-- this one is ok -->
        <logic:notEqual name="urlwparam" value="">
           <!--  wparam is not empty so OK to run search -->
		<bean:define id="mode" value="search" />
		<bean:define id="paginateMain" value="true" />
		<bean:define id="perPageDos" value="10" />     
        </logic:notEqual>
        <logic:equal  name="urlwparam" value="">
          <bean:define id="mode" value="all" />  <!-- default to mode of all -->  
          <bean:define id="paginateMain" value="true" />
	  <bean:define id="perPageDos" value="10" />  
        </logic:equal>  
   </logic:equal>
   
</logic:notEqual>



<logic:present name="forceAll">
  <bean:define id="mode" value="all" /><!-- forces all items to be displayed -->
  <bean:define id="paginateMain" value="false" />
  <bean:define id="perPageDos" value="-1" />
</logic:present>

<logic:present name="forceNoPage">
  <bean:define id="paginateMain" value="false" />
  <bean:define id="perPageDos" value="-1" />
</logic:present>




<%
String paramDelim = Utility.PARAM_DELIM ;
%>

 
<logic:equal name="mode" value="all">
  <!-- show all of a user's datasets -->
  <vegbank:get id="userdataset" select="userdataset_countobs" beanName="map" 
  where="where_usrpk" wparam="<%= strWebUserId %>" pager="<%= paginateMain %>" perPage="<%= perPageDos %>" 
  allowOrderBy="true" orderBy="xorderby_datasetname" />
     <!-- where="where usr_id={0} -->
</logic:equal>

<logic:equal name="mode" value="search">
  <!-- show all of a user's datasets, and search it  -->
  <vegbank:get id="userdataset" select="userdataset_countobs" beanName="map" 
  where="where_usrpk_search" wparam="<%= strWebUserId + paramDelim + urlwparam %>" pager="<%= paginateMain %>" perPage="<%= perPageDos %>" 
  allowOrderBy="true" orderBy="xorderby_datasetname" />
     <!-- where="where usr_id={0} -->
</logic:equal>


<logic:equal name="mode" value="mydswithtable">
  <!-- show all of a user's datasets -->
  <vegbank:get id="userdataset" select="userdataset_countobs" beanName="map" 
  where="where_userdataset_hastable" wparam="<%= strWebUserId + paramDelim + urlwparam %>" 
  pager="<%= paginateMain %>" perPage="<%= perPageDos %>" allowOrderBy="true" orderBy="xorderby_datasetname"  />
     <!-- where="where_userdataset_hastable" -->
</logic:equal>
 
<logic:equal name="mode" value="id">
  <!-- id mode: -->
  <vegbank:get id="userdataset" select="userdataset" beanName="map" 
  where="where_usrpk_dsid" wparam="<%= strWebUserId + paramDelim + urlwparam %>" pager="<%= paginateMain %>" 
  perPage="<%= perPageDos %>" allowOrderBy="true" orderBy="xorderby_datasetname" /> <!--  -->
<!-- where_usrpk_dsid=where usr_id={0} AND accessionCode={1} -->


   <!-- if still didn't get dataset, try to get it via public datasets, but not this user ID: -->
   <logic:empty name="userdataset-BEANLIST">
     <vegbank:get id="userdataset" select="userdataset_nonprivate" beanName="map" 
       where="where_userdataset_pk" wparam="<%= urlwparam %>" pager="<%= paginateMain %>" 
       perPage="<%= perPageDos %>" allowOrderBy="true" orderBy="xorderby_datasetname" /> <!--  --> 
   </logic:empty>


</logic:equal>

<logic:equal name="mode" value="ac">
  <!-- ac mode -->
  <vegbank:get id="userdataset" select="userdataset" beanName="map" 
    where="where_usrpk_ac" wparam="<%= strWebUserId + paramDelim + urlwparam %>"  pager="<%= paginateMain %>"  perPage="<%= perPageDos %>" />
 <!-- where_usrpk_ac=where usr_id={0} AND usr_id={1} -->

   <!-- if still didn't get dataset, try to get it via public datasets, but not this user ID: -->
   <logic:empty name="userdataset-BEANLIST">
        <vegbank:get id="userdataset" select="userdataset_nonprivate" beanName="map" 
          where="where_accessioncode" wparam="<%= urlwparam %>" pager="<%= paginateMain %>" 
          perPage="<%= perPageDos %>" allowOrderBy="true" orderBy="xorderby_datasetname" /> <!--  --> 
   </logic:empty>

</logic:equal>

<logic:empty name="userdataset-BEANLIST">
 <% if ( strWebUserId == "-1" ) {  %>
  <!-- user is not logged in, trying to get datacart then -->
   <vegbank:get id="userdataset" select="userdataset" beanName="map" 
  where="where_userdataset_pk" wparam="<%= (DatasetUtility.getDatacartId(request.getSession())).toString() %>" pager="false"/>
  

 <% } %>
</logic:empty>
