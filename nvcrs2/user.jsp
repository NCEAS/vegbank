
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:useBean id="beanManager" class="org.vegbank.sausage.web.BeanManager" scope="session" />
<center> 
<c:set var="registered" value="${beanManager.registered}" /> 
<c:if test ="${registered!=0}">
	<table border=0 cellpadding=0 cellspacing=1 width="%80" summary="layout">
	  <tr>      
	     <td >Welcome back, ${beanManager.name}.</td>
	     <td >(email: ${beanManager.email})</td>  
	  </tr>
	</table>
</c:if>
<c:if test ="${registered==0}">
	anonymous visitor
</c:if>

</center>
