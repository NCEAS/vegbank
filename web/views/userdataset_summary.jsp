@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@
    @ajax_js_include@
    @datacart_js_include@

<!-- $Id: userdataset_summary.jsp,v 1.5 2005-08-11 20:20:42 mlee Exp $ -->
<!-- purpose : show user's datasets, either all of them (mode=all in URL) or only certain ones:

  mode=ac is to show accessioncodes
  mode=id is to show by primary key of dataset(s) 
 BOTH of the above are comma separated, and currently accessionCodes need to be surrounded by single quotes -->


 
<TITLE>View Your VegBank Datasets - Summary</TITLE>

  
 <%@ include file="includeviews/inlinestyles.jsp" %> 
 @webpage_masthead_html@ 
  @possibly_center@  
<br/>  
<h1>View Your VegBank Datasets - Summary</h1>
<br/>
<!-- offer search box -->


<form action="@views_link@userdataset_summary.jsp" method="get">
    <table cellpadding="0" cellspacing="0" border="0" bgcolor="#DDDDDD">
    <tr>
        <td><img src="@image_server@uplt3.gif"/></td>
        <td></td>
        <td><img src="@image_server@uprt3.gif"/></td>
    </tr>
    <tr>
        <td></td>
        <td>    <input type="hidden" name="where" value="where_userdataset_search" />
            <span class="greytext">
            &nbsp; Search your datasets (name or description):
            </span>
            <br />
              <logic:present parameter="wparam">    
                <bean:parameter id="beanwparam" name="wparam" value="" />
                <input type="text" name="wparam" value="<bean:write name='beanwparam' />" />
              </logic:present>
              <logic:notPresent parameter="wparam">
                <input type="text" name="wparam" />
              </logic:notPresent>
             
             <html:submit value="search" />
             
             <!-- link to help about searching -->
             <a href="@help-for-searching-href@"><img height="14" width="14" border="0" src="@image_server@question.gif" /></a>
       
        </td>
        </tr>
        
    <tr>
        <td><img src="@image_server@lwlt3.gif"/></td>
        <td></td>
        <td><img src="@image_server@lwrt3.gif"/></td>
    </tr>
    
    </table>
</form>


  <!-- do the right get -->
 <%@ include file="includeviews/sub_getuserdatasets.jsp" %>

 <!-- display the right stuff -->
<%@ include file="includeviews/sub_userdataset_summary.jsp" %>

<br />



          @webpage_footer_html@
