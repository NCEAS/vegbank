import java.io.*;
import java.text.*;
import java.util.*;
import java.math.*;
import java.sql.*; 
import java.net.URL;


/**
 * Application to load the Ecoart Alliance table to oracle database -- this is
 * used instead of the sqlloader because there has been some problem with the
 * sql loader loading fields with more than ~500 characters
 *
 * @author John Harris
 */


public class LoadAllianceTable
{
	
	
public Vector outVector;
public int vecElementCnt;

/**
 * Main method for laoding the data
 */

public static void main(String[] args) {

String infile=(args[0]);

String driver_class = "oracle.jdbc.driver.OracleDriver"; 
String connect_string="jdbc:oracle:thin:harris/use4dev@dev.nceas.ucsb.edu:1521:exp";
Connection conn=null; 
Statement query = null;
Statement stmt = null;                       

try{ 
Class.forName(driver_class); 
System.out.println("Trying to connect..."); 
if( conn == null) {
	conn = DriverManager.getConnection (connect_string); 
	stmt = conn.createStatement ();       
	System.out.println("Connected."); 

	
	//put the file into a vector
	LoadAllianceTable a =new LoadAllianceTable();
	a.fileVectorizer (infile);

	//loop thru the vector and pass each line to the loading method
	for (int i=0; i<a.vecElementCnt; i++) {
		String line = a.outVector.elementAt(i).toString(); //line from vector
		LoadAllianceTable b =new LoadAllianceTable();
		b.loadTable(line, conn, stmt);
	}
} 
}
catch( Exception e) { System.out.println("did not connect "+e.getMessage() );}
}








/**
 * Method to put the file into a vector
 */
public void fileVectorizer (String infile) {

try {
vecElementCnt=0;
BufferedReader in = new BufferedReader(new FileReader(infile));
Vector localVector = new Vector();
String s;
while((s = in.readLine()) != null) {
	localVector.addElement(s);
	vecElementCnt++;
}
outVector=localVector;

}
catch (Exception e) {System.out.println("failed in fileVectorizer" + 
	e.getMessage());}
} 







/**
 * Method that actually puts the data into the database
 */
public void loadTable(String loadLine, Connection conn, Statement stmt) {                                  
try{
	
	StringTokenizer st = new StringTokenizer(loadLine, "|");
	
	String allianceKey = st.nextToken().replace('\'',' ');
	String allianceStatus= st.nextToken().replace('\'',' ');
	String leaderesp = st.nextToken().replace('\'',' ');
	String formationCode = st.nextToken().replace('\'',' ');
	String allianceNum = st.nextToken().replace('\'',' ');
	String formationKey = st.nextToken().replace('\'',' ');
	String allianceName = st.nextToken().replace('\'',' ');
	String allianceNameTrans = st.nextToken().replace('\'',' ');
	String allianceDesc = st.nextToken().replace(',',' ');

		allianceDesc=allianceDesc.replace('\'',' ');
		allianceDesc=allianceDesc.trim();	
	
	String system = st.nextToken();
	String assocDef = st.nextToken();
	String edition = st.nextToken();
	String edauthor = st.nextToken();
	
	String allianceOriginDate ="01-JAN-1900";
	if (st.hasMoreTokens()) {allianceOriginDate = st.nextToken(); }
	
	String updateDate ="01-JAN-1900";
	if (st.hasMoreTokens()) {updateDate = st.nextToken(); }

//	String updateDate = st.nextToken();


//	System.out.println(allianceName.length()+"   "+allianceNameTrans.length() );
//	System.out.println(allianceName+"   "+allianceNameTrans );
	//trim the description to 4000 characters
	if (allianceDesc.length()>3999) { allianceDesc=allianceDesc.substring(0, 3999); }

	
	conn.setAutoCommit(true);
		
	stmt.executeUpdate("INSERT INTO ALLIANCE ( "
	+"AllianceKey, "
	+"Leaderesp, "
	+"formationCode, "
	+"AllianceNum, "
	+"FormationKey, "
	+"AllianceName, "
	+"AllianceNameTrans, "
	+"System, "
	+"AssocDef, "
	+"Edition, "
	+"Edauthor, "
	+"AllianceOriginDate, "
	+"UpdateDate, "
	+"AllianceDesc, "
	+"AllianceStatus ) "
+"VALUES ('"
	+allianceKey+"', '"
	+leaderesp+"', '"
	+formationCode+"', '"
	+allianceNum+"', '"
	+formationKey+"', '"
	+allianceName+"', '"
	+allianceNameTrans+"', '"
	+system+"', '"
	+assocDef+"', '"
	+edition+"', '"
	+edauthor+"', '"
	+allianceOriginDate+"', '"
	+updateDate+"', '"
	+allianceDesc+"', '"
	+allianceStatus+"')");
	
}
catch (Exception ex) {System.out.println("error loading" +ex.getMessage());
 ex.printStackTrace();}
} 
} 
