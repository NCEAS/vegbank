package databaseAccess;

import java.io.IOException;
import java.io.*;
import java.util.*;

import databaseAccess.*;

/**
 * this class contains methods for storing the plots 
 * database elements and it is anticipated that this
 * class will be used by all other classes which need
 * access to tha attributes in the plots database. An 
 * example of this may be when retrieving all the plots-site
 * data from the database - rather than having to explicitly
 * define all the attributes fro selection this class will 
 * be accessed and will provide all the attributes for 
 * selection
 *
 * @Author John Harris
 * @Version May 2001
 */

public class PlotElements {

public Vector siteElementVec = new Vector();
public Vector projectElementVec = new Vector();
public Vector speciesElementVec = new Vector();
public Vector strataElementVec = new Vector();

public static int siteElementCount = 0;
public static int projectElementCount = 0;
public static int speciesElementCount = 0;
public static int strataElementCount = 0;


/**
 * method when called will
 * add the appropriate elements
 * to the vector relavent to the 
 * specific category of plots data
 * 
 */
public void buildElementLists() 
{
	//initilize the site element vector	
	siteElements();
	//initilize the project element vector	
	projectElements();
	//species attributes
	speciesElements();
	//strata attributes
	strataElements();
}

/**
 * method that returns the size of the relevant vector
 * @param listType -- can be either project, site, observation etc ..
 */
public int elementListSize(String listType) 
{
	if (listType.equals("site"))
	{
		return( siteElementVec.size() );
	}
	if (listType.equals("project"))
	{
		return( projectElementVec.size() );
	}
	if (listType.equals("species"))
	{
		return( speciesElementVec.size() );
	}
	if (listType.equals("strata"))
	{
		return( strataElementVec.size() );
	}
	else 
	{
		System.out.println("unrecognized element list requested: "+listType);
		return( 0 );
	}
}


/**
 * method that returns the project elements in order
 */
public String getSpeciesElement() 
{
	if ( speciesElementCount >= speciesElementVec.size() )
	{
		speciesElementCount=0;
		return(null);
	}
	else
	{
		String currentElement = null;
		currentElement=(String)speciesElementVec.elementAt(speciesElementCount);
		speciesElementCount++;
		return( currentElement);
	}
}



/**
 * method that returns the project elements in order
 */
public String getStrataElement() 
{
	if ( strataElementCount >= strataElementVec.size() )
	{
		strataElementCount=0;
		return(null);
	}
	else
	{
		String currentElement = null;
		currentElement=(String)strataElementVec.elementAt(strataElementCount);
		strataElementCount++;
		return( currentElement);
	}
}


/**
 * method that returns the project elements in order
 */
public String getProjectElement() 
{
	if ( projectElementCount >= projectElementVec.size() )
	{
		projectElementCount=0;
		return(null);
	}
	else
	{
		String currentElement = null;
		currentElement=(String)projectElementVec.elementAt(projectElementCount);
		projectElementCount++;
		return( currentElement);
	}
}


/**
 * method that returns the site elements in order
 */
public String getSiteElement() 
{
	if ( siteElementCount >= siteElementVec.size() )
	{
		siteElementCount=0;
		return(null);
	}
	else
	{
		String currentElement = null;
		currentElement=(String)siteElementVec.elementAt(siteElementCount);
		siteElementCount++;
		return( currentElement);
	}
}


/**
 * method that stores the project related attributes
 *
 */
public void projectElements() 
{
	projectElementVec.add(0, "projectName");
	projectElementVec.add(1, "projectDescription");
	projectElementVec.add(2, "projectContributor");
}



/**
 * method that stores the site related attributes
 */
public void siteElements() 
{
	siteElementVec.add(0, "xCoord");
	siteElementVec.add(1, "yCoord");
	siteElementVec.add(2, "state");
	siteElementVec.add(3, "county");
	siteElementVec.add(4, "country");
	siteElementVec.add(5, "namedPlace");
	siteElementVec.add(6, "plotShape");
	siteElementVec.add(7, "surveyDate");
	siteElementVec.add(8, "slopeAspect");
	siteElementVec.add(9, "slopeGradient");
	siteElementVec.add(10, "elevationValue");
	siteElementVec.add(11, "coordType");
	siteElementVec.add(12, "soilDepth");
	siteElementVec.add(13, "soilType");
	siteElementVec.add(14, "authorPlotCode");
	
	
}


/**
 * method that stores the site related attributes
 */
public void speciesElements() 
{
	speciesElementVec.add(0, "taxonName");
	speciesElementVec.add(1, "code");
	speciesElementVec.add(2, "cumStrataCover");
}


/**
 * method that stores the site related attributes
 */
public void strataElements() 
{
	strataElementVec.add(0, "stratumName");
	strataElementVec.add(1, "stratumHeight");
	strataElementVec.add(2, "stratumCover");
}


}
