@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@
<title>VegBank Simple Search</title>
<script type="text/javascript">
<!--
/*
 * STRATEGY:
 * Use 3 forms named plantform, plotform, and commform.
 * On button click, copy values to appropriate form.
 */
function doPlantQuery() {
	return validateName(document.plantform.xwhereParams_plantname_0.value, 'plant');
}
function doCommQuery() {
	return validateName(document.commform.xwhereParams_commname_0.value, 'community');
}
function doPlotQuery() {
	if (document.plantform.xwhereParams_plantname_0.value != "") {
		if (!validateName(document.plantform.xwhereParams_plantname_0.value, 'plant')) {
			return false;
		}
	}
	if (document.commform.xwhereParams_commname_0.value != "") {
		if (!validateName(document.commform.xwhereParams_commname_0.value, 'community')) {
			return false;
		}
	}
	// set plantName
	document.plotform.plantName.value = document.plantform.xwhereParams_plantname_0.value;
	// set commName
	document.plotform.commName.value = document.commform.xwhereParams_commname_0.value;
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
@webpage_masthead_html@
<h3>Simple Search</h3>
<blockquote>
<p><span class="category">Three-in-one plot/plant/community search.</span></p>
<table  bgcolor="#FFFFFF" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td></td><form name="plantform" action="@views_link@plantconcept_summary.jsp" method="get">
    
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
	<input type="text" name="xwhereParams_plantname_0" size="30"/></td>
    <td bgcolor="#66CC66"></td>
    <td bgcolor="#99FF99">&nbsp; <img src="@image_server@rtarr.gif">
              <input type="hidden" name="where" value="where_plantconcept_mpq" />
			  <input type="hidden" name="xwhereParams_plantname_1" value="pu.plantname" />
			  <input type="hidden" name="matchType" value="all" />
			  <input type="hidden" name="xwhereKey_plantname" value="xwhere_match" />
			  <input type="hidden" name="xwhereSearch_plantname" value="true" />
			  <input type="hidden" name="xwhereMatchAny_plantname" value="false" />
              <input type="hidden" name="xwhereGlue" value="AND" />
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
  <tr><form name="commform" action="@views_link@commconcept_summary.jsp" method="get">
    <td  bgcolor="#FFFFCC"></td>
    <td bgcolor="#CCCC99"></td>
    <td bgcolor="#CCCC99" class="label_small">Community name<br/>
	&nbsp; 
	
	<input type=text name="xwhereParams_commname_0" size="30"/></td>
    <td bgcolor="#CCCC99"></td>
    <td bgcolor="#FFFFCC">&nbsp; <img src="@image_server@rtarr.gif">
                <input type="hidden" name="matchType" value="all" /> 
                <input type="hidden" name="where" value="where_commconcept_mpq" />
				<input type="hidden" name="xwhereParams_commname_1" value="cu.commname" />
				<input type="hidden" name="xwhereKey_commname" value="xwhere_match" />
				<input type="hidden" name="xwhereSearch_commname" value="true" />
				<input type="hidden" name="xwhereMatchAny_commname" value="false" />
				<input type="hidden" name="xwhereGlue" value="AND" />
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
    <td bgcolor="#CCCCCC" class="label_small">State / Province <br/><span class="psmall">(list shows plot count)</span><br/>
	&nbsp; 
	<select name="state" size="1">
<option value="ANY">--ANY--</option>  
 
   <vegbank:get id="plotstatelist" select="plotstatelist" 
 		  beanName="map" pager="false" where="empty" 
 		  wparam="" perPage="-1" />
 		<logic:empty name="plotstatelist-BEANLIST">
 		  <option value="No states found">Error: no states found</option>
 		</logic:empty>
 		<logic:notEmpty name="plotstatelist-BEANLIST">
 		  <logic:iterate id="onerowofplotstatelist" name="plotstatelist-BEANLIST">
 		     <option value='<bean:write name="onerowofplotstatelist" property="stateprovince" />' >  
    <bean:write name="onerowofplotstatelist" property="stateprovince" />
    (<bean:write name="onerowofplotstatelist" property="countstate" />)
 		      
 		     </option>
 		  </logic:iterate>
         </logic:notEmpty>
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
<a href="/vegbank/LoadPlotQuery.do">Try the advanced plot search</a>.
</span>
</blockquote>
<br/>
@webpage_footer_html@
