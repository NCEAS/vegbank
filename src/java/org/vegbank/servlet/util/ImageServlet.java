package org.vegbank.servlet.util;

import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import java.io.*;

public class ImageServlet extends HttpServlet {
	private String docHome = ".";

	public void service(HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession(true);
		ServletConfig config = getServletConfig();
		ServletContext application = config.getServletContext();

		File file = findFile(request, response);
		if (file == null) {
			return;
		} else {
			response.setContentType(application.getMimeType(file.getName()));
			response.setContentLength((int) file.length());
			sendFile(file, response);
		}

	}


	protected File findFile(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		// We should store a flow-to-image mapping and return the appropriate
		// File, without any platform-specific paths. Right now we will just
		// return a GIF file.
		File file = new File(
				getServletConfig().getInitParameter("base_img_dir") +
				"/test.gif");

		return file;
	}

	protected void sendFile(File file, HttpServletResponse response)
			throws IOException {
		int c = 0;
		FileInputStream fileinputstream = null;
		try {
			ServletOutputStream servletoutputstream =
				response.getOutputStream();
			fileinputstream = new FileInputStream(file);

			while ((c = fileinputstream.read()) != -1) {
				servletoutputstream.write(c);
			}

			servletoutputstream.flush();
		}
		finally {
			if (fileinputstream != null) {
				fileinputstream.close();
			}
		}
	}

} 
