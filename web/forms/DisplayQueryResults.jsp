@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@
 
<tiles:insert  page="/tiles/layout/classicLayout.jsp" flush="true">
	<tiles:put name="title"  value="Query Results"/>
	<tiles:put name="header" value="/tiles/common/header.jsp"/>
	<tiles:put name="footer" value="/tiles/common/footer.jsp"/>
	<tiles:put name="body"   value="/tiles/QueryList.jsp"/>
	<tiles:put name="message" value="tiles/common/message.jsp"/>
</tiles:insert>

