/*
 *	'$RCSfile: MailTemplate.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2005-02-11 00:36:08 $'
 *	'$Revision: 1.2 $'
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
 * MailTemplate loads and sends tag-swappable e-mail templates
 * with locale, caching, rich-text and plain-text support.  
 * Also can be run in its own thread, perfect for web apps.
 *
 * To load templates, use the generic load() method to load a template by name.
 * ex: load( "nameOfYourTemplate.template" );
 * 
 * @author anderson
 */

package org.vegbank.common.utility.mail;

import java.util.*;
import java.io.*;

import org.vegbank.common.utility.*;


public class MailTemplate implements Runnable {

	protected String currentTemplate;
	protected String type;
	protected String locale;
	protected String subject, to, cc, bcc, from;
	protected boolean doFormat = false;
	protected Map tagTable;
	protected TemplateLoader tl;
	protected boolean richText;



  // ======= CONSTRUCTORS ============================================
	/**
	 * 
	 **/
	public MailTemplate() {
		this.tagTable = null;

		setAllProperties();
	}



	/**
	 * 
	 **/
	public MailTemplate(TemplateLoader tl) {
		this.tl = tl;
		this.tagTable = null;

		setAllProperties();
	}



  // ======= WORKHORSE METHOD =========================================
	/**
	 * This is the method that actually gets the template.  
	 * The other methods just call this in various ways.
	 * The template is loaded into the MailTemplate instance
	 * and is returned to the caller.
	 *
	 * @param file the name of the file to load
	 * @return the template in that file
	 **/
	public String load(String file) { //throws TemplateException {
		this.currentTemplate = this.tl.loadTemplate(this.type, this.locale,  file);
		return this.currentTemplate;
	}


	public String loadDirect(String file) { //throws TemplateException {
		this.currentTemplate = this.tl.loadTemplate(file);
		return this.currentTemplate;
	}


	public String loadFile(String path) throws IOException {
		File f = new File(path);
		BufferedReader in = new BufferedReader(
			new FileReader(f));
		StringBuffer sb = new StringBuffer((int)f.length());
		String line;
		while ((line=in.readLine()) != null)
			sb.append(line).append("\n");

		this.currentTemplate = sb.toString();
		return this.currentTemplate;
	}


  // ======= WHATEVER METHODS =========================================
	/**
	 * Use to set the mail to rich text (HTML) mode and send at 
	 * at the same time.
	 * @param sendAsHTML true or false
	 **/
	public void run() {
		try {
			send();

		} catch (MailerException mex) {
			String msg = "MailTemplate.run: Send problem: "+mex.toString();
			LogUtility.log(msg);
		}
	}

	/**
	 * Use to set the mail to rich text (HTML) mode and send at 
	 * at the same time.
	 * @param sendAsHTML true or false
	 **/
	public void send(boolean sendAsHTML) throws MailerException {
		this.richText = sendAsHTML;
		send();
	}

	/**
	 * Swaps all tags into the loaded template before
	 * sending the message to the already specified recipients.
	 **/
	public void send() throws MailerException {

		Mailer m = new Mailer();

		// Make sure the message body is valid
		if (this.currentTemplate == null || this.currentTemplate.length() == 0) {
			LogUtility.log("MT.send:  no body!");
			throw new MailerException("Empty message body.");
		}

		// Swap out the tags
		if (this.tagTable != null) {
			TagSwapper swapper = new TagSwapper();
			this.currentTemplate = swapper.swap(this.currentTemplate, 
					(HashMap)this.tagTable);
		}


		// Get the header fields, if they exist, and remove them
		// from the current template (in this.currentTemplate)
		String body = extractFieldsFromTemplate();


		// Make sure that there is at least one recipient
		if (this.to == null && this.cc == null && this.bcc == null)
			throw new MailerException("No recipent specified.");

		if (this.to == null) this.to = "";
		if (this.cc == null) this.cc = "";
		if (this.bcc == null) this.bcc = "";
		if (this.from == null) this.from = "";
		if (this.subject == null) this.subject = "";


		try {
		if (this.doFormat)
			body = TextUtils.formatBody(body, false);
		} catch (IOException ioe) {
			LogUtility.log("MailTemplate.send: Cannot format body: " + ioe);
		}

		if (this.richText)
			m.sendRich( body, this.subject, this.to, this.cc, this.bcc, this.from);
		else
			m.sendPlain( body, this.subject, this.to, this.cc, this.bcc, this.from);
	}



  // ======= RETRIEVAL METHODS ======================================
	/**
	 * Returns labels and values for the current template's subject,
	 * to, cc, bcc, and from fields.
	 * @return a string of all header fields.
	 */
	public String getHeader() { 
		return new StringBuffer("Subject: ").append(getSubject())
			.append("  To: ").append(getTo())
			.append("  CC: ").append(getCc())
			.append("  BCC: ").append(getBcc())
			.append("  From: ").append(getFrom())
			.toString();
	}

	/**
	 * Returns labels and values for the current template's 
	 * to, cc, and bcc fields.
	 * @return a string of all header fields.
	 */
	public String getRecips() { 
		return new StringBuffer("To: ").append(getTo())
			.append("  CC: ").append(getCc())
			.append("  BCC: ").append(getBcc())
			.toString();
	}

	public String getType()
	{ return this.type; }

	public String getLocale()
	{ return this.locale; }

	public String getTo()
	{ return this.to; }

	public String getCc()
	{ return this.cc; }

	public String getBcc()
	{ return this.bcc; }

	public String getFrom()
	{ return this.from; }

	public String getSubject()
	{ return this.subject; }

	public String getBody()
	{ return this.currentTemplate; }

	public boolean getDoFormat()
	{ return this.doFormat; }


  // ======= SETUP METHODS ===========================================
	public void setType(String type)
	{ this.type = type; }


	public void setLocale(String locale)
	{ this.locale = locale; }


	public void setTagTable(Map tagTable)
	{ this.tagTable = tagTable; }


	public void setHeader(String subject, String to, 
			String cc, String bcc, String from) { 
		this.subject = subject; 	
		this.to = to;
		this.cc = cc;	
		this.bcc = bcc;	
		this.from = from;
	}

	public void setRecips(String to, String cc, String bcc) { 
		this.to = to;
		this.cc = cc;	
		this.bcc = bcc;
	}

	public void setTo(String to)
	{ this.to = to; }

	public void setCc(String cc)
	{ this.cc = cc; }

	public void setBcc(String bcc)
	{ this.bcc = bcc; }

	public void setFrom(String from)
	{ this.from = from; }

	public void setSubject(String subject)
	{ this.subject = subject; }

	public void setBody(String body)
	{ this.currentTemplate = body; }

	public void setDoFormat(boolean doFormat)
	{ this.doFormat = doFormat; }



	/**
	 *
	 **/
	private void setAllProperties() {
		this.type = "mail.templates";
		this.locale = "en.us";
	}


	/**
	 * Extract and removes lines with "from:", "to:", "cc:",
	 * and "bcc:" directives from this.currentTemplate.
	 *
	 * @return The template without the header directives
	 **/
	private String extractFieldsFromTemplate() {
		if (this.currentTemplate == null)
			return new String();

		String tmp, tmp2, line;
		StringTokenizer st;
		StringBuffer sb = new StringBuffer();

		tmp = this.currentTemplate.toString();
		BufferedReader br = new BufferedReader( new StringReader(tmp) );

		try {

			while ((line=br.readLine()) != null) {

				st = new StringTokenizer(line, ":");

				if (st.hasMoreTokens()) {
					tmp2 = st.nextToken();

					tmp2 = tmp2.trim();

					if (tmp2.equalsIgnoreCase("this.subject"))
						this.subject = getRemainingTokens(st);
					else if (tmp2.equalsIgnoreCase("this.to"))
						this.to = getRemainingTokens(st);
					else if (tmp2.equalsIgnoreCase("this.from"))
						this.from = getRemainingTokens(st);
					else if (tmp2.equalsIgnoreCase("this.cc"))
						this.cc = getRemainingTokens(st);
					else if (tmp2.equalsIgnoreCase("this.bcc"))
						this.bcc = getRemainingTokens(st);
					else {
						sb.append(line);
						if (!line.trim().equals("\n"))
							sb.append("\n");
					}
				} else {
					sb.append(line);
					if (!line.trim().equals("\n"))
						sb.append("\n");
				}
			}
		} catch (IOException ioe) {
			LogUtility.log("MailTemplate.extract: Caught IOException: ", ioe);
		}

		return sb.toString();
	}

	private String getRemainingTokens(StringTokenizer st) {
		StringBuffer sb = new StringBuffer();
		while (st.hasMoreElements()) {
			String str = st.nextToken();
			sb.append(str);
			if (str.toLowerCase().endsWith("tag"))
				sb.append(":");
		}
		return sb.toString();
	}


	/*
	public static MailTemplate loadXML(InputStream aStream) { 

		Digester digester = new Digester(); 
		digester.setValidating(false); 
		digester.addObjectCreate("email", EmailTemplate.class); 

		digester.addBeanPropertySetter("email/subject", "subject"); 
		digester.addBeanPropertySetter("email/body", "body"); 
		digester.addBeanPropertySetter("email/from", "from"); 
		digester.addCallMethod("email/to", "addTo", 0); 
		digester.addCallMethod("email/cc", "addCc", 0); 
		digester.addCallMethod("email/bcc", "addBcc", 0); 

		try { 
			return (EmailTemplate)digester.parse(aStream); 
		} catch (IOException e) { 
			LogUtility.log("MailTemplate Error: ", e); 
			return null; 
		} catch (SAXException e) { 
			LogUtility.log("MailTemplate Error: ", e); 
			return null; 
		} 
	}
	*/
	
}
