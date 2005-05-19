<%
int perPage = 10, pageNumber = 1;
String ri_s = request.getParameter("pageNumber");
if (ri_s != null) { pageNumber = Integer.parseInt(ri_s); }
ri_s = request.getParameter("perPage");
if (ri_s != null) { perPage = Integer.parseInt(ri_s); }
if (perPage == -1) { perPage = 0; }
                                                                                                                                                                                                  
long startIndex = (perPage * (pageNumber-1)) + 1;
long rowIndex = startIndex;
%>
