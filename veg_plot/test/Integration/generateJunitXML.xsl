<?xml version='1.0' encoding='ISO-8859-1'?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format"
                version="1.0">

  <xsl:output method="xml"
    version="1.0"
    encoding="ISO-8859-1"/>

<xsl:template match="/">
  <xsl:variable name="testCount" select="count(//testcase)"/>
  <xsl:variable name="failCount" select="count(//failure)"/>

  <testsuite  tests="{$testCount}" name="{testsuite/@name}" 
              failures="{$failCount}" errors="0">
    <xsl:copy-of select="testsuite/*"/>
  </testsuite>

</xsl:template>





</xsl:stylesheet>

