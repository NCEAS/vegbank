
function doSubmit() {
    ent = document.quicksearch_form.selEntity.value;
    xwhereParams = document.quicksearch_form.xwhereParams.value;
    if (xwhereParams == null || xwhereParams == "") {
        // submit to metasearch
        ent = "anything";
        document.quicksearch_form.xwhereParams.value="vb";
    }

    if (ent == 'anything') {
        // set action
        document.quicksearch_form.action = "@forms_link@metasearch.jsp";
        document.quicksearch_form.submit();

    } else {
        getView = "std";
        getName = ent;
        getPk = ent;
        getExtra = "&xwhereMatchAny=true";


        // choose the right view
        switch (ent) {
            case 'plot': 
				getPk = "observation";
				getName = "observation";
				getView = "summary";
				getExtra = getExtra + "&perPage=3";
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
				getExtra = getExtra + "&perPage=5";
                break;
        }

        params = getPk + ";" + ent;
        getURL = "@get_link@" + getView + "/" + getName + "/" + params + 
                "?where=where_keywords_pk_in&xwhereKey=xwhere_kw_match&xwhereSearch=true&xwhereParams=" + 
                xwhereParams + getExtra;

        document.quicksearch_form.action = getURL;
        document.quicksearch_form.submit();
    }
}
