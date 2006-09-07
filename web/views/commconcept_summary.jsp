@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@

  @ajax_js_include@
  @datacart_js_include@




 
<TITLE>View VegBank Community Concepts - Summary</TITLE>
<script type="text/javascript">
function getHelpPageId() {
  return "view-comm-summary";
}

</script>



  
 <%@ include file="includeviews/inlinestyles.jsp" %> 
 @webpage_masthead_html@ 
  @possibly_center@ 

<h2>View Community Concepts - Summary</h2>
  
  <!-- tell the user what was searched for, if criteriaAsText is passed here : -->
 <div id="tut_commcriteriamessage">
  <logic:present parameter="criteriaAsText">
    <bean:parameter id="bean_criteriaAsText" name="criteriaAsText" />
    <logic:notEmpty name="bean_criteriaAsText">
      <p class="psmall">You searched for communities: <bean:write name="bean_criteriaAsText" /></p>
    </logic:notEmpty>
  </logic:present>  
 </div>
  
  <logic:notPresent parameter="orderBy">
         <!-- set default sorting -->
         <bean:define id="orderBy" value="xorderby_d_obscount_desc" />
  </logic:notPresent>
    
  <vegbank:get id="concept" select="commconcept" beanName="map" pager="true" xwhereEnable="true" 
     allowOrderBy="true" />

<vegbank:pager />

 <!-- add all results -->
 <p>
        <a href="javascript:addAllResults('commconcept');"
            title="add all query results to datacart" class="nobg"><img src="/vegbank/images/cart_star_on_blue2.gif" border="0" id="datacart-addallresults-icon" /></a>

        <a href="javascript:addAllResults('commconcept');"
            title="add all results to datacart">add all query results</a> to datacart,&nbsp;&nbsp;

        <a href="javascript:addAllOnPage()" title="add all on page to datacart" class="nobg"><img src="/vegbank/images/cart_add_one.gif" border="0" /></a>
        <a href="javascript:addAllOnPage()" title="add all on page">add plots on page</a> to datacart,&nbsp;&nbsp;
                                                                                                                                                                                                  
    <!-- drop page -->

        <a href="javascript:dropAllOnPage()" title="drop all on page from datacart" class="nobg"><img src="/vegbank/images/cart_drop_one.gif" border="0" /></a>
        <a href="javascript:dropAllOnPage()" title="drop all on page">drop plots on page</a> from datacart

    <%@ include file="../includes/setup_rowindex.jsp" %>
  </p>

    <logic:equal parameter="delta" value="findadd-accessioncode">
        <vegbank:datacart delta="findadd:commconcept:commconcept:commconcept_id:accessioncode" deltaItems="getQuery" display="false" />
    </logic:equal>


<logic:empty name="concept-BEANLIST">
             <p>Sorry, no Community concepts match your criteria.</p>
          </logic:empty>
<logic:notEmpty name="concept-BEANLIST"><!-- set up table -->

<form action="" method="GET" id="cartable">
<table width="100%" cellpadding="2" class="leftrightborders" ><!--each field, only write when HAS contents-->
<!-- header -->
<tr id="tut_mainheaderrow">
      <th valign="bottom" align="center" nowrap="nowrap">Add/Drop</th>
     <bean:define id="thisfield" value="commname" />
      <bean:define id="fieldlabel">Name</bean:define>
      <bean:define id="thatts">width="30%"</bean:define>
    <%@ include file="../includes/orderbythisfield.jsp" %>
  
     <bean:define id="thisfield" value="reference_id_transl" />
      <bean:define id="fieldlabel">Reference</bean:define>
      <bean:define id="thatts">width="7%"</bean:define>
    <%@ include file="../includes/orderbythisfield.jsp" %>
  
     <bean:define id="thisfield" value="d_obscount" />
      <bean:define id="fieldlabel">Plots</bean:define>
      <bean:define id="thatts">width="5%"</bean:define>
    <%@ include file="../includes/orderbythisfield.jsp" %>
  
  <th width="58%">Description</th>
</tr>

<bean:define id="addSelectButton" value="no" />
<logic:present parameter="requestingForm">
 
    <!-- requestingForm param is not empty -->
    <bean:define id="addSelectButton" value="yes" />
 
</logic:present>


<logic:iterate id="onerow" name="concept-BEANLIST"><!-- iterate over all records in set : new table for each -->

<tr class='@nextcolorclass@'>
      <!-- data cart -->
      <td align="center"><!-- that td cannot have a class, it gets overwritten -->
        <logic:notEmpty name="onerow" property="accessioncode">
          <bean:define id="delta_ac" name="onerow" property="accessioncode" />
          Comm #<%= rowIndex++ %>
          <br/>
          <%@ include file="../includes/datacart_checkbox.jsp" %>
        </logic:notEmpty>  
      </td>
<td class="smallfield"><bean:write name="onerow" property="commname_id_transl"/><br/><a href='@get_link@detail/commconcept/<bean:write name="onerow" property="commconcept_id"/>'>&raquo; more details</a>
  <br/>accession code: <bean:write name="onerow" property="accessioncode" />
  <logic:equal name="onerow" property="d_currentaccepted" value="f"><strong> NOT CURRENTLY ACCEPTED</strong></logic:equal>
  <!-- add "select" button if certain params are present and notEmpty -->
  <logic:equal name="addSelectButton" value="yes">
    <br/><a name="#comm<bean:write name='onerow' property='commconcept_id' />" href="#comm<bean:write name='onerow' property='commconcept_id' />"
       onclick="setOpenerFormValue('<bean:write name="onerow" property="accessioncode" />')"><img src="@images_link@btn_select.png" alt="--SELECT--" /></a>
  </logic:equal>
</td>
<td class="largefield"><a href='@get_link@std/reference/<bean:write name="onerow" property="reference_id"/>'><bean:write name="onerow" property="reference_id_transl"/></a>&nbsp;</td>
<td class="numeric smallfield">
<bean:define id="concId" name="onerow" property="commconcept_id"/>
<logic:equal name="onerow" property="d_obscount" value="0">0</logic:equal>
<logic:notEqual name="onerow" property="d_obscount" value="0">

 <bean:define id="critAsTxt">
  Interpreted as the community: <bean:write name="onerow" property="commname_id_transl"/> [<bean:write name="onerow" property="reference_id_transl"/>]
 </bean:define>
  <%  
    /* create a map of parameters to pass to the new link: */
    java.util.HashMap params = new java.util.HashMap();
    params.put("wparam", concId);
    params.put("where", "where_commconcept_observation_complex");
    params.put("criteriaAsText", critAsTxt);
    pageContext.setAttribute("paramsName", params);
  %>

<html:link page="/views/observation_summary.jsp" name="paramsName" scope="page" ><bean:write name="onerow" property="d_obscount" /></html:link>

</logic:notEqual></td>
<td class="largefield"><bean:write name="onerow" property="commdescription"/>&nbsp;</td>

</tr>

</logic:iterate>
</table>
</form>
@mark_datacart_items@

</logic:notEmpty>

<br/>
<vegbank:pager/>

<br/>
          @webpage_footer_html@
