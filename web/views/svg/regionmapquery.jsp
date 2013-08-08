@stdvegbankget_jspdeclarations@
@webpage_head_html@

<!-- only good for one field! -->
<logic:present parameter="wparam">
  <bean:parameter id="wparamBean" name="wparam" value="one field" />
</logic:present>

<title>Mapping by region - VegBank </title>
<script type="text/javascript" src="clickable-svg.js?version=1.8"></script>
<script type="text/javascript">

function getHelpPageId() {
  return "svg-map";
}
</script>
 <style type="text/css" id="mainsvgss">
    .main {background-color:#FFFFCC;}
    div.svgdiv {opacity:0.9;font-family:calibri, verdana, tahoma, sans-serif;
      padding:2px;
      border:solid 1px #000000;}
    input, textarea,select,p {opacity:0.99 ! important;}
    .draggable, .draggableParent { cursor:crosshair;}

    #pagecaption-abs{position:absolute;top:3px;left:250px;}
    #pagecaption {position:relative;float:left;display:none;width:250px;padding:0px;border:none 0px #000000;background-color:#FFFFFF;}\
    #pagecaption-container {z-index:800;font-size:14pt;opacity:1;position:absolute;top:3px;left:550px;display:none;}
    #pagecaption-display {border:none;}
    #nav {position:relative;float:left;width:200px;clear:left;}
    #nav-abs{position:absolute;left:3px;top:3px;}
    #instructions {width:550px;}
    #instructions-abs {position:absolute;left:253px;top:3px;}
    #instructions {position:relative;float:left;}
    .boxcaption {font-variant: small-caps;font-weight:bold; border:0px;padding:0px;}

    #selectbycountydiv {background-color:#FFFFCC;display:none;width:350px;float:left;}
    #selectbycountydiv-abs{position:absolute;left:215px;top:3px;}

    #linkInstructions {z-index:900; display:none;width:700px;left:30px;top:30px;position:absolute;}
    #linkInstructions-abs {position:absolute;left:3px;top:200px;}

    #sv {position:fixed;top:0px;left:0px;width:100%;height:100%;}
    #debuglog {overflow:scroll;display:none;float:right;width:250px;height:100px;font-size:8pt;}
    #noscriptmsg {position:absolute;top:44%;border:solid 1px #FF0000;background-color:#000000;font-size:48pt;color:#FF0000;}
    a.closelink {float:right}


    .colorLegend {opacity:1;border: 1px solid #333333;width:10px;height:10px;position:relative;float:left;clear:left;margin:3px;}
    #colorLegendDiv {display:none;opacity:1;float:right;position:relative;top:500px;}
    .colorLegendText {float:left;border:0px;}
    .colorLegendWrap {float:left;border:0px;clear:left;}

    a.showonlyonhover {font-size:8pt;text-decoration:none;color:#000000;}
    a.showonlyonhover:link {opacity:0;}
    a.showonlyonhover:visited {opacity:0;}
    a.showonlyonhover:hover{opacity:1;}
  </style>


@webpage_masthead_small_html@

<!-- get scope and grain -->
<bean:parameter id="beanscope" name="scope" value="n" />
<bean:parameter id="beangrain" name="grain" value="state" />

<embed id="sv" src="<%= beanscope %>-<%= beangrain %>.svg" ></embed>


<div class="svgdiv"  id="debuglog"></div>
<!--
452 wide x 399 tall
199 wide x 176 tall
-->
<div id="largeCapture" style="display:none;position:absolute;left:88px;top:52px;width:452px;height:399px;border:1px #000 solid;"> </div>

<div id="smallCapture" style="display:none;position:absolute;left:175px;top:124px;width:199px;height:176px;border:1px #f00 solid;"> </div>

<noscript><div class="svgdiv"  id="noscriptmsg">I need javascripting enabled to work!</div></noscript>

<!-- check for request to do a db summary, and if so, run it -->
<bean:parameter id="beandbsummary" name="dbsummary" value="n" />
<logic:equal name="beandbsummary" value="y">
<vegbank:get id="namedplace" select="browsenamedplacebystatequery" beanName="map"
    pager="false" perPage="-1"   />
  <logic:notEmpty name="namedplace-BEANLIST">
	         <!-- loop over list of states -->

         <bean:define id="beanlistlocs">
         <logic:iterate id="onerowofnamedplace" name="namedplace-BEANLIST" ><bean:define id="currCountOfObs" name="onerowofnamedplace" property="obscount" type="java.lang.String" /><bean:write name="onerowofnamedplace" property="placecode"/>:<%
	           if (Integer.parseInt(currCountOfObs) > 3000) {
	              %>6<%
	           } else if (Integer.parseInt(currCountOfObs) >= 1000) {
	              %>5<%
	           } else if (Integer.parseInt(currCountOfObs) >= 250) {
			   	  %>4<%
	           } else if (Integer.parseInt(currCountOfObs) >= 100) {
			   	  %>3<%
	           } else if (Integer.parseInt(currCountOfObs) >= 50) {
			   	  %>2<%
	           } else if (Integer.parseInt(currCountOfObs) >= 1) {
			   	  %>1<%
	           }
	         %>;</logic:iterate></bean:define>





  </logic:notEmpty>
</logic:equal>


<div id="savedparamdiv" style="display:none;">
  <textarea cols="100" rows="10" id="newParams"></textarea>
  <form id="savedparamform">
    This is a form.
   <input id="savedparamform-1" name="1" type="text"></input>
   <input id="savedparamform-2" name="2" type="text"></input>
   <input id="savedparamform-3" name="3" type="text"></input>
   <input id="savedparamform-4" name="4" type="text"></input>
   <input id="savedparamform-5" name="5" type="text"></input>
   <input id="savedparamform-6" name="6" type="text"></input>
   <input id="savedparamform-7" name="7" type="text"></input>
   <input id="savedparamform-8" name="8" type="text"></input>
   <input id="savedparamform-9" name="9" type="text"></input>
   <input id="savedparamform-10" name="10" type="text"></input>
   <input id="savedparamform-11" name="11" type="text"></input>
   <input id="savedparamform-12" name="12" type="text"></input>
   <input id="savedparamform-13" name="13" type="text"></input>
   <input id="savedparamform-14" name="14" type="text"></input>
   <input id="savedparamform-15" name="15" type="text"></input>
   <input id="savedparamform-16" name="16" type="text"></input>
   <input id="savedparamform-17" name="17" type="text"></input>
   <input id="savedparamform-18" name="18" type="text"></input>
   <input id="savedparamform-19" name="19" type="text"></input>
   <input id="savedparamform-20" name="20" type="text"></input>

   <input id="savedparamform-ccap1" name="ccap1" type="text"></input>
   <input id="savedparamform-ccap2" name="ccap2" type="text"></input>
   <input id="savedparamform-ccap3" name="ccap3" type="text"></input>
   <input id="savedparamform-ccap4" name="ccap4" type="text"></input>
   <input id="savedparamform-ccap5" name="ccap5" type="text"></input>
   <input id="savedparamform-ccap6" name="ccap6" type="text"></input>
   <input id="savedparamform-ccap7" name="ccap7" type="text"></input>
   <input id="savedparamform-ccap8" name="ccap8" type="text"></input>
   <input id="savedparamform-ccap9" name="ccap9" type="text"></input>
   <input id="savedparamform-ccap10" name="ccap10" type="text"></input>
   <input id="savedparamform-ccap11" name="ccap11" type="text"></input>
   <input id="savedparamform-ccap12" name="ccap12" type="text"></input>
   <input id="savedparamform-ccap13" name="ccap13" type="text"></input>
   <input id="savedparamform-ccap14" name="ccap14" type="text"></input>
   <input id="savedparamform-ccap15" name="ccap15" type="text"></input>
   <input id="savedparamform-ccap16" name="ccap16" type="text"></input>
   <input id="savedparamform-ccap17" name="ccap17" type="text"></input>
   <input id="savedparamform-ccap18" name="ccap18" type="text"></input>
   <input id="savedparamform-ccap19" name="ccap19" type="text"></input>
   <input id="savedparamform-ccap20" name="ccap20" type="text"></input>

   <input id="savedparamform-colorCount" name="colorCount" type="text"></input>
   <input id="savedparamform-zoom" name="zoom" type="text"></input>
   <input id="savedparamform-monochrome" name="monochrome" type="text"></input>
   <input id="savedparamform-hidden" name="hidden" type="text"></input>
   <input id="savedparamform-cap" name="cap" type="text"></input>
   <input id="savedparamform-capx" name="capx" type="text"></input>
   <input id="savedparamform-capy" name="capy" type="text"></input>
   <input id="savedparamform-ccapx" name="ccapx" type="text"></input>
   <input id="savedparamform-ccapy" name="ccapy" type="text"></input>

   <input id="savedparamform-colors" name="colors" type="text"></input>
   <input id="savedparamform-ccapbgcolor" name="ccapbgcolor" type="text"></input>
   <input id="savedparamform-ccaptitle" name="ccaptitle" type="text"></input>
   <input id="savedparamform-rotate" name="rotate" type="text"></input>

   <!-- scope, grain, location -->
   <input id="savedparamform-scope" name="scope" type="text"></input>
   <input id="savedparamform-location" name="location" type="text"></input>
   <input id="savedparamform-grain" name="grain" type="text"></input>
   <input id="savedparamform-readonly" name="readonly" type="text"></input>


   <input id="savedparamform-listlocs" name="listlocs" type="text"></input>
   <input id="savedparamform-dbsummary" name="dbsummary" type="text"></input>

   <input id="savedparamform-smallCapture" name="smallCapture" type="text"></input>
   <input id="savedparamform-largeCapture" name="largeCapture" type="text"></input>



  </form>

   <div id="dblookup"><bean:write ignore="true" name="beanlistlocs" filter="false" /></div>

  </div>
  <!--<div style="display:none;"><input id="x" />x <input id="y" />y <input id="zoom" />z</div>-->


<!--    #######################     INSTRUCTIONS    #######################       -->
<div id="instructions" class="main svgdiv">

<div class="boxcaption draggableParent">Instructions
<a class="closelink" title="close instructions" href="#" onclick="document.getElementById('instructions').style.display='none'; return false;">x</a>
<!--<br/>
THIS IS A TEST DOCUMENT!!!  THERE MAY BE GLITCHES!-->
</div>

Click on counties to shade colors and even save by bookmark or saving the web address.  Firefox, Safari, Chrome, Opera are all OK.  <b>IE does not work</b>!
Feedback/suggestion poll: <script type="text/javascript">
  var PDF_surveyID = "CA3F60DBF3731E95";
  var PDF_openText = "View Survey";
</script>
<script type="text/javascript" src="http://polldaddy.com/s.js"></script>
<noscript><a href="http://surveys.polldaddy.com/s/CA3F60DBF3731E95/">View Survey</a></noscript>
<br/>
Version 1.8 Feb 9, 2011 <a href="about.html">terms|about</a>
</div>


<!-- ################## Navigation and configuration ######################### -->
<div id="nav" class="main svgdiv">

  <div class="boxcaption draggableParent" title="Drag this box around the header if you want, but do so slowly!">
  Control Box
  <a class="closelink"
    title="for screenshots, hide this box until you click something on the map" href="#"
    onclick="showHideNav(false);return false;">x</a>
  </div>
  <table cellpadding="0" border="0" cellspacing="0">
  <tr><td colspan="3">
  Colors:
  <select title="select the number of color categories you'd like to use" id="maxcolorcount" onchange="maxColorsIsNow(this.value)" ><option>1</option><option>2</option><option>3</option><option>4</option><option selected="selected">5</option><option>6</option><option>7</option><option>8</option><option>9</option><option>10</option><option>11</option><option>12</option><option>13</option><option>14</option><option>15</option><option>16</option><option>17</option><option>18</option><option>19</option><option>20</option></select>
  <!--<input type="button" value="test" onclick="testThis()" />-->
  </td><td>

  <input type="button" onclick="editCaption()" value="caption..." />
  </td>
  </tr>

  <tr><td colspan="3">
  <input title="start over with a blank map" type="button" value="clear map" onclick="clearMap()" /></td>
  <td><input type="button" value="save map..." title="click to see options for saving" onclick="buildMapLink()" /></td></tr>
  <tr><td></td><td><input title="pan north, which moves the map down" type="button" value="&uarr;" onclick="moveMap(0,100);" /></td>
  <td></td>
  <td rowspan="2"><input title="Zoom in, making areas on the map larger" type="button" value="zoom in (+)" onclick="adjustZoom(getDefaultZoom())" /><br/>
                  <input title="Zoom out, showing a larger area on the map" type="button" value="zoom out (-)" onclick="adjustZoom(-1 * getDefaultZoom())" /></td></tr>
  <tr><td><input  title="pan west, which moves the map right" type="button" value="&larr;" onclick="moveMap(100,0);" />
  <td><input title="reset zoom and location of map to default" type="button" value="o" onclick="resetToDefault();" /></td>
  <td><input  title="pan east, which moves the map left" type="button" value="&rarr;" onclick="moveMap(-100,0);" /></tr>
  <tr><td></td><td><input title="pan south, which moves the map up" type="button" value="&darr;" onclick="moveMap(0,-100);" /></td><td></td>
  <td><select title="select a predefined location and zoom level" id="zoomPreset" onchange="moveMapByPicklist(this.value);">
  <option value="">zoom to...</option>
  <option value="-4000,-1600,9.8">NC</option>
  <option value="-2900,-1000,7.4">VA/NC/SC</option>
  <option value="-1000,-500,3.8">southeast</option>
  <option value="-500,0,2.4">EAST</option>
  <option value="200,0,1.8">U.S.</option>
  </select></td></tr>
  <tr><td colspan="5"><input type="checkbox" id="mapPrecisionMovement" />more precision</td></tr>
  </table>
  <!-- show county area -->
  <div class="nodrag" id="showcurrentcounty">Current region: <br/><span id="currentcountyonly">(none)</span><br/><input onclick="document.getElementById('selectbycountydiv').style.display='block';" value="select by name" type="button" /></div>
  <textarea class="nodrag" id='err' style='display:none; background-color:#FF9999;'>Error in shading region(s): </textarea>
</div>


<!-- ###################  select by name ############################ -->
<div id="selectbycountydiv" class="main svgdiv nodrag">
  <div class="boxcaption">Select region by name

  <a class="closelink" href="#" onclick="document.getElementById('selectbycountydiv').style.display='none';return false;">x</a>
  </div>

  <div class="stategrain">
  <select id="selectState" size="10" onclick="svg_adjustById(this.value)"><option value="">--select state / province--</option><option value="AL">Alabama</option><option value="AK">Alaska</option><option value="AB">Alberta</option><option value="AZ">Arizona</option><option value="AR">Arkansas</option><option value="BC">British Columbia</option><option value="CA">California</option><option value="CO">Colorado</option><option value="CT">Connecticut</option><option value="DE">Delaware</option><option value="DC">District Of Columbia</option><option value="FL">Florida</option><option value="GA">Georgia</option><option value="HI">Hawaii</option><option value="ID">Idaho</option><option value="IL">Illinois</option><option value="IN">Indiana</option><option value="IA">Iowa</option><option value="KS">Kansas</option><option value="KY">Kentucky</option><option value="LA">Louisiana</option><option value="ME">Maine</option><option value="MB">Manitoba</option><option value="MD">Maryland</option><option value="MA">Massachusetts</option><option value="MI">Michigan</option><option value="MN">Minnesota</option><option value="MS">Mississippi</option><option value="MO">Missouri</option><option value="MT">Montana</option><option value="NE">Nebraska</option><option value="NV">Nevada</option><option value="NB">New Brunswick</option><option value="NH">New Hampshire</option><option value="NJ">New Jersey</option><option value="NM">New Mexico</option><option value="NY">New York</option><option value="NL">Newfoundland and Labrador</option><option value="NC">North Carolina</option><option value="ND">North Dakota</option><option value="NT">Northwest Territories</option><option value="NS">Nova Scotia</option><option value="NU">Nunavut</option><option value="OH">Ohio</option><option value="OK">Oklahoma</option><option value="ON">Ontario</option><option value="OR">Oregon</option><option value="PA">Pennsylvania</option><option value="PE">Prince Edward Island</option><option value="QC">Quebec</option><option value="RI">Rhode Island</option><option value="SK">Saskatchewan</option><option value="SC">South Carolina</option><option value="SD">South Dakota</option><option value="TN">Tennessee</option><option value="TX">Texas</option><option value="UT">Utah</option><option value="VT">Vermont</option><option value="VA">Virginia</option><option value="WA">Washington</option><option value="WV">West Virginia</option><option value="WI">Wisconsin</option><option value="WY">Wyoming</option><option value="YT">Yukon</option></select>
  </div>

  <div class="countygrain">
  <select style="float:right;" size="10" id="selectByName" onclick="svg_adjustById(this.value)"></select>
  <select id="selectState" onchange="loadState(this.value)"><option value="">--first select state--</option><option value="AL">Alabama</option><option value="AK">Alaska</option><option value="AZ">Arizona</option><option value="AR">Arkansas</option><option value="CA">California</option><option value="CO">Colorado</option><option value="CT">Connecticut</option><option value="DE">Delaware</option><option value="DC">District Of Columbia</option><option value="FL">Florida</option><option value="GA">Georgia</option><option value="HI">Hawaii</option><option value="ID">Idaho</option><option value="IL">Illinois</option><option value="IN">Indiana</option><option value="IA">Iowa</option><option value="KS">Kansas</option><option value="KY">Kentucky</option><option value="LA">Louisiana</option><option value="ME">Maine</option><option value="MD">Maryland</option><option value="MA">Massachusetts</option><option value="MI">Michigan</option><option value="MN">Minnesota</option><option value="MS">Mississippi</option><option value="MO">Missouri</option><option value="MT">Montana</option><option value="NE">Nebraska</option><option value="NV">Nevada</option><option value="NH">New Hampshire</option><option value="NJ">New Jersey</option><option value="NM">New Mexico</option><option value="NY">New York</option><option value="NC">North Carolina</option><option value="ND">North Dakota</option><option value="OH">Ohio</option><option value="OK">Oklahoma</option><option value="OR">Oregon</option><option value="PA">Pennsylvania</option><option value="RI">Rhode Island</option><option value="SC">South Carolina</option><option value="SD">South Dakota</option><option value="TN">Tennessee</option><option value="TX">Texas</option><option value="UT">Utah</option><option value="VT">Vermont</option><option value="VA">Virginia</option><option value="WA">Washington</option><option value="WV">West Virginia</option><option value="WI">Wisconsin</option><option value="WY">Wyoming</option></select>
  <br/>Then click county names (click again to change color, as on the map)&rarr;
  </div>
  <!--<br/><input style= "position:relative;top:95%" type="button" value="close" onclick="document.getElementById('selectbycountydiv').style.display='none';" />-->

</div>

<!-- ###################  page caption ############################ -->
<div id="pagecaption-container" class="main svgdiv">
<div id="pagecaption-drag1" style="float:left;" class="draggableParent boxcaption" ><a class="showonlyonhover" href="#" onclick="return false;">drag<br/>this</a></div>
<div title="page caption: can be dragged around the screen" id="pagecaption-display"
  ondblclick="editASpan(this);" style="float:left;"  class="autoeditable_content">--Page Caption (double-click to edit)--</div>
<div id="pagecaption-close" style="float:left;" class="boxcaption" ><a class="showonlyonhover" href="#" onclick="hideCaption();return false;">close<br/>caption</a></div>
</div>


<div id="pagecaption" class="main svgdiv">
<div id="pagecaption-edit" class="main svgdiv">
<div class="boxcaption">Add/Edit Page Caption
  <a href="#" onclick="closeCaptionBox();return false;" class="closelink">x</a>
</div>


Once you click save, you can drag the caption around.
<!--<textarea id="pagecaption-input" style="width:100%;" ></textarea>-->
<!--<input type="button" value="save" onclick="saveCaption(false);" />-->
<!--<input type="button" value="encode" onclick="alert(Url.encode(document.getElementById('pagecaption-display').innerHTML));" />-->
<div id="pagecaption-editfont">

<select id="font" onchange="captionEditFont(this.value)">
  <!--<option>Sans Serif</option>
  <option>Serif</option>
  <option>Wide</option>
  <option>Narrow</option>-->
  <option value="">--font--</option>
  <option>Comic Sans MS</option>
  <option>Courier New</option>
  <option>Garamond</option>
  <option>Georgia</option>
  <option>Tahoma</option>
  <option>Trebuchet MS</option>
  <option>Verdana</option>
</select>
<select id="fontsize" onchange="captionEditFontSize(this.value)">
  <option value="">--font size--</option>
  <option value="12">small</option>
  <option value="14">normal</option>
  <option value="18">bigger</option>
  <option value="28">large</option>
  <option value="36">HUGE</option>
  <option value="-1">Other...</option>
</select>
HTML is also OK, such a &lt;i&gt;<i>for italics</i>&lt;/i&gt;, &lt;b&gt;<b>for bold</b>&lt;/b&gt;.
<!--
Background: <input type="checkbox" onchange="captionShowBackground(this.value)"></input>
Border: <input type="checkbox" onchange="captionShowBorder(this.value)"></input>
-->
</div><!-- end edit font-->
<input type="button" value="close this" onclick="closeCaptionBox();" />
<!--<input type="button" value="cancel" onclick="cancelEditCaption();" />-->
<input type="button" value="hide caption" onclick="hideCaption();" />
</div>
</div>


<div id="colorLegendDiv" class="main svgdiv">
<div class="draggableParent boxCaption">Color Legend</div>
<div class="colorLegendWrap" id="colorLegend1"><div class="img1 colorLegend"></div><div class="colorLegendText autoeditable_content" ondblclick="editASpan(this);">Color 1</div></div>
<div class="colorLegendWrap" id="colorLegend2"><div class="img2 colorLegend"></div><div class="colorLegendText autoeditable_content" ondblclick="editASpan(this);">Color 2</div></div>
<div class="colorLegendWrap" id="colorLegend3"><div class="img3 colorLegend"></div><div class="colorLegendText autoeditable_content" ondblclick="editASpan(this);">Color 3</div></div>
<div class="colorLegendWrap" id="colorLegend4"><div class="img4 colorLegend"></div><div class="colorLegendText autoeditable_content" ondblclick="editASpan(this);">Color 4</div></div>
<div class="colorLegendWrap" id="colorLegend5"><div class="img5 colorLegend"></div><div class="colorLegendText autoeditable_content" ondblclick="editASpan(this);">Color 5</div></div>
<div class="colorLegendWrap" id="colorLegend6"><div class="img6 colorLegend"></div><div class="colorLegendText autoeditable_content" ondblclick="editASpan(this);">Color 6</div></div>
<div class="colorLegendWrap" id="colorLegend7"><div class="img7 colorLegend"></div><div class="colorLegendText autoeditable_content" ondblclick="editASpan(this);">Color 7</div></div>
<div class="colorLegendWrap" id="colorLegend8"><div class="img8 colorLegend"></div><div class="colorLegendText autoeditable_content" ondblclick="editASpan(this);">Color 8</div></div>
<div class="colorLegendWrap" id="colorLegend9"><div class="img9 colorLegend"></div><div class="colorLegendText autoeditable_content" ondblclick="editASpan(this);">Color 9</div></div>
<div class="colorLegendWrap" id="colorLegend10"><div class="img10 colorLegend"></div><div class="colorLegendText autoeditable_content" ondblclick="editASpan(this);">Color 10</div></div>
<div class="colorLegendWrap" id="colorLegend11"><div class="img11 colorLegend"></div><div class="colorLegendText autoeditable_content" ondblclick="editASpan(this);">Color 11</div></div>
<div class="colorLegendWrap" id="colorLegend12"><div class="img12 colorLegend"></div><div class="colorLegendText autoeditable_content" ondblclick="editASpan(this);">Color 12</div></div>
<div class="colorLegendWrap" id="colorLegend13"><div class="img13 colorLegend"></div><div class="colorLegendText autoeditable_content" ondblclick="editASpan(this);">Color 13</div></div>
<div class="colorLegendWrap" id="colorLegend14"><div class="img14 colorLegend"></div><div class="colorLegendText autoeditable_content" ondblclick="editASpan(this);">Color 14</div></div>
<div class="colorLegendWrap" id="colorLegend15"><div class="img15 colorLegend"></div><div class="colorLegendText autoeditable_content" ondblclick="editASpan(this);">Color 15</div></div>
<div class="colorLegendWrap" id="colorLegend16"><div class="img16 colorLegend"></div><div class="colorLegendText autoeditable_content" ondblclick="editASpan(this);">Color 16</div></div>
<div class="colorLegendWrap" id="colorLegend17"><div class="img17 colorLegend"></div><div class="colorLegendText autoeditable_content" ondblclick="editASpan(this);">Color 17</div></div>
<div class="colorLegendWrap" id="colorLegend18"><div class="img18 colorLegend"></div><div class="colorLegendText autoeditable_content" ondblclick="editASpan(this);">Color 18</div></div>
<div class="colorLegendWrap" id="colorLegend19"><div class="img19 colorLegend"></div><div class="colorLegendText autoeditable_content" ondblclick="editASpan(this);">Color 19</div></div>
<div class="colorLegendWrap" id="colorLegend20"><div class="img20 colorLegend"></div><div class="colorLegendText autoeditable_content" ondblclick="editASpan(this);">Color 20</div></div>
</div>


<!--     #######################       THE SAVE/ LINK AREA  ####################### -->

<div id="linkInstructions" class="main svgdiv">
  <div class="boxcaption">Saving this map
  <a href="#" onclick="showLink(false);return false;" class="closelink">x</a>
  </div>
  You can <strong>save a web address </strong> to get back to the map you see now, saving the highlighted areas,
  zoom, and map view.  You may do so by bookmarking the link below, or copying this web address text.

    <textarea class="nodrag" style="width:550px;height:150px;" type="text" id="textLink" readonly="readonly" /></textarea><br/>
    <a id="tryLink" target="_blank" href="#">Bookmark this web address or click to test the link in new window.</a>
    <br/>
    NEW! <a id="tryLinkMonochrome" target="_blank" href="#">Try map in MONOCHROME</a>
    <br/>Or you can <a href="#" id="shortenURL" onclick="sniploc();return false;">shorten this web address with snipURL.com</a>.
    <br/><br/>
  <strong>Save the map as an image </strong> by taking a screenshot of this page.
  While I'm not endorsing any of these tools, you can use a program called SnagIt to take a screenshot, or press
  <b>PrntScrn</b> (PrintScreen) on your keyboard (Windows).
  You might <b>press F11</b> before pressing PrntScrn as this will put the browser in full-screen mode, removing browser buttons, toolbar, etc. from the screenshot.
  Press F11 again to make your browser return to normal mode.
  This saves an image to the clipboard, where you can paste
  it into a program like MS-Paint (I recommend saving as a .png file instead of .jpg).<br/><br/>
  Or, you can use extensions/add-ons such as
  <a target="_blank" href="https://addons.mozilla.org/en-US/firefox/addon/1146">ScreenGrab</a> or
  <a target="_blank" href="https://addons.mozilla.org/en-US/firefox/addon/5648">Fireshot</a> to right-click and
  save the visible browser area as an images (I recommend the .png format).  For Fireshot, you
  will need to disable the Flash capture in the preferences menu.
  <br/><br/>
  Before a screen capture, you might want to close the yellow boxes on the screen (with the close button or the x in the upper right).  Highlighting something on the map will bring back the main control box.
    <br/><input type="button" value="close" onclick="showLink(false);" />
  </div>





@webpage_footer_small_html@
