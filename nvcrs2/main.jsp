<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<tiles:insert page="/template/template1.jsp" flush="true">
  <tiles:put name="userbar" value="/user_bar.jsp" />
  <tiles:put name="menu" value="/common/menu.jsp" />
  <tiles:put name="content" value="/content.jsp" /> 
  <tiles:put name="main" value="/main_body.jsp" /> 
  <tiles:put name="footer" value="/common/footer.html" />
</tiles:insert>