/* $Id: header_search.js,v 1.9 2005-05-05 20:22:16 anderson Exp $ */

function doSubmit() {
    var ent = document.quicksearch_form.selEntity.value;
    var xwhereParams = document.quicksearch_form.xwhereParams.value;
    var qsent = "qsent=" + document.quicksearch_form.selEntity.selectedIndex;
    var xwp = "xwhereParams=" + urlEncode(xwhereParams);

    if (xwhereParams == null || xwhereParams == "") {
        // submit to metasearch
        ent = "anything";
        xwp = "xwhereParams=vb";
    }

    if (ent == 'anything') {
        // set action
        document.quicksearch_form.action = "@forms_link@metasearch.jsp?" + qsent + "&" + xwp;
        document.quicksearch_form.clearSearch.value = 1;
        document.quicksearch_form.submit();

    } else {
        var getView = "std";
        var getName = ent;
        var getPk = ent;
        var getExtra = "&xwhereMatchAny=true";


        // choose the right view
        switch (ent) {
            case 'plot': 
				getPk = "observation";
				getName = "observation";
				getView = "summary";
				getExtra += getExtra + "&perPage=7";
                break;
            case 'plant': 
				getPk = "plantconcept";
				getName = "plantconcept";
                break;
            case 'place': 
				getPk = "namedplace";
				getName = "namedplace";
                break;
            case 'party': break;
            case 'project':  break;
            case 'community': 
				getPk = "commconcept";
				getName = "commconcept";
				getExtra += getExtra + "&perPage=7";
                break;
        }

        // this delimiter is Utility.PARAM_DELIM
        var params = getPk + "__" + ent;
        var getURL = "@get_link@" + getView + "/" + getName + "/" + params + 
                "?where=where_keywords_pk_in&xwhereKey=xwhere_kw_match&xwhereSearch=true&" +
                xwp + getExtra + "&" + qsent;

        document.quicksearch_form.action = getURL;
        document.quicksearch_form.submit();
    }
}

function updateQuicksearch() {
    var ent = getURLParam("qsent");
    var xwp = getURLParam("xwhereParams");

    if (ent != null && ent != '') {
        //document.getElementById("selEntity").selectedIndex = ent;
        document.quicksearch_form.selEntity.selectedIndex = ent;
		if (xwp != 'vb') {
        	document.quicksearch_form.xwhereParams.value = urlDecode(xwp);
		}
    }
}
