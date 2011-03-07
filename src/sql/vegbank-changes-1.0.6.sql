-- VIEWS must be dropped before running this!

--create the table for the data cache.
drop table dba_dataCache;
create table dba_dataCache (
  DBA_DATACACHE_ID serial,
  cache_key varchar(200) NOT NULL,
  data1 varchar(255),
  data2 varchar(255),
  data3 varchar(255),
  data4 varchar(255),
  data5 varchar(255),
  data6 varchar(255),
  data7 varchar(255),
  data8 varchar(255),
  data9 varchar(255),
  data10 varchar(255),
  primary key (DBA_DATACACHE_ID)
);


CREATE UNIQUE INDEX dba_datacache_key ON dba_dataCache (cache_key);

--populate this table

INSERT INTO  dba_datacache (cache_key, data1) values ('DB_STAT_PLOTS',22345);
INSERT INTO  dba_datacache (cache_key, data1) values ('DB_STAT_CLASS_PLOTS',15669);
INSERT INTO  dba_datacache (cache_key, data1) values ('DB_STAT_CLASS_PLOTS_NVC',5166);
INSERT INTO  dba_datacache (cache_key, data1) values ('DB_STAT_PLANT_CONCEPTS',91984);
INSERT INTO  dba_datacache (cache_key, data1) values ('DB_STAT_PC_USDA',43753);
INSERT INTO  dba_datacache (cache_key, data1) values ('DB_STAT_PC_USDA_ON_PLOTS',7217);
INSERT INTO  dba_datacache (cache_key, data1) values ('DB_STAT_COMMUNITY_CONCEPTS',15128);
INSERT INTO  dba_datacache (cache_key, data1) values ('DB_STAT_CC_NVC',8390);
INSERT INTO  dba_datacache (cache_key, data1) values ('DB_STAT_CC_NVC_ON_PLOTS',896);

-- add denorm table for date_added to project:
ALTER TABLE project ADD COLUMN d_lastPlotAddedDate timestamp with time zone;
UPDATE project set d_lastPlotAddedDate = (select max(dateentered) from observation where observation.project_ID=project.project_ID);

-- increase size of accession codes so that we can support LSIDs, which may be longer than native VegBank AccessionCodes

ALTER TABLE dba_preassignacccode ADD COLUMN accessionCode2 varchar(255);
   UPDATE dba_preassignacccode set AccessionCode2 = accessionCode;
   alter table dba_preassignacccode drop column accessionCode;
   alter table dba_preassignacccode rename column accessionCode2 TO accessionCode;
  
ALTER TABLE dba_xmlcache ADD COLUMN accessionCode2 varchar(255);
   UPDATE dba_xmlcache set AccessionCode2 = accessionCode;
   alter table dba_xmlcache drop column accessionCode;
   alter table dba_xmlcache rename column accessionCode2 TO accessionCode;
  
ALTER TABLE graphic ADD COLUMN accessionCode2 varchar(255);
   UPDATE graphic set AccessionCode2 = accessionCode;
   alter table graphic drop column accessionCode;
   alter table graphic rename column accessionCode2 TO accessionCode;
  
ALTER TABLE note ADD COLUMN accessionCode2 varchar(255);
   UPDATE note set AccessionCode2 = accessionCode;
   alter table note drop column accessionCode;
   alter table note rename column accessionCode2 TO accessionCode;
  
ALTER TABLE project ADD COLUMN accessionCode2 varchar(255);
   UPDATE project set AccessionCode2 = accessionCode;
   alter table project drop column accessionCode;
   alter table project rename column accessionCode2 TO accessionCode;
  
ALTER TABLE commclass ADD COLUMN accessionCode2 varchar(255);
   UPDATE commclass set AccessionCode2 = accessionCode;
   alter table commclass drop column accessionCode;
   alter table commclass rename column accessionCode2 TO accessionCode;
  
ALTER TABLE userdefined ADD COLUMN accessionCode2 varchar(255);
   UPDATE userdefined set AccessionCode2 = accessionCode;
   alter table userdefined drop column accessionCode;
   alter table userdefined rename column accessionCode2 TO accessionCode;
  
ALTER TABLE referenceparty ADD COLUMN accessionCode2 varchar(255);
   UPDATE referenceparty set AccessionCode2 = accessionCode;
   alter table referenceparty drop column accessionCode;
   alter table referenceparty rename column accessionCode2 TO accessionCode;
  
ALTER TABLE commconcept ADD COLUMN accessionCode2 varchar(255);
   UPDATE commconcept set AccessionCode2 = accessionCode;
   alter table commconcept drop column accessionCode;
   alter table commconcept rename column accessionCode2 TO accessionCode;
  
ALTER TABLE referencejournal ADD COLUMN accessionCode2 varchar(255);
   UPDATE referencejournal set AccessionCode2 = accessionCode;
   alter table referencejournal drop column accessionCode;
   alter table referencejournal rename column accessionCode2 TO accessionCode;
  
ALTER TABLE commstatus ADD COLUMN accessionCode2 varchar(255);
   UPDATE commstatus set AccessionCode2 = accessionCode;
   alter table commstatus drop column accessionCode;
   alter table commstatus rename column accessionCode2 TO accessionCode;
  
ALTER TABLE covermethod ADD COLUMN accessionCode2 varchar(255);
   UPDATE covermethod set AccessionCode2 = accessionCode;
   alter table covermethod drop column accessionCode;
   alter table covermethod rename column accessionCode2 TO accessionCode;
  
ALTER TABLE userquery ADD COLUMN accessionCode2 varchar(255);
   UPDATE userquery set AccessionCode2 = accessionCode;
   alter table userquery drop column accessionCode;
   alter table userquery rename column accessionCode2 TO accessionCode;
  
ALTER TABLE taxonobservation ADD COLUMN accessionCode2 varchar(255);
   UPDATE taxonobservation set AccessionCode2 = accessionCode;
   alter table taxonobservation drop column accessionCode;
   alter table taxonobservation rename column accessionCode2 TO accessionCode;
  
ALTER TABLE userdataset ADD COLUMN accessionCode2 varchar(255);
   UPDATE userdataset set AccessionCode2 = accessionCode;
   alter table userdataset drop column accessionCode;
   alter table userdataset rename column accessionCode2 TO accessionCode;
  
ALTER TABLE soiltaxon ADD COLUMN accessionCode2 varchar(255);
   UPDATE soiltaxon set AccessionCode2 = accessionCode;
   alter table soiltaxon drop column accessionCode;
   alter table soiltaxon rename column accessionCode2 TO accessionCode;
  
ALTER TABLE taxoninterpretation ADD COLUMN accessionCode2 varchar(255);
   UPDATE taxoninterpretation set AccessionCode2 = accessionCode;
   alter table taxoninterpretation drop column accessionCode;
   alter table taxoninterpretation rename column accessionCode2 TO accessionCode;
  
ALTER TABLE namedplace ADD COLUMN accessionCode2 varchar(255);
   UPDATE namedplace set AccessionCode2 = accessionCode;
   alter table namedplace drop column accessionCode;
   alter table namedplace rename column accessionCode2 TO accessionCode;
  
ALTER TABLE plot ADD COLUMN accessionCode2 varchar(255);
   UPDATE plot set AccessionCode2 = accessionCode;
   alter table plot drop column accessionCode;
   alter table plot rename column accessionCode2 TO accessionCode;
  
ALTER TABLE plantconcept ADD COLUMN accessionCode2 varchar(255);
   UPDATE plantconcept set AccessionCode2 = accessionCode;
   alter table plantconcept drop column accessionCode;
   alter table plantconcept rename column accessionCode2 TO accessionCode;
  
ALTER TABLE observationsynonym ADD COLUMN accessionCode2 varchar(255);
   UPDATE observationsynonym set AccessionCode2 = accessionCode;
   alter table observationsynonym drop column accessionCode;
   alter table observationsynonym rename column accessionCode2 TO accessionCode;
  
ALTER TABLE plantstatus ADD COLUMN accessionCode2 varchar(255);
   UPDATE plantstatus set AccessionCode2 = accessionCode;
   alter table plantstatus drop column accessionCode;
   alter table plantstatus rename column accessionCode2 TO accessionCode;
  
ALTER TABLE stratummethod ADD COLUMN accessionCode2 varchar(255);
   UPDATE stratummethod set AccessionCode2 = accessionCode;
   alter table stratummethod drop column accessionCode;
   alter table stratummethod rename column accessionCode2 TO accessionCode;
  
ALTER TABLE observation ADD COLUMN accessionCode2 varchar(255);
   UPDATE observation set AccessionCode2 = accessionCode;
   alter table observation drop column accessionCode;
   alter table observation rename column accessionCode2 TO accessionCode;
  
ALTER TABLE aux_role ADD COLUMN accessionCode2 varchar(255);
   UPDATE aux_role set AccessionCode2 = accessionCode;
   alter table aux_role drop column accessionCode;
   alter table aux_role rename column accessionCode2 TO accessionCode;
  
ALTER TABLE reference ADD COLUMN accessionCode2 varchar(255);
   UPDATE reference set AccessionCode2 = accessionCode;
   alter table reference drop column accessionCode;
   alter table reference rename column accessionCode2 TO accessionCode;
  
ALTER TABLE party ADD COLUMN accessionCode2 varchar(255);
   UPDATE party set AccessionCode2 = accessionCode;
   alter table party drop column accessionCode;
   alter table party rename column accessionCode2 TO accessionCode;
 
