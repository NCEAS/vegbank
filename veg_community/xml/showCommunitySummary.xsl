<?xml version="1.0"?>
<!-- edited with XML Spy v4.3 U (http://www.xmlspy.com) by Michael T. Lee (private) -->
<!--
  * showSummary.xsl
  *
  *      Authors: John Harris, Michael Lee
  *    Copyright: 2000 Regents of the University of California and the 
  *               National Center for Ecological Analysis and Synthesis
  *  For Details: http://www.nceas.ucsb.edu/
  *      Created: 2000 December
  * 
  * This is an XSLT (http://www.w3.org/TR/xslt) stylesheet designed to
  * convert an XML file showing the resultset of a query
  * into an HTML format suitable for rendering with modern web browsers.
-->
<!--Style sheet for transforming vegetation community xml files, specifically
	for the servlet transformation to show summary results
	to the web browser-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html"/>
	<xsl:template match="/vegCommunity">
		<html>
			<head>
				<title> VegBank -- Community concept query results </title>
				<link rel="stylesheet" type="text/css" href="http://vegbank.nceas.ucsb.edu/vegbank/includes/default.css"/>
				<!--
             <link rel="stylesheet" type="text/css" 
              href="@web-base-url@/default.css" />
        -->
				<script LANGUAGE="JavaScript">
					<!-- Modified By:  Steve Robison, Jr. (stevejr@ce.net) -->
					<!-- This script and many more are available free online at -->
					<!-- The JavaScript Source!! http://javascript.internet.com -->
					<xsl:text disable-output-escaping="yes">
&lt;!-- Begin
var checkflag = "false";
function check(field) {
if (checkflag == "false") {
for (i = 0; i &lt; field.length; i++) {
field[i].checked = true;}
checkflag = "true";
return "Uncheck All"; }
else {
for (i = 0; i &lt; field.length; i++) {
field[i].checked = false; }
checkflag = "false";
return "Check All"; }
}
//  End -->
</xsl:text>
				</script>
			</head>
			<body bgcolor="FFFFFF">
				<br/>
				<table cellSpacing="0" cellPadding="0" width="799" border="0">
					<tbody>
						<tr>
							<td vAlign="top" width="40" bgColor="#336633" height="54" rowSpan="2">&#160;</td>
							<td vAlign="top" width="429" bgColor="#336633" height="54" rowSpan="2">
								<div align="left">
									<font face="Georgia, Times New Roman, Times, serif" size="6" color="#ffffff">
										<b>VegBank </b>
									</font>
								</div>
							</td>
							<td vAlign="top" bgColor="#336633" colSpan="3" height="28">&#160;</td>
						</tr>
						<tr>
							<td vAlign="top" width="86" bgColor="#336633" height="31">
								<div align="center">
									<font face="Georgia, Times New Roman, Times, serif">
										<b>
											<a style="COLOR: #ffff80; TEXT-DECORATION: none" href="http://vegbank.nceas.ucsb.edu/vegbank/index.html">Home</a>
										</b>
									</font>
								</div>
							</td>
							<td vAlign="top" width="152" bgColor="#336633" height="31">
								<div align="center">
									<font face="Georgia, Times New Roman, Times, serif">
										<b>
											<a style="COLOR: #ffff80; TEXT-DECORATION: none" href="http://vegbank.nceas.ucsb.edu/vegbank/general/logout.html">&#160;Logout</a>
										</b>
									</font>
								</div>
							</td>
							<td vAlign="top" width="92" bgColor="#336633" height="31">
								<div align="center">
									<font face="Georgia, Times New Roman, Times, serif">
										<b>
											<a style="COLOR: #ffff80; TEXT-DECORATION: none" href="http://vegbank.nceas.ucsb.edu/vegbank/general/help.html">Help</a>
										</b>
									</font>
								</div>
							</td>
						</tr>
					</tbody>
				</table>
				<blockquote>
					<p>
						<font face="Arial, Helvetica, sans-serif" size="5"> Results of Community Concept Lookup</font>
					</p>
				</blockquote>
				<table width="799" cellpadding="0" cellspacing="0" border="1">
					<tr vAlign="top" align="left" colspan="1">
						<th class="tablehead" colspan="3" height="25">
							<font face="Arial, Helvetica, sans-serif">
Matches found: <xsl:number value="count(community)"/>
							</font>
						</th>
					</tr>
				</table>
				<!-- TABLE HEADER -->
				<table width="799" cellpadding="0" cellspacing="0" border="1">
					<tr valign="center" align="left" colspan="1">
						<th class="tablehead" width="55%" bgcolor="#336633">
							<font color="#ffff80" face="Arial, Helvetica, sans-serif" size="-1">COMMUNITY NAME</font>
						</th>
						<th class="tablehead" width="45%" bgcolor="#336633">
							<font color="#ffff80" face="Arial, Helvetica, sans-serif" size="-1">COMMUNITY CONCEPT</font>
						</th>
					</tr>
					<!-- set up the form which is required by netscape 4.x browsers -->
					<form name="myform" action="viewData" method="post">
						<!-- Header and row colors -->
						<xsl:variable name="evenRowColor">#ffffcc</xsl:variable>
						<xsl:variable name="oddRowColor">#FFFFFF</xsl:variable>
						<xsl:for-each select="community">
							<xsl:sort select="commName"/>
							<!-- ATTRIBUTES TABLE -->
							<xsl:if test="position() mod 2 = 1">
								<tr vAlign="top">
									<td vAlign="top" align="left" width="55%" bgcolor="{$evenRowColor}" height="106">
										<p>
											<font size="-1" face="Arial, Helvetica, sans-serif">
												<b>
													<a>
														<span class="category">
Community Name: <span class="item">
																<xsl:value-of select="commName"/>
															</span>
															<br/>
Parent Name:  <span class="item">
																<xsl:value-of select="parentComm/commName"/>
															</span>
															<br/>
Date Entered: <span class="item">
																<xsl:value-of select="conceptOriginDate"/>
															</span>
														</span>
													</a>
												</b>
											</font>
										</p>
									</td>
									<td vAlign="top" align="left" width="45%" bgcolor="{$evenRowColor}" height="106">
										<p>
											<b>
												<font face="Arial, Helvetica, sans-serif" size="-1">
Code: <span class="item">
														<xsl:value-of select="classCode"/>
													</span>
													<br/>
Level: <span class="item">
														<xsl:value-of select="classLevel"/>
													</span>
													<br/>
Parent Code: <span class="item">
														<xsl:value-of select="parentComm/classCode"/>
													</span>
													<br/>
Recognizing Party: <span class="item">
														<xsl:value-of select="recognizingParty"/>
													</span>
												</font>
											</b>
											<b>
												<font face="Arial, Helvetica, sans-serif" size="-1">
													<br/>
												</font>
											</b>
										</p>
									</td>
								</tr>
							</xsl:if>
							<xsl:if test="position() mod 2 = 0">
								<tr vAlign="top">
									<td vAlign="top" align="left" width="55%" bgcolor="{$oddRowColor}" height="106">
										<p>
											<font size="-1" face="Arial, Helvetica, sans-serif">
												<b>
													<a>
														<span class="category">
Community Name: <span class="item">
																<xsl:value-of select="commName"/>
															</span>
															<br/>
Parent Name:  <span class="item">
																<xsl:value-of select="parentComm/commName"/>
															</span>
															<br/>
Date Entered: <span class="item">
																<xsl:value-of select="conceptOriginDate"/>
															</span>
														</span>
													</a>
												</b>
											</font>
										</p>
									</td>
									<td vAlign="top" align="left" width="45%" bgcolor="{$oddRowColor}" height="106">
										<p>
											<b>
												<font face="Arial, Helvetica, sans-serif" size="-1">
Code: <span class="item">
														<xsl:value-of select="classCode"/>
													</span>
													<br/>
Level: <span class="item">
														<xsl:value-of select="classLevel"/>
													</span>
													<br/>
Parent Code: <span class="item">
														<xsl:value-of select="parentComm/classCode"/>
													</span>
													<br/>
Recognizing Party: <span class="item">
														<xsl:value-of select="recognizingParty"/>
													</span>
												</font>
											</b>
											<b>
												<font face="Arial, Helvetica, sans-serif" size="-1">
													<br/>
												</font>
											</b>
										</p>
									</td>
								</tr>
							</xsl:if>
						</xsl:for-each>
					</form>
				</table>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>
