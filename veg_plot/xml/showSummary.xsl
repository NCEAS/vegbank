<?xml version="1.0"?> 

<!--Style sheet for transforming plot xml files, specifically
	for the servlet transformation to show summary results
	to the web browser-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<!--capture the information to be put in the name table-->	
<xsl:output method="html"/>
<xsl:template match="/vegPlot">


<!-- There may be multiple project contributors so do a for-each select-->
<xsl:for-each select="project/plot">

<!-- Next block corresponds to the plot table ~ there will only be one attribute here-->
<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<xsl:text>  </xsl:text> <xsl:value-of select="authorPlotCode"/> <xsl:text>  </xsl:text>

</xsl:for-each>

</xsl:template>

<!--     **********************************************************   -->


<head>
	<title>"Results page"</title>
</head>


<body BGCOLOR="#white">

<p>
</p>

<p>
<b><FONT SIZE="-1" FACE="arial">Query Results</FONT></b>
</p>

<xsl:template match="/vegPlot">
<xsl:for-each select="project/plot">

<b><FONT SIZE="+1" FACE="arial">  <xsl:text>plot name: </xsl:text> <xsl:value-of select="authorPlotCode"/></FONT></b> 

<i><FONT SIZE="-1" FACE="arial"> <xsl:text>Surficial Geology : </xsl:text> <xsl:value-of select="surfGeo"/></FONT></i>
<i><FONT SIZE="-1" FACE="arial"> <xsl:text>Plot Type : </xsl:text> <xsl:value-of select="plotType"/></FONT></i>
<i><FONT SIZE="-1" FACE="arial"> <xsl:text>Origin Latitude : </xsl:text> <xsl:value-of select="plotOriginLat"/></FONT></i>
<i><FONT SIZE="-1" FACE="arial"> <xsl:text>Origin Longitude : </xsl:text> <xsl:value-of select="plotOriginLong"/></FONT></i>



</xsl:for-each>
</xsl:template>



</body>



</xsl:stylesheet>
