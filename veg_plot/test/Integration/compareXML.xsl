<?xml version='1.0' encoding='ISO-8859-1'?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format"
                version="1.0">

  <xsl:output method="xml"
    version="1.0"
    encoding="ISO-8859-1"/>

  <xsl:param name="output"/>
  <xsl:variable name="doc2node" select="document($output)" />
  <!--<xsl:variable name="doc2node"
  select="document('native_xml_plot4.xml')"/>-->
  <xsl:variable name="in" select="$output" />

  <xsl:template match="/">
    <testsuite errors="0" failures="0" name="INTEGATION.InsertPlot" tests="1" time="">
      <xsl:for-each select="*">
        <!--<xsl:variable name="index" select="position()" />-->
        <xsl:variable name="elementName" select="name()"/>
        <xsl:message>+++<xsl:value-of select="$elementName"/> and <xsl:value-of select="$doc2node/vegPlotPackage/project/projectName"/> and <xsl:value-of select="$in"/>+++</xsl:message>
        
<!--

            <xsl:message>####################START###################</xsl:message>
            <xsl:for-each select="$doc2node">
              <xsl:message>+++++++<xsl:value-of select="name()"/>++++++++</xsl:message>
            </xsl:for-each>
            <xsl:message>###################END######################</xsl:message>
-->
        
        <xsl:apply-templates select=".">
          <xsl:with-param name="doc2node" 
            select="$doc2node/*[name() = $elementName]" /> 
        </xsl:apply-templates>
      </xsl:for-each>
    </testsuite>
  </xsl:template>

  <xsl:template match="*">
    <xsl:param name="doc2node" />
    <!-- do your element comparison here -->
    <testcase name="test{name()}"> 

      <xsl:message>Comparing =====<xsl:value-of select="text()"/>=== with ===<xsl:value-of select="$doc2node/text()"/>===</xsl:message>
      <xsl:choose>
        <xsl:when test="normalize-space(text()) = normalize-space($doc2node/text())">
        </xsl:when>
        <xsl:otherwise>
          <failure 
            message="input:&lt;{normalize-space(text())}&gt; but output was:&lt;{normalize-space($doc2node/text())}&gt;" 
            type="junit.framework.AssertionFailedError">
            junit.framework.AssertionFailedError: expected:&lt;1&gt; but was:&lt;34&gt;
            at databaseAccess.DBinsertPlotSourceTest.testFail(Unknown Source)
          </failure>
        </xsl:otherwise>
      </xsl:choose>
    </testcase> 
    <xsl:for-each select="@*">
      <xsl:variable name="name" select="name()" />
      <xsl:apply-templates select=".">
        <xsl:with-param name="doc2node" select="$doc2node/@*[name() = $name]" />
      </xsl:apply-templates>
    </xsl:for-each>
    <xsl:for-each select="*">
      <!--<xsl:variable name="index" select="position()" />-->
      <xsl:variable name="elementName" select="name()"/>-->
      <xsl:message>+++<xsl:value-of select="$elementName"/>+++</xsl:message>
      <xsl:apply-templates select=".">
        <xsl:with-param name="doc2node" 
            select="$doc2node/*[name() = $elementName]" /> 
        <!--<xsl:with-param name="doc2node" select="$doc2node/*[position() = $index]" />-->
      </xsl:apply-templates>
    </xsl:for-each>
  </xsl:template>

  <xsl:template match="@*">
    <xsl:param name="doc2node" />
    <!-- do your attribute comparison here -->
    <xsl:value-of select="name()"/> 
    <xsl:choose>
      <xsl:when test="normalize-space(text()) = normalize-space($doc2node/text())">
      </xsl:when>
      <xsl:otherwise>
        <li>
          <!--  Attribute <b><xsl:value-of select="./name()"/> </b>
          in the element <b><xsl:value-of select="../name()"/></b>-->
          the input value &quot;<xsl:value-of select="text()"/>&quot; 
          is not equal to output value &quot;<xsl:value-of select="$doc2node/text()"/>&quot;
        </li>
      </xsl:otherwise>
    </xsl:choose>


    <!--
         <xsl:value-of select="name()"/> Vs. <xsl:value-of select="name($doc2node)"/> 
         <xsl:value-of select="."/> and <xsl:value-of select="$doc2node/."/> 
         -->
  </xsl:template>
  

</xsl:stylesheet>

<!--


  <xsl:template match="/">
    <html>
      <head><title>Release Comparison</title></head>
      <body>
        <h1>XML Comparison</h1>
        
        <p>
          Old release: <xsl:apply-templates select="$old/RELEASEDOC/RELEASESELECTION" />
        </p>
        <p>
          New release: <xsl:apply-templates select="$new/RELEASEDOC/RELEASESELECTION" />
        </p>
        

        <h2>Items Added</h2>
        <xsl:call-template name="list-additional">
          <xsl:with-param name="more" select="$in" />
          <xsl:with-param name="less" select="$out" />
        </xsl:call-template>

        <xsl:apply-templates>
          <xsl:with-param name="more" select="$in" />
          <xsl:with-param name="less" select="$out" />
        </xsl:apply-templates>

        
        <h2>Items Removed</h2>
        <xsl:call-template name="list-additional">
          <xsl:with-param name="more" select="$out" />
          <xsl:with-param name="less" select="$in" />
        </xsl:call-template>

        <h2>Items Altered</h2>
        <xsl:apply-templates select="$in" mode="compare" />
        
      </body>
    </html>
  </xsl:template>

  <xsl:template match="node()">
    <xsl:value-of select="name()"/>

    <xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="RELEASESELECTION">
    Version <xsl:value-of select="VERSION" />
  </xsl:template>

  <xsl:template name="list-additional">
    <xsl:param name="more" />
    <xsl:param name="less" />
    <ul>
     
      <xsl:for-each select="$more">
        <xsl:variable name="item" select="." />
     
        <xsl:for-each select="$less">
          <xsl:if test="not(key('items', $item/NAME))">
            <li><xsl:apply-templates select="$item" mode="describe" /></li>
          </xsl:if>
        </xsl:for-each>
      </xsl:for-each>
    </ul>
  </xsl:template>

  <xsl:template match="target" mode="describe">
    <xsl:value-of select="NAME" />
  </xsl:template>

-->
