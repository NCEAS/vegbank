<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:v="urn:schemas-microsoft-com:vml">
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@
 
     <style type="text/css">
     v\:* {
       behavior:url(#default#VML);
     }
    </style>
 
 <!-- aldo -->
<script src="http://maps.google.com/maps?file=api&v=1&key=ABQIAAAAGgQ8067Uv8fomyaGenqurhScc7vbZ9k8oOw50vJywIe37DMV3RQQqfC--A7oo1vWYtUwWji02NwdAw" type="text/javascript"></script>
 
<TITLE>Google Map VegBank Datasets</TITLE>
 
<bean:define id="pageContentBean"><!-- init main page content: --></bean:define> 
 
<script type="text/javascript">
    //<![CDATA[

       // Creates a marker whose info window displays the given number
       function createMarker(point, number) {
         var marker = new GMarker(point);
       
         // Show this marker's index in the info window when it is clicked
          var html =  number  ;
         GEvent.addListener(marker, "click", function() {
           marker.openInfoWindowHtml(html);
         });
       
         return marker;
       }
  
  // adds the google map loader when the window loads.
  addEvent(window, "load", vegbankGoogleMapLoad);


 function vegbankGoogleMapLoad() {
    
    if (GBrowserIsCompatible()) {
       // alert('starting new map!');
        var map = new GMap(document.getElementById("map"));
        map.centerAndZoom(new GPoint(-82.22100, 36.59370), 4);
        map.addControl(new GLargeMapControl());
        map.addControl(new GMapTypeControl());
         
  

   <% int mapNum = 0; %>    
           // define vars to get min/max lat and long, intialize to undefined values 
           <% double minLat = 500.03; %>
           <% double maxLat = -500.03; %>
           <% double minLong = 500.03; %>
           <% double maxLong = -500.03; %>
           
           
         <logic:equal parameter="where" value="where_userdataset_pk">
          
 
           <vegbank:get id="datasets" select="userdataset" beanName="map"
             pager="false" perPage="-1" allowOrderBy="true" orderBy="xorderby_datasetname" />
             <logic:notEmpty name="datasets-BEANLIST">
               <logic:iterate id="onerowofdatasets" name="datasets-BEANLIST">
                  // define icons: 
                 
               </logic:iterate>
 
             </logic:notEmpty>  
 
          // special get for these: 
           <vegbank:get id="plotobs" select="plotobs_ds_mapping" beanName="map" 
                pager="false" perPage="-1" allowOrderBy="true" orderBy="orderby_ds_plotcode" />
           // dont replace this beanlist 
           <bean:define id="groupsdefined" value="true" />
           
         </logic:equal>
               <% mapNum = -1 ; %>
               <% String lastDS = "-1"; %>
        <logic:notPresent name="groupsdefined">
        
               
         <vegbank:get id="plotobs" select="plotandobservation" whereNumeric="where_observation_pk" 
                 whereNonNumeric="where_observation_ac" beanName="map" pager="false" perPage="-1"
                 xwhereEnable="true" allowOrderBy="true" />
       </logic:notPresent>
    
     
     // here, loop around the plots: 
     
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
                 var marker = createMarker(point, document.getElementById("plot_<bean:write name='observation_pk'/>_<bean:write ignore='true' name='onerowofobservation' property='userdataset_id' />").innerHTML);
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
           // var width = bounds.maxX - bounds.minX;
           // var height = bounds.maxY - bounds.minY;
           
           //check to see if it is too small
           
           if ( (<%= minLong %> < bounds.minX) || (<%= maxLong %> > bounds.maxX) 
                  || (<%= minLat %> < bounds.minY) || (<%= maxLong %> > bounds.maxY) ) {
             //reached too small, go up one
             map.centerAndZoom(new GPoint( MyCenterLong,MyCenterLat), (MyDefaultZoomLevel - i) + 1);
             // and stop doing this
             MyDoneWithThis = 1;
           } // coords too much
         } //not done rezooming
         
       } //for loop to rezoom
       map.setMapType(G_SATELLITE_TYPE)
      } //browser is compatible
     
   } // onload function
    //]]>
    </script>
 
 
 @webpage_masthead_html@ 
    
<h1>THIS IS NOW A TEST PAGE test!!</h1>
<!-- set up the map: -->
<!-- write hidden page content: -->
<div class="hidden" id="pagedatums">
<bean:write name="pageContentBean" filter="false"/>
</div>
<div id="map" style="width: 720px; height: 500px"></div>


MIN LAT: <%= minLat %><br/>
max LAT: <%= maxLat %><br/>
MIN long: <%= minLong %><br/>
max long: <%= maxLong %><br/>




     @webpage_footer_html@
 
