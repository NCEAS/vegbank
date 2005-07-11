--creates and populates temporary tables in VegBank:
-- MUST START WITH temptbl_ to be ignored by verify script



DROP TABLE temptbl_std_plantnames;
CREATE TABLE temptbl_std_plantnames
(
PLANTCONCEPT_ID serial ,
sciname varchar (255),
scinamenoauth varchar (255),
code varchar (255),
common varchar (255),
PRIMARY KEY ( PLANTCONCEPT_ID )
);


INSERT INTO temptbl_std_plantnames (plantConcept_ID, sciname, scinamenoauth,code,common) 
  SELECT plantConcept_ID, 
  (select (plantname) from view_std_plantnames_sciname as newnames
    WHERE newnames.plantconcept_ID = plantconcept.plantconcept_id limit 1), 
    (select (plantname) from view_std_plantnames_scinamenoauth  as newnames
    WHERE newnames.plantconcept_ID = plantconcept.plantconcept_id limit 1),
    (select (plantname) from view_std_plantnames_code  as newnames
    WHERE newnames.plantconcept_ID = plantconcept.plantconcept_id limit 1),
    (select (plantname) from view_std_plantnames_common  as newnames
    WHERE newnames.plantconcept_ID = plantconcept.plantconcept_id limit 1) FROM plantConcept where plantconcept_ID<493;

--populate systems:
--UPDATE temptbl_std_plantnames SET sciname=(select min(plantname) from view_std_plantnames_sciname as newnames
--    WHERE newnames.plantconcept_ID = temptbl_std_plantnames.plantconcept_id);
    
--UPDATE temptbl_std_plantnames SET scinamenoauth=(select min(plantname) from view_std_plantnames_scinamenoauth  as newnames
--    WHERE newnames.plantconcept_ID = plantconcept.plantconcept_id);
    
--UPDATE temptbl_std_plantnames SET code=(select min(plantname) from view_std_plantnames_code  as newnames
--    WHERE newnames.plantconcept_ID = plantconcept.plantconcept_id);
    
--UPDATE temptbl_std_plantnames SET common=(select min(plantname) from view_std_plantnames_common  as newnames
--    WHERE newnames.plantconcept_ID = plantconcept.plantconcept_id);
    
--get any that aren't std:
UPDATE temptbl_std_plantnames SET sciname=(select min(plantname) from view_all_plantnames_sciname as newnames
    WHERE newnames.plantconcept_ID = temptbl_std_plantnames.plantconcept_id) WHERE sciname is null;
    
UPDATE temptbl_std_plantnames SET scinamenoauth=(select min(plantname) from view_all_plantnames_scinamenoauth  as newnames
    WHERE newnames.plantconcept_ID = temptbl_std_plantnames.plantconcept_id) WHERE scinamenoauth is null;
    
-- not code, that leaves only to USDA : UPDATE temptbl_std_plantnames SET code=(select min(plantname) from view_all_plantnames_code  as newnames
--    WHERE newnames.plantconcept_ID = temptbl_std_plantnames.plantconcept_id) WHERE code is null;
    
UPDATE temptbl_std_plantnames SET common=(select min(plantname) from view_all_plantnames_common  as newnames
    WHERE newnames.plantconcept_ID = temptbl_std_plantnames.plantconcept_id) WHERE common is null;