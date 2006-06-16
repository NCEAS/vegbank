@webpage_top_html@
  @stdvegbankget_jspdeclarations@
<jsp:useBean id="bean" class="org.vegbank.ui.struts.UserProfileForm"/>
  @webpage_head_html@
<!-- 
  * '$RCSfile: delete_plot_confirm.jsp,v $'
  * Purpose: 
  * Copyright: 2000 Regents of the University of California and the
  *               National Center for Ecological Analysis and Synthesis
  * Authors: @author@
  *
  * '$Author: berkley $'
  * '$Date: 2006-06-16 19:37:08 $'
  * '$Revision: 1.1 $'
  *
  *
  -->
  


<TITLE>Edit User Permissions</TITLE>

<script type="text/javascript">
function getHelpPageId() {
  return "update-user-permission";
}

</script>





@webpage_masthead_html@

<br />
<br />
<h2 align="center" class="vegbank">Edit User Permissions</h2>

<html:errors/>

<html:form method="get" action="/DropPlot.do">
  <input type="hidden" name="plotIdList" value="<bean:write name="plotIdList"/>"/>
  <p>Are you sure you want to delete the plot <bean:write name="plotIdList"/>?</p>
    
  <html:submit value="Yes"/>
	</html:form>

@webpage_footer_html@

