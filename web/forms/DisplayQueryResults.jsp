<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<!-- 
*   '$RCSfile: DisplayQueryResults.jsp,v $'
*     Purpose: web page for displaying the plots stored in vegbank
*   Copyright: 2000 Regents of the University of California and the
*               National Center for Ecological Analysis and Synthesis
*     Authors: @author@
*
*    '$Author: farrell $'
*      '$Date: 2004-03-07 17:55:28 $'
*  '$Revision: 1.1 $'
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

<tiles:insert  page="/tiles/layout/classicLayout.jsp" flush="true">
	<tiles:put name="title"  value="Query Results"/>
	<tiles:put name="header" value="/tiles/common/header.jsp"/>
	<tiles:put name="footer" value="/tiles/common/footer.jsp"/>
	<tiles:put name="body"   value="/tiles/QueryList.jsp"/>
	<tiles:put name="message" value="tiles/common/message.jsp"/>
</tiles:insert>
