-- $Id: create_extras.sql,v 1.1 2004-11-11 21:39:31 anderson Exp $
-- 
-- Responsible for creating tables used by the Vegbank app
-- but not found in the model.
--

CREATE TABLE keywords (table_id integer NOT NULL,entity text NOT NULL, keywords text);

CREATE INDEX keywords_table_id_entity_key ON keywords (table_id,entity);

CREATE TABLE keywords_extra (table_id integer NOT NULL,entity text NOT NULL, keywords text);
