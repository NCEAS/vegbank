/*
 *	'$RCSfile: MailerException.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: anderson $'
 *	'$Date: 2003-11-25 17:48:52 $'
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
 *
 * @author anderson
 */

package org.vegbank.common.utility.mail;

public class MailerException extends Exception {

	/**
	* Constructs a MailerException with no detail message.
	*/
	public MailerException() {
		super();
	}

	/**
	* Constructs a MailerException with the specified detail message.
	* A detail message is a String that describes this particular exception.
	*
	* @param message detailed message.
	*/
	public MailerException(String message) {
		super(message);
	}
}
