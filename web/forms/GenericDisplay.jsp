<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<% String rootEntity = (String) request.getAttribute("rootEntity"); %>
<% String includeJSP = "../GenJSP/Display" + rootEntity + ".jsp"; %>

<html>
  <head>
    <title>View <%= rootEntity %> -- Details</title>
    <link rel="stylesheet" href="@stylesheet@" type="text/css"/>
  </head>

  <body>
    <!-- Header -->
    @vegbank_header_html_normal@ 
    
    <table>
    	<jsp:include page='<%= includeJSP  %>' flush="true"/>
    </table>
    <!-- Footer -->
    @vegbank_footer_html_tworow@    
  </body>
</html>
