-- $Id: create_extras.sql,v 1.3 2005-02-03 22:59:57 mlee Exp $
-- 
-- Responsible for creating tables used by the Vegbank app
-- but not found in the model.
--

CREATE TABLE keywords (table_id integer NOT NULL,entity text NOT NULL, keywords text);

CREATE INDEX keywords_table_id_entity_key ON keywords (table_id,entity);

CREATE TABLE keywords_extra (table_id integer NOT NULL,entity text NOT NULL, keywords text);


-----default values for fields listed here: ---

alter table plot alter column dateentered set default now();

alter table observation alter column dateentered set default now();

alter table plantname alter column dateentered set default now();

alter table commname alter column dateentered set default now();

ALTER TABLE observationSynonym ALTER COLUMN classStartDate SET default now();
ALTER TABLE partyMember ALTER COLUMN memberStart SET default now();

ALTER TABLE userDataset ALTER COLUMN datasetStart SET default now();
ALTER TABLE userDatasetItem ALTER COLUMN itemStart SET default now();
ALTER TABLE userNotify ALTER COLUMN notifyStart SET default now();
ALTER TABLE userPermission ALTER COLUMN permissionStart SET default now();
ALTER TABLE userQuery ALTER COLUMN queryStart SET default now();
ALTER TABLE userPreference ALTER COLUMN preferenceStart SET default now();
ALTER TABLE userRecordOwner ALTER COLUMN ownerStart SET default now();