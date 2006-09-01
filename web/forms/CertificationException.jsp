@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@

<!-- 
*   '$RCSfile: CertificationException.jsp,v $'
*     Purpose: web page querying the plots stored in vegbank
*   Copyright: 2000 Regents of the University of California and the
*               National Center for Ecological Analysis and Synthesis
*     Authors: @author@
*
*    '$Author: mlee $'
*      '$Date: 2006-09-01 19:15:09 $'
*  '$Revision: 1.10 $'
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



<title>You Need Permission</title>



@webpage_masthead_html@

<blockquote>


  <!-- there are 2 reasons this form could be here: 1) no logged in, 2) loggen in, but not certified enough -->
  <!-- which is it? -->
    <h3>The page you tried to access requires that you be logged in and certified.
    </h3>
    <p>
          Certified and professional users can access more VegBank functionality, 
          such as loading plots or interpreting data. 
          <a href="@help-for-certification-href@">More info on certification</a>.
    </p>

  <% if ( strWebUserId == "-1" ) { %> <!-- not logged in, that's why -->
 
     <p>
     Please login to your VegBank account.     
     </p>     
          
  <% } else { %> <!-- not certified enough, that's why -->
  
    <!-- you could go get certification, or log in as a different user -->
    
    <p>
     You can either 
     
     <ul><li>
       <a href="/vegbank/LoadCertification.do">request certification</a> (this requires a response from the VegBank staff which may take a day or so) 
       </li>
       <li> or login as a different user, who already has certification, in the fields below: </li>
     </ul>  
    </p>
  <% }  %>
	
	<br/>
	<br/>
    <%@ include file="../includes/Logon.jsp" %>
  
  <br/>
  <p>
    If you need more help or think that you've encountered this message in error, please contact us at 
    <a href="mailto:help@vegbank.org">help@vegbank.org</a>
  </p>  
</blockquote>
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

