<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<tiles:insert page="/template/template1.jsp" flush="true">
  <tiles:put name="header" value="/common/header.jsp" />
  <tiles:put name="menu" value="/common/menu.jsp" />
  <tiles:put name="content" value="/proposal_content.jsp" /> 
  <tiles:put name="main" value="/distribution.jsp" /> 
  <tiles:put name="footer" value="/common/footer.html" />
</tiles:insert>