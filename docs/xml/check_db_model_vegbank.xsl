<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:param name="errCheckType">1</xsl:param>
  <!-- 1 for normal stuff, 2 for spelling list -->
  <xsl:param name="alphalow">abcdefghijklmnopqrstuvwxyz</xsl:param>
  <xsl:param name="alphahigh">ABCDEFGHIJKLMNOPQRSTUVWXYZ</xsl:param>
  <xsl:output method="html"/>
  <xsl:template match="/ | dataModel | modelChangeDocument | modelChange | entity | change  ">
    <xsl:apply-templates/>
  </xsl:template>
  <xsl:template match="attribute">
    <xsl:choose>
      <xsl:when test="$errCheckType='1'">
        <xsl:call-template name="checkPKName"/>
        <xsl:call-template name="checkFK"/>
        <xsl:call-template name="checkName"/>
        <xsl:call-template name="checkList"/>
        <xsl:call-template name="checkVersion" />
        <xsl:call-template name="checkUnits" />
      </xsl:when>
      <xsl:otherwise>
        <!-- generate spellCheck document to sort and spell check -->
        <xsl:call-template name="checkSpelling"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  <xsl:template name="checkPKName">
    <xsl:if test="attKey='PK'">
      <xsl:if test="translate(concat(../entityName,'_ID'),$alphalow,$alphahigh)!=translate(attName,$alphalow,$alphahigh)">
10<xsl:value-of select="../entityName"/>.<xsl:value-of select="attName"/> doesn't match tableName (<xsl:value-of select="../entityName"/> )</xsl:if>
      <xsl:if test="attRelType/@type!='n/a' and attRelType/@type!='root' and attRelType/@type!='allfields' and attRelType/@type!='alltables'">
12<xsl:value-of select="../entityName"/>.<xsl:value-of select="attName"/> is PK, but invalid attRelType (<xsl:value-of select="attRelType/@type"/>)</xsl:if>
      <xsl:if test="attType!='serial'">
13<xsl:value-of select="../entityName"/>.<xsl:value-of select="attName"/> is PK, but type is not serial (<xsl:value-of select="attType"/>)</xsl:if>
    </xsl:if>
    <!-- is PK -->
    <xsl:if test="attRelType/@type='root' or attRelType/@type='allfields' or attRelType/@type='alltables'">
      <xsl:if test="attKey!='PK'">
14<xsl:value-of select="../entityName"/>.<xsl:value-of select="attName"/> should be PK from attRelType (<xsl:value-of select="attRelType/@type"/>) but isn't (<xsl:value-of select="attKey"/>)</xsl:if>
    </xsl:if>
    <xsl:if test="(attKey='n/a' and attRelType/@type!='n/a')">
16<xsl:value-of select="../entityName"/>.<xsl:value-of select="attName"/> is not a key, yet attRelType (<xsl:value-of select="attRelType/@type"/>) has special value, should be n/a</xsl:if>
  </xsl:template>
  <xsl:template name="checkFK">
    <xsl:if test="attKey='FK'">
      <xsl:if test="attReferences='n/a'">
20<xsl:value-of select="../entityName"/>.<xsl:value-of select="attName"/> is an FK, but has nothing it is referencing!</xsl:if>
      <xsl:if test="attRelType/@type!='inverted' and attRelType/@type!='normal'">
25<xsl:value-of select="../entityName"/>.<xsl:value-of select="attName"/> is an FK, but has attRelType not equal to normal or inverted : <xsl:value-of select="attRelType/@type"/>
      </xsl:if>
    </xsl:if>
    <!-- is FK -->
    <xsl:if test="attRelType/@type='inverted' or attRelType/@type='normal'">
      <xsl:if test="attKey!='FK'">
26<xsl:value-of select="../entityName"/>.<xsl:value-of select="attName"/> has attRelType=(<xsl:value-of select="attRelType/@type"/>) but isn't FK (<xsl:value-of select="attKey"/>)</xsl:if>
    </xsl:if>
    <xsl:if test="attReferences!='n/a'">
      <xsl:if test="attKey!='FK'">
30<xsl:value-of select="../entityName"/>.<xsl:value-of select="attName"/> has reference (<xsl:value-of select="attReferences"/>) but isn't an FK</xsl:if>
      <!-- check to see if reference means something -->
      <xsl:variable name="refExist">
        <xsl:call-template name="checkExist">
          <xsl:with-param name="tbl" select="substring-before(attReferences,'.')"/>
          <xsl:with-param name="fld" select="substring-after(attReferences,'.')"/>
        </xsl:call-template>
      </xsl:variable>
      <xsl:if test="$refExist!=1">
40<xsl:value-of select="../entityName"/>.<xsl:value-of select="attName"/> has reference (<xsl:value-of select="attReferences"/>) that doesn't point anywhere (or mult)  <xsl:value-of select="$refExist"/>!</xsl:if>
    </xsl:if>
  </xsl:template>
  <xsl:template name="checkName">
    <xsl:if test="attKey='FK' or attKey='PK'">
      <!-- name should end in _ID, caps -->
      <xsl:if test="substring(attName,string-length(attName)-2,3)!='_ID'">
45<xsl:value-of select="../entityName"/>.<xsl:value-of select="attName"/> is PK/FK but doesn't end in _ID.</xsl:if>
    </xsl:if>
    <xsl:if test="translate(attName,$alphalow,$alphahigh)!=attName">
      <!-- field name not all caps -->
      <xsl:if test="translate(substring(attName,1,1),$alphahigh,$alphalow)!=substring(attName,1,1)">
50 <xsl:value-of select="../entityName"/>.<xsl:value-of select="attName"/> is not all capitalized, but starts with a capital letter! against common rules.</xsl:if>
    </xsl:if>
    <!-- check if name is reserved -->
   <xsl:variable name="currAttNameReserved"><xsl:call-template name="checkSQLreserved"><xsl:with-param name="word" select="attName" /></xsl:call-template></xsl:variable>
    <xsl:if test="string-length($currAttNameReserved)&gt;0">
51 <xsl:value-of select="../entityName"/>.<xsl:value-of select="attName"/> has att name as reserved SQL keyword, consider renaming! details : <xsl:value-of select="$currAttNameReserved" />   </xsl:if>
    <xsl:variable name="currEntNameReserved"><xsl:call-template name="checkSQLreserved"><xsl:with-param name="word" select="../entityName" /></xsl:call-template></xsl:variable>
    <xsl:if test="string-length($currEntNameReserved)&gt;0">
52 <xsl:value-of select="../entityName"/>.<xsl:value-of select="attName"/> has entity name as reserved SQL keyword, consider renaming! details : <xsl:value-of select="$currEntNameReserved" />   </xsl:if>

    
  </xsl:template>
  <xsl:template name="checkList">
    <xsl:if test="attListType!='no'">
      <!-- there is a list, check to make sure that there really is -->
      <xsl:if test="count(attList/attListItem)&lt;2">
55<xsl:value-of select="../entityName"/>.<xsl:value-of select="attName"/> has a list type (<xsl:value-of select="attListType"/>), but list isn't much, only <xsl:value-of select="count(attList/attListItem)"/> items.</xsl:if>
    </xsl:if>
    <xsl:if test="count(attList/attListItem)&gt;0">
      <!-- a list has values -->
      <xsl:if test="attListType='no'">
65 <xsl:value-of select="../entityName"/>.<xsl:value-of select="attName"/> has a list type (<xsl:value-of select="attListType"/>), but has a list anyway: <xsl:value-of select="count(attList/attListItem)"/> items.</xsl:if>
      <!-- check that the list values do not exceed length of list -->
      <xsl:if test="contains(attType,'varchar')">
        <xsl:variable name="maxLeng" select="substring-before(substring-after(attType,'('),')')"/>
        <xsl:variable name="currField" select="concat(../entityName,'.',attName)"/>
        <xsl:for-each select="attList/attListItem">
          <xsl:if test="string-length(attListValue)&gt;$maxLeng">
77 <xsl:value-of select="$currField"/> has value: <xsl:value-of select="attListValue"/> which has lentgh (<xsl:value-of select="string-length(attListValue)"/>) exceeding datatype for field: varchar (<xsl:value-of select="$maxLeng"/>)</xsl:if>
        </xsl:for-each>
      </xsl:if>
      <!-- is varchar -->
    </xsl:if>
    <!-- list items exist -->
  </xsl:template>
  <xsl:template name="checkUnits">
    <xsl:if test="attType='Integer' or attType='Float'">
      <xsl:if test="attKey!='FK' and attKey!='PK' and attModel='logical' and attName!='tableRecord'">
        <xsl:if test="string-length(attUnits)=0">
78 No units for numerical non key: <xsl:value-of select="../entityName"/>.<xsl:value-of select="attName"/> type ('Float' rarely OK):  <xsl:value-of select="attType" />
        </xsl:if>
      </xsl:if>
    </xsl:if>
  </xsl:template>
  <xsl:template name="checkVersion">
    <xsl:if test="string-length(../../../modelChangeVersion)&gt;0">
      <xsl:if test="../../../modelChangeVersion!=update and update!='0.0.0'">
     <xsl:if test="not(contains(../../@type,'drop'))"><!-- ignore if a drop -->
79 <xsl:value-of select="../entityName"/>.<xsl:value-of select="attName"/>  : version (<xsl:value-of select="../../../modelChangeVersion" />) and update (<xsl:value-of select="update" />)do not match! </xsl:if>
    </xsl:if></xsl:if>
  </xsl:template>
  <xsl:template name="checkExist">
    <xsl:param name="tbl"/>
    <xsl:param name="fld"/>
    <xsl:value-of select="count(//entity[entityName=$tbl]/attribute[attName=$fld])"/>
  </xsl:template>
  <xsl:template name="checkSpelling">
    <!-- if all caps, then assume ok, what else can I do? -->
    <xsl:if test="translate(attName,$alphalow,$alphahigh)!=attName">
      <xsl:call-template name="checkSpellOneWord">
        <xsl:with-param name="word" select="attName"/>
      </xsl:call-template>
      <xsl:call-template name="checkSpellOneWord">
        <xsl:with-param name="word" select="../entityName"/>
      </xsl:call-template>
    </xsl:if>
  </xsl:template>
  <xsl:template name="checkSpellOneWord">
    <xsl:param name="word"/>
    <xsl:if test="string-length($word)&gt;0">
      <xsl:variable name="tempword" select="concat(translate(substring($word,1,1),$alphahigh,$alphalow),substring($word,2,string-length($word)-1))"/>
      <xsl:comment>tempword:<xsl:value-of select="$tempword"/>
      </xsl:comment>
      <xsl:variable name="tempword2" select="translate($tempword,concat($alphahigh,'_'),';;;;;;;;;;;;;;;;;;;;;;;;;;;')"/>
      <xsl:comment>tempword2:<xsl:value-of select="$tempword2"/>
      </xsl:comment>
      <!-- take what is before the ; -->
      <xsl:variable name="toWrite" select="substring-before(concat($tempword2,';'),';')"/>
      <xsl:comment>to write:<xsl:value-of select="$toWrite"/>
      </xsl:comment>
      <xsl:variable name="remains">
        <xsl:choose>
          <xsl:when test="string-length($toWrite)!=string-length($word)"><xsl:call-template name="cleanWord">
            <xsl:with-param name="word" select="substring($word,string-length($toWrite)+1,string-length($word)-string-length($toWrite))"/>
          </xsl:call-template></xsl:when>
          <xsl:otherwise></xsl:otherwise>
        </xsl:choose>
      </xsl:variable>  
      <xsl:comment>remains:<xsl:value-of select="$remains"/>
      </xsl:comment>
      <xsl:value-of select="$toWrite"/>
      <br/>
      <xsl:call-template name="checkSpellOneWord"><xsl:with-param name="word" select="$remains" /></xsl:call-template> 
<!--      <xsl:if test="$remains=$word">@culprit@:<xsl:value-of select="$remains"/>=word</xsl:if>-->
    </xsl:if>
  </xsl:template>
  <xsl:template name="cleanWord">
    <xsl:param name="word"/>
    <xsl:choose>
      <xsl:when test="$word='_ID'"></xsl:when>
      <xsl:when test="substring($word,1,1)='_' and $word!='_ID'">
        <xsl:if test="string-length($word)&gt;1">
          <xsl:call-template name="cleanWord">
            <xsl:with-param name="word" select="substring($word,2,string-length($word)-1)"/>
          </xsl:call-template>
        </xsl:if>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="$word"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  <xsl:template match="*"/>
  <xsl:template name="checkSQLreserved"><!-- returns statements about reservation if found to be reserved -->
    <xsl:param name="word" />
    <!-- compare upper case of word to list of words reserved, and print if found =-->
    <xsl:for-each  select="document('sql_reserved_words.xml')/table/tr[word=translate($word,$alphalow,$alphahigh)]">the name: <xsl:value-of select="$word" /> is reserved for SQL definitions : <xsl:value-of select="concat('pg: ',pg_reserved,' | sql99: ',sql99,' | sql92 :',sql92)" /> </xsl:for-each>
  </xsl:template>
</xsl:stylesheet>
