@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@

  <title>VegBank Plant Search</title>
  
  <style type="text/css">
  xxtd.c4 {color: #000000; font-family: Helvetica,Arial,Verdana}
  xxspan.c3 {color: black; font-family: Helvetica,Arial,Verdana; font-size: 70%}
  xxspan.c2 {color: #209020; font-family: Helvetica,Arial,Verdana; font-size: 80%}
  xxspan.c1 {color: #23238E; font-family: Helvetica,Arial,Verdana; font-size: 200%}
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

  @webpage_masthead_html@ 

  <h1>Plant Search</h1>


        <form action="@views_link@plantconcept_detail.jsp" method="get" name="queryform" onsubmit="prepareForm()">
			<input type="hidden" name="where" value="where_plantconcept_mpq"/>

         
                <p class="instructions">
				<strong>All search criteria are optional.</strong>
				<br />
				Try the <a href="@forms_link@CommQuery.jsp">community query</a> too.
				</p>
             
          
             <p><b>Taxon name:</b><br/>

             
		<input type="text" size="35" name="xwhereParams_plantname_0"/>
		<input type="hidden" name="xwhereParams_plantname_1" value="pu.plantname"/>
				&nbsp; <span class="normal">e.g. maple, acer rubrum
				<br/>
		<input type="radio" name="matchType" value="all" checked="checked"/>contains ALL words
				<br/>
		<input type="radio" name="matchType" value="any"/>contains ANY word
				<br/>
		<input type="radio" name="matchType" value="is"/>is exactly
				<span class="sizetiny">
				(use % as a wildcard: e.g. maple%)
				</span>
				<br/>
		<input type="hidden" name="xwhereKey_plantname" value="xwhere_match"/>
		<input type="hidden" name="xwhereSearch_plantname" value="true"/>
		<input type="hidden" name="xwhereMatchAny_plantname" value="false"/>
				</span>
			  <br />
					<input type="submit" value="search for plants"/>
         </p>
                <hr/>
              
	    <!-- Taxon level -->	    
           <p><b>Taxon level:</b>
			  <br/><span class="sizetiny">Choose multiple with CTRL-click or Apple-click</span>
			  <br/>

		
	    <select name="xwhereParams_plantlevel_0" size="5" multiple="true">
			<option value="" selected>--ANY--</option>

<vegbank:get id="plantlevel" select="plantstatus_plantlevel" where="where"/>
<logic:notEmpty name="plantlevel-BEANLIST">
	<logic:iterate id="onerow" name="plantlevel-BEANLIST">
		<logic:notEmpty name="onerow" property="plantlevel">
		  	<option value='<bean:write name="onerow" property="plantlevel"/>'>
				<bean:write name="onerow" property="plantlevel"/></option>
		</logic:notEmpty>
	</logic:iterate>
</logic:notEmpty>

		</select>

		<input type="hidden" name="xwhereParams_plantlevel_1" value="ps.plantlevel"/>
		<input type="hidden" name="xwhereKey_plantlevel" value="xwhere_in"/>
              </p>
	    
           
		<!-- Name ClassSystem -->
            <p><b>Name type:</b>
			  <br/><span class="sizetiny">Choose multiple with CTRL-click or Apple-click</span>
			  <br/>
              
	        <!--html:select property="xwhereParams_classsystem_0" size="6" multiple="true"-->
	        <select name="xwhereParams_classsystem_0" size="5" multiple="true">
			   <option value="" selected>--ANY--</option>
			   <!--html:options property="plantClassSystems"/-->

<vegbank:get id="classsystem" select="plantusage_classsystem" where="where"/>
<logic:notEmpty name="classsystem-BEANLIST">
	<logic:iterate id="onerow" name="classsystem-BEANLIST">
	<option value='<bean:write name="onerow" property="classsystem"/>'>
			<bean:write name="onerow" property="classsystem"/></option>
	</logic:iterate>
</logic:notEmpty>

	      	 <!--/html:select-->
	      	 </select>
			<input type="hidden" name="xwhereParams_classsystem_1" value="pu.classsystem"/>
			<input type="hidden" name="xwhereKey_classsystem" value="xwhere_in"/>
	      </p>
		
	    
           
            <p>
              <b>As of date:</b><br/>

              
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
              </p>

             <!-- PARTY -->


            <p>
              <b>Party:</b><br/>
            
              
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
              </p>

           <!-- AND / OR -->

                 <blockquote>
		      <input type="radio" name="xwhereGlue" value="AND" checked="checked"/>match ALL criteria<br/>
		      <input type="radio" name="xwhereGlue" value="OR"/>match ANY criteria<br/>
                </blockquote>
            
            			<input type="submit" value="search for plants"/>
              
        <!--/html:form-->
        </form>

        

@webpage_footer_html@
