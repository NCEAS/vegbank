<?xml version="1.0"?> 
<!--
  *
  *      Authors: John Harris
  *    Copyright: 2000 Regents of the University of California and the 
  *               National Center for Ecological Analysis and Synthesis
  *  For Details: http://www.nceas.ucsb.edu/
  *      Created: 2000 December
  * 
  * This is an XSLT (http://www.w3.org/TR/xslt) stylesheet designed to
  * convert an XML file showing the resultset of a query
  * into an HTML format suitable for rendering with modern web browsers.
-->


<!--Style sheet for transforming plot xml files, specifically
	for the servlet transformation to show summary results
	to the web browser
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
 <xsl:output method="html"/>
  <xsl:template match="/plotValidationReport">

<html>
	<head>
    		<link rel="stylesheet" type="text/css" href="http://vegbank.nceas.ucsb.edu/vegbank/includes/default.css" />
  	</head>

<body bgcolor="FFFFFF">
<span class="category"> 
  <font color="red">
    <!-- format the word plot to be plural if not 1 plot -->
    <xsl:if test="count(failedValidationAttribute)=1">
    1 plot
    </xsl:if>
    <xsl:if test="count(failedValidationAttribute)!=1">
    <xsl:number value="count(failedValidationAttribute)" /> attributes
    </xsl:if>
  </font>
  failed the plot validation step:<br/>
</span>

<!-- NAVIGATION TO THE NEXT AND PREVIOUS -->
<!-- (PREV and NEXT are not yet linked)
<span class="navigation">
PREV || NEXT 
</span>
<br> </br>
-->
<!-- SOME NOTES ABOUT THE USE OF ICONS-->
<br> </br>


<!-- set up a table -->
<table width="800">


<tr colspan="1" bgcolor="#336633" align="left" valign="top">
   <th class="tablehead"><font color="#ffff80" face="Arial, Helvetica, sans-serif" size="-1">Failed Attribute(s)</font></th>
</tr>

	<!-- Header and row colors -->
        <xsl:variable name="evenRowColor">#ffffcc</xsl:variable>
        <xsl:variable name="oddRowColor">#FFFFFF</xsl:variable>
	
	   
	<xsl:for-each select="failedValidationAttribute">
	
	<tr valign="top">
             
     
    <!--if even row -->
    <xsl:if test="position() mod 2 = 1">	
		
	<!-- First Cell-->
	<td  width="20%" colspan="1" bgcolor="{$evenRowColor}" align="left" valign="middle">
			
			<a> <span class="category">Table Name: </span> 
				<span class="itemsmall">
					<xsl:value-of select="dbTable"/> <br> </br>
				</span>
			</a>
			
			<a> <span class="category">Failed Attribute: </span> 	
				<span class="itemsmall">
					<xsl:value-of select="dbAttribute"/> <br> </br>
				</span>
			</a>

			<a> <span class="category">Failed Attribute Value: </span> 
				<span class="itemsmall">
					<xsl:value-of select="failedTarget"/> <br> </br>
				</span>
			</a>
			<a> <span class="category">Acceptable Value(s): </span>
				<xsl:for-each select="constraints/constraint">
				<span class="itemsmall">
					<xsl:value-of select="."/>,
				</span>
				</xsl:for-each>
			</a>
	</td>		 
	 </xsl:if>
	 
	 
	

  
  <!--if odd row -->
	<xsl:if test="position() mod 2 = 0">
		  	
	<!-- First Cell-->
	<td  width="20%" colspan="1" bgcolor="{$oddRowColor}" align="left" valign="middle">
			
			<a> <span class="category">Table Name: </span> 
				<span class="itemsmall">
					<xsl:value-of select="dbTable"/> <br> </br>
				</span>
			</a>
			
			<a> <span class="category">Failed Attribute: </span> 	
				<span class="itemsmall">
					<xsl:value-of select="dbAttribute"/> <br> </br>
				</span>
			</a>

			<a> <span class="category">Failed Attribute Value: </span> 
				<span class="itemsmall">
					<xsl:value-of select="failedTarget"/> <br> </br>
				</span>
			</a>
			<a> <span class="category">Acceptable Value(s): </span>
				<xsl:for-each select="constraints/constraint">
				<span class="itemsmall">
					<xsl:value-of select="."/>,
				</span>
				</xsl:for-each>

			</a>
	</td>		 
	 </xsl:if>

	 </tr>    
	</xsl:for-each>

</table>
</body>
</html> 

</xsl:template>
</xsl:stylesheet>
