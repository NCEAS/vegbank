/*
 * This sql script creates a snapshot table that holds most the 
 * information that would be used for querying the database
 */
drop table commSummary;
drop SEQUENCE commSummary_id_seq;


CREATE SEQUENCE commSummary_id_seq;
CREATE TABLE commSummary (
	COMMSUMMARY_ID  NUMERIC(20) default nextval('commSummary_id_seq'),
	usage_id integer, -- the usage id
	commName VARCHAR(500),
	commName_id integer,
	dateEntered date,
	classCode VARCHAR(50),
	classLevel VARCHAR(50), 
	commDescription VARCHAR(2000), --truncated description
	conceptOriginDate date,  --origin date of the concept
	conceptUpdateDate date, --date that the concept was updated
	commconcept_id  integer,
	nameRefAuthors  VARCHAR(50), --the name reference authors in the party
	recognizingParty VARCHAR(50),  -- the party that recognizes the concept
	partyConceptStatus VARCHAR(50), -- status of the party recognition
	parentCommConceptId integer, -- the concept ID if the community in this database
	parentCommConceptCode varchar(20), -- the abi code of the parent
	parentCommName VARCHAR(200), -- the name of the parent
	parentCommDescription  VARCHAR(2000), -- basically the name of the parent
	CONSTRAINT commSummary_pk PRIMARY KEY (COMMSUMMARY_ID)
);  

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
		from commUsage where commUsage.commusage_id > 0;


--CREATE INDEXES
create index commSummary_commconcept_id on commSummary  (commconcept_id);
--create index commPartyConcept_commconcept_id on commSummary  (commconcept_id);
create index commStatus_commConcept_id on commStatus  (commConcept_id);
--create index commStatus_commConceptStatus_id on commStatus  (commConceptStatus);

--UPDATE THE LEVEL IN THE HEIRACRCY
update commSummary
set classlevel =
(select commlevel from commconcept
where  commsummary.commconcept_id = commconcept.commconcept_id  );


--UPDATE THE NAMEREFAUTHORS
update commSummary
set nameRefAuthors =
(select authors from commreference
where  commreference.commreference_id = (select commreference_id from commname where commname.commname_id = commsummary.commname_id)  );


--UPDATE THE DESCRIPTIONS
update commSummary
set commdescription =
(select conceptdescription from commconcept
where  commsummary.commconcept_id = commconcept.commconcept_id  );

--UPDATE THE ORIGIN DATES
update commSummary
set conceptOriginDate  =
(select startdate from commstatus
where  commsummary.commconcept_id = commstatus.commconcept_id  );


--UPDATE THE STATUS FOR THE CONCEPT
update commSummary
set  partyConceptStatus =
(select commconceptstatus from commstatus
where  commsummary.commconcept_id = commStatus.commconcept_id  );


--UPDATE THE PARENT CONCEPT NAME, CODE and DESCRIPTION
-- get the parent concept id's from status table
update commSummary 
set parentcommconceptid  = 
(select commparent from commstatus 
where commstatus.commconcept_id = commsummary.commconcept_id );

-- get the name etc
update commSummary 
set  parentCommName = 
(select conceptdescription from commconcept 
where commsummary.parentcommconceptid = commconcept.commconcept_id );

update commSummary 
set  parentCommConceptCode = 
(select ceglcode from commconcept 
where commsummary.parentcommconceptid = commconcept.commconcept_id );

update commSummary 
set  parentCommDescription = 
(select conceptdescription from commconcept 
where commsummary.parentcommconceptid = commconcept.commconcept_id );


--UPDATE THE RECOGNIZING PARTY
update commSummary
set recognizingparty =
(select organizationname from commparty where commparty_id =
	(select commparty_id from commstatus
	where  commsummary.commconcept_id = commStatus.commconcept_id  )
);





