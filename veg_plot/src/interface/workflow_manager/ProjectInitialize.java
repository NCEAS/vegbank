import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import java.io.*;

/**
 * this class provides the interface for the 
 * vegetation nested query functionality.
 *
 *  Authors: @authors@
 *  Release: @release@
 *	
 *  '$Author: harris $'
 *  '$Date: 2001-08-21 07:09:04 $'
 * 	'$Revision: 1.2 $'
 */


public class ProjectInitialize extends javax.swing.JFrame 
{
	
		ResourceBundle rb = ResourceBundle.getBundle("interface");
		ProjectManager pm = new ProjectManager();
	//	static ProcessProgressDisplay ppd = new ProcessProgressDisplay();

		//the components for the first box in the vertical layout
		//dedicated to the selection of a project file and description
		private JPanel projectNamePanel = new JPanel();
	 	private JTextField projectNameValue = new JTextField();
		private JLabel projectNameValueLabel = new JLabel("Project Name");
		private JPanel projectFilePanel = new JPanel();
		private JButton projectFileSelector = new JButton();
		private JTextField projectFileValue = new JTextField();
		private JLabel projectDescriptionLabel = new JLabel("Description");
	 	private JTextField projectDescriptionValue = new JTextField();
		private JPanel projectSelectionPanel = new JPanel();
		private JPanel projectDescriptionPanel = new JPanel();
	
		
		//the lower panel stuff
		private JPanel projectDetailsPanel = new JPanel();
		private JPanel dataAttributeTypePanel =  new JPanel();
		private JPanel dataTypeLabelPanel = new JPanel();
		private JLabel dataTypeLabel = new JLabel("Input Data Type");
		private JPanel siteDataPanel =  new JPanel();
		private JLabel siteDataLabel = new JLabel("site data");
		private JRadioButton siteDataSelector = new JRadioButton();
		private JPanel speciesDataPanel =  new JPanel();
		private JLabel speciesDataLabel = new JLabel("species data");
		private JRadioButton speciesDataSelector = new JRadioButton();
		private JPanel entireDataPanel =  new JPanel();
		private JLabel entireDataLabel = new JLabel("site & species data");
		private JRadioButton entireDataSelector = new JRadioButton();
		//the right-lower panel objects
		private JPanel dataTasksLabelPanel = new JPanel();
		private JLabel dataTasksLabel = new JLabel("Data Tasks");
		private JPanel dataTasksPanel =  new JPanel();
		private JPanel dataTransformPanel =  new JPanel();
		private JPanel dataLoadLocalPanel =  new JPanel();
		private JPanel dataLoadRemotePanel=  new JPanel();
		private JPanel dataExportPanel =  new JPanel();
		private JLabel dataTransformLabel = new JLabel("transform data");
		private JLabel dataLoadLocalLabel = new JLabel("load local database");
		private JLabel dataLoadRemoteLabel = new JLabel("load central server");
		private JLabel dataExportLabel = new JLabel("export data");
		private JRadioButton dataTransformSelector = new JRadioButton();
		private JRadioButton dataLoadLocalSelector = new JRadioButton();
		private JRadioButton dataLoadRemoteSelector = new JRadioButton();
		private JRadioButton dataExportSelector = new JRadioButton();
		
		//the lowest panel
		private JPanel managerActivationPanel = new JPanel();
		private JButton managerActivator = new JButton();
		private JScrollPane managerCommentsScroller = new JScrollPane();
		private JTextField managerComments = new JTextField();
		
		
		
		//the main panel
		private JPanel mainPane = new JPanel();
		

    /** Creates new form ProjectInitialize */
    public ProjectInitialize() 
		{
				//do nothing on close so that other interfaces are not stopped
				setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
				setTitle(" Data-Loading Workflow Manager");
        initComponents ();
        pack ();
				//for linux increase size by 50
        //setSize(375, 370);
				setSize(425, 420);
    }


    private void initComponents() 
		{
      getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), 1));
      addWindowListener(new java.awt.event.WindowAdapter() 
			{
      	public void windowClosing(java.awt.event.WindowEvent evt) 
				{
        	exitForm(evt);
        }
			});
			
			
			//the top panel -- for file selection
//			projectNamePanel.setPreferredSize(new Dimension(250, 80));
			projectNamePanel.setLayout(new FlowLayout(0, 5, 1));
			projectNameValueLabel.setForeground(java.awt.Color.black);
			projectNamePanel.add(projectNameValueLabel);
			projectNameValue.setColumns(20);
			projectNameValue.setBackground(java.awt.Color.pink);
			projectNamePanel.add(Box.createRigidArea(new Dimension(20,0)));
			projectNamePanel.add(projectNameValue);
			
			projectFilePanel.setLayout(new FlowLayout(0, 5, 1));
			projectFileValue.setColumns(15);
			projectFileSelector.setText("Project File");
			projectFileValue.setBackground(java.awt.Color.pink);
			projectFilePanel.add(projectFileSelector);
			projectFilePanel.add(Box.createRigidArea(new Dimension(2,0)));
			projectFilePanel.add(projectFileValue);
			
			projectDescriptionPanel.setLayout(new FlowLayout(0, 5, 1));
			projectDescriptionValue.setColumns(20);
			projectDescriptionValue.setBackground(java.awt.Color.pink);
			projectDescriptionLabel.setForeground(java.awt.Color.black);
			projectDescriptionPanel.add(projectDescriptionLabel);
			projectDescriptionPanel.add(Box.createRigidArea(new Dimension(30,0)));
			projectDescriptionPanel.add(projectDescriptionValue);
			
			projectSelectionPanel.setLayout(new BoxLayout(projectSelectionPanel, BoxLayout.Y_AXIS));
			projectSelectionPanel.add(projectNamePanel);
			projectSelectionPanel.add(Box.createRigidArea(new Dimension(2,8)));
			projectSelectionPanel.add(projectFilePanel);
			projectSelectionPanel.add(Box.createRigidArea(new Dimension(2,8)));
			projectSelectionPanel.add(projectDescriptionPanel);
			
			
			//the lower panel -- for the details
			projectDetailsPanel.setLayout(new BoxLayout(projectDetailsPanel, BoxLayout.X_AXIS));
			projectDetailsPanel.setBackground(java.awt.Color.white);
			//the lower-left panel
			dataAttributeTypePanel.setLayout(new BoxLayout(dataAttributeTypePanel, BoxLayout.Y_AXIS));
			siteDataPanel.setLayout(new FlowLayout(0,5,1));
			siteDataPanel.add(siteDataSelector);
			siteDataPanel.add(siteDataLabel);
			speciesDataPanel.setLayout(new FlowLayout(0,5,1) );
			speciesDataPanel.add(speciesDataSelector);
			speciesDataPanel.add(speciesDataLabel);
			entireDataPanel.setLayout(new FlowLayout(0,5,1) );
			entireDataPanel.add(entireDataSelector);
			entireDataPanel.add(entireDataLabel);
			
			// add a box to buffer these radio buttons
			dataAttributeTypePanel.add(Box.createRigidArea(new Dimension(2,20)));
			//then add the remaining checkboxes and text
			dataTypeLabelPanel.setLayout(new FlowLayout(0,5,1));
			dataTypeLabel.setFont(new java.awt.Font ("Dialog", 2, 12));
			dataTypeLabel.setForeground(java.awt.Color.black);
			dataTypeLabelPanel.add(dataTypeLabel);
			dataAttributeTypePanel.add(dataTypeLabelPanel);
			//add some space after the label
			dataTasksPanel.add(Box.createRigidArea(new Dimension(2,8)));
			dataAttributeTypePanel.add(siteDataPanel);
			dataAttributeTypePanel.add(speciesDataPanel);
			dataAttributeTypePanel.add(entireDataPanel);
			//add another rigid box at the bottom
			dataAttributeTypePanel.add(Box.createRigidArea(new Dimension(2,80)));
			dataAttributeTypePanel.setAlignmentX(LEFT_ALIGNMENT);
			//the lower-right panel
			dataTasksPanel.setLayout(new BoxLayout(dataTasksPanel, BoxLayout.Y_AXIS));
			//add a rigidbox at the top of the panel
			dataTasksPanel.add(Box.createRigidArea(new Dimension(2,10)));
			dataTasksLabelPanel.setLayout(new FlowLayout(0,5,1));
			dataTasksLabel.setFont(new java.awt.Font ("Dialog", 2, 12));
			dataTasksLabel.setForeground(java.awt.Color.black);
			dataTasksLabelPanel.add(dataTasksLabel);
			dataTasksPanel.add(dataTasksLabelPanel);
			
			
			dataTransformPanel.setLayout(new FlowLayout(0,5,1));
			dataTransformPanel.add(dataTransformSelector);
			dataTransformPanel.add(dataTransformLabel);
			dataLoadLocalPanel.setLayout(new FlowLayout(0,5,1));
			dataLoadLocalPanel.add(dataLoadLocalSelector);
			dataLoadLocalPanel.add(dataLoadLocalLabel);
			dataLoadRemotePanel.setLayout(new FlowLayout(0,5,1));
			dataLoadRemotePanel.add(dataLoadRemoteSelector);
			dataLoadRemotePanel.add(dataLoadRemoteLabel);
			dataExportPanel.setLayout(new FlowLayout(0,5,1));
			dataExportPanel.add(dataExportSelector);
			dataExportPanel.add(dataExportLabel);
			dataTasksPanel.add(dataTransformPanel);
			dataTasksPanel.add(dataLoadLocalPanel);
			dataTasksPanel.add(dataLoadRemotePanel);
			dataTasksPanel.add(dataExportPanel);
			//add a box at the base until there are more options
			dataTasksPanel.add(Box.createRigidArea(new Dimension(2,80)));
			
				
			//add the lower left and right to the lower panel
			projectDetailsPanel.add(dataAttributeTypePanel);
			projectDetailsPanel.add(Box.createRigidArea(new Dimension(2,80)));
			projectDetailsPanel.add(dataTasksPanel);
		
			
			//the lowest panel
			managerActivationPanel.setLayout(new BoxLayout(managerActivationPanel, BoxLayout.X_AXIS));
			managerActivationPanel.add(managerActivator );
			managerComments.setColumns(20);
			managerActivator.setText("Start Manager");
			managerComments.setBackground(java.awt.Color.pink);
			
		//	JScrollPane  jScrollPane3 = new javax.swing.JScrollPane();
		//	jScrollPane3.setBounds(20, 20, 40, 60);
		//	jScrollPane3.setViewportView(updatedNestedQueryText);
			
			managerCommentsScroller.setViewportView(managerComments);
			managerActivationPanel.add(managerCommentsScroller );
			//add a rigid box to the base
			managerActivationPanel.add(Box.createRigidArea(new Dimension(2,10)));
			
			
		
			//aggregate the panes into the main pain
			mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.Y_AXIS));
			mainPane.add(Box.createRigidArea(new Dimension(2,10)));
			mainPane.add(projectSelectionPanel, BorderLayout.NORTH);
			mainPane.add(projectDetailsPanel);
			mainPane.add(managerActivationPanel);
			mainPane.add(Box.createRigidArea(new Dimension(2,10)));
			//show the panes
      getContentPane().add(mainPane);
			
			//set up the activators
			managerActivator.addActionListener(new java.awt.event.ActionListener() 
			{
        public void actionPerformed(ActionEvent evt) 
				{
        	handleManagerAcivator(evt);
        }
    	});
			
     }
		
		private String handleManagerAcivator(ActionEvent evt)
		{
			String returnString = "ok";
			try
			{
				
				//System.out.println("Button pressed");
			//		ppd.mainPane.setBackground(Color.black);
			//		Thread.sleep(500);
			//		System.out.println("finished sleeping");
				//add a new element to the progress Display
			//	ppd.mainPane.add( projectFileSelector );
			// this works!				 ppd.addPanelElement("test");
			//	ppd.ProcessProgressDisplay();
			//		ppd.appendYellow(ppd.projectInitializePanel);
			//		Thread.sleep(2500);
			//			ppd.appendGreen(ppd.projectInitializePanel);
					System.out.println("selected Parameters: " + getInterfaceParameters().toString() );
					//send these paremeters to a qa/qc modual and let the user know of any
					//problems
					if (pm.initQAQC( getInterfaceParameters() ) == false)
					{
						System.out.println( "error Message:" + pm.errorMessage.toString() );
					}
					else
					{
						//print the xml file to initiate the workflow process
						pm.initiateProcessXML( getInterfaceParameters() );
						pm.initiateProgressManager();
						//new ProcessProgressDisplay().show();
					}
					
					
					
			} 
			catch (Exception e) 
			{
				System.err.println(e);
			}
			return(returnString);
		}

	
		/**
		 * method that may be called by another class to start a 
		 * new instance of this class.  Added an action string so
		 * that the in the future the frame can be customized based
		 * on some input
		 */
		public void launchQueryModule(String action)
		{
			System.out.println("progressing to the next stage: ");
			new ProjectInitialize ().show ();
		}
		
		/**
		 * this method gets all the parameters chosen on the interface
		 * and returns their status in a hashtable
		 */
		private Hashtable getInterfaceParameters()
		{
			Hashtable chosenParameters = new Hashtable();
			//put the elements into the hashtable
			chosenParameters.put("projectName", projectNameValue.getText().toString()  );
			chosenParameters.put("projectDescription", projectDescriptionValue.getText().toString().trim());
			//the data type selectors
			chosenParameters.put("siteDataSelector", getSelectionStringStatus(siteDataSelector) );
			chosenParameters.put("speciesDataSelector", getSelectionStringStatus(speciesDataSelector) );
			chosenParameters.put("entireDataSelector", getSelectionStringStatus(entireDataSelector) );
			//the task selectors
			chosenParameters.put("dataTransformSelector", getSelectionStringStatus(dataTransformSelector) );
			chosenParameters.put("dataLoadLocalSelector", getSelectionStringStatus(dataLoadLocalSelector) );
			chosenParameters.put("dataLoadRemoteSelector", getSelectionStringStatus(dataLoadRemoteSelector) );
			chosenParameters.put("dataExportSelector", getSelectionStringStatus(dataExportSelector) );
			
			
		//	chosenParameters.put("value", queryValue.getText().trim());
			return(chosenParameters); 
		}
		
		/**
		 * method to return a string value tru / false interpreted 
		 * from the selection status of the input JRadioButton
		 */
		public String getSelectionStringStatus( JRadioButton radio)
		{
			String result = "false";
			if (radio.isSelected() == true)
			{
				return("true");
			}
			else
			{
				return("false");
			}
		}
		
		/** 
		 * method to exit the Application 
		 */
    private void exitForm(java.awt.event.WindowEvent evt) 
		{
			this.dispose();
      //System.exit (0);
    }

    /**
    * @param args the command line arguments
    */
    public static void main (String args[]) 
		{
			System.out.println("Starting the workflow manager");
        new ProjectInitialize ().show ();
			//	new ProcessProgressDisplay ().show();
    }
	}
