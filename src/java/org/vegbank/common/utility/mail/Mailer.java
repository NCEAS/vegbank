/*
 *	'$RCSfile: Mailer.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2005-02-11 00:36:08 $'
 *	'$Revision: 1.3 $'
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * 
 * DESCRIPTION:
 * Sends rich and plain text e-mail messages.
 *
 * @author anderson
 */


package org.vegbank.common.utility.mail;

import java.io.*;
import java.util.*;
import javax.mail.*;
import javax.activation.*;
import javax.mail.internet.*;

import org.vegbank.common.utility.*;


public class Mailer
{
	private Session session;
	private boolean attemptedRestart = false;


	public static void main(String args[])
	{
		Mailer m = new Mailer();
		//ram.sendRich();

		GregorianCalendar calendar = new GregorianCalendar();
		String d = new String();
		String t = new String();
		String zone = new String();

		int nDate = calendar.get(Calendar.DATE); 
		int nMin = calendar.get(Calendar.MINUTE);
		int nHour = calendar.get(Calendar.HOUR_OF_DAY);
		int nAmpm;

		nAmpm = (nHour < 12) ? 0 : 1;

		if (nHour > 12)
			nHour -= 12;

		d = (calendar.get(Calendar.MONTH) + 1) + "/" + 
			((nDate < 10) ? "0" : "") + nDate + "/" + 
			calendar.get(Calendar.YEAR);

		t = nHour + ":" + 
			((nMin < 10) ? "0" : "") + nMin + " " +
			((nAmpm == 1) ? "PM" : "AM");


		//System.err.println("Date: " + t + " || " + d);
		int argc = args.length;

		try
		{
			if (argc == 0)
			{
				System.out.print("Sending mail to mark@hldesign.com...");

				// Send it
				m.sendPlain("Test mail.\nSystem date/time is: " + d + 
					"  " + t,
					(new Date()).toString(), 
					"mark@hldesign.com", // To
					"", // CC
					"", // BCC
					"TestMail@hldesign.com");  // From

				System.out.println("MAIL SENT!");
				System.out.println("\nUsage:  Mailer" +
					"\t\t-- or --\t" +
					"Mailer <to> <cc> <bcc> <from> <subject> <body>\n");

			}
			else if (argc == 6)
			{
				String body = "System date/time is: " + d + 
					"  " + t + "\n\n" + args[5];

				System.out.print("Sending mail...");
				m.sendPlain(body,	// Body 
						args[4],	// Subject 
						args[0],    // To
						args[1], 		// CC
						args[2], 		// BCC
						args[3]);   // From

				System.out.println("MAIL SENT!");
			}
			else
				System.out.println("\nUsage:  Mailer" +
					"\t\t-- or --\t" +
					"Mailer <to> <cc> <bcc> <from> <subject> <body>\n");
		} 
		catch (Exception ex)
		{
			System.err.println("\n\nUNABLE TO SEND MAIL:  " + ex + "\n");
			ex.printStackTrace();
		}
	}
//////////////////////////////////////////////////////////


	public Mailer()
	{
		init();
	}

	private void init() {
		this.session = Session.getDefaultInstance(System.getProperties(), null);
	}

    public void sendPlain(String message, String subject,              
			String to, String cc, String bcc, String from)
			throws MailerException
	{
		String mailer = "General Mailer";
		try 
		{
			// construct the message
			Message msg = new MimeMessage(this.session);
			msg.setFrom(new InternetAddress(from));

			// Set the recipients
			msg.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(to, false));
			
			if (cc != null)
				msg.setRecipients(Message.RecipientType.CC,
					InternetAddress.parse(cc, false));
			if (bcc != null)
				msg.setRecipients(Message.RecipientType.BCC,
					InternetAddress.parse(bcc, false));

			msg.setSubject(subject);
			//StringBuffer sb = new StringBuffer(message);

			msg.setDataHandler(new DataHandler(
				new ByteArrayDataSource(message, "text/plain")));

			msg.setHeader("X-Mailer", mailer);
			msg.setSentDate(new Date());

			// Use the local send method
			send(msg); 
		}
		catch (Exception e) 
		{
			String exMsg = "**Mailer.sendPlain(): ";
			if (this.attemptedRestart)
				exMsg += "ATTEMPTED RESTART.   ";
			this.attemptedRestart = false;
			exMsg += e.getMessage();

			MailerException e2=new MailerException(exMsg);
			e2 = (MailerException)e2.fillInStackTrace();
			throw e2;
		}
	}

    public void sendRich(String message, String subject,              
			String to, String cc, String bcc, String from)
			throws MailerException
	{
		/*
		String subject = "JavaMail Tester";
		String to = "andersop@central.sun.com";
		String cc = "pmark.anderson@sun.com";
		String bcc = "andersop@colorado.edu";
		*/
		String mailer = "AspenOS Mail Service";

		try 
		{
			// construct the message
			Message msg = new MimeMessage(this.session);
			msg.setFrom(new InternetAddress(from));

			msg.setRecipients(Message.RecipientType.TO,
						InternetAddress.parse(to, false));
			
			if (cc != null)
				msg.setRecipients(Message.RecipientType.CC,
							InternetAddress.parse(cc, false));
			if (bcc != null)
				msg.setRecipients(Message.RecipientType.BCC,
							InternetAddress.parse(bcc, false));

			msg.setSubject(subject);
			msg.setDataHandler(new DataHandler(
				new ByteArrayDataSource(message, "text/html")));

			msg.setHeader("X-Mailer", mailer);
			msg.setSentDate(new Date());

			// Use the local send method
			send(msg);
		}
		catch (Exception e) 
		{
			String exMsg = "Mailer.sendRich(): ";
			if (this.attemptedRestart)
				exMsg += "ATTEMPTED RESTART.   ";
			this.attemptedRestart = false;
			exMsg += e.getMessage();

			MailerException e2=new MailerException(exMsg);
			e2 = (MailerException)e2.fillInStackTrace();
			throw e2;
		}
	}

	public void createHtmlBody(Message msg) throws Exception
	{
		/*
		String line;
		StringBuffer sb = new StringBuffer();

		sb.append("<HTML>\n");
		sb.append("<HEAD>\n");
		sb.append("<TITLE>\n");
		sb.append(msg.getSubject() + "\n");
		sb.append("</TITLE>\n");
		sb.append("</HEAD>\n");

		sb.append("<BODY>\n");
		sb.append("<H1>" + 
				"This mail proves that HTML mails can be automated!" +
				"</H1>" + "\n");

		// Import the header
		//FileInputStream in = new FileInputStream( "/usr/local/data/" + 
				//"RAHeader.html" );

		while ((line = in.readLine()) != null) 
		{
			sb.append(line);
			sb.append("\n");
		} 

		// Import the the data (body)
		// Import the footer

		sb.append("</BODY>\n");
		sb.append("</HTML>\n");

		msg.setDataHandler(new DataHandler(
			new ByteArrayDataSource(sb.toString(), "text/html")));
		*/
	}

	public Message createMultipartMessage() throws MessagingException
	{
		MimeMessage m = new MimeMessage(this.session);

		MimeBodyPart bp1 = new MimeBodyPart();
		bp1.setText("Bodypart 1");
		MimeBodyPart bp2 = new MimeBodyPart();
		bp2.setText("Bodypart 2");
		MimeMultipart mp = new MimeMultipart();
		mp.addBodyPart(bp1);
		mp.addBodyPart(bp2);
		m.setContent(mp);

		return m;
	}

	public void send(Message message) throws MessagingException
	{
		Transport.send(message);
	}
}


/* This class implements a typed DataSource from :
 * 	an InputStream
 *	a byte array
 * 	a String
 */
class ByteArrayDataSource implements DataSource 
{
    private byte[] data; // data
    private String type; // content-type

    /* Create a datasource from an input stream */
    ByteArrayDataSource(InputStream is, String type) throws IOException
	{
        this.type = type;
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		int ch;

		while ((ch = is.read()) != -1)

		// XXX : must be made more efficient by
		// doing buffered reads, rather than one byte reads
		os.write(ch);
		data = os.toByteArray();
    }

    /* Create a datasource from a byte array */
    ByteArrayDataSource(byte[] data, String type) 
	{
        this.data = data;
		this.type = type;
    }

    /* Create a datasource from a String */
    ByteArrayDataSource(String data, String type) 
	{
		try
		{
			// Assumption that the string contains only ascii
			// characters ! Else just pass in a charset into this
			// constructor and use it in getBytes()
			this.data = data.getBytes("iso-8859-1");
		}
		catch (UnsupportedEncodingException uex) 
		{ 
			LogUtility.log("Mailer: UnsupportedEncodingException: ", uex);
		}

		this.type = type;
    }

    public InputStream getInputStream() throws IOException 
	{
		if (data == null)
			throw new IOException("no data");
		return new ByteArrayInputStream(data);
    }

    public OutputStream getOutputStream() throws IOException 
	{
		throw new IOException("cannot do this");
    }

    public String getContentType() 
	{
        return type;
    }

    public String getName() 
	{
        return "dummy";
    }


}

