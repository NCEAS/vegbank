package org.vegbank.nvcrs.util;


public class Evaluation
{
	
	String eventId;
	String eventDate;
	String proposalId;
	String evaluatorId;
	String evaluatorName;
	String evaluation;
	String summary;
	
	public Evaluation()
	{
		eventId="";
		eventDate="";
		proposalId="";
		evaluatorId="";
		evaluatorName="";
		evaluation="";
		summary="";
	}
	
	
	public Evaluation(String d, String ed,String id, String eid, String ename, String e, String sm)
	{
		eventId=d;
		eventDate=ed;
		proposalId=id;
		evaluatorId=eid;
		evaluatorName=ename;
		evaluation=e;
		summary=sm;
	}
	public String getEventDate()
	{
		return eventDate;
	}
	
	public void setEventDate(String v)
	{
		eventDate=v;
	}
	public String getEventId()
	{
		return eventId;
	}
	
	public void setEventId(String v)
	{
		eventId=v;
	}
	public String getProposalId()
	{
		return proposalId ;
	}
	
	public void setProposalId(String v)
	{
		proposalId=v;
	}
	
	public String getEvaluatorId()
	{
		return evaluatorId ;
	}
	
	public void setEvaluatorId(String v)
	{
		evaluatorId=v;
	}
	
	public String getEvaluatorName()
	{
		return evaluatorName ;
	}
	
	public void setEvaluatorName(String v)
	{
		evaluatorName=v;
	}
	
	public String getEvaluation()
	{
		return evaluation ;
	}
	
	public void setEvaluation(String v)
	{
		evaluation=v;
	}
	
	public String getSummary()
	{
		return  summary;
	}
	
	public void setSummary(String v)
	{
		summary=v;
	}
	
	public void reset()
	{
		proposalId="";
		evaluatorId="";
		evaluatorName="";
		evaluation="";
		summary="";
	}
	
}