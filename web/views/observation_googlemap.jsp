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

   <!-- the following file allows configured icons -->
   <%@ include file="includeviews/inlinestyles.jsp" %>

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
       <div style="width: 700px" id="googleMapPageContainer">
         <div style="width: 97%" id="iconKeyDiv" class="thinlines">
           <strong>Combined Accuracy:</strong>
           Each plot's icon reflects the coordinate accuracy combined with any fuzzing (confidentiality) applied. <br/>
           <span id="iconkey1"> </span>0-50m  &nbsp;&nbsp;&nbsp;&nbsp;
           <span id="iconkey2"> </span>50-200m  &nbsp;&nbsp;&nbsp;&nbsp;
           <span id="iconkey3"> </span>200-1000m  &nbsp;&nbsp;&nbsp;&nbsp;
           <span id="iconkey4"> </span>&gt; 1000m  &nbsp;&nbsp;&nbsp;&nbsp;
           <span id="iconkey5"> </span>unknown  &nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:void changeMappingIcon('<logic:equal cookie="globalmapping_icons_not_colored" value="show">no</logic:equal>letters');">change icons</a>
         </div>
         <div id="map" style="width: 100%; height: 500px"></div>
       </div>

    <logic:empty name="plotobs-BEANLIST">
                    Sorry, no plots are available.
    </logic:empty>
    <logic:notEmpty name="plotobs-BEANLIST">
        <bean:define id="jsPlotMapping">
          // init this javascript bean
        </bean:define>
       <bean:define id="jsBoundaryMapping">
               // start mapping boundary, if applicable
       </bean:define>

       <logic:iterate id="onerowofobservation" name="BEANLIST">
         <bean:define id="obsId" name="onerowofobservation" property="observation_id"/>
         <bean:define id="observation_pk" name="onerowofobservation" property="observation_id"/>
         <div class="hidden" id="obsid_<bean:write name='obsId' />">
           <%@ include file="includeviews/sub_plotmappingdata.jsp" %>
         </div>
         <!-- define javascript call for this plot: -->
          <bean:define id="currlat"><bean:write name="onerowofobservation" property="latitude" /><logic:empty name="onerowofobservation" property="latitude">0</logic:empty></bean:define>
          <bean:define id="currlong"><bean:write name="onerowofobservation" property="longitude" /><logic:empty name="onerowofobservation" property="longitude">0</logic:empty></bean:define>
          <bean:define id="curraccuracy"><bean:write name="onerowofobservation" property="locationaccuracy" /><logic:empty name="onerowofobservation" property="locationaccuracy">-1</logic:empty></bean:define>
          <bean:define id="jsazimuth"><bean:write name="onerowofobservation" property="azimuth" /><logic:empty name="onerowofobservation" property="azimuth">null</logic:empty></bean:define>
          <div class="hidden" id="obsid_<bean:write name='obsId' />_dsgpoly"><bean:write name="onerowofobservation" property="dsgpoly" /><logic:empty name="onerowofobservation" property="dsgpoly">null</logic:empty></div>
         <bean:define id="jsPlotMapping">
           <bean:write name="jsPlotMapping" filter="false" />
           countPlots = VbGCreateMarker(<bean:write name="currlat" />,<bean:write name="currlong" />,
                                   gebid("obsid_<bean:write name="obsId" />").innerHTML,
                                   VbGDifferentiateMarker(blnColored,<bean:write name="currlat" />,<bean:write name="currlong" />,<bean:write name="onerowofobservation" property="degrees_fuzzed" />,<bean:write name="curraccuracy" />),
                                   blnColored, map, countPlots, countPlotsToConfirm,
                                   <bean:write name="onerowofobservation" property="degrees_fuzzed" />,<bean:write name="curraccuracy" />,
								   <bean:write name="jsazimuth" />, null,null,null,null,
								   gebid("obsid_<bean:write name='obsId' />_dsgpoly").innerHTML, false, false)
                                     //DEGREESERR,METERSERR, AZIMUTH, PLOTX, PLOTY, GPSX, GPSY, DSGPOLY , BLNAUTODRAWACC, BLNAUTODRAWBOUNDS
          //  alert('ok to here');
          // this is too slow for more than a few plots, and it still doesn't work in IE
          // if (countPlots > 0) {
             // alert('trying to find err');
          //   VbGMarkCircularAccuracy(<bean:write name="currlat" />,<bean:write name="currlong" />,
          //        <bean:write name="onerowofobservation" property="degrees_fuzzed" />,<bean:write name="curraccuracy" />,map);
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
     <bean:parameter id="pboundminlat" name="xwhereParams_minlatitude_0" value="5000" />
     <bean:parameter id="pboundmaxlat" name="xwhereParams_maxlatitude_0" value="-5000" />
     <bean:parameter id="pboundminlng" name="xwhereParams_minlongitude_0" value="5000" />
     <bean:parameter id="pboundmaxlng" name="xwhereParams_maxlongitude_0" value="-5000" />
     <bean:define id="boundminlat"><bean:write name="pboundminlat" /><logic:empty name="pboundminlat">5000</logic:empty></bean:define>
     <bean:define id="boundmaxlat"><bean:write name="pboundmaxlat" /><logic:empty name="pboundmaxlat">-5000</logic:empty></bean:define>
     <bean:define id="boundminlng"><bean:write name="pboundminlng" /><logic:empty name="pboundminlng">5000</logic:empty></bean:define>
     <bean:define id="boundmaxlng"><bean:write name="pboundmaxlng" /><logic:empty name="pboundmaxlng">-5000</logic:empty></bean:define>
             <!-- draw bounding box, too ?? -->
             <bean:define id="jsBoundaryMapping">
               // start mapping boundary, if applicable
               if ( (<bean:write name="boundminlat" /> < 5000  ) &&
                    (<bean:write name="boundmaxlat" /> > -5000) &&
                    (<bean:write name="boundminlng" /> < 5000) &&
                    (<bean:write name="boundmaxlng" /> > -5000) && true  ) {
                 // do the mapping:
                 VbGMarkRectangle(<bean:write name="boundminlat" />,<bean:write name="boundminlng" />,
                                  <bean:write name="boundmaxlat" />,<bean:write name="boundmaxlng" />,map);
               }
             </bean:define>



   </logic:notEmpty> <!-- obs not empty -->


<br />
<p><a href="@plotquery_page_advanced@">Search for more plots</a>
  | <a href="@views_link@plot-query-bymap.jsp">Search for plots using a map</a></p>

@webpage_footer_html@

<!-- now we throw in the js to map things -->
 <script type="text/javascript">
  <!-- //<![CDATA[
  function VbGLoadAllMapsThisPage(){
   var countPlots = 0;
   var countPlotsToConfirm = -1; // look up constant
   var blnColored = true;  //default
   <logic:equal cookie="globalmapping_icons_not_colored" value="show">
      blnColored = false;
   </logic:equal>
  <logic:notEmpty name="plotobs-BEANLIST">
   //fill in key:
   VbGProvideKeyIcon(blnColored,VbGDifferentiateMarker(blnColored,0,0,0,25)  ,"iconkey1");
   VbGProvideKeyIcon(blnColored,VbGDifferentiateMarker(blnColored,0,0,0,75)  ,"iconkey2");
   VbGProvideKeyIcon(blnColored,VbGDifferentiateMarker(blnColored,0,0,0,250) ,"iconkey3");
   VbGProvideKeyIcon(blnColored,VbGDifferentiateMarker(blnColored,0,0,0,1250),"iconkey4");
   VbGProvideKeyIcon(blnColored,VbGDifferentiateMarker(blnColored,0,0,0,-1)  ,"iconkey5");

   var map  = VbGMapLoadByBounds("map",<%= VbGminLat %>,<%= VbGmaxLat %>,<%= VbGminLong %>,<%= VbGmaxLong %>,2);
   <bean:write name="jsPlotMapping" filter="false" />
   <bean:write name="jsBoundaryMapping" filter="false" />
  </logic:notEmpty>
   }
   //]]>
  -->
</script>