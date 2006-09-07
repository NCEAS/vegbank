@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  <bean:define id="FullPageWidthSuffix" value="_100perc" /><!-- sets stylesheet to full width stylesheet -->
  @webpage_head_html@
  @ajax_js_include@
  @datacart_js_include@

 
       <script type="text/javascript">
         <!--
           function handleEmbargoKey(thevalue) {
             if (thevalue == "true" ) {
               document.filterform.xwhereKey_embargo.value = "xwhere_null";
             } else {
               document.filterform.xwhereKey_embargo.value = "xwhere_eq";
             }
           }
           
           function fillindate(ident) {
               // set date on particular field
               //requires that 3 fields be populated (with IDs):
               // date_{ident}_year, date_{ident}_month, date_{ident}_day
               // sets field (ID): date_{ident}_fulldate
               theyear = gebid("date_" + ident + "_year").value;
               themonth = gebid("date_" + ident + "_month").value;
               theday = gebid("date_" + ident + "_day").value;
               if ( theyear != "" &&  theyear != null &&
                    themonth != "" &&  themonth != null &&
                    theday != "" &&  theday != null 
                  ) {
                    //set value of fulldate to YYYY-MM-DD
                    gebid("date_" + ident + "_fulldate").value = theyear + "-" + themonth + "-" + theday;
                  } else {
                    //set value of fulldate to null
                    gebid("date_" + ident + "_fulldate").value = "";
                  }
             }
             function cloneResubmitFormFields(toForm) {
               //this function copies all the values in fields in the resubmitForm into another form
               // it ignores any fields that are not found on both forms
               resubmitform = document.forms.resubmitForm ;
               if (resubmitform) {
                 // now get all elements
                 var numelements = resubmitform.elements.length;
                 var numel2 = toForm.elements.length;
                 for (var i=0 ; i<numelements ; i++) {
                   for (var j=0 ; j<numel2 ; j++) {
                     if (toForm.elements[j].name == resubmitform[i].name) {
                       //matched fields, populate!
                       // only do this if value is not null and not "" -- otherwise screws up IE
                       if (resubmitform[i].value != null && resubmitform[i].value!="") toForm.elements[j].value =resubmitform[i].value;
                     }
                   }
                 
                 }
               }
             
             }
             function initThisPage() {
               cloneResubmitFormFields(document.filterform);
               // check to see if should show or hide filter
               // <bean:parameter id="hidefilter" name="hidefilter" value="false" />
               // the param needed with showhidefilter is the opposite of the parameter hidefilter.
               // does not work: showhidefilter(!<bean:write name="hidefilter" />);
             }
             addEvent(window, "load", initThisPage);
             
             function showPressFilterMessage() {
               //shows a message to user to press the filter button
               gebid('nowpressfilter').className="highlight";
             }
             
             function showhidefilter(blnShow) {
               if (blnShow) {
                 gebid('filterrow1').className='filter';
                 gebid('filterrow2').className='filter';
                 gebid('filterrow3').className='hidden';
                 // tell future pages (from pager) to not hide filter.
                // gebid('hidefilterinput').value='false';
               } else {
                 gebid('filterrow1').className='hidden';
                 gebid('filterrow2').className='hidden';
                 gebid('filterrow3').className='filter';
                // gebid('hidefilterinput').value='true';
               }
             }
             function showFootNote()
             {
               gebid('footnote').className='normal';
             }
             
         // -->
    </script>
 
<TITLE>View Your Uploaded Plots</TITLE>
 <%@ include file="includeviews/inlinestyles.jsp" %> 
 @webpage_masthead_html@ 
  @possibly_center@  
<h2>View Your Uploaded Plots</h2>
<p>The following is a list of plots that you have uploaded to VegBank.  Thank you for contributing 
   your work!

       <logic:notPresent parameter="orderBy">
            <!-- set default sorting -->
            <bean:define id="orderBy" value="xorderby_authorplotcode" />
       </logic:notPresent>
       <logic:notPresent parameter="perPage">
            <!-- set default perPage -->
            <bean:define id="perPage" value="40" />
       </logic:notPresent>
       
       
       
       <bean:define id="nofilter" value="false" type="java.lang.String" />
       <!-- nofilter= true means that this view is not filtered -->
       <!-- no filter initially -->
           <!-- need these to filter: 
          "xwhereParams_usr_0"= strWebUserId 
          "xwhereParams_usr_1"=usr_id
         "xwhereKey_usr"=xwhere_equal  -->
       
       
       <logic:equal parameter="where" value="where_simple">
       <logic:equal parameter="xwhereParams_usr_0" value="<%= strWebUserId %>">
         <logic:equal parameter="xwhereParams_usr_1" value="usr_id" >
           <logic:equal parameter="xwhereKey_usr" value="xwhere_eq" >
             <logic:equal parameter="xwhereGlue" value="AND" >
               <bean:define id="nofilter" value="true" type="java.lang.String" />    
             </logic:equal>
           </logic:equal>
         </logic:equal>
       </logic:equal>  
       </logic:equal>  
  <br/>
         <logic:notEqual name="nofilter" value="false">
           <!-- x where ok -->
           Your plots (FILTERED) <a href="@views_link@observation_userloaded.jsp">unfilter</a>
           <vegbank:get id="plot" select="userloadedplots" beanName="map" pager="true" 
            allowOrderBy="true" xwhereEnable="true" />
         </logic:notEqual>
         <logic:equal name="nofilter" value="false">
           <!-- default, get all of a user's -->
           <!-- ALL PLOTS from user: <%= strWebUserId %> -->
           All your plots
           <vegbank:get id="plot" select="userloadedplots" beanName="map" pager="true" 
            allowOrderBy="true" xwhereEnable="false" where="where_usrpk" wparam="<%= strWebUserId %>" />

         </logic:equal>
  </p>


<table cellpadding="2" class="leftrightborders">

<tr>
  <th>Plot Code</th>
  <th>Location Masking</th>
  <th>Embargo</th>
  <th>Embargo Start</th>
  <th>Stop <img src="@images_link@i.gif" border="0" width="24" height="22" id="edit_row_busy_icon" class="busyicon"/></th>
  <th>XML file</th>
  <th>Upload Date</th>
  <th>Project</th>
</tr>
                    
                  
<tr class="filter" id="filterrow1"><!-- filter row: -->
  <form action="#" name="filterform" onsubmit="fillindate('embargoStart');fillindate('embargoStop');fillindate('datasetStart');">
    <!-- setup form -->
    <input type="hidden" name="xwhereGlue" value="AND" />
    <input type="hidden" name="xwhereParams_usr_0" value="<%= strWebUserId %>" />
    <input type="hidden" name="xwhereParams_usr_1" value="usr_id" />
    <input type="hidden" name="xwhereKey_usr" value="xwhere_eq" />
    <input type="hidden" name="where" value="where_simple" />
   <!-- <input type="hidden" name="hidefilter" id="hidefilterinput" value="false" />-->
   
   <td title="Enter a plot code, use % for wildcard character.">
       <input type="text" size="15" name="xwhereParams_plotcode_0" onchange="showPressFilterMessage()" />
       <input type="hidden" name="xwhereParams_plotcode_1" value="authorplotcode" />
       <input type="hidden" name="xwhereKey_plotcode" value="xwhere_ilike" /><br/>
       <span class="sizetiny">Use % for wildcard</span>
  </td>
  <td>
    <select name="xwhereParams_fuzzing_0" onchange="showPressFilterMessage()">
          <option value="">--all--</option>
    <!-- save this for later -->
    <bean:define id="fuzzingoptions">
    <vegbank:get id="fuzzing" select="confidentialitystatuslist" beanName="map" 
      allowOrderBy="false" xwhereEnable="false" where="where_confidentialitystatus_lt" wparam="4"  pager="false" perPage="-1" />
      <logic:notEmpty name="fuzzing-BEANLIST">
        <logic:iterate id="onerowoffuzzing" name="fuzzing-BEANLIST" >
            <option value="<bean:write name='onerowoffuzzing' property='confidentialitystatus' />"><bean:write name='onerowoffuzzing' property='confidentialityshorttext' /></option>
        </logic:iterate>
      </logic:notEmpty>
    </bean:define>  
    <bean:write name="fuzzingoptions" filter="false" />
    </select>
    <input type="hidden" name="xwhereParams_fuzzing_1" value="plot.confidentialitystatus" />
    <input type="hidden" name="xwhereKey_fuzzing" value="xwhere_eq" />
  </td>
  <td>
  
    <select name="xwhereParams_embargo_0" onChange="handleEmbargoKey(this.value); showPressFilterMessage()">
          <option value="">--all--</option>
          <option value="true">no embargo</option>
    <vegbank:get id="embargo" select="confidentialitystatuslist" beanName="map" 
      allowOrderBy="false" xwhereEnable="false" where="where_confidentialitystatus_gt" wparam="5"  pager="false" perPage="-1" />
      <logic:notEmpty name="embargo-BEANLIST">
      <logic:iterate id="onerowofembargo" name="embargo-BEANLIST" >
            <option value="<bean:write name='onerowofembargo' property='confidentialitystatus' />"><bean:write name='onerowofembargo' property='confidentialityshorttext' /></option>
      </logic:iterate>
      </logic:notEmpty >
    </select>
    <input type="hidden" name="xwhereParams_embargo_1" value="plot.embstatusinclexpired" />
    <!-- the following is set by javascript: -->
    <input type="hidden" name="xwhereKey_embargo" value="xwhere_eq" />  

  </td>
  <td>
    <select name="xwhereKey_embargoStart">
      <option value="xwhere_lt">before</option>
      <option value="xwhere_gt">after</option>
    </select>
    <br/>
    <input type="hidden" name="xwhereParams_embargoStart_1" value="embargostart" />
    <input type="hidden" id="date_embargoStart_fulldate" name="xwhereParams_embargoStart_0" />
    <select  onchange="showPressFilterMessage()" title="choose a day" id="date_embargoStart_day" name="date_embargoStart_day">
      <option value=""></option>
      <%@ include file="/includes/options_31days.html" %>
    </select>-<select title="choose a month" id="date_embargoStart_month" name="date_embargoStart_month">
      <option value=""></option>
      <%@ include file="/includes/options_12months.html" %>
    </select>-<input onchange="showPressFilterMessage()" title="enter a year, format YYYY, i.e. 1999" type="text" size="4" id="date_embargoStart_year" name="date_embargoStart_year" /> 
    <br/>
    <span class="sizetiny">Enter day, month AND year, D-M-YYYY</span>
  </td>
  <td>
      <select name="xwhereKey_embargoStop">
        <option value="xwhere_lt">before</option>
        <option value="xwhere_gt">after</option>
      </select>
      <br/>
      <input type="hidden" name="xwhereParams_embargoStop_1" value="embargostop" />
      <input type="hidden" id="date_embargoStop_fulldate" name="xwhereParams_embargoStop_0" />
      <select  onchange="showPressFilterMessage()" title="choose a day" id="date_embargoStop_day" name="date_embargoStop_day" >
        <option value=""></option>
        <%@ include file="/includes/options_31days.html" %>
      </select>-<select title="choose a month" id="date_embargoStop_month" name="date_embargoStop_month" >
        <option value=""></option>
        <%@ include file="/includes/options_12months.html" %>
      </select>-<input onchange="showPressFilterMessage()" title="enter a year, format YYYY, i.e. 1999" type="text" size="4" id="date_embargoStop_year" name="date_embargoStop_year" /> 
      <br/>
      <span class="sizetiny">Enter day, month AND year, D-M-YYYY</span>
  </td>
   <td>
        <select name="xwhereParams_dataset_0"  onchange="showPressFilterMessage()">
          <option value="">--all--</option>
 <vegbank:get id="datasetdistinct" select="userloadedplots_dataset_distinct" where="where_usrpk" wparam="<%= strWebUserId %>" beanName="map" perPage="-1" pager="false" />
          <logic:iterate id="onerowofdatasetdistinct" name="datasetdistinct-BEANLIST" >
            <option value="<bean:write name='onerowofdatasetdistinct' property='userdataset_id' />">
               <logic:notEmpty name="onerowofdatasetdistinct" property="datasetname">
                  <bean:define id="ds_trunc"><bean:write name='onerowofdatasetdistinct' property='datasetname' /></bean:define>
                  <% if ( ds_trunc.length() > 30 ) { ds_trunc = ds_trunc.substring(0,19) + "..." + ds_trunc.substring(ds_trunc.length() -8,ds_trunc.length() ); } %>
                 
                 <%= ds_trunc %>
              </logic:notEmpty>
              
            </option>  
          </logic:iterate>
        </select>
        <input type="hidden" name="xwhereParams_dataset_1" value="userdataset.userdataset_id" />
        <input type="hidden" name="xwhereKey_dataset" value="xwhere_eq" />
  </td>
  <td>
     <select name="xwhereKey_datasetStart">
            <option value="xwhere_lt">before</option>
            <option value="xwhere_gt">after</option>
          </select>
          <br/>
          <input type="hidden" name="xwhereParams_datasetStart_1" value="datasetstart" />
          <input type="hidden" id="date_datasetStart_fulldate" name="xwhereParams_datasetStart_0" />
          <select  onchange="showPressFilterMessage()" title="choose a day" id="date_datasetStart_day" name="date_datasetStart_day" >
            <option value=""></option>
            <%@ include file="/includes/options_31days.html" %>
          </select>-<select title="choose a month" id="date_datasetStart_month" name="date_datasetStart_month" >
            <option value=""></option>
            <%@ include file="/includes/options_12months.html" %>
          </select>-<input onchange="showPressFilterMessage()" title="enter a year, format YYYY, i.e. 1999" type="text" size="4" id="date_datasetStart_year" name="date_datasetStart_year" /> 
          <br/>
      <span class="sizetiny">Enter day, month AND year, D-M-YYYY</span>
  </td>
  <td>
          <select name="xwhereParams_project_0" onchange="showPressFilterMessage()">
            <option value="">--all--</option>
   <vegbank:get id="projectdistinct" select="userloadedplots_project_distinct" where="where_usrpk" wparam="<%= strWebUserId %>" beanName="map" perPage="-1" pager="false" />
            <logic:iterate id="onerowofprojectdistinct" name="projectdistinct-BEANLIST" >
              <option value="<bean:write name='onerowofprojectdistinct' property='project_id' />">
                
                 <logic:notEmpty name="onerowofdatasetdistinct" property="datasetname">
                                  <bean:define id="proj_trunc"><bean:write name='onerowofprojectdistinct' property='project_id_transl' /></bean:define>
                                  <% if ( proj_trunc.length() > 30 ) { proj_trunc = proj_trunc.substring(0,27) + "..." ; } %>
                                 
                                 <%= proj_trunc %>
                </logic:notEmpty>
              </option>  
            </logic:iterate>
          </select>
          <input type="hidden" name="xwhereParams_project_1" value="project_id" />
          <input type="hidden" name="xwhereKey_project" value="xwhere_eq" />
  </td>
  </tr>
  
  <tr class="filter" id="filterrow2">
  <td colspan="19" nowrap="nowrap">
  <input type="submit" value="filter" /> <span id="nowpressfilter" class="hidden"><strong>&laquo;Press filter when finished entering criteria</strong></span>-- <input type="reset" value="reset form" />
  -- <logic:equal name="nofilter" value="true"><a href="@views_link@observation_userloaded.jsp">show all (unfilter)</a></logic:equal>
  </form> <br/>
   
    <span class="sizetiny"><a href="#" onclick="showhidefilter(false)">&lt;&lt;hide filter rows</a></span>
    <span class="instructions"> Use the picklists and fields in the yellow rows to filter the list of your plots.  Use % for the wildcard in text boxes,
    such as "Plot Code" (example: <code>GRSM%</code>).  Then press the "filter" button. </span>
  </td>
</tr>   
<tr class="hidden" id="filterrow3">
  <td colspan="19" class="sizetiny"><a href="#" onclick="showhidefilter(true);">&gt;&gt;Show Filtering options...</a> Filtering is hidden</td>
</tr>

<logic:empty name="plot-BEANLIST">
  <tr><td colspan="19">  Sorry, no plots found.  
   <% if ( strWebUserId == "-1" ) {  %>  
           <strong>You are not logged in. </strong>
           <a href="@general_link@login.jsp">Login Here.</a> 
      <% } else {
       %> 
       <logic:notEqual name="nofilter" value="true">
         You have not submitted any plots to VegBank.
       </logic:notEqual>  
       <%
      }%>
 </td></tr>
</logic:empty>

<logic:notEmpty name="plot-BEANLIST">
<logic:iterate name="plot-BEANLIST" id="onerowofplot">
<bean:define property="observation_id" name="onerowofplot" id="observation_pk"/>
<tr class="@nextcolorclass@">

  <td class="smallfield"><a href="@get_link@comprehensive/observation/<bean:write name='onerowofplot' property='observation_id' />">
    <bean:write name='onerowofplot' property='authorplotcode' /></a>
  </td>
  <td><!-- only show values that are applicable to fuzzing-->
    <logic:lessEqual name='onerowofplot' property='confidentialitystatus' value='3'>
          <span id="confidplot<bean:write name='onerowofplot' property='plot_id' />">
          <bean:write name='onerowofplot' property='confidentialitystatus_transl' /> 
          <!-- this is a link that shows the picklist to edit this confidentiality, and hides the original value -->
            <span class="sizetiny">
              <a href="#" 
                onclick="gebid('editconfidplot<bean:write name='onerowofplot' property='plot_id' />select').value='<bean:write name='onerowofplot' property='confidentialitystatus' />';gebid('editconfidplot<bean:write name='onerowofplot' property='plot_id' />').className='sizesmall';gebid('confidplot<bean:write name='onerowofplot' property='plot_id' />').className='hidden';return false;">
                edit...</a>
            </span>
          </span>
          <span class="hidden" id="editconfidplot<bean:write name='onerowofplot' property='plot_id' />">
            edit: 
            <select id="editconfidplot<bean:write name='onerowofplot' property='plot_id' />select" onchange="updatePlotLocationFuzzing(<bean:write name='onerowofplot' property='plot_id' />,this.value)">
                  
                <!-- these saved from above -->
                <bean:write name="fuzzingoptions" filter="false" />
            </select>
          </span>
          <br/>
          <span class="sizesmall" id="plot<bean:write name='onerowofplot' property='plot_id' />latlong">lat:<bean:write name='onerowofplot' property='latitude' /><br/>
          long:<bean:write name='onerowofplot' property='longitude' /></span><br/>
          
    </logic:lessEqual>
  </td>
  <td><!-- only show embargo values (456) -->
    <logic:greaterThan name='onerowofplot' property='embstatusinclexpired' value='3'>
      <bean:write name='onerowofplot' property='embstatusinclexpired_transl' />
    </logic:greaterThan>
  </td>
  <td>
    <logic:notEmpty name="onerowofplot" property="embargostart">
    <span title="<bean:write name='onerowofplot' property='embargostart' />">
      <dt:format pattern="dd-MMM-yyyy">
        <dt:parse pattern="yyyy-MM-dd">
          <bean:write name='onerowofplot' property='embargostart_datetrunc' />
        </dt:parse>
      </dt:format>
    </span>
    </logic:notEmpty>
  </td>
  <td>
    <logic:notEmpty name="onerowofplot" property="embargostop">
    <span id="embargostop_<bean:write name='onerowofplot' property='embargo_id' />" title="<bean:write name='onerowofplot' property='embargostop' />">
      <dt:format pattern="dd-MMM-yyyy">
        <dt:parse pattern="yyyy-MM-dd">
          <bean:write name='onerowofplot' property='embargostop_datetrunc' />
        </dt:parse>
      </dt:format>
    </span> <br/>
    <input type="button" value="renew 3 yrs" onclick="adjustEmbargo(<bean:write name='onerowofplot' property='embargo_id'/>,true);gebid('embargostop_'+<bean:write name='onerowofplot' property='embargo_id'/>).innerHTML='RENEWED <a href=#footnote>*</a>';showFootNote();" />
    <input type="button" value="cancel embargo" onclick="if (confirm('Are you sure you want to end the embargo on this plot, making it visible to all VegBank users?  Press OK to continue.')) {adjustEmbargo(<bean:write name='onerowofplot' property='embargo_id'/>,false,true);gebid('embargostop_'+<bean:write name='onerowofplot' property='embargo_id'/>).innerHTML='CANCELLED<a href=#footnote>*</a>';showFootNote();}" />
    </logic:notEmpty>
  </td>
  <td class="smallfield">
    <!-- here if the name is longer than 40 chars and has no spaces or dashes, split into parts -->
               
               <logic:notEmpty name="onerowofplot" property="datasetname">
                  <bean:define id="oneds_trunc"><bean:write name='onerowofplot' property='datasetname' /></bean:define>
                  <% if ( oneds_trunc.length() > 40 ) 
                    { %><!-- gt 40 -->
                      <% for (int i=1; i*40 < (oneds_trunc.length() ) ; i++)
                      {
                      %> <!-- split -->
                      <%  oneds_trunc = oneds_trunc.substring(0,(i*40)) + " " + oneds_trunc.substring((40*i)); 
                      }
                    } %>
                     
                  <%= oneds_trunc %>
                </logic:notEmpty>
                
  </td>
  <td>
    <span title="<bean:write name='onerowofplot' property='datasetstart' />">
      <dt:format pattern="dd-MMM-yyyy">
        <dt:parse pattern="yyyy-MM-dd">
          <bean:write name='onerowofplot' property='datasetstart_datetrunc' />
        </dt:parse>
      </dt:format>
    </span>  
  </td>
  <td class="largefield"><bean:write name='onerowofplot' property='project_id_transl' /></td>
  
      
     </tr>

</logic:iterate>
</logic:notEmpty>
</table>
<div id="footnote" class="hidden">
  <span class="sizetiny"><a name="footnote">*</a> Once a plot's embargo is renewed or cancelled,
    VegBank requires about one minute to apply the embargo.  If you
    just cancelled an embargo, it will be viewable online soon.
  
  </span></div>
<br/>
<vegbank:pager/>


          @webpage_footer_html@
