<!-- 
  *   '$RCSfile: certification.jsp,v $'
  *     Purpose: web form to submit vegbank cerification request
  *   Copyright: 2000 Regents of the University of California and the
  *               National Center for Ecological Analysis and Synthesis
  *     Authors: @author@
  *
  *    '$Author: anderson $'
  *      '$Date: 2004-01-08 23:39:53 $'
  *  '$Revision: 1.1 $'
  *
  *
  -->
<html>
<HEAD>

<TITLE>VegBank Certification</TITLE>
<link rel="stylesheet" href="@stylesheet@" type="text/css">

<meta http-equiv="Content-Type" content="text/html; charset=">


</HEAD>
<BODY BGCOLOR="#FFFFFF" TEXT="#531100" link="#0033CC" vlink="#005680" alink="#0066FF">

@vegbank_header_html_normal@

        <h2 align="center" class="VegBank"><font face="Georgia, Times New Roman, Times, serif">Application 
          Form for VegBank Certification</font></h2>
        <p class="VegBank"><span class="VegBank">
                Use this form to request VegBank certification, if you wish to contribute data to VegBank.
		        <a href="@help-for-certification-href@"><b>Please see the explanation 
here</b></a> for more information. <br/>
        <font face="Georgia, Times New Roman, Times, serif" size="2">
          <font color="red">*</font> Required fields <br>
          <font color="blue" size=-1><sup>#</sup></font> Fields that will be listed in the public registry of certified users 
        </font></span></p> 

<form  method="get" action="@usermanagement_servlet@" >
 	<input type="hidden" name="action" value="certification">
	
	<table width="800" border="0" cellspacing="0" cellpadding="1">
    <tr> 
      <td ><span class="VegBank"><font face="Georgia, Times New Roman, Times, serif" size="2">1 
        - Name<font color="red">*</font> <font color="blue" size=-1><sup>#</sup></font> </font></span></td>
      <td ><font face="Georgia, Times New Roman, Times, serif" size="2"> 
        First 
        <input type="text" name="givenName" size="20" maxlength="20">
        Last 
        <input type="text" name="surName" size="25" maxlength="25">
        </font></td>
    </tr>
    <tr> 
      <td ><span class="VegBank"><font face="Georgia, Times New Roman, Times, serif" size="2">2 
        - e-mail<font color="red">*</font> <font color="blue" size=-1><sup>#</sup></font> </font></span></td>
      <td ><font face="Georgia, Times New Roman, Times, serif" size="2"> 
        <input type="text" name="email" size="50">
        </font></td>
    </tr>
    <tr> 
      <td ><span class="VegBank"><font face="Georgia, Times New Roman, Times, serif" size="2">3 
        - phone<font color="red">*</font></font></span></td>
      <td ><font face="Georgia, Times New Roman, Times, serif" size="2"> 
        <input type="text" name="phoneNumber" size="20">
 				<input type="hidden" name="phoneType" value="unknown">	
        </font></td>
    </tr>
    <tr> 
      <td ><span class="VegBank"><font face="Georgia, Times New Roman, Times, serif" size="2">4 
        - current certification level<font color="red">*</font> <font color="blue" size=-1><sup>#</sup></font></font></span></td>
      <td ><font face="Georgia, Times New Roman, Times, serif" size="2"> 
        <select name="currentCertLevel">
          <option value="1">Registered User</option>
          <option value="2">Certified User</option>
          <option value="3">Professional User</option>
        </select>
        </font></td>
    </tr>
    <tr> 
      <td> 
        <p><span class="VegBank"><font face="Georgia, Times New Roman, Times, serif" size="2">5 
          - Level of certification requested:
          </font></span><span class="VegBank"><font face="Georgia, Times New Roman, Times, serif" size="2"> 
          <font color="red">*</font></font></span></p>
      </td>
      <td><font face="Georgia, Times New Roman, Times, serif" size="2"> 
        <select name="certReq">
          <option value="2">Certified User</option>
          <option value="3">Professional User</option>
        </select>
        </font>  
<a href="@help-for-userlevels-href@">        
        What do the levels mean?</a></td>
    </tr>
<!--
    <tr> 
      <td ><span class="VegBank"><font face="Georgia, Times New Roman, Times, serif" size="2">6 
        - Attach brief CV</font></span></td>
      <td ><font face="Georgia, Times New Roman, Times, serif" size="2"> 
        <input type="file" name="cv">
        </font></td>
    </tr>
-->
    <tr> 
      <td ><span class="VegBank"><font face="Georgia, Times New Roman, Times, serif" size="2">6 
        - Highest degree<font color="red">*</font> </font></span></td>
      <td ><font face="Georgia, Times New Roman, Times, serif" size="2"> 
        <select name="highestDegree">
          <option value="0">None</option>
          <option value="1">BA/BS</option>
          <option value="2">MA/MS</option>
          <option value="3">PhD</option>
        </select>
        </font></td>
    </tr>
    <tr>
      <td  ><span class="VegBank"><font face="Georgia, Times New Roman, Times, serif" size="2">7 
        - Year of degree<font color="red">*</font></font></span></td>
      <td  ><font face="Georgia, Times New Roman, Times, serif" size="2"> 
        <input type="text" name="degreeYear" size="4" maxlength="4">
        </font></td>
    </tr>
    <tr> 
      <td ><span class="VegBank"><font face="Georgia, Times New Roman, Times, serif" size="2">8 
        - Institution of highest degree<font color="red">*</font> <font color="blue" size=-1><sup>#</sup></font></font></span></td>
      <td ><font face="Georgia, Times New Roman, Times, serif" size="2"> 
        <input type="text" name="degreeInst" size="50" maxlength="50">
        </font></td>
    </tr>
    <tr> 
      <td ><span class="VegBank"><font face="Georgia, Times New Roman, Times, serif" size="2">9 
        - Current institution<font color="red">*</font> <font color="blue" size=-1><sup>#</sup></font></font></span></td>
      <td ><font face="Georgia, Times New Roman, Times, serif" size="2"> 
        <input type="text" name="currentInst">
        </font></td>
    </tr>
    <tr> 
      <td ><span class="VegBank"><font face="Georgia, Times New Roman, Times, serif" size="2">10 
        - Current position<font color="red">*</font> <font color="blue" size=-1><sup>#</sup></font></font></span></td>
      <td ><font face="Georgia, Times New Roman, Times, serif" size="2"> 
        <input type="text" name="currentPos" size="50" maxlength="50">
        </font></td>
    </tr>
    <tr> 
      <td  ><span class="VegBank"><font face="Georgia, Times New Roman, Times, serif" size="2">11 
        - ESA Certified Ecologist ?<font color="red">*</font> <font color="blue" size=-1><sup>#</sup></font></font></span></td>
      <td  ><font face="Georgia, Times New Roman, Times, serif" size="2"> 
        No 
        <input type="radio" name="esaPos" value="no" checked>
        ; Yes 
        <input type="radio" name="esaPos" value="yes">
        </font></td>
    </tr>
    <tr> 
      <td colspan="2"><span class="VegBank"><font face="Georgia, Times New Roman, Times, serif" size="2">12 
        - Other professional certification or experience?</font></span></td>
    </tr>
    <tr> 
      <td  ><font face="Georgia, Times New Roman, Times, serif" size="2"></font></td>
      <td  > 
        <textarea name="profExperienceDoc" cols="80" rows="3"></textarea>
      </td>
    </tr>
    <tr> 
      <td colspan="2" ><span class="VegBank"><font face="Georgia, Times New Roman, Times, serif" size="2">13 
        - List up to 5 relevant publications/theses <br>

    </tr>
    <tr> 
      <td ><font face="Georgia, Times New Roman, Times, serif" size="2"></font></td>
      <td > 
        <textarea name="relevantPubs" cols="80" rows="3"></textarea>
      </td>
    </tr>
    <tr> 
      <td colspan="2" ><span class="VegBank"><font face="Georgia, Times New Roman, Times, serif" size="2">14 
        - Briefly describe your background and expertise in vegetation sampling:<font color="red">*</font></font></span></td>
      
    </tr>
    <tr> 
      <td ><font face="Georgia, Times New Roman, Times, serif" size="2"></font></td>
      <td > 
        <textarea name="vegSamplingDoc" cols="80" rows="3"></textarea>
      </td>
    </tr>
    <tr> 
      <td colspan="2" ><span class="VegBank"><font face="Georgia, Times New Roman, Times, serif" size="2">15 
        - Briefly describe your background and expertise with vegetation analysis, 
        description &amp; classification:<font color="red">*</font></font></span></td>
      
    </tr>
    <tr> 
      <td ><font face="Georgia, Times New Roman, Times, serif" size="2"></font></td>
      <td > 
        <textarea name="vegAnalysisDoc" cols="80" rows="3"></textarea>
      </td>
    </tr>
    <tr> 
      <td colspan="2" ><span class="VegBank"><font face="Georgia, Times New Roman, Times, serif" size="2">16 
        - Briefly describe your prior experience with the US National Vegetation 
        Classification, if any:<font color="red">*</font></font></span></td>
      
    </tr>
    <tr> 
      <td ><font face="Georgia, Times New Roman, Times, serif" size="2"></font></td>
      <td > 
        <textarea name="usnvcExpDoc" cols="80" rows="3"></textarea>
      </td>
    </tr>
    <tr> 
      <td colspan="2" ><span class="VegBank"><font face="Georgia, Times New Roman, Times, serif" size="2">17 
        - Briefly describe your prior experience with the VegBank archive, if 
        any:<font color="red">*</font></font></span></td>
      
    </tr>
    <tr> 
      <td ><font face="Georgia, Times New Roman, Times, serif" size="2"></font></td>
      <td > 
        <textarea name="vegbankExpDoc" cols="80" rows="3"></textarea>
      </td>
    </tr>
  <tr> 
      <td colspan="2" ><span class="VegBank"><font face="Georgia, Times New Roman, Times, serif" size="2">18 
        - What use do you anticipate making the VegBank archive?<font color="red">*</font></font></span></td>
      
    </tr>
    <tr> 
      <td ><font face="Georgia, Times New Roman, Times, serif" size="2"></font></td>
      <td > 
        <textarea name="useVegbank" cols="80" rows="3"></textarea>
      </td>
    </tr>
    <tr> 
      <td colspan="2" ><span class="VegBank"><font face="Georgia, Times New Roman, Times, serif" size="2">19 
        - Briefly describe your prior experience with other vegetation plot database 
        and/or analytical tools:<font color="red">*</font></font></span></td>
      
    </tr>
    <tr> 
      <td >&nbsp;</td>
      <td > 
        <textarea name="plotdbDoc" cols="80" rows="3"></textarea>
      </td>
    </tr> 
    <tr> 
      <td colspan="2"> 
        <div align="left"><span class="VegBank"><font face="Georgia, Times New Roman, Times, serif" size="2"> 
          20 - Please rate your expertise for the 3 regions you know best in terms 
          of knowledge of <br>
          the vegetation, the flora, and the US-NVC (1=Weak, 5=Expert) <font color="blue" size=-1><sup>#</sup></font></font></span></div>
      </td>
    </tr>
    <tr><td /><td>
    <table>
    <tr> 
      <td> 
        Region
      </td>
      <td > 
        Vegetation
      </td>
      <td > 
        Floristics
      </td>
      <td > 
        US-NVC
      </td>
    </tr>
    <tr> 
      <td > 
        <select name="nvcExpRegionA">
          <option>US/CAN - NE </option>
          <option>US - SE</option>
          <option>US/CAN - Midwest </option>
          <option>US/CAN - Great Plains</option>
          <option>US/CAN - Rocky Mts </option>
          <option>US/CAN - NW </option>
          <option>US/Mex - SW</option>
          <option>US Alaska / N Canada</option>
          <option>US - California</option>
          <option>South &amp; Middle Amer</option>
          <option>Pacific</option>
          <option>Carribean</option>
          <option>Europe</option>
          <option>Asia</option>
          <option>Africa</option>
          <option>Australia/New Zealand</option>
        </select>
      </td>
      <td > 
        <select name="nvcExpVegA">
          <option value="1">1</option>
          <option value="2">2</option>
          <option value="3">3</option>
          <option value="4">4</option>
          <option value="5">5</option>
        </select>
      </td>
      <td > 
        <select name="nvcExpFloristicsA">
          <option value="1">1</option>
          <option value="2">2</option>
          <option value="3">3</option>
          <option value="4">4</option>
          <option value="5">5</option>
        </select>
      </td>
      <td > 
        <select name="nvcExpNVCA">
          <option value="1">1</option>
          <option value="2">2</option>
          <option value="3">3</option>
          <option value="4">4</option>
          <option value="5">5</option>
        </select>
      </td>
    </tr>
    <tr> 
      <td >
        <select name="nvcExpRegionB">
          <option>--no other regions--</option>
          <option>US/CAN - NE </option>
          <option>US - SE</option>
          <option>US/CAN - Midwest </option>
          <option>US/CAN - Great Plains</option>
          <option>US/CAN - Rocky Mts </option>
          <option>US/CAN - NW </option>
          <option>US/Mex - SW</option>
          <option>US Alaska / N Canada</option>
          <option>US - California</option>
          <option>South &amp; Middle Amer</option>
          <option>Pacific</option>
          <option>Carribean</option>
          <option>Europe</option>
          <option>Asia</option>
          <option>Africa</option>
          <option>Australia/New Zealand</option>
        </select>
      </td>
      <td > 
        <select name="nvcExpVegB">
          <option value="1">1</option>
          <option value="2">2</option>
          <option value="3">3</option>
          <option value="4">4</option>
          <option value="5">5</option>
        </select>
      </td>
      <td > 
        <select name="nvcExpFloristicsB">
          <option value="1">1</option>
          <option value="2">2</option>
          <option value="3">3</option>
          <option value="4">4</option>
          <option value="5">5</option>
        </select>
      </td>
      <td > 
        <select name="nvcExpNVCB">
          <option value="1">1</option>
          <option value="2">2</option>
          <option value="3">3</option>
          <option value="4">4</option>
          <option value="5">5</option>
        </select>
      </td>
    </tr>
    <tr> 
      <td ><font face="Georgia, Times New Roman, Times, serif" size="2">
        <select name="nvcExpRegionC">
          <option>--no other regions--</option>
          <option>US/CAN - NE </option>
          <option>US - SE</option>
          <option>US/CAN - Midwest </option>
          <option>US/CAN - Great Plains</option>
          <option>US/CAN - Rocky Mts </option>
          <option>US/CAN - NW </option>
          <option>US/Mex - SW</option>
          <option>US Alaska / N Canada</option>
          <option>US - California</option>
          <option>South &amp; Middle Amer</option>
          <option>Pacific</option>
          <option>Carribean</option>
          <option>Europe</option>
          <option>Asia</option>
          <option>Africa</option>
          <option>Australia/New Zealand</option>
        </select>
        </font> </td>
      <td > 
        <select name="nvcExpVegC">
          <option value="1">1</option>
          <option value="2">2</option>
          <option value="3">3</option>
          <option value="4">4</option>
          <option value="5">5</option>
        </select>
      </td>
      <td > 
        <select name="nvcExpFloristicsC">
          <option value="1">1</option>
          <option value="2">2</option>
          <option value="3">3</option>
          <option value="4">4</option>
          <option value="5">5</option>
        </select>
      </td>
      <td > 
        <select name="nvcExpNVCC">
          <option value="1">1</option>
          <option value="2">2</option>
          <option value="3">3</option>
          <option value="4">4</option>
          <option value="5">5</option>
        </select>
      </td>
    </tr>
      </table>
    </td>
    </tr>
    <tr> 
      <td colspan="2" ><span class="VegBank"><font face="Georgia, Times New Roman, Times, serif" size="2">21 
        - List up to two ESA members who are familiar with your background and 
        expertise and who have agreed to sponsor your certification.</font></span></td>
      
    </tr>
    <tr> 
      <td />
      <td nowrap><font face="Georgia, Times New Roman, Times, serif" size="2"> 
        Name: <input type="text" name="esaSponsorNameA" size="30" maxlength="50">
        e-mail: <input type="text" name="esaSponsorEmailA" size="30"></font>  
      </td>
    </tr>
    <tr> 
      <td />
      <td nowrap><font face="Georgia, Times New Roman, Times, serif" size="2">
	  	 Name: <input type="text" name="esaSponsorNameB" size="30" maxlength="50">
         e-mail: <input type="text" name="esaSponsorEmailB" size="30"></font>
      </td>
    </tr>

   <tr> 
      <td colspan="2" ><span class="VegBank"><font face="Georgia, Times New Roman, Times, serif" size="2">22 
        - Check if you would be interested in serving as a peer reviewer for proposed 
        changes in the US-NVC?</font></span>  <input type="checkbox" name="peerReview" value="yes">
        </td>
    </tr>

    <tr> 
      <td colspan="2" ><span class="VegBank"><font face="Georgia, Times New Roman, Times, serif" size="2">23
        - Additional statements:</font></span></td>
      
    </tr>
    <tr> 
      <td ><font face="Georgia, Times New Roman, Times, serif" size="2"></font></td>
      <td > 
        <textarea name="additionalStatements" cols="80" rows="3"></textarea>
      </td>
    </tr>
    <tr> 
      <td ><font face="Georgia, Times New Roman, Times, serif" size="2"></font></td>
      <td > 
        <input type="submit" name="Submit" value="Submit">
      </td>
    </tr>
  </table>
<p class="VegBank"><span class="VegBank"><font face="Georgia, Times New Roman, Times, serif" size="2">
          <font color="red">*</font> Required fields <br>
          <font color="blue" size=-1><sup>#</sup></font> Fields that will be listed in the public registry of certified users 
          </font></span></p> 
</form>
<p class="VegBank">&nbsp;</p>
        


@vegbank_footer_html_onerow@
</BODY>
</HTML>
