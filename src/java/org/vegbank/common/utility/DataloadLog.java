/*
 *	'$RCSfile: DataloadLog.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2005-02-11 00:28:27 $'
 *	'$Revision: 1.1 $'
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
 */

package org.vegbank.common.utility;

import java.io.*;
import java.util.*;
import javax.mail.MessagingException;
import javax.mail.internet.*;

//import org.vegbank.common.utility.mail.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Creates and manages a dataload log.
 *
 * @author anderson
 */
public class DataloadLog
{

	public static final String TPL_SUCCESS = "dataload-success.vm";
	public static final String TPL_FAILURE = "dataload-failure.vm";
	public static final String TPL_ADMIN = "dataload-log.vm";
	public static final String TPL_LOG = "dataload-log.vm";

	protected static Log log = LogFactory.getLog(DataloadLog.class);


	private String userEmail = "";
	private String fileSavePath = "";
    private List defTranscript = null;
    private Map defCountMap = null;
    private Map transcripts = null;
    private Map countMaps = null;
    private Map tagTable = null;



    /**
     *
     */
    public DataloadLog(String saveDir) {
        if (Utility.isStringNullOrEmpty(saveDir)) {
            // no dir specified so use data dir
            saveDir = Utility.VB_DATA_DIR;

        } else if (!saveDir.startsWith(File.separator) &&
                !saveDir.startsWith(Utility.VB_DATA_DIR)) {
            // prepend the data dir
            fileSavePath = Utility.VB_DATA_DIR + saveDir;
        } else {
            // absolute path or starts with data dir
            fileSavePath = saveDir;
        }

        if (!fileSavePath.endsWith(File.separator)) {
            fileSavePath += File.separator;
        }

        log.debug("set datalog save dir to " + fileSavePath);

        defCountMap = new HashMap();
        defTranscript = new ArrayList();
        countMaps = new HashMap();
        transcripts = new HashMap();
        tagTable = new HashMap();
    }


    /**
     * Creates default transcript if null
     * Adds an entry to the default transcript
     */
    public void append(String entry) {
        defTranscript.add(entry);
    }


    /**
     * Creates given transcript if null
     * Adds an entry to the given transcript
     */
    public void append(String transcriptName, String entry) {

        List l = (List)transcripts.get(transcriptName);
        if (l == null) {
            l = new ArrayList();
        }
        l.add(entry);
        transcripts.put(transcriptName, l);
    }


    /**
     * Creates default hash if null
     * Increments item count in default hash
     */
    public void increment(String itemKey) {
        int c = 0;
        Object o = defCountMap.get(itemKey);
        if (o != null) {
            c = ((Integer)o).intValue();
        }

        c++;
        defCountMap.put(itemKey, new Integer(c));
    }


    /**
     * Creates given hash if null
     * Increments item count in given hash
     */
    public void increment(String hashName, String itemKey) {
        int c = 0;
        Map m = (Map)countMaps.get(hashName);

        if (m == null) {
            m = new HashMap();

        } else {
            Object o = m.get(itemKey);
            if (o != null) {
                c = ((Integer)o).intValue();
            }
        }

        c++;
        m.put(itemKey, new Integer(c));

        // replace the count map
        countMaps.put(hashName, m);
    }


    /**
     * Get the count for given item in default hash
     */
    public int getCount(String itemKey) {
        Object o = defCountMap.get(itemKey);
        if (o == null) {
            return 0;
        }

        return ((Integer)o).intValue();
    }


    /**
     * Gets the count for given item in given hash
     */
    public int getCount(String hashName, String itemKey) {
        // get the proper map
        Map m = (Map)countMaps.get(hashName);
        if (m == null) {
            return 0;
        }

        // get the item count value
        Object o = m.get(itemKey);
        if (o == null) {
            return 0;
        }

        return ((Integer)o).intValue();
    }


    /**
     * Zeros the count for given item in default hash
     */
    public void resetCount(String itemKey) {
        Object o = countMaps.get(itemKey);
        if (o != null) {
            countMaps.put(itemKey, new Integer(0));
        }
    }


    /**
     * Zeros the count for given item in given hash
     */
    public void resetCount(String hashName, String itemKey) {
        Map m = (Map)countMaps.get(hashName);
        if (m == null) {
            return;
        }

        Object o = m.get(itemKey);
        if (o != null) {
            m.put(itemKey, new Integer(0));
            countMaps.put(hashName, m);
        }
    }


    /**
     * Adds a tag for template swapping.
     */
    public void addTag(String tag, Object value) {
        tagTable.put(tag, value);
    }


    /**
     * Returns list of entries for default transcript
     */
    public List getTranscriptAsList() {
        return defTranscript;
    }


    /**
     * Returns list of entries for given transcript
     */
    public List getTranscriptAsList(String transcriptName) {
        return (List)transcripts.get(transcriptName);
    }


    /**
     * swaps all transcripts and hashed counts into a template
     */
    public String getFormatted(String templateName) {
		// load the template
		VelocityParser velo = new VelocityParser(templateName);
		velo.putAll(getTemplateTags());

		return velo.processTemplate();
    }


    /**
     * Build a tag table for swapping into a template.
     * "countmap", "transcript", and all of the identifiers
     * used when incrementing and appending will be
     * populated in the template scope.
     */
    private Map getTemplateTags() {
        addTag("countmap", defCountMap);
        addTag("transcript", defTranscript);

        String key;
        Iterator kit = countMaps.keySet().iterator();
        while (kit.hasNext()) {
            key = (String)kit.next();
            addTag(key, countMaps.get(key));
        }

        kit = transcripts.keySet().iterator();
        while (kit.hasNext()) {
            key = (String)kit.next();
            addTag(key, transcripts.get(key));
        }

        return tagTable;
    }

    private String getTemplateTagsSB() {
        String body;
		StringBuffer sb = new StringBuffer();

        String key;
        Iterator kit = countMaps.entrySet().iterator();
        while (kit.hasNext()) {
            Map.Entry pair = (Map.Entry)kit.next();
            sb.append(pair.getKey() + " records added to VegBank: " + pair.getValue() + "\n" );
            sb.append(System.getProperty("line.separator"));
            //addTag(key, countMaps.get(key));
        }

      //  sb.append("end of countMaps, now on to tagTable.");
      //  sb.append(System.getProperty("line.separator"));
        kit = tagTable.entrySet().iterator();
        while (kit.hasNext()) {

           Map.Entry pair = (Map.Entry)kit.next();
            sb.append(pair.getKey() + "= " + pair.getValue()  );
           // addTag(key, transcripts.get(key));
        }
        body = sb.toString();
        return body;
    }



    /**
     * send formatted log via email
     */
    public void send(String subject, String to, String from, String templateName)
			throws AddressException, MessagingException {

        if (Utility.isStringNullOrEmpty(to)) {
            // the given address overrides class member
            to = this.userEmail;
        }

		// templates do not work any longer: ServletUtility.sendEmailTemplate(templateName,
	   //			getTemplateTags(), from, to, null, subject, true);

	   	// Send the email
	   				String mailHost = null;

	   				String body;



	   				StringBuffer sb = new StringBuffer();

                    if (templateName.equals("dataload-failure.vm")) {
						sb.append("Your data load has unfortunately not succeeded. ");


					} else if (templateName.equals("dataload-success.vm")) {
						sb.append("Thank you for loading data to VegBank.  Your data have been added successfully. \n");
					} else {
						 //any data can be passed here
				         sb.append(templateName);
					}
                    sb.append(getTemplateTagsSB());

                   sb.append("\n\n--The VegBank Team \n dba@vegbank.org \n");

	   				body = sb.toString();

				ServletUtility.sendPlainTextEmail( mailHost, from, to, "dba@vegbank.org", subject, body);


    }


    /**
     * save formatted file to disk
     * @param fileName to write the log to
     * @param templateName to use as a template
     */
    public void saveFormatted(String fileName, String templateName)
            throws IOException {
        log.debug("saving formatted template " + templateName + " to " + fileSavePath + fileName);
        Utility.saveFile(new StringReader(this.getFormatted(templateName)),
                    fileSavePath + fileName);
    }


    /**
     * Serialize this log to the disk.
     *
     * @param fileName
     * @throws IOException
     */
    public void saveBinary(String fileName) throws IOException {
        FileOutputStream fileOut = new FileOutputStream(new File(fileSavePath, fileName));
        ObjectOutputStream objectOut = new ObjectOutputStream (fileOut);
        objectOut.writeObject(this);
    }


    /**
     * Retrieves a log from disk.
     *
     * @param fileName
     * @return the DataloadLog requested
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public DataloadLog open(String fileName) throws IOException, ClassNotFoundException {
        FileInputStream fileIn = new FileInputStream(new File(fileSavePath, fileName));
        ObjectInputStream logIn = new ObjectInputStream(fileIn);
        return (DataloadLog)logIn.readObject();
    }


    /**
     *
     */
    public void setUserEmail(String e) {
        userEmail = e;
    }


    /**
     *
     */
    public String getUserEmail() {
        return userEmail;
    }

}
