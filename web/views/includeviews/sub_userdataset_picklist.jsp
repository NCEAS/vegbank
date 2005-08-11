<!-- this file is an included view that just shows a table of userdatasets, with checkboxes next to it.
  A form can easily wrap this file and do something with the array of includeds params -->



<!-- sets to force all datasets to be displayed, no pagination: -->
<bean:define id="forceNoPage" value="true" />
  <!-- do the right get -->
 <%@ include file="sub_getuserdatasets.jsp" %>
  <!-- display stuff -->

 <logic:empty name="userdataset-BEANLIST">
   <p>Sorry, no Datasets found! 
      <% if ( strWebUserId == "-1" ) {  %>  
        You are not logged in. 
        <a href="@general_link@login.jsp">Please Login Here.</a> 
      <% } %>
   </p>
 </logic:empty>
<logic:notEmpty name="userdataset-BEANLIST"><!-- set up table -->


  <select name="userdataset_id">
   <logic:iterate id="onerowofuserdataset" name="userdataset-BEANLIST"><!-- iterate over all records in set : new table for each -->
    <!-- only show those with observations -->
    
      <bean:define id="userdataset_pk" name="onerowofuserdataset" property="userdataset_id" />
     <option value="<bean:write name='userdataset_pk' />"><bean:write name="onerowofuserdataset" property="datasetname" />
     </option>
  </logic:iterate>
   </select>
  <bean:define id="successfulget" value="true" />
</logic:notEmpty>