@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@

  
   <%
                       //**************************************************************************************
                       //  Set up alternating row colors
                       //**************************************************************************************
                       String rowClass = "evenrow";
     int inttemp=0 ; 

    %>


<title>VegBank Plots</title>



@webpage_masthead_html@
 @possibly_center@  
  <h2>VegBank Plots</h2>
  
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

<vegbank:get id="plotobs" select="plotandobservation" whereNumeric="where_observation_pk" 
    whereNonNumeric="where_observation_ac" beanName="map" pager="true" xwhereEnable="true" 
    allowOrderBy="true"/>

<vegbank:pager />

<logic:empty name="plotobs-BEANLIST">
                Sorry, no plot-observations are available.
          </logic:empty>
<logic:notEmpty name="plotobs-BEANLIST"><!-- set up table -->

<TABLE width="100%" class="leftrightborders" cellpadding="2">
<tr><th>More</th>

<bean:define id="thisfield" value="stateprovince" />
<bean:define id="fieldlabel">Location</bean:define>
<%@ include file="../includes/orderbythisfield.jsp" %>

<bean:define id="thisfield" value="authorobscode" />
<bean:define id="fieldlabel">Author Code</bean:define>
<%@ include file="../includes/orderbythisfield.jsp" %>

<bean:define id="thisfield" value="area" />
<bean:define id="fieldlabel">Size</bean:define>
<%@ include file="../includes/orderbythisfield.jsp" %>

<bean:define id="thisfield" value="elevation" />
<bean:define id="fieldlabel">Elev</bean:define>
<%@ include file="../includes/orderbythisfield.jsp" %>

<bean:define id="thisfield" value="obsstartdate" />
<bean:define id="fieldlabel">Year</bean:define>
<%@ include file="../includes/orderbythisfield.jsp" %>


<th>Community</th><th colspan="3">Some Plants</th></tr>
<logic:iterate id="onerowofobservation" name="BEANLIST"><!-- iterate over all records in set : new table for each -->
<bean:define id="onerowofplot" name="onerowofobservation" />
<bean:define id="obsId" name="onerowofplot" property="observation_id"/>
<bean:define id="plot_pk" name="onerowofplot" property="plot_id"/>
<bean:define id="observation_pk" name="onerowofplot" property="observation_id"/>

<!-- start of plot & obs fields-->

<tr class='@nextcolorclass@'>
<td><a href='@get_link@comprehensive/observation/<bean:write name="onerowofobservation" property="observation_id" />'>details</a></td>

<!-- plot data -->
<td><bean:write name="onerowofobservation" property="stateprovince" />,<br/>
    <bean:write name="onerowofobservation" property="country" /></td>
<td><bean:write name="onerowofobservation" property="authorobscode" /><!--<br/>
    <bean:write name="onerowofobservation" property="observationaccessioncode" />--></td>
<td class="numeric"><bean:write name="onerowofobservation" property="area" />&nbsp;</td>
<td class="numeric"><bean:write name="onerowofobservation" property="elevation" />&nbsp;</td>

<td class="numeric" title='<bean:write name="onerowofobservation" property="obsstartdate"/>'><dt:format pattern="yyyy">
                <dt:parse pattern="yyyy-MM-dd">
                    <bean:write name="onerowofobservation" property="obsstartdate_datetrunc"/>
                </dt:parse>
            </dt:format>&nbsp;</td>

<!-- community info -->
<vegbank:get id="comminterpretation" select="comminterpretation_withobs" beanName="map" 
  where="where_observation_pk" wparam="obsId" perPage="-1" pager="false"/>
  <logic:empty name="comminterpretation-BEANLIST">
    <td>No data.</td>
  </logic:empty>
  <logic:notEmpty name="comminterpretation-BEANLIST">
    <td>
      
        <logic:iterate length="3" id="onerowofcomminterpretation" name="comminterpretation-BEANLIST"><!-- iterate over all records in set : new table for each -->
          <logic:notEmpty name="onerowofcomminterpretation" property="commconcept_id">
            
            &raquo;  <a href='@get_link@std/commclass/<bean:write name="onerowofcomminterpretation" property="commclass_id" />'><bean:write name="onerowofcomminterpretation" property="commconcept_id_transl" /></a>
            
          <br/></logic:notEmpty><!-- concept -->
        </logic:iterate>
      
    </td>
  </logic:notEmpty>


<!-- plants in this plot -->

<vegbank:get id="taxonobservation" select="taxonobservation_maxcover" where="where_taxonobservation_maxcover" wparam="observation_pk"
  pager="false" perPage="-1" beanName="map" />
  <logic:empty name="taxonobservation-BEANLIST">
    <td>Error! No data.</td>
  </logic:empty>
  <logic:notEmpty name="taxonobservation-BEANLIST">
    <td>
     
         <% inttemp = 0 ;%>
       
         <!-- iterate over all records in set : new table for each -->
         <logic:iterate length="5" id="onerowoftaxonobservation" name="taxonobservation-BEANLIST">
		   <% if (inttemp!=0) { %> ; <% }  %>
		   
		    <%  inttemp ++ ;  %>
		        <a href='@get_link@std/taxonobservation/<bean:write name="onerowoftaxonobservation" property="taxonobservation_id" />'>
				  <bean:write name="onerowoftaxonobservation" property="authorplantname" />
					<logic:notEmpty name="onerowoftaxonobservation" property="maxcover">
					  (<bean:write name="onerowoftaxonobservation" property="maxcover" />%)
					</logic:notEmpty>
				</a>
         </logic:iterate><!-- tax obs -->
      
    </td>
  </logic:notEmpty><!-- concept -->


</tr>
</logic:iterate><!-- plot -->
</TABLE>
<vegbank:pager />

</logic:notEmpty>

<br>
@webpage_footer_html@


