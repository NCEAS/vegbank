@webpage_top_html@
 @stdvegbankget_jspdeclarations@
 @webpage_head_html@
 <TITLE>VegBank Data Dictionary: Field Details</TITLE>
 <!--%@ include file="includeviews/inlinestyles.jsp" %-->
   <!-- include highlighting options -->
   <%@ include file="includeviews/hop_highlightSearchCriteria.jsp" %>

@webpage_masthead_html@ 

        <h2><a href="@datadictionary-index@">VegBank Data Dictionary</a>: Field Details</h2>
  <table class="noborders"><td>
  <!-- add search box -->
  <bean:define id="entityToSearch" value="dba_fielddescription" />
  <bean:define id="NameOfEntityToPresent" value="VegBank Field" />
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
<!--<p>
<a href="javascript:void showDataDictionaryField(true);"> show labels </a> | 
<a href="javascript:void showDataDictionaryField(false);"> show db fields </a>
</p>-->
  <logic:iterate id="onerowofdba_fielddescription" name="dba_fielddescription-BEANLIST">
    <!-- iterate over all records in set : new table for each -->
    <table class="thinlines" cellpadding="2">
      <tr colspan="2" class="@nextcolorclass@ dba_tabledescription_tablename">
        <td class="datalabel">Table (DB Name)</td><td><a href="@get_link@detail/dba_tabledescription/<bean:write name='onerowofdba_fielddescription' property='tablename' />?where=where_tablename"><bean:write name="onerowofdba_fielddescription" property="tablename" /></a></td>
      </tr>
      <tr colspan="2" class="@nextcolorclass@ dba_tabledescription_tablelabel">
        <td class="datalabel">Table</td><td><a href="@get_link@detail/dba_tabledescription/<bean:write name='onerowofdba_fielddescription' property='tablename' />?where=where_tablename"><bean:write name="onerowofdba_fielddescription" property="tablename_transl" /></a></td>
      </tr>

      <%@ include file="autogen/dba_fielddescription_detail_data.jsp" %>
      <bean:define id="tblDotFld"><bean:write name="onerowofdba_fielddescription" property="tablename" />.<bean:write name="onerowofdba_fielddescription" property="fieldname" /></bean:define>
      <!-- get list -->
      <vegbank:get id="dba_fieldlist" select="dba_fieldlist" beanName="map" pager="false" where="where_dd_tabledotfield"  orderBy="orderby_listvaluesortorder" allowOrderBy="true"  
         wparam="tblDotFld" perPage="-1" />
      <!-- ignore empties! -->
      <logic:notEmpty name="dba_fieldlist-BEANLIST">
        <TR><TD COLSPAN="2">
          <table class="leftrightborders" cellpadding="2">
            <tr><th colspan="9"><bean:write name="onerowofdba_fielddescription" property="fieldlist" /> List of Values:</th></tr>
            <tr>
              <%@ include file="autogen/dba_fieldlist_summary_head.jsp" %>
            </tr>
            <logic:iterate id="onerowofdba_fieldlist" name="dba_fieldlist-BEANLIST">
              <tr class="@nextcolorclass@">
                <%@ include file="autogen/dba_fieldlist_summary_data.jsp" %>
              </tr>
            </logic:iterate>
          </table>
        </TD></TR>
        </logic:notEmpty>
    </table>
    <p>&nbsp;</p>
  </logic:iterate>
</logic:notEmpty>
<br />
<vegbank:pager />
     
          @webpage_footer_html@
