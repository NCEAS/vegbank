
update commUsage SET commName = 
(select commname.commname from commname where 
commname.commname_ID=commUsage.commName_ID) where commName is null;

update commUsage SET COMMCONCEPT_ID = (select commstatus.commConcept_ID from 
commStatus where commStatus.commStatus_ID=commUsage.commStatus_ID) where 
commStatus_ID is not null and commConcept_ID is null;

update plantConcept SET plantName = (select plantname.plantname from 
plantname where plantname.plantname_ID=plantConcept.plantName_ID) where 
plantName is null;

update plantUsage SET plantCONCEPT_ID = (select plantstatus.plantConcept_ID 
from plantStatus where plantStatus.plantStatus_ID=plantUsage.plantStatus_ID) 
where plantStatus_ID is not null and plantConcept_ID is null;

update plantUsage SET plantName = (select plantname.plantname from plantname 
where plantname.plantname_ID=plantUsage.plantName_ID) where plantName is null;

update commInterpretation SET commName = (select commname.commname from 
commname, commconcept where commname.commname_ID=commConcept.commName_ID and 
commConcept.commConcept_ID=commInterpretation.commconcept_ID) where commName 
is null;

  UPDATE plot set country = 
    (SELECT 
       (
       SELECT np2.placeName FROM namedPlace as np2 WHERE np2.namedPlace_ID = min(np1.namedPlace_ID)
       )
    FROM namedPlace as np1 , place WHERE  np1.NAMEDPLACE_ID = place.NAMEDPLACE_ID
    and np1.placeSystem='area|country|territory' and place.plot_ID = plot.plot_ID
    )
  WHERE country is null;





update stratum set stratumMethod_ID = (select stratumType.stratumMethod_ID 
from stratumType where stratumType.stratumType_ID = stratum.stratumType_ID) 
WHERE stratumMethod_ID is null and stratumType_ID is not null;

update stratum set stratumName = (select stratumType.stratumName from 
stratumType where stratumType.stratumType_ID = stratum.stratumType_ID) WHERE 
stratumName is null and stratumType_ID is not null;

update commUsage SET party_ID = (select commstatus.party_ID from commStatus 
where commStatus.commStatus_ID=commUsage.commStatus_ID) where commStatus_ID 
is not null and party_ID is null;

update plantUsage SET party_ID = (select plantstatus.party_ID from plantStatus 
where plantStatus.plantStatus_ID=plantUsage.plantStatus_ID) where plantStatus_ID 
is not null and party_ID is null;

     UPDATE plot set stateProvince = 
       (SELECT 
          (
          SELECT np2.placeName FROM namedPlace as np2 WHERE np2.namedPlace_ID = min(np1.namedPlace_ID)
          )
       FROM namedPlace as np1 , place WHERE  np1.NAMEDPLACE_ID = place.NAMEDPLACE_ID
       and np1.placeSystem='region|state|province' and place.plot_ID = plot.plot_ID
       )
     WHERE stateProvince is null;
     
     
update plot set stateprovince = 
   (SELECT   
     (SELECT np2.placeDescription FROM namedplace as np2 WHERE 
        np2.namedPlace_Id= min(np1.namedPlace_ID)    
     ) FROM namedPlace as np1 , place where np1.NAMEDPLACE_ID=place.NAMEDPLACE_ID 
     and np1.placeSystem='county' and place.plot_ID=plot.plot_ID      
   )    WHERE stateprovince is null;



     
update taxonImportance set stratumHeight=(select stratumHeight from stratum 
where taxonImportance.stratum_ID=stratum.stratum_ID) where 
taxonImportance.stratum_ID is not null and stratumHeight is null;

update taxonImportance set stratumBase=(select stratumBase from stratum 
where taxonImportance.stratum_ID=stratum.stratum_ID) where 
taxonImportance.stratum_ID is not null and stratumBase is null;

update commConcept set commName=(select commName from commName where 
commName.commName_id=commConcept.commName_ID) where commName is null;

-- update embargo denorm fields for full embargo

-- ORDER OF THESE STATEMENTS MATTERS! the latter read from the former.

--plot
update plot set emb_plot = (select currentEmb from 
view_emb_embargo_complete where plot.plot_id = view_emb_embargo_complete.plot_ID) 
where emb_plot is null;

--observation
update observation set emb_observation = (select emb_plot from plot where 
plot.plot_ID=observation.plot_id) where emb_observation is null;

--disturbanceObs
update disturbanceObs set emb_disturbanceObs = (select emb_observation from 
observation where observation.observation_ID=disturbanceObs.observation_ID) 
where emb_disturbanceObs is null;

--soilObs
update soilObs set emb_soilObs = (select emb_observation from observation 
where observation.observation_ID=soilObs.observation_ID)  where emb_soilObs 
is null;

--commClass
UPDATE commClass set emb_commClass = (select emb_observation from observation 
where observation.observation_ID=commClass.observation_ID)  where emb_commClass 
is null;

---classContributor
UPDATE classContributor SET emb_classContributor= (select emb_commClass from 
commClass where commClass.commClass_ID=classContributor.commClass_ID) where 
emb_classContributor is null;

---commInterpretation
UPDATE commInterpretation SET emb_commInterpretation= (select emb_commClass from 
commClass where commClass.commClass_ID=commInterpretation.commClass_ID) where 
emb_commInterpretation is null;

--taxonObservation

update taxonObservation set emb_taxonObservation = (select emb_observation from 
observation where observation.observation_ID=taxonObservation.observation_ID) 
where emb_taxonObservation is null;

---taxonImportance
update taxonImportance set emb_taxonImportance = (select emb_taxonObservation 
FROM taxonObservation   where taxonImportance.taxonObservation_id =
taxonObservation.taxonObservation_ID) where emb_taxonImportance is null;

----stemCount
update stemCount set emb_stemCount = (select emb_taxonImportance FROM 
taxonImportance   where stemCount.taxonImportance_id =taxonImportance.taxonImportance_ID) 
where emb_stemCount is null;

-----stemLocation
update stemLocation set emb_stemLocation = (select emb_stemCount FROM stemCount   
where stemLocation.stemCount_id =stemCount.stemCount_ID) where emb_stemLocation 
is null;

---taxonInterpretation
update taxonInterpretation set emb_taxonInterpretation = 
(select emb_taxonObservation FROM taxonObservation   where 
taxonInterpretation.taxonObservation_id =taxonObservation.taxonObservation_ID) 
where emb_taxonInterpretation is null;

----taxonAlt
update taxonAlt set emb_taxonAlt = (select emb_taxonInterpretation FROM 
taxonInterpretation   where taxonAlt.taxonInterpretation_id =
taxonInterpretation.taxonInterpretation_ID) where emb_taxonAlt is null;

--count of obs
UPDATE plantconcept set d_obsCount = (select count(1) from (select observation_ID from 
  taxoninterpretation, view_notemb_taxonobservation as taxonobservation 
  where taxonobservation.taxonobservation_ID=taxoninterpretation.taxonobservation_id 
  AND plantconcept.plantconcept_id=taxoninterpretation.plantconcept_id
  group by observation_ID ) as foo) where d_obsCount is null;
  
  
UPDATE commconcept set d_obsCount = (select count(1) FROM (select observation_ID from
  comminterpretation, view_notemb_commclass as commclass
  WHERE commclass.commclass_ID=comminterpretation.commclass_id AND 
  commconcept.commconcept_ID=comminterpretation.commconcept_id
  GROUP BY  observation_ID) as foo) where d_obsCount is null;
  
update project set d_obsCount=(select count(1) from view_notemb_observation as observation 
      where observation.project_ID=project.project_ID) ;

UPDATE project set d_lastPlotAddedDate = (select max(dateentered) from view_notemb_observation as observation 
      where observation.project_ID=project.project_ID and observation.dateentered is not null);
      

update party set d_obsCount=(select countallcontrib 
   from view_browseparty_all_count where 
   view_browseparty_all_count.party_ID=party.party_ID) where d_obsCount is null;

UPDATE namedplace set d_obsCount=(select count(1) from place where 
namedplace.namedplacE_ID = place.namedplace_ID)  where d_obsCount is null;

-- match exact:
UPDATE taxonimportance set coverCode=(select coverIndex.coverCode from 
      coverIndex,  observation, taxonobservation
      where ((coverPercent = taxonimportance.cover ) 
      AND (coverIndex.coverMethod_ID=observation.coverMethod_ID and 
      observation.observation_ID=taxonobservation.observation_ID 
      AND taxonimportance.taxonobservation_ID=taxonobservation.taxonobservation_ID)) 
      limit 1) WHERE taxonimportance.coverCode is null ;

--match range:
UPDATE taxonimportance set coverCode=(select min(coverIndex.coverCode) from 
      coverIndex,  observation, taxonobservation
      where ((lowerlimit<=taxonimportance.cover AND upperlimit>=taxonimportance.cover ) 
      AND (coverIndex.coverMethod_ID=observation.coverMethod_ID and 
      observation.observation_ID=taxonobservation.observation_ID 
      AND taxonimportance.taxonobservation_ID=taxonobservation.taxonobservation_ID)) 
      limit 1) WHERE taxonimportance.coverCode is null ;

UPDATE taxonobservation set int_origPlantConcept_ID = (select min(plantconcept_ID) 
from taxoninterpretation 
     WHERE originalinterpretation=true AND taxonobservation.taxonobservation_id = 
     taxoninterpretation.taxonobservation_id) 
   WHERE int_origPlantConcept_ID is null ;

UPDATE taxonobservation set int_origPlantScifull = (select sciname from 
temptbl_std_plantnames as newnames
     WHERE taxonobservation.int_origPlantConcept_ID = newnames.PlantConcept_ID) 
   WHERE int_origPlantScifull is null ;

UPDATE taxonobservation set int_origPlantSciNameNoAuth = (select scinamenoauth 
from temptbl_std_plantnames as newnames
   WHERE taxonobservation.int_origPlantConcept_ID = newnames.PlantConcept_ID) 
   WHERE int_origPlantSciNameNoAuth is null ;

UPDATE taxonobservation set int_origPlantCode = (select code from 
   temptbl_std_plantnames as newnames
   WHERE taxonobservation.int_origPlantConcept_ID = newnames.PlantConcept_ID) 
   WHERE int_origPlantCode is null ;

UPDATE taxonobservation set int_origPlantCommon = (select common from 
   temptbl_std_plantnames as newnames
     WHERE taxonobservation.int_origPlantConcept_ID = newnames.PlantConcept_ID) 
   WHERE int_origPlantCommon is null ;

UPDATE taxonobservation set int_currPlantConcept_ID = 
   (select min(plantconcept_ID) from taxoninterpretation 
     WHERE originalinterpretation=true AND taxonobservation.taxonobservation_id = 
     taxoninterpretation.taxonobservation_id) 
   WHERE int_currPlantConcept_ID is null ;

UPDATE taxonobservation set int_currPlantScifull = 
   (select sciname from temptbl_std_plantnames as newnames
     WHERE taxonobservation.int_currPlantConcept_ID = newnames.PlantConcept_ID) 
   WHERE int_currPlantScifull is null ;

UPDATE taxonobservation set int_currPlantSciNameNoAuth = (select scinamenoauth 
   from temptbl_std_plantnames as newnames
     WHERE taxonobservation.int_currPlantConcept_ID = newnames.PlantConcept_ID) 
   WHERE int_currPlantSciNameNoAuth is null ;

UPDATE taxonobservation set int_currPlantCode = (select code from 
   temptbl_std_plantnames as newnames
     WHERE taxonobservation.int_currPlantConcept_ID = newnames.PlantConcept_ID) 
   WHERE int_currPlantCode is null ;

UPDATE taxonobservation set int_currPlantCommon = (select common from 
   temptbl_std_plantnames as newnames
     WHERE taxonobservation.int_currPlantConcept_ID = newnames.PlantConcept_ID) 
   WHERE int_currPlantCommon is null ;


--update denorm community fields on observation: 
UPDATE observation SET interp_orig_ci_ID = 
   (select comminterpretation_ID FROM comminterpretation, commclass 
    WHERE commclass.observation_ID = observation.observation_ID and 
    commclass.commclass_ID = comminterpretation.commclass_ID 
    ORDER BY classStartDate limit 1) 
  WHERE interp_orig_ci_ID is null;
  
  UPDATE observation SET interp_orig_cc_ID = (select commconcept_ID from 
    comminterpretation where comminterpretation_ID=interp_orig_ci_ID) WHERE 
    interp_orig_cc_ID IS NULL;
    
  UPDATE observation SET interp_orig_sciname = 
    (SELECT sciname from temptbl_std_commnames where 
    interp_orig_cc_ID=temptbl_std_commnames.commconcept_ID)
    WHERE interp_orig_sciname is null;  
    
  UPDATE observation SET interp_orig_code = (SELECT code from 
  temptbl_std_commnames where interp_orig_cc_ID=temptbl_std_commnames.commconcept_ID)
    WHERE interp_orig_code is null;  
    
  UPDATE observation SET interp_orig_party_ID = (select party_ID FROM 
  classcontributor, comminterpretation WHERE comminterpretation.commclass_ID = 
  classcontributor.commclass_ID 
  AND comminterpretation.comminterpretation_ID= observation.interp_orig_ci_ID   
  LIMIT 1)
    WHERE interp_orig_party_ID IS NULL;
  UPDATE observation SET interp_orig_partyName = (select party_id_transl FROM 
  view_party_transl where view_party_transl.party_ID =   
  observation.interp_orig_party_ID) 
    where interp_orig_partyName is null;
  
UPDATE observation SET interp_current_ci_ID = 
   (select comminterpretation_ID FROM comminterpretation, commclass 
    WHERE commclass.observation_ID = observation.observation_ID and 
    commclass.commclass_ID = comminterpretation.commclass_ID 
    ORDER BY classStartDate DESC limit 1) 
  WHERE interp_current_ci_ID is null;  

--cheat and set fields equal to other fields if current interp is same as orig:
UPDATE observation set 
   interp_current_cc_ID =      interp_orig_cc_ID ,
   interp_current_sciname =    interp_orig_sciname ,
   interp_current_code =       interp_orig_code ,
   interp_current_party_ID =   interp_orig_party_ID ,
   interp_current_partyname =  interp_orig_partyname
   WHERE   interp_current_ci_ID = interp_orig_ci_ID AND interp_current_cc_ID IS NULL;
-- now get any where the current is different from original:
  UPDATE observation SET interp_current_cc_ID = (select commconcept_ID from 
    comminterpretation where comminterpretation_ID=interp_current_ci_ID) 
    WHERE interp_current_cc_ID IS NULL;
  UPDATE observation SET interp_current_sciname = (SELECT sciname from 
    temptbl_std_commnames where interp_current_cc_ID=temptbl_std_commnames.commconcept_ID)
    WHERE interp_current_sciname is null;  
  UPDATE observation SET interp_current_code = (SELECT code from 
    temptbl_std_commnames where interp_current_cc_ID=temptbl_std_commnames.commconcept_ID)
    WHERE interp_current_code is null;  
  UPDATE observation SET interp_current_party_ID = (select party_ID FROM 
    classcontributor, comminterpretation WHERE comminterpretation.commclass_ID = classcontributor.commclass_ID 
    AND comminterpretation.comminterpretation_ID= observation.interp_current_ci_ID   LIMIT 1)
    WHERE interp_current_party_ID IS NULL;
  UPDATE observation SET interp_current_partyName = (select party_id_transl 
    FROM view_party_transl where view_party_transl.party_ID =   
    observation.interp_current_party_ID) 
    where interp_current_partyName is null;

  
UPDATE observation SET interp_bestfit_ci_ID = 
   (select comminterpretation_ID FROM view_comminterp_more 
   WHERE view_comminterp_more.observation_ID = observation.observation_ID
    ORDER BY classfitnum, classconfidencenum DESC limit 1) 
  WHERE interp_bestfit_ci_ID is null;
  
--cheat again and fill in any bestfit that is same as orig or recent:
UPDATE observation set 
   interp_bestfit_cc_ID =      interp_orig_cc_ID ,
   interp_bestfit_sciname =    interp_orig_sciname ,
   interp_bestfit_code =       interp_orig_code ,
   interp_bestfit_party_ID =   interp_orig_party_ID ,
   interp_bestfit_partyname =  interp_orig_partyname
   WHERE   interp_bestfit_ci_ID = interp_orig_ci_ID AND interp_bestfit_cc_ID IS NULL;
   
--most recent:
UPDATE observation set 
   interp_bestfit_cc_ID =      interp_current_cc_ID ,
   interp_bestfit_sciname =    interp_current_sciname ,
   interp_bestfit_code =       interp_current_code ,
   interp_bestfit_party_ID =   interp_current_party_ID ,
   interp_bestfit_partyname =  interp_current_partyname
   WHERE   interp_bestfit_ci_ID = interp_current_ci_ID AND interp_bestfit_cc_ID IS NULL;
   
--get anything that is different   

  UPDATE observation SET interp_bestfit_cc_ID = (select commconcept_ID from 
    comminterpretation where comminterpretation_ID=interp_bestfit_ci_ID) 
    WHERE interp_bestfit_cc_ID IS NULL;
  UPDATE observation SET interp_bestfit_sciname = (SELECT sciname from 
    temptbl_std_commnames where interp_bestfit_cc_ID=temptbl_std_commnames.commconcept_ID)
    WHERE interp_bestfit_sciname is null;  
  UPDATE observation SET interp_bestfit_code = (SELECT code from 
    temptbl_std_commnames where interp_bestfit_cc_ID=temptbl_std_commnames.commconcept_ID)
    WHERE interp_bestfit_code is null;  
  UPDATE observation SET interp_bestfit_party_ID = (select party_ID FROM 
    classcontributor, comminterpretation WHERE comminterpretation.commclass_ID = 
    classcontributor.commclass_ID 
    AND comminterpretation.comminterpretation_ID= observation.interp_bestfit_ci_ID   LIMIT 1)
    WHERE interp_bestfit_party_ID IS NULL;
  UPDATE observation SET interp_bestfit_partyName = (select party_id_transl 
  FROM view_party_transl where view_party_transl.party_ID =   
  observation.interp_bestfit_party_ID) 
    where interp_bestfit_partyName is null;
    
    
    
    --top n taxa
UPDATE observation set topTaxon2Name = 	 
(SELECT coalesce(int_currplantscinamenoauth,authorplantName )  FROM 
view_taxonobs_withmaxcover AS tob WHERE   
tob.observation_ID=observation.observation_ID order by maxcover DESC
limit 1 OFFSET 1) where topTaxon1Name is null;	 
 	 
UPDATE observation set topTaxon3Name = 	 
(SELECT coalesce(int_currplantscinamenoauth,authorplantName )  FROM 
view_taxonobs_withmaxcover AS tob WHERE  
tob.observation_ID=observation.observation_ID order by maxcover DESC 
limit 1 OFFSET 2) where topTaxon1Name is null;	 
 	 	 
UPDATE observation set topTaxon4Name = 	 
(SELECT coalesce(int_currplantscinamenoauth,authorplantName )  FROM 
view_taxonobs_withmaxcover AS tob WHERE   
tob.observation_ID=observation.observation_ID order by maxcover DESC limit 1 
OFFSET 3) where topTaxon1Name is null;	 
 	 
UPDATE observation set topTaxon5Name = 	 
(SELECT coalesce(int_currplantscinamenoauth,authorplantName )  FROM 
view_taxonobs_withmaxcover AS tob WHERE  
tob.observation_ID=observation.observation_ID order by maxcover DESC 
limit 1 OFFSET 4) where topTaxon1Name is null;	 
 	  
UPDATE observation set topTaxon1Name = 	 
(SELECT coalesce(int_currplantscinamenoauth,authorplantName )  FROM 
view_taxonobs_withmaxcover AS tob WHERE  
tob.observation_ID=observation.observation_ID order by maxcover DESC 
limit 1 OFFSET 0) where topTaxon1Name is null;


UPDATE observation SET hasobservationsynonym = true WHERE observation_Id IN (SELECT synonymObservation_ID FROM observationSynonym) AND hasObservationSynonym IS NULL;
