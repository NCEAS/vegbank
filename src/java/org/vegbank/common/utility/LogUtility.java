/*
 *	'$RCSfile: LogUtility.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2004-02-24 01:24:59 $'
 *	'$Revision: 1.5 $'
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

import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



/**
 * @author anderson
 */

public class LogUtility
{
	private static final boolean enable = Boolean.valueOf(ResourceBundle
			.getBundle("general").getString("logging.enable")).booleanValue();
		
    /**
     * Commons Logging instance.
     */
    protected static Log log = LogFactory.getLog(LogUtility.class);

	public static final int TRACE = 1;
	public static final int DEBUG = 2;
	public static final int INFO = 3;
	public static final int WARN = 4;
	public static final int ERROR = 5;
	public static final int FATAL = 6;
		
    // TODO:
    // Needs client code to give an indication of severity
    // Then the logged messages can be filtered according to 
    // taste using the logging properties files (src/java)
    

	public static void log(Object message) {
		if (enable && message != null) 
		{
			//System.out.println("LU--"+message.toString());
			if ( log.isDebugEnabled() )
			{
				log.debug("LU--"+message.toString());
			}
		}
	}
	
	public static void log( Object message, int severity )
	{
		switch (severity) 
		{
			case TRACE:
				if ( log.isTraceEnabled() )
				{
					log.trace( message.toString() );
				} 
				break;
			case DEBUG:
				if ( log.isDebugEnabled() )
				{
					log.debug( message.toString() );
				} 
				break;
			case INFO:
				if ( log.isInfoEnabled() )
				{
					log.info( message.toString() );
				} 
				break;
			case WARN:
				if ( log.isWarnEnabled() )
				{
					log.warn( message.toString() );
				} 
				break;
			case ERROR:
				if ( log.isErrorEnabled() )
				{
					log.error( message.toString() );
				} 
				break;
			case FATAL:
				if ( log.isFatalEnabled() )
				{
					log.fatal( message.toString() );
				} 
				break;
			default:
				log.debug( message.toString() );																
		}
	
	}
	
	public static void log(Object message, java.lang.Throwable t) {
		if (enable) 
		{
			if ( log.isErrorEnabled() )
			{
				String tmp = "";
				if (message != null) {
					tmp += message.toString();
				}
				if (t != null) {
					tmp += "\nEXCEPTION: " + t.toString();
					
					// Print out stackTrace TODO: make turn off configurable
					tmp = appendStackTrace(t, tmp);
				}
				//System.out.println("LU--"+message.toString());

				log.debug(tmp);
			}
			//System.out.println(tmp);
		}
	}

	private static String appendStackTrace(java.lang.Throwable t, String tmp)
	{
		StackTraceElement[] stElements = t.getStackTrace();
		tmp += "\n---------------Start StackTrace-------------------------";
		for ( int i=0; i<stElements.length ; i++)
		{
			tmp += "\n\t" + stElements[i];
		}
		tmp += "\n---------------End StackTrace-------------------------\n";
		return tmp;
	}
	
}
