insert into dba_cookie ( cookieName ,  defaultvalue,  viewname, examplePK, description, sortorder)  values ( 'table_stemsize','hide','observation_comprehensive',3062,'show table of stem sizes on this view',1 ) ;
insert into dba_cookie ( cookieName ,  defaultvalue,  viewname, examplePK, description, sortorder)  values ( 'table_stemsize','show','observation_taxa',3062,'show table of stem sizes on this view' ,3) ;
insert into dba_cookie ( cookieName ,  defaultvalue,  viewname, examplePK, description, sortorder)  values ( 'graphic_stemsize','show','observation_comprehensive',3062,'show graphic of stem sizes on this view' ,4) ;
insert into dba_cookie ( cookieName ,  defaultvalue,  viewname, examplePK, description, sortorder)  values ( 'graphic_stemsize','show','observation_taxa',3062,'show graphic of stem sizes on this view',6 ) ;

insert into dba_cookieLabels ( viewOrCookie , description) values ('graphic_stemsize','A graphical representation of tree stems, using DBH size');
insert into dba_cookieLabels ( viewOrCookie , description) values ('table_stemsize','A tabular overview of stems for each taxon');
insert into dba_cookieLabels ( viewOrCookie , description) values ('observation_comprehensive','The Comprehensive Plot View');
insert into dba_cookieLabels ( viewOrCookie , description) values ('observation_taxa','View of plot(s) showing plants only');