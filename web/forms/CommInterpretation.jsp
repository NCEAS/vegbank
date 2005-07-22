@webpage_top_html@
  @stdvegbankget_jspdeclarations@
  <%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
  @webpage_head_html@
<bean:parameter id="obsId" name="obsId"/>
<bean:define id="isCommInterp" value="true"/>
<style type="text/css"> .interpCommLink {display:none} </style>

<script type="text/javascript" src="@includes_link@comm_interp.js"></script>

<title>Interpret Plot</title>
 
  
  @webpage_masthead_html@ 
  
  <h2>Interpret Plot</h2>
<logic:messagesPresent message="false">
<table border="0" width="70%"><tr><td>
<h3><font color="red">Please Try Again</font></h3>
	<ul>
	<html:messages id="error" message="false">
		<li><bean:write name="error"/></li>
	</html:messages>
	</ul>
	<hr/>
</td></tr></table>
<br/>
</logic:messagesPresent>
<logic:messagesPresent property="saved" message="true">
<table border="0"><tr><td>
	<ul>
	<html:messages id="msg" property="saved" message="true">
		<li><bean:write name="msg"/></li>
	</html:messages>
	</ul>
	<hr/>
</td></tr></table>
<br/>
</logic:messagesPresent>



<vegbank:get id="plotobs" select="plotandobservation" where="where_observation_pk" 
    beanName="map" pager="false" wparam="obsId"/>
<vegbank:get id="reference" select="reference_min"/>

<script language="javascript">
</script>


<html:form action="/SaveCommInterpretation.do" method="get">
<bean:parameter id="pObsId" name="obsId" value="0"/>


<table class="thinlines">
<tr bgcolor="#666666"><th colspan="2">What:</th></tr>
<tr><td class="listhead">Plot</td><td class="item"><bean:write name="plotobs-BEAN" property="authorobscode" />
<html:hidden value="<%= pObsId %>" property="obsId"/>

<a href="javascript:popupObs()">See plot</a></td></tr>
    

<!--tr><td>You are:</td><td class="item"><input text disabled="true" value="Joe Schmoe"/></td></tr-->

<tr><th bgcolor="#666666" colspan="2">Method of Classification:</th></tr>
<tr><td class="listhead">Inspection</td><td class="item"><html:checkbox property="commclass.inspection" value="true" /></td></tr>
<tr><td class="listhead">Tabular Analysis</td><td class="item"><html:checkbox property="commclass.tableanalysis" value="true" /></td></tr>
<tr><td class="listhead">Multivariate Analysis</td><td class="item"><html:checkbox property="commclass.multivariateanalysis" value="true" /></td></tr>
<tr><td class="listhead">Expert System (desc. in notes)</td><td class="item"><html:checkbox property="commclass.expertsystem" value="true" /></td></tr>
<tr><td class="listhead">Classification publication</td>
    <td class="item">
    <html:select property="commclass.classpublication_id">
            <option value="" selected="selected">--none--</option>
        <logic:iterate id="ref" name="BEANLIST">
            <option value='<bean:write name="ref" property="reference_id"/>'><bean:write name="ref" property="reference_id_transl"/></option>
        </logic:iterate>
        </html:select>
        <!--br/><a href="javascript:popupAddReference()">Add reference</a--></td></tr>

<tr><td class="listhead">Notes</td><td class="item"><html:textarea property="commclass.classnotes" rows="4" cols="45"></html:textarea></td></tr>
<!--tr><th bgcolor="#666666" colspan="2">Communities to which this plot belongs:</th></tr-->
</table>

<h3>Choose communities to which this plot belongs</h3>
<!-- DHTML adds and removes these community interpretation entries -->
<!-- STATIC INTERP TEMPLATE -->
<div id="record_container">
<!-- all records go here -->
</div>

<!-- BEGIN ADD FORM -->
<div id="add_form" class="block" style="margin-top: 10px" onchange="setAddFormChanged()">
    <a named="add_form_anchor" id="add_form_anchor" />
    <input id="af_rec_id" type="hidden" name="tmp_rec_id" />

    <table class="thinlines">
    <tr><td class="listhead2">Community accession code</td>
        <td class="item"><input id="af_commconcept_ac" type="text" name="tmp_commconcept_ac" size="28"/>
        <input type="button" onclick="popupCommLookup(this.form.name,'tmp_commconcept_ac')" value="Lookup" /></td></tr>

    <tr><td class="listhead2">Class fit</td>
        <td class="item"><select id="af_classfit" name="tmp_classfit">
            <option value="" selected="selected">--none--</option>
            @VB_INSERT_CLOSEDLIST_comminterpretation.classfit@</select></td></tr>

    <tr><td class="listhead2">Class confidence</td>
        <td class="item"><select id="af_classconfidence" name="tmp_classconfidence">
            <option value="" selected="selected">--none--</option>
            @VB_INSERT_CLOSEDLIST_comminterpretation.classconfidence@</select></td></tr>


    <tr><td class="listhead2">Reference</td>
        <td class="item">
        <select id="af_commauthority_id" name="tmp_commauthority_id">
            <option value="" selected="selected">--none--</option>
        <logic:iterate id="ref" name="BEANLIST">
            <option value='<bean:write name="ref" property="reference_id"/>'><bean:write name="ref" property="reference_id_transl"/></option>
        </logic:iterate>
        </select>
        <!--br/><a href="javascript:popupAddReference()">Add reference</a--></td></tr>

    <tr><td class="listhead2">This is a typal plot of this community?</td>
        <td class="item"><input type="checkbox" id="af_type" name="tmp_type" value="true" /></td></tr>

    <tr><td class="listhead2">This is a typal plot of the nomenclature (Braun-Blanquet)?</td>
        <td class="item"><input type="checkbox" id="af_nomenclaturaltype" name="tmp_nomenclaturaltype" value="true" /></td></tr>

    <tr><td class="listhead2">Notes</td>
        <td class="item"><textarea id="af_notes" name="tmp_notes" rows="4" cols="45"></textarea></td></tr>

    <tr><td colspan="2" class="item">
        <input type="button" value="save this community" onclick="javascript:saveForm(); closeAddForm(); return false;"/>
        &nbsp; &nbsp;
        <input type="button" value="cancel" onclick="javascript:closeAddForm(); return false;"/>
    </td></tr>
    </table>
</div>
<!-- END ADD FORM -->

<p id="add_another_link" style="display: none"><a href="#add_form_anchor" onclick="javascript:resetAddForm();openAddForm();return true">Add another community</a></p>
<br />
<div id="submit_button_msg">Please complete the community interpretation form for at least one community.</div>
<div>
  <br />
    &nbsp; &nbsp; &nbsp; &nbsp;
  <html:submit property="submit" value="Submit Interpretation" disabled="true" styleId="submit_button" />
	&nbsp; &nbsp; &nbsp; &nbsp;
  <html:cancel/>
</div>

  </html:form>

<!-- BEGIN STATIC ROW TEMPLATE -->
<div id="record_template" style="display: none; margin-bottom: 10px">
    <span class="control_tab">
        <a href="#add_form_anchor" onclick="javascript: editRecord(event); return true;">edit</a> |
        <a href="#" onclick="javascript: toggleRecordDisplayByEvent(event); return false;">hide</a> |
        <a href="#" onclick="javascript: lookupComm(event); return false;">lookup</a> |
        <a href="#" onclick="javascript: deleteRecord(event); return false;">delete</a>
        &nbsp; &nbsp;
        <span id="title"></span>
    </span>
    <br/>


    <div class="block" >
    <table class="thinlines">
    <tr><td class="listhead">Community accession code</td><td class="item">
        <input id="commconcept_ac" type="hidden" name="tpl_commconcept_ac" size="28"/><span></span></td></tr>

    <tr><td class="listhead">Class fit</td>
        <td class="item"><input id="classfit" type="hidden" name="tmp_classfit"/><span></span></td></tr>

    <tr><td class="listhead">Class confidence</td>
        <td class="item"><input id="classconfidence" type="hidden" name="tpl_classconfidence"/><span></span></td></tr>

    <tr><td class="listhead">Reference</td>
        <td class="item"><input id="commauthority_id" type="hidden" name="tpl_commauthority_id"/><span></span></td></tr>

    <tr><td class="listhead">This is a typal plot of this community?</td>
        <td class="item"><input id="type" type="hidden" name="tpl_type"/><span></span></td></tr>

    <tr><td class="listhead">This is a typal plot of the nomenclature <span class="small">(Braun-Blanquet)</span>?</td>
        <td class="item"><input id="nomenclaturaltype" type="hidden" name="tpl_nomenclaturaltype"/><span></span></td></tr>

    <tr><td class="listhead">Notes</td><td class="item"><input id="notes" type="hidden" name="tpl_notes"/><span></span></td></tr>
    </table>
    </div>
</div>
<!-- END STATIC ROW TEMPLATE -->


<!-- extant interps -->
<br/> <br/> <br/>
<h3>Other Interpretations</h3>
<% String rowClass = "evenrow"; %>
<vegbank:get id="commclass" select="commclass" beanName="map" pager="false" where="where_observation_pk" wparam="obsId" perPage="-1" />
<%@ include file="../views/includeviews/sub_commclass_summary.jsp" %>
 

  @webpage_footer_html@
