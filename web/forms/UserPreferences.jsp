@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@

 
<TITLE>Set Your VegBank Preferences</TITLE>

  @webpage_masthead_html@
 
 <h2>Set Your VegBank Preferences</h2>
  <p class="instructions">
    Use this page to specify your preferences about your experience on VegBank.
  </p>
   <% String theCookieVal=null; String isChecked=null ; %>
   <!-- cookies controlled by db table -->
   <vegbank:get id="cookie" select="dba_cookie" beanName="map" pager="false" perPage="-1" />
    <!-- allowOrderBy="true" orderBy="x_orderby_order_asc" /-->
   <logic:notEmpty name="cookie-BEANLIST">
     <% 
      // get the cookies, but only once 
      Cookie[] cookies = request.getCookies(); 
      %>
       <logic:iterate id="onerowofcookie" name="cookie-BEANLIST">
         <bean:define id="checkforcookie"><bean:write name="onerowofcookie" property="fullcookiename" /></bean:define>
         <bean:define id="defaultval"><bean:write name="onerowofcookie" property="defaultvalue" /></bean:define>
         <bean:write name="onerowofcookie" property="cookiename" /> : 
         <% 
         theCookieVal=null; 
         for(int i=0;i<cookies.length;i++){ 
              if(cookies[i].getName().equals(checkforcookie)){ 
               theCookieVal = cookies[i].getValue(); 
               %>
               <!-- got the cookie value! <%= theCookieVal %> -->
               <%
              } 
         }
          if (theCookieVal == null) {
             theCookieVal=defaultval ;
             %>
             <!-- got the default value ...  <%= theCookieVal %> -->
             <%
         } 
          isChecked = "" ;
          if (theCookieVal.equals("show")) {
          
          isChecked = " checked='checked'" ;
          
          }
          
         %>
<input type="checkbox" onchange="setCookieForField('<bean:write name='onerowofcookie' property='fullcookiename' />',this.checked)"  <%= isChecked %> /> 
    <br/>
       </logic:iterate>
    
   </logic:notEmpty>
  
  
  
 
  @webpage_footer_html@