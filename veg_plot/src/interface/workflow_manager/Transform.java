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
 * This class represents the Transform attributes of a vegetation
 * collection / anaysis Transform
 *
 *  Authors: @authors@
 *  Release: @release@
 *	
 *  '$Author: harris $'
 *  '$Date: 2001-08-23 04:22:42 $'
 * 	'$Revision: 1.1 $'
 */
 
public class Transform extends JFrame 
{
	
	public JFrame frame = new JFrame();
	
	
	//the main frame panel
	public JPanel mainPane = new JPanel();
	public JPanel secondaryPanel = new JPanel();
	public JPanel currentFileNamePanel = new JPanel();
	
	//the main scroller
	public JScrollPane scroller = new JScrollPane();
	public JScrollPane queryCriteraListScroller = new JScrollPane();
	
	//the fileNames stuff
	public  JTextField fileNameText = new JTextField();
	public Vector  fileNameTextVector = new Vector();
	
	
	//selectors
	public  JButton continueSelector = new JButton();
	public  JButton moreFileSelector = new JButton();
	public  JButton fewerFileSelector = new JButton();
	

	XMLparse xp = new XMLparse();


	 
  /** Creates new form Transform */
	public Transform()
	{
				//do nothing on close so that other interfaces are not stopped
//			setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
				setTitle(" Transform Identification");
			 initComponents();
       pack ();
			setSize(375, 370);
	}
	
	
	/** 
	 * method to initaiate the components into the frame
	 *
	 * @param componentHash -- the components which should be added to the
	 * interface; like contributor=key, true=value
	 *
	 */
	 public void initComponents( ) 
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
		
		//returns panel with a textfield
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
	
	
	
		private void  handleContinueAcivator(ActionEvent evt)
		{
				for(int i = 0; i < fileNameTextVector.size(); i++ ) 
				{ 
					JTextField textField = (JTextField)fileNameTextVector.elementAt(i);
					System.out.println( textField.getText().toString() );
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
	
	
	public void fileChooser(ActionEvent e) 
	{
		System.out.println( e.getActionCommand()+" "+e.paramString() );
		FileDialog d = new FileDialog(
		Transform.this,
		"what file do you want");
		d.setFile("*.pdat");
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
	
		
	
		 
	
	 public static void main (String args[]) 
	{
		System.out.println("Starting Transform");
		new Transform().show();
   }
	
}
