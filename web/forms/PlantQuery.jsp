<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>


<html>
<!-- 
  *   '$RCSfile: PlantQuery.jsp,v $'
  *     Purpose: web form to query the plant taxonomy portion of vegbank
  *   Copyright: 2000 Regents of the University of California and the
  *               National Center for Ecological Analysis and Synthesis
  *     Authors: @author@
  *
  *    '$Author: farrell $'
  *      '$Date: 2003-12-05 22:45:31 $'
  *  '$Revision: 1.1 $'
  *
  *
  -->

<head>
  <meta name="generator" content=
  "HTML Tidy for Linux/x86 (vers 1st November 2003), see www.w3.org">

  <title>PLANT CONCEPT LOOKUP</title>
  <link rel="stylesheet" href="@stylesheet@" type="text/css">
  <script type="text/javascript">
        <!--
        function getHelp()
        {
        return "@manualFrameFor-search-for-plants@"
        }
        
        -->
  </script>
  <style type="text/css">
  body {
  background-color: #FFFFFF;
  color: #531100;
  }
  :link { color: #0033CC }
  :visited { color: #005680 }
  :active { color: #0066FF }
  td.c4 {color: #000000; font-family: Helvetica,Arial,Verdana}
  span.c3 {color: black; font-family: Helvetica,Arial,Verdana; font-size: 70%}
  span.c2 {color: #209020; font-family: Helvetica,Arial,Verdana; font-size: 80%}
  span.c1 {color: #23238E; font-family: Helvetica,Arial,Verdana; font-size: 200%}
  </style>
</head>

<body>
  @vegbank_header_html_normal@ <!-- SECOND TABLE -->

  <table align="left" border="0" width="90%" cellspacing="0" cellpadding="0">
    <tr>
      <td bgcolor="white"><img align="center" border="0" height="100" src=
      "@image_server@owlogoBev.jpg" alt="Veg plots logo"></td>

      <td align="left" valign="middle">
        <table border="0" cellpadding="5" width="366" height="55">
          <tr>
            <td align="left" valign="bottom"><span class="c1">Plant Concept
            Lookup</span><br></td>
          </tr>
        </table>
      </td>
    </tr>

    <tr>
      <!-- LEFT MARGIN -->
      <td width="15%" bgcolor="white" align="left" valign="top">

      <td align="left">
        <html:form action="/PlantQuery">
          <table>
            <tr valign="top">
              <td align="left" valign="middle" colspan="2">
                <span class="c2"><b>Enter the taxon name below and toggle the
                correct type and level of the taxon.</b></span>

                <p>
			<small>
				<html:submit value="search for plants"/>
                		&nbsp;&nbsp; 
				<html:reset value="reset"/>
			</small>
		</p>
              </td>
            </tr>
          </table>

          <p>&nbsp;</p><br>
          <br>

          <table border="0" width="722">
            <!--TAXON NAME -->

            <tr align="left">
              <td width="156"><b>Taxon name:</b></td>
              <!--        PLANT TAXON INPUT-->

              <td width="556">
		<html:text size="35" property="plantname"/>
              	&nbsp;
	      </td>
            </tr><!-- IGNORE CASE -->

            <tr align="left">
              <td width="156"></td>

              <td width="556"><span class="c3">(wildcard = '%')<br>
              All Queries are Case-Insensitive</span> 
              <!-- <input type = "checkbox" name="ignoreCase" value = "true"> Ignore Case<br> --></td>
            </tr><!-- HORIZONTAL LINE -->

            <tr>
              <td colspan="2" rowspan="1" align="left" valign="middle">
                <hr size=".5">
              </td>
            </tr><!-- TAXON TYPE -->

            <tr>
              <td width="156" align="left" valign="top"><b>Taxon type:</b></td>

              <td class="c4" align="left" valign="top" width="556">
		<html:radio property="nameType" value=""/>Any<br/>
                <html:radio property="nameType" value="scientific"/>Scientific name<br/>
                <html:radio property="nameType" value="common"/>Common name<br/>
                <html:radio property="nameType" value="code"/>Code<br/>
              </td>
            </tr><!-- HORIZONTAL LINE -->

            <tr>
              <td colspan="2" rowspan="1" align="left" valign="middle">
                <hr size=".5">
              </td>
            </tr><!-- TAXON LEVEL -->

            <tr>
              <td width="156" align="left" valign="top"><b>Taxon
              level:</b></td>

              <td class="c4" align="left" valign="top" width="556">
                <html:radio property="taxonLevel" value=""/> Any<br/>
                <html:radio property="taxonLevel" value="family"/>Family<br>
                <html:radio property="taxonLevel" value="genus"/>Genus<br>
                <html:radio property="taxonLevel" value="species"/>Species<br>
                <html:radio property="taxonLevel" value="subspecies"/>Sub-species<br>
                <html:radio property="taxonLevel" value="variety"/>Variety
                <span class="itemsmall">
                  -- Names with variety are of form "Genus species var. variety"
                </span>
                <br/>
                <html:radio property="taxonLevel" value="hybrid"/>Hybrid<br>
              </td>
            </tr><!-- HORIZONTAL LINE -->

            <tr>
              <td colspan="2" rowspan="1" align="left" valign="middle">
                <hr size=".5">
              </td>
            </tr>

            <tr>
              <td width="156" align="left" valign="top" height="20">
              <b>Date:</b></td>

              <td class="c4" align="left" valign="top" height="20" width="556">
               <html:text property="nameExistsBeforeDate"/> 
                 <span class="itemsmall">
                   -- If left blank, today's date will be used 
                   <a href="@help-for-concept-date-href@"><img height="14" width="14" border="0" src="@image_server@question.gif"></a>
                 </span>
              </td>
            </tr>

            <tr>
              <td width="156"></td>
              <td class="c4" width="556" align="left" valign="top" height="20"
              colspan="2"><span class="itemsmall">(Format: DD-MMM-YYYY, like
              12-AUG-2002)</span></td>
            </tr>

            <tr>
              <td colspan="2" rowspan="1" align="left" valign="middle">
                <hr size=".5">
              </td>
            </tr><!-- PARTY -->

            <tr>
              <td width="156" align="left" valign="top" height="54">
              <b>Party:</b></td>

              <td class="c4" align="left" valign="top" height="54" width="556">
                <html:select property="accordingToParty">
                  <option value="">All</option>
                  <option value="usda">USDA</option>
                </html:select>
              </td>
            </tr>

            <tr>
              <td align="left" valign="middle" colspan="2">
                <small>
                  <html:submit value="search for plants"/> &nbsp;&nbsp; 
                  <html:reset value="reset"/>
                </small>
              </td>
            </tr>

            <tr>
              <td width="156"></td>
            </tr>
          </table>
        </html:form>

        <table border="0" cellspacing="5" cellpadding="5">
          <tr>
            <td colspan="2"><!-- VEGBANK FOOTER -->
            @vegbank_footer_html_tworow@</td>
          </tr>
        </table>
      </td>
    </tr>
  </table>
</body>
</html>
