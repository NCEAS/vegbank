<?xml version="1.0" encoding="UTF-8"?>
<!--<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"  xmlns:fo="http://www.w3.org/1999/XSL/Format">-->
<!--@undo@--><xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:redirect="http://xml.apache.org/xalan/redirect" extension-element-prefixes="redirect">
  <xsl:import href="http://xsltsl.sourceforge.net/modules/stdlib.xsl"/><!--@undoEnd@-->
<xsl:output method="html" />
<xsl:param name="indexLevel">3</xsl:param>

<xsl:template match="/LoadModDoc">
  <!-- root -->
  <xsl:for-each select="table"><xsl:comment>New Table= <xsl:value-of select="TableName" /> ---- </xsl:comment>
<!--index-->
     <xsl:call-template name="writeOneTbl_Lvl">
        <xsl:with-param name="currtbl">index</xsl:with-param>
        <xsl:with-param name="showFlds" select="$indexLevel" />
        <xsl:with-param name="jumpMenu">yes</xsl:with-param>
     </xsl:call-template>
     <xsl:call-template name="writeOneTbl_Lvl">
        <xsl:with-param name="currtbl">index</xsl:with-param>
        <xsl:with-param name="showFlds" select="$indexLevel" />
        <xsl:with-param name="jumpMenu">no</xsl:with-param>
     </xsl:call-template>

<!--1-->
     <xsl:call-template name="writeOneTbl_Lvl">
        <xsl:with-param name="currtbl" select="TableName" />
        <xsl:with-param name="showFlds">1</xsl:with-param>
        <xsl:with-param name="jumpMenu">yes</xsl:with-param>
     </xsl:call-template>
     <xsl:call-template name="writeOneTbl_Lvl">
        <xsl:with-param name="currtbl" select="TableName" />
        <xsl:with-param name="showFlds">1</xsl:with-param>
        <xsl:with-param name="jumpMenu">no</xsl:with-param>
     </xsl:call-template>
<!--2-->
     <xsl:call-template name="writeOneTbl_Lvl">
        <xsl:with-param name="currtbl" select="TableName" />
        <xsl:with-param name="showFlds">2</xsl:with-param>
        <xsl:with-param name="jumpMenu">yes</xsl:with-param>
     </xsl:call-template>
     <xsl:call-template name="writeOneTbl_Lvl">
        <xsl:with-param name="currtbl" select="TableName" />
        <xsl:with-param name="showFlds">2</xsl:with-param>
        <xsl:with-param name="jumpMenu">no</xsl:with-param>
     </xsl:call-template>

<!--3-->
     <xsl:call-template name="writeOneTbl_Lvl">
        <xsl:with-param name="currtbl" select="TableName" />
        <xsl:with-param name="showFlds">3</xsl:with-param>
        <xsl:with-param name="jumpMenu">yes</xsl:with-param>
     </xsl:call-template>
     <xsl:call-template name="writeOneTbl_Lvl">
        <xsl:with-param name="currtbl" select="TableName" />
        <xsl:with-param name="showFlds">3</xsl:with-param>
        <xsl:with-param name="jumpMenu">no</xsl:with-param>
     </xsl:call-template>

<!--4-->
     <xsl:call-template name="writeOneTbl_Lvl">
        <xsl:with-param name="currtbl" select="TableName" />
        <xsl:with-param name="showFlds">4</xsl:with-param>
        <xsl:with-param name="jumpMenu">yes</xsl:with-param>
     </xsl:call-template>
     <xsl:call-template name="writeOneTbl_Lvl">
        <xsl:with-param name="currtbl" select="TableName" />
        <xsl:with-param name="showFlds">4</xsl:with-param>
        <xsl:with-param name="jumpMenu">no</xsl:with-param>
     </xsl:call-template>

<!--9-->
     <xsl:call-template name="writeOneTbl_Lvl">
        <xsl:with-param name="currtbl" select="TableName" />
        <xsl:with-param name="showFlds">9</xsl:with-param>
        <xsl:with-param name="jumpMenu">yes</xsl:with-param>
     </xsl:call-template>
     <xsl:call-template name="writeOneTbl_Lvl">
        <xsl:with-param name="currtbl" select="TableName" />
        <xsl:with-param name="showFlds">9</xsl:with-param>
        <xsl:with-param name="jumpMenu">no</xsl:with-param>
     </xsl:call-template>


  </xsl:for-each>
</xsl:template>

<xsl:template name="writeOneTbl_Lvl">
<xsl:param name="currtbl"/>
<xsl:param name="showFlds"/>
<xsl:param name="jumpMenu" />

  <xsl:variable name="suffix">
    <xsl:if test="$jumpMenu!='yes'">_simple</xsl:if>
  </xsl:variable>
  <xsl:variable name="fileName">
     <xsl:value-of select="translate($currtbl,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')" /><xsl:if test="$currtbl!='index'">~<xsl:value-of select="$showFlds" /></xsl:if><xsl:value-of select="$suffix"/>.html</xsl:variable>
<!--@undo@--><redirect:write file="{$fileName}"><!--@undoEnd@--><xsl:comment><xsl:value-of select="$fileName" /></xsl:comment>
  <html>
    <head>
      <link rel="stylesheet" href="@stylesheet@" type="text/css"/>
      <style type="text/css">
          
          td.color9 {background-color: CC99CC}
          td.color1 {background-color: FF6666}
          td.color2 {background-color: FFCC99}
          td.color3 {background-color: FFFF99}
          td.color4 {background-color: 99FFCC}
          
        </style>
      <xsl:if test="$jumpMenu='yes'">
        <script type="text/javascript">
          <xsl:comment>
function MM_jumpMenu(targ,selObj,restore){ //v3.0
  eval(targ+".location='"+selObj.options[selObj.selectedIndex].value+"'");
  if (restore) selObj.selectedIndex=0;
}
//</xsl:comment>
        </script>
      </xsl:if>
    </head>
    <body>
    @vegbank_header_html_normal@

      <h2 class="VegBank">
        <a name="top"/> VegBranch Loading Module - tables
      </h2>
      <xsl:if test="$currtbl='index'">
        <h4>index</h4>
        <p class="sansserif">
Select a table to find out more about it below.  Once you have opened one of these links,
a table will show you the different fields for that table.  Several examples are given,
some comments on the examples are provided, and the database structure of each field is given,
including a definition.<br/>
          <xsl:choose>
            <xsl:when test="$jumpMenu='yes'">This page requires a javascript enabled browser.  If this is not possible for your browser, use the version <a href="index_simple.html">here</a>. </xsl:when>
            <xsl:otherwise>A version of this page is available <a href="index.html">here</a> that uses JavaScript, if your browser supports it. </xsl:otherwise>
          </xsl:choose>
        </p>
      </xsl:if>
      <p class="sansserif">
        <xsl:for-each select="../table">
          <a href="{translate(TableName,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')}~{$showFlds}{$suffix}.html">
            <xsl:value-of select="tableNickName"/>
          </a>
          <xsl:text disable-output-escaping="yes">&amp;nbsp;&amp;nbsp;&amp;nbsp;</xsl:text>
          <xsl:if test="position() mod 6=0">
            <br/>
          </xsl:if>
        </xsl:for-each>
      </p>
      <xsl:if test="$currtbl='index'">
  <xsl:call-template name="indexInfo" />

      </xsl:if>
      
      <xsl:if test="$currtbl!='index'">
        <form name="FieldsToShow">
          <p>Show Fields: 
<xsl:choose>
              <xsl:when test="$jumpMenu='yes'">
                <select name="pickFields" onChange="MM_jumpMenu('document',this,0)">
                  <option value="" selected="selected">--Select which fields to show--</option>
                  <option value="{translate($currtbl,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')}~1.html">
                    <xsl:value-of select="translate(translate(number($showFlds='1'),'1','*'),'0',' ')"/>Required Fields Only</option>
                  <option value="{translate($currtbl,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')}~2.html">
                    <xsl:value-of select="translate(translate(number($showFlds='2'),'1','*'),'0',' ')"/>add commonly used fields</option>
                  <option value="{translate($currtbl,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')}~3.html">
                    <xsl:value-of select="translate(translate(number($showFlds='3'),'1','*'),'0',' ')"/>add sometimes used fields</option>
                  <option value="{translate($currtbl,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')}~4.html">
                    <xsl:value-of select="translate(translate(number($showFlds='4'),'1','*'),'0',' ')"/>add advanced fields</option>
                  <option value="{translate($currtbl,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')}~9.html">
                    <xsl:value-of select="translate(translate(number($showFlds='9'),'1','*'),'0',' ')"/>all fields</option>
                </select>
  A* appears before the current setting of how many fields are shown in this picklist.
  </xsl:when>
              <xsl:otherwise><xsl:comment>current level: <xsl:value-of select="$showFlds" /></xsl:comment>
                <br/>
                <a href="{translate($currtbl,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')}~1{$suffix}.html">
                  <xsl:value-of select="translate(translate(number($showFlds=1),'1','*'),'0',' ')"/>Required Fields Only</a>
                <br/>
                <a href="{translate($currtbl,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')}~2{$suffix}.html">
                  <xsl:value-of select="translate(translate(number($showFlds=2),'1','*'),'0',' ')"/>add commonly used fields</a>
                <br/>
                <a href="{translate($currtbl,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')}~3{$suffix}.html">
                  <xsl:value-of select="translate(translate(number($showFlds=3),'1','*'),'0',' ')"/>add sometimes used fields</a>
                <br/>
                <a href="{translate($currtbl,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')}~4{$suffix}.html">
                  <xsl:value-of select="translate(translate(number($showFlds=4),'1','*'),'0',' ')"/>add advanced fields</a>
                <br/>
                <a href="{translate($currtbl,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')}~9{$suffix}.html">
                  <xsl:value-of select="translate(translate(number($showFlds=9),'1','*'),'0',' ')"/>all fields</a>
                <br/>
  A* appears before the current setting of how many fields are shown in this picklist.
  </xsl:otherwise>
            </xsl:choose>
            <br />
            Loading Module Information <a href="index{$suffix}.html">home.</a>
          </p>
        </form>


     <!--   <xsl:for-each select="table[TableName=$currtbl]"> -->
          <p>
            <b>
              <font size="+1">
                <xsl:value-of select="tableNickName"/>
              </font>
            </b>
            <br/>
            <b>Description: </b>
            <xsl:value-of select="TableDescription"/>
            <br/>
            <b>Am I required to use this table? </b>
            <xsl:value-of select="TableReqd"/>
            <br/>
            <b>Database table name: </b>
            <xsl:value-of select="TableName"/>
          </p>
          <table border="1" cellspacing="0" class="item">
            <xsl:variable name="strLen">25</xsl:variable>
            <tr>
              <th>FldNm:</th>
              <xsl:for-each select="field[fieldUseGroup&lt;=$showFlds and fieldUseGroup&gt;0]">
                <td class="{concat('color',fieldUseGroup)}">
                  <b>
                    <xsl:value-of select="FieldName"/>
                  </b>
                  <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
                </td>
              </xsl:for-each>
            </tr>
            <tr>
              <th>exmpl1:</th>
              <xsl:for-each select="field[fieldUseGroup&lt;=$showFlds and fieldUseGroup&gt;0]">
                <td class="{concat('color',fieldUseGroup)}">
                  <xsl:value-of select="example1"/>
                  <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
                </td>
              </xsl:for-each>
            </tr>
            <tr>
              <th>exmpl2:</th>
              <xsl:for-each select="field[fieldUseGroup&lt;=$showFlds and fieldUseGroup&gt;0]">
                <td class="{concat('color',fieldUseGroup)}">
                  <xsl:value-of select="example2"/>
                  <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
                </td>
              </xsl:for-each>
            </tr>
            <tr>
              <th>exmpl3:</th>
              <xsl:for-each select="field[fieldUseGroup&lt;=$showFlds and fieldUseGroup&gt;0]">
                <td class="{concat('color',fieldUseGroup)}">
                  <xsl:value-of select="example3"/>
                  <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
                </td>
              </xsl:for-each>
            </tr>
            <tr>
              <th>exmpl4:</th>
              <xsl:for-each select="field[fieldUseGroup&lt;=$showFlds and fieldUseGroup&gt;0]">
                <td class="{concat('color',fieldUseGroup)}">
                  <xsl:value-of select="example4"/>
                  <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
                </td>
              </xsl:for-each>
            </tr>
            <tr>
              <th>comment:</th>
              <xsl:for-each select="field[fieldUseGroup&lt;=$showFlds and fieldUseGroup&gt;0]">
                <td class="{concat('color',fieldUseGroup)}">
                  <xsl:choose>
                    <xsl:when test="string-length(exampleComments)&gt;0">
                      <textarea rows="3" readonly="readonly">
                        <xsl:value-of select="exampleComments"/>
                      </textarea>
                    </xsl:when>
                    <xsl:otherwise>
                      <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
                    </xsl:otherwise>
                  </xsl:choose>
                </td>
              </xsl:for-each>
            </tr>
            <tr>
              <th>
                <!--<xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>-->
              </th>
              <xsl:for-each select="field[fieldUseGroup&lt;=$showFlds and fieldUseGroup&gt;0]">
                <td>
                  <!-- <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>-->
                </td>
              </xsl:for-each>
            </tr>
            <tr>
              <th>defn:</th>
              <xsl:for-each select="field[fieldUseGroup&lt;=$showFlds and fieldUseGroup&gt;0]">
                <td class="{concat('color',fieldUseGroup)}">
                  <textarea rows="3" readonly="readonly">
                    <xsl:value-of select="FieldDefinition"/>
                  </textarea>
                  <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
                </td>
              </xsl:for-each>
            </tr>
            <tr>
              <th>type:</th>
              <xsl:for-each select="field[fieldUseGroup&lt;=$showFlds and fieldUseGroup&gt;0]">
                <td class="{concat('color',fieldUseGroup)}">
                  <xsl:value-of select="dataType"/>
                  <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
                </td>
              </xsl:for-each>
            </tr>
            <tr>
              <th>size:</th>
              <xsl:for-each select="field[fieldUseGroup&lt;=$showFlds and fieldUseGroup&gt;0]">
                <td class="{concat('color',fieldUseGroup)}">
                  <xsl:value-of select="FieldSize"/>
                  <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
                </td>
              </xsl:for-each>
            </tr>
            <tr>
              <th>nulls:</th>
              <xsl:for-each select="field[fieldUseGroup&lt;=$showFlds and fieldUseGroup&gt;0]">
                <td class="{concat('color',fieldUseGroup)}">
                  <xsl:value-of select="Nulls"/>
                  <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
                </td>
              </xsl:for-each>
            </tr>
            <tr>
              <th>key:</th>
              <xsl:for-each select="field[fieldUseGroup&lt;=$showFlds and fieldUseGroup&gt;0]">
                <td class="{concat('color',fieldUseGroup)}">
                  <xsl:value-of select="key"/>
                  <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
                </td>
              </xsl:for-each>
            </tr>
            <tr>
              <th>dest. field:</th>
              <xsl:for-each select="field[fieldUseGroup&lt;=$showFlds and fieldUseGroup&gt;0]">
                <td class="{concat('color',fieldUseGroup)}">
                  <xsl:choose>
                    <xsl:when test="contains(DestinationField,'.')">
                      <xsl:value-of select="substring-before(DestinationField,'.')"/>. <xsl:value-of select="substring-after(DestinationField,'.')"/>
                    </xsl:when>
                    <xsl:otherwise>
                      <xsl:value-of select="DestinationField"/>
                    </xsl:otherwise>
                  </xsl:choose>
                  <xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text>
                </td>
              </xsl:for-each>
            </tr>
          </table>
       <!--?? </xsl:for-each> -->
Color Key:
<table border="1" cellspacing="0">
        <tr>
          <td class="color1">req'd fld</td>
          <td class="color2">often used</td>
          <td class="color3">sometimes</td>
          <td class="color4">expert user</td>
          <td colspan="2" class="color9">internal field</td>
        </tr>
      </table>

      </xsl:if>
      <br/>

      <br/>
    @vegbank_footer_html_onerow@
      </body>
  </html>
<!--@undo@--></redirect:write><!--@undoEnd@-->
</xsl:template>

<xsl:template name="indexInfo">
<!-- link to simpler guide -->
<h4>A simpler guide to tables is also available:</h4>
<!-- begin paste from access generated files -->
<p>Navigation between loading tables:<br /> <a href='overview-a.html'>Party Information</a>  |  <a href='overview-b.html'>Species List</a>  |  <a href='overview-c.html'>Plot Data</a>  |  <a href='overview-c2.html'>Plot Normalized</a>  |  <a href='overview-d.html'>Presence Data</a>  |  <a href='overview-d2.html'>Strata Cover Data</a>  |  <a href='overview-e.html'>Stem Data</a>  |  <a href='overview-f.html'>Soil Data</a>  |  <a href='overview-g.html'>Disturbance Data</a>  |  <a href='overview-h.html'>Community Concept</a>  |  <a href='overview-i.html'>Plot Communities</a>  |  <a href='overview-j.html'>User Defined Metadata</a> </p>

<!-- end paste from VegBranch generated files -->
    <h4>How to read this guide to the Loading Tables of VegBranch:</h4>
      <p> <b><font size="+1">Example Table</font></b> [Loading table name]<br />
  <b>Description: </b>[description of this loading table] This table allows users to add information about ... and ...<br />
<b>Am I required to use this table? </b>Not required.<br />
  <b>Database table name: </b>[actual name of the table in the database] Z_USER_R_exampleNotReal</p>
<table class="item" cellspacing="0" border="1">
  <tr> 
    <th>FldNm:</th>
    <td class="color1"><b>field Name #1</b></td>
    <td class="color1"><b>plantCode</b> </td>
    <td class="color1"><b>plantName</b> </td>
    <td class="color3"><b>more headers...</b></td>
  </tr>
  <tr> 
    <th>exmpl1:</th>
    <td class="color1">example #1 data</td>
    <td class="color1">ACRU </td>
    <td class="color1">Acer rubrum L. </td>
    <td class="color3">...</td>
  </tr>
  <tr> 
    <th>exmpl2:</th>
    <td class="color1">example #2 data</td>
    <td class="color1">Acer barbatum </td>
    <td class="color1">Acer barbatum Michx. var. longii (Fern.) Fern. </td>
    <td class="color3">...</td>
  </tr>
  <tr> 
    <th>exmpl3:</th>
    <td class="color1">example #3 data</td>
    <td class="color1">LIRITUL </td>
    <td class="color1">Liriodendron tulipifera </td>
    <td class="color3">...</td>
  </tr>
  <tr> 
    <th>exmpl4:</th>
    <td class="color1">example #4 data</td>
    <td class="color1">sp12974 </td>
    <td class="color1">Arbutus menziesii </td>
    <td class="color3">...</td>
  </tr>
  <tr> 
    <th>comment:</th>
    <td class="color1"> 
      <textarea readonly="readonly" rows="3">Comments about this field and examples</textarea>
    </td>
    <td class="color1"> (sometimes there aren't comments)</td>
    <td class="color1"> 
      <textarea readonly="readonly" rows="3">VegBranch can look up different types of names.  USDA codes, scientific names, and scientific names without authors are best (in that descening order).</textarea>
    </td>
    <td class="color3">...</td>
  </tr>
  <tr> 
    <th></th>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
  </tr>
  <tr> 
    <th>defn:</th>
    <td class="color1"> 
      <textarea readonly="readonly" rows="3">Field #1 Definition</textarea>
       </td>
    <td class="color1"> 
      <textarea readonly="readonly" rows="3">A code that uniquely identifies a plant taxon in your other loading tables (Cover Data, Stratum Cover, Stem Data).  This can be any combination of letters, numbers, spaces, and symbols.  It can be a real code, such as a USDA code, or a temporary code, such as a number.</textarea>
       </td>
    <td class="color1"> 
      <textarea readonly="readonly" rows="3">The plant name for which VegBranch will look up the plant in your vegbank module.  If the plant name is not found, then the current record will be added as a new plant concept, based on the name in this field.</textarea>
       </td>
    <td class="color3"> 
      <textarea readonly="readonly" rows="3">...</textarea>
       </td>
  </tr>
  <tr> 
    <th>type:</th>
    <td class="color1">Field #1 data type</td>
    <td class="color1">Text </td>
    <td class="color1">Text </td>
    <td class="color3">...</td>
  </tr>
  <tr> 
    <th>size:</th>
    <td class="color1">Field #1 size </td>
    <td class="color1">255 </td>
    <td class="color1">255 </td>
    <td class="color3">...</td>
  </tr>
  <tr> 
    <th>nulls:</th>
    <td class="color1">are nulls allowed (no=required field)</td>
    <td class="color1">no </td>
    <td class="color1">no </td>
    <td class="color3">...</td>
  </tr>
  <tr> 
    <th>key:</th>
    <td class="color1">Primary Key (PK) or Foreign Key (FK) ?</td>
    <td class="color1">n/a </td>
    <td class="color1">n/a </td>
    <td class="color3">...</td>
  </tr>
  <tr> 
    <th>dest. field:</th>
    <td class="color1">Vegbank table name and field name<br/>
      that this field will be sent to in VegBranch.</td>
    <td class="color1">plantName. plantName </td>
    <td class="color1">plantName. plantName </td>
    <td class="color3">... </td>
  </tr>
</table>
<br/>
Color Key:
<table cellspacing="0" border="1">
 
  <tr> 
    <td class="color1">req'd fld</td>
    <td class="color2">often used</td>
    <td class="color3">sometimes</td>
    <td class="color4">expert user</td>
    <td class="color9" colspan="2">internal field</td>
  </tr> <tr> 
    <td class="color1">This field is required in the Loading Table.</td>
    <td class="color2">This field is often used in the loading table.</td>
    <td class="color3">This field is sometimes used in the loading table.</td>
    <td class="color4">This field should only be used by experienced VegBranch 
      users. </td>
    <td class="color9" colspan="2">This is an internal field and should not be 
      altered by users.</td>
  </tr>
</table>
</xsl:template>

</xsl:stylesheet>
