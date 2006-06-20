drop trigger update_denorm on observation;
drop function process_denorm_update();
drop table trigger_audit;
drop language plpgsql;


CREATE TABLE trigger_audit( 
    operation         char(1)   NOT NULL,
    stamp             timestamp NOT NULL,
    userid            text      NOT NULL,
    obs_id            integer   NOT NULL
);

create language plpgsql;

CREATE OR REPLACE FUNCTION process_denorm_update() RETURNS TRIGGER AS $process_denorm_update$
    BEGIN
        INSERT INTO trigger_audit SELECT 'U', now(), user, NEW.obervation_id;
        --insert into trigger_audit (operation, stamp, userid, obs_id)
        --  values ('U', now(), user, 1234);
        RETURN NULL; 
    END;
    
$process_denorm_update$ LANGUAGE plpgsql;

CREATE TRIGGER update_denorm
AFTER INSERT OR UPDATE ON observation
    FOR EACH ROW EXECUTE PROCEDURE process_denorm_update();
