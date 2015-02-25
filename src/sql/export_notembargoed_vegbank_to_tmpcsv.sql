-- MUST BE SUPERUSER TO RUN THIS IN PSQL.  LOG IN as postgres to do so.
-- may also want to do something to export the schema, such as pg_dump -s  -U vegbank vegbank > /tmp/vegbank/vegbank-schema.sql

COPY (select * from aux_role) TO '/tmp/vegbank/aux_role.csv' WITH CSV HEADER;
COPY (select * from commconcept) TO '/tmp/vegbank/commconcept.csv' WITH CSV HEADER;
COPY (select * from commcorrelation) TO '/tmp/vegbank/commcorrelation.csv' WITH CSV HEADER;
COPY (select * from commlineage) TO '/tmp/vegbank/commlineage.csv' WITH CSV HEADER;
COPY (select * from commname) TO '/tmp/vegbank/commname.csv' WITH CSV HEADER;
COPY (select * from commstatus) TO '/tmp/vegbank/commstatus.csv' WITH CSV HEADER;
COPY (select * from commusage) TO '/tmp/vegbank/commusage.csv' WITH CSV HEADER;
COPY (select * from coverindex) TO '/tmp/vegbank/coverindex.csv' WITH CSV HEADER;
COPY (select * from covermethod) TO '/tmp/vegbank/covermethod.csv' WITH CSV HEADER;
COPY (select * from namedplace) TO '/tmp/vegbank/namedplace.csv' WITH CSV HEADER;
COPY (select * from observationsynonym) TO '/tmp/vegbank/observationsynonym.csv' WITH CSV HEADER;
COPY (select * from party) TO '/tmp/vegbank/party.csv' WITH CSV HEADER;
COPY (select * from place) TO '/tmp/vegbank/place.csv' WITH CSV HEADER;
COPY (select * from plantconcept) TO '/tmp/vegbank/plantconcept.csv' WITH CSV HEADER;
COPY (select * from plantcorrelation) TO '/tmp/vegbank/plantcorrelation.csv' WITH CSV HEADER;
COPY (select * from plantlineage) TO '/tmp/vegbank/plantlineage.csv' WITH CSV HEADER;
COPY (select * from plantname) TO '/tmp/vegbank/plantname.csv' WITH CSV HEADER;
COPY (select * from plantstatus) TO '/tmp/vegbank/plantstatus.csv' WITH CSV HEADER;
COPY (select * from plantusage) TO '/tmp/vegbank/plantusage.csv' WITH CSV HEADER;
COPY (select * from project) TO '/tmp/vegbank/project.csv' WITH CSV HEADER;
COPY (select * from projectcontributor) TO '/tmp/vegbank/projectcontributor.csv' WITH CSV HEADER;
COPY (select * from reference) TO '/tmp/vegbank/reference.csv' WITH CSV HEADER;
COPY (select * from referencealtident) TO '/tmp/vegbank/referencealtident.csv' WITH CSV HEADER;
COPY (select * from referencecontributor) TO '/tmp/vegbank/referencecontributor.csv' WITH CSV HEADER;
COPY (select * from referencejournal) TO '/tmp/vegbank/referencejournal.csv' WITH CSV HEADER;
COPY (select * from referenceparty) TO '/tmp/vegbank/referenceparty.csv' WITH CSV HEADER;
COPY (select * from soiltaxon) TO '/tmp/vegbank/soiltaxon.csv' WITH CSV HEADER;
COPY (select * from stratum) TO '/tmp/vegbank/stratum.csv' WITH CSV HEADER;
COPY (select * from stratummethod) TO '/tmp/vegbank/stratummethod.csv' WITH CSV HEADER;
COPY (select * from stratumtype) TO '/tmp/vegbank/stratumtype.csv' WITH CSV HEADER;

COPY (select * from view_export_classContributor) TO '/tmp/vegbank/classContributor.csv' WITH CSV HEADER;
COPY (select * from view_export_commClass) TO '/tmp/vegbank/commClass.csv' WITH CSV HEADER;
COPY (select * from view_export_commInterpretation) TO '/tmp/vegbank/commInterpretation.csv' WITH CSV HEADER;
COPY (select * from view_export_disturbanceObs) TO '/tmp/vegbank/disturbanceObs.csv' WITH CSV HEADER;
COPY (select * from view_export_observation) TO '/tmp/vegbank/observation.csv' WITH CSV HEADER;
COPY (select * from view_export_observationcontributor) TO '/tmp/vegbank/observationcontributor.csv' WITH CSV HEADER;
COPY (select * from view_export_plot) TO '/tmp/vegbank/plot.csv' WITH CSV HEADER;
COPY (select * from view_export_soilObs) TO '/tmp/vegbank/soilObs.csv' WITH CSV HEADER;
COPY (select * from view_export_stemCount) TO '/tmp/vegbank/stemCount.csv' WITH CSV HEADER;
COPY (select * from view_export_stemLocation) TO '/tmp/vegbank/stemLocation.csv' WITH CSV HEADER;
COPY (select * from view_export_taxonAlt) TO '/tmp/vegbank/taxonAlt.csv' WITH CSV HEADER;
COPY (select * from view_export_taxonImportance) TO '/tmp/vegbank/taxonImportance.csv' WITH CSV HEADER;
COPY (select * from view_export_taxonInterpretation) TO '/tmp/vegbank/taxonInterpretation.csv' WITH CSV HEADER;
COPY (select * from view_export_taxonObservation) TO '/tmp/vegbank/taxonObservation.csv' WITH CSV HEADER;

