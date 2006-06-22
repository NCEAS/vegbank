-- $Id: create_extras.sql,v 1.4 2006-06-22 18:29:32 mlee Exp $
-- 
-- Runs extra SQL on the data model after it is created to make things as they need to be
-- CHIEFLY default values and extra things like custom sequences
-- POPULATION of tables is NOT handled here, neither are triggers
--  NO TABLES ARE CREATED HERE -- they should all be created from db_model_vegbank.xml !

-----default values for fields listed here: ---

ALTER TABLE plot ALTER COLUMN dateentered SET default now();
ALTER TABLE observation ALTER COLUMN dateentered SET default now();
ALTER TABLE plantname ALTER COLUMN dateentered SET default now();
ALTER TABLE commname ALTER COLUMN dateentered SET default now();

ALTER TABLE observationSynonym ALTER COLUMN classStartDate SET default now();
ALTER TABLE partyMember ALTER COLUMN memberStart SET default now();

ALTER TABLE userDataset ALTER COLUMN datasetStart SET default now();
ALTER TABLE userDatasetItem ALTER COLUMN itemStart SET default now();
ALTER TABLE userNotify ALTER COLUMN notifyStart SET default now();
ALTER TABLE userPermission ALTER COLUMN permissionStart SET default now();
ALTER TABLE userQuery ALTER COLUMN queryStart SET default now();
ALTER TABLE userPreference ALTER COLUMN preferenceStart SET default now();
ALTER TABLE userRecordOwner ALTER COLUMN ownerStart SET default now();


-- create a unique sequence that feeds the dba_requestnumber field:
-- can't drop it once it exists, else it would reset! DROP SEQUENCE dba_preassignacccode_dba_requestnumber_seq;
CREATE SEQUENCE dba_preassignacccode_dba_requestnumber_seq;