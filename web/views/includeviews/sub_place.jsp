<vegbank:get id="place" select="place" beanName="map" pager="false" where="where_plot_pk" wparam="plot_pk" perPage="-1" />


<logic:notEmpty name="place-BEANLIST">
<TR class="@nextcolorclass@"><TD class="datalabel">Named Places</TD><TD>
<table class="leftrightborders" cellpadding="2" width="100%">

<logic:iterate id="onerowofplace" name="place-BEANLIST">
<tr class="@nextcolorclass@">
<%@ include file="../autogen/place_summary_data.jsp" %>
</tr>
</logic:iterate>
</table>
</TD></TR>
</logic:notEmpty>