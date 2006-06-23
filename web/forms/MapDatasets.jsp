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
  
 <h1><img src="@images_link@maplogo.gif" /> Map Your VegBank Datasets</h1> 

<p class="instructions">Use this page to map the location of the plots in one of more of your datasets.
</p>

<p class="instructions">You must be a registered user who is logged in to use this feature.</p>


 <p class="instructions">
   Mapping is handled in an external website.  VegBank uses 
   <a href="http://maps.yahoo.com">Yahoo! Maps</a> and 
   <a href="http://maps.google.com">Google Maps</a>,
   who have generously made their mapping software open to such requests.
   
    VegBank is not responsible for the content in these maps,
    including any advertisements found there. <br/>
    
    
 
 </p>
 
  <p class="instructions">Please check the row next to the datasets you would like to map.  
  Then choose whether you would like to use Yahoo Maps or Google Maps.  
  For Google Maps, you have a choice of icons as well.
  
  
 </p>
 
  <form name="dataset" action="@views_link@userdataset_map.jsp" onsubmit="prepareForm()">
  <bean:define id="where" value="where_userdataset_hastable" />
  <bean:define id="wparam" value="observation" />
  
  <%@ include file="/views/includeviews/sub_userdataset_checkboxes.jsp" %>
  <logic:present name="successfulget">
    <!-- only put this button if there were user datasets retrieved -->
    

    <p>Map using: </p>
     <table class="thinlines"><tr class="evenrow"><td>
     <input type="radio" name="mapwith" value="yahoo" />Yahoo! Maps 
     </td></tr>
     <tr class="oddrow"><td> 
     <input type="radio" name="mapwith" value="google" checked="checked">Google Maps 
     <div class="small">
       <br/>&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="mapicons" value="colors" checked="checked"/> Smaller Icons of Different Colors <img src="@images_link@map_google_c_0.png" alt="example small icon #1" /><img src="@images_link@map_google_c_1.png" alt="example small icon #2" /><img src="@images_link@map_google_c_2.png" alt="example small icon #3" />
       <br/>&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="mapicons" value="nocolors" /> Larger Icons Marked With Letters <img src="@images_link@map_google_l_0.png" alt="example large icon #1" /><img src="@images_link@map_google_l_1.png" alt="example large icon #2" /><img src="@images_link@map_google_l_2.png" alt="example large icon #3" />
     </div>
     </td></tr></table>
     
    
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


