
package org.vegbank.nvcrs.util;

/**
 * This class makes it easier to print out debug statements.
 * The Debug.print statements are printed to System.err
 * if debuggingOn = true.
 */

public final class Debug {

    public static final boolean debuggingOn = true;

    public static final void print(String msg) {

        if (debuggingOn) {
           System.err.println("Debug: " + msg);
        }
    }

    public static final void print(String msg, Object object) {

        if (debuggingOn) {
           System.err.println("Debug: " + msg);
           System.err.println("       " + object.getClass().getName());
        }
    }

}  // Debug
