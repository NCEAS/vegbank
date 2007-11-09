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


  // Creates a marker whose info window displays the given html (not number)
   function VbGCreateMarker(lat,lng, html, number,  blnColored, map, markerNumber, markerConfirmNumber) {
         //this creates marker with listener that responds to clicks:
         // lat and lng are floating point latitude and longitude in decimal degrees
         // html is html to display for the marker (ie plot identification)
         // number is the group number to display: 0-9 
         // blnColored is true if using small colored icons, else larger lettered ones
         // markerNumber is a counter for the number of plots mapped
         // markerConfirmNumber is a threshhold past which the user gets a confirmation message to continue mapping.
         // if markerConfirmNumber is -1, then uses the default, supplied in this function, else uses what is set in page
         
         if ( markerConfirmNumber == -1 ) {
            markerConfirmNumber = 100 ; //default value 
         }
         
         // // alert( 'writing new marker at ' + lat + ',  ' + lng);
//          GLog.write('trying marking #' + markerNumber);
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

             GEvent.addListener(marker, "click", function() {
               marker.openInfoWindowHtml(html);
             });
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
    //this does the reverse of VbGMarkCircularAccuracy, which uses degrees, this uses meters.
          
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
  
  



   function VbGMarkCircularAccuracy(lat,lng,degreesErr,metersErr,map) {
       // this marks a map with a cirular-ish (really polygon) circle marking the inaccuracy in a plot's location
       // lat and lng are latitude and longitude in decimal degrees
       // degreesErr is error margin in degrees
       // metersErr is meters error, additive to degreesErr

       //first, it calculates the meters error by guessing where the points should go,
       // using 111.319718km = 1 deg Lat and 75km = 1 deg Long (varies depending on where you are)
       //calc the meters err as longitude:
       // alert( 'init VbGMarkCircularAccuracy');
       var mErrToLat = (metersErr / 111131.9718);
       // GLog.write("Long Err:" + mErrToLong);
       //calc the meters err as latitude, adjusting for by latitude:
       // alert( 'starting math cosine');
       var LongAdjust = Math.cos((lat * Math.PI) / 180);
       // alert( "Long Adjustment is: " + LongAdjust);
       var mErrToLong = (mErrToLat / LongAdjust);
       // alert( metersErr + " -> Lat:" + mErrToLat );
       // alert( "--Long:" + mErrToLong);
       //now see if Google agrees:
       var OriginPoint = new GLatLng(lat,lng);
    //   VbGCreateMarker(lat,lng, "Origin", 1,  false, map);
       var LatErrPoint = new GLatLng(lat + mErrToLat,lng);
      // VbGCreateMarker(lat + mErrToLat,lng, "Origin", 2,  true, map);
       var LongErrPoint = new GLatLng(lat,lng + mErrToLong);
      // VbGCreateMarker(lat,lng + mErrToLong, "Origin", 3,  true, map);
       // alert( "orig error was:" + metersErr);
//     GLog.write("Google thinks error (meters) Lat:" + OriginPoint.distanceFrom(LatErrPoint) );
//     GLog.write("-g--Long:" + OriginPoint.distanceFrom(LongErrPoint));

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
       //  GLog.write("added point:");
       //  GLog.write(LatPoints[i]);
       //  GLog.write(LongPoints[i]);
        // VbGCreateMarker(lat+LatPoints[i],lng+LongPoints[i],"this is " + i,i,true,map);
         points.push(new GLatLng(lat + LatPoints[i],lng + LongPoints[i]));
       }
       // alert( 'done with loop');
       // push the original location again:
       points.push(new GLatLng(lat + LatPoints[0],lng + LongPoints[0]));
       // alert( 'pushed some points');
       map.addOverlay(new GPolyline(points,"#CCCCCC"));
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
       map.addOverlay(new GPolyline(newPoints,"#FF0000"));
       map.addOverlay(new GPolyline(evenNewerPoints,"#0000FF"));
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
                 VbGCreateMarker(thisPlotLat,
                                      thisPlotLng,
                                      markers[i].getAttribute("name"),
                                      1, true, map, markerNumber, markerConfirmNumber);
              // markers[i].getElementsByTagName("htmltoshow").toString()
              markerNumber ++;
            }
          }
          if (blnSetBoundsBasedOnPlots == true) {
              //reset map boundaries 
              VbGFixZoomToBounds(map,plotMinLat,plotMaxLat,plotMinLng,plotMaxLng,true) 
          }
          
       //}); //old GDownloadUrl, which failed b/c of permissions... weird.
       return map;
   }

  function VbGMapCSV(csvstring,mapDivId,minLat,maxLat,minLng,maxLng, blnSetBoundsBasedOnPlots, blnTryToMarkProjects, zoomMapId) {
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
             var thisPlotName = thisPlotCSV[0];
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
			 if (thisPlotCSV.length > 3) {
                 thisExtraPlotDetail = thisPlotCSV[3];
			 }
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
              var thisPlotNameLabel = "Plot: <strong>" + thisPlotName + "</strong>";
              var thisPlotMapZoomLink = "http://www.topozone.com/map.asp?lat=" + thisPlotLat + "&lon=" + thisPlotLng + "&datum=NAD83&s=24&size=m&extra=" + thisPlotName;
             //function to show the map on TopoZone next to our map
              var thisPlotOnClick='VbGUpdateZoomMap("' + zoomMapId + '","' + thisPlotNameLabel + '","' + thisPlotMapZoomLink + '");return false;';
              var thisPlotHTML = "<div class='vbgmaplabel'>" + thisPlotNameLabel + "<br/>" + thisExtraPlotDetail + "<br/>Show on TopoZone.com map <a href='#' onclick='" + thisPlotOnClick + "'>below</a> or in a <a target='_new' href='" + thisPlotMapZoomLink + "'>new window</a> </div>";
             
              markerNumber = VbGCreateMarker(thisPlotLat,
                                      thisPlotLng,
                                      thisPlotHTML,
                                      colorNumber, true, map, markerNumber, markerConfirmNumber);
              // markers[i].getElementsByTagName("htmltoshow").toString()
              //markerNumber ++;
            }
          } //end of for loop
          if (map && blnSetBoundsBasedOnPlots == true) {
              //reset map boundaries 
              VbGFixZoomToBounds(map,plotMinLat,plotMaxLat,plotMinLng,plotMaxLng,true) 
          }
          
       //}); //old GDownloadUrl, which failed b/c of permissions... weird.
       return map;
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
   
