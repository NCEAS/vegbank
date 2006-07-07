
/*************************************************************************************************/
/*  the following from http://www.kryogenix.org/code/browser/sorttable/   on about 2004-DEC-01   */
/*  but it has been heavily adapted to suit our needs                                            */
/*************************************************************************************************/

// this has been known to cause firefox tables to lose their lines, if there are rowspans
addEvent(window, "load", sortables_init);

var SORT_COLUMN_INDEX;

function sortables_init() {
    // Find all tables with class sortable and make them sortable
    if (!document.getElementsByTagName) return;
    tbls = document.getElementsByTagName("table");
    for (ti=0;ti<tbls.length;ti++) {
        thisTbl = tbls[ti];
        if (((thisTbl.className).indexOf("sortable") != -1) ) {
            //initTable(thisTbl.id);
            if (thisTbl.getAttribute("id").length>0) ts_makeSortable(thisTbl);
        }
    }
}

function ts_makeSortable(table) {
    if (table.rows && table.rows.length > 0) {
        //check for first 3 rows to see if one row's class is "sortthisrow", if so, sort that row, not top one
        var firstRowNum = 0; //default 0
        
        //how many rows to look at for "sortbythisrow" className.  See 10, unless table shorter than that.
        var lookAtHowManyRows = Math.min(10,table.rows.length);
        
        for (var rowI=0;rowI<lookAtHowManyRows;rowI++){
            if (table.rows[rowI].className.indexOf("sortbythisrow") != -1) {
                firstRowNum = rowI;
            }
        }
        var firstRow = table.rows[firstRowNum];
    }
    if (!firstRow) return;

    // We have a first row: assume it's the header, and make its contents clickable links
    for (var i=0;i<firstRow.cells.length;i++) {
        var cell = firstRow.cells[i];
        //var txt = ts_getInnerText(cell);
        // var extralink = ts_getExtraLink(cell);
        var txtHTML = cell.innerHTML;
        //always explicitly state column then row number:
        var linkStart = '<a href="#" title="Click here to sort by this column" class="sortlink" onclick="ts_resortTable(this, '+i+','+firstRowNum+');return false;">';
        var linkEnd = '<span class="sortarrow"></span></a>';
        // look for <a
        var whereA = txtHTML.indexOf("<a");
        //DEBUG: alert ('exists: ' + whereA);
        if (whereA == -1) {
			// there are no links, span entire cell
			//DEBUG: alert('could not find <a in :' + txtHTML);
			cell.innerHTML = linkStart + txtHTML + linkEnd;
		}
		if (whereA != -1) {
			// there are links, add link up to point where new link starts
			//DEBUG: alert('<a starts @ ' + whereA + ' in ' + txtHTML);
			if (whereA == 0 ) {
				alert ('<a starts:' + txtHTML);
				// add a little something since the start of the whole thing is a link
				linkStart = linkStart + 'sort';
			}
			// insert links up to point where next link starts
			cell.innerHTML = linkStart + txtHTML.substring(0,whereA) + linkEnd + txtHTML.substring(whereA,txtHTML.length);

		}
        //cell.innerHTML = 'sort' + cell.innerHTML ;
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

function ts_resortTable(lnk, cellIndex, headerRowIndex) {
    // get the span
    var span;
    var spanColl = lnk.getElementsByTagName("span");
    // dEBUG: &(spanColl.length);
    for (var ci=0;ci<spanColl.length;ci++) {
        if (spanColl[ci].className && spanColl[ci].className.indexOf('sortarrow') != -1) {
			span = spanColl[ci];
		}
    }
    var spantext = ts_getInnerText(span);
    var td = getParent(lnk,"th");
    // try it the old way for Safari
   // var td2 = lnk.parentNode;
    
    var column = cellIndex ; // just use the passed cell index or column index
    
    var table = getParent(td,'TABLE');

    // Work out a type for the column
    if (table.rows.length <= 1) return;
    // problem: sometimes the cells are hidden and this causes problems with this approach:
    //  var itm = ts_getInnerText(table.rows[1].cells[column]);
    var itm= "";
    var cr = headerRowIndex + 1;  //start on next row after headerRowIndex passed
    //get a value from the table, and try to get one that isn't empty.
    //alert('column is:' + column);
    // default:
    SORT_COLUMN_INDEX = column;
    
    //loop through rows and columns to try to get the first piece of data, to see what type of data it is:
    // loop rows:
    do
	  {
        // loop through columns to find the one we are sorting based on
        for (co=column;co<table.rows[cr].cells.length;co++) {
			if (  ( column == co)) {
				//set to co for over all col
                SORT_COLUMN_INDEX = co;
                //if (cr==1) alert('set item!');
				itm = ts_getInnerText(table.rows[cr].cells[co]);
                //alert("item: " + itm);
                // get rid of junk.
				//itm = itm.replace(/\n/,"");
				//alert('>' + itm + '<');
				// regular expressions aren't working in FireFox...

				if (isTextNotEmpty(itm) == false) {
					// itm is empty!
					itm = "";
			    }
			}
	    }

	    // debug: alert('itm inside: >>' + itm + '<< cr:' + cr + ' of ' + table.rows.length);
	    
         cr++;
	  }
    
    while (itm =="" && cr<table.rows.length)



    // alert('sorting by value: >' + itm + '<' + ' read from row #: ' + cr + ' and column:' + SORT_COLUMN_INDEX);
    sortfn = ts_sort_caseinsensitive;
    if (itm.match(/^\d\d[\/-]\d\d[\/-]\d\d\d\d$/)) sortfn = ts_sort_date;
    if (itm.match(/^\d\d[\/-]\d\d[\/-]\d\d$/)) sortfn = ts_sort_date;
    if (itm.match(/^[\$]/)) sortfn = ts_sort_currency;
    if (itm.match(/^[\d\.]+$/)) sortfn = ts_sort_numeric;
    if (itm.match(/^[\s]*[\d\.]+[ \%]*/)) sortfn = ts_sort_numeric;
     //alert('sorting function is: ' + sortfn);
    // SORT_COLUMN_INDEX = column;
   // var firstRow = new Array();
    var newRows = new Array();
   // for (i=0;i<table.rows[headerRowIndex].length;i++) { firstRow[i] = table.rows[0][i]; }
   // alert("firstRow is this long: " + firstRow.length);
    var startSortingAtRowNum = headerRowIndex + 1; //start sorting at next row after header that was passed
    for (j=( startSortingAtRowNum );j<table.rows.length;j++) { 
        // alert('adding row:' + j);
        newRows[j- startSortingAtRowNum] = table.rows[j]; 
        }
    newRows.sort(sortfn);

    // alert("ok so far");
    if (span) {
		// dEBUG: &('we have a span');
		// dEBUG: &('span innerHTML:' + span.innerHTML);
	}

    if (span && span.className.indexOf('up') != -1) {
		// dEBUG: &("it is an up arrow!");
        ARROW = '&darr;';
        newRows.reverse();
        span.className = span.className.replace('up','down');
        if (span.className.indexOf('down')==-1) {
			//add downarrow to class name
			span.className=span.className + ' downarrow';
		}
    } else {
		// dEBUG: &("it is NOT an up arrow");
        ARROW = '&uarr;';
        if (span) {
		  span.className=span.className.replace('down','up');
          if (span.className.indexOf('uparrow')==-1) {
			//add downarrow to class name
			span.className=span.className + ' uparrow';
		  }
		} else {
			// add a span to IE
			// alert('adding a span');
			ARROW = '<span class="sortarrow uparrow">&uarr;</span>';
		}
    }

    // We appendChild rows that already exist to the tbody, so it moves them rather than creating new ones
    // don't do sortbottom rows
    for (i=0;i<newRows.length;i++) { 
          //alert('looking at row:' + i);
          if (!newRows[i].className || (newRows[i].className && (newRows[i].className.indexOf('sortbottom') == -1))) {
                table.tBodies[0].appendChild(newRows[i]);
          }
    }

    // do sortbottom rows only
     for (i=0;i<newRows.length;i++) { 
          if (newRows[i].className && (newRows[i].className.indexOf('sortbottom') != -1)) {
              table.tBodies[0].appendChild(newRows[i]);
          }
     }

    // Delete any other arrows there may be showing
    var allspans = getParent(td,"TR").getElementsByTagName("span");
    if (allspans.length == 0 ) {
        //backup attempt to get them:
        allspans = table.getElementsByTagName("span");
    }
    //alert('got spans in TR:' + allspans.length);
    for (ci=0;ci<allspans.length;ci++) {
        if (allspans[ci].className.indexOf('sortarrow') != -1) {
            if (getParent(allspans[ci],"table") == getParent(lnk,"table")) { // in the same table as us?
     		     allspans[ci].innerHTML = ''
                // DO NOT TRY TO REMOVE uparrow and downarrow classes here!
            }
        }
    }

    if (span) {
      span.innerHTML = ARROW;
    } else {
	  lnk.innerHTML = lnk.innerHTML + ARROW;
	}
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
