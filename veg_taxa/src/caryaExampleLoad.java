import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.StringTokenizer;

public class caryaExampleLoad{
public static void main(String[] args) {





String namesInfile=("./carya/ScientificName.csv");
String referenceInfile=("./carya/Reference.csv");
String circumscriptionInfile=("./carya/Circumscription.csv");
String partyScientificNameUsageInfile=("./carya/PartyScientificNameUsage.csv");
String statusInfile=("./carya/Status.csv");
String partyInfile=("./carya/Party.csv");
String correlationInfile=("./carya/Correlation2.csv");


String namesArray[]=new String[1000];
int namesCnt=0;
String referenceArray[]=new String[1000];
int refCnt=0;
String circumArray[]=new String[1000];
int circumCnt=0;
String usageArray[]=new String[1000];
int usageCnt=0;
String statusArray[]=new String[1000];
int statusCnt=0;
String partyArray[]=new String[1000];
int partyCnt=0;
String correlationArray[]=new String[1000];
int correlationCnt=0;



/*read all files into their arrys*/
try {

/*read the name file into the array*/
BufferedReader in = new BufferedReader(new FileReader(namesInfile), 8192);
String s=null;
int lineNum=0;
	while ( (s=in.readLine()) !=null){
		if  (s.startsWith("#"))  {System.out.println("header");}

		else{
			namesArray[lineNum]=s.trim();;
			lineNum++;
		} //end else	
	} //end while
	namesCnt=lineNum;
	System.out.println("names: "+namesCnt);


/*read the refs file into the array*/
lineNum=0;
in = new BufferedReader(new FileReader(referenceInfile), 8192);
        while ( (s=in.readLine()) !=null){
                if  (s.startsWith("#"))  {System.out.println("header");}

                else{
                        referenceArray[lineNum]=s.trim();;
                        lineNum++;
                } //end else
        } //end while
        refCnt=lineNum;
        System.out.println("ref: "+refCnt);

/*read the circum file into the array*/
lineNum=0;
in = new BufferedReader(new FileReader(circumscriptionInfile), 8192);
        while ( (s=in.readLine()) !=null){
                if  (s.startsWith("#") ) {System.out.println("header");}

                else{
                        circumArray[lineNum]=s.trim();;
                        lineNum++;
                } //end else
        } //end while
        circumCnt=lineNum;
        System.out.println("circums: "+circumCnt);


/*read the usage file into the array*/
lineNum=0;
in = new BufferedReader(new FileReader(partyScientificNameUsageInfile), 8192);
        while ( (s=in.readLine()) !=null){
                if  (s.startsWith("#") ) {System.out.println("header");}

                else{
                        usageArray[lineNum]=s.trim();;
                        lineNum++;
                } //end else
        } //end while
        usageCnt=lineNum;
        System.out.println("usage: "+usageCnt);


/*read the status file into the array*/
lineNum=0;
in = new BufferedReader(new FileReader(statusInfile), 8192);
        while ( (s=in.readLine()) !=null){
                if  (s.startsWith("#") ) {System.out.println("header");}

                else{
                        statusArray[lineNum]=s.trim();;
                        lineNum++;
                } //end else
        } //end while
        statusCnt=lineNum;
        System.out.println("status: "+statusCnt);


/*read the party file into the array*/
lineNum=0;
in = new BufferedReader(new FileReader(partyInfile), 8192);
        while ( (s=in.readLine()) !=null){
                if  (s.startsWith("#") ) {System.out.println("header");}

                else{
                        partyArray[lineNum]=s.trim();;
                        lineNum++;
                } //end else
        } //end while
        partyCnt=lineNum;
        System.out.println("party: "+partyCnt);


/*read the party correlation into the array*/
lineNum=0;
in = new BufferedReader(new FileReader(correlationInfile), 8192);
        while ( (s=in.readLine()) !=null){
                if  (s.startsWith("#") ) {System.out.println("header");}

                else{
                        correlationArray[lineNum]=s.trim();;
                        lineNum++;
                } //end else
        } //end while
        correlationCnt=lineNum;
        System.out.println("correlation: "+correlationCnt);



} catch(Exception e) {System.out.println("error loading the xml file to memory"+e.getMessage());}


String driver_class = "oracle.jdbc.driver.OracleDriver";
String connect_string = "jdbc:oracle:thin:@128.111.220.63:1521:exp";
String login = "harris";
String passwd = "use4dev";


Connection conn=null;
Statement query = null;
Statement stmt = null;

/* connect to the database*/
try{
Class.forName(driver_class);
System.out.println("connecting to the database");
if( conn == null)
	conn = DriverManager.getConnection (connect_string, login, passwd);
	query = conn.createStatement ();
	stmt = conn.createStatement ();
	System.out.println("Connected...");
 }
catch(Exception e) {System.out.println("did not connect "+e.getMessage());}


/*load the reference table -- first*/
try {
int ref_id=-999;
String author=null;
String date_entered=null;
String citation=null;
String speciesName=null;
String circumType=null;
int i=0;

while (i<refCnt) {
StringTokenizer tok = new StringTokenizer(referenceArray[i], ",");
	String buf=tok.nextToken();
	ref_id=Float.valueOf(buf).intValue();
	buf=tok.nextToken();
	author=buf;
	buf=tok.nextToken();
	date_entered="24-AUG-"+buf;
	buf=tok.nextToken();
	citation=buf;
	buf=tok.nextToken();
	circumType=buf;
	buf=tok.nextToken();
	speciesName=buf;

stmt.executeUpdate("INSERT into reference (ref_id, author, speciesName, circumType, date_entered, citation) values ("+ref_id+",'"+author+"','"+speciesName+"','"+circumType+"','"+date_entered+"','"+citation+"')");


//System.out.println(date_entered);
i++;
}





} //end try
catch (Exception ex) {System.out.println("Loading the data failed at the reference table"+ex);System.exit(1);}


/*Load the name table*/
try {

int name_id =-999;
String symbol=null;
String name=null;
int ref_id=-999;
String date_entered=null;
String family="Juglandaceae";
String author=null;
int i=0;
String citation=null;
int refIdLookup=-999;


while (i<namesCnt) {

StringTokenizer tok = new StringTokenizer(namesArray[i], ",");
//name_id =tok.nextToken();
	String buf=tok.nextToken();
	name_id =Float.valueOf(buf).intValue();
	buf=tok.nextToken();
	ref_id =Float.valueOf(buf).intValue();
	buf=tok.nextToken();
	symbol=buf;
	buf=tok.nextToken();
	name=buf;
	buf=tok.nextToken();
	author=buf;
	buf=tok.nextToken();
	date_entered="24-AUG-"+buf;
	buf=tok.nextToken();
	citation=buf;


stmt.executeUpdate("INSERT into name (name_id, symbol, name, ref_id, family, authorName) values ("+name_id+", '"+symbol+"','"+name+"',"+ref_id+",'"+family+"','"+author+"')");

//System.out.println(author);
i++;
}

//ResultSet testQuery =query.executeQuery("SELECT name_id from  name");


} //end try
catch (Exception ex) {System.out.println("Loading the data failed at the correlation level"+ex);System.exit(1);}






/*load the circumscription table*/
try {

int circum_id =-999;
//int ref_id=-999;
String notes=null;
String citation=null;
String author=null;
String date=null;
String taxonName=null;


int i=0;


while (i<circumCnt) {

StringTokenizer tok = new StringTokenizer(circumArray[i], ",");
        String buf=tok.nextToken();
        circum_id =Float.valueOf(buf).intValue();
        buf=tok.nextToken();
        citation=buf;
        buf=tok.nextToken();
        author=buf;
        buf=tok.nextToken();
        date="24-AUG-"+buf;
        buf=tok.nextToken();
        taxonName=buf;
	buf=tok.nextToken();
	notes=buf;

ResultSet testQuery =query.executeQuery("SELECT ref_id  from reference  where citation  like '%"+citation+"%'");
int ref_id=-999;
while (testQuery.next()) {ref_id=testQuery.getInt(1);}
	if (ref_id != -999 ) {
                System.out.println("circum_id "+circum_id+"ref_id: "+ref_id);
                }

	/*pick out the Kartesz entries*/

	if ( (circumArray[i].indexOf("Kartesz")>0)  && (circumArray[i].indexOf("1994")>0) ) {
		System.out.println("Kartesz entry");
		ref_id=90;
		} //end if
		 
	        if ( (circumArray[i].indexOf("Kartesz")>0)  && (circumArray[i].indexOf("1980")>0) ) {
                System.out.println("Kartesz entry");
                ref_id=91;
                } //end if




stmt.executeUpdate("INSERT into circumscription (circum_id, ref_id, notes) values ("+circum_id+", "+ref_id+",'"+notes+"')");

i++;
}

//ResultSet testQuery =query.executeQuery("SELECT name_id from  name");


} //end try
catch (Exception ex) {System.out.println("Loading the data failed at the Circumscription loading "+ex);System.exit(1);}



/*load the name_usage table*/
try {

int nameUsageId=-999;
int circum_id=-999;
int name_id=-999;
int party_id=-999;

String party=null;
String startDate=null;
String stopDate=null;


int i=0;


while (i<usageCnt) {

StringTokenizer tok = new StringTokenizer(usageArray[i], ",");
        String buf=tok.nextToken();
        nameUsageId =Float.valueOf(buf).intValue();
        buf=tok.nextToken(); //snu-fk
	buf=tok.nextToken(); //party
	party=buf;
	buf=tok.nextToken(); //accept
	buf=tok.nextToken(); //start
        startDate="24-AUG-"+buf;
	buf=tok.nextToken(); //stop
	stopDate="24-AUG-"+buf;
        buf=tok.nextToken(); //name-fk
	name_id=Float.valueOf(buf).intValue();
        buf=tok.nextToken(); //circum-fk
        circum_id=Float.valueOf(buf).intValue();
        buf=tok.nextToken();  //there is more

System.out.println(nameUsageId+" "+party+" "+startDate+" "+stopDate);

if (party.equals("NFA")) {party_id=001;}
if (party.equals("PLANTS")) {party_id=002;}
if (stopDate.equals("24-AUG--999")) {stopDate="24-AUG-2000";}
stmt.executeUpdate("INSERT into name_usage (nameUsageId, circum_id, party_id, name_id, startDate, stopDate) values ("+nameUsageId+", "+circum_id+","+party_id+","+name_id+",'"+startDate+"','"+stopDate+"')");


i++;
}

//ResultSet testQuery =query.executeQuery("SELECT name_id from  name");


} //end try
catch (Exception ex) {System.out.println("Loading the data failed at the name usage loading "+ex);System.exit(1);}

/*load the status*/
try {

int party_circum_id=-999;
int circum_id=-999;
int party_id=-999;

String party=null;
String status=null;
String startDate=null;
String stopDate=null;


int i=0;


while (i<statusCnt) {

StringTokenizer tok = new StringTokenizer(statusArray[i], ",");
        String buf=tok.nextToken();
        party_circum_id=Float.valueOf(buf).intValue();
        buf=tok.nextToken(); //cir-fk
	circum_id=Float.valueOf(buf).intValue();
        buf=tok.nextToken(); //party
        party=buf;
        buf=tok.nextToken(); //status
	status=buf;
        buf=tok.nextToken(); //start
        startDate="24-AUG-"+buf;
        buf=tok.nextToken(); //stop
        stopDate="24-AUG-"+buf;
        buf=tok.nextToken();  //there is more

System.out.println(party_circum_id+" "+status+" "+party+" "+startDate+" "+stopDate);
if (party.equals("FNA")) {party_id=001;}
if (party.equals("NFA")) {party_id=001;}
if (party.equals("PLANTS")) {party_id=002;}
if (stopDate.equals("24-AUG--999")) {stopDate="24-AUG-2000";}

stmt.executeUpdate("INSERT into status (party_circum_id, circum_id, party_id, status, startDate, stopDate) values ("+party_circum_id+", "+circum_id+","+party_id+",'"+status+"','"+startDate+"','"+stopDate+"')");


i++;
}



} //end try
catch (Exception ex) {System.out.println("Loading the data failed at the STATUS  loading "+ex);System.exit(1);}

/*load the correlation Data*/
try {

int correlation_id=-999;
int party_circum1=-999;
int party_circum2=-999;

String convergence=null;
String startDate=null;
String stopDate=null;


int i=0;


while (i<correlationCnt) {

StringTokenizer tok = new StringTokenizer(correlationArray[i], ",");
        String buf=tok.nextToken();
        correlation_id=Float.valueOf(buf).intValue();
        buf=tok.nextToken(); //st-N-fk
        party_circum1=Float.valueOf(buf).intValue();
        buf=tok.nextToken(); //st-S-fk
        party_circum2=Float.valueOf(buf).intValue();
        buf=tok.nextToken(); //convergence
        convergence=buf;
        buf=tok.nextToken(); //start
        startDate="24-AUG-"+buf;
        buf=tok.nextToken(); //stop
        stopDate="24-AUG-"+buf;
        buf=tok.nextToken();  //there is more

System.out.println(correlation_id+" "+startDate+" "+stopDate);

if (stopDate.equals("24-AUG--999")) {stopDate="24-AUG-2005";}

stmt.executeUpdate("INSERT into correlation (correlation_id, party_circum1, party_circum2, convergence, startDate, stopDate) values ("+correlation_id+", "+party_circum1+","+party_circum2+",'"+convergence+"','"+startDate+"','"+stopDate+"')");


i++;
}



} //end try
catch (Exception ex) {System.out.println("Loading the data failed at the Correlation  loading "+ex);System.exit(1);}




}
}

