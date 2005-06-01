/* $Id: header_search.js,v 1.10 2005-06-01 08:44:23 mlee Exp $ */

function doSubmit() {
	// alert("running doSubmit");
    var ent = document.quicksearch_form.selEntity.value;
     var xwhereParams = document.quicksearch_form.xwhereParams.value;
    document.quicksearch_form.qsent.value = document.quicksearch_form.selEntity.selectedIndex;
     var xwp = "xwhereParams=" + urlEncode(xwhereParams);
    // alert("xwp: " + document.quicksearch_form.xwhereParams.value) ;
    if (document.quicksearch_form.xwhereParams.value == null || document.quicksearch_form.xwhereParams.value == "") {
        // submit to metasearch
        ent = "anything";
        document.quicksearch_form.xwhereParams.value = "vb";
    }

    if (ent == 'anything') {
        // set action
        document.quicksearch_form.action = "@forms_link@metasearch.jsp";
        //+ qsent + "&" + xwp;
        document.quicksearch_form.clearSearch.value = 0; //this was 1, but should be 0.  0 is for all metadata.
        document.quicksearch_form.submit();

    } else {
        var getView = "summary";
        var getName = ent;
        var getPk = ent;
        // var getExtra = "&xwhereMatchAny=false"; // HANDLED in form now.


        // choose the right view
        switch (ent) {
            case 'plot':
				getPk = "observation";
				getName = "observation";
				getView = "summary";
				document.quicksearch_form.perPage.value = "7";
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
				document.quicksearch_form.perPage.value = "7";
                break;
        }

    //       <input type="hidden" name="ignore_where" value="" />
	//       <input type="hidden" name="ignore_xwhereKey" value="" />
	//       <input type="hidden" name="ignore_xwhereSearch" value="" />
    //       <input type="hidden" name="ignore_wparam" value="" />

        // populate the fields
		        // this delimiter is Utility.PARAM_DELIM
		    var params = getPk + "__" + ent;
		    document.quicksearch_form.ignore_wparam.value = params;
		    document.quicksearch_form.ignore_where.value = "where_keywords_pk_in";
		    document.quicksearch_form.ignore_xwhereKey.value = "xwhere_kw_match";
            document.quicksearch_form.ignore_xwhereSearch.value = "true";

        // rename some fields so they get used:
        document.quicksearch_form.ignore_where.name = "where";
        document.quicksearch_form.ignore_xwhereKey.name = "xwhereKey";
        document.quicksearch_form.ignore_xwhereSearch.name = "xwhereSearch";
        document.quicksearch_form.ignore_wparam.name = "wparam";



    //    var getURL = "@get_link@" + getView + "/" + getName + "/" + params +
    //            "?where=where_keywords_pk_in&xwhereKey=xwhere_kw_match&xwhereSearch=true&" +
    //            xwp + getExtra + "&" + qsent;

        document.quicksearch_form.action = "@views_link@" + getName  + "_" + getView + ".jsp";

        document.quicksearch_form.submit();

    }
}

function updateQuicksearch() {
	// alert("running updateQuicksearch()");
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
