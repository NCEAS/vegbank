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
      var oNew = document.createElement("script");
      oNew.type = "text/javascript";
      oNew.src = sURI;
      hIncludes[sURI]=true;
      document.getElementsByTagName("head")[0].appendChild(oNew);
    }
  }
}

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


/*************************************************************************************************/
/*  the following from http://www.kryogenix.org/code/browser/sorttable/   on about 2004-DEC-01   */
/*************************************************************************************************/

addEvent(window, "load", sortables_init);

var SORT_COLUMN_INDEX;

function sortables_init() {
    // Find all tables with class sortable and make them sortable
    if (!document.getElementsByTagName) return;
    tbls = document.getElementsByTagName("table");
    for (ti=0;ti<tbls.length;ti++) {
        thisTbl = tbls[ti];
        if (((' '+thisTbl.className+' ').indexOf("sortable") != -1) && (thisTbl.id)) {
            //initTable(thisTbl.id);
            ts_makeSortable(thisTbl);
        }
    }
}

function ts_makeSortable(table) {
    if (table.rows && table.rows.length > 0) {
        var firstRow = table.rows[0];
    }
    if (!firstRow) return;
    
    // We have a first row: assume it's the header, and make its contents clickable links
    for (var i=0;i<firstRow.cells.length;i++) {
        var cell = firstRow.cells[i];
        var txt = ts_getInnerText(cell);
        cell.innerHTML = '<a href="#" class="sortlink" onclick="ts_resortTable(this);return false;">'+txt+'<span class="sortarrow"></span></a>';
    }
}

function ts_getInnerText(el) {
	if (typeof el == "string") return el;
	if (typeof el == "undefined") { return el };
	if (el.innerText) return el.innerText;	//Not needed but it is faster
	var str = "";
	
	var cs = el.childNodes;
	var l = cs.length;
	for (var i = 0; i < l; i++) {
		switch (cs[i].nodeType) {
			case 1: //ELEMENT_NODE
				str += ts_getInnerText(cs[i]);
				break;
			case 3:	//TEXT_NODE
				str += cs[i].nodeValue;
				break;
		}
	}
	return str;
}

function ts_resortTable(lnk) {
    // get the span
    var span;
    for (var ci=0;ci<lnk.childNodes.length;ci++) {
        if (lnk.childNodes[ci].tagName && lnk.childNodes[ci].tagName.toLowerCase() == 'span') span = lnk.childNodes[ci];
    }
    var spantext = ts_getInnerText(span);
    var td = lnk.parentNode;
    var column = td.cellIndex;
    var table = getParent(td,'TABLE');
    
    // Work out a type for the column
    if (table.rows.length <= 1) return;
    var itm = ts_getInnerText(table.rows[1].cells[column]);
    sortfn = ts_sort_caseinsensitive;
    if (itm.match(/^\d\d[\/-]\d\d[\/-]\d\d\d\d$/)) sortfn = ts_sort_date;
    if (itm.match(/^\d\d[\/-]\d\d[\/-]\d\d$/)) sortfn = ts_sort_date;
    if (itm.match(/^[£$]/)) sortfn = ts_sort_currency;
    if (itm.match(/^[\d\.]+$/)) sortfn = ts_sort_numeric;
    if (itm.match(/^[\s]*[\d\.]+[ \%]*/)) sortfn = ts_sort_numeric;
    SORT_COLUMN_INDEX = column;
    var firstRow = new Array();
    var newRows = new Array();
    for (i=0;i<table.rows[0].length;i++) { firstRow[i] = table.rows[0][i]; }
    for (j=1;j<table.rows.length;j++) { newRows[j-1] = table.rows[j]; }

    newRows.sort(sortfn);

    if (span.getAttribute("sortdir") == 'down') {
        ARROW = '&uarr;';
        newRows.reverse();
        span.setAttribute('sortdir','up');
    } else {
        ARROW = '&darr;';
        span.setAttribute('sortdir','down');
    }
    
    // We appendChild rows that already exist to the tbody, so it moves them rather than creating new ones
    // don't do sortbottom rows
    for (i=0;i<newRows.length;i++) { if (!newRows[i].className || (newRows[i].className && (newRows[i].className.indexOf('sortbottom') == -1))) table.tBodies[0].appendChild(newRows[i]);}
    // do sortbottom rows only
    for (i=0;i<newRows.length;i++) { if (newRows[i].className && (newRows[i].className.indexOf('sortbottom') != -1)) table.tBodies[0].appendChild(newRows[i]);}
    
    // Delete any other arrows there may be showing
    var allspans = document.getElementsByTagName("span");
    for (ci=0;ci<allspans.length;ci++) {
        if (allspans[ci].className == 'sortarrow') {
            if (getParent(allspans[ci],"table") == getParent(lnk,"table")) { // in the same table as us?
                allspans[ci].innerHTML = '';
            }
        }
    }
        
    span.innerHTML = ARROW;
}

function getParent(el, pTagName) {
	if (el == null) return null;
	else if (el.nodeType == 1 && el.tagName.toLowerCase() == pTagName.toLowerCase())	// Gecko bug, supposed to be uppercase
		return el;
	else
		return getParent(el.parentNode, pTagName);
}
function ts_sort_date(a,b) {
    // y2k notes: two digit years less than 50 are treated as 20XX, greater than 50 are treated as 19XX
    aa = ts_getInnerText(a.cells[SORT_COLUMN_INDEX]);
    bb = ts_getInnerText(b.cells[SORT_COLUMN_INDEX]);
    if (aa.length == 10) {
        dt1 = aa.substr(6,4)+aa.substr(3,2)+aa.substr(0,2);
    } else {
        yr = aa.substr(6,2);
        if (parseInt(yr) < 50) { yr = '20'+yr; } else { yr = '19'+yr; }
        dt1 = yr+aa.substr(3,2)+aa.substr(0,2);
    }
    if (bb.length == 10) {
        dt2 = bb.substr(6,4)+bb.substr(3,2)+bb.substr(0,2);
    } else {
        yr = bb.substr(6,2);
        if (parseInt(yr) < 50) { yr = '20'+yr; } else { yr = '19'+yr; }
        dt2 = yr+bb.substr(3,2)+bb.substr(0,2);
    }
    if (dt1==dt2) return 0;
    if (dt1<dt2) return -1;
    return 1;
}

function ts_sort_currency(a,b) { 
    aa = ts_getInnerText(a.cells[SORT_COLUMN_INDEX]).replace(/[^0-9.]/g,'');
    bb = ts_getInnerText(b.cells[SORT_COLUMN_INDEX]).replace(/[^0-9.]/g,'');
    return parseFloat(aa) - parseFloat(bb);
}

function ts_sort_numeric(a,b) { 
    aa = parseFloat(ts_getInnerText(a.cells[SORT_COLUMN_INDEX]));
    if (isNaN(aa)) aa = 0;
    bb = parseFloat(ts_getInnerText(b.cells[SORT_COLUMN_INDEX])); 
    if (isNaN(bb)) bb = 0;
    return aa-bb;
}

function ts_sort_caseinsensitive(a,b) {
    aa = ts_getInnerText(a.cells[SORT_COLUMN_INDEX]).toLowerCase();
    bb = ts_getInnerText(b.cells[SORT_COLUMN_INDEX]).toLowerCase();
    if (aa==bb) return 0;
    if (aa<bb) return -1;
    return 1;
}

function ts_sort_default(a,b) {
    aa = ts_getInnerText(a.cells[SORT_COLUMN_INDEX]);
    bb = ts_getInnerText(b.cells[SORT_COLUMN_INDEX]);
    if (aa==bb) return 0;
    if (aa<bb) return -1;
    return 1;
}


function addEvent(elm, evType, fn, useCapture)
// addEvent and removeEvent
// cross-browser event handling for IE5+,  NS6 and Mozilla
// By Scott Andrew
{
  if (elm.addEventListener){
    elm.addEventListener(evType, fn, useCapture);
    return true;
  } else if (elm.attachEvent){
    var r = elm.attachEvent("on"+evType, fn);
    return r;
  } else {
    alert("Handler could not be removed");
  }
  return false;
}


/****************************************************************************/
/*  the preceding from http://www.kryogenix.org/code/browser/sorttable/     */
/****************************************************************************/



/***********************************************************************************************/
/*  the following from http://johnkerry.com/js/kerry.js  on 2004-DEC-03   function showtwo     */
/***********************************************************************************************/


function showorhidediv(theid)
{
	var el = document.getElementById(theid);
	if (el.style.display == 'none')
	{
		el.style.display = 'block';

	} else {
		el.style.display = 'none';
		
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


/**
 * Uses AJaX and <vegbank:datacart> to update the datacart items.
 */
function updateCartItem(elem, curClass) {
	ajax = initAjax();

    var fnWhenDone = function(oXML) { 
        //alert("pong");
        document.getElementById("datacart-count").innerHTML = oXML.responseText;
    };

    var params;

    if (elem.checked) { 
        params = "delta=add";
        elem.parentNode.className = 'highlight';
    } else { 
        params = "delta=drop"; 
        elem.parentNode.className = curClass;
    }

    params += "&deltaItems="+encodeURIComponent(elem.value);
    var url = "@web_context@general/get_datacart_count.ajax.jsp";

    //alert(url + "?" + params);
    ajax.connect(url, "POST", params, fnWhenDone);
}



//
// Mark all items already in datacart
//
function markDatacartItems(dsId) {
	var nodeList = document.getElementById('cartable').getElementsByTagName('input');

	if (nodeList.length > 0) {
		var first = true;
		var acString = "'";
		var i, node;
		for (i=0; i<nodeList.length; i++) {
			node = nodeList.item(i);
			if (node.type == 'checkbox') {
				// add to list
				if (first) { first = false;
				} else { acString += "','"; }
				acString += node.value;
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
	ajax = initAjax();

    var fnWhenDone = function(oXML) { 
		// oXML.responseText contains a CSV of accession codes 
		// found on this page and in the datacart
		var nodeList = document.getElementById('cartable').getElementsByTagName('input');

		datacartACs = oXML.responseText.toLowerCase();
		//alert("response: " + datacartACs);

		// mark the right boxes
		for (var i=0; i<nodeList.length; i++) {
			var node = nodeList.item(i);
			if (node.type == 'checkbox') {
				if (datacartACs.indexOf(node.value.toLowerCase()) != -1) { 
					// mark it
					node.checked = true;
					node.parentNode.className = 'highlight';
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

