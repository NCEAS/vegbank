<!--
  *   '$RCSfile: multiple_file_upload.jsp,v $'
  *   '$Author: anderson $'
  *   '$Date: 2004-11-01 23:51:19 $'
  *   '$Revision: 1.1 $'
  *
  *
  * This program is free software; you can redistribute it and/or modify
  * it under the terms of the GNU General Public License as published by
  * the Free Software Foundation; either version 2 of the License, or
  * (at your option) any later version.
  *
  * This program is distributed in the hope that it will be useful,
  * but WITHOUT ANY WARRANTY; without even the implied warranty of
  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  * GNU General Public License for more details.
  *
  * You should have received a copy of the GNU General Public License
  * along with this program; if not, write to the Free Software
  * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
-->
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
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>

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


<title>:: Upload Files ::</title>
<link rel="stylesheet" type="text/css" href="default.css">
</head>
<body>


<form action="../nsupload/multiple_file_upload.jsp"
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


<br>2. Choose the files you want to upload:
  
<form action="/nsupload/servlet/upload" enctype="multipart/form-data" name="fileform" method="post"  onsubmit="return doSubmit(this)">
 <input type="hidden" name="action" value="upload"/>
  <table border="0">
  <%
	  for (int i = 0; i < (int)count; i++) {
      String fName = "f_"+i;
		  out.println("<tr><td> :: file "+(i+1)+ " :: <td> <td> <input name=\""+fName+"\" size=\"40\" type=\"file\"> </td></tr>");
    }
  %>
</table>
<br>3. Submit the files to the server:<br>
<input type="submit" value="Submit"/>
<input type="reset" value="Reset"/>
</form>





</body>
</html>
