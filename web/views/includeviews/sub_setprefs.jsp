
 <h1>Set Your VegBank Preferences</h1>
  <p class="instructions">
    Use this page to specify your preferences about your experience on VegBank.  This page uses cookies
    to store the values you select.  This means the next time you visit VegBank on 
    <strong>this computer</strong>
    these values will still be used, unless you delete your cookies or switch browsers.  
    There is no need to push a button; as soon as you check
    or uncheck a box, VegBank will remember the answer.
  </p>
  
  <h4>Preferred Plant Interpretation and Name</h4>
  <p class="instructions">Many views in VegBank show you names of plants that occurred on a plot.
    The Author of the plot had a name to identify the plant, and this can always be shown.
    The Author's name will always include any irregular species notation, most commonly something like "Carex sp."
    Each plant is interpreted, and you can choose either the original interpretation or the current interpretation,
    which links the plant to a <strong>Plant Concept</strong>.  Each plant concept has many different names
    associated with it, and you can select one of these (Scientific, Common Name, Code, etc.)
    along with the Original or Current Interpretation. </p>
    
    <%@ include file="sub_taxonimportance_showallplantnames_menu.jsp" %>
    <br/>
    <strong>Note:</strong> This picklist is shown on many views.  Changing those
    picklists is the same as changing this one.  VegBank continues showing the plant you selected until you select another.</td>
    
    
    <h4>Optional Items to Show in Views</h4>
    <span class="instructions">Please check any items you wish to see on a particular view, 
       and uncheck those you do not wish to view.</span>
   <% String theCookieVal=null; String isChecked=null ; %>
   <!-- cookies controlled by db table -->
   <vegbank:get id="cookie" select="dba_cookie" beanName="map" pager="false" perPage="-1" 
     allowOrderBy="true" orderBy="orderby_cookie" />
   <logic:notEmpty name="cookie-BEANLIST">
     <% 
      // get the cookies, but only once 
      Cookie[] cookies = request.getCookies(); 
      %>
        <bean:define id="lastview" value=" there is no such !!" />
        <table class="noborder">
       <logic:iterate id="onerowofcookie" name="cookie-BEANLIST">
         
         <bean:define id="checkforcookie"><bean:write name="onerowofcookie" property="fullcookiename" /></bean:define>
         <bean:define id="defaultval"><bean:write name="onerowofcookie" property="defaultvalue" /></bean:define>
         <bean:define id="lastviewfresh"><bean:write  name="lastview" /></bean:define><!-- this ensures it's text -->
         <logic:notEqual name="onerowofcookie" property="viewname" value="<%= lastviewfresh %>">
           <!-- header for view: -->
           <tr><td colspan="9">
            <strong><br/>
             <bean:write name="onerowofcookie" property="viewnamelabel" />
             <logic:empty name="onerowofcookie" property="viewnamelabel">
               <bean:write name="onerowofcookie" property="viewname" />
             </logic:empty>  
            </strong>
           </td></tr>
           <logic:notEqual name="onerowofcookie" property="viewname" value="global">
              <tr><td colspan="9">
(<a target="_new" href="@views_link@<bean:write name='onerowofcookie' property='viewname' />.jsp?wparam=<bean:write name='onerowofcookie' property='examplepk' />">Example View</a> - opens in a new window) 
              </td></tr>
           </logic:notEqual>  
         </logic:notEqual>
           <tr> 
             <td>
           

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
            </td>
            <td>
              <bean:write name="onerowofcookie" property="cookienamelabel" />
              <logic:empty name="onerowofcookie" property="cookienamelabel">
                <bean:write name="onerowofcookie" property="cookiename" />
              </logic:empty> 
           </td>   
         </tr>    
         <!-- remember this for next time -->
         <bean:define id="lastview"><bean:write name="onerowofcookie" property="viewname" /></bean:define>
       </logic:iterate>
     </table>
     <div class="backToOpener">
       <input type="button" value="Update Last View and Return to it" onclick="resubmitOpeningForm()" />
       <p>Note: you can update any view to reflect that above preferences 
        by reloading the page with the "reload" or "refresh" button
        on your browser.</p>
     </div>
     
   </logic:notEmpty>
  
  
  
  <h2>Future Development</h2>
    <p>
    <strong>Save these changes</strong> permanently to your account, so that whenever and whereever you login to VegBank,
    they are the same.  If you do not save the changes, they will still be used on your computer until you
    change them.
    </p>
    <p>
  <input type="button" disabled="disabled" value="Save to Your Account" />
    </p>
 