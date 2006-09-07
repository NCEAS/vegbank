@webpage_top_html@
 @stdvegbankget_jspdeclarations@
 @webpage_head_html@
   <!-- include script for sorting tables -->
   <script language="javascript" src="@includes_link@sort_table.js"></script>

 
 <TITLE>View VegBank Data: Stems </TITLE>

<!-- justa simple include now -->
<%@ include file="includeviews/inlinestyles.jsp" %>

<%     int rowOrder = 1;  %><!-- this is the number for the original sort order -->

@webpage_masthead_html@ 


      @possibly_center@
        <h2>View VegBank Stems</h2>
  
  <!-- *********************************************************************** -->
  <!-- IMPORTANT NOTE: THIS VIEW MUST BE SORTED by unique key,
        else pagination may not work! --> 
  <!-- *********************************************************************** -->
   
       <logic:notPresent parameter="orderBy">
         <!-- set default sorting -->
         <bean:define id="orderBy" value="orderby_stemobs" />
       </logic:notPresent>

       <logic:notPresent parameter="perPage">
         <!-- set default sorting -->
         <bean:define id="perPage" value="800" />
       </logic:notPresent>
        
        <vegbank:get id="stemcount" select="stemcount" beanName="map" pager="true" 
          allowOrderBy="true"  />

  <vegbank:pager />
  <logic:empty name="stemcount-BEANLIST">
    <p>  Sorry, no Stems found.</p>
  </logic:empty>
  <logic:notEmpty name="stemcount-BEANLIST">
  <!-- check to see if this is a one plot or many plots view -->
  <!-- one plot if where="where_observation_pk" and wparam exists as param -->
  
  <bean:define id="onePlot" value="false" /><!-- default value -->
  <logic:present parameter="wparam">
   <logic:notMatch parameter="wparam" value=","><!-- only single pk given -->
    <logic:equal parameter="where" value="where_observation_pk">
      <bean:define id="onePlot" value="true" />
      <bean:parameter id="observation_pk" name="wparam" />
      <p>The following stems are from the plot: <%@ include file="includeviews/sub_observation_transl.jsp" %></p>
    </logic:equal>
   </logic:notMatch> 
  </logic:present>
  
  
    <p>
  
      <!-- menu of plant names : -->
      <bean:define id="showTaxonNameDivID">stemdatatable</bean:define>
      <%@ include file="includeviews/sub_taxonimportance_showallplantnames_menu.jsp" %>
     <!-- <br/>
      <a href="javascript:void setupConfig('<bean:write name='thisviewid' />');">Configure data displayed on this page</a> -->
    </p>
    
    
    
    <table id="stemdatatable" class="thinlines sortable" cellpadding="2">
     <tr>
       <th title="original sort order">ord</th>
       <logic:notEqual name="onePlot" value="true"> <th>Plot</th></logic:notEqual>
       <%@ include file="autogen/taxonobservation_summary_head.jsp" %>
       <%@ include file="autogen/stemcount_summary_head.jsp" %>
       <th class="graphic_stemsize">Diameter Graphic</th>
     </tr>
     <logic:iterate id="onerowofstemcount" name="stemcount-BEANLIST">
       <!-- clone stemcount for new includes file from taxonobs -->
       <bean:define id="onerowoftaxonobservation" name="onerowofstemcount" />
       <bean:define id="observation_pk" name="onerowofstemcount" property="observation_id" />
       <tr><!-- class="@nextcolorclass@"-->
         <td><%= rowOrder ++ %></td>
         <logic:notEqual name="onePlot" value="true"> 
           <td class="smallfield">
             <a href="@get_link@comprehensive/observation/<bean:write name='observation_pk' />"><bean:write name="onerowofstemcount" property="observation_id_transl" /></a></td>
         </logic:notEqual>
         <%@ include file="autogen/taxonobservation_summary_data.jsp" %>
         <%@ include file="autogen/stemcount_summary_data.jsp" %>
         <td class="graphic_stemsize"><%@ include file="includeviews/sub_stemcount_graphical.jsp" %></td>
       </tr>
          <!--bean:define id="stemcount_pk" name="onerowofstemcount" property="stemcount_id" /-->
     </logic:iterate>
   </table>
  </logic:notEmpty>
  <br />
  <vegbank:pager />
     
  @webpage_footer_html@