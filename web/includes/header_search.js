function preSubmit() {
    if (document.quicksearch_form.xwhereParams.value == null ||
        document.quicksearch_form.xwhereParams.value == "") {
        document.quicksearch_form.xwhereParams.value="vb";
        document.quicksearch_form.clearSearch.value="1";
    }
}
