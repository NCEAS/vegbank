@webpage_top_html@
@stdvegbankget_jspdeclarations@
@webpage_head_html@
<title>VegBank</title>
@webpage_masthead_html@

    <div class="spacer">&nbsp;</div>

    <div id="content-3col-a">
    <fieldset>
    <legend>Find Plots</legend>
    <ul>
    <li><a href="@browseplotspage@">Browse plots</a></li>
    <li><a href="@plotquery_page_simple@">Simple search</a></li>
    <li><a href="@plotquery_page_advanced@">Advanced plot search</a></li>
    </ul>
     <%@ include file="includes/plot-map-northamerica-home.jsp" %>
     <%@ include file="includes/plot-map-northamerica-key.jsp" %>
    </fieldset>

    </div>


    <div id="content-3col-b">
    <fieldset>
    <legend>Plant Taxa</legend>
    <ul>
    <li><a href="@forms_link@PlantQuery.jsp">Search plants</a></li>
    <li><a href="@general_link@browseplants.jsp">Browse plants</a></li>
    <li><a href="@DisplayUploadPlotAction@">Submit plants</a></li>
    </ul>
    </fieldset>

    <br />
    <fieldset>
    <legend>Plant Communities</legend>
    <ul>
    <li><a href="@forms_link@CommQuery.jsp">Search communities</a></li>
    <!--li><a href="xxx">Browse communities</a></li-->
    <li><a href="@DisplayUploadPlotAction@">Submit communities</a></li>
    </ul>
    </fieldset>

    <br />
    <fieldset>
    <legend>Supplemental Data</legend>
    <ul>
    <li><a href="@get_link@std/party">People</a></li>
    <li><a href="@get_link@std/stratummethod">Stratum Methods</a></li>
    <li><a href="@get_link@std/covermethod">Cover Methods</a></li>
    <li><a href="@get_link@std/project">Projects</a></li>
    <li><a href="@get_link@std/reference">References</a></li>
    <li><a href="@general_link@metadata.html">More Data</a></li>
    </ul>
    </fieldset>

    </div>



    <div id="content-3col-c">
    <fieldset>
    <legend>Learn About VegBank</legend>
    <ul>
    <li><a href="@general_link@info.html">What is VegBank?</a></li>
    <li><a href="@general_link@faq.html">FAQ</a></li>
    <li><a href="@general_link@instructions.html">Tutorial</a></li>
    <li><a href="@general_link@cite.html">Cite or link to VegBank</a></li>
    <li><a href="@general_link@terms.html">Terms of use</a></li>
    <li><a href="@general_link@sitemap.html">Site map</a></li>
    <li><a href="@general_link@contact.html">Contact</a></li>
    </ul>
    </fieldset>

    <br />
    <fieldset>
    <legend>Contribute Plot Data</legend>
    <ul>
    <li><a href="@DisplayUploadPlotAction@">Submit plots</a></li>
    <li><a href="@forms_link@getHelp.jsp?helpPage=@manual_link@interpret-taxon-on-plot.html">Annotate plots</a></li>
    <li><a href="@forms_link@getHelp.jsp?helpPage=@manual_link@interpret-a-plot.html">Classify plots</a></li>
    </ul>
    </fieldset>

    <br />
    <fieldset>
    <legend>Tools</legend>
    <ul>
    <li><a href="@searchplugins_link@install-search.html">Firefox toolbar</a></li>
    <li><a href="@vegbranch_link@vegbranch.html">VegBranch client database</a></li>
    <li><a href="@vegbranch_link@docs/normalizer/normalizer.html">Data matrix normalizer</a></li>
    </ul>
    </fieldset>

    <br />
    <fieldset>
    <legend>My VegBank Account</legend>
    <ul>
    <li><a href="@web_context@LoadUser.do">Edit profile information</a></li>
    <li><a href="@web_context@DisplayDatasets.do">Manage datasets</a></li>
    </ul>
    </fieldset>


 </div>


@webpage_footer_html@
