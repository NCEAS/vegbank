/*Sample query on prototype database:
* will return the AUTHORPLOTNUM, originalTaxonSymbol, ORIGINALTAXONNAME, STRATUMTYPE and percentCover
* for a given plot name (in this case any plot with a string containing 98K123)
*/


set Heading off;
set linesize 200;
set autocommit on;
set wrap off;

select plotmaster.AUTHORPLOTNUM, speciesTaxon.originalTaxonSymbol, speciesTaxon.ORIGINALTAXONNAME, speciesTaxon.STRATUMTYPE, speciesTaxon.PERCENTCOVER
	from speciesTaxon, plotMaster 
where  speciesTaxon.plot_id=plotmaster.plot_id and 
plotMaster.AUTHORPLOTNUM  like '%98K123%' ;

