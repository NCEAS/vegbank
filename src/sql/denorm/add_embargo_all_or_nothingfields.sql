--create fields:
ALTER TABLE plot ADD COLUMN emb_plot Integer;
ALTER TABLE observation ADD COLUMN emb_observation Integer;
ALTER TABLE disturbanceObs ADD COLUMN emb_disturbanceObs Integer;
ALTER TABLE soilObs ADD COLUMN emb_soilObs Integer;
ALTER TABLE taxonObservation ADD COLUMN emb_taxonObservation Integer;
ALTER TABLE taxonInterpretation ADD COLUMN emb_taxonInterpretation Integer;
ALTER TABLE taxonAlt ADD COLUMN emb_taxonAlt Integer;
ALTER TABLE taxonImportance ADD COLUMN emb_taxonImportance Integer;
ALTER TABLE stemCount ADD COLUMN emb_stemCount Integer;
ALTER TABLE stemLocation ADD COLUMN emb_stemLocation Integer;
ALTER TABLE commClass ADD COLUMN emb_commClass Integer;
ALTER TABLE commInterpretation ADD COLUMN emb_commInterpretation Integer;
ALTER TABLE classContributor ADD COLUMN emb_classContributor Integer;

--set default values: to 10 so that we can automatically show those <5 (new values not shown until script completes to denorm this data)

ALTER TABLE plot ALTER COLUMN emb_plot DROP DEFAULT;
ALTER TABLE observation ALTER COLUMN emb_observation DROP DEFAULT;
ALTER TABLE disturbanceObs ALTER COLUMN emb_disturbanceObs DROP DEFAULT;
ALTER TABLE soilObs ALTER COLUMN emb_soilObs DROP DEFAULT;
ALTER TABLE taxonObservation ALTER COLUMN emb_taxonObservation DROP DEFAULT;
ALTER TABLE taxonInterpretation ALTER COLUMN emb_taxonInterpretation DROP DEFAULT;
ALTER TABLE taxonAlt ALTER COLUMN emb_taxonAlt DROP DEFAULT;
ALTER TABLE taxonImportance ALTER COLUMN emb_taxonImportance DROP DEFAULT;
ALTER TABLE stemCount ALTER COLUMN emb_stemCount DROP DEFAULT;
ALTER TABLE stemLocation ALTER COLUMN emb_stemLocation DROP DEFAULT;
ALTER TABLE commClass ALTER COLUMN emb_commClass DROP DEFAULT;
ALTER TABLE commInterpretation ALTER COLUMN emb_commInterpretation DROP DEFAULT;
ALTER TABLE classContributor ALTER COLUMN emb_classContributor DROP DEFAULT;