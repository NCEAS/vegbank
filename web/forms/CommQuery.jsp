@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@

  <title>VegBank Community Type Search</title>
 
<script language="javascript">


function getHelpPageId() {
  return "search-for-communities";
}




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
        qexplain = " contains any words of "; /* this used later in building criteriaAsText */
	} else if (matchType[2].checked) {
		document.queryform.xwhereKey_commname.value = "xwhere_ilike";
		document.queryform.xwhereSearch_commname.value = "false";
		document.queryform.xwhereMatchAny_commname.value = "false";
        qexplain = " is "; /* this used later in building criteriaAsText */
	} else {
		document.queryform.xwhereKey_commname.value = "xwhere_match";
		document.queryform.xwhereSearch_commname.value = "true";
		document.queryform.xwhereMatchAny_commname.value = "false";
        qexplain = " contains ALL words of "; /* this used later in building criteriaAsText */
	}

    /* do same as above but for description */
    
      matchTypeDesc = document.queryform.matchTypeDesc;
          
      if (matchTypeDesc[1].checked) {
        document.queryform.xwhereKey_commdesc.value = "xwhere_match";
        document.queryform.xwhereSearch_commdesc.value = "true";
        document.queryform.xwhereMatchAny_commdesc.value = "true";
            qexplainDesc = " contains any words of "; /* this used later in building criteriaAsText */
      } else if (matchTypeDesc[2].checked) {
        document.queryform.xwhereKey_commdesc.value = "xwhere_ilike";
        document.queryform.xwhereSearch_commdesc.value = "false";
        document.queryform.xwhereMatchAny_commdesc.value = "false";
            qexplainDesc = " is "; /* this used later in building criteriaAsText */
      } else {
        document.queryform.xwhereKey_commdesc.value = "xwhere_match";
        document.queryform.xwhereSearch_commdesc.value = "true";
        document.queryform.xwhereMatchAny_commdesc.value = "false";
            qexplainDesc = " contains ALL words of "; /* this used later in building criteriaAsText */
      }
    
    


    /* this part of the function sets the criteriaAsText field which the next form displays as: You searched for this and that... */
    /* var to store building string */
    text = "" ;
    strSep = " AND" ;
    /* get relevant fields into variables */
        thecommname = document.queryform.xwhereParams_commname_0.value ;
         if ( thecommname != "" &&  thecommname != null )
              {
                  text = text + strSep + " Community Name " + qexplain + "\"" + thecommname + "\"" ;
              }
        
    
        thedesc = document.queryform.xwhereParams_commdesc_0.value ;
                       if ( thedesc != "" &&  thedesc != null )
                                   {
                                      text = text + strSep + " Community Description " + qexplainDesc + "\"" + thedesc + "\"" ;
                           }
    
    
       /* value picklists, not PK */
         
              thelevel = getValuesFromList(document.queryform.xwhereParams_commlevel_0,"value") ;
              if ( thelevel != "" &&  thelevel != null )
                   {
                      text = text + strSep + " Community Level is " + thelevel;
               }
               
              theclasssystem = getValuesFromList(document.queryform.xwhereParams_classsystem_0,"value") ;
              if ( theclasssystem != "" &&  theclasssystem != null )
                   {
                      text = text + strSep + " Type of Name is " + theclasssystem;
                   }
              
            
              thedate = document.queryform.xwhereParams_date_0.value ;
                  if ( thedate != "" &&  thedate != null )
                           {
                              text = text + strSep + " On the Date: " + thedate ;
                       }
                  else { /* tell user all dates are being looked at, special case */
                           text = text + strSep + " On ALL DATES " ;
                  }
              
               theparty = getValuesFromList(document.queryform.xwhereParams_accordingtoparty_0,"text") ;
                          if ( theparty != "" &&  theparty != null )
                                   {
                                      text = text + strSep + " Party is " + theparty;
                               }

              
              
              theplotcount = document.queryform.xwhereParams_obscount_0.value ;
                   if ( theplotcount != "" &&  theplotcount != null )
                               {
                                  text = text + strSep + " with at least " + theplotcount + " plot(s)" ;
                           }




     
    text=text + "                   "; /* ensures substring will not fail */
    document.queryform.criteriaAsText.value =  text.substring(strSep.length)
    
}

</script>




  @webpage_masthead_html@ 
  
 <h1>Community Type Search</h1>
			
    
         <form action="@web_context@views/commconcept_summary.jsp" method="get" name="queryform" onsubmit="prepareForm()">
        
			      <input type="hidden" name="where" value="where_commconcept_mpq" size="40"/>
            <input name="criteriaAsText" type="hidden" value="" /> <!-- text to show user what they searched for --> 
          
          <logic:present parameter="requestingForm">  
            <!-- record requesting form name and requesting field name if in URL to pass to next page, when form is submitted -->
            <bean:parameter id="requestingForm" name="requestingForm" />
            <bean:parameter id="requestingField" name="requestingField" />
            <input type="hidden" name="requestingForm" value="<bean:write name='requestingForm' />" />
            <input type="hidden" name="requestingField" value="<bean:write name='requestingField' />" />
          </logic:present>  
        <p class="instructions">
				<strong>All search criteria are optional.</strong>
				<br/>
				Try the <a href="@web_context@forms/PlantQuery.jsp">plant query</a> too.
				</p>
            
         <div id="tut_commquerynamefield">
         <p><b>Community Name:</b><br/>
              <!-- COMM TAXON INPUT--> 

              
		<input type="text" size="35" name="xwhereParams_commname_0"/>
       <!-- link to help about searching -->
       <a href="@help-for-searching-href@"><img height="14" width="14" border="0" src="@image_server@question.gif" /></a>
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
          </div>
          <hr/>
          
                   <div id="tut_commquerydescfield">
                   <p><b>Community Description:</b><br/>
                        <!-- COMM TAXON INPUT--> 
          
                        
              <input type="text" size="35" name="xwhereParams_commdesc_0"/>
              <input type="hidden" name="xwhereParams_commdesc_1" value="cc.commdescription"/>
                  &nbsp; <span class="normal">e.g. acer quercus "north carolina"
                  <br/>
              <input type="radio" name="matchTypeDesc" value="all" checked="checked"/>contains ALL words
                  <br/>
              <input type="radio" name="matchTypeDesc" value="any"/>contains ANY word
                  <br/>
              <input type="radio" name="matchTypeDesc" value="is"/>is exactly 
                  <span class="sizetiny">
                  (use % as a wildcard: e.g. %acer %quercus %)
                  </span>
                  <br/>
              <input type="hidden" name="xwhereKey_commdesc" value="xwhere_match"/>
              <input type="hidden" name="xwhereSearch_commdesc" value="true"/>
              <input type="hidden" name="xwhereMatchAny_commdesc" value="false"/>
                  </span>
                          
          
                  </p>
          </div>
          
          
          
                <hr/>
           
	    <!-- Community level -->	    
            <p><b>Community level:</b>
			  <br/><span class="sizetiny">Choose multiple with CTRL-click or Apple-click</span>
			  <br/>

		
	    <select name="xwhereParams_commlevel_0" size="5" multiple="multiple">
			<option value="" selected>--ANY--</option>
			<!--html:options property="commLevels"/-->

<!--vegbank:get id="commlevel" select="commstatus_commlevel" where="where" pager="false" perPage="-1"/-->
        <!-- ORDERED list of levels -->
        <%@ include file="../includes/CommLevelList.jsp" %> 


		<!--/html:select-->
		</select>
		<input type="hidden" name="xwhereParams_commlevel_1" value="cs.commlevel"/>
		<input type="hidden" name="xwhereKey_commlevel" value="xwhere_in"/>
              </p>
            
	    
      <!-- Name ClassSystem -->
            <p><b>Name type:</b>
			  <br/><span class="sizetiny">Choose multiple with CTRL-click or Apple-click</span>
			  <br/>
            
	        <select name="xwhereParams_classsystem_0" size="5" multiple="multiple">
			   <option value="" selected>--ANY--</option>
			   <!--html:options property="commClassSystems"/-->

<vegbank:get id="classsystem" select="commusage_classsystem" where="where" pager="false" perPage="-1"/>
<logic:notEmpty name="classsystem-BEANLIST">
	<logic:iterate id="onerow" name="classsystem-BEANLIST">
	<option value='<bean:write name="onerow" property="classsystem"/>'>
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
					<option value=""></option> <%@ include file="/includes/days_currentselected.jsp" %>
				</select>
                -
				<select name="ebMonth"> 
					<option value=""></option> <%@ include file="/includes/months_currentselected.jsp" %>
				</select>
				-  <bean:define id="currentDateYear"><dt:format pattern="yyyy"><dt:currentTime/></dt:format></bean:define>
				<input type="text" name="ebYear" size="4" value="<%= currentDateYear %>" />
			
			<input type="hidden" name="xwhereParams_date_0"/> 
				<!--input type="hidden" name="xwhereParams_date_1" value="cs.startdate"/-->
				<input type="hidden" name="xwhereKey_date" value="where_commstatus_daterange"/>
                   <a href="@help-for-concept-date-href@"><img 
				   height="14" width="14" border="0" src="@image_server@question.gif" /></a>
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
	<option value='<bean:write name="onerow" property="party_id"/>'>
			<bean:write name="onerow" property="party_id_transl"/></option>
	</logic:iterate>
</logic:notEmpty>

                </select>
				<input type="hidden" name="xwhereParams_accordingtoparty_1" value="cs.party_id"/>
				<input type="hidden" name="xwhereKey_accordingtoparty" value="xwhere_eq"/>
              </p>
    <!-- return only comms that are on some number of plots? -->
          <p id="tut_minplots">
            <b>Show communities representing at least this many plots:</b><br/>
                          <input name="xwhereParams_obscount_0" value="0" />
            <input type="hidden" name="xwhereParams_obscount_1" value="cc.d_obscount"/>
            <input type="hidden" name="xwhereKey_obscount" value="xwhere_gteq"/>
          </p>




           <!-- AND / OR -->
             <!-- doesn't work with date 
                <blockquote>
		      <input type="radio" name="xwhereGlue" value="AND" checked="checked"/>match ALL criteria<br/>
		      <input type="radio" name="xwhereGlue" value="OR"/>match ANY criteria<br/>
                 </blockquote>
                 -->
                <input type="hidden" name="xwhereGlue" value="AND" />
            
					<input type="submit" value="search for communities"/>
              
        <!--/html:form-->
        </form>
	


         
@webpage_footer_html@
