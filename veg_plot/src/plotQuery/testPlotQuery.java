import java.util.*;
import java.io.*;

public class testPlotQuery {
public static void main (String[] args)  {

try {
String taxonName = "Carya cordiformi";

plotLookup m = new plotLookup();

	m.getTaxonObservation(taxonName);
//        String outputData[]=new String[1000];


        String plotData[]=m.outPlotData;
	int plotDataNum=m.outPlotDataNum;


        System.out.println("Query produced: "+plotDataNum+" results");
            for (int ii=0; ii<plotDataNum; ii++) {
               System.out.println(plotData[ii]);
             } //end for



}

catch( Exception e ) {System.out.println(" failed in: testQuery "+e.getMessage());}



}
} 

