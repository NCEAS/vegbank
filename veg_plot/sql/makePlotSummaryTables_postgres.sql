/*
 *
 * This sql script will create two table which queries composed within the
 * VegBank servlets can access for querying denormalized data.  Basically 
 * the two tables include site data and species information
 *
 * '$Author: harris $'
 * '$Date: 2002-08-01 18:07:58 $'
 * '$Revision: 1.11 $'
 *
 */


/*
 * Update the core VegBank tables so that there will be
 * no database errors while trying to load the 'snapshot'
 * tables.  
 */
update observation set obsenddate = '2002-JAN-01' where obsenddate = null;
update observation set obsstartdate = '2002-JAN-01' where obsstartdate = null;


/*
 * drop already existing tables
 */ 
drop table plotSiteSummary;
drop SEQUENCE PLOTSITESUMMARY_ID_seq;


/*
 * create the tables
 */

CREATE SEQUENCE PLOTSITESUMMARY_ID_seq;

CREATE TABLE plotSiteSummary (
PLOTSITESUMMARY_ID NUMERIC(20) default nextval('PLOTSITESUMMARY_ID_seq'),
PLOT_ID integer,
OBSERVATION_ID integer,
PROJECT_ID integer,
PLOTTYPE VARCHAR(30),
SAMPLINGMETHOD VARCHAR(45),
COVERSCALE VARCHAR(30),
PLOTORIGINLAT NUMERIC(30),
PLOTORIGINLONG NUMERIC(30),
PLOTSHAPE VARCHAR(30),
PLOTAREA VARCHAR(30),
ALTVALUE float,
SLOPEASPECT NUMERIC(30),
SLOPEGRADIENT NUMERIC(30),
SLOPEPOSITION VARCHAR(100),
HYDROLOGICREGIME VARCHAR(100),
SOILDRAINAGE VARCHAR(100),
CURRENTCOMMUNITY VARCHAR(140),
CURRENTCOMMUNITYCODE VARCHAR(45),
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
COUNTRY VARCHAR(100),
PARENTPLOT NUMERIC(12),
AUTHOROBSCODE VARCHAR(100),
ACCESSION_NUMBER varchar (200),
CONSTRAINT plotSiteSummary_pk PRIMARY KEY (PLOTSITESUMMARY_ID)
);



/*
 * Load the site table
 */
insert into plotSiteSummary (plot_id, project_id, 
plotoriginlat, plotoriginlong, plotshape, altvalue, slopeaspect,
slopegradient, slopeposition, hydrologicregime, soildrainage, currentcommunity,
xcoord, ycoord, coordtype, obsstartdate, obsstopdate, effortlevel,  
authorplotcode, surfGeo, state, country, parentplot, authorobscode,
soilDepth, leaftype, accession_number )
select plot.plot_id, plot.project_id,  
plot.latitude, plot.longitude, plot.shape,  plot.elevation,
plot.slopeaspect, plot.slopegradient, plot.topoposition, observation.hydrologicregime,
observation.soildrainage, null, plot.authore, plot.authorn,
plot.authordatum, observation.obsstartdate, observation.obsenddate,
observation.effortLevel,
plot.authorplotcode, plot.geology, plot.state, 
plot.country, null, observation.authorobscode, 
observation.soilDepth, null, plot.accession_number
from plot, observation where observation.plot_id = plot.plot_id;

-- UPDATE THE OBSERVATION ID'S BASED ON THE PLOTS, THESE PROCESSES
-- SHOULD BE REVERSED AT SOME POINT
update PLOTSITESUMMARY set OBSERVATION_ID = 
	(select OBSERVATION_ID from OBSERVATION where OBSERVATION.PLOT_ID = PLOTSITESUMMARY.PLOT_ID);

-- UPDATE THE COMMUNITY INFORMATION IN THE PLOT SITE SUMMARY TABLE
update PLOTSITESUMMARY set  CURRENTCOMMUNITY = 
	(select COMMNAME from COMMCLASS where COMMCLASS.OBSERVATION_ID = PLOTSITESUMMARY.OBSERVATION_ID);
update PLOTSITESUMMARY set  CURRENTCOMMUNITYCODE = 
	(select COMMCODE from COMMCLASS where COMMCLASS.OBSERVATION_ID = PLOTSITESUMMARY.OBSERVATION_ID);



/*
 * create the tables  -- for plot species related information
 */
drop table plotSpeciesSum;
drop SEQUENCE PLOTSPECIESSUM_ID_seq;
CREATE SEQUENCE PLOTSPECIESSUM_ID_seq;
CREATE TABLE plotSpeciesSum (
PLOTSPECIESSUM_ID integer default nextval('PLOTSITESUMMARY_ID_seq'),
PLOT_ID integer,
OBS_ID integer,
PARENTPLOT integer,
AUTHORNAMEID VARCHAR(300),
AUTHORPLANTCODE VARCHAR(300),
AUTHORPLOTCODE VARCHAR(100),
AUTHOROBSCODE VARCHAR(100),
TAXONOBSERVATION_ID NUMERIC(20),
STRATUMTYPE VARCHAR(32),
PERCENTCOVER NUMERIC(8),
CONSTRAINT plotSpeciesSum_pk PRIMARY KEY (PLOTSPECIESSUM_ID)
);



/*
* load the species summary table 
*/

insert into plotSpeciesSum (OBS_ID, AUTHORNAMEID, STRATUMTYPE, PERCENTCOVER, AUTHORPLANTCODE )
select 
	TAXONOBSERVATION.OBSERVATION_ID, 
	TAXONOBSERVATION.cheatPlantName, 
	STRATUMCOMPOSITION.CHEATSTRATUMNAME,
	STRATUMCOMPOSITION.TAXONSTRATUMCOVER,
	STRATUMCOMPOSITION.CHEATPLANTCODE
from 
	TAXONOBSERVATION, STRATUMCOMPOSITION 
where 
	TAXONOBSERVATION.TAXONOBSERVATION_ID =
	STRATUMCOMPOSITION.TAXONOBSERVATION_ID 
and 
	STRATUMCOMPOSITION.STRATUMCOMPOSITION_ID > 0;
	


--UPDATE THE SPECIES SUMMARY TABLE


--update the 'AUTHOROBSCODE' table
update plotSpeciesSum
	set AUTHOROBSCODE  = 
	(select OBSERVATION.AUTHOROBSCODE
	from OBSERVATION 
	where OBSERVATION.OBSERVATION_ID = PLOTSPECIESSUM.OBS_ID);


--update the 'parentPlot' table
update plotSpeciesSum
	set PARENTPLOT  = 
	(select OBSERVATION.PLOT_ID
	from OBSERVATION
	where OBSERVATION.OBSERVATION_ID = PLOTSPECIESSUM.OBS_ID);


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



/*
* Send a test query
*/
--select plot_id, accession_number, authorplotcode, plotoriginlat, plotoriginlong from plotsitesummary;
--select PLOT_ID, AUTHORPLOTCODE, AUTHOROBSCODE,  
--AUTHORNAMEID,  STRATUMTYPE, PERCENTCOVER 
--from plotSpeciesSum where AUTHORPLOTCODE like '%';








