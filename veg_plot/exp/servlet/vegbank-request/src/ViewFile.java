import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;


public class ViewFile extends HttpServlet {

  public void doGet(HttpServletRequest req, HttpServletResponse res) 
                               throws ServletException, IOException {
    // Use a ServletOutputStream since we may pass binary information
    ServletOutputStream out = res.getOutputStream();

    // Get the file to view
    String file = req.getPathTranslated();
	out.println(file);
    // No file, nothing to view
    if (file == null) {
      out.println("No file to view");
      out.println(file);
      return;
    }

    // Get and set the type of the file
    String contentType = getServletContext().getMimeType(file);
    res.setContentType(contentType);
    out.println("contentType: "+contentType );

    // Return the file
    try {
      ViewFile.returnFile(file, out);
    }
    catch (FileNotFoundException e) { 
      out.println("File not found");
    }
    catch (IOException e) { 
      out.println("Problem sending file: " + e.getMessage());
    }
  }
  
  //send the contents of a file to the outputStream
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
	  
}
