/*
 * create_veguser_tables.sql -- Creates the tables 
 *  	used to store information about the users 
 *		of the vegetation databases
 */

drop sequence  user_id_seq;
drop table user_info;
drop table user_submitted_queries;

																								

CREATE SEQUENCE user_id_seq;

create table user_info (
	user_id   int primary key  default nextval('user_id_seq'),
	password varchar(50) not null,
	permission_type varchar(50),
	begin_time date,
	ticket_count int,
	institution varchar(50),
	email_address varchar(50),
	initial_ip_address varchar(50),
	given_name  varchar(50),
	sur_name  varchar(50),
	remote_address  varchar(50)
);

create table user_submitted_queries (
	query_id		int,
	user_id			int,
	submission_time		date,
	ip_address		varchar(50),
	FOREIGN KEY (user_id) REFERENCES user_info
);
		

INSERT INTO user_info (email_address, password, ticket_count)
values ('user', 'pass', 1);				
