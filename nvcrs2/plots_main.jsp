<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<tiles:insert page="/template/type_templates.jsp" flush="true">
  <tiles:put name="userbar" value="/user_bar.jsp" />
  <tiles:put name="menu" value="/common/menu.jsp" />
  <tiles:put name="content" value="/proposal_content.jsp" /> 
  <tiles:put name="maintop" value="/type.jsp" /> 
  <tiles:put name="mainmenu" value="/common/type_submenu.jsp" /> 
  <tiles:put name="mainbody" value="/plots.jsp" /> 
  <tiles:put name="footer" value="/common/footer.html" />
</tiles:insert>