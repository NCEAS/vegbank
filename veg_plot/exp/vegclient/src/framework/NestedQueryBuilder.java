/**
 *
 * this class provides the interface for the 
 * vegetation nested query functionality. Thus
 * is a GUI class
 *
 * @author @author@ 
 * @version @release@ 
 *
 *     '$Author: harris $'
 *     '$Date: 2001-10-10 18:12:42 $'
 *     '$Revision: 1.1 $'
 */
package vegclient.framework;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import java.io.*;


import vegclient.framework.*;
import vegclient.datarequestor.*;


public class NestedQueryBuilder extends JFrame 
{
	
		ResourceBundle rb = ResourceBundle.getBundle("vegclient");
		private HtmlViewer hv = new HtmlViewer();
	 	private JTextField queryValue = new JTextField();
	 	private JList queryCriteriaList = new JList();
		private JPanel queryCriteriaListPane = new JPanel(); //the pane containing
		//the label and the scroller
		private JList queryOperatorList = new JList();
		private JPanel queryOperatorListPane = new JPanel();
		private JPanel mainPane = new JPanel();
		private JPanel databaseTypePane = new JPanel();
		private JPanel queryLabelPane = new JPanel();
	 	private JPanel queryPane = new JPanel();
		private JPanel queryValuePane = new JPanel();
		private JPanel returnTypePane = new JPanel();
		private JPanel queryActivationPane = new JPanel();
		private JPanel updatedNestedQueryPane = new JPanel();
		private JTextField updatedNestedQueryText = new JTextField();
		
		
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
		private JRadioButton localDatabaseTypeSelector = new JRadioButton();
		private JRadioButton remoteDatabaseTypeSelector = new JRadioButton();
    private JRadioButton vegPlotReturnTypeSelector = new JRadioButton();
		private JRadioButton plantTaxaReturnTypeSelector = new JRadioButton();
		private JRadioButton vegCommunityReturnTypeSelector = new JRadioButton();
		private JButton appendQueryActivator = new JButton();
		private JButton runQueryActivator = new JButton();
		private JButton renewQueryActivator = new JButton();
		
		//vector to store the nested query for issue against the local database
		static Vector queryVector = new Vector();
		
		//utility classes
		private DataRequestClient drc = new DataRequestClient();
		private DataRequestHandler requestHandler = new DataRequestHandler();
		
		
    /** Creates new form NestedQueryBuilder */
    public NestedQueryBuilder() 
		{
				//do nothing on close so that other interfaces are not stopped
				setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
				setTitle(" NestedQueryBuilder -- build:  @release@ ");
        initComponents ();
        pack ();
        setSize(720, 450);
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
			//lay out the database type selector -- central vs local
			databaseTypePane.setLayout( new FlowLayout(FlowLayout.LEFT,1,1) );
			databaseTypePane.add(localDatabaseTypeSelector);
			databaseTypePane.add(remoteDatabaseTypeSelector);
			localDatabaseTypeSelector.setAlignmentX(0.3F);
      localDatabaseTypeSelector.setText("Local Database");
			localDatabaseTypeSelector.setAlignmentX(LEFT_ALIGNMENT);
			remoteDatabaseTypeSelector.setAlignmentX(0.3F);
      remoteDatabaseTypeSelector.setText("Remote Database");
			remoteDatabaseTypeSelector.setAlignmentX(LEFT_ALIGNMENT);
			
      
			//lay out the queryPane -- three components (criteria,value,operator)
			//first the labels 
			JLabel criteriaLabel = new JLabel("Query Criteria");
			JLabel operatorLabel = new JLabel("Query Operator");
			JLabel valueLabel = new JLabel("Query Token");
			
			
			//criteria elements array
			String [] queryCriteriaElements = {"Plant Taxon", "State",  "Elevation",
			"Longitude", "Latitude", "Plot Code", "Geology", "Slope Aspect", 
			"Slope Gradient", "Slope Position",  "Plot Shape"};
      queryCriteriaList.setListData(queryCriteriaElements);
      //operators array
			String [] queryOperatorElements = {"like", "less than", "greater than", "equals", 
				"begins with", "ends with", "contains"};
      queryOperatorList.setListData(queryOperatorElements);
			//value text area
			queryValue.setText("%");
    	queryValue.setColumns(20);
			queryValue.setBackground(java.awt.Color.pink);
			
			//the criteria scroller -- in a pane
			JScrollPane queryCriteraListScroller = new JScrollPane(queryCriteriaList);
			queryCriteraListScroller.setPreferredSize(new Dimension(100, 40));
			queryCriteraListScroller.setMinimumSize(new Dimension(100, 40));
			queryCriteraListScroller.setAlignmentX(LEFT_ALIGNMENT);
			queryCriteriaListPane.setLayout(new BoxLayout(queryCriteriaListPane, BoxLayout.Y_AXIS));
			queryCriteriaListPane.add(criteriaLabel);
			queryCriteriaListPane.add(queryCriteraListScroller);
			
			//the operator scroller -- in a pane
			JScrollPane queryOperatorListScroller = new JScrollPane(queryOperatorList);
			queryOperatorListScroller.setPreferredSize(new Dimension(100, 40));
			queryOperatorListScroller.setMinimumSize(new Dimension(100, 40));
			queryOperatorListScroller.setAlignmentX(LEFT_ALIGNMENT);
			queryOperatorListPane.setLayout(new BoxLayout(queryOperatorListPane, BoxLayout.Y_AXIS));
			queryOperatorListPane.add(operatorLabel);
			queryOperatorListPane.add(queryOperatorListScroller);
			
			//the query value scroller -- in a pane
			//the value -- add the panel then the text area to that panel
			//queryValuePane.setLayout(new BoxLayout(queryValuePane, BoxLayout.Y_AXIS));
			//queryValue.setAlignmentY(CENTER_ALIGNMENT);
			queryValuePane.add(valueLabel);
			queryValuePane.add(queryValue);
			
			
			//the main query pane containing the criteria / operator / value panes
			queryPane.setLayout(new BoxLayout(queryPane, BoxLayout.X_AXIS));
      
			queryPane.add(Box.createRigidArea(new Dimension(5,5)));
			//add the individual criteria / operator / value panes
			queryPane.add(queryCriteriaListPane);
			queryPane.add(Box.createRigidArea(new Dimension(5,5)));
			queryPane.add(queryOperatorListPane);
			queryPane.add(Box.createRigidArea(new Dimension(5,5)));
			queryPane.add( queryValuePane );
			queryPane.setBorder(BorderFactory.createEmptyBorder(22,2,2,22));


			//add the elements to the return type pane
			returnTypePane.setLayout( new FlowLayout(FlowLayout.LEFT,1,1) );
      vegPlotReturnTypeSelector.setAlignmentX(0.3F);
      vegPlotReturnTypeSelector.setText("vegetation plots");
			vegPlotReturnTypeSelector.setAlignmentX(LEFT_ALIGNMENT);
			
			vegCommunityReturnTypeSelector.setAlignmentX(0.3F);
      vegCommunityReturnTypeSelector.setText("vegetation community");
			vegCommunityReturnTypeSelector.setAlignmentX(LEFT_ALIGNMENT);
			
			plantTaxaReturnTypeSelector.setAlignmentX(0.3F);
      plantTaxaReturnTypeSelector.setText("plant Taxonomy");
			plantTaxaReturnTypeSelector.setAlignmentX(LEFT_ALIGNMENT);
			
			//add the choices to the returnTypePane
			returnTypePane.setAlignmentX(RIGHT_ALIGNMENT);
      returnTypePane.add(vegPlotReturnTypeSelector);
			returnTypePane.add(vegCommunityReturnTypeSelector);
			returnTypePane.add(plantTaxaReturnTypeSelector);
			returnTypePane.setBorder(BorderFactory.createEmptyBorder(22,2,2,22));
			
			//the updated nested query text
			updatedNestedQueryPane.setLayout(new BoxLayout(updatedNestedQueryPane, BoxLayout.X_AXIS));
			//updatedNestedQueryPane.setLayout( new FlowLayout(FlowLayout.LEFT,1,1) );
			updatedNestedQueryText.setText("taxonName equals Fraxinus dipetala");
    	updatedNestedQueryText.setColumns(100);
		//	updatedNestedQueryText.setBorder(BorderFactory.createEmptyBorder(200,2,2,200));
			updatedNestedQueryText.setBackground(java.awt.Color.pink);
			
			//put the text area in a scroll pane
			JScrollPane  jScrollPane3 = new javax.swing.JScrollPane();
			jScrollPane3.setBounds(20, 20, 40, 60);
			jScrollPane3.setViewportView(updatedNestedQueryText);
			updatedNestedQueryPane.setPreferredSize(new Dimension(400, 40));
			updatedNestedQueryPane.setMinimumSize(new Dimension(400, 40));
			updatedNestedQueryPane.setAlignmentX(CENTER_ALIGNMENT);  //don't know why!
			updatedNestedQueryPane.add( Box.createHorizontalGlue());
			updatedNestedQueryPane.add(jScrollPane3);
			
			
			//the activation pane -- contains the buttons to run app
			appendQueryActivator.setText("Append Query");
			//add the append query Action Listener
			appendQueryActivator.addActionListener(new java.awt.event.ActionListener() 
			{
        public void actionPerformed(ActionEvent evt) 
				{
        	handleQueryUpdate(evt);
        }
    	});
			
			runQueryActivator.setText("Issue Query");
			//add the append query Action Listener
			runQueryActivator.addActionListener(new java.awt.event.ActionListener() 
			{
        public void actionPerformed(ActionEvent evt) 
				{
        	handleQuerySubmittal(evt);
        }
    	});
			
			renewQueryActivator.setText("New Query");
			//add the append query Action Listener
			renewQueryActivator.addActionListener(new java.awt.event.ActionListener() 
			{
        public void actionPerformed(ActionEvent evt) 
				{
        	handleQuerySubmittal(evt);
        }
    	});
			appendQueryActivator.setAlignmentX(CENTER_ALIGNMENT);

			
//		appendQueryActivator.setLayout( new FlowLayout(FlowLayout.LEFT,5,5) );
			queryActivationPane.setLayout( new FlowLayout(FlowLayout.LEFT,5,5) );
			queryActivationPane.add(Box.createRigidArea(new Dimension(5,5)));
			queryActivationPane.add( appendQueryActivator);
			queryActivationPane.add( runQueryActivator);
			queryActivationPane.add( renewQueryActivator ); 
			
			
			//aggregate the panes into the main pain
			mainPane.setLayout(new BoxLayout(mainPane, BoxLayout.Y_AXIS));
//			mainPane.add(queryLabelPane);
			mainPane.add(databaseTypePane, BorderLayout.NORTH);
			mainPane.add(queryPane, BorderLayout.NORTH);
			mainPane.add(returnTypePane, BorderLayout.CENTER);
			mainPane.add(updatedNestedQueryPane, BorderLayout.CENTER);
			mainPane.add(Box.createVerticalGlue());
			mainPane.add(queryActivationPane, BorderLayout.SOUTH);
			
			//show that panes
      getContentPane().add(mainPane);
     }
		
		
	
	/**
	 * method to build the aggregate of  queries in a hashtable that should be
	 * continued to be saved until the query is activated and then the parameters
	 * are written to an xml document and issued to the database access module 
	 * for submittal
	 */
	private Vector appendQueryAttributes(Hashtable attributeParameters) 
	{
		Hashtable queryInstance = new Hashtable();
		try 
		{
			//make sure that the correct parameters are passed 
			//into this method
			if (attributeParameters.containsKey("criteria") == true)
			{
				String criteriaValue=attributeParameters.get("criteria").toString();
				String operator = attributeParameters.get("operator").toString();
				String value = attributeParameters.get("value").toString();
				//add these elements to the instance hash
				queryInstance.put("criteria", criteriaValue);
				queryInstance.put("operator", operator);
				queryInstance.put("value", value);
				//add the instance hash to the queryVector
				//that is a static variable in this class
				queryVector.addElement( queryInstance );
				System.out.println("current query: " + queryVector.toString());
			}
			else 
			{
				System.out.println("did not find the correct parameters");
			}
		}
		catch( Exception e ) 
		{
			System.out.println("** failed in:  "
			+e.getMessage());
		}
		return(queryVector);
	}
	
		
		
		/**
		 * method to translate a query criteria token into a 
		 * parameter that can be sent to the 'QueryBuilderServlet'
		 * or 'DataRequestServlet'.  For example the user of the 
		 * client software uses the query criteria 'Taxon Name',
		 * this token will be translated to 'taxonName' inorder
		 * to be used by the servlets
		 *
		 * @param queryCriteria -- the query criteria as used in the 
		 * interface {Plant Taxon, State, Longitude, Latitude, Plot Code,
		 * Geology Slope Aspect, Slope Gradient, Slope Position,  Plot Shape}
		 *
		 */
		private String translateQueryCriteria(String queryCriteria)
		{
			if (queryCriteria == null)
			{
				System.out.println("the query criteria is null!");
			}
			else 
			{
				if ( queryCriteria.trim().equals("Plant Taxon") ) queryCriteria="plantTaxon";
				else if ( queryCriteria.trim().equals("State") ) queryCriteria="state";
				else if ( queryCriteria.trim().equals("Elevation") ) queryCriteria="altValue";
				else if ( queryCriteria.trim().equals("Longitude") ) queryCriteria="plotoriginlat";
				else if ( queryCriteria.trim().equals("Latitude") ) queryCriteria="plotoriginlong";
				else if ( queryCriteria.trim().equals("Plot Code") ) queryCriteria="authorPlotCode";
				else if ( queryCriteria.trim().equals("Geology") ) queryCriteria="surfGeo";
				else if ( queryCriteria.trim().equals("Slope Aspect") ) queryCriteria="slopeAspect";
				else if ( queryCriteria.trim().equals("Slope Position") ) queryCriteria="slopePosition";
				else if ( queryCriteria.trim().equals("Plot Shape") ) queryCriteria="plotShape";
				else System.out.println("Unrecognized query criteria");
			}
			return(queryCriteria);
		}
		
		
		/**
		 * method to translate a query operator token into a 
		 * parameter that can be sent to the 'QueryBuilderServlet'
		 * or 'DataRequestServlet'.  For example the user of the 
		 * client software uses the query criteria 'equals',
		 * this token will be translated to 'eq' inorder
		 * to be used by the servlets
		 *
		 * @param queryOperator -- the query criteria as used in the 
		 * interface {"less than", "greater than", "equals", 
		 *	"begins with", "ends with", "contains"}
		 *
		 */
		private String translateQueryOperator(String queryOperator)
		{
			if (queryOperator == null)
			{
				System.out.println("the query operator is null!");
			}
			else 
			{
				if ( queryOperator.trim().equals("equals") ) queryOperator="eq";
				else if ( queryOperator.trim().equals("greater than") ) queryOperator="gt";
				else if ( queryOperator.trim().equals("less than") ) queryOperator="lt";
				else if ( queryOperator.trim().equals("contains") ) queryOperator="contains";
				else if ( queryOperator.trim().equals("ends with") ) queryOperator="endsWith";
				else if ( queryOperator.trim().equals("begins with") ) queryOperator="beginsWith";
				else System.out.println("Unrecognized query operator");
			}
			return(queryOperator);
		}
		
		
		/**
		 * method to handle the appending of a query instance to
		 * the nested query
		 *
		 */
		 private void handleQueryUpdate(ActionEvent evt) 
			{
				
				//check if local database or remote database
				if (remoteDatabaseTypeSelector.isSelected() == true ) 
				{
        	//this is a test to request data from a servlet at NCEAS
				//	String servlet = "/harris/servlet/QueryBuilderServlet";
				//	String protocol = "http://";
				//	String host = "dev.nceas.ucsb.edu";
					
					String host=(rb.getString("servletHost"));
      		String servlet=(rb.getString("queryBuilderServlet"));
      		String protocol=(rb.getString("servletProtocol"));
				
				 	//get the parameters chosen by the client user
					Hashtable parameterHash = getClientParameters();
				
					//set the required parameters for the remotely hosted servlet
					Properties parameters = new Properties();
					parameters.setProperty("queryParameterType", "append" );
					parameters.setProperty("requestDataType", "vegPlot");
					parameters.setProperty("resultType", "summary" );
					//the query components parameters
					parameters.setProperty("criteria",  translateQueryCriteria(
						parameterHash.get("criteria").toString()) );
					parameters.setProperty("operator",   translateQueryOperator(
						parameterHash.get("operator").toString()) );
					parameters.setProperty("value",  parameterHash.get("value").toString());
					
					//test to get the parameters chosen by the client user
					System.out.println("PARAMETERS: "+getClientParameters().toString());
					
					//connect to the servlet thru the 'DataRequestClient' and update the
					// 'updatedNestedQueryText'
					String returnedData= ( drc.requestURL(servlet, protocol, host, parameters) );
					updatedNestedQueryText.setText(returnedData);
				}
				else if(localDatabaseTypeSelector.isSelected() == true )
				{
					//make a hashtbable to store these parameters
					//get the parameters chosen by the client user
					Hashtable parameterHash = getClientParameters();
					
					//now translate these parameters into the boolean / relational
					// operators that need to be passed to the db access module
					Hashtable translatedParameters = new Hashtable();
					translatedParameters.put( "criteria",  translateQueryCriteria(
						parameterHash.get("criteria").toString() ) );
					//the operator
					translatedParameters.put( "operator",  translateQueryOperator(
						parameterHash.get("operator").toString() ) );
					// the value -- no need for translation
					translatedParameters.put( "value",  parameterHash.get("value").toString()  );
					
					//append these criteria to the static vector that stores the query
					appendQueryAttributes(translatedParameters);
					System.out.println("passed the parameters to update local query");
				}
				else 
				{
					System.out.println("Please select the database type {remote, local}");
				}
			}
		
		/**
		 * method that returns all the parameters chosen on the 
		 * graphical interface the time that the method is being 
		 * called
		 */
		private Hashtable getClientParameters()
		{
			Hashtable chosenParameters = new Hashtable();
			//get the query criteria elements
			chosenParameters.put("criteria", queryCriteriaList.getSelectedValue().toString().trim());
			chosenParameters.put("operator", queryOperatorList.getSelectedValue().toString().trim());
			chosenParameters.put("value", queryValue.getText().trim());
			return(chosenParameters); 
		}
		
    /**
		 * method to submit the query the nested query
		 * to the database.  The nested query was composed
		 * in the 'handleQueryUpdate' method and 
		 */
		 private void handleQuerySubmittal(ActionEvent evt) 
			{
					//check if local database or remote database
				if (remoteDatabaseTypeSelector.isSelected() == true ) 
				{
        	//this is a test to request data from a servlet at NCEAS
	//				String servlet = "/harris/servlet/QueryBuilderServlet";
	//				String protocol = "http://";
	//				String host = "dev.nceas.ucsb.edu";
		
					String host=(rb.getString("servletHost"));
      		String servlet=(rb.getString("queryBuilderServlet"));
      		String protocol=(rb.getString("servletProtocol"));
					
					//set the required parameters for the remotely hosted servlet
					Properties parameters = new Properties();
					parameters.setProperty("clientType", "clientApplication");
					parameters.setProperty("requestDataFormatType", "xml");
					parameters.setProperty("queryParameterType", "commit");
					parameters.setProperty("requestDataType", "vegPlot");
					parameters.setProperty("resultType", "summary");
					
					//connect to the servlet thru the 'DataRequestClient' grab the results
					//and pass the results to the viewer
				
					//write the results to the local file system
					drc.writeURLResponseToFile(servlet, protocol, host, parameters, "summary.xml");
					
					//	String returnedData=( drc.requestURL(servlet, protocol, host, parameters) );
      		hv.show();
      		hv.showData("summary.xml",  "transformMultiPlotSummary.xsl", "text/html");  
					
				}
				//if the local database
				else if(localDatabaseTypeSelector.isSelected() == true )
				{
					System.out.println("submitting the local query vector");
					//pass the query vector to the DataRequestHandler that will issue the
					// query and get the results
					int numberOfPlots =	requestHandler.handleExtendedQuery(queryVector);
					//make a new instance of the queryVector
					queryVector = new Vector();
					//access the summary viewer if enough plots to warrant it
					if ( numberOfPlots > 0)
					{
						//hv.show();
      			//hv.showData("summary.xml",  "autoGenerate.xsl", "text/html");  
						StyleSheetInterface stylesheet = new StyleSheetInterface();
						stylesheet.show();
					}
					else
					{
						ProjectManager.debug(0, "number of plots: "+numberOfPlots );
					}
					
				}
				else 
				{
					System.out.println("Please select the database type {remote, local}");
				}
			}
		
		/**
		 * method that may be called by another class to start a 
		 * new instance of this class.  Added an action string so
		 * that the in the future the frame can be customized based
		 * on some input
		 */
		public void launchQueryModule(String action)
		{
			System.out.println("starting");
			new NestedQueryBuilder ().show ();
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
			System.out.println("Starting vegetation query module");
        new NestedQueryBuilder ().show ();
    }
	}
