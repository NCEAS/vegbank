<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<tiles:insert page="/template/template.jsp" flush="true">
 <tiles:put name="userbar" value="/user_bar.jsp" />
  <tiles:put name="menu" value="/common/menu.html" />
  <tiles:put name="main" value="/usrs.jsp" /> 
  <tiles:put name="footer" value="/common/footer.html" />
</tiles:insert>