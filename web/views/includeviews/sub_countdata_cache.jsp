<vegbank:get id="dbcountdata" select="dba_datacache_dbstats" beanName="map"
    pager="false" allowOrderBy="false" perPage="-1" />



 <logic:empty name="dbcountdata-BEANLIST">
    <p>Database error.  No data found.</p>
    </logic:empty>
<logic:notEmpty name="dbcountdata-BEANLIST">
<table class="leftrightborders" cellpadding="1">
<logic:iterate id="onerowofdbcountdata" name="dbcountdata-BEANLIST">
<tr class="sizetiny @nextcolorclass@">
  <td><bean:write name="onerowofdbcountdata" property="cache_label"/></td>
  <td class="numeric" >&nbsp;<bean:write name="onerowofdbcountdata" property="data1form"/></td>
</tr>
</logic:iterate>
</table>
</logic:notEmpty>