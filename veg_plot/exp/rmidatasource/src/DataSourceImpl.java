import java.sql.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.math.*;
import java.net.*;
import java.io.*;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;

//the data translator classes
import PlotDataSource;

public class DataSourceImpl extends UnicastRemoteObject
  implements DataSourceServerInterface 
	{
		private String name;
	 	private PlotDataSource source;
	 
	 
	 //constructor
   public DataSourceImpl(String sourcePluginClass) 
	 	throws RemoteException
	 {
		 super();
		 System.out.println("DataSourceImpl > plugin to be used: " + sourcePluginClass);
		 source = new PlotDataSource(sourcePluginClass);
     //source.getPlot("VOYA.03");
   }
	
	// see the interface for method descriptions
	public String getProjectDescription(String plotName)
	{
		try
		{
			return( source.projectDescription );
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(null);
	}
	
	
	/**
	 * simple method for transfering files back and forth 
	 * between the client and the server
   */
	 public byte[] downloadFile(String fileName)
	 {
      try 
			{
         File file = new File(fileName);
         byte buffer[] = new byte[(int)file.length()];
         BufferedInputStream input = new
      	 BufferedInputStream(new FileInputStream(fileName));
         input.read(buffer,0,buffer.length);
         input.close();
         return(buffer);
      } 
			catch(Exception e)
			{
         System.out.println("FileImpl: "+e.getMessage());
         e.printStackTrace();
         return(null);
      }
   }
	 
	/**
	 * method that retuns all the plots stored in a 
	 * data source
	 */
  public Vector getPlotNames()
	{
		Vector v = new Vector();
		try
		{
			System.out.println("DataSourceImpl > get plot names contacted");
			v = source.getPlotNames();
			System.out.println("DataSourceImpl > plot names: " + v.toString() );
		}
		catch( Exception e)
		{
			System.out.println("Exception: " + e.getMessage() );
			e.printStackTrace();
		}
		return(v);
	}
		
		
		//returns the easting
	public String getXCoord(String plotName)
	{
		try
		{
			 return("x coord not done yet");
			//return( source.getXCoord(plotName) );
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(null);
	}
	
	//returns the northing
	public String getYCoord(String plotName)
	{
		try
		{
			return( source.projectDescription );
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(null);
	}
	
	//returns the latitude
	public String getLatitude(String plotName)
	{
		try
		{
			return( source.projectDescription );
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(null);
	}
	
	//returns the longitude
	public String getLongitude(String plotName)
	{
		try
		{
			return( source.projectDescription );
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(null);
	}
	
	//returns the geographic zone
	public String getUTMZone(String plotName)
	{
		try
		{
			return( source.projectDescription );
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(null);
	}
	
	//returns the plot shape
	public String getPlotShape(String plotName)
	{
		try
		{
			return( source.projectDescription );
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(null);
	}
	
	
	//returns the plot area
	public String getPlotArea(String plotName)
	{
		try
		{
			return( source.projectDescription );
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(null);
	}
	
	//returns the state for the current plot
	public String getCommunityName(String plotName)
	{
		try
		{
			return( source.projectDescription );
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(null);
	}
	
	
	//returns the state in which the plot exists
	public String getState(String plotName)
	{
		try
		{
			return( source.projectDescription );
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(null);
	}
	
	//retuns the hydrologic regime
	public String getHydrologicRegime(String plotName)
	{
		try
		{
			return( source.projectDescription );
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(null);
	}
	
	//returns the topo position
	public String getTopoPosition(String plotName)
	{
		try
		{
			return( source.projectDescription );
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(null);
	}
	
	//returns the slope aspect
	public String getSlopeAspect(String plotName)
	{
		try
		{
			return( source.projectDescription );
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(null);
	}
	
	//returns yje slope gradient
	public String getSlopeGradient(String plotName)
	{
		try
		{
			return( source.projectDescription );
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(null);
	}
	
	//returns the surficial geology
	public String getSurfGeo(String plotName)
	{
		try
		{
			return( source.projectDescription );
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(null);
	}
	
	//retuns the country
	public String getCountry(String plotName)
	{
		try
		{
			return( source.projectDescription );
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(null);
	}
	
	//returns the size of the stand -- extensive etc..
	public String getStandSize(String plotName)
	{
		try
		{
			return( source.projectDescription );
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(null);
	}
	
	//returns the location as described by the author
	public String getAuthorLocation(String plotName)
	{
		try
		{
			return( source.projectDescription );
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(null);
	}
	
	//returns the landForm
	public String getLandForm(String plotName)
	{
		try
		{
			return( source.projectDescription );
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(null);
	}
	
	//retuns the elevation
	public String getElevation(String plotName)
	{
		try
		{
			return( source.projectDescription );
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(null);
	}
	
	//returns the elevation accuracy
	public String getElevationAccuracy(String plotName)
	{
		try
		{
			return( source.projectDescription );
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(null);
	}
	
	//return the confidentiality reason -- not null
	public String getConfidentialityReason(String plotName)
	{
		try
		{
			return( source.projectDescription );
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(null);
	}
	
	//return the confidentiality status -- not null 0-6
	public String getConfidentialityStatus(String plotName)
	{
		try
		{
			return( source.projectDescription );
		}
		catch (Exception e)
		{
			System.out.println("Exception: "+e.getMessage());
      e.printStackTrace();
    }
		return(null);
	}	
		
	 
	 //this method retrives all the attributes for a gine plotname
  public void getPlotData(String plotName)
	{
		try
		{
		}
		catch( Exception e)
		{
			System.out.println("Exception: " + e.getMessage() );
			e.printStackTrace();
		}
	}
		
	//returns the project name 
	public String getProjectName(String plotName)
	{
		String s = null;
		try
		{
			s = 	source.projectName;
			System.out.println("proj name contacted");
			this.getPlotNames();
		}
		catch(Exception e) 
		{
     	System.err.println("FileServer exception: "+ e.getMessage());
      e.printStackTrace();
    }
		return(s);
	}
	
}


