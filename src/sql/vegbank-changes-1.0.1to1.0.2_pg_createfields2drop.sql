
BEGIN TRANSACTION;
-------dropping fields -------------------

ALTER TABLE plot ADD COLUMN layoutNarative  varchar(255);

-------dropping fields -------------------

ALTER TABLE stemCount ADD COLUMN TAXONOBSERVATION_ID  Integer;

ALTER TABLE taxonObservation ADD COLUMN taxonCollection  varchar(255);

ALTER TABLE taxonObservation ADD COLUMN taxonCover  varchar(255);

ALTER TABLE taxonObservation ADD COLUMN taxonBasalArea  varchar(255);

ALTER TABLE taxonObservation ADD COLUMN cheatplantname  varchar(255);

ALTER TABLE taxonObservation ADD COLUMN PLANTNAME_ID  Integer;

-------dropping tables----------------

CREATE TABLE stratumComposition
(
STRATUMCOMPOSITION_ID serial ,
TAXONOBSERVATION_ID Integer NOT NULL ,
STRATUM_ID Integer NOT NULL ,
taxonStratumCover Float NOT NULL ,
cheatstratumname varchar (200) ,
cheatplantname varchar (200) ,

PRIMARY KEY ( STRATUMCOMPOSITION_ID )
);

-------dropping fields -------------------

ALTER TABLE commStatus ADD COLUMN commPARTY_ID  Integer;

ALTER TABLE commUsage ADD COLUMN commPARTY_ID  Integer;

ALTER TABLE plantStatus ADD COLUMN plantPARTY_ID  Integer;

ALTER TABLE plantUsage ADD COLUMN plantPARTY_ID  Integer;

-------dropping tables----------------

CREATE TABLE plantParty
(
PLANTPARTY_ID serial ,
salutation varchar (20) ,
givenName varchar (50) ,
middleName varchar (50) ,
surName varchar (50) ,
organizationName varchar (100) ,
currentName_ID Integer ,
contactInstructions text ,
owner_ID Integer ,
email varchar (120) ,

PRIMARY KEY ( PLANTPARTY_ID )
);

CREATE TABLE commParty
(
COMMPARTY_ID serial ,
salutation varchar (20) ,
givenName varchar (50) ,
middleName varchar (50) ,
surName varchar (50) ,
organizationName varchar (100) ,
currentName_ID Integer ,
contactInstructions text ,
owner_ID Integer ,

PRIMARY KEY ( COMMPARTY_ID )
);


-------dropping fields -------------------

ALTER TABLE party ADD COLUMN owner_ID  Integer;

-------dropping fields -------------------

ALTER TABLE plot ADD COLUMN accession_number  varchar (255);

ALTER TABLE observation ADD COLUMN obsaccessionnumber  varchar (255);

-------dropping fields -------------------

ALTER TABLE projectContributor ADD COLUMN role  varchar (255);

ALTER TABLE plot ADD COLUMN state  varchar (255);

ALTER TABLE definedValue ADD COLUMN value  varchar (255);



END TRANSACTION;
