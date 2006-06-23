@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  @webpage_head_html@
  @ajax_js_include@
  @datacart_js_include@

  
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
On this page, you can draw a rectangle on the map in order to find the plots within it.
<b>Click the map four times</b> to form a rectangle, with each click representing a vertex
of the rectangle.  As you click, a blue rectangle will form to 
show you where the boundaries of your rectangle are.  It doesn't have to be a perfect rectangle,
but it should be close to a rectangle.  You can also zoom in or out, and drag to move the map around.
If you make a mistake, click the "reset drawn box on map"
link below and the map will reset.

</p>

<p class="instructions">After you select four points, press the "find plots" button at the bottom of the page.
You can close the rectangle, but you don't have to. </p>

<div id="querymap" style="width: 500px; height: 350px">
<!-- placeholder for google map to start query -->
</div>
<!-- just a link to itself: -->
<a href="@views_link@plot-query-bymap.jsp?reload=true" onclick='return VbGClearThisMap()'>reset drawn box on map</a> 

<br />
<form name="mapqueryform" action="@views_link@observation_summary.jsp" method="get" onsubmit="javascript:return prepareForm()">

    <input type="submit" value="find plots"/>
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

        <div id="previousLat"> </div><div id="previousLong"> </div>
        <hr />
        <div id="allPoints"> </div>
        <div id="pointsClicked">0</div>
    </div>

</form>


<div id="message">

</div>

<p>If you can't find the plots you're interested in with this search, 
could try the <a href="@plotquery_page_advanced@">advanced search</a>.</p>

@webpage_footer_html@

<!-- now we throw in the js to map things -->
  <script type="text/javascript">
  <!-- //<![CDATA[
  function VbGLoadAllMapsThisPage(){
  
   var querymap = VbGMapLoadByCenter("querymap",49.2678,-97.3828,3);
   VbGMakeMapQueryClickable(querymap);
  
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