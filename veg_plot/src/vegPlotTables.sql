/*
 * vegPlotDB.sql -- Creates the veg plots database 
 */


CREATE TABLE project (
project_id INT NOT NULL PRIMARY KEY,
projectName varchar(20),  --with prototype data from TNC this is
purpose varchar(20),
samplingMethod varchar(20),
coverScale INT);

/*
* Some attributes are missing from this table - in order to capture just the data fields 
* represented by the prototype TNC data
*/

CREATE TABLE plotMaster (
plot_id INT NOT NULL PRIMARY KEY,
project_id INT NOT NULL,
authorPlotNum VARCHAR(30),
previousPlotRecord INT,
plotDate DATE,
dateAccuracy INT,
landOwner VARCHAR(10),
effortLevel VARCHAR(8),
standSize INT,
treePlotSize INT,
plotType VARCHAR(15),
plotOriginLat NUMBER(22,6),
plotOriginLong NUMBER(22,6), 
plotShape VARCHAR(12),
plotSize VARCHAR(20),
horizPosAcc NUMBER(3),
altValue NUMBER(8),
vertPosAcc NUMBER(3),
town VARCHAR(12),
county VARCHAR(12),
state VARCHAR(12),
country VARCHAR(12),
slopeAspect NUMBER(22,6),
slopeGradient NUMBER(22,6),
slopePosition VARCHAR(20),
hydrologicRegime VARCHAR(32),
soilDrainage VARCHAR(32),
surfGeo VARCHAR(100),
FOREIGN KEY (project_id) REFERENCES project
);



/*
* Both the speciesTaxon and strataComposition tables from the ERD are lumped into this table
* they may be separated at a later time - probably when the ecologists figure out the 'dominance' issue
*/


CREATE TABLE speciesTaxon (
taxon_id INT NOT NULL PRIMARY KEY,
plot_id INT NOT NULL,
originalTaxonName VARCHAR(120),
originalTaxonSymbol VARCHAR(10),
authority VARCHAR(32),
currentTaxonUnit VARCHAR(32),
cumStrataCoverage NUMBER(4,6),
stratumType VARCHAR (10),
percentCover NUMBER(8),
FOREIGN KEY (plot_id) REFERENCES plotMaster
);

CREATE TABLE party (
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
FOREIGN KEY (party_id) REFERENCES party
);


CREATE TABLE plotContributor (
plotContributor_id INT NOT NULL PRIMARY KEY,
plot_id INT NOT NULL,
party_id INT NOT NULL,
roleCode VARCHAR(32),
FOREIGN KEY (plot_id) REFERENCES plotMaster,
FOREIGN KEY (party_id) REFERENCES party
);


CREATE TABLE telephone (
party_id INT NOT NULL,
voicePhone  VARCHAR(10),
voicePhoneExt VARCHAR(7),
facsimilePhone  VARCHAR(10),
otherPhone  VARCHAR(10),
otherPhoneType  VARCHAR(10),
FOREIGN KEY (party_id) REFERENCES party
);

CREATE TABLE email (
party_id INT NOT NULL,
emailAddress VARCHAR(32),
FOREIGN KEY (party_id) REFERENCES party
);

CREATE TABLE onlineResource (
party_id INT NOT NULL,
linkage VARCHAR(31),
protocol VARCHAR(31),
name VARCHAR(31),
applicationProfile VARCHAR(31),
description VARCHAR(31),
FOREIGN KEY (party_id) REFERENCES party
);


/*
* this view was created to consolidate the 'key' fields of the database into a single manageable view
*/


CREATE VIEW salientPlotMaster 
AS SELECT plot_id, 
project_id, 
authorPlotNum, 
plotOriginLat, 
plotOriginLong,
altValue,
slopeGradient,
slopePosition,
hydrologicRegime,
surfGeo
FROM plotMaster;

/* Section below is artificial data intended to test the integrity of the 
 * database.  In furture versions this will be broken-out into its own
 * file 
*/

INSERT into project (project_id, projectName) VALUES (001, 'testProjectOne');
INSERT into project (project_id, projectName) VALUES (002, 'testProjectTwo');
INSERT into project (project_id, projectName) VALUES (003, 'tncTestData');

INSERT into plotMaster (plot_id, project_id, authorPlotNum, plotDate) VALUES (001, 001, 999.25, '12-Feb-00');
INSERT into plotMaster (plot_id, project_id, authorPlotNum) VALUES (002, 001, 999.25);
INSERT into plotMaster (plot_id, project_id, authorPlotNum) VALUES (003, 001, 999.25);
INSERT into plotMaster (plot_id, project_id, authorPlotNum) VALUES (004, 001, 999.25);
INSERT into plotMaster (plot_id, project_id, authorPlotNum) VALUES (005, 001, 999.25);

INSERT into plotMaster (plot_id, project_id, authorPlotNum) VALUES (006, 002, 999.25);
INSERT into plotMaster (plot_id, project_id, authorPlotNum) VALUES (007, 002, 999.25);
INSERT into plotMaster (plot_id, project_id, authorPlotNum) VALUES (008, 002, 999.25);
INSERT into plotMaster (plot_id, project_id, authorPlotNum) VALUES (009, 002, 999.25);
INSERT into plotMaster (plot_id, project_id, authorPlotNum) VALUES (010, 002, 999.25);
