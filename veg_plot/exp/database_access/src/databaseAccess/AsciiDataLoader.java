package databaseAccess;

import java.util.StringTokenizer;
import java.util.Vector;

/**
 * This class allows a user to input an ascii file along with a header file that
 * can be loaded directly to a database.  It is intended that the user use this
 * facility lo load a table of information
 */

public class AsciiDataLoader
{
	Vector dataVector = new Vector();
	Vector schemaVector = new Vector();
	Vector attributeVector = new Vector();
	String delimeterString = " "; //the delimeterString is overridden in the
	//getSchema method
	String tableName = null;
	StringBuffer attributeBuffer = new StringBuffer();
	//String currentDataValue;

	/**
	 * Main method for testing
	 */
	public static void main(String[] args)
	{

		if (args.length < 1)
		{
			System.out.println(
				"Usage: java AsciiDataLoader  [ascifile] [headerfile] ");
			System.exit(0);
		}
		else
		{
			String dataFile = args[0];
			String headerFile = args[1];

			//load the data
			AsciiDataLoader adl = new AsciiDataLoader();
			adl.loadAsciiData(dataFile, headerFile);
		}
	}

	/**
	 * Method that actually loads the data
	 * 
	 * @param dataFile
	 * @param headerFile
	 *
	 */
	public void loadAsciiData(String dataFile, String headerFile)
	{
		try
		{

			//make the data file a vector
			utility u = new utility();
			u.fileVectorizer(dataFile);
			dataVector = u.outVector;

			//make the header / schema file a vector too
			u.fileVectorizer(headerFile);
			schemaVector = u.outVector;

			//grab the information from the schema file
			getSchema(schemaVector);

			//build the statement
			String insertString = "INSERT INTO " + tableName;

			//get the attributes into a string of the, to be, inserted attributes
			System.out.println(attributeVector.toString());
			for (int i = 0; i < attributeVector.size(); i++)
			{
				if (attributeVector.elementAt(i).toString() != null)
				{
					String buf = attributeVector.elementAt(i).toString();
					attributeBuffer.append(buf);
				}
				if (i + 1 != (attributeVector.size()))
				{
					attributeBuffer.append(", ");
				}
				else
				{
					attributeBuffer.append(" ");
				}
			}

			//System.out.println("> "+attributeBuffer.toString() );
			String attributeString = attributeBuffer.toString();

			int inputValueNum = attributeVector.size();
			String inputValue[] = new String[attributeVector.size()];

			//load the array with the values to be loaded to the database
			for (int row = 0; row < dataVector.size(); row++)
			{
				for (int i = 0; i < attributeVector.size(); i++)
				{
					//make sure to get the appropriate data value from the data set
					inputValue[i] =
						getDataValue(dataVector, row, i, delimeterString);
				}
				//get the valueString from the method
				issueStatement k = new issueStatement();
				k.getValueString(inputValueNum);
				String valueString = k.outValueString;

				issueStatement j = new issueStatement();
				j.issueInsert(
					insertString,
					attributeString,
					valueString,
					inputValueNum,
					inputValue);
				System.out.println(
					"Insert: " + insertString + " " + attributeString);
			}
		}
		catch (Exception e)
		{
			System.out.println(
				"failed at: AsciiDataLoader.loadAsciiData  " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Method that reads the header / schema file and extractes into
	 * public variables class varibles including delemeter, tableName etc.
	 *
	 * @param schemaVector
	 *
	 */
	public void getSchema(Vector schemaVector)
	{
		for (int i = 0; i < schemaVector.size(); i++)
		{
			//grab the table name
			if (schemaVector.elementAt(i).toString().startsWith("table"))
			{
				StringTokenizer t =
					new StringTokenizer(
						schemaVector.elementAt(i).toString(),
						" ");
				String buf = t.nextToken();
				buf = t.nextToken().replace('"', ' ').replace(';', ' ').trim();
				tableName = buf;
				//		System.out.println("tablename: " + buf);	
			}
			//grab the collumns
			if (schemaVector.elementAt(i).toString().startsWith("string"))
			{
				StringTokenizer t =
					new StringTokenizer(
						schemaVector.elementAt(i).toString(),
						" ");
				String buf = t.nextToken();
				buf = t.nextToken().replace('"', ' ').replace(';', ' ').trim();
				attributeVector.addElement(buf.trim());
				//				System.out.println("column: " + buf);
			}
			if (schemaVector.elementAt(i).toString().startsWith("delimeter"))
			{
				StringTokenizer t =
					new StringTokenizer(
						schemaVector.elementAt(i).toString(),
						" ");
				String buf = t.nextToken();
				buf = t.nextToken().replace('"', ' ').replace(';', ' ').trim();
				delimeterString = buf;
				System.out.println("delimeter: " + delimeterString);
			}
		}
	}

	/**
	 * This method will return a value from an ascii-data vector using 
	 * as input the row number and collumn number
	 *
	 * @param dataVector -- the data file as stored in a vector
	 * @param rowNumber -- the 
	 */
	public String getDataValue(
		Vector dataVector,
		int rowNumber,
		int columnNumber,
		String delimeterString)
	{

		//grab the row of data
		//String s = dataVector.elementAt(i).toString();
		//		System.out.println("searching form column number: "+columnNumber);
		String currentDataValue = null;
		StringTokenizer t =
			new StringTokenizer(
				dataVector.elementAt(rowNumber).toString(),
				delimeterString);
		String buf = null;
		//buf =t.nextToken(); // the first column
		try
		{
			for (int i = 0; i <= columnNumber; i++)
			{
				if (t.hasMoreTokens())
				{
					buf = t.nextToken();
					//				System.out.println("collecting: "+buf+" "+ i+ " "+ columnNumber);
				}
				else
				{
					//					System.out.println("missing a token value!");
					buf = "null";
				}
			}
		}
		catch (Exception e)
		{
			System.out.println("failed at:   " + e.getMessage());
			e.printStackTrace();
		}
		currentDataValue = buf;
		//		System.out.println("retuned value: "+currentDataValue);
		return currentDataValue;
	}

}
