DROP TABLE project
end -- dropped the project table
CREATE TABLE project (
project_id INT NOT NULL PRIMARY KEY,
projectName varchar(20), 
description varchar(200)
)
end -- created the project table
DROP TABLE plot
end -- dropped the plot table
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
plotOriginLat DECIMAL(22,6),
plotOriginLong DECIMAL(22,6), 
horizPosAcc DECIMAL(22,6),
plotShape VARCHAR(12),
plotSize VARCHAR(20),
plotSizeAcc DECIMAL(3),
altValue DECIMAL(8),
altPosAcc DECIMAL(3),
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
)
end --created the plot table

DROP TABLE plotObservation
end -- drop the plot observation
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
)
end -- created the plotObservation


DROP TABLE strata
end -- dropped the strata table

CREATE TABLE strata (
strata_id INT NOT NULL PRIMARY KEY,
obs_id INT NOT NULL,
stratumType VARCHAR(32),
stratumCover VARCHAR(40),
stratumHeight VARCHAR(40)
)
end -- created the strata table

DROP TABLE strataComposition
end -- dropped the strataComposition table

CREATE TABLE strataComposition (
strataComposition_id DECIMAL(20),
taxonObservation_id DECIMAL(20),
strata_id DECIMAL(20),
cheatStratumType VARCHAR(32),
percentCover DECIMAL(8,8),
)
end  -- created the strataComposition table

DROP TABLE taxonObservation
end -- dropped the taxonObservation table
CREATE TABLE taxonObservation (
taxonObservation_id INT NOT NULL PRIMARY KEY,
obs_id INT NOT NULL,
authorNameId VARCHAR(300),
originalAuthority VARCHAR(32),
cumStrataCoverage DECIMAL(8)
)
end - created the taxonObservation table

DROP TABLE communityType
end -- dropped the communityType table

CREATE TABLE communityType (
communityType_id INT NOT NULL PRIMARY KEY,
obs_id INT NOT NULL,
classAssociation VARCHAR(32),
classificationQuality VARCHAR(32),
startDate DATE,
stopDate DATE
)
end -- created the communityTable


DROP TABLE plotSiteSummary
end
CREATE TABLE plotSiteSummary (
PLOTSITESUMMARY_ID INT,
PLOT_ID INT,
PROJECT_ID INT,
PLOTTYPE VARCHAR2(30),
SAMPLINGMETHOD VARCHAR2(45),
COVERSCALE VARCHAR2(30),
PLOTORIGINLAT INT,
PLOTORIGINLONG INT,
PLOTSHAPE VARCHAR2(30),
PLOTAREA VARCHAR2(30),
ALTVALUE INT,
SLOPEASPECT VARCHAR2(30),
SLOPEGRADIENT VARCHAR2(100),
SLOPEPOSITION VARCHAR2(100),
HYDROLOGICREGIME VARCHAR2(100),
SOILDRAINAGE VARCHAR2(100),
CURRENTCOMMUNITY VARCHAR2(140),
XCOORD VARCHAR2(100),
YCOORD VARCHAR2(100),
COORDTYPE VARCHAR2(100),
OBSSTARTDATE DATE,
OBSSTOPDATE DATE,
EFFORTLEVEL VARCHAR2(100),
HARDCOPYLOCATION VARCHAR2(100),
SOILTYPE VARCHAR2(30),
SOILDEPTH VARCHAR2(100),
PERCENTROCKGRAVEL VARCHAR2(30),
PERCENTSOIL VARCHAR2(30),
PERCENTLITTER VARCHAR2(30),
PERCENTWOOD VARCHAR2(30),
PERCENTWATER VARCHAR2(30),
PERCENTSAND VARCHAR2(30),
PERCENTCLAY VARCHAR2(30),
PERCENTORGANIC VARCHAR2(120),
LEAFTYPE VARCHAR2(50),
PHYSIONOMICCLASS VARCHAR2(100),
AUTHORPLOTCODE VARCHAR2(100),
SURFGEO  VARCHAR2(70),
STATE VARCHAR2(50),
PARENTPLOT INT,
AUTHOROBSCODE VARCHAR2(100),
PRIMARY KEY (PLOTSITESUMMARY_ID)
)
