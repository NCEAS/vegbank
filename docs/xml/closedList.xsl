<?xml version="1.0"?>
<!-- This style sheet will allow the user to parse an xml document into a
	flat-ascii text format which will have all the attributes corresponding to
	the plots database, and vegPlot.dtd-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <!--capture the information to be put in the name table-->
  <xsl:output method="html"/>
  <xsl:template match="/tableDefns">
    <a name="top" />
    <h2>Current VegBank closed list values</h2>
    <p>updated: <xsl:value-of select="update" /> <br />(sort order, if any, is applied)</p>
      <p><font size="-1">
      <xsl:for-each select="list">
       <a href="#{TableName}.{FieldName}"><xsl:value-of select="TableName" />.<xsl:value-of select="FieldName" /></a><xsl:text> </xsl:text>
      </xsl:for-each>
   </font>      </p>
    <table border="1" cellpadding="0" cellspacing="0" width="100%">
      <xsl:for-each select="list">
        <tr>
          <td colspan="4">
            <b><a name="{TableName}.{FieldName}" />
              <font size="+2">Table: <xsl:value-of select="TableName"/>
              </font>
              <br/>
              Field: <a href="http://vegbank.nceas.ucsb.edu/vegbank/dbdictionary/dd~table~{translate(TableName,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')}~field~{translate(FieldName,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')}~type~tableview.html">
                <xsl:value-of select="FieldName"/>
              </a>
            </b>
          </td>
        </tr>
        <tr>
          <td colspan="4" bgcolor="#FFFFCC">
            <i>
              <xsl:value-of select="listcomment"/>
            </i>
          </td>
        </tr>
        <tr>
          <!-- <td width="5"><font size="-2">ord</font></td> -->
          <td>
            <b>Value:</b>
          </td>
          <td>
            <b>Description:</b>
          </td>
          <td>
            <b>Internal Notes</b>
          </td>
        </tr>
        <xsl:for-each select="record">
          <xsl:sort data-type="number" select="sortOrd"/>
          <tr>
            <!--       <td><xsl:value-of select="sortOrd" /></td> -->
            <td>
              <xsl:value-of select="values"/>
              <xsl:text/>
            </td>
            <td>
              <xsl:if test="string-length(valueDescription)&gt;'1'">
                <xsl:value-of select="valueDescription"/>
              </xsl:if>
              <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
            </td>
                <td>
                  <xsl:value-of select="valueNotes"/>
                </td>
          </tr>
        </xsl:for-each>
        <tr>
          <td colspan="4">
            <a href="#top">back to top</a>
          </td>
        </tr>
      </xsl:for-each>
    </table>
  </xsl:template>
</xsl:stylesheet>
