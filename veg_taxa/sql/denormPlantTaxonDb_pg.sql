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
 (plantusage_id, plantname_id, plantconcept_id, plantName, classsystem, plantnamestatus, startdate, stopDate)
 SELECT plantusage_id, plantname_id, plantconcept_id, plantname, classsystem, plantnamestatus, usagestart, usagestop
 from plantusage where plantusage_id > 0;

/*
--UPDATE THE CONCEPT INFORMATION WHICH FOR THE 
--CODES AND COMMON NAMES IS THE CONCEPT ID 
--CORRESPONDING TO THE SCIENTIFIC NAME
update  veg_taxa_summary 
 set plantConcept_id = (select plantConcept_id from plantConcept where veg_taxa_summary.plantName_id = plantconcept.plantname_id );
update veg_taxa_summary
 set plantConcept_id = (select plantConcept_id from plantUsage where veg_taxa_summary.plantName_id = plantusage.plantname_id );
*/



-- UPDATE THE DESCRIPTION OF THE PLANT WHICH WILL BE THE NAME OF THE 
-- CORESPONDING SCI NAME
update  veg_taxa_summary 
 set plantDescription = (select plantDescription from plantConcept where veg_taxa_summary.plantconcept_id = plantconcept.plantconcept_id );

/*
--UPDATE THE NAME STATUS OF EACH PLANT
update  veg_taxa_summary 
 set plantnamestatus = (select plantnamestatus from plantusage where veg_taxa_summary.plantName_id = plantusage.plantname_id );
*/

/*
--UPDATE THE CLASS SYSTEM (EG. CODE, SCIENTIFIC NAME)
update  veg_taxa_summary 
 set classsystem = (select classsystem from plantusage where veg_taxa_summary.plantName_id = plantusage.plantname_id );
*/

--UPDATE THE PLANT LEVEL (E.G., SPECIES GENUS VARIETY)
update  veg_taxa_summary 
 set plantlevel = (select plantlevel from plantconcept where veg_taxa_summary.plantconcept_id = plantconcept.plantconcept_id );
--UPDATE THE PARENT NAME
update  veg_taxa_summary 
 set parentName = (select plantparentname from plantstatus where veg_taxa_summary.plantconcept_id = plantstatus.plantconcept_id );



/*

update  veg_taxa_summary 
 set acceptedSynonym =
 (select distinct  plantName from plantUsage where plantUsage.plantName_id = 
  (select distinct  plantName_id from plantUsage where plantUsage.partyUsageStatus  like 'accepted%' and 
    plantUsage.plantConcept_id = veg_taxa_summary.plantconcept_id and
    plantUsage.startDate = veg_taxa_summary.startDate and
    plantUsage.stopDate = veg_taxa_summary.stopDate )
 );



--select distinct  plantUsage.plantName_id, plantUsage.partyUsageStatus, 
--    plantUsage.plantname, plantUsage.startDate 
--from plantUsage , veg_taxa_summary where plantUsage.partyUsageStatus  like 'accepted%' and
--    plantUsage.plantConcept_id = veg_taxa_summary.plantconcept_id and 
--    plantUsage.startDate = veg_taxa_summary.startDate and 
--    plantUsage.stopDate = veg_taxa_summary.stopDate;


--column  concatenatedName format a22

select SUBSTR(concatenatedName, 0, 28), SUBSTR(acceptedSynonym, 0, 28), SUBSTR(status, 0, 3), startDate from 
 veg_taxa_summary order by acceptedSynonym;

*/
