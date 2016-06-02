@stdvegbankget_jspdeclarations@
<!-- this is the plot-obs plotlevel jsp -->

 <vegbank:get id="plotobs" select="plotandobservation" whereNumeric="where_observation_pk"
    whereNonNumeric="where_observation_ac" beanName="map" pager="true"/>


<logic:notEmpty name="plotobs-BEAN">
	<!-- NOPE logic:notEmpty name="plotobs-BEANLIST" NOPE--><!-- set up table -->
	<!-- NOPE logic:iterate id="onerowofobservation" name="BEANLIST" NOPE--><!-- iterate over all records in set : new table for each -->
	  <bean:define id="onerowofobservation" name="plotobs-BEAN" />
	  <bean:define id="onerowofplot" name="onerowofobservation" />
	  <bean:define id="observation_pk" name="onerowofplot" property="observation_id"/>
	  <bean:define id="plot_pk" name="onerowofplot" property="plot_id"/>


	<bean:define id="observation_pk" name="onerowofplot" property="observation_id"/>
	  <bean:define id="plot_pk" name="onerowofplot" property="plot_id"/>

	<div class="padded">
	<table width="100%" class="leftrightborders" cellpadding="2"><!--each field, only write when HAS contents-->

		<tr><th class="major" colspan="2">
				<bean:write name="onerowofplot" property="authorplotcode"/>
		</th></tr>

	  @mark_datacart_items@

	<tr class="@nextcolorclass@"><td colspan="2">&raquo; Citation URL: <a href='/cite/<bean:write name="onerowofplot" property="observationaccessioncode" />'>http://vegbank.org/cite/<bean:write name="onerowofplot" property="observationaccessioncode" /></a>
	<br/>&raquo; <a href="@general_link@cite.html">Citing info</a></td></tr>
	<tr><th>Plot ID Fields:</th><th>&nbsp;</th></tr>
	<bean:define id="hadData" value="false" />
	<%@ include file="autogen/plot_plotidlong_data.jsp" %>
	<%@ include file="autogen/observation_plotidlong_data.jsp" %>
	<%@ include file="includeviews/sub_haddata.jsp" %>

	<!-- custom, show children if there are any -->
	 <logic:greaterThan name="onerowofplot" property="countchildplots" value="0">
	   <tr class="@nextcolorclass@"><td class="datalabel">Has Sub Plots</td>
		 <td>
		   <a href="@get_link@summary/observation/<bean:write name='onerowofplot' property='plot_id' />?where=where_plot_childrenof">
			 <bean:write name="onerowofplot" property="countchildplots" /> plot(s)
		   </a>
		 </td>
	   </tr>
	 </logic:greaterThan>
	<tr><th>Location Fields:</th>
	  <th>MAP:
	  <logic:notEmpty name='onerowofplot' property='latitude'>
	    <logic:notEmpty name='onerowofplot' property='longitude'>
			  <bean:define id="trunclat"><bean:write name='onerowofplot' property='latitude' /></bean:define>
			  <bean:define id="trunclong"><bean:write name='onerowofplot' property='longitude' /></bean:define>
			  <% if (trunclat.length() > 7 ) { trunclat = trunclat.substring(0,7); } %>
			  <% if (trunclong.length() > 7 ) { trunclong = trunclong.substring(0,7); } %>
			  <a title="Google maps (new window)" target="_new" href="http://maps.google.com/maps?q=<bean:write name='onerowofplot' property='latitude' />,<bean:write name='onerowofplot' property='longitude' />+(plot+<bean:write name='onerowofplot' property='authorplotcode' />)">Google</a>
			  | <a title="Yahoo maps (new window)" target="_new" href="http://maps.yahoo.com/#mvt=m&lat=<bean:write name='onerowofplot' property='latitude' />&lon=<bean:write name='onerowofplot' property='longitude' />">Yahoo</a>
			  | <a title="TopoZone provides a close up map of the plot's location (new window)" target="_new" href="http://www.topozone.com/map.asp?lat=<bean:write name='onerowofplot' property='latitude' />&lon=<bean:write name='onerowofplot' property='longitude' />&datum=nad83&u=5">TopoZone</a>
			  | <a title="MapQuest provides a more general map of the plot's location (new window)" target="_new" href="http://www.mapquest.com/maps/map.adp?searchtype=address&formtype=latlong&latlongtype=decimal&latitude=<%= trunclat %>&longitude=<%= trunclong %>">MapQuest</a>
    	  </logic:notEmpty>
	  </logic:notEmpty>
	  <logic:empty name='onerowofplot' property='latitude'>
	    n/a
	  </logic:empty>
	  </th></tr>
	<bean:define id="hadData" value="false" />
	<%@ include file="autogen/plot_plotloclong_data.jsp" %>
	<%@ include file="autogen/observation_plotloclong_data.jsp" %>
	<%@ include file="includeviews/sub_place.jsp" %>
	<%@ include file="includeviews/sub_haddata.jsp" %>
	<tr><th>Layout Fields:</th><th>&nbsp;</th></tr>
	<bean:define id="hadData" value="false" />
	<%@ include file="autogen/plot_plotlayoutlong_data.jsp" %>
	<%@ include file="autogen/observation_plotlayoutlong_data.jsp" %>
	<%@ include file="includeviews/sub_haddata.jsp" %>
	<tr><th>Environment Fields:</th><th>&nbsp;</th></tr>
	<bean:define id="hadData" value="false" />
	<%@ include file="autogen/plot_plotenvlong_data.jsp" %>
	<%@ include file="autogen/observation_plotenvlong_data.jsp" %>
	<%@ include file="includeviews/sub_haddata.jsp" %>
	<tr><th>Methods Fields:</th><th>&nbsp;</th></tr>
	<bean:define id="hadData" value="false" />
<!-- customize dates with less than "exact" accuracy -->

<logic:notEmpty name="onerowofobservation" property="obsstartdate">
<tr class="@nextcolorclass@">
<td class="observation_obsstartdate datalabel" title="The date of the observation, or the first day if the observation spanned more than one day.">Observation Start Date <a title="Click here for definition of this field." target="_blank" class="image" href="/dd/observation/obsstartdate" onclick="popupDD('/dd/observation/obsstartdate'); return false;"><span class="ddlink"><img src="@images_link@question.gif" alt="?" border="0"></span></a></td><td class="observation_obsstartdate">
<logic:notEmpty name="onerowofobservation" property="obsstartdate_datetrunc">
<span title="<bean:write name='onerowofobservation' property='obsstartdate' />">



<logic:equal name="onerowofobservation" property="dateaccuracy" value="One year">
    @subst_lt@dt:format pattern="yyyy"@subst_gt@
  	  @subst_lt@dt:parse pattern="yyyy-MM-dd"@subst_gt@
  	<bean:write name="onerowofobservation" property="obsstartdate_datetrunc"></bean:write>
  	   @subst_lt@/dt:parse@subst_gt@
	   @subst_lt@/dt:format@subst_gt@
</logic:equal>

<!-- have to next notEquals so we get things to print only once -->
<logic:notEqual name="onerowofobservation" property="dateaccuracy" value="One year">
    <logic:equal name="onerowofobservation" property="dateaccuracy" value="Three years">
        @subst_lt@dt:format pattern="yyyy"@subst_gt@
      	  @subst_lt@dt:parse pattern="yyyy-MM-dd"@subst_gt@
      	<bean:write name="onerowofobservation" property="obsstartdate_datetrunc"></bean:write>
      	   @subst_lt@/dt:parse@subst_gt@
    	   @subst_lt@/dt:format@subst_gt@  
    </logic:equal>
    <logic:notEqual name="onerowofobservation" property="dateaccuracy" value="Three years">
          <logic:equal name="onerowofobservation" property="dateaccuracy" value="Ten years">
	          @subst_lt@dt:format pattern="yyyy"@subst_gt@
	        	  @subst_lt@dt:parse pattern="yyyy-MM-dd"@subst_gt@
	        	<bean:write name="onerowofobservation" property="obsstartdate_datetrunc"></bean:write>
	        	   @subst_lt@/dt:parse@subst_gt@
	      	   @subst_lt@/dt:format@subst_gt@  
          </logic:equal>
          <logic:notEqual name="onerowofobservation" property="dateaccuracy" value="Ten years">
          @subst_lt@dt:format pattern="dd-MMM-yyyy"@subst_gt@
  	  @subst_lt@dt:parse pattern="yyyy-MM-dd"@subst_gt@
  	<bean:write name="onerowofobservation" property="obsstartdate_datetrunc"></bean:write>
  	   @subst_lt@/dt:parse@subst_gt@
	   @subst_lt@/dt:format@subst_gt@
	   </logic:notEqual> <!-- not 10 years -->
    </logic:notEqual> <!-- not 3 years -->  
</logic:notEqual> <!-- not 1 year -->
<logic:notEmpty name="onerowofobservation" property="dateaccuracy">
<logic:notEqual name="onerowofobservation" property="dateaccuracy" value="Exact">
   &plusmn; <bean:write name="onerowofobservation" property="dateaccuracy"/>	
</logic:notEqual>	
</logic:notEmpty>
	 </span>
</logic:notEmpty>
<logic:empty name="onerowofobservation" property="obsstartdate">
<logic:notEqual parameter="textoutput" value="true">&nbsp;</logic:notEqual>
</logic:empty>
</td>
</tr>
<bean:define id="hadData" value="true"></bean:define>
</logic:notEmpty><logic:notEmpty name="onerowofobservation" property="obsenddate">
<tr class="@nextcolorclass@">
<td class="observation_obsenddate datalabel" title="If the observation event spanned more than a single day, this is the last day on which observations were made.">Observation End Date <a title="Click here for definition of this field." target="_blank" class="image" href="/dd/observation/obsenddate" onclick="popupDD('/dd/observation/obsenddate'); return false;"><span class="ddlink"><img src="@images_link@question.gif" alt="?" border="0"></span></a></td><td class="observation_obsenddate">
<logic:notEmpty name="onerowofobservation" property="obsenddate_datetrunc">
<span title="<bean:write name='onerowofobservation' property='obsenddate' />">
	  <logic:equal name="onerowofobservation" property="dateaccuracy" value="One year">
	      @subst_lt@dt:format pattern="yyyy"@subst_gt@
	    	  @subst_lt@dt:parse pattern="yyyy-MM-dd"@subst_gt@
	    	<bean:write name="onerowofobservation" property="obsenddate_datetrunc"></bean:write>
	    	   @subst_lt@/dt:parse@subst_gt@
	  	   @subst_lt@/dt:format@subst_gt@
	  </logic:equal>
	  
	  <!-- have to next notEquals so we get things to print only once -->
	  <logic:notEqual name="onerowofobservation" property="dateaccuracy" value="One year">
	      <logic:equal name="onerowofobservation" property="dateaccuracy" value="Three years">
	          @subst_lt@dt:format pattern="yyyy"@subst_gt@
	        	  @subst_lt@dt:parse pattern="yyyy-MM-dd"@subst_gt@
	        	<bean:write name="onerowofobservation" property="obsenddate_datetrunc"></bean:write>
	        	   @subst_lt@/dt:parse@subst_gt@
	      	   @subst_lt@/dt:format@subst_gt@  
	      </logic:equal>
	      <logic:notEqual name="onerowofobservation" property="dateaccuracy" value="Three years">
	            <logic:equal name="onerowofobservation" property="dateaccuracy" value="Ten years">
	  	          @subst_lt@dt:format pattern="yyyy"@subst_gt@
	  	        	  @subst_lt@dt:parse pattern="yyyy-MM-dd"@subst_gt@
	  	        	<bean:write name="onerowofobservation" property="obsenddate_datetrunc"></bean:write>
	  	        	   @subst_lt@/dt:parse@subst_gt@
	  	      	   @subst_lt@/dt:format@subst_gt@  
	            </logic:equal>
	            <logic:notEqual name="onerowofobservation" property="dateaccuracy" value="Ten years">
	            @subst_lt@dt:format pattern="dd-MMM-yyyy"@subst_gt@
	    	  @subst_lt@dt:parse pattern="yyyy-MM-dd"@subst_gt@
	    	<bean:write name="onerowofobservation" property="obsenddate_datetrunc"></bean:write>
	    	   @subst_lt@/dt:parse@subst_gt@
	  	   @subst_lt@/dt:format@subst_gt@
	  	   </logic:notEqual> <!-- not 10 years -->
	      </logic:notEqual> <!-- not 3 years -->  
	  </logic:notEqual> <!-- not 1 year -->
	  <logic:notEmpty name="onerowofobservation" property="dateaccuracy">
	  <logic:notEqual name="onerowofobservation" property="dateaccuracy" value="Exact">
	     &plusmn; <bean:write name="onerowofobservation" property="dateaccuracy" />	
          </logic:notEqual>
          </logic:notEmpty>	  
	 </span>
</logic:notEmpty>
<logic:empty name="onerowofobservation" property="obsenddate">
<logic:notEqual parameter="textoutput" value="true">&nbsp;</logic:notEqual>
</logic:empty>
</td>
</tr>
<bean:define id="hadData" value="true"></bean:define>
</logic:notEmpty>

<logic:notEmpty name="onerowofobservation" property="dateaccuracy">
<tr class="@nextcolorclass@">
<td class="observation_dateaccuracy datalabel" title="Estimated accuracy of the observation date.">Date Accuracy <a title="Click here for definition of this field." target="_blank" class="image" href="/dd/observation/dateaccuracy" onclick="popupDD('/dd/observation/dateaccuracy'); return false;"><span class="ddlink"><img src="@images_link@question.gif" alt="?" border="0"></span></a></td><td class="observation_dateaccuracy">
<logic:notEmpty name="onerowofobservation" property="dateaccuracy">
<span>
<bean:write name="onerowofobservation" property="dateaccuracy"></bean:write>
</span>
</logic:notEmpty>
<logic:empty name="onerowofobservation" property="dateaccuracy">
<logic:notEqual parameter="textoutput" value="true">&nbsp;</logic:notEqual>
</logic:empty>
</td>
</tr>
<bean:define id="hadData" value="true"></bean:define>
</logic:notEmpty>

	
	<%@ include file="autogen/plot_plotmethodlong_data.jsp" %>
	<%@ include file="autogen/observation_plotmethodlong_data.jsp" %>
	<%@ include file="includeviews/sub_haddata.jsp" %>
	<tr><th>Plot Quality Fields:</th><th>&nbsp;</th></tr>
	<bean:define id="hadData" value="false" />
	<%@ include file="autogen/plot_plotqualitylong_data.jsp" %>
	<%@ include file="autogen/observation_plotqualitylong_data.jsp" %>
	<%@ include file="includeviews/sub_haddata.jsp" %>
	<tr><th>Overall Plot Vegetation Fields:</th><th>&nbsp;</th></tr>
	<bean:define id="hadData" value="false" />
	<%@ include file="autogen/plot_plotoverallveglong_data.jsp" %>
	<%@ include file="autogen/observation_plotoverallveglong_data.jsp" %>
	<%@ include file="includeviews/sub_haddata.jsp" %>
	<tr><th>Misc Fields:</th><th>&nbsp;</th></tr>
	<bean:define id="hadData" value="false" />
	<%@ include file="autogen/plot_plotmisclong_data.jsp" %>
	<%@ include file="autogen/observation_plotmisclong_data.jsp" %>
	<%@ include file="includeviews/sub_haddata.jsp" %>
	<!-- end of plot/obs fields -->
	</table>
	</div>

</logic:notEmpty>