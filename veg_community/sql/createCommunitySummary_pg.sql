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

--CREATE SEQUENCE commSummary_id_seq;
--CREATE TRIGGER commSummary_before_insert
--BEFORE INSERT ON commSummary FOR EACH ROW
--BEGIN
--  SELECT commSummary_id_seq.nextval
--    INTO :new.COMMSUMMARY_ID
--    FROM dual;
--END;
--/

/*
 * Load the summary table
 */

insert into commSummary (commName, dateEntered, abiCode, classCode, classLevel,
commDescription, conceptOriginDate, conceptUpdateDate, parentAbiCode, commConcept_id)
	select commName.commName, commName.dateEntered, commName.abiCode, commConcept.classCode, 
	commConcept.classLevel, commConcept.commDescription,
	commConcept.originDate, commConcept.updatedate,
	commConcept.parentAbiCode, commConcept.commConcept_id
	from commName, commConcept
	where commName.abiCode = commConcept.abiCode;
	commit;


--CREATE INDEXES
create index commSummary_commconcept_id on commSummary  (commconcept_id);
create index commPartyConcept_commconcept_id on commSummary  (commconcept_id);
create index commSummary_parentAbiCode_id on commSummary  (partentAbiCode);

/*
 * update the summary table -- added the not null statement to get it to work
 * on postgres where it was not working beforehand
 */
update commSummary
set  partyConceptStatus =
(select partyConceptStatus from commPartyConcept
where commPartyConcept.commconcept_id = commSummary.commconcept_id 
and commPartyConcept.partyConceptStatus != null);
commit;


--	update commSummary
--	set  partyConceptStatus = 
--	(select partyConceptStatus from commPartyConcept 
--	where commPartyConcept.commconcept_id = commSummary.commconcept_id);
--	commit;

update commSummary
set  parentCommName = 
(select commConceptName from commConcept 
where commConcept.abiCode = commSummary.parentAbiCode);
commit;

