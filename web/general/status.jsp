<?xml version="1.0"?>
<?xml-stylesheet type="text/xsl" href="rss.xsl"?>
<rss version="2.0">
  <channel>
    <title>Is VegBank Up (RSS Feed)?</title>
<description>This is a simple RSS feed that gets the time from the VegBank Server, showing it is up.</description>
<lastBuildDate><%= new java.util.Date().toString()%></lastBuildDate>
    <link>@machine_url@@general_link@status.jsp</link>
	<item>
	  <title>VegBank UP @ <%= new java.util.Date().toString() %></title>
	  <pubDate><%= new java.util.Date().toString() %></pubDate>
	  <link>@machine_url@@general_link@status.jsp</link>
	</item>
  </channel>
</rss>  
