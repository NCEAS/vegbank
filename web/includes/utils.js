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

function VBNav_viewObsCode(targ,accCode) {
  eval(targ+".location='/vegbank/servlet/DataRequestServlet?requestDataType=vegPlot&resultType=summary&queryType=simple&accessionCode="+accCode+"'");
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

