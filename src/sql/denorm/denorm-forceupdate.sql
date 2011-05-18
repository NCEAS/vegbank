
update commUsage SET commName = (select commname.commname from commname where 
commname.commname_ID=commUsage.commName_ID) ;

update commUsage SET COMMCONCEPT_ID = (select commstatus.commConcept_ID from 
commStatus where commStatus.commStatus_ID=commUsage.commStatus_ID) where commStatus_ID is not null ;

update plantConcept SET plantName = (select plantname.plantname from plantname 
where plantname.plantname_ID=plantConcept.plantName_ID) ;

update plantUsage SET plantCONCEPT_ID = (select plantstatus.plantConcept_ID 
from plantStatus where plantStatus.plantStatus_ID=plantUsage.plantStatus_ID) 
where plantStatus_ID is not null ;

update plantUsage SET plantName = (select plantname.plantname from plantname 
where plantname.plantname_ID=plantUsage.plantName_ID) ;

update commInterpretation SET commName = (select commname.commname from 
commname, commconcept where commname.commname_ID=commConcept.commName_ID and 
commConcept.commConcept_ID=commInterpretation.commconcept_ID) ;

  UPDATE plot set country = 
    (SELECT 
       (
       SELECT np2.placeName FROM namedPlace as np2 WHERE np2.namedPlace_ID = min(np1.namedPlace_ID)
       )
    FROM namedPlace as np1 , place WHERE  np1.NAMEDPLACE_ID = place.NAMEDPLACE_ID
    and np1.placeSystem='area|country|territory' and place.plot_ID = plot.plot_ID
    )
  ;

update stratum set stratumMethod_ID = (select stratumType.stratumMethod_ID 
from stratumType where stratumType.stratumType_ID = stratum.stratumType_ID) 
WHERE  stratumType_ID is not null;

update stratum set stratumName = (select stratumType.stratumName from 
stratumType where stratumType.stratumType_ID = stratum.stratumType_ID) 
WHERE  stratumType_ID is not null;

update commUsage SET party_ID = (select commstatus.party_ID from commStatus 
where commStatus.commStatus_ID=commUsage.commStatus_ID) where commStatus_ID 
is not null ;

update plantUsage SET party_ID = (select plantstatus.party_ID from plantStatus 
where plantStatus.plantStatus_ID=plantUsage.plantStatus_ID) where 
plantStatus_ID is not null ;

# this is needed so that grabbing county description works 
  UPDATE plot set stateProvice = null;
  

     UPDATE plot set stateProvince = 
       (SELECT 
          (
          SELECT np2.placeName FROM namedPlace as np2 WHERE np2.namedPlace_ID = min(np1.namedPlace_ID)
          )
       FROM namedPlace as np1 , place WHERE  np1.NAMEDPLACE_ID = place.NAMEDPLACE_ID
       and np1.placeSystem='region|state|province' and place.plot_ID = plot.plot_ID
       )
     ;
     
# get state province from county description in namedplace , if state is not already specified (from above)     
update plot set stateprovince = 
   (SELECT   
     (SELECT np2.placeDescription FROM namedplace as np2 WHERE 
        np2.namedPlace_Id= min(np1.namedPlace_ID)    
     ) FROM namedPlace as np1 , place where np1.NAMEDPLACE_ID=place.NAMEDPLACE_ID 
     and np1.placeSystem='county' and place.plot_ID=plot.plot_ID      
   )    WHERE stateprovince is null;



update taxonImportance set stratumHeight=(select stratumHeight from stratum 
where taxonImportance.stratum_ID=stratum.stratum_ID) where 
taxonImportance.stratum_ID is not null ;

update taxonImportance set stratumBase=(select stratumBase from stratum where 
taxonImportance.stratum_ID=stratum.stratum_ID) where 
taxonImportance.stratum_ID is not null;


update commConcept set commName=(select commName from commName where 
commName.commName_id=commConcept.commName_ID);

-- update embargo denorm fields for full embargo

-- ORDER OF THESE STATEMENTS MATTERS! the latter read from the former.

--plot
update plot set emb_plot = (select currentEmb from view_emb_embargo_complete 
where plot.plot_id = view_emb_embargo_complete.plot_ID);

--observation
update observation set emb_observation = (select emb_plot from plot 
where plot.plot_ID=observation.plot_id);

--disturbanceObs
update disturbanceObs set emb_disturbanceObs = (select emb_observation 
from observation where observation.observation_ID=disturbanceObs.observation_ID);

--soilObs
update soilObs set emb_soilObs = (select emb_observation from observation 
where observation.observation_ID=soilObs.observation_ID);

--commClass
UPDATE commClass set emb_commClass = (select emb_observation from observation 
where observation.observation_ID=commClass.observation_ID);

---classContributor
UPDATE classContributor SET emb_classContributor= (select emb_commClass from 
commClass where commClass.commClass_ID=classContributor.commClass_ID);

---commInterpretation
UPDATE commInterpretation SET emb_commInterpretation= (select emb_commClass 
from commClass where commClass.commClass_ID=commInterpretation.commClass_ID);

--taxonObservation

update taxonObservation set emb_taxonObservation = (select emb_observation 
from observation where observation.observation_ID=taxonObservation.observation_ID);

---taxonImportance
update taxonImportance set emb_taxonImportance = (select emb_taxonObservation 
FROM taxonObservation   where 
taxonImportance.taxonObservation_id =taxonObservation.taxonObservation_ID);

----stemCount
update stemCount set emb_stemCount = (select emb_taxonImportance FROM 
taxonImportance   
where stemCount.taxonImportance_id =taxonImportance.taxonImportance_ID);

-----stemLocation
update stemLocation set emb_stemLocation = (select emb_stemCount FROM stemCount   
where stemLocation.stemCount_id =stemCount.stemCount_ID);

---taxonInterpretation
update taxonInterpretation set emb_taxonInterpretation = 
(select emb_taxonObservation FROM taxonObservation   
where taxonInterpretation.taxonObservation_id =taxonObservation.taxonObservation_ID);

----taxonAlt
update taxonAlt set emb_taxonAlt = (select emb_taxonInterpretation 
FROM taxonInterpretation where 
taxonAlt.taxonInterpretation_id = taxonInterpretation.taxonInterpretation_ID);

-- count observations per entity:
UPDATE plantconcept set d_obsCount = (select count(1) from (select observation_ID from 
  taxoninterpretation, view_notemb_taxonobservation as taxonobservation 
  where taxonobservation.taxonobservation_ID=taxoninterpretation.taxonobservation_id 
  AND plantconcept.plantconcept_id=taxoninterpretation.plantconcept_id
  group by observation_ID ) as foo);
  
UPDATE commconcept set d_obsCount = (select count(1) FROM (select observation_ID from
  comminterpretation, view_notemb_commclass as commclass
  WHERE commclass.commclass_ID=comminterpretation.commclass_id AND 
  commconcept.commconcept_ID=comminterpretation.commconcept_id
  GROUP BY  observation_ID) as foo);
  
update project set d_obsCount=(select count(1) from view_notemb_observation as observation 
      where observation.project_ID=project.project_ID);

UPDATE project set d_lastPlotAddedDate = (select max(dateentered) from view_notemb_observation as observation 
      where observation.project_ID=project.project_ID and observation.dateentered is not null);

update party set d_obsCount=(select countallcontrib 
    from view_browseparty_all_count where view_browseparty_all_count.party_ID=party.party_ID);

UPDATE namedplace set d_obsCount=(select count(1) from place 
where namedplace.namedplacE_ID = place.namedplace_ID);

-- match range:
UPDATE taxonimportance set coverCode=(select coverIndex.coverCode 
from coverIndex,  observation, taxonobservation
      where ((lowerlimit<=taxonimportance.cover AND upperlimit>=taxonimportance.cover ) 
      AND (coverIndex.coverMethod_ID=observation.coverMethod_ID and observation.observation_ID=taxonobservation.observation_ID 
      AND taxonimportance.taxonobservation_ID=taxonobservation.taxonobservation_ID)) limit 1);
-- match exact:
UPDATE taxonimportance set coverCode=(select coverIndex.coverCode 
from coverIndex,  observation, taxonobservation
      where ((coverPercent = taxonimportance.cover ) 
      AND (coverIndex.coverMethod_ID=observation.coverMethod_ID and observation.observation_ID=taxonobservation.observation_ID 
      AND taxonimportance.taxonobservation_ID=taxonobservation.taxonobservation_ID)) limit 1);

UPDATE taxonobservation set int_origPlantConcept_ID = 
(select min(plantconcept_ID) from taxoninterpretation 
     WHERE originalinterpretation=true AND taxonobservation.taxonobservation_id = taxoninterpretation.taxonobservation_id) 
   ;

UPDATE taxonobservation set int_origPlantScifull = 
(select sciname from temptbl_std_plantnames as newnames
     WHERE taxonobservation.int_origPlantConcept_ID = newnames.PlantConcept_ID) 
   ;

UPDATE taxonobservation set int_origPlantSciNameNoAuth = 
(select scinamenoauth from temptbl_std_plantnames as newnames
     WHERE taxonobservation.int_origPlantConcept_ID = newnames.PlantConcept_ID) 
   ;

UPDATE taxonobservation set int_origPlantCode = 
(select code from temptbl_std_plantnames as newnames
     WHERE taxonobservation.int_origPlantConcept_ID = newnames.PlantConcept_ID) 
   ;

UPDATE taxonobservation set int_origPlantCommon = 
(select common from temptbl_std_plantnames as newnames
     WHERE taxonobservation.int_origPlantConcept_ID = newnames.PlantConcept_ID) 
   ;

UPDATE taxonobservation set int_currPlantConcept_ID = 
(select min(plantconcept_ID) from taxoninterpretation 
     WHERE originalinterpretation=true AND taxonobservation.taxonobservation_id = taxoninterpretation.taxonobservation_id) 
   ;

UPDATE taxonobservation set int_currPlantScifull = 
(select sciname from temptbl_std_plantnames as newnames
     WHERE taxonobservation.int_currPlantConcept_ID = newnames.PlantConcept_ID) 
   ;

UPDATE taxonobservation set int_currPlantSciNameNoAuth = 
(select scinamenoauth from temptbl_std_plantnames as newnames
     WHERE taxonobservation.int_currPlantConcept_ID = newnames.PlantConcept_ID) 
   ;

UPDATE taxonobservation set int_currPlantCode = 
(select code from temptbl_std_plantnames as newnames
     WHERE taxonobservation.int_currPlantConcept_ID = newnames.PlantConcept_ID) 
   ;

UPDATE taxonobservation set int_currPlantCommon = 
(select common from temptbl_std_plantnames as newnames
     WHERE taxonobservation.int_currPlantConcept_ID = newnames.PlantConcept_ID) 
   ;
   
   


--update denorm community fields on observation: 
UPDATE observation SET interp_orig_ci_ID = 
   (select comminterpretation_ID FROM comminterpretation, commclass 
    WHERE commclass.observation_ID = observation.observation_ID and 
    commclass.commclass_ID = comminterpretation.commclass_ID 
    ORDER BY classStartDate limit 1) 
  ;
  
  UPDATE observation SET interp_orig_cc_ID = (select commconcept_ID from 
  comminterpretation where comminterpretation_ID=interp_orig_ci_ID) 
    ;
  UPDATE observation SET interp_orig_sciname = (SELECT sciname from 
  temptbl_std_commnames where interp_orig_cc_ID=temptbl_std_commnames.commconcept_ID)
    ;  
  UPDATE observation SET interp_orig_code = (SELECT code from 
  temptbl_std_commnames where interp_orig_cc_ID=temptbl_std_commnames.commconcept_ID)
    ;  
  UPDATE observation SET interp_orig_party_ID = 
  (select party_ID FROM classcontributor, comminterpretation WHERE 
  comminterpretation.commclass_ID = classcontributor.commclass_ID 
  AND comminterpretation.comminterpretation_ID= observation.interp_orig_ci_ID   LIMIT 1)
    ;
  UPDATE observation SET interp_orig_partyName = (select party_id_transl 
  FROM view_party_transl where view_party_transl.party_ID =   
  observation.interp_orig_party_ID) 
    ;
  
UPDATE observation SET interp_current_ci_ID = 
   (select comminterpretation_ID FROM comminterpretation, commclass 
    WHERE commclass.observation_ID = observation.observation_ID and 
    commclass.commclass_ID = comminterpretation.commclass_ID 
    ORDER BY classStartDate DESC limit 1) 
  ;  

-- now get any where the current is different from original:
  UPDATE observation SET interp_current_cc_ID = (select commconcept_ID from 
  comminterpretation where comminterpretation_ID=interp_current_ci_ID) 
    ;
  UPDATE observation SET interp_current_sciname = (SELECT sciname from 
  temptbl_std_commnames where interp_current_cc_ID=temptbl_std_commnames.commconcept_ID)
    ;  
  UPDATE observation SET interp_current_code = (SELECT code from 
  temptbl_std_commnames where interp_current_cc_ID=temptbl_std_commnames.commconcept_ID)
    ;  
  UPDATE observation SET interp_current_party_ID = (select party_ID FROM 
  classcontributor, comminterpretation WHERE 
  comminterpretation.commclass_ID = classcontributor.commclass_ID 
  AND comminterpretation.comminterpretation_ID= observation.interp_current_ci_ID   LIMIT 1)
    ;
  UPDATE observation SET interp_current_partyName = (select party_id_transl 
  FROM view_party_transl where view_party_transl.party_ID =   
  observation.interp_current_party_ID) 
    ;

  
UPDATE observation SET interp_bestfit_ci_ID = 
   (select comminterpretation_ID FROM view_comminterp_more 
   WHERE view_comminterp_more.observation_ID = observation.observation_ID
    ORDER BY classfitnum, classconfidencenum DESC limit 1) 
  ;
  

  UPDATE observation SET interp_bestfit_cc_ID = (select commconcept_ID 
  from comminterpretation where comminterpretation_ID=interp_bestfit_ci_ID) 
    ;
  UPDATE observation SET interp_bestfit_sciname = (SELECT sciname from 
  temptbl_std_commnames where interp_bestfit_cc_ID=temptbl_std_commnames.commconcept_ID)
    ;  
  UPDATE observation SET interp_bestfit_code = (SELECT code from 
  temptbl_std_commnames where interp_bestfit_cc_ID=temptbl_std_commnames.commconcept_ID)
    ;  
  UPDATE observation SET interp_bestfit_party_ID = (select party_ID FROM 
  classcontributor, comminterpretation WHERE 
  comminterpretation.commclass_ID = classcontributor.commclass_ID 
  AND comminterpretation.comminterpretation_ID= observation.interp_bestfit_ci_ID   LIMIT 1)
    ;
  UPDATE observation SET interp_bestfit_partyName = (select party_id_transl 
  FROM view_party_transl where view_party_transl.party_ID =   
  observation.interp_bestfit_party_ID) 
    ;
    
    
        --top n taxa
    UPDATE observation set topTaxon2Name = 	 
    (SELECT coalesce(int_currplantscinamenoauth,authorplantName )  FROM 
    view_taxonobs_withmaxcover AS tob WHERE   
    tob.observation_ID=observation.observation_ID order by maxcover 
    DESC limit 1 OFFSET 1) ; 
     	 
    UPDATE observation set topTaxon3Name = 	 
    (SELECT coalesce(int_currplantscinamenoauth,authorplantName )  FROM 
    view_taxonobs_withmaxcover AS tob WHERE  
    tob.observation_ID=observation.observation_ID order by maxcover DESC 
    limit 1 OFFSET 2) ;
     	 	 
    UPDATE observation set topTaxon4Name = 	 
    (SELECT coalesce(int_currplantscinamenoauth,authorplantName )  FROM 
    view_taxonobs_withmaxcover AS tob WHERE   
    tob.observation_ID=observation.observation_ID order by maxcover DESC 
    limit 1 OFFSET 3) ; 
     	 
    UPDATE observation set topTaxon5Name = 	 
    (SELECT coalesce(int_currplantscinamenoauth,authorplantName )  FROM 
    view_taxonobs_withmaxcover AS tob WHERE  
    tob.observation_ID=observation.observation_ID order by maxcover DESC 
    limit 1 OFFSET 4) ;
     	  
    UPDATE observation set topTaxon1Name = 	 
    (SELECT coalesce(int_currplantscinamenoauth,authorplantName )  FROM 
    view_taxonobs_withmaxcover AS tob WHERE  
    tob.observation_ID=observation.observation_ID order by maxcover DESC 
    limit 1 OFFSET 0) ;
