/**
* Sample query on prototype database: 
*/


set Heading off;
set linesize 200;
set autocommit on;
set wrap off;




/*
* select all the species related information
*/

select AUTHORNAMEID, ORIGINALAUTHORITY from taxonObservation where OBS_ID in
	(select OBS_ID from PLOTOBSERVATION where PARENTPLOT in 
		(select plot_ID from plot where PLOT_ID = 2));



		
		
/*
* select plot.AUTHORPLOTCODE, cumStrataCoverage speciesTaxon.originalTaxonSymbol, speciesTaxon.ORIGINALTAXONNAME, speciesTaxon.STRATUMTYPE, speciesTaxon.PERCENTCOVER
 *from speciesTaxon, plotMaster 
 *where  speciesTaxon.plot_id=plotmaster.plot_id and plotMaster.AUTHORPLOTNUM  like '%98K123%' ;
*/
