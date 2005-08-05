<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:v="urn:schemas-microsoft-com:vml">
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@
 
     <style type="text/css">
     v\:* {
       behavior:url(#default#VML);
     }
    </style>
 
       <!-- look for special set of dataset params: -->
      <!-- the contents of the following include are commented out: 
        <%@ include file="includeviews/get_datasetparams.jsp" %> 
     -->
 
 <!-- key is set in build.properties -->
<script src="http://maps.google.com/maps?file=api&v=1&key=@googlemaps_apikey@" type="text/javascript"></script>
 
<TITLE>Google Map of VegBank Datasets</TITLE>
 
<bean:define id="pageContentBean"><!-- init main page content: --></bean:define> 


<!-- define where if not defined -->
<bean:parameter id="where" name="where" value="where_userdataset_pk" />


 
    <% int mapNum = -1; %>    
            <!-- define vars to get min/max lat and long, intialize to undefined values  -->
            <% double minLat = 500.03; %>
            <% double maxLat = -500.03; %>
            <% double minLong = 500.03; %>
            <% double maxLong = -500.03; %>
   <!-- check to see if wparam is bad -->
<logic:notEqual name="wparambad" value="true">             
     <!-- wparam is NOT BAD -->       
          <logic:equal name="where" value="where_userdataset_pk">
           
  
            <vegbank:get id="datasets" select="userdataset_countobs" beanName="map"
              pager="false" perPage="-1" allowOrderBy="true" orderBy="xorderby_datasetname" />


  
           <!-- special get for these: -->
            <vegbank:get id="plotobs" select="plotobs_ds_mapping" beanName="map" 
                 pager="false" perPage="-1" allowOrderBy="true" orderBy="orderby_ds_plotcode" />
           <!-- dont replace this beanlist -->
            <bean:define id="groupsdefined" value="true" />
            
          </logic:equal>

                <% String lastDS = "-1"; %>
         <logic:notPresent name="groupsdefined">
         
                
          <vegbank:get id="plotobs" select="plotandobservation" whereNumeric="where_observation_pk" 
                  whereNonNumeric="where_observation_ac" beanName="map" pager="false" perPage="-1"
                  xwhereEnable="true" allowOrderBy="true" />
        </logic:notPresent>
     
      
     <!-- now define script functions that define all this -->
 
<script type="text/javascript">
    //<![CDATA[

  
  
      
  
  // adds the google map loader when the window loads.
  addEvent(window, "load", vegbankGoogleMapLoad);


 function vegbankGoogleMapLoad() {
    
    if (GBrowserIsCompatible()) {
       // alert('starting new map!');
        var map = new GMap(document.getElementById("map"));
        map.centerAndZoom(new GPoint(-82.22100, 36.59370), 4);
        map.addControl(new GLargeMapControl());
        map.addControl(new GMapTypeControl());
         
         // go ahead and set default icon.
         <% String iconPrefix = "map_google_c_" ; %>
  
              <logic:notEmpty name="datasets-BEANLIST">
                // define base of class of icon:
                var baseIcon = new GIcon();
                // do a key, and differentiate icons. 
                // default icons are colored 
                   
                   baseIcon.shadow = "@images_link@map_google_shadow_20.png";
                   baseIcon.iconSize = new GSize(12, 20);
                   baseIcon.shadowSize = new GSize(22, 20);
                   baseIcon.iconAnchor = new GPoint(6, 20);
                   baseIcon.infoWindowAnchor = new GPoint(5, 1);
                   

                   <logic:equal parameter="mapicons" value="nocolors">
                      <% iconPrefix = "map_google_l_" ; %>
                      baseIcon.shadow = "@images_link@map_google_shadow_50.png";
                      baseIcon.iconSize = new GSize(20, 34);
                      baseIcon.shadowSize = new GSize(37, 34);
                      baseIcon.iconAnchor = new GPoint(9, 34);
                      baseIcon.infoWindowAnchor = new GPoint(9, 2);
                      baseIcon.infoShadowAnchor = new GPoint(18, 25);
                   </logic:equal>
                //get as javascript variable, too   
                var iconPrefix = "<%= iconPrefix %>" ;
                
               <bean:define id="mapKey">
               <!-- float these suckers left to make them stay together and not wrap badly: -->
               <div style="float:left; padding:2px"><strong>Dataset Key: </strong> </div>
                <logic:iterate id="onerowofdatasets" name="datasets-BEANLIST">
                   <%     mapNum ++ ;
                          if (mapNum == 10 ) {
                             mapNum = 0;  //reset mapNum to 0, as there are only 0-9 for images.
                          }
                    %>
                  <div style="float:left; padding:2px"> <img src="@images_link@<%= iconPrefix + mapNum + ".png" %>" />
                    <a href="@get_link@detail/userdataset/<bean:write name='onerowofdatasets' property='userdataset_id' />" target="_blank"><bean:write name="onerowofdatasets" property="datasetname" /></a>
                   (<bean:write name="onerowofdatasets" property="countobs" /> plots) </div>
                </logic:iterate>
               </bean:define>
                   <% mapNum = -1 ; %>
              </logic:notEmpty>  
  

     <logic:notEmpty name="plotobs-BEANLIST">
      <logic:iterate id="onerowofobservation" name="plotobs-BEANLIST">
                // figure out which map icon to use 

                <logic:notEmpty name="onerowofobservation" property="userdataset_id">
                  <logic:notEqual value="<%= lastDS %>" name="onerowofobservation" property="userdataset_id">
                    <% 
                    // if not same as before, up mapNum
                    mapNum ++; %>
                    <bean:define id="lastDSBean"><bean:write name="onerowofobservation" property="userdataset_id" /></bean:define>
                    <% lastDS = lastDSBean ; %>
                  </logic:notEqual>
                </logic:notEmpty>
                <logic:empty name="onerowofobservation" property="userdataset_id">
                  <% 
                    // increase  mapNum by 1
                  mapNum ++; %>
                </logic:empty>
                <%   if (mapNum == 10 ) {
                       mapNum = 0;  //reset mapNum to 0, as there are only 0-9 for images.
                       
                     }
                    %>
         <bean:define id="observation_pk" name="onerowofobservation" property="observation_id"/>
           //  write the html for popup window: 
           //   dataset may be null, that's ok 
           

           
        // alert('writing new point <bean:write name='observation_pk'/>');
          
                        <bean:define id="pageContentBean"><bean:write name="pageContentBean" filter="false" />
                            <div id="plot_<bean:write name='observation_pk'/>_<bean:write ignore='true' name='onerowofobservation' property='userdataset_id' />">
                             Plot: <a target="_blank" href="@get_link@comprehensive/observation/<bean:write name='observation_pk'/>"><bean:write name="onerowofobservation" property="authorplotcode" /></a>
                             <logic:notEmpty name="onerowofobservation" property="datasetname">
                              <br/> Dataset: <bean:write name="onerowofobservation" property="datasetname"/>
                             </logic:notEmpty>
                            </div>
                        </bean:define>
          
                 var point = new GPoint(<bean:write name="onerowofobservation" property="longitude" />,
                                        <bean:write name="onerowofobservation" property="latitude" />);
                 var marker = createMarker(point, 
                   document.getElementById("plot_<bean:write name='observation_pk'/>_<bean:write ignore='true' name='onerowofobservation' property='userdataset_id' />").innerHTML,
                     <%= mapNum %>, iconPrefix , baseIcon );
                 //var marker = createMarker(point, "goo");
                             map.addOverlay(marker);
                 
             
        
        //  check to see if lat and long set any boundaries for min max 
        <bean:define id="currlat"><bean:write name="onerowofobservation" property="latitude" /></bean:define>
        <bean:define id="currlong"><bean:write name="onerowofobservation" property="longitude" /></bean:define>
        <% if ( minLat > java.lang.Double.parseDouble(currlat) ) {
             minLat = java.lang.Double.parseDouble(currlat) ;
        } %>
        <% if ( maxLat < java.lang.Double.parseDouble(currlat) ) {
             maxLat = java.lang.Double.parseDouble(currlat) ;
        } %>       
 
        <% if ( minLong > java.lang.Double.parseDouble(currlong) ) {
             minLong = java.lang.Double.parseDouble(currlong) ;
        } %>
        <% if ( maxLong < java.lang.Double.parseDouble(currlong) ) {
             maxLong = java.lang.Double.parseDouble(currlong) ;
        } %>
        
      </logic:iterate>  
     </logic:notEmpty>
 
  
     
       var MyDefaultZoomLevel = 15;
       var MyDoneWithThis = 0;
       var MyCenterLat =  <%= (maxLat + minLat)*0.5 %>;
       var MyCenterLong = <%= (maxLong + minLong)*0.5 %>;
       map.centerAndZoom(new GPoint( MyCenterLong,MyCenterLat), MyDefaultZoomLevel); 
       for(i=1; i<MyDefaultZoomLevel; i++) {
         //try to zoom in and see the plots better
         if (MyDoneWithThis == 0 ) {
           map.centerAndZoom(new GPoint( MyCenterLong,MyCenterLat), (MyDefaultZoomLevel - i)); 
           //get map size
           var bounds = map.getBoundsLatLng();
            var width = bounds.maxX - bounds.minX;
            var height = bounds.maxY - bounds.minY;
           
           //check to see if it is too small
           
           if ( (<%= minLong %> < bounds.minX) || (<%= maxLong %> > bounds.maxX) 
                  || (<%= minLat %> < bounds.minY) || (<%= maxLat %> > ( bounds.maxY - (height/10) )) ) {
             //reached too small, go up one
             map.centerAndZoom(new GPoint( MyCenterLong,MyCenterLat), (MyDefaultZoomLevel - i) + 1);
             // and stop doing this
             MyDoneWithThis = 1;
           } // coords too much
         } //not done rezooming
         
       } //for loop to rezoom
       map.setMapType(G_SATELLITE_TYPE)
      
      var whereismap = new GMap(document.getElementById("whereismap"));
      whereismap.centerAndZoom(new GPoint(MyCenterLong,MyCenterLat), 16);
        var boundsnew = map.getBoundsLatLng();
        var points = [];
        
          points.push(new GPoint(boundsnew.minX, boundsnew.minY));
          points.push(new GPoint(boundsnew.minX, boundsnew.maxY));
          points.push(new GPoint(boundsnew.maxX, boundsnew.maxY));
          points.push(new GPoint(boundsnew.maxX, boundsnew.minY));
          points.push(new GPoint(boundsnew.minX, boundsnew.minY));
          
        whereismap.addOverlay(new GPolyline(points));
        whereismap.disableDragging();
      } //browser is compatible
     
   } // onload function
    
    
     // Creates a marker whose info window displays the given html (not number)
           function createMarker(point, html, number, iconPrefix, baseIcon) {
             var icon = new GIcon(baseIcon);
             icon.image = "@images_link@" + iconPrefix +  number + ".png";
             var marker = new GMarker(point, icon);
           
             // show plot info when clicked
             
             GEvent.addListener(marker, "click", function() {
               marker.openInfoWindowHtml(html);
             });
           
             return marker;
       }
    
    
    //]]>
    </script>
 </logic:notEqual> <!-- wparambad -->
 
 @webpage_masthead_html@ 
 

<logic:equal name="wparambad" value="true">
  <!-- failed, report errors here -->
  <p>
   Sorry, but VegBank didn't access any datasets to map.  
   Please Press <a href="javascript:history.back()">back</a> 
   (the back button on your browser if that doesn't work),
   and try again.
   
   </p>
   <p>
   You can also go <a href="@web_context@">Back to the Home Page.</a>
   </p>
 
</logic:equal>

<logic:notEqual name="wparambad" value="true">

<!-- set up the map: -->
<!-- write hidden page content: -->
  <div class="hidden" id="pagedatums">
    <bean:write name="pageContentBean" filter="false" />
  </div>
  <div style="width: 700px" id="key" >
     <table width="100%" border="0" class="noborder"><tr><td width="80%">
    <h1>Map of your plots</h1>
<span class="instructions">This page shows you a <a target="_blank" href="http://maps.google.com">Google Map</a> of your datasets selected.
However, it is not compatible with all browsers, though most of the major modern browsers 
are satisfactory.  Firefox/Mozilla and Internet Explorer are fine.  See the help pages Google's Website for more on browser compatibility.
<br/>
<strong>Links on this page will open in a new window</strong> (except the built in Google links), as you can move the map around,
and zoom in and out, but if you use the back button to get to this page, the map will be reset.<br/>
</span>
     <bean:write name="mapKey" ignore="true" filter="false"/>               
     
     </td><td>
     <span class="small">Initial location of larger map outlined here:</span><br/>
     <div id="whereismap" style="width: 190px; height: 100px"></div>
     </td></tr></table>        
  
  </div>
  <div id="map" style="width: 700px; height: 500px"></div>

<!--
MIN LAT: <%= minLat %><br/>
max LAT: <%= maxLat %><br/>
MIN long: <%= minLong %><br/>
max long: <%= maxLong %><br/>
-->

</logic:notEqual>

     @webpage_footer_html@
 
