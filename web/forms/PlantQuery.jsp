@stdvegbankget_jspdeclarations@


<html>
<!-- 
  *   '$RCSfile: PlantQuery.jsp,v $'
  *     Purpose: web form to query the plant taxonomy portion of vegbank
  *   Copyright: 2000 Regents of the University of California and the
  *               National Center for Ecological Analysis and Synthesis
  *     Authors: @author@
  *
  *    '$Author: anderson $'
  *      '$Date: 2005-02-10 21:20:00 $'
  *  '$Revision: 1.10 $'
  *
  *
  -->

<head>@defaultHeadToken@

  <title>PLANT CONCEPT LOOKUP</title>
  <link rel="stylesheet" href="@stylesheet@" type="text/css">

  <style type="text/css">
  body {
  background-color: #FFFFFF;
  color: #531100;
  }
  :link { color: #0033CC }
  :visited { color: #005680 }
  :active { color: #0066FF }
  td.c4 {color: #000000; font-family: Helvetica,Arial,Verdana}
  span.c3 {color: black; font-family: Helvetica,Arial,Verdana; font-size: 70%}
  span.c2 {color: #209020; font-family: Helvetica,Arial,Verdana; font-size: 80%}
  span.c1 {color: #23238E; font-family: Helvetica,Arial,Verdana; font-size: 200%}
  </style>

<script language="javascript">
function prepareForm() {
	setDate();
	setNameMatchType();
}

function setDate() {
	month = document.queryform.ebMonth.value;
	date = document.queryform.ebDate.value;
	year = document.queryform.ebYear.value;

	document.queryform.xwhereParams_date_0.value = null;

	if (month != null && date != null && year != null &&
			month != "" && date != "" && year != "") {

		document.queryform.xwhereParams_date_0.value = 
			date + "-" + month + "-" + year;
	}
}


function setNameMatchType() {
	matchType = document.queryform.matchType;

	if (matchType[1].checked) {
		document.queryform.xwhereKey_plantname.value = "xwhere_match";
		document.queryform.xwhereSearch_plantname.value = "true";
		document.queryform.xwhereMatchAny_plantname.value = "true";
	} else if (matchType[2].checked) {
		document.queryform.xwhereKey_plantname.value = "xwhere_ilike";
		document.queryform.xwhereSearch_plantname.value = "false";
		document.queryform.xwhereMatchAny_plantname.value = "false";
	} else {
		document.queryform.xwhereKey_plantname.value = "xwhere_match";
		document.queryform.xwhereSearch_plantname.value = "true";
		document.queryform.xwhereMatchAny_plantname.value = "false";
	}
}

</script>

</head>

<body>
  @vegbank_header_html_normal@ <!-- SECOND TABLE -->

  <table align="left" border="0" width="90%" cellspacing="0" cellpadding="0">
    <tr>
      <td bgcolor="white"><img align="center" border="0" height="100" src=
      "@image_server@owlogoBev.jpg" alt="Veg plots logo"></td>

      <td align="left" valign="middle">
        <table border="0" cellpadding="5" width="366" height="55">
          <tr>
            <td align="left" valign="bottom"><span class="c1">Plant Concept Lookup</span><br></td>
          </tr>
        </table>
      </td>
    </tr>

    <tr>
      <!-- LEFT MARGIN -->
      <td width="15%" bgcolor="white" align="left" valign="top">

      <td align="left">

        <form action="@views_link@plantconcept_detail.jsp" method="get" name="queryform" onsubmit="prepareForm()">
			<input type="hidden" name="where" value="where_plantconcept_mpq"/>

          <table>
            <tr valign="top">
              <td align="left" valign="middle" colspan="2">
                <span class="c2">
				<b>All search criteria are optional.</b>
				<br>
				Try the <a href="@forms_link@CommQuery.jsp">community query</a> too.
				</span>
              </td>
            </tr>
          </table>

          <br/>

          <table border="0" width="722">
            <!--TAXON NAME -->

            <tr align="left" valign="top">
              <td width="156"><b>Taxon name:</b></td>

              <td width="556">
		<input type="text" size="35" name="xwhereParams_plantname_0"/>
		<input type="hidden" name="xwhereParams_plantname_1" value="pu.plantname"/>
				&nbsp; <span class="normal">e.g. maple, acer rubrum
				<br>
		<input type="radio" name="matchType" value="all" checked="checked"/>contains ALL words
				<br>
		<input type="radio" name="matchType" value="any"/>contains ANY word
				<br>
		<input type="radio" name="matchType" value="is"/>is exactly
				<span class="sizetiny">
				(use % as a wildcard to match anything)
				</span>
				<br>
		<input type="hidden" name="xwhereKey_plantname" value="xwhere_match"/>
		<input type="hidden" name="xwhereSearch_plantname" value="true"/>
		<input type="hidden" name="xwhereMatchAny_plantname" value="false"/>
				</span>

	      </td>
            </tr><!-- IGNORE CASE -->

            <tr>
              <td colspan="2" rowspan="1" align="left" valign="middle">
                <hr size=".1">
              </td>
            </tr>
	    
	    <!-- Taxon level -->	    
            <tr>
              <td width="156" align="left" valign="top"><b>Taxon level:</b>
			  <br><span class="sizetiny">Choose multiple with CTRL-click or Apple-click</span>
			  </td>

		<td width="556" class="c4" align="left" valign="top" width="556">
	    <select name="xwhereParams_plantlevel_0" size="5" multiple="true">
			<option value="" selected>--ANY--</option>

<vegbank:get id="plantlevel" select="plantstatus_plantlevel" where="where"/>
<logic:notEmpty name="plantlevel-BEANLIST">
	<logic:iterate id="onerow" name="plantlevel-BEANLIST">
		<logic:notEmpty name="onerow" property="plantlevel">
		  	<option value="<bean:write name="onerow" property="plantlevel"/>">
				<bean:write name="onerow" property="plantlevel"/></option>
		</logic:notEmpty>
	</logic:iterate>
</logic:notEmpty>

		</select>

		<input type="hidden" name="xwhereParams_plantlevel_1" value="ps.plantlevel"/>
		<input type="hidden" name="xwhereKey_plantlevel" value="xwhere_in"/>
              </td>
            </tr>
	    
	    <!-- HORIZONTAL LINE -->
            <tr>
              <td colspan="2" rowspan="1" align="left" valign="middle">
                <hr size=".1">
              </td>
            </tr>
		<!-- Name ClassSystem -->
            <tr>
              <td width="156" align="left" valign="top"><b>Name type:</b>
			  <br><span class="sizetiny">Choose multiple with CTRL-click or Apple-click</span>
			  </td>
              <td class="c4" align="left" valign="top" width="556">
	        <!--html:select property="xwhereParams_classsystem_0" size="6" multiple="true"-->
	        <select name="xwhereParams_classsystem_0" size="5" multiple="true">
			   <option value="" selected>--ANY--</option>
			   <!--html:options property="plantClassSystems"/-->

<vegbank:get id="classsystem" select="plantusage_classsystem" where="where"/>
<logic:notEmpty name="classsystem-BEANLIST">
	<logic:iterate id="onerow" name="classsystem-BEANLIST">
	<option value="<bean:write name="onerow" property="classsystem"/>">
			<bean:write name="onerow" property="classsystem"/></option>
	</logic:iterate>
</logic:notEmpty>

	      	 <!--/html:select-->
	      	 </select>
			<input type="hidden" name="xwhereParams_classsystem_1" value="pu.classsystem"/>
			<input type="hidden" name="xwhereKey_classsystem" value="xwhere_in"/>
	      </td>
	    </tr>
		
	    
            <!-- HORIZONTAL LINE -->
            <tr>
              <td colspan="2" rowspan="1" align="left" valign="middle">
                <hr size=".1">
              </td>
            </tr>

            <tr>
              <td width="156" align="left" valign="top" height="20">
              <b>As of date:</b></td>

              <td class="c4" align="left" valign="top" height="20" width="556">
				<select name="ebDate"> 
					<option value=""></option> <option value="1">1</option>
					<option value="2">2</option> <option value="3">3</option>
					<option value="4">4</option> <option value="5">5</option>
					<option value="6">6</option> <option value="7">7</option>
					<option value="8">8</option> <option value="9">9</option>
					<option value="10">10</option> <option value="11">11</option>
					<option value="12">12</option> <option value="13">13</option>
					<option value="14">14</option> <option value="15">15</option>
					<option value="16">16</option> <option value="17">17</option>
					<option value="18">18</option> <option value="19">19</option>
					<option value="20">20</option> <option value="21">21</option>
					<option value="22">22</option> <option value="23">23</option>
					<option value="24">24</option> <option value="25">25</option>
					<option value="26">26</option> <option value="27">27</option>
					<option value="28">28</option> <option value="29">29</option>
					<option value="30">30</option> <option value="31">31</option>
				</select>
				-
				<select name="ebMonth"> 
					<option value=""></option> <option value="JAN">JAN</option>
					<option value="FEB">FEB</option> <option value="MAR">MAR</option>
					<option value="APR">APR</option> <option value="MAY">MAY</option>
					<option value="JUN">JUN</option> <option value="JUL">JUL</option>
					<option value="AUG">AUG</option> <option value="SEP">SEP</option>
					<option value="OCT">OCT</option> <option value="NOV">NOV</option>
					<option value="DEC">DEC</option> 
				</select>
				-
				<input type="text" name="ebYear" size="4"/>
				<input type="hidden" name="xwhereParams_date_0"/> 
				<!--input type="hidden" name="xwhereParams_date_1" value="ps.startdate"/-->
				<input type="hidden" name="xwhereKey_date" value="where_plantstatus_daterange"/>
                   <a href="@help-for-concept-date-href@"><img 
				   height="14" width="14" border="0" src="@image_server@question.gif"></a>
              </td>
            </tr>

            <tr>
              <td colspan="2" rowspan="1" align="left" valign="middle">
                <hr size=".1">
              </td>
            </tr><!-- PARTY -->


            <tr>
              <td width="156" align="left" valign="top">
              <b>Party:</b></td>
            
              <td class="c4" align="left" valign="top" height="54" width="556">
                <!--html:select property="xwhereParams_accordingtoparty_0"-->
                <select name="xwhereParams_accordingtoparty_0">
                  <option value="">--ANY--</option>
<vegbank:get id="party" select="plantstatus_party" where="where"/>
<logic:notEmpty name="party-BEANLIST">
	<logic:iterate id="onerow" name="party-BEANLIST">
	<option value="<bean:write name="onerow" property="party_id"/>">
			<bean:write name="onerow" property="party_id_transl"/></option>
	</logic:iterate>
</logic:notEmpty>

                </select>
				<input type="hidden" name="xwhereParams_accordingtoparty_1" value="ps.party_id"/>
				<input type="hidden" name="xwhereKey_accordingtoparty" value="xwhere_eq"/>
              </td>
            </tr>

            <tr>
              <td colspan="2" rowspan="1" align="left" valign="middle">
                <hr size=".1">
              </td>
            </tr>
			<!-- AND / OR -->

            <tr>
              <td align="left" valign="middle" colspan="2">
                <blockquote>
		      <input type="radio" name="xwhereGlue" value="AND" checked="checked"/>match ALL criteria<br/>
		      <input type="radio" name="xwhereGlue" value="OR"/>match ANY criteria<br/>
                </blockquote>
              </td>
            </tr>
	    
            <tr>
              <td align="left" valign="middle" colspan="2">
			  <br>
					<input type="submit" value="search for plants"/>
              </td>
            </tr>
	    
            <tr>
              <td width="156"></td>
            </tr>
          </table>
        <!--/html:form-->
        </form>

        <table border="0" cellspacing="5" cellpadding="5">
          <tr>
            <td colspan="2"><!-- VEGBANK FOOTER -->
            @vegbank_footer_html_tworow@</td>
          </tr>
        </table>
      </td>
    </tr>
  </table>
</body>
</html>
