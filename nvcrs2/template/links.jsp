
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<jsp:useBean id="beanManager" class="org.vegbank.sausage.web.BeanManager" scope="session"/>
<center> 
<c:set var="registered" value="${beanManager.registered}" /> 
<c:if test ="${registered==0}">
	<table border=0 cellpadding=10 cellspacing=2 width="%80" summary="layout">
	  <tr>      
	  <c:url var="url" value="/home" />
	    <td bgcolor="#FFFFCC"><a href="${url}">Home</a></td>
	  <c:url var="url" value="/login" />
	    <td bgcolor="#FFFFCC"><a href="${url}">Login</a></td>  
	  <c:url var="url" value="/register" />
	    <td bgcolor="#FFFFCC"><a href="${url}">Register</a></td>  
	  </tr>
	</table>
</c:if>
<c:if test ="${registered!=0}">
<table border=0 cellpadding=10 cellspacing=0 width="80%" summary="layout">
  <tr>      
  <c:url var="url" value="/main" />
    <td bgcolor="#FFFFCC"><a href="${url}">Home</a></td>
  <c:url var="url" value="/userinfo" />
    <td bgcolor="#FFFFCC"><a href="${url}">User</a></td>
  <c:url var="url" value="/addproposals" />
    <td bgcolor="#FFFFCC"><a href="${url}">New proposal</a></td>
  <c:url var="url" value="/proposals" />
    <td bgcolor="#FFFFCC"><a href="${url}">View proposals</a></td>
  <c:url var="url" value="/Review" />
    <td bgcolor="#FFFFCC"><a href="${url}">Review proposals</a></td>
  <c:url var="url" value="/logoff" />
    <td bgcolor="#FFFFCC"><a href="${url}">Logoff</a></td>
  </tr>
</table>
</c:if>
</center>
