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

alter table usercertification rename column current_institution to current_org;
alter table usercertification rename column current_position to current_pos;
alter table usercertification rename column esa_certified to esa_member;
alter table usercertification rename column prof_experience_doc to prof_exp;
alter table usercertification rename column veg_sampling_doc to veg_sampling_exp;
alter table usercertification rename column veg_analysis_doc to veg_analysis_exp;
alter table usercertification rename column usnvc_experience_doc to usnvc_exp;
alter table usercertification rename column vegbank_experience_doc to vb_exp;
alter table usercertification rename column plotdb_experience_doc to tools_exp;
alter table usercertification rename column additional_statements to addl_stmt;

alter table usercertification add column vegbank_expected_uses text;

alter table usercertification add column exp_region_a varchar (32);
alter table usercertification add column exp_region_a_veg integer;
alter table usercertification add column exp_region_a_flor integer;
alter table usercertification add column exp_region_a_nvc integer;

alter table usercertification add column exp_region_b varchar (32);
alter table usercertification add column exp_region_b_veg integer;
alter table usercertification add column exp_region_b_flor integer;
alter table usercertification add column exp_region_b_nvc integer;

alter table usercertification add column exp_region_c varchar (32);
alter table usercertification add column exp_region_c_veg integer;
alter table usercertification add column exp_region_c_flor integer;
alter table usercertification add column exp_region_c_nvc integer; 


----------------------------------------------------------------------------
-- CREATE userregionalexp
----------------------------------------------------------------------------
CREATE SEQUENCE userregionalexp_id_seq;
CREATE TABLE userregionalexp
(
    userregionalexp_id integer  NOT NULL
          NOT NULL PRIMARY KEY default nextval('userregionalexp_id_seq'),
    usercertification_id Integer NOT NULL,
    region varchar (32) NOT NULL,
    vegetation integer,
    floristics integer,
    nvc_ivc integer
);

----------------------------------------------------------------------------
-- Populate  userregionalexp
----------------------------------------------------------------------------
-- Actually, we're limiting regional expertise to 3 entries for now.
-- See usercertification.exp_region_*
-- INSERT INTO userregionalexp ( usercertification_id, region, vegetation, floristics, nvc_ivc) SELECT usercertification_id, nvc_exp_region_a, nvc_exp_vegetation_a, nvc_exp_floristics_a, nvc_exp_usnvc_a from usercertification;

----------------------------------------------------------------------------
-- Convert old regional expertise values
-- Other conversions are possible but there are no records for them.
----------------------------------------------------------------------------
UPDATE usercertification SET exp_region_a='asia' WHERE nvc_exp_region_a='Asia';
UPDATE usercertification SET exp_region_a='us-ak_n-can' WHERE nvc_exp_region_a='US Alaska /';
UPDATE usercertification SET exp_region_a='uscan-midwest' WHERE nvc_exp_region_a='US/CAN - Mid';
UPDATE usercertification SET exp_region_a='uscan-ne' WHERE nvc_exp_region_a='US/CAN - NE';
UPDATE usercertification SET exp_region_a='uscan-nw' WHERE nvc_exp_region_a='US/CAN - NW';
UPDATE usercertification SET exp_region_a='uscan-rockies' WHERE nvc_exp_region_a='US/CAN - Rocky Mts';
UPDATE usercertification SET exp_region_a='us-se' WHERE nvc_exp_region_a='US - SE';

UPDATE usercertification SET exp_region_a='n/a' WHERE nvc_exp_region_a IS NULL;

-- Drop these obsolete columns
ALTER TABLE usercertification DROP COLUMN nvc_exp_region_a;
ALTER TABLE usercertification DROP COLUMN nvc_exp_vegetation_a;
ALTER TABLE usercertification DROP COLUMN nvc_exp_floristics_a;
ALTER TABLE usercertification DROP COLUMN nvc_exp_usnvc_a;

----------------------------------------------------------------------------
-- Populate requested_cert_level to value of 3 
----------------------------------------------------------------------------
ALTER TABLE usercertification ADD COLUMN requested_cert_level Integer ;
UPDATE usercertification SET requested_cert_level=3; 

----------------------------------------------------------------------------
-- Turn user_info into usr
----------------------------------------------------------------------------
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
-- Populate PARTY_ID to value of 1 ( need to exist in db we populate and need
-- to be FIXED afterwards, i.e. link to correct party using email_address.
----------------------------------------------------------------------------
ALTER TABLE usr ADD COLUMN party_id Integer ;
UPDATE usr SET party_id=17; 

----------------------------------------------------------------------------
-- Drop unused tables
----------------------------------------------------------------------------
DROP TABLE user_downloads;
DROP TABLE user_submitted_queries ;
DROP TABLE datafile;
DROP TABLE file_accession;
-- DROP TABLE graphic;
DROP TABLE user_certification_old;


