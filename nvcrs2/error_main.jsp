<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ page import="org.vegbank.nvcrs.web.BeanManager"  %>
<%@ page import="java.util.*" %> 
<center>
<br>
<h3>Error</h3>
<%
	String strError=(String)pageContext.getAttribute("nvcrs_error", PageContext.SESSION_SCOPE);
%>
<%= strError %>
<br>
</center>
