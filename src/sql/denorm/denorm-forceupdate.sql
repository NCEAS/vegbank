
update commUsage SET commName = (select commname.commname from commname where commname.commname_ID=commUsage.commName_ID) ;

update commUsage SET COMMCONCEPT_ID = (select commstatus.commConcept_ID from commStatus where commStatus.commStatus_ID=commUsage.commStatus_ID) where commStatus_ID is not null ;

update plantConcept SET plantName = (select plantname.plantname from plantname where plantname.plantname_ID=plantConcept.plantName_ID) ;

update plantUsage SET plantCONCEPT_ID = (select plantstatus.plantConcept_ID from plantStatus where plantStatus.plantStatus_ID=plantUsage.plantStatus_ID) where plantStatus_ID is not null ;

update plantUsage SET plantName = (select plantname.plantname from plantname where plantname.plantname_ID=plantUsage.plantName_ID) ;

update commInterpretation SET commName = (select commname.commname from commname, commconcept where commname.commname_ID=commConcept.commName_ID and commConcept.commConcept_ID=commInterpretation.commconcept_ID) ;

  UPDATE plot set country = 
    (SELECT 
       (
       SELECT np2.placeName FROM namedPlace as np2 WHERE np2.namedPlace_ID = min(np1.namedPlace_ID)
       )
    FROM namedPlace as np1 , place WHERE  np1.NAMEDPLACE_ID = place.NAMEDPLACE_ID
    and np1.placeSystem='area|country|territory' and place.plot_ID = plot.plot_ID
    )
  ;

update stratum set stratumMethod_ID = (select stratumType.stratumMethod_ID from stratumType where stratumType.stratumType_ID = stratum.stratumType_ID) WHERE  stratumType_ID is not null;

update stratum set stratumName = (select stratumType.stratumName from stratumType where stratumType.stratumType_ID = stratum.stratumType_ID) WHERE  stratumType_ID is not null;

update commUsage SET party_ID = (select commstatus.party_ID from commStatus where commStatus.commStatus_ID=commUsage.commStatus_ID) where commStatus_ID is not null ;

update plantUsage SET party_ID = (select plantstatus.party_ID from plantStatus where plantStatus.plantStatus_ID=plantUsage.plantStatus_ID) where plantStatus_ID is not null ;

     UPDATE plot set stateProvince = 
       (SELECT 
          (
          SELECT np2.placeName FROM namedPlace as np2 WHERE np2.namedPlace_ID = min(np1.namedPlace_ID)
          )
       FROM namedPlace as np1 , place WHERE  np1.NAMEDPLACE_ID = place.NAMEDPLACE_ID
       and np1.placeSystem='region|state|province' and place.plot_ID = plot.plot_ID
       )
     ;
     
     

-- UPDATE to observation.topTaxon#[Name|cover]
-- MTL : 11/11/04: updates only when topTaxon1Name is null, must be done in this order
-- rationale being that we don't want to ever update part of an observaiton, only the whole observation or nothing
-- this is why 2 3 4 5 1 is the order of updating

UPDATE observation set topTaxon2Name = 
(SELECT authorPlantName  FROM taxonObservation as tob,  taxonImportance as ti WHERE tob.taxonObservation_ID=ti.taxonObservation_ID and stratum_id  is null and cover is not null and authorPlantName is not null AND  tob.observation_ID=observation.observation_ID order by cover DESC limit 1 OFFSET 1) ;

UPDATE observation set topTaxon2Cover = 
(SELECT cover  FROM taxonObservation as tob,  taxonImportance as ti WHERE tob.taxonObservation_ID=ti.taxonObservation_ID and stratum_id  is null and cover is not null and authorPlantName is not null AND  tob.observation_ID=observation.observation_ID order by cover DESC limit 1 OFFSET 1) ;

--

UPDATE observation set topTaxon3Name = 
(SELECT authorPlantName  FROM taxonObservation as tob,  taxonImportance as ti WHERE tob.taxonObservation_ID=ti.taxonObservation_ID and stratum_id  is null and cover is not null and authorPlantName is not null AND  tob.observation_ID=observation.observation_ID order by cover DESC limit 1 OFFSET 2) ;

UPDATE observation set topTaxon3Cover = 
(SELECT cover  FROM taxonObservation as tob,  taxonImportance as ti WHERE tob.taxonObservation_ID=ti.taxonObservation_ID and stratum_id  is null and cover is not null and authorPlantName is not null AND  tob.observation_ID=observation.observation_ID order by cover DESC limit 1 OFFSET 2) ;

--

UPDATE observation set topTaxon4Name = 
(SELECT authorPlantName  FROM taxonObservation as tob,  taxonImportance as ti WHERE tob.taxonObservation_ID=ti.taxonObservation_ID and stratum_id  is null and cover is not null and authorPlantName is not null AND  tob.observation_ID=observation.observation_ID order by cover DESC limit 1 OFFSET 3) ;

UPDATE observation set topTaxon4Cover = 
(SELECT cover  FROM taxonObservation as tob,  taxonImportance as ti WHERE tob.taxonObservation_ID=ti.taxonObservation_ID and stratum_id  is null and cover is not null and authorPlantName is not null AND  tob.observation_ID=observation.observation_ID order by cover DESC limit 1 OFFSET 3) ;

--

UPDATE observation set topTaxon5Name = 
(SELECT authorPlantName  FROM taxonObservation as tob,  taxonImportance as ti WHERE tob.taxonObservation_ID=ti.taxonObservation_ID and stratum_id  is null and cover is not null and authorPlantName is not null AND  tob.observation_ID=observation.observation_ID order by cover DESC limit 1 OFFSET 4) ;

UPDATE observation set topTaxon5Cover = 
(SELECT cover  FROM taxonObservation as tob,  taxonImportance as ti WHERE tob.taxonObservation_ID=ti.taxonObservation_ID and stratum_id  is null and cover is not null and authorPlantName is not null AND  tob.observation_ID=observation.observation_ID order by cover DESC limit 1 OFFSET 4) ;




UPDATE observation set topTaxon1Cover = 
(SELECT cover  FROM taxonObservation as tob,  taxonImportance as ti WHERE tob.taxonObservation_ID=ti.taxonObservation_ID and stratum_id  is null and cover is not null and authorPlantName is not null AND  tob.observation_ID=observation.observation_ID order by cover DESC limit 1 OFFSET 0) ;

UPDATE observation set topTaxon1Name = 
(SELECT authorPlantName  FROM taxonObservation as tob,  taxonImportance as ti WHERE tob.taxonObservation_ID=ti.taxonObservation_ID and stratum_id  is null and cover is not null and authorPlantName is not null AND  tob.observation_ID=observation.observation_ID order by cover DESC limit 1 OFFSET 0) ;
