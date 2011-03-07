

-- count observations per entity:
UPDATE plantconcept set d_obsCount = (select count(1) from (select observation_ID from 
  taxoninterpretation, view_notemb_taxonobservation as taxonobservation 
  where taxonobservation.taxonobservation_ID=taxoninterpretation.taxonobservation_id 
  AND plantconcept.plantconcept_id=taxoninterpretation.plantconcept_id
  group by observation_ID ) as foo);
  
UPDATE commconcept set d_obsCount = (select count(1) FROM (select observation_ID from
  comminterpretation, view_notemb_commclass as commclass
  WHERE commclass.commclass_ID=comminterpretation.commclass_id AND commconcept.commconcept_ID=comminterpretation.commconcept_id
  GROUP BY  observation_ID) as foo);
  
update project set d_obsCount=(select count(1) from view_notemb_observation as observation 
      where observation.project_ID=project.project_ID);

UPDATE project set d_lastPlotAddedDate = (select max(dateentered) from view_notemb_observation as observation 
    where observation.project_ID=project.project_ID and observation.dateentered is not null);


update party set d_obsCount=(select countallcontrib 
    from view_browseparty_all_count where view_browseparty_all_count.party_ID=party.party_ID);

UPDATE namedplace set d_obsCount=(select count(1) from place where namedplace.namedplacE_ID = place.namedplace_ID);

