@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@
    @ajax_js_include@
    @datacart_js_include@

<!-- $Id: userdataset_deleted.jsp,v 1.1 2005-08-11 20:31:09 mlee Exp $ -->
<!-- purpose : show user's expired datasets,  -->

 
<TITLE>VegBank Compost Bin: Your Deleted Datasets</TITLE>

  
 <%@ include file="includeviews/inlinestyles.jsp" %> 
 @webpage_masthead_html@ 
  @possibly_center@  
<br/>  
<h1>Your Deleted Datasets</h1>
<p class="instructions">
The following is a list of any datasets you have deleted within the last month.
You can restore any of them so that they appear in your 
<a href="@web_context@DisplayDatasets.do">list of regular datasets.</a>
One month after being deleted, they are permanently removed from the system.
</p>
<!-- get the deleted datasets -->

  <vegbank:get id="userdataset" select="userdataset_deleted" beanName="map" 
  where="where_usrpk" wparam="<%= strWebUserId %>" pager="false" perPage="-1" />

 <logic:empty name="userdataset-BEANLIST">
   <p>Sorry, no Datasets found! 
      <% if ( strWebUserId == "-1" ) {  %>  
        You are not logged in. 
        <a href="@general_link@login.jsp">Login Here.</a> 
      <% } %>
   </p>
 </logic:empty>
 <logic:notEmpty name="userdataset-BEANLIST">

<table class="thinlines">
 <!-- display the right stuff -->
<tr>
 <th>Restore?</th>
 <th>Deleted Date</th>
<%@ include file="custom/userdataset_summary_head.jsp" %>
</tr>

<logic:iterate id="onerowofuserdataset" name="userdataset-BEANLIST">
<tr class="@nextcolorclass@" id="<bean:write name='onerowofuserdataset' property='userdataset_id' />">
  <td rowspan="2" class="control_tab_link">
  <a href="#" onclick="removeDatasetRow(this.parentNode,true);return false;" class="nobg">Restore</a>
  </td>
  <td>
    <logic:notEmpty property="datasetstop_datetrunc" name="onerowofuserdataset"><span title="<bean:write name='onerowofuserdataset' property='datasetstop' />">
              @subst_lt@dt:format pattern="dd-MMM-yyyy"@subst_gt@
              @subst_lt@dt:parse pattern="yyyy-MM-dd"@subst_gt@
            <bean:write name="onerowofuserdataset" property="datasetstop_datetrunc"/>
               @subst_lt@/dt:parse@subst_gt@
               @subst_lt@/dt:format@subst_gt@
             </span>
    </logic:notEmpty>
  </td>
<%@ include file="custom/userdataset_summary_data.jsp" %>
</tr>
<logic:empty name="onerowofuserdataset" property="datasetdescription">
    <bean:define id="dsDesc" value="no description"/>
</logic:empty>
<logic:notEmpty name="onerowofuserdataset" property="datasetdescription">
    <bean:define id="dsDesc" name="onerowofuserdataset" property="datasetdescription"/>
</logic:notEmpty>
<tr class="<%= rowClass %>" id="<bean:write name="onerowofuserdataset" property="userdataset_id" />-desc">
<td colspan="4" class="special_note"><bean:write name="dsDesc" /></td>
</tr>
</logic:iterate>
</table>
<br />
</logic:notEmpty>



          @webpage_footer_html@
