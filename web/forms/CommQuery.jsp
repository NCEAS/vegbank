@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@

  <title>VegBank Community Type Search</title>
 
<script language="javascript">
function prepareForm() {
	setDate();
	setNameMatchType();
}

function setDate() {
	month = document.queryform.ebMonth.value;
	date = document.queryform.ebDate.value;
	year = document.queryform.ebYear.value;

	document.queryform.xwhereParams_date_0.value = "";

	if (month != null && date != null && year != null &&
			month != "" && date != "" && year != "") {

		document.queryform.xwhereParams_date_0.value = 
			date + "-" + month + "-" + year;
	}

}

function setNameMatchType() {
	matchType = document.queryform.matchType;

	if (matchType[1].checked) {
		document.queryform.xwhereKey_commname.value = "xwhere_match";
		document.queryform.xwhereSearch_commname.value = "true";
		document.queryform.xwhereMatchAny_commname.value = "true";
	} else if (matchType[2].checked) {
		document.queryform.xwhereKey_commname.value = "xwhere_ilike";
		document.queryform.xwhereSearch_commname.value = "false";
		document.queryform.xwhereMatchAny_commname.value = "false";
	} else {
		document.queryform.xwhereKey_commname.value = "xwhere_match";
		document.queryform.xwhereSearch_commname.value = "true";
		document.queryform.xwhereMatchAny_commname.value = "false";
	}
}
</script>




  @webpage_masthead_html@ 
  
 <h1>Community Type Search</h1>
			
    
         <form action="@web_context@views/commconcept_summary.jsp" method="get" name="queryform" onsubmit="prepareForm()">
        <!--html:form action="/CommQuery"-->
			<input type="hidden" name="where" value="where_commconcept_mpq" size="40"/>

        <p class="instructions">
				<strong>All search criteria are optional.</strong>
				<br/>
				Try the <a href="@web_context@forms/PlantQuery.jsp">plant query</a> too.
				</p>
            

         <p><b>Community name:</b><br/>
              <!-- COMM TAXON INPUT--> 

              
		<input type="text" size="35" name="xwhereParams_commname_0"/>
		<input type="hidden" name="xwhereParams_commname_1" value="cu.commname"/>
				&nbsp; <span class="normal">e.g. tidal, brackish
				<br/>
		<input type="radio" name="matchType" value="all" checked="checked"/>contains ALL words
				<br/>
		<input type="radio" name="matchType" value="any"/>contains ANY word
				<br/>
		<input type="radio" name="matchType" value="is"/>is exactly 
				<span class="sizetiny">
				(use % as a wildcard: e.g. %sedge)
				</span>
				<br/>
		<input type="hidden" name="xwhereKey_commname" value="xwhere_match"/>
		<input type="hidden" name="xwhereSearch_commname" value="true"/>
		<input type="hidden" name="xwhereMatchAny_commname" value="false"/>
				</span>
                <br/>
					<input type="submit" value="search for communities"/>

	      </p>
                <hr/>
           
	    <!-- Community level -->	    
            <p><b>Community level:</b>
			  <br/><span class="sizetiny">Choose multiple with CTRL-click or Apple-click</span>
			  <br/>

		
	    <select name="xwhereParams_commlevel_0" size="5" multiple="true">
			<option value="" selected>--ANY--</option>
			<!--html:options property="commLevels"/-->

<vegbank:get id="commlevel" select="commstatus_commlevel" where="where" pager="false" perPage="-1"/>
<logic:notEmpty name="commlevel-BEANLIST">
	<logic:iterate id="onerow" name="commlevel-BEANLIST">
	<option value="<bean:write name="onerow" property="commlevel"/>">
			<bean:write name="onerow" property="commlevel"/></option>
	</logic:iterate>
</logic:notEmpty>


		<!--/html:select-->
		</select>
		<input type="hidden" name="xwhereParams_commlevel_1" value="cs.commlevel"/>
		<input type="hidden" name="xwhereKey_commlevel" value="xwhere_in"/>
              </p>
            
	    
      <!-- Name ClassSystem -->
            <p><b>Name type:</b>
			  <br/><span class="sizetiny">Choose multiple with CTRL-click or Apple-click</span>
			  <br/>
            
	        <select name="xwhereParams_classsystem_0" size="5" multiple="true">
			   <option value="" selected>--ANY--</option>
			   <!--html:options property="commClassSystems"/-->

<vegbank:get id="classsystem" select="commusage_classsystem" where="where" pager="false" perPage="-1"/>
<logic:notEmpty name="classsystem-BEANLIST">
	<logic:iterate id="onerow" name="classsystem-BEANLIST">
	<option value="<bean:write name="onerow" property="classsystem"/>">
			<bean:write name="onerow" property="classsystem"/></option>
	</logic:iterate>
</logic:notEmpty>

	      	 <!--/html:select-->
	      	 </select>
			<input type="hidden" name="xwhereParams_classsystem_1" value="cu.classsystem"/>
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
				<!--input type="hidden" name="xwhereParams_date_1" value="cs.startdate"/-->
				<input type="hidden" name="xwhereKey_date" value="where_commstatus_daterange"/>
                   <a href="@help-for-concept-date-href@"><img 
				   height="14" width="14" border="0" src="@image_server@question.gif"></a>
              </p>

            <!-- PARTY -->


            <p>
              <b>Party:</b><br/>
            
             
                <!--html:select property="xwhereParams_accordingtoparty_0"-->
                <select name="xwhereParams_accordingtoparty_0">
                  <option value="">--ANY--</option>
<vegbank:get id="party" select="commstatus_party" where="where" pager="false" perPage="-1"/>
<logic:notEmpty name="party-BEANLIST">
	<logic:iterate id="onerow" name="party-BEANLIST">
	<option value="<bean:write name="onerow" property="party_id"/>">
			<bean:write name="onerow" property="party_id_transl"/></option>
	</logic:iterate>
</logic:notEmpty>

                </select>
				<input type="hidden" name="xwhereParams_accordingtoparty_1" value="cs.party_id"/>
				<input type="hidden" name="xwhereKey_accordingtoparty" value="xwhere_eq"/>
              </p>

           <!-- AND / OR -->

                <blockquote>
		      <input type="radio" name="xwhereGlue" value="AND" checked="checked"/>match ALL criteria<br/>
		      <input type="radio" name="xwhereGlue" value="OR"/>match ANY criteria<br/>
                </blockquote>
           
            
					<input type="submit" value="search for communities"/>
              
        <!--/html:form-->
        </form>
	


         
@webpage_footer_html@
