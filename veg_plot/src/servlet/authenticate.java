import java.io.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 * Servlet to perform user authentication for all the 
 * servlets within a domain.  This servlet will recognize varying 
 * degrees of authentication for users and will place cookies representing
 * the authentication level
 *
 * <p>Valid parameters are:<br><br>
 * 
 * userName - user name of the user tying to be authenticated <br>
 * password - the password of the user attempting to be authenticated <br> 
 *
 * @version 12 Jan 2001
 * @author John Harris
 * 
 */

public class authenticate extends HttpServlet 
{
	public int cookieValidFlag=0;  //1=valid
	public Cookie registeredCookie;
	public servletUtility su = new servletUtility();
	
	ResourceBundle rb = ResourceBundle.getBundle("LocalStrings");
	//public String clientLogFile = null; //the file to log the client usage


	/** Handle "POST" method requests from HTTP clients */
	public void doPost(HttpServletRequest request,
	HttpServletResponse response)
	throws IOException, ServletException
	{
		doGet(request, response);
	}
    
	/** Handle "POST" method requests from HTTP clients */
	public void doGet(HttpServletRequest request,
	HttpServletResponse response)
	throws IOException, ServletException
	{

		//clientLogFile=rb.getString("requestparams.clientLog");
		//System.out.println("the client info log file is: "+clientLogFile);

		//first block will look for the userName and password as well as valid cookie
		try
		{
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
	
			//get the request - parameters using the 
			//servlet Utility class 
			Hashtable requestParams = su.parameterHash(request);
			System.out.println( requestParams.toString() );
			//grab the remote host information
			String remoteHost=request.getRemoteHost();
			String remoteAddress = request.getRemoteAddr();
			System.out.println("remoteHost: "+remoteHost+" Address: "+remoteAddress);
			
			
			//determine what the client wants to do
			if ( requestParams.containsKey("authType") == false)
			{
				out.println("unrecognized request -- redirecting to vegetation portal");
			}
			else
			{
				//figure out what the client wants to do -- exactly
				//LOGIN THE USER
				if ( requestParams.get("authType").toString().equals("loginUser")  )
				{
					System.out.println("user login attempt");
					if ( authenticateUser(requestParams, remoteAddress) == true)
					{
						//log the login in the clientLogger
						//set a cookie
						Cookie cookie = new Cookie("null", "null");
						authenticate m =new authenticate();  
						m.cookieDelegator(cookie, requestParams, remoteAddress);
						response.addCookie(m.registeredCookie);
						
 						
						//send the user to the correct page
						Thread.currentThread().sleep(500);
						response.sendRedirect("/harris/servlet/pageDirector?pageType=loggedin");
					}
					else 
					{
						//send the user to the correct page
						Thread.currentThread().sleep(500);
						response.sendRedirect("/harris/servlet/pageDirector?pageType=login");
					}
					
				}
				// CREATE A NEW USER
				else if ( requestParams.get("authType").toString().equals("createUser")  )
				{
					System.out.println("creating a new user");
					if (createNewUser(requestParams, remoteAddress) == true )
					{
						Thread.currentThread().sleep(1000);
        		response.sendRedirect("/harris/servlet/pageDirector?pageType=loggedin");
					}
					else
					{
						out.println("unsuccessful user creation -- please try again");
					}
				}
			}
			

		}	  //end try
		catch( Exception e ) 
		{
			System.out.println("failed in: "
			+e.getMessage());
			e.printStackTrace();
		}
	}

	
	/**
	 * method to authenticate a known user
	 *
	 */
	 private boolean authenticateUser(Hashtable requestParams, String remoteAddress)
	 {
		 			//grab the user name and password
			String userName=requestParams.get("userName").toString();
			String passWord=requestParams.get("password").toString();
			System.out.println("name password/pair: "+userName+" "+passWord);

			//validate the name password pair
	 		if (userName.equals("user") && passWord.equals("pass")) 
 			{
				System.out.println("authentication successful ");
				return(true);
 			}
 			else 
 			{
				return(false);
 			}
		// return(false);
	 }
	 

	 /**
	 * method to create a new user identification 
	 * in the vegetation user database
	 *
	 */
	 private boolean createNewUser(Hashtable requestParams, String remoteAddress)
	 {
		 //get the key variables 
		 String emailAddress = requestParams.get("emailAddress").toString();
		 String passWord =  requestParams.get("password").toString();
		 String retypePassWord = requestParams.get("password2").toString();
		 
		 System.out.println("password comparison: '"+passWord+"' '"+retypePassWord+"'");
		 if ( passWord.equals(retypePassWord) &&  passWord.length() > 2 )
		 {
			 System.out.println("equals");
			 UserDatabaseAccess uda = new UserDatabaseAccess();
			 uda.createUser(emailAddress, passWord);
			 
			 
			 
			 return(true);
		 }
		 else
		 {
			 System.out.println("not equals");
			 return(false);
		 }
	 }


/**
	* Method to register a cookie and set it in the browser if there is a match 
 * between the issued user name and password with a valid name password pair
 * stored in some data store
 *  
 * @param cookie - a cookie whose name, value and max age are set depending on
 * 	the results of validation 
 * @param userName - the user name issued
 * @param passWord - the issued password
 *
 */
	public void cookieDelegator (Cookie cookie, Hashtable requestParams, 
		String remoteAddress) 
	{

		
		// below is quite crude but will suffice for the time being.  
		// Soon will rewrite the method to send more meaning values 
		// and will use a database query to access 
		// user information -- the resource bundle is set here b/c
		// this method may be call from another class
		ResourceBundle rbun = ResourceBundle.getBundle("LocalStrings");
		String clientLogFile = rbun.getString("requestparams.clientLog");
		String cookieName = "xploit";  //will become dynamic
		String cookieValue = "001"; //will become something meaningful
		String cookieAddress = remoteAddress; //same
		cookie = new Cookie(cookieName, cookieValue);
		cookie.setMaxAge(3600);  //set cookie for an hour
		registeredCookie=cookie;
	
 	
 	 // Register the cookie in the client log with the following format:
 	 // userName|password|remoteAddress|date|cookieName|cookieValue|cookieAddress
 	 //
		try 
		{
			PrintStream clientLog = new PrintStream(new FileOutputStream(clientLogFile, 
			true));
			Date date = new Date();
			clientLog.println(date+"|"+cookieName+"|"+cookieValue+"|"+cookieAddress);
		}
		catch (Exception e) 
		{
			System.out.println("failed: " 
			+e.getMessage());
			e.printStackTrace();
		}
	}





/**
 *  Method to check and see if a cookie is valid using as inputs the name of the
 * cookie, the value of the cookie and the remote host information
 *
 * @param cookieName - name of the cookie for validation
 * @param cookieValue - value of the cookie
 * @param remoteHost - remote host's address
 *
 */
	public void cookieChecker (String cookieName, String cookieValue, 
		String remoteHost) 
	{
		ResourceBundle rb = ResourceBundle.getBundle("plotQuery");
 		if (cookieName.equals("xploit") && cookieValue.equals("001")) 
		{ 
			cookieValidFlag=1;
 		}

 		else 
	 {
		cookieValidFlag=0;
 		}
	}
	
}


