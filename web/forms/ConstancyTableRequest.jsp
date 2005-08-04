@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@
 <%@ include file="/views/includeviews/inlinestyles.jsp" %> 
  <title>
   Request A Constancy Table
  </title>


<!-- VegBank Header -->
  @webpage_masthead_html@
  
 <h1>Request A Constancy Table</h1> 

<p class="instructions">Use this page to request a constancy table for one or more datasets.
A constancy table shows you the taxa on each set of plots, showing how many
plots have each species for each dataset, and also the average cover percent (if available in the database).
</p>

<p class="instructions">You must be a registered user who is logged in to use this feature.</p>

 <p class="instruction">Please check the row next to the datasets you would like to include
  in the constancy table.</p>
  <form name="dataform" action="@views_link@userdataset_constancyanalysis.jsp">

  <%@ include file="/views/includeviews/sub_userdataset_checkboxes.jsp" %>
  <logic:present name="successfulget">
    <!-- only put this button if there were user datasets retrieved -->
    <input type="submit" value="Get Constancy Table" />
  </logic:present>  
 </form>
</logic:notEmpty>

 
@webpage_footer_html@


