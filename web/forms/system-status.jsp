



@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@


 
<TITLE>VegBank System Status</TITLE>

 




@webpage_masthead_html@
 


  <h1 >
    Welcome to VegBank's Status Report</h1>

	<p>Note you must be logged in as an administrator to see anything on this page.</p>
	<!-- Admin -->
<% 
	Boolean isAdmin = (Boolean)(request.getSession().getAttribute("isAdmin"));

	if (isAdmin != null) {
		if (isAdmin.booleanValue()) {
%>
    <hr />
    <p>Admin is logged in.</p>
      <h3>System Status</h3>
      <h4>Results of Deploy Props:</h4>
      <p><!-- have to do this weird path to get THIS machine's status (full http:// not ok) ... /vegbank is set as relative home in struts -->
      <bean:include id="stat1" href='<%= "http://" + request.getServerName() + "/status/deploy-props.txt"%>' />
		<bean:write name="stat1" filter="false"/>
	  </p>
	  <hr/>
	  <h4>Results of Comparing Ideal XML Model to actual model as implemented on above database:</h4>
       
    <p>About This Comparison:</p>
    <p> <bean:include id="stat3" href='<%= "http://" + request.getServerName() + "/status/verify.readme"%>' />
		<bean:write name="stat3" filter="false"/>
	  </p>
    <bean:include id="stat2" href='<%= "http://" + request.getServerName() + "/status/modeldiffs.txt"%>' />
	 <logic:notEmpty name="stat2">
		  <p class="error">There ARE differences!</p>
		  <pre><!-- preserve spacing -->
		  <bean:write name="stat2" filter="false"/>
		  </pre>
		</logic:notEmpty>
		<logic:empty name="stat2">
		  <p>GOOD: No differences!</p>
		</logic:empty>
	      
       <hr/>
	  <h3>Business Rules Adherence</h3>
	  <p>Please see <a href="@forms_link@businessrules.jsp">Business Rules Page</a>.</p>
<% 
		}
	} 
%>

<br/>


@webpage_footer_html@
