/*
 * vegPlotDB.sql -- Creates the veg plots database 
 */


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
coverScale NUMBER(22,6),
treePlotSize INT,
plotOriginLat NUMBER(22,6),
plotOriginLong NUMBER(22,6), 
horizPosAcc NUMBER(22,6),
plotShape VARCHAR(12),
plotSize VARCHAR(20),
plotSizeAcc NUMBER(3),
altValue NUMBER(8),
altPosAcc NUMBER(3),
slopeAspect NUMBER(22,6),
slopeGradient NUMBER(22,6),
slopePosition VARCHAR(20),
hydrologicRegime VARCHAR(32),
soilDrainage VARCHAR(32),
surfGeo VARCHAR(100),
currentCommunity VARCHAR(200)  
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
hardCopyLocation VARCHAR(30)
);


CREATE TABLE stand (
stand_id INT NOT NULL PRIMARY KEY, 
standSize VARCHAR(20),
description VARCHAR(100)
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
stratumCover NUMBER(8),
stratumHeight NUMBER(8)
);

CREATE TABLE strataComposition (
strataComposition_id INT NOT NULL PRIMARY KEY,
taxonObservation_id INT NOT NULL,
strata_id INT NOT NULL,
cheatStratumType VARCHAR(32),
percentCover NUMBER(8)
);


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
