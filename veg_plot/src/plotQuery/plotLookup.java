import java.util.*;
import java.io.*;

public class plotLookup{
	public void plotLookup() {



outPlotData=null;
outPlotDataNum=0;

		
}

public void getTaxonObservation(String input_s) {


try {
String taxonName = input_s;

/*get all the maching names*/
taxonObservationQuery m = new taxonObservationQuery();
m.getTaxonName(taxonName);
        //String taxonNameArray[]=new String[10000];
	int taxonObservationId[]=m.outTaxonObservationId;
        int taxonObservationNum=m.outTaxonObservationNum;
	int observationId[]=m.outObservationId;
	String authorTaxonName[]=m.outAuthorTaxonName;
	String originalAuthority[]=m.outOriginalAuthority;
	String cumStrataCoverage[]=m.outCumStrataCoverage;
	


       // nameArray=m.outName;


 
            for (int ii=0; ii<taxonObservationNum; ii++) {
            
	
		//System.out.println("TaxonObservationId : "+taxonObservationId[ii]);
		outPlotData[ii]=""+taxonObservationId[ii]+"|"+observationId[ii]+"|"+authorTaxonName[ii]+"|"+originalAuthority[ii]+"|"+cumStrataCoverage[ii]+"|";	       
		
		outPlotDataNum=ii;	       
             } //end for
	
		
} //end try
catch( Exception e ) {System.out.println(" failed at: currentTaxa  "+e.getMessage());}

}




public String outPlotData[]=new String[1000];
public int outPlotDataNum;


//public String output_s;
//public String outName[]=new String[1000];
//public int outNameId[]=new int[1000];
//public int outNameNum;
//public int outCircumStatusNum;
//public String outCircumStatus[]=new String[1000];
//public String outCorrelativeCircum[]=new String[1000];
//int outCorrelativeCircumNum;	
	
} 

