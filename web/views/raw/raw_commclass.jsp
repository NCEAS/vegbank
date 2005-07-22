@stdvegbankget_jspdeclarations@
<% String rowClass = "evenrow" ; %>
<!-- MUST BE passed observation_pk -->

<!-- this is the raw one -->
<!--  request.getRequestURL() |  request.getQueryString()  -->
<vegbank:get id="commclass" select="commclass" beanName="map" 
    where="where_observation_pk" wparam="observation_pk" pager="false" />
<bean:parameter name="observation_pk" id="observation_pk" />
<%@ include file="../includeviews/sub_commclass_summary.jsp" %>
