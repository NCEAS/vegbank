<!-- show what kind of name? -->
Change plant label: <a href="@help-for-plantnames-href@"><img border="0" src="@image_server@question.gif" /></a> <br/>
<select id="taxonNameSelect" onChange="showTaxonName(this.value,'taxonObservationof<bean:write name='observation_pk' />')">
  <option value="taxobs_authorplantname">Author's plant name</option>
  <option value="taxobs_orig_scinamewithauth">Original Interpretation, full Scientific Name</option>
    <option value="taxobs_orig_scinamenoauth">Original Interpretation, Scientific Name without authors</option>
     <option value="taxobs_orig_code">Original Interpretation, USDA Code</option>
   <option value="taxobs_orig_common">Original Interpretation, Common Name</option>
  <option value="taxobs_curr_scinamewithauth">Current Interpretation, full Scientific Name</option>
    <option value="taxobs_curr_scinamenoauth" selected="selected">Current Interpretation, Scientific Name without authors</option>
             <option value="taxobs_curr_code">Current Interpretation, USDA Code</option>
           <option value="taxobs_curr_common">Current Interpretation, Common Name</option>
</select>
