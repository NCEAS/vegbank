<?xml version="1.0" encoding="UTF-8" ?>
<?xml-stylesheet type="text/xsl" href="@xml_link@util/htmltable2csv.xsl"?>
@stdvegbankget_jspdeclarations@
<!-- declare how many communities to get -->
<% int commnum = 4 ; %>

<vegbank:get id="plotobs" select="plotandobservation" whereNumeric="where_observation_pk" 
whereNonNumeric="where_observation_ac" beanName="map" pager="true"/>
<!-- header -->
<table>
<tr>
<%@ include file="autogen/observation_summarycsv_head.jsp" %>
<%@ include file="autogen/plot_summarycsv_head.jsp" %>


  <%
  for (int i=1; i<=commnum ; i++)
  {
  %>
  
  <th>Community Name <%= i %></th>
  <th>Community Date <%= i %></th>
  <th>Community Fit <%= i %></th>
  <th>Community Confidence <%= i %></th>
  
  <%
  }
  %>
 <!-- for debugging comms : <th>last col</th> -->
</tr>
<logic:notEmpty name="plotobs-BEANLIST">

 <logic:iterate id="onerowofobservation" name="BEANLIST">
   <bean:define id="onerowofplot" name="onerowofobservation" />
   <bean:define id="observation_pk" name="onerowofplot" property="observation_id"/>
   <bean:define id="plot_pk" name="onerowofplot" property="plot_id"/>
 <tr>
 <%@ include file="autogen/observation_summarycsv_data.jsp" %>   
 <%@ include file="autogen/plot_summarycsv_data.jsp" %>
 <!-- need to include some community info, too -->
 
      <vegbank:get id="comminterpretation" select="comminterpretation_withobs" beanName="map" 
         where="where_observation_pk" wparam="observation_pk" perPage="-1" pager="false"/>
     <logic:empty name="comminterpretation-BEANLIST">
      <%
     	  for (int k=1; k<=commnum ; k++)
    	  {
	   %>
	        <td/>
	        <td/>
	        <td/>
	        <td/>  
	  <%
      	  }
       %>
     </logic:empty>

     <logic:notEmpty name="comminterpretation-BEANLIST">
      <% int j=1 ; %>
     <logic:iterate id="onerowofcomminterpretation" name="comminterpretation-BEANLIST" length="4">
     <!-- iterate over all records in set : new table for each -->
       <td><bean:write name="onerowofcomminterpretation" property="commconcept_id_transl" /></td>
       <td><bean:write name="onerowofcomminterpretation" property="classstartdate" /></td>
       <td><bean:write name="onerowofcomminterpretation" property="classfit" /></td>
       <td><bean:write name="onerowofcomminterpretation" property="classconfidence" /></td>
       <% j++ ; %>
     </logic:iterate>
       <%
         for (int j2=j; j2<=commnum ; j2++)
         {
       %>
         <td/><!-- extra row -->
         <td/>
         <td/>
         <td/>
       <%
         }
       %>
       
     </logic:notEmpty>
     
       
   <!-- for debugging comms : <td>last val</td> -->
 
 </tr><!-- observation row -->
</logic:iterate></logic:notEmpty>
</table>