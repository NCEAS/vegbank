@webpage_top_html@
 @stdvegbankget_jspdeclarations@
 @webpage_head_html@
 <TITLE>VegBank Data Dictionary - Search Fields</TITLE>
   <%@ include file="includeviews/inlinestyles.jsp" %>
   @webpage_masthead_html@ 


  <h2>VegBank Data Dictionary - Search Fields</h2>
  <table class="noborders"><td>
  <!-- add search box -->
  <bean:define id="entityToSearch" value="dba_fielddescription" />
  <bean:define id="NameOfEntityToPresent" value="VegBank Fields" />
  <bean:define id="SearchInstructions" value="(enter a word, field name, etc.)" /> 
  <bean:define id="alternateSearchInputs">
    <input type="hidden" name="xwhereKey" value="xwhere_match" />
    <input type="hidden" name="where" value="where_simple" />
    <input type="hidden" name="xwhereParams_custom_1" value="fieldkeywords" /><!-- name of field to search -->
  </bean:define>
  <%@ include file="includeviews/sub_searchEntity.jsp" %>
</td><td>&nbsp;&nbsp;
  <a href="@get_link@std/dba_tabledescription">Search VegBank Tables Here</a>
</td></tr></table>
<vegbank:get id="dba_fielddescription" select="dba_fielddescription" beanName="map" pager="true" xwhereEnable="true"/>

<vegbank:pager />

<logic:empty name="dba_fielddescription-BEANLIST">
  <p>  Sorry, no fields found.</p>
</logic:empty>

<logic:notEmpty name="dba_fielddescription-BEANLIST">
<p>
<a href="javascript:void showDataDictionaryField(true);"> show labels </a> | 
<a href="javascript:void showDataDictionaryField(false);"> show db fields </a>
</p>
  <table class="leftrightborders" cellpadding="2">
    <tr>
      <!-- if multiple tables, add table name -->
      <th>Table</th>
      <%@ include file="autogen/dba_fielddescription_summary_head.jsp" %>
    </tr>
    <logic:iterate id="onerowofdba_fielddescription" name="dba_fielddescription-BEANLIST">
      <tr class="@nextcolorclass@">
        <td title="<bean:write name='onerowofdba_fielddescription' property='tablename' />">
        <a href="@get_link@detail/dba_tabledescription/<bean:write name='onerowofdba_fielddescription' property='tablename' />?where=where_tablename"><bean:write name="onerowofdba_fielddescription" property="tablename_transl" /></a></td>
        <%@ include file="autogen/dba_fielddescription_summary_data.jsp" %>
      </tr>
    </logic:iterate>
  </table>
</logic:notEmpty>
<br />
<vegbank:pager />
     
  @webpage_footer_html@