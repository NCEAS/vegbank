
@webpage_top_html@
  @stdvegbankget_jspdeclarations@
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
     
     <bean:define id="newBeanCSVLoc"><bean:write name="bean_latlongfile" /></bean:define>
     <bean:include id="csvdata" href="<%= newBeanCSVLoc %>"/>
      

     
  </logic:present> 
  <logic:present parameter="csvraw">
      <bean:parameter id="csvdata" name="csvraw" value="none" />
  </logic:present>

      <!-- dataValidated prevents someone from escaping the textarea and inserting random code into this -->
    <logic:present name="csvdata">  
      <script type="text/javascript">
      function VbGLoadAllMapsThisPage() {
        <logic:notMatch name="csvdata" value="<">
          <bean:define id="dataValidated" value="true" />
          var bigmap = VbGMapCSV(document.getElementById("domCSVData").value,"map",35,36,-127,-126, true, true, "zoomInMap") ;
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
     
     </logic:present>
     
 @webpage_masthead_html@ 
  @possibly_center@  


<h2>Map YOUR plots</h2>


<!-- see if there is even data defined.  If not, this page is a menu-->
<logic:notPresent name="csvdata">
  <!-- there is no data, just display menu -->
  <p>This page will map your plots from a CSV file that is publicly accessible.  <strong>They are NOT plots in VegBank.  </strong>
  </p>
</logic:notPresent>

<logic:present name="csvdata"><!-- we have data -->



  <logic:notEqual name="dataValidated" value="false"> <!-- and it is valid -->

    <!-- dont forget a place to put the google map: -->
    <div style="width: 700px" id="googleMapPageContainer">
      <div id="map" style="width: 100%; height: 500px"></div>
    </div> 
  
    <bean:define id="dataValidated" value="true" /><!-- tell that this is ok -->
    <textarea id="domCSVData" class="hidden"><bean:write name="csvdata" filter="false" /></textarea>


      <logic:notEmpty name="bean_latlongfile">
        You are mapping plots from this URL: 
        <bean:write name="bean_latlongfile" />
      </logic:notEmpty>
  
  <div id="zoomInMap" class="hidden" style="clear:both;position:relative; width:600px;height:450px; overflow:hidden; border:medium double rgb(0,0,0); ">
    <iframe style="position: absolute; left:-191px; top:-167px; width: 791px; height: 617px; border: 0px none ; margin: 0px; overflow: hidden; " frameborder="0" scrolling="no"></iframe>
    <div style="position:absolute; left:0px; top:0px; width:100%; height:100%; z-index:10">
      <span id="zoomInPlotName" style="position:absolute;bottom:0px; left:0px; background-color:#CCCCCC">no plot selected</span>
      <a id="zoomInCloseLink" style="position:absolute;top:0px;right:0px;background-color:#CCCCCC" href="#" onclick="VbGCloseZoomMap('zoomInMap');return false;">close</a>
      <a id="zoomInMapLink" style="position:absolute;bottom:0px; right:0px; background-color:#CCCCCC" target="_new" href="#">bigger&gt;</a>
    </div>
  </div>
  
<DIV style="width:700px" style="clear:both;"><!-- start a container for rest of page -->
   <!-- all this page does is iterate through the plots and generate js to map them -->
  <div style="float:right" id="whereismap_wrapper">
    Initial location of larger map outlined here: </br/>    
    <div id="whereismap" style="width: 190px; height: 100px"></div>      
  </div>

  </logic:notEqual> <!-- validated -->

  <logic:equal name="dataValidated" value="false">
     <bean:define id="csvdata">--invalid!--</bean:define>
    INVALID REQUEST!  The data you tried to map was not a validly formatted.  See below for how to structure this information.
  </logic:equal>
  
  <p>
  <strong>If you'd like to map new plots, please see the instructions below:</strong>
  </p>
</DIV>
</logic:present> <!-- data here -->


<p>
<strong>Option 1: Paste your data</strong>
<br/>
<form name="pasteCSVData" action="<%= request.getRequestURL() %>" method="post"><!-- action is self -->
  Paste your csv data here, in the format stated below.
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

<p><strong>Linking to this page</strong><br/>
You can map plots, then link to this page with your plots mapped.  If you have a large number of plots, you may have 
to use an external file due to URL length limitations.

<br/>

<% if (Utility.isStringNullOrEmpty( request.getQueryString() )) { %>
 <strong> You are mapping too many plots to link via a URL.</strong>
 <br/>You can still send someone the link to this page: 
 <pre><a href="<%= request.getRequestURL() %>"><%= request.getRequestURL() %></a></pre>
 <br/> and instruct them to paste in the plot locations: <br/>
 <pre><bean:write name="csvdata" filter="false" ignore="true"/><pre>
<% } else { %>

 <br/>
 <strong>Link to this map with this URL:</strong> <br/>
 <a href="<%= request.getRequestURL() %>?<%= request.getQueryString() %>"><%= request.getRequestURL() %>?<%= request.getQueryString() %></a>
 
<% } %>
</p>

    @webpage_footer_html@

