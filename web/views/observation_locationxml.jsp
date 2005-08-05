<?xml version="1.0" encoding="UTF-8"?>
<rss version="2.0" xmlns:geo="http://www.w3.org/2003/01/geo/wgs84_pos#" xmlns:ymaps="http://api.maps.yahoo.com/Maps/V1/AnnotatedMaps.xsd">
<!-- @stdvegbankget_jspdeclarations@  -->
  <channel>
    <title>A Map of VegBank Plots</title>
    <link><![CDATA[@machine_url@/]]></link>
    <description>This map shows you the plots you requested on VegBank.  A file is generated in VegBank, then processed by an external mapping program, such as that found on Yahoo.com.  Maps will vary depending on the mapping program used.</description>
    <image><url>@machine_url@/vegbank/images/vegbank_fulllogo.jpg</url><title>VegBank</title><link>@machine_url@/</link></image>
          <!-- create groups for each dataset listed -->
          <% int mapNum = 0; %>    
    <ymaps:Groups>
        <logic:equal parameter="where" value="where_userdataset_pk">
         

          <vegbank:get id="datasets" select="userdataset" beanName="map"
            pager="false" perPage="-1" allowOrderBy="true" orderBy="xorderby_datasetname" />
            <logic:notEmpty name="datasets-BEANLIST">
              <logic:iterate id="onerowofdatasets" name="datasets-BEANLIST">
                 <!-- define groups: -->
                 <group>
                   <Title><bean:write name="onerowofdatasets" property="datasetname" /></Title>
                   <Id>vb_<bean:write name="onerowofdatasets" property="userdataset_id" /></Id>
     <!--<![CDATA[]]>--><ymaps:BaseIcon>@machine_url@/vegbank/images/map_<%= mapNum %>.gif</ymaps:BaseIcon>
                   <% mapNum++;
                    if (mapNum == 10 ) {
                      mapNum = 0;  //reset mapNum to 0, as there are only 0-9 for images.
                      %> <!-- resetting map numbers to 0 -->
                      <%
                    }
                   %>
                </group>
              </logic:iterate>

            </logic:notEmpty>  

         <!-- special get for these: -->
          <vegbank:get id="plotobs" select="plotobs_ds_mapping" beanName="map" 
               pager="false" perPage="-1" allowOrderBy="true" orderBy="orderby_ds_plotcode" />
          <bean:define id="groupsdefined" value="true" /><!-- dont replace this beanlist -->
        </logic:equal>
              <% mapNum = -1 ; %>
              <% String lastDS = "-1"; %>
       <logic:notPresent name="groupsdefined">
        <group>
          <Title>VegBank Plots</Title>
          <Id>vb</Id>
          <BaseIcon><![CDATA[@machine_url@/vegbank/images/map.gif]]></BaseIcon>
        </group>
        <vegbank:get id="plotobs" select="plotandobservation" whereNumeric="where_observation_pk" 
                whereNonNumeric="where_observation_ac" beanName="map" pager="false" perPage="-1"
                xwhereEnable="true" allowOrderBy="true" />
      </logic:notPresent>
    </ymaps:Groups>
    
    <!-- here, loop around the plots: -->
    
    <logic:notEmpty name="plotobs-BEANLIST">
     <logic:iterate id="onerowofobservation" name="plotobs-BEANLIST">
               <!-- figure out which map icon to use -->
               <!-- this can be removed as soon as yahoo fixes their interface -->
               <logic:notEmpty name="onerowofobservation" property="userdataset_id">
                 <!-- if not same as before, up mapNum -->
                 <logic:notEqual value="<%= lastDS %>" name="onerowofobservation" property="userdataset_id">
                   <% mapNum ++; %>
                   <bean:define id="lastDSBean"><bean:write name="onerowofobservation" property="userdataset_id" /></bean:define>
                   <% lastDS = lastDSBean ; %>
                 </logic:notEqual>
               </logic:notEmpty>
               <logic:empty name="onerowofobservation" property="userdataset_id">
                 <!-- increase  mapNum by 1 -->
                 <% mapNum ++; %>
               </logic:empty>
               <%   if (mapNum == 10 ) {
                      mapNum = 0;  //reset mapNum to 0, as there are only 0-9 for images.
                      %> <!-- resetting map numbers to 0 -->
                      <%
                    }
                   %>
               
       <bean:define id="observation_pk" name="onerowofobservation" property="observation_id"/>
       <item>
         <title>Plot: <bean:write name="onerowofobservation" property="authorobscode" />
             <logic:empty name="onerowofobservation" property="authorobscode">
               <bean:write name="onerowofobservation" property="authorplotcode" />
             </logic:empty></title>
         <link><![CDATA[@machine_url@/get/comprehensive/observation/<bean:write name="observation_pk" />]]></link>
         <description>
           <logic:notEmpty name="onerowofobservation" property="datasetname">Dataset: 
             <bean:write name="onerowofobservation" property="datasetname" ignore="true"/>
           </logic:notEmpty>
         </description>
         <geo:lat><bean:write name="onerowofobservation" property="latitude" /></geo:lat>
         <geo:long><bean:write name="onerowofobservation" property="longitude" /></geo:long>
         <!-- the following assigns to a group -->
         <ymaps:GroupId>vb<logic:notEmpty name="onerowofobservation" property="userdataset_id">_<bean:write name="onerowofobservation" property="userdataset_id" /></logic:notEmpty></ymaps:GroupId>
         <ymaps:BaseIcon>@machine_url@/vegbank/images/map_<%= mapNum %>.gif</ymaps:BaseIcon><!-- shouldn't be needed, but it is.  should just link to group and use its icon -->
         <ymaps:CityState><bean:write name="onerowofobservation" property="stateprovince" /></ymaps:CityState>
         <ymaps:Country><bean:write name="onerowofobservation" property="country" /></ymaps:Country>
       </item>
       
       
     </logic:iterate>  
    </logic:notEmpty>
  </channel>
</rss>
