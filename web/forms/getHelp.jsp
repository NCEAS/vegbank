@webpage_top_html@
<head>
<title>VegBank Tutorial</title>

<script type="text/javascript">

/* dont want to allow nested frames with tutorial */
function breakoutofframes()
{
/* alert("js executing"); */
if (window.top!=window.self) 
  
 {
 window.top.location=window.location ;
 }
}

</script>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>



 <%

   String strMainPg = request.getParameter("mainPage");
   strMainPg = strMainPg!=null?strMainPg:"@web_context@";
   
  %>
 <%

   String strHelpPg = request.getParameter("helpPage");
   strHelpPg = strHelpPg!=null?strHelpPg:"@manual_link@quick-intro.html";
   
  %>


<frameset  cols="*,275" frameborder="yes" border="2" framespacing="0"> 
 <frame onLoad="breakoutofframes()" name="upperframe" scrolling="yes" src="<%= strMainPg %>" >

 <frame  name="lowerframe" scrolling="yes" src="<%= strHelpPg %>" >


</frameset>
<noframes>
Sorry, but your browser doesn't support frames.  

<p>
 Your browser should be able to handle frames to use the tutorial.
 Some links may require that your browser support JavaScript, too.
</p>
<p>If your browser cannot handle frames or JavaScript, 
<a href="@manual_link@manual-index.html">try this version</a>, which will use two different
windows instead of frames.  Highlighting will not function, however.</p>
</noframes>
</html>


