<!-- this file gets observations from the parameters of the URL (or posted, either way) -->

<div id="tut_plotcriteriamessages">
  <!-- special case for linking from NatureServeExplorer : -->
<logic:equal parameter="where" value="where_observation_nsuid">
  <p>Welcome to VegBank.  You've clicked a link from <a href="http://natureserve.org/explorer">NatureServe Explorer</a>
  requesting to see the plots in VegBank that our users have linked to a particular community, which is: <br/>

    <vegbank:get id="commconcept" select="commconcept" where="where_commconcept_nsuid" 
     beanName="map" pager="false" xwhereEnable="false" perPage="-1"/>

    <logic:notEmpty name="commconcept-BEANLIST">
      <logic:iterate id="onerowofcommconcept" name="commconcept-BEANLIST">
        <a href='@get_link@std/commconcept/<bean:write name="onerowofcommconcept" property="commconcept_id" />'><bean:write name="onerowofcommconcept" property="commname" /></a>
      </logic:iterate>
    </logic:notEmpty>
    <logic:empty name="commconcept-BEANLIST">
      Sorry, VegBank has no record of the NatureServe community requested: 
      <bean:parameter id="beanwparam" name="wparam" />
      <bean:write name="beanwparam" />
    </logic:empty>
  </p>
</logic:equal>

<!-- tell the user what was searched for, if criteriaAsText is passed here : -->
<logic:present parameter="criteriaAsText">
  <bean:parameter id="bean_criteriaAsText" name="criteriaAsText" />
  <logic:notEmpty name="bean_criteriaAsText">
    <p class="psmall">You searched for plots: <bean:write name="bean_criteriaAsText" /></p>


  </logic:notEmpty>
</logic:present>  
</div>


<vegbank:get id="plotobs" select="plotandobservation" whereNumeric="where_observation_pk" 
    whereNonNumeric="where_observation_ac" beanName="map" pager="true" xwhereEnable="true" 
    allowOrderBy="true" />  <!-- save="plot-search-results" /-->
