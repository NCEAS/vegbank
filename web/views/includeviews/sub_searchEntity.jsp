
<!-- expects two beans to be defined: 
     #1 entityToSearch     with lowercase name of what to search for:
     optional: #2 SearchInstructions are examples, etc.  
     optional: #3 NameOfEntityToPresent is a nicely formatted name of what we're searching for: i.e. Stratum Method-->
<!-- ############################################### -->
<!--            start of search box                  --> 
<!-- ############################################### -->
<!-- % String searchString = request.getParameter("xwhereParams"); % -->
<!-- collect previous info on <bean:write name="entityToSearch" />: -->
<logic:present name="alternateSearchInputs">
  <bean:parameter id="beansearchString" value="" name="xwhereParams_custom_0" />
</logic:present>
<logic:notPresent name="alternateSearchInputs">
  <bean:parameter id="beansearchString" value="" name="xwhereParams" />
</logic:notPresent>

<bean:parameter id="beanxwhereMatchAny" value="" name="xwhereMatchAny" />
<bean:parameter id="beanxwhereMatchWholeWords" value="" name="xwhereMatchWholeWords"/>

<logic:notPresent name="NameOfEntityToPresent">
  <!-- define presentation name as same as entity if not defined -->
  <bean:define id="NameOfEntityToPresent"><bean:write name="entityToSearch" /></bean:define>
</logic:notPresent>

<form action="@views_link@<bean:write name='entityToSearch' />_summary.jsp" method="get">
    <table cellpadding="0" cellspacing="0" border="0" bgcolor="#DDDDDD">
    <tr>
        <td><img src="@image_server@uplt3.gif"/></td>
        <td></td>
        <td><img src="@image_server@uprt3.gif"/></td>
    </tr>

    <tr>
        <td></td>
        <td><span id="search<bean:write name='entityToSearch' />Shown">
            <!-- link to hide this: -->
            
            <logic:present name="alternateSearchInputs">
              <!-- this allows calling page to change this by specifying inputs to use here, typically: -->
             <!-- xwhereKey=xwhere_match  -->
             <!-- where=where_simple -->
             <!-- xwhereParams_custom_1=projectname --> 
             
             <!-- xwhereParams_custom_0=alvar (this is specified BELOW, but IS NEEDED -->
             <bean:write name="alternateSearchInputs" filter="false" />
            </logic:present>
            <logic:notPresent name="alternateSearchInputs">
                <!-- typical settings:-->
                <input type="hidden" name="xwhereKey" value="xwhere_kw_match" />
                <input type="hidden" name="where" value="where_keywords_pk_in" />
                <input type="hidden" name="wparam" value="<bean:write name="entityToSearch" />__<bean:write name="entityToSearch" />" />
                <!-- end of typical settings-->
            </logic:notPresent>    
            
            
            <input type="hidden" name="clearSearch" value="" />
            <input type="hidden" name="xwhereSearch" value="true" />
            <span class="greytext">
            &nbsp; Search for a <bean:write name="NameOfEntityToPresent" /> <bean:write name="SearchInstructions" ignore="true" />:
            &nbsp;&nbsp;
              <a href="#" onclick="showorhidediv('search<bean:write name="entityToSearch" />Shown');showorhidediv('search<bean:write name="entityToSearch" />Hidden');return false;">
                &lt;&lt;Hide search
              </a>
            </span>
            <br />
              <!-- alt search suffix used to add "_name_0" to this input -->
             <input type="text" name="xwhereParams<logic:present name='alternateSearchInputs'>_custom_0</logic:present>" size="30" value="<bean:write name='beansearchString' />"/>
             <input type="hidden" name="hop_params" value="xwhereParams<logic:present name='alternateSearchInputs'>_custom_0</logic:present>" />
             <html:submit value="search" />
             
             <!-- link to help about searching -->
             <a href="@help-for-searching-href@"><img height="14" width="14" border="0" src="@image_server@question.gif" /></a>
       
       <!--</td>
        </tr>
        <tr><td></td><td align="right"> -->
        
        <div align="right">
             <input type="checkbox" name="xwhereMatchAny" <logic:equal value='on' name='beanxwhereMatchAny' >checked="checked" </logic:equal> />
                Match any word
        </div>
       
        <!--
        </td>
        <td></td>
    </tr>
        <tr><td></td><td align="right">
        -->
        <div align="right">
        <!-- check this box if this search is a wparam search and it was checked before, or if it isn't a search: -->
              <input type="checkbox" 
                     name="xwhereMatchWholeWords" 
                     <logic:empty  name='beansearchString' > checked="checked" </logic:empty> 
                     <logic:equal value='on' name='beanxwhereMatchWholeWords' >checked="checked" </logic:equal>  
                />
                Match whole words only
          </div>
       <!-- end of shown search span --></span>
       
       <span id="search<bean:write name="entityToSearch" />Hidden" style="display:none" class="greytext">
           <a href="#" onclick="showorhidediv('search<bean:write name="entityToSearch" />Shown');showorhidediv('search<bean:write name="entityToSearch" />Hidden');return false;">
             Search for <bean:write name="NameOfEntityToPresent" /> &gt;&gt;
           </a>
        </span></td>
            <td></td> 
            
    </tr>
    <tr>
        <td><img src="@image_server@lwlt3.gif"/></td>
        <td></td>
        <td><img src="@image_server@lwrt3.gif"/></td>
    </tr>
    
    </table>
</form>
<br />
<!-- ############################################### -->
<!--            end of search box                    --> 
<!-- ############################################### -->