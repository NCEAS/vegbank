/**
 * This class represents the FileTransformer attributes of a vegetation
 * collection / anaysis FileTransformer
 *
 *  Authors: @author@
 *  Release: @release@
 *	
 *  '$Author: harris $'
 *  '$Date: 2001-10-10 18:12:41 $'
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
//the xml editor
import org.xmloperator.Tool;

import vegclient.framework.*;

 
public class FileTransformer extends JFrame 
{
	
	public JFrame frame = new JFrame();
	
	
	//the main frame panel
	public JPanel mainPane = new JPanel();
	public JPanel secondaryPanel = new JPanel();
	public JPanel currentFileNamePanel = new JPanel();
	//the panel on which the user chooses the file format type for the input data 
	public JPanel dataTypePanel = new JPanel();
	
	
	
	//the main scroller
	public JScrollPane scroller = new JScrollPane();
	public JScrollPane queryCriteraListScroller = new JScrollPane();
	
	//the fileNames stuff
	public  JTextField fileNameText = new JTextField();
	//expicit file names for the tnc data sets
	public JTextField tncProjectDataText = new JTextField();
	public JTextField tncSiteDataText = new JTextField();
	public JTextField tncSpeciesDataText = new JTextField();
	
	public Vector  fileNameTextVector = new Vector();
	
	
	
	//button selectors
	public  JButton continueSelector = new JButton();
	public  JButton moreFileSelector = new JButton();
	public  JButton fewerFileSelector = new JButton();
	public JButton dataFileTypeSelector = new JButton();
	
	
	//radio buttons
	private JRadioButton tncDataFormatSelector = new JRadioButton();
	private JRadioButton unknownDataFormatSelector = new JRadioButton();
	private JRadioButton structuredDataFormatSelector = new JRadioButton();
	private JRadioButton turboVegDataFormatSelector = new javax.swing.JRadioButton();
	
	//classes to be used
	XMLparse xp = new XMLparse();
	ProjectManager pm = new ProjectManager();
	HtmlViewer hv = new HtmlViewer();


	 
  /** 
	 * Creates new form FileTransformer 
	 * @param action -- the desired action from the class, can include
	 * 'chooseFileType', 'transformTNC' or 'transformUnknown'
	 */
	public FileTransformer(String action)
	{
				//do nothing on close so that other interfaces are not stopped
//			setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
			setTitle(" FileTransformer build: @release@ ");
			if (action.trim().equals("chooseFileType") )
			{
				initFileTransformer();
				pack();
				setSize(375, 370);
			}
			else if (action.trim().equals("transformUnknown"))
			{
				initChooserComponents();
				 pack();
				setSize(375, 370);
			}
			else if (action.trim().equals("transformTNC"))
			{
				initSelectTncFiles();
				 pack();
				setSize(550, 230);
			}
			//this is called last inorder to edit the files
			else if (action.trim().equals("editTransformedFile"))
			{
				initEditTransformedData();
				 pack();
				setSize(375, 370);
			}
			else
			{
				System.out.println("Unknown Action");
			}
     
	}
	
	/**
	 * method that allows the user to edit the recently transformed files and
	 * to view summary information about the transformed file
	 */
	 public void initEditTransformedData()
	 {
		  frame.getContentPane().setLayout(new BoxLayout(getContentPane(), 1));
      addWindowListener(new java.awt.event.WindowAdapter() 
			{
      	public void windowClosing(java.awt.event.WindowEvent evt) 
				{
					System.out.println("closing");
        	// exitForm(evt);
        }
			});
			
			//the components for the project summary viewer
			JButton viewProjectSummary = new JButton();
			viewProjectSummary.setText("View Project Summary");
			viewProjectSummary.setActionCommand("viewProjectSummary");
			
			JButton editProjectFile = new JButton();
			editProjectFile.setText("Edit Project");
			editProjectFile.setActionCommand("editProjectFile");
			
			
				//add the components
			mainPane.add(viewProjectSummary);
			mainPane.add(editProjectFile);
			
			
			mainPane.add(Box.createRigidArea(new Dimension(2,15)));
			mainPane.add(Box.createRigidArea(new Dimension(2,15)));			
		
			//show the panes
		  setContentPane(mainPane);
      //frame.getContentPane().add(mainPane);
		
			//set up the activators
		viewProjectSummary.addActionListener(new java.awt.event.ActionListener() 
		{
      public void actionPerformed(ActionEvent evt) 
			{
        
				hv.show();
       	hv.showData("test_project.xml", "transformMultiPlotSummary.xsl",
				"text/html");
       }
    });
		
		//call the xml editor
		editProjectFile.addActionListener(new java.awt.event.ActionListener() 
		{
      public void actionPerformed(ActionEvent evt) 
			{
        //Tool xmlTool = new Tool("test_project.xml");
				Tool.createToolInstance("test_project.xml");
				
       }
    });
		
	 }
		 
	 
	 
	
	/**
	 * method that provides the user with an interface for selecting the files
	 * that are consistent with the tnc storage format
	 */
	 public void initSelectTncFiles()
	 {
		 frame.getContentPane().setLayout(new BoxLayout(getContentPane(), 1));
      addWindowListener(new java.awt.event.WindowAdapter() 
			{
      	public void windowClosing(java.awt.event.WindowEvent evt) 
				{
					System.out.println("closing");
        	// exitForm(evt);
        }
			});
			
	
			
			//set up the sub panels for the site and species data
			JPanel projectFilePanel = new JPanel();
			JPanel siteFilePanel = new JPanel();
			JPanel speciesFilePanel = new JPanel();
			
			//buttons for selecting the files
			JButton projectFileSelector = new JButton();
			JButton siteFileSelector = new JButton();
			JButton speciesFileSelector = new JButton();
			projectFileSelector.setText("choose project file");
			siteFileSelector.setText("choose site file");
			speciesFileSelector.setText("choose species file");
			
			//the continue button
			JButton transformTncData = new JButton();
			transformTncData.setText("transform data");
			
			//the project file
			projectFilePanel.setLayout(new FlowLayout(0,5,1));
			projectFilePanel.add( new JLabel("Project Data File: " ) );
			tncProjectDataText.setColumns(15);
			tncProjectDataText.setBackground(Color.pink);
			tncProjectDataText.setText("tncYosemiteProject.txt");
			projectFilePanel.add(tncProjectDataText );
			projectFilePanel.add(projectFileSelector );
			
			//the site file
			siteFilePanel.setLayout(new FlowLayout(0,5,1));
			siteFilePanel.add( new JLabel("Site Data File: " ) );
			tncSiteDataText.setColumns(15);
			tncSiteDataText.setBackground(Color.pink);
			tncSiteDataText.setText("tncSamplePlotsInput.txt");
			siteFilePanel.add(tncSiteDataText);
			siteFilePanel.add(siteFileSelector);
			
			
			//the species file
			speciesFilePanel.setLayout(new FlowLayout(0,5,1));
			speciesFilePanel.add( new JLabel("Species Data File: " ) );
			tncSpeciesDataText.setColumns(15);
			tncSpeciesDataText.setBackground(Color.pink);
			tncSpeciesDataText.setText("tncSampleSpeciesInput.txt");
			speciesFilePanel.add(tncSpeciesDataText);
			speciesFilePanel.add(speciesFileSelector);
			
		
			//add the sub-panes
			mainPane.add(projectFilePanel);
			mainPane.add(siteFilePanel);
			mainPane.add(speciesFilePanel);
			
		
			mainPane.add(Box.createRigidArea(new Dimension(2,15)));
			//add the continue buttons
			mainPane.add( transformTncData );
			mainPane.add(Box.createRigidArea(new Dimension(2,15)));			
		
			//show the panes
		  setContentPane(mainPane);
      //frame.getContentPane().add(mainPane);
		
			//set up the activators
			
		//choose the project file
		projectFileSelector.addActionListener(new java.awt.event.ActionListener() 
		{
      public void actionPerformed(ActionEvent evt) 
			{
        fileChooser(evt, tncProjectDataText);
       }
    });
		
		//choose the site file
		siteFileSelector.addActionListener(new java.awt.event.ActionListener() 
		{
      public void actionPerformed(ActionEvent evt) 
			{
        fileChooser(evt, tncSiteDataText);
       }
    });
		//choose the species file
		speciesFileSelector.addActionListener(new java.awt.event.ActionListener() 
		{
      public void actionPerformed(ActionEvent evt) 
			{
        fileChooser(evt, tncSpeciesDataText);
       }
    });
		
		//this is pointing to the method that does the data transformation
		transformTncData.addActionListener(new java.awt.event.ActionListener() 
		{
      public void actionPerformed(ActionEvent evt) 
			{
				//call the method that is in the ProjectManager class 
				//to initiate the tnc plot transform process
				System.out.println("calling: ProjectManager.tranformTNCDataSet");
        pm.tranformTNCDataSet(tncProjectDataText.getText(), 
				tncSiteDataText.getText(), 
				tncSpeciesDataText.getText(), "|" );
				
				//now allow the user to view a summary or edit the file
				//this.dispose();
				new FileTransformer("editTransformedFile").show();
				
				//make the icons on the progress display class green
				ProcessProgressDisplay.finishProcess("dataTransformation");
       }
    });
		
		
	 }
	
	
	/**
	 * method that prompts the user for the format type of the data that is to be
	 * transformed into the xml document that can be injested into the plots
	 * database.  This can be thought of as the beginning point for the plot data
	 * file transformation process
	 */
	 public void initFileTransformer()
	 {
		 frame.getContentPane().setLayout(new BoxLayout(getContentPane(), 1));
      addWindowListener(new java.awt.event.WindowAdapter() 
			{
      	public void windowClosing(java.awt.event.WindowEvent evt) 
				{
        	// exitForm(evt);
        }
			});
			
			//the secondary panel
			
			secondaryPanel.setLayout(new BoxLayout(secondaryPanel, BoxLayout.Y_AXIS));
			
					
			//this is the main panel for the interface
			mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.Y_AXIS));
			
			dataTypePanel = getDataTypePanel();
			secondaryPanel.add( dataTypePanel );
		
			mainPane.add(Box.createRigidArea(new Dimension(2,15)));
			mainPane.add( dataTypePanel );
			mainPane.add(Box.createRigidArea(new Dimension(2,15)));			
		
			//show the panes
		  setContentPane(mainPane);
      //frame.getContentPane().add(mainPane);
		
			//set up the activators
	 }
	
	
	/** 
	 * method to initaiate the components into the frame for choosing the files
	 * that are going to be involved in the file transformation process
	 *
	 */
	 public void initChooserComponents( ) 
		{
      frame.getContentPane().setLayout(new BoxLayout(getContentPane(), 1));
      addWindowListener(new java.awt.event.WindowAdapter() 
			{
      	public void windowClosing(java.awt.event.WindowEvent evt) 
				{
        	// exitForm(evt);
        }
			});
			
			
			//the secondary panel
			secondaryPanel.setLayout(new BoxLayout(secondaryPanel, BoxLayout.Y_AXIS));
			
					
			//this is the main panel for the interface
			mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.Y_AXIS));
			
			currentFileNamePanel = fileSelector();
			secondaryPanel.add( currentFileNamePanel );
		
				
				
			scroller.setOpaque(true);
			scroller.setPreferredSize(new Dimension(375, 40));
			scroller.setMinimumSize(new Dimension(375, 40));
			scroller.setAlignmentX(RIGHT_ALIGNMENT);
			scroller.getViewport().add(secondaryPanel);
				
			//	mainPane.add(Box.createRigidArea(new Dimension(2,20)));
		//	scroller.add( secondaryPanel);
			
			//put the name on the buttons
			continueSelector.setText("continue");
			moreFileSelector.setText("more files");
			fewerFileSelector.setText("fewer files");
				
			JPanel buttonPanel = new JPanel();
			buttonPanel.setLayout(new FlowLayout(0,5,1));
			buttonPanel.add( moreFileSelector );
			buttonPanel.add( fewerFileSelector );
			buttonPanel.add( continueSelector );
			mainPane.add(Box.createRigidArea(new Dimension(2,15)));
			mainPane.add( scroller );
			mainPane.add(Box.createRigidArea(new Dimension(2,15)));
			mainPane.add( buttonPanel);
			
		
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
			moreFileSelector.addActionListener(new java.awt.event.ActionListener() 
		{
      public void actionPerformed(ActionEvent evt) 
			{
        handleMoreActivator(evt);
       }
    });
		fewerFileSelector.addActionListener(new java.awt.event.ActionListener() 
		{
      public void actionPerformed(ActionEvent evt) 
			{
        handleFewerActivator(evt);
       }
    });
		
		}
		
		
		/**
		 * method that returns a panel that propmts the user
		 * to select the type of data that is to be transformed
		 */
		private JPanel getDataTypePanel()
		{
			
			JPanel	panel = new javax.swing.JPanel();
       JPanel tncPanel = new javax.swing.JPanel();
        //tncDataFormatSelector = new javax.swing.JRadioButton();
       JPanel structuredDataPanel = new javax.swing.JPanel();
       //JRadioButton structuredDataFormatSelector = new javax.swing.JRadioButton();
       JPanel turboVegPanel = new javax.swing.JPanel();
       //JRadioButton turboVegDataFormatSelector = new javax.swing.JRadioButton();
       JPanel unknownPanel = new javax.swing.JPanel();
        //unknownDataFormatSelector = new javax.swing.JRadioButton();
       JPanel fileTypeSelectionPanel = new javax.swing.JPanel();
       JButton dataFileTypeSelector = new javax.swing.JButton();
        
       
        
        panel.setLayout(new javax.swing.BoxLayout(panel, javax.swing.BoxLayout.Y_AXIS));
        
        tncPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        
        tncPanel.setBorder(new javax.swing.border.EtchedBorder());
        tncPanel.setPreferredSize(new java.awt.Dimension(140, 30));
        tncPanel.setMinimumSize(new java.awt.Dimension(140, 30));
        tncDataFormatSelector.setForeground(java.awt.Color.blue);
        tncDataFormatSelector.setText("TNC Dataset");
        tncPanel.add(tncDataFormatSelector);
        
        panel.add(tncPanel);
        
        structuredDataPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        
        structuredDataPanel.setBorder(new javax.swing.border.EtchedBorder());
        structuredDataPanel.setPreferredSize(new java.awt.Dimension(14, 30));
        structuredDataFormatSelector.setForeground(java.awt.Color.blue);
        structuredDataFormatSelector.setText("Structured Data Package");
        structuredDataPanel.add(structuredDataFormatSelector);
        
        panel.add(structuredDataPanel);
        
        turboVegPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        
        turboVegPanel.setBorder(new javax.swing.border.EtchedBorder());
        turboVegPanel.setPreferredSize(new java.awt.Dimension(14, 30));
        turboVegDataFormatSelector.setForeground(java.awt.Color.blue);
        turboVegDataFormatSelector.setText("TurboVeg Export Data File");
        turboVegPanel.add(turboVegDataFormatSelector);
        
        panel.add(turboVegPanel);
        
        unknownPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        
        unknownPanel.setBorder(new javax.swing.border.EtchedBorder());
        unknownPanel.setPreferredSize(new java.awt.Dimension(14, 30));
        unknownDataFormatSelector.setForeground(java.awt.Color.blue);
        unknownDataFormatSelector.setText("User Defined Format");
        unknownPanel.add(unknownDataFormatSelector);
        
        panel.add(unknownPanel);
        
        fileTypeSelectionPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 12));
        
        fileTypeSelectionPanel.setBackground(java.awt.Color.lightGray);
        fileTypeSelectionPanel.setPreferredSize(new java.awt.Dimension(10, 30));
        dataFileTypeSelector.setForeground(java.awt.Color.blue);
        dataFileTypeSelector.setText("Continue");
        fileTypeSelectionPanel.add(dataFileTypeSelector);
        
        panel.add(fileTypeSelectionPanel);
				dataFileTypeSelector.addActionListener(new java.awt.event.ActionListener() 
				{
    			  public void actionPerformed(ActionEvent evt) 
						{
    			    fileTypeChooser(evt);
    			   }
    		});
				return(panel);
/**
			JPanel panel = new JPanel(); //main panel
			JPanel tncPanel = new JPanel();
			JPanel unknownPanel = new JPanel();
			
//			JPanel fileNamePanel = new JPanel();
			JButton fileSelector = new JButton();
			
			//set up the labels for the selector radio buttons
			JLabel  tncDataFormatLabel= new JLabel("TNC Data Format");
			JLabel unknownDataFormatSelectorlabel = new JLabel("Other Data Format");
			
			//set up the panels that go within the main panel
			tncPanel.setLayout(new FlowLayout(0,5,1));
			tncPanel.add( tncDataFormatLabel );
			tncPanel.add( tncDataFormatSelector );
			unknownPanel.setLayout(new FlowLayout(0,5,1));
			unknownPanel.add(unknownDataFormatSelectorlabel );
			unknownPanel.add( unknownDataFormatSelector );
			
			//set up the button
			dataFileTypeSelector.setText("continue");
			 
			 
			//add the subpanels to the main panel
			panel.add( tncPanel);
			panel.add(unknownPanel);
			panel.add( dataFileTypeSelector);
	
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			panel.setPreferredSize(new Dimension(220, 30));			
			panel.setMinimumSize(new Dimension(220, 30));

		dataFileTypeSelector.addActionListener(new java.awt.event.ActionListener() 
		{
      public void actionPerformed(ActionEvent evt) 
			{
        fileTypeChooser(evt);
       }
    });
			
			return(panel);
		**/
		}
		
	/**
	 * this method determines the file type choice that the user makes regarding
	 * the file format type of the input data set and then continues the process
	 * the method determine the file type chosen from the current frame
	 * 
	 */
	private void fileTypeChooser(ActionEvent e) 
	{
		//figure out what the user has selected for the file format type
		if (tncDataFormatSelector.isSelected())
		{
			System.out.println("Starting FileTransformer");
			//close this window for the user
			this.dispose();
			new FileTransformer("transformTNC").show();
		}
		//if the user has seleceted the structured data package 
		//transformation option
		else if (structuredDataFormatSelector.isSelected() )
		{
			System.out.println("starting the transform process for unknown file format");
			//close this window for the user
			this.dispose();
			new FileTransformer("transformUnknown").show();
		}
		//handle this the same as the structured
		else if (unknownDataFormatSelector.isSelected() )
		{
			System.out.println("starting the transform process for unknown file format");
			//close this window for the user
			this.dispose();
			new FileTransformer("transformUnknown").show();
		}
		//handle the turboveg selector
		else if (turboVegDataFormatSelector.isSelected() )
		{
			System.out.println("starting the transform process for turboVeg export file format");
			//close this window for the user
		//	this.dispose();
		//	new FileTransformer("transformUnknown").show();
		new TurboVegConverterInterface().show();
		}
		else
		{
			System.out.println("nothing selected");
			pm.debug(0, "please choose a data format type");
		}
	}
		
		
		
		
		
		/**
		 * method that returns a JPanel which accomodates the 
		 * selection of a file
		 */		
		private JPanel fileSelector()
		{
			
			JPanel panel = new JPanel(); //main panel
			JPanel labelPanel = new JPanel();
			JPanel fileNamePanel = new JPanel();
			JButton fileSelector = new JButton();
			
			fileSelector.setText("file");
			
			labelPanel.setLayout(new FlowLayout(0,5,1));
			labelPanel.add( new JLabel("File "+fileNameTextVector.size() ) );
			
			
			fileNameText = new JTextField();
			fileNameTextVector.addElement( fileNameText );
			fileNameText.setColumns(15);
			fileNameText.setBackground(Color.pink);
			fileNameText.setText("");
			
			
			fileNamePanel.setLayout(new FlowLayout(0,5,1));
			fileNamePanel.add( fileNameText );
			fileNamePanel.add( fileSelector );
			// add the components to the main panel
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			panel.setPreferredSize(new Dimension(220, 30));			
			panel.setMinimumSize(new Dimension(220, 30));
			panel.add(labelPanel);
			panel.add(fileNamePanel);
			
		fileSelector.addActionListener(new java.awt.event.ActionListener() 
		{
      public void actionPerformed(ActionEvent evt) 
			{
        fileChooser(evt);
       }
    });
			
			return(panel);
		}
	
	
	/**
	 * method that handles the continue button
	 */
		private void  handleContinueAcivator(ActionEvent evt)
		{
			for(int i = 0; i < fileNameTextVector.size(); i++ ) 
			{ 
				JTextField textField = (JTextField)fileNameTextVector.elementAt(i);
				System.out.println("FileTransformer.handleContinueAcivator" + textField.getText().toString() );
			}
		}
		
		private void handleFewerActivator( ActionEvent evt )
		{
		System.out.println("removing a file");
		JPanel panel = new JPanel();
		//Component comp  = (Component)secondaryPanel.lastElement();
		secondaryPanel.remove(currentFileNamePanel);
		//secondaryPanel.invalidate();
		mainPane.validate();
		}
		
		
		private void handleMoreActivator(ActionEvent evt)
		{
			currentFileNamePanel = new JPanel();
			currentFileNamePanel = fileSelector();
			secondaryPanel.add(currentFileNamePanel);
			mainPane.validate();
		}
	
	
	
	
	
	/**
	 * method that updates a textarea with the name of the file that is chosen
	 * @param e -- the action event
	 * @param textFieldName -- the JTextArea that should be updated with the name
	 * of the chosen file
	 *
	 */
	public void fileChooser(ActionEvent e,  JTextField textFieldName) 
	{
		System.out.println( e.getActionCommand()+" "+e.paramString() );
		FileDialog d = new FileDialog(
		FileTransformer.this,
		"what file do you want");
		d.setFile("*.txt");
		d.setDirectory(".");
		d.show();
		String yourfile = "*.*";
		if ((yourfile = d.getFile()) != null) {
		textFieldName.setText(yourfile);}
		else
		textFieldName.setText("Guess there is no file");
	}
	
	
	
	/**
	 * this method allows the user to retrieve a file from the file system and the
	 *chosen file is displayed in the text area named 'fileNameText' which makes
	 * this method a wrapper for the method with the same name
	 */
	public void fileChooser(ActionEvent e) 
	{
		System.out.println( e.getActionCommand()+" "+e.paramString() );
		FileDialog d = new FileDialog(
		FileTransformer.this,
		"what file do you want");
		d.setFile("*.txt");
		d.setDirectory(".");
		d.show();
		String yourfile = "*.*";
		if ((yourfile = d.getFile()) != null) {
		fileNameText.setText(yourfile);}
		else
		fileNameText.setText("Guess there is no file");
	}
		
		
		/**
		 * this method gets all the parameters chosen on the interface
		 * and returns their status in a hashtable
		 */
		private Hashtable getInterfaceParameters()
		{
			Hashtable chosenParameters = new Hashtable();
			return(chosenParameters); 
		}
	
		
	
		 
	/**
	 * main method to act as a standalone application and for testing
	 */
	 public static void main (String args[]) 
	{
		System.out.println("Starting FileTransformer");
		new FileTransformer("chooseFileType").show();
		//new FileTransformer("chooseFileType").show();
   }
	
}
