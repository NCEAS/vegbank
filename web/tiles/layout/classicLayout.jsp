<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<%-- Layout Tiles 
  This layout render a header, left menu, body and footer.
  @param title String use in page title
  @param header Header tile (jsp url or definition name)
  @param menu Menu 
  @param body Body
  @param footer Footer
--%>

<html:html>
  <HEAD>
    <html:base/>
    <link rel="STYLESHEET" href="@stylesheet@" type="text/css" /> 
    <title><tiles:getAsString name="title"/></title>
  </HEAD>

  <html:body>

<table border="0" width="100%" cellspacing="5">
<tr>
  <td colspan="2"><tiles:insert attribute="header" /></td>
</tr>
<tr>
  <td valign="top"  align="left">
    <tiles:insert attribute="body" />
  </td>
</tr>
<tr>
  <td colspan="2">
    <tiles:insert attribute="footer" />
  </td>
</tr>
</table>
</html:body>
</html:/html>

