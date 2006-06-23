@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@
  @ajax_js_include@
  @datacart_js_include@
     <!-- style bit is for google maps -->
     <style type="text/css">
     v\:* {
       behavior:url(#default#VML);
     }
    </style>
  
   <%
     int inttemp=0 ; 
     String howManyPlantsComplex="3"; // this many plants if complex query
     String howManyPlantsDefault="5"; // this many plants if default query (overrides above)
    %>
    
        <% double VbGminLat = 500.03; %>
            <% double VbGmaxLat = -500.03; %>
            <% double VbGminLong = 500.03; %>
        <% double VbGmaxLong = -500.03; %>
       
     <!-- key is set in build.properties -->
    <script src="http://maps.google.com/maps?file=api&v=2&key=@googlemaps_apikey@" type="text/javascript"></script>

    <script src="@includes_link@VbGoogleMap.js" type="text/javascript"></script>
    
    <title>VegBank Plot Query Results -- Mapped </title>
    
    @webpage_masthead_html@
    
     <h2><img src="@images_link@maplogo.gif" /> VegBank Plots -- mapped with Google Maps</h2>
     <p>If you are having problems with this mapping functionality, please see the instructions
     and tips on <a href="@forms_link@MapDatasets.jsp">this page</a>. </p>
     
     
        <!-- pagination is handled by the google javascript function where you are asked if you want to keep mapping after N plots -->
        <bean:define id="perPage" value="-1" />
     
     <!-- call sub file to get observations (this code is reused)-->
       <%@ include file="includeviews/sub_getobservation.jsp" %>

     
        
        <!-- all this page does is iterate through the plots and generate js to map them -->
        
        <!-- dont forget this: -->
         <div id="map" style="width: 700px; height: 500px"></div>


    <logic:empty name="plotobs-BEANLIST">
                    Sorry, no plots are available.
    </logic:empty>
    <logic:notEmpty name="plotobs-BEANLIST">
        <bean:define id="jsPlotMapping">
          // init this javascript bean 
        </bean:define>
       
    
       <logic:iterate id="onerowofobservation" name="BEANLIST">
         <bean:define id="obsId" name="onerowofobservation" property="observation_id"/>
         <bean:define id="observation_pk" name="onerowofobservation" property="observation_id"/>
         <div class="hidden" id="obsid_<bean:write name='obsId' />">
           <%@ include file="includeviews/sub_plotmappingdata.jsp" %>
         </div>
         <!-- define javascript call for this plot: -->
          <bean:define id="currlat"><bean:write name="onerowofobservation" property="latitude" /></bean:define>
          <bean:define id="currlong"><bean:write name="onerowofobservation" property="longitude" /></bean:define>
          <bean:define id="curraccuracy"><bean:write name="onerowofobservation" property="locationaccuracy" /><logic:empty name="onerowofobservation" property="locationaccuracy">0</logic:empty></bean:define>
         <bean:define id="jsPlotMapping">
           <bean:write name="jsPlotMapping" filter="false" />
           countPlots = VbGCreateMarker(<bean:write name="currlat" />,
                                        <bean:write name="currlong" />, 
                                   gebid("obsid_" + <bean:write name="obsId" /> ).innerHTML, 
                                   1, true, map, countPlots, countPlotsToConfirm )
          //  alert('ok to here');
          // if (countPlots > 0) {
             // alert('trying to find err');
            // VbGMarkCircularAccuracy(<bean:write name="currlat" />,<bean:write name="currlong" />,0,0,map);
          // }
           // alert('ok to circ accuracy');
         </bean:define> <!-- end of defining javascript call at end of this -->
         <!-- update min and max Lat/Long -->
           <% if ( VbGminLat > java.lang.Double.parseDouble(currlat) ) {
                      VbGminLat = java.lang.Double.parseDouble(currlat) ;
                 } %>
                 <% if ( VbGmaxLat < java.lang.Double.parseDouble(currlat) ) {
                      VbGmaxLat = java.lang.Double.parseDouble(currlat) ;
                 } %>
         
                 <% if ( VbGminLong > java.lang.Double.parseDouble(currlong) ) {
                      VbGminLong = java.lang.Double.parseDouble(currlong) ;
                 } %>
                 <% if ( VbGmaxLong < java.lang.Double.parseDouble(currlong) ) {
                      VbGmaxLong = java.lang.Double.parseDouble(currlong) ;
            } %>
           
     </logic:iterate> <!-- iterating across obs -->
     
     <!-- check for presence of min and max boundaries -->
     <bean:parameter id="boundminlat" name="xwhereParams_minlatitude_0" value="5000" />
     <bean:parameter id="boundmaxlat" name="xwhereParams_maxlatitude_0" value="-5000" />
     <bean:parameter id="boundminlng" name="xwhereParams_minlongitude_0" value="5000" />
     <bean:parameter id="boundmaxlng" name="xwhereParams_maxlongitude_0" value="-5000" />
             <!-- draw bounding box, too ?? -->
             <bean:define id="jsBoundaryMapping">
               // start mapping boundary, if applicable
               if ( (<bean:write name="boundminlat" /> < 5000  ) && 
                    (<bean:write name="boundmaxlat" /> > -5000) && 
                    (<bean:write name="boundminlng" /> < 5000) && 
                    (<bean:write name="boundmaxlng" /> > -5000)  ) {
                 // do the mapping:
                 VbGMarkRectangle(<bean:write name="boundminlat" />,<bean:write name="boundminlng" />,
                                  <bean:write name="boundmaxlat" />,<bean:write name="boundmaxlng" />,map);
               }
             </bean:define>
           
     
     
   </logic:notEmpty> <!-- obs not empty -->
   
   
<br />
@webpage_footer_html@

<!-- now we throw in the js to map things -->
 <script type="text/javascript">
  <!-- //<![CDATA[
  function VbGLoadAllMapsThisPage(){
   var countPlots = 0;
   var countPlotsToConfirm = -1; // look up constant
   var map  = VbGMapLoadByBounds("map",<%= VbGminLat %>,<%= VbGmaxLat %>,<%= VbGminLong %>,<%= VbGmaxLong %>,2);
   <bean:write name="jsPlotMapping" filter="false" />
   <bean:write name="jsBoundaryMapping" filter="false" />
   
   }
   //]]>
  -->
</script>