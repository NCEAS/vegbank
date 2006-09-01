
<!-- this particular style element needed to turn highlighting on and off -->
<style type="text/css" id="hop_highlightStyle">
  .hop_highlight {background-color : #FFFF33 }
</style>

<script type="text/javascript">
  
addEvent(window, "load", hop_highlightShownTextOnPageCustom);

</script>  
<!-- make control available to turn highlighting on and off -->
<bean:define id="hop_highlightcontrolBean">
  <div id="hop_highlightcontrol" class="hidden">
    <span id="hop_highlightcontrol_unhigh" class="show">
    <a href="#" onclick="enableHighlightCSS(false);return false;"><img src="@images_link@unhighlight.png" alt=""/> unhighlight</a>
    </span>
    <span id="hop_highlightcontrol_high" class="hidden">
      <a href="#" onclick="enableHighlightCSS(true);return false;"><img src="@images_link@highlight.png" alt=""/> highlight</a>
    </span>  
   <!-- <a href="#" onclick="enableHighlightCSS(false);return false;"><img src="@images_link@unhighlight.png" alt=""/> unhighlight</a> -->
  </div>
</bean:define>