<bean:define value="false" id="hadData"/>
<tr>
<th colspan="2" class="major">
<logic:notEmpty property="salutation" name="onerowofparty">
<bean:write property="salutation" name="onerowofparty"/>
<bean:define value="true" id="hadData"/>
</logic:notEmpty>

<logic:notEmpty property="givenname" name="onerowofparty">
<bean:write property="givenname" name="onerowofparty"/>
<bean:define value="true" id="hadData"/>
</logic:notEmpty>

<logic:notEmpty property="middlename" name="onerowofparty">
<bean:write property="middlename" name="onerowofparty"/>
<bean:define value="true" id="hadData"/>
</logic:notEmpty>

<logic:notEmpty property="surname" name="onerowofparty">
<bean:write property="surname" name="onerowofparty"/>
<bean:define value="true" id="hadData"/>
</logic:notEmpty>

<logic:equal name="hadData" value="false">
	<logic:notEmpty property="organizationname" name="onerowofparty">
		<bean:write property="organizationname" name="onerowofparty"/>
	</logic:notEmpty>
	<logic:empty property="organizationname" name="onerowofparty">
		Unnamed Party
	</logic:empty>
</logic:equal>
</th>
</tr>

<logic:notEmpty property="partytype" name="onerowofparty">
<tr class="@nextcolorclass@">
<td class="datalabel">Party Type</td>
<!--WRITE FIELD: partytype and att is: partyType--><td>
<logic:notEmpty property="partytype" name="onerowofparty"><span>
<bean:write property="partytype" name="onerowofparty"/></span>
</logic:notEmpty>&nbsp;</td>
</tr>
<bean:define value="true" id="hadData"/>
</logic:notEmpty>


<logic:notEmpty property="organizationname" name="onerowofparty">
<tr class="@nextcolorclass@">
<td class="datalabel">Organization Name</td>
<!--WRITE FIELD: organizationname and att is: organizationName--><td>
<logic:notEmpty property="organizationname" name="onerowofparty"><span class="sizetiny">
<bean:write property="organizationname" name="onerowofparty"/></span>
</logic:notEmpty>&nbsp;</td>
</tr>
<bean:define value="true" id="hadData"/>
</logic:notEmpty>
<logic:notEmpty property="currentname_id" name="onerowofparty">
<tr class="@nextcolorclass@">
<td class="datalabel">Current Name</td>
<!--WRITE FIELD: currentname_id and att is: currentName_ID--><td>
<logic:notEmpty property="currentname_id" name="onerowofparty"><span>
<bean:write property="currentname_id" name="onerowofparty"/></span>
</logic:notEmpty>&nbsp;</td>
</tr>
<bean:define value="true" id="hadData"/>
</logic:notEmpty>
<logic:notEmpty property="contactinstructions" name="onerowofparty">
<tr class="@nextcolorclass@">
<td class="datalabel">Contact Instructions</td>
<!--WRITE FIELD: contactinstructions and att is: contactInstructions--><td>
<logic:notEmpty property="contactinstructions" name="onerowofparty"><span class="sizetiny">
<bean:write property="contactinstructions" name="onerowofparty"/></span>
</logic:notEmpty>&nbsp;</td>
</tr>
<bean:define value="true" id="hadData"/>
</logic:notEmpty>
<logic:notEmpty property="email" name="onerowofparty">
<tr class="@nextcolorclass@">
<td class="datalabel">Email</td>
<!--WRITE FIELD: email and att is: email--><td>
<logic:notEmpty property="email" name="onerowofparty"><span class="sizetiny">
<a href="mailto:<bean:write property="email" name="onerowofparty"/>"><bean:write property="email" name="onerowofparty"/></a></span>
</logic:notEmpty>&nbsp;</td>
</tr>
<bean:define value="true" id="hadData"/>
</logic:notEmpty>
<logic:notEmpty property="accessioncode" name="onerowofparty">
<tr class="@nextcolorclass@">
<td class="datalabel">Accession Code</td>
<!--WRITE FIELD: accessioncode and att is: accessionCode--><td>
<logic:notEmpty property="accessioncode" name="onerowofparty"><span class="sizetiny">
<bean:write property="accessioncode" name="onerowofparty"/></span>
</logic:notEmpty>&nbsp;</td>
</tr>
<bean:define value="true" id="hadData"/>
</logic:notEmpty>
