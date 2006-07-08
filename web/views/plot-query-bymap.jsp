@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@
  @ajax_js_include@
  @datacart_js_include@
     <!-- style bit is for google maps -->
     <style type="text/css">
     v\:* {
       behavior:url(#default#VML);
     }
    </style>
  
   <%
     int inttemp=0 ; 
     String howManyPlantsComplex="3"; // this many plants if complex query
     String howManyPlantsDefault="5"; // this many plants if default query (overrides above)
    %>
    
        <% double VbGminLat = 500.03; %>
            <% double VbGmaxLat = -500.03; %>
            <% double VbGminLong = 500.03; %>
        <% double VbGmaxLong = -500.03; %>
       
     <!-- key is set in build.properties -->
    <script src="http://maps.google.com/maps?file=api&v=2&key=@googlemaps_apikey@" type="text/javascript"></script>
    <script src="@includes_link@VbGoogleMap.js" type="text/javascript"></script>
    
    <script type="text/javascript">
    
    function prepareForm() {
        // alert(Math.round(document.mapqueryform.xwhereParams_minlatitude_0.value  * 100));
       document.mapqueryform.criteriaAsText.value = "Latitude between " + 
          (Math.round(document.mapqueryform.xwhereParams_minlatitude_0.value  * 100) / 100) + " and " + 
          (Math.round(document.mapqueryform.xwhereParams_maxlatitude_0.value  * 100) / 100)+ ", and Longitude between " +
          (Math.round(document.mapqueryform.xwhereParams_minlongitude_0.value * 100) / 100)+ " and " + 
          (Math.round(document.mapqueryform.xwhereParams_maxlongitude_0.value * 100) / 100)+ ".";
       return true;
    }
    
     
    </script>
    
    
    <title>VegBank Plot Query Results -- Mapped </title>
    
    @webpage_masthead_html@
    
     <h2><img src="@images_link@maplogo.gif" /> Search for VegBank Plots on a Map</h2>

<p class="instructions">
On this page, you can search for plots within a particular latitude and longitude.
</p>
<ul class="instructions">
  <li><b>Click the map four times</b> to form a rectangle.</li>
  <li>As you click, a blue rectangle will form to as the boundaries of your rectangle</li>
  <li>You can also <b>zoom</b> in or out using the + and - buttons, and <b>drag</b> to move the map around.</li>
  <li>If you make a mistake, click the <b>"reset map"</b> button below and the map will reset.</li>
  <li>Once you have your rectangle, click the <b>"Find Plots"</b> button.</li>
  <li>Plots will shown that are within the maximim and minimum latitude and longitude of your clicked points.</li>
</ul>  

<div id="querymap" style="width: 500px; height: 350px">
<!-- placeholder for google map to start query -->
</div>
<!-- just a link to itself: -->

<!-- @views_link@plot-query-bymap.jsp?reload=true -->
<br />
<form name="mapqueryform" action="@views_link@observation_summary.jsp" method="get" onsubmit="javascript:return prepareForm()">

    <input type="submit" value="Find Plots"/> | 
      <input type="button" onclick='if (VbGClearThisMap()) { VbGClearMapForm(); alert("The map will be reset when you click it."); } ;' value="Reset Map" /> 
    <div class="hidden">
      <!-- the following are copied from the plot-query form, using names from that form, and id's from this one -->
        
            <input type="hidden" name="xwhereKey_minlatitude" value="xwhere_gteq" />
            <input type="hidden" name="xwhereParams_minlatitude_1" value="latitude" />
            <input value="5000" id="minLat" name="xwhereParams_minlatitude_0" class="number" size="20"/>
            <input type="hidden" name="xwhereKey_maxlatitude" value="xwhere_lteq" />
            <input type="hidden" name="xwhereParams_maxlatitude_1" value="latitude" />
            <input value="-5000" id="maxLat" name="xwhereParams_maxlatitude_0" class="number" size="20"/>

            <input type="hidden" name="xwhereKey_minlongitude" value="xwhere_gteq" />
            <input type="hidden" name="xwhereParams_minlongitude_1" value="longitude" />
            <input value="5000" id="minLng" name="xwhereParams_minlongitude_0" class="number" size="20"/>

            <input type="hidden" name="xwhereKey_maxlongitude" value="xwhere_lteq" />
            <input type="hidden" name="xwhereParams_maxlongitude_1" value="longitude" />
            <input value="-5000" id="maxLng" name="xwhereParams_maxlongitude_0" class="number" size="20"/>

            <!-- stuff needed to make xwhere work: -->
            <input type="hidden" name="where" value="where_simple" />
            <input type="hidden" name="xwhereGlue" value="AND" />
            <input type="hidden" name="criteriaAsText" value="Plots inside boundary drawn on map." />

        prevLat:<div id="previousLat"> </div> prevLong:<div id="previousLong"> </div>
        <hr />
        <div id="allPoints"> </div>
        <div id="pointsClicked">0</div>
    </div>

</form>


<div id="message">

</div>

<p>If you can't find the plots you're interested in with this search, 
you could try the <a href="@plotquery_page_advanced@">advanced search</a>.</p>

@webpage_footer_html@

<!-- now we throw in the js to map things -->
  <script type="text/javascript">
  <!-- //<![CDATA[
  function VbGLoadAllMapsThisPage(){
  
   var map = VbGMapLoadByCenter("querymap",49.2678,-97.3828,3);
   VbGMakeMapQueryClickable(map);
  
   }
   //]]>
  -->
</script>



<!-- how to calculate inside polygon:

int pnpoly(int npol, float *xp, float *yp, float x, float y)
    {
      int i, j, c = 0;
      for (i = 0, j = npol-1; i < npol; j = i++) {
        if ((((yp[i]<=y) && (y<yp[j])) ||
             ((yp[j]<=y) && (y<yp[i]))) &&
            (x < (xp[j] - xp[i]) * (y - yp[i]) / (yp[j] - yp[i]) + xp[i]))

          c = !c;
      }
      return c;
    }
    
    -->