--drop table commSummary;
--drop SEQUENCE commSummary_id_seq;


--CREATE SEQUENCE commSummary_id_seq;
--CREATE TABLE commSummary (



-- LOAD THE SUMMARY TABLE WITH APPROXIMATES
insert into commSummary (
	usage_id,
	commName,
	commName_id,
	classCode, 
	commConcept_id
	)
		select 
		commUsage.commusage_id,
		commUsage.commName,
		commUsage.commName_id,
		commUsage.ceglCode, 
		commUsage.commConcept_id
		from commUsage where commUsage.commusage_id > ( select  max(commsummary.usage_id) from commsummary);


--CREATE INDEXES

--UPDATE THE LEVEL IN THE HEIRACRCY
update commSummary
set classlevel =
	(select commlevel from commconcept
	where  commsummary.commconcept_id = commconcept.commconcept_id  )
where commSummary_id > (select max(commusage.commusage_id)-10 from commusage);



--UPDATE THE NAMEREFAUTHORS
update commSummary
set nameRefAuthors =
(select authors from commreference
where  commreference.commreference_id = (select commreference_id from commname where commname.commname_id = commsummary.commname_id)  )
where commSummary_id > (select max(commusage.commusage_id)-10 from commusage);


--UPDATE THE DESCRIPTIONS
update commSummary
set commdescription =
(select conceptdescription from commconcept
where  commsummary.commconcept_id = commconcept.commconcept_id  )
where commSummary_id > (select max(commusage.commusage_id)-10 from commusage);

--UPDATE THE ORIGIN DATES
update commSummary
set conceptOriginDate  =
(select startdate from commstatus
where  commsummary.commconcept_id = commstatus.commconcept_id  )
where commSummary_id > (select max(commusage.commusage_id)-10 from commusage);


--UPDATE THE STATUS FOR THE CONCEPT
update commSummary
set  partyConceptStatus =
(select commconceptstatus from commstatus
where  commsummary.commconcept_id = commStatus.commconcept_id  )
where commSummary_id > (select max(commusage.commusage_id)-10 from commusage);


--UPDATE THE PARENT CONCEPT NAME, CODE and DESCRIPTION
-- get the parent concept id's from status table
update commSummary 
set parentcommconceptid  = 
(select commparent from commstatus 
where commstatus.commconcept_id = commsummary.commconcept_id )
where commSummary_id > (select max(commusage.commusage_id)-10 from commusage);

-- get the name etc
update commSummary 
set  parentCommName = 
(select conceptdescription from commconcept 
where commsummary.parentcommconceptid = commconcept.commconcept_id )
where commSummary_id > (select max(commusage.commusage_id)-10 from commusage);

update commSummary 
set  parentCommConceptCode = 
(select ceglcode from commconcept 
where commsummary.parentcommconceptid = commconcept.commconcept_id )
where commSummary_id > (select max(commusage.commusage_id)-10 from commusage);

update commSummary 
set  parentCommDescription = 
(select conceptdescription from commconcept 
where commsummary.parentcommconceptid = commconcept.commconcept_id )
where commSummary_id > (select max(commusage.commusage_id)-10 from commusage);


--UPDATE THE RECOGNIZING PARTY
update commSummary
set recognizingparty =
(select organizationname from commparty where commparty_id =
	(select commparty_id from commstatus
	where  commsummary.commconcept_id = commStatus.commconcept_id  )
)
where commSummary_id > (select max(commusage.commusage_id)-10 from commusage);





