@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@
 <%@ include file="/views/includeviews/inlinestyles.jsp" %> 
  <title>
   Map Your VegBank Datasets
  </title>
 <script type="text/javascript">
   function prepareForm() {
    // alert('trying!');
     for (i=0;i<document.dataset.elements.length;i++) {
      // alert(document.dataset.elements[i].name);
       if (document.dataset.elements[i].name == 'mapwith' && document.dataset.elements[i].checked  &&    document.dataset.elements[i].value == 'google' ) {
         document.forms.dataset.action = "@views_link@userdataset_googlemap.jsp";
        // alert('fixed to google');
       }
     }

   }
 </script>

<!-- VegBank Header -->
  @webpage_masthead_html@
  
 <h1>Map Your VegBank Datasets</h1> 

<p class="instructions">Use this page to map the location of the plots in one of more of your datasets.
</p>

<p class="instructions">You must be a registered user who is logged in to use this feature.</p>

 <p class="instruction">Please check the row next to the datasets you would like to map.  Each dataset
 will be marked with a different colored and shaped icon in a map, using Yahoo! Maps.
 
 </p>
 <p>
   The maps from this form open in a new window, in an external website.  VegBank uses Yahoo! Maps,
   who have generously made their mapping software open to such requests.
   
    VegBank is not responsible for the content in the lower window, including any advertisements found there. <br/>
    
    
 
 </p>
 
  <form name="dataset" action="@views_link@userdataset_map.jsp" onsubmit="prepareForm()">

  <%@ include file="/views/includeviews/sub_userdataset_checkboxes.jsp" %>
  <logic:present name="successfulget">
    <!-- only put this button if there were user datasets retrieved -->
    

    <p>Map using:<br/>
     <input type="radio" name="mapwith" value="yahoo" />Yahoo! Maps <br/>
     <input type="radio" name="mapwith" value="google" checked="checked">Google Maps <br/>
     </p>
     <input type="submit" value="Map Datasets" />
   </logic:present> 
  </form>
 <p>
 <strong>Known Problems:</strong> (which VegBank can't fix) </p>
    Yahoo Maps:
    <ul>
       <li> When requesting a map, sometimes Yahoo doesn't wait long enough to receive the VegBank
     locations file.  It says "Error 0 out of -1 bytes received."  If this happens, try again, as it often works on the second try. 
       </li>
       <li>
         If you map more than 4 datasets simultaneously, the legend displays the dataset icon incorrectly.
       </li>
       <li>   Pressing the refresh button, back button, or forward button on your browser after 
     zooming in or recentering the map may cause plots not to be displayed correctly. </li>
     <li>
     When datasets are mapped, any apostrophes (') in the dataset name will truncate name display.</li>
     <li>
     Mapping in Yahoo doesn't seem to work in Windows/Opera.</li>
    </ul>
    Google Maps:
    <ul>
      <li>
        Doesn't work in all browsers.  Requires Firefox/Mozilla or Win/IE 5.5+.  Win/Opera doesn't really work.
        Mac/Safari ???.
      </li>
    </ul>

 
 
@webpage_footer_html@


