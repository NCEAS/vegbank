DROP TABLE veg_taxa_summary;

create table veg_taxa_summary (
	plantusage_id int,
	plantname_id int,
	plantconcept_id int,
	plantName varchar(230),
	plantDescription varchar(230),
	plantnamestatus varchar(23),
	classsystem varchar(23),
	plantlevel varchar(23),
	parentName varchar(230),
	acceptedSynonym varchar(80),
	startDate date,
	stopDate date
);


--INSERT ALL THE PLANT NAMES FROM THE USAGE TABLE
--ALL NAMES SHOULD BE INCLUDED HERE
INSERT INTO veg_taxa_summary 
 (plantusage_id, plantname_id, plantconcept_id, plantName, classsystem, plantnamestatus, startdate, stopDate, acceptedSynonym)
 SELECT plantusage_id, plantname_id, plantconcept_id, plantname, classsystem, plantnamestatus, usagestart, usagestop, acceptedSynonym
 from plantusage where plantusage_id > 0;


-- UPDATE THE DESCRIPTION OF THE PLANT WHICH WILL BE THE NAME OF THE 
-- CORESPONDING SCI NAME
update  veg_taxa_summary 
 set plantDescription = (select plantDescription from plantConcept where veg_taxa_summary.plantconcept_id = plantconcept.plantconcept_id );


--UPDATE THE PLANT LEVEL (E.G., SPECIES GENUS VARIETY)
update  veg_taxa_summary 
 set plantlevel = (select plantlevel from plantconcept where veg_taxa_summary.plantconcept_id = plantconcept.plantconcept_id );
--UPDATE THE PARENT NAME
update  veg_taxa_summary 
 set parentName = (select plantparentname from plantstatus where veg_taxa_summary.plantconcept_id = plantstatus.plantconcept_id );

