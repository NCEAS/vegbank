
<!-- This is only a jsp snipit -->
<!-- include random image file only: eventually link + text as it will be a plot? -->
<%
  java.util.Random rand = new java.util.Random();      
  int r = rand.nextInt(4) + 1 ; 
        
  String thisString = "homeimg" + r + ".jpg" ;
%>
<img src="@image_server@<%= thisString %>" />

<!-- End logon form Include -->
