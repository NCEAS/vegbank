<!-- just gets details about the current web usr -->

 <bean:define id="beanWebUserEmail" value="" type="java.lang.String"/><!-- sets to default of "" --> 
  <vegbank:get id="webusr" select="usr_email" where="where_usrpk" wparam="<%= strWebUserId %>" /> 
  <logic:notEmpty name="webusr-BEANLIST">
  <logic:iterate id="onerowofwebusr" name="webusr-BEANLIST">
      <!-- gets user's email address -->
      <bean:define type="java.lang.String" id="beanWebUserEmail" name="onerowofwebusr" property="email_address" />    
  </logic:iterate>
  </logic:notEmpty>