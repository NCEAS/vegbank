/**
 * This class is a GUI class that allows the user to access all 
 * the tools in the veg plot database related tools -- this is the 
 * main interface class
 *
 *  Authors: @author@
 *  Release: @release@
 *	
 *  '$Author: harris $'
 *  '$Date: 2001-10-23 00:25:02 $'
 * 	'$Revision: 1.3 $'
 */
package vegclient.framework;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import java.io.*;

import vegclient.framework.*;
import vegclient.ternarysearch.ObjectLookupInterface; // a utility



public class VegClient extends JFrame
   implements ActionListener, MenuListener
{  
	private QueryModule qm = new QueryModule();
	private NestedQueryBuilder nqb = new NestedQueryBuilder();
	private DataUpload du = new DataUpload();
	//private FileTransformer transformer = new FileTransformer();
	
	private JCheckBoxMenuItem readonlyItem;
	private JPopupMenu popup;
	
	//classes that will be used by this one
	
	
	 /**
   * Creates a new instance of VegClient with the given title.
   *
   */
	public VegClient()
  {  
		setTitle("Vegetation Database Workbench - build: @release@ ");
		//setTitle(" Vegetation DataBase Workbench V1.0");
		setSize(500, 200);
    addWindowListener(new WindowAdapter()
    {  
			public void windowClosing(WindowEvent e)
      {  
				System.exit(0);
      }
    } );

		//add a cool image to the window pane
		JLabel label = new JLabel();
		Container contentPane = getContentPane();
		contentPane.add(label, "Center");
		//get the image from the resource bundle
		label.setIcon(new ImageIcon(getClass().getResource("images/owlogos.jpg")));
		//label.setIcon(new ImageIcon(("images/owlogos.jpg")));
			
  	JMenuBar mbar = new JMenuBar();
		setJMenuBar(mbar);

		//Data Format Menu
		JMenu fileMenu = new JMenu("Data Manipulation");
		//fileMenu.addMenuListener(this);
		JMenuItem transformData = new JMenuItem("Transform Legacy Plots Data");
		JMenuItem loadNewPlotData = new JMenuItem("Load New Plot Data");
		mbar.add(makeMenu(fileMenu,
    new Object[]
		{  
			transformData,
			loadNewPlotData,
			"Exit"
		},
			this));

		
			
      // demonstrate check box and radio button menus
      readonlyItem = new JCheckBoxMenuItem("Read-only");
      ButtonGroup group = new ButtonGroup();
      JRadioButtonMenuItem insertItem  = new JRadioButtonMenuItem("Insert");
      insertItem.setSelected(true);
      JRadioButtonMenuItem overtypeItem = new JRadioButtonMenuItem("Overtype");
      group.add(insertItem);
      group.add(overtypeItem);

			//Database Uploand menu
			mbar.add(makeMenu("Data Load",
			new Object[]
			{  
				new JMenuItem("Upload Plot Data"),
				new JMenuItem("Upload Plot Data - new"),
				new JMenuItem("Upload Vegetation Community", new ImageIcon("copy.gif"))
				},
         this));

	      //Database Query Menu
	      JMenu helpMenu = new JMenu("Data Query");
	      helpMenu.setMnemonic('H');

	      mbar.add(makeMenu(helpMenu,
         new Object[]
         {  
					 	new JMenuItem("Query Vegetation Databases", 'I'),
						new JMenuItem("Extended Veg Database Query", 'I'),
						new JMenuItem("Plot Summary Viewer", 'I'),
						new JMenuItem("Coordinate transform", 'A')
         },
				 this));

     		// demonstrate pop-ups
    		  popup = makePopupMenu(
         new Object[]
         {  "Cut",
            "Copy",
            "Paste"
         },
         this);
				 
				 //the utilities menu
				JMenu utilitiesMenu = new JMenu("Utilities");
				//fileMenu.addMenuListener(this);
				JMenuItem configuration = new JMenuItem("Client Configuration");
				JMenuItem workflowManager = new JMenuItem("Workflow Manager");
				JMenuItem databaseTools = new JMenuItem("Database Tools");
				JMenuItem commonLists = new JMenuItem("Common Lists");
				
				//set the action command
				mbar.add(makeMenu(utilitiesMenu,
    		new Object[]
				{  
					configuration,
					workflowManager,
					databaseTools,
					commonLists,
					"Exit"
				},
				this));
		

      getContentPane().addMouseListener(new MouseAdapter()
			{  
				public void mouseReleased(MouseEvent evt)         
            {  
							if (evt.isPopupTrigger())
                  popup.show(evt.getComponent(),
                  evt.getX(), evt.getY());
            }
         });
   }

/**
 * method to perform the action which should be broken into smaller
 * sub-methods
 *
 */
	public void actionPerformed(ActionEvent evt)
	{  
		String arg = evt.getActionCommand();
    Process listener;  // define the listener for the external exacutable
    System.out.println("launching: "+arg );
		try
		{
			//the simple query module
			if (arg.equals("Query Vegetation Databases")) 
			{
				qm.launchQueryModule("start");
			}
			//the nested query module
			else if (arg.equals("Extended Veg Database Query") )
			{
				nqb.launchQueryModule("start");
			}
			//data upload
			else if  (arg.trim().equals("Upload Plot Data") )
			{
				du.launchUploadModule("start");
			}
			else if  (arg.trim().equals("Upload Plot Data - new") )
			{
				new Loader().show();
			}
			else if (arg.equals("Exit"))
			{
    		System.exit(0);
			}
			else if (arg.trim().equals("Workflow Manager"))
			{
				new ProjectInitialize ().show ();
			}
			
			else if (arg.trim().equals("Database Tools"))
			{
				new DatabaseManagerInterface ().show ();
			}
			
			//Plot Summary Viewer
			else if (arg.trim().equals("Plot Summary Viewer"))
			{
				new SummaryViewerInterface().show ();
			}
			
			else if (arg.trim().equals("Client Configuration"))
			{
				new ConfigurationInterface ().show ();
			}
			
			else if ( arg.trim().equals("Transform Legacy Plots Data") )
			{
				System.out.println("Launching the file Transformer");
				new FileTransformer("chooseFileType").show();
			}
			else if ( arg.trim().equals("Load New Plot Data") )
			{
				System.out.println("Launching the New Data Loader");
				new NewDataLoader().show();
			}
			else if ( arg.trim().equals("Common Lists") )
			{
				System.out.println("Launching the ObjectLookupInterface");
				new ObjectLookupInterface().show();
			}
			else 
			{
				//send to the debugger
				ProjectManager.debug(0, "not yet implemented: "+evt.getActionCommand() );
			}
   	}
		catch  (Exception ex)
		{
				System.out.println(" failed "); 
		}
	}
		

		public void menuSelected(MenuEvent evt)
   	{  
//			saveItem.setEnabled(!readonlyItem.isSelected());
//      saveAsItem.setEnabled(!readonlyItem.isSelected());
//			convertItem.setEnabled(!readonlyItem.isSelected());   
		}

   public void menuDeselected(MenuEvent evt)
   {
   }

   public void menuCanceled(MenuEvent evt)
   {
   }

   public static JMenu makeMenu(Object parent,
      Object[] items, Object target)
   {  JMenu m = null;
      if (parent instanceof JMenu)
         m = (JMenu)parent;
      else if (parent instanceof String)
         m = new JMenu((String)parent);
      else
         return null;

      for (int i = 0; i < items.length; i++)
      {  if (items[i] == null)
            m.addSeparator();
         else
            m.add(makeMenuItem(items[i], target));
      }

      return m;
   }

   public static JMenuItem makeMenuItem(Object item,
      Object target)
   {  JMenuItem r = null;
      if (item instanceof String)
         r = new JMenuItem((String)item);
      else if (item instanceof JMenuItem)
         r = (JMenuItem)item;
      else return null;

      if (target instanceof ActionListener)
         r.addActionListener((ActionListener)target);
      return r;
   }

   public static JPopupMenu makePopupMenu
      (Object[] items, Object target)
   {  JPopupMenu m = new JPopupMenu();

      for (int i = 0; i < items.length; i++)
      {  if (items[i] == null)
            m.addSeparator();
         else
            m.add(makeMenuItem(items[i], target));
      }

      return m;
   }

	 /**
	  * The main method used to start the vegetation 
		* workbench software
		* 
		*/
   public static void main(String[] args)
   {  
		 	//pop up the SplashFrame
			SplashFrame sf = new SplashFrame();
      sf.setVisible(true);

			//show the main frame from which the various modules may be 
			//started from
		 	Frame f = new VegClient();
      f.show();
   }

}
