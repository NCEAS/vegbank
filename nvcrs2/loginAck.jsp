<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<center>

<jsp:useBean id="beanManager" class="org.vegbank.sausage.web.BeanManager" scope="session"/>
<c:set var="registered" value="${beanManager.registered}" /> 
<c:set var="username" value="${beanManager.name}" /> 
<c:url var="url_login" value="/login" />
<c:url var="url_register" value="/register" />
<c:if test ="${registered==0}">
	<table border=0 cellpadding=10 cellspacing=20 width=600 summary="layout">
	  <tr>      
	     	<td bgcolor="#889A00">
	     		<p>The system can not find your infomation.</p>
			<p>If you have registered, please check the user name and password and try to <a href="${url_login}">login</a> again</p>
			<p>If you have not registered, please <a href="${url_register}">register</a></p>
		</td>  
	  </tr>
	</table>
</c:if>
<c:if test ="${registered==0}">
<p>Welcome back! ${username} </p>
</c:if>
</center>
