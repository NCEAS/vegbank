
drop table dba_cookie;
create table dba_cookie ( cookie_ID serial , cookieName varchar(75) not null, defaultValue varchar(75) not null,
  viewname varchar(25) not null, description text, examplePK integer not null, sortorder integer, primary key(cookie_ID));

drop table dba_cookieLabels;
create table dba_cookieLabels (cookielabel_id serial, viewOrCookie varchar(50), description text, primary key (viewOrCookie));


insert into dba_cookie ( cookieName ,  defaultvalue,  viewname, examplePK, description, sortorder)  values ( 'table_stemsize','hide','observation_comprehensive',3062,'show table of stem sizes on this view',1 ) ;
insert into dba_cookie ( cookieName ,  defaultvalue,  viewname, examplePK, description, sortorder)  values ( 'table_stemsize','show','observation_taxa',3062,'show table of stem sizes on this view' ,3) ;
insert into dba_cookie ( cookieName ,  defaultvalue,  viewname, examplePK, description, sortorder)  values ( 'graphic_stemsize','show','observation_comprehensive',3062,'show graphic of stem sizes on this view' ,4) ;
insert into dba_cookie ( cookieName ,  defaultvalue,  viewname, examplePK, description, sortorder)  values ( 'graphic_stemsize','show','observation_taxa',3062,'show graphic of stem sizes on this view',6 ) ;

insert into dba_cookie ( cookieName ,  defaultvalue,  viewname, examplePK,  sortorder)  values ( 'table_stemsize','show','taxonobservation_detail',68777,8) ;
insert into dba_cookie ( cookieName ,  defaultvalue,  viewname, examplePK,  sortorder)  values ( 'graphic_stemsize','show','taxonobservation_detail',68777,9) ;

insert into dba_cookie ( cookieName ,  defaultvalue,  viewname, examplePK,  sortorder)  values ( 'taxonimportance_covercode','show','global',68777,10) ;

insert into dba_cookie ( cookieName ,  defaultvalue,  viewname, examplePK,  sortorder)  values ( 'taxonimportance_basalarea','show','global',68777,12) ;
insert into dba_cookie ( cookieName ,  defaultvalue,  viewname, examplePK,  sortorder)  values ( 'taxonimportance_biomass','show','global',68777,13) ;
insert into dba_cookie ( cookieName ,  defaultvalue,  viewname, examplePK,  sortorder)  values ( 'taxonimportance_inferencearea','show','global',68777,14) ;



--help bits
insert into dba_cookie ( cookieName ,  defaultvalue,  viewname, examplePK, sortorder)  values ( 'ddlink','hide','global',0,0 ) ;

insert into dba_cookieLabels ( viewOrCookie , description) values ('graphic_stemsize','A graphical representation of tree stems, using DBH size');
insert into dba_cookieLabels ( viewOrCookie , description) values ('table_stemsize','A tabular overview of stems for each taxon');
insert into dba_cookieLabels ( viewOrCookie , description) values ('observation_comprehensive','The Comprehensive Plot View');
insert into dba_cookieLabels ( viewOrCookie , description) values ('observation_taxa','View of plot(s) showing plants only');

insert into dba_cookieLabels ( viewOrCookie , description) values ('global','Global Settings');
insert into dba_cookieLabels ( viewOrCookie , description) values ('ddlink','Links to Field Definitions with Red Question Marks');
insert into dba_cookieLabels ( viewOrCookie , description) values ('taxonobservation_detail','View of a one species on a plot, with importance values and interpretations.');

insert into dba_cookieLabels ( viewOrCookie , description) values ('taxonimportance_covercode','Cover Code used for a Cover Class to estimate cover');
insert into dba_cookieLabels ( viewOrCookie , description) values ('taxonimportance_basalarea','Basal Area for a species');
insert into dba_cookieLabels ( viewOrCookie , description) values ('taxonimportance_biomass','Biomass for a species');
insert into dba_cookieLabels ( viewOrCookie , description) values ('taxonimportance_inferencearea','Inference Area used to determine importance values (cover, biomass, basal area, etc.) for a species.');