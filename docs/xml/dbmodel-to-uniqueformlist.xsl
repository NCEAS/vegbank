<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" >
  <xsl:output method="xml" />
  <xsl:template match="dataModel">
  <allforms>
    <xsl:for-each select="entity/attribute/attForms/formShow">
      <xsl:sort select="@name" />

       <xsl:if test="position()=1">
             <xsl:call-template name="writeUnique">
               <xsl:with-param name="oneName" select="@name" />
               <xsl:with-param name="currPos" select="position()" />
             </xsl:call-template>
          </xsl:if>
       
    </xsl:for-each>
    </allforms>
  </xsl:template>
  <xsl:template name="writeUnique">
    <xsl:param name="oneName" />
    <xsl:param name="currPos" />
   <!-- <xsl:comment>Starting write Unique with: <xsl:value-of select="$oneName" /> (<xsl:value-of select="$currPos" />)</xsl:comment> -->
    <xsl:if test="string-length($oneName)&gt;0">
       <oneForm><xsl:value-of select="$oneName" /></oneForm>

           <xsl:variable name="tempPos">
             <xsl:for-each select="/dataModel/entity/attribute/attForms/formShow">
                <xsl:sort select="@name" />
                <xsl:if test="position()&gt;$currPos and @name!=$oneName">
                  <xsl:value-of select="position()" />,
                </xsl:if> 
             </xsl:for-each>,
           </xsl:variable>
          <xsl:variable name="tempPos_numb" select="substring-before($tempPos,',')" />
          
          
           <xsl:variable name="tempName">
             <xsl:for-each select="/dataModel/entity/attribute/attForms/formShow">
                <xsl:sort select="@name" />
                <xsl:if test="position()=$tempPos_numb">
                  <xsl:value-of select="@name" />
                </xsl:if> 
             </xsl:for-each>  
           </xsl:variable>
           

       <!--    <xsl:comment>calling recursively: new name= <xsl:value-of select="$tempName" /> and pos: <xsl:value-of select="$tempPos" /></xsl:comment> -->
       <xsl:call-template name="writeUnique" >
         <xsl:with-param name="oneName" select="$tempName" />
         <xsl:with-param name="currPos" select="$tempPos_numb" />
                 
       </xsl:call-template>
    </xsl:if>
  </xsl:template>
</xsl:stylesheet>
