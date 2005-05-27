@stdvegbankget_jspdeclarations@
<% String rowClass = "evenrow" ; %>

<!-- this is the raw one -->

<vegbank:get id="project" select="project" beanName="map" 
    where="where_dobscount_min" wparam="1" 
    orderBy="xorderby_maxdateentered_desc" pager="true" allowOrderBy="true"
    perPage="4" />
    
    <logic:empty name="project-BEANLIST">
    <p>Database error.  No projects found.</p>
    </logic:empty>
<logic:notEmpty name="project-BEANLIST">
<table class="leftrightborders" cellpadding="1" id="projectsummarytable">
<tr>
<th>Project</th><th>Added</th></tr>
<logic:iterate id="onerowofproject" name="project-BEANLIST">
<tr class="sizetiny @nextcolorclass@">
    <bean:define id="name_trunc"><bean:write name="onerowofproject" property="projectname" /></bean:define>
    <% if ( name_trunc.length() > 32 ) { name_trunc = name_trunc.substring(0,29) + "..." ; } %>
  <td><a href='@get_link@std/project/<bean:write name="onerowofproject" property="project_id" />'><%= name_trunc %></a></td>
  <td title='<bean:write name="onerowofproject" property="maxdateentered" />'>
      <dt:format pattern="dd-MMM-yy">
          <dt:parse pattern="yyyy-MM-dd">
              <bean:write name="onerowofproject" property="maxdateentered" />
          </dt:parse>
    </dt:format>
  </td>
</tr>
</logic:iterate>
</table>
</logic:notEmpty>