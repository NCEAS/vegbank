

<!-- 
  *   '$Id: view_certification.jsp,v 1.11 2005-03-15 18:59:58 mlee Exp $ '
  *     Purpose: web form to submit vegbank cerification request
  *   Copyright: 2000 Regents of the University of California and the
  *               National Center for Ecological Analysis and Synthesis
  *     Authors: @author@
  *
  *    '$Author: mlee $'
  *      '$Date: 2005-03-15 18:59:58 $'
  *  '$Revision: 1.11 $'
  *
  *
  -->
@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@


<TITLE>VegBank Certification Administration</TITLE>






@webpage_masthead_html@

<a name="top"/>



        <h2 align="center" class="vegbank">Certification Application: 
			<i><bean:write name="certBean" property="surName"/></i>
		</h2>

		<html:errors/>

        <p class="sizenormal">
        </p> 
	
	<!-- main table -->
	<table  border="0" cellspacing="5" cellpadding="2">
	<html:form method="post" action="ViewCertification.do">
		<html:hidden name="certBean" property="usrId"/>
		<html:hidden name="certBean" property="certId"/>

    <tr> 
      <td colspan="2" class="sizesmall">
	    <br/>
        <b>Enter comments</b> (optional):
        <br/>
        <html:textarea name="certBean" property="certificationstatuscomments" cols="60" rows="3"/>
      </td>
    </tr>
    <tr> 
      <td colspan="3" class="sizesmall"> 
		<b>Choose a new status:</b>
			<html:select name="certBean" property="certificationstatus">
			  <option value="<bean:write name="certBean" property="certificationstatus"/>">
			  	--<bean:write name="certBean" property="certificationstatus"/>--</option>
			  <html:optionsCollection name="certstatuslistbean" property="allCertstatusesNames"/>
			</html:select>
		<!-- action is the language-specific button label -->
        <input type="submit" value="update status" name="action">  
		&nbsp; &nbsp; &nbsp; &nbsp; 
        <html:cancel accesskey="c" value="cancel"/>
		<!-- cmd is an implementation field that represents the action -->
        <input type="hidden" value="updateStatus" name="cmd">  
		<br/>
		= = = = = = = = = = = = = = = = = = = = = = = = = = =
		<br/>&nbsp;
	  </td>
	</tr>
    <tr> 
      <td colspan="2">
	    <!-- user personal info table -->
		<table bgcolor="#BBBBBB" cellpadding="1" cellspacing="0" border="0">
		<tr>
		<td bgcolor="#FFFFFF"><img src="@image_server@pix_clear" width="20" height="1"></td>

		<td>
			<table bgcolor="#CCCCCC" cellpadding="1" cellspacing="1" border="0" >
			<tr bgcolor="#EBF3F8"> 
			  <td align="center">Personal Information</td> 
			</tr>
			<tr bgcolor="#FEFEFE">
			  <td>
			  	<p>
				<bean:write name="certBean" property="givenName"/> 
				<bean:write name="certBean" property="surName"/>
        		(<bean:write name="certBean" property="currentCertLevelName"/>)
				<br/>

				<logic:notEqual name="certBean" property="emailAddress" value="null">
					<bean:write name="certBean" property="emailAddress"/>
				</logic:notEqual> 
				<logic:notEqual name="certBean" property="phoneNumber" value="null">
					<bean:write name="certBean" property="phoneNumber"/>
				</logic:notEqual>
				</p>
			  </td>
			</tr>
			</table>
		</td></tr>
		</table>
    </tr>
    <tr> 
      <td> 
		 
        <span class="sizesmall">
		Requested certification 
		</span>
		<br/>
		&raquo; <logic:equal name="certBean" property="requestedCert" value="2">
	    	CERTIFIED
		</logic:equal>
		<logic:equal name="certBean" property="requestedCert" value="4">
	    	PROFESSIONAL
		</logic:equal>
	  </td>
    </tr>
    <tr> 
      <td>
		 
	    <span class="sizesmall">
		Highest degree
	    </span>
        <br/> 
	    &raquo; <bean:write name="certBean" property="highestDegree"/>
        </td>
    </tr>
    <tr>
      <td>
	    <span class="sizesmall">Year of degree</span>
        <br/> 
        &raquo; <bean:write name="certBean" property="degreeYear"/>
        </td>
    </tr>
    <tr> 
      <td>
		
	    <span class="sizesmall">
		Institution of highest degree
		</span>
        <br/> 
        &raquo; <bean:write name="certBean" property="degreeInst"/>
        </td>
    </tr>
    <tr> 
      <td>
		
		 
	    <span class="sizesmall">
		Current organization  
		</span>
		<br/>
        &raquo; <bean:write name="certBean" property="currentOrg"/>
        </td>
    </tr>
    <tr> 
      <td>
        
		 
	    <span class="sizesmall">
		Current position </span>
		<br/>
        &raquo; <bean:write name="certBean" property="currentPos"/>
        </td>
    </tr>
    <tr> 
      <td>
        
		 
	    <span class="sizesmall">
		ESA Certified Ecologist?</span>
		<br/>
		&raquo;
		<logic:equal name="certBean" property="esaMember" value="1">
		 	YES
		</logic:equal>
		<logic:equal name="certBean" property="esaMember" value="0">
		 	NO
		</logic:equal>
        </td>
    </tr>
    <tr> 
      <td colspan="2">
	    <br/>
        <span class="sizesmall">Other <b>PROFESSIONAL</b> certification or experience?</span>
		<br/>
        &raquo; <bean:write name="certBean" property="profExp"/>
      </td>
    </tr>
    <tr> 
      <td colspan="2">
		<br/>
	    <span class="sizesmall">
        List up to 5 of your relevant <b>PUBLICATIONS/THESES</b>:
		</span>
		<br/>
        &raquo; <bean:write name="certBean" property="relevantPubs"/>
      </td>
    </tr>
    <tr> 
      <td colspan="2">
        <br/>
         
	    <span class="sizesmall">
		Briefly describe your background and expertise in vegetation <b>SAMPLING</b>:</span>
        <br/>
        &raquo; <bean:write name="certBean" property="vegSamplingExp"/>
      </td>
    </tr>
    <tr> 
      <td colspan="2">
        <br/>
         
	    <span class="sizesmall">
		Briefly describe your background and expertise with vegetation <b>ANALYSIS</b>, description &amp; classification:</span>
        <br/>
        &raquo; <bean:write name="certBean" property="vegAnalysisExp"/>
      </td>
    </tr>
    <tr> 
      <td colspan="2">
        <br/>
         
	    <span class="sizesmall">
		Briefly describe your prior experience with the <b>US National Vegetation Classification</b>, if any:</span>
		<br/>
        &raquo; <bean:write name="certBean" property="usnvcExp"/>
      </td>
    </tr>
    <tr> 
      <td colspan="2">
        <br/>
	    <span class="sizesmall">
		Briefly describe your prior experience with <b>VEGBANK</b>, if any:</span>
        <br/>
        &raquo; <bean:write name="certBean" property="vbExp"/>
      </td>
    </tr>
  <tr> 
      <td colspan="2">
        <br/>
         
	    <span class="sizesmall">
		How do you <b>INTEND TO USE VEGBANK</b>?</span>
		<br/>
        &raquo; <bean:write name="certBean" property="vbIntention"/>
      </td>
    </tr>
    <tr> 
      <td colspan="2">
        <br/> 
	    <span class="sizesmall">
		Briefly describe your prior experience with vegetation plot <b>DATABASES</b> and/or analytical <b>TOOLS</b>:</span>
		<br/>
        &raquo; <bean:write name="certBean" property="toolsExp"/>
      </td>
    </tr> 
	<tr bgcolor="#FFFFFF"> 
	  <td colspan="4">
	    <br/> 
	     
		<span class="sizesmall">
	    Please rate your expertise for the <b>3 REGIONS</b> you know best in terms 
	    of knowledge of the vegetation, the flora, and the US-NVC (1=Weak, 5=Expert) 
		</span>
	    <br/>

		<!-- Region expertise table -->
		<table bgcolor="#FFFFFF" cellpadding="1" cellspacing="0" border="0">
		<tr>
		<td bgcolor="#FFFFFF"><img src="@image_server@pix_clear" width="20" height="1"></td>

		<td bgcolor="#BBBBBB">
		<table bgcolor="#CCCCCC" cellpadding="1" cellspacing="1" border="0" >
		<tr bgcolor="#EBF3F8"> 
		  <td width="40%" align="center">Region</td> 
		  <td width="20%" align="center">Vegetation</td>
		  <td width="20%" align="center">Floristics</td>
		  <td width="20%" align="center">US-NVC</td>
		</tr>
		<tr bgcolor="#666666"> 
		  <td colspan="7"><img src="@image_server@pix_clear" width="1" height="1"></td>
		</tr>

		<tr bgcolor="#FFFFFF"> 
		  <td align="center"> 
			<html:select name="certBean" property="expRegionA" disabled="true">
			  <option value="">--select a region--</option>
			  <html:optionsCollection name="regionlistbean" property="allRegionsNames"/>
			</html:select>
		  </td>
		  <td bgcolor="#FFFFFF" align="center"> 
			<bean:write name="certBean" property="expRegionAVeg"/>
		  </td>
		  <td align="center"> 
			<bean:write name="certBean" property="expRegionAFlor"/>
		  </td>
		  <td align="center"> 
			<bean:write name="certBean" property="expRegionANVC"/>
		  </td>
		</tr>
		<tr bgcolor="#EEEEEE"> 
		  <td align="center">
			<html:select name="certBean" property="expRegionB" disabled="true">
			  <option value="">--no other regions--</option>
			  <html:optionsCollection name="regionlistbean" property="allRegionsNames"/>
			</html:select>
		  </td>
		  <td align="center"> 
			<bean:write name="certBean" property="expRegionBVeg"/>
		  </td>
		  <td align="center"> 
			<bean:write name="certBean" property="expRegionBFlor"/>
		  </td>
		  <td align="center"> 
			<bean:write name="certBean" property="expRegionBNVC"/>
		  </td>
		</tr>
		<tr bgcolor="#FFFFFF"> 
		  <td align="center">
			<html:select name="certBean" property="expRegionC" disabled="true">
			  <option value="">--no other regions--</option>
			  <html:optionsCollection name="regionlistbean" property="allRegionsNames"/>
			</html:select>
			</td>
		  <td bgcolor="#FFFFFF" align="center"> 
			<bean:write name="certBean" property="expRegionCVeg"/>
		  </td>
		  <td align="center"> 
			<bean:write name="certBean" property="expRegionCFlor"/>
		  </td>
		  <td bgcolor="#FFFFFF" align="center"> 
			<bean:write name="certBean" property="expRegionCNVC"/>
		  </td>
		</tr>
		  </table>
		</td></tr>
		</table>
      </td>
    </tr>
    <tr> 
      <td colspan="2"></td>
    </tr>
	<tr bgcolor="#FFFFFF">
	  <td colspan="5">
	  	<br/>
		  <span class="sizesmall">
		  List up to two ESA members who are familiar with your background and 
		  expertise and who have agreed to sponsor your certification.
		  </span>
		  <br/>
		    <!-- sponsor table -->
			<table bgcolor="#FFFFFF" cellpadding="1" cellspacing="1" border="0">
			<tr>
			<td bgcolor="#FFFFFF"><img src="@image_server@pix_clear" width="20" height="1"></td>

			<td bgcolor="#BBBBBB">
			<table bgcolor="#CCCCCC" cellpadding="1" cellspacing="1" border="0" >
			<tr bgcolor="#EBF3F8"> 
			  <td colspan="2" align="center">Member #1</td>
			  <td colspan="2" align="center">Member #2</td>
			</tr>
			<tr bgcolor="#666666"> 
			  <td colspan="5"><img src="@image_server@pix_clear" width="1" height="1"></td>
			</tr>

			<tr bgcolor="#FFFFFF"> 
			  <td colspan="2">
			    <table>
				<tr>
				  <td align="right"><span class="vegclass_small">Name: </span></td>
				  <td><bean:write name="certBean" property="esaSponsorNameA"/>
				</tr>
				<tr>
				  <td align="right"><span class="vegclass_small">e-Mail: </span></td>
				  <td><bean:write name="certBean" property="esaSponsorEmailA"/>
				</tr>
				</table>
			  </td>

			  <td colspan="2">
			    <table>
				<tr>
				  <td align="right"><span class="vegclass_small">Name: </span></td>
				  <td><bean:write name="certBean" property="esaSponsorNameB"/>
				</tr>
				<tr>
				  <td align="right"><span class="vegclass_small">e-Mail: </span></td>
				  <td><bean:write name="certBean" property="esaSponsorEmailB"/>
				</tr></table>
			  </td>
			</tr>

			</table>
			</td></tr>
			</table>

   <tr> 
      <td colspan="2">
		<br/>
	    <span class="sizesmall">
        Are you interested in serving as a <b>PEER REVIEWER</b> for proposed changes in the US-NVC?
        </span>
		<br/>
		&nbsp;&nbsp;&nbsp;&raquo; 
		<logic:equal name="certBean" property="peerReview" value="1">
		 	YES, I am interested in being a peer reviewer.
		</logic:equal>
		<logic:equal name="certBean" property="peerReview" value="0">
		 	NO thank you.
		</logic:equal>
		</td>
    </tr>

    <tr> 
      <td colspan="2">
	    <br/>
		<span class="sizesmall">
        Additional statements:
		</span>
        <br/>
		<bean:write name="certBean" property="addlStmt"/>
      </td>
    </tr>
    <tr> 
      <td colspan="2">
        <br/>
		&nbsp;
		<a href="#top">Jump to top of page</a>
		&nbsp; &nbsp; &nbsp; &nbsp; 
        <html:cancel accesskey="c" value="cancel"/>

      </td>
    </tr>
</html:form>
<!--form-->
  </table>
           



@webpage_footer_html@
