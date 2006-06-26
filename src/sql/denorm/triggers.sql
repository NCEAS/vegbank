drop trigger update_denorm_commname on commname;
drop trigger update_denorm_commname2 on commname;
drop trigger update_denorm_commstatus on commstatus;
drop trigger update_denorm_commstatus2 on commstatus;
drop trigger update_denorm_plantname on plantname;
drop trigger update_denorm_plantname2 on plantname;
drop trigger update_denorm_plantstatus on plantstatus;
drop trigger update_denorm_plantstatus2 on plantstatus;
drop trigger update_denorm_namedplace on namedplace;
drop trigger update_denorm_stratumtype on stratumtype;
drop trigger update_denorm_stratumtype2 on stratumtype;
drop trigger update_denorm_namedPlace2 on namedPlace;
drop trigger update_denorm_stratum on stratum;
drop trigger update_denorm_stratum2 on stratum;
drop trigger update_denorm_commName3 on commName;

drop function process_denorm_update();
drop table dba_trigger_audit;
drop language plpgsql;


CREATE TABLE dba_trigger_audit( 
    stamp             timestamp NOT NULL,
    stmt              char(1024)
);

create language plpgsql;

CREATE OR REPLACE FUNCTION process_denorm_update() 
RETURNS TRIGGER AS $process_denorm_update$
    DECLARE
      stmt varchar(1024);
    BEGIN
        
        --update commUsage SET commName = 
        --(select commname.commname from commname where 
        --commname.commname_ID=commUsage.commName_ID) where commName is null;
        --$1 = commusage
        --$2 = commname
        --$3 = commname.commname
        --$4 = commname.commname_id
        --$5 = commusage.commname_id
        --stmt := ' update  ' || TG_ARGV[0] || ' set ' || TG_ARGV[1] ||
        --     ' = (select ' || TG_ARGV[2] || ' from ' || TG_ARGV[3] ||
        --     ' where ' || TG_ARGV[4] ||' = ' || TG_ARGV[5] ||
        --     ') where ' || TG_ARGV[1] ||' is null;';
        --     
        INSERT INTO dba_trigger_audit SELECT now(), tg_argv[0];
        EXECUTE tg_argv[0];
        
        RETURN null; 
    END;
    
$process_denorm_update$ LANGUAGE plpgsql;

CREATE TRIGGER update_denorm_commname
AFTER INSERT OR UPDATE ON commname
    FOR EACH ROW EXECUTE PROCEDURE 
    process_denorm_update('update commUsage SET commName = 
      (select commname.commname from commname where 
      commname.commname_ID=commUsage.commName_ID) where commName is null;');
      
CREATE TRIGGER update_denorm_commstatus
AFTER INSERT OR UPDATE ON commstatus
    FOR EACH ROW EXECUTE PROCEDURE 
    process_denorm_update('update commUsage SET COMMCONCEPT_ID = (select commstatus.commConcept_ID from 
      commStatus where commStatus.commStatus_ID=commUsage.commStatus_ID) where 
      commStatus_ID is not null and commConcept_ID is null;');
      
CREATE TRIGGER update_denorm_plantname
AFTER INSERT OR UPDATE ON plantname
    FOR EACH ROW EXECUTE PROCEDURE 
    process_denorm_update('update plantConcept SET plantName = (select plantname.plantname from 
plantname where plantname.plantname_ID=plantConcept.plantName_ID) where 
plantName is null;');

CREATE TRIGGER update_denorm_plantstatus
AFTER INSERT OR UPDATE ON plantstatus
    FOR EACH ROW EXECUTE PROCEDURE 
    process_denorm_update('update plantUsage SET plantCONCEPT_ID = (select plantstatus.plantConcept_ID 
from plantStatus where plantStatus.plantStatus_ID=plantUsage.plantStatus_ID) 
where plantStatus_ID is not null and plantConcept_ID is null;');

CREATE TRIGGER update_denorm_plantname2
AFTER INSERT OR UPDATE ON plantname
    FOR EACH ROW EXECUTE PROCEDURE 
    process_denorm_update('update plantUsage SET plantName = (select plantname.plantname from plantname 
where plantname.plantname_ID=plantUsage.plantName_ID) where plantName is null;');
    
CREATE TRIGGER update_denorm_commname2
AFTER INSERT OR UPDATE ON commname
    FOR EACH ROW EXECUTE PROCEDURE 
    process_denorm_update('update commInterpretation SET commName = (select commname.commname from 
commname, commconcept where commname.commname_ID=commConcept.commName_ID and 
commConcept.commConcept_ID=commInterpretation.commconcept_ID) where commName 
is null;');
    
CREATE TRIGGER update_denorm_namedplace
AFTER INSERT OR UPDATE ON namedplace
    FOR EACH ROW EXECUTE PROCEDURE 
    process_denorm_update('UPDATE plot set country = (SELECT (SELECT 
    np2.placeName FROM namedPlace as np2 WHERE np2.namedPlace_ID = min(np1.namedPlace_ID))
    FROM namedPlace as np1 , place WHERE  np1.NAMEDPLACE_ID = place.NAMEDPLACE_ID
    and np1.placeSystem="area|country|territory" and place.plot_ID = plot.plot_ID)
    WHERE country is null;');
    
CREATE TRIGGER update_denorm_stratumtype
AFTER INSERT OR UPDATE ON stratumtype
    FOR EACH ROW EXECUTE PROCEDURE 
    process_denorm_update('update stratum set stratumMethod_ID = (select stratumType.stratumMethod_ID 
from stratumType where stratumType.stratumType_ID = stratum.stratumType_ID) 
WHERE stratumMethod_ID is null and stratumType_ID is not null;');
    
CREATE TRIGGER update_denorm_stratumtype2
AFTER INSERT OR UPDATE ON stratumtype
    FOR EACH ROW EXECUTE PROCEDURE 
    process_denorm_update('update stratum set stratumName = (select stratumType.stratumName from 
stratumType where stratumType.stratumType_ID = stratum.stratumType_ID) WHERE 
stratumName is null and stratumType_ID is not null;');

CREATE TRIGGER update_denorm_commStatus2
AFTER INSERT OR UPDATE ON commStatus
    FOR EACH ROW EXECUTE PROCEDURE 
    process_denorm_update('update commUsage SET party_ID = (select commstatus.party_ID from commStatus 
where commStatus.commStatus_ID=commUsage.commStatus_ID) where commStatus_ID 
is not null and party_ID is null;');
    
CREATE TRIGGER update_denorm_plantStatus2
AFTER INSERT OR UPDATE ON plantStatus
    FOR EACH ROW EXECUTE PROCEDURE 
    process_denorm_update('update plantUsage SET party_ID = (select plantstatus.party_ID from plantStatus 
where plantStatus.plantStatus_ID=plantUsage.plantStatus_ID) where plantStatus_ID 
is not null and party_ID is null;');
    
CREATE TRIGGER update_denorm_namedPlace2
AFTER INSERT OR UPDATE ON namedPlace
    FOR EACH ROW EXECUTE PROCEDURE 
    process_denorm_update('UPDATE plot set stateProvince = (SELECT (SELECT 
    np2.placeName FROM namedPlace as np2 WHERE np2.namedPlace_ID = min(np1.namedPlace_ID))
    FROM namedPlace as np1 , place WHERE  np1.NAMEDPLACE_ID = place.NAMEDPLACE_ID
    and np1.placeSystem="region|state|province" and place.plot_ID = plot.plot_ID)
    WHERE stateProvince is null;');

--this seems to put pgsql into an inf. loop or make it take a really long time anyway  
--CREATE TRIGGER update_denorm_stratum
--AFTER INSERT OR UPDATE ON stratum
--    FOR EACH ROW EXECUTE PROCEDURE 
--    process_denorm_update('update taxonImportance set stratumHeight=(select stratumHeight from stratum 
--where taxonImportance.stratum_ID=stratum.stratum_ID) where 
--taxonImportance.stratum_ID is not null and stratumHeight is null;');

--this seems to put pgsql into an inf. loop or make it take a really long time anyway
--CREATE TRIGGER update_denorm_stratum2
--AFTER INSERT OR UPDATE ON stratum
--    FOR EACH ROW EXECUTE PROCEDURE 
--    process_denorm_update('update taxonImportance set stratumBase=(select stratumBase from stratum 
--where taxonImportance.stratum_ID=stratum.stratum_ID) where 
--taxonImportance.stratum_ID is not null and stratumBase is null;');

CREATE TRIGGER update_denorm_commName3
AFTER INSERT OR UPDATE ON commName
    FOR EACH ROW EXECUTE PROCEDURE 
    process_denorm_update('update commConcept set commName=(select commName from commName where 
commName.commName_id=commConcept.commName_ID) where commName is null;');


