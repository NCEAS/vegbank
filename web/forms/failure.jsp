@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@
<!-- 
  *   '$RCSfile: failure.jsp,v $'
  *     Purpose: Deliver an error message to the user 
  *   Copyright: 2000 Regents of the University of California and the
  *               National Center for Ecological Analysis and Synthesis
  *     Authors: @author@
  *
  *    '$Author: mlee $'
  *      '$Date: 2005-03-15 12:35:28 $'
  *  '$Revision: 1.8 $'
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









@webpage_masthead_html@ 


<br/>
<h3><font color="red">A problem occured</font></h3>Please review the following error(s) before proceeding.

<logic:messagesPresent message="false">
	<ul>
	<html:messages id="error" message="false">
		<li><bean:write name="error"/></li>
	</html:messages>
	</ul>
</logic:messagesPresent>

<br/>
<br/>


<!-- VEGBANK FOOTER -->

 


@webpage_footer_html@
