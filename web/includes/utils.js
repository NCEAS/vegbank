function toggle(e) {
	if (e.checked) {
		highlight(e);
	} else {
		unhighlight(e);
	}
}

function checkAll(checkboxName) {
	var form = document.DownLoadManagerForm;
	var len = form.elements.length;
	for (var i = 0; i < len; i++) {
		var e = form.elements[i];
		if (e.name == checkboxName) {
			check(e);
		}
	}
}

function clearAll(checkboxName) {
	var form = document.DownLoadManagerForm;
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
	var form = document.DownLoadManagerForm;
	var len = form.elements.length;
	for (var i = 0; i < len; i++) {
		var e = form.elements[i];
		if (e.name == checkboxName && e.checked == true) {
			highlight(e);
		}
	}
}

function VBNav_viewObsCode(targ,accCode) {
  eval(targ+".location='/vegbank/servlet/DataRequestServlet?requestDataType=vegPlot&resultType=summary&queryType=simple&accessionCode="+accCode+"'");
}


function MM_jumpMenu(targ,selObj,restore){ //v3.0
  eval(targ+".location='"+selObj.options[selObj.selectedIndex].value+"'");
  if (restore) selObj.selectedIndex=0;
}