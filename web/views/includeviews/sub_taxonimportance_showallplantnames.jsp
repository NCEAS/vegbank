<!-- shows all names associated with taxonObs, hide and show as appropriate -->
<!-- link to taxonObs view -->

 
         <td class="taxobs_authorplantname largefield"><a href="@get_link@std/taxonobservation/<bean:write name='onerowoftaxonimportance' property='taxonobservation_id' />">
                          <bean:write name='onerowoftaxonimportance' property='authorplantname' /> </a></td>
         <td class="taxobs_orig_scinamewithauth largefield"><a href="@get_link@std/taxonobservation/<bean:write name='onerowoftaxonimportance' property='taxonobservation_id' />">
                          <logic:empty name='onerowoftaxonimportance' property='int_origplantsciname' >
                             <bean:write name='onerowoftaxonimportance' property='authorplantname' />**
                          </logic:empty>
                          <bean:write name='onerowoftaxonimportance' property='int_origplantsciname' /> </a></td>
         <td class="taxobs_orig_scinamenoauth largefield"><a href="@get_link@std/taxonobservation/<bean:write name='onerowoftaxonimportance' property='taxonobservation_id' />">
                          <logic:empty name='onerowoftaxonimportance' property='int_origplantscinamenoauth' >
                             <bean:write name='onerowoftaxonimportance' property='authorplantname' />**
                          </logic:empty>

                          <bean:write name='onerowoftaxonimportance' property='int_origplantscinamenoauth' /> </a></td>
         <td class="taxobs_orig_code largefield"><a href="@get_link@std/taxonobservation/<bean:write name='onerowoftaxonimportance' property='taxonobservation_id' />">
                          <logic:empty name='onerowoftaxonimportance' property='int_origplantcode' >
                             <bean:write name='onerowoftaxonimportance' property='authorplantname' />**
                          </logic:empty>

                          <bean:write name='onerowoftaxonimportance' property='int_origplantcode' /> </a></td>
         <td class="taxobs_orig_common largefield"><a href="@get_link@std/taxonobservation/<bean:write name='onerowoftaxonimportance' property='taxonobservation_id' />">
                          <logic:empty name='onerowoftaxonimportance' property='int_origplantcommon' >
                             <bean:write name='onerowoftaxonimportance' property='authorplantname' />**
                          </logic:empty>

                          <bean:write name='onerowoftaxonimportance' property='int_origplantcommon' /> </a></td>
         
         <td class="taxobs_curr_scinamewithauth largefield"><a href="@get_link@std/taxonobservation/<bean:write name='onerowoftaxonimportance' property='taxonobservation_id' />">
                          <logic:empty name='onerowoftaxonimportance' property='int_currplantsciname' >
                             <bean:write name='onerowoftaxonimportance' property='authorplantname' />**
                          </logic:empty>

                          <bean:write name='onerowoftaxonimportance' property='int_currplantsciname' /> </a></td>
         <td class="taxobs_curr_scinamenoauth largefield"><a href="@get_link@std/taxonobservation/<bean:write name='onerowoftaxonimportance' property='taxonobservation_id' />">
                          <logic:empty name='onerowoftaxonimportance' property='int_currplantscinamenoauth' >
                             <bean:write name='onerowoftaxonimportance' property='authorplantname' />**
                          </logic:empty>

                          <bean:write name='onerowoftaxonimportance' property='int_currplantscinamenoauth' /> </a></td>
         <td class="taxobs_curr_code largefield"><a href="@get_link@std/taxonobservation/<bean:write name='onerowoftaxonimportance' property='taxonobservation_id' />">
                          <logic:empty name='onerowoftaxonimportance' property='int_currplantcode' >
                             <bean:write name='onerowoftaxonimportance' property='authorplantname' />**
                          </logic:empty>
                          <bean:write name='onerowoftaxonimportance' property='int_currplantcode' /> </a></td>
         <td class="taxobs_curr_common largefield"><a href="@get_link@std/taxonobservation/<bean:write name='onerowoftaxonimportance' property='taxonobservation_id' />">
                          <logic:empty name='onerowoftaxonimportance' property='int_currplantcommon' >
                             <bean:write name='onerowoftaxonimportance' property='authorplantname' />**
                          </logic:empty>
                          <bean:write name='onerowoftaxonimportance' property='int_currplantcommon' /> </a></td>
         


 



 
 
