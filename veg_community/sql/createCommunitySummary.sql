/*
 * This sql script creates a snapshot table that holds most the 
 * information that would be used for querying the database
 */
 
set define off;
set linesize 600;
set heading off;
set pagesize 1000;



drop table commSummary;
drop SEQUENCE commSummary_id_seq;


CREATE TABLE commSummary (
COMMSUMMARY_ID  NUMBER(20),
commName VARCHAR2(500),
dateEntered date,
abiCode VARCHAR2(50),
classCode VARCHAR2(50),
classLevel VARCHAR2(50), 
commDescription VARCHAR2(4000), --truncated description
conceptOriginDate date,  --origin date of the concept
conceptUpdateDate date, --date that the concept was updated
parentAbiCode VARCHAR2(50), --abicode of the parent
commconcept_id  NUMBER(20),
recognizingParty VARCHAR2(50),  -- the party that recognizes the concept
partyConceptStatus VARCHAR2(50), -- status of the party recognition
parentCommName VARCHAR2(500),
parentCommDescription  VARCHAR2(4000),
--add the community name usage stuff here
CONSTRAINT commSummary_pk PRIMARY KEY (COMMSUMMARY_ID)
);  

CREATE SEQUENCE commSummary_id_seq;
CREATE TRIGGER commSummary_before_insert
BEFORE INSERT ON commSummary FOR EACH ROW
BEGIN
  SELECT commSummary_id_seq.nextval
    INTO :new.COMMSUMMARY_ID
    FROM dual;
END;
/

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
	
/*
 * update the summary table
 */
	update commSummary
	set  partyConceptStatus = 
	(select partyConceptStatus from commPartyConcept 
	where commPartyConcept.commconcept_id = commSummary.commconcept_id);
	commit;

	update commSummary
	set  parentCommName = 
	(select commConceptName from commConcept 
	where commConcept.abiCode = commSummary.parentAbiCode);
	commit;

