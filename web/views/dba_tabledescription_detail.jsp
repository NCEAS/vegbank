@webpage_top_html@
 @stdvegbankget_jspdeclarations@
 @webpage_head_html@
 <TITLE>VegBank Data Dictionary</TITLE>
        <%@ include file="includeviews/inlinestyles.jsp" %>
@webpage_masthead_html@ 

 <h2><a href="@datadictionary-index@">VegBank Data Dictionary</a>: Tables</h2>
  <!-- add search box -->
 <table class="noborders"><td>
 <bean:define id="entityToSearch" value="dba_tabledescription" />
  <bean:define id="NameOfEntityToPresent" value="VegBank Table" />
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
<a href="javascript:void showDataDictionaryField(true);">Show field labels </a> | 
<a href="javascript:void showDataDictionaryField(false);">Show database names</a>
</p>

  <logic:iterate id="onerowofdba_tabledescription" name="dba_tabledescription-BEANLIST">
    <!-- iterate over all records in set : new table for each -->
    <table class="leftrightborders" cellpadding="2">
         <%@ include file="autogen/dba_tabledescription_detail_data.jsp" %>
        <bean:define id="dba_tabledescription_pk" name="onerowofdba_tabledescription" property="dba_tabledescription_id" />
        <bean:define id="dba_tablename" name="onerowofdba_tabledescription" property="tablename" />
        
        <!-- check to see if only a particular field was requested: -->
        <logic:present parameter="fieldname">
          <!-- get the paramter -->
          <bean:parameter id="fieldNameParamBean" name="fieldname" value="unknown" />
          <bean:define id="tableDotFieldBean"><bean:write name="dba_tablename" />.<bean:write name="fieldNameParamBean" /></bean:define>
          <vegbank:get id="dba_fielddescription" select="dba_fielddescription" beanName="map" 
                    pager="false" perPage="-1" where="where_dd_tabledotfield" wparam="tableDotFieldBean" />
                    <!-- add notification to user that only one is shown -->
          <TR><TD colspan="2">
            <a href="@get_link@detail/dba_tabledescription/<bean:write name='dba_tablename' />?where=where_tablename">Show all fields</a> on this table.
          </TD></TR>
        </logic:present>
        <logic:notPresent parameter="fieldname">
          <!-- get all fields on this here table! -->
          <vegbank:get id="dba_fielddescription" select="dba_fielddescription" beanName="map" 
                    pager="false" perPage="-1" where="where_tablename" wparam="dba_tablename" />
        </logic:notPresent>
          <TR><TD colspan="2">
            <table class="leftrightborders" cellpadding="2">
              <logic:empty name="dba_fielddescription-BEANLIST">
                <tr><td> --No fields--  </td></tr>
              </logic:empty>
              <logic:notEmpty name="dba_fielddescription-BEANLIST">
                 <!-- insert field header -->
                 <tr>
                  <!-- <th>More</th> -->
                   <%@ include file="autogen/dba_fielddescription_summary_head.jsp" %>
                 </tr>
                <logic:iterate id="onerowofdba_fielddescription" name="dba_fielddescription-BEANLIST">
                  <bean:define id="field_pk" name="onerowofdba_fielddescription" property="dba_fielddescription_id" />
                  <!-- perhaps flag row as req'd -->
                  <tr class="@nextcolorclass@ <logic:equal name='onerowofdba_fielddescription' property='fieldnulls' value='no'>requiredfield</logic:equal>">
                   <!-- <td><a href="@get_link@detail/dba_fielddescription/<bean:write name='field_pk' />">Info</a></td> -->
                    <%@ include file="autogen/dba_fielddescription_summary_data.jsp" %>
                  </tr>
                </logic:iterate>

              </logic:notEmpty>
            </table>
          </TD></TR>  

    </table>
    <p>&nbsp;</p>
  </logic:iterate>
</logic:notEmpty>

<br />
<vegbank:pager />
     
          @webpage_footer_html@
