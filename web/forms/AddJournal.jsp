@webpage_top_html@
@stdvegbankget_jspdeclarations@
@webpage_head_html@
  <link rel="STYLESHEET" href="@stylesheet@" type="text/css" />
  <title>Add a New Journal</title>
    @webpage_masthead_html@

    <br/>



    <table>
      <tr>
	<td>
	  
	  <html:form action="/AddJournal" onsubmit="return validateAddJournal(this)" >


	  <table align="left" border="0" width="90%" cellspacing="0" cellpadding="0">
	    <tr>
	      <td colspan="2" bgcolor="white">
		<img align="center" border="0" height="144" src="@image_server@owlogoBev.jpg" alt="[VegBank logo]" />
	      </td>
	      <td align="left" valign="middle">
		<table border="0" cellpadding="5">
		  <tr>
		    <td align="left" valign="bottom">
		      <font face="Helvetica,Arial,Verdana" size="6" color="#23238E">Add a New Journal</font>
		      <blockquote>
		      <font color="#23238E" face="Helvetica,Arial,Verdana" size="2">
		      <b>
		      <frm_instr>Use this form to add a new journal to VegBank. <br />
		      <font color="red">*</font> Indicates a required field.</frm_instr>
		      </b>
		      </font>
		      </blockquote>
		    </td>

		  </tr>
		</table>
	      </td>
	    </tr>
	    <tr>
	      <td width="10%" bgcolor="white" align="left" valign="top" />
	      <td width="5%" bgcolor="white" align="left" valign="top" />
	      <td align="left">
		<!--variables that are used by the servlet to figure out which query(s) to issue -->
		<input name="requestDataFormatType" type="hidden" value="html" />
		<input name="clientType" type="hidden" value="browser" />
		<input name="requestDataType" type="hidden" value="vegPlot" />
		<input name="resultType" type="hidden" value="identity" />
		<!--yellow group header :  back one indent -->
	      </td>
	    </tr>
	    <tr bgcolor="#FFFFCC">
	      <td width="10%">&nbsp;</td>

	      <td colspan="2">
		<p>
		<font face="Helvetica,Arial,Verdana" size="3">
		<b>&nbsp;</b>
		</font>
		</p>
	      </td>
	    </tr>
	    <tr>
	      <td colspan="2">&nbsp;</td>
	      <td>
    <html:errors/>
		<table border="0" width="100%" bgcolor="#DFE5FA">
		  <tr>
		    <td align="left" valign="top" width="5%" colspan="2">
		      <font face="Helvetica,Arial,Verdana" size="3" color="#23238E">
		      <b>Journal Details</b>
		      </font>
		    </td>
		  </tr>
		  <tr>
		    <td align="center" width="5%">
		      <img src="@image_server@icon_cat31.gif" alt="[!]" width="15" height="15" />
		    </td>
		    <td class="item">
		      <p>Please enter any information that is appropriate for the journal that you would like to add to VegBank.</p>
		    </td>
		  </tr>
		</table>

		<!--Data gathering :-->
		<table>
		  <tr>
		    <td>
		      <p>
		      <span class="category">
		      <a href="@dbdictionary_link@dd~table~referencejournal~field~journal~type~tableview.html">Journal Name</a>:<font color="red">*</font>
		      </span>
		      </p>
		    </td>
		    <td>
		      <html:text property="referenceJournal.journal" size="50"/>
		    </td>
		  </tr>
		  <tr>
		    <td colspan="2">
		      <p>
		      <span class="item">The official name of the journal.</span>
		      </p>
		    </td>
		  </tr>

		  <tr>
		    <td>
		      <p>
		      <span class="category">
		      <a href="@dbdictionary_link@dd~table~referencejournal~field~issn~type~tableview.html">ISSN</a>:</span>
		      </p>
		    </td>
		    <td>
		      <html:text property="referenceJournal.issn" size="50"/>
		    </td>
		  </tr>
		  <tr>
		    <td colspan="2">
		      <p>
		      <span class="item">International Standard Serial Number</span>
		      </p>

		    </td>
		  </tr>
		  <tr>
		    <td>
		      <p>
		      <span class="category">
		      <a href="@dbdictionary_link@dd~table~referencejournal~field~abbreviation~type~tableview.html">Journal Abbreviation</a>:</span>
		      </p>

		    </td>
		    <td>
		      <html:text property="referenceJournal.abbreviation" size="50"/>
		    </td>
		  </tr>
		  <tr>
		    <td colspan="2">
		      <p>
		      <span class="item">The standard abbreviated name for the journal, examples: Can. J. Bot./Rev. Can. Bot., JAMA</span>

		      </p>
		    </td>
		  </tr>
		</table>
		<hr size="0.5" />
		<table border="0" width="100%" bgcolor="#DFE5FA">
		  <tr>
		    <td align="left" valign="top" width="5%" colspan="2">
		      <font face="Helvetica,Arial,Verdana" size="3" color="#23238E">

		      <b>Submit to Vegbank</b>
		      </font>
		    </td>
		  </tr>
		  <tr>
		    <td align="center" width="5%">
		      <img src="@image_server@icon_cat31.gif" alt="[!]" width="15" height="15" />
		    </td>

		    <td class="item">
		      <p>Press the button on the left to submit this data to VegBank.  Press "reset" to clear this form and start over.</p>
		    </td>
		  </tr>

		</table>
		<!--Data gathering :-->

		<html:submit property="submit" value="--Add this Journal to VegBank--" /> | <html:reset/> 
		
	      </td>
	    </tr>
	    
	    
	  </table>
	  
	  
	</td>
      </tr>
      <tr>
	<td>
    
	  @webpage_footer_html@
	</td>
      </tr>
    </table>
    
  

