 <tr><th>State/Province</th><th>Plots</th></tr>
     <logic:iterate id="onerowofnamedplace" name="namedplace-BEANLIST" >
       <!-- loop over list of states -->
       <tr class="@nextcolorclass@"><td ><bean:write name="onerowofnamedplace" property="region_name"/>&nbsp;</td>
       <td class="numeric">&nbsp;
  <bean:define id="namedplace_pk" name="onerowofnamedplace" property="namedplace_id" />
  <bean:define id="critAsTxt">
  In the <bean:write name="onerowofnamedplace" property="placesystem"/>: <bean:write name="onerowofnamedplace" property="placename"/>
  </bean:define>
  <%  
      /* create a map of parameters to pass to the new link: */
      java.util.HashMap params = new java.util.HashMap();
      params.put("wparam", namedplace_pk);
      params.put("where", "where_place_complex");
      params.put("criteriaAsText", critAsTxt);
      pageContext.setAttribute("paramsName", params);
  %>
  
  <html:link page="/views/observation_summary.jsp" name="paramsName" scope="page" >
  


<bean:write name="onerowofnamedplace" property="count_obs"/></html:link></td></tr>

     </logic:iterate>