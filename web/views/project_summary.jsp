
@stdvegbankget_jspdeclarations@

<html>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8" />
@defaultHeadToken@
 
<TITLE>View VegBank Data: projects - Summary</TITLE>
<link rel="stylesheet" href="@stylesheet@" type="text/css" />
</HEAD>
<body>  
 @vegbank_header_html_normal@ 
  @possibly_center@  
<h2>View VegBank Projects</h2>
<!--Get standard declaration of rowClass as string: -->
        <% String rowClass = "evenrow"; %>
        <vegbank:get id="project" select="project" beanName="map" pager="true"  xwhereEnable="true"/>
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
          <th>Plots</th>
          <th>Classified Plots</th>
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
		    
		   <logic:empty name="onerowofproject" property="countobs">0</logic:empty>
		   <logic:notEmpty name="onerowofproject" property="countobs">
		   
		   <logic:notEqual name="onerowofproject" property="countobs" value="0">
		   <a href="@get_link@simple/observation/<bean:write name='project_pk' />?where=where_project_pk"><bean:write name="onerowofproject" property="countobs" /></a>
		   </logic:notEqual>
		   <logic:equal name="onerowofproject" property="countobs" value="0">0</logic:equal>
		   </logic:notEmpty>
          </td>
          <!-- classified plot count -->
           <td class="numeric">
		  		    <vegbank:get id="classobservation" select="observation_count" beanName="map" pager="false" perPage="-1" 
		  		     where="where_project_pk_and_observationclassified" wparam="project_pk" />
		  		   <logic:empty name="classobservation-BEAN">0</logic:empty>
		  		   <logic:notEmpty name="classobservation-BEAN">
		  		   <logic:notEqual name="classobservation-BEAN" property="count_observations" value="0">
		  		   <a href="@get_link@simple/observation/<bean:write name='project_pk' />?where=where_project_pk_and_observationclassified"><bean:write name="classobservation-BEAN" property="count_observations" /></a>
		  		   </logic:notEqual>
                   <!-- write 0 with no link -->
                   <logic:equal name="classobservation-BEAN" property="count_observations" value="0">0</logic:equal>
		  		   </logic:notEmpty>
          </td>
          <!-- state/province list -->
          
          <td class="largefield"> 
          <vegbank:get id="places" select="place_summ" beanName="map" perPage="-1" pager="false" 
           where="where_group_place_summ_project" wparam="project_pk"/>
		   <logic:notEmpty name="places-BEANLIST">
		       <bean:define id="firstplace" value="true" />
		       <logic:iterate id="oneplace" name="places-BEANLIST" >
		         <!-- loop over list of states -->
		        <logic:notEqual name="firstplace" value="true">; 
		        <!-- add semicolon before states that aren't the first one -->
		        </logic:notEqual>
		        <bean:define id="firstplace" value="false" /> 
		        <a href='@get_link@simple/observation/<bean:write name="project_pk" />;<bean:write name="oneplace" property="namedplace_id" />?where=where_project_place'><bean:write name="oneplace" property="region_name"/>
		         (<bean:write name="oneplace" property="count_obs"/>)</a>
		       </logic:iterate>
		    
			 </logic:notEmpty>
          
          </td>
   
<bean:define id="project_pk" name="onerowofproject" property="project_id" />
<!--Insert a nested get statement here:
   example:   

<NOT vegbank:get id="related_table" select="related_table" beanName="map" pager="false" perPage="-1" where="where_project_pk" wparam="project_pk" />-->
</logic:iterate>
</table>
</logic:notEmpty>
<br />
<vegbank:pager />
</body></html>
          @vegbank_footer_html_tworow@
