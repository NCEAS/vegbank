
--get rid of userDatasets that are ANON and OLD
-- first dataset items:
DELETE FROM userDatasetItem WHERE userdataset_ID IN 
   (SELECT userdataset_ID FROM userDataset WHERE (age(datasetStart)>('14 days'::INTERVAL)) AND usr_id IS NULL);
-- then datasets themselves:
DELETE FROM userDataSet WHERE (age(datasetStart)>('14 days'::INTERVAL)) AND usr_id IS NULL AND 
  (select count(1) from userDatasetItem where userDatasetItem.userdataset_ID=userdataset.userdataset_ID) = 0;

--get rid of datasetItems that users have deleted more than a month ago
DELETE FROM userDatasetItem WHERE userdataset_ID IN 
   (SELECT userdataset_ID FROM userDataset WHERE (age(datasetStop)>('40 days'::INTERVAL)) and datasetStop is not null AND datasetType <> 'load' );
-- then datasets themselves:
DELETE FROM userDataSet WHERE (age(datasetStop)>('40 days'::INTERVAL)) and datasetStop is not null AND datasetType <> 'load' AND 
  (select count(1) from userDatasetItem where userDatasetItem.userdataset_ID=userdataset.userdataset_ID) = 0;





UPDATE dba_datacache SET data1 = (SELECT COUNT(1) AS count_observation FROM view_notemb_observation AS observation) where cache_key='DB_STAT.PLOTS';

UPDATE dba_datacache SET data1 = (SELECT COUNT(1) AS count_observation FROM view_notemb_observation AS observation where 
               observation_ID in (select commclass.observation_id from commclass)) WHERE cache_key='DB_STAT.CLASS_PLOTS';

UPDATE dba_datacache SET data1 = (SELECT COUNT(1) AS count_observation FROM view_notemb_observation AS observation where observation_ID in (select commclass.observation_id from commclass, comminterpretation, commstatus, party 
                where commclass.commclass_id=comminterpretation.commclass_id AND comminterpretation.commconcept_id=commstatus.commconcept_id and commstatus.party_id=party.party_id AND 
                lower(party.accessioncode)='vb.py.512.natureserve' AND commStatus.commconceptstatus='accepted' AND 
                commstatus.stopdate is null)) WHERE cache_key='DB_STAT.CLASS_PLOTS_NVC';

UPDATE dba_datacache SET data1 = (SELECT COUNT(1) AS count_plantconcept FROM plantconcept) WHERE cache_key='DB_STAT.PLANT_CONCEPTS';

UPDATE dba_datacache SET data1 = (SELECT COUNT(1) AS count_plantconcept FROM plantconcept where plantconcept_id in (select plantconcept_ID from plantstatus,party where plantstatus.party_Id=party.party_id AND lower(party.accessioncode)='vb.py.511.usdanrcsplants2' AND plantStatus.plantconceptstatus='accepted' 
                AND plantstatus.stopdate is null)) WHERE cache_key='DB_STAT.PC_USDA';

-- dont require current acceptance for plants on plots (add back AND plantstatus.stopdate is null to get it back:
UPDATE dba_datacache SET data1 = (SELECT COUNT(1) AS count_plantconcept FROM plantconcept where plantconcept_id in (select plantconcept_ID from plantstatus,party where plantstatus.party_Id=party.party_id AND lower(party.accessioncode)='vb.py.511.usdanrcsplants2' AND plantStatus.plantconceptstatus='accepted' 
                                         ) AND d_obsCount>=1) WHERE cache_key='DB_STAT.PC_USDA_ON_PLOTS';

UPDATE dba_datacache SET data1 = (SELECT COUNT(1) AS count_commconcept FROM commconcept) WHERE cache_key='DB_STAT.COMMUNITY_CONCEPTS';

UPDATE dba_datacache SET data1 = (SELECT COUNT(1) AS count_commconcept FROM commconcept where commconcept_id in (select commconcept_ID from commstatus,party where commstatus.party_Id=party.party_id AND lower(party.accessioncode)='vb.py.512.natureserve' AND commStatus.commconceptstatus='accepted' AND 
              commstatus.stopdate is null)) WHERE cache_key='DB_STAT.CC_NVC';

UPDATE dba_datacache SET data1 = (SELECT COUNT(1) AS count_commconcept FROM commconcept where commconcept_id in (select commconcept_ID from commstatus,party where commstatus.party_Id=party.party_id AND lower(party.accessioncode)='vb.py.512.natureserve' AND commStatus.commconceptstatus='accepted' AND 
              commstatus.stopdate is null)  AND d_obsCount>=1) WHERE cache_key='DB_STAT.CC_NVC_ON_PLOTS';




-- tell the database to update indexes, etc., speeds things up a TON! (or a tonne!)
-- DONE IN BACKUP SCRIPT:
-- VACUUM ANALYZE;
