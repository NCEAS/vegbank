import java.io.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

/*extras for extensibility*/
import java.sql.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.math.*;
import java.net.URL;

/*JHH for NCEAS 7/2000
This servlet will write out, as an ascii file, vegetation inormation related to a plot
*/


public class vegDes extends HttpServlet {

/*access the local resource bundle file called "modified_code.properties"*/
ResourceBundle rb = ResourceBundle.getBundle("modified_code");

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
        response.setContentType("text/html");

/*define the outputfile*/
PrintStream out_file = new PrintStream(new FileOutputStream("/plot_database/servlet_output.file.xml"));

        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<body>");
        out.println("<head>");

      String title = rb.getString("vegDes.title");
	out.println("<title>" + title + "</title>");
        out.println("</head>");
        out.println("<body bgcolor=\"224466\">");
	  out.println("<h3>" + title + "</h3>");
      	String plot_id = request.getParameter("plot_id");
      	String orig_species = request.getParameter("orig_species");
		String stratum_type = request.getParameter("stratum_type");
		String percent_cover = request.getParameter("percent_cover");
		String dominance_id = request.getParameter("dominance_id");
		String layer_height = request.getParameter("layer_height");
		String physiogn_A = request.getParameter("physiogn_A");
		String physiogn_B = request.getParameter("physiogn_B");

/*tell the client which data are requested*/
        out.println(rb.getString("vegDes.params-in-req") + "<br>");


	out.println("<P>");
      out.print("<form action=\"");
	out.print("vegDes\" ");
	  out.println("method=POST>");
      out.println(rb.getString("vegDes.plot_id"));
      out.println("<input type=text size=20 name=plot_id>");
      out.println("<br>");
      out.println(rb.getString("vegDes.orig_species"));
      out.println("<input type=text size=20 name=orig_species>");
      out.println("<br>");

      out.println(rb.getString("vegDes.stratum_type"));
	  out.println("<input type=text size=20 name=stratum_type>");
      out.println("<br>");

	  out.println(rb.getString("vegDes.percent_cover"));
	  out.println("<input type=text size=20 name=percent_cover>");
	  out.println("<br>");

	  out.println(rb.getString("vegDes.dominance_id"));
	  out.println("<input type=text size=20 name=dominance_id>");
	  out.println("<br>");

	  out.println(rb.getString("vegDes.layer_height"));
	  out.println("<input type=text size=20 name=layer_height>");
	  out.println("<br>");

	  out.println(rb.getString("vegDes.physiogn_A"));
	  out.println("<input type=text size=20 name=physiogn_A>");
	  out.println("<br>");

	  out.println(rb.getString("vegDes.physiogn_B"));
	  	  out.println("<input type=text size=20 name=physiogn_B>");
	  out.println("<br>");


      out.println("<input type=submit>");
	out.println("<P>");
/*if the client has entered values into at least some field then
	return the values to the screen*/

if (plot_id != null || orig_species != null) {
            out.println("<P>");
		out.println(rb.getString("vegDes.plot_id"));
            out.println(" = " + orig_species + "<br>");
            out.println(rb.getString("vegDes.orig_species"));
            out.println(" = " + plot_id);


/* write this stuff to an xml file which is consistent with the veg_description.dtd
    make sure to escape characters*/
	out_file.println("<?xml version=\"1.0\"?>");
	out_file.println("<!DOCTYPE vegetation_description SYSTEM \"file:C:\\plot_database\\veg_description.dtd\">");
	out_file.println("<vegetation_description>");
		out_file.println("<plot_id>"+plot_id+"</plot_id>");
		out_file.println("<species>");
			out_file.println("<orig_species>"+orig_species+"</orig_species>");
			out_file.println("<stratum>");
				out_file.println("<stratum_type>"+stratum_type+"</stratum_type>");
				out_file.println("<percent_cover>"+percent_cover+"</percent_cover>");
				out_file.println("<dominance>");
					out_file.println("<id>"+dominance_id+"</id>");
					out_file.println("<layer_height>"+layer_height+"</layer_height>");
					out_file.println("<physiogn_A>"+physiogn_A+"</physiogn_A>");
					out_file.println("<physiogn_B>"+physiogn_B+"</physiogn_B>");
				out_file.println("</dominance>");
			out_file.println("</stratum>");
			out_file.println("</species>");
	/*this is should be the last element in the xml*/
	out_file.println("</vegetation_description>");

try {
/*next line calls the datbase loader*/
Process listener;
listener=Runtime.getRuntime().exec("/jdk1.3/bin/java.exe xmlLoadglue");
//listener=Runtime.getRuntime().exec("notepad");

if (listener==null) {out.println("broken");}

}

catch (Exception ex) {
      System.out.println("error fixing the parsed xml file for data loading. "+ex);
      System.exit(1);
		}



}  //end if

/*else if there are no required fields then ask for some*/

else {
	out.println("<P>");
	out.println(rb.getString("vegDes.no-params"));
	}



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
