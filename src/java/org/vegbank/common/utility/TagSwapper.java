/*
 *	'$RCSfile: TagSwapper.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2003-11-25 17:48:23 $'
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
 *
 * 
 * DESCRIPTION:
 * Read a template and replace every occurence of a {tag} with string.
 *
 * @author anderson
 */

package org.vegbank.common.utility;

import java.util.*;

/**
 *
 */
public class TagSwapper {

	/**
	 * 
	 */
	public TagSwapper() {
	}
	
	
	/**
	 * swap()
	 */
	public String swap(String inString, HashMap tagTable) {

		if (inString == null) 
			return null;
		if (tagTable == null)
			return inString;

		StringBuffer inBuffer = new StringBuffer(inString);
		
		boolean inTag = false;
		boolean inDefval = false;
		char [] token = new char[4];
		StringBuffer tag = null;
		StringBuffer defval = null;
		String tagValue = null;
		int bufferLength = inBuffer.length();
		int numberOfTags = tagTable.size();

		
		StringBuffer outBuffer = new StringBuffer(
				(bufferLength + (numberOfTags * 100)));
		
		try {
			char array[] = new char [bufferLength];
			inBuffer.getChars(0, bufferLength, array, 0);
			int i;	
			for(i=0; i < bufferLength; i++) {
				switch(array[i]) {
					case '[':
						// Make sure we don't throw an array exception
						if (bufferLength - i < 6) {
							outBuffer.append(array[i]);
							continue;
						}

						// read the next 4 chars
						for(int j = 0; j < 4; j++)
							token[j] = array[i+j+1];
							
						if(String.valueOf(token).equals("tag:") ) {
							i+=4;
							inTag = true;
							tag = new StringBuffer(25);

						} else {

							outBuffer.append(array[i]);
							continue;
						}
						break;
					case '\\':
						if(i<bufferLength-1) {
							if (array[i+1] == ':') {
								if (inTag)
									tag.append(array[++i]);
								else if (inDefval)
									defval.append(array[++i]);
								else
									outBuffer.append(array[i]);
							}
							else
								outBuffer.append(array[i]);
						}
						break;
					case ':':
						if(inTag) {
							inTag = false;
							inDefval = true;
							defval = new StringBuffer(25);
						} else {
						   outBuffer.append(array[i]);
						   continue;
						}
						break;
					case ']':
						if(inTag) {
							inTag = false;
							Object o = tagTable.get(tag.toString());
							if (o==null) tagValue=null;
							else tagValue = o.toString();
							outBuffer.append((tagValue==null) ? "" : tagValue);
						} else if (inDefval) {
							inDefval = false;
							tagValue = (String)tagTable.get(tag.toString());

							// If tag not in hash and there is a defval
							if (tagValue == null && defval != null)
								outBuffer.append(defval);
							else
								outBuffer.append(tagValue);
						} else {
						   outBuffer.append(array[i]);
						   continue;
						}
						break;
					default:
						if (inTag)
							tag.append(array[i]);
						else if (inDefval)
							defval.append(array[i]);
						else
							outBuffer.append(array[i]);
				}
			}
		} catch( Exception e ) {
			LogUtility.log("TagSwapper.swap ", e);
		}
		return outBuffer.toString();
	}


	public static void main(String args[]) {
		HashMap tagTable = new HashMap();
		tagTable.put("tag1", "This is Tag I");
		tagTable.put("tag2", "This is Tag II");

		TagSwapper swapper = new TagSwapper();
		TemplateLoader tl = new TemplateLoader();
		String templ = tl.loadTemplate(
				"/usr/vegbank/templates/trial_signup.msg", false);
		String page = swapper.swap(templ, tagTable);

		System.out.println("Final page:\n" + page);
	}
}



