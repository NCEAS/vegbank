<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:import href="model_pieces_toSQL.xsl" />

  <xsl:output method="text"/>
  <xsl:param name="getID"></xsl:param>
  <xsl:param name="onlyVersion">1.0.5</xsl:param>
  <xsl:param name="apos">'</xsl:param>
  <xsl:param name="replc">`</xsl:param>
  <xsl:param name="level">1</xsl:param><!-- 1 is for postgres, 2 is for access -->
  <xsl:template match="* "/>
  <xsl:template match="/ | modelChangeDocument">
    <xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="dataModel">
    <!-- for regular xml doc -->
    <xsl:choose>
      <xsl:when test="string-length($onlyVersion)&gt;0">
        <!-- print changes for one version -->
        <xsl:for-each select="entity">
          <xsl:choose>
            <xsl:when test="entityCreatedVersion=$onlyVersion">
               <xsl:call-template name="createtable" />
            </xsl:when>
            <xsl:otherwise>
              <xsl:for-each select="attribute[update=$onlyVersion]">
                <xsl:call-template name="createonefield" />
              </xsl:for-each>
            </xsl:otherwise>
          </xsl:choose>
        </xsl:for-each>
      </xsl:when>
      <xsl:otherwise>
        <!-- regular -->
        <xsl:for-each select="entity">
          <xsl:call-template name="createfield" />
        </xsl:for-each>
      </xsl:otherwise>
    </xsl:choose>
    
  </xsl:template>
  
  <xsl:template match="modelChange">
     <xsl:if test="modelChangeID=$getID">
   <!--   <xsl:if test="modelChangeVersion='1.0.2'">  -->
      <xsl:apply-templates/>
    </xsl:if>
  </xsl:template>
  <xsl:template match="change">
    <xsl:choose>
      <xsl:when test="@type='create table'">
----creating tables------------      
<xsl:for-each select="entity">
  <xsl:call-template name="createtable" />
  <xsl:call-template name="addrels" />
  <xsl:if test="$level='2'"><xsl:call-template name="makeClosedListTbl" /></xsl:if>
</xsl:for-each>

      </xsl:when>
      <xsl:when test="@type='drop table'">
-------dropping tables----------------
<xsl:for-each select="entity">
<xsl:call-template name="droptable" />
</xsl:for-each>
      </xsl:when>
      <xsl:when test="@type='create field'">
---creating fields ----------------------
<xsl:for-each select="entity">
<xsl:call-template name="createfield" />
<xsl:call-template name="addrels" />
  <xsl:if test="$level='2'"><xsl:call-template name="makeClosedListTbl" /></xsl:if>
</xsl:for-each>
      </xsl:when>
      <xsl:when test="@type='drop field'">
-------dropping fields -------------------
<xsl:for-each select="entity">
<xsl:call-template name="dropfield" />
</xsl:for-each>
      </xsl:when>
      <xsl:when test="@type='alter field'">
----alter field---------
<xsl:for-each select="entity">
<xsl:call-template name="alterfield" />
<xsl:if test="$level='2'"><xsl:call-template name="makeClosedListTbl" /></xsl:if>
</xsl:for-each>


      </xsl:when>
    </xsl:choose>
  
  
  </xsl:template>
 

</xsl:stylesheet>
