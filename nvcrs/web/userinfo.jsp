<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<tiles:insert page="/template/template.jsp" flush="true">
  <tiles:put name="header" value="/common/header.jsp" />
  <tiles:put name="menu" value="/common/menu.html" />
  <tiles:put name="main" value="/usr.jsp" /> 
  <tiles:put name="footer" value="/common/footer.html" />
</tiles:insert>