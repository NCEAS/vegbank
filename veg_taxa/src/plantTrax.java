import java.io.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.math.*;
import java.net.URL;


public class plantTrax extends HttpServlet {
ResourceBundle rb = ResourceBundle.getBundle("plantTrax");

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
        throws IOException, ServletException
    {


/*next lines are the DB login variables*/
String driver_class = "oracle.jdbc.driver.OracleDriver";
String connect_string = "jdbc:oracle:thin:@128.111.220.63:1521:exp";
String login = "harris";
String passwd = "use4dev";
Connection conn=null;
Statement query = null;
Statement stmt = null;

response.setContentType("text/html");
PrintWriter out = response.getWriter();

out.println("<html>");
out.println("<body>");
out.println("<head>");
String title =("plantTrax");
out.println("<title>" + title + "</title>");
out.println("</head>");
out.println("<body bgcolor=\"white\">");
out.println("<h3>" + title + "</h3>");

out.println("<p>");
out.println("<img alt=\"this is not the final image\" src=\"/harris/owlogo.jpg\" border=\"0\">");
out.println("<p>");

String firstName = request.getParameter("firstname");
String lastName = request.getParameter("lastname");
int i=0;
out.println(rb.getString("requestparams.params-in-req") + "<br>");

if (firstName != null || lastName != null) {
    out.println(rb.getString("requestparams.firstname"));
    out.println(" = " + firstName + "<br>");
    out.println(rb.getString("requestparams.lastname"));
    out.println(" = " + lastName);
		//for (i = 0; i < 5; i++) {out.println(firstName);} //end for

/*Query the DB*/

try {
Class.forName(driver_class);
out.println("Trying to connect...<br>");
if( conn == null)
	conn = DriverManager.getConnection (connect_string, login, passwd);
	query = conn.createStatement ();
	stmt = conn.createStatement ();
	out.println("Connected.<br>");
	}
catch ( Exception e ){out.println("did not connect "+e.getMessage());}



/*query the database*/
ResultSet results = null;
String commonName=null;
String name=null;
String symbol=null;
String nameAuthor=null;
String party=null;
String orgName=null;
String family=null;
int name_usage_id=0;
int circumId=0;
int partyId=0;
String circumAuthor=null;
String citation=null;
String  refSpeciesName=null;
String refType=null;





/*get the basic name information*/
try {
out.println(" query " + lastName+ "<br>");
results = query.executeQuery("select name, symbol, commonname, family, AUTHORNAME  from name where name like '%"+firstName+"%'");

while (results.next()) {
	name = results.getString("name");
	symbol= results.getString("symbol");
	commonName= results.getString("commonname");
	nameAuthor=results.getString("AUTHORNAME");
	family=results.getString("family");

	out.println("<b>Selection results:</b> <br>"); 
	out.println("Taxon name: <i>"+name+"</i><br>  \n");
	out.println("                   Symbol: <i> "+symbol+"</i><br> \n");
	out.println("                   Common  name: <i>"+commonName+" </i><br>\n");
	out.println("                   Family: <i>"+family+" </i><br>\n");
	out.println("                   Taxon author: <i>"+nameAuthor+" </i><br>\n");
	out.println("</i><P>");


} //end while
} //end try
catch ( Exception e ){out.println("The Query Jumbled"+e.getMessage());}

/*query for the circumscription information*/
try {

results = query.executeQuery("select NAMEUSAGEID, PARTY_ID, CIRCUM_ID  from name_usage where (select name_id from name where name like '%"+firstName+"%')=name_id");


while (results.next()) {
	name_usage_id=results.getInt("NAMEUSAGEID");
	circumId=results.getInt("CIRCUM_ID");
	partyId=results.getInt("PARTY_ID");
} //end  while

/*establish the party using the name*/
results = query.executeQuery("select ORG_NAME from party where party_id ="+partyId);
while (results.next()) {
	orgName= results.getString("ORG_NAME");
} //end while

out.println("                   Organization  Name: <i>"+orgName+" </i><br>\n");


/*establish the reference information from the circumscription id number*/
results = query.executeQuery("select AUTHOR, CITATION, SPECIESNAME, CIRCUMTYPE  from reference where (select REF_ID from CIRCUMSCRIPTION where CIRCUM_ID ="+circumId+")=REF_ID");

while (results.next()) {
        circumAuthor=results.getString("AUTHOR");
	citation=results.getString("CITATION");
	refSpeciesName=results.getString("SPECIESNAME");
	refType=results.getString("CIRCUMTYPE");

out.println("                   Circumsription Author: <i>"+circumAuthor+" </i><br>\n");
out.println("                   Citation: <i>"+citation+" </i><br>\n");
out.println("                   Taxon Name Used in Citation:  <i>"+refSpeciesName+" </i><br>\n");
out.println("                   Type of Reference Stored:  <i>"+refType+" </i><br>\n");

} //end while
} //end try
catch ( Exception e ){out.println("The Query Jumbled - circumscription "+e.getMessage());}







} //end if
 else {
            out.println(rb.getString("requestparams.no-params"));
        }
out.println("<P>");
out.print("<form action=\"");
out.print("plantTrax\" ");   /*make sure to have this pointing to the right servlet6*/
out.println("method=POST>");
out.println("Enter taxon");
out.println("<input type=text size=20 name=firstname>");
out.println("<br>");
out.println("<br>");
out.println("<input type=submit>");
out.println("</form>");
out.println("</body>");
out.println("</html>");


    }

    public void doPost(HttpServletRequest request,
                      HttpServletResponse response)
        throws IOException, ServletException
    {
        doGet(request, response);
    }

}
