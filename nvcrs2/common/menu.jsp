<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ page import="org.vegbank.nvcrs.web.BeanManager" %>

<%
BeanManager manager=(BeanManager)pageContext.getAttribute("beanManager", PageContext.SESSION_SCOPE);
String permission=null;
String menu="Home";
if(manager!=null)
{
	permission=manager.getUserPermission();
	menu=manager.getCurMenu();
	if(permission==null) permission="0";
	permission=permission.trim();
}
else
{
	String menu1=(String)pageContext.getAttribute("menu", PageContext.SESSION_SCOPE);
	if(menu1!=null) menu=menu1;
}
%>

<table border=0 cellpadding=0 cellspacing=0 width="%80" >
<tr>
<%	
	if(menu.equals("Home"))
	{
%>
	<td valign="top"><img border="0" src="image/homehighlight.bmp"></td> 
<%
	}
	else
	{
%>
	<td valign="top"><a href="menu.go?role=Home"><img border="0" src="image/menu_home.bmp" onmouseout="src='image/menu_home.bmp';"
		 onmouseover="src='image/homehighlight.bmp';"></a></td> 
<%
	}
%>
	<%
		
		if(permission!=null)
		{
			if(permission.equals("0"))
			{
			%>
				<%	
				if(menu.equals("Author"))
				{
				%>
					<td valign="top"><img border="0" src="image/author_highlight.bmp"></td>
				<%
				}
				else
				{
				%>
					<td valign="top"><a href="menu.go?role=Author"><img border="0" src="image/menu_author.bmp" onmouseout="src='image/menu_author.bmp';"
					 onmouseover="src='image/author_highlight.bmp';"></a></td> 
				<%
				}
				%>
			<%
			}
			else if(permission.equals("1"))
			{
			%>
				<%	
				if(menu.equals("Peer-viewer"))
				{
				%>
					<td valign="top"><img border="0" src="image/peerviewer_highlight.bmp"></td>
				<%
				}
				else
				{
				%>
					<td valign="top"><a href="menu.go?role=Peer-viewer"><img border="0" src="image/menu_peerviewer.bmp" 
						onmouseout="src='image/menu_peerviewer.bmp';"
					 	onmouseover="src='image/peerviewer_highlight.bmp';"></a></td>
				<%
				}
				%>
				
			<%
			}
			else if(permission.equals("2"))
			{
			%>
			
				<%	
				if(menu.equals("Manager"))
				{
				%>
					<td valign="top"><img border="0" src="image/manager_highlight.bmp"></td>
				<%
				}
				else
				{
				%>
					<td valign="top"><a href="menu.go?role=Manager"><img border="0" src="image/menu_manager.bmp" onmouseout="src='image/menu_manager.bmp';"
					 onmouseover="src='image/manager_highlight.bmp';"></a></td>
				<%
				}
				%>
			<%
			}
			else if(permission.equals("3"))
			{
			%>
				<%	
				if(menu.equals("Author"))
				{
				%>
					<td valign="top"><img border="0" src="image/author_highlight.bmp"></td>
				<%
				}
				else
				{
				%>
					<td valign="top"><a href="menu.go?role=Author"><img border="0" src="image/menu_author.bmp" onmouseout="src='image/menu_author.bmp';"
					 onmouseover="src='image/author_highlight.bmp';"></a></td> 
				<%
				}
				%>
				<%	
				if(menu.equals("Peer-viewer"))
				{
				%>
					<td valign="top"><img border="0" src="image/peerviewer_highlight.bmp"></td>
				<%
				}
				else
				{
				%>
					<td valign="top"><a href="menu.go?role=Peer-viewer"><img border="0" src="image/menu_peerviewer.bmp" 
					 onmouseout="src='image/menu_peerviewer.bmp';"
					 onmouseover="src='image/peerviewer_highlight.bmp';"></a></td>
				<%
				}
				%>
			<%
			}
			else if(permission.equals("4"))
			{
			%>
				<%	
				if(menu.equals("Author"))
				{
				%>
					<td valign="top"><img border="0" src="image/author_highlight.bmp"></td>
				<%
				}
				else
				{
				%>
					<td valign="top"><a href="menu.go?role=Author"><img border="0" src="image/menu_author.bmp" onmouseout="src='image/menu_author.bmp';"
					 onmouseover="src='image/author_highlight.bmp';"></a></td> 
				<%
				}
				%>
				<%	
				if(menu.equals("Manager"))
				{
				%>
					<td valign="top"><img border="0" src="image/manager_highlight.bmp"></td>
				<%
				}
				else
				{
				%>
					<td valign="top"><a href="menu.go?role=Manager"><img border="0" src="image/menu_manager.bmp" onmouseout="src='image/menu_manager.bmp';"
					 onmouseover="src='image/manager_highlight.bmp';"></a></td>
				<%
				}
				%>
			<%
			}
			else if(permission.equals("5"))
			{
			%>
				<%	
				if(menu.equals("Peer-viewer"))
				{
				%>
					<td valign="top"><img border="0" src="image/peerviewer_highlight.bmp"></td>
				<%
				}
				else
				{
				%>
					<td valign="top"><a href="menu.go?role=Peer-viewer"><img border="0" src="image/menu_peerviewer.bmp" onmouseout="src='image/menu_peerviewer.bmp';"
					 onmouseover="src='image/peerviewer_highlight.bmp';"></a></td>
				<%
				}
				%>
				<%	
				if(menu.equals("Manager"))
				{
				%>
					<td valign="top"><img border="0" src="image/manager_highlight.bmp"></td>
				<%
				}
				else
				{
				%>
					<td valign="top"><a href="menu.go?role=Manager"><img border="0" src="image/menu_manager.bmp" onmouseout="src='image/menu_manager.bmp';"
					 onmouseover="src='image/manager_highlight.bmp';"></a></td>
				<%
				}
				%>
			<%
			}
			else if(permission.equals("6"))
			{
			%>
				<%	
				if(menu.equals("Author"))
				{
				%>
					<td valign="top"><img border="0" src="image/author_highlight.bmp"></td>
				<%
				}
				else
				{
				%>
					<td valign="top"><a href="menu.go?role=Author"><img border="0" src="image/menu_author.bmp" onmouseout="src='image/menu_author.bmp';"
					 onmouseover="src='image/author_highlight.bmp';"></a></td> 
				<%
				}
				%>
				<%	
				if(menu.equals("Peer-viewer"))
				{
				%>
					<td valign="top"><img border="0" src="image/peerviewer_highlight.bmp"></td>
				<%
				}
				else
				{
				%>
					<td valign="top"><a href="menu.go?role=Peer-viewer"><img border="0" src="image/menu_peerviewer.bmp" onmouseout="src='image/menu_peerviewer.bmp';"
					 onmouseover="src='image/peerviewer_highlight.bmp';"></a></td>
				<%
				}
				%>
				<%	
				if(menu.equals("Manager"))
				{
				%>
					<td valign="top"><img border="0" src="image/manager_highlight.bmp"></td>
				<%
				}
				else
				{
				%>
					<td valign="top"><a href="menu.go?role=Manager"><img border="0" src="image/menu_manager.bmp" onmouseout="src='image/menu_manager.bmp';"
					 onmouseover="src='image/manager_highlight.bmp';"></a></td>
				<%
				}
				%>
			<%
			}
			else
			{
			}
		}
		%>
	<%
	if(menu.equals("About"))
	{
	%>
	<td valign="top"><img border="0" src="image/about_highlight.bmp"></td> 
	<%
	}
	else
	{
	%>
		<td valign="top"> <a href="menu.go?role=About"><img border="0" src="image/menu_about.bmp" onmouseout="src='image/menu_about.bmp';"
		 onmouseover="src='image/about_highlight.bmp';"></a></td> 
	<%
	}
	%>
</tr>
</table>
