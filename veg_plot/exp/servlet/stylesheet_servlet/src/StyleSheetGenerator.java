package servlet;

import java.io.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.math.*;
import java.net.URL;
import java.sql.*;

import org.apache.tools.mail.MailMessage;
//import servlet.plugin.*;
//import org.w3c.dom.Document;
//import xmlresource.utils.XMLparse;
//import servlet.util.ServletUtility;


/**
 * Servlet to read the attributes from an html form, store the 
 * attributes in an RDBMS and then send an email to the submitter
 * describing the stored attributes
 *
 * <p>Valid parameters are:<br><br>
 * @param tableName -- the name of the database table inwhich to store
 * 		attributes
 * @param databaseName -- the name of the database to use to store data
 *
 * Optional Parameters
 * @param redirectPoint -- the url to redirect the user to
 * @param responseMessage -- the message to respond to the client after data 
 *	has be retrieved
 * @param validation -- a comma-separated string containg the attributes that 
 * must be validated.  in this case validated simply means that it is required
 * 
 */

public class StyleSheetGenerator extends HttpServlet 
{
	
	
	private String databaseName = null;
	private String tableName = null;
	private String emailAddress = null;
	private String redirectPoint = null;
	private String responseMessage = null;
	private String validation = null; 
	
	/**
	 * constructor method
	 */
	public StyleSheetGenerator()
	{
		System.out.println("new instance of StyleSheetGenerator");
	}
	
	
	/** Handle "POST" method requests from HTTP clients */
	public void doPost(HttpServletRequest request,
		HttpServletResponse response)
 	 throws IOException, ServletException 	
		{
			doGet(request, response);
		}

		
		
	/** Handle "GET" method requests from HTTP clients */ 
	public void doGet(HttpServletRequest request, 
		HttpServletResponse response)
		throws IOException, ServletException  
		{
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			try 
			{
			//enumeration is needed for the attributes
				Enumeration enum =request.getParameterNames();
				Hashtable params = this.parameterHash( request );
				//the required parameters
				this.databaseName = (String)params.get("databaseName");
				this.tableName = (String)params.get("tableName");
				this.redirectPoint = (String)params.get("redirectPoint");
				this.responseMessage = (String)params.get("responseMessage");
				this.emailAddress = (String)params.get("emailAddress");
				this.validation = (String)params.get("validation");
				System.out.println("validation string: " + this.validation );
				
				//out.println("IN PARAMETERS: " + params.toString() + "\n" );
				//System.out.println("testing the form reader class: " + params);
				
				//validate
				if ( this.validateInput( params ) == true )
				{
				
						//try to load the database
						if ( this.handleRequest(request, params) == true )
						{
							//send response
							out.println( this.sendResponseMessage(this.responseMessage, true ) );
						}
						//end the failure message
						else
						{
							//send response
							out.println( this.sendResponseMessage(this.responseMessage , false));
						}
				}
				//end the failure message
				else
				{
					//send response
					out.println( this.sendResponseMessage(this.responseMessage , false));
				}
				
			}
		catch( Exception e ) 
		{
			System.out.println("Exception:  "
			+e.getMessage());
			e.printStackTrace();
		}
	}
	
	//method to send the response to the clent and a linkto the redirect point
	private String sendResponseMessage(String message, boolean validationpass)
	{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd G 'at' hh:mm:ss a zzz");
		java.util.Date now = new java.util.Date();
		String dateString = formatter.format(now);
		
		StringBuffer sb = new StringBuffer();
		if (validationpass == true )
		{
			sb.append("<html> \n");
			sb.append("<head> \n");
			sb.append("<title> Data Received -- Thank You! </title> \n");
			sb.append("</head> \n");
			sb.append("<body> \n");
			sb.append("Thank You: " + this.emailAddress + " <br> ");
			sb.append(" receipt email sent: "+ dateString + "+<br> <br> \n");
			sb.append("<br> "+ this.responseMessage+ "<br> ");
			sb.append("<a href="+this.redirectPoint+" > return </a>" );
		
			sb.append("</body> \n");
			sb.append("</html> \n");
			return(sb.toString() );
		}
		
		else
		{
			sb.append("<html> \n");
			sb.append("<head> \n");
			sb.append("<title> Missing Data! </title> \n");
			sb.append("</head> \n");
			sb.append("<body> \n");
			sb.append("Missing data in a Required field! Go Back<br> <br> \n");
			sb.append("</body> \n");
			sb.append("</html> \n");
			return(sb.toString() );
		}
	}
	
	/**
	 * method that inserts the parameters into a database table
	 *
	 */
	 private boolean handleRequest(HttpServletRequest request, Hashtable params )
	 {
		 try
		 {
			 //get the database connection
			 Connection c = this.getConnection();
			 //if he database exists then load it 
			 if (this.tableExists(c, "form") == true)
			 {
				 System.out.println("exists and loading");
				 Vector v = getAttributeNames( request  );
				 if ( loadAttributes( c, v, request,  params  ) == true )
				 {
				 	//this.loadAttributes( c, v, request,  params  );
				 	this.mailRecipt(request);
				 	//this.sendResponseMessage();
				 }
				 else
				 {
					 System.out.println(" Data not loaded to DB");
					 return(false);
				 }
			 }
			 //otherwise create it then load it
			 else
			 {
				 Vector v = getAttributeNames( request  );
				 System.out.println("creating table: " + v.toString() );
				 this.createTable(c, v, this.tableName);
				 if ( loadAttributes( c, v, request,  params  ) == true )
				 {
				 //this.loadAttributes(c, v, request,  params  );
			 	 this.mailRecipt(request);
				 //this.sendResponseMessage()
				 }
				 else
				 {
					 System.out.println(" Data not loaded to DB");
					 return(false);
				 }
			 }
		 }
		 catch (Exception e)
		 {
			 System.out.println("Exception: " + e.getMessage());
			 e.printStackTrace();
		 }
		 return(true);
	 }
	 
	 
	 /**
	  * method that loads the attributes
		*/
		private boolean loadAttributes(Connection conn, Vector attributeNames, 
		HttpServletRequest request, Hashtable params)
		{
		 try
		 {
			 PreparedStatement pstmt;
			 StringBuffer sb = new StringBuffer();
			 
			 sb.append("INSERT into "+this.tableName+" (");
			 
			 //the attribute names
			 for (int i=0; i<attributeNames.size(); i++) 
				{
					//comma separate these
					if ( i < (attributeNames.size() -1) )
					{
						sb.append(attributeNames.elementAt(i).toString()  + ", " );
					}
					else
					{
						sb.append(attributeNames.elementAt(i).toString()  + " ) ");
					}
				}
	
			sb.append(" values (");
			//the values
			for (int i=0; i<attributeNames.size(); i++) 
			{
					//comma separate these
					if ( i < (attributeNames.size() -1) )
					{
						String key = (String)attributeNames.elementAt(i).toString();
						String val = ((String)params.get( key )).replace('\'', ' ');
						sb.append(" '"+val  + "', " );
					}
					else
					{
						String key = (String)attributeNames.elementAt(i).toString();
						String val = ((String)params.get(key)).replace('\'', ' ');
						sb.append(" '"+val + "') " );
					}
			}
			pstmt = conn.prepareStatement( sb.toString() );
			
			pstmt.execute();
			pstmt.close();
			 
		 }
		 catch (Exception e)
		 {
			 System.out.println("Exception: " + e.getMessage());
			 //e.printStackTrace();
			 return(false);
		 }
		 return(true);
		}
		
		
		/**
		 * method to mail the receipt
		 */
		 private void mailRecipt( HttpServletRequest req )
		 {
			 try
			 {
			 	String mailhost = "nceas.ucsb.edu";  // or another mail host
 				String from = "vegbank";
 				String to = this.emailAddress;
 				String cc1 = "vegbank@nceas.ucsb.edu";
 				//String cc2 = "cc2@you.com";
 				//String bcc = "bcc@you.com";
  
 				MailMessage msg = new MailMessage(mailhost);
 				msg.from(from);
 				msg.to(to);
 				msg.cc(cc1);
 				//msg.cc(cc2);
 				//msg.bcc(bcc);
 				msg.setSubject(" Auto-Receipt ");
 				PrintStream out = msg.getPrintStream();
  
 				Enumeration enum = req.getParameterNames();
 				while (enum.hasMoreElements()) 
				{
   			String name = (String)enum.nextElement();
   			String value = req.getParameter(name);
   			out.println(name + " = " + value);
 				}
  
			 msg.sendAndClose();
 			}
			 catch (Exception e)
		 	 {
				 System.out.println("Exception: " + e.getMessage());
				 e.printStackTrace();
			 }
		 }
		 
		
	 
	 /**
	  *  method that returns a vector with the names of the attributes 
		* stored in the parmeters
		*/
		private Vector getAttributeNames(HttpServletRequest request)
		{
			Vector v = new Vector();
			try
		 	{
				Enumeration enum =request.getParameterNames();
 				while (enum.hasMoreElements()) 
				{
					String name = (String) enum.nextElement();
					String values[] = request.getParameterValues(name);
					if (values != null) 
					{
						for (int i=0; i<values.length; i++) 
						{
							v.addElement(name);
							//params.put(name,values[i]);
						}
					}
 				}
				
			}
			 catch (Exception e)
		 	{
				 System.out.println("Exception: " + e.getMessage());
				 e.printStackTrace();
		 	}
		 	return(v);
		}
		
	 
	 /**
	  * method to create a table 
		*/
		private void createTable(Connection conn, Vector attributeNames, String tableName )
		{
			try
		 	{
				StringBuffer sb = new StringBuffer();
      	PreparedStatement pstmt;
      	
				sb.append("CREATE TABLE "+tableName+" ( ");
				
				//add each attribute as a varchar
				for (int i=0; i<attributeNames.size(); i++) 
				{
					//comma separate these
					if ( i < (attributeNames.size() -1) )
					{
						sb.append(attributeNames.elementAt(i).toString()  + " varchar(200), " );
					}
					else
					{
						sb.append(attributeNames.elementAt(i).toString()  + " varchar(200) " );
					}
				}
				//add the ending text
				sb.append( " )");
     		pstmt = conn.prepareStatement(sb.toString() );
				
				// pstmt.setString(1, sitecode);
     	 	pstmt.execute();
      	pstmt.close();
				 
			}

			catch (Exception e)
		 	{
				System.out.println("Exception: " + e.getMessage());
			 	e.printStackTrace();
		 	}
		}
	 
	 /**
	  *
		* method that tells if a table by the name of the table that the 
		* form correspons to exists
		*/
		private boolean tableExists(Connection conn, String tableName)
		{
		 try
		 {
				
     
			DatabaseMetaData dmd = conn.getMetaData();
			String types[] = {"TABLE", "INDEX", "SEQUENCE" };
			ResultSet rs = dmd.getTables("", "", "%", types );
			ResultSetMetaData rsmd = rs.getMetaData();
			
			//figure out if there is a match
			int cols = rsmd.getColumnCount();
			for ( int i = 1; i <=cols;i++)
			{
				System.out.println("table: " + rsmd.getColumnLabel(i) );
			}

			//now the table names 
			while ( rs.next() )
			{
				for ( int i = 1; i <=cols;i++)
				{
					Object o = rs.getObject(i);
					if (rs.wasNull() )
						System.out.print(" ");
					else
					{
						String objName = o.toString();
						System.out.println("object name: " + o.toString() );
						if ( this.tableName.equals(objName) )
						{
							return (true);
						}
					}
				}
			}
		 }
		 catch (Exception e)
		 {
			 System.out.println("Exception: " + e.getMessage());
			 e.printStackTrace();
		 }
		 return(false);
		
		}
		
	 /**
	  * method that validates the input -- meaning that it checks to see if
		* every thing is there
		*/
		private boolean validateInput(Hashtable params)
		{
			boolean test = true;
			try
			{
				
				//get the attributes which must have to have an attribute
				StringTokenizer t = new StringTokenizer(this.validation, ",");
				while (t.hasMoreTokens() )
				{
					String buf = t.nextToken().trim();
					System.out.println("validation token: " + buf);
					
					//get the value from the hash -- rememeber that it must be there
					//bc who wrote the form also wrote the validation string
					String val = (String)params.get(buf);
					System.out.println("validation value: " + val );
					//make sure that the val has a character -- if not then 
					//return a false
					if ( val.length() < 1 || val == null )
					{
						return(false);
					}
					
				}
			}
			catch (Exception e)
			{
				System.out.println("Exception - mis matching form and validation string");
				System.out.println(" Exception: " + e.getMessage() );
				//e.printStackTrace();
			}
			return (true);
		}
	 
	 
	 /**
	  * method to get a database connection
		*/
	private Connection getConnection()
	{
		Connection c = null;
		try 
 		{
			Class.forName("org.postgresql.Driver");
			//the html_forms database
			c = DriverManager.getConnection("jdbc:postgresql://beta.nceas.ucsb.edu/html_forms", "datauser", "");
		}
		catch ( Exception e )
		{
			System.out.println("Exception: "+e.getMessage());
			e.printStackTrace();
		}
			return(c);
	}
 
	 

	 /**
	 * method to stick the parameters from the client 
	 * into a hashtable - where each attribute is stored 
	 * as a string in the hashtable -- maybe change this in
	 * the future to a vector for each attribute so that multiple
	 * attribute vales can be stored
	 */
	private Hashtable parameterHash(HttpServletRequest request) 
	{
		Hashtable params = new Hashtable();
		try 
		{
			Enumeration enum =request.getParameterNames();
 			while (enum.hasMoreElements()) 
			{
				String name = (String) enum.nextElement();
				String values[] = request.getParameterValues(name);
				if (values != null) 
				{
					for (int i=0; i<values.length; i++) 
					{
						params.put(name,values[i]);
					}
				}
 			}
		}
		catch( Exception e ) 
		{
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		}
		return(params);
	}
}
