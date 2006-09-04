
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




-- tell the database to update indexes, etc., speeds things up a TON! (or a tonne!)
-- DONE IN BACKUP SCRIPT:
-- VACUUM ANALYZE;