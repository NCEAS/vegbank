
@stdvegbankget_jspdeclarations@


<HEAD   >
<META http-equiv="Content-Type" content="text/html; charset=UTF-16">

@defaultHeadToken@
 
<TITLE>View Data in VegBank : commClass(s) : Summary</TITLE>
<link rel="stylesheet" href="@stylesheet@" type="text/css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</HEAD>
<body   >@vegbank_header_html_normal@  <h2>View VegBank Community Classifications</h2>
<% String rowClass = "evenrow"; %>
<vegbank:get id="commclass" select="commclass" beanName="map" where="where_observation_pk" pager="true"/>
<vegbank:pager />
<logic:empty name="commclass-BEANLIST">
<p>  Sorry, no communties have been assigned to this plot-observation.</p>
</logic:empty>
<logic:notEmpty name="commclass-BEANLIST">
<table class="thinlines" cellpadding="2">
<tr>
<th>More</th>
<%@ include file="autogen/commclass_summary_head.jsp" %>
<th>Community Assignment(s)</th><th>Contributors</th>
</tr>
<logic:iterate id="onerowofcommclass" name="commclass-BEANLIST">
<tr class="@nextcolorclass@">
<td class="sizetiny"><a href="@get_link@detail/commclass/<bean:write name='onerowofcommclass' property='commclass_id' />">
                            Details
                            </a></td>
<%@ include file="autogen/commclass_summary_data.jsp" %>
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
<%@ include file="autogen/comminterpretation_summary_head.jsp" %>
</tr>
<logic:iterate id="onerowofcomminterpretation" name="comminterpretation-BEANLIST">
<tr class="@nextcolorclass@">


<%@ include file="autogen/comminterpretation_summary_data.jsp" %></tr>
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
<%@ include file="autogen/classcontributor_summary_head.jsp" %>
</tr>
<logic:iterate id="onerowofclasscontributor" name="classcontributor-BEANLIST">
<tr class="@nextcolorclass@">
<%@ include file="autogen/classcontributor_summary_head.jsp" %>
</tr>
</logic:iterate>

</logic:notEmpty>
</table>

</TD>

</tr>
</logic:iterate>
</table>
</logic:notEmpty><br><vegbank:pager id="commclass"/>
          @vegbank_footer_html_tworow@
