import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

/**
*
* This servlet handles all the requests for html pages and has been implemented
* in conjunction with the authenticate servlet.  This class will check to see
* if the browser has a valid cookie and if so will provide the user with the 
* requested page, otherwise the login page is displayed  
*/
public class pageDirector extends HttpServlet {

  public void doGet(HttpServletRequest req, HttpServletResponse res) 
                               throws ServletException, IOException {
				       
// Use a ServletOutputStream since we may pass binary information
ServletOutputStream out = res.getOutputStream();

//set the content type to html
String contentType = getServletContext().getMimeType("login.html");
res.setContentType(contentType);

//get the cookies - if there are any
String cookieName = null;
String cookieValue = null;

Cookie[] cookies = req.getCookies();
        if (cookies.length > 0) {
         
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
		//out.print("Cookie Name: " +cookie.getName()  + "<br>");
                	cookieName=cookie.getName();
		//out.println("  Cookie Value: " + cookie.getValue() +"<br><br>");
			cookieValue=cookie.getValue();
            }
  	} 
	
	else {
           out.println("cookies.no-cookies");
	}


//get the requested page type
String pageType = req.getParameter("pageType");

//first determine if the browser has a valid cookie
//out.println("debug flag val "+cookieName+" "+cookieValue);
if (cookieName != null && cookieValue != null) {
	
	//check that the cookie is a valid one
	authenticate m =new authenticate();  
	m.cookieChecker(cookieName, cookieValue, "remoteHost");
	
	//if valid cookie then forward to page
	if (m.cookieValidFlag==1) {
		pageDirector n =new pageDirector();  
		n.pageTranslator(pageType);
		ViewFile.returnFile(n.pageFileName, out);
	
	}
	
	else {
		ViewFile.returnFile("C:\\\\jakarta-tomcat\\webapps\\examples\\WEB-INF\\lib\\login.html", out);
	}
          
        }

//if not then pass the login page to the browser	
else {	
	ViewFile.returnFile("C:\\\\jakarta-tomcat\\webapps\\examples\\WEB-INF\\lib\\login.html", out);
}				       

}
  
/**
* Method to send the contents of a file to the outputStream
*/  
  public static void returnFile(String fileName, OutputStream out)
  throws FileNotFoundException, IOException {
	  //A FileInputStream is for Bytes
	  FileInputStream fis=null;
	  try {
	  
	  fis = new FileInputStream(fileName);
	  byte[] buf = new byte[4 * 1024]; //4K buffer
	  int bytesRead;
	  while ((bytesRead=fis.read(buf)) != -1) {
		  out.write(buf, 0, bytesRead);
	  }
	  }
	  finally {
		  if (fis !=null) fis.close();
	  }
  }
  
/**
*  Method to match the requested page type with the file to be returned to the 
*  browser
*
*  @param pageType String - the type of page requsted 
*/
public void pageTranslator (String pageType) 
	{
	if (pageType.equals("plotQuery")) pageFileName="C:\\\\jakarta-tomcat\\webapps\\examples\\WEB-INF\\lib\\plotQuery.html";
	
	if (pageType.equals("communityQuery")) pageFileName="C:\\\\jakarta-tomcat\\webapps\\examples\\WEB-INF\\lib\\communityQuery.html";
	
	if (pageType.equals("plantQuery")) pageFileName="C:\\\\jakarta-tomcat\\webapps\\examples\\WEB-INF\\lib\\plantQuery.html";
	
	if (pageType.equals("login")) pageFileName="C:\\\\jakarta-tomcat\\webapps\\examples\\WEB-INF\\lib\\login.html";
	
	if (pageType.equals("loggedin")) pageFileName="C:\\\\jakarta-tomcat\\webapps\\examples\\WEB-INF\\lib\\loggedin.html";

	if (pageType.equals("download")) pageFileName="C:\\\\jakarta-tomcat\\webapps\\examples\\WEB-INF\\lib\\download.html";
	
	}
public String pageFileName; 
	  
}
