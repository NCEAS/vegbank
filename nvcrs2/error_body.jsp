<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ page import="org.vegbank.nvcrs.web.BeanManager"  %>
<%@ page import="java.util.*" %> 
<center>
<br>
<h3>Error</h3>
<%
	BeanManager beanManager=(BeanManager)pageContext.getAttribute("beanManager", PageContext.SESSION_SCOPE);
	java.util.ArrayList list = beanManager.getErrors();
	String strErr="";
	if(list!=null && !list.isEmpty())
	{
		int num=list.size();
		for(int i=0;i<num;i++)
		{
			strErr = (String)list.get(i);
			
%>    	
<%= strErr %>
<%
		}
	}
%>
<br>

</center>
