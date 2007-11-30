<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:v="urn:schemas-microsoft-com:vml">
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@

     <!-- style bit is for google maps -->
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
  <script src="http://maps.google.com/maps?file=api&v=2&key=@googlemaps_apikey@" type="text/javascript"></script>
  <script src="@includes_link@VbGoogleMap.js" type="text/javascript"></script>
  <script type="text/javascript">
    function VbGLoadAllMapsThisPage() {
      // calls the right one on this page:
      vegbankGoogleMapLoad()
    }
  </script>
<TITLE>Google Map of VegBank Datasets</TITLE>


 <!-- this bean holds the contents of every plot that will be mapped and information about it.  When the google icon is clicked, it gets loaded -->
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

 function vegbankGoogleMapLoad() {

    if (GBrowserIsCompatible()) {
       //setup counters:
          var countPlots = 0;
          var countPlotsToConfirm = -1; // look up constant


       var map = VbGMapLoadByCenter("map",36.59370,-82.22100,13)

         // go ahead and set default icon.
         <% String iconPrefix = "map_google_c_" ; %>
         // store user preference for later:
         changeMappingIcon("noletters", true);
              <logic:notEmpty name="datasets-BEANLIST">

                   <logic:equal parameter="mapicons" value="nocolors">
                      <% iconPrefix = "map_google_l_" ; %>
                      // again, store user preference for later:
                      changeMappingIcon("letters", true);
                   </logic:equal>

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

     // set whether or not icons are colored.
     <logic:notEmpty name="plotobs-BEANLIST">
        var blnColored = true; //default
      <logic:equal parameter="mapicons" value="nocolors">
        blnColored = false;
      </logic:equal>

      <logic:iterate id="onerowofobservation" name="plotobs-BEANLIST">
                // figure out which number map icon to use

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

                <bean:define id="pageContentBean"><bean:write name="pageContentBean" filter="false" />
                    <div id="plot_<bean:write name='observation_pk'/>_<bean:write ignore='true' name='onerowofobservation' property='userdataset_id' />">
                     <%@ include file="includeviews/sub_plotmappingdata.jsp" %>
                     <logic:notEmpty name="onerowofobservation" property="datasetname">
                      <br/>(Dataset: <bean:write name="onerowofobservation" property="datasetname"/>)
                     </logic:notEmpty>
                    </div>
                    <bean:define id="jsazimuth"><bean:write name="onerowofobservation" property="azimuth" /><logic:empty name="onerowofobservation" property="azimuth">null</logic:empty></bean:define>
                    <div class="hidden" id="obsid_<bean:write name='observation_pk'/>_<bean:write ignore='true' name='onerowofobservation' property='userdataset_id' />_dsgpoly"><bean:write name="onerowofobservation" property="dsgpoly" /><logic:empty name="onerowofobservation" property="dsgpoly">null</logic:empty></div>
                </bean:define>
               <bean:define id="curraccuracy"><bean:write name="onerowofobservation" property="locationaccuracy" /><logic:empty name="onerowofobservation" property="locationaccuracy">-1</logic:empty></bean:define>

             countPlots=  VbGCreateMarker(<bean:write name="onerowofobservation" property="latitude" />,
                               <bean:write name="onerowofobservation" property="longitude" />,
                       document.getElementById("plot_<bean:write name='observation_pk'/>_<bean:write ignore='true' name='onerowofobservation' property='userdataset_id' />").innerHTML,
                       <%= mapNum %>, blnColored, map, countPlots, countPlotsToConfirm,
                                   <bean:write name="onerowofobservation" property="degrees_fuzzed" />,<bean:write name="curraccuracy" />,
								   <bean:write name="jsazimuth" />, null,null,null,null,
								   gebid("obsid_<bean:write name='observation_pk'/>_<bean:write ignore='true' name='onerowofobservation' property='userdataset_id' />_dsgpoly").innerHTML,
								   false, false
                       ); //degreeserr,meterserr, azimuth, plotx, ploty, gpsx, gpsy, dsgpoly , blnautodrawacc, blnautodrawbounds


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

       //correct the location based on what was mapped.
       VbGFixZoomToBounds(map,<%= minLat %>,<%= maxLat %>,<%= minLong %>,<%= maxLong %>,true)

       // add a "where is this stuff" map ; an overview of where the big map is.  Note the big maps bounds, not plot bounds
    VbGMapLoadWhereIsMap("whereismap",map)

      } // end of browser is compatible if statement

   } // end of main function
    //]]>
    </script>
 </logic:notEqual> <!-- wparambad -->

 @webpage_masthead_html@


<logic:equal name="wparambad" value="true">
  <!-- failed, report errors here -->
  <p>
   Sorry, but VegBank didn't access any datasets to map.
   Please <a href="@forms_link@MapDatasets.jsp">try again</a>.

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
    <h2><img src="@images_link@maplogo.gif" /> Map of your plots</h2>
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
  <p><a href="@forms_link@MapDatasets.jsp">Map other datasets</a> <span class="sizetiny">(Requires Login)</span></p>
<!--

FYI Debugging:
MIN LAT: <%= minLat %><br/>
max LAT: <%= maxLat %><br/>
MIN long: <%= minLong %><br/>
max long: <%= maxLong %><br/>
-->

</logic:notEqual>

     @webpage_footer_html@

