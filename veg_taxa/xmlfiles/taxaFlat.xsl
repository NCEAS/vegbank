<?xml version="1.0"?> 

<!-- This style sheet will allow the user to parse an xml document into a
		flat-ascii text file which will be loaded into the circumscription DB -->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<!--capture the information to be put in the name table-->	
	<xsl:output method="xml"/>
	<xsl:template match="/plantTaxa">
	<xsl:value-of select="name/symbol"/>
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
	<xsl:value-of select="name/taxon" />
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
	<xsl:value-of select="name/commonName" />
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
	<xsl:value-of select="name/family" />
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
	<xsl:value-of select="name/entryDate" />
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
	<xsl:value-of select="name/nameReference/author" />
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
	<xsl:value-of select="name/nameReference/dateEntered" />
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
	<xsl:value-of select="name/nameReference/citation" />
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
	<xsl:value-of select="name/nameReference/label" />
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<!--capture the information to go in the circumscription table-->	
	<xsl:text disable-output-escaping="yes">#Begin circumscription Information &#xA;  </xsl:text>
	<xsl:value-of select="circumscription/type" />
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
	<xsl:value-of select="circumscription/circumReference/author" />
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
	<xsl:value-of select="circumscription/circumReference/dateEntered" />
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
	<xsl:value-of select="circumscription/circumReference/citation" />
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
	<xsl:value-of select="circumscription/circumReference/label" />
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<!--capture the information to go in the party table-->
	<xsl:text disable-output-escaping="yes">#Begin party information &#xA;  </xsl:text>
	<xsl:value-of select="party/orgName" />
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<!--capture the information to go in the status table-->
	<xsl:text disable-output-escaping="yes">#Begin status Information &#xA;  </xsl:text>
	<xsl:value-of select="status/currentStatus" />
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>	
	<xsl:value-of select="status/statusParty" />
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
	<xsl:value-of select="status/startDate" />
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
	<xsl:value-of select="status/stopDate" />
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<!--capture the information to go in the correlation table-->	
	<xsl:text disable-output-escaping="yes">#Begin correlation Information &#xA;  </xsl:text>
	<xsl:value-of select="correlation/correlationParty" />
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
	<xsl:value-of select="correlation/correlationReference/correlationAuthor" />
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
	<xsl:value-of select="correlation/correlationReference/correlationDateEntered" />
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
	<xsl:value-of select="correlation/correlationReference/correlationCitation" />
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
	<xsl:value-of select="correlation/correlationReference/label" />
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>	
	<xsl:value-of select="correlation/congruence" />
	<xsl:text disable-output-escaping="yes"> &#xA; </xsl:text>
<!--capture the information to go in the usage table-->	
	</xsl:template>
</xsl:stylesheet>
