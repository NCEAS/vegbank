import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import java.io.*;

import org.w3c.dom.Document;

/**
 * This class represents the project attributes of a vegetation
 * collection / anaysis project
 *
 *  Authors: @authors@
 *  Release: @release@
 *	
 *  '$Author: harris $'
 *  '$Date: 2001-08-21 07:12:14 $'
 * 	'$Revision: 1.1 $'
 */
 
public class Project extends JFrame 
{
	
	public JFrame frame = new JFrame();
	
	
	//the main frame panel
	public  JPanel mainPane = new JPanel();
	
	//the main categories for the manager
	public  JPanel projectSummaryPanel = new JPanel();
	public  JPanel projectContributorPanel = new JPanel();
	public  JPanel projectDatePanel = new JPanel();
	public  JPanel projectPlacePanel = new JPanel();
	
	
	//the labels for the main catagory panels
	private JLabel projectSummaryLabel = new JLabel("Description");
	private JLabel projectContributorsLabel = new JLabel("Contributors");
	private JLabel projectDateLabel = new JLabel("Date");
	private JLabel projectPlaceLabel = new JLabel("Location");
	
	
	
	//test status stuff
	public static JTextField projectNameText = new JTextField();
	public static JTextField projectDescriptionText = new JTextField();
	public static JTextField contributorSurNameText = new JTextField();
	public static JTextField contributorGivenNameText  = new JTextField();
	public static JTextField contributorInstitutionText = new JTextField();
	
	//selectors
	public static JButton continueSelector = new JButton();
	public static JButton addContributorSelector = new JButton();
	
	
	XMLparse xp = new XMLparse();
	ProjectManager manager = new ProjectManager();
 
  /** Creates new form ProjectInitialize */
	public Project(Hashtable componentHash)
	{
				//do nothing on close so that other interfaces are not stopped
//			setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
				setTitle(" Project Identification");
			 initComponents(componentHash);
       pack ();
				//for linux increase size by 50
        //setSize(375, 370);
			setSize(425, 420);
	}
	
	
	/** 
	 * method to initaiate the components into the frame
	 *
	 * @param componentHash -- the components which should be added to the
	 * interface; like contributor=key, true=value
	 *
	 */
	 public void initComponents(Hashtable componentHash ) 
		{
      frame.getContentPane().setLayout(new BoxLayout(getContentPane(), 1));
      addWindowListener(new java.awt.event.WindowAdapter() 
			{
      	public void windowClosing(java.awt.event.WindowEvent evt) 
				{
        	// exitForm(evt);
        }
			});
			
			if (componentHash.containsKey("projectSummaryPanel") == true )
			{
				if (componentHash.get("projectSummaryPanel").toString().equals("true") )
				{
						projectSummaryPanel = projectSummary();
				}
			}
			if (componentHash.containsKey("projectContributorPanel") == true )
			{
				if (componentHash.get("projectContributorPanel").toString().equals("true") )
				{
						projectContributorPanel = projectContributor();
				}
			}
		
			
			//this is the main panel for the interface
			mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.Y_AXIS));
			mainPane.add(Box.createRigidArea(new Dimension(2,15)));
			mainPane.add( projectSummaryPanel );
			mainPane.add(Box.createRigidArea(new Dimension(2,15)));		
			mainPane.add( projectContributorPanel );
			mainPane.add(Box.createRigidArea(new Dimension(2,30)));
			mainPane.add( updateProject() );
			mainPane.add(Box.createRigidArea(new Dimension(2,100)));
		
			//show the panes
		  setContentPane(mainPane);
      //frame.getContentPane().add(mainPane);
		
			//set up the activators
			continueSelector.addActionListener(new java.awt.event.ActionListener() 
		{
      public void actionPerformed(ActionEvent evt) 
			{
				handleAddContributorActivator(evt);
        handleUpdateAcivator(evt);
       }
    });
		
		}
		
		
		
		/**
		 * method that provides a JPanel containing components that 
		 * may be updated and that define project description 
		 *
		 * @return panel -- a JPanel with text areas for projectName
		 * & projDescription
		 */
		public JPanel projectSummary()
		{
			
		 	JPanel panel = new JPanel(); //nain panel
			JPanel labelPanel = new JPanel();
			JPanel namePanel = new JPanel(); //sub panel for name
			JPanel descriptionPanel = new JPanel();
			
			
			//setup the label panel
			labelPanel.setLayout(new FlowLayout(0,5,1));
			labelPanel.add( new JLabel("Project Summary") );
			
			//setup the name panel
			namePanel.setLayout(new FlowLayout(0,5,1));
			namePanel.add(new JLabel("Project Name") );
			projectNameText.setColumns(15);
			projectNameText.setBackground(Color.pink);
			projectNameText.setText( xp.getNodeValue("process.xml", "projectName"));
			namePanel.add(projectNameText);
			
			//setup the description panel
			descriptionPanel.setLayout(new FlowLayout(0,5,1));
			descriptionPanel.add(new JLabel("Project Description") );
			projectDescriptionText.setColumns(15);
			projectDescriptionText.setBackground(Color.pink);
			projectDescriptionText.setText( xp.getNodeValue("process.xml", "projectDescription"));
			descriptionPanel.add(projectDescriptionText);
			
			//add the components to the main panel
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			panel.setPreferredSize(new Dimension(220, 23));
			panel.add( labelPanel );
			panel.add( namePanel );
			panel.add( descriptionPanel);
			return(panel);
		}
		
		public JPanel  projectContributor()
		{
			JPanel panel = new JPanel(); //nain panel
			JPanel labelPanel = new JPanel( );
			JPanel givenNamePanel = new JPanel(); //sub panel for name
			JPanel surNamePanel = new JPanel();
			JPanel institutionPanel = new JPanel();
			
			//setup the label panel
			labelPanel.setLayout(new FlowLayout(0,5,1));
			labelPanel.add( new JLabel("Project Contributors") );
			
			
			//setup the name panel
			givenNamePanel.setLayout(new FlowLayout(0,5,1));
			givenNamePanel.add(new JLabel("First Name: ") );
			contributorGivenNameText.setColumns(15);
			contributorGivenNameText.setBackground(Color.pink);
			contributorGivenNameText.setText("");
			givenNamePanel.add( contributorGivenNameText);
			
			//setup the description panel
			surNamePanel.setLayout(new FlowLayout(0,5,1));
			surNamePanel.add(new JLabel("Last Name") );
			contributorSurNameText.setColumns(15);
			contributorSurNameText.setBackground(Color.pink);
			contributorSurNameText.setText("");
			surNamePanel.add( contributorSurNameText );
			
			//setup the institution panel
			institutionPanel.setLayout(new FlowLayout(0,5,1));
			institutionPanel.add(new JLabel("Institution") );
			contributorInstitutionText.setColumns(15);
			contributorInstitutionText.setBackground(Color.pink);
			contributorInstitutionText.setText("");
			institutionPanel.add( contributorInstitutionText );
			 addContributorSelector.setText("Add Contributor");
			institutionPanel.add( addContributorSelector );
			
			//add the components to the main panel
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			panel.setPreferredSize(new Dimension(220, 23));
			
			panel.add( labelPanel);
			panel.add( givenNamePanel );
			panel.add( surNamePanel );
			panel.add( institutionPanel );
			return(panel);
		}
	
		/**
		 * method that activates the project updating process
		 * 
		 * @return panel -- a JPanel that contains the a button that
		 * 	allows the user to save the modifications
		 *
		 */
		private JPanel updateProject()
		{
			JPanel panel = new JPanel(); //nain panel
			JPanel labelPanel = new JPanel( );
			JPanel buttonPanel = new JPanel();
			
			//setup the label panel
			labelPanel.setLayout(new FlowLayout(0,5,1));
			labelPanel.add( new JLabel("Update Project") );
			
			//setup the name panel
			buttonPanel.setLayout(new FlowLayout(0,5,1));
			continueSelector.setText("commit");
			buttonPanel.add(continueSelector);
			
			//add the components to the main panel
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			panel.setPreferredSize(new Dimension(220, 23));
			panel.add( labelPanel);
			panel.add( buttonPanel );
			return(panel);
			
		}
		
			/**
		 * this method gets all the parameters chosen on the interface
		 * and returns their status in a hashtable
		 */
		private Hashtable getInterfaceParameters()
		{
			Hashtable chosenParameters = new Hashtable();
			//put the elements into the hashtable
			chosenParameters.put("projectName", projectNameText.getText().toString()  );
			chosenParameters.put("projectDescription", projectDescriptionText.getText().toString().trim());
			return(chosenParameters); 
		}
		
		/**
		 * method to handle the request for an additional form
		 * for a contributor
		 *
		 */
		private void handleAddContributorActivator( ActionEvent evt)
		{
			Hashtable componentHash = new Hashtable();
			componentHash.put("projectContributorPanel", "true");
	//		setTitle(" Project Identification");
	//		 initComponents(componentHash);
  //     pack ();
				//for linux increase size by 50
        //setSize(375, 370);
	//		setSize(425, 820);
	
		setTitle("Progress ...");
//			Container contentPane = getContentPane();
//			contentPane.remove(button);
		JPanel newElement = new JPanel();
			newElement.setBackground(Color.white);
			JButton button = new JButton();
			newElement.add(button);
//		//	Container contentPane =	getContentPane();
//	//		frame.getContentPane().add(newElement);
			 getContentPane().add(newElement);
//			 //  pack();
//      //  setSize(375, 370);
//		contentPane.validate();
//			repaint();

//		getContentPane().add(  );
	//	setSize(425, 820);
		repaint();
		
			new Project(componentHash).show();
		}
		
		
		/** 
		 * method to handle the updating of a project
		 */
		private String handleUpdateAcivator(ActionEvent evt)
		{
			String returnString = "ok";
			try
			{
				
				//System.out.println("Button pressed");
			
			//add the project node to the process xml file
			//Document doc = xp.getDocument("process.xml");
			
			Hashtable params =  getInterfaceParameters();
			if (params.toString() != null)
			{
				manager.initiateProjectXML(params);
			}
			else
			{
				System.out.println( "no project criteria selected" );
			}
//			xp.addNewElement( "process.xml", "project", 1, "surName", "john");
			
			
			//ppd.mainPane.setBackground(Color.black);
			//		Thread.sleep(500);
			//		System.out.println("finished sleeping");
				//add a new element to the progress Display
			//	ppd.mainPane.add( projectFileSelector );
			// this works!				 ppd.addPanelElement("test");
			//	ppd.ProcessProgressDisplay();
			//		ppd.appendYellow(ppd.projectInitializePanel);
			//		Thread.sleep(2500);
			//			ppd.appendGreen(ppd.projectInitializePanel);
			//		System.out.println("selected Parameters: " + getInterfaceParameters().toString() );
					//send these paremeters to a qa/qc modual and let the user know of any
					//problems
			//		if (pm.initQAQC( getInterfaceParameters() ) == false)
			//		{
			//			System.out.println( "error Message:" + pm.errorMessage.toString() );
			//		}
			//		else
			//		{
			//			//print the xml file to initiate the workflow process
			//			pm.intitateProcessXML( getInterfaceParameters() );
			//			pm.initiateProgressManager();
			//			//new ProcessProgressDisplay().show();
			//		}
					
					
					
			} 
			catch (Exception e) 
			{
				System.err.println(e);
			}
			return(returnString);
		}
		
	
	 public static void main (String args[]) 
	{
		Hashtable componentHash = new Hashtable();
		 componentHash.put("projectSummaryPanel", "true");
		 componentHash.put("projectContributorPanel", "true");
		 componentHash.put("projectDatePanel", "true");
		 componentHash.put("projectPlacePanel", "true");
		System.out.println("Starting Project");
		new Project(componentHash).show();
   }
	
}
