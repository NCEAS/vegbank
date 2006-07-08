
@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@
 
<TITLE>View VegBank Data: projects - Summary</TITLE>
  
 <%@ include file="includeviews/inlinestyles.jsp" %> 
 @webpage_masthead_html@ 
  @possibly_center@  
<h2>View VegBank Projects</h2>



<!-- ############################################### -->
<!--            start of search box                  --> 
<!-- ############################################### -->
<!-- % String searchString = request.getParameter("xwhereParams"); % -->
<!-- collect previous info on project: -->
<bean:parameter id="beansearchString" value="" name="xwhereParams" />
<bean:parameter id="beanxwhereMatchAny" value="" name="xwhereMatchAny" />
<bean:parameter id="beanxwhereMatchWholeWords" value="" name="xwhereMatchWholeWords"/>

<form action="@views_link@project_summary.jsp" method="get">
    <table cellpadding="0" cellspacing="0" border="0" bgcolor="#DDDDDD">
    <tr>
        <td><img src="@image_server@uplt3.gif"/></td>
        <td></td>
        <td><img src="@image_server@uprt3.gif"/></td>
    </tr>

    <tr>
        <td></td>
        <td><span id="searchProjectShown">
            <!-- link to hide this: -->
            
            <input type="hidden" name="clearSearch" value="" />
            <input type="hidden" name="xwhereKey" value="xwhere_kw_match" />
            <input type="hidden" name="where" value="where_keywords_pk_in" />
            <input type="hidden" name="wparam" value="project__project" />
            <input type="hidden" name="xwhereSearch" value="true" />
            <span class="greytext">
            &nbsp; Search for a project (enter a name, organization, etc.):
            &nbsp;&nbsp;
              <a href="#" onclick="showorhidediv('searchProjectShown');showorhidediv('searchProjectHidden');return false;">
                &lt;&lt;Hide search
              </a>
            </span>
            <br />
             <input type="text" name="xwhereParams" size="30" value="<bean:write name='beansearchString' />"/>
             <html:submit value="search" />
       
       <!--</td>
        </tr>
        <tr><td></td><td align="right"> -->
        
        <div align="right">
             <input type="checkbox" name="xwhereMatchAny" <logic:equal value='on' name='beanxwhereMatchAny' >checked="checked" </logic:equal> />
                Match any word
        </div>
       
        <!--
        </td>
        <td></td>
    </tr>
        <tr><td></td><td align="right">
        -->
        <div align="right">
        <!-- check this box if this search is a wparam search and it was checked before, or if it isn't a search: -->
              <input type="checkbox" 
                     name="xwhereMatchWholeWords" 
                     <logic:empty  name='beansearchString' > checked="checked" </logic:empty> 
                     <logic:equal value='on' name='beanxwhereMatchWholeWords' >checked="checked" </logic:equal>  
                />
                Match whole words only
          </div>
       <!-- end of shown search span --></span>
       
       <span id="searchProjectHidden" style="display:none" class="greytext">
           <a href="#" onclick="showorhidediv('searchProjectShown');showorhidediv('searchProjectHidden');return false;">
             Search for Project &gt;&gt;
           </a>
        </span></td>
            <td></td> 
            
    </tr>
    <tr>
        <td><img src="@image_server@lwlt3.gif"/></td>
        <td></td>
        <td><img src="@image_server@lwrt3.gif"/></td>
    </tr>
    
    </table>
</form>
<br />
<!-- ############################################### -->
<!--            end of search box                    --> 
<!-- ############################################### -->


     <logic:notPresent parameter="orderBy">
          <!-- set default sorting by project name? -->
          <bean:define id="orderBy" value="xorderby_projectname" />
     </logic:notPresent>
     <vegbank:get id="project" select="project" beanName="map" pager="true"
     xwhereEnable="true" allowOrderBy="true"/>
 
<!--Where statement removed from preceding: -->
<logic:empty name="project-BEANLIST">
<p>  Sorry, no projects found.</p>
</logic:empty>
<logic:notEmpty name="project-BEANLIST">
<table class="leftrightborders" cellpadding="2" id="projectsummarytable">
<tr>
<th>More</th>
                  <%@ include file="autogen/project_summary_head.jsp" %>
				    <!-- a few extra columns: -->
				  		<bean:define id="thisfield" value="d_obscount" />
				  		<bean:define id="fieldlabel">Plots</bean:define>
		            <%@ include file="../includes/orderbythisfield.jsp" %>
                        <bean:define id="thisfield" value="countclassplots" />
                        <bean:define id="fieldlabel">Classified Plots</bean:define>
                    <%@ include file="../includes/orderbythisfield.jsp" %>

				           <th>States / Provinces</th>

                
                  
                  </tr>
<logic:iterate id="onerowofproject" name="project-BEANLIST">
<bean:define id="project_pk" name="onerowofproject" property="project_id" />
<tr class="@nextcolorclass@">
<td class="largefield">
<a href="@get_link@detail/project/@subst_lt@bean:write name='onerowofproject' property='project_id' /@subst_gt@">
                            Details
                            </a>
</td>
                       <%@ include file="autogen/project_summary_data.jsp" %>
           
         
         <!-- extra data -->
         <!-- plot count -->
		  <td class="numeric">
		    
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
          </td> 
		   
		
          <!-- classified plot count -->
           <td class="numeric">
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
          <!-- state/province list -->
          
          <td class="largefield"> 
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
   
<bean:define id="project_pk" name="onerowofproject" property="project_id" />
<!--Insert a nested get statement here:
   example:   

<NOT vegbank@_colon_@get id="related_table" select="related_table" beanName="map" pager="false" perPage="-1" where="where_project_pk" wparam="project_pk" />-->
</tr>
</logic:iterate>

</table>
</logic:notEmpty>
<br />
<vegbank:pager />

          @webpage_footer_html@
