<!-- this file set the default styles to use for jsp, and edits them according to cookie's contents -->
  <bean:define id="plantNamesToShowBean">taxobs_curr_scinamenoauth</bean:define > <!-- default -->
  <!-- looking for full -->
  <logic:present cookie="taxon_name_full">
    <!-- getting the cookie value which IS present: -->
    <bean:cookie id="plantNamesToShowCookie" name="taxon_name_full"  value="taxobs_curr_scinamenoauth" /> 
    <!-- if cookie was set, then set to define new bean -->
    <bean:define id="plantNamesToShowBean"><bean:write name="plantNamesToShowCookie" property="value" /></bean:define>
  </logic:present>
  <!-- DEBUG: getcookies got: plantNamesToShowBean: <bean:write name="plantNamesToShowBean" /> -->

  <!-- stems: -->
  <bean:define id="stemTableToShowBean">false</bean:define > <!-- default -->
    <!-- looking for full -->
    <logic:present cookie="show_stemtable">
      <!-- getting the cookie value which IS present: -->
      <bean:cookie id="stemTableToShowCookie" name="show_stemtable"  value="false" /> 
      <!-- if cookie was set, then set to define new bean -->
      <bean:define id="stemTableToShowBean"><bean:write name="stemTableToShowCookie" property="value" /></bean:define>
    </logic:present>
  <!-- DEBUG: getcookies got: stemTableToShowBean: <bean:write name="stemTableToShowBean" /> -->
  
  <bean:define id="stemGraphicToShowBean">true</bean:define > <!-- default -->
    <!-- looking for full -->
    <logic:present cookie="show_stemgraphic">
      <!-- getting the cookie value which IS present: -->
      <bean:cookie id="stemGraphicToShowCookie" name="show_stemgraphic"  value="true" /> 
      <!-- if cookie was set, then set to define new bean -->
      <bean:define id="stemGraphicToShowBean"><bean:write name="stemGraphicToShowCookie" property="value" /></bean:define>
    </logic:present>
  <!-- DEBUG: getcookies got: stemGraphicToShowBean: <bean:write name="stemGraphicToShowBean" /> -->
  
  <style type="text/css">
   .<bean:write name="plantNamesToShowBean"  />  {visibility: visible;}
  <logic:notEqual name="plantNamesToShowBean"  value="taxobs_authorplantname">.taxobs_authorplantname { display:none; } </logic:notEqual>
  <logic:notEqual name="plantNamesToShowBean"  value="taxobs_orig_scinamewithauth">.taxobs_orig_scinamewithauth { display:none; }</logic:notEqual>
  <logic:notEqual name="plantNamesToShowBean"  value="taxobs_orig_scinamenoauth">.taxobs_orig_scinamenoauth { display:none; }</logic:notEqual>
  <logic:notEqual name="plantNamesToShowBean"  value="taxobs_orig_code">.taxobs_orig_code { display:none; }</logic:notEqual>
  <logic:notEqual name="plantNamesToShowBean"  value="taxobs_orig_common">.taxobs_orig_common { display:none; }</logic:notEqual>
  <logic:notEqual name="plantNamesToShowBean"  value="taxobs_curr_scinamewithauth">.taxobs_curr_scinamewithauth { display:none; }</logic:notEqual>
  <logic:notEqual name="plantNamesToShowBean"  value="taxobs_curr_scinamenoauth">.taxobs_curr_scinamenoauth { display:none; } </logic:notEqual>
  <logic:notEqual name="plantNamesToShowBean"  value="taxobs_curr_code">.taxobs_curr_code { display:none; }</logic:notEqual>
  <logic:notEqual name="plantNamesToShowBean"  value="taxobs_curr_common">.taxobs_curr_common { display:none; }</logic:notEqual>
 
  /* stems */ 
  <logic:equal name="stemTableToShowBean" value="false"> .table_stemsize { display:none;} </logic:equal> 
  <logic:equal name="stemGraphicToShowBean" value="false"> .stemsize_graphic { display:none;} </logic:equal> 
  </style>