
/**
*
* Simple sql query to retrieve all plot_id's, their project_id's abd the
* authorPlotCode for plots containing a specific taxonName.
*
*/


select PLOT_ID, PROJECT_ID, AUTHORPLOTCODE from PLOT where PLOT_ID in
        (select PARENTPLOT  from PLOTOBSERVATION  where OBS_ID in
                (select OBS_ID from TAXONOBSERVATION where AUTHORNAMEID like '%Sparganium%'));
