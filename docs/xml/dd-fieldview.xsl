<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"  xmlns:redirect="http://xml.apache.org/xalan/redirect" extension-element-prefixes="redirect"> 
  <!-- <xsl:param name="CurrentTable" />
<xsl:param name="CurrentField" /> -->
  <!-- not needed : <xsl:param name="CurrentTable">observation</xsl:param>
  <xsl:param name="CurrentField">PLOT_ID</xsl:param> -->

    <xsl:param name="htmlPrefix" />

  <xsl:output method="html"/>
  <xsl:template match="/dataModel">
  <xsl:for-each select="entity/attribute">
  
 <redirect:write file="{$htmlPrefix}~table~{translate(../entityName,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')}~field~{translate(attName,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')}~type~fieldview.html">@webpage_top_html@
  @webpage_head_html@

 <title>VegBank Data Dictionary - field view : <xsl:value-of select="../entityName" />.<xsl:value-of select="attName" /></title>
<script type="text/javascript">

function getHelpPageId() {
  return "data-dictionary";
}


</script>
    @webpage_masthead_html@

      <h2>
        <a  href="{$htmlPrefix}-index.html">VegBank data dictionary</a>
      </h2>
            <p>Table: <strong><xsl:value-of select="../entityName"/></strong>  (<a href="{$htmlPrefix}~table~{translate(../entityName,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')}~field~{translate(attName,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')}~type~tableview.html">Click for table view</a>)
              <br/>
              <blockquote>
                <p>
                  <xsl:value-of select="../entitySummary"/>
                </p>
              </blockquote>
             
            </p>
            <table class="thinlines">
               <tr class="oddrow">
                <td><b><a href="dd-guide.html#field-label">field label:</a></b></td>
                <td>
                  <xsl:choose>
                    <xsl:when test="attNulls='no'">
                      <b>
                        <xsl:value-of select="attLabel"/>
                      </b>
                    </xsl:when>
                    <xsl:otherwise>
                      <xsl:value-of select="attLabel"/>
                    </xsl:otherwise>
                  </xsl:choose>
                  <xsl:if test="attModel='denorm'">
                    <br/>
                    <span class="bright" title="denormlized field, data drawn from elsewhere in the datamodel">Denormalized</span>
                  </xsl:if>

                </td>

              </tr>

              
              <tr class="evenrow">
                <td>
                  <b>
                    <a href="dd-guide.html#field-name">field name:</a>
                  </b>
                </td>
                <td>
                  <xsl:choose>
                    <xsl:when test="attNulls='no'">
                      <b>
                        <xsl:value-of select="attName"/>
                      </b>
                    </xsl:when>
                    <xsl:otherwise>
                      <xsl:value-of select="attName"/>
                    </xsl:otherwise>
                  </xsl:choose>
                  <xsl:if test="attModel='denorm'">
                    <br/>
                    <span class="bright" title="denormlized field, data drawn from elsewhere in the datamodel">Denormalized</span>
                  </xsl:if>
                </td>
              </tr>
              <tr class="oddrow">
                <td>
                  <b>
                    <a  href="dd-guide.html#nulls">nulls:</a>
                  </b>
                </td>
                <td>
                  <xsl:value-of select="attNulls"/>
                </td>
              </tr>
              <tr class="evenrow">
                <td>
                  <b>
                    <a  href="dd-guide.html#type">type:</a>
                  </b>
                </td>
                <td>
                  <xsl:value-of select="attType"/>
                </td>
              </tr>
              <tr class="oddrow">
                <td>
                  <b>
                    <a  href="dd-guide.html#key">key:</a>
                  </b>
                </td>
                <td>
                  <xsl:value-of select="attKey"/>
                </td>
              </tr>
              <tr class="evenrow">
                <td>
                  <b>
                   <a  href="dd-guide.html#references">references:</a>
                  </b>
                </td>
                <td>
                  <!-- references can be split so that it doesn't make the column too wide in output -->
<xsl:choose>
                    <xsl:when test="contains(attReferences,'.')">
                      <a  href="{$htmlPrefix}~table~{translate(substring-before(attReferences,'.'),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')}~field~{translate(substring-after(attReferences,'.'),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')}~type~tableview.html">
                        <xsl:value-of select="substring-before(attReferences,'.')"/>. <xsl:value-of select="substring-after(attReferences,'.')"/>
                      </a>
                    </xsl:when>
                    <xsl:otherwise>
                      <xsl:value-of select="attReferences"/>
                    </xsl:otherwise>
                  </xsl:choose>

                </td>
              </tr>
              <tr class="oddrow">
                <td>
                  <b>
                    <a  href="dd-guide.html#list">list:</a>
                  </b>
                </td>
                <td>
                  <xsl:value-of select="attListType"/>
                </td>
              </tr>
              <tr class="evenrow">
                <td>
                  <b>
                <a  href="dd-guide.html#field-notes">field notes:</a>
                  </b>
                </td>
                <td>
                  <xsl:value-of select="attNotes"/>
                </td>
              </tr>
              <tr class="oddrow">
                <td>
                  <b>
                 <a  href="dd-guide.html#field-definition">field definition:</a>
                  </b>
                </td>
                <td>
                  <xsl:value-of select="attDefinition"/>
                </td>
              </tr>
              <tr>
                <td colspan="2">
                  <xsl:if test="attListType!='no'">
                    <b>
             list values:
                  </b>
                    <br/>
                    <table class="thinlines">
                      <tr>
                        <th>
                          value
                        </th>
                        <th>
                          description
                        </th>
                        <th>
                          sorting
                        </th>
                      </tr>
                      <xsl:for-each select="attList/attListItem">
                        <xsl:sort data-type="number" select="attListSortOrd"/>
                        <!-- <xsl:if test="attribute/attModel='logical'"> -->
                   
                        <xsl:variable name="RowColor"><xsl:call-template name="getClass"><xsl:with-param name="position" select="position()" /></xsl:call-template>
                        </xsl:variable>
                        <tr class="{$RowColor}">
                          <td>
                            <xsl:value-of select="attListValue"/>
                          </td>
                          <td>
                            <xsl:choose>
                              <xsl:when test="string-length(attListValueDesc)&gt;0">
                                <xsl:value-of select="attListValueDesc"/>
                              </xsl:when>
                              <xsl:otherwise>--</xsl:otherwise>
                            </xsl:choose>
                          </td>
                          <td>
                            <xsl:value-of select="attListSortOrd"/>
                          </td>
                        </tr>
                      </xsl:for-each>
                    </table>
                  </xsl:if>
                </td>
              </tr>
              <!--        <tr bgcolor="#{$RowColor}"> -->
            </table>
      <p><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></p>
@webpage_footer_html@

</redirect:write>
<!-- ****************  SMALL DD ************************************************************************************ -->
<!-- ****************  SMALL DD ************************************************************************************ -->
<!-- ****************  SMALL DD ************************************************************************************ -->
<redirect:write file="{$htmlPrefix}~table~{translate(../entityName,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')}~field~{translate(attName,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')}~type~smallview.html">@webpage_top_html@
  @webpage_head_html@

 <title>VegBank Data Dictionary - field view : <xsl:value-of select="../entityName" />.<xsl:value-of select="attName" /></title>
<script type="text/javascript">

function getHelpPageId() {
  return "data-dictionary";
}


</script>
    @webpage_masthead_small_html@

      <h2>
        <a  href="{$htmlPrefix}-index.html">VegBank data dictionary</a>
      </h2>
            <p>Table:<strong><xsl:value-of select="../entityName"/></strong>  (<a href="{$htmlPrefix}~table~{translate(../entityName,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')}~field~{translate(attName,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')}~type~tableview.html">Click for table view</a>)
              <br/>
              <blockquote>
                <p>
                  <xsl:value-of select="../entitySummary"/>
                </p>
              </blockquote>
             
            </p>
            <table class="thinlines">
              <tr class="oddrow">
                <td><b><a href="dd-guide.html#field-label">field label:</a></b></td>
                <td>
                  <xsl:choose>
                    <xsl:when test="attNulls='no'">
                      <b>
                        <xsl:value-of select="attLabel"/>
                      </b>
                    </xsl:when>
                    <xsl:otherwise>
                      <xsl:value-of select="attLabel"/>
                    </xsl:otherwise>
                  </xsl:choose>
                  <xsl:if test="attModel='denorm'">
                    <br/>
                    <span class="bright" title="denormlized field, data drawn from elsewhere in the datamodel">Denormalized</span>
                  </xsl:if>

                </td>

              </tr>
              
              <tr class="evenrow">
                <td>
                  <b>
                    <a href="dd-guide.html#field-name">field name:</a>
                  </b>
                </td>
                <td>
                  <xsl:choose>
                    <xsl:when test="attNulls='no'">
                      <b>
                        <xsl:value-of select="attName"/>
                      </b>
                    </xsl:when>
                    <xsl:otherwise>
                      <xsl:value-of select="attName"/>
                    </xsl:otherwise>
                  </xsl:choose>
                  <xsl:if test="attModel='denorm'">
                    <br/>
                    <span class="bright" title="denormlized field, data drawn from elsewhere in the datamodel">Denormalized</span>
                  </xsl:if>

                </td>
              </tr>
              <tr class="oddrow">
                <td>
                  <b>
                    <a  href="dd-guide.html#nulls">nulls:</a>
                  </b>
                </td>
                <td>
                  <xsl:value-of select="attNulls"/>
                </td>
              </tr>
              <tr class="evenrow">
                <td>
                  <b>
                    <a  href="dd-guide.html#type">type:</a>
                  </b>
                </td>
                <td>
                  <xsl:value-of select="attType"/>
                </td>
              </tr>
              <tr class="oddrow">
                <td>
                  <b>
                    <a  href="dd-guide.html#key">key:</a>
                  </b>
                </td>
                <td>
                  <xsl:value-of select="attKey"/>
                </td>
              </tr>
              <tr class="evenrow">
                <td>
                  <b>
                   <a  href="dd-guide.html#references">references:</a>
                  </b>
                </td>
                <td>
                  <!-- references can be split so that it doesn't make the column too wide in output -->
<xsl:choose>
                    <xsl:when test="contains(attReferences,'.')">
                      <a  href="{$htmlPrefix}~table~{translate(substring-before(attReferences,'.'),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')}~field~{translate(substring-after(attReferences,'.'),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')}~type~tableview.html">
                        <xsl:value-of select="substring-before(attReferences,'.')"/>. <xsl:value-of select="substring-after(attReferences,'.')"/>
                      </a>
                    </xsl:when>
                    <xsl:otherwise>
                      <xsl:value-of select="attReferences"/>
                    </xsl:otherwise>
                  </xsl:choose>

                </td>
              </tr>
              <tr class="oddrow">
                <td>
                  <b>
                    <a  href="dd-guide.html#list">list:</a>
                  </b>
                </td>
                <td>
                  <xsl:value-of select="attListType"/>
                </td>
              </tr>
              <tr class="evenrow">
                <td>
                  <b>
                <a  href="dd-guide.html#field-notes">field notes:</a>
                  </b>
                </td>
                <td>
                  <xsl:value-of select="attNotes"/>
                </td>
              </tr>
              <tr class="oddrow">
                <td>
                  <b>
                 <a  href="dd-guide.html#field-definition">field definition:</a>
                  </b>
                </td>
                <td>
                  <xsl:value-of select="attDefinition"/>
                </td>
              </tr>
              <tr>
                <td colspan="2">
                  <xsl:if test="attListType!='no'">
                    <b>
             list values:
                  </b>
                    <br/>
                    <table class="thinlines">
                      <tr>
                        <th>
                          value
                        </th>
                        <th>
                          description
                        </th>
                        <th>
                          sorting
                        </th>
                      </tr>
                      <xsl:for-each select="attList/attListItem">
                        <xsl:sort data-type="number" select="attListSortOrd"/>
                        <!-- <xsl:if test="attribute/attModel='logical'"> -->
                   
                        <xsl:variable name="RowColor"><xsl:call-template name="getClass"><xsl:with-param name="position" select="position()" /></xsl:call-template>
                        </xsl:variable>
                        <tr class="{$RowColor}">
                          <td>
                            <xsl:value-of select="attListValue"/>
                          </td>
                          <td>
                            <xsl:choose>
                              <xsl:when test="string-length(attListValueDesc)&gt;0">
                                <xsl:value-of select="attListValueDesc"/>
                              </xsl:when>
                              <xsl:otherwise>--</xsl:otherwise>
                            </xsl:choose>
                          </td>
                          <td>
                            <xsl:value-of select="attListSortOrd"/>
                          </td>
                        </tr>
                      </xsl:for-each>
                    </table>
                  </xsl:if>
                </td>
              </tr>
              <!--        <tr bgcolor="#{$RowColor}"> -->
            </table>
      <p><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text></p>
@webpage_footer_small_html@

</redirect:write>



</xsl:for-each> <!-- table -->
  </xsl:template>
  <xsl:template name="getClass">
    <xsl:param name="position" />
    <xsl:choose>
      <xsl:when test="$position mod 2=0">evenrow</xsl:when>
      <xsl:otherwise>oddrow</xsl:otherwise>
    </xsl:choose>
  </xsl:template>
</xsl:stylesheet>
