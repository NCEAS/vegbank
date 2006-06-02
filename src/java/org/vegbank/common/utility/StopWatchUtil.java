package org.vegbank.common.utility;
/*
 * '$RCSfile: StopWatchUtil.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: berkley $'
 *	'$Date: 2006-06-02 21:15:15 $'
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
 */
/**
 * @author farrell
 *
 * A utility to assist in benchmarking
 */
public class StopWatchUtil
{
		
	static final int HOURS_PER_DAY = 24;
	static final int MINUTES_PER_HOUR = 60;
	static final int SECONDS_PER_MINUTE = 60;
	static final int MILLISECONDS_PER_SECOND = 1000;
	
	private long startTime = 0;
	private long stopTime = 0;
	private String taskName = "";
	
	public StopWatchUtil( String taskName )
	{
		this.taskName = taskName;	
	}
	
	public void startWatch()
	{
		//System.out.println("Starting clock on '" + taskName + "'");
		startTime = System.currentTimeMillis();
	}
	
	public void stopWatch()
	{
		//System.out.println("Stopping clock on '" + taskName + "'");
		stopTime = System.currentTimeMillis();
	}
	
	public void  printTimeElapsed()
	{
		String timeTaken = "";
		
		if ( startTime == 0 || stopTime == 0)
		{
			System.out.println("Error: Clock not started or stopped");
		}
		else
		{
			long millisecondsPassed = stopTime - startTime;
			this.printHMSM(  millisecondsPassed );
		}
	}
	
	/**
	 * Convert a milliseconds value into days, hours, minutes, seconds, millis
	 * 
	 * Prints out the value
	 * 
	 * @param togo
	 */
	public void printHMSM(long togo) 
	{
		int millis = (int)( togo % MILLISECONDS_PER_SECOND );
		/* /= is just shorthand for togo = togo / 1000 */
		togo /= MILLISECONDS_PER_SECOND ;

		int seconds = (int)( togo % SECONDS_PER_MINUTE );
		togo /= SECONDS_PER_MINUTE ;

		int minutes = (int)( togo % MINUTES_PER_HOUR );
		togo /= MINUTES_PER_HOUR ;

		int hours = (int)( togo % HOURS_PER_DAY );
		int days = (int)( togo / HOURS_PER_DAY ); 

		StringBuffer sb = new StringBuffer();
		sb.append( taskName + ": took " );
		
		
		if ( days != 0 )
		{
			if ( days == 1)
			{
				sb.append(days + " day ");
			}
			else
			{
				sb.append(days + " days ");
			}

		}
		if ( hours != 0 )
		{
			if ( hours == 1)
			{
				sb.append(hours + " hour ");
			}
			else
			{
				sb.append(hours + " hours ");
			}
		}
		
		if ( minutes != 0 )
		{
			sb.append(minutes + "min ");
		}
		
		if ( seconds != 0 )
		{
			sb.append(seconds + "sec ");
		}
		
		if ( millis != 0 )
		{
			sb.append(millis + "ms ");
		}
		
		sb.append("to complete.");

	   //System.out.println( sb.toString() );
	}

}
