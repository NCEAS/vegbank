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
	return validateName(document.commform.communityName.value, 'community');
}

function doPlotQuery() {
	if (document.plantform.plantname.value != "") {
		if (!validateName(document.plantform.plantname.value, 'plant')) {
			return false;
		}
	}

	if (document.commform.communityName.value != "") {
		if (!validateName(document.commform.communityName.value, 'community')) {
			return false;
		}
	}

	// set plantName
	document.plotform.plantName.value = document.plantform.plantname.value;

	// set commName
	document.plotform.commName.value = document.commform.communityName.value;

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
  <tr><form name="commform" action="/vegbank/servlet/DataRequestServlet" method="get">
    <td  bgcolor="#FFFFCC"></td>
    <td bgcolor="#CCCC99"></td>
    <td bgcolor="#CCCC99" class="label_small">Community name<br/>
	&nbsp; 
	<input type=text name="communityName" size="30"/></td>
    <td bgcolor="#CCCC99"></td>
    <td bgcolor="#FFFFCC">&nbsp; <img src="@image_server@rtarr.gif">
                <input type="submit" onClick="javascript:return doCommQuery()" value="search for communities" name="btnComm"></td>
    <td bgcolor="#FFFFCC"><input type="hidden" name="requestDataType" value="vegCommunity">
	<input type="hidden" name="requestDataFormatType" value="html">
	<input type="hidden" name="clientType" value="browser">
	<input type="hidden" name="communityLevel" value="%">
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
<option value="AG">Aguascalientes</option>
<option value="AL">Alabama</option>
<option value="AK">Alaska</option>
<option value="AB">Alberta</option>
<option value="AS">American Samoa</option>
<option value="AZ">Arizona</option>

<option value="AR">Arkansas</option>
<option value="AE">Armed Forces Africa (AE)</option>
<option value="AA">Armed Forces Americas (AA)</option>
<option value="AE">Armed Forces Canada (AE)</option>
<option value="AE">Armed Forces Europe (AE)</option>
<option value="AE">Armed Forces Middle East (AE)</option>
<option value="AP">Armed Forces Pacific (AP)</option>
<option value="BN">Baja California Norte</option>
<option value="BS">Baja California Sur</option>

<option value="BC">British Columbia</option>
<option value="CA">California</option>
<option value="CP">Campeche</option>
<option value="CS">Chiapas</option>
<option value="CI">Chihuahua</option>
<option value="CH">Coahuila</option>
<option value="CL">Colima</option>
<option value="CO">Colorado</option>
<option value="CT">Connecticut</option>

<option value="DE">Delaware</option>
<option value="DC">District Of Columbia</option>
<option value="DF">Districto Federal</option>
<option value="DG">Durango</option>
<option value="FL">Florida</option>
<option value="GA">Georgia</option>
<option value="GU">Guam</option>
<option value="GJ">Guanajuato</option>
<option value="GE">Guerrero</option>

<option value="HI">Hawaii</option>
<option value="HD">Hidalgo</option>
<option value="ID">Idaho</option>
<option value="IL">Illinois</option>
<option value="IN">Indiana</option>
<option value="IA">Iowa</option>
<option value="JA">Jalisco</option>
<option value="KS">Kansas</option>
<option value="KY">Kentucky</option>

<option value="LA">Louisiana</option>
<option value="ME">Maine</option>
<option value="MB">Manitoba</option>
<option value="MH">Marshall Islands</option>
<option value="MD">Maryland</option>
<option value="MA">Massachusetts</option>
<option value="MI">Michigan</option>
<option value="MN">Minnesota</option>
<option value="MS">Mississippi</option>

<option value="MO">Missouri</option>
<option value="MT">Montana</option>
<option value="NE">Nebraska</option>
<option value="NV">Nevada</option>
<option value="NB">New Brunswick</option>
<option value="NH">New Hampshire</option>
<option value="NJ">New Jersey</option>
<option value="NM">New Mexico</option>
<option value="NY">New York</option>

<option value="NF">Newfoundland</option>
<option value="NC">North Carolina</option>
<option value="ND">North Dakota</option>
<option value="MP">Northern Mariana Is</option>
<option value="NT">Northwest Territories</option>
<option value="NS">Nova Scotia</option>
<option value="OH">Ohio</option>
<option value="OK">Oklahoma</option>
<option value="ON">Ontario</option>

<option value="OR">Oregon</option>
<option value="PW">Palau</option>
<option value="PA">Pennsylvania</option>
<option value="PE">Prince Edward Island</option>
<option value="PQ">Province du Quebec</option>
<option value="PU">Puebla</option>
<option value="PR">Puerto Rico</option>
<option value="QE">Quer&#233;taro</option>

<option value="QI">Quintana Roo</option>
<option value="RI">Rhode Island</option>
<option value="SL">San Luis Potos&#237;</option>
<option value="SK">Saskatchewan</option>
<option value="SI">Sinaloa</option>
<option value="SO">Sonora</option>
<option value="SC">South Carolina</option>
<option value="SD">South Dakota</option>
<option value="TB">Tabasco</option>

<option value="TA">Tamaulipas</option>
<option value="TN">Tennessee</option>
<option value="TX">Texas</option>
<option value="TL">Tlaxcala</option>
<option value="UT">Utah</option>
<option value="VC">Veracruz</option>
<option value="VT">Vermont</option>
<option value="VI">Virgin Islands</option>
<option value="VA">Virginia</option>

<option value="WA">Washington</option>
<option value="WV">West Virginia</option>
<option value="WI">Wisconsin</option>
<option value="WY">Wyoming</option>
<option value="YU">Yucat&#225;n</option>
<option value="YT">Yukon Territory</option>
<option value="ZA">Zacatecas</option>

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
