@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@




        
<!-- 
*  '$RCSfile: observation_summary.jsp,v $'
*   Copyright: 2000 Regents of the University of California and the
*              National Center for Ecological Analysis and Synthesis
*   Authors: @author@
*
*  '$Author: mlee $'
*  '$Date: 2005-03-15 06:57:08 $'
*  '$Revision: 1.25 $'
*
* This program is free software; you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation; either version 2 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
-->
  
   <%
                       //**************************************************************************************
                       //  Set up alternating row colors
                       //**************************************************************************************
                       String rowClass = "evenrow";
    %>


<title>Plot Observation Summary View</title>




  

@webpage_masthead_html@
 @possibly_center@  
  <h2>View Plot-Observations</h2>
  <vegbank:get id="plotobs" select="plotandobservation" whereNumeric="where_observation_pk" 
    whereNonNumeric="where_observation_ac" beanName="map" pager="true" xwhereEnable="true"/>

<vegbank:pager />

<logic:empty name="plotobs-BEANLIST">
                Sorry, no plot-observations are available.
          </logic:empty>
<logic:notEmpty name="plotobs-BEANLIST"><!-- set up table -->


<logic:iterate id="onerowofobservation" name="BEANLIST"><!-- iterate over all records in set : new table for each -->
<bean:define id="onerowofplot" name="onerowofobservation" />
<bean:define id="obsId" name="onerowofplot" property="observation_id"/>
<bean:define id="plot_pk" name="onerowofplot" property="plot_id"/>
<bean:define id="observation_pk" name="onerowofplot" property="observation_id"/>

<!-- start of plot & obs fields-->
<TABLE width="100%" border="0" cellpadding="2" cellspacing="2">
<tr><td colspan="2">See more info with the <a href='@get_link@comprehensive/observation/<bean:write name="onerowofobservation" property="observation_id" />'>comprehensive view.</a></td></tr>
<TR><TD width="55%" valign="top"><!-- plot level info -->


<table class="leftrightborders" cellpadding="1"><!--each field, only write when HAS contents-->
<tr><th class="major" colspan="2">Plot Level Data: <bean:write name="onerowofplot" property="authorplotcode"/></th></tr>

<tr><th>Plot ID Fields:</th><th>&nbsp;</th></tr>
<bean:define id="hadData" value="false" /> <!-- sets to false the variable that keeps track if we have written a field in this section -->

<%@ include file="autogen/plot_plotidshort_data.jsp" %>         
<%@ include file="autogen/observation_plotidshort_data.jsp" %>

<%@ include file="includeviews/sub_haddata.jsp" %>
<tr><th>Location Fields:</th><th>&nbsp;</th></tr>
<bean:define id="hadData" value="false" /> <!-- sets to false the variable that keeps track if we have written a field in this section -->

<%@ include file="autogen/plot_plotlocshort_data.jsp" %>
<%@ include file="autogen/observation_plotlocshort_data.jsp" %>
<%@ include file="includeviews/sub_place.jsp" %>

<%@ include file="includeviews/sub_haddata.jsp" %>
<tr><th>Layout Fields:</th><th>&nbsp;</th></tr>
<bean:define id="hadData" value="false" /> <!-- sets to false the variable that keeps track if we have written a field in this section -->


<%@ include file="autogen/plot_plotlayoutshort_data.jsp" %>
<%@ include file="autogen/observation_plotlayoutshort_data.jsp" %>

<%@ include file="includeviews/sub_haddata.jsp" %>
<tr><th>Environment Fields:</th><th>&nbsp;</th></tr>
<bean:define id="hadData" value="false" /> <!-- sets to false the variable that keeps track if we have written a field in this section -->

<%@ include file="autogen/plot_plotenvshort_data.jsp" %>
<%@ include file="autogen/observation_plotenvshort_data.jsp" %>


<%@ include file="includeviews/sub_haddata.jsp" %>
<tr><th>Methods Fields:</th><th>&nbsp;</th></tr>
<bean:define id="hadData" value="false" /> <!-- sets to false the variable that keeps track if we have written a field in this section -->

<%@ include file="autogen/plot_plotmethodshort_data.jsp" %>
<%@ include file="autogen/observation_plotmethodshort_data.jsp" %>


<%@ include file="includeviews/sub_haddata.jsp" %>
<tr><th>Plot quality Fields:</th><th>&nbsp;</th></tr>
<bean:define id="hadData" value="false" /> <!-- sets to false the variable that keeps track if we have written a field in this section -->

<%@ include file="autogen/plot_plotqualityshort_data.jsp" %>
<%@ include file="autogen/observation_plotqualityshort_data.jsp" %>

<%@ include file="includeviews/sub_haddata.jsp" %>
<tr><th>Overall Plot Vegetation Fields:</th><th>&nbsp;</th></tr>
<bean:define id="hadData" value="false" /> <!-- sets to false the variable that keeps track if we have written a field in this section -->

<%@ include file="autogen/plot_plotoverallvegshort_data.jsp" %>
<%@ include file="autogen/observation_plotoverallvegshort_data.jsp" %>

<%@ include file="includeviews/sub_haddata.jsp" %>

<tr><th>Misc Fields:</th><th>&nbsp;</th></tr>
<bean:define id="hadData" value="false" /> <!-- sets to false the variable that keeps track if we have written a field in this section -->
<%@ include file="autogen/plot_plotmiscshort_data.jsp" %>
<%@ include file="autogen/observation_plotmiscshort_data.jsp" %>

<%@ include file="includeviews/sub_haddata.jsp" %>

<!-- end of plot/obs fields -->




<tr><th>Community Classification:</th><th>&nbsp;</th></tr>

<!-- community info -->
<vegbank:get id="comminterpretation" select="comminterpretation_withobs" beanName="map" 
  where="where_observation_pk" wparam="obsId" perPage="-1" pager="false"/>
<logic:empty name="comminterpretation-BEANLIST">
<tr class='@nextcolorclass@'><td>  No Community Interpretations.</td></tr>
</logic:empty>
<logic:notEmpty name="comminterpretation-BEANLIST">
<tr><td colspan="2">
<table class="leftrightborders" cellpadding="2" width="100%"><!--each field, only write when field HAS contents-->

<tr>
<%@ include file="autogen/comminterpretation_summary2_head.jsp" %>
</tr>
<logic:iterate id="onerowofcomminterpretation" name="comminterpretation-BEANLIST"><!-- iterate over all records in set : new table for each -->
<logic:notEmpty name="onerowofcomminterpretation" property="commconcept_id">
<tr class='@nextcolorclass@'>
<%@ include file="autogen/comminterpretation_summary2_data.jsp" %>
</tr>
</logic:notEmpty>
</logic:iterate>
</table>

</td></tr>
</logic:notEmpty>



</table>


</TD><TD valign="top"><!-- plants in this plot -->

<%@ include file="includeviews/sub_taxonobservation.jsp" %>
</TD>
</TR>

<TR><TD colspan="2"><hr noshade="true"/><br/></TD></TR>
</TABLE>

</logic:iterate>

<vegbank:pager/>

</logic:notEmpty>

<br>
@webpage_footer_html@


