package databaseAccess;

import java.lang.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.sql.*;

import databaseAccess.*;

/**
 * This class acts as a store for sql queries which are used depending on the 
 * number and type of query elements passed to the sqlMapper class.  Ultimately
 * these queries are issued to the database though the issue select method.
 * This class stores queries and query generation mechanisms exclusively for
 * querying the community database 
 *
 * @author John Harris
 *
 */

public class  CommunityQueryStore
 {

	public Vector communitySummaryOutput = new Vector();
	private String communityName = null;
	private String communityLevel = null;
	private String action = null; //sql action (select, insert)
	private StringBuffer statement = new StringBuffer(); //sql statement


 
	/**
 	* Method that takes as input queryelements such as communityLevel (the level in
 	* the NVC heirarchy eg. class, subclass, group etc.) and communityName
 	*/
	public void getCommunitySummary(String communityName, String communityLevel )
	{
		try
		{
		Connection conn = this.getConnection();
		
		action = "select"; 
		statement.append("select ");
		statement.append("commName, abiCode, commDescription, parentCommName, "
		+" dateEntered, classCode, classLevel, conceptOriginDate, conceptUpdateDate, "
		+" parentAbiCode, recognizingParty, partyConceptStatus,  " 
		+" parentCommDescription, commSummary_Id ");
		statement.append("from commSummary where commName like '");
		statement.append(communityName+"'");
		statement.append("and classLevel like '"+communityLevel+"'");

		String returnFields[]=new String[14];
		int returnFieldLength=14;
		
		
		

		System.out.println("CommunityQueryStore > "+ statement.toString()  );
		
		PreparedStatement pstmt;
      
		//create the accession number
		//PreparedStatement pstmt;
    pstmt = conn.prepareStatement( statement.toString()  );
                
    pstmt.execute();
    ResultSet rs = pstmt.getResultSet();
		

		while (rs.next()) 
		{
			System.out.println("CommunityQueryStore > 	new record "  );
			
			//put the name values into the serial string buffer
			StringBuffer serial = new StringBuffer();
			
			String commName = rs.getString(1);
			serial.append(commName+"|");
			String abiCode = rs.getString(2);
			serial.append(abiCode+"|");

			String commDescription = rs.getString(3);
			serial.append(commDescription+"|");
			String parentCommName = rs.getString(4);
			serial.append(parentCommName+"|");
			String dateEntered = rs.getString(5);
			serial.append(dateEntered+"|");
			String classCode = rs.getString(6);
			serial.append(classCode+"|");
			String classLevel = rs.getString(7);
			serial.append(classLevel+"|");
			String conceptOriginDate = rs.getString(8);
			serial.append(conceptOriginDate+"|");
			String conceptUpdateDate = rs.getString(9);
			serial.append(conceptUpdateDate+"|");
			String parentAbiCode = rs.getString(10);
			serial.append(parentAbiCode+"|");
			String recognizingParty = rs.getString(11);
			serial.append(recognizingParty+"|");
			String partyConceptStatus = rs.getString(12);
			serial.append(partyConceptStatus+"|");
			String parentCommDescription = rs.getString(13);
			serial.append(parentCommDescription+"|");
			String commSummary_Id = rs.getString(14);
			serial.append(commSummary_Id+"|");
		
				System.out.println("CommunityQueryStore > commName: "+ commName   );
				System.out.println("CommunityQueryStore > abiCode: "+ abiCode   );
				System.out.println("CommunityQueryStore > commDescription: "+commDescription    );
				System.out.println("CommunityQueryStore > parentCommName: "+ parentCommName    );
				System.out.println("CommunityQueryStore > dateEntered: "+ dateEntered    );
				System.out.println("CommunityQueryStore > classCode : "+  classCode  );
				System.out.println("CommunityQueryStore > classLevel: "+ classLevel   );
				System.out.println("CommunityQueryStore > conceptOriginDate: "+ conceptOriginDate    );
				System.out.println("CommunityQueryStore > conceptUpdateDate : "+ conceptUpdateDate    );
				System.out.println("CommunityQueryStore > recognizingParty: "+ recognizingParty    );
				System.out.println("CommunityQueryStore > partyConceptStatus : "+ partyConceptStatus    );
				System.out.println("CommunityQueryStore > parentCommDescription: "+ parentCommDescription    );
				System.out.println("CommunityQueryStore > commSummary_Id : "+  commSummary_Id   );
			
			System.out.println("CommunityQueryStore > serial string: "+ serial.toString()  );
			communitySummaryOutput.addElement(serial.toString() );
		}
		
		//issue the statement
///		issueStatement j = new issueStatement();
///		j.issueSelect(statement.toString(), action, returnFields, returnFieldLength);

		//pass the results back to the calling class
///		communitySummaryOutput =j.returnedValues;  //copy this vector


		System.out.println("CommunityQueryStore > vector size: "+ communitySummaryOutput.size() );
		//System.out.println( j.returnedValues.toString());
		}
		catch(Exception e)
		{
			System.out.println("CommunityQueryStore > exception: " + e.getMessage() );
			e.printStackTrace();
			
		}
	}
	
	
	/**
	 * method that will return a database connection for use with the database
	*/
	private Connection getConnection()
	{
		Connection c = null;
		try 
 		{
			Class.forName("org.postgresql.Driver");
			//the community database
			c = DriverManager.getConnection("jdbc:postgresql://dev.nceas.ucsb.edu/nvc", "datauser", "");
		}
		catch ( Exception e )
		{
			System.out.println("CommunityQueryStore > exception: "
			+"dbConnect.makeConnection: "+e.getMessage());
			e.printStackTrace();
		}
			return(c);
	}
	
	
}
