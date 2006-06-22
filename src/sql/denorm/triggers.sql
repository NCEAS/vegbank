drop trigger update_denorm_commname on commname;
drop trigger update_denorm_commstatus on commstatus;
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
