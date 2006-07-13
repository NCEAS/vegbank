-- ATTENTION!!!

-- views MUST BE DROPPED FIRST, then this file run, then VIEWS RESTORED.  (drop_vegank_views.sql then create_vegbank_views.sql)

--create the table for the xml cache.
create table dba_xmlCache (
accessioncode   varchar(100),
xml             bytea);

--creates a table that stores when each version is implemented on each database
CREATE TABLE dba_datamodelversion
(
dba_datamodelversion_ID serial NOT NULL ,
versionText varchar (20) NOT NULL ,
versionImplemented  timestamp with time zone  DEFAULT now() ,

PRIMARY KEY ( dba_datamodelversion_ID )
);

--create table that stores a list of newly requested accession codes, but these elements aren't in the db yet:
create table dba_preassignacccode (
  dba_preassignacccode_id serial, 
  dba_requestnumber int not null, 
  databasekey varchar(20) not null, 
  tableabbrev varchar(10) not null, 
  confirmcode varchar(70) not null, 
  accessioncode varchar(100), 
  primary key (dba_preassignacccode_id)
  );

-- create a unique sequence that feeds the dba_requestnumber field:
create sequence dba_preassignacccode_dba_requestnumber_seq;
 
 -- set the default value for confirm code
 alter table dba_preassignacccode alter column confirmcode set default (replace(replace(replace(replace(now(),' ','T'),'-',''),':',''),'.','d') || 'R' || floor(random()*1000000));
 
 create table dba_onerow (
   dba_onerow_id int,
   primary key (dba_onerow_id)
   );
   
 --populate dba_onerow (just one empty row of data):  
 insert into dba_onerow (dba_onerow_id) values (1);


--change commclass'es expert system from boolean to text:
ALTER TABLE commClass RENAME COLUMN expertSystem TO expertSystemBoolean;
ALTER TABLE commClass ADD COLUMN expertSystem text;
--TEMP SINCE I DID THIS ALREADY!!!!!
UPDATE commclass SET expertSystem = 'true' WHERE expertSystemBoolean=true;
ALTER TABLE commClass DROP COLUMN expertSystemBoolean;


---new acccession codes added

ALTER TABLE commStatus ADD COLUMN accessionCode varchar (100);
ALTER TABLE plantStatus ADD COLUMN accessionCode varchar (100);
ALTER TABLE GRAPHIC ADD COLUMN accessionCode varchar (100);
ALTER TABLE NOTE ADD COLUMN accessionCode varchar (100);
ALTER TABLE OBSERVATIONSYNONYM ADD COLUMN accessionCode varchar (100);
ALTER TABLE taxonInterpretation ADD COLUMN accessionCode varchar (100);

--new descriptive field on plot:
ALTER TABLE plot ADD COLUMN plotRationaleNarrative text;

--add a number of species found, explicitly:
ALTER TABLE observation ADD COLUMN numberOfTaxa Integer;

---record that this version is now implemented:
insert into dba_datamodelversion (versionText) values ('1.0.5');


-- whether parties are publicly viewable or not, by default true, but turned off as user registers, back on if they get certified
-- so this is just for parties added through the XML loader, which should all be public.
ALTER TABLE party ALTER COLUMN partypublic SET default true;
