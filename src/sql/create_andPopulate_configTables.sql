
drop table dba_cookie;
create table dba_cookie ( cookie_ID serial , cookieName varchar(75) not null, defaultValue varchar(75) not null,
  viewname varchar(25) not null, description text, examplePK integer not null, sortorder integer, startGroup boolean, prefixHTML text, suffixHTML text, primary key(cookie_ID));

drop table dba_cookieLabels;
create table dba_cookieLabels (cookielabel_id serial, viewOrCookie varchar(50), description text, primary key (viewOrCookie));


insert into dba_cookie ( cookieName ,  defaultvalue,  viewname, examplePK, description, sortorder)  values ( 'table_stemsize','hide','observation_comprehensive',3062,'show table of stem sizes on this view',1) ;
insert into dba_cookie ( cookieName ,  defaultvalue,  viewname, examplePK, description, sortorder)  values ( 'table_stemsize','show','observation_taxa',3062,'show table of stem sizes on this view' ,3) ;
insert into dba_cookie ( cookieName ,  defaultvalue,  viewname, examplePK, description, sortorder)  values ( 'graphic_stemsize','show','observation_comprehensive',3062,'show graphic of stem sizes on this view' ,4) ;
insert into dba_cookie ( cookieName ,  defaultvalue,  viewname, examplePK, description, sortorder)  values ( 'graphic_stemsize','show','observation_taxa',3062,'show graphic of stem sizes on this view',6 ) ;

insert into dba_cookie ( cookieName ,  defaultvalue,  viewname, examplePK,  sortorder)  values ( 'table_stemsize','show','taxonobservation_detail',68777,8) ;
insert into dba_cookie ( cookieName ,  defaultvalue,  viewname, examplePK,  sortorder)  values ( 'graphic_stemsize','show','taxonobservation_detail',68777,9) ;

insert into dba_cookie ( cookieName ,  defaultvalue,  viewname, examplePK,  sortorder)  values ( 'taxonimportance_covercode','show','global',68777,10) ;

insert into dba_cookie ( cookieName ,  defaultvalue,  viewname, examplePK,  sortorder)  values ( 'taxonimportance_basalarea','show','global',68777,12) ;
insert into dba_cookie ( cookieName ,  defaultvalue,  viewname, examplePK,  sortorder)  values ( 'taxonimportance_biomass','show','global',68777,13) ;
insert into dba_cookie ( cookieName ,  defaultvalue,  viewname, examplePK,  sortorder)  values ( 'taxonimportance_inferencearea','show','global',68777,14) ;


insert into dba_cookie ( cookieName ,  defaultvalue,  viewname, examplePK,  sortorder,startGroup)  values ( 'plant_concept_name','hide','global',68777,14,true) ;
insert into dba_cookie ( cookieName ,  defaultvalue,  viewname, examplePK,  sortorder)  values ( 'plant_full_scientific_name','hide','global',68777,15) ;
insert into dba_cookie ( cookieName ,  defaultvalue,  viewname, examplePK,  sortorder)  values ( 'plant_scientific_name_noauthors','show','global',68777,16) ;
insert into dba_cookie ( cookieName ,  defaultvalue,  viewname, examplePK,  sortorder)  values ( 'plant_common_name','hide','global',68777,17) ;
insert into dba_cookie ( cookieName ,  defaultvalue,  viewname, examplePK,  sortorder)  values ( 'plant_code','hide','global',68777,18) ;

--help bits
insert into dba_cookie ( cookieName ,  defaultvalue,  viewname, examplePK, sortorder)  values ( 'ddlink','hide','global',0,0 ) ;

--userdefined
insert into dba_cookie ( cookieName ,  defaultvalue,  viewname, examplePK, sortorder,startGroup)  values ( 'user_defined_data','show','global',0,20,true ) ;

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


insert into dba_cookieLabels (viewOrCookie , description) values ('plant_concept_name','The plant name the concept is based on');
insert into dba_cookieLabels (viewOrCookie , description) values ('plant_full_scientific_name','The full scientific name, including authors, of the plant concept');
insert into dba_cookieLabels (viewOrCookie , description) values ('plant_scientific_name_noauthors','The scientific name without authors of the plant concept');
insert into dba_cookieLabels (viewOrCookie , description) values ('plant_common_name','The common name for the plant concept, if available');
insert into dba_cookieLabels (viewOrCookie , description) values ('plant_code','The Code for the plant concept, if available');

insert into dba_cookieLabels (viewOrCookie , description) values ('user_defined_data','Display User Defined Data for plots, if available');



drop table dba_confidentialityStatus;
CREATE TABLE dba_confidentialityStatus ( confidentialityStatus integer, confidentialityText varchar(100), confidentialityShortText varchar(100),primary key (confidentialityStatus));

insert into dba_confidentialityStatus (confidentialityStatus,confidentialityText,confidentialityShortText) values (0,'public','public');
insert into dba_confidentialityStatus (confidentialityStatus,confidentialityText,confidentialityShortText) values (1,'1 km radius (nearest 0.01 degree)','1 km');
insert into dba_confidentialityStatus (confidentialityStatus,confidentialityText,confidentialityShortText) values (2,'10 km radius (nearest 0.1 degree)','10 km');
insert into dba_confidentialityStatus (confidentialityStatus,confidentialityText,confidentialityShortText) values (3,'100 km radius (nearest degree)','100 km');
insert into dba_confidentialityStatus (confidentialityStatus,confidentialityText,confidentialityShortText) values (4,'NOT IMPLEMENTED: location embargo','location');
insert into dba_confidentialityStatus (confidentialityStatus,confidentialityText,confidentialityShortText) values (5,'NOT IMPLEMENTED: public embargo','public emb.');
insert into dba_confidentialityStatus (confidentialityStatus,confidentialityText,confidentialityShortText) values (6,'complete embargo','complete');