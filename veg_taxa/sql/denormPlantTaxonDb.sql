set linesize 700
set heading off
set wrap off
DROP TABLE snapshot2000;

create table snapshot2000 (
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
--it as the basis for the snapshot2000

INSERT INTO snapshot2000 (plantname_id, concatenatedName, status, plantconcept_id, startDate, stopDate)
 SELECT plantname_id, plantname, partyUsageStatus, plantconcept_id, startDate, stopDate 
 from plantusage where plantname_id > 0;



update  snapshot2000 
 set acceptedSynonym =
 (select distinct  plantName from plantUsage where plantUsage.plantName_id = 
  (select distinct  plantName_id from plantUsage where plantUsage.partyUsageStatus  like 'accepted%' and 
    plantUsage.plantConcept_id = snapshot2000.plantconcept_id and
    plantUsage.startDate = snapshot2000.startDate and
    plantUsage.stopDate = snapshot2000.stopDate )
 );



--select distinct  plantUsage.plantName_id, plantUsage.partyUsageStatus, 
--    plantUsage.plantname, plantUsage.startDate 
--from plantUsage , snapshot2000 where plantUsage.partyUsageStatus  like 'accepted%' and
--    plantUsage.plantConcept_id = snapshot2000.plantconcept_id and 
--    plantUsage.startDate = snapshot2000.startDate and 
--    plantUsage.stopDate = snapshot2000.stopDate;


column  concatenatedName format a22
select SUBSTR(concatenatedName, 0, 28), SUBSTR(acceptedSynonym, 0, 28), SUBSTR(status, 0, 3), startDate from 
 snapshot2000 order by acceptedSynonym;
