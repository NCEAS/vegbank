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

public Vector communitySummaryOutput;
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
	
returnFields[0]="commName";
returnFields[1]="abiCode";
returnFields[2]="commDescription";
returnFields[3]="parentCommName";
returnFields[4]="dateEntered";
returnFields[5]="classCode";
returnFields[6]="classLevel";
returnFields[7]="conceptOriginDate";
returnFields[8]="conceptUpdateDate";
returnFields[9]="parentAbiCode";
returnFields[10]="recognizingParty";
returnFields[11]="partyConceptStatus";
returnFields[12]="parentCommDescription";
returnFields[13]="commSummary_Id";




//issue the statement
issueStatement j = new issueStatement();
j.issueSelect(statement.toString(), action, returnFields, returnFieldLength);

//pass the results back to the calling class
communitySummaryOutput=j.returnedValues;  //copy this vector


//System.out.println("vector numbers: "+j.returnedValues.size() );
//System.out.println( j.returnedValues.toString());

}
}
