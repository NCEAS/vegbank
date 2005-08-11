@stdvegbankget_jspdeclarations@<%
AccessionCode ac = new AccessionCode(request.getParameter("wparam"));
String table = ac.getEntityName();
if (table == null || table.equals("")) {
    out.print("0"); 
} else {
    String vselect = table + "_recordid";
%><vegbank:get id="verify" select="<%= vselect %>" where="where_accessioncode" /><logic:notEmpty name="verify-BEAN"><bean:write name="verify-BEAN" property="record_id" /></logic:notEmpty><logic:empty name="verify-BEAN">0</logic:empty><%
}
%>
