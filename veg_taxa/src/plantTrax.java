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

/*
*database specific variables
*/
ResultSet results = null;
String commonName=null;
String name=null;
int name_id=-999;
String symbol=null;
String nameAuthor=null;
String party=null;
String orgName=null;
String family=null;
int name_usage_id=-999;
int circumId=-999;
int partyId=-999;
String circumAuthor=null;
String citation=null;
String  refSpeciesName=null;
String refType=null;
String correlationRefSpeciesName=null;
String correlationTaxon=null;
String correlationCommonName=null;
String correlationSymbol=null;

String curStatus=null;
int partyCircumId=-999;
int correlationId=-999;
String correlationAuthor=null;
String convergence=null;
String acceptedTaxon=null;
String acceptedSymbol=null;
String acceptedRefSpeciesName=null;
String acceptedAuthor=null;
String acceptedCommonName=null;


/*start the output to the browser*/
out.println("<html>");
out.println("<body>");
out.println("<head>");
String title =("plantTrax - Interface to Plant Taxa Database");
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
	out.println("<FONT COLOR=green>");
	out.println("Party Perspective: "+lastName+ "<br>");
	out.println("Name Entered: "+firstName+"<br>");
	out.println("</FONT>");
	out.println("<br>");
	out.println("<b>Selection results:</b> ");


/*Make connection to the DB*/
try {
//PrintStream outfile  = new PrintStream(new FileOutputStream("/opt/tomcat/webapps/harris/results.html"));
PrintStream outfile  = new PrintStream(new FileOutputStream("C://jakarta-tomcat/webapps/examples/WEB-INF/lib/results.html", true));

Class.forName(driver_class);
if( conn == null)
	conn = DriverManager.getConnection (connect_string, login, passwd);
	query = conn.createStatement ();
	stmt = conn.createStatement ();
	outfile.println("Database Connection: dev.nceas.ucsb.edu:exp <br>");
	out.println("<br>");
	}
catch ( Exception e ){out.println("did not connect "+e.getMessage());}



/*
* query the database for all the names similar to the name given at the browser
*/
try {
//PrintStream outfile  = new PrintStream(new FileOutputStream("/opt/tomcat/webapps/harris/results.html", true));
PrintStream outfile  = new PrintStream(new FileOutputStream("C://jakarta-tomcat/webapps/examples/WEB-INF/lib/results.html", true));
results = query.executeQuery("select name_id, name, symbol, commonname, family, "
	+" AUTHORNAME  from name where name like '%"+firstName+"%'");
int ii=0;
while (results.next()) {
	ii++;
	name_id=results.getInt("name_id");
	name = results.getString("name");
	symbol= results.getString("symbol");
	commonName= results.getString("commonname");
	nameAuthor=results.getString("AUTHORNAME");
	family=results.getString("family");

	outfile.println("Taxon name: <i>"+name+"</i><br>  \n");
	outfile.println("                   Symbol: <i> "+symbol+"</i><br> \n");
	outfile.println("                   Common  name: <i>"+commonName+" </i><br>\n");
	outfile.println("                   Family: <i>"+family+" </i><br>\n");
	outfile.println("                   Taxon author: <i>"+nameAuthor+" </i><br>\n");
	outfile.println("</i><P>");
} //end while

/*if more than one names are returned warn the user*/
if (ii>1) {
	out.println("<FONT COLOR=red>");
	out.println(ii+" taxa found matching search criteria <br>");
	out.println("using taxon: "+name+" for the circumscription lookup <br>");
	out.println("</FONT>");
	} //end if 

} //end try
catch ( Exception e ){out.println("The Query Jumbled at the first block - looking for names"+e.getMessage());}



/*
* query for the circumscription and correlation information information at this point only one name and
* name_id should be analyzed at a time
*/
try {
//PrintStream outfile  = new PrintStream(new FileOutputStream("/opt/tomcat/webapps/harris/results.html", true)); //append mode
PrintStream outfile  = new PrintStream(new FileOutputStream("C://jakarta-tomcat/webapps/examples/WEB-INF/lib/results.html", true));

/*select all the usages of the name as provided in the browser - later the name_id's should come from above block*/
//results = query.executeQuery("select NAMEUSAGEID, PARTY_ID, CIRCUM_ID  from name_usage where (select name_id from name where name like '%"+firstName+"%')=name_id");

results = query.executeQuery("select NAMEUSAGEID, PARTY_ID, CIRCUM_ID  from name_usage where name_id="+name_id);

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

outfile.println("Organization  Name: <i>"+orgName+" </i><br>\n");


/*establish the reference information from the circumscription id number - this is the reference information for the circumscription, not the original authoring refernce*/
results=query.executeQuery("select AUTHOR, CITATION, SPECIESNAME, CIRCUMTYPE  from reference where (select REF_ID from CIRCUMSCRIPTION where CIRCUM_ID ="+circumId+")=REF_ID");

while (results.next()) {
        circumAuthor=results.getString("AUTHOR");
	citation=results.getString("CITATION");
	refSpeciesName=results.getString("SPECIESNAME");
	refType=results.getString("CIRCUMTYPE");

outfile.println("                   Circumsription Author: <i>"+circumAuthor+" </i><br>\n");
outfile.println("                   Citation: <i>"+citation+" </i><br>\n");
outfile.println("                   Taxon Name Used in Citation:  <i>"+refSpeciesName+" </i><br>\n");
outfile.println("                   Type of Reference Stored:  <i>"+refType+" </i><br>\n");
} //end while

/*
* gain the status info-add a second variable when the prototype grows
* if this status is standard for the desired party then return the accepetd
* name and related information which is the information in the above circumscription
* otherwise look in the correlation table for a correlation containing the information
* and where this circum is equal/subset of the other 
*/
results = query.executeQuery("select status, PARTY_CIRCUM_ID  from status where CIRCUM_ID ="+circumId);

while (results.next()) {
        curStatus=results.getString("status");
	partyCircumId=results.getInt("PARTY_CIRCUM_ID");
outfile.println("                   CurrentStatus: <i>"+curStatus+" </i><br>\n");
outfile.println("                   partyCircumId: <i>"+partyCircumId+" </i><br>\n");
outfile.println("<br> \n");
} //end  while


/*If this is the standrd Circumscription then go no futher*/
if (curStatus.equals("STD")) {
	acceptedTaxon=name;
	acceptedSymbol=symbol;
	acceptedAuthor=circumAuthor;
	acceptedRefSpeciesName=refSpeciesName;
	acceptedCommonName=commonName;

} //end if

/*gain the correlation information because the the usage above was not standard for the party*/
else {
	results = query.executeQuery("select CORRELATION_ID, CONVERGENCE  from CORRELATION where PARTY_CIRCUM1 ="+partyCircumId);
	while (results.next()) {
        	correlationId=results.getInt("CORRELATION_ID");
		convergence=results.getString("CONVERGENCE");

		outfile.println("Correlation(s) found - correlation ID: <i>"+correlationId+" </i><br>\n");
		outfile.println("Congruence: <i>"+convergence+" </i><br>\n");
	} //end  while

	/*backtrack and get all the circumscription information about the correlation information*/
	results = query.executeQuery("select AUTHOR, DATE_ENTERED, SPECIESNAME, PAGE, CIRCUMTYPE from reference where(select REF_ID  from CIRCUMSCRIPTION  where(select CIRCUM_ID from STATUS where(select PARTY_CIRCUM2  from CORRELATION where CORRELATION_ID="+correlationId+")=PARTY_CIRCUM_ID)=CIRCUM_ID)=REF_ID");

	while (results.next()) {
        	correlationAuthor=results.getString("AUTHOR");
		correlationRefSpeciesName=results.getString("SPECIESNAME");

		outfile.println("Correlation Author: <i>"+correlationAuthor+" </i><br>\n");
		outfile.println("Taxon Used in Citation: <i>"+correlationRefSpeciesName+" </i><br>\n");
	} //end  while

	results = query.executeQuery("select name, commonName, symbol  from name where(select name_id from name_usage where(select CIRCUM_ID from STATUS where(select PARTY_CIRCUM2  from CORRELATION where CORRELATION_ID="+correlationId+")=PARTY_CIRCUM_ID)=circum_id)=name_id");

	while (results.next()) {
        	correlationTaxon=results.getString("name");
        	correlationCommonName=results.getString("commonName");
		correlationSymbol=results.getString("symbol");

		outfile.println("Correlation Taxon: <i>"+correlationTaxon+" </i><br>\n");
		outfile.println("Correlation Common Name : <i>"+correlationCommonName+" </i><br>\n");
		outfile.println("Correlation Symbol: <i>"+correlationSymbol+" </i><br>\n");
		acceptedTaxon=correlationTaxon;
        	acceptedSymbol=correlationSymbol;
		acceptedAuthor=correlationAuthor;
		acceptedRefSpeciesName=correlationRefSpeciesName;
		acceptedCommonName=correlationCommonName;

	} //end  while
} //end else


/*print results for the accepted information to the browser - the rest of the data went to the results.html file*/
out.println("<FONT COLOR=blue>");
out.println("<FONT COLOR=black> <u> Accepted Name </u></FONT><br>\n");
out.println("<spacer type=block width=25> Correlation Taxon: <i>"+acceptedTaxon+" </i><br>\n");
out.println("<FONT COLOR=black> <u> Circumscription </u></FONT><br>\n");
 out.println("<spacer type=block width=25> Circumscriptions Authors: <i>"+acceptedAuthor+" </i><br>\n"); 
 out.println("<spacer type=block width=25> Taxon used in Circumscription: <i>"+acceptedRefSpeciesName+" </i><br>\n");
out.println("</FONT>");				
out.println("<br><i>");


stmt.close();
query.close();
conn.close();
} //end try
catch ( Exception e ){out.println("The Query Jumbled - circumscription "+e.getMessage());}


/*links to other dynamically generated pages*/
out.println("<A HREF=\"http://dev.nceas.ucsb.edu/harris/results.html\"><B><FONT SIZE=\"-1\" FACE=\"arial\">Expanded Results</FONT></B></A>");






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

/*this is ther party selection form*/
out.println("<br><i>Party Perspective </i>");
out.println("<SELECT NAME=lastname SIZE=2 onChange=\"checker(this)\"><OPTION>USDA<OPTION>Kartesz<OPTION>TNC<OPTION>alaskensis</SELECT>");
out.println("<br><br> \n");

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
