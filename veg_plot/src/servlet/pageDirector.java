import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 *
 * This servlet handles all the requests for html pages 
 * and has been implemented conjunction with the authenticate 
 * servlet.  This class will check to see if the browser has 
 * a valid cookie and if so will provide the user with the 
 * requested page, otherwise the login page is displayed 
 * and redisplayed
 *
 * <p>Valid parameters are:<br><br>
 * 
 * pageType - type of page requested by the user, may include plotQuery, login
 * 	download etc <br>
 */

public class pageDirector extends HttpServlet 
{

	ResourceBundle rb = ResourceBundle.getBundle("pageDirector");

	private String plotQueryPage = null;
	private String loginPage = null;
	private String loggedinPage = null;
	private String plantQueryPage = null;
	private String communityQueryPage = null;
	private String downloadPage = null;
	private String uploadPage = null;
	private String createUserPage = null;
	
	public String pageFileName; 


	public void doGet(HttpServletRequest req, HttpServletResponse res) 
		throws ServletException, IOException 
	{
		// Read the parameters from the properties file*/
		try 
		{
			createUserPage=(rb.getString("requestparams.createUserPage"));
 			plotQueryPage=(rb.getString("requestparams.plotQueryPage"));
 			loginPage=(rb.getString("requestparams.loginPage"));
 			loggedinPage=(rb.getString("requestparams.loggedinPage"));
 			plantQueryPage=(rb.getString("requestparams.plantQueryPage"));
 			communityQueryPage=(rb.getString("requestparams.communityQueryPage"));
 			downloadPage=(rb.getString("requestparams.downloadPage"));
 			uploadPage=(rb.getString("requestparams.uploadPage"));
		}
		catch( Exception e ) 
		{
			System.out.println("servlet failed in: pageDirector.main "
			+" first try - reading parameters "+e.getMessage()); 
		}



		// Use a ServletOutputStream since we may pass binary information
		ServletOutputStream out = res.getOutputStream();

		//set the content type to html
		String contentType = getServletContext().getMimeType("login.html");
		res.setContentType(contentType);

		//get the cookies - if there are any
		String cookieName = null;
		String cookieValue = null;

		Cookie[] cookies = req.getCookies();
		//determine if the requested page should be shown
    if (cookies.length > 0) 
		{
			for (int i = 0; i < cookies.length; i++) 
			{
      	Cookie cookie = cookies[i];
				//out.print("Cookie Name: " +cookie.getName()  + "<br>");
        cookieName=cookie.getName();
				//out.println("  Cookie Value: " + cookie.getValue() +"<br><br>");
				cookieValue=cookie.getValue();
			}
  	} 
				
		else 
		{
			//if the request is made to become a new user - do it
			if ( req.getParameter("pageType").equals("createUser") )
			{
			//	ViewFile.returnFile(createUserPage, out);
				res.sendRedirect("/harris/html/create_user.html");
				System.out.println(" redirected to a form to create a new account");
			}
			else 
			{
    		out.println("cookies.no-cookies");
			}
		}


		//get the requested page type
		String pageType = req.getParameter("pageType");

		//first determine if the browser has a valid cookie
		//out.println("debug flag val "+cookieName+" "+cookieValue);
		if (cookieName != null && cookieValue != null) 
		{
	
			//check that the cookie is a valid one
			authenticate m =new authenticate();  
			m.cookieChecker(cookieName, cookieValue, "remoteHost");
	
			//if valid cookie then forward to page
			if (m.cookieValidFlag==1) 
			{
				pageTranslator(pageType);
				returnFile(pageFileName, out);
			}
			else 
			{
				ViewFile.returnFile(loginPage, out);
			}
		}

		//if not then pass the login page to the browser	
		else 
		{	
			ViewFile.returnFile(loginPage, out);
		}				       
	}


/**
 * Method to send the contents of a file to the outputStream
 *
 * @param fileName - the name of the file that should be sent to the browser.
 * @param out the output stream to the client
 *
 */  
  public static void returnFile(String fileName, OutputStream out)
  throws FileNotFoundException, IOException 
	{
	  //A FileInputStream is for Bytes
	  FileInputStream fis=null;
	  try {
	  
	  	fis = new FileInputStream(fileName);
	  	byte[] buf = new byte[4 * 1024]; //4K buffer
	  	int bytesRead;
	  	while ((bytesRead=fis.read(buf)) != -1) 
			{
		  	out.write(buf, 0, bytesRead);
	  	}
	  }
	  finally 
		{
		  if (fis !=null) fis.close();
	  }
  }


/**
 *  Method to match the requested page type with the file to be returned to the 
 *  browser
 *
 *  @param pageType - the type of page requsted 
 */
	public void pageTranslator (String pageType) 
	{
		if (pageType.equals("createUser")) pageFileName=createUserPage;
		if (pageType.equals("plotQuery")) pageFileName=plotQueryPage;
		if (pageType.equals("communityQuery")) pageFileName=communityQueryPage;
		if (pageType.equals("plantQuery")) pageFileName=plantQueryPage;
		if (pageType.equals("login")) pageFileName=loginPage;
		if (pageType.equals("loggedin")) pageFileName=loggedinPage;
		if (pageType.equals("download")) pageFileName=downloadPage;
		if (pageType.equals("upload")) pageFileName=uploadPage;
	}
	  
}
