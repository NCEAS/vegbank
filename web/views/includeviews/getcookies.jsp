  <bean:define id="plantNamesToShowBean">taxobs_curr_scinamenoauth</bean:define > <!-- default -->
  <!-- looking for full -->
  <logic:present cookie="taxon_name_full">
    <!-- getting the cookie value which IS present: -->
    <bean:cookie id="plantNamesToShowCookie" name="taxon_name_full"  value="taxobs_curr_scinamenoauth" /> 
    <!-- if cookie was set, then set to define new bean -->
    <bean:define id="plantNamesToShowBean"><bean:write name="plantNamesToShowCookie" property="value" /></bean:define>
  </logic:present>
  <!-- getcookies got: <bean:write name="plantNamesToShowBean" /> -->
  <!-- checking if that is present SHOULD BE -->
  <logic:notPresent name="plantNamesToShowBean">
     <!-- ERROR!  The bean: plantNamesToShowBean is not present, yet I can write its value: <bean:write name="plantNamesToShowBean" /> -->
  </logic:notPresent>
  
  