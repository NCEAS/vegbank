@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8" />
@defaultHeadToken@
 
<TITLE>Browse VegBank Plants</TITLE>
<link rel="stylesheet" href="@stylesheet@" type="text/css" />


       @webpage_masthead_html@
      @possibly_center@
        <h2>Browse VegBank Plants</h2>
        <p class="instructions">With this page, you can browse through plant concepts in VegBank.  For more search options, please see
        the <a href="@forms_link@PlantQuery.jsp">plant query</a>. </p> 
<!--Get standard declaration of rowClass as string: -->
        <% String rowClass = "evenrow"; %>
 <h3>By Name</h3>
 <p> 
 <%@ include file="../includes/menu-plants-byletter.jsp" %>
 </p>
 <h3>By Hierarchy</h3>
 <p>You may select the top of the hierarchy to start browsing: </p>
  <p><blockquote> <a href="@get_link@std/plantconcept/'VB.PC.92206.PLANTAE'">Plantae</a></blockquote></p>
 
 <p>Or you may start browing by choosing a family from this list:</p>
   <blockquote>
   <form action="@views_link@plantconcept_detail.jsp?where=where_plantconcept_pk" method="get" name="getfamily">
      <input type="hidden" value="where_plantconcept_pk" name="where" />
    <select size="10" name="wparam">
      <vegbank:get id="plantconcept" select="plantconcept" 
        pager="false" perPage="-1" beanName="map" where="where_plantconcept_isfamily_ord" wparam="1" />
        <logic:iterate id="onerowofplantconcept" name="plantconcept-BEANLIST">
          <option value='<bean:write name="onerowofplantconcept" property="plantconcept_id" />'><bean:write name="onerowofplantconcept" property="plantname_id_transl" /></option>        
        
        </logic:iterate>
    </select>
    <br />
    <input type="submit" value="submit" />
    </form>
    </blockquote>
 <p>If you still can't find the plants you're interested in, <br />please use the <a href="@forms_link@PlantQuery.jsp">plant query</a> 
 to search with more options.</p>
 
 
          @webpage_footer_html@
