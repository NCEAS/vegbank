import java.io.*;
import java.util.*;
import java.math.*;
import java.net.URL;
import java.util.Date;



public class plantstoXml
{
  public static void main(String[] args)
  {



/*global variables*/
String infile=(args[0]);
String outfile=("outFile.xml");
String infileArray[]=new String[100000];
int lineNum=0;
String synonymType = null;
String symbol = null;
String name=null;
String binomial=null;
String synonym=null;
String family=null;
String nameAuthor=null;


/*Read the entire plants data set into memory*/
try {
BufferedReader in = new BufferedReader(new FileReader(infile), 8192);
System.out.println("reading the input file");
String s=null;

	/*read the input file into the array*/
	while ( (s=in.readLine()) != null ){
	infileArray[lineNum]=s;
	

	lineNum++;
	} //end while
System.out.println("Number of lines in the file: "+lineNum);
} //end try
catch(Exception e) {System.out.println("did not load all the data "+e.getMessage());}





/*next blck will read the attributes from the array into discrete variables*/
try {
for (int i=0; i<lineNum; i++) {

	if (infileArray[i].indexOf("=") >0 ) { 
		synonymType = "synonym";
		StringTokenizer t = new StringTokenizer(infileArray[i], "|");
			symbol=t.nextToken().replace('"',' ').trim();
			name=t.nextToken().replace('"',' ').trim();
			synonym=t.nextToken().replace('=',' ').trim();
			family=t.nextToken().replace('"',' ').trim();
			


 /*next lines will separate the author from the binomial - this should be modified cause 
   only works when there is a Genus and species name*/
                        StringTokenizer t2 = new StringTokenizer(name, " ");
                        String buf = t2.nextToken();
			String Genus = buf;
			buf= t2.nextToken();
			String species=buf;
			buf= t2.nextToken();
			binomial = Genus+" "+species;
			/*make sure to get the whole name*/
			while (t2.hasMoreTokens()) {String remaingBuf = t2.nextToken(); buf=buf+remaingBuf;} 
			nameAuthor=buf;

	} //end if




/*next block will write out the xml document*/
	try {
	PrintStream out = new PrintStream(new FileOutputStream(outfile));
		if (synonymType.equals("synonym")) {
			out.println("<?xml version=\"1.0\"?>");
			out.println("<!DOCTYPE plantTaxa SYSTEM \"taxa.dtd\">");
			out.println("<plantTaxa>");
			out.println("<name>");
			out.println("<symbol>"+symbol+"</symbol>");
			out.println("<taxon>"+binomial+"</taxon>");
			out.println("<commonName>null</commonName>");
			out.println("<family>"+family+"</family>");
			out.println("<entryDate>2000</entryDate>");
			out.println("<nameReference>");
			out.println("<author>"+nameAuthor+"</author>");
			out.println("<dateEntered>2000</dateEntered>");
			out.println("<citation>unknown, from plants 1996</citation>");
			out.println("<label>"+name+"</label>");
			out.println("</nameReference>");
			out.println("</name>");
			out.println("");
			out.println("<circumscription>");
			out.println("<type>No</type>");
			out.println("<circumReference>");
			out.println("<author>Plants1996</author>");
			out.println("<dateEntered>2000</dateEntered>");
			out.println("<citation>Plants 1996: plants.usda.gov</citation>");	
			out.println("<label>"+name+"</label>");
			out.println("</circumReference>");
			out.println("</circumscription>");
			out.println("");
			out.println("<party>");
			out.println("<orgName>USDA</orgName>");
			out.println("</party>");
			out.println("");
			out.println("<status>");
			out.println("<currentStatus>NS</currentStatus>"); //this is std where there is no equals sign
			out.println("<statusParty>USDA</statusParty>");
			out.println("<startDate>1996</startDate>");  // edit this for the particular year
			out.println("<stopDate>null</stopDate>");
			out.println("</status>");
			out.println("");
			out.println("<correlation>");
			out.println("<correlationParty>USDA</correlationParty>");
			out.println("<correlationReference>");
			out.println("<correlationAuthor>Plants1996</correlationAuthor>");
			out.println("<correlationDateEntered>2000</correlationDateEntered>");
			out.println("<correlationCitation>Plants 1996: plants.usda.gov</correlationCitation>");
			out.println("<label>"+synonym+"</label>");
			out.println("</correlationReference>");
			out.println("<congruence>equal</congruence>");
			out.println("</correlation>");
			out.println("</plantTaxa>");


		} //end if
 		
	} //end try
	catch(Exception e) {System.out.println("did not load all the data "+e.getMessage());}






} //end for
} //end try
catch(Exception e) {System.out.println("did not load all the data "+e.getMessage());}



  }
}

