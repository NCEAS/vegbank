/*
 *
 * '$Author: harris $'
 * '$Date: 2002-06-11 23:32:33 $'
 * '$Revision: 1.2 $'
 *
 */

drop sequence datafile_DATAFILE_ID_seq;
drop table datafile;

CREATE table datafile
(
 	DATAFILE_ID serial,
	user_givenname varchar(200), 
	user_surname varchar(200),
	insert_date date,
	filesize numeric,
	path varchar(200),
	user_filename varchar(200),
	db_filename varchar(200),
	file_type varchar(200)
);

--the accession number table
drop sequence   file_accession_accession_id_seq;
drop table file_accession;

create table file_accession
(
 ACCESSION_ID serial,
 create_date date,
 create_time time
);

--insert a row to the accession number
insert into file_accession (create_date, create_time ) values( now()::date, now()::time );
