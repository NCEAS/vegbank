  @stdvegbankget_jspdeclarations@
<bean:define id="lastResultsURL" value="<%=Utility.getLastSearchURL(request.getSession())%>" />
<logic:notEmpty name="lastResultsURL">
 <div style="float: right; padding-right: 40px;">
    &raquo; <a href='<%=lastResultsURL%>&lr=true'>last search results</a>
 </div>
</logic:notEmpty>

