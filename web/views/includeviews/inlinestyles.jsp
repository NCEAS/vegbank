<!-- this file set the default styles to use for jsp, and edits them according to cookie's contents -->
  <%@ include file="getcookies.jsp" %>
  
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
 
  </style>