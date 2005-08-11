<!-- STUFF IN THIS FILE THAT ARE ALL CAPS ARE ALREADY IN TYPICAL VIEWS.  THE OTHER STUFF NEEDS ADDING TO 
 GET IT HOOKED UP TO DATACART ADDITION -->


  @ajax_js_include@
  @datacart_js_include@

<VEGBANK:GET ID="{ID}"/>  

      <!--data cart -->
    <!-- add all results -->

        <a href="javascript:addAllResults('{ID}');"
            title="add all query results to datacart" class="nobg"><img src="/vegbank/images/cart_star_on_blue2.gif" border="0" id="datacart-addallresults-icon" /></a>

        <a href="javascript:addAllResults('{ID}');"
            title="add all results to datacart">add all query results</a> to datacart,&nbsp;&nbsp;

        <a href="javascript:addAllOnPage()" title="add all on page to datacart" class="nobg"><img src="/vegbank/images/cart_add_one.gif" border="0" /></a>
        <a href="javascript:addAllOnPage()" title="add all on page">add plots on page</a> to datacart,&nbsp;&nbsp;
                                                                                                                                                                                                  
    <!-- drop page -->

        <a href="javascript:dropAllOnPage()" title="drop all on page from datacart" class="nobg"><img src="/vegbank/images/cart_drop_one.gif" border="0" /></a>
        <a href="javascript:dropAllOnPage()" title="drop all on page">drop plots on page</a> from datacart

<%@ include file="../includes/setup_rowindex.jsp" %>

<LOGIC:NOTEMPTY NAME="{ID}-BEANLIST"><!-- SET UP TABLE -->
     <!--data cart -->
    <logic:equal parameter="delta" value="findadd-observationaccessioncode">
        <vegbank:datacart delta="findadd:observation:observation:observation_id:observationaccessioncode" deltaItems="getQuery" display="false" />
    </logic:equal>

<form action="" method="GET" id="cartable">
 <TABLE width="100%" class="leftrightborders" cellpadding="2" id="observationsummarytableid">
  <TR>
    <th valign="bottom" align="center" nowrap="nowrap">Add/Drop</th>
  </TR>

  <LOGIC:ITERATE ID="ONEROWOFOBSERVATION" NAME="BEANLIST">
    <!-- ITERATE OVER ALL RECORDS IN SET : NEW TABLE FOR EACH -->
    <BEAN:DEFINE ID="OBSID" NAME="ONEROWOFPLOT" PROPERTY="OBSERVATION_ID"/>
    <!--BEAN:DEFINE ID="OBSERVATION_PK" NAME="ONEROWOFPLOT" PROPERTY="OBSERVATION_ID"/-->


    <TR CLASS='@NEXTCOLORCLASS@' ALIGN="LEFT">
      <td align="center"><!-- that td cannot have a class, it gets overwritten -->
        <bean:define id="delta_ac" name="onerowof{id}" property="accessioncode" />
        Plot #<%= rowIndex++ %>
        <br/>
        <%@ include file="../includes/datacart_checkbox.jsp" %>
      </td>
    </TR>
  </LOGIC:ITERATE><!-- PLOT -->
 </TABLE>
</form>
@mark_datacart_items@


</LOGIC:NOTEMPTY>



<BR />
@WEBPAGE_FOOTER_HTML@


<!-- also MUST add a file to ajax/findadd_{id}_datacart.ajax.jsp