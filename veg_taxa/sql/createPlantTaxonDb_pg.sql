/*
 * Creates the tables for the 
 * plant names database on an 
 * postgresql  RDBMS
 *
 */


DROP TABLE plantCitation;
DROP TABLE plantName;
DROP TABLE plantUsage;
DROP TABLE plantCorrelation;
DROP TABLE plantConcept;
DROP TABLE plantLineage;
DROP TABLE plantConceptStatus;

DROP SEQUENCE plantname_id_seq;
DROP SEQUENCE plantconcept_id_seq;
DROP SEQUENCE plantusage_id_seq;
DROP SEQUENCE plantcorrelation_id_seq;
DROP SEQUENCE plantconstatus_id_seq;


/*
 * citations table
 */
CREATE TABLE plantCitation (
plantcitation_id INT NOT NULL PRIMARY KEY,
citation varchar(400)
);



/*
 * plant conceptstatus table
 */

CREATE SEQUENCE plantconstatus_id_seq;
 
CREATE TABLE plantConceptStatus (
plantconceptstatus_id INT default nextval('plantconstatus_id_seq'),
plantconcept_id INT ,
status varchar(70),
startDate date,
partyName varchar(20),
stopDate date,
event varchar(20)
--FOREIGN KEY (ref_id) REFERENCES reference
);

--CREATE SEQUENCE plantconstatus_id_seq;
--CREATE TRIGGER plantconstatus_before_insert
--BEFORE INSERT ON plantconceptstatus FOR EACH ROW
--BEGIN
--  SELECT plantconstatus_id_seq.nextval
--    INTO :new.plantconceptstatus_id
--    FROM dual;
--END;
--/





/*
 * plant names table
 */
CREATE SEQUENCE plantname_id_seq;

CREATE TABLE plantName (
plantname_id INT default nextval('plantname_id_seq'),
plantSymbol varchar(70),
plantName varchar(200),
plantCommonName varchar(200),
plantcitation_id INT,
date_entered date
--FOREIGN KEY (ref_id) REFERENCES reference
);

--CREATE SEQUENCE plantname_id_seq;
--CREATE TRIGGER plantname_before_insert
--BEFORE INSERT ON plantname FOR EACH ROW
--BEGIN
--  SELECT plantname_id_seq.nextval
--    INTO :new.plantname_id
--    FROM dual;
--END;
--/


/*
 * plant name - concept usage table
 */
CREATE SEQUENCE plantusage_id_seq;

CREATE TABLE plantUsage (
plantusage_Id INT NOT NULL PRIMARY KEY default nextval('plantusage_id_seq'),
plantname_id INT,
plantconcept_id INT,
partyName varchar(70),
partyUsageStatus varchar(70),
startdate date,
stopdate date,
tsnCode varchar(70), --denormalized from the concept table
plantName varchar(300) --denormalized from the name table
--FOREIGN KEY (plantname_id) REFERENCES plantName,
--FOREIGN KEY (plantconcept_id) REFERENCES plantConcept,
);

--CREATE SEQUENCE plantusage_id_seq;
--CREATE TRIGGER plantusage_before_insert
--BEFORE INSERT ON plantusage FOR EACH ROW
--BEGIN
--  SELECT plantusage_id_seq.nextval
--    INTO :new.plantusage_id
--    FROM dual;
--END;
--/




/*
 * correlation table
 */
CREATE SEQUENCE plantcorrelation_id_seq;

CREATE TABLE plantCorrelation (
plantcorrelation_id INT NOT NULL PRIMARY KEY default nextval('plantcorrelation_id_seq'),
plantName varchar(200),
plantConceptStatus_id INT,
plantConcept_id INT,
convergence varchar(20),
startDate date,
stopDate date
--FOREIGN KEY (plantUsage1_id) REFERENCES plantUsage,
--FOREIGN KEY (plantUsage2_id) REFERENCES plantUsage
);
--CREATE SEQUENCE plantcorrelation_id_seq;
--CREATE TRIGGER plantcorrelation_before_insert
--BEFORE INSERT ON plantcorrelation FOR EACH ROW
--BEGIN
--  SELECT plantcorrelation_id_seq.nextval
--    INTO :new.plantcorrelation_id
--    FROM dual;
--END;
--/



/*
 * plant concept table
 */
CREATE SEQUENCE plantconcept_id_seq;

CREATE TABLE plantConcept (
plantconcept_id INT NOT NULL PRIMARY KEY default nextval('plantconcept_id_seq'),
plantcitation_id INT,
parentConcept_id INT,
partyName varchar(25),
classCode varchar(20), -- general code for reference (tsnCode for itis etc)
classRank varchar(20), -- ie. kingdom, class etc
conceptRef varchar(26), -- the reference src for the concept (like PLANTS-1996) 
partyConceptStatus varchar(20),
startDate date,
stopDate date,
plantName varchar(120) -- cheat column to refer to the first instance of a name
	--that is associated with this concept
--FOREIGN KEY (plantUsage1_id) REFERENCES plantUsage,
--FOREIGN KEY (plantUsage2_id) REFERENCES plantUsage
);

--CREATE SEQUENCE plantconcept_id_seq;
--CREATE TRIGGER plantconcept_before_insert
--BEFORE INSERT ON plantconcept FOR EACH ROW
--BEGIN
--  SELECT plantconcept_id_seq.nextval
--    INTO :new.plantconcept_id
--    FROM dual;
--END;
--/





/*
 * lineage table
 */
CREATE TABLE plantLineage (
plantlineage_id INT NOT NULL PRIMARY KEY,
plantUsage1_id INT,
plantUsage2_id INT
--FOREIGN KEY (plantUsage1_id) REFERENCES plantUsage,
--FOREIGN KEY (plantUsage2_id) REFERENCES plantUsage
);



/*
INSERT into party (party_id, org_name)  VALUES (001, 'NFA');
INSERT into party (party_id, org_name)  VALUES (002, 'PLANTS');
INSERT into party (party_id, org_name)  VALUES (003, 'USDA');
*/



