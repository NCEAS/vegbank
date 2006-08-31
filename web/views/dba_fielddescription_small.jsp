@stdvegbankget_jspdeclarations@
@webpage_head_html@
 
<!-- only good for one field! -->
<logic:present parameter="wparam">
  <bean:parameter id="wparamBean" name="wparam" value="one field" />
</logic:present>  

<title>VegBank Data Dictionary - <bean:write name="wparamBean" ignore="true" /></title>
<script type="text/javascript">

function getHelpPageId() {
  return "data-dictionary";
}
</script>

@webpage_masthead_small_html@
      <h2>
        <a href="@datadictionary-index@">VegBank Data Dictionary</a>
      </h2>

<logic:notPresent name="wparamBean">
  <p>Sorry, the request made to the data dictionary wasn't valid.  Please check that you clicked a link from VegBank and 
  <a href="@general_link@contact.html">contact us</a> if you followed a link from within VegBank.</p>
</logic:notPresent>
<logic:present name="wparamBean">
  <vegbank:get id="dba_fielddescription" select="dba_fielddescription" 
     beanName="map" pager="false" where="where_dd_tabledotfield" />
      <logic:notEmpty name="dba_fielddescription-BEANLIST">
        <table class="thinlines" cellpadding="2">
          <logic:iterate id="onerowofdba_fielddescription" name="dba_fielddescription-BEANLIST">
            <tr colspan="2" class="@nextcolorclass@ dba_tabledescription_tablename">
              <td class="datalabel">Table (DB Name)</td><td><a href="@get_link@detail/dba_tabledescription/<bean:write name='onerowofdba_fielddescription' property='tablename' />?where=where_tablename"><bean:write name="onerowofdba_fielddescription" property="tablename" /></a></td>
            </tr>
            <tr colspan="2" class="@nextcolorclass@ dba_tabledescription_tablelabel">
              <td class="datalabel">Table</td><td><a href="@get_link@detail/dba_tabledescription/<bean:write name='onerowofdba_fielddescription' property='tablename' />?where=where_tablename"><bean:write name="onerowofdba_fielddescription" property="tablename_transl" /></a></td>
            </tr>
            <%@ include file="autogen/dba_fielddescription_small_data.jsp" %>    
         
          </logic:iterate>
        </table>
    </logic:notEmpty>
    <logic:empty name="dba_fielddescription-BEANLIST">
      <p>Sorry, the Data Dictionary doesn't have any information about the field you requested. </p>
    </logic:empty>
</logic:present>
           
@webpage_footer_small_html@
