<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<html>
<head>@defaultHeadToken@
<title>VegBank Simple Query</title>

<link type="text/css" href="@stylesheet@" rel="stylesheet">

<script type="text/javascript">
<!--
/*
 * STRATEGY:
 * Use 3 forms named plantform, plotform, and commform.
 * On button click, copy values to appropriate form.
 */
function doPlantQuery() {
	return validateName(document.plantform.plantname.value, 'plant');
}

function doCommQuery() {
	return validateName(document.commform.wparam.value, 'community');
}

function doPlotQuery() {
	if (document.plantform.plantname.value != "") {
		if (!validateName(document.plantform.plantname.value, 'plant')) {
			return false;
		}
	}

	if (document.commform.wparam.value != "") {
		if (!validateName(document.commform.wparam.value, 'community')) {
			return false;
		}
	}

	// set plantName
	document.plotform.plantName.value = document.plantform.plantname.value;

	// set commName
	document.plotform.commName.value = document.commform.wparam.value;

	return true;
}
			
function validateName(value, label) {
	// VALIDATE
	err = false;
	if (value == "") {
		alert("Please enter a " + label + " name.");
		err = true;

	} else if (value.length < 3) {
		alert(label + " names must be at least 3 characters long.");
		err = true;
	}

	if (err) {
		return false;
	}
	return true;
}

-->
</script>

</head>

<body>
@vegbank_header_html_normal@
<h3>VegBank Simple Query</h3>
<blockquote>
<p><span class="category">Three-in-one plot/plant/community search.</span></p>
<table  bgcolor="#FFFFFF" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td></td><form name="plantform" action="/vegbank/PlantQuery.do" method="get">
    <td bgcolor="#CCCCCC"><img src="@image_server@uplt3.gif" /></td>
    <td bgcolor="#CCCCCC"></td>
    <td bgcolor="#CCCCCC"><img src="@image_server@uprt3.gif" /></td>
    <td></td>
    <td></td>
  </tr>
  <tr>
    <td bgcolor="#99FF99"><img src="@image_server@uplt3.gif" /></td>
    <td bgcolor="#66CC66"></td>
    <td bgcolor="#66CC66"></td>
    <td bgcolor="#66CC66"></td>
    <td bgcolor="#99FF99"></td>
    <td bgcolor="#99FF99"><img src="@image_server@uprt3.gif" /></td>
  </tr>
  <tr>
    <td bgcolor="#99FF99"></td>
    <td bgcolor="#66CC66"></td>
    <td bgcolor="#66CC66" class="label_small">Plant name<br/>
	&nbsp; 
	<input type=text name="plantname" size="30"/></td>
    <td bgcolor="#66CC66"></td>
    <td bgcolor="#99FF99">&nbsp; <img src="@image_server@rtarr.gif">
              <input type="submit" value="search for plants" onClick="javascript:return doPlantQuery()" name="btnPlant"/></td>
    <td bgcolor="#99FF99"></td></form>
  </tr>
  <tr>
    <td bgcolor="#99FF99"><img src="@image_server@lwlt3.gif" /></td>
    <td bgcolor="#66CC66"></td>
    <td bgcolor="#66CC66"></td>
    <td bgcolor="#66CC66"></td>
    <td bgcolor="#99FF99"></td>
    <td bgcolor="#99FF99"><img src="@image_server@lwrt3.gif" /></td>
  </tr>
  <tr>
    <td><img src="@image_server@transparent.gif" height="6" /></td>
    <td bgcolor="#CCCCCC"></td>
    <td bgcolor="#CCCCCC"></td>
    <td bgcolor="#CCCCCC"></td>
    <td></td>
    <td></td>
  </tr>
  
  <tr>
    <td  bgcolor="#FFFFCC"><img src="@image_server@uplt3.gif" /></td>
    <td bgcolor="#CCCC99"></td>
    <td bgcolor="#CCCC99"></td>
    <td bgcolor="#CCCC99"></td>
    <td bgcolor="#FFFFCC"></td>
    <td bgcolor="#FFFFCC"><img src="@image_server@uprt3.gif" /></td>
  </tr>
  <tr><form name="commform" action="@views_link@commconcept_query.jsp" method="get">
    <td  bgcolor="#FFFFCC"></td>
    <td bgcolor="#CCCC99"></td>
    <td bgcolor="#CCCC99" class="label_small">Community name<br/>
	&nbsp; 
	<input type=text name="wparam" size="30"/></td>
    <td bgcolor="#CCCC99"></td>
    <td bgcolor="#FFFFCC">&nbsp; <img src="@image_server@rtarr.gif">
                <input type="submit" onClick="javascript:return doCommQuery()" value="search for communities" name="btnComm"></td>
    <td bgcolor="#FFFFCC">
	
</form></td>	
  </tr>
  <tr>
    <td bgcolor="#FFFFCC"><img src="@image_server@lwlt3.gif" /></td>
    <td bgcolor="#CCCC99"></td>
    <td bgcolor="#CCCC99"></td>
    <td bgcolor="#CCCC99"></td>
    <td bgcolor="#FFFFCC"></td>
    <td bgcolor="#FFFFCC"><img src="@image_server@lwrt3.gif" /></td>
  </tr>
  <tr>
    <td></td>
    <td bgcolor="#CCCCCC"></td>
    <td bgcolor="#CCCCCC"></td>
    <td bgcolor="#CCCCCC"></td>
    <td></td>
    <td></td>
  </tr>
  <!-- plotform -->
<form name="plotform" action="/vegbank/PlotQuery.do" method="get">
	<input type="hidden" name="plantName" value="">
	<input type="hidden" name="commName" value="">
  <tr>
    <td></td>
    <td bgcolor="#CCCCCC"></td>
    <td bgcolor="#CCCCCC" class="label_small">State / Province<br/>
	&nbsp; 
	<select name="state" size="1">
<option value="ANY">--ANY--</option>  
 <%@ include file="../includes/StatesList.jsp" %>

</select></td>
    <td bgcolor="#CCCCCC"></td>
    <td></td>
    <td></td>
  </tr>
  <tr>
    <td></td>
    <td bgcolor="#CCCCCC"></td>
    <td bgcolor="#CCCCCC" align="center"><img src="@image_server@downarr.gif"><br/>
		<input type="submit" onClick="javascript:return doPlotQuery()" value="search for plots"/ name="btnPlot"><br/>
              <span class="sizetiny">Plot search uses all three criteria</span></td>
    <td bgcolor="#CCCCCC"></td>
    <td></td>
    <td></td>
  </tr></form>
  <tr>
    <td></td>
    <td bgcolor="#CCCCCC"><img src="@image_server@lwlt3.gif" /></td>
    <td bgcolor="#CCCCCC"></td>
    <td bgcolor="#CCCCCC"><img src="@image_server@lwrt3.gif" /></td>
    <td></td>
    <td></td>
  </tr>
 </table>

 <br>
<p><span class="category">Want more search options?<br>
<a href="/vegbank/LoadPlotQuery.do">Try the advanced plot query</a>.
</span>
</blockquote>

<br/>
<p>&nbsp;

@vegbank_footer_html_tworow@
</body>
</html>
