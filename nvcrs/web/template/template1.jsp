<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<META HTTP-EQUIV="Pragma" CONTENT="no-cache"> 
<META HTTP-EQUIV="expires" CONTENT="0"> 
<html:html>
 <head>
  <title>NVCRS</title>
  <html:base/>
 </head>
 <body topmargin="0" leftmargin="0" bgcolor="#FFFFFF">
 <center>
 <Table width="75%">
 	<!-- banner -->
 	<tr><td bgcolor=#CCFFCC><tiles:insert attribute="header"/></td></tr>
 	<!-- Menu bar -->
	<tr><td bgcolor=#FFCCFF><tiles:insert attribute="menu"/></td></tr>
 </table>
 <Table border=0 cellpadding=0 cellspacing=3 width="75%" rules="cols" border="1"  >
	<tr>
		<!-- contents -->
		<td width="1%" background="image/bgmain.bmp"></td>
		<td width="18%" valign="top"><tiles:insert attribute="content"/></td>
		<td width="1%" background="image/bgmain.bmp"></td>
		<!-- main -->
		<td width="79%"><tiles:insert attribute="main"/></td>
		<td width="1%" background="image/bgmain.bmp"></td>
	</tr>
 	<!-- footer -->
 	<tr><td></td></tr>
 	<tr><td></td></tr>
 </table>			
 <Table width="75%">
 	<tr><td bgcolor=#FFCCFF><tiles:insert attribute="footer"/></td></tr>
 </table>

 </center>
</body>
</html:html>