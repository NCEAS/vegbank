-- $Id: create_extras.sql,v 1.2 2004-11-30 23:56:53 mlee Exp $
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
