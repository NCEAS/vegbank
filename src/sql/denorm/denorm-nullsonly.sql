
update commUsage SET commName = (select commname.commname from commname where commname.commname_ID=commUsage.commName_ID) where commName is null;

update commUsage SET COMMCONCEPT_ID = (select commstatus.commConcept_ID from commStatus where commStatus.commStatus_ID=commUsage.commStatus_ID) where commStatus_ID is not null and commConcept_ID is null;

update plantConcept SET plantName = (select plantname.plantname from plantname where plantname.plantname_ID=plantConcept.plantName_ID) where plantName is null;

update plantUsage SET plantCONCEPT_ID = (select plantstatus.plantConcept_ID from plantStatus where plantStatus.plantStatus_ID=plantUsage.plantStatus_ID) where plantStatus_ID is not null and plantConcept_ID is null;

update plantUsage SET plantName = (select plantname.plantname from plantname where plantname.plantname_ID=plantUsage.plantName_ID) where plantName is null;

update commInterpretation SET commName = (select commname.commname from commname, commconcept where commname.commname_ID=commConcept.commName_ID and commConcept.commConcept_ID=commInterpretation.commconcept_ID) where commName is null;

  UPDATE plot set country = 
    (SELECT 
       (
       SELECT np2.placeName FROM namedPlace as np2 WHERE np2.namedPlace_ID = min(np1.namedPlace_ID)
       )
    FROM namedPlace as np1 , place WHERE  np1.NAMEDPLACE_ID = place.NAMEDPLACE_ID
    and np1.placeSystem='area|country|territory' and place.plot_ID = plot.plot_ID
    )
  WHERE country is null;

update stratum set stratumMethod_ID = (select stratumType.stratumMethod_ID from stratumType where stratumType.stratumType_ID = stratum.stratumType_ID) WHERE stratumMethod_ID is null and stratumType_ID is not null;

update stratum set stratumName = (select stratumType.stratumName from stratumType where stratumType.stratumType_ID = stratum.stratumType_ID) WHERE stratumName is null and stratumType_ID is not null;

update commUsage SET party_ID = (select commstatus.party_ID from commStatus where commStatus.commStatus_ID=commUsage.commStatus_ID) where commStatus_ID is not null and party_ID is null;

update plantUsage SET party_ID = (select plantstatus.party_ID from plantStatus where plantStatus.plantStatus_ID=plantUsage.plantStatus_ID) where plantStatus_ID is not null and party_ID is null;

     UPDATE plot set stateProvince = 
       (SELECT 
          (
          SELECT np2.placeName FROM namedPlace as np2 WHERE np2.namedPlace_ID = min(np1.namedPlace_ID)
          )
       FROM namedPlace as np1 , place WHERE  np1.NAMEDPLACE_ID = place.NAMEDPLACE_ID
       and np1.placeSystem='region|state|province' and place.plot_ID = plot.plot_ID
       )
     WHERE stateProvince is null;