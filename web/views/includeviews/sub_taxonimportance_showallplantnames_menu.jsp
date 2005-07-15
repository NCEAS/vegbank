<!-- show what kind of name? -->

<!-- bean should be defined earlier that tells us what kind of name this is: -->
<!--bean:write name="plantNamesToShowBean" /-->
<logic:notPresent name="plantNamesToShowBean" >
   <bean:define id="plantNamesToShowBean" value="n/a" /> <!-- default value to set, NEEDED IN RAW VIEWS -->
</logic:notPresent>
<logic:present name="inrawview">
  <bean:define id="plantNamesToShowBean" value="n/a" /> <!-- in a raw view, dont use the cookie about taxon name -->
</logic:present>
<logic:notPresent name="showTaxonNameDivID">
  <bean:define id="showTaxonNameDivID">*</bean:define> <!-- default of everything: SHOULD NOT BE USED -->
</logic:notPresent>
Change plant label: <a href="@help-for-plantnames-href@"><img border="0" src="@image_server@question.gif" /></a> <br/>
<select onChange="showTaxonName(this.value,'<bean:write name="showTaxonNameDivID" />')">
                             <option value="" >--Choose a value--</option>
       <option value="taxobs_authorplantname" <logic:equal name="plantNamesToShowBean" value="taxobs_authorplantname"> selected="selected" </logic:equal>>Author's plant name</option>
  <option value="taxobs_orig_scinamewithauth" <logic:equal name="plantNamesToShowBean" value="taxobs_orig_scinamewithauth"> selected="selected" </logic:equal>>Original Interpretation, full Scientific Name</option>
    <option value="taxobs_orig_scinamenoauth" <logic:equal name="plantNamesToShowBean" value="taxobs_orig_scinamenoauth"> selected="selected" </logic:equal>>Original Interpretation, Scientific Name without authors</option>
             <option value="taxobs_orig_code" <logic:equal name="plantNamesToShowBean" value="taxobs_orig_code"> selected="selected" </logic:equal>>Original Interpretation, USDA Code</option>
           <option value="taxobs_orig_common" <logic:equal name="plantNamesToShowBean" value="taxobs_orig_common"> selected="selected" </logic:equal>>Original Interpretation, Common Name</option>
  <option value="taxobs_curr_scinamewithauth" <logic:equal name="plantNamesToShowBean" value="taxobs_curr_scinamewithauth"> selected="selected" </logic:equal>>Current Interpretation, full Scientific Name</option>
    <option value="taxobs_curr_scinamenoauth" <logic:equal name="plantNamesToShowBean" value="taxobs_curr_scinamenoauth"> selected="selected" </logic:equal>>Current Interpretation, Scientific Name without authors</option>
             <option value="taxobs_curr_code" <logic:equal name="plantNamesToShowBean" value="taxobs_curr_code"> selected="selected" </logic:equal>>Current Interpretation, USDA Code</option>
           <option value="taxobs_curr_common" <logic:equal name="plantNamesToShowBean" value="taxobs_curr_common"> selected="selected" </logic:equal>>Current Interpretation, Common Name</option>
</select>

