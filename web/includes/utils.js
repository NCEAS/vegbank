/*
var loadFrom = false;
function include(aFile)
{
    if (loadFrom && loadFrom.parent) {
        loadFrom.parent.removeChild(loadFrom);
    }
    loadFrom=document.createElement("script");
    loadFrom.setAttribute("src",aFile);
    document.getElementsByTagName('head').item(0).appendChild(loadFrom);
}
*/
var hIncludes = null;
function include(sURI)
{
  if (document.getElementsByTagName)
  {
    if (!hIncludes)
    {
      hIncludes = {};
      var cScripts = document.getElementsByTagName("script");
      for (var i=0,len=cScripts.length; i < len; i++)
        if (cScripts[i].src) hIncludes[cScripts[i].src] = true;
    }
    if (!hIncludes[sURI])
    {
      var oNew = createElement("script");
      oNew.type = "text/javascript";
      oNew.src = sURI;
      hIncludes[sURI]=true;
      document.getElementsByTagName("head")[0].appendChild(oNew);
    }
  }
}
var CON_WEBSITE_MAIN_CONTENT_DIV_ID = "centercontent"; //the main content page of the website (all pages)
var browser_agent;
var browser_major;
var browser_ie;
var browser_ie3;
var browser_ie4;
var browser_iemac;
var browser_safari;
var browser_mozilla;
var browser_gecko;
var browser_firefox;

function detectBrowser() {
    browser_agent=navigator.userAgent.toLowerCase();

    // Browser Detection stuff
    browser_major = parseInt(navigator.appVersion);
    browser_ie     = ((browser_agent.indexOf("msie") != -1) && (browser_agent.indexOf("opera") == -1));
    browser_ie3    = (browser_ie && (browser_major < 4));
    browser_ie4    = (browser_ie && (browser_major == 4) && (browser_agent.indexOf("msie 4")!=-1) );
	browser_iemac  = (browser_ie && (browser_agent.indexOf("mac")!=-1));
	browser_safari  = (!browser_ie && (browser_agent.indexOf("safari")!=-1));
    browser_mozilla = (!browser_ie && (browser_agent.indexOf("mozilla")!=-1));
    browser_gecko   = (!browser_ie && (browser_agent.indexOf("gecko")!=-1));
    browser_firefox = (!browser_ie && (browser_agent.indexOf("firefox")!=-1));    
}
detectBrowser();

function toggle(e) {
	if (e.checked) {
		highlight(e);
	} else {
		unhighlight(e);
	}
}

function checkAll(checkboxName) {
	var form = document.theform;
	var len = form.elements.length;
	for (var i = 0; i < len; i++) {
		var e = form.elements[i];
		if (e.name == checkboxName) {
			check(e);
		}
	}
}

function clearAll(checkboxName) {
	var form = document.theform;
	var len = form.elements.length;
	for (var i = 0; i < len; i++) {
		var e = form.elements[i];
		if (e.name == checkboxName) {
			clear(e);
		}
	}
}

function highlight(e) {
	var r = null;
	if (e.parentNode && e.parentNode.parentNode) {
		r = e.parentNode.parentNode;
	}
	else if (e.parentElement && e.parentElement.parentElement) {
		r = e.parentElement.parentElement;
	}
	if (r) {
		if (r.className == "listRowA") {
			r.className = "listRowA-Hi";
		} else if (r.className == "listRowB") {
			r.className = "listRowB-Hi";
		}
	}
}

function unhighlight(e) {
	var r = null;
	if (e.parentNode && e.parentNode.parentNode) {
		r = e.parentNode.parentNode;
	}
	else if (e.parentElement && e.parentElement.parentElement) {
		r = e.parentElement.parentElement;
	}
	if (r) {
		if (r.className == "listRowA-Hi") {
			r.className = "listRowA";
		} else if (r.className == "listRowB-Hi") {
			r.className = "listRowB";
		}
	}
}

function check(e) {
	e.checked = true;
	highlight(e);
}

function clear(e) {
	e.checked = false;
	unhighlight(e);
}

function refreshHighlight(checkboxName) {
	var form = document.theform;
	var len = form.elements.length;
	for (var i = 0; i < len; i++) {
		var e = form.elements[i];
		if (e.name == checkboxName && e.checked == true) {
			highlight(e);
		}
	}
}


function MM_jumpMenu(targ,selObj,restore){ //v3.0
  eval(targ+".location='"+selObj.options[selObj.selectedIndex].value+"'");
  if (restore) selObj.selectedIndex=0;
}


function trim(s) {
  //trims a string, use trim_cr if you want carriage returns and line feeds removed (but that's buggy)
  if (s==null || s=="") return "";
  while (s.substring(0,1) == ' ') {
    s = s.substring(1,s.length);
  }
  while (s.substring(s.length-1,s.length) == ' ') {
    s = s.substring(0,s.length-1);
  }
 // while (s.indexOf("  ")!=-1) {
//	  s.replace(/  /g," ");
 // }
  return s;
}
function trim_cr(s) {
  //trims a string, also replaces double spaces with single spaces
  if (s==null || s=="") return "";
  s = s.replace(/\n/g, " ");
  s = trim(s);
  return s;
}

function trim_accode(s) {
	//function trims everything from string except [0-9\.a-zA-Z]
	var ret = "";
	for (i=0;i<s.length;i++) {
      var charCode = s.charCodeAt(i);
     // alert(charCode + " is charCode" + s.substr(i,1));
	  if ( (charCode== 46) || ( (charCode>= 48) && (charCode<= 57) ) || ( (charCode>= 65) && (charCode<= 90) ) || ( (charCode>= 97) && (charCode<= 122) )) {
		  ret += s.substr(i,1);
	  }

	}
	return ret;
}

/************************/
/* MOUSE OVER FUNCTIONS */
/************************/
function MM_preloadImages() { //v3.0
	  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
		      var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
				      if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function MM_swapImgRestore() { //v3.0
	  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_findObj(n, d) { //v4.01
	  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
		      d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
	    if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
		  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
		    if(!x && d.getElementById) x=d.getElementById(n); return x;
}

function MM_swapImage() { //v3.0
	  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
		     if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}


addEvent(window, "load", populateDefinedValueDivs);
function populateDefinedValueDivs() {
 // this populates the defined value divs in a page.
 if (!document.getElementById('definedValuesToPopulateByJs')) return;
 var containerdiv = document.getElementById('definedValuesToPopulateByJs');
 var datadivs = containerdiv.getElementsByTagName("div");
 // loop thru all bits of data
 for (i=0;i<datadivs.length;i++) {
   // if the write div exists
   //debug: alert('trying to set #' + i );
   //debug: alert(' which is ID:' + datadivs[i].id);
     if (document.getElementById('write_' + datadivs[i].id)) {
       //set the write div to the data value
       document.getElementById('write_' + datadivs[i].id).innerHTML = datadivs[i].innerHTML;
     }
 }

}


// addEvent and removeEvent
// cross-browser event handling for IE5+,  NS6 and Mozilla
// By Scott Andrew
function addEvent(elm, evType, fn)
{
    if (evType.substring(0,2) == "on") {
        evType = evType.substring(2);
    }

    if (elm.addEventListener){
        elm.addEventListener(evType, fn, true);
        return true;
    } else if (elm.attachEvent){
        var r = elm.attachEvent("on"+evType, fn);
        return r;
    } else {
        alert("Handler could not be removed");
    }

    return false;
}




function removeEvent(obj, evType, fn, useCapture){
  if (obj.removeEventListener){
    obj.removeEventListener(evType, fn, useCapture);
    return true;
  } else if (obj.detachEvent){
    var r = obj.detachEvent("on"+evType, fn);
    return r;
  } else {
    alert("Handler could not be removed");
  }
  return false;
}

function isTextNotEmpty(txt) {
	// looks @ text and returns true if there is really something there (letter or character or number or _)
    if (txt.toLowerCase().indexOf('1') != -1) return true;
    if (txt.toLowerCase().indexOf('2') != -1) return true;
    if (txt.toLowerCase().indexOf('3') != -1) return true;
    if (txt.toLowerCase().indexOf('4') != -1) return true;
    if (txt.toLowerCase().indexOf('5') != -1) return true;
    if (txt.toLowerCase().indexOf('6') != -1) return true;
    if (txt.toLowerCase().indexOf('7') != -1) return true;
    if (txt.toLowerCase().indexOf('8') != -1) return true;
    if (txt.toLowerCase().indexOf('9') != -1) return true;
    if (txt.toLowerCase().indexOf('0') != -1) return true;
    if (txt.toLowerCase().indexOf('a') != -1) return true;
    if (txt.toLowerCase().indexOf('b') != -1) return true;
    if (txt.toLowerCase().indexOf('c') != -1) return true;
    if (txt.toLowerCase().indexOf('d') != -1) return true;
    if (txt.toLowerCase().indexOf('e') != -1) return true;
    if (txt.toLowerCase().indexOf('f') != -1) return true;
    if (txt.toLowerCase().indexOf('g') != -1) return true;
    if (txt.toLowerCase().indexOf('h') != -1) return true;
    if (txt.toLowerCase().indexOf('i') != -1) return true;
    if (txt.toLowerCase().indexOf('j') != -1) return true;
    if (txt.toLowerCase().indexOf('k') != -1) return true;
    if (txt.toLowerCase().indexOf('l') != -1) return true;
    if (txt.toLowerCase().indexOf('m') != -1) return true;
    if (txt.toLowerCase().indexOf('n') != -1) return true;
    if (txt.toLowerCase().indexOf('o') != -1) return true;
    if (txt.toLowerCase().indexOf('p') != -1) return true;
    if (txt.toLowerCase().indexOf('q') != -1) return true;
    if (txt.toLowerCase().indexOf('r') != -1) return true;
    if (txt.toLowerCase().indexOf('s') != -1) return true;
    if (txt.toLowerCase().indexOf('t') != -1) return true;
    if (txt.toLowerCase().indexOf('u') != -1) return true;
    if (txt.toLowerCase().indexOf('v') != -1) return true;
    if (txt.toLowerCase().indexOf('w') != -1) return true;
    if (txt.toLowerCase().indexOf('x') != -1) return true;
    if (txt.toLowerCase().indexOf('y') != -1) return true;
    if (txt.toLowerCase().indexOf('z') != -1) return true;
    if (txt.toLowerCase().indexOf('_') != -1) return true;
    if (txt.toLowerCase().indexOf('-') != -1) return true;
    if (txt.toLowerCase().indexOf('+') != -1) return true;
    if (txt.toLowerCase().indexOf('!') != -1) return true;
    if (txt.toLowerCase().indexOf('~') != -1) return true;

    return false;
}



/***********************************************************************************************/
/*  the following from http://johnkerry.com/js/kerry.js  on 2004-DEC-03   function showtwo     */
/***********************************************************************************************/


function showorhidediv(theid)
{
	var el = gebid(theid);
	if (el.style.display == 'none') {
		show(el);
	} else {
		hide(el);
	}
}




/****************************************************************************/
/*  the preceding from http://johnkerry.com/js/kerry.js  on 2004-DEC-03     */
/****************************************************************************/


function defaultOnLoad() {
    try {
        // this function must be defined somewhere else
        customOnLoad();
    } catch (e) {
        // forget about it
    }
}


/**
 * Incredibly useful function.
 */
function getURLParam(strParamName) {
    var strReturn = "";
    var strHref = window.location.href.toLowerCase();
    if (strHref.indexOf("?") > -1) {
        var strQueryString = strHref.substr(strHref.indexOf("?")).toLowerCase();
        var aQueryString = strQueryString.split("&");
        for ( var iParam = 0; iParam < aQueryString.length; iParam++ ) {
            if (aQueryString[iParam].indexOf(strParamName.toLowerCase() + "=") > -1 ) {
                var aParam = aQueryString[iParam].split("=");
                strReturn = aParam[1];
                break;
            }
        }
    }
    return strReturn;
}



//
// Initialize the ajax engine
//
function initAjax() {
    var ajax = new XHConn();
    if (!ajax) {
        // this client lacks XMLHttp support
        alert("Sorry, there was a problem using VegBank. " +
                "Please upgrade your browser to take advantage of many features on the Web.");
    }
	return ajax;
}


function formConvertToGetIfURLLengthOK(theformname) {
    //this checks to see what the length of the resulting URL will be if using "GET"
    //if it's longer than 2000 chars, it switches the form to post
    //if it's shorter, it uses GET
   // alert('seeing if I can make this a GET');
    //get the form in question
    var theFormToCheck = document.forms[theformname];
    //loop through all elements
    var numelements = theFormToCheck.elements.length;
    var cumulativeLen = 0;
    for (var i=0 ; i<numelements ; i++) {
      //this will figure out how long the params will be:
         var elementName = theFormToCheck.elements[i].name;
         var elementValue = theFormToCheck.elements[i].value;
          // add length of name and value plus room for &/? and =
          cumulativeLen = cumulativeLen + 1 + elementName.length + 1 + elementValue.length;
      }
      //now add action length, too
      cumulativeLen = cumulativeLen + theFormToCheck.action.length;
  //  alert(theformname + " will have parameters length of " + cumulativeLen + " when submitted");  
  if (cumulativeLen > 2000 ) {
      // has to be a post
      theFormToCheck.method = "POST";
  } else {
      //can be a get, we prefer that
      theFormToCheck.method = "GET";
  }
    
}

//
// Use this to get the items in a list
//
function getValuesFromList(thelist, getValueOrText) {
  //the list is the form object that is a <select>
  // getValueOrText is either "value" or "text"
  //  -- if "value" then the values are listed (i.e. <option value="THIS">not this</option> )
  //  -- if "text"  then the text shown are listed (i.e. <option value="not this">BUT THIS</option> )
  listvalues = "";
  hasvalues = "false"; /*default*/
  strSeparator = ", ";
  for (var i=0;i<thelist.length;i++) {
    if (thelist.options[i].selected==true) {
      hasvalues = "true"; /* has some values, make sure to return something */
      if ( getValueOrText == "value" ) {
        listvalues = listvalues + strSeparator + thelist.options[i].value ;
      }
      if ( getValueOrText == "text" ) {
        if ( thelist.options[i].text != "--ANY--" )
          {
            listvalues = listvalues + strSeparator + thelist.options[i].text ;
          }
      }

    }
  }
  if ( hasvalues == "true" ) {
    /* window.alert("value is:>" + listvalues.substring(strSeparator.length) + "<"); */
    return(listvalues.substring(strSeparator.length));  /* remove initial strSeparator if there */
  } else {
      /* return nothing, but should edit list to selected something with value = "" .  This just helps the xwhere_query.  */
        fixednull = "false";
        for (var j=0;j<thelist.length;j++) {
          if (thelist.options[j].value=="" && fixednull == "false" ) {
            thelist.options[j].selected = true;
            fixednull = "true" ;
          }
        }

      return("");

  }
}

function getValuesFromList_ifexists(theformname,thelistname,getValueOrText) {
    //this function just sets value to "" if list doesn't exist
    varWhatToReturn = "";
    try {
       varWhatToReturn =  getValuesFromList(document.forms[theformname].elements[thelistname],getValueOrText);
    } catch (e) {
        //set to ""
        // alert('Failed to find list:' + thelistname);
        varWhatToReturn = "";
    }
    return varWhatToReturn;
}

function getValuesFromFIELD_ifexists(theformname,thefieldname) {
    //this function just sets value to "" if list doesn't exist
    varWhatToReturn = "";
    try {
       varWhatToReturn =  document.forms[theformname].elements[thefieldname].value;
    } catch (e) {
        //set to ""
        // alert('Failed to find FIELD: ' + thefieldname );
        varWhatToReturn = "";
    }
    return varWhatToReturn;
}

function getValuesFromCheckBox_ifexists(theformname,thefieldname) {
    //this function just sets value to "" if list doesn't exist
    varWhatToReturn = "";
    try {
       varWhatToReturn =  document.forms[theformname].elements[thelistname].checked;
    } catch (e) {
        //set to ""
        varWhatToReturn = false;
    }
    return varWhatToReturn;
}


/** * @(#)isNumeric.js * * Copyright (c) 2000 by Sundar Dorai-Raj
  * * @author Sundar Dorai-Raj
  * * Email: sdoraira@vt.edu
  * * This program is free software; you can redistribute it and/or
  * * modify it under the terms of the GNU General Public License
  * * as published by the Free Software Foundation; either version 2
  * * of the License, or (at your option) any later version,
  * * provided that any use properly credits the author.
  * * This program is distributed in the hope that it will be useful,
  * * but WITHOUT ANY WARRANTY; without even the implied warranty of
  * * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
  * * GNU General Public License for more details at http://www.gnu.org * * */

  var numbers=".0123456789";
  function isNumeric(y) {
    // is x a String or a character?
     var x = y+"";
    if(x.length>1) {
      // remove negative sign
      x=Math.abs(x)+"";
      for(j=0;j<x.length;j++) {
        // call isNumeric recursively for each character
        number=isNumeric(x.substring(j,j+1));
        if(!number) return number;
      }
      return number;
    }
    else {
      // if x is number return true
      if(numbers.indexOf(x)>=0) return true;
      return false;
    }
  }






  function VB_isDate(y) {
      // function written by Michael Lee 2005-05-07 (and that IS a valid date)
      // is this a valid date?
     blnFine=false;
     var x = y+"";
       var mytool_array=x.split("/");

       if ( mytool_array.length == 3 )
       {
         if  ( isNumeric(mytool_array[0]) && isNumeric(mytool_array[1]) && isNumeric(mytool_array[2]) )
         {

           if ( mytool_array[0]>0 && mytool_array[0]<13 &&  mytool_array[1]>0 && mytool_array[1]<32  &&  mytool_array[2]>0 && mytool_array[2]<10000) {
           // assume ok
           blnFine = true;
                if ( mytool_array[1]==31 && ( mytool_array[0]==6 || mytool_array[0]==4 ||mytool_array[0]==9 ||mytool_array[0]==11  ) ) {
                // not ok for 31 in these months
                blnFine = false;
                }
                if (mytool_array[1]>29 && mytool_array[0]==2) {
                //not ok in Feb
                blnFine = false;
                }
                if (mytool_array[1]==29 && ( (mytool_array[2])/4 != Math.floor(mytool_array[2]/4) ) ) {
                   blnFine = false ; //not leap year
                }

           }


         }

       }
    return blnFine;
  }


//******** JAVASCRIPT  FOR  FORM  VALIDATION ***********************//


function validateThisForm(thisform) {
       /* This function validates this form as best it can, for now that means checking to see
       if numeric fields are numeric (based on className=number) and dates are in right format, too */
              blnIsValid = true;
              var numelements = thisform.elements.length;
              var item;
              for (var i=0 ; i<numelements ; i++) {
                  item = thisform.elements[i];
                  if ( (item.className == "number" || item.className == "errNumber" ) && blnIsValid ) {
                  // should be number and only check until there is one error
                        // check to see if numeric:
                        if( isNumeric( item.value) || item.value == null || item.value == "" ) {
                            //is ok if numeric, null or empty string
                            item.className = "number";
                          }
                          else
                          {
                        item.className = "errNumber" ;
                        item.focus();
                        alert("You entered an invalid number: " + item.value + " Please fix this and try again.");

                        blnIsValid = false;
                        }
                        
                         //check min and max, if both exist:
						      if ( item.value == null || item.value == "")  {
							     //ignore it for now	  
							  } else {
								//not null, see if it's a min field   
						        // format: xwhereParams_min[fieldname]_0 and xwhereParams_max[fieldname]_0
						        var itemName = item.name;
						        if (itemName.substring(0,16) == "xwhereParams_min" && itemName.substring(itemName.length-2)=="_0" ) {
								  //see if max field exists and is populated
								  try {
                                      var maxValue = getValuesFromFIELD_ifexists(thisform.name,itemName.substring(0,13) + "max" + itemName.substring(16));
									  //if it got here, compare the vlaue and make sure max is > min
                                      if ( (maxValue != "") && (maxValue != null) && Number(maxValue) < Number(item.value)) {
										  item.className = "errNumber";
										  item.focus();
										  alert("You entered a minimum value (" + item.value + ") that is greater than the maximum ("
										       + maxValue + ")  Please fix this and try again.");
										       blnIsValid = false;
									  }
								  } catch (e) {
								  	 // alert(e);
									  //forget about it and keep going
								  }
							    } //end of is a min field
				              } //end of item populated or not
                        
                  } //end of numeric check
                  //check dates:
                  if ( (item.className == "date" || item.className == "errDate" ) && blnIsValid ) {
                         // should be number and only check until there is one error
                            // check to see if date:
                            if( VB_isDate(item.value) || item.value == null || item.value == "" ) {
                                //is ok if numeric, null or empty string
                                item.className = "date";
                              }
                              else
                              {
                            item.className = "errDate" ;
                            item.focus();
                            alert("You entered an invalid date: " + item.value + " Please format like MM/DD/YYYY, example: 06/30/1977.");

                            blnIsValid = false;
                            }
                  }
                 
              }
        return blnIsValid;
}


function delay(ms) {
    var d = new Date(), mill, diff;
    while (1) {
        mill = new Date();
        diff = mill - d;
        if(diff > ms) { break; }
    }
}

function getParamFromResubmitForm(theParam) {
	// function gets param value from resubmitForm, returns "" if nothing found
	try {
    var theResubmitForm;
	  theResubmitForm = document.forms.resubmitForm ;
  //  alert (theResubmitForm.elements.length);
    var theReturnValue;

    var i;
    i=0 ;
    while ( theResubmitForm.elements[i].name != theParam && i < theResubmitForm.elements.length -1)
    {
     i++;
    }
  //  alert ("ok to here" + i);
    theReturnValue  = theResubmitForm.elements[i].value;
	  return theReturnValue;
	}
	catch (e) {
	    return "";
    }
}




//this for putting a value from a popup window back into the opener -- depends on params requestingForm and requestingField
function setOpenerFormValue(theValueToSet) {
  // this sets the value of the opener form field, based on requestingForm att and requestingField att.
  var tempname;
  tempname=  getParamFromResubmitForm("requestingForm")
 // alert(tempname);
  var formObject;
  try {
    formObject = opener.document.forms[tempname];
    // alert ("form : " + formObject.name);
    tempname= getParamFromResubmitForm("requestingField");
    // alert ("form field: " + tempname);
    formObject.elements[tempname].value = theValueToSet;
    window.close();

  }
  catch (e) {
    alert("There was an error trying to fill in the Accession Code.  Make sure the original window is still opened.");
  }
}

function resubmitOpeningForm() {
	// resubmits opening form
	try {
		// get opener
		// does not work, loses functions (and form): opener.document.write("Refreshing data...");
		opener.postNewParam("name","");
		window.close();
	}
	catch (e) {
		//DEBUG:
		//alert(e);
		return "";
	}
    return false;
}

function setupConfig(strView) {
	// opens popup to config this view
	var strURL =  '@views_link@UserPreferences_popup.jsp';
	if (strView != "" )
	  {
		strURL = strURL + '?where=where_cookie_view&wparam='+strView;
      }
    setupWin = window.open(strURL,'vegbank_setup','toolbar=yes, location=yes, menubar=yes, scrollbars=yes, resizable=yes, copyhistory=no, width=400, height=600');
    setupWin.focus();
}


function popupDD(url) {
	DDWin = window.open(url,'vegbank_dd','toolbar=yes, location=yes, menubar=yes, scrollbars=yes, resizable=yes, copyhistory=no, width=400, height=600');
	DDWin.focus();
}

function resubmitWithNewPage(baseurl) {
	//resubmits the current form, but with a different page:
	 resubmitform = document.forms.resubmitForm ;
	 resubmitform.action = baseurl;
     //convert to get if possible
     formConvertToGetIfURLLengthOK("resubmitForm");
     resubmitform.submit();
	 return false;
}

function postNewParam(theName,theVal) {
  // this uses the resubmitForm Form in the page that should be put there by vegbank(colon)resubmitForm
  // to send user to new location, but using the posted instead of URL parameters.
  if ( theVal == null ) {return false; }
  resubmitform = document.forms.resubmitForm ;

  if ( theVal != "" ) {
    var numelements = resubmitform.elements.length;
    var wasDone = "false" ;
      for (var i=0 ; i<numelements ; i++) {
        if (resubmitform.elements[i].name == theName ) {
          resubmitform.elements[i].value = theVal ;
          wasDone = "true" ;
        }
      }

    if ( wasDone == "false") {
      //didnt get done, add new input to form (tricky)
      resubmitform.placeholder.name = theName ;
      resubmitform.placeholder.value = theVal ;
    }
  }
  
  //convert to get if possible
  formConvertToGetIfURLLengthOK("resubmitForm");
  
  //submit form
  resubmitform.submit();
    return false;
}

function tut_close() {
	parent.location=parent.upperframe.location;

}

function tut_togglehighlightMainFrameEl(divToHighlight,pageToRedirectTo) {
 /* this function highlights a section of the other page, and if fails, asks user if ok to redirect to page passed and try again */
    var blnWorked = parent.upperframe.tut_togglehighlightElement(divToHighlight);
    if ( blnWorked == false ) {
      /* ask user if ok to redirect upperframe to correct place */
      var blnRedirect = confirm("The left window is not on the correct page to highlight this item.  Do you want to change the left window to correct page (then you should press highlight again)?");
      if ( blnRedirect ) {
        parent.upperframe.location = pageToRedirectTo ;
        /* does not work, because page may take a while to load: parent.upperframe.tut_togglehighlightElement(divToHighlight); */
      }
    }

}

function tut_unhighlightMainFrame() {
  parent.upperframe.removeClassFromDoc("tut_highlight");
}

function tut_togglehighlightElement(divToHighlight) {
  /* highlights the div but adds tut_highlight to element instead of replacing classname */
  /* unhighlights if already highlighted */
  try {
    theElement = document.getElementById(divToHighlight);
    var currentclass = theElement.className;
    var dohighlight = false;
    if (currentclass.indexOf("tut_highlight")==-1) {
      /* highlight */
      dohighlight = true;
    }

    /* first remove any extant highlighting: */
    tut_unhighlight();
    /* then apply new highlighting , if needs it */
    if ( dohighlight == true ) {
        theElement.className = theElement.className + " tut_highlight" ;
    }

    return true;

  } catch (e) {
    return false;
  }

}

function tut_unhighlight() {
  removeClassFromDoc("tut_highlight");
}

/* following based loosely on http://www.dynamicdrive.com/dynamicindex1/navigate2_dev.htm with notice required to copy: */
/***********************************************
* Contractible Headers script- © Dynamic Drive (www.dynamicdrive.com)
* This notice must stay intact for legal use. Last updated Oct 21st, 2003.
* Visit http://www.dynamicdrive.com/ for full source code
***********************************************/

function removeClassFromDoc(classname) {
  var inc=0
  var alltags=alltags=gebid(tableid).getElementsByTagName(CON_WEBSITE_MAIN_CONTENT_DIV_ID) ; //hard-code this web-sites main content ID
  if ( !alltags ) { 
    //didn't work, try whole document
    alltags=document.all? document.all : document.getElementsByTagName("*") ;
  }
  
  document.all? document.all : document.getElementsByTagName("*")
  for (i=0; i<alltags.length; i++){
    if (alltags[i].className.indexOf(classname)!=-1)
    /* remove the requested className.  Does not set entire class to space string b/c there could be 2 classes embedded. */
    alltags[i].className = alltags[i].className.replace(classname," ");
   }
}

function removeClassFromDocIfAlsoClass(removeclass,ifalsoclass,tableid,addsuffix) {
	// function removes a class from elements, if also another class is present
	// example: remove class "hidden" from all "email" class elements would unhide emails on a page
	// will add suffix to ifalsoclass
	var inc=0;
    var alltags;
      if (tableid == "*") { tableid = CON_WEBSITE_MAIN_CONTENT_DIV_ID }; //hard-code this web-sites main content ID
	  if (tableid != "*") { alltags=gebid(tableid).getElementsByTagName("*") ;}
      //if didn't find it, then get whole page.
      if ( !alltags ) { alltags=document.all? document.all : document.getElementsByTagName("*") ;}
      
      for (i=0; i<alltags.length; i++){
	    if (alltags[i].className.indexOf(ifalsoclass)!=-1)
	    {
			// add the suffix if not there:
			if (alltags[i].className.indexOf(ifalsoclass + addsuffix)==-1) {
		    	alltags[i].className = alltags[i].className.replace(ifalsoclass,ifalsoclass + addsuffix);
		    }
	    // check to see if also another class is there:
	      if (alltags[i].className.indexOf(removeclass)!=-1)
	      {
	        /* remove the requested className.  Does not set entire class to space string b/c there could be 2 classes embedded. */
	        alltags[i].className = alltags[i].className.replace(removeclass," ");
	      }
	    }
   }
}
function addClassToDocIfAlsoClass(addclass,ifclass,tableid,addsuffix) {
	// function removes a class from elements, if also another class is present
	// example: remove class "hidden" from all "email" class elements would unhide emails on a page
	  var inc=0;
      var alltags;
	  if (tableid == "*") { alltags=document.all? document.all : document.getElementsByTagName("*") ;}
	  if (tableid != "*") { alltags=gebid(tableid).getElementsByTagName("*") ;}
	//debugging:  var dontagain = false;
	  for (i=0; i<alltags.length; i++){
	    if (alltags[i].className.indexOf(ifclass)!=-1)
	    {
		// add the suffix if not there:
		if (alltags[i].className.indexOf(ifclass + addsuffix)==-1) {
		  alltags[i].className = alltags[i].className.replace(ifclass,ifclass + addsuffix);
	    }
	    // check to see if already there:
	      if (alltags[i].className.indexOf(addclass)==-1)
	      {
	        /* remove the requested className.  Does not set entire class to space string b/c there could be 2 classes embedded. */
	        alltags[i].className = alltags[i].className + " " + addclass ;
	     //debugging:   if ( !dontagain ) {	        alert("added as class:" + alltags[i].className);  dontagain = true; }
	      }
	    }
   }
}

function showTaxonName(toshow,tableid) {
	//function shows a particular type of taxon name for plots, and hides others.
	if (toshow=="" ) { return false; }
	// first hide all
	var suffixToOverride = "_override_";

	addClassToDocIfAlsoClass("hidden","taxonobservation_authorplantname",tableid,suffixToOverride);
	addClassToDocIfAlsoClass("hidden","taxonobservation_int_origplantscifull",tableid,suffixToOverride);
	addClassToDocIfAlsoClass("hidden","taxonobservation_int_origplantscinamenoauth",tableid,suffixToOverride);
	addClassToDocIfAlsoClass("hidden","taxonobservation_int_origplantcode",tableid,suffixToOverride);
	addClassToDocIfAlsoClass("hidden","taxonobservation_int_origplantcommon",tableid,suffixToOverride);
	addClassToDocIfAlsoClass("hidden","taxonobservation_int_currplantscifull",tableid,suffixToOverride);
	addClassToDocIfAlsoClass("hidden","taxonobservation_int_currplantscinamenoauth",tableid,suffixToOverride);
	addClassToDocIfAlsoClass("hidden","taxonobservation_int_currplantcode",tableid,suffixToOverride);
    addClassToDocIfAlsoClass("hidden","taxonobservation_int_currplantcommon",tableid,suffixToOverride);
	// then show the one
	removeClassFromDocIfAlsoClass ("hidden",toshow,tableid,suffixToOverride);

    // set cookie
    setCookie('taxon_name_full', toshow);
    //alert('setting cookie: taxon_name: ' + gebid("taxonNameSelect").selectedIndex);
    return false;
}

function showDataDictionaryField(blnLabels) {
    //shows or hides labels/names on a page and sets the cookie to remember that
    var suffixToOverride = "_override_";
    if (blnLabels == true) {
        addClassToDocIfAlsoClass("hidden","dba_fielddescription_fieldname","*",suffixToOverride);
        addClassToDocIfAlsoClass("hidden","dba_tabledescription_tablename","*",suffixToOverride);
        removeClassFromDocIfAlsoClass("hidden","dba_tabledescription_tablelabel","*",suffixToOverride);
        removeClassFromDocIfAlsoClass("hidden","dba_fielddescription_fieldlabel","*",suffixToOverride);
        //set cookie
        setCookie('globaldd_showlabels_notnames','show');
    } else {
        removeClassFromDocIfAlsoClass("hidden","dba_fielddescription_fieldname","*",suffixToOverride);
        removeClassFromDocIfAlsoClass("hidden","dba_tabledescription_tablename","*",suffixToOverride);
        addClassToDocIfAlsoClass("hidden","dba_tabledescription_tablelabel","*",suffixToOverride);
        addClassToDocIfAlsoClass("hidden","dba_fielddescription_fieldlabel","*",suffixToOverride);
           //set cookie
        setCookie('globaldd_showlabels_notnames','hide');
        
    }
}

function changeMappingIcon(strColorsNoColors, blnNoRepost) {
  //changes the page by setting the cookie: globalmapping_icons_not_colored    , then reloads page
  // pass either "letters" or "noletters"
  var strShowHide = "";
  if (strColorsNoColors == "letters") {
      strShowHide = "show";  
      } else {
      strShowHide = "hide";
      }
  setCookie('globalmapping_icons_not_colored',strShowHide);
  if ( blnNoRepost == undefined || blnNoRepost == false ) {
    //reset form
    postNewParam("nothing","");
  }
}


function helpNeedsNewPage() {
   /* attempts to determine if help needs to open as new page.  Rules:
    * DOES NOT need to open new page if this is .html file
    * DOES NOT need to open new page if this is .jsp file with parameters on URL
    * does need to if this is .jsp with no params on URL AND there are params on resubmitForm
    * does need to if .do
    *****************************************/
  //get the path name, everything through the filename
   var pathnm = window.location.pathname ;

  //    alert(pathnm) ;
  // get where the last dot is
     var whereLastDot = pathnm.lastIndexOf(".") ;
  //    alert(whereLastDot);
   var extens = "" ;

   if ( whereLastDot != -1 ) {
	   extens = pathnm.substring(whereLastDot,pathnm.length);
 //     alert(extens);
   }

   var getNewWindow = true;
   switch (extens)
   {
   	case ".html":
   		getNewWindow = false;
   	break
   	case ".htm":
   		getNewWindow = false;
   	break
   	case "":
	   		getNewWindow = false; //this is a bit risky, but there are some useful rewrites in place like /browse that dont have periods.
	   	break

   	case ".jsp":
   		// are there variables in URL and in postParams form? If so, then false
   		// are there none in each, also false if so.
   		   var blnHasParamsURL = ( window.location.search.length > 0 );
		 //   alert("window has URL params : " + blnHasParamsURL);

		   var blnHasParamsResbmitForm = ( document.forms.resubmitForm.elements.length > 1) ; //>1 b/c of placeholder.
         //   alert("params is resubmit Form : " + blnHasParamsResbmitForm) ;

        if ( blnHasParamsURL == blnHasParamsResbmitForm) {
		  // if both of above are true or both are false, then it ISNOT a posted form, otherwise it is.
		  getNewWindow = false;
		}
   	break
   	default:
   		getNewWindow = true; //default to new window
    }
    // alert(getNewWindow + " is what is for new window");
   	return getNewWindow;


}

function getHelpForMe() {
	/* This function gets help for the current form.  First it tries function getHelpPageId() and if that fails, uses regular help page */
	try {
	        // this function must be defined somewhere else
	        var strHelpId = getHelpPageId();
	        getHelpFor(strHelpId);
	    } catch (e) {
	        // forget about it
	        // other function not defined, so go to regular tutorial:
	        getHelpFor("");
    }

}

function getHelpFor(helpTopic) {
  /* this function calls for a help topic, and attempts to get this page as the left screen
   * This is only not possible when the current page is a .jsp with posted parameters.
   * In this case, a new window is opened with the example file on the left
   * if nothing passed, gets default help tutorial
   *****************************************************************************/

  if ( helpTopic.length == 0 ) {
	 // default help topic defined here
	 helpTopic = "manual-index-basic" ;

  }
      var needsNew = helpNeedsNewPage();
      //  alert (needsNew + " in getHelpFor " ) ;
      document.forms.getHelpForm.elements.helpPage.value = "@manual_link@" + helpTopic + ".html";
      document.forms.getHelpForm.elements.mainPage.value = window.location;

      if ( needsNew == true ) {
    	  //  alert("set new target for help form");
    	  document.forms.getHelpForm.target = "_new" ;
          document.forms.getHelpForm.elements.mainPage.value = "@forms_link@getHelpNewWindow.jsp?mainPage=" + window.location ;
      }

      document.forms.getHelpForm.submit();

}


/*
createElement function found at http://simon.incutio.com/archive/2003/06/15/javascriptWithXML
*/
function createElement(element) {
    if (typeof document.createElementNS != 'undefined') {
        return document.createElementNS('http://www.w3.org/1999/xhtml', element);
    }
    if (typeof document.createElement != 'undefined') {
        return document.createElement(element);
    }
    return false;
}


function gebid(id) {
    return document.getElementById(id);
}

function getEventElement(theEvent) {
    var elem = null;
    var evt = (theEvent) ? theEvent : ((window.event) ? window.event : null);
    if (evt) {
        // equalize W3C/IE models to get event target reference
        elem = (evt.target) ? evt.target : ((evt.srcElement) ? evt.srcElement : null);
        if (elem.nodeType == 3) { // defeat Safari bug
            elem = elem.parentNode;
        }
    }
    return elem;
}


/* COOKIES ================================================================= */

function getCookieVal (offset) {
    var endstr = document.cookie.indexOf (";", offset);
    if (endstr == -1)
        endstr = document.cookie.length;
    return unescape(document.cookie.substring(offset, endstr));
}

function getCookie (name) {
    var arg = name + "=";
    var alen = arg.length;
    var clen = document.cookie.length;
    var i = 0;
    while (i < clen) {
        var j = i + alen;
        if (document.cookie.substring(i, j) == arg)
            return getCookieVal (j);
        i = document.cookie.indexOf(" ", i) + 1;
        if (i == 0)
            break;
    }
    return null;
}

//
// sets a 30 day cookie by default
// PARAMS: name, value, expires (Date object), path, domain, secure (t/f)
//

function setCookieForField(name,onoff) {
	// just sends to setCookie function, but coverts on to show and off to hide
	// alert(onoff + " is onoff");
	var val = "hide";
	if ( onoff == true ) {
	  val = "show" ;
	}
	//alert("setting " + name + " to : " + val);
	setCookie (name,val) ;
}

function setCookie (name, value) {
    var expDays = 30;
    var expDefault = new Date();
    expDefault.setTime(expDefault.getTime() + (expDays*24*60*60*1000));

    var argv = setCookie.arguments;
    var argc = setCookie.arguments.length;
    var expires = (argc > 2) ? argv[2] : expDefault;
    var path = (argc > 3) ? argv[3] : null;
    var domain = (argc > 4) ? argv[4] : null;
    var secure = (argc > 5) ? argv[5] : false;
    document.cookie = name + "=" + escape (value) +
    ((expires == null) ? "" : ("; expires=" + expires.toGMTString())) +
    ((path == null) ? "" : ("; path=" + path)) +
    ((domain == null) ? "" : ("; domain=" + domain)) +
    ((secure == true) ? "; secure" : "");
}

function DeleteCookie (name) {
    var exp = new Date();
    exp.setTime (exp.getTime() - 1);
    var cval = getCookie (name);
    document.cookie = name + "=" + cval + "; expires=" + exp.toGMTString();
}


//
// Searches siblings of given node with
// given nodeName a maximum of max times.
//
function findSibling(node, nodeName, max) {
    var sibling = node.nextSibling;
    var count = 0;
    while (sibling != null && sibling.nodeName.toLowerCase() != nodeName.toLowerCase() && count < max) {
        sibling = sibling.nextSibling;
        count++;
    }
    if (count == max) {
        return null;
    }
    return sibling;
}

function show(elem) { elem.style.display = 'block'; }
function hide(elem) { elem.style.display = 'none'; }

function showbyid(id) { show(gebid(id)) };
function hidebyid(id) { hide(gebid(id)) };
function showhidebyid(id,toshow) {
    if (toshow==true) {
      showbyid(id);  
    } else {
      hidebyid(id);
    }
}


//
// @param ac: the accession code to verify
// @param fnWhenDone: function(oXML) { stuff }
//
function verifyAC(ac, fnWhenDone) {
	var ajax = initAjax();
    var params = "wparam=" + encodeURIComponent(ac);
    var url = "@ajax_link@verify_ac.ajax.jsp";
    ajax.connect(url, "POST", params, fnWhenDone);
}

// functions associated with highlighting search criteria on a page:
var hop_cookieName = "hop_current_searchcriteria"; 
var hop_ieSwapCookieSuffix = "_ieswap";
    
  function splitWithQuotes(strText) {
    //does a split of a string, but treats things within quotes as one item.
    // returns array of strings
    // REQUIRES THAT SPACES BE BEFORE AND AFTER QUOTES
    // debug:
   // document.getElementById('goo_debug').value = "";
    
    var arrTemp = new Array();
    var arrFinal = new Array();
    arrTemp = strText.split(" ");
    //now we have split based on space, but recombine if one starts with " and later another ends in "
    var blnInQuote = false;
    var strCombine = "";
    for ( var iParam = 0; iParam < arrTemp.length; iParam++ ) {
        var strWord = arrTemp[iParam];
        
        if (blnInQuote == true) {
          // we are already in a quote, see if it ends here
          // in any case, add what is here to new variable
          
          if (strWord.substring(strWord.length -1 ,strWord.length) == '"' ) { //last is quote
            //does end with quote, add it and turn off blnInQuote
           // document.getElementById('goo_debug').value = document.getElementById('goo_debug').value + " ends quote" ;
            blnInQuote = false;
            strCombine = strCombine + ' ' + strWord.substring(0,strWord.length -1);
            arrFinal.push(strCombine);
            strCombine = "" ; // reset
          } else {
            //add to strCombine
            strCombine = strCombine + ' ' + strWord;
          }
        } else {
          // not already in quote
          if (strWord.substring(0,1) == '"' ) { //first is quote
            // starts with a quote
            // does it also end in quote?
            if ((strWord.substring(strWord.length - 1,strWord.length) == '"') && (strWord.length>2 )) { //last is quote and len >2
              //ends in quote, get in-between
              arrFinal.push(strWord.substring(1,strWord.length-1));
            } else  {
              //does not end in quote
              blnInQuote = true;
              strCombine = strWord.substring(1); //take all but quote, get rest later
            }
            
            
          } else { //no quotes, just add
            arrFinal.push(strWord);
          }
        } // in quote or no  
    } //looping thru array
    //check to see if something is still waiting to be added
    if (strCombine.length > 0) { //still have somehting in strCombine
      //add this too
      arrFinal.push(strCombine);
    }
    
    //debug: report:
   // document.getElementById('goo').value = "";
   //  for ( var jParam = 0; jParam < arrFinal.length; jParam++ ) {
   //    document.getElementById('goo').value = document.getElementById('goo').value + "__" + arrFinal[jParam];
   //  }
    
    return arrFinal;
  }
  
  
  function highlightShownTextOnPage(searchFor,elID, blnSplitSearchFor) {
      //highlights a word or words on some elementId (or page if * passed)
      // blnSplitSearchFor is true if the term should be split and separately highlighted
      // case insensitive!
      
      if (searchFor == "") {
        return; //nothing to highlight!
      }

      var arrSearchFor = new Array()
      
      if (blnSplitSearchFor == true) {
        // split-up searchFor, delimiting with spaces
        // keeps quoted phrases together and throws out stuff with - at front
        arrSearchFor = splitWithQuotes(searchFor);   
      } else {
        //just add the original term to the searching
        arrSearchFor.push(searchFor);
      }
      
     // alert('searching for ' + arrSearchFor.length + ' words');
      
      var thisContainer;
        if (elID == "*") { thisContainer=document ;}
        if (elID != "*") { thisContainer=document.getElementById(elID) ;}
           //for (var i=alltags.length - 1; i>=0; i--){
           //  if (i > 100) { break ; }
             //alert(i + ":" + alltags[i].tagName);
             
             //alert(i + ":" + thisContainer.innerHTML);
             //search for text
             var aBitOfText = thisContainer.innerHTML;
             if ( aBitOfText != null ) {
               //loop through array of values
               for ( var jParam = 0; jParam < arrSearchFor.length; jParam++ ) {
                 
                 var newSearchFor = arrSearchFor[jParam];
                 // alert('searching for ' + newSearchFor);
                 if ((aBitOfText.indexOf(newSearchFor)!=-1) && (newSearchFor.length > 0) && (newSearchFor != " ")) {
                   var whereItIs = aBitOfText.indexOf(newSearchFor);
                   searchForRegEx = new RegExp(newSearchFor , "gi");
                   thisContainer.innerHTML = replaceShownText(thisContainer.innerHTML,searchForRegEx,"<span class='hop_highlight'>" + newSearchFor + "</span>");
                 } // searched for item found
                 
               } //loop through array of terms
             } //text is not null 
              
           //}
        //turn on CSS
        // not really needed, enabled by default, and not highlighted on demand except with this function.
        // enableHighlightCSS(true);
  }
  
  function enableHighlightCSS(blnEnable) {
    //enable or disable css highlighting
    var styleEl = document.getElementById("hop_highlightStyle");

    if (styleEl) {
      if (browser_ie == true) {
         // ie doesn't like changing the css after it was already written, I guess, so swap cookie and postNewPAram to reload page without hop_q  
         if (blnEnable == true) {   
             //show it
             // reset cookie and reload page instead (that's what you get for using IE)
             var cookieValueToUse = getCookie(hop_cookieName + hop_ieSwapCookieSuffix);
             setCookie(hop_cookieName , cookieValueToUse);
             // remove swap cookie:
             setCookie(hop_cookieName + hop_ieSwapCookieSuffix, "");
             postNewParam("hop_q",cookieValueToUse); // empty value doesn't work with postNewParam (that's too bad)
         } else {
             //hide it
              // reset cookie and reload page instead (that's what you get for using IE)
              setCookie(hop_cookieName + hop_ieSwapCookieSuffix,getCookie(hop_cookieName )); //remember what was highlighted in case they want it back
              setCookie(hop_cookieName ,""); //no highlighting now
              postNewParam("hop_q"," "); // empty value doesn't work with postNewParam (that's too bad)
         }
         // ok now post a new param and reload
         //alert('about to reload');
         
      } else { //thank you for not using IE
         if (blnEnable == true) {
           //have some highlighting
            styleEl.innerHTML = ".hop_highlight {background-color : #FFFF33 }";    
         } else { //hide it!
             styleEl.innerHTML = " /* .hop_highlight {background-color : #FFFF33 } */ "; 
         } //show hide in non-IE
         showUnhighlightPen(blnEnable); //shows the reverse of what's seen now
         
      } //using IE or not
     
    } //has the style element I was looking for
    
  } //end of enableHighlightCSS
   
  function replaceShownText(strText,strFind,strReplace) {
    // strFind is either a regular expression of a string
    //function replaces SHOWN text (i.e. ignores stuff inside a tag)
    // <span>span </span> would only replace the middle span.
    // badly formed html will break this
    // strText can be thought of as this: TEXT <tag> TEXT <tag> TEXT
    // strText can also have other things to skip, ie &something;
    // also skip <input>THIS STUFF</input> and similar tags:  script, style,  textarea, title  (input,button these don't work to include)
    var strTagsToSkip = " script style textarea title ";
    // so this just goes through and replaces where you see TEXT but not tag
    
    if (strFind == "") {
      return strText;
    }
    var strRemaining = strText;
    var strNewText = "";
    var closingChar = ">"; //default
    var strCloseTagFirst = ""; //variable to store what tag we have to close before continuing anything
        
    do
      {
        // get first bit of strRemaining
        var whereAmp = strRemaining.indexOf("&");
        var whereLT = strRemaining.indexOf("<"); 
        if ((whereAmp < whereLT) && (whereAmp != -1)) {
            //commit ampersand bit first
            whereLT = whereAmp; //pretend that "&" is a "<"
            closingChar = ";"; //semi-colon closes a &something; tag -- pretend that ";" is a ">"
        } else { //look for < not & 
            closingChar = ">"; //looking for > not ;
        } // < not &
        
            var strBit = "";
            if (whereLT == -1) { //there is no <
              //no tags left, just do a replace
              strNewText  = strNewText + strRemaining.replace(strFind,strReplace);
              strRemaining = "";
            } else { //there is at least one tag
              if (whereLT == 0 ) {
                //starts with a tag, so copy the tag
                var whereGT = strRemaining.indexOf(closingChar); 
                if (whereGT == -1) {
                  //hmmm, there is not GT, just add this to the new text, but this is an error
                  strNewTExt = strNewText + strRemaining;
                  strRemaining = "";
                } else {
                  //there is a GT, copy up to it and pass the rest to remaining
                  //but first see if this is a tag that should be starting something special, i.e. script that we shouldn't replace inside
                  strCloseTagFirst = hop_interpretTextTag(strRemaining);
                  //alert(strRemaining.substring(whereGT -1,whereGT));
                  if ((strTagsToSkip.indexOf(" " + strCloseTagFirst + " ") != -1) && (strRemaining.substring(whereGT,whereGT+1) != "/")) {
                      // alert("found special tag that doesn't close itself:" + strCloseTagFirst + " : " + strRemaining.substring(0,whereGT + 1));
                      //ok this is a special tag, so skip everything to the closing tag of the same kind
                      //find closing tag
                      var strRemainLowerCase = strRemaining.toLowerCase();
                      var whereCloseTag = strRemainLowerCase.indexOf("</" + strCloseTagFirst);
                      // dump lowercase strReamin
                      strRemainLowerCase = "";
                      if (whereCloseTag == -1) {
                          //this doesn't close, so just copy everything
                          strNewText = strNewText + strRemaining;
                          strRemaining = "";
                      } else { // it does close (good)
                          // copy up to but not including the last (that will happen on the next go-around)
                          strNewText = strNewText + strRemaining.substring(0,whereCloseTag );
                          strRemaining = strRemaining.substring(whereCloseTag);
                      } //tag closes or not
                      
                  } else { //normal tag, copy the tag and the inside part of the tag will be dealt with later.
                    strNewText = strNewText + strRemaining.substring(0,whereGT + 1);
                    strRemaining = strRemaining.substring(whereGT + 1);
                  }  //normal or special tag
                } //closing tag exists

              } else {
                //does not start with a tag
                //replace stuff up to the tag and continue
                strBit = strRemaining.substring(0,whereLT);
                strNewText = strNewText + strBit.replace(strFind,strReplace);
                strRemaining = strRemaining.substring(whereLT);
              } //does (not) start with a tag
            } //tags or not
          
      } //do loop
    while (strRemaining!="")
    return strNewText;
  }
  
function hop_interpretTextTag(strText) {
  // tells us what the first tag is, including possibily closing tags
  // <foo> returns foo
  // <foo bar="bar"> returns foo
  // </foo> returns /foo
  // RETURNS LOWER CASE
  if (strText.indexOf("<") != -1) {
      //there is a tag!
      var strTag = strText.substring(strText.indexOf("<") + 1);
      // where does tag end?
      var whereSpace = strTag.indexOf(" ");
      var whereGT = strTag.indexOf(">");
      var whereEndTag ;
      //figure out where tag ends:
      if (whereSpace == -1 && whereGT == -1) {
          //neither exists, so the tag doesn't end, therefore no tag!
          return "";
      } else if (whereSpace != -1 && whereGT != -1) {
          //both exist, get the smaller one
          whereEndTag = Math.min(whereSpace,whereGT);
      } else if (whereSpace != -1) {
          //use whereSpace, since it was found (whereGT must not, as it would be caught above)
          whereEndTag = whereSpace;
      } else {
          // finally, must be whereGT
          whereEndTag = whereGT;
      }  // figured out where tag ended
      strTag = strTag.substring(0,whereEndTag ); //knocks off space or > that ended it, too!
      return strTag.toLowerCase();
  }
  
}
  
  //actually called from pages:
  
  function hop_highlightShownTextOnPageCustom() {
     // if (browser_ie) {
     //     return; //doesn't work on IE, surprise, surpise!
     // }
      if ( document.forms.resubmitForm ) {
          var theResubForm = document.forms.resubmitForm;
           if ( theResubForm.hop_q ) {
                  //normal, just set the cookie
                  
                  setCookie(hop_cookieName,theResubForm.hop_q.value); //set even if empty
               } else {
                  // there is not hop_q, look for hop_params
                  if (theResubForm.hop_params ) {
                    // populate cookie based on params instead of value
                    var strParams = " " + theResubForm.hop_params.value + " "; //name of params
                    var strParamValues = ""; // var to store values in
                    for (var i = 0; i < theResubForm.elements.length ; i++ ) {
                        //loop through form values, adding values if they match strParams
                        var strCheckField = " " + theResubForm.elements[i].name + " ";
                        if (strParams.indexOf(strCheckField) != -1) {
                            if (strParamValues.length > 0) {
                                strParamValues = strParamValues + " "; //add a space after it
                            } //there is already something there
                           strParamValues = strParamValues + theResubForm.elements[i].value;
                        } //matches
                    } //loop through elements on form
                    
                    setCookie(hop_cookieName,strParamValues); //set even if empty
                    
                  } //there are hop_params
               } //there is a hop_q
           } //there is a resubmit form
          
          
       var nowSearchFor =  getCookie(hop_cookieName);
       highlightShownTextOnPage(nowSearchFor,CON_WEBSITE_MAIN_CONTENT_DIV_ID,true);
       
       // if ie has swap cookie there, then show controls, too:
       var blnHideControls = ((nowSearchFor.length == 0) || (nowSearchFor == " "));
       var blnShowUnhighlightPen = true;
       
       if (blnHideControls && browser_ie) {
           //see if swap cookie there
           nowSearchFor = getCookie(hop_cookieName + hop_ieSwapCookieSuffix);
           var blnHideControls = ((nowSearchFor.length == 0) || (nowSearchFor == " "));
           if (blnHideControls == false) {
               // dont hide controls, but default the pen the other way for IE
               blnShowUnhighlightPen = false;
           }
       }
       //alert('thinking about showing controls');
       //show/hide controls to turn on or off highlighting
       if ( gebid("hop_highlightcontrol")) { //controls exist
           if (blnHideControls ) {
             //alert('hiding ctls');
             gebid("hop_highlightcontrol").className= "hidden";  //hide them, we aren't highlighting anything  
           } else {
             //alert('showing ctls');
             gebid("hop_highlightcontrol").className= "show";  //show them, b/c we highlighted something (or tried)
           }
           showUnhighlightPen(blnShowUnhighlightPen);
       } //if controls exist on page
  }
  
  function showUnhighlightPen(blnShowUn) {
      // shows unhighlight pen if blnShowUn, else shows highlight Pen
      var strPenUnhighID = "hop_highlightcontrol_unhigh";
      var strPenHighID = "hop_highlightcontrol_high";
    if ( (gebid(strPenUnhighID)) && (gebid (strPenHighID))) {  
      if (blnShowUn == true) {
         //show unhighlight pen, not highlight pen
         //alert('showing unhighlight pen');
         gebid(strPenUnhighID).className="show";
         gebid(strPenHighID).className="hidden";            
      } else {
         //show highlight pen, not unhighlight pen
         //alert('showing HIGHLIGHT pen');
         gebid(strPenUnhighID).className="hidden";
         gebid(strPenHighID).className="show"; 
                     
      }
    } //pens exist
  }