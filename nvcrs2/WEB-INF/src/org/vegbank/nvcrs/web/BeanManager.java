package org.vegbank.nvcrs.web;

import java.sql.*;
import javax.sql.*;
import java.util.*;
import org.vegbank.nvcrs.util.*;


public class BeanManager
{	
	public static final String UNKNOWN_ID="-1";
	public static final String EMPTY_VALUE="";
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
	
	String something;
	String message;
	
	String proposalIds;
	String usrIds;
	ArrayList errors;
	Connection con;
	
	String cur_menu;
	Database database;
	SystemManager systemManager;
	
	public BeanManager()
	{
		userId=UNKNOWN_ID;
		usrIds=UNKNOWN_ID;
		userPermission=UNKNOWN_ID;
		userCurrentRole="Author";
		userName=UNKNOWN_ID;
		userEmail=UNKNOWN_ID;
		proposalId=UNKNOWN_ID;
		proposalIds=UNKNOWN_ID;
		proposalOwnerId=UNKNOWN_ID;
		proposalStatus=UNKNOWN_ID;
		typeId=UNKNOWN_ID;
		reviewId=UNKNOWN_ID;
		reviewOwnerId=UNKNOWN_ID;
		reviewStatus=UNKNOWN_ID;
		message="";
		something="";
		errors=new ArrayList();
		con=null;
		database=new Database();
		cur_menu="Home";
	}
	
	public BeanManager(String userId,String userPermission, String userName,String userEmail)
	{
		this.userId=userId;
		this.userPermission=userPermission;
		this.userCurrentRole="Author";
		this.userName=userName;
		this.userEmail=userEmail;
		proposalId=UNKNOWN_ID;
		proposalIds=UNKNOWN_ID;
		proposalOwnerId=UNKNOWN_ID;
		proposalStatus=UNKNOWN_ID;
		typeId=UNKNOWN_ID;
		reviewId=UNKNOWN_ID;
		reviewOwnerId=UNKNOWN_ID;
		reviewStatus=UNKNOWN_ID;
		message="";
		something="";
		errors=new ArrayList();
		con=null;
		database=new Database();
		cur_menu="Home";
	}
	
	public void setSystemManager(SystemManager manager)
	{
		systemManager=manager;
	}
	//getters
	
	public String getCurMenu()
	{
		return cur_menu;
	}
	public void setCurMenu(String menu)
	{
		cur_menu=menu;
	}
	
	public String getProposalIds()
	{
		return proposalIds;
	}
	
	public String getUsrIds()
	{
		return usrIds;
	}
	
	public String getSomething()
	{
		return something;
	}
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
	
	public void setProposalIds(String ids)
	{
		proposalIds=ids;
	}
	public void setUsrIds(String ids)
	{
		usrIds=ids;
	}
	public void setSomething(String something)
	{
		this.something=something;
	}
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
	
	public void setTypeId(String id)
	{
		this.typeId=id;
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
		return !userId.equals(UNKNOWN_ID);
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
				if(primaryKeyValue.equals(UNKNOWN_ID)) return 0;
			}
			else
			{
				//if(!proposalOwnerId.equals(userId)) return -1;
				
				if(proposalStatus.trim().equals("unsubmitted"))
				{
					if(primaryKeyValue.equals(UNKNOWN_ID))
						return 0;
					else
						return 3;
				}
				else return 999;
			}
		}
		
		if(userCurrentRole.equals("Peer-viewer"))
		{
			if(tblName.equals("event"))
			{
				if(primaryKeyValue.equals(UNKNOWN_ID))
					return 0;
				else
					return 3;
				
				//else return 999;
			}
			
			else if(tblName.equals("usr"))
			{
				if(primaryKeyValue.equals(userId) && !isRegistered()) return 0;
				if(primaryKeyValue.equals(userId) && isRegistered()) return 1;
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
				if(primaryKeyValue.equals(BeanManager.UNKNOWN_ID))
					return 0;
				else
					return 3;
			}
			else if(tblName.equals("usr"))
			{
				if(primaryKeyValue.equals(userId) && !isRegistered()) return 0;
				else if(primaryKeyValue.equals(userId) && isRegistered()) return 1;
				else return 999;
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
		proposalId=BeanManager.UNKNOWN_ID;
		proposalStatus=BeanManager.UNKNOWN_ID;
		typeId=BeanManager.UNKNOWN_ID;
		reviewId=BeanManager.UNKNOWN_ID;
		reviewStatus=BeanManager.UNKNOWN_ID;
	}
	
	public void clearErrors()
	{
		errors.clear();
	}
	
	public void deleteProposal(String id) throws Exception
	{
		String tmp="";
		PreparedStatement prepStmt=null;
		try
		{
			if(id==null || id.length()==0)
				throw new Exception("Invalid parameter");
			//prepare sql string
			
			con=database.getConnection();
			
			String delProposal="delete from proposal where PROPOSAL_ID='"+id+"'";
			String delTypes="delete from type where PROPOSAL_ID='"+id+"'";
			String delEvents="delete from event where PROPOSAL_ID='"+id+"'";
			//get type ids
			String sql="select TYPE_ID from type where PROPOSAL_ID='"+id+"'";
			prepStmt = con.prepareStatement(sql);
			ResultSet types=prepStmt.executeQuery();

			tmp="";
			while(types.next())
			{
				String tpid=types.getString(1);
				if(tmp.length()<1)
					tmp=" where TYPE_ID='"+tpid+"'";
				else
					tmp+=" OR TYPE_ID='"+tpid+"'";
			}
			
			
			con.setAutoCommit(false);
			prepStmt = con.prepareStatement("delete from correlation " +tmp);
			prepStmt.executeUpdate();
			
			prepStmt = con.prepareStatement("delete from composition " +tmp);
			prepStmt.executeUpdate();
			prepStmt = con.prepareStatement("delete from distribution " +tmp);
			prepStmt.executeUpdate();
			prepStmt = con.prepareStatement("delete from typename " +tmp);
			prepStmt.executeUpdate();
			prepStmt = con.prepareStatement("delete from typereference " +tmp);
			prepStmt.executeUpdate();
			prepStmt = con.prepareStatement("delete from plot " +tmp);
			prepStmt.executeUpdate();
			
			prepStmt = con.prepareStatement(delTypes);
			prepStmt.executeUpdate();
			
			prepStmt = con.prepareStatement(delEvents);
			prepStmt.executeUpdate();
			
			prepStmt = con.prepareStatement(delProposal);
			prepStmt.executeUpdate();
			
			con.commit();
	        prepStmt.close();
			con.close();
		}
		catch(Exception e)
		{
		
			if(!con.isClosed())
			{
				con.rollback();
				prepStmt.close();
				con.close();
			}
			throw e;
		}
	}
	public void deleteType(String id) throws Exception
	{
		String tmp="";
		PreparedStatement prepStmt=null;
		try
		{
			if(id==null || id.length()==0)
				throw new Exception("Invalid parameter");
			//prepare sql string
			
			con=database.getConnection();
			
			
			String delType="delete from type where TYPE_ID='"+id+"'";
			
			con.setAutoCommit(false);
			prepStmt = con.prepareStatement("delete from correlation where TYPE_ID='" +id+"' OR CORRELATEDTYPE_ID='"+id+"'");
			prepStmt.executeUpdate();
			
			prepStmt = con.prepareStatement("delete from composition where TYPE_ID='" +id+"'");
			prepStmt.executeUpdate();
			prepStmt = con.prepareStatement("delete from distribution where TYPE_ID='" +id+"'");
			prepStmt.executeUpdate();
			prepStmt = con.prepareStatement("delete from typename where TYPE_ID='" +id+"'");
			prepStmt.executeUpdate();
			prepStmt = con.prepareStatement("delete from typereference where TYPE_ID='" +id+"'");
			prepStmt.executeUpdate();
			prepStmt = con.prepareStatement("delete from plot where TYPE_ID='" +id+"'");
			prepStmt.executeUpdate();
			
			prepStmt = con.prepareStatement(delType);
			prepStmt.executeUpdate();
			
		
			con.commit();
	        prepStmt.close();
			con.close();
		}
		catch(Exception e)
		{
		
			if(!con.isClosed())
			{
				con.rollback();
				prepStmt.close();
				con.close();
			}
			throw e;
		}
	}
	public void deleteProject(String id) throws Exception
	{
		String tmp="";
		PreparedStatement prepStmt=null;
		try
		{
			if(id==null || id.length()==0)
				throw new Exception("Invalid parameter");
			//prepare sql string
			
			con=database.getConnection();
			
			
			String delType="delete from project where PROJECT_ID="+id;
			
			con.setAutoCommit(false);
			prepStmt = con.prepareStatement("update proposal set PROJECT_ID="+UNKNOWN_ID+" where PROJECT_ID="+id);
			prepStmt.executeUpdate();
			
			prepStmt = con.prepareStatement(delType);
			prepStmt.executeUpdate();
			
		
			con.commit();
	        prepStmt.close();
			con.close();
		}
		catch(Exception e)
		{
			try{
				if(!con.isClosed())
				{
					con.rollback();
					prepStmt.close();
					con.close();
				}
			}
			catch(Exception ex){}
			throw e;
		}
	}
	public void deleteSingle(String tbName,String idFld, String id) throws Exception
	{
		String tmp="";
		PreparedStatement prepStmt=null;
		try
		{
			if(tbName==null || tbName.length()==0||id==null || id.length()==0)
				throw new Exception("Invalid parameter");
			//prepare sql string
			
			con=database.getConnection();
			
			
			String delSQL="delete from "+tbName+" where "+idFld +"='"+id+"'";
			
			con.setAutoCommit(false);
			
			prepStmt = con.prepareStatement(delSQL);
			prepStmt.executeUpdate();
			
			con.commit();
	        prepStmt.close();
			con.close();
		}
		catch(Exception e)
		{
		
			if(!con.isClosed())
			{
				con.rollback();
				prepStmt.close();
				con.close();
			}
			throw e;
		}
	}
	
	public String getAuthorOfProposal(String proposalId) throws Exception
	{
		String tmp="";
		PreparedStatement prepStmt=null;
		try
		{
			if(proposalId==null || proposalId.length()==0)
				throw new Exception("Invalid parameter");
			//prepare sql string
			
			con=database.getConnection();
			
			
			String strSQL="select USR_ID from event where PROPOSAL_ID='"+proposalId+"' AND ACTION_ID='submitted'";
			
			prepStmt = con.prepareStatement(strSQL);
			ResultSet ret=prepStmt.executeQuery();
			String id;
			if(ret.next())
			{
				id=ret.getString(1);
				prepStmt.close();
				con.close();
				return id;
			}
			else
				throw new Exception("No author is found for proposal: " + proposalId);
		}
		catch(Exception e)
		{
		
			if(!con.isClosed())
			{
				con.rollback();
				prepStmt.close();
				con.close();
			}
			throw e;
		}
	}
	
	public void assign() throws Exception
	{
		if(proposalIds.equals("") || usrIds.equals(""))
			throw new Exception("No proposal or peer-viewer selected.");
		
		String [] pids=proposalIds.split(",");
		String [] uids=usrIds.split(",");
		
		int pnum=pids.length;
		int unum=uids.length;
		if(pnum==0 || unum==0)
			throw new Exception("No proposal or peer-viewer selected.");
		
		int i,j;
		
		String pStr="update proposal set current_status='assigned' where ";
		for(i=0;i<pnum;i++)
		{
			if(i>0)pStr+=" OR ";
			pStr+="PROPOSAL_ID=" + pids[i];
		}
		Calendar date=Calendar.getInstance();
		int day = date.get(Calendar.DATE);
		int month = date.get(Calendar.MONTH);
		int year = date.get(Calendar.YEAR);
		String dt=String.valueOf(year)+"-"+String.valueOf(month+1)+"-"+String.valueOf(day);
				
		PreparedStatement prepStmt=null;
		try
		{
			con=database.getConnection();
			con.setAutoCommit(false);
			prepStmt = con.prepareStatement(pStr);
			prepStmt.executeUpdate();
				
			pStr="insert into event (summary,document1,document2,document3,publicComments,privateComments,ACTION_ID,eventDate,ROLE_ID,reviewText,USR_ID,PROPOSAL_ID,SUBJECTUSR_ID) values " + 
				"('','','','','','','assigned','"+dt+"',2,'new',"+userId+",?,?)";
			prepStmt = con.prepareStatement(pStr);
			for(i=0;i<pnum;i++)
			{
				for(j=0;j<unum;j++)
				{
					prepStmt.setString(1,pids[i]);
					prepStmt.setString(2,uids[j]);
					prepStmt.executeUpdate();
				}
			}
			
			con.commit();
		}
		catch(Exception e)
		{
		
			
			if(!con.isClosed())
			{
				con.rollback();
				prepStmt.close();
				con.close();
			}
			throw e;
		}
		
		event_Form fr=new event_Form();
		
		for(j=0;j<unum;j++)
		{
			fr.setUSR_ID(userId);
			fr.setSUBJECTUSR_ID(uids[j]);
			systemManager.handleEvent(SystemManager.EVENT_PROPOSAL_ASSIGNED,fr,null,null);
		}
	}
	
	
	public boolean isAllEvaluated(String pid) throws Exception
	{
		if(pid==null)throw new Exception("beanManager:isAllEvaluated():Null proposal id");
		event_Form fr = new event_Form();
		fr.findRecords("select * from event where ACTION_ID='assigned' AND PROPOSAL_ID='"+pid+"'");
		int assigned_num=fr.getRecords().size();
		fr.findRecords("select * from event where ACTION_ID='evaluated' AND PROPOSAL_ID='"+pid+"'");
		int evaluated_num=fr.getRecords().size();
		return (assigned_num==evaluated_num);
		
	}
	
	ArrayList getEvaluations(String proposalId, String type) throws Exception
	{
		return null;
	}
	
	ArrayList getAssignmentsByProposal(String proposalId, Vector statuses) throws Exception
	{
		if(userCurrentRole.equals("Author")) throw new Exception("Invalid action: get assignments");
		
		Assignment a;
		ArrayList as=new ArrayList();
		
		String sql="select event.*,usr.first_name from event,usr where event.ACTION_ID='assigned' AND event.PROPOSAL_ID='"+proposalId+"'";
		
		int n=statuses.size();
		if(n!=0)
		{
			sql+=" AND (";
			for(int i=0;i<n;i++)
			{
				if(i>0) sql+=" OR ";
				sql+="event.reviewText='"+(String)statuses.get(i)+"'";
			}
			sql+=" ) ";
		}
		
		if(userCurrentRole.equals("Peer-viewer"))
			sql+=" AND event.SUBJECTUSR_ID='"+userId+"'";
		
		sql+=" AND usr.USR_ID=event.SUBJECTUSR_ID";
		
		PreparedStatement prepStmt=null;
		try
		{
			con=database.getConnection();
			prepStmt = con.prepareStatement(sql);
			ResultSet es=prepStmt.executeQuery();
			
			while(es.next())
			{
				a=new Assignment(es.getString("EVENT_ID"),es.getString("eventDate"),proposalId,es.getString("USR_ID"),es.getString("USR_ID"),
					es.getString("SUBJECTUSR_ID"),es.getString("first_name"),
					es.getString("publicComments"),es.getString("reviewText"));	
				as.add(a);
			}
			prepStmt.close();
			con.close();
			
			return as;
		}
		catch(Exception e)
		{
		
			
			if(!con.isClosed())
			{
				prepStmt.close();
				con.close();
			}
			throw e;
		}
	}

	ArrayList getAssignmentsByPeerviewer(String pvId, Vector statuses) throws Exception
	{
		if(userCurrentRole.equals("Author")) throw new Exception("Invalid action: get assignments");
		
		Assignment a;
		ArrayList as=new ArrayList();
		
		String sql="select event.*,usr.first_name from event,usr where event.ACTION_ID='assigned' ";
		
		int n=statuses.size();
		if(n!=0)
		{
			sql+=" AND (";
			for(int i=0;i<n;i++)
			{
				if(i>0) sql+=" OR ";
				sql+="event.reviewText='"+(String)statuses.get(i)+"'";
			}
			sql+=" ) ";
		}
		
		if(userCurrentRole.equals("Peer-viewer"))
			sql+=" AND event.SUBJECTUSR_ID='"+userId+"'";
		else
			sql+=" AND event.SUBJECTUSR_ID='"+pvId+"'";
			
		sql+=" AND usr.USR_ID=event.SUBJECTUSR_ID";
		
		PreparedStatement prepStmt=null;
		try
		{
			con=database.getConnection();
			prepStmt = con.prepareStatement(sql);
			ResultSet es=prepStmt.executeQuery();
			
			while(es.next())
			{
				a=new Assignment(es.getString("EVENT_ID"),es.getString("eventDate"),es.getString("PROPOSAL_ID"),es.getString("USR_ID"),es.getString("USR_ID"),
					es.getString("SUBJECTUSR_ID"),es.getString("first_name"),
					es.getString("publicComments"),es.getString("reviewText"));	
				as.add(a);
			}
			prepStmt.close();
			con.close();
			
			return as;
		}
		catch(Exception e)
		{
		
			
			if(!con.isClosed())
			{
				prepStmt.close();
				con.close();
			}
			throw e;
		}
	}
		
	public String getAssignmentStatus(String pid, String vid) throws Exception
	{
		String sql="select reviewText from event where ACTION_ID='assigned' AND PROPOSAL_ID='";
		sql+= pid + "' AND SUBJECTUSR_ID='"+vid +"'";
		
		PreparedStatement prepStmt=null;
		try
		{
			con=database.getConnection();
			prepStmt = con.prepareStatement(sql);
			ResultSet es=prepStmt.executeQuery();
			
			if(es.next()) reviewStatus=es.getString(1);
			else throw new Exception("Failed to find event for Proposal: "+pid +" assigned to peer-viewer:"+vid);
				
			prepStmt.close();
			con.close();
			
			return reviewStatus;
		}
		catch(Exception e)
		{
			if(!con.isClosed())
			{
				prepStmt.close();
				con.close();
			}
			throw e;
		}
	}
	
	ArrayList getEvaluationsByProposal(String proposalId) throws Exception
	{
		
		Evaluation a;
		ArrayList as=new ArrayList();
		
		String sql="select event.*,usr.first_name from event,usr where event.ACTION_ID='evaluated' AND event.PROPOSAL_ID='"+proposalId+"'";
		sql+=" AND usr.USR_ID=event.USR_ID";
		
		PreparedStatement prepStmt=null;
		try
		{
			con=database.getConnection();
			prepStmt = con.prepareStatement(sql);
			ResultSet es=prepStmt.executeQuery();
			
			while(es.next())
			{
				a=new Evaluation(es.getString("EVENT_ID"),es.getString("eventDate"),proposalId,es.getString("USR_ID"),es.getString("first_name"),
					es.getString("reviewText"),es.getString("summary"));	
				as.add(a);
			}
			prepStmt.close();
			con.close();
			
			return as;
		}
		catch(Exception e)
		{
		
			
			if(!con.isClosed())
			{
				prepStmt.close();
				con.close();
			}
			throw e;
		}
	}
	

}