@stdvegbankget_jspdeclarations@


<HEAD   >
<META http-equiv="Content-Type" content="text/html; charset=UTF-16">

@defaultHeadToken@
 
<TITLE>View Data in VegBank : coverMethod(s) : Detail</TITLE>
<link rel="stylesheet" href="@stylesheet@" type="text/css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</HEAD>
<body   >@vegbank_header_html_normal@  <br>
<h2>View VegBank Cover Methods</h2>
<% String rowClass = "evenrow"; %>

<vegbank:get id="covermethod" select="covermethod" beanName="map" where="where_covermethod_pk" pager="true"/>

<logic:empty name="covermethod-BEANLIST">
<p>  Sorry, no coverMethods found.</p>
</logic:empty>
<logic:notEmpty name="covermethod-BEANLIST">
<logic:iterate id="onerowofcovermethod" name="covermethod-BEANLIST"><!-- iterate over all records in set : new table for each -->
<table class="leftrightborders" cellpadding="2"><!--each field, only write when field HAS contents-->


<%@ include file="autogen/covermethod_detail_data.jsp" %>


  <TR><TD COLSPAN="2">
  <!-- remember coverMethod -->
  <bean:define id="cmId" name="onerowofcovermethod" property="covermethod_id"/>  
  <vegbank:get id="coverindex" select="coverindex" beanName="map" where="where_covermethod_pk" pager="false" wparam="cmId" perPage="-1"/>

<logic:empty name="coverindex-BEANLIST">
<p  class="@nextcolorclass@">  Sorry, no coverIndexes found.</p>
</logic:empty>

<logic:notEmpty name="coverindex-BEANLIST">
  <table class="leftrightborders" cellpadding="2">
    <%@ include file="autogen/coverindex_summary_head.jsp" %>
    <logic:iterate id="onerowofcoverindex" name="coverindex-BEANLIST">
     
     <%@ include file="autogen/coverindex_summary_data.jsp" %>
    </logic:iterate>
  </table>
</logic:notEmpty>

  </TD></TR>
</table>
<p>&nbsp;</p>
</logic:iterate>
</logic:notEmpty><br>

<br/>
<vegbank:pager/>
          @vegbank_footer_html_tworow@