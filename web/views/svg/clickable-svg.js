    /////////////////////////////////////////////////////////////////
    // SETUP SVG DOCUMENT ///////////////////////////////////////////
    /////////////////////////////////////////////////////////////////
    // all elements that could be shaded must have a unique ID
    // and also have onclick="top.svg_adjust(this)"
    // version = 1.8
var devMode = false;
var spf = "savedparamform";
var cssAdded = false;
var warnedUserHowToGetNavAgain = false;

  addEvent(window, "load", prepareSVG);

function svg_setMonochrome() {
	//alert('adding monochrome rules');
	var colorList = new Array() ;
	// old method: addCSSRuleToSVG(".img1", " fill: #FF0000;");
	colorList.push ("url(#hatch00)");
	colorList.push ("url(#hatch01)");
	colorList.push ("url(#hatch02)");
	colorList.push ("url(#hatch03)");
	for (var i=1;i<=20;i++) {
	  //assume fill for SVG and background-color for main doc
	   addCSSRuleToBothDocs(".img" + i,  colorList[i - 1] + ";");
	}
	// state as white:
	cssAdded = addCSSRuleToBothDocs(".state", "fill:#FFFFFF", true);
}

function svg_addColorCSS() {
  //this adds CSS rules for images to svg doc
  // colors thanks to http://colorschemedesigner.com/
	var colorList = new Array() ;
	var colorsURL = getParamForm("colors").split(",");
	if (colorsURL.length>0) {
		for (var i = 0;i<colorsURL.length;i++) {
		   var colorCheck = colorsURL[i].toUpperCase().match(/[0-9A-F]{6}/);
		   if (colorCheck != null) {
			   //alert('custom color:' + colorCheck);
			   colorList.push ("#" + colorCheck);
		   }
		}
	}
	// make sure there are enough colors:
	if (document.getElementById('maxcolorcount').value < colorList.length) {
	   document.getElementById('maxcolorcount').value = colorList.length;
	   maxColorsIsNow(colorList.length);
	}

	//default colors:
	colorList.push ("#104BA9");
	colorList.push ("#C30083");
	colorList.push ("#AEF100");
	colorList.push ("#FFA200");
	colorList.push ("#284A7E");
	colorList.push ("#92256E");
	colorList.push ("#8FB52D");
	colorList.push ("#BF8B30");
	colorList.push ("#052D6E");
	colorList.push ("#7F0055");
	colorList.push ("#719D00");
	colorList.push ("#A66A00");
	colorList.push ("#447BD4");
	colorList.push ("#E138AA");
	colorList.push ("#C4F83E");
	colorList.push ("#FFBA40");
	colorList.push ("#6A93D4");
	colorList.push ("#E165B9");
	colorList.push ("#D2F870");
	colorList.push ("#FFCC73");

	for (var i=1;i<=20;i++) {
		 //assume fill for SVG and background-color for main doc
		addCSSRuleToBothDocs(".img" + i,  colorList[i - 1] + ";");
	}
	//check for bgcolor:
	var validbgcolor = getParamForm("bgcolor").toUpperCase().match(/[0-9A-F]{6}/);
	if (validbgcolor == null) {
	   validbgcolor = "d0d0d0"; //default
	}
	cssAdded = addCSSRuleToBothDocs(".state", "fill:#" + validbgcolor, true);

   //save values: by adding to newParams value:
   var pcolors = "";
   for (var ni=0;ni<colorList.length;ni++) {
	  pcolors = pcolors + colorList[ni].replace("#","") + "," ;
   }
   saveParamNorm("color",pcolors);
   saveParamNorm("bgcolor",validbgcolor);
}



function addCSSRuleToBothDocs(selector, attributes, fullCSSHere) {
   // adds css rules to both svg document and main document
   // thanks to http://www.webdeveloper.com/forum/archive/index.php/t-93091.html
   var SD2 = getSD();
   var ss = SD2.styleSheets[SD2.styleSheets.length-1];
   var SVGPrefix = "";
   var mainPrefix = "";
   if (!fullCSSHere) {
	   SVGPrefix = " fill: ";
	   mainPrefix = " background-color: ";
   }
   var blnSuccess = false;
   blnSuccess = addCssRuleToSS(ss,selector,SVGPrefix + attributes);
   var ssMain = document.styleSheets[document.styleSheets.length-1];
   blnSuccess = blnSuccess && addCssRuleToSS(ssMain,selector,mainPrefix + attributes);
   return blnSuccess;
}

function addCssRuleToSS(ss,selector,attributes) {
	//adds css to ss
	if(ss.insertRule) ss.insertRule(selector + '{' + attributes + '}', ss.cssRules.length);
	else if(ss.addRule) ss.addRule(selector, attributes);
	return true;
}

function showGrain(showWhat) {
 //shows elements related to a particular grain and hides other elements not related to that grain
 var ssMain = document.styleSheets[document.styleSheets.length-1];
  if (showWhat == "county") {
	  addCssRuleToSS(ssMain,".countygrain","display:block;");
	  addCssRuleToSS(ssMain,".stategrain","display:none;");
  } else {
	  addCssRuleToSS(ssMain,".countygrain","display:none;");
	  addCssRuleToSS(ssMain,".stategrain","display:block;");
  }
}

var thisReadOnly = "unkn"; //remember whether svg is read only or not
function svg_adjust_interface(O){
	   //interface is being used to adjust display.  Allowed?
	   if (thisReadOnly == "unkn") {
		 thisReadOnly = getParamForm("readonly");
	 }
	 if (getParamForm("dbsummary") == "y") {
		 thisReadOnly = "readonly"; //db summary is read only!
	 }
	 if (thisReadOnly != "readonly") {
		 svg_adjust(O);
	 } else {
		// alert('readonly');
		showHideNav(true);
	 }
  }

function svg_adjust(O){
	//increases color class by one, as when clicked
	//O = svgDocument.getElementById(id);
	if (cssAdded == false) {
		handleMonochrome(); //handles whether monochrome or not and css
	}
	var currClassNum = 0; //init
	var class = O.getAttribute("class");
	if (class != null) {
		currClassNum = Number(O.getAttribute("class").substr(3));
	}
	//alert(getMaxColors());
	 if (currClassNum >= getMaxColors()) {
	   currClassNum = -1; //reset!
	 }
	O.setAttribute("class","img" + (currClassNum + 1));
	 //alert(O.getAttribute("id"));
	 saveParam(O.getAttribute("id"),(currClassNum + 1));
	//if (O.getAttribute("class")=="red") O.setAttribute("fill","blue");
	//else O.setAttribute("fill","red");
	// update number of clicks made:
	updateClickCounter(1);
	showHideNav(true); //make sure control box is shown

}

function svg_adjustById(elId){
//call svg_adjust, getting element with ID:
  var SD2 = getSD();
  if (elId.length != 0 ) {
    var el = SD2.getElementById(elId.toLowerCase());
	if (el != null) {
	   svg_adjust_interface(el);
	}
  }

}

function getDefaultZoom() {
  //gets initial default zoom, from defaultZoom id element in svg document, with value in the title attribute
  var SD2 = getSD();
  var defZoom= Number(SD2.getElementById("defaultZoom").getAttribute("title"));
  //alert(defZoom + ' is defZoom');
  return defZoom;
}
function resetToDefault() {
  //resets postition to default position and zoom, using resetPosition id element in svg document, with value in the title attribute
  var SD2 = getSD();
  var defPos =  SD2.getElementById("resetPosition").getAttribute("title");
  moveMapByPicklist(defPos);
}



function svg_rotate(degrees) {
  var SD2 = getSD();
  SD2.getElementById("mainRotate").setAttribute("transform","rotate(" + degrees + ")");
}

/////////////////////////////////////////////////////////
// HTML parameters which allow "saving the view"
/////////////////////////////////////////////////////////
//valid params: 1-20: list of state_county to shade
	//   colorCount: count of colors (numeric)
	//   zoom: zoom level
	//   monochrome: true if monochrome mode
	//   hidden: list of hidden states (by abbreviation)
	//   cap: caption text                                       --saved and settable
	//     capx, capy, capfont, capfontsize: caption metadata    --saved, and settable
	//   colors: list of colors to use: e.g.,  #FFFFCC,#FF0000   --saved, but not settable
	//   ccap1-ccap20: captions for colors 1 to 20               --saved, but settable
	//   ccapbgcolor: background color for color legend          --not saved, nor settable
	//   ccaptitle: title of color legend                        --saved, but settable
	//   ccapx, ccapy: x and y of color legene                   --saved?, settable.
	//   rotate: how much rotation to add
	//   readonly: readonly if no adjustments to colors allowed

function interpParams() {
   //looks for params and sets colors according to them

   // load all params into form:
   var strHref = window.location.href;
			if (strHref.indexOf("?") > -1) {
			   var strQueryString = strHref.substr(strHref.indexOf("?") + 1); //added +1 MTL
			   var aQueryString = strQueryString.split("&");
			   for ( var iParam = 0; iParam < aQueryString.length; iParam++ ) {
				   //if (aQueryString[iParam].indexOf(strParamName + "=") == 0  ) { //made == 0 instead of > -1 MTL
						 var aParam = aQueryString[iParam].split("=");
						 //strReturn = aParam[1];
						 saveParamNorm(aParam[0],Url.decode(aParam[1]));
						 //break;
				   //      }
				} //end for
			} //end has params

   //what map to use?
   var thisScope = getParamForm("scope");
   if (thisScope == "") {
	 //no scope specified, assume continent
	 thisScope = "continent";
   }
   var thisGrain = getParamForm("grain");
   if (thisGrain == "") {
	  //assume state = state/prov
	  thisGrain = "state";
   }

   var thisLoc = getParamForm("loc");
   if (thisLoc == "") {
	   thisLoc = "n"; //n is north america
   }
   //gebid("sv").setAttribute("src", thisScope + "-" + thisLoc + "-" + thisGrain + ".svg");
   showGrain(thisGrain);

   //add beforeunload event if not readonly
   if (getParamForm("readonly") != "readonly") {
     addEvent(window, "beforeunload", confirmLeave);
   }
   if (getParamForm("smallCapture") == "y") {
        showhidebyid("smallCapture",true);
        warnedUserHowToGetNavAgain = true;
        showHideNav(false);
        document.getElementById('instructions').style.display='none';
   }
   if (getParamForm("largeCapture") == "y") {
        showhidebyid("largeCapture",true);
        warnedUserHowToGetNavAgain = true;
        showHideNav(false);
        document.getElementById('instructions').style.display='none';
   }

   var thisListLocs = "" ; // init
   var maxColorsUsed = 0;
   if (getParamForm("dbsummary") == "y") {
	  thisListLocs = gebid("dblookup").innerHTML;
	  //alert(thisListLocs + ' found from db');
   } else {
     //alert('set new sv src');
	   // "loads" map from URL parameters
	   for (var i=1;i<=20;i++) {
		 //get i param:
		 var currShadeText = getParamForm(i);
		// if (i<3) {
		//	 alert(i + ':' + currShadeText);
		// }
		 if (currShadeText.length > 0 ) {
			//split by comma:
			 var currShades = currShadeText.split(",");
			 for (var j=0;j<currShades.length;j++) {
			   // shade this whatever color we are on:
			   setElClassById(currShades[j].toLowerCase(),i);
			 }
			 if (i > maxColorsUsed) { //remember max colors used
				 maxColorsUsed = i;
			 }
		 }
	   }  //end for loop
	   thisListLocs = getParamForm("listlocs");
   } //dbsummary or not

	   if (thisListLocs != "" ) {
		  // list locations individually.  More space for URL, but easier to generate
		 var arrListLocs = thisListLocs.split(";");
		 for (var ill = 0; ill<arrListLocs.length;ill++) {
			 var thisOneLoc = arrListLocs[ill];
			 if (thisOneLoc.indexOf(":")>0) {
				try {
					var thisOneLocStr = thisOneLoc.split(":")[0];
					var thisOneLocCol = Number(thisOneLoc.split(":")[1]);
					setElClassById(thisOneLocStr.toLowerCase(),thisOneLocCol);
					if (thisOneLocCol > maxColorsUsed) {
						maxColorsUsed = thisOneLocCol;
					}
				} catch(e) {
					//ignore error for now!
				}
			 }
		 }
	   } //is a list of locs



   //set colors:
   var colCount = getParamForm("colorCount");

   if (colCount.length <= 0) {
	   colCount = 5; //default
   }
   if (maxColorsUsed > colCount) {
	   colCount = maxColorsUsed;
   }
   document.getElementById('maxcolorcount').value = colCount;
   maxColorsIsNow(colCount); //adjust number of colors on key
   //load saved x,y, zoom:
   var x = getParamForm("x");
   var y = getParamForm("y");
   var zoom = getParamForm("zoom");
   if ( (x.length > 0) && (y.length > 0) ) {
	 setMapXYShift(x,y);
   }
   if (zoom.length >0) {
	 setZoom(zoom);
   }
   var rotate = getParamForm("rotate");
   if (rotate.length>0) {
	 svg_rotate(rotate);
   }
   handleMonochrome();

   //hidden states:
   try {
	   var hidSt = getParamForm("hidden").split(",");
	   for (var hsi=0;hsi<hidSt.length;hsi++) {
		  setElClassById(hidSt[hsi].toLowerCase(),"hidden");
	   }
   } catch(e) {
     //ignore errors
   }

   // caption:
   try {
   var cap = getParamForm("cap");
   //alert('cap param >' + cap + '<');
   var capEl = document.getElementById('pagecaption-display');
   var capCont = gebid('pagecaption-container');
   if (cap.length >0) {

	   capEl.innerHTML = Url.decode(cap);
	   capCont.style.display = 'block';
	   var capx = getParamForm("capx");
		   var capy = getParamForm("capy");
		   if ((capx.length>0)) {
			  capCont.style.left = capx ;
		   }
		   if ((capy.length>0)) {
			   capCont.style.top = capy;
		   }
		   var capfont = getParamForm("capfont");
		   //alert('font ' +  Url.decode(capfont));
		   if (capfont.length > 0 ) {
			   capEl.style.fontFamily = Url.decode(capfont);
		   }
		   var capfontsize = getParamForm("capfontsize");
		   if ((capfontsize.length>0)) {
			   capEl.style.fontSize = capfontsize;
		   }
   }
   // deal with color captions/labels
   var blnShowColorLegend = false;
   for (cci = 1 ; cci<=20; cci++) {
	 var ccap = getParamForm("ccap" + cci);
	 if (ccap.length>0) {
		 //apply label:
		 blnShowColorLegend = true;
		 var elToEdit = getColorCapEl(cci);
		 if (elToEdit) {
			elToEdit.innerHTML = Url.decode(ccap);
		 }
		//var elToEdit = gebid("colorLegend" + cci).getElementsByTagName("DIV");
		// for (var ee=0;ee<elToEdit.length;ee++) {
		//	 //find the right one:
		//	 if (elToEdit[ee].className.indexOf("colorLegendText")!=-1) {
		//		 elToEdit[ee].innerHTML = Url.decode(ccap);
		//	 }
		// }
	 }
   }
   //check additional color captions stuff:
   var ccapbgcolor = getParamForm("ccapbgcolor");
   if (ccapbgcolor.length>0 ) {
		gebid("colorLegendDiv").style.backgroundColor= ccapbgcolor;
		blnShowColorLegend = true;
   }
   var ccaptitle = getParamForm("ccaptitle");
   if (ccaptitle.length>0) {
	   gebid("colorLegendDiv").getElementsByTagName("DIV")[0].innerHTML = Url.decode(ccaptitle);
   }
   var ccapx = getParamForm("ccapx");
   if (ccapx.length>0) {
	   gebid("colorLegendDiv").style.position = "absolute";
	   gebid("colorLegendDiv").style.left = Url.decode(ccapx);
   }
   var ccapy = getParamForm("ccapy");
		  if (ccapy.length>0) {
		   gebid("colorLegendDiv").style.position = "absolute";
		   gebid("colorLegendDiv").style.top = Url.decode(ccapy);
   }


   if (blnShowColorLegend == true) {
	   showColorLegend();
	   //check additional
   }

  } catch (e) {
	//dont deal with errors, as could be old version of page.
  }
}


function handleMonochrome() {
 //monochrome?
  var mono = getParamForm("monochrome");
  if (mono == "true" ) {
	//set to monochrome mode:
	svg_setMonochrome();
   } else {
	// color!
	svg_addColorCSS();
   }
}

function getColorCapEl(cci) {
  //returns a color caption element (for text)
  var elToEdit = gebid("colorLegend" + cci).getElementsByTagName("DIV");
	 for (var ee=0;ee<elToEdit.length;ee++) {
		 //find the right one:
		 if (elToEdit[ee].className.indexOf("colorLegendText")!=-1) {
			 return elToEdit[ee]; // = Url.decode(ccap);
		 }
	 }
  //if didn't find it, return null
  return null;
}

function getParamForm(strParamName) {
 //gets parameter from form:
 try {
	return gebid(spf + "-" + strParamName).value;
 } catch(e) {
	 //doesnt exist
	 return "";
 }
}

function saveParamNorm(strParamName,strValue) {
 // save color num and id as new parameter
 // see if control exists:

 var f = gebid(spf);
 var ctls = gebid(spf + "-" + strParamName);
 var sc; //control to save value to
 if (ctls == null ) {
  //create it:
	 sc = document.createElement('input');
	 sc.id = spf + "-" + strParamName;
	 sc.name = strParamName;
	 f.appendChild(sc);

 } else {
   //use it:
   sc = ctls;
 }
 sc.value = strValue;

}

function saveParam(strID, colorNum) {
  //save ID and color num to textarea:
  //this "Saves" the current view to a hidden textarea so that a link can be built on demand
  var strCurrParam = document.getElementById('newParams').value;
  //first, check to see if it is there already, if so, remove it:
  strCurrParam = strCurrParam.replace(strID + ',',"");
  //remove double commas:
  strCurrParam = strCurrParam.replace(",,",",");
  //now add to right place, is numeric param here?
  var strToFind = "&" + colorNum + "=";
  var paramLoc = strCurrParam.indexOf(strToFind);
  if (paramLoc > -1) {
	//class is there, so embed value next to it:
	strCurrParam = strCurrParam.substr(0,paramLoc + strToFind.length) + strID + "," + strCurrParam.substr(paramLoc + strToFind.length);
  } else {
	//add param and value:
	strCurrParam = strCurrParam + "&" + colorNum + "=" + strID + "," ;
  }
   document.getElementById('newParams').value = strCurrParam;
   //hide link, as it is outdated:
   showLink(false);
}




function buildMapLink() {
   //clean up params and send user link:
   //get window location:
   var strHref = window.location.href;
   if (strHref.indexOf("?") > -1) {
	 strHref = strHref.substr(0,strHref.indexOf("?"));
   }
   // add params:
   var strParams = document.getElementById('newParams').value;
   //remove & at front
   if (strParams.substr(0,1) == "&" ) {
	 strParams =  strParams.substr(1);
   }
   //remove ,&
   strParams = strParams.replace(",,",",");
   strParams = strParams.replace(/,\&/gi,"&");
   strParams = strParams.replace("=,","=");
   if (strParams.substr(strParams.length -1)==",") {
	 strParams= strParams.substr(0,strParams.length-1);
   }
   //append number of colors:
   strParams = strParams + "&colorCount=" + getMaxColors();
   //x,y,zoom:
   //strParams =  strParams + "&x=" + document.getElementById("x").value ;
   //strParams =  strParams + "&y=" +    document.getElementById("y").value ;
   //strParams =  strParams + "&zoom=" +    document.getElementById("zoom").value ;


   saveCapToParam();

   //load all other params:
   var f = gebid(spf);
   for (var fc = 0;fc<f.elements.length;fc++) {
	  if (f.elements[fc].value != "") {
		strParams = strParams + "&" + f.elements[fc].name + "=" + Url.encode(f.elements[fc].value);
	  }
   }


   //ready:
   //alert(strHref + strParams);
   var fullLink = strHref + "?" + strParams;
   document.getElementById("textLink").value = fullLink;
   document.getElementById("tryLink").setAttribute("href",fullLink);
   try {document.getElementById("tryLinkMonochrome").setAttribute("href",fullLink + "&monochrome=true");} catch (e) {}
   showLink(true); //show the link area
}

function saveCapToParam() {
//saves cap info to parameteres:
	var capCont =   gebid('pagecaption-container');
	var capEl = gebid('pagecaption-display');
	  saveParamNorm("cap" ,capEl.innerHTML);
	   saveParamNorm("capfont" ,capEl.style.fontFamily);
	   saveParamNorm("capfontsize" , capEl.style.fontSize);
	   saveParamNorm("capx" , capCont.style.left);
	   saveParamNorm("capy" , capCont.style.top);
	  //save ccaps: ccap1-ccap20
	  for (var cci = 1 ; cci<=getMaxColors();cci++) {
		 var elToEdit = getColorCapEl(cci);
		 if (elToEdit) {
			 saveParamNorm("ccap" + cci,elToEdit.innerHTML);
		 } else {
			 //clear it:
			 saveParamNorm("ccap" + cci,"");
		 }
	  }
  saveParamNorm("ccapx",gebid("colorLegendDiv").style.left);
  saveParamNorm("ccapy",gebid("colorLegendDiv").style.top);

}

function showLink(blnShow) {
   if (blnShow == true) {
	 document.getElementById("linkInstructions").style.display="block";
   } else {
	 document.getElementById("linkInstructions").style.display="none";
   }
}

//////////////////////////////////////////////////////////
// interface: clear the map
//////////////////////////////////////////////////////////

function clearMap() {
   // use saved params to know what to clear, set all to 0
   //confirm:
   if (confirm("Really clear the map, removing any coloring?")) {
	   var strIDText = document.getElementById("newParams").value;
	   //brute force get IDs:
	   strIDText = strIDText.replace(/\&[0-9]+=/gi,"");
	   //split IDs
	   var strIDs = strIDText.split(",");
	   for (var i=0;i<strIDs.length;i++) {
		 //reset ID:
		 setElClassById(strIDs[i],0);
	   }
	   document.getElementById("newParams").value = "";
	 }
}



function showHideNav(show) {
	 //shows or hides control box
	 dlog("showHideName init:" + show);
	 var nav = document.getElementById('nav');
	 if (show==true) {
		 nav.style.display='block';
	 } else {
		 if (warnedUserHowToGetNavAgain == false) {
			 // tell user how to get box back:
			 show = !confirm("This will hide the main control box.  Click a shape on the map to show it again.");
		 }
		 if (show == false) {
			 warnedUserHowToGetNavAgain = true; //do not warn again
			 nav.style.display='none';
		 }
	 }
}

////////////////////////////////////////////////////////////
// load main page
////////////////////////////////////////////////////////////

function prepareSVG() {
  // svg_init(); // initializes SVG document so that it is "click fillable"
  interpParams(); //interpret parameters passed to page
  //add css:
  prepDragEls(); //make elements draggable

}

     ////////////////////////////////////////////////////
       // Helper functions
       ////////////////////////////////////////////////////

   /* from vegbank.org: /*
   /**
 * Incredibly useful function.
 */
function getURLParam(strParamName) {
	var strReturn = "";
	var strHref = window.location.href;
	if (strHref.indexOf("?") > -1) {
		var strQueryString = strHref.substr(strHref.indexOf("?") + 1); //added +1 MTL
		var aQueryString = strQueryString.split("&");
		for ( var iParam = 0; iParam < aQueryString.length; iParam++ ) {
			if (aQueryString[iParam].indexOf(strParamName + "=") == 0  ) { //made == 0 instead of > -1 MTL
				var aParam = aQueryString[iParam].split("=");
				strReturn = aParam[1];
				break;
			}
		}
	}
	return strReturn;
}

function getURLParamNew(strParamName) {  //rewritten 27-APR-2010 MTL - 2x as fast and more reliable
		var strReturn = "";
		var strHref = window.location.href;
		if (strHref.indexOf("?") > -1) {
			var strQueryString = "&" + strHref.substr(strHref.indexOf("?") + 1) + '&';
			//alert('q' + strQueryString);
			//is param here, if so, there will a &paramName= in the string:
			var intParamStart = strQueryString.indexOf("&" + strParamName + "=");
			//alert('iPS' + intParamStart);
			if (intParamStart > -1 ) {
			  //get from = to the next &
			  var strIntermed = strQueryString.substr(intParamStart + strParamName.length + 2);
			//  alert('int' + strIntermed);
			  strReturn = strIntermed.substr(0,strIntermed.indexOf('&'));
			}
			//var aQueryString = strQueryString.split("&");
			//for ( var iParam = 0; iParam < aQueryString.length; iParam++ ) {
			//    if (aQueryString[iParam].indexOf(strParamName + "=") > -1  ) {
			//        var aParam = aQueryString[iParam].split("=");
			//        strReturn = aParam[1];
			//        break;
			//    }
			//}
		}
		return strReturn;
}


 function maxColorsIsNow(howMany) {
	  if (howMany > 0) {
		  for (var i=1;i<=20;i++) {
			 showhidebyid("colorLegend" + i,(howMany >= i));
		  }
	  }
 }

 function getMaxColors() {
   //gets the number of colors to use that the user selected:
	   try {
		 var colNum = 20;
		 colNum = document.getElementById("maxcolorcount").value;
		 return colNum;
	   } catch (e) {
		 return 20; //default
	   }
 }

  function setElClassById(elId,classNum) {
  //sets element class:
  // alert("trying ID" + elId + " to class " + classNum);
  if (elId.length >0 ) {
	 try {
		 var SD2 = getSD();
		 if (elId == "n-us-nb") {
			 elId = "n-us-ne"; //Nebraska change!
		 }
		 var elToUse = SD2.getElementById(elId);
		 if (!elToUse && elId.length == 2) {
	 	   //try prepending "n-us-" and "n-cn-" for us and canada
	 	   elToUse = SD2.getElementById("n-us-" + elId);
	 	   if (!elToUse) {
			 elToUse = SD2.getElementById("n-cn-" + elId);
		   }
	     }
		 elToUse.setAttribute("class","img" + classNum);
		 saveParam(elId,classNum);
		 } catch(e) {
		 var err = document.getElementById('err');
		 err.style.display = 'block';
		 err.value = err.value + ' ' + elId;
	 }
	 }
  }

function getSD() {
	var S=document.getElementById("sv");
	var SD=S.getSVGDocument();
	return SD;
}


 ////////////////////////////////////////////////////////
 // MAP POSITIONING AND SIZE (ZOOM)
 ////////////////////////////////////////////////////////

  function setMapXYZoom(x,y,zoom) {
	 setMapXYShift(x,y);
	 setZoom(zoom);
  }

  function adjustZoom(amt) {
	//changes zoom of map
	var SD2 = getSD();
	//alert(gebid("mapPrecisionMovement").checked);
	if (gebid("mapPrecisionMovement").checked==true) {
			amt = amt / 10;
	}
	var zoom = SD2.getElementById("mainZoom");
	var zoomXform = zoom.getAttribute("transform");
	//parse this:
	var zoomLevel = Number(zoomXform.replace("scale(","").replace(")",""));
	//zoom.setAttribute("transform","scale(" + (zoomLevel + amt) + ")");
	setZoom(zoomLevel + amt);
  }
  function setZoom(zoomLevel) {
	var SD2 = getSD();
	var zoom = SD2.getElementById("mainZoom");
	zoom.setAttribute("transform","scale(" + (zoomLevel) + ")");
	//document.getElementById("zoom").value = zoomLevel;
	saveParamNorm("zoom",zoomLevel);
	//try to fix hatching:
	//alert('attempt fix');
	//SD2.getElementById("hatch00").setAttribute("x",-1);
	//SD2.getElementById("hatch00").setAttribute("x",-1);
  }

  function moveMap(xAmt, yAmt) {
	 //changes center of map by xAmt, yAmt
	var SD2 = getSD();
	//check for more precision:
	if (gebid("mapPrecisionMovement").value=="on") {
		xAmt = xAmt / 10;
		yAmt = yAmt / 10;
	}
	var shiftEl = SD2.getElementById("mainXYLoc");
	var shiftXform = shiftEl.getAttribute("transform");
	//parse this:
	var coordPair = shiftXform.replace("translate(","").replace(")","").replace(" ","");
	//alert(coordPair);
	var coords = coordPair.split(",");

	var x = Number(coords[0]);
	var y = Number(coords[1]);
	if (isNaN(y)) {
	  y = 0;
	}
	if (isNaN(x)) {
	  x = 0 ;
	}
	// shiftEl.setAttribute("transform","translate(" + (x + xAmt) + "," + (y + yAmt) + ")");
	setMapXYShift(x + xAmt, y + yAmt);
  }
  function setMapXYShift(x,y) {
	 var SD2 = getSD();
	var shiftEl = SD2.getElementById("mainXYLoc");
	 shiftEl.setAttribute("transform","translate(" + (x) + "," + (y) + ")");
	 //document.getElementById("x").value = x;
	 //document.getElementById("y").value = y;
	 saveParamNorm("x",x);
	 saveParamNorm("y",y);
  }
  function moveMapByPicklist(xyzoom) {
	if (xyzoom.length > 0) {
	  var allVars = xyzoom.split(",");
	  setMapXYZoom(allVars[0],allVars[1],allVars[2]);
	}
  }
  var clickCounter = 0;
  function updateClickCounter(adj) {
	if (adj < -10) {
	  clickCounter = 0;
	} else {
	  clickCounter = clickCounter + adj;
	}
  }
  function confirmLeave() {
	//alert('confirm leave!' + clickCounter);
	if (clickCounter > 5) {
	  return false;
	  //if (confirm("Do you really want to leave this page and lose your edits to the map?")) {
		//fine, bye!
	  //} else {
		//get link location
	  //  return false;
		//buildMapLink();
		//set page location here:
		//window.location = document.getElementById("tryLink").getAttribute("href");
	  //}
	}
  }

/////////////////////////////////////////
// drag elements
// from http://www.webreference.com/programming/javascript/mk/column2/
/////////////////////////////////////////
document.onmousemove = mouseMove;
document.onmouseup   = mouseUp;

var dragObjectFull  = null; //the thing that moves
var dragObjectHandle = null; //the thing you actually drag (could be only part of it)
var mouseOffset = null;

function getMouseOffset(target, ev){
	ev = ev || window.event;

	var docPos    = getPosition(target);
	var mousePos  = mouseCoords(ev);
	return {x:mousePos.x - docPos.x, y:mousePos.y - docPos.y};
}

function getPosition(e){
	var left = 0;
	var top  = 0;

	while (e.offsetParent){
		left += e.offsetLeft;
		top  += e.offsetTop;
		e     = e.offsetParent;
	}

	left += e.offsetLeft;
	top  += e.offsetTop;

	return {x:left, y:top};
}

function mouseMove(ev){
	ev           = ev || window.event;
	var mousePos = mouseCoords(ev);

	if(dragObjectFull){
		dragObjectFull.style.position = 'absolute';
		dragObjectFull.style.top      = mousePos.y - mouseOffset.y;
		dragObjectFull.style.left     = mousePos.x - mouseOffset.x;


		return false;
	}
}
function mouseUp(){
	dragObjectHandle = null;
	dragObjectFull = null;
}

function mouseCoords(ev){
	if(ev.pageX || ev.pageY){
		return {x:ev.pageX, y:ev.pageY};
	}
	return {
		x:ev.clientX + document.body.scrollLeft - document.body.clientLeft,
		y:ev.clientY + document.body.scrollTop  - document.body.clientTop
	};
}

	function makeDraggable(item){
	if(!item) return;
	item.onmousedown = function(ev){
		dragObjectFull  = this;
		dragObjectHandle= this;
		mouseOffset = getMouseOffset(this, ev);
		return false;
	}
}

function makeParentDraggable(item){
			if(!item) return;
			item.onmousedown = function(ev){
				dragObjectFull  = this.parentNode;
				dragObjectHandle= this;
				mouseOffset = getMouseOffset(this, ev);
				return false;
			}
}

function makeUnDraggable(item){
			if(!item) return;
			item.onmousedown = function(ev){
				dragObjectFull  = null;
				dragObjectHandle  = null;
				mouseOffset = null;
				return false;
			}
}

  function prepDragEls() {
		 var divs = document.getElementsByTagName("DIV");
		 for (var i=0;i<divs.length;i++) {
		   var divClass = divs[i].getAttribute("class");
		   if (divClass != null) {
			   if (divClass.indexOf("draggableParent")!= -1) {
				 makeParentDraggable(divs[i]);
			   //} else if (divClass.indexOf("nodrag")!= -1) {
				// makeUnDraggable(divs[i]);
			   } else if (divClass.indexOf("draggable")!= -1) {
					makeDraggable(divs[i]);
			   }
		   }
		 }
 }


function loadState(state) {
  //get state g:
  var SD2 = getSD();
  var sg = SD2.getElementById(state.toLowerCase());
  var cos = sg.getElementsByTagName("path");
  //alert(cos.length + " counties found");
  var currCoArr = [];
  for (var i=0;i<cos.length;i++) {
	var currCo = cos[i].getAttribute("id");

	//if (i==0) { alert('first co is ' + currCo);}
	if (currCo != null ) {
	  if (currCo.indexOf("_")==2) {
	  currCoArr.push("" + currCo + "");
	} }  // end ok path
  } //end loop
  //alert(newOpts);
  //sort array:
  //alert(currCoArr.length + ' items in array');
  currCoArr.sort();
  //build list:
  var newOpts = "";
  for (i=0;i<currCoArr.length;i++) {
	currCo = currCoArr[i];
	newOpts = newOpts + "<option value='" + currCo + "'>" + currCo.substr(3) + "</option>";
  }
  document.getElementById("selectByName").value = "";
  document.getElementById("selectByName").innerHTML =  newOpts;
}

//////////////////////////////
// show current element
//////////////////////////////
function showid(el) {
  if (devMode == true) {//dont do this unless in development mode
	  document.getElementById('currentcountyonly').innerHTML = el.getAttribute("id");
	  try {
		  el.setAttribute("class","state_lines2");
	  } catch(e) {
		  //ignore
	  }
	  }
}

function showcountyname(el) {
  document.getElementById('currentcountyonly').innerHTML = el.getAttribute("title");
}

 ////////////////////////////////
 // shorten URL
 ////////////////////////////////
  function sniploc() {
	 var d=document;
	 var w=window;
	 var f='http://snipurl.com/site/snip';
	 var l=d.location;
	 var targURL = document.getElementById("textLink").value;
	 var e=encodeURIComponent;
	 var p='?link='+e(targURL) +'&title='+e(d.title);
	 u=f+p;
	 try{if(!/^(.*\.)?(snipurl|snurl|snipr)[^.]*$/.test(l.host))throw(0);
	 }catch(z){
	   a =function(){
		 if(!w.open(u,'t','height=550,width=625,title=no,location=no,modal=yes,dependent=yes,status=1,scrollbars=yes,menubars=no,toolbars=no,resizable=yes'))l.href=u;
	 };
	 if(/Firefox/.test(navigator.userAgent))setTimeout(a,0);
	 else a();
	 }
   }


/////////////////////////////////////
// debugging ////////////////////////
/////////////////////////////////////

var debug = null;
var debugType = null;
function dlog(message) {
   //attempts to log messages to div id=degbuglog
   // if not there, it attempt to add it to the doc
   // if that fails, then you get alert messages
   // this does nothing unless parameter: debug=log or debug=alert
   // does the user want to debug?

   if (debug == null) {
	  var getd = getURLParam("debug");
	  if (getd.toLowerCase() == "log" || getd.toLowerCase() == "alert" ) {
		debug = true;
		debugType = getd.toLowerCase();
	  }  //log or alert
   } // initial debug is null ?
   if (debug == true) {
	   // do some debugging!
	   if (debugType == "log") {
		   var dlog = document.getElementById('debuglog');
		   if (dlog == null) {
			   //append this to document:
				alert('attempt create debuglog div');
				var divs = document.getElementsByTagName("DIV");
				dlog = document.createElement('div');
				dlog.id = 'debuglog';
				divs[0].appendChild(dlog);
				//dlog = y.appendChild(document.createElement('span'));
		   }
		   if (dlog == null) {
			   debugType ="alert";
			   alert('failed to create node');
		   } else {
			  // append message to end of log
			  //alert("found log!");
			  dlog.innerHTML = dlog.innerHTML + "<br/>" + message;
			  dlog.style.display='block';
		   }

	   }  //end log or log attempt
	   if (debugType=="alert") { //alert, easy debug
		  debug = confirm(message);
	   } //alert or log
   } //there is some debugging!
} //end dlog function






//////////////////////////////////////
// caption ///////////////////////////
//////////////////////////////////////

function saveCaption(blnClose) {
 var newCap = document.getElementById('pagecaption-input').value;
 //replace line breaks with <br/>
 newCap = newCap.replace(/[\r\n]/g, "<br/>")
 document.getElementById('pagecaption-display').innerHTML = newCap;
// saveParam(Url.encode(newCap),'cap');
 showPageCaptionCont();
 if (blnClose == true) {
   //  document.getElementById('pagecaption-display').style.display = "block";
   document.getElementById('pagecaption-edit').style.display = "none";
 }

}

function showPageCaptionCont() {
  var pagecont =  document.getElementById('pagecaption-container');
  pagecont.style.display = "block";
  pagecont.style.opacity = 1;

}

function editCaption() {
 editASpan(gebid('pagecaption-display'));
 //document.getElementById('pagecaption-input').value= document.getElementById('pagecaption-display').innerHTML;
 gebid('pagecaption').style.display="block";
 showPageCaptionCont();
 gebid('colorLegendDiv').style.display="block";
 //document.getElementById('pagecaption-edit').style.display = "block";
}

function closeCaptionBox() {
   document.getElementById('pagecaption').style.display="none";
}

function cancelEditCaption() {
 document.getElementById('pagecaption-input').value= document.getElementById('pagecaption-display').innerHTML;
 showPageCaptionCont();
 document.getElementById('pagecaption-edit').style.display = "none";
}

function hideCaption() {
 hide(gebid('pagecaption-container'));
 hide(gebid('pagecaption'));
 hide(gebid('colorLegendDiv'));
}

function showColorLegend() {
	show(gebid('colorLegendDiv'));
}


function captionEditFont(fontName) {
  if (fontName.length != 0) {
  document.getElementById('pagecaption-display').style.fontFamily=fontName;

 // saveParam(Url.encode(fontName),'capfont');

}
}
function captionEditFontSize(size) {
  if (size.length !=0) {
	  if (size <=0) {
		  var newSize = prompt("What size font (in points) would you like to use?","14");
		  if (isNaN(newSize)) {
			  return false;
		  } else {
			  size=newSize;
		  }

	  }
  document.getElementById('pagecaption-display').style.fontSize = size + 'pt';
 // saveParam(size,'capfontsize');
 }
}


/**
*
*  URL encode / decode
*  http://www.webtoolkit.info/
*
**/

var Url = {

// public method for url encoding
encode : function (string) {
	return escape(this._utf8_encode(string));
},

// public method for url decoding
decode : function (string) {
	return this._utf8_decode(unescape(string));
},

// private method for UTF-8 encoding
_utf8_encode : function (string) {
	string = string.replace(/\r\n/g,"\n");
	var utftext = "";

	for (var n = 0; n < string.length; n++) {

		var c = string.charCodeAt(n);

		if (c < 128) {
			utftext += String.fromCharCode(c);
		}
		else if((c > 127) && (c < 2048)) {
			utftext += String.fromCharCode((c >> 6) | 192);
			utftext += String.fromCharCode((c & 63) | 128);
		}
		else {
			utftext += String.fromCharCode((c >> 12) | 224);
			utftext += String.fromCharCode(((c >> 6) & 63) | 128);
			utftext += String.fromCharCode((c & 63) | 128);
		}

	}

	return utftext;
},

// private method for UTF-8 decoding
_utf8_decode : function (utftext) {
	var string = "";
	var i = 0;
	var c = c1 = c2 = 0;

	while ( i < utftext.length ) {

		c = utftext.charCodeAt(i);

		if (c < 128) {
			string += String.fromCharCode(c);
			i++;
		}
		else if((c > 191) && (c < 224)) {
			c2 = utftext.charCodeAt(i+1);
			string += String.fromCharCode(((c & 31) << 6) | (c2 & 63));
			i += 2;
		}
		else {
			c2 = utftext.charCodeAt(i+1);
			c3 = utftext.charCodeAt(i+2);
			string += String.fromCharCode(((c & 15) << 12) | ((c2 & 63) << 6) | (c3 & 63));
			i += 3;
		}

	}

	return string;
}

}



      ///////////////////////////////////////////////
      // double-click editable stuff:
      ///////////////////////////////////////////////

 function getElsByClass(node,classname) {
	 if(!node) node = document.getElementsByTagName("body")[0];
	 var a = [];
	 var re = new RegExp('\\b' + classname + '\\b');
	 var els = node.getElementsByTagName("*");
	 for(var i=0,j=els.length; i<j; i++)
	 if(re.test(' ' + els[i].className + ' '))a.push(els[i]);
	 //alert('returning ' + a.length + ' items of class ' + classname);
	 return a;

      }


 function encodeTypingToHTML(strText) {
	 return strText.replace(/[\r\n]/g, "<br/>")
 }
 function decodeHTMLToText(strHTML) {
	 var strTxt = strHTML.replace(/<br\/>/gi, String.fromCharCode(10));
	 strTxt = strTxt.replace(/<br>/gi,String.fromCharCode(10));
	return strTxt;
 }

function finishTextArea(save,ta,hideEl) {
   //reverses editASpan
  // alert('finishTextArea init!' + save);
   var strHTML = ta.value;
   strHTML = encodeTypingToHTML(strHTML);
   var wrapNode = ta.parentNode.parentNode;
   var targEl = wrapNode; //getElsByClass(wrapNode,"autoeditable_content")[0];
 //  alert(targEl.innerHTML);

   if (save==true) {
	 targEl.innerHTML = strHTML;
   } else {
	 //recover text:
	 var origHTML = getElsByClass(wrapNode,"autoeditable_original")[0].innerHTML;
	 targEl.innerHTML = origHTML;
   }

   if (hideEl==true) {
	 hide(targEl);
   } else {
	 show(targEl);
   }
  // var removeEdit = getElsByClass(targEl,"autoeditable_edit")[0];
  // alert (removeEdit.innerHTML);
  // removeEdit = "<!-- goner -->";
  // removeEdit.innerHTML = "";
  // removeEdit.className= "";
  // var x= wrapNode.removeChild(wrapNode.childNodes[2]);
   //alert('done');
   //var eventAdded = addEvent(ta.parentNode,"click","editASpan(this)");
   //ta.parentNode.onclick="editASpan(this)";
   //ta.parentNode.innerHTML = strHTML;
 }



 function saveButton(btn) {
   //save sibling textarea:
   finishTextArea(true,btn.parentNode.getElementsByTagName("TEXTAREA")[0]);
 }
 function cancelButton(btn) {
   //cancel sibling textarea:
   finishTextArea(false,btn.parentNode.getElementsByTagName("TEXTAREA")[0]);
 }
 function hideButton(btn) {
   finishTextArea(false,btn.parentNode.getElementsByTagName("TEXTAREA")[0],true);
 }

 function editASpan(el, allowHide) {
   //this edits a span item, using onblur to save it automatically.
   //get span innerHTML:
  // var el = this;
  // alert(el.innerHTML);
  if  ((el.innerHTML.indexOf("autoeditable_edit") == -1)
	&& (el.innerHTML.indexOf("autoeditable_editta") == -1)
	&& (el.innerHTML.indexOf("autoeditable_original") == -1)) {  //only edit something that isn't already being edited!

	   var strSpanCont = el.innerHTML;
	   strSpanCont = decodeHTMLToText(strSpanCont);
	   //replace instructions to double-click:
	   strSpanCont = strSpanCont.replace(/\(?double-click to edit\)?/gi,"");
	  // alert('editASpan init!' + strSpanCont);
	   //sub for textarea
	   // alert(el.getAttribute("ID"));
	   el.onclick = "";
	   var newEl = document.createElement("span");
	   newEl.className = "autoeditable_edit";
	   //newEl.setAttribute("class","autoeditable_edit");
	   newEl.innerHTML = "<textarea class='autoeditable_editta'>" + strSpanCont + "</textarea>" +
		 '<br/><input type="button" value="save" onclick="saveButton(this);" />' +
		' <input type="button" value="cancel" onclick="cancelButton(this);" />';
	   if (allowHide==true) {
		   newEl.innerHTML = newEl.innerHTML + " <input type='button' value='hide' onclick='hideButton(this);' />";
	   }
	   var oldEl = document.createElement("span");
	   oldEl.className = "autoeditable_original";
	   oldEl.innerHTML = strSpanCont;
	   el.innerHTML = ""; //clear
	   el.appendChild(oldEl);
	   el.appendChild(newEl);
	   hide(oldEl);
	}
 }
 function gebid(id) {
	//get element by Id alias
  return document.getElementById(id);
}
 function showhidebyid(id,blnShow) {
   var elem = gebid(id);
   if (!elem) {
	  dlog(id + "cannot be found!");
   }
   if (blnShow==true) {
	 show(elem);
   } else {
	 hide(elem);
   }
 }

function show(elem) { elem.style.display = 'block';}
function hide(elem) { elem.style.display = 'none'; }

