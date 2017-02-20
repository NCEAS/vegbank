<!-- this is a sub form that should be included in another .jsp that gets the datasets needed.  This form displays the contents of the dataset -->
<!-- note: most often, you'll need to see just one dataset at a time in this form.  If so, pager will apply to DATASETITEMS.  Else, pager still applies to dataset -->


 <logic:empty name="userdataset-BEANLIST">
   <p>Sorry, no Datasets found!
      <% if ( strWebUserId == "-1" ) {  %>
        You are not logged in.
        <a href="@general_link@login.jsp">Login Here.</a>
      <% } %>
   </p>
 </logic:empty>
<logic:notEmpty name="userdataset-BEANLIST"><!-- set up table -->

<bean:define id="mainPagerAsBean">
  <!-- the pager, as saved : -->&nbsp;<vegbank:pager />
</bean:define>

<logic:iterate id="onerowofuserdataset" name="userdataset-BEANLIST"><!-- iterate over all records in set : new table for each -->
<br />
<table class="leftrightborders" cellpadding="3"><!--each field, only write when HAS contents-->

<%@ include file="../autogen/userdataset_detail_data.jsp" %>

<logic:equal name="onerowofuserdataset" property="datasetsharing"  value="public">
<tr class="@nextcolorclass@"><td class="datalabel">Public Link</td><td><a href='/cite/<bean:write name="onerowofuserdataset" property="accessioncode" />'>@machine_url@/cite/<bean:write name="onerowofuserdataset" property="accessioncode" /></a>

</tr>
</logic:equal>
<tr class="@nextcolorclass@"><td class="datalabel">Items in this Dataset:</td>
<td class="smallfield"><%@ include file="../custom/userdatasetitem_quicksummary_data.jsp" %></td>
</tr>


<!-- show dataset items -->
<TR><TD colspan="2">

<bean:define id="ud_id" name="onerowofuserdataset" property="userdataset_id" />

 <!-- ALLWAYS paginate, but reset perPage and pageNumber if "not" paginating" -->
 <!-- pagination is opposite of last pagination, which should be still defined, even if not, still will be paginated here: -->
 <!--bean:define id="paginateSub" value="true" /-->
 <!--logic:equal name="paginateMain" value="true" -->
   <!--bean:define id="paginateSub" value="false" /-->
 <!--/logic:equal-->
 <!-- check to see if perPage is defined, if so, capture it, else set it.  Need to know this to know when to add link to all userDatasetItems if NOT paginating here. -->

   <!-- going through all this to get beanPerPage set as string -->
   <bean:parameter id="beanPerPage" name="perPage" value="10" />
   <logic:empty name="beanPerPage">
     <bean:define id="beanPerPage" value="10" />
   </logic:empty>
   <!-- do same with pageNumber.  Reset to 1 if paginating main form -->
   <bean:parameter id="beanPageNumber" name="pageNumber" value="1" />
   <logic:empty name="beanPageNumber">
        <bean:define id="beanPageNumber" value="1" />
   </logic:empty>
   <logic:equal name="paginateMain" value="true">
        <!-- resetting sub to page 1 -->
        <bean:define id="beanPageNumber" value="1" />
        <!-- resetting perPage to 10 -->
        <bean:define id="beanPerPage" value="10" />
   </logic:equal>

<vegbank:get id="userdatasetitem" select="userdatasetitem" beanName="map"
  where="where_userdataset_pk" wparam="ud_id" pager="true" perPage="<%= beanPerPage %>" pageNumber="<%= beanPageNumber %>" />
   <logic:empty name="userdatasetitem-BEANLIST">
      Nothing is in the dataset.
   </logic:empty>

<logic:notEmpty name="userdatasetitem-BEANLIST">
<form action="" method="GET" id="cartable">
<table class="leftrightborders" cellpadding="2" >
<tr>
  <logic:equal name="thisisdatacart" value="true">
    <th>Drop</th>
  </logic:equal>
<th>Item</th>
<%@ include file="../autogen/userdatasetitem_summary_head.jsp" %>
<th>More Info</th>
</tr>

<%@ include file="../../includes/setup_rowindex.jsp" %>
<% long countUDIRows = 0 ; %>
<logic:equal name="beanPageNumber" value="1">
  <% rowIndex = 1; %>
</logic:equal>
<logic:iterate id="onerowofuserdatasetitem" name="userdatasetitem-BEANLIST">
<% countUDIRows++ ;%>
<tr class='@nextcolorclass@'>
<bean:define id="delta_ac" name="onerowofuserdatasetitem" property="itemaccessioncode" />
<!-- only show if this is the datacart -->
<logic:equal name="thisisdatacart" value="true">
  <td>
   <%@ include file="/includes/datacart_checkbox.jsp" %>
   <%= rowIndex++ %>.&nbsp;
  </td>
</logic:equal>
<!-- dont cite these as sometimes accession code isn't correct, instead use pk and item type -->
<bean:define id="tableToView"><bean:write name="onerowofuserdatasetitem" property="itemtable"/></bean:define><!-- writing bean makes it a string so we can use toLowerCase() below -->
<td class="smallfield"><a href='@get_link@std/<%= tableToView.toLowerCase() %>/<bean:write name="onerowofuserdatasetitem" property="itemrecord"/>'>link</a></td>
<%@ include file="../autogen/userdatasetitem_summary_data.jsp" %>
<td class="smallfield">
  <logic:equal name="onerowofuserdatasetitem" property="itemtable" value="observation">
    <!-- add some info about observations if this is an observation -->
      <bean:define id="obs_pk" name="onerowofuserdatasetitem" property="itemrecord" />
      <!--bean:write name="obs_pk" /-->
      <vegbank:get id="obs" select="plotandobservation" beanName="map"
        where="where_observation_pk" wparam="obs_pk" pager="false" />
        <logic:notEmpty name="BEAN">
            <bean:write name="BEAN" property="authorplotcode" />: <bean:write name="BEAN" property="stateprovince" />
        </logic:notEmpty>
  </logic:equal>
&nbsp; </td>
</tr>



  <!-- this defines specific variables to use in datacart -->
  <logic:equal name="onerowofuserdataset" property="datasettype" value="datacart">
    <logic:equal name="onerowofuserdatasetitem" property="itemtable" value="plantconcept">
      <bean:define id="datacart_contains_plantconcept" value="true" />
    </logic:equal>
    <logic:equal name="onerowofuserdatasetitem" property="itemtable" value="commconcept">
      <bean:define id="datacart_contains_commconcept" value="true" />
    </logic:equal>
    <logic:equal name="onerowofuserdatasetitem" property="itemtable" value="observation">
      <bean:define id="datacart_contains_observation" value="true" />
    </logic:equal>
  </logic:equal>

</logic:iterate>
<!-- if you've made it this far, and you are NOT paginating datasetitems, but HAVE reached the number of iterations as perPage, then provide link to full dataset -->
  <logic:equal name="paginateMain" value="true">
    <bean:define id="strCountUDIRows"><%= countUDIRows %></bean:define>
    <logic:equal name="beanPerPage" value="<%= strCountUDIRows %>">
      <!-- tell user about more udi's being possible -->
      <tr><td colspan="10">There could be more items.  <a href="@get_link@detail/userdataset/<bean:write name='ud_id' />">Click here to see the full dataset</a></td></tr>
    </logic:equal>
  </logic:equal>
  <logic:notEqual name="paginateMain" value="true">
    <!-- then we CAN paginate the items here -->
    <tr><td colspan="10"><vegbank:pager /></td></tr>
  </logic:notEqual>
</table>
</form>
@mark_datacart_items@
</logic:notEmpty>
</TD></TR>

</table>



</logic:iterate>

<!-- writes the pager calculated BEFORE the second :get statement -->
<bean:write name="mainPagerAsBean" filter="false" />
</logic:notEmpty>

