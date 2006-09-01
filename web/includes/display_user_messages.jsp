   <!-- this file displays user messages -->
   <bean:define id="user_message_html"> &nbsp;  <!-- this is here so that the bean:define doesn't cause error -->
    <logic:equal parameter="justLoggedIn" value="true">
      <!-- user just logged in, let's let them know it worked and welcome them warmly to VegBank -->
      <% if ( strWebUserId != "-1" ) { %> <!-- confirmed that they ARE logged in -->
       Welcome <bean:write name="usr_greetingBean" ignore="true" /> 
       <bean:define id="user_message_exists">true</bean:define>
      <% } %>
    </logic:equal>
    <logic:equal parameter="justLoggedOff" value="true">
      <!-- user just logged off (out), let's let them know it worked  -->
      <% if ( strWebUserId == "-1" ) { %> <!-- confirmed that they ARE logged out -->
         You are now logged out.  You can <a href="@general_link@login.jsp">Login again here</a>. 
         <bean:define id="user_message_exists">true</bean:define>
      <% } %>
    </logic:equal>
    <logic:equal parameter="justRegistered" value="true">
      <!-- user just logged off (out), let's let them know it worked  -->
      <% if ( strWebUserId != "-1" ) { %> <!-- confirmed that they ARE logged in after registering -->
         Thanks for signing up.  You are now logged in. 
         <bean:define id="user_message_exists">true</bean:define>
      <% } %>
    </logic:equal>
    <logic:equal parameter="justEmailedPassword" value="true">
      <!-- user just logged off (out), let's let them know it worked  -->
         A new temporary password has been sent to your e-mail address.  
         Once you receive the e-mail from @systemEmail@ (you may have to add that address to your contact list), 
         please <a href="@general_link@login.jsp">Login</a> and change your password.
         <bean:define id="user_message_exists">true</bean:define>
    </logic:equal>
   </bean:define> 
    <!-- only write the div + messages if there ARE messages -->
    <logic:equal name="user_message_exists" value="true">
      <div class="message"><bean:write name="user_message_html" filter="false" />  </div>
    </logic:equal>  