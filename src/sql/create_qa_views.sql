

CREATE VIEW qa_perObs_classContributor AS SELECT observation.OBSERVATION_ID, Count(classContributor.CLASSCONTRIBUTOR_ID) AS classContributorCount
FROM (observation LEFT JOIN commClass ON observation.OBSERVATION_ID=commClass.OBSERVATION_ID) LEFT JOIN classContributor ON commClass.COMMCLASS_ID=classContributor.COMMCLASS_ID
GROUP BY observation.OBSERVATION_ID;

CREATE VIEW qa_perObs_commClass AS SELECT observation.OBSERVATION_ID, Count(commClass.COMMCLASS_ID) AS commClassCount
FROM observation LEFT JOIN commClass ON observation.OBSERVATION_ID=commClass.OBSERVATION_ID
GROUP BY observation.OBSERVATION_ID;

CREATE VIEW qa_perObs_commInterpretation AS SELECT observation.OBSERVATION_ID, Count(commInterpretation.COMMCLASS_ID) AS countcommInterpretation
FROM (observation LEFT JOIN commClass ON observation.OBSERVATION_ID=commClass.OBSERVATION_ID) LEFT JOIN commInterpretation ON commClass.COMMCLASS_ID=commInterpretation.COMMCLASS_ID
GROUP BY observation.OBSERVATION_ID;

CREATE VIEW qa_perObs_disturbanceObs AS SELECT observation.OBSERVATION_ID, Count(disturbanceObs.disturbanceObs_ID) AS disturbanceObsCount
FROM observation LEFT JOIN disturbanceObs ON observation.OBSERVATION_ID=disturbanceObs.OBSERVATION_ID
GROUP BY observation.OBSERVATION_ID;

CREATE VIEW qa_perObs_embargo AS SELECT observation.OBSERVATION_ID, Count(embargo.embargo_ID) AS embargoCount
FROM (plot LEFT JOIN observation ON plot.PLOT_ID=observation.PLOT_ID) LEFT JOIN embargo ON plot.PLOT_ID=embargo.plot_ID
GROUP BY observation.OBSERVATION_ID;

CREATE VIEW qa_perObs_Observation AS SELECT observation.authorObsCode, observation.OBSERVATION_ID, Count(observation.COVERMETHOD_ID) AS CountOfCOVERMETHOD_ID, Count(observation.STRATUMMETHOD_ID) AS CountOfSTRATUMMETHOD_ID, Count(observation.SOILTAXON_ID) AS CountOfSOILTAXON_ID
FROM observation
GROUP BY observation.authorObsCode, observation.OBSERVATION_ID;

CREATE VIEW qa_perObs_observationContributor AS SELECT observation.OBSERVATION_ID, Count(observationContributor.observationContributor_ID) AS observationContributorCount
FROM observation LEFT JOIN observationContributor ON observation.OBSERVATION_ID=observationContributor.OBSERVATION_ID
GROUP BY observation.OBSERVATION_ID;

CREATE VIEW qa_perObs_place AS SELECT observation.OBSERVATION_ID, Count(place.PLOTPLACE_ID) AS placeCount
FROM (plot LEFT JOIN place ON plot.PLOT_ID=place.PLOT_ID) LEFT JOIN observation ON plot.PLOT_ID=observation.PLOT_ID
GROUP BY observation.OBSERVATION_ID;

CREATE VIEW qa_perObs_soilObs AS SELECT observation.OBSERVATION_ID, Count(soilObs.SOILOBS_ID) AS soilObsCount
FROM observation LEFT JOIN soilObs ON observation.OBSERVATION_ID=soilObs.OBSERVATION_ID
GROUP BY observation.OBSERVATION_ID;

CREATE VIEW qa_perObs_stemCount AS SELECT observation.OBSERVATION_ID, Count(stemCount.STEMCOUNT_ID) AS STEMCOUNTCount
FROM ((observation LEFT JOIN taxonObservation ON observation.OBSERVATION_ID=taxonObservation.OBSERVATION_ID) LEFT JOIN taxonImportance ON taxonObservation.TAXONOBSERVATION_ID=taxonImportance.taxonObservation_ID) LEFT JOIN stemCount ON taxonImportance.taxonImportance_ID=stemCount.TAXONIMPORTANCE_ID
GROUP BY observation.OBSERVATION_ID;

CREATE VIEW qa_perObs_stemLocation AS SELECT observation.OBSERVATION_ID, Count(stemLocation.STEMLOCATION_ID) AS stemLocationCount
FROM (((observation LEFT JOIN taxonObservation ON observation.OBSERVATION_ID=taxonObservation.OBSERVATION_ID) LEFT JOIN taxonImportance ON taxonObservation.TAXONOBSERVATION_ID=taxonImportance.taxonObservation_ID) LEFT JOIN stemCount ON taxonImportance.taxonImportance_ID=stemCount.TAXONIMPORTANCE_ID) LEFT JOIN stemLocation ON stemCount.STEMCOUNT_ID=stemLocation.STEMCOUNT_ID
GROUP BY observation.OBSERVATION_ID;

CREATE VIEW qa_perObs_stratum AS SELECT observation.OBSERVATION_ID, Count(stratum.STRATUM_ID) AS stratumCount
FROM observation LEFT JOIN stratum ON observation.OBSERVATION_ID=stratum.OBSERVATION_ID
GROUP BY observation.OBSERVATION_ID;

CREATE VIEW qa_perObs_taxonAlt AS SELECT observation.OBSERVATION_ID, Count(taxonAlt.taxonAlt_ID) AS taxonAltCount
FROM ((observation LEFT JOIN taxonObservation ON observation.OBSERVATION_ID=taxonObservation.OBSERVATION_ID) LEFT JOIN taxonInterpretation ON taxonObservation.TAXONOBSERVATION_ID=taxonInterpretation.TAXONOBSERVATION_ID) LEFT JOIN taxonAlt ON taxonInterpretation.TAXONINTERPRETATION_ID=taxonAlt.taxonInterpretation_ID
GROUP BY observation.OBSERVATION_ID;

CREATE VIEW qa_perObs_taxonImportance AS SELECT observation.OBSERVATION_ID, Count(taxonImportance.taxonImportance_ID) AS taxonImportanceCount
FROM (observation LEFT JOIN taxonObservation ON observation.OBSERVATION_ID=taxonObservation.OBSERVATION_ID) LEFT JOIN taxonImportance ON taxonObservation.TAXONOBSERVATION_ID=taxonImportance.taxonObservation_ID
GROUP BY observation.OBSERVATION_ID;

CREATE VIEW qa_perObs_taxonInterpretation AS SELECT observation.OBSERVATION_ID, Count(taxonInterpretation.TAXONINTERPRETATION_ID) AS taxonInterpretationCount
FROM (observation LEFT JOIN taxonObservation ON observation.OBSERVATION_ID=taxonObservation.OBSERVATION_ID) LEFT JOIN taxonInterpretation ON taxonObservation.TAXONOBSERVATION_ID=taxonInterpretation.TAXONOBSERVATION_ID
GROUP BY observation.OBSERVATION_ID;

CREATE VIEW qa_perObs_taxonObservation AS SELECT observation.OBSERVATION_ID, Count(taxonObservation.TAXONOBSERVATION_ID) AS taxonObservationCount
FROM observation LEFT JOIN taxonObservation ON observation.OBSERVATION_ID=taxonObservation.OBSERVATION_ID
GROUP BY observation.OBSERVATION_ID;

CREATE VIEW qa_perObs_ALL AS SELECT qa_perObs_Observation.authorObsCode, project.projectName, qa_perObs_commClass.commClassCount, qa_perObs_commInterpretation.countcommInterpretation, qa_perObs_disturbanceObs.disturbanceObsCount, qa_perObs_embargo.embargoCount, qa_perObs_Observation.CountOfCOVERMETHOD_ID, qa_perObs_Observation.CountOfSTRATUMMETHOD_ID, qa_perObs_Observation.CountOfSOILTAXON_ID, qa_perObs_observationContributor.observationContributorCount, qa_perObs_place.placeCount, qa_perObs_soilObs.soilObsCount, qa_perObs_stemCount.STEMCOUNTCount, qa_perObs_classContributor.classContributorCount, qa_perObs_taxonObservation.taxonObservationCount, qa_perObs_taxonInterpretation.taxonInterpretationCount, qa_perObs_taxonImportance.taxonImportanceCount, qa_perObs_taxonAlt.taxonAltCount, qa_perObs_stratum.stratumCount, qa_perObs_stemLocation.stemLocationCount
FROM (((((((((((((((qa_perObs_Observation INNER JOIN qa_perObs_embargo ON qa_perObs_Observation.OBSERVATION_ID=qa_perObs_embargo.OBSERVATION_ID) INNER JOIN qa_perObs_disturbanceObs ON qa_perObs_Observation.OBSERVATION_ID=qa_perObs_disturbanceObs.OBSERVATION_ID) INNER JOIN qa_perObs_commInterpretation ON qa_perObs_Observation.OBSERVATION_ID=qa_perObs_commInterpretation.OBSERVATION_ID) INNER JOIN qa_perObs_commClass ON qa_perObs_Observation.OBSERVATION_ID=qa_perObs_commClass.OBSERVATION_ID) INNER JOIN qa_perObs_observationContributor ON qa_perObs_Observation.OBSERVATION_ID=qa_perObs_observationContributor.OBSERVATION_ID) INNER JOIN qa_perObs_place ON qa_perObs_Observation.OBSERVATION_ID=qa_perObs_place.OBSERVATION_ID) INNER JOIN qa_perObs_soilObs ON qa_perObs_Observation.OBSERVATION_ID=qa_perObs_soilObs.OBSERVATION_ID) INNER JOIN qa_perObs_stemCount ON qa_perObs_Observation.OBSERVATION_ID=qa_perObs_stemCount.OBSERVATION_ID) INNER JOIN qa_perObs_classContributor ON qa_perObs_Observation.OBSERVATION_ID=qa_perObs_classContributor.OBSERVATION_ID) INNER JOIN qa_perObs_taxonObservation ON qa_perObs_Observation.OBSERVATION_ID=qa_perObs_taxonObservation.OBSERVATION_ID) INNER JOIN qa_perObs_taxonInterpretation ON qa_perObs_Observation.OBSERVATION_ID=qa_perObs_taxonInterpretation.OBSERVATION_ID) INNER JOIN qa_perObs_taxonImportance ON qa_perObs_Observation.OBSERVATION_ID=qa_perObs_taxonImportance.OBSERVATION_ID) INNER JOIN qa_perObs_taxonAlt ON qa_perObs_Observation.OBSERVATION_ID=qa_perObs_taxonAlt.OBSERVATION_ID) INNER JOIN qa_perObs_stratum ON qa_perObs_Observation.OBSERVATION_ID=qa_perObs_stratum.OBSERVATION_ID) INNER JOIN qa_perObs_stemLocation ON qa_perObs_Observation.OBSERVATION_ID=qa_perObs_stemLocation.OBSERVATION_ID) INNER JOIN (project INNER JOIN observation ON project.PROJECT_ID=observation.PROJECT_ID) ON qa_perObs_Observation.OBSERVATION_ID=observation.OBSERVATION_ID
ORDER BY qa_perObs_Observation.authorObsCode;