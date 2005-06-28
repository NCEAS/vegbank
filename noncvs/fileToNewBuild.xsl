<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:param name="prefix">http://vegbank.org/noncvs/</xsl:param>
  <xsl:template match="/noncvs">
  <project name="checknoncvsfiles">
    <target name="check">
 
    <xsl:for-each select="file">
      <xsl:variable name="thisFile" select="name" />
         <fail message="MISSING non-cvs file: {$prefix}{$thisFile}">
       <condition>
         <not> <http url="{$prefix}{$thisFile}" />
         </not>
    </condition>
    </fail>

    </xsl:for-each>
    <echo>success! ALL noncvs files were found!</echo>
    </target>
  </project>
  </xsl:template>
</xsl:stylesheet>
