/*
 * This sql script creates a snapshot table that holds most the 
 * information that would be used for querying the database
 */
drop table commSummary;
drop SEQUENCE commSummary_id_seq;


CREATE SEQUENCE commSummary_id_seq;
CREATE TABLE commSummary (
	COMMSUMMARY_ID  NUMERIC(20) default nextval('commSummary_id_seq'),
	commName VARCHAR(500),
	dateEntered date,
	abiCode VARCHAR(50),
	classCode VARCHAR(50),
	classLevel VARCHAR(50), 
	commDescription VARCHAR(2000), --truncated description
	conceptOriginDate date,  --origin date of the concept
	conceptUpdateDate date, --date that the concept was updated
	commconcept_id  integer,
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
	commName, 
	dateEntered, 
	abiCode, 
	classCode, 
	classLevel,
	commDescription, 
	conceptOriginDate, 
	conceptUpdateDate, 
	commConcept_id,
	parentCommConceptId)
	select 
	commName.commName, 
	commName.dateEntered, 
	'null', 
	commConcept.ceglCode, 
	commConcept.commLevel, 
	commConcept.conceptDescription,
	'01-JAN-2001', 
	'01-JAN-2001',
	commConcept.commConcept_id,
	commConcept.commParent
	from commName, commConcept
	where commName.commname_id = commConcept.commname_id;


--CREATE INDEXES
create index commSummary_commconcept_id on commSummary  (commconcept_id);
--create index commPartyConcept_commconcept_id on commSummary  (commconcept_id);
create index commStatus_commConcept_id on commStatus  (commConcept_id);
--create index commStatus_commConceptStatus_id on commStatus  (commConceptStatus);


--UPDATE THE STATUS FOR THE CONCEPT
update commSummary
set  partyConceptStatus =
(select commconceptstatus from commstatus
where  commsummary.commconcept_id = commStatus.commconcept_id  );


--UPDATE THE PARENT CONCEPT NAME, CODE and DESCRIPTION
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





