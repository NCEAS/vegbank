import java.io.IOException;
import java.io.PrintWriter;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class protoServ extends HttpServlet {
ResourceBundle rb = ResourceBundle.getBundle("protoServ");

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
        throws IOException, ServletException
    {



response.setContentType("text/html");
PrintWriter out = response.getWriter();

/*Global variables*/
int targetPerspective=-999;

/*start the output to the browser*/
out.println("<html>");
out.println("<body>");
out.println("<head>");
String title =("prototype design");
out.println("<title>" + title + "</title>");
out.println("</head>");
out.println("<body bgcolor=\"WHITE\">");
out.println("<h3>" + title + "</h3>");

out.println("<p>");
out.println("<img alt=\"this is not the correct image\" src=\"/harris/owlogo.jpg\" border=\"0\">");
out.println("<p>");

String firstName = request.getParameter("firstname");
String lastName = request.getParameter("lastname");
String operation=request.getParameter("operation");
int i=0;



out.println(rb.getString("requestparams.params-in-req") + "<br>");

if (firstName != null || lastName != null) {
	out.println("<FONT COLOR=green>");
	out.println("Party Perspective: "+lastName+ "<br>");
	out.println("Name Entered: "+firstName+"<br>");
	out.println("</FONT>");
	out.println("<br>");
	out.println("<b>Selection results:</b> ");



/*figure out what the party perspective is desired from he browser*/
try {
if (lastName.equals("PLANTS")) {targetPerspective=2;}
if (lastName.equals("FNA")) {targetPerspective=1;}

}
catch ( Exception e ){out.println("Figuring out the party perspective from the browser entry crashed"+e.getMessage());}

/*
* Determine the operation to be performed
*/
try {
if (operation.equals("nameLookup")) {out.println("operation: name lookup <br>");

	/*pass the name taxaQuery  class*/
	try {
	taxaQuery m = new taxaQuery();

		out.println("First call to doOneThing");
     		m.getName(firstName);
     		String nameArray[]=new String[10];
		int nameNum=m.outNameNum;
		nameArray=m.outName;
		out.println("Query produced: "+nameNum+" results <br>");
			for (int ii=0; ii<nameNum; ii++) {	
				out.println("<i> "+nameArray[ii]+"</i><br>");
			} //end for

	} //end try 
	catch ( Exception e ){out.println(" failed while calling MyClass "+e.getMessage());}
} //end if

if (operation.equals("circumLookup")) {out.println("operation: name lookup <br>");

        /*pass the name taxaQuery  class*/
        try {
        taxaQuery m = new taxaQuery();

                out.println("call to taxaQuery.getCircum");
                m.getCircum(firstName, targetPerspective);
                String circumStatusArray[]=new String[10];
                int circumStatusNum=m.outCircumStatusNum;
                circumStatusArray=m.outCircumStatus;
                out.println("Query produced: "+circumStatusNum+" results <br>");
                        for (int ii=0; ii<=circumStatusNum; ii++) {

                                /*tokenize the array returned by the taxaQuery class*/
				if (circumStatusArray[ii] != null) {
					StringTokenizer t = new StringTokenizer(circumStatusArray[ii], "|");
					String name=t.nextToken();
					String author=t.nextToken();
					String citation=t.nextToken();
					String speciesName=t.nextToken(); 
					String circumType=t.nextToken();
					String circumDate=t.nextToken();
						circumDate=circumDate.substring(0, 4);
					String status =t.nextToken();
					String statusStartDate =t.nextToken();
						statusStartDate = statusStartDate.substring(0, 4);
					String statusStopDate =t.nextToken();
						statusStopDate=statusStopDate.substring(0, 4);
					String partyId =t.nextToken();
						if (partyId.equals("2")) {partyId="PLANTS";}
						if (partyId.equals("1")) { partyId="FNA";}
					String partyCircumId=t.nextToken();
				out.println("<i>"+name+"</i><br>");
				out.println("<b><spacer type=block width=25> "+author+"</b><br>");
				out.println("<b><spacer type=block width=25> "+citation+"</b><br>");
				out.println("<b><spacer type=block width=25> "+circumDate+"</b><br>");
				out.println("<b><spacer type=block width=25> "+speciesName+"</b><br>");
				out.println("<b><spacer type=block width=25> "+circumType+"</b><br>");
					 out.println("<FONT COLOR=\"336699\">");
					out.println("<spacer type=block width=35> "+status+"<br>");
	                                out.println("<spacer type=block width=35> "+statusStartDate+"-"+statusStopDate+"<br>");
					out.println("<spacer type=block width=35> "+partyId+"<br>");
						if (status.equals("N")) {
							out.println("<spacer type=block width=35> "+partyCircumId+"<br>");
							//convert a String to an int
							Integer tmp=Integer.valueOf(partyCircumId);
							int nonStdCircumId=tmp.intValue();
							
							try {	
								m.getCorrelation(nonStdCircumId);
								String correlationCircum[]=new String[10];
								correlationCircum =m.outCorrelativeCircum;
								int correlationCircumNum=m.outCorrelativeCircumNum;
									for (int iii=0; iii<correlationCircumNum; iii++) {
										if (correlationCircum[iii] != null) {	
											out.println("<spacer type=block width=50> "+correlationCircum[iii]+"<br>");
											} //end if
									} //end for
							} //end try 
							catch ( Exception e ){out.println(" failed while calling taxaQuery.getCorrelation"+e.getMessage());}
							
							} //end if 

					 out.println("</FONT>");
				} //end if
                        } //end for

        } //end try
        catch ( Exception e ){out.println(" failed while calling taxaQuery.getCircum"+e.getMessage());}
} //end if


/*
*
*  Get the info on standard uses and the correlations to nonstandard uses
*
*/

if (operation.equals("statusLookup")) {

	out.println("operation: status lookup <br>");


try {

int partyId=2;
currentTaxa m = new currentTaxa();

        m.getCurrentTaxon(firstName, targetPerspective);
        String standardUsage[]=new String[1000];
        String nonStandardUsage[]=new String[1000];

        int standardUsageNum=m.outStandardUsageNum;
        int nonStandardUsageNum=m.outNonStandardUsageNum;

        standardUsage=m.outStandardUsage;
        nonStandardUsage=m.outNonStandardUsage;

        out.println("Query produced: "+standardUsageNum+" results for standard usage <br><br>");
            for (int ii=0; ii<standardUsageNum; ii++) {
		StringTokenizer t = new StringTokenizer(standardUsage[ii], "|");
		String inputTaxon=t.nextToken();
		String standardTaxon=t.nextToken();
		String startDate =t.nextToken();
		String stopDate =t.nextToken();		
//               out.println(standardUsage[ii]+"<br>");
			out.println("Input String: <B><FONT COLOR=990033>"+inputTaxon+"</B></FONT><br>");
			out.println("Standard Taxon: <B><FONT COLOR=336600>"+standardTaxon+"</B></FONT><br>");
			out.println("This Taxon was standard during the period:  <B>"+startDate+"-"+stopDate+"</B><br>");
			out.println("<br>");	
             } //end for


        out.println("Query produced: "+nonStandardUsageNum+" results for non standard uses <br><br>");
        for (int ii=0; ii<nonStandardUsageNum; ii++) {
		StringTokenizer t1 = new StringTokenizer(nonStandardUsage[ii], "|");
                String inputTaxon=t1.nextToken();
                String standardTaxon=t1.nextToken();
                String startDate =t1.nextToken();
                String stopDate =t1.nextToken();

               out.println(nonStandardUsage[ii]+"<br>");
		out.println("Input String: <B><FONT COLOR=red>"+inputTaxon+"</B></FONT><br>");
                        out.println("Standard Taxon: <B><FONT COLOR=green>"+standardTaxon+"</B></FONT><br>");
                        out.println("This Taxon was standard during the period:  <B>"+startDate+"-"+stopDate+"</B><br>");
                        out.println("<br>"); 
             } //end for



}

catch( Exception e ) {System.out.println(" failed in: testQuery "+e.getMessage());}






} //end if


if (operation.equals("correlationLookup")) {

        out.println("operation:Correlation lookup <br>");


int partyId=2;
currentTaxa m = new currentTaxa();

        m.getTaxaCorrelation(firstName, targetPerspective);
        String standardCorrelation[]=new String[1000];
        String nonStandardCorrelation[]=new String[1000];

        int standardCorrelationNum=m.outStandardCorrelationNum;
        int nonStandardCorrelationNum=m.outNonStandardCorrelationNum;

        standardCorrelation=m.outStandardCorrelation;
        nonStandardCorrelation=m.outNonStandardCorrelation;
        
        //out.println("Query produced: "+standardCorrelationNum+" results for standard usage <br>");
            for (int ii=0; ii<standardCorrelationNum; ii++) {
		StringTokenizer t1 = new StringTokenizer(standardCorrelation[ii], "|");
               	String acceptence=t1.nextToken();
		String inputTaxon=t1.nextToken();
		String statusCircumId=t1.nextToken();
		String status=t1.nextToken();
		String statusPartyId=t1.nextToken();
		String statusStartDate=t1.nextToken();
		String statusStopDate=t1.nextToken();
		//out.println(standardCorrelation[ii]);
			//out.println("Acceptence: <B><FONT COLOR=red>"+acceptence+"</B></FONT><br>");
                        //out.println("Input String: <B><FONT COLOR=green>"+inputTaxon+"</B></FONT><br>");
                        //out.println("status circum id: :  <B>"+statusCircumId+"</B><br>");
			//out.println("status :  <B>"+status+"</B><br>");
			//out.println("status party id :  <B>"+statusPartyId+"</B><br>");
			//out.println("status period :  <B>"+statusStartDate+"-"+statusStopDate+"</B><br>");
			
                        out.println("<br>");
             } //end for


        out.println("Query produced: "+nonStandardCorrelationNum+" results correlative taxa uses - showing only those with standard party acceptence <br><br>");
        for (int ii=0; ii<nonStandardCorrelationNum; ii++) {
		 StringTokenizer t = new StringTokenizer(nonStandardCorrelation[ii], "|");
                String acceptence=t.nextToken();
                String inputTaxon=t.nextToken();
                String statusCircumId=t.nextToken();
                String correlationId=t.nextToken();
                String congruence=t.nextToken();
                String statusCircumId2=t.nextToken();
		String correlationCircumId=t.nextToken();
                String correlationStatus=t.nextToken();
		String correlationNameId=t.nextToken();
		String correlationPartyId=t.nextToken();
		String correlationStartDate=t.nextToken();
		String correlationStopDate=t.nextToken();
		String currentTaxon=t.nextToken();
		String correlationNameAcceptence=t.nextToken();
                //out.println(standardCorrelation[ii]);
                        
			
		if (correlationNameAcceptence.equals("S")) {
			out.println("Acceptence: "+acceptence+"<br>");
                        out.println("Input String: <B><FONT COLOR=red>"+inputTaxon+"</B></FONT><br>");
                        out.println("status circum id: :  <B>"+statusCircumId+"</B><br>");
                        
			out.println("correlation id :  <B>"+correlationId+"</B><br>");
                        out.println("congruence :  <B>"+congruence+"</B><br>");
                        out.println("correlative status circum id :  <B>"+statusCircumId2+"</B><br>");
                        out.println("correlative circum id :  <B>"+correlationStatus+"</B><br>");
			out.println("correlation status: <B>"+correlationStatus+"</B><br>");
			out.println("correlation name id: <B>"+correlationNameId+"</B><br>");
			out.println("correlation party id: <B>"+correlationPartyId+"</B><br>");              
			out.println("correlation period : <B>"+correlationStartDate+"-"+correlationStopDate+"</B><br>");
			out.println("correlation name: <B><FONT COLOR=green>"+currentTaxon+"</B></FONT><br>");
			out.println("correlation name acceptence: "+correlationNameAcceptence+"</B><br>");
 			//out.println(nonStandardCorrelation[ii]);
             
		} //end if
	} //end for





}  //end if
try {

}

catch( Exception e ) {System.out.println(" failed in: testQuery "+e.getMessage());}







} //end try
catch ( Exception e ){out.println("Figuring out the operation"+e.getMessage());}




/*links to other dynamically generated pages*/
//out.println("<A HREF=\"http://dev.nceas.ucsb.edu/harris/results.html\"><B><FONT SIZE=\"-1\" FACE=\"arial\">Expanded Results</FONT></B></A>");






} //end if
 else {
            out.println(rb.getString("requestparams.no-params"));
        }
out.println("<P>");
out.print("<form action=\"");
out.print("protoServ\" ");   /*make sure to have this pointing to the right servlet6*/
out.println("method=POST>");
out.println("Taxon String: ");
out.println("<input type=text size=35 name=firstname>");
out.println("<br>");
out.println("<br>");

/*this is ther party selection form*/
out.println("<br><i>Party Perspective </i>");
out.println("<SELECT NAME=lastname SIZE=2 onChange=\"checker(this)\"><OPTION>ALL PARTIES<OPTION>PLANTS<OPTION>FNA<OPTION>USDA<OPTION>KARTESZ</SELECT>");
out.println("<br><br> \n");


out.println("<input type=\"radio\" NAME=\"operation\" Value=\"nameLookup\" CHECKED> name lookup");
out.println("<input type=\"radio\" NAME=\"operation\" Value=\"circumLookup\" > circumscription lookup");
out.println("<input type=\"radio\" NAME=\"operation\" Value=\"statusLookup\" > standard usage");
out.println("<input type=\"radio\" NAME=\"operation\" Value=\"correlationLookup\" > correlation lookup");
out.println("<input type=\"radio\" NAME=\"operation\" Value=\"plot lookup\" > plot lookup");

out.println("<br><br> \n");


out.println("<input type=submit>");
out.println("</form>");


out.println("</body>");
out.println("</html>");


    }

    public void doPost(HttpServletRequest request,
                      HttpServletResponse response)
        throws IOException, ServletException
    {
        doGet(request, response);
    }

}
