@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  <%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
  @webpage_head_html@
<bean:parameter id="obsId" name="obsId"/>

<!--script type="text/javascript" src="@includes_link@prototype.js"></script-->

<script language="javascript">
var interpCount = 0;

function toggleRecordDisplayByEvent(evt) {
    var link = getEventElement(evt);
    toggleRecordDisplay(link);
}
                                                                                                                                                                              
function toggleRecordDisplay(link) {
    var sibling = findSibling(link.parentNode.nextSibling, "DIV", 15);
                                                                                                                                                                              
    if (sibling.style.display == "none") {
        showRecord(sibling, link);
    } else {
        hideRecord(sibling, link);
    }
}
                                                                                                                                                                              
function showRecord(record, link) {
    show(record);
    link.innerHTML = "hide";
}

function hideRecord(record, link) {
    hide(record);
    link.innerHTML = "show";
}

function deleteRecord(evt) {
    var link = getEventElement(evt);
    var sibling = link.parentNode.parentNode;
    var parent = link.parentNode.parentNode.parentNode;
    parent.removeChild(sibling);

    // TODO: find a smart way to re-index the comminterps
    // maybe push this interp's index to an open index queue
    interpCount--;
}

function lookupComm(evt) {
    var link = getEventElement(evt);
    var ac = findSibling(link, "span", 10);
    window.open('@cite_link@' + ac.innerHTML, '', 'width=810,height=600,location,status,scrollbars,toolbar,resizable');
}

function findSibling(node, nodeName, max) {
    var sibling = node.nextSibling;
    var count = 0;
    while (sibling != null && sibling.nodeName.toLowerCase() != nodeName.toLowerCase() && count < max) {
        sibling = sibling.nextSibling;
        count++;
    }
    return sibling;
}
                                                                                                                                                                              

function show(elem) { elem.style.display = 'block'; }
function hide(elem) { elem.style.display = 'none'; }

//
// Hides the add_form.
//
function closeAddForm() { 
    hide(gebid('add_form'));
    show(gebid("add_another_link"));
}

//
// Displays the add_form for adding records.
//
function openAddForm() { 
    show(gebid('add_form'));
    hide(gebid("add_another_link"));
}

//
// Resets the add_form.
//
function resetAddForm() { 
    gebid("af_rec_id").value = "";
    gebid("af_commconcept_ac").value = "";
    gebid("af_classfit").selectedIndex = 0;
    gebid("af_classconfidence").selectedIndex = 0;
    gebid("af_commauthority_id").selectedIndex = 0;
    gebid("af_type").value = false;
    gebid("af_type").checked = false;
    gebid("af_nomenclaturaltype").checked = false;
    gebid("af_nomenclaturaltype").value = false;
    gebid("af_notes").value = "";
}

//
// Copies add_form values to a newly inserted record_template.
//
function saveForm() {
    var addForm = gebid('add_form');
    var isNew = false;
    var saveRecord = null;

    var recId = gebid('af_rec_id').value;
    //alert("af_rec_id is a " + recId);

    var recTitleSpan;
    var title = gebid("af_commconcept_ac").value;

    // is this new or and edit?
    if (recId == null || recId == "") {
        recId = "rec" + interpCount;
        isNew = true;
        saveRecord = gebid('record_template').cloneNode(true);
        saveRecord.setAttribute("id", "rec" + interpCount);
        recTitleSpan = saveRecord.getElementsByTagName("span")[0].getElementsByTagName("span")[0];
        recTitleSpan.setAttribute("id", "rec" +  interpCount + "_title");
    } else {
        isNew = false;
        saveRecord = gebid(recId);
        recTitleSpan = saveRecord.getElementsByTagName("span")[0].getElementsByTagName("span")[0];
        //alert("saving old record '" + recId + "': " + saveRecord);
    }

    recTitleSpan.innerHTML = title;

    // set IDs for fields in table
    var table = saveRecord.getElementsByTagName("div")[0].getElementsByTagName("table")[0];
    var rows = table.getElementsByTagName("tr");

    for (var i=0; i<rows.length; i++) {
        var destField = rows.item(i).getElementsByTagName("input")[0];
        //alert("input: " + destField + " name: " + destField.getAttribute("name"));
        var tmpId = destField.getAttribute("id");

        if (isNew) {
            //alert("Changing " + tmpId + " to rec"+ interpCount + "_"  + tmpId);
            destField.setAttribute("id", "rec" + interpCount + "_" + tmpId);
        } else {
            tmpId = tmpId.substring(tmpId.indexOf("_") + 1);
            //alert("getting af_" + tmpId);
        }

        var srcField = gebid("af_" + tmpId);

        if (srcField.nodeName == "SELECT") {
            // get the ref name for display
            destField.value = srcField.value;
            var refIndex = srcField.selectedIndex;
            var tmpRef = srcField.options.item(refIndex);
            destField.nextSibling.innerHTML = tmpRef.text;

        } else if (tmpId == "type" || tmpId == "nomenclaturaltype") {
            // checkbox
            if (srcField.checked) {
                destField.value = true;
                destField.nextSibling.innerHTML = "yes";
            } else {
                destField.value = false;
                destField.nextSibling.innerHTML = "no";
            }
        } else {
            destField.value = srcField.value;
            destField.nextSibling.innerHTML = destField.value;
        }

        var fieldIndex = recId.substring("rec".length);
        //alert("adding to index " + fieldIndex);
        if (tmpId == "commconcept_ac") {
            destField.setAttribute("name", "commconcept_ac[" + fieldIndex + "]");
        } else {
            destField.setAttribute("name", "comminterp[" + fieldIndex + "]." + tmpId);
        }
    }


    if (isNew) {
        // slap the new record on the end
        gebid('record_container').appendChild(saveRecord);
        show(saveRecord);
        interpCount++;
    } else {
        // reveal the old record
        //showRecord(saveRecord);
    }

}

function editRecord(evt) {
    var link = getEventElement(evt);
    var thisRecord = link.parentNode.parentNode;
    var recId = thisRecord.getAttribute("id").substring("rec".length);
    var addForm = gebid('add_form');
    var recordDiv = findSibling(link.parentNode.nextSibling, "DIV", 15);
                                                                                                                                                              
    //hideRecord(recordDiv, findSibling(link.nextSibling, "A", 10));
    var prefix = "rec" + recId;
    gebid("af_rec_id").value = prefix;
    gebid("af_commconcept_ac").value = gebid(prefix + "_commconcept_ac").value;
    gebid("af_classfit").value = gebid(prefix + "_classfit").value;
    gebid("af_classconfidence").value = gebid(prefix + "_classconfidence").value;
    gebid("af_commauthority_id").value = gebid(prefix + "_commauthority_id").value;
    gebid("af_type").value = gebid(prefix + "_type").value;
    gebid("af_nomenclaturaltype").value = gebid(prefix + "_nomenclaturaltype").value;
    gebid("af_notes").value = gebid(prefix + "_notes").value;

    openAddForm();
}

function popupAddReference() {
	window.open('@forms_link@AddReference.jsp', '', 'width=810,height=600,location,status,scrollbars,toolbar,resizable');
}

function updateCommTitle(name, input) {
    gebid(name + "-title").innerHTML = input.value;

}

function popupCommLookup(thisFormName,thisCtlName) {
	window.open('@forms_link@CommQuery.jsp?requestingForm=' + thisFormName + '&requestingField=' + thisCtlName, '', 'width=810,height=600,location,status,scrollbars,toolbar,resizable');
}

function popupObs() {
	window.open('@server_address@@get_link@detail/observation/<bean:write name="obsId"/>', '', 'width=810,height=600,location,status,scrollbars,toolbar,resizable');
}

function getHelpPageId() {
  return "interpret-taxon-on-plot";
}
</script>
<title>Interpret Plot</title>
 
  
  @webpage_masthead_html@ 
  
  <h2>Interpret Plot</h2>
<logic:messagesPresent message="false">
<table border="0" width="70%"><tr><td>
<h3><font color="red">Please Try Again</font></h3>
	<ul>
	<html:messages id="error" message="false">
		<li><bean:write name="error"/></li>
	</html:messages>
	</ul>
	<hr/>
</td></tr></table>
<br/>
</logic:messagesPresent>
<logic:messagesPresent property="saved" message="true">
<table border="0"><tr><td>
	<ul>
	<html:messages id="msg" property="saved" message="true">
		<li><bean:write name="msg"/></li>
	</html:messages>
	</ul>
	<hr/>
</td></tr></table>
<br/>
</logic:messagesPresent>



<vegbank:get id="plotobs" select="plotandobservation" where="where_observation_pk" 
    beanName="map" pager="false" wparam="obsId"/>
<vegbank:get id="reference" select="reference_min"/>

<script language="javascript">
</script>


<html:form action="/SaveCommInterpretation.do" method="get">
<bean:parameter id="pObsId" name="obsId" value="0"/>


<table class="thinlines">
<tr bgcolor="#666666"><th colspan="2">What:</th></tr>
<tr><td class="listhead">Plot</td><td class="item"><bean:write name="plotobs-BEAN" property="authorobscode" />
<html:hidden value="<%= pObsId %>" property="obsId"/>

<a href="javascript:popupObs()">See plot</a></td></tr>
    

<!--tr><td>You are:</td><td class="item"><input text disabled="true" value="Joe Schmoe"/></td></tr-->

<tr><th bgcolor="#666666" colspan="2">Method of Classification:</th></tr>
<tr><td class="listhead">Inspection</td><td class="item"><html:checkbox property="commclass.inspection" value="true" /></td></tr>
<tr><td class="listhead">Tabular Analysis</td><td class="item"><html:checkbox property="commclass.tableanalysis" value="true" /></td></tr>
<tr><td class="listhead">Multivariate Analysis</td><td class="item"><html:checkbox property="commclass.multivariateanalysis" value="true" /></td></tr>
<tr><td class="listhead">Expert System (desc. in notes)</td><td class="item"><html:checkbox property="commclass.expertsystem" value="true" /></td></tr>
<tr><td class="listhead">Classification publication</td>
    <td class="item">
    <html:select property="commclass.classpublication_id">
            <option value="" selected="selected">--none--</option>
        <logic:iterate id="ref" name="BEANLIST">
            <option value='<bean:write name="ref" property="reference_id"/>'><bean:write name="ref" property="reference_id_transl"/></option>
        </logic:iterate>
        </html:select>
        <!--br/><a href="javascript:popupAddReference()">Add reference</a--></td></tr>

<tr><td class="listhead">Notes</td><td class="item"><html:textarea property="commclass.classnotes" rows="4" cols="45"></html:textarea></td></tr>
<!--tr><th bgcolor="#666666" colspan="2">Communities to which this plot belongs:</th></tr-->
</table>

<h3>Choose communities to which this plot belongs</h3>
<!-- DHTML adds and removes these community interpretation entries -->
<!-- STATIC INTERP TEMPLATE -->
<div id="record_container">
<!-- all records go here -->
</div>

<!-- BEGIN ADD FORM -->
<div id="add_form" class="block" style="margin-top: 10px">
    <a named="add_form_anchor" id="add_form_anchor" />
    <input id="af_rec_id" type="hidden" name="tmp_rec_id" />

    <table class="thinlines">
    <tr><td class="listhead2">Community accession code</td>
        <td class="item"><input id="af_commconcept_ac" type="text" name="tmp_commconcept_ac" size="28"/>
        <input type="button" onclick="popupCommLookup(this.form.name,'tmp_commconcept_ac')" value="Lookup" /></td></tr>

    <tr><td class="listhead2">Class fit</td>
        <td class="item"><select id="af_classfit" name="tmp_classfit">
            <option value="" selected="selected">--none--</option>
            @VB_INSERT_CLOSEDLIST_comminterpretation.classfit@</select></td></tr>

    <tr><td class="listhead2">Class confidence</td>
        <td class="item"><select id="af_classconfidence" name="tmp_classconfidence">
            <option value="" selected="selected">--none--</option>
            @VB_INSERT_CLOSEDLIST_comminterpretation.classconfidence@</select></td></tr>


    <tr><td class="listhead2">Reference</td>
        <td class="item">
        <select id="af_commauthority_id" name="tmp_commauthority_id">
            <option value="" selected="selected">--none--</option>
        <logic:iterate id="ref" name="BEANLIST">
            <option value='<bean:write name="ref" property="reference_id"/>'><bean:write name="ref" property="reference_id_transl"/></option>
        </logic:iterate>
        </select>
        <!--br/><a href="javascript:popupAddReference()">Add reference</a--></td></tr>

    <tr><td class="listhead2">This is a typal plot of this community?</td>
        <td class="item"><input type="checkbox" id="af_type" name="tmp_type" value="true" /></td></tr>

    <tr><td class="listhead2">This is a typal plot of the nomenclature (Braun-Blanquet)?</td>
        <td class="item"><input type="checkbox" id="af_nomenclaturaltype" name="tmp_nomenclaturaltype" value="true" /></td></tr>

    <tr><td class="listhead2">Notes</td>
        <td class="item"><textarea id="af_notes" name="tmp_notes" rows="4" cols="45"></textarea></td></tr>

    <tr><td colspan="2" class="item">
        <input type="button" value="save" onclick="javascript:saveForm(); closeAddForm(); return false;"/>
        &nbsp; &nbsp;
        <input type="button" value="cancel" onclick="javascript:closeAddForm(); return false;"/>
    </td></tr>
    </table>
</div>
<!-- END ADD FORM -->

<p id="add_another_link" style="display: none"><a href="#add_form_anchor" onclick="javascript:resetAddForm();openAddForm();return true">Add another community</a></p>
<p>&nbsp; &nbsp; &nbsp; &nbsp;
  <html:submit property="submit" value="Submit Interpretation" />
	&nbsp; &nbsp; &nbsp; &nbsp;
  <html:cancel/>
</p>

  </html:form>

<!-- BEGIN STATIC ROW TEMPLATE -->
<div id="record_template" style="display: none; margin-bottom: 10px">
    <span class="control_tab">
        <a href="#add_form_anchor" onclick="javascript: editRecord(event); return true;">edit</a> |
        <a href="#" onclick="javascript: toggleRecordDisplayByEvent(event); return false;">hide</a> |
        <a href="#" onclick="javascript: lookupComm(event); return false;">lookup</a> |
        <a href="#" onclick="javascript: deleteRecord(event); return false;">delete</a>
        &nbsp; &nbsp;
        <span id="title"></span>
    </span>
    <br/>


    <div class="block" >
    <table class="thinlines">
    <tr><td class="listhead">Community accession code</td><td class="item">
        <input id="commconcept_ac" type="hidden" name="tpl_commconcept_ac" size="28"/><span></span></td></tr>

    <tr><td class="listhead">Class fit</td>
        <td class="item"><input id="classfit" type="hidden" name="tmp_classfit"/><span></span></td></tr>

    <tr><td class="listhead">Class confidence</td>
        <td class="item"><input id="classconfidence" type="hidden" name="tpl_classconfidence"/><span></span></td></tr>

    <tr><td class="listhead">Reference</td>
        <td class="item"><input id="commauthority_id" type="hidden" name="tpl_commauthority_id"/><span></span></td></tr>

    <tr><td class="listhead">This is a typal plot of this community?</td>
        <td class="item"><input id="type" type="hidden" name="tpl_type"/><span></span></td></tr>

    <tr><td class="listhead">This is a typal plot of the nomenclature <span class="small">(Braun-Blanquet)</span>?</td>
        <td class="item"><input id="nomenclaturaltype" type="hidden" name="tpl_nomenclaturaltype"/><span></span></td></tr>

    <tr><td class="listhead">Notes</td><td class="item"><input id="notes" type="hidden" name="tpl_notes"/><span></span></td></tr>
    </table>
    </div>
</div>
<!-- END STATIC ROW TEMPLATE -->


<!-- extant interps -->
<br/>
<h3>Other Interpretations</h3>
<% String rowClass = "evenrow"; %>
<vegbank:get id="commclass" select="commclass" beanName="map" pager="false" where="where_observation_pk" wparam="obsId" perPage="-1" />
<%@ include file="../views/includeviews/sub_commclass_summary.jsp" %>
 

  @webpage_footer_html@
