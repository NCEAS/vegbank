/*
 * create the tables 
 */
set define off;
set linesize 600;
set heading off;
set pagesize 1000;



drop table commConcept;
drop table commName;
drop table commUsage;
drop table commParty;
drop table commPartyConcept;
drop table commCitation;
drop table commPartyUsage;

drop SEQUENCE commConcept_id_seq;
drop SEQUENCE commName_id_seq;
drop SEQUENCE commUsage_id_seq;
drop SEQUENCE commParty_id_seq;
drop SEQUENCE commPartyConcept_id_seq;
drop SEQUENCE commCitation_id_seq;
drop SEQUENCE commPartyUsage_id_seq;



//DROP TRIGGER commConcept_before_insert;
//DROP TRIGGER commName_before_insert;


CREATE TABLE commConcept (
COMMCONCEPT_ID  NUMBER(20),
COMMCITATION_ID  NUMBER(20),
parentClass VARCHAR2(50),
abiCode  VARCHAR2(50),
classCode VARCHAR2(50),
classLevel VARCHAR2(50),
commDescription VARCHAR2(4000),
commConceptName VARCHAR2(500),  --this maps directly to the commName.commName and
				--the name in each of the ecoart table (cheater)
originDate date, --this a denormalized value corresponding to alliance.allianceorigindate
updateDate date, --this is a denormalized value corresponding to class.update, etc.update
parentAbiCode VARCHAR2(50),
CONSTRAINT commConcept_pk PRIMARY KEY (COMMCONCEPT_ID)
);  
  

CREATE SEQUENCE commConcept_id_seq;
CREATE TRIGGER commConcept_before_insert
BEFORE INSERT ON commConcept FOR EACH ROW
BEGIN
  SELECT commConcept_id_seq.nextval
    INTO :new.COMMCONCEPT_ID
    FROM dual;
END;
/



CREATE TABLE commName (
COMMNAME_ID  NUMBER(20),
COMMNAME VARCHAR2(500),
dateEntered date,
COMMCITATION_ID NUMBER(20),
abiCode  VARCHAR2(50), -- this maps to the classKey, subclassKey, CEGLCode etc
originDate date, --this a denormalized value corresponding to alliance.allianceorigindate
updateDate date, --this is a denormalized value corresponding to class.update, etc.update
CONSTRAINT commName_pk PRIMARY KEY (COMMNAME_ID)
);  
  

CREATE SEQUENCE commName_id_seq;
CREATE TRIGGER commName_before_insert
BEFORE INSERT ON commName FOR EACH ROW
BEGIN
  SELECT commName_id_seq.nextval
    INTO :new.COMMNAME_ID
    FROM dual;
END;
/


CREATE TABLE commUsage (
COMMUSAGE_ID  NUMBER(20),
COMMNAME_ID NUMBER(20),
COMMCONCEPT_ID  NUMBER(20),
commName VARCHAR2(500), --denormalized
abiCode VARCHAR2(50), --denormalized
classSystem VARCHAR2(45),
CONSTRAINT commUsage_pk PRIMARY KEY (COMMUSAGE_ID)
);  


CREATE SEQUENCE commUsage_id_seq;
CREATE TRIGGER commUsage_before_insert
BEFORE INSERT ON commUsage FOR EACH ROW
BEGIN
  SELECT commUsage_id_seq.nextval
    INTO :new.COMMUSAGE_ID
    FROM dual;
END;
/

CREATE TABLE commParty (
COMMPARTY_ID  NUMBER(20),
commPartyName VARCHAR2(45),
CONSTRAINT commParty_pk PRIMARY KEY (COMMPARTY_ID)
);  


CREATE SEQUENCE commParty_id_seq;
CREATE TRIGGER commParty_before_insert
BEFORE INSERT ON commParty FOR EACH ROW
BEGIN
  SELECT commParty_id_seq.nextval
    INTO :new.COMMPARTY_ID
    FROM dual;
END;
/

CREATE TABLE commPartyConcept (
COMMPARTYCONCEPT_ID  NUMBER(20),
COMMCONCEPT_ID  NUMBER(20),
COMMPARTY_ID  NUMBER(20),
startDate date,
stopDate date,
partyConceptStatus VARCHAR2(45),
CONSTRAINT commPartyConcept_pk PRIMARY KEY (COMMPARTYCONCEPT_ID)
);  


CREATE SEQUENCE commPartyConcept_id_seq;
CREATE TRIGGER commPartyConcept_before_insert
BEFORE INSERT ON commPartyConcept FOR EACH ROW
BEGIN
  SELECT commPartyConcept_id_seq.nextval
    INTO :new.COMMPARTYCONCEPT_ID
    FROM dual;
END;
/

CREATE TABLE commCitation (
COMMCITATION_ID  NUMBER(20),
citation  VARCHAR2(40),
abicode VARCHAR2(45),
CONSTRAINT commCitation_pk PRIMARY KEY (COMMCITATION_ID)
);  


CREATE SEQUENCE commCitation_id_seq;
CREATE TRIGGER commCitation_before_insert
BEFORE INSERT ON commCitation FOR EACH ROW
BEGIN
  SELECT commCitation_id_seq.nextval
    INTO :new.COMMCITATION_ID
    FROM dual;
END;
/

CREATE TABLE commPartyUsage (
COMMPARTYUSAGE_ID  NUMBER(20),
COMMPARTY_ID  NUMBER(20),
COMMUSAGE_ID NUMBER(20),
startDate date,
stopDate date,
partyUsageStatus VARCHAR2(20),
commName VARCHAR2(500), --denormalized
abiCode VARCHAR2(50), --denormalized
CONSTRAINT commPartyUsage_pk PRIMARY KEY (COMMPARTYUSAGE_ID)
);  


CREATE SEQUENCE commPartyUsage_id_seq;
CREATE TRIGGER commPartyUsage_before_insert
BEFORE INSERT ON commPartyUsage FOR EACH ROW
BEGIN
  SELECT commPartyUsage_id_seq.nextval
    INTO :new.COMMPARTYUSAGE_ID
    FROM dual;
END;
/



/*
 * Load the tables directly from the ecoart tables -- class table
 */

insert into commConcept (abiCode, classCode, classLevel, commDescription, commConceptName, updateDate) 
select ClassKey, classCode, 'CLASS', classDesc, className, updatedate from class;
commit;

insert into commName (commname, dateEntered, abiCode, updatedate) 
select ClassName, '07-FEB-2001', classKey, updatedate from class;
commit;

insert into commCitation (citation, abiCode) 
select 'ecoartDecember12_2000', classKey from class;
commit;


/*
 * sub class table
 */

insert into commConcept (abiCode, classCode, classLevel, commDescription, commConceptName, updateDate, parentAbiCode) 
select SubClassKey, SubClassCode, 'SUBCLASS', SubClassDesc, SubClassName, updatedate, classKey from subclass;
commit;

insert into commName (commname, dateEntered, abiCode, updatedate) 
select SubClassName, '07-FEB-2001', SubClassKey, updatedate from subclass;
commit;

insert into commCitation (citation, abiCode) 
select 'ecoartDecember12_2000', SubClassKey from subclass;
commit;


/*
 * group_ table
 */
insert into commConcept (abiCode, classCode, classLevel, commDescription, commConceptName, updateDate, parentAbiCode) 
select GroupKey, GroupCode, 'GROUP', GroupDesc, GroupName, updatedate, subclassKey from Group_;
commit;

insert into commName (commname, dateEntered, abiCode, updatedate) 
select GroupName, '07-FEB-2001', GroupKey, updatedate from Group_;
commit;

insert into commCitation (citation, abiCode) 
select 'ecoartDecember12_2000', GroupKey from Group_;
commit;



/*
 * subgroup_ table
 */
insert into commConcept (abiCode, classCode, classLevel, commDescription, commConceptName, updateDate, parentAbiCode) 
select SubGroupKey, SubGroupCode, 'SUBGROUP', SubGroupDesc, SubGroupName, updatedate, groupKey from subgroup;
commit;

insert into commName (commname, dateEntered, abiCode, updatedate) 
select SubGroupName, '07-FEB-2001', SubGroupKey, updatedate from subGroup;
commit;

insert into commCitation (citation, abiCode) 
select 'ecoartDecember12_2000', subGroupKey from subGroup;
commit;



/*
 * formation table
 */
insert into commConcept (abiCode, classCode, classLevel, commDescription, commConceptName, updateDate, parentAbiCode) 
select FormationKey, FormationCode, 'FORMATION', FormationDesc, FormationName, updatedate, subgroupKey from formation;
commit;

insert into commName (commname, dateEntered, abiCode, updatedate) 
select FormationName, '07-FEB-2001', FormationKey, updatedate from formation;
commit;

insert into commCitation (citation, abiCode) 
select 'ecoartDecember12_2000', formationKey from formation;
commit;


/*
 * alliance table
 */
 --notice that the classCode here is the AllianceNum check this out later
insert into commConcept (abiCode, classCode, classLevel, commDescription, commConceptName, updateDate, parentAbiCode) 
select AllianceKey, AllianceNum, 'ALLIANCE', AllianceDesc, AllianceName, updatedate, FormationKey from alliance;
commit;

insert into commName (commname, dateEntered, abiCode, updatedate) 
select AllianceName, '07-FEB-2001', AllianceKey, updatedate from Alliance;
commit;

insert into commCitation (citation, abiCode) 
select 'ecoartDecember12_2000', allianceKey from Alliance;
commit;



/*
 * ETC table -- associations
 */
 --notice the null value in the classCode
 --notice that used the GanmeTrans for the commDesc
insert into commConcept (abiCode, classCode, classLevel, commDescription, commConceptName, updateDate, parentAbiCode) 
select Elcode, '-999.25', 'ASSOCIATION', GnameTrans, GName, gupdate, classifKey from ETC;
commit;

insert into commName (commname, dateEntered, abiCode, updatedate) 
select GName, '07-FEB-2001', Elcode, gupdate from ETC;
commit;

insert into commCitation (citation, abiCode) 
select 'ecoartDecember12_2000', Elcode from Etc;
commit;




/*
 * Update the tables -- from the community tables
 */
 
--update the commUsage table
insert into commUsage (COMMNAME_ID, COMMCONCEPT_ID, classSystem, commName, abiCode) 
select commName.COMMNAME_ID, commConcept.COMMCONCEPT_ID, 'NVC', commName.commname, commConcept.abiCode 
from commName, commConcept
where commName.abiCode = commConcept.abiCode;
commit;

--update the commParty table
insert into commParty (commPartyName) VALUES ('ABI');
commit;
 
 
--update the commPartyConcept table 
insert into commPartyConcept (COMMCONCEPT_ID, COMMPARTY_ID, startDate, partyConceptStatus) 
select commConcept.COMMCONCEPT_ID, 1, commConcept.updateDate, 'accepted' from commConcept
where commConcept.COMMCONCEPT_ID >= 0;
commit;

//insert into commPartyConcept (COMMCONCEPT_ID, COMMPARTY_ID, startDate) 
//select commConcept.COMMCONCEPT_ID, 1, class.updateDate from commConcept, class
//where commConcept.COMMCONCEPT_ID >= 0 and class.classKey like commConcept.abiCode;
//commit;


--update the commPartyUsage table
--commPartyUsage.startDate = commPartyConcept.startDate notice
insert into commPartyUsage (COMMPARTY_ID, COMMUSAGE_ID, commName, abiCode) 
select 1, COMMUSAGE_ID, commName, abiCode from commUsage;
commit;
update commPartyUsage --grab the origin date of the concept
	set startDate =
	(select commConcept.originDate from commConcept
	 where commConcept.abiCode = commPartyUsage.abiCode);
commit;


--update the commConcept table with the correct citation id
update commConcept
	set COMMCITATION_ID = 
	(select COMMCITATION_ID from commcitation 
	where commCitation.abiCode = commConcept.abiCode);
commit;


//select COMMCITATION_ID, abiCode from commConcept;

