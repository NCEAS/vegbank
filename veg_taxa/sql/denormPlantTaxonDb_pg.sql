DROP TABLE veg_taxa_summary;

create table veg_taxa_summary (
	plantname_id int,
	plantconcept_id int,
	concatenatedName varchar(80),
	commonName varchar(100),
	status varchar(12),
	acceptedSynonym varchar(80),
	startDate date,
	stopDate date
);

--assume that the table with the most 
--rows will be the usage table so use
--it as the basis for the veg_taxa_summary

INSERT INTO veg_taxa_summary (plantname_id, concatenatedName, status, plantconcept_id, startDate, stopDate)
 SELECT plantname_id, plantname, partyUsageStatus, plantconcept_id, startDate, stopDate 
 from plantusage where plantname_id > 0;



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
