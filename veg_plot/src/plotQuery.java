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


public class plotQuery extends HttpServlet {
ResourceBundle rb = ResourceBundle.getBundle("plotQuery");

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
Statement query2=null;
Statement query3=null;
Statement stmt = null;

response.setContentType("text/html");
PrintWriter out = response.getWriter();

out.println("<html>");
out.println("<body>");
out.println("<head>");
String title =("plotQuery - Interface to the Plots Database");
out.println("<title>" + title + "</title>");
out.println("</head>");
out.println("<body bgcolor=\"336699\">");
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
	query2 = conn.createStatement ();
	query3= conn.createStatement ();
	stmt = conn.createStatement ();
	out.println("Connected.<br>");
	}
catch ( Exception e ){out.println("did not connect "+e.getMessage());}



/*query the database*/
ResultSet results = null;
ResultSet obsData=null;
ResultSet plotData=null;
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
String correlationRefSpeciesName=null;
String correlationTaxon=null;
String correlationCommonName=null;

String curStatus=null;
int partyCircumId=-999;
int correlationId=-999;
String correlationAuthor=null;
String convergence=null;


int obsId=-999;
int parentPlot=-99;
String authorPlotCode=null;
String surfGeo=null;


/*get the basic name information*/
try {
out.println("<b>Selection results:</b> <br>");
out.println("<b>Author assigned plot name:</b> <br>");
results = query.executeQuery("select obs_id from taxonObservation where authornameid like '%"+firstName+"%'");

while (results.next()) {
	obsId = results.getInt("obs_id");
//	out.println(obsId);
	
		obsData = query2.executeQuery("select parentPlot from plotObservation where obs_id="+obsId);

		while (obsData.next()) {
		parentPlot=obsData.getInt("parentPlot");
		//out.println(obsId+","+parentPlot);	
	//	}


			plotData=query3.executeQuery("select authorplotcode, surfGeo  from plot where plot_id= "+parentPlot);

			while (plotData.next()) {
      				 authorPlotCode =plotData.getString("authorplotcode");
				surfGeo=plotData.getString("surfGeo");

			out.println("<i>"+authorPlotCode+"	"+surfGeo+"</i><br>  \n");
			out.println("</i><P>");
		} 
	} //end while
} //end while


stmt.close();
query.close();
conn.close();


} //end try
catch ( Exception e ){out.println("The Query Jumbled"+e.getMessage());}





} //end if
 else {
            out.println(rb.getString("requestparams.no-params"));
        }
out.println("<P>");
out.print("<form action=\"");
out.print("plotQuery\" ");   /*make sure to have this pointing to the right servlet6*/
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
