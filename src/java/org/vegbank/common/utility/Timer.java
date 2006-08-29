/*
*  '$RCSfile: Timer.java,v $'
 *  Purpose: A utility class for the VegBank database access module
 *  Copyright: 2002 Regents of the University of California and the
 *             			National Center for Ecological Analysis and Synthesis
 *
 *	'$Author: berkley $'
 *	'$Date: 2006-08-29 21:48:29 $'
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
 */
package org.vegbank.common.utility;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author chad berkley
 *
 * Do some simple time calculations and log them to the log file
 */
public class Timer
{
  //change this to false if you don't want the Timer to print to std out.
  private static boolean SYS_OUT = false;
	private static Log log = LogFactory.getLog(Timer.class);
  private long startTime;
  private long endTime;
  private String msg;
  private long quotient = 1000;
  
  /**
   * create a new timer instance and start the clock.
   */
  public Timer(String msg)
  {
    this(msg, false);
  }
  
  /**
   * create a new timer and tell it if you want the display to be in ms.  
   * if useMilliseconds is fale, it will display in seconds.
   */
  public Timer(String msg, boolean useMilliseconds)
  {
    startTime = System.currentTimeMillis();
    this.msg = msg;
    if(useMilliseconds)
    {
      quotient = 1;
    }
  }
  
  /**
   * stop the clock and print the results to system.out and to the log
   */
  public void stop()
  {
    endTime = System.currentTimeMillis();
    long totalTime = (endTime - startTime)/quotient;
    String unit;
    if(quotient == 1000)
      unit = "seconds";
    else
      unit = "milliseconds";
    String printMsg = "TIMER: " + msg + " :::total time: " + totalTime + " " + unit;
    
    //log.debug(printMsg);
    if(SYS_OUT)
      System.out.println(printMsg);
  }
}
