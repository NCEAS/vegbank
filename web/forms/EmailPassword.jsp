


@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@

	
<!-- 
  *   '$RCSfile: EmailPassword.jsp,v $'
  *     Purpose: Jsp form to allow users to retrive lost passwords from the
  *              system over email. 
  *   Copyright: 2000 Regents of the University of California and the
  *               National Center for Ecological Analysis and Synthesis
  *     Authors: @author@
  *
  *    '$Author: mlee $'
  *      '$Date: 2005-05-31 18:39:18 $'
  *  '$Revision: 1.9 $'
  *
  *
  -->
  



		<title>Retrieve a Lost VegBank Password</title>

<script type="text/javascript">
function getHelpPageId() {
  return "forgot-my-password";
}

</script> 
 

	



@webpage_masthead_html@


<html:errors/>

<!-- SECOND TABLE -->
<h2>Retrieve a Lost VegBank Password</h2>
	
			<html:form action="/EmailPassword" focus="email">

			
 <p class="instructions">Use this form to request your VegBank Password. 
 Please enter your email address and your password will be emailed to you immediately.</p>
			   
	
	
		
<p>
			Email address:
		  <html:text property="email" size="25" maxlength="70" />
</p>		





			<html:submit value="continue"/>
			&nbsp;&nbsp;
			<html:reset value="reset"/>

</html:form>

	

@webpage_footer_html@
