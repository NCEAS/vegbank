package org.vegbank.common.utility;

/*
 * '$RCSfile: XMLUtil.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: berkley $'
 *	'$Date: 2006-07-14 22:07:09 $'
 *	'$Revision: 1.8 $'
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
 

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.sql.*;

import org.vegbank.common.model.VBModelBean;
import org.vegbank.plots.datasource.DBModelBeanReader;

/**
 * @author farrell
 *
 * A utility class for handling XML
 */
public class XMLUtil
{

	/**
	 * Takes a collection VegbankModelBeans and returns an XML String
	 * 
	 * @param Objects
	 * @return
	 */
	public static String getVBXML( Collection vegbankModelBeans)
	{
		System.out.println("here in getVBXML( Collection ) ");
		// Stringbuffer for constructing XML String
		StringBuffer sb = new StringBuffer();
		
		// TODO: Need a sort here to ensure schema order is followed
		Iterator iterator = vegbankModelBeans.iterator();
		while ( iterator.hasNext() )
		{
      //System.out.println("iterating through vegbankModelBeans");
			VBModelBean vbmb = (VBModelBean) iterator.next();
      //System.out.println("creating xml: " + vbmb.toXML());
      String xml;
      //first check to see if the xml is cached in the database, if not
      //then generate it from the bean.
      try
      {
        DBConnection conn = null;
        conn = DBConnectionPool.getInstance().getDBConnection("Need " +
          "connection for getting cached xml");
        conn.setAutoCommit(true);
        String accCode = vbmb.getAccessioncode();
        String sql = "select xml from dba_xmlcache where accessioncode like ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, accCode);
        ResultSet rs = ps.executeQuery();
        if(rs.next())
        {
          byte[] b = rs.getBytes(1);
          xml = new String(b);
          System.out.println("Got cached xml");
          rs.close();
        }
        else
        {
          System.out.println("got xml from bean");
          xml = vbmb.toXML();
        }
        DBConnectionPool.returnDBConnection(conn);
      }
      catch(Exception e)
      {
        System.out.println("error generating xml: " + e.getMessage());
        e.printStackTrace();
        xml = vbmb.toXML();
      }
      
			sb.append(xml);
		}
		String entireXML = wrapInBoilerPlateXML(sb); 	
		return entireXML;
	}
	

	/**
	 * Convience method when only one VBModelBean to XMLify
	 * 
	 * @param vbmb
	 * @return
	 */
	public static String getVBXML( VBModelBean  vbmb)
	{
		System.out.println("here in getVBXML( VBModelBean ) ");
		Collection c = new ArrayList();
		c.add(vbmb);
		return getVBXML(c);
	}
	
	/**
	 * Wrap StringBuffer in boilerplate XML.
	 * 
	 * @param StringBuffer -- get wrapped in boilerplate 
	 */
	private  static String wrapInBoilerPlateXML( StringBuffer sb)
	{
		StringBuffer fullXML = new StringBuffer();
		
		// TODO: This varible need to be configurable
		String schemaWebLocation = "http://vegbank.org/vegdocs/xml/";
		
		fullXML.append("<?xml version='1.0' encoding='UTF-8'?>\n");
		// TODO: Delaring schema to use here, may need to allow refernce to schema and no refernce
		fullXML.append("<VegBankPackage xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"" + schemaWebLocation + Utility.VEGBANK_SCHEMA_NAME + "\">\n" );
		 
		fullXML.append("\t<doc-VegBankVersion>" + Utility.VEGBANK_VERSION + "</doc-VegBankVersion>\n");
		fullXML.append("\t<doc-date>" + convertDateToXSdatetime(Utility.getNow()) + "</doc-date>\n");
		// TODO: Implement a getCurrentPartyFullName() method and/or pass in value 
		fullXML.append("\t<doc-author>" + "Gabriel Farrell" + "</doc-author>\n"); 
		fullXML.append("\t<doc-authorSoftware>Vegbank, version: " + Utility.VEGBANK_VERSION + "</doc-authorSoftware>\n");
		// TODO: Add relevant comments 
		fullXML.append("\t<doc-comments></doc-comments>\n");

		// Add the true content
		fullXML.append( sb.toString() );
		
		// Close up shop
		fullXML.append("</VegBankPackage>");

		// Overwrite the incomming sb with the fullXML sb
		return  fullXML.toString();
	}

	
	/**
	 * Convert Java Date object to a string that is a valid xml schema datatime datatype
	 * 
	 * @param Date -- the java date to convert
	 * @return String -- the date in valid xs datetime syntax
	 */
	public static String convertDateToXSdatetime(Date date)
	{
		String xsDateTimePattern = "yyyy-MM-dd'T'HH:mm:ss";
		SimpleDateFormat df = new SimpleDateFormat(xsDateTimePattern);
		//LogUtility.log("Utility: " + df.format(date));
		return df.format(date);
	}

  /**
   * main method for running xml utilities on the command line
   */
  public static void main(String[] args)
  {
    //right now the only thing this does is create cached xml from the existing
    //database structure.  
    //process: 1) get accession codes
    //         2) check to see if the code already exists in the dba_xmlcache table
    //         3) if it does, don'd do anything
    //         4) if it doesn't then create a bean from that accession code
    //         5) run bean.toXML() and put the result into the dba_xmlcache table
    //         6) commit the change
    
    String sql[] = new String[5];
    sql[0] = "select accessioncode from observation";
    sql[1] = "select accessioncode from plantconcept";
    sql[2] = "select accessioncode from commconcept";
    sql[3] = "select accessioncode from project";
    sql[4] = "select accessioncode from party";
    //if you want to check for accession codes in more tables, add the sql here
    
    try
    {
      String accCode = "";
      DBModelBeanReader beanReader = new DBModelBeanReader();
      DBConnection conn = null;
        conn = DBConnectionPool.getInstance().getDBConnection("Need " +
          "connection for caching xml");
        conn.setAutoCommit(true);
      for(int i=0; i<sql.length; i++)
      {
        System.out.println("getting accession codes: " + sql[i]);
        PreparedStatement ps = conn.prepareStatement(sql[i]);
        ResultSet rs = ps.executeQuery();
        while(rs.next())
        { //go through each accesssion code and create the xml if it's not
          //already in the dba_xmlcache table
          try
          {
            accCode = rs.getString(1);
            System.out.println("Checking for cached xml for accCode: " + accCode);
            String checkSQL = "select accessioncode from dba_xmlcache where " +
              "accessioncode = ?";
            PreparedStatement ps2 = conn.prepareStatement(checkSQL);
            ps2.setString(1, accCode);
            ResultSet rs2 = ps2.executeQuery();
            if(!rs2.next())
            { //it's not in the cache table so get the xml and add it
              System.out.println("XML not already in cache for accCode: " + accCode);
              VBModelBean bean = beanReader.getVBModelBean(accCode);
              if(bean == null)
              { //if there is no bean available for the accCode, ignore it
                System.out.println("No bean available for accCode: " + accCode);
                continue;
              }
              else
              {
                System.out.println("Found bean for accCode: " + accCode);
              }
              
              String xml = bean.toXML();
              System.out.println("Caching xml for accCode " + accCode);
              //got the xml, now cache it
              String insertSQL = "insert into dba_xmlcache (accessioncode, xml) " +
                "values (?, ?)";
              PreparedStatement ps3 = conn.prepareStatement(insertSQL);
              ps3.setString(1, accCode);
              ps3.setBytes(2, xml.getBytes());
              int rowcount = ps3.executeUpdate();
              if(rowcount != 1)
              {
                System.out.println("ERROR, xml for acc code " + accCode + " not " +
                  "correctly inserted: " + ps3.toString());
              }
              else
              {
                System.out.println("XML successfully cached for accCode: " + accCode);
              }
            }
            else
            {
              System.out.println("XML already cached for " + accCode);
            }
            rs2.close();
          }
          catch(Exception ee)
          {
            System.out.println("******Skipping accCode: " + accCode);
            System.out.println("Error: " + ee.getMessage());
          }
          System.out.println();
        }
        rs.close();
      }
      DBConnectionPool.returnDBConnection(conn);
      System.out.println("******Finished caching xml**********");
    }
    catch(Exception e)
    {
      System.out.println("Error caching xml from beans: " + e.getMessage());
      e.printStackTrace();
      System.exit(0);
    }
  }
}
