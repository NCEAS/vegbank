/*
 * Created on Mar 6, 2004
 *	'$RCSfile: StatusBarUtil.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: farrell $'
 *	'$Date: 2004-03-07 17:55:28 $'
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
package org.vegbank.common.utility.CommandLineTools;

/**
 * @author Gabriel
 *
 * This is Utility stolen from Mark Andersons AccessionGen Class that
 * gives the user some indication of the progress of long running commands.
 * 
 * It is very cool
 */
public class StatusBarUtil {
	double scalar = .6; // width of screen
	long pct1, l = 0;
	boolean smallCount = false;
	
	/**
	 * Contruct a StatusBarUtil for printing out progress information
	 */
	public StatusBarUtil ()
	{
	}

	/**
	 * Indicates to the user that the process has finished
	 * 
	 * @param count
	 */
	public void completeStatusBar(long count) {
		if (smallCount) {
			int num = (int) (100 * scalar) - (int) pct1 * (int) l;
			for (int i = 0; i < num; i++) {
				System.out.print("-");
			}
		} else {
			int num = (int) ((100 * scalar) - ((int) count / (int) pct1));
			for (int i = 0; i < num; i++) {
				System.out.print("-");
			}
		}
		System.out.println("| 100%");
		System.out.println("updated " + l + "\n");
	}

	/**
	 * Update the status bar as chunks of work done
	 */
	public void updateStatusBar() {
		l++;
		if (smallCount) {
			for (int i = 0; i < pct1; i++) {
				System.out.print("-");
			}

		} else {
			if (l < pct1) {
				// do nothing
			} else if (l % pct1 == 0) {
				System.out.print("-");
			}
		}
	}

	/**
	 * Display the status bar setup to user
	 * @param count -- how many chunks of work to do
	 */
	public void setupStatusBar(long count) {

		pct1 = (long) ((count / 100) / scalar);
		if (pct1 < 1) {
			pct1 = (long) ((100 / count) * scalar);
			smallCount = true;
		} else {
			// correction
			pct1++;
		}

		System.out.print("0% |");
		for (int i = 0; i < 100 * scalar; i++) {
			System.out.print("-");
		}
		System.out.println("| 100%  ");
		// First part of true status bar
		System.out.print("0% |");
	}
}
