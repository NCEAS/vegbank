@webpage_top_html@
<head>
<title>VegBank Tutorial</title>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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
<noframes>
Sorry, but your browser doesn't support frames.  <a href="<%= strHelpPg %>">Try this link</a> or press the back button on your browser.
</noframes>
</html>


