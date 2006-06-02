
<!--  @stdvegbankget_jspdeclarations@ -->
  
<vegbank:get id="newkey" select="newrequestacckey" beanName="map" pager="false"/>
<preassignedAccessionCodePackage>
   <!-- START COMMENT:  -->
        <logic:empty name="newkey-BEANLIST">
           <!--  failed! -->
        </logic:empty>
        
        <logic:notEmpty name="newkey-BEAN">
       <!--   <bean:write name="newkey-BEAN" property="newrequestid" />
           is YOUR new ID.   -->
           
           <bean:parameter id="thisRecordCount" name="recordcount" value="1" />
           <bean:parameter id="thisTableName" name="tablename" value="1" />
           
        <!--   Now moving on to placing your order:-->
         
           <bean:define id="thisRequestKey" name="newkey-BEAN" property="newrequestid" />
           
           
           <vegbank:requestaccessioncode table="thisTableName" requestkey="thisRequestKey" 
              recordcount="thisRecordCount" />
              
           <!--   Getting your newly formed accession codes -->
              
              <vegbank:get id="newacckeys" select="newacckeys" beanName="map" pager="false" perPage="-1"
                 where="where_requestkey" wparam="thisRequestKey" />
              <logic:notEmpty name="newacckeys-BEANLIST">
               <logic:iterate id="onerowofacckeys" name="newacckeys-BEANLIST">
                 <pre_accessioncode>
                   <accessioncode><bean:write name="onerowofacckeys" property="accessioncode" /></accessioncode></pre_accessioncode>
               </logic:iterate>
              </logic:notEmpty>
        </logic:notEmpty>

     <!-- END COMMENT  -->   

</preassignedAccessionCodePackage>
