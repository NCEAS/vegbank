<?xml version="1.0" encoding="UTF-8"?>
<rss version="2.0" xmlns:geo="http://www.w3.org/2003/01/geo/wgs84_pos#" xmlns:ymaps="http://api.maps.yahoo.com/Maps/V1/AnnotatedMaps.xsd">
<!-- @stdvegbankget_jspdeclarations@  -->
  <channel>
    <title>A Map of VegBank Plots</title>
    <link><![CDATA[@machine_url@/]]></link>
    <description>This map shows you the plots you requested on VegBank.  A file is generated in VegBank, then processed by an external mapping program, such as that found on Yahoo.com.  Maps will vary depending on the mapping program used.</description>
    <image><url>@machine_url@/vegbank/images/vegbank_logo69x100trans.gif</url><title>VegBank</title><link>http://vegbank.org</link></image>
    <ymaps:Groups>
      <group>
        <Title>VegBank Plots</Title>
        <Id>vegbankplots</Id>
        <BaseIcon><![CDATA[@machine_url@/vegbank/images/map.gif]]></BaseIcon>
      </group>
    </ymaps:Groups>
    <!-- here, loop around the plots: -->
    <vegbank:get id="plotobs" select="plotandobservation" whereNumeric="where_observation_pk" 
        whereNonNumeric="where_observation_ac" beanName="map" pager="false" perPage="-1"
        xwhereEnable="true" allowOrderBy="true" />
    <logic:notEmpty name="plotobs-BEANLIST">
     <logic:iterate length="50" id="onerowofobservation" name="plotobs-BEANLIST">
       <bean:define id="observation_pk" name="onerowofobservation" property="observation_id"/>
       <item>
         <title>Plot: <bean:write name="onerowofobservation" property="authorobscode" /></title>
         <link><![CDATA[@machine_url@/get/comprehensive/observation/<bean:write name="observation_pk" />]]></link>
         <description>Plot: <bean:write name="onerowofobservation" property="authorobscode" /></description>
         <geo:lat><bean:write name="onerowofobservation" property="latitude" /></geo:lat>
         <geo:long><bean:write name="onerowofobservation" property="longitude" /></geo:long>
         <ymaps:GroupId>vegbankplots</ymaps:GroupId>
         <ymaps:CityState><bean:write name="onerowofobservation" property="stateprovince" /></ymaps:CityState>
         <ymaps:Country><bean:write name="onerowofobservation" property="country" /></ymaps:Country>
       </item>
     </logic:iterate>  
    </logic:notEmpty>
  </channel>
</rss>
