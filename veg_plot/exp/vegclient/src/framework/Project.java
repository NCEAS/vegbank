/**
 * This class represents the project attributes of a vegetation
 * collection / anaysis project
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
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import java.io.*;

import org.w3c.dom.Document;

import vegclient.framework.*;


 
public class Project extends JFrame 
{
	
	public JFrame frame = new JFrame();
	
	
	//the main frame panel
	public  JPanel mainPane = new JPanel();
	
	public JScrollPane scroller = new JScrollPane();
	
	//the main categories for the manager
	public  JPanel projectSummaryPanel = new JPanel();
	//this is the panel that contains all the project cont panels
	public  JPanel projectContributorPanel = new JPanel();
	public  JPanel projectDatePanel = new JPanel();
	public  JPanel projectLocationPanel = new JPanel();
	
	//this is the current (most recent) projectContributorPanel
	public  JPanel currentProjectContributorPanel = new JPanel();
	
	
	
	//the labels for the main catagory panels
	private JLabel projectSummaryLabel = new JLabel("Description");
	private JLabel projectContributorsLabel = new JLabel("Contributors");
	private JLabel projectDateLabel = new JLabel("Date");
	private JLabel projectPlaceLabel = new JLabel("Location");
	
	
	
	//test status stuff
	public JTextField projectNameText = new JTextField();
	public JTextField projectDescriptionText = new JTextField();
	public JTextField contributorSurNameText = new JTextField();
	public JTextField contributorGivenNameText  = new JTextField();
	public JTextField contributorInstitutionText = new JTextField();
	public JTextField projectStartDateText = new JTextField();
	public JTextField projectStopDateText = new JTextField();
	public JTextField projectStateNameText = new JTextField();
	public JTextField projectPlaceNameText = new JTextField();
	
	public Vector contributorGivenNameTextVector = new Vector();
	public Vector contributorSurNameTextVector = new Vector();
	public Vector contributorInstitutionTextVector = new Vector();
	
	
	//selectors
	public  JButton continueSelector = new JButton();
	public  JButton addContributorSelector = new JButton();
	
	
	XMLparse xp = new XMLparse();
	ProjectManager manager = new ProjectManager();
	
 
 	/**
	 * method to initiate the Project class and GUI 
	 * and is an overloaded method of the other 'Project' 
	 * method, calling this method assumes that all the 
	 * panels are to be loaded
	 *
	 *
	 */
	public Project()
	{
		Hashtable componentHash = projectPanelHash();
		setTitle(" Project Identification");
			 initComponents(componentHash);
       pack ();
				//for linux increase size by 50
        //setSize(375, 370);
			setSize(500, 550);
	}
	 
  /** Creates new form Project */
	public Project(Hashtable componentHash)
	{
				//do nothing on close so that other interfaces are not stopped
//			setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
				setTitle(" Project Identification");
			 initComponents(componentHash);
       pack ();
				//for linux increase size by 50
        //setSize(375, 370);
			setSize(500, 550);
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
			JPanel contributorSelectorPanel = new JPanel();
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
					projectSummaryPanel = new JPanel();
					projectSummaryPanel = projectSummary();
				}
			}
			//the contributors panel
			if (componentHash.containsKey("projectContributorPanel") == true )
			{
				if (componentHash.get("projectContributorPanel").toString().equals("true") )
				{
					projectContributorPanel.setLayout(new BoxLayout(projectContributorPanel, BoxLayout.Y_AXIS));
				//	currentProjectContributorPanel = new JPanel();
					currentProjectContributorPanel = projectContributor();
					projectContributorPanel.add(currentProjectContributorPanel);
				}
			}
			//the date panel
			if (componentHash.containsKey("projectDatePanel") == true )
			{
				if (componentHash.get("projectDatePanel").toString().equals("true") )
				{
					projectDatePanel = new JPanel();
					projectDatePanel = projectDate();
				}
			}
			//location panel
			if (componentHash.containsKey("projectLocationPanel") == true )
			{
				if (componentHash.get("projectLocationPanel").toString().equals("true") )
				{
					projectLocationPanel = new JPanel();
					projectLocationPanel = projectLocation();
				}
			}
			
			//this is the main panel for the interface
			mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.Y_AXIS));
			mainPane.add(Box.createRigidArea(new Dimension(2,15)));
			mainPane.add( projectSummaryPanel );
			mainPane.add(Box.createRigidArea(new Dimension(2,15)));		
				scroller.setOpaque(true);
				scroller.setPreferredSize(new Dimension(300, 100));
				scroller.setMinimumSize(new Dimension(300, 100));
				scroller.setAlignmentX(RIGHT_ALIGNMENT);
				scroller.getViewport().add(projectContributorPanel);
			mainPane.add( scroller);	
			
			addContributorSelector.setText("Add Contributor");

			
			//setup the selector panel
				contributorSelectorPanel.setLayout(new FlowLayout(0,5,1));
				contributorSelectorPanel.add(addContributorSelector);
				contributorSelectorPanel.setPreferredSize(new Dimension(220, 30));
				contributorSelectorPanel.setMinimumSize(new Dimension(220, 30));

			
				mainPane.add( contributorSelectorPanel );
			mainPane.add(Box.createRigidArea(new Dimension(2,30)));
			mainPane.add( projectDatePanel);
			mainPane.add(Box.createRigidArea(new Dimension(2,15)));		
			mainPane.add( projectLocationPanel);
			mainPane.add(Box.createRigidArea(new Dimension(2,15)));		
			mainPane.add( updateProject() );
			mainPane.add(Box.createRigidArea(new Dimension(2,20)));
		
				
		
			
			//show the panes
		  setContentPane(mainPane);
      //frame.getContentPane().add(mainPane);
		
		//set up the activators
		//update the project
		continueSelector.addActionListener(new java.awt.event.ActionListener() 
		{
      public void actionPerformed(ActionEvent evt) 
			{
        handleUpdateAcivator(evt);
       }
    });
		
		//add contributors
		addContributorSelector.addActionListener(new java.awt.event.ActionListener() 
		{
      public void actionPerformed(ActionEvent evt) 
			{
				handleAddContributorActivator_ed(evt);
       }
    });
		
		
		}
		
		/**
		 * method to set-up the form panel for the collection 
		 * of project dates
		 */
		public JPanel projectDate()
		{
			JPanel panel = new JPanel(); //main panel
			JPanel labelPanel = new JPanel();
			JPanel startDatePanel = new JPanel();
			JPanel stopDatePanel = new JPanel();
			
			labelPanel.setLayout(new FlowLayout(0,5,1));
			labelPanel.add( new JLabel("Project Date") );
			
			//setup the start date panel
			startDatePanel.setLayout(new FlowLayout(0,5,1));
			startDatePanel.add(new JLabel("Start Date:") );
			projectStartDateText.setColumns(15);
			projectStartDateText.setBackground(Color.pink);
			projectStartDateText.setText("");
			startDatePanel.add(projectStartDateText);
			
			//setup the start date panel
			stopDatePanel.setLayout(new FlowLayout(0,5,1));
			stopDatePanel.add(new JLabel("Stop Date:") );
			projectStopDateText.setColumns(15);
			projectStopDateText.setBackground(Color.pink);
			projectStopDateText.setText("");
			stopDatePanel.add(projectStopDateText);
			
			
			// add the components to the main panel
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			panel.setPreferredSize(new Dimension(220, 23));			
			panel.add(labelPanel);
			panel.add(startDatePanel);
			panel.add(stopDatePanel);
			
			return(panel);
		}
		
		
			
		/**
		 * method that provides a JPanel containing components that 
		 * may be used to define the loactation of a project
		 *
		 * @return panel -- panel containing text fields for the state and placeName
		 *
		 */
		public JPanel projectLocation()
		{
			JPanel panel = new JPanel(); //nain panel
			JPanel labelPanel = new JPanel();
			JPanel stateNamePanel = new JPanel();
			JPanel placeNamePanel = new JPanel();
			
			
			//setup the label panel
			labelPanel.setLayout(new FlowLayout(0,5,1));
			labelPanel.add( new JLabel("Project Location") );
			
			//setup the state name panel
			stateNamePanel.setLayout(new FlowLayout(0,5,1));
			stateNamePanel.add(new JLabel("State: ") );
			projectStateNameText.setColumns(15);
			projectStateNameText.setBackground(Color.pink);
			projectStateNameText.setText( "CA");
			stateNamePanel.add(projectStateNameText);
			
			
			//setup the place name panel
			placeNamePanel.setLayout(new FlowLayout(0,5,1));
			placeNamePanel.add(new JLabel("Place: ") );
			projectPlaceNameText.setColumns(15);
			projectPlaceNameText.setBackground(Color.pink);
			projectPlaceNameText.setText( "Yosemite");
			placeNamePanel.add(projectPlaceNameText);
			
			
			//add the components to the main panel
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			panel.setPreferredSize(new Dimension(220, 23));
			panel.add( labelPanel );
			panel.add(stateNamePanel);
			panel.add( placeNamePanel);
			return(panel);
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
			JPanel selectorPanel = new JPanel();
			
			//make a new instance of the textareas and add to the 
			//respective vectors
			contributorGivenNameText = new JTextField();
			contributorSurNameText = new JTextField();
			contributorInstitutionText = new JTextField();
			contributorGivenNameTextVector.addElement(contributorGivenNameText);
			contributorSurNameTextVector.addElement(contributorSurNameText);
			contributorInstitutionTextVector.addElement(contributorInstitutionText);
			
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
///			 addContributorSelector.setText("Add Contributor");
///			//institutionPanel.add( addContributorSelector );
			
			//setup the selector panel
///			selectorPanel.setLayout(new FlowLayout(0,5,1));
///			selectorPanel.add(addContributorSelector);
///			selectorPanel.setPreferredSize(new Dimension(220, 30));
///			selectorPanel.setMinimumSize(new Dimension(220, 30));
			
			//add the components to the main panel
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			panel.setPreferredSize(new Dimension(220, 135));
			panel.setMinimumSize(new Dimension(220, 135));
			
			panel.add( labelPanel);
			panel.add( givenNamePanel );
			panel.add( surNamePanel );
			panel.add( institutionPanel );
			panel.add( selectorPanel);
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
			continueSelector.setText("Update Project");
			continueSelector.setActionCommand("updateProject");
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
			chosenParameters.put("surName", contributorSurNameText.getText().toString().trim());
			chosenParameters.put("givenName", contributorGivenNameText.getText().toString().trim());
			chosenParameters.put("institution", contributorInstitutionText.getText().toString().trim());
			
			//dates
			chosenParameters.put("startDate", projectStartDateText.getText().toString().trim());
			chosenParameters.put("stopDate", projectStopDateText.getText().toString().trim());
			
			//place
			chosenParameters.put("stateName", projectStateNameText.getText().toString().trim());
			chosenParameters.put("placeName", projectPlaceNameText.getText().toString().trim());
			
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
		//remove the single contributorPane
	//	getContentPane().remove( projectContributorPanel );
//			Container contentPane = getContentPane();
//			contentPane.remove(button);
//		JPanel newElement = new JPanel();
//			newElement.setBackground(Color.white);
//			JButton button = new JButton();
//			newElement.add(button);
//		//	Container contentPane =	getContentPane();
//	//		frame.getContentPane().add(newElement);
//			 getContentPane().add(newElement);
//			 //  pack();
//      //  setSize(375, 370);
//		contentPane.validate();
//			repaint();

//		getContentPane().add(  );
	//	setSize(425, 820);
		repaint();
		
			new Project(componentHash).show();
		}
		
		
		
		private void handleAddContributorActivator_ed( ActionEvent evt)
		{
			JPanel currentPanel = new JPanel();
			currentPanel = projectContributor();
			projectContributorPanel.add( currentPanel );
			//mainPane.add(currentPanel);
			scroller.validate();
			mainPane.validate();
	
		}
		
		
		/** 
		 * method to handle the updating of a project ie. adding this
		 * data to the project xml file
		 */
		private String handleUpdateAcivator(ActionEvent evt)
		{
			String returnString = "ok";
			try
			{
				System.out.println("activation: "+evt.getActionCommand());
				
				//update the Progress Display class
					ProcessProgressDisplay.finishProcess("projectInitialization");
				
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
					
			} 
			catch (Exception e) 
			{
				System.err.println(e);
			}
			return(returnString);
		}
		
		
	/**
	 * method that returns a Hashtable containing keys corresponding to
	 * the panels that may be called in this class, and a value equal
	 * to true.  The hashtable may be edited and then passed to the 
	 * initiation method of this class
	 *
	 *
	 */
		public Hashtable projectPanelHash()
		{
			Hashtable componentHash = new Hashtable();
		 	componentHash.put("projectSummaryPanel", "true");
		 	componentHash.put("projectContributorPanel", "true");
		 	componentHash.put("projectDatePanel", "true");
		 	componentHash.put("projectLocationPanel", "true");
			return(componentHash);
		}
		 
	
	 public static void main (String args[]) 
	{
		Hashtable componentHash = new Hashtable();
		 componentHash.put("projectSummaryPanel", "true");
		 componentHash.put("projectContributorPanel", "true");
		 componentHash.put("projectDatePanel", "true");
		 componentHash.put("projectLocationPanel", "true");
		System.out.println("Starting Project");
		new Project(componentHash).show();
   }
	
}
