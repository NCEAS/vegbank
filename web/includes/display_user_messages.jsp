   <!-- this file displays user messages -->
    <logic:equal parameter="justLoggedIn" value="true">
      <!-- user just logged in, let's let them know it worked and welcome them warmly to VegBank -->
      <% if ( strWebUserId != "-1" ) { %> <!-- confirmed that they ARE logged in -->
      <div class="message"> Welcome <bean:write name="usr_greetingBean" ignore="true" /> </div>
      <% } %>
    </logic:equal>
    <logic:equal parameter="justLoggedOff" value="true">
      <!-- user just logged off (out), let's let them know it worked  -->
      <% if ( strWebUserId == "-1" ) { %> <!-- confirmed that they ARE logged out -->
        <div class="message"> You are now logged out.  You can <a href="@general_link@login.jsp">Login again here</a>. </div>
      <% } %>
    </logic:equal>
    <logic:equal parameter="justRegistered" value="true">
      <!-- user just logged off (out), let's let them know it worked  -->
      <% if ( strWebUserId != "-1" ) { %> <!-- confirmed that they ARE logged in after registering -->
        <div class="message"> Thanks for signing up.  You are now logged in. </div>
      <% } %>
    </logic:equal>