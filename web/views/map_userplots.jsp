
@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  <!-- have to figure out sizing up here, as this is where the css is -->
        <!-- deal with size of the map: -->
        <!-- first defaults: -->
        <bean:define id="mapWidth">700</bean:define>
        <bean:define id="mapHeight">500</bean:define>
        
        <bean:define id="sml_link_html">small</bean:define>
        <bean:define id="med_link_html"><span class="lk" onclick="postNewParam('mapSize','m');return false;">Medium</span></bean:define>
        <bean:define id="lrg_link_html"><span class="lk" onclick="postNewParam('mapSize','l');return false;">LARGE</span></bean:define>
        <bean:define id="cust_link_html"><span class="lk" onclick="showbyid('resizeMapCustom');return false;">custom...</span></bean:define>
        
        <logic:present parameter="mapSize">
           <logic:equal parameter="mapSize" value="s">
              <bean:define id="mapWidth">700</bean:define>
              <bean:define id="mapHeight">500</bean:define>
              <!-- reset sml link: -->
              <bean:define id="sml_link_html">small</bean:define>
           </logic:equal>
  
           <logic:equal parameter="mapSize" value="m">
              <bean:define id="mapWidth">950</bean:define>
              <bean:define id="mapHeight">650</bean:define>
              <!-- reset med link: -->
              <bean:define id="med_link_html">Medium</bean:define>
              <bean:define id="sml_link_html"><span class="lk" onclick="postNewParam('mapSize','s');return false;">small</span></bean:define>
           </logic:equal>
           
           <logic:equal parameter="mapSize" value="l">
              <bean:define id="mapWidth">1350</bean:define>
              <bean:define id="mapHeight">800</bean:define>
              <bean:define id="lrg_link_html">LARGE</bean:define>
              <bean:define id="sml_link_html"><span class="lk" onclick="postNewParam('mapSize','s');return false;">small</span></bean:define>
           </logic:equal>
           <logic:equal parameter="mapSize" value="custom">
             <!-- what is the size? -->
             <bean:parameter id="mapWidth" name="mapSizeWidth"  value="700" />
             <bean:parameter id="mapHeight" name="mapSizeHeight"  value="500" />
             <bean:define id="sml_link_html"><span class="lk" onclick="postNewParam('mapSize','s');return false;">small</span></bean:define>
             <bean:define id="cust_link_html">custom...</bean:define>
             <bean:define id="noHideCustBox">NO</bean:define><!--prevent hidden later -->
           </logic:equal>
      </logic:present>
  
  <logic:greaterThan name="mapWidth" value="750">
    <!-- sets stylesheet to full width stylesheet since it wont fit in regular VegBank page -->
    <bean:define id="FullPageWidthSuffix" value="_100perc" />
  </logic:greaterThan>
  @webpage_head_html@
 
 
 
<TITLE>Map YOUR plots (a service from VegBank)</TITLE>

     <!-- style bit is for google maps -->
     <style type="text/css">
     v\:* {
       behavior:url(#default#VML);
     }
    </style>
  
   <!-- the following file allows configured icons -->
   <%@ include file="includeviews/inlinestyles.jsp" %> 
 
       
     <!-- key is set in build.properties -->
    <script src="http://maps.google.com/maps?file=api&v=2&key=@googlemaps_apikey@" type="text/javascript"></script>

    <script src="@includes_link@VbGoogleMap.js" type="text/javascript"></script>
  

  <!-- this script deals with some imported XML as text then parses it to deal with mapping -->

  <!-- there should be a parameter: xmllatlong passed, get it -->
  
  <bean:define id="dataValidated" value="false" /> <!-- default -->


  
  <logic:present parameter="latlongfile">
      <bean:parameter id="bean_latlongfile" name="latlongfile" />
     <!-- now get the file as a writable object -->
     <logic:notEmpty name="bean_latlongfile">
       <bean:define id="newBeanCSVLoc"><bean:write name="bean_latlongfile" /></bean:define>
       <bean:include id="csvdata" href="<%= newBeanCSVLoc %>"/>
     </logic:notEmpty>  
  </logic:present> 
  <logic:present parameter="csvraw">
      <logic:notPresent name="csvdata"> <!-- not already defined -->
        <bean:parameter id="csvdata" name="csvraw" value="none" />
      </logic:notPresent>
  </logic:present>

      <!-- dataValidated prevents someone from escaping the textarea and inserting random code into this -->
    <logic:present name="csvdata">  
      <script type="text/javascript">
     //declare map object:
     var bigmap;
      function VbGLoadAllMapsThisPage() {
        <logic:notMatch name="csvdata" value="<">
          <bean:define id="dataValidated" value="true" />
          bigmap = VbGMapCSV(document.getElementById("domCSVData").value,"map",35,36,-127,-126, true, true, "zoomInMap") ;
          if (bigmap == null) {
            //clean up this page a bit:
            hidebyid("googleMapPageContainer");
            hidebyid("whereismap_wrapper");
            alert('no plots were mapped.  Please check that the file matches the correct format!');
          } else {
            //got a map
            // alert('got a map ok');
            VbGMapLoadWhereIsMap("whereismap",bigmap);
          }  
        </logic:notMatch>  
       }

     
     
       </script>
     
     <style id="hideGoogleControlsViaCSS" type="text/css"></style>
     <script type="text/javascript">
       function showHideGoogleControls() {
         var styleData = document.getElementById("hideGoogleControlsViaCSS");
         // alert('css in question:' + styleData.innerHTML);
         try {
           //see what's in the style:
           if (styleData.innerHTML.indexOf("none") == -1) {
             styleData.innerHTML = ".gmnoprint {display:none}";
           } else {
             styleData.innerHTML = ".gmnoprint {display:block}";
           }
         } catch (e) {
           if (browser_ie == true) {
             alert("Sorry, Internet Explorer doesn't support this feature.  You could try using Firefox.");
             return false;
           } else {
             alert("Sorry, but your browser doesn't support this currently.");
           }
         }
       }
     </script>
     </logic:present>
     
 @webpage_masthead_html@ 
  @possibly_center@  

<!-- see if there is even data defined.  If not, this page is a menu-->
<logic:notPresent name="csvdata">
  <!-- there is no data, just display menu -->
<h2>Map YOUR plots</h2>

  <p>This page will map your plots from a CSV file that is publicly accessible.  <strong>They are NOT plots in VegBank.  </strong>
  </p>
</logic:notPresent>

<DIV id="alternateBigPageWrapper">
<logic:present name="csvdata"><!-- we have data -->
  <logic:notEqual name="dataValidated" value="false"> <!-- and it is valid -->
  
    <div id="mapTitleLine">
      <div id="mapsizesetter" style="float:right;">
      Controls: <span class="lk" onclick="showHideGoogleControls();return false;">show/hide</span>. --
      Map size: <span style="font-size:8pt"> <bean:write name="sml_link_html" filter="false" /> </span>
      | <span style="font-size:10pt"><bean:write name="med_link_html" filter="false" />  </span>
      | <span style="font-size:12pt"><bean:write name="lrg_link_html" filter="false" /> </span>
      | <bean:write name="cust_link_html" filter="false" />
      <span id="resizeMapCustom" class="<bean:write name='noHideCustBox' ignore='true' />hidden">
      
      <form name="sizeToCustom" action="<%= request.getRequestURL() %>" method="post">
        <logic:notPresent parameter="latlongfile">
        <textarea class="hidden" name="csvraw" cols="60" rows="6"><bean:write name="csvdata" filter="false" ignore="true"/></textarea>
        </logic:notPresent>
        <logic:present parameter="latlongfile">
          <input type="hidden" name="latlongfile" size="80" value='<bean:write name="bean_latlongfile" ignore="true"/>' />
        </logic:present>
        <input type="hidden" name="mapSize" value="custom" />
      width=<input name="mapSizeWidth" value="<bean:write name='mapWidth' />" type="text" size="4" maxlength="5" /> 
      height=<input  name="mapSizeHeight" value="<bean:write name='mapHeight' />" type="text" size="4" maxlength="5"/> (in pixels) 
      <input type="submit" value="resize" onclick="formConvertToGetIfURLLengthOK('sizeToCustom');"/></form></span>
      </div>
      <h2>Map YOUR plots</h2>
      
    </div>
    <!-- dont forget a place to put the google map: -->
    <div id="googleMapPageContainer" style="clear: both;"> <!-- style="width: 700px" -->
      <div id="map" style="width:<bean:write name='mapWidth' />px; height:<bean:write name='mapHeight' />px"></div>
    </div> 
  
    <bean:define id="dataValidated" value="true" /><!-- tell that this is ok -->
    <textarea id="domCSVData" class="hidden"><bean:write name="csvdata" filter="false" /></textarea>



  
  <div id="zoomInMap" class="hidden" style="clear:both;position:relative; width:600px;height:450px; overflow:hidden; border:medium double rgb(0,0,0); ">
    <iframe style="position: absolute; left:-191px; top:-167px; width: 791px; height: 617px; border: 0px none ; margin: 0px; overflow: hidden; " frameborder="0" scrolling="no"></iframe>
    <div style="position:absolute; left:0px; top:0px; width:100%; height:100%; z-index:10">
      <span id="zoomInPlotName" style="position:absolute;bottom:0px; left:0px; background-color:#CCCCCC">no plot selected</span>
      <a id="zoomInCloseLink" style="position:absolute;top:0px;right:0px;background-color:#CCCCCC" href="#" onclick="VbGCloseZoomMap('zoomInMap');return false;">close</a>
      <a id="zoomInMapLink" style="position:absolute;bottom:0px; right:0px; background-color:#CCCCCC" target="_new" href="#">bigger&gt;</a>
    </div>
  </div>
  
<!--DIV style="width:700px" style="clear:both;"--><!-- start a container for rest of page -->
   
  <div style="float:right" id="whereismap_wrapper">
    Initial location of larger map outlined here: </br/>    
    <div id="whereismap" style="width: 190px; height: 100px"></div>      
  </div>

    <p>
     <logic:notEmpty name="bean_latlongfile">
      You are mapping plots from this URL: 
      <bean:write name="bean_latlongfile" /> <br/>
     </logic:notEmpty> 
    
    <strong>Linking to this page</strong><br/>
    You can map plots, then link to this page with your plots mapped.  If you have a large number of plots, you may have 
    to use an external file due to URL length limitations.

    <br/>

    <% if (Utility.isStringNullOrEmpty( request.getQueryString() )) { %>
     <strong> You are mapping too many plots to link via a URL.</strong>
     <br/>You can still send someone the link to this page: 
     <pre><a href="<%= request.getRequestURL() %>"><%= request.getRequestURL() %></a></pre>
     <br/> and instruct them to paste in the plot locations: <br/>
     <pre><bean:write name="csvdata" filter="false" ignore="true"/></pre>
    <% } else { %>

     <br/>
     <strong>Link to this map with this URL:</strong> <br/>
     <a href="<%= request.getRequestURL() %>?<%= request.getQueryString() %>"><%= request.getRequestURL() %>?<%= request.getQueryString() %></a>

    <% } %>
    </p>

  </logic:notEqual> <!-- validated -->

  <logic:equal name="dataValidated" value="false">
     <bean:define id="csvdata">--invalid!--</bean:define>
    INVALID REQUEST!  The data you tried to map was not a validly formatted.  See below for how to structure this information.
  </logic:equal>
  
  <p>
  <strong>If you'd like to map new plots, please see the instructions below:</strong>
  </p>
<!--/DIV-->
</logic:present> <!-- data here -->


<p>
<strong>Option 1: Paste your data</strong>
<br/>
<form name="pasteCSVData" action="<%= request.getRequestURL() %>" method="post"><!-- action is self -->
  Paste your csv data here, in the format stated below. <br/>
  <textarea name="csvraw" cols="60" rows="6"><bean:write name="csvdata" filter="false" ignore="true"/></textarea> <br/>
  <input type="submit" value="map pasted plots" onclick="formConvertToGetIfURLLengthOK('pasteCSVData');"/>
  <input type="button" value="clear data" onclick="if (confirm('Really clear data?')) {forms.pasteCSVData.csvraw.value = '';} "/>
</form>

<hr/>

<strong>Option 2: Link to your (csv) data</strong>
  Specify where your data file is (must be publicly accessible on the internet):
  <form name="linkToCSVData" action="<%= request.getRequestURL() %>" method="get">
    <input type="text" name="latlongfile" size="80" value='<bean:write name="bean_latlongfile" ignore="true"/>' /> <br/>
    <input type="submit" value="map plots from file" />
  </form>
  

</p>
<p><strong>Format: </strong>
Plot Name can only contain numbers, letters, spaces, underscores (_) and dashes (-).
The CSV data should be structured as Plot Name, Latitude, Longitude 
where coordinates are in decimal degrees in WGS84 datum.  There should <b>not</b> be a header line.
An example:
<bean:define id="exampleFile">
xxx,35.12356,-118.43534
yyy,37.12356,-119.43534
</bean:define>
  <pre><bean:write name="exampleFile" filter="true" /></pre>

</p>



</DIV><!-- alternateBigPageWrapper -->

    @webpage_footer_html@

