<bean:define id="thisviewid"><%= request.getRequestURI().substring(1+request.getRequestURI().lastIndexOf("/"),request.getRequestURI().indexOf(".")) %></bean:define>
<!-- view: ><bean:write name="thisviewid" />< -->
<!-- check for parameter which limits user defined data:, if so hide it, used in raw views where cookies are not accessible -->
<logic:present parameter="do_not_show_userdefined_data">
  <bean:define id="do_not_show_userdefined_data" value="true" />
</logic:present>
<!-- this file set the default styles to use for jsp, and edits them 
   according to cookie's contents -->

<!-- plant names are a simple case: -->
  <bean:define id="plantNamesToShowBean">taxonobservation_int_currplantscinamenoauth</bean:define > <!-- default -->
  <!-- looking for full -->
  <logic:present cookie="taxon_name_full">
    <!-- getting the cookie value which IS present: -->
    <bean:cookie id="plantNamesToShowCookie" name="taxon_name_full"  value="taxonobservation_int_currplantscinamenoauth" /> 
    <!-- if cookie was set, then set to define new bean -->
    <bean:define id="plantNamesToShowBean"><bean:write name="plantNamesToShowCookie" property="value" /></bean:define>
  </logic:present>
  <!-- DEBUG: getcookies got: plantNamesToShowBean: <bean:write name="plantNamesToShowBean" /> -->

  
 <bean:define id="filterplants" value="true" />
 <logic:equal name="thisviewid" value="taxonobservation_detail">
   <bean:define id="filterplants" value="false" />
 </logic:equal>
 
 <logic:equal name="filterplants" value="true">
   <<bean:write ignore='true' name='noinlinestyles' />style type="text/css">
     .<bean:write name="plantNamesToShowBean" /> {visibility: visible;}
     <logic:notEqual name="plantNamesToShowBean" value="taxonobservation_authorplantname" >.taxonobservation_authorplantname { display:none; } </logic:notEqual>
     <logic:notEqual name="plantNamesToShowBean" value="taxonobservation_int_origplantscifull" >.taxonobservation_int_origplantscifull { display:none; }</logic:notEqual>
     <logic:notEqual name="plantNamesToShowBean" value="taxonobservation_int_origplantscinamenoauth" >.taxonobservation_int_origplantscinamenoauth { display:none; }</logic:notEqual>
     <logic:notEqual name="plantNamesToShowBean" value="taxonobservation_int_origplantcode" >.taxonobservation_int_origplantcode { display:none; }</logic:notEqual>
     <logic:notEqual name="plantNamesToShowBean" value="taxonobservation_int_origplantcommon" >.taxonobservation_int_origplantcommon { display:none; }</logic:notEqual>
     <logic:notEqual name="plantNamesToShowBean" value="taxonobservation_int_currplantscifull" >.taxonobservation_int_currplantscifull { display:none; }</logic:notEqual>
     <logic:notEqual name="plantNamesToShowBean" value="taxonobservation_int_currplantscinamenoauth" >.taxonobservation_int_currplantscinamenoauth { display:none; } </logic:notEqual>
     <logic:notEqual name="plantNamesToShowBean" value="taxonobservation_int_currplantcode" >.taxonobservation_int_currplantcode { display:none; }</logic:notEqual>
     <logic:notEqual name="plantNamesToShowBean" value="taxonobservation_int_currplantcommon" >.taxonobservation_int_currplantcommon { display:none; }</logic:notEqual>
   </<bean:write ignore='true' name='noinlinestyles' />style> 
 </logic:equal>
 
 
 <!-- special dd cookie to show/hide label or database name -->
 <!-- default: -->
 <bean:define id="ddShowLabelsNotNameBean">show</bean:define>
 <logic:present cookie="globaldd_showlabels_notnames">
   <!-- show according to cookie -->
   <bean:cookie id="ddShowLabelsNotNameCookie" name="globaldd_showlabels_notnames" value="show" />
   <bean:define id="ddShowLabelsNotNameBean"><bean:write name="ddShowLabelsNotNameCookie" property="value" /></bean:define>
 </logic:present>

 <logic:equal name="ddShowLabelsNotNameBean" value="show">
    <!-- show labels not names -->
    <style type="text/css">
     
      .dba_fielddescription_fieldname {display:none; }
     
      .dba_tabledescription_tablename {display:none; }
    </style>
 </logic:equal>
 <logic:notEqual name="ddShowLabelsNotNameBean" value="show">
    <!-- show no labels, instead names -->
    <style type="text/css">
      .dba_fielddescription_fieldlabel {display:none; }
     
      .dba_tabledescription_tablelabel {display:none; }
     
    </style>   
 </logic:notEqual>
 
 
 <% String theCookieVal=null; %>
 <!-- cookies controlled by db table -->
 <vegbank:get id="cookie" select="dba_cookie" beanName="map" pager="false" perPage="-1"
   where="where_cookie_view" wparam="thisviewid" />
 <logic:notEmpty name="cookie-BEANLIST">
   <!-- comments need to stay up here, not in style part -->
    
   <% 
    // get the cookies, but only once 
    Cookie[] cookies = request.getCookies(); 
    %>
   
   <<bean:write ignore='true' name='noinlinestyles' />style type="text/css">
     <logic:iterate id="onerowofcookie" name="cookie-BEANLIST">
       <bean:define id="checkforcookie"><bean:write name="onerowofcookie" property="fullcookiename" /></bean:define>
       <bean:define id="defaultval"><bean:write name="onerowofcookie" property="defaultvalue" /></bean:define>
       <% 
       if (cookies!= null) {
         theCookieVal=null; 
         for(int i=0;i<cookies.length;i++){ 
            if(cookies[i].getName().equals(checkforcookie)){ 
             theCookieVal = cookies[i].getValue(); 
             
            } 
         }
         if (theCookieVal == null) {
           theCookieVal=defaultval ;
           
         } 
         if (theCookieVal.equals("hide")) {
        %>
         .<bean:write name="onerowofcookie" property="cookiename" /> { display:none; }
         <logic:equal name="onerowofcookie" property="fullcookiename" value="globaluser_defined_data">
           <% // define special bean that says NOT to show user defined data. Don't even look it up %>
           <bean:define id="do_not_show_userdefined_data" value="true" />
         </logic:equal>
         
        <%
        } //cookie is a hide cookie
        
       } //not null cookies.
       %>
       
       
       
     </logic:iterate>
   </<bean:write ignore='true' name='noinlinestyles' />style>
 </logic:notEmpty>
  
  
  </style><!-- this may be an extra style icon, but that doesn't cause errors. -->