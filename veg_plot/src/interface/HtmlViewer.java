/*
 * HtmlViewer.java
 *
 * Created on February 20, 2001, 2:52 PM
 */
//import client.*;
import java.io.*;
import java.sql.*;
import java.text.*;
import java.util.*;

/**
 *  This class is used to view html, txt, and xml returened by the veg plot related 
 *  servlets.  This cuurent version allows a calling class to pass a string containg 
 *  data formated as html and the html will be diplayed in the window.
 *
 * @author  harris
 * @version 
 */
public class HtmlViewer extends javax.swing.JFrame {

    /** Creates new form HtmlViewer */
    public HtmlViewer() {
        initComponents ();
        pack ();
        setSize(650, 720);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the FormEditor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        openMenuItem = new javax.swing.JMenuItem();
        saveMenuItem = new javax.swing.JMenuItem();
        saveAsMenuItem = new javax.swing.JMenuItem();
        exitMenuItem = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        cutMenuItem = new javax.swing.JMenuItem();
        copyMenuItem = new javax.swing.JMenuItem();
        pasteMenuItem = new javax.swing.JMenuItem();
        deleteMenuItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        contentsMenuItem = new javax.swing.JMenuItem();
        aboutMenuItem = new javax.swing.JMenuItem();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jEditorPane1 = new javax.swing.JEditorPane();
        
        fileMenu.setText("File");
          
          openMenuItem.setText("Open");
            fileMenu.add(openMenuItem);
            
          saveMenuItem.setText("Save");
            fileMenu.add(saveMenuItem);
            
          saveAsMenuItem.setText("Save As ...");
            fileMenu.add(saveAsMenuItem);
            
          exitMenuItem.setText("Exit");
            exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    exitMenuItemActionPerformed(evt);
                }
            }
            );
            fileMenu.add(exitMenuItem);
            menuBar.add(fileMenu);
          
        editMenu.setText("Edit");
          
          cutMenuItem.setText("Cut");
            editMenu.add(cutMenuItem);
            
          copyMenuItem.setText("Copy");
            editMenu.add(copyMenuItem);
            
          pasteMenuItem.setText("Paste");
            editMenu.add(pasteMenuItem);
            
          deleteMenuItem.setText("Delete");
            editMenu.add(deleteMenuItem);
            menuBar.add(editMenu);
          
        helpMenu.setText("Help");
          
          contentsMenuItem.setText("Contents");
            helpMenu.add(contentsMenuItem);
            
          aboutMenuItem.setText("About");
            helpMenu.add(aboutMenuItem);
            menuBar.add(helpMenu);
          getContentPane().setLayout(null);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        }
        );
        
        jButton1.setText("view data");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        }
        );
        
        getContentPane().add(jButton1);
        jButton1.setLocation(10, 630);
        jButton1.setSize(jButton1.getPreferredSize());
        
        
        
        jEditorPane1.setText("test");
          jScrollPane1.setViewportView(jEditorPane1);
          
          
        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(20, 30, 600, 590);
        
        setJMenuBar(menuBar);
        
    }//GEN-END:initComponents

    /**
     *  Method to allow the user access to the main veg database site when the 
     *  button is pressed
     *
     */
  private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
// Add your handling code here:
      try {
     
          
      jEditorPane1.setContentType("text/html"); 
      DataRequestClient drc = new DataRequestClient();
      jEditorPane1.setText(drc.requestURL(
      "http://beta.nceas.ucsb.edu:8080/examples/servlet/pageDirector"));
     
     // jEditorPane1.setPage("http://www.cnn.com");
      
      }
      catch (Exception e) {System.err.println(e);}
  }//GEN-LAST:event_jButton1ActionPerformed

    private void exitMenuItemActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        System.exit (0);
    }//GEN-LAST:event_exitMenuItemActionPerformed

    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        System.exit (0);
    }//GEN-LAST:event_exitForm

    /**
    * Method to set the exet in the pane from an input file
    * which can be called by other interfaces
    */
    public void showData () {
        try { 
            jEditorPane1.setText("reading data"); 
            
            //the current location of the veg plot servlet
            jEditorPane1.setPage("http://beta.nceas.ucsb.edu:8080/examples/servlet/pageDirector");
         
            } catch (Exception e) {System.err.println(e);}
    }
    
    /**
    * Method to set the exet in the pane from an input file
    * which can be called by other interfaces
    */
    public void showData (String inString) {
        try { 
            
            jEditorPane1.setContentType("text/html");
            jEditorPane1.setText(inString); 
            //jEditorPane1.setPage("http://www.cnn.com");
         } catch (Exception e) {System.err.println(e);}
    }
    
    /**
    * Method to set the exet in the pane from an input file
    * which can be called by other interfaces
    */
    public void showData (String inXML, String styleSheet, String contentType) {
        try { 
            
            //this is where the transformed xml document is to be stored
            StringBuffer results = new StringBuffer();
            
            jEditorPane1.setContentType(contentType);
            
            //now transform the xml with the style sheet
             transformXML m = new transformXML();
             m.getTransformed(inXML, styleSheet);
    
    
            //the stringwriter containg all the transformed data
            StringWriter transformedData=m.outTransformedData;  

            //pass to the utility class to convert the StringWriter to an array
            utility u =new utility();
            u.convertStringWriter(transformedData);

            String transformedString[]=u.outString; 
            int transformedStringNum=u.outStringNum; 
    
            for (int i=0; i<transformedStringNum; i++) 
	    {
		//System.out.println(transformedString[i]);	
                results.append(transformedString[i]+"\n");
            }
    
            
            
            jEditorPane1.setText(results.toString()); 

         } catch (Exception e) {System.err.println(e);}
    }
    
    
    
    /**
    * @param args the command line arguments
    */
    public static void main (String args[]) {
      //  new HtmlViewer ().show ();
        
       HtmlViewer hv = new  HtmlViewer(); 
       hv.show();
       hv.showData();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenuItem openMenuItem;
    private javax.swing.JMenuItem saveMenuItem;
    private javax.swing.JMenuItem saveAsMenuItem;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenu editMenu;
    private javax.swing.JMenuItem cutMenuItem;
    private javax.swing.JMenuItem copyMenuItem;
    private javax.swing.JMenuItem pasteMenuItem;
    private javax.swing.JMenuItem deleteMenuItem;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JMenuItem contentsMenuItem;
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JButton jButton1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JEditorPane jEditorPane1;
    // End of variables declaration//GEN-END:variables

}
