package org.vegbank.nvcrs.web;

import org.vegbank.nvcrs.util.Evaluation;
import java.util.*;

public class Evaluations
{
	ArrayList evaluations;
	BeanManager manager;
	String proposalId;
	String status;
	
	
	public Evaluations()
	{
		evaluations=new ArrayList();
	}
	
	public void setEvaluations(ArrayList list)
	{
		evaluations=list;
	}
	public void setManager(BeanManager manager)
	{
		this.manager=manager;
	}
	
	public void setProposalId(String id)
	{
		proposalId=id;
	}
	
	public void setStatus(String sts)
	{
		status=sts;
	}
	
	public void addEvaluation(Evaluation assign)
	{
		evaluations.add(assign);
	}	
	
	public Evaluation getEvaluation(int index) throws Exception
	{
		if(evaluations.size()-1<index || index<0) throw new Exception("Invalid index for array (size="+evaluations.size()+"): "+index);
		return (Evaluation)evaluations.get(index);
	}
	
	public int getEvaluationCount()
	{
		return evaluations.size();
	}
	
	public void removeEvaluation(Evaluation agn) throws Exception
	{
		try{
			evaluations.remove(agn);
		}
		catch(Exception e)
		{
			throw e;
		}
	}
	
	public void removeEvaluation(int index) throws Exception
	{
		try{
			evaluations.remove(index);
		}
		catch(Exception e)
		{
			throw e;
		}
	}
	
	public void getEvaluations() throws Exception
	{
		if(manager==null) throw new Exception("Asignments: Null bean manager");
		if(proposalId==null) throw new Exception("Assignments: Null proposal id");
		try
		{
			
			if(status==null)
				evaluations=manager.getEvaluations(proposalId, "all");
			else
				evaluations=manager.getEvaluations(proposalId, status);
		}
		catch(Exception e)
		{
			throw e;
		}
			
	}
}