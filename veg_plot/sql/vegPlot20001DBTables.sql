/*
 * vegPlotDB.sql -- Creates the veg plots database
 * on the oracle backend
 */

/*
 * DROP ALL THE TABLES
 */
drop table citationContributor;
drop table citation;
drop table graphic;
drop table communityType;
drop table address;
drop table telephone;
drop table onlineResource;
drop table email;
drop table plotContributor;
drop table projectContributor;
drop table vegplotparty;
drop table project;
drop table taxonObservation;
drop table STAND;
drop table STRATACOMPOSITION;
drop table NAMEDPLACE;
drop table PLOTOBSERVATION;
drop table STRATA;
drop table plot;
drop table interpretation;



CREATE TABLE project (
project_id INT NOT NULL PRIMARY KEY,
projectName varchar(20), 
description varchar(200)
);

/*
* Some attributes are missing from this table - in order to capture just the data fields 
* represented by the prototype TNC data
*/

CREATE TABLE plot (
plot_id INT NOT NULL PRIMARY KEY,
project_id INT NOT NULL,
stand_id INT,
authorPlotCode  VARCHAR(30),
parentPlot INT,
namedPlace INT,
plotType VARCHAR(15),
samplingMethod VARCHAR(22),
coverScale VARCHAR(100),
treePlotSize INT,
plotOriginLat NUMBER(22,6),
plotOriginLong NUMBER(22,6), 
horizPosAcc NUMBER(22,6),
plotShape VARCHAR(12),
plotSize VARCHAR(20),
plotSizeAcc NUMBER(3),
altValue NUMBER(8),
altPosAcc NUMBER(3),
slopeAspect VARCHAR(20),
slopeGradient VARCHAR(20),
slopePosition VARCHAR(20),
hydrologicRegime VARCHAR(32),
soilDrainage VARCHAR(32),
surfGeo VARCHAR(100),
currentCommunity VARCHAR(200),  
state VARCHAR(100),
xCoord VARCHAR(100),
yCoord VARCHAR(100),
coordType VARCHAR(100)
);


CREATE TABLE plotObservation (
obs_id INT NOT NULL PRIMARY KEY,
parentPlot INT NOT NULL,
previousObs INT,
authorObsCode VARCHAR(30),
obsStartDate DATE,
obsStopDate DATE,
obsDateAcc INT,
effortLevel VARCHAR(30),
hardCopyLocation VARCHAR(30),
soilType VARCHAR(30),
soilDepth VARCHAR(30),
soilDepthRange VARCHAR(30),
percentRockGravel VARCHAR(30),
percentSoil VARCHAR(30),
percentLitter VARCHAR(30),
percentWood VARCHAR(30),
percentWater VARCHAR(30),
percentSand VARCHAR(30),
percentClay VARCHAR(30),
percentOrganic  VARCHAR(30),
leafPhenology  VARCHAR(100),
leafType  VARCHAR(100),
physionomicClass  VARCHAR(120)
);



CREATE TABLE namedPlace (
namedPlace_id INT NOT NULL PRIMARY KEY,
placeName VARCHAR(100),
placeDesc VARCHAR(100),
gazateerRef VARCHAR(100)
);


/*
* Both the speciesTaxon and strataComposition tables from the ERD are lumped into this table
* they may be separated at a later time - probably when the ecologists figure out the 'dominance' issue
*/


CREATE TABLE strata (
strata_id INT NOT NULL PRIMARY KEY,
obs_id INT NOT NULL,
stratumType VARCHAR(32),
stratumCover VARCHAR(40),
stratumHeight VARCHAR(40)
);


drop table strataComposition;
drop SEQUENCE strataComposition_id_seq;


CREATE TABLE strataComposition (
strataComposition_id NUMBER(20),
taxonObservation_id NUMBER(20),
strata_id NUMBER(20),
cheatStratumType VARCHAR(32),
percentCover NUMBER(8),
--add  the sequence here
CONSTRAINT strataComposition_pk PRIMARY KEY (strataComposition_id)
);

CREATE SEQUENCE strataComposition_id_seq;
CREATE TRIGGER strataComp_before_insert
BEFORE INSERT ON strataComposition FOR EACH ROW
BEGIN
  SELECT strataComposition_id_seq.nextval
    INTO :new.strataComposition_id
    FROM dual;
END;
/




CREATE TABLE taxonObservation (
taxonObservation_id INT NOT NULL PRIMARY KEY,
obs_id INT NOT NULL,
authorNameId VARCHAR(300),
originalAuthority VARCHAR(32),
cumStrataCoverage NUMBER(8)
);

CREATE TABLE communityType (
communityType_id INT NOT NULL PRIMARY KEY,
obs_id INT NOT NULL,
classAssociation VARCHAR(32),
classificationQuality VARCHAR(32),
startDate DATE,
stopDate DATE
);

CREATE TABLE interpretation (
interpretation_id INT NOT NULL PRIMARY KEY,
party_id INT NOT NULL,
taxonObservation_id  INT NOT NULL,
circumscription  VARCHAR(32),
role  VARCHAR(32),
interpDate DATE,
notes  VARCHAR(32)
);


CREATE TABLE vegPlotParty (
party_id INT NOT NULL PRIMARY KEY,
salutation varchar(20),
givenName varchar(20),
surname varchar(20),
orginaizationName varchar(20),
positionName varchar(20),
hoursOfService INT,
contactInstructions varchar(20));


CREATE TABLE projectContributor (
projectContributor_id INT NOT NULL PRIMARY KEY,
project_id INT NOT NULL,
party_id INT NOT NULL,
roleCode VARCHAR(32),
FOREIGN KEY (project_id) REFERENCES project,
FOREIGN KEY (party_id) REFERENCES vegPlotParty
);


CREATE TABLE plotContributor (
plotContributor_id INT NOT NULL PRIMARY KEY,
plot_id INT NOT NULL,
party_id INT NOT NULL,
roleCode VARCHAR(32),
FOREIGN KEY (plot_id) REFERENCES plot,
FOREIGN KEY (party_id) REFERENCES vegPlotParty
);


CREATE TABLE telephone (
telephone_id INT NOT NULL PRIMARY KEY, 
party_id INT NOT NULL,
voicePhone  VARCHAR(10),
voicePhoneExt VARCHAR(7),
facsimilePhone  VARCHAR(10),
otherPhone  VARCHAR(10),
otherPhoneType  VARCHAR(10),
FOREIGN KEY (party_id) REFERENCES vegPlotParty
);

CREATE TABLE email (
email_id INT NOT NULL PRIMARY KEY,
party_id INT NOT NULL,
emailAddress VARCHAR(32),
FOREIGN KEY (party_id) REFERENCES vegPlotParty
);

CREATE TABLE onlineResource (
onlineResource_id INT NOT NULL PRIMARY KEY,
party_id INT NOT NULL,
linkage VARCHAR(31),
protocol VARCHAR(31),
name VARCHAR(31),
applicationProfile VARCHAR(31),
description VARCHAR(31),
FOREIGN KEY (party_id) REFERENCES vegPlotParty
);

CREATE TABLE address (
address_id INT NOT NULL PRIMARY KEY,
party_id INT NOT NULL,
deliveryPoint VARCHAR(31),
city VARCHAR(31),
administrativeArea VARCHAR(31),
postalCode INT,
country VARCHAR(31),
currentFlag INT
);





CREATE TABLE graphic (
graphic_id INT NOT NULL PRIMARY KEY,
plot_id INT NOT NULL,
browsen VARCHAR(31),
browsed VARCHAR(31),
browset VARCHAR(31),
FOREIGN KEY (plot_id) REFERENCES plot
);



CREATE TABLE citation (
citation_id INT NOT NULL PRIMARY KEY,
plot_id INT NOT NULL,
title VARCHAR(31),
alternateTitle VARCHAR(31),
pubDate date,
edition VARCHAR(31),
editionDate date,
seriesName VARCHAR(31),
issueIdentification VARCHAR(31),
otherCitationDetails VARCHAR(31),
page VARCHAR(31),
ISBN VARCHAR(31),
ISSN VARCHAR(31),
FOREIGN KEY (plot_id) REFERENCES plot
);

CREATE TABLE citationContributor (
citationContributor_id INT NOT NULL PRIMARY KEY,
citation_id INT NOT NULL,
party_id INT NOT NULL,
roleCode VARCHAR(31),
FOREIGN KEY (party_id) REFERENCES vegPlotParty,
FOREIGN KEY (citation_id) REFERENCES citation
);
