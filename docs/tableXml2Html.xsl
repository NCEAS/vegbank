<?xml version="1.0"?>


<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

  <xsl:output method="html" encoding="iso-8859-1"/>

  <xsl:template match="/dataModel">
	
<head>
	<title><xsl:value-of select="title"/></title>
</head>


<body BGCOLOR="#FFFFFF">

<p>
<a href="#image" border="0">
  <img border="0" src="owlogo.jpg" alt="this image will change"/>
</a>
</p>

<p>
<b><FONT SIZE="-1" FACE="arial">Vegetation Plots Database Model 2000</FONT></b>
</p>


<b><FONT SIZE="+2" FACE="arial"><xsl:value-of select="entity/entityName"/></FONT></b> 
<p>
<i><FONT SIZE="-1" FACE="arial"><xsl:value-of select="entity/entitySummary"/></FONT></i>
</p>



<TABLE BORDER="1" CELLPADDING="0" CELLSPACING="0" WIDTH="650">
<TR>
        <TD></TD>
</TR>

<TR>
        <TD><B><FONT SIZE="-1" FACE="arial">COLUMN NAME</FONT></B></TD>
        <TD><B><FONT SIZE="-1" FACE="arial">NULLS</FONT></B></TD>
        <TD><B><FONT SIZE="-1" FACE="arial">DATA TYPE</FONT></B></TD>
        <TD><B><FONT SIZE="-1" FACE="arial">K</FONT></B></TD>
        <TD><A HREF="FKDefinition.html"><B><FONT SIZE="-1" FACE="arial">REFERENCES</FONT></B></A></TD>
        <TD><A HREF="NotesDefinition.html"><B><FONT SIZE="-1"
             FACE="arial">NOTES</FONT></B></A></TD>
        <TD><B><FONT SIZE="-1" FACE="arial">DEFINITION</FONT></B></TD>
    </TR>


<xsl:for-each select="entity/attribute">

 <TR>
        <TD><FONT SIZE="-1" FACE="arial"><xsl:value-of select="attName"/></FONT></TD>
        <TD><FONT SIZE="-1" FACE="arial"><xsl:value-of select="attNulls"/></FONT></TD>
        <TD><FONT SIZE="-1" FACE="arial"><xsl:value-of select="attType"/></FONT></TD>
        <TD><FONT SIZE="-1" FACE="arial"><xsl:value-of select="attKey"/></FONT></TD>
        <TD><FONT SIZE="-1" FACE="arial"><xsl:value-of select="attReferences"/></FONT></TD>
        <TD><FONT SIZE="-1" FACE="arial"><xsl:value-of select="attNotes"/></FONT></TD>
        <TD><FONT SIZE="-1" FACE="arial"><xsl:value-of select="attDefinition"/></FONT></TD>
</TR>

</xsl:for-each>
</TABLE>


<p>
</p>

<p>
<i><FONT SIZE="-2" FACE="arial"><xsl:value-of select="entity/entityDescription"/></FONT></i>
</p>


<P>
<B><FONT SIZE="-1" FACE="arial">Child tables: </FONT></B><FONT
 SIZE="-1" FACE="arial"> <xsl:value-of select="entity/entityName"/> serves as the parent of the 
following tables: <xsl:value-of select="entity/children"/> </FONT> </P>
<P>
<FONT SIZE="-2">last updated: <xsl:value-of select="date"/>  by: <xsl:value-of select="author"/> at: <xsl:value-of select="institution"/>  </FONT></P>


</body>
</xsl:template>
</xsl:stylesheet>
