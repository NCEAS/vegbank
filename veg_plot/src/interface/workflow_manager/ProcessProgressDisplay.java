import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import java.io.*;

/**
 *
 *  Authors: @authors@
 *  Release: @release@
 *	
 *  '$Author: harris $'
 *  '$Date: 2001-08-18 00:49:16 $'
 * 	'$Revision: 1.1 $'
 */


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
	
	//status icons 
	public static ImageIcon red_icon = new ImageIcon("images/red_box.jpg");
	public static ImageIcon yellow_icon = new ImageIcon("images/yellow_box.jpg");
	public static ImageIcon green_icon = new ImageIcon("images/green_box.jpg");
	public static JLabel red_label = new JLabel("",red_icon,JLabel.CENTER);
	public static JLabel yellow_label = new JLabel("",yellow_icon,JLabel.CENTER);
	public static JLabel green_label = new JLabel("",green_icon,JLabel.CENTER);
	
	//test status stuff
	public static JTextField projectDescriptionStatus = new JTextField();
	public static JTextField readLegacyDataStatus = new JTextField();
	public static JTextField processLegacyDataStatus = new JTextField();
	public static JTextField projectLoadLocalStatus = new JTextField();
	public static JTextField projectLoadRemoteStatus = new JTextField();
	public static JTextField projectExportStatus = new JTextField();
	
	
//	private JLabel projectDescriptionStatus = new JLabel("pending");
	

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
        setSize(320, 400);
    }
		
		 public void initComponents(int buttonNumber, Color color) 
		{
      frame.getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), 1));
      addWindowListener(new java.awt.event.WindowAdapter() 
			{
      	public void windowClosing(java.awt.event.WindowEvent evt) 
				{
        	// exitForm(evt);
        }
			});
			
			//this is the main panel for the interface
		mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.Y_AXIS));
		mainPane.add(Box.createRigidArea(new Dimension(2,30)));
		mainPane.add( projectDescriptionStatus(projectDescriptionPanel) );
		mainPane.add( transformDataStatus(transformDataPanel) );
		mainPane.add( loadDatabaseStatus(loadDatabasePanel) );
		mainPane.add( exportDataStatus( exportDataPanel ) );
		mainPane.add(Box.createRigidArea(new Dimension(2,30)));
		
		//add the continue button
		continueSelector.setText("Continue");
		continueSelectorPanel.setLayout(new FlowLayout(0,5,1));
		continueSelectorPanel.add(continueSelector);
		mainPane.add(continueSelectorPanel);

			//show the panes
			setContentPane(mainPane);
      //frame.getContentPane().add(mainPane);
		}
		
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
			exportDataSelectorPanel.add(exportDataSelector);
			exportDataSelectorPanel.add(projectExportStatus);
			panel.add(labelPanel);
			panel.add(exportDataSelectorPanel);
			return(panel);
		}
		
		public JPanel loadDatabaseStatus(JPanel panel)
		{
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			labelPanel = new JPanel();
			labelPanel.setLayout(new FlowLayout(0,5,1));
			labelPanel.add(loadDatabaseLabel);
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
			
			loadRemoteDatabaseSelector.setPreferredSize(new Dimension(220, 20));
			loadLocalDatabaseSelector.setPreferredSize(new Dimension(220, 20));
			
			loadRemoteDatabaseSelectorPanel.add(loadRemoteDatabaseSelector);
			loadRemoteDatabaseSelectorPanel.add(projectLoadRemoteStatus);
			panel.add(labelPanel);
			panel.add(loadLocalDatabaseSelectorPanel);
			panel.add(loadRemoteDatabaseSelectorPanel);
			return(panel);
		}
		
		public JPanel transformDataStatus(JPanel panel)
		{
		//	panel.setPreferredSize(new Dimension(220, 23));
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			readLegacyDataPanel.setLayout(new FlowLayout(0,5,1));
			processLegacyDataPanel.setLayout(new FlowLayout(0,5,1));
			
			readLegacyDataSelector.setText("Read Legacy Data Files");
			processLegacyDataSelector.setText("Process Legacy Data Files");
			
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
			
			//red_label = new JLabel( "",red_icon,JLabel.CENTER );
			readLegacyDataPanel.add( readLegacyDataStatus );
			//red_label = new JLabel( "",red_icon,JLabel.CENTER );
			processLegacyDataPanel.add( processLegacyDataSelector );
			processLegacyDataPanel.add( processLegacyDataStatus );
			//make a new panel for the label
			labelPanel = new JPanel();
			labelPanel.setLayout(new FlowLayout(0,5,1));
			labelPanel.add(transformDataLabel);
			panel.add(labelPanel);
			panel.add(readLegacyDataPanel);
			panel.add(processLegacyDataPanel);
			return(panel);
		}
		
		public JPanel projectDescriptionStatus(JPanel panel)
		{
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			projectInitializePanel.setLayout(new FlowLayout(0,5,1));
			projectInitializeSelector.setText("Describe Project");
			projectInitializeSelector.setPreferredSize(new Dimension(220, 20));
			projectInitializePanel.add(projectInitializeSelector);
		//	projectInitializePanel.add( red_label );
			projectDescriptionStatus.setColumns(7);
			projectDescriptionStatus.setBackground(java.awt.Color.pink);
			projectDescriptionStatus.setText("pending");
			projectInitializePanel.add( projectDescriptionStatus );
			labelPanel.setLayout(new FlowLayout(0,5,1));
			labelPanel.add(projectDescriptionLabel);
			panel.add(labelPanel);
			panel.add(projectInitializePanel);
			return(panel);
		}
		
		
		public void appendYellow(JPanel panel )
		{
			Container contentPane = getContentPane();
			panel.remove(red_label);
			panel.add(yellow_label);
			contentPane.validate();
			repaint();
		}
			
		public void appendGreen(JPanel panel )
		{
			Container contentPane = getContentPane();
			panel.remove(yellow_label);
			panel.add(green_label);
			contentPane.validate();
			repaint();
		}
		
		
		
		public void addPanelElement(String itemName)
		{
			setTitle("Progress ...");
			Container contentPane = getContentPane();
			contentPane.remove(button);
			JPanel newElement = new JPanel();
			newElement.setBackground(Color.white);
			button = new JButton();
			newElement.add(button);
		//	Container contentPane =	getContentPane();
	//		frame.getContentPane().add(newElement);
			 getContentPane().add(newElement);
			 //  pack();
      //  setSize(375, 370);
			contentPane.validate();
			repaint();
        
		}
	
}
