/**
 *  '$RCSfile: EcoartVegCommunitySource.java,v $'
 *
 * '$Author: farrell $'     
 * '$Date: 2003-03-20 19:57:37 $' 
 * '$Revision: 1.16 $'
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

//package edu.ucsb.nceas.vegcommunity;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Vector;
	
	/**
 	 * This class is the basic interface to the vegetation community data 
 	 * stored in the ecoart database and is to be used by the class that loads the
 	 * VegBank veg community database tables  
 	 */
	public class EcoartVegCommunitySource
	{
	private String dbUrl = "jdbc:odbc:ecoart";
	//made the connection public so that the calling class can close it
	//before re-initiilizing this class
	public Connection con = null;
	//the level in the heirarchy that that community is in
	private String level = null;
	
	//below are public variables used to define the 'refererence'
	//and 'party' attributes of ecoart
	public String otherCitationDetails = "Natural Heritage Central Databases. "
	+" NatureServe, Arlington, VA.";
	public String refAuthors = "NatureServe";
	public String refTitle = "International Classification of Ecological "
	+" Communities: Terrestrial Vegetation. EcoArt version 2.65";
	public String refPubDate = "13-FEB-2002";
	public String partyOrgName = "U.S. National Vegetation Classification System";
	
	
	//constructor to create the database connections etc..
	public EcoartVegCommunitySource()
	{
			try 
		{
			// Load the sun jdbc-odbc bridge driver
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");

			// connect to the jdbc-odbc bridge driver
			con = DriverManager.getConnection(dbUrl, "user", "pass");

			// Get the DatabaseMetaData object and display
			// some information about the connection
			DatabaseMetaData dma = con.getMetaData();

			System.out.println("EcoartVegCommunitySource > Connected to " + dma.getURL() );
			System.out.println("EcoartVegCommunitySource > Driver       " + dma.getDriverName() );
			System.out.println("EcoartVegCommunitySource > Version      " + dma.getDriverVersion() );
			System.out.println("EcoartVegCommunitySource > Catalog      " + con.getCatalog() );
			
		}
		catch (SQLException ex) 
		{
			// Error, a SQLException was generated. Display the error information
			System.out.println ("<BR><B>*** SQLException caught ***</B><BR>");
			while (ex != null)
			{
				System.out.println ("EcoartVegCommunitySource > ErrorCode: " + ex.getErrorCode () + "<BR>");
				System.out.println ("EcoartVegCommunitySource > SQLState:  " + ex.getSQLState () + "<BR>");
				System.out.println ("EcoartVegCommunitySource > Message:   " + ex.getMessage () + "<BR>");
				ex = ex.getNextException();
			}
		}
		catch (java.lang.Exception ex) 
		{   // All other types of exceptions
			System.out.println("EcoartVegCommunitySource > Exception: " + ex );
		}
	}

	/**
 	 * method that takes a community code, in this case the ecoart code and
	 * returns the community name that is associated with the ecoart code.
	 * specifically there is an 'Elcode' that is associated with the 
	 * community Associations, each code represents a unique community concept
	 * and thus by using these codes the attributes realeted to the community
	 * entity can be returned to the calling method
	 *
	 * @param community -- the ecoart community code, can be an alliance code or a
	 * 	elcode
	 * @return commName -- the name of the community
	 */
	 public String getCommunityName(String community)
	 {
		 String commName = null;
		 try
		 {
			 //first figure out the level in the heirarcy
			 this.level = this.getCommunityLevel(community);
			 //System.out.println("EcoartVegCommunity > internal level: " + level); 
			// Create a Statement so we can submit SQL statements to the driver
			Statement stmt = con.createStatement();
			String query = null;
			
			if (level.equals("association") )
			{
				query = "select ([Gname]) from ETC where ([Elcode]) = '"+community+"'";
			}
			else if (level.equals("alliance") )
			{
				query = "select ([AllianceName]) from Alliance where ([AllianceKey]) = '"+community+"'";
			}
			else if (level.equals("formation") )
			{
				query = "select ([FormationName]) from Formation where ([FormationKey]) = '"+community+"'";
			}
			else if (level.equals("subgroup") )
			{
				query = "select ([Subgroupname]) from subgroup where ([subgroupKey]) = '"+community+"'";
			}
			else if (level.equals("group") )
			{
				query = "select ([Groupname]) from group_ where ([GroupKey]) = '"+community+"'";
			}
			else if (level.equals("subclass") )
			{
				query = "select ([SubClassname]) from subclass where ([subclassKey]) = '"+community+"'";
			}
			else if (level.equals("class") )
			{
				query = "select ([Classname]) from class where ([classKey]) = '"+community+"'";
			}
			
			//execute the query
			if ( query != null )
			{
				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) 
				{
					commName = rs.getString(1);
				}
				rs.close();
				stmt.close();
			}
		 }
		 catch (Exception e)
		 {
			 System.out.println("EcoartVegCommunity > Exception: " + e.getMessage() );
			 System.out.println("EcoartVegCommunity > Exception on community: '" 
			 + community+"'" );
			 e.printStackTrace();
		 }
		 return(commName);
	 }
	 
	  /**
	 * public method that retuns the create date for a community
		*
		* @param communityCode -- the alliance code
		* @return allianceNameTrans -- the common name
		*/
		public String getAllianceNameTrans(String code)
		{
			String allianceNameTrans = null;
			//first figure out the level in the heirarcy
			this.level = this.getCommunityLevel(code);
			
			//if NOT alliance
			if ( ! level.equals("alliance"))
			{
				return null;
			}
			try
			{
				// Create a Statement so we can submit SQL statements to the driver
				Statement stmt = con.createStatement();
				String query = null;
				query = "select ([alliancenametrans]) from alliance where ([alliancekey]) = '"+code+"'";
				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) 
				{
					//trim out the faulty characters
					String s =  rs.getString(1);
				}
				rs.close();
				stmt.close();
			}
			catch (Exception e)
			{
				 System.out.println("EcoartVegCommunity > Exception: " + e.getMessage() );
				 e.printStackTrace();
			}
			return(allianceNameTrans);
		}
		
		
	 
		
	 
	 /**
 	 * method that takes a community code, in this case the ecoart code and
	 * returns the community 'common' name that is associated with the ecoart code.
	 * specifically there is an 'Elcode' that is associated with the 
	 * community Associations, each code represents a unique community concept
	 * and thus by using these codes the attributes realeted to the community
	 * entity can be returned to the calling method
	 *
	 * @param community -- the ecoart community code, can be an alliance code or a
	 * 	elcode
	 * @return commName -- the name of the community
	 */
	 private String getCommunityCommonName(String community)
	 {
		 String commName = null;
		 try
		 {
			 
			 
			//first figure out the level in the heirarcy
			this.level = this.getCommunityLevel(community);
			//System.out.println("EcoartVegCommunity > internal level: " + level); 
			// Create a Statement so we can submit SQL statements to the driver
			Statement stmt = con.createStatement();
			String query = null;
			if (level.equals("association") )
			{
				query = "select ([GnameTrans]) from ETC where ([Elcode]) = '"+community+"'";
			}
			else if (level.equals("alliance") )
			{
				query = "select ([AllianceNameTrans]) from Alliance where ([AllianceKey]) = '"+community+"'";
			}
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) 
			{
				commName = rs.getString(1);
			}
		 }
		 catch (Exception e)
		 {
			 System.out.println("EcoartVegCommunity > Exception: " + e.getMessage() );
			 e.printStackTrace();
		 }
		 return(commName);
	 }
	 
	 
	 /**
	  * method to return the date entered
		*/
		public String getDateEntered(String communityCode, String level)
		{
			String dateEntered = null;
			try
			{
				//System.out.println("EcoartVegCommunity > level: " + level);
				// Create a Statement so we can submit SQL statements to the driver
				Statement stmt = con.createStatement();
				String query = null;
				if ( level.equals("alliance") )
				{
					query = "select ([AllianceOriginDate]) from ALLIANCE where ([AllianceKey]) = '"+communityCode+"'";
				}
				else if ( level.equals("association"))
				{
					query = "select ([AssocOriginDate]) from ETC where ([Elcode]) = '"+communityCode+"'";
				}
				else if  ( level.equals("formation"))
				{
					query = "select ([Update]) from Formation where ([FormationKey]) = '"+communityCode+"'";
				}
				else if  ( level.equals("subgroup"))
				{
					query = "select ([Update]) from subgroup where ([subgroupKey]) = '"+communityCode+"'";
				}
				
				else if  ( level.equals("group"))
				{
					query = "select ([Update]) from group_ where ([groupKey]) = '"+communityCode+"'";
				}
				else if  ( level.equals("subclass"))
				{
					query = "select ([Update]) from subclass where ([subclassKey]) = '"+communityCode+"'";
				}
				else if  ( level.equals("class"))
				{
					query = "select ([Update]) from class where ([classKey]) = '"+communityCode+"'";
				}
				//System.out.println("EcoartVegCommunity > query: " + query);
				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) 
				{
					dateEntered = rs.getString(1);
				}
				rs.close();
				stmt.close();
			}
			 catch (Exception e)
		 {
			 System.out.println("EcoartVegCommunity > Exception: " + e.getMessage() );
			 e.printStackTrace();
		 }
		 //System.out.println("EcoartVegCommunity > date: " + dateEntered);
		 return(dateEntered);
		}
	 
	 
	 
 	 /** method that returns the community codes for the entire ecoart 
 		* database
 		*
 		* @return communities -- a vector containing all the communities
		*/
	 public Vector getCommunityCodes(String level)
	 {
	
		//System.out.println("EcoartVegCommunity > level: " + level);
	 	Vector queries = new Vector();
	 	if (level.equals("association"))
	 	{
	 		// Associations come from several tables in the the datasource !!!
			queries.addElement("select ([Elcode]) from ETC");
		}
	 	else if (level.equals("alliance"))
		{
			//query = "select distinct ([AllianceKey]) from Alliance";
		 	queries.addElement("select distinct ([AllianceKey]) from Alliance");
		}
		else if (level.equals("formation"))
		{
			queries.addElement("select distinct([FormationKey]) from Formation");
		}
		else if (level.equals("subgroup"))
		{
			queries.addElement("select distinct([SubgroupKey]) from Subgroup");
		}
		else if (level.equals("group"))
		{
			queries.addElement("select distinct([GroupKey]) from Group_ ");
		}
		else if (level.equals("subclass"))
		{
			queries.addElement("select distinct([SubClassKey]) from subclass ");
		}
		else if (level.equals("class"))
		{
			queries.addElement("select distinct([ClassKey]) from Class ");
		}
				 
		Vector communities = this.getCommunityCodes(queries);		 
		return(communities);
	 }
	
	 /** 
	  * Returns all the results of a set of queries as a Vector
	  * of Strings.
	  * Filters out some of the results ( CVE, COMPLEX and CECX)
	  * 
		* @param querys --  vector of sql queries to be run
		* @return community codes -- a vector containing code found by the queries
		*/ 
	private Vector getCommunityCodes (Vector querys)
	{	 	
		Vector results = new Vector();
		Enumeration queryEnum = querys.elements();
		while(queryEnum.hasMoreElements())
		{
			String query =  (String) queryEnum.nextElement();
			try
			{
				System.out.println("We have '" + query + "'");
	
				Statement stmt = con.createStatement();
				//create the result set
				ResultSet rs = stmt.executeQuery(query);
				while (rs.next())
				{
					String code = rs.getString(1);
					//System.out.println("EcoartVegCommunity > resp: " + resp );
	
					// Filter out CAVE & COMPLEX rows
					if (code.startsWith("CAVE")
						|| code.startsWith("COMPLEX")
						|| code.startsWith("CECX"))
					{
						// Do not add to communities vector
					}
					else
					{
						results.addElement(code);
					}
				}
	
				rs.close();
				stmt.close();
			}
			catch (Exception e)
			{
				System.out.println("EcoartVegCommunity > Exception: " + e.getMessage());
				e.printStackTrace();
			}
		}
		return(results);
	}
	
	 /**
		* method that returns the community level in the heirarchy based on 
		* an input code
		* @param code -- the ecoart code for the community
		* @return level -- the level of the community in the heirarchy and may 
		* 	include the association, alliance etc..
		*/
	private String getCommunityLevel(String community)
 	{
		if ( community.startsWith("CEG") )
		{
			return("association");
		}
		else if (community.startsWith("A.") )
		{
			return("alliance");
		}
			else if (community.startsWith("F.") )
		{
			return("formation");
		}
		else if (community.startsWith("SG.") )
		{
			return("subgroup");
		}
		else if (community.startsWith("G.") )
		{
			return("group");
		}
		else if (community.startsWith("SC.") )
		{
			return("subclass");
		}
		else if (community.startsWith("C.") )
		{
			return("class");
		}
		else
		{
			return(null);
		}
	}
	 
	 
	 /**
		* method that returns the code of the parent community, eq, the association 
		* has a parent, an alliance, so this method passes back the alliance key
		* if a 'elcode' is passed to this method as a parameter
		* @param community -- the ecoart code
		* @return parentCode -- the parent code 
		*/
	public String getParentCode(String community)
	 {
		 String parentCode = null;
		 try
		 {
			 //first figure out the level in the heirarcy
			this.level = this.getCommunityLevel(community);
			
			// Create a Statement so we can submit SQL statements to the driver
			Statement stmt = con.createStatement();
			//System.out.println("EcoartVegCommunity > internal level: " + level);
			String query = null;
			
			if ( level.equals("association") )
			{
				query = "select ([ClassifKey]) from ETC where ([Elcode]) = '"+community+"'" ;
			}	
			else if ( level.equals("alliance") )
			{
				query = "select ([FormationKey]) from Alliance where ([AllianceKey]) = '"+community+"'" ;
			}
			else if ( level.equals("formation") )
			{
				query = "select ([SubGroupKey]) from Formation where ([FormationKey]) = '"+community+"'" ;
			}			
			else if ( level.equals("subgroup") )
			{
				query = "select ([GroupKey]) from subGroup where ([SubGroupKey]) = '"+community+"'" ;
			}			
			else if ( level.equals("group") )
			{
				query = "select ([SubclassKey]) from Group_ where ([GroupKey]) = '"+community+"'" ;
			}
			else if ( level.equals("subclass") )
			{
				query = "select ([classKey]) from  subclass where ([subclassKey]) = '"+community+"'" ;
			}
			else if ( level.equals("class") )
			{
				return null; // class is the top of the tree ... no parent
			}
			else
			{
				// Should throw exception here
				System.out.println("EcoartVegCommunity > Invalid level: " + level );
			}
				
			//execute the query
			if ( query != null)
			{
				//create the result set
				ResultSet rs = stmt.executeQuery( query);
				while (rs.next()) 
				{
					parentCode = rs.getString(1);
				}
				rs.close();
				stmt.close();
			}
			else
			{
				return(null);  
			}
			
		 }
		 catch (Exception e)
		 {
			 System.out.println("EcoartVegCommunity > Exception: " + e.getMessage() );
			 e.printStackTrace();
		 }
		 return(parentCode);
	 }
	 
	 
	 /**
	  * Get community description from the datasource given the communityCode
	  * 
	  * @param String communityCode
	  * @return String communityDescription
	  */
	 public String getCommunityDescription( String communityCode )
	 {
		String communityDescription = null;
		try
		{
			//first figure out the level in the heirarcy
		 this.level = this.getCommunityLevel(communityCode);
			
		 // Create a Statement so we can submit SQL statements to the driver
		 Statement stmt = con.createStatement();
		 //System.out.println("EcoartVegCommunity > internal level: " + level);
		 String query = null;
			
		 if ( level.equals("alliance") )
		 {
			 query = "select ([AllianceDesc]) from Alliance where ([AllianceKey]) = '"+communityCode+"'" ;
		 }
		 else if (level.equals("subclass") )
		 {
			 query = "select ([SubclassDesc]) from subclass where ([subclassKey]) = '"+communityCode+"'";
		 }
		 else if (level.equals("class"))
		 {
			query = "select ([ClassDesc]) from class where ([classKey]) = '"+communityCode+"'" ;
		 }
		 // The other level types don't have a real description -- use null
		 else
		 {
				communityDescription =	null;
		 }		
		 
		 //execute the query
		 if ( query != null)
		 {
			 //create the result set
			 ResultSet rs = stmt.executeQuery( query);
			 while (rs.next()) 
			 {
				communityDescription = rs.getString(1);
			 }
			 rs.close();
			 stmt.close();
		 }
		 
		}
		catch (Exception e)
		{
			System.out.println("EcoartVegCommunity > Exception: " + e.getMessage() );
			e.printStackTrace();
		}
		return(communityDescription);			
	 }
	 
	 /**
	  * main method for testing this code
		*/
		public static void main(String[] args)
		{
			try
			{
				EcoartVegCommunitySource source = new EcoartVegCommunitySource();
				//print the number of codes
				
				System.out.println("EcoartVegCommunitySource > number of alliances: " 
				+ source.getCommunityCodes("alliance").size()  );
				
				System.out.println("EcoartVegCommunitySource > number of association: " 
				+ source.getCommunityCodes("association").size()  );
				
				String ecoartCode = "A.101";
				System.out.println("EcoartVegCommunitySource > alliance trans: " 
				+ source.getAllianceNameTrans(ecoartCode)  );
		}
			catch (Exception e)
		 {
			 System.out.println("EcoartVegCommunity > Exception: " + e.getMessage() );
			 e.printStackTrace();
		 }
		}
	 
}
