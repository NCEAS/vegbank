----------------------------------------------------------------------------
-- CREATE userRegionalExp
----------------------------------------------------------------------------
CREATE SEQUENCE userRegionalExp_ID_seq;
CREATE TABLE userRegionalExp
(
    userRegionalExp_ID integer  NOT NULL
          NOT NULL PRIMARY KEY default nextval('userRegionalExp_ID_seq'),
    userCertification_ID Integer NOT NULL,
    region varchar (50) NOT NULL,
    vegetation varchar (50),
    floristics varchar (50),
    nvc_ivc varchar (50)
                                                                                                                                                            
);

----------------------------------------------------------------------------
-- Drop unused tables
----------------------------------------------------------------------------
drop TABLE user_downloads;
drop TABLE user_submitted_queries ;
drop TABLE datafile;
drop TABLE file_accession;
drop TABLE graphic;
drop TABLE user_certification_old;


----------------------------------------------------------------------------
-- Link the user_info and the user_certification table first
----------------------------------------------------------------------------
UPDATE user_certification SET user_id = (SELECT user_id FROM user_info WHERE user_certification.email_address = user_info.email_address);

----------------------------------------------------------------------------
-- Alter Tables, involves name changes and column drops, add not needed?
----------------------------------------------------------------------------
alter TABLE user_certification rename to userCertification;
alter TABLE usercertification RENAME certification_id to usercertification_id;
alter TABLE usercertification RENAME user_id to usr_id;
ALTER TABLE usercertification DROP COLUMN email_address;
ALTER TABLE usercertification DROP COLUMN sur_name;
ALTER TABLE usercertification DROP COLUMN given_name;
ALTER TABLE usercertification DROP COLUMN phone_number;
ALTER TABLE usercertification DROP COLUMN phone_type;
ALTER TABLE usercertification DROP COLUMN cv_documentation;

alter TABLE user_info rename to usr;
alter TABLE usr RENAME user_id to usr_id;
alter TABLE usr DROP COLUMN initial_ip_address;
ALTER TABLE usr DROP COLUMN institution;
ALTER TABLE usr DROP COLUMN given_name;
ALTER TABLE usr DROP COLUMN sur_name;
ALTER TABLE usr DROP COLUMN address;
ALTER TABLE usr DROP COLUMN city;
ALTER TABLE usr DROP COLUMN phone_number;
ALTER TABLE usr DROP COLUMN phone_type;
ALTER TABLE usr DROP COLUMN state;
ALTER TABLE usr DROP COLUMN country;
ALTER TABLE usr DROP COLUMN zip_code;



----------------------------------------------------------------------------
-- Populate  userregionalexp
----------------------------------------------------------------------------
INSERT INTO userregionalexp ( usercertification_id, region, vegetation, floristics, nvc_ivc) SELECT usercertification_id, nvc_exp_region_a, nvc_exp_vegetation_a, nvc_exp_floristics_a, nvc_exp_usnvc_a from usercertification;

-- Drop these moved columns
ALTER TABLE usercertification DROP COLUMN nvc_exp_region_a;
ALTER TABLE usercertification DROP COLUMN nvc_exp_vegetation_a;
ALTER TABLE usercertification DROP COLUMN nvc_exp_floristics_a;
ALTER TABLE usercertification DROP COLUMN nvc_exp_usnvc_a;

----------------------------------------------------------------------------
-- Populate requested_cert_level to value of 3 
----------------------------------------------------------------------------
ALTER TABLE usercertification ADD COLUMN requested_cert_level Integer ;
UPDATE usercertification SET requested_cert_level=2; 


----------------------------------------------------------------------------
-- Populate PARTY_ID to value of 1 ( need to exist in db we populate and need
-- to be FIXED afterwards, i.e. link to correct party using email_address.
----------------------------------------------------------------------------
ALTER TABLE usr ADD COLUMN party_id Integer ;
UPDATE usr SET party_id=3; 
