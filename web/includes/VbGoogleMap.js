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
          map.setMapType(G_SATELLITE_MAP);
		  //save this position:
          map.savePosition();
         map.addControl(new GSmallMapControl());
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
	   var ctlLng = ((minLng + maxLng) * 0.5);
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
	    
	    // set center as same
	    whereismap.setCenter(new GLatLng((minLat + maxLat) * 0.5,(minLng + maxLng) * 0.5), 1);
	    //array of points
	    var points = [];
	    //add the points, in sequence
	    points.push(new GLatLng(minLat, minLng));
	    points.push(new GLatLng(minLat, maxLng));
	    points.push(new GLatLng(maxLat, maxLng));
	    points.push(new GLatLng(maxLat, minLng));
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
            //get center and center it still:
              var MyCenterLat =   (maxLat + minLat)*0.5 ;
	        //   GLog.write('center Lat:' + MyCenterLat);
	          var MyCenterLong = (minLng + maxLng)*0.5 ;
	   	    // calc a GLatLngBounds for the area:
	   	    var boundsObj = new GLatLngBounds(new GLatLng(minLat,minLng),new GLatLng(maxLat,maxLng))
	   	    //center it and zoom according to GLatLngBounds:
	   	    map.setCenter(new GLatLng( MyCenterLat,MyCenterLong));
	   	    map.setZoom(map.getBoundsZoomLevel(boundsObj));
	   	  //  map.zoomOut();
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
// 					 GLog.write('marking #' + markerNumber + 'cancelled');
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
             while (number > 9) {
				 number = number - 10;
			 }
			 var icon = new GIcon(baseIcon,"@images_link@" + VbGiconPrefix +  number + ".png");
			 // icon.image = "@images_link@" + VbGiconPrefix +  number + ".png";
			 var marker = new GMarker(point, icon);

			 // show plot info when clicked

			 GEvent.addListener(marker, "click", function() {
			   marker.openInfoWindowHtml(html);
			 });
// 		      GLog.write('before adding marker:');
			  map.addOverlay(marker);
			  markerNumber ++;
	 } //keepMapping true
// 	     GLog.write('returning: ' + markerNumber);
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
// 	   GLog.write("Google thinks error (meters) Lat:" + OriginPoint.distanceFrom(LatErrPoint) );
// 	   GLog.write("-g--Long:" + OriginPoint.distanceFrom(LongErrPoint));

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
       map.addOverlay(new GPolyline(points,"CCCCCC"));
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
       map.addOverlay(new GPolyline(newPoints,"FF0000"));
       map.addOverlay(new GPolyline(evenNewerPoints,"0000FF"));
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
		  map.clearOverlays();
	  }    
    
    if (document.getElementById("pointsClicked").innerHTML == "0") {
	  // first time, draw point:
	  VbGCreateMarker(point.lat(),point.lng(), "This the first point on the map that you drew.  It is a placeholder point until you click another point.", 0,  true, map, 1, 500);	 
	  // make sure that the values aren't preloaded (ie with back button):
	  VbGClearMapForm();
	} 

	  
	  //map these with the current Lat/Long
      VbGMarkLine(prevLat,prevLong,point.lat(),point.lng(),map);
	
    document.getElementById("pointsClicked").innerHTML = Number(document.getElementById("pointsClicked").innerHTML) + 1;
    document.getElementById("message").innerHTML =  "added point #" + document.getElementById("pointsClicked").innerHTML 
              + "(" + point.lat() + "," + point.lng() + ")" ;
    
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

  function VbGMapURL(url,mapDivId,minLat,maxLat,minLng,maxLng) {
	  //try to get map set:
	  alert(url);
	  var map = VbGMapLoadByBounds(mapDivId,parseFloat(minLat),parseFloat(maxLat),parseFloat(minLng),parseFloat(maxLng)) ;
	  GDownloadUrl(url, function(data, responseCode) {
	  //  alert('got some data:' + data);
	    var xml = GXml.parse(data);
	    var markers = xml.documentElement.getElementsByTagName("plot");
	    var markerNumber = 0;
	    var markerConfirmNumber = 80;
	    alert('got ' + markers.length + ' plots');
	    for (var i = 0; i < markers.length; i++) {
	      // var point = new GLatLng(parseFloat(markers[i].getAttribute("latitude")),
	      //                        parseFloat(markers[i].getAttribute("longitude")));
	      // map.addOverlay(new GMarker(point));
	     if (markerNumber >= 0) {

				 VbGCreateMarker(parseFloat(markers[i].getAttribute("latitude")),
									  parseFloat(markers[i].getAttribute("longitude")),
									  "html placeholder",
									  1, true, map, markerNumber, markerConfirmNumber);
			  // markers[i].getElementsByTagName("htmltoshow").toString()
			  markerNumber ++;
	        }
	      }
	   });
   }

