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
 * @author John Harris <harris02@hotmail.com>
 */

public class authenticate extends HttpServlet {

    ResourceBundle rb = ResourceBundle.getBundle("LocalStrings");
    
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
	
	
	catch( Exception e ) {System.out.println("servlet failed in: authenticate.main second try   "+e.getMessage());}

	    
//if statement to see if a username and password are being passed to the serv
/**
        response.setContentType("text/html");

        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<body bgcolor=\"white\">");
        out.println("<head>");

        String title = rb.getString("cookies.title");
        out.println("<title>" + title + "</title>");
        out.println("</head>");
        out.println("<body>");

	// relative links

        // XXX
        // making these absolute till we work out the
        // addition of a PathInfo issue 
	
        out.println("<a href=\"/examples/servlets/cookies.html\">");
        out.println("<img src=\"/examples/images/code.gif\" height=24 " +
                    "width=24 align=right border=0 alt=\"view code\"></a>");
        out.println("<a href=\"/examples/servlets/index.html\">");
        out.println("<img src=\"/examples/images/return.gif\" height=24 " +
                    "width=24 align=right border=0 alt=\"return\"></a>");

        out.println("<h3>" + title + "</h3>");

        Cookie[] cookies = request.getCookies();
        if (cookies.length > 0) {
            out.println(rb.getString("cookies.cookies") + "<br>");
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                out.print("Cookie Name: " + cookie.getName() + "<br>");
                out.println("  Cookie Value: " + cookie.getValue() +
			    "<br><br>");
            }
        } else {
            out.println(rb.getString("cookies.no-cookies"));
        }

        String cookieName = request.getParameter("cookiename");
        String cookieValue = request.getParameter("cookievalue");
        if (cookieName != null && cookieValue != null) {
            Cookie cookie = new Cookie(cookieName, cookieValue);
            response.addCookie(cookie);
            out.println("<P>");
            out.println(rb.getString("cookies.set") + "<br>");
            out.print(rb.getString("cookies.name") + "  " + cookieName +
		      "<br>");
            out.print(rb.getString("cookies.value") + "  " + cookieValue);
        }
        
        out.println("<P>");
        out.println(rb.getString("cookies.make-cookie") + "<br>");
        out.print("<form action=\"");
        out.println("authenticate\" method=POST>");
        out.print(rb.getString("cookies.name") + "  ");
        out.println("<input type=text length=20 name=cookiename><br>");
        out.print(rb.getString("cookies.value") + "  ");
        out.println("<input type=text length=20 name=cookievalue><br>");
        out.println("<input type=submit></form>");
            
            
        out.println("</body>");
        out.println("</html>");
*/
	
    }

    public void doPost(HttpServletRequest request,
                      HttpServletResponse response)
        throws IOException, ServletException
    {
        doGet(request, response);
    }

/**
*  Method to register a cookie and set it in the browser
*/
public void cookieDelegator (Cookie cookie, String userName, String passWord, 
	String remoteHost) {
	ResourceBundle rb = ResourceBundle.getBundle("plotQuery");
	String cookieName = "xploit";
	String cookieValue = "001";
	String cookieAddress = "ipAddress";
	cookie = new Cookie(cookieName, cookieValue);
	cookie.setMaxAge(3600);  //set cookie for an hour
	registeredCookie=cookie;
	
	/**register the cookie with in the following format:
	*  userName, password, ipAddress, date, cookieName, cookieAddress
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
*  Method to check and see if a cookie is valid
*/
public void cookieChecker (String cookieName, String cookieValue, String remoteHost) 
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


