<!--
*  '$Id: admin_menu.jsp,v 1.8 2006-06-09 17:43:52 berkley Exp $'
*   Purpose: Home for admins
*   Copyright: 2000 Regents of the University of California and the
*              National Center for Ecological Analysis and Synthesis
*   Authors: @author@
*
*  '$Author: berkley $'
*  '$Date: 2006-06-09 17:43:52 $'
*  '$Revision: 1.8 $'
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

@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@


 
<title>VegBank Admin Menu</title>

 


@webpage_masthead_html@
 
<blockquote> 
  <h2 align="center"><br/>
    <span class="vegbank">VEGBANK ADMINISTRATION</span></h2>
  <h4 class="vegbank">Welcome, VegBank administrator.</h4>
  <ul>
    <li> 
      <h4 class="vegbank"><html:link forward="CertificationList">Certify Users</html:link></h4>
    </li>
    <li> 
      <h4 class="vegbank"><html:link forward="UserList">View/Edit Users</html:link></h4>
    </li>
    <li> 
      <h4 class="vegbank"><html:link forward="DropPlotAction">Delete plots</html:link></h4>
    </li>
    <li> 
      <h4 class="vegbank"><!--html:link forward="ManageUsers"-->Manage users &amp; roles<!--/html:link--> (not ready)</h4>
    </li>
    <li> 
      <h4 class="vegbank"><!--a href="#"-->System administration<!--/a--> (not ready)</h4>
    </li>
  </ul>
</blockquote>

<br/>


@webpage_footer_html@
