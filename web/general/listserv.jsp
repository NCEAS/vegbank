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


<!-- <p>Now I'd show: <%= strPg %> </p> -->
<p class="psmall">The following iframe shows the VegBank listserv information.  Anyone may read the list archives.
   Remember that passwords and emails for the listservs are maintained separately from the normal VegBank login email and password. 
   See <a href="@general_link@contact.html">the contact page</a> for more information about lists.
   If your browser doesn't support iframes, <a href='<bean:write name="beanPage" />'>you may view the page here</a>.
   </p>
   	
   <p><strong>Menu of lists:</strong> (Tip: Log in to VegBank before clicking the edit links below.) <br/>
      --<strong>VegBank Users:</strong> <a href="@general_link@listserv.jsp?list=users&toget=info">info</a>, 
   <a href="@general_link@listserv.jsp?list=users&toget=archive">read</a>,  
   <a href="@general_link@listserv.jsp?list=users&toget=edit">edit options</a> (login to VegBank required)
   <br/>
                     --<strong>VegBranch:</strong> 
  <a href="@general_link@listserv.jsp?list=vegbranch&toget=info">info</a>, 
  <a href="@general_link@listserv.jsp?list=vegbranch&toget=archive">read</a>, 
  <a href="@general_link@listserv.jsp?list=vegbranch&toget=edit">edit options</a> (login to VegBank required)
                     </p>
   <p></p>
     
     
                  
   <iframe width="695" height="540" src='<bean:write name="beanPage" />' >
   </iframe><!-- closing element is needed as separate from start for iframe -->
   
   
   
  
@webpage_footer_html@
