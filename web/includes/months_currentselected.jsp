<% // this file provides 12 options for months and selects current one %>
  
  <bean:define id="currentDateMonth"><dt:format pattern="MMM"><dt:currentTime/></dt:format></bean:define>
  <bean:define id="currentDateMonthUpper"><%= currentDateMonth.toUpperCase() %></bean:define>
  <%
   String AllMonths = "JANFEBMARAPRMAYJUNJULAUGSEPOCTNOVDEC";
   String oneMonth = new String();
   for (int i=0; i<12 ; i++)
  {
   oneMonth = AllMonths.substring( ( i * 3 ), ( i * 3 ) + 3 );
  %>
  <bean:define id="beanMonth"><%= oneMonth %></bean:define>
  <bean:define id="thisMSelected" value="" />
  <logic:equal name="currentDateMonthUpper" value="<%= beanMonth %>">
    <bean:define id="thisMSelected" value='selected' />
  </logic:equal>
  <option value="<%= oneMonth %>"  <bean:write name="thisMSelected" /> ><%= oneMonth %></option>      
 
  <%
  }
  %>