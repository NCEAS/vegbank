create index taxonobservation_id_idx on stratacomposition (taxonobservation_id);
create index parentplot_idx on plotobservation (parentplot);
create index TAXONSTRATUMCOVER_IDX on STRATUMCOMPOSITION (TAXONSTRATUMCOVER);
create index PLANTNAME_ID_IDX on TAXONOBSERVATION  (PLANTNAME_ID);
create index PLANTNAME_IDX on PLANTNAME  (PLANTNAME);
create index STRATUMTYPE_ID_IDX on STRATUM  (STRATUMTYPE_ID);
create index STRATUMNAME_IDX on STRATUMTYPE  (STRATUMNAME);
create index PLOT_ID_IDX on OBSERVATION  (PLOT_ID);
create index TAXONOBSERVATION_ID_IDX on TAXONOBSERVATION  (TAXONOBSERVATION_ID);
create index PLANTNAME_ID_IDX on PLANTNAME  (PLANTNAME_ID);
create index STRATUM_ID_IDX on STRATUM  (STRATUM_ID);
create index OBSERVATION_ID_IDX on OBSERVATION  (OBSERVATION_ID);
create index plantusage_plantname_idx on plantusage (plantname_id);
create index plantconcept_plantname_idx on plantconcept (plantname_id);

create index commusage_commname_idx on commusage (commname_id);
create index commconcept_commname_idx on commconcept (commname_id);
