<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@ page language="java" %>
<html>
<head>
<title>VegBank Tutorial</title>
<meta http-equiv="Content-Type" content="text/html; charset=">

</head>
 <%

   String strMainPg = request.getParameter("mainPage");
   strMainPg = strMainPg!=null?strMainPg:"@web_context@";
   
  %>
 <%

   String strHelpPg = request.getParameter("helpPage");
   strHelpPg = strHelpPg!=null?strHelpPg:"@manual_link@manual-index.html";
   
  %>


<frameset  cols="*,275" frameborder="yes" border="2" framespacing="0"> 
  <frame name="upperframe" scrolling="yes" src="<%= strMainPg %>" >

 <frame  name="lowerframe" scrolling="yes" src="<%= strHelpPg %>" >


</frameset>
<noframes><body>
Sorry, but your browser doesn't support frames.  <a href="<%= strHelpPg %>">Try this link</a> 





<I></I>
	

