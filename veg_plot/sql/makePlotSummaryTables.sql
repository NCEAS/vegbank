/*
 *
 * This sql script will create two table which queries composed within the
 * servlets can access for querying denormalized data.  Basically the two tables
 * include site data and species information
 *
 */

set heading off;
set autocommit on;
set wrap off;
set define off;
set linesize 800;
set heading off;
set pagesize 1000;

/*
 * Do the formatting
 */

/*
column  AUTHORNAMEID format a100
column  ORIGINALAUTHORITY format a100

column  plot.authorplotcode format a12
column  plot.surfGeo format a12
column  plot.surfGeo format a12
column   plotObservation.soilType format a12
*/

/*
 * drop already existing tables
 */ 
drop table plotSiteSummary;
drop SEQUENCE PLOTSITESUMMARY_ID_seq;
DROP TRIGGER PLOTSITESUMMARY_before_insert;



/*
 * create the tables
 */

 
CREATE TABLE plotSiteSummary (
PLOTSITESUMMARY_ID NUMBER(20),
PLOT_ID NUMBER(30),
PROJECT_ID NUMBER(30),
PLOTTYPE VARCHAR2(30),
SAMPLINGMETHOD VARCHAR2(45),
COVERSCALE VARCHAR2(30),
PLOTORIGINLAT NUMBER(30),
PLOTORIGINLONG NUMBER(30),
PLOTSHAPE VARCHAR2(30),
PLOTAREA VARCHAR2(30),
ALTVALUE NUMBER(30),
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
PARENTPLOT NUMBER(12),
AUTHOROBSCODE VARCHAR2(100),
CONSTRAINT plotSiteSummary_pk PRIMARY KEY (PLOTSITESUMMARY_ID)
);

CREATE SEQUENCE PLOTSITESUMMARY_ID_seq;
CREATE TRIGGER PLOTSITESUMMARY_before_insert
BEFORE INSERT ON plotSiteSummary FOR EACH ROW
BEGIN
  SELECT PLOTSITESUMMARY_ID_seq.nextval
    INTO :new.PLOTSITESUMMARY_ID
    FROM dual;
END;
/

/*
 * Load the site table
 */
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
commit;

/*
 *Show the load results
 */

 /*
 select PLOTTYPE, AUTHOROBSCODE, SLOPEGRADIENT, SLOPEASPECT from plotsitesummary;
 select HYDROLOGICREGIME, AUTHOROBSCODE from plotsitesummary;
*/

/** ------------------------------------------------------------------------*/

/*
 * drop already existing tables -- for plot species information
 */ 
drop table plotSpeciesSum;
drop SEQUENCE PLOTSPECIESSUM_ID_seq;
DROP TRIGGER PLOTSPECIESSUM_before_insert;


/*
 * create the tables  -- for plot species related information
 */

 
CREATE TABLE plotSpeciesSum (
PLOTSPECIESSUM_ID NUMBER(20),
PLOT_ID NUMBER(30),
OBS_ID NUMBER(30),
PARENTPLOT NUMBER(38),
AUTHORNAMEID VARCHAR2(300),
AUTHORPLOTCODE VARCHAR2(100),
AUTHOROBSCODE VARCHAR2(100),
TAXONOBSERVATION_ID NUMBER(20),
STRATUMTYPE VARCHAR2(32),
PERCENTCOVER NUMBER(8),
CONSTRAINT plotSpeciesSum_pk PRIMARY KEY (PLOTSPECIESSUM_ID)
);

CREATE SEQUENCE PLOTSPECIESSUM_ID_seq;
CREATE TRIGGER PLOTSPECIESSUM_before_insert
BEFORE INSERT ON plotSpeciesSum FOR EACH ROW
BEGIN
  SELECT PLOTSPECIESSUM_ID_seq.nextval
    INTO :new.PLOTSPECIESSUM_ID
    FROM dual;
END;
/

/*
* load the species summary table 
*/

insert into plotSpeciesSum (OBS_ID, AUTHORNAMEID, STRATUMTYPE, PERCENTCOVER )
select TAXONOBSERVATION.OBS_ID, TAXONOBSERVATION.AUTHORNAMEID, STRATACOMPOSITION.CHEATSTRATUMTYPE,
STRATACOMPOSITION.PERCENTCOVER
from TAXONOBSERVATION, STRATACOMPOSITION where TAXONOBSERVATION.TAXONOBSERVATION_ID =
STRATACOMPOSITION.TAXONOBSERVATION_ID 
and STRATACOMPOSITION.STRATACOMPOSITION_ID > 0 ;
commit;


--update the 'AUTHOROBSCODE' table
update plotSpeciesSum
	set AUTHOROBSCODE  = 
	(select PLOTOBSERVATION.AUTHOROBSCODE
	from PLOTOBSERVATION 
	where PLOTOBSERVATION.OBS_ID = PLOTSPECIESSUM.OBS_ID);
commit;

--update the 'parentPlot' table
update plotSpeciesSum
	set PARENTPLOT  = 
	(select PLOTOBSERVATION.PARENTPLOT
	from PLOTOBSERVATION
	where PLOTOBSERVATION.OBS_ID = PLOTSPECIESSUM.OBS_ID);
commit;

--update the authorplot code
update plotSpeciesSum
	set AUTHORPLOTCODE = 
	(select PLOT.AUTHORPLOTCODE from PLOT 
	where PLOT.PLOT_ID = PLOTSPECIESSUM.PARENTPLOT);
commit;

--update the PLOT_ID
update plotSpeciesSum
	set PLOT_ID = 
	(select PLOT.PLOT_ID from PLOT 
	where PLOT.PLOT_ID = PLOTSPECIESSUM.PARENTPLOT);
commit;


/*
* Send a test query
*/
column  AUTHORPLOTCODE format a25
column  AUTHORNAMEID format a25
column  AUTHOROBSCODE format a20
column  STRATUMTYPE format a5
column PERCENTCOVER format 99

select PLOT_ID, AUTHORPLOTCODE, AUTHOROBSCODE,  AUTHORNAMEID,  STRATUMTYPE, PERCENTCOVER 
from plotSpeciesSum where AUTHORPLOTCODE like 'VOYA.03';




