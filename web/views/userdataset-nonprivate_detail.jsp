@stdvegbankget_jspdeclarations@


<HEAD   >
@defaultHeadToken@
 
<TITLE>View Data in VegBank : Public Dataset(s)</TITLE>
<link rel="stylesheet" href="@stylesheet@" type="text/css">
<meta http-equiv="Content-Type" content="text/html; charset=">
</HEAD>
<body>@vegbank_header_html_normal@  <br><h2>View Public VegBank Datasets</h2>
<vegbank:get select="userdataset_nonprivate" beanName="map" where="where_usr_email" pager="true"/>

<%@ include file="includeviews/sub_userdataset_detail.jsp" %>

<br>
          @vegbank_footer_html_tworow@