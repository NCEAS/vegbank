<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:output method="html"/>
  <xsl:template match="/">
    <html>
      <xsl:for-each select="modelChangeDocument/modelChange">
        <h3>Model Change:  <xsl:value-of select="modelChangeName"/>
        </h3>
        <blockquote>
          <xsl:value-of select="modelChangeComments"/>
        </blockquote>
        <table border="1" cellspacing="0" cellpadding="0">
          <tr>
            <td>
              <b>TableName</b>
            </td>
            <td>
              <b>FieldName</b>
            </td>
            <td>
              <b>Type</b>
            </td>
            <td>
              <b>Nulls</b>
            </td>
            <td>
              <b>Field Definition</b>
            </td>
            <td>
              <b>Permissible Values</b>
            </td>
          </tr>
          <xsl:for-each select="table">
            <xsl:variable name="shadeOfGrey">
              <xsl:value-of select="translate(translate(string((position() mod 2)),'1','D'),'0','B')"/>
            </xsl:variable>
            <xsl:variable name="TableColor">#<xsl:value-of select="$shadeOfGrey"/>
              <xsl:value-of select="$shadeOfGrey"/>
              <xsl:value-of select="$shadeOfGrey"/>
              <xsl:value-of select="$shadeOfGrey"/>
              <xsl:value-of select="$shadeOfGrey"/>
              <xsl:value-of select="$shadeOfGrey"/>
            </xsl:variable>
<xsl:for-each select="field">
<!-- the following vairable is 'F' for even rows, and 'C' for odd rows -->
<xsl:variable name="shadeOfYellow">
<xsl:value-of select="translate(translate(string((position() mod 2)),'0','F'),'1','C')"/></xsl:variable>
<!-- this variable sets the row color for this row -->
<xsl:variable name="RowColor">#FFFF<xsl:value-of select="$shadeOfYellow"/><xsl:value-of select="$shadeOfYellow"/></xsl:variable>
<tr>           <td bgcolor="{$TableColor}">
                  <xsl:value-of select="../tableName"/>
                </td>
                <td bgcolor="{$RowColor}">
                  <xsl:value-of select="fieldName"/>
                </td>
                <td bgcolor="{$RowColor}">
                  <xsl:value-of select="fieldType"/>
                </td>
                <td bgcolor="{$RowColor}">
                  <xsl:value-of select="nulls"/>
                </td>
                <td bgcolor="{$RowColor}">
                  <xsl:value-of select="fieldDefinition"/>
                </td>
                <td bgcolor="{$RowColor}">
                  <xsl:for-each select="closedListValue">
                      <xsl:value-of select="values" /><br />
                  </xsl:for-each>
                </td>
              </tr>
            </xsl:for-each>
          </xsl:for-each>
        </table>
      </xsl:for-each>
    </html>
  </xsl:template>
</xsl:stylesheet>
