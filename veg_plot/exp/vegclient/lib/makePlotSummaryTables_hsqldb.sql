--drop table plotSiteSummary;
--drop table plotSpeciesSum;
CREATE cached TABLE plotSiteSummary (
PLOTSITESUMMARY_ID INT IDENTITY,
PLOT_ID NUMERIC(30),
PROJECT_ID NUMERIC(30),
PLOTTYPE VARCHAR(30),
SAMPLINGMETHOD VARCHAR(45),
COVERSCALE VARCHAR(30),
PLOTORIGINLAT NUMERIC(30),
PLOTORIGINLONG NUMERIC(30),
PLOTSHAPE VARCHAR(30),
PLOTAREA VARCHAR(30),
ALTVALUE NUMERIC(30),
SLOPEASPECT VARCHAR(30),
SLOPEGRADIENT VARCHAR(100),
SLOPEPOSITION VARCHAR(100),
HYDROLOGICREGIME VARCHAR(100),
SOILDRAINAGE VARCHAR(100),
CURRENTCOMMUNITY VARCHAR(140),
XCOORD VARCHAR(100),
YCOORD VARCHAR(100),
COORDTYPE VARCHAR(100),
OBSSTARTDATE DATE,
OBSSTOPDATE DATE,
EFFORTLEVEL VARCHAR(100),
HARDCOPYLOCATION VARCHAR(100),
SOILTYPE VARCHAR(30),
SOILDEPTH VARCHAR(100),
PERCENTROCKGRAVEL VARCHAR(30),
PERCENTSOIL VARCHAR(30),
PERCENTLITTER VARCHAR(30),
PERCENTWOOD VARCHAR(30),
PERCENTWATER VARCHAR(30),
PERCENTSAND VARCHAR(30),
PERCENTCLAY VARCHAR(30),
PERCENTORGANIC VARCHAR(120),
LEAFTYPE VARCHAR(50),
PHYSIONOMICCLASS VARCHAR(100),
AUTHORPLOTCODE VARCHAR(100),
SURFGEO  VARCHAR(70),
STATE VARCHAR(50),
PARENTPLOT NUMERIC(12),
AUTHOROBSCODE VARCHAR(100)
);

insert into plotSiteSummary (plot_id, project_id, plotType, samplingmethod, coverscale,
plotoriginlat, plotoriginlong, plotshape, altvalue, slopeaspect,
slopegradient, slopeposition, hydrologicregime, soildrainage, currentcommunity,
xcoord, ycoord, coordtype, obsstartdate, obsstopdate, effortlevel, hardcopylocation, 
soiltype, authorplotcode, surfGeo, state, parentplot, authorobscode,
soilDepth, leaftype )
select plot.plot_id, plot.project_id, plot.plotType, plot.samplingMethod, plot.coverScale,
plot.plotoriginlat, plot.plotoriginlong, plot.plotshape,  plot.altvalue,
plot.slopeaspect, plot.slopegradient, plot.slopeposition, plot.hydrologicregime,
plot.soildrainage, plot.currentcommunity, plot.xcoord, plot.ycoord,
plot.coordtype, plotObservation.obsstartdate, plotObservation.obsstopdate,
plotObservation.effortLevel, plotObservation.hardcopylocation,
plotObservation.soiltype, plot.authorplotcode, plot.surfGeo, plot.state, 
plotObservation.parentplot, plotObservation.authorobscode, 
plotObservation.soilDepth, plotObservation.leaftype
from plot, plotObservation where plot_id > 0
and plotObservation.parentplot = plot.plot_id;



CREATE cached TABLE  plotSpeciesSum (
PLOTSPECIESSUM_ID INT IDENTITY,
PLOT_ID NUMERIC(30),
OBS_ID NUMERIC(30),
PARENTPLOT NUMERIC(38),
AUTHORNAMEID VARCHAR(300),
AUTHORPLOTCODE VARCHAR(100),
AUTHOROBSCODE VARCHAR(100),
TAXONOBSERVATION_ID NUMERIC(20),
STRATUMTYPE VARCHAR(32),
PERCENTCOVER NUMERIC(8)
);


insert into plotSpeciesSum (OBS_ID, AUTHORNAMEID, STRATUMTYPE, PERCENTCOVER )
select TAXONOBSERVATION.OBS_ID, TAXONOBSERVATION.AUTHORNAMEID, STRATACOMPOSITION.CHEATSTRATUMTYPE,
STRATACOMPOSITION.PERCENTCOVER
from TAXONOBSERVATION, STRATACOMPOSITION where TAXONOBSERVATION.TAXONOBSERVATION_ID =
STRATACOMPOSITION.TAXONOBSERVATION_ID 
and STRATACOMPOSITION.STRATACOMPOSITION_ID > 0;


--update the 'AUTHOROBSCODE' table
update plotSpeciesSum
	set AUTHOROBSCODE  = 
	(select PLOTOBSERVATION.AUTHOROBSCODE
	from PLOTOBSERVATION 
	where PLOTOBSERVATION.OBS_ID = PLOTSPECIESSUM.OBS_ID);


--update the 'parentPlot' table
update plotSpeciesSum
	set PARENTPLOT  = 
	(select PLOTOBSERVATION.PARENTPLOT
	from PLOTOBSERVATION
	where PLOTOBSERVATION.OBS_ID = PLOTSPECIESSUM.OBS_ID);


--update the authorplot code
update plotSpeciesSum
	set AUTHORPLOTCODE = 
	(select PLOT.AUTHORPLOTCODE from PLOT 
	where PLOT.PLOT_ID = PLOTSPECIESSUM.PARENTPLOT);


--update the PLOT_ID
update plotSpeciesSum
	set PLOT_ID = 
	(select PLOT.PLOT_ID from PLOT 
	where PLOT.PLOT_ID = PLOTSPECIESSUM.PARENTPLOT);




--Send a test query

select PLOT_ID, AUTHORPLOTCODE, AUTHOROBSCODE,  AUTHORNAMEID,  STRATUMTYPE, PERCENTCOVER 
from plotSpeciesSum where AUTHORPLOTCODE like '%';




