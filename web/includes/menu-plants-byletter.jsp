  Browse for plants by letter: 
  
  <%
  String alph = new String();
  String lett = new String();
  alph = "abcdefghijklmnopqrstuvwxyz" ;
  for (int i=0; i<26 ; i++)
  {
  lett = alph.substring(i,i + 1);
  %>
  <!-- URL of plant link -->
  <a href="@get_link@summary/plantconcept/<%= lett %>&where=where_plantconcept_firstletter&perPage=25"><%= lett.toUpperCase() %></a> <% if (i<25) { %> | <% }  %>
        
 
  <%
  }
  %>