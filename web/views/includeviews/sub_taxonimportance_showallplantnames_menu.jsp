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
Change plant label: <a class="image" href="@help-for-plantnames-href@"><img border="0" src="@image_server@question.gif" /></a> <br/>
<select onChange="showTaxonName(this.value,'<bean:write name="showTaxonNameDivID" />')">
                             <option value="" >--Choose a value--</option>
       <option value="taxonobservation_authorplantname" <logic:equal name="plantNamesToShowBean" value="taxonobservation_authorplantname"> selected="selected" </logic:equal>>Author's plant name</option>
<option value="taxonobservation_int_origplantscifull" <logic:equal name="plantNamesToShowBean"       value="taxonobservation_int_origplantscifull">           selected="selected" </logic:equal>>Original Interpretation, full Scientific Name</option>
<option value="taxonobservation_int_origplantscinamenoauth" <logic:equal name="plantNamesToShowBean" value="taxonobservation_int_origplantscinamenoauth">         selected="selected" </logic:equal>>Original Interpretation, Scientific Name without authors</option>
<option value="taxonobservation_int_origplantcode" <logic:equal name="plantNamesToShowBean"          value="taxonobservation_int_origplantcode">  selected="selected" </logic:equal>>Original Interpretation, USDA Code</option>
<option value="taxonobservation_int_origplantcommon" <logic:equal name="plantNamesToShowBean"        value="taxonobservation_int_origplantcommon">    selected="selected" </logic:equal>>Original Interpretation, Common Name</option>
<option value="taxonobservation_int_currplantscifull" <logic:equal name="plantNamesToShowBean"       value="taxonobservation_int_currplantscifull">           selected="selected" </logic:equal>>Current Interpretation, full Scientific Name</option>
<option value="taxonobservation_int_currplantscinamenoauth" <logic:equal name="plantNamesToShowBean" value="taxonobservation_int_currplantscinamenoauth">         selected="selected" </logic:equal>>Current Interpretation, Scientific Name without authors</option>
<option value="taxonobservation_int_currplantcode" <logic:equal name="plantNamesToShowBean"          value="taxonobservation_int_currplantcode">  selected="selected" </logic:equal>>Current Interpretation, USDA Code</option>
<option value="taxonobservation_int_currplantcommon" <logic:equal name="plantNamesToShowBean"        value="taxonobservation_int_currplantcommon">    selected="selected" </logic:equal>>Current Interpretation, Common Name</option>
</select>

