



@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@


<script language="JavaScript" type="text/JavaScript">
  <!--
  function doSubmit(formObj) {
    
    
    var action = formObj.action.value;
    
    if (!action || action=="" ) {
      document.countform.submit();
      return true;
    }

    if ( action == "generateForm" ) {
    	document.countform.submit();
    }
    if ( action == "upload" ) {
    	document.fileform.submit();
    }
    return true;
  }
//-->
</script>


<title>Upload Files to VegBank.org</title>



@webpage_masthead_html@
 


<%

	System.out.println("\n######## debugging from multiple_file_upload.jsp ########\n");
  String postURL = "multiple_file_upload.jsp";
  int MAX_COUNT = 22;

  HttpSession sess = request.getSession(false);
  String sessId = sess.getId();
  session.setAttribute("sessionId",sessId);
  
  String action = request.getParameter("action");
  action = (action!=null)? action.trim() : "";
  System.out.println("action: " + action);

  String _count = request.getParameter("count");
  _count = (_count!=null)? _count.trim() : "1";

  int count = Integer.parseInt(_count);
  if ( count > MAX_COUNT ) count = MAX_COUNT;

  System.out.println("count: " + count);
  System.out.println("session id: " + sessId);

%>
<form action="@forms_link@multiple_file_upload.jsp"
	name="countform" method="post" onchange="return doSubmit(this)">
<input type="hidden" name="action" value="generateForm"/>
<table>
<tbody>
<tr>
	<td>1. Select the number of files you want to upload:</td>
	<td>
	<select name="count" onchange="return doSubmit(countform)">

  <%
    for (int i = 1; i <= MAX_COUNT; i++) {
      if ( i == count ) {
        out.println("<option  selected=\"selected\" value=\""+i+"\">"+i+"</option>");
      } else {
        out.println("<option  value=\""+i+"\">"+i+"</option>");
      }
    }
  %>
</select>
  </td>
</tr>
<tr>
</table>
</form>


<p>2. Choose the files you want to upload: </p>
  
<form action="@web_context@servlet/upload" enctype="multipart/form-data" name="fileform" method="post"  onsubmit="return doSubmit(this)">
 <input type="hidden" name="action" value="upload"/>
  <table border="0">
  <%
	  for (int i = 0; i < (int)count; i++) {
      String fName = "f_"+i;
		  out.println("<tr><td> :: file "+(i+1)+ " :: <td> <td> <input name=\""+fName+"\" size=\"40\" type=\"file\"> </td></tr>");
    }
  %>
</table>
<p>3. Submit the files to the server:</p>
<input type="submit" value="Submit"/>
<input type="reset" value="Reset"/>
</form>






@webpage_footer_html@
