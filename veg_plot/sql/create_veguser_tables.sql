/*
 *	CREATE_veguser_tables.sql -- Creates the tables 
 * 	used to store information about the users	of the VegBank System 
 *	database.  Included in this sql script are the tables to store 
 *	the registered user info, info about certified users, and 
 *	information related to the practices of downloading apps and
 *	
 *
 * '$Author: farrell $'
 * '$Date: 2003-07-23 18:05:06 $'
 * '$Revision: 1.9 $'
 *
 */

drop sequence  user_id_seq;
drop table user_info;
drop table user_submitted_queries;
drop sequence user_downloads_seq;
drop table user_downloads;
DROP sequence  accession_id_seq;
DROP table file_accession;
DROP table datafile;
 
																							

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
	remote_address  varchar(50),
	address text,
	city varchar(50),
	phone_number varchar(20),
	phone_type varchar(12),
	state varchar(20),
	country varchar(50),
	zip_code varchar(20)
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
phone_type varchar(50),
current_cert_level varchar(50),
cv_documentation text,
highest_degree varchar(50),
degree_year  varchar(50),
degree_institution  varchar(50),
current_institution  varchar(50),
current_position varchar(200),
esa_certified varchar(50),
prof_experience_doc  text,
relevant_pubs  text,
veg_sampling_doc  text,
veg_analysis_doc  text,
usnvc_experience_doc  text,
vegbank_experience_doc  text,
plotdb_experience_doc  text,
nvc_exp_region_a  varchar(50),
nvc_exp_vegetation_a varchar(50),
nvc_exp_floristics_a varchar(50),
nvc_exp_usnvc_a varchar(50),
esa_sponsor_name_a varchar(120),
esa_sponsor_email_a varchar(120),
peer_review varchar(10),
additional_statements text
);

-- This supports the file upload function
CREATE SEQUENCE accession_id_seq;
  
CREATE table file_accession (
  accession_id int primary key  default nextval('accession_id_seq'),
  create_date date not null
);
             
CREATE table datafile (
  accession_id int,
  file_type varchar(50),
  file_size int,
  user_filename varchar(50) not null,
  path varchar(50) not null,
  FOREIGN KEY (accession_id) REFERENCES
  file_accession
);
	

--CREATE A TEST USER WITH A HIGH PERMISSION LEVEL 
INSERT INTO user_info (sur_name, given_name, email_address, password, ticket_count, permission_type)
values ('testlastname', 'testfirstname', 'test@test.com', 'pass', 1, 2);				
