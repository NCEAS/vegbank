/*
 *	CREATE_veguser_tables.sql -- Creates the tables 
 * 	used to store information about the users	of the VegBank System 
 *	database.  Included in this sql script are the tables to store 
 *	the registered user info, info about certified users, and 
 *	information related to the practices of downloading apps and
 *	
 *
 * '$Author: harris $'
 * '$Date: 2002-06-13 13:02:24 $'
 * '$Revision: 1.3 $'
 *
 */

drop sequence  user_id_seq;
drop table user_info;
drop table user_submitted_queries;
drop sequence user_downloads_seq;
drop table user_downloads;

																								

CREATE SEQUENCE user_id_seq;

CREATE table user_info (
	user_id   int primary key  default nextval('user_id_seq'),
	password varchar(50) not null,
	permission_type varchar(50),
	begin_time date,
	last_connect date,
	ticket_count int,
	institution varchar(50),
	email_address varchar(50),
	initial_ip_address varchar(50),
	given_name  varchar(50),
	sur_name  varchar(50),
	remote_address  varchar(50)
);

CREATE table user_submitted_queries (
	query_id		int,
	user_id			int,
	submission_time		date,
	ip_address		varchar(50),
	FOREIGN KEY (user_id) REFERENCES user_info
);

CREATE SEQUENCE user_downloads_seq;
CREATE table user_downloads (
download_id  int primary key  default nextval('user_downloads_seq'),
user_id     int,
email_address varchar(50),
download_type varchar(50),
last_download date,
download_count int,
FOREIGN KEY (user_id) REFERENCES user_info
);
					

CREATE SEQUENCE user_cert_seq;
CREATE table user_certification (
certification_id int primary key  default nextval('user_cert_seq'),
user_id     int,
email_address varchar(50),
sur_name varchar(50),
given_name varchar(50),
phone_number varchar(20),
phone_type varchar(12),
current_cert_level varchar(50),
cv_documentation varchar(500),
highest_degree varchar(12),
degree_year  varchar(12),
degree_institution  varchar(50),
current_institution  varchar(50),
current_position varchar(200),
esa_certified varchar(12),
prof_experience_doc  varchar(500),
revevant_pubs  varchar(500),
veg_sampling_doc  varchar(500),
veg_analysis_doc  varchar(500),
usnvc_experience_doc  varchar(500),
vegbank_experience_doc  varchar(500),
plotdb_experience_doc  varchar(500),
nvc_exp_region_a  varchar(12),
nvc_exp_vegetation_a varchar(12),
nvc_exp_floristics_a varchar(12),
nvc_exp_usnvc_a varchar(12),
esa_sponsor_name_a varchar(120),
esa_sponsor_email_a varchar(120),
peer_review varchar(10),
additional_statements varchar(500)
)



--INSERT INTO user_info (email_address, password, ticket_count)
--values ('user', 'pass', 1);				
