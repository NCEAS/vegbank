<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:useBean id="beanManager" class="org.vegbank.sausage.web.BeanManager" scope="session" />
<c:set var="serr" value="${beanManager.strErr}" />
<center>
<h3><p>Error message</h3>
<font color=#FF6600>${serr}</font>
</center>
