// this file deals with VegBank's ability to use Google Maps API:
// for more on this API, see http://www.google.com/apis/maps/documentation/reference.html
// plot locations are generally mapped, initially through datasets, though the intent of this page
// is to extend that functionality to any search results, too.


//this adds funtions to load and onload.
// all functions should be called AFTER </html> (or at least after </body> otherwise an ugly error gets thrown in IE (after it maps ok ironically)
//  addEvent(window, "load", VbGLoadAllMapsThisPage);
//this is supposed to keep memory from leaking in IE: (why all the IE crap?  Get Firefox!)

  addEvent(window, "unload", VbGUnload);
  addEvent(window, "load", VbGLoadAllMapsThisPageWrapper);

var CON_MAX_ZOOM_LEVEL_ALLOWED = 14; //prevents zooming in too much!
var CON_MAX_GOOGLE_ICONS = 9; //highest numbered colored icon for google icons in mapping.
var CON_VBGMAP_ACCURACY_COLOR = "#FF0000";
var CON_VBGMAP_FUZZING_COLOR = "#0000FF";
var CON_LAT_TO_METERS_DIVISOR = 111131.9718; // the number of meters per degree latitude


 function VbGLoadAllMapsThisPageWrapper() {
       VbGLoadAllMapsThisPage();
  }
 function VbGUnload() {
   // unloads the google map
  // // alert( 'unloading Google maps now...');
   GUnload();
  // alert( 'GoogleMaps now unloaded.  Thank you for mapping with us!');
 }

  function VbGMapLoadByCenter(elementId,lat,lng,zoomLevel) {

     if (GBrowserIsCompatible()) {
        // // alert( 'starting new map!');

         var map = new GMap2(document.getElementById(elementId));
         map.setCenter(new GLatLng(lat,lng), zoomLevel);
      //   // alert( 'ok so far');
          map.setMapType(G_HYBRID_MAP);
          //save this position:
         map.savePosition();
         map.addControl(new GLargeMapControl());
         map.addControl(new GMapTypeControl());
         map.addControl(new GScaleControl());
         return map;
      }

   }

   function VbGMapLoadByBounds(elementId,minLat,maxLat,minLng,maxLng) {
     if (GBrowserIsCompatible()) {
       //this function loads up a map, but based on max and min coordinates rather than center
       var ctrLat = ((minLat + maxLat) * 0.5);
       var ctlLng = ((minLng + maxLng) * 0.5);
       //map via center
       var map = VbGMapLoadByCenter(elementId,ctrLat,ctlLng,3);
       // fix zooming to bounds:
       VbGFixZoomToBounds(map,minLat,maxLat,minLng,maxLng,true);
       return map;
     } //compatible
   }

   function VbGMapRecenterZoomByBounds(map,minLat,maxLat,minLng,maxLng) {
     if (GBrowserIsCompatible()) {
       //this function loads up a map, but based on max and min coordinates rather than center
       var ctrLat = ((minLat + maxLat) * 0.5);
       var ctrLng = ((minLng + maxLng) * 0.5);
       //map via center
      // var map = VbGMapLoadByCenter(elementId,ctrLat,ctlLng,3);
       map.setCenter(new GLatLng(ctrLat,ctrLng));
       // fix zooming to bounds:
       VbGFixZoomToBounds(map,minLat,maxLat,minLng,maxLng,true);
       return map;
     } //compatible
   }

   function VbGMapLoadWhereIsMap(elementId,refMap) {
       // this function loads a "where is" map, or key to where something is.
        var whereismap = new GMap2(document.getElementById(elementId));
        // load boundaries of reference Map
        var boundsnew = refMap.getBounds();
        //record all 4 as separate vars
        var minLat = boundsnew.getSouthWest().lat();
        var maxLat = boundsnew.getNorthEast().lat();
        var minLng = boundsnew.getSouthWest().lng();
        var maxLng = boundsnew.getNorthEast().lng();

        //get distance the map covers:
        var rangeLat = maxLat - minLat;
        var rangeLng = maxLng - minLng;
        if (rangeLat < 0.2 || rangeLng < 0.2 ) {
            //scoot the min out a bit so that the markers show:
            //alert(rangeLat + "," + rangeLng + " trigger 0.1! adjustment");
            minLat = minLat - 0.1;
            maxLat = maxLat + 0.1;
            minLng = minLng - 0.1;
            maxLng = maxLng + 0.1;
        }

        //alert('rangeLat: ' + rangeLat + ', rangeLong: ' + rangeLng);
        // set center as same
        whereismap.setCenter(new GLatLng((minLat + maxLat) * 0.5,(minLng + maxLng) * 0.5), 1);
        //array of points
        var points = [];
        //add the points, in sequence
        points.push(new GLatLng(minLat, minLng ));
        points.push(new GLatLng(minLat, maxLng));
        points.push(new GLatLng(maxLat, maxLng));
        points.push(new GLatLng(maxLat, minLng ));
        points.push(new GLatLng(minLat, minLng));
             //  GLog.write("about to add overview polyline:");
        //overlay the array of points
        whereismap.addOverlay(new GPolyline(points));
           //  GLog.write("DONE adding  overview polyline.");
        //disable dragging to stop people from accidentally moving it
        whereismap.disableDragging();
   }


        function VbGFixZoomToBounds(map,minLat,maxLat,minLng,maxLng,blnSaveZoom) {
            // new way of zooming to right level, using getBoundsZoomLevel:

              var MyCenterLat =   (maxLat + minLat)*0.5 ;
            //   GLog.write('center Lat:' + MyCenterLat);
              var MyCenterLong = (minLng + maxLng)*0.5 ;
            // calc a GLatLngBounds for the area:
            var boundsObj = new GLatLngBounds(new GLatLng(minLat,minLng),new GLatLng(maxLat,maxLng))
            //center it and zoom according to GLatLngBounds:
            map.setCenter(new GLatLng( MyCenterLat,MyCenterLong));
            map.setZoom(map.getBoundsZoomLevel(boundsObj));
            //alert('map zoom at: ' + map.getZoom());
            //make sure we aren't too far zoomed in
            if (map.getZoom() > CON_MAX_ZOOM_LEVEL_ALLOWED) {
                map.setZoom(CON_MAX_ZOOM_LEVEL_ALLOWED);
            }
            //see how close we are in pixels to the edges:

             var topMarginDeg = map.getBounds().getNorthEast().lat()-maxLat;
             var bottomMarginDeg = minLat - map.getBounds().getSouthWest().lat();
             var leftMarginDeg = minLng - map.getBounds().getSouthWest().lng();
             var rightMarginDeg = map.getBounds().getNorthEast().lng()-maxLng;
            //get map size, in pixels and in Lat/Long
              var mapSizePxX = map.getSize().width;
              var mapSizePxY = map.getSize().height;
            //  GLog.write('mapSize X:' + mapSizePxX);
            //  GLog.write('mapSize Y:' + mapSizePxY);
            //  GLog.write('new bounds Lng:' + map.getBounds().getNorthEast().lng() + ',' + map.getBounds().getSouthWest().lng());
            //  GLog.write('new bounds Lat:' + map.getBounds().getNorthEast().lat() + ',' + map.getBounds().getSouthWest().lat());

              var mapSizeDegLng = (map.getBounds().getNorthEast().lng() - map.getBounds().getSouthWest().lng());
              var mapSizeDegLat = (map.getBounds().getNorthEast().lat() - map.getBounds().getSouthWest().lat());
            //  GLog.write('mapSize Lat:' + mapSizeDegLat);
            //  GLog.write('mapSize Long:' + mapSizeDegLng);

            //estimate of how much this is in pixels:
             var topMarginPx = ((topMarginDeg / mapSizeDegLat) * mapSizePxY);
             var bottomMarginPx = ((bottomMarginDeg / mapSizeDegLat) * mapSizePxY);
             var leftMarginPx = ((leftMarginDeg / mapSizeDegLng) * mapSizePxX);
             var rightMarginPx = ((rightMarginDeg / mapSizeDegLng) * mapSizePxX);
             //report:
            // GLog.write('top margin, Lat:' + topMarginDeg + ' , px:' + topMarginPx);
            // GLog.write('bottom margin, Lat:' + bottomMarginDeg + ' , px:' + bottomMarginPx);
            // GLog.write('left margin, Lat:' + leftMarginDeg + ' , px:' + leftMarginPx);
            // GLog.write('right margin, Lat:' + rightMarginDeg + ' , px:' + rightMarginPx);
             if ( (topMarginPx < 30) || (bottomMarginPx < 10 ) || (leftMarginPx < 40) || (rightMarginPx < 15) ) {
            //     GLog.write('zooming out, b/c it was too close!');
                 map.zoomOut();
             }
            //maybe save this for returning to it later?
            if (blnSaveZoom==true) {
                map.savePosition();
            }
   }

  var VbG_global_mapMainMap = null;
  // Creates a marker whose info window displays the given html (not number)
   function VbGCreateMarker(lat,lng, html, number,  blnColored, map, markerNumber, markerConfirmNumber,
            degreesErr,metersErr, azimuth, plotX, plotY, GPSX, GPSY, dsgPoly , blnAutoDrawAcc, blnAutoDrawBounds) {
         //this creates marker with listener that responds to clicks:
         // lat and lng are floating point latitude and longitude in decimal degrees
         // html is html to display for the marker (ie plot identification)
         // number is the group number to display: 0-9
         // blnColored is true if using small colored icons, else larger lettered ones
         // markerNumber is a counter for the number of plots mapped
         // markerConfirmNumber is a threshhold past which the user gets a confirmation message to continue mapping.
         // if markerConfirmNumber is -1, then uses the default, supplied in this function, else uses what is set in page
        // GLog.write('init VbGCreateMarker(' + lat+ ',' + lng+ ',' +  'html' + ',' +  number+ ',' +   blnColored+ ',' +  '[map]'+ ',' +  markerNumber+ ',' +  markerConfirmNumber+ ',' +             degreesErr+ ',' + metersErr+ ',' +  azimuth+ ',' +  plotX+ ',' +  plotY+ ',' +  GPSX+ ',' +  GPSY+ ',' +  dsgPoly + ',' +  blnAutoDrawAcc+ ',' +  blnAutoDrawBounds + ')');
        //save map globally if not set:
        if (VbG_global_mapMainMap) {
			//fine
		} else {
			// GLog.write("set main global map.  Should happen only once.");
			VbG_global_mapMainMap = map;
		}


         if ( markerConfirmNumber == -1 ) {
            markerConfirmNumber = 100 ; //default value
         }

         // // alert( 'writing new marker at ' + lat + ',  ' + lng);
         //GLog.write('trying marking #' + markerNumber);
         //deal with whether or not to continue this?
         var keepMapping = true ; //default
         if (markerNumber < 0) {
             keepMapping = false ; //cancelled earlier
         }

         if (markerConfirmNumber > 0 && markerNumber > 0) {
             // test to see if we should stop mapping
             if (Math.floor(markerNumber/markerConfirmNumber) == (markerNumber/markerConfirmNumber)) {
                 // tell user that X plots have been mapped, continue mapping?
                 if (!confirm(markerNumber + " plots have been mapped, continue mapping (browser may get slow if too many plots are mapped)?")) {
//                   GLog.write('marking #' + markerNumber + 'cancelled');
                     markerNumber =  -2; //set to not map anything else, though js will still be iterated through
                     keepMapping = false ; //map no more!
                    // keepMapping = false; // for now, continue mapping this one.
                 }
             }
          }
        if (keepMapping == true) {
//             GLog.write('really marking #' + markerNumber);
            //create the position
              var point = new GLatLng(lat,lng);

             //create icon
             var baseIcon = new GIcon();
             if (blnColored==true) {
               // default icons are colored
                 baseIcon.shadow = "@images_link@map_google_shadow_20.png";
                 baseIcon.iconSize = new GSize(12, 20);
                 baseIcon.shadowSize = new GSize(22, 20);
                 baseIcon.iconAnchor = new GPoint(6, 20);
                 baseIcon.infoWindowAnchor = new GPoint(5, 1);
                 VbGiconPrefix = "map_google_c_";
              } else {
                 baseIcon.shadow = "@images_link@map_google_shadow_50.png";
                 baseIcon.iconSize = new GSize(20, 34);
                 baseIcon.shadowSize = new GSize(37, 34);
                 baseIcon.iconAnchor = new GPoint(9, 34);
                 baseIcon.infoWindowAnchor = new GPoint(9, 2);
                 baseIcon.infoShadowAnchor = new GPoint(18, 25);
                 VbGiconPrefix = "map_google_l_";
              }
             //make sure the group number doesn't exceed 9
             while (number > CON_MAX_GOOGLE_ICONS) {
                 number = number - CON_MAX_GOOGLE_ICONS - 1;
             }
             var icon = new GIcon(baseIcon,"@images_link@" + VbGiconPrefix +  number + ".png");
             // icon.image = "@images_link@" + VbGiconPrefix +  number + ".png";
             var marker = new GMarker(point, icon);

             // show plot info when clicked

             //see if there is circular accuracy or shape to draw:
             var strMarkAccText = "" ; //init : info for user
             var strMarkAccKey  = "" ; //init : key to marking on map
             var strMarkAccFcn = "" ; //init //function to do it
             strUserFuzzAccuracyNote = "" ; //init
             if (metersErr == -1) {metersErr=null}; //null flag

             if (degreesErr || metersErr) {
				 strUserFuzzAccuracyNote = "<br/>Location masked: "; //style='color:" + CON_VBGMAP_FUZZING_COLOR + "'
				 if (degreesErr) {
					 strUserFuzzAccuracyNote = strUserFuzzAccuracyNote + degreesErr ;
				 } else {
					 strUserFuzzAccuracyNote = strUserFuzzAccuracyNote + "unknown";
				 }
				 strUserFuzzAccuracyNote = strUserFuzzAccuracyNote + " degrees. Accuracy: "; //style='color:" + CON_VBGMAP_ACCURACY_COLOR + "'
				 if (metersErr) {
					 strUserFuzzAccuracyNote = strUserFuzzAccuracyNote + metersErr ;
				 } else {
					 strUserFuzzAccuracyNote = strUserFuzzAccuracyNote + "unknown";
				 }
				 strUserFuzzAccuracyNote = strUserFuzzAccuracyNote + " meters.";


				 // GLog.write(degreesErr);
				 // GLog.write((degreesErr == null));
				 // GLog.write(isNaN(degreesErr));
				 var nzDegreesErr = degreesErr;
				 if (nzDegreesErr==null || isNaN(nzDegreesErr) || nzDegreesErr=="") { nzDegreesErr="null" };
				 var nzMetersErr =  metersErr;
				 if (nzMetersErr==null || isNaN(nzMetersErr) || nzMetersErr=="") { nzMetersErr="null" };
				 strMarkAccFcn = 'VbGMarkCircularAccuracy(' + lat + ',' + lng + ',' + nzDegreesErr + ',' + nzMetersErr + ',VbG_global_mapMainMap)';
				 strMarkAccText = "<span class='lk VbG_markAccuracy' onclick='" + strMarkAccFcn + "'>Mark uncertainty</span>";
				 strMarkAccKey = "(<span style='color:" + CON_VBGMAP_FUZZING_COLOR + "'>masking</span> + <span style='color:" + CON_VBGMAP_ACCURACY_COLOR + "'>accuracy</span>)";
			 } else {
				  //no deg/m err info available, forget about it:
				  strMarkAccFcn = "";
				  strMarkAccText = "";
				  strMarkAccKey = "";
				 }// degreesErr or metersErr passed

            //see if there is sufficient info to map plot boundaries:
            var thisDrawBoundsJS = "";
            var thisDrawBoundsCmd = "" ; //init
            var thisDrawBoundsInfo = "Plot boundaries: " ; //init
				  var GPSXY = "" ;
				  if (GPSX && GPSY) {
					GPSXandY =  GPSX + ',' + GPSY; // both present
				  } else {
					GPSXandY = "null,null"; //one of both missing, don't pass last parameters
				  }
             if (plotX>0 && plotY>0 && (azimuth)) {
				  //has X, Y, and azimuth, allow it to be mapped:
				  // GLog.write("mapping via X,Y,azi");
				  thisDrawBoundsJS = 'VbGDrawPlotBounds(VbG_global_mapMainMap,' + lat + ',' + lng + ',' + azimuth + ',' + plotX + ',' + plotY + ',' + GPSXandY + ');';
				  thisDrawBoundsInfo = thisDrawBoundsInfo + plotX + "m (X) by " + plotY + "m (Y).  X @ " + azimuth + " deg.";
			  } else { //lacks X,Y or azimuth
				  if (dsgPoly && azimuth) { //has dsgPoly and azi
				      // GLog.write("instructing to map via dsgPoly:" + dsgPoly);
					  thisDrawBoundsJS = 'VbGDrawPlotBounds(VbG_global_mapMainMap,' + lat + ',' + lng + ',' + azimuth + ',null,null,' + GPSXandY + ',"' + dsgPoly + '");';
					  // GLog.write("this draw bounds JS:" + thisDrawBoundsJS);
					  thisDrawBoundsInfo = thisDrawBoundsInfo + dsgPoly ;
				  } else { //lacks something vital for drawing boundaries
                     //nothing to do!
                     // GLog.write("nothing to do");
                     thisDrawBoundsInfo = "";
				  }
			  } //has X,Y and azimuth or not
			  //same command to draw, different JS, perhaps, if dsgPoly or not
              if (thisDrawBoundsJS != "") {
				  thisDrawBoundsCmd = "<span class='lk drawPlotBounds' onclick='" + thisDrawBoundsJS + "'>Draw plot boundaries</span>";
			  }
             var strTotalHTML = html ; //init
             //accuracy:
             strTotalHTML = strTotalHTML + strUserFuzzAccuracyNote;
             if (strMarkAccFcn != "") { //have accuracy info
				 if ((blnAutoDrawAcc == true) && (strMarkAccFcn != "")) {
					 //do it now:
						 eval(strMarkAccFcn);
						 strTotalHTML = strTotalHTML + " Marked as: " + strMarkAccKey;
				 } else { //add chance to mark accuracy:
					 strTotalHTML = strTotalHTML  + "<br/>" + strMarkAccText + " - can be marked as: " + strMarkAccKey;
				 }
		     } //something to mark accuracy with

             if (thisDrawBoundsJS != "") {
				 if ((blnAutoDrawBounds == true)) {
					 //draw it now
					 eval(thisDrawBoundsJS);
					 //still show info:
					 strTotalHTML = strTotalHTML + "<br/>" + thisDrawBoundsInfo;
				 } else {
					 //allow them to draw it later:
					 // GLog.write("including command to draw bounds.");
					 strTotalHTML = strTotalHTML + "<br/>" + thisDrawBoundsInfo + " " + thisDrawBoundsCmd ;
				 } //auto draw or not
			 } //have boundary info

             GEvent.addListener(marker, "click", function(overlay, point) {
               marker.openInfoWindowHtml("<div class='vbgmaplabel'>" + strTotalHTML + "</div>");
             });
           //  GLog.write('adding listener for double click before overlaying on map');
           //  GEvent.addListener(marker, "dblclick", function() {
			// 		  alert('I am double clicked!');
			// 		  VbGMarkLine(lat-0.5,lng-0.5,lat+0.5,lng+0.5,map,000000,1,1)
			//				 });

//            GLog.write('before adding marker:');
              map.addOverlay(marker);
               markerNumber ++;
     } //keepMapping true
//       GLog.write('returning: ' + markerNumber);
         return markerNumber;
   }


 //Mark a line on the map:
   function VbGMarkLine(swLat,swLng,neLat,neLng,map,color,weight,opacity) {
       //map is a GMap2 that we want to mark the line on (doesnt have to be sw and ne)
       //color is a string(optional) identifying what color to use
       //weight is number of pixels
       //opacity is between 0 (invisible) and 1 (opaque)
      var points = [];
      points.push(new GLatLng(swLat, swLng));
      points.push(new GLatLng(neLat, neLng));
     // alert('array pushed');
      map.addOverlay(new GPolyline(points,color,weight,opacity));
      }

   //Mark a rectangle on the map:
   function VbGMarkRectangle(swLat,swLng,neLat,neLng,map,color,weight,opacity) {
       //map is a GMap2 that we want to mark the rectangle on
       //color is a string(optional) identifying what color to use
       //weight is number of pixels
       //opacity is between 0 (invisible) and 1 (opaque)
      var points = [];
      points.push(new GLatLng(swLat, swLng));
      points.push(new GLatLng(swLat, neLng));
      points.push(new GLatLng(neLat, neLng));
      points.push(new GLatLng(neLat, swLng));
      points.push(new GLatLng(swLat, swLng));

      map.addOverlay(new GPolyline(points,color,weight,opacity));
      }


function VbGProvideKeyIcon(blnColored,number,elementToWrite) {
    // this function fills in the elementToWrite with innerHTML of an image that represents the number indicated
       //get icon prefix
       var VbGiconPrefix = "init";
       if (blnColored==true) {
           VbGiconPrefix = "map_google_c_";
       } else {
           VbGiconPrefix = "map_google_l_";
       }
       //check number that it's between 0 and 9
       while (number > CON_MAX_GOOGLE_ICONS) {
         number = number - CON_MAX_GOOGLE_ICONS - 1;
       }
       //write to the element:
       //"@images_link@" + VbGiconPrefix +  number + ".png"
       document.getElementById(elementToWrite).innerHTML = "<img src='@images_link@"  + VbGiconPrefix +  number + ".png' alt='icon #" + number + "' />";
      // alert("done..." + number);
}

function VbGDifferentiateMarker(blnColored,lat,lng,degreesErr,metersErr) {
    //differentiate marker via accuracy:
    //this does the reverse of VbG MarkCircularAccuracy, which uses degrees, this uses meters.

          //if metersErr is -1 then the actual error isn't known
          var blnUnknownErr  = false ; //default
          if (metersErr == -1) {
              blnUnknownErr = true;
              //reset metersErr to not interfere with calcs: (this is picky, admittedly)
              metersErr = 0;
          }
           //first, it calculates the meters error by guessing where the points should go,
           // using 111.319718km = 1 deg Lat and 75km = 1 deg Long (varies depending on where you are)
           //calc the meters err as latitude, which is always the same no matter where on earth you are:
           var latToMErr = (degreesErr * 111131.9718);
           // long error will be different, because longitude shrinks as you get near the poles
           //calc the meters err as longitude, adjusting by latitude:
           var longAdjust = Math.cos((lat * Math.PI) / 180);
          //  alert( "Long Adjustment is: " + longAdjust);
           var longToMErr = (latToMErr * longAdjust);
           //combinePrev with degreesErr
           var totalLatErr = latToMErr + metersErr;
           var totalLngErr = longToMErr + metersErr;

         var avgErr = (totalLatErr + totalLngErr) * 0.5;
      //   alert ("avgErr: " + avgErr);
         // split into 5 classes:
         // 1: 0-50m (excellent)
         // 2: 50-200m (good)
         // 3: 200-1000m (fair)
         // 4: 1000m (poor)
         // 5: unknown
         var errClass = 5; //default
         if (avgErr <= 50) {
             errClass = 1;
         } else if (avgErr <= 200) {
             errClass = 2;
         } else if (avgErr <= 1000) {
             errClass = 3;
         } else {
             errClass = 4;
         }
         //now, if errClass is good, but metersErr wasn't known, then we have to say unknown accuracy:
         if (blnUnknownErr == true && errClass < 4) {
             errClass = 5;
         }
        // alert("errClass: " + errClass);
         //now icon numbers are assigned:
         var iconNum = 0;
         if (blnColored == true) {
           //colored:
           // 2,6,3,0,9 for 1-5
           switch(errClass) {
             case 1:
                 iconNum = 2;
                 break
            case 2:
                 iconNum = 6;
                  break
            case 3:
                  iconNum = 3;
                  break
            case 4:
                 iconNum = 0;
                  break
            case 5:
                 iconNum = 4;
                  break
           } // switch ends
         } else {
           //not colored, use letters
           // 1-4 is 0-3, 5 is 9
           iconNum = errClass - 1;
           // unknown jumps to higher letter
           if (iconNum == 4) {
               iconNum = 9;
           } //is unknown
         } //not colored
    return iconNum;
  }




//try using global map:
   function VbGMarkCircularAccuracy(lat,lng,degreesErr,metersErr,circMap) {
       // this marks a map with a cirular-ish (really polygon) circle marking the inaccuracy in a plot's location
       // lat and lng are latitude and longitude in decimal degrees
       // degreesErr is error margin in degrees
       // metersErr is meters error, additive to degreesErr
       if (!circMap) {
		    circMap = VbG_global_mapMainMap;
		    // GLog.write("VbGMarkCircularAccuracy> using global var as no map was passed.");
		}

      // GLog.write("init circular accuracy using global map var instead: north lat:" + circMap.getBounds().getNorthEast().lat());
      // GLog.write("test draw on map");
      // VbGMarkLine(circMap.getBounds().getNorthEast().lat()-1,circMap.getBounds().getNorthEast().lng()-1,circMap.getBounds().getNorthEast().lat(),circMap.getBounds().getNorthEast().lng(),
      //   circMap,000000,1,1)

       //first, it calculates the meters error by guessing where the points should go,
       // using 111.319718km = 1 deg Lat and 75km = 1 deg Long (varies depending on where you are)
       //calc the meters err as longitude:
       // alert( 'init VbGMarkCircularAccuracy');
       var mErrToLat = (metersErr / CON_LAT_TO_METERS_DIVISOR);
      //  GLog.write("Long Err:" + mErrToLong);
       //calc the meters err as latitude, adjusting for by latitude:
      // GLog.write( 'starting math cosine');
       var LongAdjust = Math.cos((lat * Math.PI) / 180);
      // GLog.write( "Long Adjustment is: " + LongAdjust);
       var mErrToLong = (mErrToLat / LongAdjust);
      // GLog.write( metersErr + " -> Lat:" + mErrToLat );
      // GLog.write( "--Long:" + mErrToLong);
       //now see if Google agrees:
       var OriginPoint = new GLatLng(lat,lng);
       // VbGCreateMarker(lat,lng, "Origin", 1,  false, circMap);
       var LatErrPoint = new GLatLng(lat + mErrToLat,lng);
       // VbGCreateMarker(lat + mErrToLat,lng, "Origin", 2,  true, circMap);
       var LongErrPoint = new GLatLng(lat,lng + mErrToLong);
       // VbGCreateMarker(lat,lng + mErrToLong, "Origin", 3,  true, circMap);
      // GLog.write( "orig error was:" + metersErr);
    // GLog.write("Google thinks error (meters) Lat:" + OriginPoint.distanceFrom(LatErrPoint) );
    // GLog.write("-g--Long:" + OriginPoint.distanceFrom(LongErrPoint));

       //combinePrev with degreesErr
       var TotalLatErr = degreesErr + mErrToLat;
       var TotalLngErr = degreesErr + mErrToLong;

       ///// -------------------
       ////  Now we have established the coordinates N,S,E,W of the circular-ish polygon, add some more:

       //establish various coordinates around the circle, using sin and cos:
         var LatPoints = [];
         var LongPoints = [];
         var quarterDivisions = 36;
         var points = [];
         // alert( 'starting arrays');
       for ( var i = 0; i < quarterDivisions; i++ ) {
         LatPoints.push(TotalLatErr * Math.sin(i * (Math.PI * 2) / (quarterDivisions)));
         LongPoints.push(TotalLngErr * Math.cos(i * (Math.PI * 2) / (quarterDivisions)));
    //     GLog.write("added point:");
    //     GLog.write(LatPoints[i]);
    //     GLog.write(LongPoints[i]);
         // VbGCreateMarker(lat+LatPoints[i],lng+LongPoints[i],"this is " + i,i,true,circMap);
         points.push(new GLatLng(lat + LatPoints[i],lng + LongPoints[i]));
       }
       // alert( 'done with loop');
       // push the original location again:
       points.push(new GLatLng(lat + LatPoints[0],lng + LongPoints[0]));
       // alert( 'pushed some points');
       circMap.addOverlay(new GPolyline(points,"#CCCCCC"));
       // alert( 'added overlay #1');
       // also draw from origin going out 45 degrees to show the 2 different error sources: fuzzing and uncertainty
       var newPoints = [];
       newPoints.push(new GLatLng(lat,lng));
       // at 45 degrees:
       var midLat = lat - (mErrToLat) * Math.sin(Math.PI / 4);
       var midLng = lng + (mErrToLong) * Math.cos(Math.PI / 4);

       newPoints.push(new GLatLng(midLat,midLng));
       var evenNewerPoints = [];
       evenNewerPoints.push(new GLatLng(midLat,midLng));
       evenNewerPoints.push(new GLatLng(lat - (TotalLatErr) * Math.sin(Math.PI / 4),lng + (TotalLngErr) * Math.cos(Math.PI / 4)));
       //draw overlays of different color indicating src of error:
       circMap.addOverlay(new GPolyline(newPoints,CON_VBGMAP_ACCURACY_COLOR)); // accuracy
       circMap.addOverlay(new GPolyline(evenNewerPoints,CON_VBGMAP_FUZZING_COLOR)); //fuzzing
       // alert( 'done with circular adding function');

   }

function VbGMakeMapQueryClickable(map) {
     // this function makes a map "query clickable"
   GEvent.addListener(map, "click", function(overlay, point) {
    // var center = map.getCenter();
    // see if there are already points in prevous:
    var prevLat = parseFloat(document.getElementById("previousLat").innerHTML);
    var prevLong = parseFloat(document.getElementById("previousLong").innerHTML);
      if (Number(document.getElementById("pointsClicked").innerHTML)<=1) {
          //clear the first point, or anything already there
    //    alert("clearing overlays");
          map.clearOverlays();
      }

      //map these with the current Lat/Long
    //alert(prevLat + " , " + prevLong + " --> " + point.lat() + " , " + point.lng());
    if (document.getElementById("pointsClicked").innerHTML == "0") {
       // first time, draw point:
       VbGCreateMarker(point.lat(),point.lng(), "This the first point <br/>on the map that you drew. <br/>It is a placeholder point until <br/>you click another point.", 0, true, map, 1, 500);
       // make sure that the values are not preloaded (ie with back button):
       VbGClearMapForm();
    } else {
        //ok to mark map
        VbGMarkLine(prevLat,prevLong,point.lat(),point.lng(),map);
    }
    // increase the count #
    document.getElementById("pointsClicked").innerHTML = Number(document.getElementById("pointsClicked").innerHTML) + 1;
    // reflect last point in message text
    document.getElementById("message").innerHTML =  "added point #" + document.getElementById("pointsClicked").innerHTML
              + " (" + point.lat() + "," + point.lng() + ")" ;
    //set prevLat/prevLong for next point
     document.getElementById("previousLat").innerHTML = point.lat().toString();
     document.getElementById("previousLong").innerHTML = point.lng().toString();
     document.getElementById("allPoints").innerHTML = document.getElementById("allPoints").innerHTML + point.lat() + ',' + point.lng() + ';';

     //check for min and max
     if (point.lat() < parseFloat(document.getElementById("minLat").value)) {
        // alert('set new min');
        document.getElementById("minLat").value = point.lat().toString();
     }
     if (point.lat() > parseFloat(document.getElementById("maxLat").value)) {
            document.getElementById("maxLat").value = point.lat().toString();
     }
         // long --
     if (point.lng() < parseFloat(document.getElementById("minLng").value)) {
        document.getElementById("minLng").value = point.lng().toString();
     }
     if (point.lng() > parseFloat(document.getElementById("maxLng").value)) {
        document.getElementById("maxLng").value = point.lng().toString();
     }

    });
}

  function VbGClearMapForm() {
    // map.clearOverlays();
     document.getElementById("previousLat").innerHTML = "";
     document.getElementById("previousLong").innerHTML = "";
     document.getElementById("allPoints").innerHTML = "";
     document.getElementById("minLat").value = 5000;
     document.getElementById("maxLat").value = -5000;
     document.getElementById("minLng").value = 5000;
     document.getElementById("maxLng").value = -5000;
     document.getElementById("message").innerHTML = "";
     document.getElementById("pointsClicked").innerHTML = "0";
  }

  function VbGClearThisMap() {
    var blnOK = false;
    if (confirm("Really clear this map?") == true) {
      blnOK = true;
    }
    return blnOK;

  }

  function VbGMapXML(xmlstring,mapDivId,minLat,maxLat,minLng,maxLng, blnSetBoundsBasedOnPlots) {
      //try to get map set:
      //alert(url);
      //GDownloadUrl(url, function(data, responseCode) {
      //  alert('got some data:' + data);
      //  var xml = GXml.parse(data);
        var xmlobject = (new DOMParser()).parseFromString(xmlstring, "text/xml"); //only works with Firefox, or at least it fails with IE
        var markers = xmlobject.getElementsByTagName("plot");
        var markerNumber = 0;
        var markerConfirmNumber = 80;
        //alert('got ' + markers.length + ' plots');
        var plotMinLat ; //new variable to hold real min & maxes
        var plotMaxLat ;
        var plotMinLng ;
        var plotMaxLng ;
        var map;
        if (blnSetBoundsBasedOnPlots == true) {
        //dont know where this is (yet)
           map = VbGMapLoadByBounds(mapDivId,parseFloat(markers[0].getAttribute("latitude"))-1,parseFloat(markers[0].getAttribute("latitude"))+1,parseFloat(markers[0].getAttribute("longitude"))-1,parseFloat(markers[0].getAttribute("longitude"))+1) ;
        } else {
           map = VbGMapLoadByBounds(mapDivId,parseFloat(minLat),parseFloat(maxLat),parseFloat(minLng),parseFloat(maxLng)) ;
        }
        for (var i = 0; i < markers.length; i++) {
          // var point = new GLatLng(parseFloat(markers[i].getAttribute("latitude")),
          //                        parseFloat(markers[i].getAttribute("longitude")));
          // map.addOverlay(new GMarker(point));
         if (markerNumber >= 0) {
             var thisPlotLat =  parseFloat(markers[i].getAttribute("latitude")); //new variables to hold plot lat/long
             var thisPlotLng = parseFloat(markers[i].getAttribute("longitude"));
             if (i == 0) {
                //init:
                plotMinLat = thisPlotLat;
                plotMaxLat = thisPlotLat;
                plotMinLng = thisPlotLng;
                plotMaxLng = thisPlotLng;
             } else {
                //not the first time here
                if (thisPlotLat < plotMinLat) {plotMinLat = thisPlotLat;}
                if (thisPlotLat > plotMaxLat) {plotMaxLat = thisPlotLat;}
                if (thisPlotLng < plotMinLng) {plotMinLng = thisPlotLng;}
                if (thisPlotLng > plotMaxLng) {plotMaxLng = thisPlotLng;}
             }
                markerNumber = VbGCreateMarker(thisPlotLat,
                                      thisPlotLng,
                                      markers[i].getAttribute("name"),
                                      1, true, map, markerNumber, markerConfirmNumber);
              // markers[i].getElementsByTagName("htmltoshow").toString()
              //markerNumber ++;
            }
          }
          if (blnSetBoundsBasedOnPlots == true) {
              //reset map boundaries
              // GLog.write("resetting plot bounds to be around all plots.");
              VbGFixZoomToBounds(map,plotMinLat,plotMaxLat,plotMinLng,plotMaxLng,true)
          }

       //}); //old GDownloadUrl, which failed b/c of permissions... weird.
       return map;
   }

  function VbGMapCSV(csvstring,mapDivId,minLat,maxLat,minLng,maxLng, blnSetBoundsBasedOnPlots, blnTryToMarkProjects, zoomMapId, blnAutoDrawAcc, blnAutoDrawBounds) {
      //try to get map set:
      //alert(url);
      //GDownloadUrl(url, function(data, responseCode) {
      //  alert('got some data:' + data);
      //  var xml = GXml.parse(data);
        //var xmlobject = (new DOMParser()).parseFromString(xmlstring, "text/xml");
        //first, replace the newlines with ; in the csv string:
        // alert('start csv:' + csvstring);
        var csvOneLine = csvstring.replace(/\n/g,";");
        // alert('one line csv: ' + csvOneLine);
        var markers = csvOneLine.split(";");
        // alert('found ' + markers.length + ' plots');
        var markerNumber = 0;
        var markerConfirmNumber = -1; //default
        var colorNumber = 0; //0-9
        //alert('got ' + markers.length + ' plots');
        var plotMinLat ; //new variable to hold real min & maxes
        var plotMaxLat ;
        var plotMinLng ;
        var plotMaxLng ;
        var map;

        var thisPlotPrefix = "";
        var lastPlotPrefix = "";

        for (var i = 0; i < markers.length; i++) {
           //  alert('plot #' + i + ': ' + markers[i]);
             var thisPlotCSV = markers[i].split(",");
             //alert("have another row... with columns: " + thisPlotCSV.length);
           if (thisPlotCSV.length > 2) {
			 var thisPlotName = thisPlotCSV[0];
			 //alert("have another and has enough columns to map");
             // check prefix and toggle marker?
             if (blnTryToMarkProjects == true) {
                 //make all delimiters into a dash
                 var tempPlotName = thisPlotName.replace(/ /,"-");
                 tempPlotName = tempPlotName.replace(/_/,"-");
                 if (tempPlotName.indexOf("-") >= 0) {
                     thisPlotPrefix = tempPlotName.split("-")[0]; //get whatever is before the dash
                     if (thisPlotPrefix != lastPlotPrefix) { //if this prefix not same as last, increase colorNumber
                         colorNumber ++;
                         // but not past the max number of icons we have
                         if (colorNumber > CON_MAX_GOOGLE_ICONS) {
                             colorNumber = colorNumber -  CON_MAX_GOOGLE_ICONS - 1;
                         } //too high for color number
                     } //new prefix
                     lastPlotPrefix = thisPlotPrefix; //setup for next iteration
                  } //has a "dash" - keep same if no dash (prevents unprefixed plots from being calico)
             } //trying to mark plots by project

           //  alert('  name:' + thisPlotName);
             var thisPlotLat =  parseFloat(thisPlotCSV[1]); //new variables to hold plot lat/long
            // alert('  lat:' + thisPlotLat);
             var thisPlotLng = parseFloat(thisPlotCSV[2]);
           //  alert('  long:' + thisPlotLng);
             var thisExtraPlotDetail = "  "; //init
             var thisPlotAzimuth = null; //init;
             var thisPlotX = 0; //init, for rectangular plots;
             var thisPlotY = 0; //init;
             var thisGPSX = 0; //init;
             var thisGPSY = 0; //init;
             var thisDsgPoly = ""; //init;
             var thisFuzzingDeg = null;  //init
             var thisAccuracyMeters = null; //init
             // GLog.write("CSV length:" + thisPlotCSV.length);
			 if (thisPlotCSV.length > 3) {
                 thisExtraPlotDetail = thisPlotCSV[3]; //extra stuff just gets printed
                 if (thisPlotCSV.length > 4) {
					  //fuzzing:
					  thisFuzzingDeg = thisPlotCSV[4];
				 }
				 if (thisPlotCSV.length > 5) {
					 //accuracy
					  thisAccuracyMeters = thisPlotCSV[5];
				 }
				 if (thisPlotCSV.length > 8) {
					 //Azimuth:
                    thisPlotAzimuth = thisPlotCSV[6];
                    // plot dimensions
					thisPlotX = thisPlotCSV[7];
					thisPlotY = thisPlotCSV[8];
					//location of the point relative to the plot axis:
					if (thisPlotCSV.length > 10) {
						thisGPSX = thisPlotCSV[9];
						thisGPSY = thisPlotCSV[10];
						if (thisPlotCSV.length > 11) {
							//dsgPoly, MUST BE LAST FIELD!
							var intDSGLoop = "";
							for (intDSGLoop = 11; intDSGLoop < thisPlotCSV.length; intDSGLoop++) {
								//collect DSG Poly point pairs as array
                                thisDsgPoly = thisDsgPoly  + thisPlotCSV[intDSGLoop] + ",";
                                // GLog.write("thisDsgPoly thus far: " + thisDsgPoly);
							} //loop through last fields
							//todo: there is an extra , at end of thisDSGPoly, oh well.
							//remove any quotes and semicolons, which may be lurking.
							thisDsgPoly = thisDsgPoly.replace(/"/g,""); //" // end quote;
							thisDsgPoly = thisDsgPoly.replace(/;/g,""); // remove ;
						} //there are DSG Poly fields
					} //there are GPS X and Y
				 } //there is azimuth, etc.
			 } //there is extra stuff after lat/long

         if (markerNumber >= 0 && !isNaN(thisPlotLat) && !isNaN(thisPlotLng) ) { //wasn't yet cancelled, is valid
             if (markerNumber == 0) {
                //initial run through this:
                plotMinLat = thisPlotLat;
                plotMaxLat = thisPlotLat;
                plotMinLng = thisPlotLng;
                plotMaxLng = thisPlotLng;
                if (blnSetBoundsBasedOnPlots == true ) {
                  //dont know where this is (yet)
                  map = VbGMapLoadByBounds(mapDivId,thisPlotLat-1,thisPlotLat+1,thisPlotLng-1,thisPlotLng+1) ;
                } else {
                  map = VbGMapLoadByBounds(mapDivId,parseFloat(minLat),parseFloat(maxLat),parseFloat(minLng),parseFloat(maxLng)) ;
                }

             } else {
                //not the first time here
                if (thisPlotLat < plotMinLat) {plotMinLat = thisPlotLat;}
                if (thisPlotLat > plotMaxLat) {plotMaxLat = thisPlotLat;}
                if (thisPlotLng < plotMinLng) {plotMinLng = thisPlotLng;}
                if (thisPlotLng > plotMaxLng) {plotMaxLng = thisPlotLng;}
             }
              //get URL to link to on TopoZone
              var thisPlotNameSafeInJS = thisPlotName.replace(/"/g,""); // end annoying single quote> "
              var thisPlotNameLabel = "Plot: <strong>" + thisPlotNameSafeInJS + "</strong>";
              var thisPlotMapZoomLink = "http://www.topozone.com/map.asp?lat=" + thisPlotLat + "&lon=" + thisPlotLng + "&datum=NAD83&s=24&size=m&extra=" + thisPlotNameSafeInJS;
             //function to show the map on TopoZone next to our map
              var thisPlotOnClick='VbGUpdateZoomMap("' + zoomMapId + '","' + thisPlotNameLabel + '","' + thisPlotMapZoomLink + '");return false;';
              var thisPlotHTML =  thisPlotNameLabel + "<br/>" + thisExtraPlotDetail + "<br/>Show on TopoZone.com map <a href='#' onclick='" + thisPlotOnClick + "'>below</a> or in a <a target='_new' href='" + thisPlotMapZoomLink + "'>new window</a>";
             // if (thisPlotX>0 && thisPlotY>0 && (thisPlotAzimuth)) {
				  //has X, Y, and azimuth, allow it to be mapped:
			//	  var thisDrawBounds = 'VbGDrawPlotBounds(VbG_global_mapMainMap,' + thisPlotLat + ',' + thisPlotLng + ',' + thisPlotAzimuth + ',' + thisPlotX + ',' + thisPlotY + ',' + thisGPSX + ',' + thisGPSY + ');';
			//	  thisPlotHTML = thisPlotHTML + "<span class='lk drawPlotBounds' onclick='" + thisDrawBounds + "'>draw plot boundaries</span>";
			 // }


              markerNumber =  VbGCreateMarker(thisPlotLat,
                                      thisPlotLng,
                                      thisPlotHTML,
                                      colorNumber, true, map, markerNumber, markerConfirmNumber, thisFuzzingDeg, thisAccuracyMeters,
                                      thisPlotAzimuth , thisPlotX , thisPlotY , thisGPSX , thisGPSY , thisDsgPoly,
                                      blnAutoDrawAcc, blnAutoDrawBounds);
              //if ( thisMarker ) {
			//	  markerNumber ++;
			  //} else {
			//	  markerNumber = -2 ; //cancel!
			  //}

			  //now can add new listener to marker:
			//  GLog.write("adding dbl click listener after overlay:"  + (thisPlotLat-0.5) + "," + (thisPlotLng-0.5) + "," + (thisPlotLat+0.5) + "," + (thisPlotLng+0.5));
			//	GEvent.addListener(thisMarker, "dblclick", function() {
			//				   alert('I am double clicked!');
			//				   VbGMarkLine(thisPlotLat-0.5,thisPlotLng-0.5,thisPlotLat+0.5,thisPlotLng+0.5,map,000000,1,1)
			//				 });
				//              GLog.write('adding marker redundantly:');
			//				  map.addOverlay(thisMarker);  //it's already there!
              // markers[i].getElementsByTagName("htmltoshow").toString()
              //markerNumber ++;
            } //wasn't yet cancelled.
	      } //end of if at least 3 items in CSV
          } //end of for loop
          if (map && blnSetBoundsBasedOnPlots == true) {
              //reset map boundaries
              VbGFixZoomToBounds(map,plotMinLat,plotMaxLat,plotMinLng,plotMaxLng,true)
          }

       //}); //old GDownloadUrl, which failed b/c of permissions... weird.
       return map;
   }

   function VbGDrawPlotLine(boundsmap,lat,lng,azimuth,X1,Y1,X2,Y2,GPSX,GPSY) {
	   //function draws a line on boundsmap, using lat and lng as plot coordinates, offset by plot grid at azimuth angle,
	   // line is from (X1,Y1) to (X2,Y2) offset by GPSX and GPSY if present
	   if (GPSX && GPSY) {
		   //fine!
	   } else {
		   //not fine one is missing, assume 0
		   GPSX = 0;
		   GPSY = 0;
	   }

	  // GLog.write("VbGDrawPlotLine> " + X1 + "," + Y1 + " -> " + X2 + "," + Y2);
	  //adjust by GPS X and GPSY:
	  var GPSAdjX1 = X1 - GPSX;
	  var GPSAdjX2 = X2 - GPSX;
	  var GPSAdjY1 = Y1 - GPSY;
	  var GPSAdjY2 = Y2 - GPSY;
	  // GLog.write("VbGDrawPlotLine> Adjust for GPS position: " + GPSAdjX1 + "," + GPSAdjY1 + " -> " + GPSAdjX2 + "," + GPSAdjY2);


	  //first figure out the angle on the plot grid for each point:
	  var alpha1 = Math.atan2(GPSAdjY1,GPSAdjX1);
	 // if (GPSAdjX1 != 0) {
	 //   alpha1 = Math.atan(GPSAdjY1/GPSAdjX1);  //angle for plot coordinates to point 1
    //	} else {
	//	if (GPSAdjY1 > 0) {
	//	  alpha1 = Math.PI / 2; //if X is 0, then could be up or down
	//		} else {
	 //     alpha1 = -1 * (Math.PI / 2);
	//		}
    //	}
	  var alpha2 = Math.atan2(GPSAdjY2,GPSAdjX2);

	  //if (GPSAdjX2 != 0 ) {
	  //  alpha2 = Math.atan(GPSAdjY2/GPSAdjX2);  //angle for plot coordinates to point 2
		//} else {
		//	if (GPSAdjY2 > 0) {
		//	  alpha2 = Math.PI / 2; //if X is 0, then could be up or down
		//		} else {
		//	  alpha2 = -1 * (Math.PI / 2);
		//		}
		//}
      // GLog.write("alpha1 (in deg): " + (alpha1 * 180) / Math.PI);
      // GLog.write("alpha2 (in deg): " + (alpha2 * 180) / Math.PI);

	  var Dist1 = Math.sqrt(Math.pow(GPSAdjX1,2) + Math.pow(GPSAdjY1,2)); //distance to point, on plot coordinates
	  var Dist2 = Math.sqrt(Math.pow(GPSAdjX2,2) + Math.pow(GPSAdjY2,2)); //distance to point, on plot coordinates

      // GLog.write("Dist1 (m): " + Dist1);
      // GLog.write("Dist2 (m): " + Dist2);

	  //this angle is modified by beta, the angle difference between N and plot 0 degress: (azimuth), plus 90, since N is at 90 in Cartesian:
	  var beta = (azimuth * (Math.PI / 180));
      // GLog.write("beta (in deg): " + (beta * 180) / Math.PI);
      var gamma1 = ((alpha1 - beta) + (Math.PI / 2)); //geocoordinate angle to point1
      var gamma2 = ((alpha2 - beta) + (Math.PI / 2)); //geocoordinate angle to point2
      // GLog.write("gamma1 (in deg): " + (gamma1 * 180) / Math.PI);
      // GLog.write("gamma2 (in deg): " + (gamma2 * 180) / Math.PI);
	  //figure out where these locations should be on polar grid, in meters
	  var AdjX1 = (Math.cos(gamma1) * Dist1 ) ;
	  // GLog.write("Rotated X1: " + AdjX1);
	  var AdjY1 = (Math.sin(gamma1) * Dist1 ) ;
	  // GLog.write("Rotated Y1: " + AdjY1);

	  //figure out where these locations should be on polar grid, in meters
	  var AdjX2 = (Math.cos(gamma2) * Dist2 ) ;
	  // GLog.write("Rotated X2: " + AdjX2);
	  var AdjY2 = (Math.sin(gamma2) * Dist2 ) ;
	  // GLog.write("Rotated Y2: " + AdjY2);

       //adjust to Geocoordinates:
     var LongAdjust = Math.cos((lat * Math.PI) / 180);
     var LngX1 = lng + ((AdjX1 / CON_LAT_TO_METERS_DIVISOR) / LongAdjust);
      // GLog.write("LngX1: " + LngX1);
     var LatY1 = lat + ((AdjY1 / CON_LAT_TO_METERS_DIVISOR));
      // GLog.write("LatY1: " + LatY1);
     var LngX2 = lng + ((AdjX2 / CON_LAT_TO_METERS_DIVISOR) / LongAdjust);
	  // GLog.write("LngX2: " + LngX2);
	 var LatY2 = lat + ((AdjY2 / CON_LAT_TO_METERS_DIVISOR));
	  // GLog.write("LatY2: " + LatY2);
	 //draw it!
     VbGMarkLine(LatY1,LngX1,LatY2,LngX2,boundsmap,000000,1,1);


   }

   function VbGDrawPlotBounds(boundsmap,lat,lng,azimuth,X,Y,GPSX,GPSY, dsgPoly) {
	    //function draws boundaries of plot, given lat,long, azimth, X,Y,GPSX, and GPSY


        if (!boundsmap) {
			boundsmap = VbG_global_mapMainMap;
		    // GLog.write("VbGDrawPlotBounds> using global var as no map was passed.");
		}
		if ((azimuth) && (X) && (Y)) {
			//have azimuth, X and Y, go for it!
			// farm this out to map single lines at a time, using plot dimensions:
			VbGDrawPlotLine(boundsmap,lat,lng,azimuth,0,0,X,0,GPSX,GPSY);
			VbGDrawPlotLine(boundsmap,lat,lng,azimuth,X,0,X,Y,GPSX,GPSY);
			VbGDrawPlotLine(boundsmap,lat,lng,azimuth,X,Y,0,Y,GPSX,GPSY);
			VbGDrawPlotLine(boundsmap,lat,lng,azimuth,0,Y,0,0,GPSX,GPSY);

		} else {
		   // non rectangular, use dsgpoly if passed:
		   if ((azimuth) && (dsgPoly)) {
			   //use azimuth and dsgPoly
			   // GLog.write("VbGDrawPlotBounds> dsgPoly not implemented yet.");
			   //dsgPoly is structured as (X1,Y1)(X2,Y2)(X3,Y3)...
			   //so first split based on )
			   var arrDsgPoints = dsgPoly.split(")");
			   //now an array like [(X1,Y1][(X2,Y2][(X2,Y2]
			   var blnLastPointDefined = false;
			   var lastX = null;
			   var lastY = null;
			   for (var pt = 0; pt < arrDsgPoints.length; pt++) {
				   //loop through points and map from one to next
				   //get this point:
				   var thisPoint = arrDsgPoints[pt];
				   //strip first (
				   if (thisPoint.indexOf("(") != -1  ) {
					  thisPoint = thisPoint.substr(thisPoint.indexOf("(")+1);
				   }
				   var thisX = parseFloat(thisPoint.substring(0,thisPoint.indexOf(",")));
				   var thisY = parseFloat(thisPoint.substring(thisPoint.indexOf(",")+1));
				   // GLog.write("Magically doing a point: " + thisX + "," + thisY);
				   if (!isNaN(thisX) && !isNaN(thisY)) {
					   if (blnLastPointDefined == true) {
						   //draw from this to last point:
						   VbGDrawPlotLine(boundsmap,lat,lng,azimuth,lastX,lastY,thisX,thisY,GPSX,GPSY);
					   } //last point exists
					   //remember this point for next time:
					   lastX = thisX;
					   lastY = thisY;
					   blnLastPointDefined = true;

				   } //X and Y are OK

			   } //end loop through points
		   } //end dsgPoly instructions
		} // X,Y, and azi or not

   }

   function VbGUpdateZoomMap(ZoomId,plotName,url) {
       //updates the zoomed in map to something new
       var zoomMapDiv = document.getElementById(ZoomId);
       zoomMapDiv.style.display = 'block';
       var zoomMapIFrame = zoomMapDiv.getElementsByTagName("iframe")[0];
       zoomMapIFrame.src = url;
       document.getElementById('zoomInPlotName').innerHTML = plotName;
       document.getElementById('zoomInMapLink').href= url;
   }

   function VbGCloseZoomMap(ZoomId) {
       //closes the zoomed in map.
       var zoomMapDiv = document.getElementById(ZoomId);
       zoomMapDiv.style.display = 'none';
   }

