import java.sql.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.math.*;
import java.net.URL;

/* JHH for NCEAS 7/2000 - this code will read a file into a MYSQL
database.  The code can be quickly modified to work on another RDBMS*/

public class xmlLoadglue {
public static void main(String argv[]) {


/*first process will call the XML parser*/
try{

System.out.println("parsing the XML data");
Process listener;
listener = Runtime.getRuntime().exec("/jdk1.3/bin/java.exe org.apache.xalan.xslt.Process -IN C:\\plot_database\\servlet_output.file.xml  -XSL C:\\plot_database\\veg_desc_flat.xsl -OUT C:\\plot_database\\xml.flat");
if (listener == null)  {System.out.println("parser did not work");}


}
catch (Exception ex) {
      System.out.println("error parsing the xml data. "+ex);
      System.exit(1);
    }


/*edit the parsed xml file*/
try {
BufferedReader in = new BufferedReader(new FileReader("/plot_database/xml.flat"), 8192);
PrintStream out = new PrintStream(new FileOutputStream("/plot_database/xml_edit.flat"));
String s = null;

	while ((s=in.readLine()) != null) {

		if (s.indexOf("|")>= 0) {System.out.println("reading file");
		out.println(s);
		}

		else {
		System.out.println("reading EV header");
		}
	}
}

catch (Exception ex) {
      System.out.println("error fixing the parsed xml file for data loading. "+ex);
      System.exit(1);
		}

/*this process will call the database loader*/
try{

System.out.println("calling the java database loader");
Process listener;
listener = Runtime.getRuntime().exec("/jdk1.3/bin/java.exe load_veg_desc_jdbc");

}
catch (Exception ex) {
      System.out.println("error calling shell. "+ex);
      System.exit(1);
    }

                                 }
                                 }
