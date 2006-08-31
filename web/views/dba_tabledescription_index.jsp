@webpage_top_html@
 @stdvegbankget_jspdeclarations@
 @webpage_head_html@
 <TITLE>VegBank Data Dictionary: Menu</TITLE>
        <%@ include file="includeviews/inlinestyles.jsp" %>
@webpage_masthead_html@ 

 <h2>VegBank Data Dictionary: Menu of Tables</h2>
  <p>In addition to this data dictionary, you may find the <a href="@design_link@erd/vegbank_erd.pdf">Entity Relationship Diagram (ERD)</a> helpful in figuring out our data model.</p>
  <!-- add search box -->
  <table class="noborders"><td>
  <bean:define id="entityToSearch" value="dba_fielddescription" />
   <bean:define id="NameOfEntityToPresent" value="VegBank Field" />
   <bean:define id="SearchInstructions" value="(enter a word, field name, etc.)" /> 
   <bean:define id="alternateSearchInputs">
     <input type="hidden" name="xwhereKey" value="xwhere_match" />
     <input type="hidden" name="where" value="where_simple" />
     <input type="hidden" name="xwhereParams_custom_1" value="fieldkeywords" /><!-- name of field to search -->
   </bean:define>
   <%@ include file="includeviews/sub_searchEntity.jsp" %>
   </td><td>&nbsp;&nbsp;
  <a href="@get_link@std/dba_tabledescription">Search VegBank Tables Here</a></td></tr></table>
        <div class="spacer">&nbsp;</div>
  
      <div id="content-3col-a">
        <fieldset>
        <legend>Core Plot Tables</legend>
          <ul>
            <li><a href="/dd/plot">Plot</a></li>
            <li><a href="/dd/observation">Observation</a></li>
            <li><a href="/dd/taxonobservation">Taxon Observation</a></li>
            <li><a href="/dd/taxoninterpretation">Taxon Interpretation</a></li>
            <li><a href="/dd/taxonimportance">Taxon Importance</a></li>
          </ul>
        </fieldset>
     <br/>
        <fieldset>
          <legend>Supporting Plot Tables</legend>
          <ul>
            <li><a href="/dd/stratum">Stratum</a></li>
            <li><a href="/dd/soilobs">Soil Obs</a></li>
            <li><a href="/dd/disturbanceobs">Disturbance Obs</a></li>
            <li><a href="/dd/stemcount">Stem Count</a></li>
            <li><a href="/dd/stemlocation">--Stem Location</a></li>
            <li><a href="/dd/taxonalt">Taxon Alt</a></li>
            <li><a href="/dd/observationcontributor">Observation Contributor</a></li>
            <li><a href="/dd/place">Place</a></li>

          </ul>
        </fieldset>
      <br/>  
        <fieldset>
          <legend>Plot Classification</legend>
          <ul>
            <li><a href="/dd/commclass">Community Classification</a></li>
            <li><a href="/dd/comminterpretation">Community Interpretation</a></li>
            <li><a href="/dd/classcontributor">Classification Contributor</a></li>
          </ul>
        </fieldset>
      </div>
  
  
      <div id="content-3col-b">
        <fieldset>
        <legend>Plants</legend>
          <ul>
            <li><a href="/dd/plantconcept">Plant Concept</a></li>
            <li><a href="/dd/plantname">Plant Name</a></li>
            <li><a href="/dd/reference">Reference</a></li>
            <li><a href="/dd/plantstatus">Plant Status</a></li>
            <li><a href="/dd/plantcorrelation">Plant Correlation</a></li>
            <li><a href="/dd/plantusage">Plant Usage</a></li>
          </ul>
        </fieldset>
        <br/>
        <fieldset>
        <legend>Community Types</legend>
          <ul>
            <li><a href="/dd/commconcept">Community Concept</a></li>
            <li><a href="/dd/commname">Community Name</a></li>
            <li><a href="/dd/reference">Reference</a></li>
            <li><a href="/dd/commstatus">Community Status</a></li>
            <li><a href="/dd/commcorrelation">Community Correlation</a></li>
            <li><a href="/dd/commusage">Community Usage</a></li>
          </ul>
        </fieldset>
        <br/>
        <fieldset>
           <legend>Advanced</legend>
           <ul>
             <li><a href="/dd/embargo">Embargo</a></li>
             <li><a href="/dd/revision">*Revision</a></li>
             <li><a href="/dd/userdefined">User Defined</a></li>
             <li><a href="/dd/definedvalue">--Defined Value</a></li>
             <li><a href="/dd/note">*Note</a></li>
             <li><a href="/dd/notelink">*Note Link</a></li>
             <li><a href="/dd/observationsynonym">*Observation Synonym</a></li>
             <li><a href="/dd/graphic">*Graphic</a></li>
           </ul>
        </fieldset>
      </div>

      <div id="content-3col-c">
          
      <fieldset>
        <legend>Supplemental Data</legend>
        <ul>
          <li><a href="/dd/party">Party</a></li>
          <li><a href="/dd/telephone">--Telephone</a></li>
          <li><a href="/dd/address">--Address</a></li>
          <li><a href="/dd/aux_role">--Role</a></li>
          <li><a href="/dd/project">Project</a></li>
          <li><a href="/dd/projectcontributor">--Project Contributor</a></li>
          <li><a href="/dd/stratummethod">Stratum Method</a></li>
          <li><a href="/dd/stratumtype">--Stratum Type</a></li>
          <li><a href="/dd/covermethod">Cover Method</a></li>
          <li><a href="/dd/coverindex">--Cover Index</a></li>
          <li><a href="/dd/reference">Reference</a></li>
          <li><a href="/dd/referencejournal">--Reference Journal</a></li>
          <li><a href="/dd/referenceparty">--Reference Party</a></li>
          <li><a href="/dd/referencecontributor">--Reference Contributor</a></li>
          <li><a href="/dd/referencealtident">--Reference Alternate ID</a></li>
          <li><a href="/dd/namedplace">Named Place</a></li>
          <li><a href="/dd/namedplacecorrelation">--Named Place Correlation</a></li>
          <li><a href="/dd/soiltaxon">Soil Taxon</a></li>

        </ul>
      </fieldset>
      
       <p>* Fields marked with an asterisk have not yet been implemented into the website.
       <br/>- Fields marked with two hyphens are related to the previous table.</p>
  
     </div>

          @webpage_footer_html@
