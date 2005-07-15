@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@

<TITLE>View VegBank Data: Observations - Simple Summary</TITLE>
<%@ include file="includeviews/inlinestyles.jsp" %>
      @webpage_masthead_html@
      @possibly_center@
        <h2>View VegBank Observations</h2>
<!--Get standard declaration of rowClass as string: -->
        <% String rowClass = "evenrow"; %>
 <vegbank:get id="observation" select="plotandobservation" whereNumeric="where_observation_pk" 
     whereNonNumeric="where_observation_ac" beanName="map" pager="true" xwhereEnable="true"/>


<vegbank:pager /><logic:empty name="observation-BEANLIST">
<p>  Sorry, no Observations found.</p>
</logic:empty>
<logic:notEmpty name="observation-BEANLIST">

<logic:iterate id="onerowofobservation" name="observation-BEANLIST">
<TABLE class="leftrightborders" cellpadding="2" ><!-- table for obs -->

<!-- iterate over all records in set : new table for each -->

<bean:define id="onerowofplot" name="onerowofobservation" />
<!-- get both PKs -->
        <bean:define id="observation_pk" name="onerowofobservation" property="observation_id" />
        <bean:define id="plot_pk" name="onerowofobservation" property="plot_id" />


<% rowClass = "evenrow"; %> <!-- reset colors -->

<TR>
<TH class="major" colspan="4">
<bean:write name="onerowofobservation" property="authorobscode" />
  -<a href='@get_link@comprehensive/observation/<bean:write name="onerowofobservation" property="observation_id" />'>more details</a>
</TH></TR>
  <TR><TD colspan="2" valign="top">
  <table class="leftrightborders"><!-- table for plot level data -->
  <%@ include file="autogen/observation_plotshowmany_data.jsp" %>
        <%@ include file="autogen/plot_plotshowmany_data.jsp" %>
        
   <!-- community info -->
   <vegbank:get id="comminterpretation" select="comminterpretation_withobs" beanName="map" 
     where="where_observation_pk" wparam="observation_pk" perPage="-1" pager="false"/>
   <logic:notEmpty name="comminterpretation-BEANLIST">
   <!-- <tr><th>Community Classification:</th><th>&nbsp;</th></tr> -->
   <tr class='@nextcolorclass@'><td class="datalabel">Community Type:</td><td>
   <table class="leftrightborders" cellpadding="2" width="100%"><!-- table for types -->
     <!--each field, only write when field HAS contents-->
   
   
   <logic:iterate id="onerowofcomminterpretation" name="comminterpretation-BEANLIST">
     <!-- iterate over all records in set  -->
   <logic:notEmpty name="onerowofcomminterpretation" property="commconcept_id">
   <tr>
   <%@ include file="autogen/comminterpretation_summary2_data.jsp" %><td class="largefield">
   <a href="@get_link@summary/commclass/<bean:write name='onerowofcomminterpretation' property='commclass_id' />">details</a></td>
   </tr>
   </logic:notEmpty>
   </logic:iterate>
   </table> <!-- table for types -->
   
   </td></tr>
</logic:notEmpty>
   
   
   <%@ include file="includeviews/sub_place.jsp" %>
      
      </table><!-- table for plot level data -->
      
<!--Insert a nested get statement here:
-->



</TD><TD COLSPAN="2" valign="top">
<table class="thinlines"><!-- table for plants -->
<bean:define id="smallheader" value="yes" />
<bean:define id="limitPlantRecs2Show" value="yes" />
<bean:define id="showStrataDefn" value="no" />
<%@ include file="includeviews/sub_taxonobservation.jsp" %>


</table><!-- table for plants -->
</TD>
</TR>
</TABLE><!-- table for obs -->
<br/>
</logic:iterate>


</logic:notEmpty>
<br />
<vegbank:pager />

          @webpage_footer_html@

