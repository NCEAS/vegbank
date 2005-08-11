@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@
<title>Oops! VegBank Error</title>
  @webpage_masthead_html@


<blockquote>
<h3 class="error">Oops! You found a VegBank bug.</h3>
<p class="sizelarge">Sorry, but the last thing you tried to do did not work.</p>
<p class="sizesmall">
Please try again right now by reloading/refreshing this page or going back.<br/>
If it is still broken, please try again later.<br/>
This problem has been reported to the developers.</p>

	<p class="sizenormal">
		Thank you for your patience.<br/>
		<a href="mailto:help@vegbank.org">help@vegbank.org</a>
	</p>	

	<p>
	<span class="error">ERROR MESSAGES:</span>

<logic:messagesPresent message="false">
    <ul>
    <html:messages id="error" message="false" property="vberror">
      <logic:notEmpty name="error">
        <li><bean:write name="error"/></li>
      </logic:notEmpty>
    </html:messages>
    </ul>
</logic:messagesPresent>
</blockquote>
                                                                                                                                                                     
<%
/*
   // this is helpful for debugging Struts lame message stuff
  //Object o = request.getAttribute(Globals.ERROR_KEY);
  Object o = request.getAttribute(Globals.MESSAGE_KEY);
  if (o != null) {
    ActionMessages ae = (ActionMessages)o;
                                                                                                                                                                     
    // Get the locale and message resources bundle
    Locale locale =
      (Locale)session.getAttribute(Globals.LOCALE_KEY);
    MessageResources messages =
      (MessageResources)request.getAttribute
      (Globals.MESSAGES_KEY);
                                                                                                                                                                     
    // Loop thru all the labels in the ActionMessage's
    for (Iterator i = ae.properties(); i.hasNext();) {
      String property = (String)i.next();
      out.println("<br>property " + property + ": ");
                                                                                                                                                                     
      // Get all messages for this label
      for (Iterator it = ae.get(property); it.hasNext();) {
        ActionMessage a = (ActionMessage)it.next();
        String key = a.getKey();
        Object[] values = a.getValues();
        out.println(" [key=" + key +
            ", message=" + messages.getMessage(locale,key,values) + "]");
      }
    }
}
*/
%>


<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>
<br/>


<!-- VEGBANK FOOTER -->

@webpage_footer_html@ 



