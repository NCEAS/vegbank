drop table plotSiteSummary
end --

CREATE TABLE plotSiteSummary (
PLOTSITESUMMARY_ID DECIMAL(20),
PLOT_ID INT NOT NULL,
PROJECT_ID DECIMAL(30),
PLOTTYPE VARCHAR2(30),
SAMPLINGMETHOD VARCHAR2(45),
COVERSCALE VARCHAR2(30),
PLOTORIGINLAT DECIMAL(30),
PLOTORIGINLONG DECIMAL(30),
PLOTSHAPE VARCHAR2(30),
PLOTAREA VARCHAR2(30),
ALTVALUE DECIMAL(30),
SLOPEASPECT VARCHAR2(30),
SLOPEGRADIENT VARCHAR2(100),
SLOPEPOSITION VARCHAR2(100),
HYDROLOGICREGIME VARCHAR2(100),
SOILDRAINAGE VARCHAR2(100),
CURRENTCOMMUNITY VARCHAR2(140),
XCOORD VARCHAR2(100),
YCOORD VARCHAR2(100),
COORDTYPE VARCHAR2(100),
OBSSTARTDATE VARCHAR2(100),
OBSSTOPDATE VARCHAR2(100),
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
PARENTPLOT DECIMAL(12),
AUTHOROBSCODE VARCHAR2(100)
)
end --


drop table plotSpeciesSum
end --
CREATE TABLE plotSpeciesSum (
PLOTSPECIESSUM_ID DECIMAL(20),
PLOT_ID DECIMAL(30),
OBS_ID INT NOT NULL,
PARENTPLOT INT,
AUTHORNAMEID VARCHAR2(300),
AUTHORPLOTCODE VARCHAR2(100),
AUTHOROBSCODE VARCHAR2(100),
TAXONOBSERVATION_ID DECIMAL(20),
STRATUMTYPE VARCHAR2(32),
PERCENTCOVER DECIMAL(8,8)
)
end --




