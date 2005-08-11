var interpCount = 0;
var isAddFormChanged = false;

function setAddFormChanged() {
    isAddFormChanged = true;
}

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

    if (interpCount > 0) {
        // there are some interps already.
        // if form is changed, disable the submit button
        if (isAddFormChanged) {
            // form is open and changed
            setSubmitDisabled(true);
            show(gebid('submit_button_msg'));
        } else {
            // no form, some interps exist
            setSubmitDisabled(false);
            hide(gebid('submit_button_msg'));
        }
    } else {
        // no interps yet
        setSubmitDisabled(true);
        show(gebid('submit_button_msg'));
    }
}

function lookupComm(evt) {
    var link = getEventElement(evt);
    var ac = findSibling(link, "span", 10);
    window.open('@cite_link@' + ac.innerHTML, '', 'width=810,height=600,location,status,scrollbars,toolbar,resizable');
}

//
// Hides the add_form.
//
function closeAddForm() {
    if (isAddFormChanged) {
        if (!confirm('The community interpretation form has not been saved. Click "OK" to lose any changes.')) {
            // confirm box was cancelled
            return;
        }
        isAddFormChanged = false;
    }
    hide(gebid('add_form'));
    show(gebid("add_another_link"));

    if (interpCount > 0) {
        setSubmitDisabled(false);
        hide(gebid('submit_button_msg'));
    } else {
        // no interps yet
        setSubmitDisabled(true);
        show(gebid('submit_button_msg'));
    }
}

//
// Enables or disables the submit button.
//
function setSubmitDisabled(b) {
    if (b) {
        // disable
        gebid('submit_button').setAttribute("disabled", "disabled");
    } else {
        // enable
        gebid('submit_button').removeAttribute("disabled");
    }
}

//
// Displays the add_form for adding records.
//
function openAddForm() { 
    setSubmitDisabled(true);
    show(gebid('submit_button_msg'));
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
// Checks AC via AJAX, then calls saveValidatedForm if ok.
//
function saveForm() {
    // validate ac
    var fnWhenDone = function(oXML) { 
        if (oXML.responseText == 0) {
            alert("Please choose or enter a valid community accession code.");
        } else {
            saveValidatedForm();
            closeAddForm();
        }
    };

    var ccac = gebid("af_commconcept_ac").value;
    verifyAC(ccac, fnWhenDone);
}


//
// Copies add_form values to a newly inserted record_template.
//
function saveValidatedForm() {
    var addForm = gebid('add_form');
    var isNew = false;
    var saveRecord = null;

    var title = gebid("af_commconcept_ac").value;
    var recId = gebid('af_rec_id').value;
    //alert("af_rec_id is a " + recId);

    var recTitleSpan, errorMsgRow;
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
    errorMsgRow = findSibling(recTitleSpan.parentNode, "span", 10);
    errorMsgRow.setAttribute("id", "error-" + title);

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

    isAddFormChanged = false;
}

function editRecord(evt) {
    if (isAddFormChanged) {
        if (!confirm('The community interpretation form has not been saved. Click "OK" to lose any changes.')) {
            // confirm box was cancelled
            return;
        }
        isAddFormChanged = false;
    }
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

function popupObs(obsId) {
	window.open('@server_address@@get_link@detail/observation/' + obsId, '', 'width=810,height=600,location,status,scrollbars,toolbar,resizable');
}

function getHelpPageId() {
  return "interpret-taxon-on-plot";
}

//
// NOT USED
// For each given commInterp, verify the comm AC.
//
/*
function verifyCommACs() {
    
    var fnWhenDone = function(oXML) { 
        alert("pk: " + oXML.responseText);
        //gebid("").value = oXML.responseText;
    };

    var ac, i;
    var tmpField;
    var elems = document.InterpretCommForm.elements;
    var type, name;
                                                                                                                                                                     
    // find the commconcept_ac[] elements
    for (i=0; i<elems.length; i++) {
        tmpField = elems[i];
        type = tmpField.getAttribute("type");
        name = tmpField.getAttribute("name");
                                                                                                                                                                     
        if (type && type.toLowerCase() == "hidden" && name.substring(0,15) == "commconcept_ac[") {
            verifyAC(tmpField.value, fnWhenDone);
            delay(300);
        }
    }

    return false;
}
*/
