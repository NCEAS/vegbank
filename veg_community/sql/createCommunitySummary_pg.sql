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
commDescription VARCHAR(4000), --truncated description
conceptOriginDate date,  --origin date of the concept
conceptUpdateDate date, --date that the concept was updated
parentAbiCode VARCHAR(50), --abicode of the parent
commconcept_id  NUMERIC(20),
recognizingParty VARCHAR(50),  -- the party that recognizes the concept
partyConceptStatus VARCHAR(50), -- status of the party recognition
parentCommName VARCHAR(500),
parentCommDescription  VARCHAR(4000),
--add the community name usage stuff here
CONSTRAINT commSummary_pk PRIMARY KEY (COMMSUMMARY_ID)
);  

-- LOAD THE SUMMARY TABLE WITH APPROXIMATES
insert into commSummary (commName, dateEntered, abiCode, classCode, classLevel,
	commDescription, conceptOriginDate, conceptUpdateDate, parentAbiCode, commConcept_id)
	select commName.commName, commName.dateEntered, 'null', commConcept.ceglCode, 
	commConcept.commLevel, commConcept.conceptDescription,
	'01-JAN-2001', '01-JAN-2001',
	commConcept.commParent, commConcept.commConcept_id
	from commName, commConcept
	where commName.commname_id = commConcept.commname_id;


--CREATE INDEXES
create index commSummary_commconcept_id on commSummary  (commconcept_id);
create index commPartyConcept_commconcept_id on commSummary  (commconcept_id);
--create index commSummary_parentAbiCode_id on commSummary  (partentAbiCode);

--UPDATE THE STATUS FOR THE CONCEPT
update commSummary
set  partyConceptStatus =
(select commconceptstatus from commstatus
where commStatus.commconcept_id = commSummary.commconcept_id);

--UPDATE THE PARENT CONCEPT
update commSummary
	set  parentCommName = 
	(select commParent from commConcept 
	where commConcept.commconcept_id = commSummary.commconcept_id);


