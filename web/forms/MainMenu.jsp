


<!--
*  '$RCSfile: MainMenu.jsp,v $'
*   Purpose: web form tosubmit community data to vegbank system
*   Copyright: 2000 Regents of the University of California and the
*              National Center for Ecological Analysis and Synthesis
*   Authors: @author@
*
*  '$Author: mlee $'
*  '$Date: 2005-03-15 12:35:28 $'
*  '$Revision: 1.18 $'
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


 
<TITLE>VegBank Main Menu</TITLE>

 


@webpage_masthead_html@


 


  <h1 align="center">
    Welcome to VegBank</h1>
<tABLE with="100%"  cellspacing="8" border="0">
<tR>
<!-- 2 column overall layout: -->

<tD width="60%" valign="top">
	<TABLE  cellpadding="0" cellspacing="0" border="0">

		  <TR valign="middle"><TD valign="middle" colspan="2" bgcolor="#CCCCCC"><img src="@images_link@transparent.gif" height="2" /></TD></TR>
	<TR><TD colspan="1" bgcolor="#CCCCCC"  align="center"><span class="greytext">View Data</span></TD><td/></TR>
	   
	<TR valign="middle"><TD align="center" valign="middle"><img src="@images_link@magnglas.png" /> </TD><TD>	       
  <table cellpadding="4" cellspacing="4" border="0"><tr valign="middle"><td valign="middle"> 

      <span class=" sizelarger">Search for <a href="@general_link@plots.html">Plots</a></span>
        <br> &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
		&raquo; <a href="@forms_link@metasearch.jsp">Metasearch</a>
        <br> &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
		&raquo; <a href="@plotquery_page@">Advanced query</a>
        <br> &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
		&raquo; <a href="@forms_link@plot-query-simple.jsp">Simple 3-in-1 search</a>
      </td></tr>
	  </table><table cellpadding="4" cellspacing="4" border="0"><tr valign="middle"><td valign="middle"> 
    

     
      <span class=" sizelarge">Search for 
	  <a href="@forms_link@CommQuery.jsp">Community Types</a></span>
      </td> </tr>
	  </table><table cellpadding="4" cellspacing="4" border="0"><tr valign="middle"><td valign="middle"> 

     
      <span class=" sizelarge"><span>Search for  
	  <a href="@forms_link@PlantQuery.jsp">Plants</a> </span></span>
    </td> </tr>
	  </table><table cellpadding="4" cellspacing="4" border="0"><tr valign="middle"><td valign="middle"> 

    
      <span class=" sizelarge">
        View  
		<a href="@general_link@metadata.html">Supplemental Data</a> 
		
      
      </span>

    </td> <td valign="middle"> <p>e.g. Methods, People, Projects, References</p></td></tr>

    <tr valign="middle"><td valign="middle"> 
      <span class=" sizelarge">
        View <a href="@web_context@DisplayDatasets.do">Your Datasets</a>
	  </TD></TR>


	  </table>
	  </TD></TR>
	<TR valign="middle"><TD align="center" valign="middle">&nbsp;</TD><TD></TD></TR><!--spacer -->

	 <TR><TD colspan="2" bgcolor="#CCCCCC"><img src="@images_link@transparent.gif" height="2" /></TD></TR>
		  	   <TR><TD colspan="1" bgcolor="#CCCCCC" align="center"><span class="greytext">My Preferences</span></TD><td/></TR>
	 <TR valign="middle"><TD align="center"><img src="@images_link@users.png" /></TD><TD>
	  <table cellpadding="4" cellspacing="4" border="0"><tr valign="middle"><td valign="middle"> 
    
    
	<!--li> 
		<span class=" sizelarge">Access or submit <a href="@general_link@parties.html">Parties</a> data</span>
		<span class=" sizelarge">Access or Submit Parties data 
		<font color="#F90000"> <b> Coming soon! </b></font> </span>
    </li-->
<!--
     
      <span class=" sizelarge">
          Edit your VegBank <html:link action="LoadUser.do">Profile</html:link>
        
      </span> 
-->			
	
      <span class=" sizelarge">View your <a href="@general_link@account.html">VegBank Account </a> 
		 </span>
		</td> </tr>
	  </table>
	      </TD></TR>
	<TR valign="middle"><TD align="center" valign="middle">&nbsp;</TD><TD></TD></TR><!--spacer -->

	        <TR><TD colspan="2" bgcolor="#CCCCCC"><img src="@images_link@transparent.gif" height="2" /></TD></TR>
	   <TR><TD colspan="1" bgcolor="#CCCCCC"  align="center"><span class="greytext">Info</span></TD><td/></TR>
	   
		  	  
	<TR valign="middle"><TD align="center" valign="middle"><img src="@images_link@info.png" /></TD><TD>

<table cellpadding="4" cellspacing="4" border="0"><tr valign="middle"><td valign="middle"> 
      <span class=" sizelarge">See <a href="@forms_link@vbsummary.jsp">an overview of VegBank Data</a> and Map 
      </span> 
      </td> </tr>
	  </table>

<table cellpadding="4" cellspacing="4" border="0"><tr valign="middle"><td valign="middle"> 
      <span class=" sizelarge">VegBank <a href="@general_link@info.html">Information</a> 
      </span> 
      </td> </tr>
	  </table>
	
	<table cellpadding="4" cellspacing="4" border="0"><tr valign="middle"><td valign="middle"> 
      <span class=" sizelarge">ESA Vegetation <a href="@panel_link@panel.html">Panel</a> 
      </span> 
      </td> </tr>
	  </table>
	  
	  <table cellpadding="4" cellspacing="4" border="0"><tr valign="middle"><td valign="middle"> 
    
    
  <!--  
      <span class=" sizelarge">
        
          Apply to become a <html:link action="LoadCertification.do">Certified VegBank User</html:link>
	<a href="@help-for-certification-href@"><img border="0" src="@image_server@question.gif"></a>
      </span>
     -->
     
      <span class=" sizelarge">See our 
	  	<a href="@general_link@sitemap.html">Site Map</a> </span></td><td valign="middle"> 
		<p><font color="#F90000"> Newly updated! </font></p>
    		

    </td> </tr>
	  </table>
	  </TD></TR>
</TABLE>
</tD> 
<!-- overall 2 column -->
<tD width="60%" valign="top">
	<TABLE  cellpadding="0" cellspacing="0" border="0">


	  	<TR><TD colspan="2" bgcolor="#CCCCCC"><img src="@images_link@transparent.gif" height="2" /></TD></TR>
		 <TR><TD colspan="1" bgcolor="#CCCCCC"  align="center"><span class="greytext">Add Data</span></TD><td/></TR>
 
	  <TR valign="middle"><TD align="center" valign="middle"><img src="@images_link@upload.png" /></TD><TD>
	  <table cellpadding="4" cellspacing="4" border="0"><tr valign="middle"><td valign="middle"> 
    
      <span class=" sizelarge">Load <a href="@DisplayUploadPlotAction@">Plot Data</a></span>
     </td><td valign="middle"> 
	  <p>
      
     &raquo; Download<a href="@vegbranch_link@vegbranch.html"> VegBranch</a></p>
     </td> </tr>
	  </table><table cellpadding="4" cellspacing="4" border="0"><tr valign="middle"><td valign="middle"> 

     
      <span class=" sizelarge">Annotate data</span></td><td valign="middle"> 
	  <p><font color="#F90000"> Coming soon! </font></p>
    		
	
	<!--li>
      <span class=" sizelarge"><a href="@general_link@client.html">Download</a> 
        the Desktop Client</span>
	</li-->
  </td></tr>
</table>
 
 <table cellpadding="4" cellspacing="4" border="0"><tr valign="middle"><td valign="middle"> 
 
      
       <span class=" sizelarge"><a href="@general_link@input.html">Input Data</a> In Forms</span></td><td valign="middle"> 
 	 
     		
 	
 	<!--li>
       <span class=" sizelarge"><a href="@general_link@client.html">Download</a> 
         the Desktop Client</span>
 	</li-->
   </td></tr>
</table>
 
</TD></TR>

	<TR valign="middle"><TD align="center" valign="middle">&nbsp;</TD><TD></TD></TR><!--spacer -->

	        <TR><TD colspan="2" bgcolor="#CCCCCC"><img src="@images_link@transparent.gif" height="2" /></TD></TR>
	   <TR><TD colspan="1" bgcolor="#CCCCCC"  align="center"><span class="greytext">Tools</span></TD><td/></TR>
	   
		  	  
	<TR valign="middle"><TD align="center" valign="middle"><img src="@images_link@tools.png" /></TD><TD>

<table cellpadding="4" cellspacing="4" border="0"><tr valign="middle"><td valign="middle"> 
      <span class=" sizelarge">VegBank <a href="@searchplugins_link@install-search.html">Mozilla/Firefox Search Engine Toolbar</a> 
      </span> 
      </td> </tr>
	  </table>

<table cellpadding="4" cellspacing="4" border="0"><tr valign="middle"><td valign="middle"> 
      <span class=" sizelarge"><a href="@vegbranch_link@vegbranch.html">VegBranch</a> 
      </span> 
      </td> </tr>
	  </table>
	
	<table cellpadding="4" cellspacing="4" border="0"><tr valign="middle"><td valign="middle"> 
      <span class=" sizelarge">The <a href="@vegbranch_link@docs/normalizer/normalizer.html">Normalizer</a> 
      </span> 
      </td> </tr>
	  </table>
	  
		  </TD></TR>
<!-- Admin -->
<% 
	Boolean isAdmin = (Boolean)(request.getSession().getAttribute("isAdmin"));

	if (isAdmin != null) {
		if (isAdmin.booleanValue()) {
%>
    <TR valign="middle"><TD align="center" valign="middle">&nbsp;</TD><TD></TD></TR><!--spacer -->

	        <TR><TD colspan="2" bgcolor="#CCCCCC"><img src="@images_link@transparent.gif" height="2" /></TD></TR>
	   <TR><TD colspan="1" bgcolor="#CCCCCC"  align="center"><span class="greytext">Admin</span></TD><td/></TR>

    
     <TR valign="middle"><TD align="center" valign="middle"><img src="@images_link@saw.png" /></TD><TD>
      
      <span class=" sizelarge">ADMINISTRATION</span>
	  
		<span class=" sizelarge">Go to the 
			<html:link action="AdminMenu.do">Admin Menu</html:link></span>
		<br/><br/>
		<span class="sizelarge">See the current <a href="@forms_link@system-status.jsp">System Status</a></span>
	    <br/><br/>
		<span class="sizelarge">Check <a href="@forms_link@businessrules.jsp">Business Rules</a></span>
     </TD></TR>
   
<% 
		}
	} 
%>     
     
</TABLE>

</tD>

</tR>

</tABLE>
	
	


<br/>



@webpage_footer_html@
