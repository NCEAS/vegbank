<!--Where statement removed from preceding: -->
<!-- can ask for only total plots -->
<!--<logic:empty name="browseparty-BEANLIST">
<p>  Sorry, no parties found (error!)</p>
</logic:empty> -->
<logic:notEmpty name="browseparty-BEANLIST">
<table class="leftrightborders" cellpadding="2">
<tr>
<!-- don't show party name if already shown on master view -->
<logic:notEqual name="partyShown" value="yes">
<th>Party</th>
</logic:notEqual>
          <th><logic:notEqual name="onlytotalplots" value="yes">Total </logic:notEqual>Plots</th><!-- doesn't need to say total if that's all that's happening on this form -->

<logic:notEqual name="onlytotalplots" value="yes">

          <th>Observation Contribution Plots</th>
          <th>Classified Plots</th>
          <th>Project Contribution Plots</th>
</logic:notEqual>                
                  
 </tr>

<logic:iterate id="onerowofbrowseparty" name="browseparty-BEANLIST">
  <bean:define property="party_id" name="onerowofbrowseparty" id="party_pk"/>
<tr class="@nextcolorclass@">
<!-- don't show party name if already shown on master view -->
<logic:notEqual name="partyShown" value="yes">
<td>
  <a href='@get_link@std/party/<bean:write name="onerowofbrowseparty" property="party_id" />'><bean:write name="onerowofbrowseparty" property="party_id_transl" /></a>
</td>
</logic:notEqual>
<td class="numeric">
  <logic:notEmpty name="onerowofbrowseparty" property="countallcontrib">
          <bean:define id="critAsTxt">
            with <bean:write name="onerowofbrowseparty" property="party_id_transl"/> as Contributor.
          </bean:define>
          <%  
              /* create a map of parameters to pass to the new link: */
              java.util.HashMap params = new java.util.HashMap();
              params.put("wparam", party_pk);
              params.put("where", "where_obs_allparty");
              params.put("criteriaAsText", critAsTxt);
              pageContext.setAttribute("paramsName", params);
          %>

          <html:link page="/views/observation_summary.jsp" name="paramsName" scope="page" >
            <bean:write name="onerowofbrowseparty" property="countallcontrib" />
          </html:link>


  </logic:notEmpty>
  <logic:empty name="onerowofbrowseparty" property="countallcontrib">
    0
  </logic:empty>
</td>


<logic:notEqual name="onlytotalplots" value="yes">
         

<td class="numeric">
  <logic:notEmpty name="onerowofbrowseparty" property="countobscontrib">
          <bean:define id="critAsTxt">
            with <bean:write name="onerowofbrowseparty" property="party_id_transl"/> was Observation Contributor.
          </bean:define>
          <%  
              /* create a map of parameters to pass to the new link: */
              java.util.HashMap params = new java.util.HashMap();
              params.put("wparam", party_pk);
              params.put("where", "where_obs_obscontrib");
              params.put("criteriaAsText", critAsTxt);
              pageContext.setAttribute("paramsName", params);
          %>

          <html:link page="/views/observation_summary.jsp" name="paramsName" scope="page" >
            <bean:write name="onerowofbrowseparty" property="countobscontrib" />
          </html:link>    
  
  </logic:notEmpty>
  <logic:empty name="onerowofbrowseparty" property="countobscontrib">
    0
  </logic:empty>
</td>

<td class="numeric">
  <logic:notEmpty name="onerowofbrowseparty" property="countclasscontrib">
          <bean:define id="critAsTxt">
            with <bean:write name="onerowofbrowseparty" property="party_id_transl"/> was Classifier.
          </bean:define>
          <%  
              /* create a map of parameters to pass to the new link: */
              java.util.HashMap params = new java.util.HashMap();
              params.put("wparam", party_pk);
              params.put("where", "where_obs_classcontrib");
              params.put("criteriaAsText", critAsTxt);
              pageContext.setAttribute("paramsName", params);
          %>

          <html:link page="/views/observation_summary.jsp" name="paramsName" scope="page" >
            <bean:write name="onerowofbrowseparty" property="countclasscontrib" />
          </html:link> 

  </logic:notEmpty>
  <logic:empty name="onerowofbrowseparty" property="countclasscontrib">
    0
  </logic:empty>
</td>

<td class="numeric">
  <logic:notEmpty name="onerowofbrowseparty" property="countprojectcontrib">
          <bean:define id="critAsTxt">
            with <bean:write name="onerowofbrowseparty" property="party_id_transl"/> was Project Contributor.
          </bean:define>
          <%  
              /* create a map of parameters to pass to the new link: */
              java.util.HashMap params = new java.util.HashMap();
              params.put("wparam", party_pk);
              params.put("where", "where_obs_projcontrib");
              params.put("criteriaAsText", critAsTxt);
              pageContext.setAttribute("paramsName", params);
          %>

          <html:link page="/views/observation_summary.jsp" name="paramsName" scope="page" >
            <bean:write name="onerowofbrowseparty" property="countprojectcontrib" />
          </html:link> 

  </logic:notEmpty>
  <logic:empty name="onerowofbrowseparty" property="countprojectcontrib">
    0
  </logic:empty>
</td>

</logic:notEqual>  <!-- only total -->


</logic:iterate>
</table>
</logic:notEmpty>