/*
 * vegPlotDB.sql -- Creates the veg plots database 
 */




CREATE TABLE party (
party_id INT NOT NULL PRIMARY KEY,
org_name varchar(70)
);


CREATE TABLE circumscription (
circum_id INT NOT NULL PRIMARY KEY,
ref_id INT
);

CREATE TABLE reference (
ref_id INT NOT NULL PRIMARY KEY,
author varchar(70),  
date_entered date,
citation varchar(300),
page varchar(12),
speciesName varchar (100)
);


CREATE TABLE name (
name_id INT NOT NULL PRIMARY KEY,
symbol varchar(70),
Name varchar(200),
ref_id INT,
date_entered date,
commonName varchar (100),
family varchar(100),
authorName varchar(100),
FOREIGN KEY (ref_id) REFERENCES reference
);


CREATE TABLE status (
party_circum_id INT NOT NULL PRIMARY KEY,
circum_id INT,
party_id INT,
status varchar(4),
startdate date,
stopdate date,
FOREIGN KEY (circum_id) REFERENCES circumscription,
FOREIGN KEY (party_id) REFERENCES party
);


CREATE TABLE name_usage (
nameUsageId INT NOT NULL PRIMARY KEY,
circum_id INT,
name_id INT,
party_id INT,
startdate date,
stopdate date,
FOREIGN KEY (party_id) REFERENCES party,
FOREIGN KEY (circum_id) REFERENCES circumscription,
FOREIGN KEY (name_id) REFERENCES name
);



CREATE TABLE correlation (
correlation_id INT NOT NULL PRIMARY KEY,
party_circum1 INT,
party_circum2 INT,
convergence varchar(20),
startDate date,
stopDate date,
FOREIGN KEY (party_circum1) REFERENCES status,
FOREIGN KEY (party_circum2) REFERENCES status
);




