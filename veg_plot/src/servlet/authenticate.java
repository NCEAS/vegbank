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

public class authenticate extends HttpServlet {

ResourceBundle rb = ResourceBundle.getBundle("LocalStrings");

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

//first block will look for the userName and password as well as valid cookie
try {
response.setContentType("text/html");
PrintWriter out = response.getWriter();
	
//grab the user name and password
String userName=request.getParameter("userName");
String passWord=request.getParameter("password");
System.out.println(userName+" "+passWord);
	
//grab the remote host information
String remoteHost=request.getRemoteHost();
System.out.println(remoteHost);
	
//validate the name password pair
int validate=0;  //0=invalid
 if (userName.equals("user") && passWord.equals("pass")) {
	validate=1;
	out.println("authentication successful - return to the previous \n"
	+"page to access the database - Soon there will be a redirection "
	+"to a more relevant page ");
		
	//log the login in the clientLogger
	//set a cookie
	Cookie cookie = new Cookie("null", "null");
	authenticate m =new authenticate();  
	m.cookieDelegator(cookie, userName, passWord, remoteHost);
	response.addCookie(m.registeredCookie);
		
	/** redirect to the 'logged in' page with options for db interaction
	* this however is not working with netscape6 */
	//redirect to the 'logged in' page
		
	//try sleeping 
	//Thread.currentThread().sleep(3000);
        //response.sendRedirect("/examples/servlet/pageDirector?pageType=loggedin");
	
 }
	
 else {
	validate=0;
	out.println("invalid login");
	//log the login attemp in the clientLogger
	
	//redirect to the login page
	//response.sendRedirect("/examples/servlet/pageDirector?pageType=login");
 }


}  //end try
catch( Exception e ) {System.out.println("servlet failed in: "
		+"authenticate.main second try   "+e.getMessage());}
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
public void cookieDelegator (Cookie cookie, String userName, String passWord, 
	String remoteHost) {

// below is quite crude but will suffice for the time being.  Soon will rewrite
// the method to send more meaning values and will use a database query to access 
// user information 
ResourceBundle rb = ResourceBundle.getBundle("plotQuery");
String cookieName = "xploit";  //will become dynamic
String cookieValue = "001"; //will become something meaningful
String cookieAddress = "ipAddress"; //same
cookie = new Cookie(cookieName, cookieValue);
cookie.setMaxAge(3600);  //set cookie for an hour
registeredCookie=cookie;
	
/**
* Register the cookie in the client log with the following format:
* userName|password|remoteAddress|date|cookieName|cookieValue|cookieAddress
*/
try {
PrintStream clientLog = new PrintStream(new FileOutputStream(
rb.getString("requestparams.clientLog"), true));
Date date = new Date();

clientLog.println(userName+"|"+passWord+"|"+remoteHost
	+"|"+date+"|"+cookieName+"|"+cookieValue+"|"+cookieAddress);

}
catch (Exception e) {System.out.println("failed in authenticate.cookieDelegator"
	+"trying to access client log" + e.getMessage());}
	
}
public Cookie registeredCookie;


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
 if (cookieName.equals("xploit") && cookieValue.equals("001")) { 
		cookieValidFlag=1;
 }

 else {
	cookieValidFlag=0;
 }
}

public int cookieValidFlag=0;  //1=valid
}


