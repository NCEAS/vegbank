/*
 * '$Id: BeanManager.java,v 1.1.1.1 2004-04-21 17:10:06 anderson Exp $'
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

package org.vegbank.nvcrs.web;

import java.sql.*;
import javax.sql.*;
import java.util.*;
import org.vegbank.nvcrs.util.*;


public class BeanManager
{
	String userId;
	String userName;
	String userEmail;
	String userPermission;
	String userCurrentRole;
	String proposalId;
	String proposalOwnerId;
	String proposalStatus;
	
	String typeId;
	String reviewId;
	String reviewOwnerId;
	String reviewStatus;
	
	String message;
	ArrayList errors;
	Connection con;
	Database database;
	
	public BeanManager()
	{
		userId="-1";
		userPermission="";
		userCurrentRole="Author";
		userName="";
		userEmail="";
		proposalId="";
		proposalOwnerId="";
		proposalStatus="";
		typeId="";
		reviewId="";
		reviewOwnerId="";
		reviewStatus="";
		message="";
		errors=new ArrayList();
		con=null;
		database=new Database();
	}
	
	public BeanManager(String userId,String userPermission, String userName,String userEmail)
	{
		this.userId=userId;
		this.userPermission=userPermission;
		this.userCurrentRole="Author";
		this.userName=userName;
		this.userEmail=userEmail;
		proposalId="";
		proposalOwnerId="";
		proposalStatus="";
		typeId="";
		reviewId="";
		reviewOwnerId="";
		reviewStatus="";
		message="";
		errors=new ArrayList();
		con=null;
		database=new Database();
	}
	
	//getters
	public Database getDatabase()
	{
		return database;
	}
	
	
	public String getProposalOwnerId()
	{
		return proposalOwnerId;
	}
	
	public String getReviewOwnerId()
	{
		return reviewOwnerId;
	}
	
	public String getMessage()
	{
		return message;
	}
	
	public ArrayList getErrors()
	{
		return errors;
	}
	public String getUserId()
	{
		return userId;
	}
	
	public String getUserPermission()
	{
		return userPermission;
	}

	public String getUserCurrentRole()
	{
		return userCurrentRole;
	}
	public String getUserName()
	{
		return userName;
	}
	
	public String getUserEmail()
	{
		return userEmail;
	}
	
	public String getProposalId()
	{
		return proposalId;
	}
	
	public String getProposalStatus()
	{
		return proposalStatus;
	}
	
	public String getTypeId()
	{
		return typeId;
	}
	
	public String getReviewId()
	{
		return reviewId;
	}
	
	public String getReviewStatus()
	{
		return reviewStatus;
	}
	//setters
	
	public void setDatabase(Database db)
	{
		this.database=db;
	}
	public void setProposalOwnerId(String id)
	{
		proposalOwnerId=id;
	}
	
	public void setReviewOwnerId(String id)
	{
		reviewOwnerId=id;
	}
	
	public void setMessage(String msg)
	{
		this.message=msg;
	}
	
	public void setErrors(ArrayList errors)
	{
		this.errors=errors;
		
	}
	public void setUserId(String userId)
	{
		this.userId=userId;
	}
	
	public void setUserCurrentRole(String userCurrentRole)
	{
		this.userCurrentRole=userCurrentRole;
	}
	public void setUserPermission(String userPermission)
	{
		this.userPermission=userPermission;
	}
	
	public void setUserName(String userName)
	{
		this.userName=userName;
	}
	
	public void setUserEmail(String userEmail)
	{
		this.userEmail=userEmail;
	}
	
	public void setProposalId(String proposalId)
	{
		this.proposalId=proposalId;
	}
	
	public void setProposalStatus(String proposalStatus)
	{
		this.proposalStatus=proposalStatus;
	}
	
	public void setReviewId(String reviewId)
	{
		this.reviewId=reviewId;
	}
	
	public void setReviewStatus(String reviewStatus)
	{
		this.reviewStatus=reviewStatus;
	}
	
	public boolean isRegistered()
	{
		return !userId.equals("-1");
	}
	
	public int getPermissionByRecord(String tblName, String primaryKey, String primaryKeyValue)
	{
		if(!isRegistered()) return -1;
		if(userCurrentRole.equals("Author"))
		{
			if(tblName.equals("event")) return 999;
			else if(tblName.equals("usr"))
			{
				if(primaryKeyValue.equals(userId) && !isRegistered()) return 0;
				if(primaryKeyValue.equals(userId) && isRegistered()) return 1;
				if(primaryKeyValue.equals("")) return 0;
			}
			else
			{
				//if(!proposalOwnerId.equals(userId)) return -1;
				
				if(proposalStatus.equals("unsubmitted"))
					if(primaryKeyValue.equals(""))
						return 0;
					else
						
return 3;
				else return 999;
			}
		}
		
		if(userCurrentRole.equals("Peer-viewer"))
		{
			if(tblName.equals("event"))
			{
				if(!reviewOwnerId.equals(userId)) return -1;
				
				if(reviewStatus.equals("unsubmitted")) return 1;
				else return 999;
			}
			
			else if(tblName.equals("usr"))
			{
				if(primaryKeyValue.equals("userId") && !isRegistered()) return 0;
				if(primaryKeyValue.equals("userId") && isRegistered()) return 1;
			}
			else
			{
				if(proposalStatus.equals("unsubmitted")) return -1;
				else return 999;
			}
		}
		
		if(userCurrentRole.equals("Manager"))
		{
			if(tblName.equals("event"))
			{
				if(reviewStatus.equals("unsubmitted")) return -1;
				
				else return 999;
			}
			else if(tblName.equals("usr"))
			{
				if(primaryKeyValue.equals("userId") && !isRegistered()) return 0;
				else if(primaryKeyValue.equals("userId") && isRegistered()) return 1;
				else return 3;
			}
			else
			{
				if(proposalStatus.equals("unsubmitted")) return -1;
				else return 999;
			}
		}
		return -1;
	}
	
	public void clear()
	{
		proposalId="";
		proposalStatus="";
		typeId="";
		reviewId="";
		reviewStatus="";
	}
	
	public void clearErrors()
	{
		errors.clear();
	}
	

	
}