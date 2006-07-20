package org.vegbank.common.utility;

/*
 * '$RCSfile: XMLUtil.java,v $'
 *	Authors: @author@
 *	Release: @release@
 *
 *	'$Author: berkley $'
 *	'$Date: 2006-07-20 21:58:51 $'
 *	'$Revision: 1.10 $'
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
import java.util.Vector;
import java.sql.*;
import java.io.*;

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

    Vector excludesList = new Vector();
    if(args.length != 0)
    { //allow the user to pass the excludes list in via the command line
      for(int i=0; i<args.length; i++)
      {
        excludesList.addElement(args[i]);
      }
    }
    else
    {
      excludesList.addElement(new String("VB.Ob.26164.044060608"));
      excludesList.addElement(new String("VB.Ob.26085.027070408"));
      excludesList.addElement(new String("VB.Ob.26155.044040609"));
      excludesList.addElement(new String("VB.Ob.26105.027070564"));
      excludesList.addElement(new String("VB.Ob.25665.SHNP574"));
      excludesList.addElement(new String("VB.Ob.26154.044040608"));
      excludesList.addElement(new String("VB.Ob.25612.SHNP521"));
      excludesList.addElement(new String("VB.Ob.25636.SHNP545"));
      excludesList.addElement(new String("VB.Ob.25618.SHNP527"));
      excludesList.addElement(new String("VB.Ob.25602.SHNP510"));
      excludesList.addElement(new String("VB.Ob.25553.SHNP093"));
      excludesList.addElement(new String("VB.Ob.25558.SHNP098"));
      excludesList.addElement(new String("VB.Ob.25561.SHNP101"));
      excludesList.addElement(new String("VB.Ob.25562.SHNP102"));
      excludesList.addElement(new String("VB.Ob.25563.SHNP103"));
      excludesList.addElement(new String("VB.Ob.25590.SHNP130"));
      excludesList.addElement(new String("VB.Ob.25601.SHNP509"));
      excludesList.addElement(new String("VB.Ob.25626.SHNP535"));
      excludesList.addElement(new String("VB.Ob.25642.SHNP551"));
      excludesList.addElement(new String("VB.Ob.25647.SHNP556"));
      excludesList.addElement(new String("VB.Ob.25655.SHNP564"));
      excludesList.addElement(new String("VB.Ob.25674.SHNP583"));
      excludesList.addElement(new String("VB.Ob.25665.SHNP574"));
      excludesList.addElement(new String("VB.Ob.25632.SHNP541"));
      excludesList.addElement(new String("VB.Ob.25612.SHNP521"));
      excludesList.addElement(new String("VB.Ob.25636.SHNP545"));
      excludesList.addElement(new String("VB.Ob.25618.SHNP527"));
      excludesList.addElement(new String("VB.Ob.25602.SHNP510"));
      excludesList.addElement(new String("VB.Ob.25620.SHNP529"));
      excludesList.addElement(new String("VB.Ob.25604.SHNP513"));
      excludesList.addElement(new String("VB.Ob.25553.SHNP093"));
      excludesList.addElement(new String("VB.Ob.25558.SHNP098"));
      excludesList.addElement(new String("VB.Ob.25561.SHNP101"));
      excludesList.addElement(new String("VB.Ob.25562.SHNP102"));
      excludesList.addElement(new String("VB.Ob.25563.SHNP103"));
      excludesList.addElement(new String("VB.Ob.25590.SHNP130"));
      excludesList.addElement(new String("VB.Ob.25601.SHNP509"));
      excludesList.addElement(new String("VB.Ob.25603.SHNP511"));
      excludesList.addElement(new String("VB.Ob.25606.SHNP515"));
      excludesList.addElement(new String("VB.Ob.25615.SHNP524"));
      excludesList.addElement(new String("VB.Ob.25626.SHNP535"));
      excludesList.addElement(new String("VB.Ob.25642.SHNP551"));
      excludesList.addElement(new String("VB.Ob.25647.SHNP556"));
      excludesList.addElement(new String("VB.Ob.25655.SHNP564"));
      excludesList.addElement(new String("VB.Ob.25674.SHNP583"));
      excludesList.addElement(new String("VB.Ob.25679.SHNP588"));
      excludesList.addElement(new String("VB.Ob.25682.SHNP591"));
      excludesList.addElement(new String("VB.Ob.25704.SHNP613"));
      excludesList.addElement(new String("VB.Ob.25740.SHNP649"));
      excludesList.addElement(new String("VB.Ob.25746.SHNP655"));
      excludesList.addElement(new String("VB.Ob.25747.SHNP656"));
      excludesList.addElement(new String("VB.Ob.25755.SHNP664"));
      excludesList.addElement(new String("VB.Ob.25756.SHNP665"));
      excludesList.addElement(new String("VB.Ob.25757.SHNP666"));
      excludesList.addElement(new String("VB.Ob.25759.SHNP668"));
      excludesList.addElement(new String("VB.Ob.25760.SHNP669"));
      excludesList.addElement(new String("VB.Ob.25769.SHNP678"));
      excludesList.addElement(new String("VB.Ob.25770.SHNP679"));
      excludesList.addElement(new String("VB.Ob.25611.SHNP520"));
      excludesList.addElement(new String("VB.Ob.25631.SHNP540"));
      excludesList.addElement(new String("VB.Ob.25766.SHNP675"));
      excludesList.addElement(new String("VB.Ob.25545.SHNP085"));
      excludesList.addElement(new String("VB.Ob.25660.SHNP569"));
      excludesList.addElement(new String("VB.Ob.25465.SHNP005"));
      excludesList.addElement(new String("VB.Ob.25466.SHNP006"));
      excludesList.addElement(new String("VB.Ob.25471.SHNP011"));
      excludesList.addElement(new String("VB.Ob.25488.SHNP028"));
      excludesList.addElement(new String("VB.Ob.25489.SHNP029"));
      excludesList.addElement(new String("VB.Ob.25490.SHNP030"));
      excludesList.addElement(new String("VB.Ob.25494.SHNP034"));
      excludesList.addElement(new String("VB.Ob.25507.SHNP047"));
      excludesList.addElement(new String("VB.Ob.25537.SHNP077"));
      excludesList.addElement(new String("VB.Ob.25538.SHNP078"));
      excludesList.addElement(new String("VB.Ob.25539.SHNP079"));
      excludesList.addElement(new String("VB.Ob.25541.SHNP081"));
      excludesList.addElement(new String("VB.Ob.25543.SHNP083"));
      excludesList.addElement(new String("VB.Ob.25544.SHNP084"));
      excludesList.addElement(new String("VB.Ob.25546.SHNP086"));
      excludesList.addElement(new String("VB.Ob.25547.SHNP087"));
      excludesList.addElement(new String("VB.Ob.25548.SHNP088"));
      excludesList.addElement(new String("VB.Ob.25549.SHNP089"));
      excludesList.addElement(new String("VB.Ob.25551.SHNP091"));
      excludesList.addElement(new String("VB.Ob.25552.SHNP092"));
      excludesList.addElement(new String("VB.Ob.25554.SHNP094"));
      excludesList.addElement(new String("VB.Ob.25556.SHNP096"));
      excludesList.addElement(new String("VB.Ob.25638.SHNP547"));
      excludesList.addElement(new String("VB.Ob.25721.SHNP630"));
      excludesList.addElement(new String("VB.Ob.25688.SHNP597"));
      excludesList.addElement(new String("VB.Ob.25641.SHNP550"));
      excludesList.addElement(new String("VB.Ob.25640.SHNP549"));
      excludesList.addElement(new String("VB.Ob.25469.SHNP009"));
      excludesList.addElement(new String("VB.Ob.25560.SHNP100"));
      excludesList.addElement(new String("VB.Ob.25646.SHNP555"));
      excludesList.addElement(new String("VB.Ob.25464.SHNP004"));
      excludesList.addElement(new String("VB.Ob.25609.SHNP518"));
      excludesList.addElement(new String("VB.Ob.25652.SHNP561"));
      excludesList.addElement(new String("VB.Ob.25610.SHNP519"));
      excludesList.addElement(new String("VB.Ob.25624.SHNP533"));
      excludesList.addElement(new String("VB.Ob.25700.SHNP609"));
      excludesList.addElement(new String("VB.Ob.25680.SHNP589"));
      excludesList.addElement(new String("VB.Ob.25685.SHNP594"));
      excludesList.addElement(new String("VB.Ob.25708.SHNP617"));
      excludesList.addElement(new String("VB.Ob.25725.SHNP634"));
      excludesList.addElement(new String("VB.Ob.25613.SHNP522"));
      excludesList.addElement(new String("VB.Ob.25720.SHNP629"));
      excludesList.addElement(new String("VB.Ob.25726.SHNP635"));
      excludesList.addElement(new String("VB.Ob.25719.SHNP628"));
      excludesList.addElement(new String("VB.Ob.25752.SHNP661"));
      excludesList.addElement(new String("VB.Ob.25754.SHNP663"));
      excludesList.addElement(new String("VB.Ob.25470.SHNP010"));
      excludesList.addElement(new String("VB.Ob.25648.SHNP557"));
      excludesList.addElement(new String("VB.Ob.25658.SHNP567"));
      excludesList.addElement(new String("VB.Ob.25673.SHNP582"));
      excludesList.addElement(new String("VB.Ob.25758.SHNP667"));
      excludesList.addElement(new String("VB.Ob.25511.SHNP051"));
      excludesList.addElement(new String("VB.Ob.25745.SHNP654"));
      excludesList.addElement(new String("VB.Ob.25524.SHNP064"));
      excludesList.addElement(new String("VB.Ob.25462.SHNP002"));
      excludesList.addElement(new String("VB.Ob.25463.SHNP003"));
      excludesList.addElement(new String("VB.Ob.25467.SHNP007"));
      excludesList.addElement(new String("VB.Ob.25521.SHNP061"));
      excludesList.addElement(new String("VB.Ob.25472.SHNP012"));
      excludesList.addElement(new String("VB.Ob.25473.SHNP013"));
      excludesList.addElement(new String("VB.Ob.25474.SHNP014"));
      excludesList.addElement(new String("VB.Ob.25475.SHNP015"));
      excludesList.addElement(new String("VB.Ob.25476.SHNP016"));
      excludesList.addElement(new String("VB.Ob.25477.SHNP017"));
      excludesList.addElement(new String("VB.Ob.25478.SHNP018"));
      excludesList.addElement(new String("VB.Ob.25479.SHNP019"));
      excludesList.addElement(new String("VB.Ob.25480.SHNP020"));
      excludesList.addElement(new String("VB.Ob.25481.SHNP021"));
      excludesList.addElement(new String("VB.Ob.25482.SHNP022"));
      excludesList.addElement(new String("VB.Ob.25483.SHNP023"));
      excludesList.addElement(new String("VB.Ob.25484.SHNP024"));
      excludesList.addElement(new String("VB.Ob.25485.SHNP025"));
      excludesList.addElement(new String("VB.Ob.25486.SHNP026"));
      excludesList.addElement(new String("VB.Ob.25487.SHNP027"));
      excludesList.addElement(new String("VB.Ob.25491.SHNP031"));
      excludesList.addElement(new String("VB.Ob.25492.SHNP032"));
      excludesList.addElement(new String("VB.Ob.25493.SHNP033"));
      excludesList.addElement(new String("VB.Ob.25495.SHNP035"));
      excludesList.addElement(new String("VB.Ob.25496.SHNP036"));
      excludesList.addElement(new String("VB.Ob.25497.SHNP037"));
      excludesList.addElement(new String("VB.Ob.25498.SHNP038"));
      excludesList.addElement(new String("VB.Ob.25499.SHNP039"));
      excludesList.addElement(new String("VB.Ob.25500.SHNP040"));
      excludesList.addElement(new String("VB.Ob.25501.SHNP041"));
      excludesList.addElement(new String("VB.Ob.25502.SHNP042"));
      excludesList.addElement(new String("VB.Ob.25503.SHNP043"));
      excludesList.addElement(new String("VB.Ob.25504.SHNP044"));
      excludesList.addElement(new String("VB.Ob.25505.SHNP045"));
      excludesList.addElement(new String("VB.Ob.25506.SHNP046"));
      excludesList.addElement(new String("VB.Ob.25508.SHNP048"));
      excludesList.addElement(new String("VB.Ob.25509.SHNP049"));
      excludesList.addElement(new String("VB.Ob.25510.SHNP050"));
      excludesList.addElement(new String("VB.Ob.25512.SHNP052"));
      excludesList.addElement(new String("VB.Ob.25513.SHNP053"));
      excludesList.addElement(new String("VB.Ob.25514.SHNP054"));
      excludesList.addElement(new String("VB.Ob.25515.SHNP055"));
      excludesList.addElement(new String("VB.Ob.25516.SHNP056"));
      excludesList.addElement(new String("VB.Ob.25517.SHNP057"));
      excludesList.addElement(new String("VB.Ob.25518.SHNP058"));
      excludesList.addElement(new String("VB.Ob.25519.SHNP059"));
      excludesList.addElement(new String("VB.Ob.25520.SHNP060"));
      excludesList.addElement(new String("VB.Ob.25522.SHNP062"));
      excludesList.addElement(new String("VB.Ob.25523.SHNP063"));
      excludesList.addElement(new String("VB.Ob.25525.SHNP065"));
      excludesList.addElement(new String("VB.Ob.25526.SHNP066"));
      excludesList.addElement(new String("VB.Ob.25527.SHNP067"));
      excludesList.addElement(new String("VB.Ob.25528.SHNP068"));
      excludesList.addElement(new String("VB.Ob.25529.SHNP069"));
      excludesList.addElement(new String("VB.Ob.25530.SHNP070"));
      excludesList.addElement(new String("VB.Ob.25531.SHNP071"));
      excludesList.addElement(new String("VB.Ob.25532.SHNP072"));
      excludesList.addElement(new String("VB.Ob.25533.SHNP073"));
      excludesList.addElement(new String("VB.Ob.25534.SHNP074"));
      excludesList.addElement(new String("VB.Ob.25535.SHNP075"));
      excludesList.addElement(new String("VB.Ob.25536.SHNP076"));
      excludesList.addElement(new String("VB.Ob.25540.SHNP080"));
      excludesList.addElement(new String("VB.Ob.25542.SHNP082"));
      excludesList.addElement(new String("VB.Ob.25550.SHNP090"));
      excludesList.addElement(new String("VB.Ob.25555.SHNP095"));
      excludesList.addElement(new String("VB.Ob.25557.SHNP097"));
      excludesList.addElement(new String("VB.Ob.25559.SHNP099"));
      excludesList.addElement(new String("VB.Ob.25564.SHNP104"));
      excludesList.addElement(new String("VB.Ob.25565.SHNP105"));
      excludesList.addElement(new String("VB.Ob.25566.SHNP106"));
      excludesList.addElement(new String("VB.Ob.25567.SHNP107"));
      excludesList.addElement(new String("VB.Ob.25568.SHNP108"));
      excludesList.addElement(new String("VB.Ob.25569.SHNP109"));
      excludesList.addElement(new String("VB.Ob.25570.SHNP110"));
      excludesList.addElement(new String("VB.Ob.25571.SHNP111"));
      excludesList.addElement(new String("VB.Ob.25572.SHNP112"));
      excludesList.addElement(new String("VB.Ob.25573.SHNP113"));
      excludesList.addElement(new String("VB.Ob.25574.SHNP114"));
      excludesList.addElement(new String("VB.Ob.25575.SHNP115"));
      excludesList.addElement(new String("VB.Ob.25576.SHNP116"));
      excludesList.addElement(new String("VB.Ob.25577.SHNP117"));
      excludesList.addElement(new String("VB.Ob.25578.SHNP118"));
      excludesList.addElement(new String("VB.Ob.25579.SHNP119"));
      excludesList.addElement(new String("VB.Ob.25580.SHNP120"));
      excludesList.addElement(new String("VB.Ob.25581.SHNP121"));
      excludesList.addElement(new String("VB.Ob.25582.SHNP122"));
      excludesList.addElement(new String("VB.Ob.25583.SHNP123"));
      excludesList.addElement(new String("VB.Ob.25584.SHNP124"));
      excludesList.addElement(new String("VB.Ob.25585.SHNP125"));
      excludesList.addElement(new String("VB.Ob.25586.SHNP126"));
      excludesList.addElement(new String("VB.Ob.25587.SHNP127"));
      excludesList.addElement(new String("VB.Ob.25588.SHNP128"));
      excludesList.addElement(new String("VB.Ob.25589.SHNP129"));
      excludesList.addElement(new String("VB.Ob.25591.SHNP131"));
      excludesList.addElement(new String("VB.Ob.25592.SHNP132"));
      excludesList.addElement(new String("VB.Ob.25593.SHNP133"));
      excludesList.addElement(new String("VB.Ob.25594.SHNP501"));
      excludesList.addElement(new String("VB.Ob.25595.SHNP502"));
      excludesList.addElement(new String("VB.Ob.25596.SHNP503"));
      excludesList.addElement(new String("VB.Ob.25597.SHNP504"));
      excludesList.addElement(new String("VB.Ob.25600.SHNP508"));
      excludesList.addElement(new String("VB.Ob.25605.SHNP514"));
      excludesList.addElement(new String("VB.Ob.25607.SHNP516"));
      excludesList.addElement(new String("VB.Ob.25608.SHNP517"));
      excludesList.addElement(new String("VB.Ob.25614.SHNP523"));
      excludesList.addElement(new String("VB.Ob.25616.SHNP525"));
      excludesList.addElement(new String("VB.Ob.25617.SHNP526"));
      excludesList.addElement(new String("VB.Ob.25619.SHNP528"));
      excludesList.addElement(new String("VB.Ob.25622.SHNP531"));
      excludesList.addElement(new String("VB.Ob.25623.SHNP532"));
      excludesList.addElement(new String("VB.Ob.25625.SHNP534"));
      excludesList.addElement(new String("VB.Ob.25627.SHNP536"));
      excludesList.addElement(new String("VB.Ob.25628.SHNP537"));
      excludesList.addElement(new String("VB.Ob.25629.SHNP538"));
      excludesList.addElement(new String("VB.Ob.25630.SHNP539"));
      excludesList.addElement(new String("VB.Ob.25633.SHNP542"));
      excludesList.addElement(new String("VB.Ob.25634.SHNP543"));
      excludesList.addElement(new String("VB.Ob.25635.SHNP544"));
      excludesList.addElement(new String("VB.Ob.25639.SHNP548"));
      excludesList.addElement(new String("VB.Ob.25644.SHNP553"));
      excludesList.addElement(new String("VB.Ob.25645.SHNP554"));
      excludesList.addElement(new String("VB.Ob.25649.SHNP558"));
      excludesList.addElement(new String("VB.Ob.25650.SHNP559"));
      excludesList.addElement(new String("VB.Ob.25651.SHNP560"));
      excludesList.addElement(new String("VB.Ob.25654.SHNP563"));
      excludesList.addElement(new String("VB.Ob.25656.SHNP565"));
      excludesList.addElement(new String("VB.Ob.25657.SHNP566"));
      excludesList.addElement(new String("VB.Ob.25659.SHNP568"));
      excludesList.addElement(new String("VB.Ob.25661.SHNP570"));
      excludesList.addElement(new String("VB.Ob.25662.SHNP571"));
      excludesList.addElement(new String("VB.Ob.25664.SHNP573"));
      excludesList.addElement(new String("VB.Ob.25666.SHNP575"));
      excludesList.addElement(new String("VB.Ob.25667.SHNP576"));
      excludesList.addElement(new String("VB.Ob.25668.SHNP577"));
      excludesList.addElement(new String("VB.Ob.25669.SHNP578"));
      excludesList.addElement(new String("VB.Ob.25670.SHNP579"));
      excludesList.addElement(new String("VB.Ob.25671.SHNP580"));
      excludesList.addElement(new String("VB.Ob.25672.SHNP581"));
      excludesList.addElement(new String("VB.Ob.25675.SHNP584"));
      excludesList.addElement(new String("VB.Ob.25676.SHNP585"));
      excludesList.addElement(new String("VB.Ob.25678.SHNP587"));
      excludesList.addElement(new String("VB.Ob.25681.SHNP590"));
      excludesList.addElement(new String("VB.Ob.25683.SHNP592"));
      excludesList.addElement(new String("VB.Ob.25684.SHNP593"));
      excludesList.addElement(new String("VB.Ob.25686.SHNP595"));
      excludesList.addElement(new String("VB.Ob.25687.SHNP596"));
      excludesList.addElement(new String("VB.Ob.25689.SHNP598"));
      excludesList.addElement(new String("VB.Ob.25690.SHNP599"));
      excludesList.addElement(new String("VB.Ob.25692.SHNP601"));
      excludesList.addElement(new String("VB.Ob.25693.SHNP602"));
      excludesList.addElement(new String("VB.Ob.25694.SHNP603"));
      excludesList.addElement(new String("VB.Ob.25695.SHNP604"));
      excludesList.addElement(new String("VB.Ob.25696.SHNP605"));
      excludesList.addElement(new String("VB.Ob.25699.SHNP608"));
      excludesList.addElement(new String("VB.Ob.25701.SHNP610"));
      excludesList.addElement(new String("VB.Ob.25702.SHNP611"));
      excludesList.addElement(new String("VB.Ob.25703.SHNP612"));
      excludesList.addElement(new String("VB.Ob.25706.SHNP615"));
      excludesList.addElement(new String("VB.Ob.25707.SHNP616"));
      excludesList.addElement(new String("VB.Ob.25709.SHNP618"));
      excludesList.addElement(new String("VB.Ob.25710.SHNP619"));
      excludesList.addElement(new String("VB.Ob.25711.SHNP620"));
      excludesList.addElement(new String("VB.Ob.25712.SHNP621"));
      excludesList.addElement(new String("VB.Ob.25713.SHNP622"));
      excludesList.addElement(new String("VB.Ob.25714.SHNP623"));
      excludesList.addElement(new String("VB.Ob.25715.SHNP624"));
      excludesList.addElement(new String("VB.Ob.25716.SHNP625"));
      excludesList.addElement(new String("VB.Ob.25717.SHNP626"));
      excludesList.addElement(new String("VB.Ob.25718.SHNP627"));
      excludesList.addElement(new String("VB.Ob.25722.SHNP631"));
      excludesList.addElement(new String("VB.Ob.25723.SHNP632"));
      excludesList.addElement(new String("VB.Ob.25724.SHNP633"));
      excludesList.addElement(new String("VB.Ob.25727.SHNP636"));
      excludesList.addElement(new String("VB.Ob.25728.SHNP637"));
      excludesList.addElement(new String("VB.Ob.25729.SHNP638"));
      excludesList.addElement(new String("VB.Ob.25730.SHNP639"));
      excludesList.addElement(new String("VB.Ob.25731.SHNP640"));
      excludesList.addElement(new String("VB.Ob.25732.SHNP641"));
      excludesList.addElement(new String("VB.Ob.25733.SHNP642"));
      excludesList.addElement(new String("VB.Ob.25734.SHNP643"));
      excludesList.addElement(new String("VB.Ob.25735.SHNP644"));
      excludesList.addElement(new String("VB.Ob.25736.SHNP645"));
      excludesList.addElement(new String("VB.Ob.25737.SHNP646"));
      excludesList.addElement(new String("VB.Ob.25738.SHNP647"));
      excludesList.addElement(new String("VB.Ob.25739.SHNP648"));
      excludesList.addElement(new String("VB.Ob.25742.SHNP651"));
      excludesList.addElement(new String("VB.Ob.25743.SHNP652"));
      excludesList.addElement(new String("VB.Ob.25744.SHNP653"));
      excludesList.addElement(new String("VB.Ob.25748.SHNP657"));
      excludesList.addElement(new String("VB.Ob.25749.SHNP658"));
      excludesList.addElement(new String("VB.Ob.25750.SHNP659"));
      excludesList.addElement(new String("VB.Ob.25751.SHNP660"));
      excludesList.addElement(new String("VB.Ob.25761.SHNP670"));
      excludesList.addElement(new String("VB.Ob.25762.SHNP671"));
      excludesList.addElement(new String("VB.Ob.25763.SHNP672"));
      excludesList.addElement(new String("VB.Ob.25768.SHNP677"));
      excludesList.addElement(new String("VB.Ob.25643.SHNP552"));
      excludesList.addElement(new String("VB.Ob.25691.SHNP600"));
      excludesList.addElement(new String("VB.Ob.25705.SHNP614"));
      excludesList.addElement(new String("VB.Ob.25637.SHNP546"));
      excludesList.addElement(new String("VB.Ob.25764.SHNP673"));
      excludesList.addElement(new String("VB.Ob.25765.SHNP674"));
      excludesList.addElement(new String("VB.Ob.25767.SHNP676"));
      excludesList.addElement(new String("VB.Ob.25599.SHNP507"));
      excludesList.addElement(new String("VB.Ob.25653.SHNP562"));
      excludesList.addElement(new String("VB.Ob.25663.SHNP572"));
      excludesList.addElement(new String("VB.Ob.25697.SHNP606"));
      excludesList.addElement(new String("VB.Ob.25741.SHNP650"));
      excludesList.addElement(new String("VB.Ob.25753.SHNP662"));
      excludesList.addElement(new String("VB.Ob.25468.SHNP008"));
      excludesList.addElement(new String("VB.Ob.25461.SHNP001"));
      excludesList.addElement(new String("VB.Ob.25621.SHNP530"));
      excludesList.addElement(new String("VB.Ob.25698.SHNP607"));
      excludesList.addElement(new String("VB.Ob.25677.SHNP586"));
      excludesList.addElement(new String("VB.Ob.25598.SHNP505"));
    }

    Vector alreadyCompleted;
    
    try
    {
      alreadyCompleted = deserializeVector();
    }
    catch(Exception ioe)
    {
      alreadyCompleted = new Vector();
    }
    
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
            if(excludesList.contains(accCode.trim()) || 
               alreadyCompleted.contains(accCode.trim()))
            { //check both the serialized alreadyCompleted vector and the
              //excludeslist.  if the acccode is in there, don't run the
              //process on it
              
              //System.out.println("Skipping accCode: " + accCode);
              //System.out.println();
              if(!alreadyCompleted.contains(accCode.trim()))
              { //add to alreadyCompleted if its not there already
                alreadyCompleted.addElement(accCode.trim());
                serializeVector(alreadyCompleted);
              }
              continue;
            }
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
                alreadyCompleted.addElement(accCode);
                serializeVector(alreadyCompleted);
              }
              else
              {
                System.out.println("XML successfully cached for accCode: " + accCode);
                //add to alreadyCompleted
                alreadyCompleted.addElement(accCode);
                serializeVector(alreadyCompleted);
              }
            }
            else
            {
              //add to alreadyCompleted
              alreadyCompleted.addElement(accCode);
              serializeVector(alreadyCompleted);
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
  
  /**
   * serialize a vector to the local file 'vector'
   */
  private static void serializeVector(Vector v)
    throws Exception
  {
    FileOutputStream fos = new FileOutputStream("vector");
    ObjectOutputStream oos = new ObjectOutputStream(fos);
    oos.writeObject(v);
    oos.close();
  }
  
  /**
   * deserialize a vector from the local file 'vector'
   */
  private static Vector deserializeVector()
    throws Exception
  {
    FileInputStream fis = new FileInputStream("vector");
    ObjectInputStream ois = new ObjectInputStream(fis);
    Vector v = (Vector) ois.readObject();
    ois.close();
    return v;
  }
}
