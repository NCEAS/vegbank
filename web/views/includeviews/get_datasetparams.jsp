  <bean:define id="wparambad" value="false" />
  <% /////////////////////////////////////////////////// %>
  <% // NO HTML COMMENTS IN THIS FILE!!! because some files comments this whole thing out, so that just jsp stuff happens %>
  <% /////////////////////////////////////////////////// %>
  <% // default define that wparam is not bad %>
  <logic:present parameter="wparam">
    <bean:parameter name="wparam" id="wparam" value="-1" />
  </logic:present>
  <logic:notPresent parameter="wparam">
      <logic:present parameter="includeds">
        <%
        // define variables
        String wparamsarray = "-1";
        String[] arr_includeds;
        
        // assign values to variables
        arr_includeds = request.getParameterValues("includeds");
        for(int counter = 0; counter < arr_includeds.length; counter++)
        {
         wparamsarray = wparamsarray + "," + arr_includeds[counter];
        } 
         
        %>
        <bean:define id="wparam"><%= wparamsarray %></bean:define>
       
       </logic:present>
       <logic:notPresent parameter="includeds">
         <bean:define id="wparambad" value="true" />
         No datasets were selected.
         Please choose one or more datasets.  Press <a href="javascript:history.back()">Here to go back</a>.
       </logic:notPresent>       
  </logic:notPresent>