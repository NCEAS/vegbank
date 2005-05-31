@stdvegbankget_jspdeclarations@
<% String rowClass = "evenrow" ; %>
<!-- MUST BE passed observation_pk -->


<!-- this is the raw taxonobs -->
<!--  request.getRequestURL() |  request.getQueryString()  -->

<logic:notPresent parameter="observation_pk">
Sorry, badly formatted request to get taxa.  Needs observation ID.
</logic:notPresent>

<logic:present parameter="observation_pk">
  <bean:parameter id="observation_pk" name="observation_pk" />
<%@ include file="../includeviews/sub_taxonobservation.jsp" %>
</logic:present>