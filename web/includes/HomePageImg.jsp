<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*" %>

<!-- This is only a jsp snipit -->
<!-- include random image file only: eventually link + text as it will be a plot? -->
<%
  Random rand = new Random();      
  int r = rand.nextInt(4) + 1 ; 
        
  String thisString = "homeimg" + r + ".jpg" ;
%>
<img src="@image_server@<%= thisString %>" />

<!-- End logon form Include -->
