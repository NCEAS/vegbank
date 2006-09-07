
<table class="thinlines" cellpadding="2">
<tr><th colspan="19">Community Classification:</th></tr>

<tr class="interpCommLink"><td class="useraction" colspan="19">
    <a href='@web_context@InterpretCommObservation.do?obsId=<%= request.getParameter("observation_pk") %>'>Interpret this plot to a community concept</a>
</td></tr>

<logic:empty name="commclass-BEANLIST">
<tr><td>  No communties have been assigned to this plot.</td></tr>
</logic:empty>
<logic:notEmpty name="commclass-BEANLIST">

<tr>
<th>More</th>
<%@ include file="../autogen/commclass_summary_head.jsp" %>
<th>Community Assignments</th><th>Contributors</th>
</tr>
<logic:iterate id="onerowofcommclass" name="commclass-BEANLIST">
<tr class="@nextcolorclass@">
<td class="smallfield"><a href="@get_link@detail/commclass/<bean:write name='onerowofcommclass' property='commclass_id' />">
                            Details
                            </a></td>
<%@ include file="../autogen/commclass_summary_data.jsp" %>
<bean:define id="ccId" name="onerowofcommclass" property="commclass_id"/>
<TD valign="top">
<!--------------- CONCEPTS IN COMMINTERP ----------------------------------->
<vegbank:get id="comminterpretation" select="comminterpretation" beanName="map" 
where="where_commclass_pk" pager="false" perPage="-1" wparam="ccId" />
<table class="leftrightborders" cellpadding="2">
<logic:empty name="comminterpretation-BEANLIST">
<tr><td>No Community Interpretations.</td></tr>
</logic:empty>
<logic:notEmpty name="comminterpretation-BEANLIST">

<tr>
<%@ include file="../autogen/comminterpretation_summary_head.jsp" %>
</tr>
<logic:iterate id="onerowofcomminterpretation" name="comminterpretation-BEANLIST">
<tr class="@nextcolorclass@">


<%@ include file="../autogen/comminterpretation_summary_data.jsp" %></tr>
</logic:iterate>

</logic:notEmpty>
</table>
 <!--------------- end of CONCEPTS IN COMMINTERP ----------------------------------->
</TD>

<TD valign="top"><!-- CONTRIB -->

 


<vegbank:get id="classcontributor" select="classcontributor" 
  beanName="map" where="where_commclass_pk" wparam="ccId" pager="false" perPage="-1"/>
<table class="leftrightborders" cellpadding="2">
<logic:empty name="classcontributor-BEANLIST">
<tr><td>
No Class Contributors.</td></tr>
</logic:empty>
<logic:notEmpty name="classcontributor-BEANLIST">

<tr>
<!-- self explanatory this not needed: % @ include file="../autogen/classcontributor_summary_head.jsp" % -->
</tr>
<logic:iterate id="onerowofclasscontributor" name="classcontributor-BEANLIST">
<tr class="@nextcolorclass@">
<%@ include file="../autogen/classcontributor_summary_data.jsp" %>
</tr>
</logic:iterate>

</logic:notEmpty>
</table>

</TD>

</tr>
</logic:iterate>

</logic:notEmpty>
</table>
