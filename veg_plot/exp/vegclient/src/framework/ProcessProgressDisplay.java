/**
 *
 *  Authors: @author@
 *  Release: @release@
 *	
 *  '$Author: harris $'
 *  '$Date: 2001-10-10 18:12:42 $'
 * 	'$Revision: 1.1 $'
 */

package vegclient.framework;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import java.io.*;

import vegclient.framework.*;


public class ProcessProgressDisplay extends JFrame 
{
	public  JButton button = new JButton();
	public static JPanel mainPane = new JPanel();
	public static JFrame frame = new JFrame();
	
	//the 4 main categories for the manager
	public static JPanel projectDescriptionPanel = new JPanel();
	public static JPanel transformDataPanel = new JPanel();
	public static JPanel loadDatabasePanel = new JPanel();
	public static JPanel exportDataPanel = new JPanel();
	//panel for the main categories labels
	public static JPanel labelPanel = new JPanel();
	
	//the sub-category panels correcponding to the buttons
	public static JPanel projectInitializePanel = new JPanel();
	public static JPanel readLegacyDataPanel = new JPanel(); 
	public static JPanel processLegacyDataPanel = new JPanel();
	public static JPanel loadLocalDatabaseSelectorPanel = new JPanel();
	public static JPanel loadRemoteDatabaseSelectorPanel = new JPanel();
	public static JPanel exportDataSelectorPanel = new JPanel();
	public static JPanel continueSelectorPanel = new JPanel();
	
	
	//the labels for the main catagory panels
	private JLabel projectDescriptionLabel = new JLabel("Project Description");
	private JLabel transformDataLabel = new JLabel("Data Transformation");
	private JLabel loadDatabaseLabel = new JLabel("Load Database");
	private JLabel exportDataLabel = new JLabel("Export Data");
	
	
	//selectors
	public static JButton projectInitializeSelector = new JButton();
	public static JButton readLegacyDataSelector = new JButton();
	public static JButton processLegacyDataSelector = new JButton();
	public static JButton loadLocalDatabaseSelector = new JButton();
	public static JButton loadRemoteDatabaseSelector = new JButton();
	
	public static JButton exportDataSelector = new JButton();
	public static JButton continueSelector = new JButton();
	
	//STATUS ICONS
	public static ImageIcon red_icon = new ImageIcon("images/red_box.jpg");
	public static ImageIcon yellow_icon = new ImageIcon("images/yellow_box.jpg");
	public static ImageIcon green_icon = new ImageIcon("images/green_box.jpg");
	//the 1st 3 icons are associated with the project initialization
	public static JLabel red_label = new JLabel("",red_icon,JLabel.CENTER);
	public static JLabel yellow_label = new JLabel("",yellow_icon,JLabel.CENTER);
	public static JLabel green_label = new JLabel("",green_icon,JLabel.CENTER);
	//the next 6 icons are associated with the data transform status icons
	public static JLabel readRedLabel = new JLabel("",red_icon,JLabel.CENTER);
	public static JLabel readYellowLabel = new JLabel("",yellow_icon,JLabel.CENTER);
	public static JLabel readGreenLabel = new JLabel("",green_icon,JLabel.CENTER);
	public static JLabel processRedLabel = new JLabel("",red_icon,JLabel.CENTER);
	public static JLabel processYellowLabel = new JLabel("",yellow_icon,JLabel.CENTER);
	public static JLabel processGreenLabel = new JLabel("",green_icon,JLabel.CENTER);
	//the next 6 icons are for loading the data to the database
	public static JLabel loadLocalRedLabel = new JLabel("",red_icon,JLabel.CENTER);
	public static JLabel loadLocalYellowLabel = new JLabel("",yellow_icon,JLabel.CENTER);
	public static JLabel loadLocalGreenLabel = new JLabel("",green_icon,JLabel.CENTER);
	public static JLabel loadRemoteRedLabel = new JLabel("",red_icon,JLabel.CENTER);
	public static JLabel loadRemoteYellowLabel = new JLabel("",yellow_icon,JLabel.CENTER);
	public static JLabel loadRemoteGreenLabel = new JLabel("",green_icon,JLabel.CENTER);
	// the next 3 icons are for the export mechanism associated with data export
	public static JLabel exportDataRedLabel = new JLabel("",red_icon,JLabel.CENTER);
	public static JLabel exportDataYellowLabel = new JLabel("",yellow_icon,JLabel.CENTER);
	public static JLabel exportDataGreenLabel = new JLabel("",green_icon,JLabel.CENTER);
	
	
	//test status stuff
	public static JTextField projectDescriptionStatus = new JTextField();
	public static JTextField readLegacyDataStatus = new JTextField();
	public static JTextField processLegacyDataStatus = new JTextField();
	public static JTextField projectLoadLocalStatus = new JTextField();
	public static JTextField projectLoadRemoteStatus = new JTextField();
	public static JTextField projectExportStatus = new JTextField();
	
	
//	private JLabel projectDescriptionStatus = new JLabel("pending");
		//other classes needed	
		XMLparse xp = new XMLparse();
		ProjectManager pm = new ProjectManager();
//		Project proj = new Project();
	

	 /**
    * @param args the command line arguments
    */
    public static void main (String args[]) 
		{
			System.out.println("Starting");
        new ProcessProgressDisplay ().show ();
    }
		
	 /** Creates new form ProjectInitialize */
    public ProcessProgressDisplay() 
		{
				//do nothing on close so that other interfaces are not stopped
				setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
				setTitle("Progress");
        initComponents(1, Color.pink);
        pack();
				//for linux increase size by 50
        //setSize(320, 400);
				setSize(370, 450);
    }
		
		
		/**
		 * method to set up the components on the frame
		 */
		 public void initComponents(int buttonNumber, Color color) 
		{
      frame.getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), 1));
      addWindowListener(new java.awt.event.WindowAdapter() 
			{
      	public void windowClosing(java.awt.event.WindowEvent evt) 
				{
        	 exitForm(evt);
        }
			});
			
			//this is the main panel for the interface
			mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.Y_AXIS));
			mainPane.add(Box.createRigidArea(new Dimension(2,30)));
			mainPane.add( projectDescriptionStatus(projectDescriptionPanel) );
			if (processConfig("dataTransformSelector") == true)
			{
				mainPane.add( transformDataStatus(transformDataPanel) );
			}
			//use the workflow manager config file
			if (processConfig("dataLoadRemoteSelector") == true)
			{
				mainPane.add( loadDatabaseStatus(loadDatabasePanel) );
  	  }
			if (processConfig("dataExportSelector") == true)
			{
				mainPane.add( exportDataStatus( exportDataPanel ) );
			}
			mainPane.add(Box.createRigidArea(new Dimension(2,30)));
		
			//add the continue button
			continueSelector.setText("Continue");
			continueSelector.setActionCommand("continueProcess");
			continueSelectorPanel.setLayout(new FlowLayout(0,5,1));
			continueSelectorPanel.add(continueSelector);
			mainPane.add(continueSelectorPanel);

			//show the panes
			setContentPane(mainPane);
      //frame.getContentPane().add(mainPane);
		
			//set up the activators
			continueSelector.addActionListener(new java.awt.event.ActionListener() 
			{
    	  public void actionPerformed(ActionEvent evt) 
				{
    	    handleContinueAcivator(evt);
    	   }
    	});
		
	}
	
		/** 
		 * method to exit the Application 
		 */
    private void exitForm(java.awt.event.WindowEvent evt) 
		{
			this.dispose();
      //System.exit (0);
			
			//let the project manager know that the progress monitor is no longer
			// running
			pm.statusManagerRunning = false;
    }
	
	/**
	 * method to denote an initialized  proccess by coloring the corresponding 
	 * panel green yellow.
	 * 
	 * @param processName -- the name of the process that is to be marled as
	 * finished.  Includes: projectInitialization, dataTransformation
	 */
	 public static void initializeProcess(String processName)
	 {
		 if ( processName.equals("projectInitialization") )
		 {
		 	projectInitializePanel.remove( red_label );
			projectInitializePanel.add( yellow_label );
			projectInitializePanel.validate();
		 }
		 else if ( processName.equals("dataTransformation") )
		 {
			readLegacyDataPanel.remove( readRedLabel );
			readLegacyDataPanel.add( readYellowLabel );
			//the process icon
			processLegacyDataPanel.remove( processRedLabel );
			processLegacyDataPanel.add( processYellowLabel );
			readLegacyDataPanel.validate();
			processLegacyDataPanel.validate();
		 }
	 }
	
	/**
	 * method to denote a finished  proccess by coloring the  panel green
	 * this method id to be called from the classes that are spawned from this
	 * method -- ie thats why it is static
	 * 
	 * @param processName -- the name of the process that is to be marled as
	 * finished.  Includes: projectInitialization, dataTransformation
	 */
	 public static void finishProcess(String processName)
	 {
		 if ( processName.equals("projectInitialization") )
		 {
		 	projectInitializePanel.remove( yellow_label );
			projectInitializePanel.add( green_label );
			projectInitializePanel.validate();
		 }
		 else if ( processName.equals("dataTransformation") )
		 {
			readLegacyDataPanel.remove( readYellowLabel );
			readLegacyDataPanel.add( readGreenLabel );
			//the process status label
			processLegacyDataPanel.remove( processYellowLabel );
			processLegacyDataPanel.add( processGreenLabel );
			
			readLegacyDataPanel.validate();
			processLegacyDataPanel.validate();
		 }
	 }
	
	/**
	 * method to handle the continuation thru the work flow process
	 * as the 'continue' button is pushed -- this method should probably
	 * be put into the 'Manager' class
	 */
	 	private String handleContinueAcivator(ActionEvent evt)
		{
			String returnString = "ok";
			try
			{
				//print out the command that activated this method
				System.out.println( "continuing: "+evt.getActionCommand() );
				//determine the action to be carried out depending on the activation
				// command -- the first if is for the continue button
				if (evt.getActionCommand().trim().equals("continueProcess") )
				{
					//for now just start the project Description class
					new Project().show();
				}
				//initilizeProject
				else if ( evt.getActionCommand().trim().equals("initializeProject") )
				{
					//start the project Description class
					new Project().show();
					//change the indication box from red to yellow
					initializeProcess("projectInitialization");
				}
				//read or process legacy data
				else if( evt.getActionCommand().trim().equals("readLegacyDataFiles")
					|| evt.getActionCommand().trim().equals("processLegacyDataFiles"))
				{
					new FileTransformer("chooseFileType").show();
					//change the indication box from red to yellow
					initializeProcess("dataTransformation");
				}
				//else print an error to the screen
				else
				{
					System.out.println( "unknown action event" );
					pm.debug(0, "action not recognized: "+evt.getActionCommand());
				}
			} 
			catch (Exception e) 
			{
				System.err.println(e);
			}
			return(returnString);
		}
		
		//method that returns true or false based on the 
		//actions provided in the xml config file
		public boolean processConfig (String elementName)
		{
			String nodeValue = xp.getNodeValue("process.xml", elementName);
			System.out.println(" returned node for : "+elementName+" "+nodeValue);
			if (nodeValue != null)
			{
				if ( nodeValue.trim().equals("true") )
				{
					return(true);
				}
				else
				{
					return(false);
				}
			}
			else 
			{
				return(false);
			}
			
		}
		
		/** 
		 * method that returns a panel with component for the user to 
		 * select data to export from the local or remote database
		 */
		
		public JPanel exportDataStatus(JPanel panel)
		{
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			labelPanel = new JPanel();
			labelPanel.setLayout(new FlowLayout(0,5,1));
			labelPanel.add(exportDataLabel);
			exportDataSelector.setText("Export Data");
			exportDataSelector.setPreferredSize(new Dimension(220, 20));
			projectExportStatus.setColumns(7);
			projectExportStatus.setBackground(java.awt.Color.pink);
			projectExportStatus.setText("pending");
			
			exportDataSelectorPanel.setLayout(new FlowLayout(0,5,1));
			exportDataSelectorPanel.add(exportDataSelector);
			exportDataSelectorPanel.add(projectExportStatus);
			//add the red label
			exportDataSelectorPanel.add(exportDataRedLabel);
			panel.add(labelPanel);
			panel.add(exportDataSelectorPanel);
				//set up the activators
			exportDataSelector.addActionListener(new java.awt.event.ActionListener() 
			{
    	  public void actionPerformed(ActionEvent evt) 
				{
    	    handleContinueAcivator(evt);
    	   }
    	});
			return(panel);
		}
		
		
		/**
		 * method to provide a panel with the components required to load the 
		 * legacy data into the databases - both the local and remote
		 */
		public JPanel loadDatabaseStatus(JPanel panel)
		{
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
//			labelPanel = new JPanel();
//			labelPanel.setLayout(new FlowLayout(0,5,1));
//			labelPanel.add(loadDatabaseLabel);
			loadRemoteDatabaseSelectorPanel.setLayout(new FlowLayout(0,5,1));
			loadLocalDatabaseSelectorPanel.setLayout(new FlowLayout(0,5,1));
			
			loadLocalDatabaseSelector.setText("Load Local Database");
			loadRemoteDatabaseSelector.setText("Load Remote Database");
			loadLocalDatabaseSelectorPanel.add(loadLocalDatabaseSelector);
			projectLoadLocalStatus.setColumns(7);
			projectLoadLocalStatus.setBackground(java.awt.Color.pink);
			projectLoadLocalStatus.setText("pending");
			loadLocalDatabaseSelectorPanel.add(projectLoadLocalStatus);
			projectLoadRemoteStatus.setColumns(7);
			projectLoadRemoteStatus.setBackground(java.awt.Color.pink);
			projectLoadRemoteStatus.setText("pending");
			//load the red icon to the load local
			projectLoadRemoteStatus.add(loadLocalRedLabel);
			
			loadRemoteDatabaseSelector.setPreferredSize(new Dimension(220, 20));
			loadLocalDatabaseSelector.setPreferredSize(new Dimension(220, 20));
			
			loadRemoteDatabaseSelectorPanel.add(loadRemoteDatabaseSelector);
			loadRemoteDatabaseSelectorPanel.add(projectLoadRemoteStatus);
			//load the red icon to the load local
			loadLocalDatabaseSelectorPanel.add(loadLocalRedLabel);
			//add the red icon to the load remote panel
			loadRemoteDatabaseSelectorPanel.add(loadRemoteRedLabel);
			labelPanel = new JPanel();
			labelPanel.setLayout(new FlowLayout(0,5,1));
			labelPanel.add(loadDatabaseLabel);
			panel.add(labelPanel);
			panel.add(loadLocalDatabaseSelectorPanel);
			panel.add(loadRemoteDatabaseSelectorPanel);
				//set up the activators
			//load the local
			loadLocalDatabaseSelector.addActionListener(new java.awt.event.ActionListener() 
			{
    	  public void actionPerformed(ActionEvent evt) 
				{
    	    handleContinueAcivator(evt);
    	   }
    	});
			//load the remote
			loadRemoteDatabaseSelector.addActionListener(new java.awt.event.ActionListener() 
			{
    	  public void actionPerformed(ActionEvent evt) 
				{
    	    handleContinueAcivator(evt);
    	   }
    	});
			
			return(panel);
		}
		
		
		/**
		 * method that allows the user to choose an option that can transform their
		 * data into a cormat consistent with the veg plots xml schema
		 */
		public JPanel transformDataStatus(JPanel panel)
		{
		//	panel.setPreferredSize(new Dimension(220, 23));
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			readLegacyDataPanel.setLayout(new FlowLayout(0,5,1));
			processLegacyDataPanel.setLayout(new FlowLayout(0,5,1));
			
			readLegacyDataSelector.setText("Read Legacy Data Files");
			//set the action command for the activator
			readLegacyDataSelector.setActionCommand("readLegacyDataFiles");
			processLegacyDataSelector.setText("Process Legacy Data Files");
			//set the action command for the activatior
			processLegacyDataSelector.setActionCommand("processLegacyDataFiles");
			
			readLegacyDataSelector.setPreferredSize(new Dimension(220, 20));
			processLegacyDataSelector.setPreferredSize(new Dimension(220, 20));
			
			readLegacyDataPanel.add(readLegacyDataSelector);
			processLegacyDataPanel.add(processLegacyDataStatus);
			
			readLegacyDataStatus.setColumns(7);
			readLegacyDataStatus.setBackground(java.awt.Color.pink);
			readLegacyDataStatus.setText("pending");
			
			processLegacyDataStatus.setColumns(7);
			processLegacyDataStatus.setBackground(java.awt.Color.pink);
			processLegacyDataStatus.setText("pending");
			
			
			readLegacyDataPanel.add( readLegacyDataStatus );
			//add the red icon
			readLegacyDataPanel.add(readRedLabel);
			//red_label = new JLabel( "",red_icon,JLabel.CENTER );
			processLegacyDataPanel.add( processLegacyDataSelector );
			processLegacyDataPanel.add( processLegacyDataStatus );
			//add the red icon
			processLegacyDataPanel.add( processRedLabel );
			
			//make a new panel for the label
			labelPanel = new JPanel();
			labelPanel.setLayout(new FlowLayout(0,5,1));
			labelPanel.add(transformDataLabel);
			panel.add(labelPanel);
			panel.add(readLegacyDataPanel);
			panel.add(processLegacyDataPanel);
			
			//set up the activators
			readLegacyDataSelector.addActionListener(new java.awt.event.ActionListener() 
			{
    	  public void actionPerformed(ActionEvent evt) 
				{
    	    handleContinueAcivator(evt);
    	   }
    	});
			processLegacyDataSelector.addActionListener(new java.awt.event.ActionListener() 
			{
    	  public void actionPerformed(ActionEvent evt) 
				{
    	    handleContinueAcivator(evt);
    	   }
    	});
			
			return(panel);
		}
		
		/**
		 * method that returns a panel which has components allowing the user to
		 * describe a project
		 *
		 */
		public JPanel projectDescriptionStatus(JPanel panel)
		{
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			projectInitializePanel.setLayout(new FlowLayout(0,5,1));
			projectInitializeSelector.setText("Describe Project");
			//set the action command so that the activator knows what to do
			projectInitializeSelector.setActionCommand("initializeProject");
			projectInitializeSelector.setPreferredSize(new Dimension(220, 20));
			projectInitializePanel.add(projectInitializeSelector);
	
			projectDescriptionStatus.setColumns(7);
			projectDescriptionStatus.setBackground(java.awt.Color.pink);
			projectDescriptionStatus.setText("pending");
			projectInitializePanel.add( projectDescriptionStatus );
			//add the color boxes to the far right in the panel
			projectInitializePanel.add( red_label );
			labelPanel.setLayout(new FlowLayout(0,5,1));
			labelPanel.add(projectDescriptionLabel);
			panel.add(labelPanel);
			panel.add(projectInitializePanel);
			//set up the activators
			projectInitializeSelector.addActionListener(new java.awt.event.ActionListener() 
			{
    	  public void actionPerformed(ActionEvent evt) 
				{
    	    handleContinueAcivator(evt);
    	   }
    	});
			return(panel);
		}
		

}
