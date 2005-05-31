@webpage_top_html@
<head>
@webpage_stylesheets_declaration_html@
<title>VegBank Tutorial</title>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>



 <%

   String strMainPg = request.getParameter("mainPage");
   strMainPg = strMainPg!=null?strMainPg:"@web_context@";
   
  %>
 
<body>
  <div style="text-align:left;padding:5px">
  <p>You've requested help from complicated search results.  This is a new window.  To see an example of the
  complicated search results here, click the blue link on the header of the help displayed in the form to the right
  &gt;&gt;&gt;&gt;&gt;&gt;&gt;&gt;&gt;<br/>
  If that doesn't work, try <a href="<%= strMainPg %>">this link</a>.  
  
  <br/><br/>
  <strong>Please note</strong> that the data shown after clicking one of the two links mentioned above
  <strong>will be different data</strong> from the data you had on your page.
  
  </p>
  <p><strong>To get back to your data,</strong> please move, minimize, or close this window.  Your data is in another 
  browser window.
  </p>
  
  <p>Why does this page appear here instead of my data?</p>
  <p>When you send VegBank a complicated search, it takes a bit of time to get the results.  Adding a new
  window like this one for help saves some time because VegBank doesn't repeat your complex search.
  Also, because of URL length limitations, it is difficult to reconstruct your search using the frames used
  by our help pages.  We apologize for the inconvenience. </p>
  </div>
  
</body>

</html>


