import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import java.io.*;

/**
 * This class is a Gui that allows the user to access all the tools 
 * in the veg plot database related tools
 *
 *  Authors: @authors@
 *  Release: @release@
 *	
 *  '$Author: harris $'
 *  '$Date: 2001-07-26 20:40:32 $'
 * 	'$Revision: 1.3 $'
 */

public class VegClient extends JFrame
   implements ActionListener, MenuListener
{  
	private QueryModule qm = new QueryModule();
	private NestedQueryBuilder nqb = new NestedQueryBuilder();
	private JMenuItem saveItem;
	private JMenuItem saveAsItem;
	private JMenuItem convertItem;
	private JMenuItem convert_pdat_wl;
	private JCheckBoxMenuItem readonlyItem;
	private JPopupMenu popup;
	
	
	public VegClient()
  {  
		setTitle(" Vegetation DataBase Workbench ");
		setSize(500, 200);
    addWindowListener(new WindowAdapter()
    {  
			public void windowClosing(WindowEvent e)
      {  
				System.exit(0);
      }
    } );

		/*add a cool image to the window pane*/
		JLabel label = new JLabel();
		Container contentPane = getContentPane();
		contentPane.add(label, "Center");
		label.setIcon(new ImageIcon("owlogos.jpg"));

  	  JMenuBar mbar = new JMenuBar();
		setJMenuBar(mbar);

		//Data Format Menu
		JMenu fileMenu = new JMenu("Data Format");
		fileMenu.addMenuListener(this);

		// demonstrate accelerators
		JMenuItem openItem = new JMenuItem("Convert xyz to GoCad");
		saveItem = new JMenuItem("Convert xyzp to GoCad");
		saveAsItem = new JMenuItem("Las (v2.0) to EV wlg");
		convertItem = new JMenuItem("Convert EV 3dgrid to GoCad");
		convert_pdat_wl = new JMenuItem("Convert EV pdat to Gocad well");

		mbar.add(makeMenu(fileMenu,
    new Object[]
		{  
			"New",
      openItem,
      null,
      saveItem,
      saveAsItem,
	    convertItem,
			convert_pdat_wl,
      null,
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

      // demonstrate icons and nested menus
			//Interpolate Data menu
			mbar.add(makeMenu("Data Load",
			new Object[]
			{  
				new JMenuItem("Create Empty 3D Grid", new ImageIcon("cut.gif")),
				new JMenuItem("Create Empty 2D Grid", new ImageIcon("copy.gif")),
				null,
				new JMenuItem("inverse distance 2D gridding", new ImageIcon("paste.gif")),
				new JMenuItem("inv. dist. 2Dgridding - elliptical search", new ImageIcon("paste.gif")),
				null,
				new JMenuItem("inverse distance 3D gridding", new ImageIcon("paste.gif")),
				new JMenuItem("inv. dist. 3Dgridding - elliptical search", new ImageIcon("paste.gif")),
				null,
				makeMenu("Options",
				new Object[]
					{
						readonlyItem,
						null,
						insertItem,
						overtypeItem
					},
					this)
				},
         this));

	      //Utilities Menu
	      JMenu helpMenu = new JMenu("Data Query");
	      helpMenu.setMnemonic('H');

	      mbar.add(makeMenu(helpMenu,
         new Object[]
         {  
					 	new JMenuItem("Query Vegetation Databases", 'I'),
						new JMenuItem("Extended Veg Database Query", 'I'),
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

				/*here is where the items are launched from the system*/
			if (arg.equals("Query Vegetation Databases")) 
			{
				try 
				{
					qm.launchQueryModule("start");
				}
				catch (Exception ex) 
				{
					System.out.println(" failed calling QueryModule"); 
				}
			}
			if (arg.equals("Extended Veg Database Query") )
			{
				try 
				{
					//Parallel p = new Parallel();
					nqb.launchQueryModule("start");
				}
				catch  (Exception ex)
				{
					System.out.println(" failed "); 
				}
			}
			if (arg.equals("Convert xyzp to GoCad")) 
			{
				try 
				{
					listener = Runtime.getRuntime().exec("java pdat2gocad"); 
				}
				catch (IOException ex) 
				{
					System.out.println("Uh oh, got an IOException error!"); ex.printStackTrace();
				}
			}
			if (arg.equals("Las (v2.0) to EV wlg")) 
			{
				try 
				{
					listener = Runtime.getRuntime().exec("java las2wlg"); }
				catch (IOException ex) 
				{
					System.out.println("Uh oh, got an IOException error!"); 
					ex.printStackTrace();
				}
			} //end if
			if (arg.equals("Exit"))
         System.exit(0);
   	}

		public void menuSelected(MenuEvent evt)
   	{  
			saveItem.setEnabled(!readonlyItem.isSelected());
      saveAsItem.setEnabled(!readonlyItem.isSelected());
			convertItem.setEnabled(!readonlyItem.isSelected());   
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

   public static void main(String[] args)
   {  Frame f = new VegClient();
      f.show();
   }

}
