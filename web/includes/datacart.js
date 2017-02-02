// $Id: datacart.js,v 1.15 2006-06-28 17:38:40 mlee Exp $
// Handles AJaX routines for datacart access.
// Uses ARC customize the form's checkboxes.


/////////////// GLOBALS

var dc_formId = null;
var dc_onClassCheckbox = null;
var dc_offClassCheckbox = null;
var dc_editedCol = null;



/**
 * Uses AJaX and <vegbank:datacart> to count the datacart items.
 */
function refreshCartCount() {
    alert("refreshing");
	var ajax = initAjax();

    var fnWhenDone = function(oXML) {
        //alert("pong");
		setDatacartCount(oXML.responseText);
    };

    var params = "delta=drop";
    var url = "@ajax_link@get_datacart_count.ajax.jsp";
    ajax.connect(url, "POST", params, fnWhenDone);
}


/**
 * Uses AJaX and <vegbank:datacart> to update the datacart items.
 */
function updateCartItem(elem, add) {
    setDatacartBusy(true);
	var ajax = initAjax();

    var fnWhenDone = function(oXML) {
        //alert("pong");
		setDatacartCount(oXML.responseText);
        flashDatacart();
        setDatacartBusy(false);
    };

    var params;

    if (add) {
        params = "delta=add";
        //elem.parentNode.className = 'highlight';
    } else {
        params = "delta=drop";
        //elem.parentNode.className = curClass;
    }

    params += "&deltaItems="+encodeURIComponent(elem.value);
    var url = "@ajax_link@get_datacart_count.ajax.jsp";

    //alert(url + "?" + params);
    ajax.connect(url, "POST", params, fnWhenDone);
}


/**
 * Adds all search results to the datacart.
 * Uses AJaX.
 */
function addAllResults(itemType) {

    setDatacartBusy(true);

	var ajax = initAjax();

    var fnWhenDone = function(oXML) {
		setDatacartCount(oXML.responseText);
        changePageItemStates(true, false);
        flashDatacart();
        setDatacartBusy(false);
    };


    // combine form elements
    var params = "";
    for (var i=0; i < document.resubmitForm.elements.length; i++) {
        if (i!=0) {
            params += "&";
        }

        params += document.resubmitForm.elements[i].name + "=" +
                encodeURIComponent(document.resubmitForm.elements[i].value);
    }

    //alert(params);
    var url = "@ajax_link@findadd_" + itemType + "_datacart.ajax.jsp";
    ajax.connect(url, "POST", params, fnWhenDone);

}

/**
 * Adds all on page.
 */
function addAllOnPage() {
    changePageItemStates(true, true);
}

/**
 * Drops all on page.
 */
function dropAllOnPage() {
    changePageItemStates(false, true);
}

/**
 * Turns on or off all checkboxes on page.
 */
function changePageItemStates(isChecked, doCartUpdate) {
    var form = document.getElementById("cartable");
	var inputs = form.getElementsByTagName("input");

    for (var i=0; i<inputs.length; i++) {
        if (inputs[i].type == "checkbox" && inputs[i].checked != isChecked) {
            delay(100);
			changeItemState(inputs[i], isChecked, doCartUpdate);
        }
    }
}


/**
 *
 */
function setCheckboxParentHighlight(elem, on) {
    //alert(elem + " IS CHECKED: " + elem.checked + " CLASSNAME: " + elem.className);
    if (on) {
        elem.parentNode.className = 'highlight';
    } else {
        elem.parentNode.className = elem.className;
    }
}


//
// Mark all items already in datacart
//
function markDatacartItems(dsId) {
	var nodeList = document.getElementById('cartable').getElementsByTagName('input');

	if (nodeList.length > 0) {
		var first = true;
		var acString = "'";
		var i, elem;
		for (i=0; i<nodeList.length; i++) {
			elem = nodeList.item(i);
			if (elem.type == 'checkbox') {
				// add to list
				if (first) { first = false;
				} else { acString += "','"; }
				acString += elem.value;
			} // end if checkbox
		}
		acString += "'";

		// run query through AJaX
		// can't use jsp since the page is already loaded at this point.
		findSelectedDatacartItems(dsId, acString);
	}
}


//
// Uses AJaX to connect to getDatacartACs.ajax.jsp
//
function findSelectedDatacartItems(dsId, acString) {
	var ajax = initAjax();

    var fnWhenDone = function(oXML) {
		// oXML.responseText contains a CSV of accession codes
		// found on this page and in the datacart
		var nodeList = document.getElementById('cartable').getElementsByTagName('input');

		datacartACs = oXML.responseText.toLowerCase();
		//alert("response: " + datacartACs);

		// mark the right boxes
		for (var i=0; i<nodeList.length; i++) {
			var elem = nodeList.item(i);
			if (elem.type == 'checkbox') {
				if (datacartACs.indexOf(elem.value.toLowerCase()) != -1) {
					// mark it
					elem.checked = true;
					elem.parentNode.className = 'highlight';
                    toggleLabelStyle(elem.label);
				}
			}
		}
    };

    var url = "@ajax_link@get_datacart_acs.ajax.jsp";
    var params = "wparam="+encodeURIComponent(dsId);
    params += "&wparam="+encodeURIComponent(acString);

    //alert(url + params);
    ajax.connect(url, "POST", params, fnWhenDone);
}




///////////////////////// ARC /////////////////////////
/**
NAME: initARC()

ABOUT:
 Detect the current user browser and customize the form's checkboxes if
 the browser is not IE mac, <= IE 4 or NS4.

USAGE:
 In your main HTML body use onLoad() to call initARC(), passing in the form id
 and on/off class names you wish to use to customise your checkbox buttons.
 e.g. <body onLoad="initARC('myform','cbOnClass', 'cbOffClass');">

PARAMS:
 formId   - The ID of the form you wish to customise
 onClass  - The CSS class name for the checkbox's on style
 offClass - The CSS class name for the checkbox's off style
*/
function initARC(formId,onClassCheckbox,offClassCheckbox) {
    var agt=navigator.userAgent.toLowerCase();

    // Browser Detection stuff
    this.major = parseInt(navigator.appVersion);
    this.ie     = ((agt.indexOf("msie") != -1) && (agt.indexOf("opera") == -1));
    this.ie3    = (this.ie && (this.major < 4));
    this.ie4    = (this.ie && (this.major == 4) && (agt.indexOf("msie 4")!=-1) );
	this.iemac  = (this.ie && (agt.indexOf("mac")!=-1));

	dc_formId = formId;
	dc_onClassCheckbox = onClassCheckbox;
	dc_offClassCheckbox = offClassCheckbox;

	if( !(this.iemac || ie3 || ie4) ){
		customiseInputs();
	}
}



//Add a .label reference to all input elements. Handy! Borrowed from...
//http://www.codingforums.com/archive/index.php/t-14672
function addLabelProperties(f){
	if(typeof f.getElementsByTagName == 'undefined') { return; }
	var labels = f.getElementsByTagName("label"), label, elem, i=0, j=0;

	label = labels[0];
	while (label) {
		if(typeof label.htmlFor == 'undefined') return;
		elem = document.getElementById(label.htmlFor);
		//elem = f.elements[label.htmlFor]; /* old method */

		if(typeof elem == 'undefined'){
			//no label defined, find first sub-input
			var inputs = label.getElementsByTagName("input");
			if(inputs.length==0){
				continue;
			} else {
				elem=inputs[0];
			}
		} else if(typeof elem.label != 'undefined') { // label property already added
			continue;
		} else if(typeof elem.length != 'undefined' && elem.length > 1 && elem.nodeName != 'SELECT'){
			for(j=0; j<elem.length; j++){
				elem.item(j).label = label;
			}
		}
		elem.label = label;
        i++;
        label = labels[i];
	}
}



/**
NAME: toggleLabelStyle()

ABOUT:
 This function is attached to our label's onClick event. So when the label is
 clicked this function alters the checkbox's members to an unchecked state
 and style, and alters the currently selected label to the on style and checked
 state.

USAGE:
 ARC currently assumes that the label contains a FOR='id' in it's HTML. The other
 valid form of a label is <label>text <input /></label> - while it is possible
 to modify this code to allow for this form I have left this as an exercise for
 the reader.

PARAMS:
 formId   - Parent form of this label
 label    - The label for a checkbox we wish to toggle
 onClass  - The CSS class name for the checkbox's on style
 offClass - The CSS class name for the checkbox's off style
*/
function toggleLabelStyle(label) {
	if(!document.getElementById || !label) return;

	var form = document.getElementById(dc_formId); //label.form;
	if(!form) return;

	//find checkbox associated with label (if in htmlFor form)
	if(label.htmlFor) {
		var e = document.getElementById(label.htmlFor);

		if(e.type=="checkbox"){
			e.label.className = (e.label.className==dc_onClassCheckbox) ? dc_offClassCheckbox : dc_onClassCheckbox;
			e.checked = (e.label.className==dc_onClassCheckbox);
		}
	}
}



/**
NAME: customiseInputs()

ABOUT:
 This function does all the magic. It finds the <input>'s within the passed form
 and attaches a .label reference to the element, and also adds an onClick
 function to that label to the toggleLabelStyle() function.
 It hides all checkbox elements from the form and mirrors the startup checked values
 in the label's customised checkbox button styles.

USAGE:
 Called from initARC()

PARAMS:
 formId   - The form we're customising
 onClass  - The CSS class name for the checkbox's on style
 offClass - The CSS class name for the checkbox's off style
*/
function customiseInputs() {
	if(!document.getElementById) return;

	var prettyForm = document.getElementById(dc_formId);
	if(!prettyForm) return;

	//onReset, reset to initial values
	prettyForm.onreset = function() { customiseInputs(); }

	//attach an easy to access .label reference to form elements
	addLabelProperties(prettyForm);

	var inputs = prettyForm.getElementsByTagName('input');
	for (var i=0; i < inputs.length; i++) {

		//CHECKBOX ONLY
		if( (inputs[i].type=="checkbox") && inputs[i].label && dc_onClassCheckbox && dc_offClassCheckbox){
			//hide element
			inputs[i].style.position="absolute"; inputs[i].style.left = "-1000px";
			//initialise element
			inputs[i].label.className=dc_offClassCheckbox;

            //enable whole-box clicking
            inputs[i].parentNode.onclick =
                function () {
                    var elem = this.getElementsByTagName("input")[0];
                    changeItemState(elem, !elem.checked, true);
                    return false;
                };

            //mouseover
            inputs[i].parentNode.onmouseover =
                function () {
                    if (this.className == 'highlight') {
                        this.className = 'mouseover_dark';
                    } else {
                        this.className = 'mouseover_bright';
                    }
                };

            //mouseout
            inputs[i].parentNode.onmouseout =
                function () {
                    if (this.className == 'mouseover_dark') {
                        this.className = 'highlight';
                    } else if (this.className != 'highlight') {
                        this.className = this.getElementsByTagName("input")[0].className;
                    }
                };

			//if the checkbox was checked by default, change this label's style to checked
			if(inputs[i].defaultChecked || inputs[i].checked) {
                toggleLabelStyle(inputs[i].label);
            }
		}

		if((inputs[i].type=="checkbox") && inputs[i].label){
			//Attach keyboard navigation
			if(!this.ie){ //IE has problems with this method
				//You could set these to grab a passed in class name if you wanted to
				//do something a bit more interesting for keyboard states. But for now the
				//generic dotted outline will do for most elements.
				inputs[i].label.style.margin = "1px";
				inputs[i].onfocus =
                    function () {
                        this.label.style.border = "1px dotted #333";
                        this.label.style.margin="0px";
                        return false;
                    };

				inputs[i].onblur =
                    function () {
                        this.label.style.border = "none";
                        this.label.style.margin="1px";
                        return false;
                    };
			}
		}
    }
}


function changeItemState(elem, isChecked, doCartUpdate) {
	setCheckboxParentHighlight(elem, isChecked);
    if (doCartUpdate) {
        updateCartItem(elem, isChecked);
    }
	toggleLabelStyle(elem.label);
}


function setDatacartBusy(isBusy) {
    document.getElementById("datacart-count-icon").className =
		(isBusy ? "busyanim" : "datacart-icon-normal");
}

function setEditRowBusy(isBusy) {
    if (document.getElementById("edit_row_busy_icon")) {
		document.getElementById("edit_row_busy_icon").className =
		(isBusy ? "busyanim" : "busyicon");
	}
}


function setDatacartCount(newCount) {
	document.getElementById("datacart-count").innerHTML = newCount;
}


function flashDatacart() {
    Fat.fade_element("datacart", 12, 1500, "#FF9933", "#FFFFFF");
}

function editDatasetRow(col) {
    var staticRow = col.parentNode;
    var dsId = staticRow.getAttribute("id");

    if (dc_editedCol != null) {
        cancelDatasetEdit(dc_editedCol);
    }

    // populate the editable name and description fields
    var edit_row = gebid("edit_row_tpl");
    var editCols = edit_row.getElementsByTagName("td");
    // form -> td, td -> input
    var nameInput = gebid("name_input");
    var sharingInput = gebid("sharing_input");
    // form -> td, td, td -> textarea
    var descTextarea = gebid("desc_textarea");

    // populate the name field
    var staticNameEl = gebid(dsId + "-name"); // findSibling(col, "td", 20);
    var dsName = staticNameEl.innerHTML;
    if (dsName && dsName[0] == "\n") {
	            dsName = dsName.substring(1);
       }
    dsName=trim_cr(dsName);
    //debug  alert('>' + dsName.charAt(0) + '<  = #' + dsName.charCodeAt(0)  );
    if (dsName.charCodeAt(0) == 13 || dsName.charCodeAt(0) == 10) {
		dsName = dsName.substring(1);
	}
    //encoding!
    dsName = UnHTMLSafe(dsName);
    nameInput.value = dsName;
    hide(staticRow);

    // populate the description
    var descEl = gebid(dsId + "-description");
    if (descEl) {
        var dsDesc = descEl.innerHTML;
        if (dsDesc && dsDesc[0] == "\n") {
            dsDesc = dsDesc.substring(1);
        }
        if (dsDesc == "no description") {
            dsDesc = "";
        }
        dsDesc = UnHTMLSafe(dsDesc);
        descTextarea.value = dsDesc;
        //hide(descRow);
    }

     // populate the sharing
	    var sharingEl = gebid(dsId + "-sharing");
	    if (sharingEl) {
	        var dsSharing = sharingEl.innerHTML;
	        if (dsSharing && dsSharing[0] == "\n") {
	            dsSharing = dsSharing.substring(1);
	        }
	       // if (dsSharing == "no description") {
	       //     dsSharing = "";
	       // }
	        dsSharing = UnHTMLSafe(dsSharing);
	        sharingInput.value = dsSharing;
	        //hide(descRow);
    }

    // show the edit row
    edit_row.style.display = "";
    //nameInput.focus();
    staticRow.parentNode.insertBefore(edit_row, findSibling(staticRow, "tr", 20));
    dc_editedCol = col;
}

function cancelDatasetEdit(col) {
    // hide the edit_row form
    // by moving it into the hidden table
    var edit_row = gebid("edit_row_tpl");
    var hidden_table = gebid("hidden_table");
    var hidden_table_row = gebid("hidden_table_row");

    var staticRow = edit_row.previousSibling;

    hidden_table.insertBefore(edit_row, hidden_table.firstChild);

    // show the static row
    while (staticRow.nodeName.toLowerCase() != "tr") {
        staticRow = staticRow.previousSibling;
    }
    var dsId = staticRow.getAttribute("id");

    //staticRow.className = "thinlines";
    staticRow.style.display = "";
    //staticRow.style.background = "#ff0";

    // show the static description row if there
  //  var descRow = gebid(dsId + "-desc");
  //  if (descRow) {
  //      descRow.style.display = "";
        //descRow.style.background = "#00F";
  //  }

    dc_editedCol = null;
}


/**
 * Uses AJaX and vegbank:update to update the dataset header.
 */
 function adjustEmbargo(lngID,blnRenew,blnCancel) {
	// this adjusts embargo, given its ID and either blnRenew to renew for 3 years or blnCancel to cancel the embargo
	//debug: alert('adjustEmbargo: ' + lngID + "," + blnRenew + "," + blnCancel );

	 setEditRowBusy(true);
	 var ajax = initAjax();
	  var fnWhenDone = function(oXML) {
	         //alert("pong");
	         //debug: alert("done with adjustEmbargo function, now dealing with response: " + oXML.responseText);
	         //uses response text to know what accession code (plot accession code) to run updates of embargo denorm on.
	         updateEmbargoDenorms(trim_accode(oXML.responseText));
	          setEditRowBusy(false);
      };
          var params = "";
	      params += "fieldNames=embargostop";
	      if (blnRenew) params += "&adjustCurrDateTime=3&adjustUnit=year";
	      if (blnCancel) params += "&getCurrDateTime=true";
	      params += "&recordId="+encodeURIComponent(lngID);
          var url = "@ajax_link@update_embargo.ajax.jsp";
   //call ajax
   ajax.connect(url, "POST", params, fnWhenDone);
   //still need to update denormalized fields, too, this done with 2nd ajax call: in fnWhenDone

 }


function updateEmbargoDenorms(accCode) {
	// updates observation denorm embargo fields to allow it to be shown on website
	// can pass plotAccCode, too, and if so it is used
	  //debug: alert("updateEmbargoDenorms>> with: " + accCode);

	   var ajax = initAjax();
	   var fnWhenDone = function(oXML) {
		         //alert("pong");
		         //alert(oXML.responseText);
		         setEditRowBusy(false);
		        //debug: alert("done with updateEmbargoDenorms");
	      };
	  var params = "emb_only=true";
	  var url="@ajax_link@denorm_observation.jsp";
     //debug: alert("accCode length:" + accCode.length);
      if (accCode.length>0) {
		setEditRowBusy(true);
		if (accCode.toLowerCase().indexOf(".pl.") != -1 ) {
		  //special case: plot accession code
		 // alert("running plot accession code embargo denorm update");
          params += "&plotacccode=" + accCode;
	    } else {
		  //std observation accessioncode assumed.
		 // alert("running OBS accession code embargo denorm update");
		  params += "&wparam=" + accCode;
        }
         ajax.connect(url, "POST", params, fnWhenDone);
	  }

}


function updatePlotLocationFuzzing(lngID,intLevel) {
	 //function updates plot.confidentialityStatus, which controls the lat and long denorm fields in the plot table
	 // subsequent ajax updates lat/long, this one updates only plot.confidentialityStatus with vegbank:update
      //only allow 0-3 for update values
     //debug: alert("updatePlotLocationFuzzing> plot:" + lngID + " lev:" + intLevel);
      if (intLevel >=0 && intLevel <=3 && !isNaN(intLevel)) {
		//debug: alert(intLevel + "is between 0 and 3");
         //use put function to say that we're thinking about it
         putAjaxResponseOnPage("plot" + lngID + "latlong: calculating...");

         setEditRowBusy(true);
		 var ajax = initAjax();
		  var fnWhenDone = function(oXML) {
		         //alert("pong");
		        //debug: alert("done with updatePlotLocationFuzzing function, now dealing with response: " + oXML.responseText);
		         //uses response text to know what accession code (plot accession code) to run updates of embargo denorm on.
		         // updateLatLongDenorms(trim_accode(oXML.responseText));
                 putAjaxResponseOnPage(oXML.responseText);
		          setEditRowBusy(false);
	      };
	          var params = "";
		      params += "fieldNames=confidentialitystatus";
		      params += "&fieldValues=" + encodeURIComponent(intLevel);
		      params += "&recordId="+encodeURIComponent(lngID);
	          var url = "@ajax_link@update_plotlocationfuzzing.ajax.jsp";
	   //call ajax
       ajax.connect(url, "POST", params, fnWhenDone);
   }
}


function genericAjax(file,recordId) {
	//generic function that runs an ajax call, adding param recordId
	//generally used for get functions that get a string
	           var ajax = initAjax();
			   var fnWhenDone = function(oXML) {
				      //alert("pong");
				      //alert(oXML.responseText);
				      //setEditRowBusy(false);
				      //take first bit of responseText up to : and this is the ID that should be used to set in document
				      putAjaxResponseOnPage(oXML.responseText);

				   //debug:   alert("done with genericAjax");
				      //return oXML.responseText;
			      };
			  var params = "recordId="+encodeURIComponent(recordId);;
			  var url="@ajax_link@" + file;
 	         ajax.connect(url, "POST", params, fnWhenDone);
}

function putAjaxResponseOnPage(response) {
	// puts an ajax response on the page, replacing the div ID's innerHTML
	// response should be of the format:
	// IDOfElement:HTMLToUse
	// example:
	// plot225467:lat is 45 and long is 15
	if (response.indexOf(":") != -1) {
							  var divId = response.substr(0,response.indexOf(":"));
							  //only allow numbers and letters:
							  divId=trim_accode(divId);
							 // alert(divId + " is divId");
							  var restHTML = response.substr(response.indexOf(":")+1);
							 // alert("rest:" + restHTML);
							  gebid(divId).innerHTML = restHTML;
					  }
}

/**
 * Uses AJaX and <vegbank:update> to update the dataset header.
 */



function saveDatasetEdit(col) {
    setEditRowBusy(true);
	var ajax = initAjax();

    var fnWhenDone = function(oXML) {
        //alert('pong');  //todo: actually deal with response, as it might have failed!
        setEditRowBusy(false);
        cancelDatasetEdit(dc_editedCol);
    };

    var nameInput = gebid("name_input");
    var descTextarea = gebid("desc_textarea");
    var datasetSharing = gebid("sharing_input");
    var staticRow = gebid("edit_row_tpl").previousSibling;
    while (staticRow.nodeName.toLowerCase() != "tr") {
        staticRow = staticRow.previousSibling;
    }
    var dsId = staticRow.getAttribute("id");
    var dsName = nameInput.value;
    var dsSharing =  datasetSharing.value;
    //debug  alert('>' + dsName + '<');
    // needs utils.js:
    dsName = trim_cr(dsName);
    //debug alert('after trim: >' + dsName + '<');

    var dsDesc = descTextarea.value;
    dsDesc = trim_cr(dsDesc);

     var sharingEl = gebid(dsId + "-sharing");

    var acccodeparams = "";
    console.log("seeing if public");
    if (dsSharing == 'public') {
      // and it wasnt public before:
      console.log("verily it is public");
      if (sharingEl) {
		  console.log("have a sharing el value>" + sharingEl.innerHTML  + "<");
        if (sharingEl.innerHTML.indexOf('private') != -1) {
			console.log("verily current value is private");
          var accCodeEl = gebid(dsId + "-accessioncode");
          if (accCodeEl) {

	         //see if ends with .UNNAMEDDATASET and if so fix it
	         dsAccCode = accCodeEl.innerHTML;
	         dsAccCode=trim_cr(dsAccCode);
	         console.log("have accCode, zwar " + dsAccCode);
	         if (dsAccCode.indexOf('.UNNAMEDDATASET') != -1) {
				 //create random chars
				 console.log("and its an unnameddataset situation");
				 var newAccCode = dsAccCode.substring(0,dsAccCode.indexOf('.UNNAMEDDATASET')) + '.' + getRandomChars(20);
				 acccodeparams = "&fieldNames=accessioncode" + "&fieldValues="+encodeURIComponent(newAccCode);
				 console.log('new params' + acccodeparams);
				 // update in page too:
				      accCodeEl.innerHTML = HTMLSafe(newAccCode);
			 }
          }
	    }
	  }
    }

    var params = "";
    params += "fieldNames=datasetname";
    params += "&fieldValues=" + encodeURIComponent(dsName);
    params += "&fieldNames=datasetsharing";
    params += "&fieldValues=" + encodeURIComponent(dsSharing);
    params += "&fieldNames=datasetdescription"
    params += "&fieldValues="+encodeURIComponent(dsDesc);
    params += "&recordId="+encodeURIComponent(dsId);
    params += acccodeparams;
    var url = "@ajax_link@update_userdataset.ajax.jsp";

    console.log('final params '  + params);

   // var staticCols = staticRow.getElementsByTagName("td");
   // staticCols[1].firstChild.innerHTML = HTMLSafe(dsName);

    var nameEl = gebid(dsId + "-name");
       if (nameEl) {
            nameEl.innerHTML = HTMLSafe(dsName);
    }


    // populate the description
       var descEl = gebid(dsId + "-description");
       if (descEl) {
            descEl.innerHTML = HTMLSafe(dsDesc);
    }

	    if (sharingEl) {
			sharingEl.innerHTML = HTMLSafe(dsSharing);
		}

    //alert(url + "?" + params);
    ajax.connect(url, "POST", params, fnWhenDone);

}

  function getRandomChars(lngHowMany) {
        var strRet = "";
        var min = 65;
        var max = 65+25;
        for (var i=1;i<=lngHowMany;i++) {
    		strRet += String.fromCharCode(Math.random() * (max - min) + min);
    	}
    	//alert(strRet);
    	return strRet;
      }
function saveCurrentRecord() {
    saveDatasetEdit(dc_editedCol);
    return false;
}
function removeDatasetRow(col, undelete) {
	//deletes a dataset by updating the stopdate and removing from screen using js
	//special param: getCurrDateTime gets the current time for values in the called ajax file
	if (!undelete) {
        if (!confirm("Do you really want to delete this dataset?  Press OK to delete it, or press Cancel to delete nothing.")) {
     		return false;
    	}
    }

	  //  setEditRowBusy(true);
		var ajax = initAjax();

	    var fnWhenDone = function(oXML) {
	        //alert("pong");
	       // setEditRowBusy(false);
	       // dont need this:  cancelDatasetEdit(dc_editedCol);
    };

    var staticRow = col.parentNode;
    var dsId = staticRow.getAttribute("id");
    var table = col.parentNode.parentNode;
    var gonerClass = staticRow.className;

    //set up ajax
    var params = "";
    params += "fieldNames=datasetstop";
    params += "&recordId="+encodeURIComponent(dsId);
    if (undelete) {
		// set to null by NOT passing fieldValues
	} else {
		// set to now()
      params += "&getCurrDateTime=true";}
    var url = "@ajax_link@update_userdataset.ajax.jsp";

    staticRow.className = "deleted";
    var descRow = findSibling(staticRow, "tr", 20);
    if (descRow) {
		 //alert('found desc row');
		 descRow.className = "deleted";
	}
	var staticCols = staticRow.getElementsByTagName("td");
	if (undelete) {
      staticCols[0].innerHTML="undeleted!";
    } else {
	  staticCols[0].innerHTML="deleted!";
    }
    /* THIS DOESNT WORK SO I'M JUST NOTING IT WAS DELETED :
    var tmpSibling = findSibling(staticRow, "tr", 20);
    var siblingClass;
    var tmpClass;
    if (tmpSibling != null) {
        siblingClass = tmpSibling.className;
        tmpClass = siblingClass;
    }

    table.removeChild(staticRow);

    // reset row colors
    while (tmpSibling != null) {
        if (tmpClass == gonerClass) {
            tmpClass = siblingClass;
        } else {
            tmpClass = gonerClass;
        }

        tmpSibling.className = tmpClass;
        tmpSibling = findSibling(tmpSibling, "tr", 20);
    }
    */

    //run ajax
    ajax.connect(url, "POST", params, fnWhenDone);
    return true;
}
