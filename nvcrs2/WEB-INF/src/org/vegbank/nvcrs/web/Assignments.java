package org.vegbank.nvcrs.web;

import org.vegbank.nvcrs.util.Assignment;
import java.util.*;

public class Assignments
{
	BeanManager manager;
	ArrayList assignments;
	String proposalId;
	String usrId;
	Vector statuses;
	
	public Assignments()
	{
		manager=null;
		proposalId=null;
		statuses=new Vector();
		usrId=null;
		assignments=new ArrayList();
	}
	
	public void setManager(BeanManager manager)
	{
		this.manager=manager;
	}
	
	public void setProposalId(String id)
	{
		proposalId=id;
	}
	
	public void setStatus(Vector  sts)
	{
		statuses=sts;
	}
	
	public void setPeerviewerId(String id)
	{
		usrId=id;
	}
	public void addAssignment(Assignment assign)
	{
		assignments.add(assign);
	}	
	
	public Assignment getAssignment(int index) throws Exception
	{
		if(assignments.size()-1<index || index<0) throw new Exception("Invalid index for array (size="+assignments.size()+"): "+index);
		return (Assignment)assignments.get(index);
	}
	
	public int getAssignmentCount()
	{
		return assignments.size();
	}
	
	public void removeAssignment(Assignment agn) throws Exception
	{
		try{
			assignments.remove(agn);
		}
		catch(Exception e)
		{
			throw e;
		}
	}
	
	public void removeAssignment(int index) throws Exception
	{
		try{
			assignments.remove(index);
		}
		catch(Exception e)
		{
			throw e;
		}
	}
	
	public void removeAssignmentById(String id) throws Exception
	{
		int i,num;
		try
		{

			num=assignments.size();
			
			for(i=0;i<num;i++)
			{
				Assignment a=getAssignment(i);
				if(a.getEventId().equals(id))
				{
					removeAssignment(i);
					i=num+1;
				}
			}
		}
		catch(Exception e)
		{
			throw e;
		}
		if(i==num)
		{
			throw new Exception("No assignment find for id: "+id);
		}
	}
	
	public void getAssignmentsByProposal() throws Exception
	{
		if(manager==null) throw new Exception("Asignments: Null bean manager");
		if(proposalId==null) throw new Exception("Assignments: Null proposal id");
		try
		{
			assignments=manager.getAssignmentsByProposal(proposalId, statuses);
		}
		catch(Exception e)
		{
			throw e;
		}
			
	}
	
	public void getAssignmentsByPeerviewer() throws Exception
	{
		if(manager==null) throw new Exception("Asignments: Null bean manager");
		if(usrId==null) throw new Exception("Assignments: Null peer-viewer id");
		try
		{
			assignments=manager.getAssignmentsByPeerviewer(usrId, statuses);
		}
		catch(Exception e)
		{
			throw e;
		}
	}
	
	public void addStatus(String st)
	{
		statuses.add(st);
	}
	
	public String getStatus(int index)
	{
		return (String)statuses.get(index);
	}
	public void reset()
	{
		proposalId=null;
		statuses.clear();
		usrId=null;
		assignments=new ArrayList();
	}
	
}