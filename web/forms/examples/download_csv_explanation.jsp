@webpage_top_html@
  @stdvegbankget_jspdeclarations@
<bean:define id="FullPageWidthSuffix" value="_100perc" /><!-- sets stylesheet to full width stylesheet -->
  @webpage_head_html@
 
  <title>
   Explanation of CSV Download
  </title>


  @webpage_masthead_html@
  
  <h1>    Explanation of CSV Download </h1>
  
  <h4>A <strong>very brief</strong> introduction to .csv files and our variant of them:</h4>
  
  These files are called .csv or "comma-separated values" files.  This means that commas
  are used to separate the field values.  A header row labels what kind of data is in each column.
  
  A table like this:
  <table class="thinlines">
    <tr><th>plot number</th><th>State</th><th>Elevation</th></tr>
    <tr><td>1</td><td>NC</td><td>1543</td></tr>
    <tr><td>2</td><td>KS</td><td>991</td></tr>
    <tr><td>4</td><td></td><td>13</td></tr>
  </table>
  
  Is simply represented like this:
  
<pre>
plot number,State,Elevation
1,NC,1543
2,KS,991
4,,13
</pre>

We use quotes (") to enclose any fields that might have commas in them, so
it's clear that commas within quotes are NOT dividing two columns.  Any quotes
within a field are doubled.  The default is to have null fields be represented
with two consecutive commas, as above, but there is an option on the download page
to have the word "null" used instead.  For a similar table:
  <table class="thinlines">
    <tr><th>plot number</th><th>State</th><th>Elevation</th><th>Comments</th></tr>
    <tr><td>1</td><td>NC, USA</td><td>1543</td><td>This is a "strange" plot</td></tr>
    <tr><td>2</td><td>KS, USA</td><td>991</td><td>No comments</td></tr>
    <tr><td>4</td><td></td><td>13</td><td></td></tr>
  </table>


<pre>
plot number,State,Elevation,Comments
1,"NC, USA",1543,"This is a ""strange"" plot"
2,"KS, USA",991,"No comments"
4,null,13,null
</pre>

<p>For more information on .csv files, there are plenty of tutorials and information sites describing them.</p>

<h4>Our download files</h4>
 <p> The .csv plot download is one zipped file containing two files.  
 One is a file containing plot and environmental data, and is called 
 <strong>plot_env.csv</strong>.  This lists one plot per row, with information
 like latitude, longitude, state, etc. in the many columns.  Tip: delete any columns you 
 aren't interested in after downloading, as there are a lot of columns. </p>
 <p>The first row shown is the column header.  The second row has a brief definition,
 and often a link to the VegBank data dictionary where you can find out more about a field.  All 
 foreign keys are replaced with fields that tell you the name of the item referenced in VegBank.
 All <strong>Accession Codes</strong> can be used to link to VegBank to find out more about
 an item.  Link with the URL: http://vegbank.org/cite/[AccessionCode] (put the Accession Code without the brackets).
 See <a href="@general_link@cite.html">the VegBank Citation Page</a> for more information on how to link to VegBank.
 </p>
 
 
 <p>Format and Example: (<a href="example_plot_env.csv">download same plots as .csv file here</a>).
 </p>
    
     <iframe width="100%" height="550" src="@forms_link@examples/example_plot_env.html">
    </iframe>
   <a href="@forms_link@examples/example_plot_env.html">View this table in a full window</a>
 <p>The other file, called <strong>plot_taxa.csv</strong>, lists species on your plot, along with cover and other importance data.
 If strata were used in collecting data, species are listed per strata, and if also available,
 for overall values (for all strata).  This file is tied to the above file with the 
 <strong>observation_id</strong> field.</p>
 
 <p>Format and Example: (<a href="example_plot_taxa.csv">download same plots as .csv file here</a>).
  </p>
  
  <!--%@ include file="example_plot_taxa.html" %-->
    <iframe width="100%" height="550" src="@forms_link@examples/example_plot_taxa.html">
    </iframe>
   <a href="@forms_link@examples/example_plot_taxa.html">View this table in a full window</a>
@webpage_footer_html@


