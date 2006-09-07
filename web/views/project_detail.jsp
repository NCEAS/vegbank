
@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@
<TITLE>View VegBank Data: projects - Detail</TITLE>
 <%@ include file="includeviews/inlinestyles.jsp" %> 
 @webpage_masthead_html@ 
  @possibly_center@  
<h2>View VegBank Projects</h2>

        <vegbank:get id="project" select="project" beanName="map" pager="true" xwhereEnable="true" 
        orderBy="xorderby_projectname" />
<!--Where statement removed from preceding: -->
<vegbank:pager />
<logic:empty name="project-BEANLIST">
<p>  Sorry, no projects found.</p>
</logic:empty>
<logic:notEmpty name="project-BEANLIST">
<logic:iterate id="onerowofproject" name="project-BEANLIST">
<!-- iterate over all records in set : new table for each -->
<table class="leftrightborders" cellpadding="2">
<tr><th class="major_smaller" colspan="9">Project: <bean:write name="onerowofproject" property="projectname" /></th></tr>
        <%@ include file="autogen/project_detail_data.jsp" %>
        <bean:define id="project_pk" name="onerowofproject" property="project_id" />
<!-- custom bits:
-->



<tr  class='@nextcolorclass@'><td class="datalabel">Plots in this Project</td>
 <!-- plot count -->
		  <td>
		    
		   <logic:empty name="onerowofproject" property="d_obscount">0</logic:empty>
		   <logic:notEmpty name="onerowofproject" property="d_obscount">
		   
		   <logic:notEqual name="onerowofproject" property="d_obscount" value="0">
                    <bean:define id="critAsTxt">
                    In Project: <bean:write name="onerowofproject" property="projectname"/>
                    </bean:define>
                    <%  
                        /* create a map of parameters to pass to the new link: */
                        java.util.HashMap params = new java.util.HashMap();
                        params.put("wparam", project_pk);
                        params.put("where", "where_project_pk");
                        params.put("criteriaAsText", critAsTxt);
                        pageContext.setAttribute("paramsName", params);
                    %>
                    
                    <html:link page="/views/observation_summary.jsp" name="paramsName" scope="page" >
                        <bean:write name="onerowofproject" property="d_obscount" />
                    </html:link>

		   </logic:notEqual>
		   <logic:equal name="onerowofproject" property="d_obscount" value="0">0</logic:equal>
		   </logic:notEmpty>
          </td></tr>



           <!-- classified plot count -->
           <tr  class='@nextcolorclass@'><td class="datalabel">Classified Plots</td>
           <td>
             <logic:empty name="onerowofproject" property="countclassplots">0</logic:empty>
                         <logic:notEmpty name="onerowofproject" property="countclassplots">
                           <logic:notEqual name="onerowofproject" property="countclassplots" value="0">
                              <bean:define id="critAsTxt">
                              In Project: <bean:write name="onerowofproject" property="projectname"/> AND are classified to a community.
                              </bean:define>
                              <%  
                                  /* create a map of parameters to pass to the new link: */
                                  java.util.HashMap params = new java.util.HashMap();
                                  params.put("wparam", project_pk);
                                  params.put("where", "where_project_pk_and_observationclassified");
                                  params.put("criteriaAsText", critAsTxt);
                                  pageContext.setAttribute("paramsName", params);
                              %>
                              
                              <html:link page="/views/observation_summary.jsp" name="paramsName" scope="page" >
                                 <bean:write name="onerowofproject" property="countclassplots" />
                              </html:link>
                           </logic:notEqual>
                           <logic:equal name="onerowofproject" property="countclassplots" value="0">0</logic:equal>
             </logic:notEmpty>
          </td>
          </tr>
          <!-- state/province list -->
          <tr  class='@nextcolorclass@'><td class="datalabel">Locations of Plots</td>
          <td class="smallfield"> 
          <vegbank:get id="places" select="place_summ" beanName="map" perPage="-1" pager="false" 
           where="where_group_place_summ_project" wparam="project_pk"/>
		   <logic:notEmpty name="places-BEANLIST">
		 
		        
			   	<logic:iterate id="oneplace" name="places-BEANLIST" >
			   	  <!-- loop over list of states -->
                    <bean:define id="critAsTxt">
                    In Project: <bean:write name="onerowofproject" property="projectname"/> AND In <bean:write name="oneplace" property="region_name"/>.
                    </bean:define>
                    <bean:define id="complexwparam"><bean:write name="project_pk" /><%= Utility.PARAM_DELIM %><bean:write name="oneplace" property="namedplace_id" /></bean:define>
                    <%  
                        /* create a map of parameters to pass to the new link: */
                        java.util.HashMap params = new java.util.HashMap();
                        params.put("wparam", complexwparam);
                        params.put("where", "where_project_place");
                        params.put("criteriaAsText", critAsTxt);
                        pageContext.setAttribute("paramsName", params);
                    %>
                    
                    <html:link page="/views/observation_summary.jsp" name="paramsName" scope="page" >
                       <bean:write name="oneplace" property="region_name"/>
                       (<bean:write name="oneplace" property="count_obs"/>)
                    </html:link>

		       </logic:iterate>
		    
			 </logic:notEmpty>
          
          </td>
          </tr>
<!--Insert a nested get statement here:
   example:   

<NOT vegbankget id="related_table" select="related_table" beanName="map" pager="false" perPage="-1" where="where_project_pk" wparam="project_pk" />-->

<TR><TD COLSPAN="2">
<vegbank:get id="projectcontributor" select="projectcontributor" beanName="map" pager="false" where="where_project_pk" wparam="project_pk" perPage="-1" />
<table class="leftrightborders" cellpadding="2" >
<tr><th colspan="2">Project Contributors:</th></tr>
<logic:empty name="projectcontributor-BEANLIST">
<tr><td class="@nextcolorclass@"> [None]</td></tr>
</logic:empty>
<logic:notEmpty name="projectcontributor-BEANLIST">

<tr>
<%@ include file="autogen/projectcontributor_summary_head.jsp" %>
</tr>
<logic:iterate id="onerowofprojectcontributor" name="projectcontributor-BEANLIST">
<tr class="@nextcolorclass@">
<%@ include file="autogen/projectcontributor_summary_data.jsp" %>
</tr>
</logic:iterate>

</logic:notEmpty>
</table>
</TD></TR>
</table>
<p>&nbsp;</p>
</logic:iterate>
</logic:notEmpty>
<br />
<vegbank:pager />

          @webpage_footer_html@

