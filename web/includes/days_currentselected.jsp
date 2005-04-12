<% // this file provides 31 options for days of month and select current one %>
  
  <bean:define id="currentDateDay"><dt:format pattern="d"><dt:currentTime/></dt:format></bean:define>
  
  <%
  
   for (int i=1; i<32 ; i++)
  {
   
  %>
  <bean:define id="beanI"><%= i %></bean:define>
  <bean:define id="thisSelected" value="" />
  <logic:equal name="currentDateDay" value="<%= beanI %>">
    <bean:define id="thisSelected" value='selected' />
  </logic:equal>
  <option value="<%= i %>"  <bean:write name="thisSelected" /> ><%= i %></option>      
 
  <%
  }
  %>