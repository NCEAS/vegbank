
 @stdvegbankget_jspdeclarations@
      
<logic:present parameter="wparam">
<vegbank:get id="definedvalue" select="definedvalue" beanName="map" 
  pager="false" perPage="-1" where="where_definedvalue_table_recs" />
</logic:present>

<logic:empty name="definedvalue-BEANLIST">
<!-- @!NO_USER_DEFINED_VALUES!@ this token signals the calling page that there are none-->
</logic:empty>
<logic:notEmpty name="definedvalue-BEANLIST">


<logic:iterate id="onerowofdefinedvalue" name="definedvalue-BEANLIST">
   
  <tr class="@nextcolorclass@">
    <td class="datalabel userdefined">
 <a href="@get_link@std/userdefined/<bean:write name='onerowofdefinedvalue' property='userdefined_id' />">
  <bean:write name="onerowofdefinedvalue" property="userdefinedname" />
 </a></td>
    <td class="largefield"><bean:write name="onerowofdefinedvalue" property="definedvalue" /></td>
  </tr>
</logic:iterate>

</logic:notEmpty>


       
