// $Id: datacart.js,v 1.2 2005-05-19 01:26:10 anderson Exp $
// Handles AJaX routines for datacart access.
// Uses ARC customize the form's checkboxes.


/////////////// GLOBALS

var dc_formId = null;
var dc_onClassCheckbox = null;
var dc_offClassCheckbox = null;



/**
 * Uses AJaX and <vegbank:datacart> to update the datacart items.
 */
function updateCartItem(elem, add) {
	var ajax = initAjax();

    var fnWhenDone = function(oXML) { 
        //alert("pong");
        document.getElementById("datacart-count").innerHTML = oXML.responseText;
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
    var url = "@web_context@general/get_datacart_count.ajax.jsp";

    //alert(url + "?" + params);
    ajax.connect(url, "POST", params, fnWhenDone);
}


/**
 * Adds all on page.
 */
function addAllOnPage() {
    changeCheckboxStates(true);
}

/**
 * Drops all on page.
 */
function dropAllOnPage() {
    changeCheckboxStates(false);
}

/**
 * Turns on or off all checkboxes on page.
 */
function changeCheckboxStates(isChecked) {
    var form = document.getElementById("cartable");
	var inputs = form.getElementsByTagName("input");

    for (var i=0; i<inputs.length; i++) {
        if (inputs[i].type == "checkbox" && inputs[i].checked != isChecked) {
            delay(100);
			changeItemState(inputs[i], isChecked);
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

    var url = "@web_context@general/get_datacart_acs.ajax.jsp";
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

                /*
            //when the label is clicked..
            inputs[i].label.onclick = function () {
                var elem = document.getElementById(this.htmlFor);
                setCheckboxParentHighlight(elem, !elem.checked);
                updateCartItem(elem, !elem.checked);
                toggleLabelStyle(this);
                return false; 
            };

            //enable keyboard navigation
            inputs[i].onclick = function () {
                var elem = document.getElementById(this.label.htmlFor);
                setCheckboxParentHighlight(elem, !elem.checked);
                updateCartItem(elem, !elem.checked);
                toggleLabelStyle(this.label);
                return false; 
            };
                */

            //enable whole-box clicking
            inputs[i].parentNode.onclick = function () {
                var elem = this.getElementsByTagName("input")[0];
				changeItemState(elem, !elem.checked);
                //setCheckboxParentHighlight(elem, !elem.checked);
                //updateCartItem(elem, !elem.checked);
                //toggleLabelStyle(elem.label);
                return false; 
            };

            //mouseover
            inputs[i].parentNode.onmouseover = function () {
                if (this.className == 'highlight') {
                    this.className = 'mouseover_dark';
                } else {
                    this.className = 'mouseover_bright';
                }
            };

            //mouseout
            inputs[i].parentNode.onmouseout = function () {
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
				inputs[i].onfocus = function (){ this.label.style.border = "1px dotted #333"; this.label.style.margin="0px"; return false; };
				inputs[i].onblur  = function (){ this.label.style.border = "none"; this.label.style.margin="1px"; return false; };
			}
		}
    }
}


function changeItemState(elem, isChecked) {
	setCheckboxParentHighlight(elem, isChecked);
	updateCartItem(elem, isChecked);
	toggleLabelStyle(elem.label);
}
