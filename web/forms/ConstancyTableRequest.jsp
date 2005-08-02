@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@
 <%@ include file="/views/includeviews/inlinestyles.jsp" %> 
  <title>
   Request A Constancy Table
  </title>


<!-- VegBank Header -->
  @webpage_masthead_html@
  
 <h1>Request A Constancy Table</h1> 

<p class="instructions">Use this page to request a constancy table for one or more datasets.
A constancy table shows you the taxa on each set of plots, showing how many
plots have each species for each dataset, and also the average cover percent (if available in the database).
</p>

<p class="instructions">You must be a register user who is logged in to use this feature.</p>

<!-- set perPage to -1 -->
<bean:define id="perPage" value="-1" />
<!-- sets to force all datasets to be displayed, no pagination: -->
<bean:define id="forceAll" value="true" />
  <!-- do the right get -->
 <%@ include file="/views/includeviews/sub_getuserdatasets.jsp" %>
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


 <p class="instruction">Please check the row next to the datasets you would like to include
  in the constancy table.</p>
 <form name="dataform" action="@views_link@userdataset_constancyanalysis.jsp">
  <table class="thinlines" cellpadding="1">
    <tr>
      <th>Include in Constancy Table</th>
      <th>View Dataset</th>
      <%@ include file="/views/autogen/userdataset_summary_head.jsp" %>
      <th>Plots</th>
    </tr>

  <logic:iterate id="onerowofuserdataset" name="userdataset-BEANLIST"><!-- iterate over all records in set : new table for each -->
    <!-- only show those with observations -->
    <logic:greaterThan name="onerowofuserdataset" property="countobs" value="0">
      <bean:define id="userdataset_pk" name="onerowofuserdataset" property="userdataset_id" />
     <tr>
       <td><input type="checkbox" value="<bean:write name='userdataset_pk' />" name="includeds" /></td>
       <td><a href="@get_link@summary/userdataset/<bean:write name='userdataset_pk' />">details</a>
       <%@ include file="/views/autogen/userdataset_summary_data.jsp" %>
       <td>
       <a href='@get_link@std/observation/<bean:write name="onerowofuserdataset" property="accessioncode" />?where=where_inuserdataset_ac_obs'>
       <bean:write name="onerowofuserdataset" property="countobs" /></a></td>
     </tr>


    </logic:greaterThan>

  </logic:iterate>
  </table>
 
  
  <input type="submit" value="Get Constancy Table" />
 </form>
</logic:notEmpty>

 
@webpage_footer_html@


