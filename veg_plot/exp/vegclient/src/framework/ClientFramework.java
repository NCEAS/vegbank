/**
 *
 *  Authors: @author@
 *  Release: @release@
 *	
 *  '$Author: harris $'
 *  '$Date: 2001-10-11 13:00:03 $'
 * 	'$Revision: 1.2 $'
 */
 
package vegclient.framework;

import javax.swing.filechooser.*;
import javax.swing.event.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;

import vegclient.framework.*;
 
public class ClientFramework extends JFrame {
   public ClientFramework() {
      //super("JFileChooser Directory Chooser Demonstration");
 		//	directoryChooser();
   }
	 
	 
	 
/**
 *  Method to copy a file
 *
 * @param  inFile  a string representing the input file
 * @param  outFile a string representing the output, compressed, file
 * @param appendFlag -- should the file be repaced or appended
 */
public void fileCopy (String inFile, String outFile, String appendFlag) 
{
	try
	{
		//the input
//		BufferedReader in = new BufferedReader(new FileReader(inFile));
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(inFile));
		
		//PrintStream out  = new PrintStream(new FileOutputStream(outFile, true));
		FileOutputStream out = new FileOutputStream(outFile, false);
		
//		if (appendFlag.equals("append")) 
//		{
//			out  = new PrintStream(new FileOutputStream(outFile, false)); 
//		}
//		if (appendFlag.equals("concat")) 
//		{
//			out  = new PrintStream(new FileOutputStream(outFile, true)); 
//		}
//		System.out.println("copying a file");
//		int c;
//		while((c = in.read()) != -1)
//        out.write(c);
//		in.close();
//		out.close();
		
			byte[] buf1 = new byte[4 * 1024]; //4K buffer
						int i = in.read(buf1);
						while( i  != -1 ) 
						{
							out.write(buf1, 0, i);
							i = in.read(buf1);
						}
						in.close();
						out.close();
	}
	catch(Exception e) 
	{
		System.out.println("Exception: ");
		e.printStackTrace();
	}
}



	 /**
	  * method to choose a directory
		*/
	 public String directoryChooser()
	 {
						JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int r = fc.showOpenDialog(ClientFramework.this); 
            while (r != JFileChooser.APPROVE_OPTION) 
						{
              JOptionPane.showMessageDialog(ClientFramework.this,"Try again, select a directory.", 
               "ERROR", JOptionPane.ERROR_MESSAGE); 
              r = fc.showOpenDialog(ClientFramework.this); 
            }
  
	
            System.out.println("Selected directory: " + fc.getSelectedFile());
 						return(fc.getSelectedFile().toString() );
	 }
	 
	 	
  /**
   * Print debugging messages based on severity level, where severity level 1
   * are the most critical and higher numbers are more trivial messages.
   * Messages with severity 1 to 4 will result in an error dialog box for the
   * user to inspect.  Those with severity 5-9 result in a warning dialog
   * box for the user to inspect.  Those with severity greater than 9 are
   * printed only to standard error.
   * Setting the debug_level to 0 in the configuration file turns all messages
   * off.
   *
   * @param severity the severity of the debug message
   * @param message the message to log
   */
  public static void debug(int severity, String message)
  {
		
//    if (debug) {
//      if (debug_level > 0 && severity <= debug_level) {
        // Show a dialog for severe errors
//        if (severity < 5) {
					JOptionPane.showMessageDialog(null, message, "Error!",
                                        JOptionPane.ERROR_MESSAGE);
																				
     //     JOptionPane.showMessageDialog(null, message, "Error!");
//        } else if (severity < 10) {
//          JOptionPane.showMessageDialog(null, message, "Warning!",
 //                                       JOptionPane.WARNING_MESSAGE);
//        }

        // Everything gets printed to standard error
        System.err.println(message);
 //     }
 //   }
  } 
	
	 
	 /**
	  * method that allows the user to choose a file, and where the 
		* directory is returned too
		*/
		 public String fileChooser()
	 {
		 //get the current directory
			String dir = System.getProperty("user.dir");
			System.out.println("currebt dir: " + dir);
			File directory = new File(dir);
		
			JFileChooser fc = new JFileChooser(directory);
      fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
      int r = fc.showOpenDialog(ClientFramework.this); 
     // while (r != JFileChooser.APPROVE_OPTION) 
		//	{
		//		JOptionPane.showMessageDialog(ClientFramework.this,"Try again, select a directory.", 
		//		 "ERROR", JOptionPane.ERROR_MESSAGE); 
		//			r = fc.showOpenDialog(ClientFramework.this); 
    //  }
			System.out.println("Selected directory: " + fc.getSelectedFile());
			return(fc.getSelectedFile().toString() );
	 }
	 
	 
	 /**
	  * main method for testing
		*/
   public static void main(String[] args) {
      ClientFramework main = new ClientFramework();
      main.pack();
      main.setVisible(true);
			main.fileChooser();
   }
}


