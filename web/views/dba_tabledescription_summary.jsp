@webpage_top_html@
 @stdvegbankget_jspdeclarations@
 @webpage_head_html@
 <TITLE>VegBank Data Dictionary: Tables</TITLE>
        <%@ include file="includeviews/inlinestyles.jsp" %>
   <!-- include highlighting options -->
   <%@ include file="includeviews/hop_highlightSearchCriteria.jsp" %>

@webpage_masthead_html@ 

 <h2><a href="@datadictionary-index@">VegBank Data Dictionary</a>: Tables</h2>
  <!-- add search box -->
  <table class="noborders"><td>
  <bean:define id="entityToSearch" value="dba_tabledescription" />
   <bean:define id="NameOfEntityToPresent" value="VegBank Tables" />
   <bean:define id="SearchInstructions" value="(enter a word, field name, etc.)" /> 
   <bean:define id="alternateSearchInputs">
     <input type="hidden" name="xwhereKey" value="xwhere_match" />
     <input type="hidden" name="where" value="where_simple" />
     <input type="hidden" name="xwhereParams_custom_1" value="tablekeywords" /><!-- name of field to search -->
   </bean:define>
   <%@ include file="includeviews/sub_searchEntity.jsp" %>
   </td><td>&nbsp;&nbsp;
  <a href="@get_link@std/dba_fielddescription">Search VegBank Fields Here</a></td></tr></table>
  
<vegbank:get id="dba_tabledescription" select="dba_tabledescription" beanName="map" pager="true" xwhereEnable="true"/>

<vegbank:pager />

<logic:empty name="dba_tabledescription-BEANLIST">
  <p>  Sorry, no Table Descriptions found for the requested criteria.</p>
</logic:empty>
<logic:notEmpty name="dba_tabledescription-BEANLIST">
<p>
<a href="javascript:void showDataDictionaryField(true);">Show table/field labels </a> | 
<a href="javascript:void showDataDictionaryField(false);">Show database names</a>
</p>

<table class="leftrightborders" cellpadding="2">
<tr>
   <%@ include file="autogen/dba_tabledescription_summary_head.jsp" %>
  <th>Fields (Columns)</th>
</tr>
<logic:iterate id="onerowofdba_tabledescription" name="dba_tabledescription-BEANLIST">
<tr class="@nextcolorclass@">
  <%@ include file="autogen/dba_tabledescription_summary_data.jsp" %>

  <bean:define id="dba_tabledescription_pk" name="onerowofdba_tabledescription" property="dba_tabledescription_id" />
  <bean:define id="dba_tablename" name="onerowofdba_tabledescription" property="tablename" />

  <vegbank:get id="dba_fielddescription" select="dba_fielddescription" beanName="map" 
    pager="false" perPage="-1" where="where_tablename" wparam="dba_tablename" />
  <td class="largefield">
    <logic:empty name="dba_fielddescription-BEANLIST">
       --No fields--  
    </logic:empty>
    <logic:notEmpty name="dba_fielddescription-BEANLIST">
      <logic:iterate id="onerowofdba_fielddescription" name="dba_fielddescription-BEANLIST">
        <bean:define id="field_pk" name="onerowofdba_fielddescription" property="dba_fielddescription_id" />
        <a href="@get_link@detail/dba_fielddescription/<bean:write name='field_pk' />"><span class="dba_fielddescription_fieldname"><bean:write name="onerowofdba_fielddescription" property="fieldname" /></span><span class="dba_fielddescription_fieldlabel"><bean:write name="onerowofdba_fielddescription" property="fieldlabel" /></span></a>
        &nbsp;&nbsp;
      </logic:iterate>

    </logic:notEmpty>
  </td>
</tr>

</logic:iterate>
</table>
</logic:notEmpty>
<br />
<vegbank:pager />
     
          @webpage_footer_html@
