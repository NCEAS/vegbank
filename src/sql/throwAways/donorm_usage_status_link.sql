update commUsage set commstatus_id=(select min(commstatus_id) from commstatus where commstatus.commconcept_id=commUsage.commConcept_ID and commstatus.party_Id=commusage.party_ID);

update plantUsage set plantstatus_id=(select min(plantstatus_id) from plantstatus where plantstatus.plantconcept_id=plantUsage.plantConcept_ID and plantstatus.party_Id=plantusage.party_ID);