package org.vegbank.nvcrs.util;

public class Assignment
{
	public static final String STATUS_NEW="new";
	public static final String STATUS_ACCEPTED="accepted";
	public static final String STATUS_EVALUATING="evaluating";
	public static final String STATUS_DECLINED="declined";
	public static final String STATUS_COMPLETED="completed";
	
	
	String eventId;
	String date;
	String proposalId;
	String assignById;
	String assignBy;
	String assignToId;
	String assignTo;
	String note;
	String status;
	
	public Assignment()
	{
		eventId="";
		date="";
		proposalId="";
		assignById="";
		assignBy="";
		assignToId="";
		assignTo="";
		note="";
		status="";
	}
	
	public Assignment(String eid,String dt,String id,String byid,String by,String toid,String to,String nt,String sts)
	{
		eventId=eid;
		date=dt;
		proposalId=id;
		assignById=byid;
		assignBy=by;
		assignToId=toid;
		assignTo=to;
		note=nt;
		status=sts;
	}
	
	public String getEventId()
	{
		return  eventId;
	}
	
	public void setEventId(String v)
	{
		eventId=v;
	}
	
	public String getDate()
	{
		return  date;
	}
	
	public void setDate(String v)
	{
		date=v;
	}
	
	public String getProposalId()
	{
		return proposalId ;
	}
	
	public void setProposalId(String v)
	{
		proposalId=v;
	}
	
	public String getAssignById()
	{
		return  assignById;
	}
	
	public void setAssignById(String v)
	{
		assignById=v;
	}
	
	public String getAssignBy()
	{
		return assignBy;
	}
	
	public void setAssignBy(String v)
	{
		assignBy=v;
	}
	
	public String getAssignToId()
	{
		return  assignToId;
	}
	
	public void setAssignToId(String v)
	{
		assignToId=v;
	}
	
	public String getAssignTo()
	{
		return  assignTo;
	}
	
	public void setAssignTo(String v)
	{
		assignTo=v;
	}
	
	public String getNote()
	{
		return note ;
	}
	
	public void setNote(String v)
	{
		note=v;
	}
	
	public String getStatus()
	{
		return  status;
	}
	
	public void setStatus(String v)
	{
		status=v;
	}
	
	public void reset()
	{
		date="";
		proposalId="";
		assignById="";
		assignBy="";
		assignToId="";
		assignTo="";
		note="";
		status="";
	}
}