@stdvegbankget_jspdeclarations@

<h2>denorm observation</h2>
<p>If no errors are presented on this page, it should have worked ok. </p>

<!-- look for wparam, if so, then do ac -->
<p> Will run the default, which is to update null denorm fields, unless another message appears here: </p>
<logic:present parameter="wparam">
  <bean:define id="denormtype" value="ac" type="java.lang.String" />
  <bean:parameter id="tempac" name="wparam" value="ERROR!notfound" />

    <p>Updating for only one observation accession code: <a href="@cite_link@<bean:write name='tempac' />"><bean:write name='tempac' /></a></p>
    <%@ include file="sub_denorm_observation.jsp" %>
  
</logic:present>


  

<logic:notPresent parameter="wparam">

  <logic:present parameter="plotacccode">
    <!-- lookup obs one by one from plot accession code-->
    <bean:parameter id="tempac" name="plotacccode" value="ERROR!notfound" />
    <p>plot ac requested, running 1 obs at a time.</p>
    <vegbank:get select="plotallid" id="plot" where="where_plot_ac" wparam="tempac" perPage="-1" pager="false" />
    
    <logic:notEmpty name="plot-BEANLIST">
    GOT SOME PLOTS!
    <logic:iterate id="onerowofplot" name="plot-BEANLIST">
        <bean:define id="wparam"><bean:write name="onerowofplot" property="observationaccessioncode" /></bean:define>
        <bean:define id="denormtype" value="ac" type="java.lang.String" />
        <p>updating: <bean:write name="onerowofplot" property="observationaccessioncode" /></p>
        <%@ include file="sub_denorm_observation.jsp" %>    
      </logic:iterate>
    </logic:notEmpty>
    
  </logic:present>
  <logic:notPresent parameter="plotacccode">
     <!-- null is default, else can specify denormtype=all on URL -->
     <logic:equal parameter="denormtype" value="all">
       <p>Updating ALL records.</p>
     </logic:equal>
     <%@ include file="sub_denorm_observation.jsp" %>
  </logic:notPresent>   


</logic:notPresent>



