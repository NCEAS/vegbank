/*
 * Creates the tables required to load the ecoart data into the 
 * community database
 */

 /*
 * first create tables that are identical to the ecoart data minus the foreign
 * key relationships
 */
 

CREATE TABLE CLASS (
ClassKey varchar(12),
ClassCode varchar(10),
ClassName varchar(100),
ClassDesc varchar(1200),
UpdateDate date,
System varchar(20)
);


CREATE TABLE SUBCLASS (
SubClassKey varchar(20),
SubClassCode varchar(20),
SubClassName varchar(100),
SubClassDesc varchar(1200),
UpdateDate date,
ClassKey varchar(12)
);

CREATE TABLE GROUP_ (
GroupKey varchar(20),
GroupCode varchar(20),
GroupName varchar(100),
GroupDesc varchar(1200),
UpdateDate date,
SubClassKey varchar(12)
);


CREATE TABLE SUBGROUP (
SubgroupKey varchar(20),
SubgroupCode varchar(20),
SubgroupName varchar(100),
SubgroupDesc varchar(1200),
UpdateDate date,
GroupKey varchar(12)
);

CREATE TABLE FORMATION (
FormationKey varchar(20),
FormationCode varchar(20),
FormationName varchar(400),
FormationDesc varchar(1200),
UpdateDate date,
SubGroupKey varchar(12),
Wetland varchar(12),
Leaderesp varchar(15),
Edition varchar(25),
Edauthor varchar(100),
AlliancesDef varchar(12)
);


CREATE TABLE ALLIANCE (
AllianceKey varchar(20),
AllianceStatus varchar(5),
Leaderesp varchar(15),
FormationCode varchar(12),
AllianceNum varchar(100),
FormationKey varchar(12),
AllianceName varchar(300),
AllianceNameTrans varchar(300),
AllianceDesc varchar(4000),
System varchar(25),
AssocDef varchar(15),
Edition varchar(25),
Edauthor varchar(100),
AllianceOriginDate date,
UpdateDate date
);



CREATE TABLE ETC (
Elcode varchar(330),
AccosStatus varchar(315),
ClassifResp varchar(335),
ClassifKey varchar(335),
FormationCode varchar(322),
AllianceNum varchar(3100),
Gname varchar(3150),
GnameTrans varchar(3200),
Gcomname varchar(3150),
AssocOriginDate date,
ClassifUsed varchar(315),
GconfLevel varchar(315),
Level_ varchar(325),
System varchar(325),
Nation varchar(315),
SubformationName varchar(365),
Acronym varchar(365),
Author varchar(355),
UsCrosswalked varchar(315),
Gcrosswalked varchar(315),
Grank varchar(230),
Grankdate date,
RoundedGrank varchar(315),
Grankform varchar(315),
Grankresp varchar(315),
CcagResp varchar(325),
Conspl varchar(315),
Consplresp varchar(35),
Stewab varchar(315),
Stewabresp varchar(35),
Jurisendem varchar(35),
Gslide varchar(315),
Gupdate date
);


