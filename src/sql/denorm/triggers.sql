drop trigger update_denorm on observation;
drop function process_denorm_update();
drop table trigger_audit;
drop language plpgsql;


CREATE TABLE trigger_audit( 
    operation         char(10)   NOT NULL,
    stamp             timestamp NOT NULL,
    userid            text      NOT NULL
);

create language plpgsql;

CREATE OR REPLACE FUNCTION process_denorm_update() RETURNS TRIGGER AS $process_denorm_update$
    BEGIN
        
        update commUsage SET commName = (select commname.commname from commname where 
        commname.commname_ID=commUsage.commName_ID) ;
        

        
        INSERT INTO trigger_audit SELECT 'DENORM', now(), user;
        RETURN NULL; 
    END;
    
$process_denorm_update$ LANGUAGE plpgsql;

CREATE TRIGGER update_denorm
AFTER INSERT OR UPDATE ON observation
    FOR EACH ROW EXECUTE PROCEDURE process_denorm_update();
