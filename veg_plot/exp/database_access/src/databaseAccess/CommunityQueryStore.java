package databaseAccess;

/**
 *  '$RCSfile: CommunityQueryStore.java,v $'
 *    Authors: @authors@
 *    Release: @release@
 *
 * '$Author: farrell $'
 * '$Date: 2003-05-16 03:33:34 $'
 * '$Revision: 1.4 $'
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Hashtable;
import java.util.ResourceBundle;
import java.util.Vector;

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
	private String dbConnectString = ""; // the db connect string
	private ResourceBundle rb = ResourceBundle.getBundle("database");

	/**
	 * method that will return a database connection for use with the database
	 */
	private Connection getConnection()
	{
		Connection c = null;
		try 
 		{
			this.dbConnectString = rb.getString("connectString");
			String driverClass = rb.getString("driverClass");
			String user = rb.getString("user");
			String password = rb.getString("password");
			
			Class.forName(driverClass);
			System.out.println("CommunityQueryStore > db connect string: " + dbConnectString);
			c = DriverManager.getConnection(dbConnectString, user, password);
		}
		catch ( Exception e )
		{
			System.out.println("CommunityQueryStore > exception: " + e.getMessage());
			e.printStackTrace();
		}
		return(c);
	}
 
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
			StringBuffer sqlStatement = new StringBuffer(
				"select commusage.commname, commname.dateentered, commstatus.commlevel, "				+ "commconcept.conceptdescription, commstatus.startdate, commusage.classsystem, "
				+ "commconcept.commconcept_id, commstatus.commconceptstatus from commusage, "				+ "commstatus, commconcept, commname " 				+ "where commconcept.commname_id = commname.commname_id and "
				+ "commstatus.commconcept_id = commconcept.commconcept_id and "
				+ "commusage.commconcept_id = commconcept.commconcept_id "
			);
			
			sqlStatement.append(" and upper(commusage.commname) like " + "'" + communityName.toUpperCase()+"'");
			sqlStatement.append(" and commstatus.commlevel like '"+communityLevel+"'");
			
			System.out.println("CommunityQueryStore > "+ statement.toString()  );
			PreparedStatement pstmt;
    	pstmt = conn.prepareStatement( sqlStatement.toString()  );
                
    	pstmt.execute();
    	ResultSet rs = pstmt.getResultSet();
		

			while (rs.next()) 
			{
				System.out.println("CommunityQueryStore > 	new record "  );
				//put the name values into the serial string buffer
				StringBuffer serial = new StringBuffer();
				
				String commname = rs.getString(1);
				serial.append(commname+"|");
				String  dateentered = rs.getString(2);
				serial.append(dateentered+"|");
				String classlevel = rs.getString(3);
				serial.append(classlevel+"|");
				String commdescription = rs.getString(4);
				serial.append(commdescription+"|");
				String conceptorigindate = rs.getString(5);
				serial.append(conceptorigindate+"|");
				String commconceptid = rs.getString(6);
				serial.append(commconceptid+"|");
				String partyconceptstatus = rs.getString(7);
				serial.append(partyconceptstatus+"|");
				
				System.out.println("CommunityQueryStore > commname: "+ commname   );
				System.out.println("CommunityQueryStore > dateentered: "+ dateentered   );
				System.out.println("CommunityQueryStore > classlevel: "+ classlevel    );
				System.out.println("CommunityQueryStore > commdescription: "+ commdescription    );
				System.out.println("CommunityQueryStore > conceptorigindate: "+ conceptorigindate   );
				System.out.println("CommunityQueryStore > commconceptid: "+ commconceptid    );
				System.out.println("CommunityQueryStore > partyconceptstatus: "+ partyconceptstatus );
				
				//System.out.println("CommunityQueryStore > serial string: "+ serial.toString()  );
				communitySummaryOutput.addElement(serial.toString() );
			}

			System.out.println("CommunityQueryStore > vector size: "+ communitySummaryOutput.size() );
			//System.out.println( j.returnedValues.toString());
		}
		catch(Exception e)
		{
			System.out.println("CommunityQueryStore > exception: " + e.getMessage() );
			e.printStackTrace();
			
		}
	}

	//get a vector that contains hashtables with all the possible 
	//correlation ( name, recognizing party, system, status, level)
	//as input use the authors and the commname
	public Vector getCorrelationTargets(
		String commName, 
		String nameRefAuthors, 
		String classLevel)
	{
		Vector v = new Vector();
		try
		{
			Connection conn = this.getConnection();
			
//			statement.append("select ");
//			statement.append("commName, recognizingParty, partyConceptStatus, classLevel,  ");
//			statement.append(" commconcept_id, usage_id ");
//			statement.append("from commSummary where upper(commName) like '");
//			statement.append(commName.toUpperCase()+"' and upper(nameRefAuthors) ");
//			statement.append(" like '"+nameRefAuthors.toUpperCase()+"' and ");
//			statement.append(" upper(classlevel) like '"+classLevel.toUpperCase()+"'");
			//gather the responses
			statement.append(
				"select commname.commname, commstatus.commconceptstatus, "
				+ " commstatus.commlevel, commconcept.commconcept_id,  "
				+ "commusage.commusage_id from commname, commstatus, commusage"
				+ "commconcept  where commstatus.commconcept_id = commconcept.commconcept_id"
				+ " and commname.commname_id = commusage.commname_id  "
				+ " and upper(commname.commname) like '" + commName.toUpperCase() +"'"
				+ " and upper(commstatus.classlevel) like '"+classLevel.toUpperCase()+"'"
				);
			
			System.out.println("CommunityQueryStore > "+ statement.toString()  );
			PreparedStatement pstmt;
    	pstmt = conn.prepareStatement( statement.toString()  );            
    	pstmt.execute();
    	ResultSet rs = pstmt.getResultSet();
			
			String buf = null;
			while (rs.next()) 
			{
				Hashtable hash = new Hashtable();
				System.out.println("CommunityQueryStore > record: "   );
				
				buf = rs.getString(1);
				hash.put("commName", ""+buf);
				//buf = rs.getString(2);
				//hash.put("recognizingParty", ""+buf);
				buf = rs.getString(2);
				hash.put("conceptStatus", ""+buf);
				buf = rs.getString(3);
				hash.put("level", ""+buf);
				buf = rs.getString(4);
				hash.put("commConceptId", ""+buf);
				buf = rs.getString(6);
				hash.put("usageId", ""+buf);
				//put the hash into the vector
				v.addElement(hash);
				
			}
			
			pstmt.close();
			conn.close();
		}
		catch(Exception e)
		{
			System.out.println("CommunityQueryStore > exception: " + e.getMessage() );
			e.printStackTrace();
		}
		return(v);
	}
						
	
	/**
	 * this method will return the name reference information based 
	 * soley on a community name
	 * 
	 * @param commName -- the community name
	 */
	public Hashtable getNameReference(String commName)
	{
		Hashtable nameRef = new Hashtable();
		try
		{
			System.out.println("CommunityQueryStore > looking up name ref for: " + commName);
			Connection conn = this.getConnection();
			
			statement.append("select ");
			statement.append("title, authors, pubDate, edition, seriesName, issueIdentification,  ");
			statement.append("otherCitationDetails, page, isbn, issn  ");
			statement.append("from commReference where commreference_id = ");
			statement.append(" (select commreference_id from commName where upper(commName)   ");
			statement.append(" like '"+commName.toUpperCase()+"')");
			//gather the responses
			System.out.println("CommunityQueryStore > "+ statement.toString()  );
			PreparedStatement pstmt;
    	pstmt = conn.prepareStatement( statement.toString()  );            
    	pstmt.execute();
    	ResultSet rs = pstmt.getResultSet();
			
			String buf = null;
			while (rs.next()) 
			{
				buf = rs.getString(1);
				nameRef.put("title", ""+buf);
				buf = rs.getString(2);
				nameRef.put("authors", ""+buf);
				System.out.println("CommunityQueryStore > found authors:  " + buf  );
				buf = rs.getString(3);
				nameRef.put("pubDate", ""+buf);
				buf = rs.getString(4);
				nameRef.put("edition", ""+buf);
				buf = rs.getString(5);
				nameRef.put("seriesName", ""+buf);
				buf = rs.getString(6);
				nameRef.put("issueId", ""+buf);
				buf = rs.getString(7);
				nameRef.put("otherCitationDetails", ""+buf);
				buf = rs.getString(8);
				nameRef.put("page", ""+buf);
				buf = rs.getString(9);
				nameRef.put("isbn", ""+buf);
				buf = rs.getString(10);
				nameRef.put("issn", ""+buf);
			}
			
			pstmt.close();
			conn.close();
			
		}
		catch(Exception e)
		{
			System.out.println("CommunityQueryStore > exception: " + e.getMessage() );
			e.printStackTrace();
		}
		return(nameRef);
	}
	
	
	
	
	public Vector getCommunityNames(String commName)
	{
		Vector names = new Vector();
		try
		{
			Connection conn = this.getConnection();
			
			statement.append("select ");
			statement.append("commName ");
			//MAKE ALL QUERIES CASE INSENSITIVE
			statement.append("from commname where upper(commName) like '");
			statement.append(commName.toUpperCase()+"'");
			//gather the responses
			System.out.println("CommunityQueryStore > "+ statement.toString()  );
			PreparedStatement pstmt;
    	pstmt = conn.prepareStatement( statement.toString()  );            
    	pstmt.execute();
    	ResultSet rs = pstmt.getResultSet();
		
			while (rs.next()) 
			{
				System.out.println("CommunityQueryStore > 	new record "  );
				String buf = rs.getString(1);
				names.addElement( buf );
			}
			
			pstmt.close();
			conn.close();
			
		}
		catch(Exception e)
		{
			System.out.println("CommunityQueryStore > exception: " + e.getMessage() );
			e.printStackTrace();
		}
		return(names);
	}
	
	
}
