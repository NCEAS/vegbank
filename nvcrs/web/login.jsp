<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<tiles:insert page="/template/template.jsp" flush="true">
  <tiles:put name="header" value="/common/header.jsp" />
  <tiles:put name="menu" value="/common/menuInit.jsp" />
  <tiles:put name="main" value="/login_body.jsp" /> 
  <tiles:put name="footer" value="/common/footer.html" />
</tiles:insert>
