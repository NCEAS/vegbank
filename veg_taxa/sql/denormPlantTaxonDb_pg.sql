DROP TABLE veg_taxa_summary;
DROP INDEX plantusage_id_idx;
DROP INDEX sumconcept_id_idx;
DROP INDEX plantcon_id_idx;
DROP INDEX plantuse_id_idx;
DROP INDEX summary_idx;

DROP INDEX plantdesc_idx;
DROP INDEX plantlevel_idx;
DROP INDEX parentname_idx;

create table veg_taxa_summary (
	plantusage_id integer,
	plantname_id integer,
	plantconcept_id integer,
	plantName varchar(230),
	plantNameAlias varchar(230),
	plantDescription varchar(230),
	plantnamestatus varchar(23),
	classsystem varchar(23),
	plantlevel varchar(23),
	parentName varchar(230),
	acceptedSynonym varchar(80),
	startDate date,
	stopDate date,
	plantConceptRefAuthor varchar(230),
	plantConceptRefDate date,
	plantparty_id integer,
	plantUsagePartyOrgName varchar(230)
);

--CREATE INDICIES ON BASE TABLES IMPROVE PERFORMANCE ON QUERYING
create index plantuse_id_idx on plantusage (plantusage_id);
create index plantcon_id_idx on plantconcept (plantconcept_id);

--INSERT ALL THE PLANT NAMES FROM THE USAGE TABLE
--ALL NAMES SHOULD BE INCLUDED HERE
INSERT INTO veg_taxa_summary 
 (plantusage_id, plantname_id, plantconcept_id, plantName, classsystem, plantnamestatus, startdate, stopDate, acceptedSynonym, plantparty_id)
 SELECT plantusage_id, plantname_id, plantconcept_id, plantname, classsystem, plantnamestatus, usagestart, usagestop, acceptedSynonym,  plantparty_id
 from plantusage where plantusage_id > 0;

-- CREATE INDICIES ON THE SUMMARY TABLE FOR IMPROVED QUERY PERFORMANCE
--create index plantusage_id_idx on veg_taxa_summary (plantusage_id);
--create index sumconcept_id_idx on veg_taxa_summary (plantconcept_id);
--create index plantdesc_idx on veg_taxa_summary (plantdescription);
--create index plantlevel_idx on veg_taxa_summary (plantlevel);
--create index parentname_idx on veg_taxa_summary (parentname);

-- UPDATE THE PLANT NAME WITH AUTHOR
update  veg_taxa_summary 
 set plantNameAlias = (select plantnamewithauthor from plantname where veg_taxa_summary.plantname_id = plantname.plantname_id );
-- UPDATE THE DESCRIPTION OF THE PLANT WHICH WILL BE THE NAME OF THE 
-- CORESPONDING SCI NAME
update  veg_taxa_summary 
 set plantDescription = (select plantDescription from plantConcept where veg_taxa_summary.plantconcept_id = plantconcept.plantconcept_id );
--UPDATE THE PLANT LEVEL (E.G., SPECIES GENUS VARIETY)
update  veg_taxa_summary 
 set plantlevel = (select plantlevel from plantconcept where veg_taxa_summary.plantconcept_id = plantconcept.plantconcept_id );
--UPDATE THE PARENT NAME
update  veg_taxa_summary 
 set parentName = (select distinct(plantparentname) from plantstatus where veg_taxa_summary.plantconcept_id = plantstatus.plantconcept_id );
--UPDATE THE CONCEPT AUTHOR AND THE DATE
update  veg_taxa_summary 
 set plantConceptRefAuthor = (select authors from plantreference where plantreference_id = 
 (select plantreference_id from plantconcept where  veg_taxa_summary.plantconcept_id = plantconcept.plantconcept_id  ) );
update  veg_taxa_summary 
 set plantConceptRefDate = (select pubdate from plantreference where plantreference_id = 
 (select plantreference_id from plantconcept where  veg_taxa_summary.plantconcept_id = plantconcept.plantconcept_id  ) );
--UPDATE THE PARTY OR NAME FOR THE USAGE
update  veg_taxa_summary 
 set plantUsagePartyOrgName = (select organizationname from plantparty where veg_taxa_summary.plantparty_id = plantparty.plantparty_id );



--SET THE STOPDATES FOR THE 1986 DATA AT 2002 AND SET THE STOP DATES FOR THE 2002 DATA TILL 2005, WHEN THE NEXT PLANTS LIST WILL BE LOADED
update veg_taxa_summary  set stopdate  = '2002-06-30' where startdate = '1996-06-30';
update veg_taxa_summary  set stopdate  = '2005-06-30' where startdate = '2002-08-20';

--CREATE THE INDEX FOR QUERY PERFORMANCE
create index summary_idx on veg_taxa_summary (plantname, PLANTUSAGEPARTYORGNAME,  STARTDATE, STOPDATE, PLANTLEVEL, CLASSSYSTEM);


