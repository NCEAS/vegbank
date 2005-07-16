
drop table dba_cookie;
create table dba_cookie ( cookie_ID serial , cookieName varchar(75) not null, defaultValue varchar(75) not null,
  viewname varchar(25) not null, description text, examplePK integer not null, sortorder integer, primary key(cookie_ID));

drop table dba_cookieLabels;
create table dba_cookieLabels (cookielabel_id serial, viewOrCookie varchar(50), description text, primary key (viewOrCookie));