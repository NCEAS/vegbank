    <!-- References Control -->
    <jsp:useBean id="references" class="java.sql.ResultSet" scope="application"/>
    <%@ page language="java" import="java.sql.*"%>

    <table>
      <tr>
        <td class="formLbl">reference:</td>
        <td> 
          <select name="reference_ID"> 
            <option value="-1" selected></option>
            <%
          	  while (references.next() )
		          {
			          String id = references.getString(1);
			          String name = references.getString(2);
          			out.println("<option value='" + id +"'>" + name + "</option>");
		          }
              // Reset the cursor back to the begining of the resultset
              references.beforeFirst();
            %>
          </select>  
        </td>
      </tr>
      <tr>
        <td>
          &nbsp;
        </td>
        <td>
          <input name="add_newReference" value="add a new reference" type="submit" />
        </td>
      </tr>
    </table>
    <!-- End References Control -->
