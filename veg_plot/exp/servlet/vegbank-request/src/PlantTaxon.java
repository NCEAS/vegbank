import java.io.*;
import java.text.*;
import java.util.*;
import java.util.Date;


/**
 * This class represents a plant taxon
 */


public class PlantTaxon
{
	
	public Hashtable plantTaxon;
	
	/**
	 * constructor method -- which when called creates a new instance of a 
	 * plant taxon
	 */
	public PlantTaxon()
	{
		System.out.println("PlantTaxon > constructing a new instance");
		this.plantTaxon = new Hashtable();
	}

	/**
	 * method to set the inserter's email address
	 */
	 public void setEmailAddress(String emailAddress)
	 {
		 plantTaxon.put("emailAddress", emailAddress);
	 }
	 
	 /**
	 * method to get the inserter's email address
	 */
	 public String getEmailAddress()
	 {
		 String s = null;
		 if ( plantTaxon.containsKey("emailAddress") )
		 {
			 s = (String)plantTaxon.get("emailAddress");
		 }
		 return(s);
	 }
	 
	 
	 /**
	 * method to set the inserter's givenName
	 */
	 public void setInserterGivenName(String givenName)
	 {
		 plantTaxon.put("inserterGivenName", givenName);
	 }
	 
	 /**
	 * method to get the inserter's givenName
	 */
	 public String getInserterGivenName()
	 {
		 String s = null;
		 if ( plantTaxon.containsKey("inserterGivenName") )
		 {
			 s = (String)plantTaxon.get("inserterGivenName");
		 }
		 return(s);
	 }
	 
	 
	 /**
	 * method to set the inserter's surName
	 */
	 public void setInserterSurName(String surName)
	 {
		 plantTaxon.put("inserterSurName", surName);
	 }
	 
	  /**
	 * method to get the inserter's surName
	 */
	 public String getInserterSurName()
	 {
		 String s = null;
		 if ( plantTaxon.containsKey("inserterSurName") )
		 {
			 s = (String)plantTaxon.get("inserterSurName");
		 }
		 return(s);
	 }
	 
	 
	 /**
	 * method to set the inserter's institution
	 */
	 public void setInserterInstitution(String institution)
	 {
		 plantTaxon.put("inserterInstitution", institution);
	 }
	
	 /**
	 * method to get the inserter's Institution
	 */
	 public String getInserterInstitution()
	 {
		 String s = null;
		 if ( plantTaxon.containsKey("inserterInstitution") )
		 {
			 s = (String)plantTaxon.get("inserterInstitution");
		 }
		 return(s);
	 }
	
	
	/**
	 * method to set the scientificName of the plant
	 */
	 public void setScientificName(String name)
	 {
		 plantTaxon.put("scientificName", name);
	 }
	 
	 /**
	 * method to get the scientific name
	 */
	 public String getScientificName()
	 {
		 String s = null;
		 if ( plantTaxon.containsKey("scientificName") )
		 {
			 s = (String)plantTaxon.get("scientificName");
		 }
		 return(s);
	 }
	 
	 
	 /**
	 * method to set the commonName of the plant
	 */
	 public void setCommonName(String name)
	 {
		 plantTaxon.put("commonName", name);
	 }
	 
	 /**
	 * method to get the common name
	 */
	 public String getCommonName()
	 {
		 String s = null;
		 if ( plantTaxon.containsKey("commonName") )
		 {
			 s = (String)plantTaxon.get("commonName");
		 }
		 return(s);
	 }
	 
	 
	 /**
	 * method to set the code of the plant
	 */
	 public void setCode(String name)
	 {
		 plantTaxon.put("code", name);
	 }
	 
	 /**
	 * method to get the code
	 */
	 public String getCode()
	 {
		 String s = null;
		 if ( plantTaxon.containsKey("code") )
		 {
			 s = (String)plantTaxon.get("code");
		 }
		 return(s);
	 }
	 
	 
	 
	 /**
	 * method to set the authors' name(s) of the scientific name reference
	 */
	 public void setScientificNameRefAuthors(String longNameRefAuthors)
	 {
		 plantTaxon.put("scientificNameRefAuthors", longNameRefAuthors);
	 }
	 
	  /**
	 * method to get 
	 */
	 public String getScientificNameRefAuthors()
	 {
		 String s = null;
		 if ( plantTaxon.containsKey("scientificNameRefAuthors") )
		 {
			 s = (String)plantTaxon.get("scientificNameRefAuthors");
		 }
		 return(s);
	 }
	 
	 
	  /**
	 * method to set the title of the scientific name reference
	 */
	 public void setScientificNameRefTitle(String longNameRefTitle)
	 {
		 plantTaxon.put("scientificNameRefTitle", longNameRefTitle );
	 }
	 
	   /**
	 * method to get 
	 */
	 public String getScientificNameRefTitle()
	 {
		 String s = null;
		 if ( plantTaxon.containsKey("scientificNameRefTitle") )
		 {
			 s = (String)plantTaxon.get("scientificNameRefTitle");
		 }
		 return(s);
	 }
	 
	 
	  /**
	 * method to set the date of the scientific name reference
	 */
	 public void setScientificNameRefDate(String longNameRefDate)
	 {
		 plantTaxon.put("scientificNameRefDate", longNameRefDate );
	 }
	 
	   /**
	 * method to get 
	 */
	 public String getScientificNameRefDate()
	 {
		 String s = null;
		 if ( plantTaxon.containsKey("scientificNameRefDate") )
		 {
			 s = (String)plantTaxon.get("scientificNameRefDate");
		 }
		 return(s);
	 }
	 
	 
	  /**
	 * method to set the edition of the scientific name reference
	 */
	 public void setScientificNameRefEdition(String longNameRefEdition)
	 {
		 plantTaxon.put("scientificNameRefEdition", longNameRefEdition);
	 }
	 
	   /**
	 * method to get 
	 */
	 public String getScientificNameRefEdition()
	 {
		 String s = null;
		 if ( plantTaxon.containsKey("scientificNameRefEdition") )
		 {
			 s = (String)plantTaxon.get("scientificNameRefEdition");
		 }
		 return(s);
	 }
	 
	 
	  /**
	 * method to set the series name of the scientific name reference
	 */
	 public void setScientificNameRefSeriesName(String longNameRefSeriesName)
	 {
		 plantTaxon.put("scientificNameRefSeriesName", longNameRefSeriesName);
	 }
	 
	   /**
	 * method to get 
	 */
	 public String getScientificNameRefSeriesName()
	 {
		 String s = null;
		 if ( plantTaxon.containsKey("scientificNameRefSeriesName") )
		 {
			 s = (String)plantTaxon.get("scientificNameRefSeriesName");
		 }
		 return(s);
	 }
	 
	 
	  /**
	 * method to set the volume of the scientific name reference
	 */
	 public void setScientificNameRefVolume(String longNameRefVolume)
	 {
		 plantTaxon.put("scientificNameRefVolume", longNameRefVolume );
	 }
	 
	   /**
	 * method to get 
	 */
	 public String getScientificNameRefVolume()
	 {
		 String s = null;
		 if ( plantTaxon.containsKey("scientificNameRefVolume") )
		 {
			 s = (String)plantTaxon.get("scientificNameRefVolume");
		 }
		 return(s);
	 }
	 
	 
	  /**
	 * method to set the page of the scientific name reference
	 */
	 public void setScientificNameRefPage(String longNameRefPage)
	 {
		 plantTaxon.put("scientificNameRefPage", longNameRefPage);
	 }
	 
	   /**
	 * method to get 
	 */
	 public String getScientificNameRefPage()
	 {
		 String s = null;
		 if ( plantTaxon.containsKey("scientificNameRefPage") )
		 {
			 s = (String)plantTaxon.get("scientificNameRefPage");
		 }
		 return(s);
	 }
	 
	 
	  /**
	 * method to set the ISSN of the scientific name reference
	 */
	 public void setScientificNameRefISSN(String longNameRefISBN)
	 {
		 plantTaxon.put("scientificNameRefISBN",longNameRefISBN );
	 }
	 
	   /**
	 * method to get 
	 */
	 public String getScientificNameRefISSN()
	 {
		 String s = null;
		 if ( plantTaxon.containsKey("scientificNameRefISBN") )
		 {
			 s = (String)plantTaxon.get("scientificNameRefISBN");
		 }
		 return(s);
	 }
	 
	 
	  /**
	 * method to set the ISBN of the scientific name reference
	 */
	 public void setScientificNameRefISBN(String longNameRefISSN)
	 {
		 plantTaxon.put("scientificNameRefISSN", longNameRefISSN);
	 }
	 
	   /**
	 * method to get 
	 */
	 public String getScientificNameRefISBN()
	 {
		 String s = null;
		 if ( plantTaxon.containsKey("scientificNameRefISSN") )
		 {
			 s = (String)plantTaxon.get("scientificNameRefISSN");
		 }
		 return(s);
	 }
	 
	 
	 /**
	 * method to set other citation details of the scientific name reference
	 */
	 public void setScientificNameRefOtherCitDetails(String longNameRefOtherCitDetails)
	 {
		 plantTaxon.put("scientificNameRefOtherCitDetails", longNameRefOtherCitDetails);
	 }
	 
	   /**
	 * method to get 
	 */
	 public String getScientificNameRefOtherCitDetails()
	 {
		 String s = null;
		 if ( plantTaxon.containsKey("scientificNameRefOtherCitDetails") )
		 {
			 s = (String)plantTaxon.get("scientificNameRefOtherCitDetails");
		 }
		 return(s);
	 }
	 
	 
	/**
	 * method to set the authors' name(s) of the common name reference
	 */
	 public void setCommonNameRefAuthors(String name )
	 {
		 plantTaxon.put("commonNameRefAuthors", name );
	 }
	  
		  /**
	 * method to get 
	 */
	 public String getCommonNameRefAuthors()
	 {
		 String s = null;
		 if ( plantTaxon.containsKey("commonNameRefAuthors") )
		 {
			 s = (String)plantTaxon.get("commonNameRefAuthors");
		 }
		 return(s);
	 }
	 
	 
		/**
	 * method to set the title of the common name reference
	 */
	 public void setCommonNameRefTitle(String name)
	 {
		 plantTaxon.put("commonNameRefTitle", name );
	 }
	 
	   /**
	 * method to get 
	 */
	 public String getCommonNameRefTitle()
	 {
		 String s = null;
		 if ( plantTaxon.containsKey("commonNameRefTitle") )
		 {
			 s = (String)plantTaxon.get("commonNameRefTitle");
		 }
		 return(s);
	 }
	 
	 
	 /**
	 * method to set the date of the common name reference
	 */
	 public void setCommonNameRefDate(String name)
	 {
		 plantTaxon.put("commonNameRefDate", name  );
	 }
	 
	   /**
	 * method to get 
	 */
	 public String getCommonNameRefDate()
	 {
		 String s = null;
		 if ( plantTaxon.containsKey("commonNameRefDate") )
		 {
			 s = (String)plantTaxon.get("commonNameRefDate");
		 }
		 return(s);
	 }
	 
	 
	  /**
	 * method to set the edition of the common name reference
	 */
	 public void setCommonNameRefEdition(String name )
	 {
		 plantTaxon.put("commonNameRefEdition", name );
	 }
	 
	   /**
	 * method to get 
	 */
	 public String getCommonNameRefEdition()
	 {
		 String s = null;
		 if ( plantTaxon.containsKey("commonNameRefEdition") )
		 {
			 s = (String)plantTaxon.get("commonNameRefEdition");
		 }
		 return(s);
	 }
	 
	 
	  /**
	 * method to set the series name of the common name reference
	 */
	 public void setCommonNameRefSeriesName(String name )
	 {
		 plantTaxon.put("commonNameRefSeriesName", name );
	 }
	 
	   /**
	 * method to get 
	 */
	 public String getCommonNameRefSeriesName()
	 {
		 String s = null;
		 if ( plantTaxon.containsKey("commonNameRefSeriesName") )
		 {
			 s = (String)plantTaxon.get("commonNameRefSeriesName");
		 }
		 return(s);
	 }
	 
	 
	  /**
	 * method to set the volume of the common name reference
	 */
	 public void setCommonNameRefVolume(String name)
	 {
		 plantTaxon.put("commonNameRefVolume", name  );
	 }
	 
	   /**
	 * method to get 
	 */
	 public String getCommonNameRefVolume()
	 {
		 String s = null;
		 if ( plantTaxon.containsKey("commonNameRefVolume") )
		 {
			 s = (String)plantTaxon.get("commonNameRefVolume");
		 }
		 return(s);
	 }
	 
	 
	  /**
	 * method to set the page of the common name reference
	 */
	 public void setCommonNameRefPage(String name)
	 {
		 plantTaxon.put("commonNameRefPage", name );
	 }
	 
	   /**
	 * method to get 
	 */
	 public String getCommonNameRefPage()
	 {
		 String s = null;
		 if ( plantTaxon.containsKey("commonNameRefPage") )
		 {
			 s = (String)plantTaxon.get("commonNameRefPage");
		 }
		 return(s);
	 }
	 
	 
	  /**
	 * method to set the ISSN of the common name reference
	 */
	 public void setCommonNameRefISSN(String name)
	 {
		 plantTaxon.put("commonNameRefISBN", name );
	 }
	 
	   /**
	 * method to get 
	 */
	 public String getCommonNameRefISSN()
	 {
		 String s = null;
		 if ( plantTaxon.containsKey("commonNameRefISBN") )
		 {
			 s = (String)plantTaxon.get("commonNameRefISBN");
		 }
		 return(s);
	 }
	 
	 
	  /**
	 * method to set the ISBN of the common name reference
	 */
	 public void setCommonNameRefISBN(String name)
	 {
		 plantTaxon.put("commonNameRefISSN",  name);
	 }
	 
	   /**
	 * method to get 
	 */
	 public String getCommonNameRefISBN()
	 {
		 String s = null;
		 if ( plantTaxon.containsKey("commonNameRefISSN") )
		 {
			 s = (String)plantTaxon.get("commonNameRefISSN");
		 }
		 return(s);
	 }
	 
	 
	  /**
	 * method to set other citation details of the common name reference
	 */
	 public void setCommonNameRefOtherCitDetails(String name )
	 {
		 plantTaxon.put("commonNameRefOtherCitDetails", name);
	 }
	 
	   /**
	 * method to get 
	 */
	 public String getCommonNameRefOtherCitDetails()
	 {
		 String s = null;
		 if ( plantTaxon.containsKey("commonNameRefOtherCitDetails") )
		 {
			 s = (String)plantTaxon.get("commonNameRefOtherCitDetails");
		 }
		 return(s);
	 }
	 
	 
	 /**
	 * method to set the authors' name(s) of the code reference
	 */
	 public void setCodeRefAuthors(String name )
	 {
		 plantTaxon.put("codeRefAuthors", name );
	 }
	 
	   /**
	 * method to get 
	 */
	 public String getCodeRefAuthors()
	 {
		 String s = null;
		 if ( plantTaxon.containsKey("codeRefAuthors") )
		 {
			 s = (String)plantTaxon.get("codeRefAuthors");
		 }
		 return(s);
	 }
	 
	 
	  /**
	 * method to set the title of the code reference
	 */
	 public void setCodeRefTitle(String name)
	 {
		 plantTaxon.put("codeRefTitle", name );
	 }
	 
	   /**
	 * method to get 
	 */
	 public String getCodeRefTitle()
	 {
		 String s = null;
		 if ( plantTaxon.containsKey("codeRefTitle") )
		 {
			 s = (String)plantTaxon.get("codeRefTitle");
		 }
		 return(s);
	 }
	 
	 
	   /**
	 * method to set the date of the code reference
	 */
	 public void setCodeRefDate(String name)
	 {
		 plantTaxon.put("codeRefDate", name  );
	 }
	 
	   /**
	 * method to get 
	 */
	 public String getCodeRefDate()
	 {
		 String s = null;
		 if ( plantTaxon.containsKey("codeRefDate") )
		 {
			 s = (String)plantTaxon.get("codeRefDate");
		 }
		 return(s);
	 }
	 
	 
	  /**
	 * method to set the edition of the code reference
	 */
	 public void setCodeRefEdition(String name )
	 {
		 plantTaxon.put("codeRefEdition", name );
	 }
	 
	   /**
	 * method to get 
	 */
	 public String getCodeRefEdition()
	 {
		 String s = null;
		 if ( plantTaxon.containsKey("codeRefEdition") )
		 {
			 s = (String)plantTaxon.get("codeRefEdition");
		 }
		 return(s);
	 }
	 
	 
	   /**
	 * method to set the series name of the code reference
	 */
	 public void setCodeRefSeriesName(String name )
	 {
		 plantTaxon.put("codeRefSeriesName", name );
	 }
	 
	   /**
	 * method to get 
	 */
	 public String getCodeRefSeriesName()
	 {
		 String s = null;
		 if ( plantTaxon.containsKey("codeRefSeriesName") )
		 {
			 s = (String)plantTaxon.get("codeRefSeriesName");
		 }
		 return(s);
	 }
	 
	 
	   /**
	 * method to set the volume of the code reference
	 */
	 public void setCodeRefVolume(String name)
	 {
		 plantTaxon.put("codeRefVolume", name  );
	 }
	 
	   /**
	 * method to get 
	 */
	 public String getCodeRefVolume()
	 {
		 String s = null;
		 if ( plantTaxon.containsKey("codeRefVolume") )
		 {
			 s = (String)plantTaxon.get("codeRefVolume");
		 }
		 return(s);
	 }
	 
	 
	  /**
	 * method to set the page of the code reference
	 */
	 public void setCodeRefPage(String name)
	 {
		 plantTaxon.put("codeRefPage", name );
	 }
	 
	   /**
	 * method to get 
	 */
	 public String getCodeRefPage()
	 {
		 String s = null;
		 if ( plantTaxon.containsKey("codeRefPage") )
		 {
			 s = (String)plantTaxon.get("codeRefPage");
		 }
		 return(s);
	 }
	 
	 
	   /**
	 * method to set the ISSN of the code reference
	 */
	 public void setCodeRefISSN(String name)
	 {
		 plantTaxon.put("codeRefISBN", name );
	 }
	 
	   /**
	 * method to get 
	 */
	 public String getCodeRefISSN()
	 {
		 String s = null;
		 if ( plantTaxon.containsKey("codeRefISBN") )
		 {
			 s = (String)plantTaxon.get("codeRefISBN");
		 }
		 return(s);
	 }
	 
	 
	  /**
	 * method to set the ISBN of the code reference
	 */
	 public void setCodeRefISBN(String name)
	 {
		 plantTaxon.put("codeRefISSN",  name);
	 }
	 
	   /**
	 * method to get 
	 */
	 public String getCodeRefISBN()
	 {
		 String s = null;
		 if ( plantTaxon.containsKey("codeRefISSN") )
		 {
			 s = (String)plantTaxon.get("codeRefISSN");
		 }
		 return(s);
	 }
	 
	 
	  /**
	 * method to set other citation details of the code reference
	 */
	 public void setCodeRefOtherCitDetails(String name )
	 {
		 plantTaxon.put("codeRefOtherCitDetails", name);
	 }
	 
	   /**
	 * method to get 
	 */
	 public String getCodeRefOtherCitDetails()
	 {
		 String s = null;
		 if ( plantTaxon.containsKey("codeRefOtherCitDetails") )
		 {
			 s = (String)plantTaxon.get("codeRefOtherCitDetails");
		 }
		 return(s);
	 }
	 
	 
	 
	 /**
	 * method to set the concept description
	 */
	 public void setConceptDescription(String conceptDescription)
	 {
		 plantTaxon.put("conceptDescription", conceptDescription );
	 }
	 
	   /**
	 * method to get 
	 */
	 public String getConceptDescription()
	 {
		 String s = null;
		 if ( plantTaxon.containsKey("conceptDescription") )
		 {
			 s = (String)plantTaxon.get("conceptDescription");
		 }
		 return(s);
	 }
	 
	 
	  /**
	 * method to set the authors names of the concept
	 */
	 public void setConceptRefAuthors(String conceptRefAuthors)
	 {
		 plantTaxon.put("conceptRefAuthors",conceptRefAuthors );
	 }
	 
	   /**
	 * method to get 
	 */
	 public String getConceptRefAuthors()
	 {
		 String s = null;
		 if ( plantTaxon.containsKey("conceptRefAuthors") )
		 {
			 s = (String)plantTaxon.get("conceptRefAuthors");
		 }
		 return(s);
	 }
	 
	 
	  /**
	 * method to set the title of the concept refererence
	 */
	 public void setConceptRefTitle(String conceptRefTitle)
	 {
		 plantTaxon.put("conceptRefTitle",conceptRefTitle );
	 }
	 
	   /**
	 * method to get 
	 */
	 public String getConceptRefTitle()
	 {
		 String s = null;
		 if ( plantTaxon.containsKey("conceptRefTitle") )
		 {
			 s = (String)plantTaxon.get("conceptRefTitle");
		 }
		 return(s);
	 }
	 
	 
	  /**
	 * method to set the date of the concept reference
	 */
	 public void setConceptRefDate(String conceptRefDate)
	 {
		 plantTaxon.put("conceptRefDate",conceptRefDate );
	 }
	 
	   /**
	 * method to get 
	 */
	 public String getConceptRefDate()
	 {
		 String s = null;
		 if ( plantTaxon.containsKey("conceptRefDate") )
		 {
			 s = (String)plantTaxon.get("conceptRefDate");
		 }
		 return(s);
	 }
	 
	 
	  /**
	 * method to set  the edition of the concept reference
	 */
	 public void setConceptRefEdition(String conceptRefEdition)
	 {
		 plantTaxon.put("conceptRefEdition",conceptRefEdition );
	 }
	 
	   /**
	 * method to get 
	 */
	 public String getConceptRefEdition()
	 {
		 String s = null;
		 if ( plantTaxon.containsKey("conceptRefEdition") )
		 {
			 s = (String)plantTaxon.get("conceptRefEdition");
		 }
		 return(s);
	 }
	 
	 
	  /**
	 * method to set  the series name of the concept reference
	 */
	 public void setConceptRefSeriesName(String conceptRefSeriesName)
	 {
		 plantTaxon.put("conceptRefSeriesName",conceptRefSeriesName );
	 }
	 
	   /**
	 * method to get 
	 */
	 public String getConceptRefSeriesName()
	 {
		 String s = null;
		 if ( plantTaxon.containsKey("conceptRefSeriesName") )
		 {
			 s = (String)plantTaxon.get("conceptRefSeriesName");
		 }
		 return(s);
	 }
	 
	 
	  /**
	 * method to set the volume number of the concept referennce 
	 */
	 public void setConceptRefVolume(String conceptRefVolume)
	 {
		 plantTaxon.put("conceptRefVolume",conceptRefVolume );
	 }
	 
	   /**
	 * method to get 
	 */
	 public String getConceptRefVolume()
	 {
		 String s = null;
		 if ( plantTaxon.containsKey("conceptRefVolume") )
		 {
			 s = (String)plantTaxon.get("conceptRefVolume");
		 }
		 return(s);
	 }
	 
	 
	  /**
	 * method to set page number of the concept refernce
	 */
	 public void setConceptRefPage(String conceptRefPage)
	 {
		 plantTaxon.put("conceptRefPage",conceptRefPage );
	 }
	 
	   /**
	 * method to get 
	 */
	 public String getConceptRefPage()
	 {
		 String s = null;
		 if ( plantTaxon.containsKey("conceptRefPage") )
		 {
			 s = (String)plantTaxon.get("conceptRefPage");
		 }
		 return(s);
	 }
	 
	 
	  /**
	 * method to set the ISSN of the concept reference
	 */
	 public void setConceptRefISSN(String conceptRefISSN)
	 {
		 plantTaxon.put("conceptRefISSN",conceptRefISSN );
	 }
	 
	   /**
	 * method to get 
	 */
	 public String getConceptRefISSN()
	 {
		 String s = null;
		 if ( plantTaxon.containsKey("conceptRefISSN") )
		 {
			 s = (String)plantTaxon.get("conceptRefISSN");
		 }
		 return(s);
	 }
	 
	 
	  /**
	 * method to set the isbn of the concept refernce
	 */
	 public void setConceptRefISBN(String conceptRefISBN)
	 {
		 plantTaxon.put("conceptRefISBN",conceptRefISBN );
	 }
	 
	   /**
	 * method to get 
	 */
	 public String getConceptRefISBN()
	 {
		 String s = null;
		 if ( plantTaxon.containsKey("conceptRefISBN") )
		 {
			 s = (String)plantTaxon.get("conceptRefISBN");
		 }
		 return(s);
	 }
	 
	 
	  /**
	 * method to set the other citation details for the concept reference
	 */
	 public void setConceptRefOtherCitDetails(String conceptRefOtherCitDetails)
	 {
		 plantTaxon.put("conceptRefOtherCitDetails", conceptRefOtherCitDetails);
	 }
	 
	   /**
	 * method to get 
	 */
	 public String getConceptRefOtherCitDetails()
	 {
		 String s = null;
		 if ( plantTaxon.containsKey("conceptRefOtherCitDetails") )
		 {
			 s = (String)plantTaxon.get("conceptRefOtherCitDetails");
		 }
		 return(s);
	 }
	 
	 
	  /**
	 * method to set name of the plants parent 
	 */
	 public void setPlantParentName(String plantParentName)
	 {
		 plantTaxon.put("plantParentName", plantParentName );
	 }
	 
	   /**
	 * method to get 
	 */
	 public String getPlantParentName()
	 {
		 String s = null;
		 if ( plantTaxon.containsKey("plantParentName") )
		 {
			 s = (String)plantTaxon.get("plantParentName");
		 }
		 return(s);
	 }
	 
	 
	  /**
	 * method to set the plant parents name reference title
	 */
	 public void setPlantParentRefTitle(String plantParentRefTitle)
	 {
		 plantTaxon.put("plantParentRefTitle", plantParentRefTitle );
	 }
	 
	   /**
	 * method to get 
	 */
	 public String getPlantParentRefTitle()
	 {
		 String s = null;
		 if ( plantTaxon.containsKey("plantParentRefTitle") )
		 {
			 s = (String)plantTaxon.get("plantParentRefTitle");
		 }
		 return(s);
	 }
	 
	 
	  /**
	 * method to set  the plant parents refernce authors 
	 */
	 public void setPlantParentRefAuthors(String plantParentRefAuthors)
	 {
		 plantTaxon.put("plantParentRefAuthors", plantParentRefAuthors );
	 }
	 
	   /**
	 * method to get 
	 */
	 public String getPlantParentRefAuthors()
	 {
		 String s = null;
		 if ( plantTaxon.containsKey("plantParentRefAuthors") )
		 {
			 s = (String)plantTaxon.get("plantParentRefAuthors");
		 }
		 return(s);
	 }
	 
	 
	 
	/**
	 * method to plants concept status
	 */
	 public void setConceptStatus(String conceptStatus)
	 {
		 plantTaxon.put("conceptStatus", conceptStatus  );
	 }
	 
	   /**
	 * method to get 
	 */
	 public String getConceptStatus()
	 {
		 String s = null;
		 if ( plantTaxon.containsKey("conceptStatus") )
		 {
			 s = (String)plantTaxon.get("conceptStatus");
		 }
		 return(s);
	 }
	 
	 
	 /**
	 * method to a plants status startdate
	 */
	 public void setStatusStartDate(String statusStartDate)
	 {
		 plantTaxon.put("statusStartDate", statusStartDate  );
	 }
	 
	   /**
	 * method to get 
	 */
	 public String getStatusStartDate()
	 {
		 String s = null;
		 if ( plantTaxon.containsKey("statusStartDate") )
		 {
			 s = (String)plantTaxon.get("statusStartDate");
		 }
		 return(s);
	 }
	 
	 
	 /**
	 * method to a plants status stop date
	 */
	 public void setStatusStopDate(String statusStopDate)
	 {
		 plantTaxon.put("statusStopDate",  statusStopDate);
	 }
	 
	   /**
	 * method to get 
	 */
	 public String getStatusStopDate()
	 {
		 String s = null;
		 if ( plantTaxon.containsKey("statusStopDate") )
		 {
			 s = (String)plantTaxon.get("statusStopDate");
		 }
		 return(s);
	 }
	 
	 
	 /**
	 * method to set a plants usage start date
	 */
	 public void setUsageStartDate(String usageStartDate)
	 {
		 plantTaxon.put("usageStartDate", usageStartDate  );
	 }
	 
	   /**
	 * method to get 
	 */
	 public String getUsageStartDate()
	 {
		 String s = null;
		 if ( plantTaxon.containsKey("usageStartDate") )
		 {
			 s = (String)plantTaxon.get("usageStartDate");
		 }
		 return(s);
	 }
	 
	 
	 /**
	 * method to set a plants usage stop date
	 */
	 public void setUsageStopDate(String usageStopDate)
	 {
		 plantTaxon.put("usageStopDate", usageStopDate  );
	 }
	 
	   /**
	 * method to get 
	 */
	 public String getUsageStopDate()
	 {
		 String s = null;
		 if ( plantTaxon.containsKey("usageStopDate") )
		 {
			 s = (String)plantTaxon.get("usageStopDate");
		 }
		 return(s);
	 }
	 
	 
	 /**
	 * method to set a plants status description
	 */
	 public void setStatusDescription(String statusDescription)
	 {
		 plantTaxon.put("statusDescription", statusDescription  );
	 }
	 
	 /**
	 * method to get 
	 */
	 public String getStatusDescription()
	 {
		 String s = null;
		 if ( plantTaxon.containsKey("statusDescription") )
		 {
			 s = (String)plantTaxon.get("statusDescription");
		 }
		 return(s);
	 }
	 
	 
	 /**
	 * method to set a plants taxonomic level 
	 */
	 public void setTaxonLevel(String taxonLevel)
	 {
		 plantTaxon.put("taxonLevel",  taxonLevel );
	 }
	 
	   /**
	 * method to get 
	 */
	 public String getTaxonLevel()
	 {
		 String s = null;
		 if ( plantTaxon.containsKey("taxonLevel") )
		 {
			 s = (String)plantTaxon.get("taxonLevel");
		 }
		 return(s);
	 }
	 
	 
	 
	 /**
	  * method that returns true if the planttaxon is valid and 
		* that it can be loaded to the database
		*/
		public boolean isValid()
		{
			System.out.println("PlantTaxon > values: " + plantTaxon.toString()  );
			return(true);
		}
		
		/**
		 * this method returns the plant taxon elements in a hashtable 
		 *
		 */
		 public Hashtable getPlantTaxonHash()
		 {
			 System.out.println("PlantTaxon > returning the plant elements in a hashtable ");
			 return( this.plantTaxon );
		 }

}
