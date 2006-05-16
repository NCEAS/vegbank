<%@ page contentType="text/html;charset=UTF-8" language="java" import="org.vegbank.common.utility.*,java.util.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/vegbank.tld" prefix="vegbank" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<bean:define id="thisviewid" value="global_" />
<%  Long longWebUserId = (Long)(request.getSession().getAttribute("USER")); 
  String strWebUserId = "-1"; 
  if (longWebUserId!=null) { 
    strWebUserId = longWebUserId.toString();
  } %> 
  <%      String rowClass = "evenrow";   %>

