@webpage_top_html@
   @stdvegbankget_jspdeclarations@
   @webpage_head_html@
   <title>VegBank Listserves</title>
  @webpage_masthead_html@ 
 


<!-- get user ID to var: strWebUserId-->
   	   <%@ include file="../views/includeviews/sub_getwebuserid.jsp" %>
   

 <%
   String strList = request.getParameter("list");
   strList = strList!=null?strList:"users";
   
   String strToGet = request.getParameter("toget");
   
   strToGet = strToGet!=null?strToGet:"info";
   
   String strPg = "not overwritten yet" ;
   %>
 
  <bean:define id="beanToGet" value="<%= strToGet %>" />
  <!--logic:equal name="beanToGet" value="info"-->
    <bean:define id="beanPage">@listservinfo_link@<%= strList %></bean:define> <!-- default -->
  <!--/logic:equal-->
  <logic:equal name="beanToGet" value="archive">
    <bean:define id="beanPage">@listservarchive_link@<%= strList %></bean:define>
  </logic:equal>
  <logic:equal name="beanToGet" value="edit">
    <bean:define id="beanPage">@listservuser_link@<%= strList %>/<bean:write name="beanWebUserEmail" /></bean:define>
  </logic:equal>
   
   
   
   <!-- %
   if ( strToGet == "info" ) {     strPg = "@listservinfo_link@" + strList ;  }
   
   else if ( strToGet == "edit" )   {     strPg = "@listservuser_link@" + strList  ;   }
     
   
     
  % -->



<p class="psmall">Below are links to the VegBank listservs.  Anyone may read the list archives.
  The lists are now managed on Google Groups. 
   See <a href="@general_link@contact.html">the contact page</a> for more information about lists.
   
   </p>
   	
   <p><strong>Menu of lists:</strong>  <br/>
      --<strong><a href="https://groups.google.com/a/vegbank.org/d/forum/users">VegBank Users</a></strong> 
   <br/>
                     --<strong><a href="https://groups.google.com/a/vegbank.org/d/forum/vegbranch">VegBranch</a></strong> 
 
                     </p>
   <p></p>
      
   
   
   
  
@webpage_footer_html@
