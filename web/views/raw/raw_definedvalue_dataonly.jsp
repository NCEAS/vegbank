 @stdvegbankget_jspdeclarations@
 <!-- you have to pass to this file a wparam that has these, IN ORDER:
    tableName, PK value(s), UserDefined_ID(s) separated by Utility.PARAM_DELIM 
   OR, pass your own where, such as where_definedvalue_obstaxonimportance_udlist
  -->
<logic:present parameter="wparam">
  <logic:notPresent parameter="where">
    <!-- default where -->
    <bean:define id="where" value="where_definedvalue_table_recs_udlist" />
  </logic:notPresent>
<vegbank:get id="definedvalue" select="definedvalue" beanName="map" 
  pager="false" perPage="-1" />
</logic:present>

<logic:empty name="definedvalue-BEANLIST">
<!-- @!NO_USER_DEFINED_VALUES!@ this token signals the calling page that there are none -->
</logic:empty>
<logic:notEmpty name="definedvalue-BEANLIST">


<logic:iterate id="onerowofdefinedvalue" name="definedvalue-BEANLIST">
<!-- write this div as contents of defined value, but display elsewhere -->
<div id="defval_<bean:write name='onerowofdefinedvalue' property='lowertablename' />_rec<bean:write name='onerowofdefinedvalue' property='tablerecord_id' />_ud<bean:write name='onerowofdefinedvalue' property='userdefined_id' />" class="hidden">
  <bean:write name='onerowofdefinedvalue' property='definedvalue' />    
</div>   

</logic:iterate>

</logic:notEmpty>
