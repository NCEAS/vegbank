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

ALTER TABLE plot ALTER COLUMN emb_plot SET DEFAULT 10;
ALTER TABLE observation ALTER COLUMN emb_observation SET DEFAULT 10;
ALTER TABLE disturbanceObs ALTER COLUMN emb_disturbanceObs SET DEFAULT 10;
ALTER TABLE soilObs ALTER COLUMN emb_soilObs SET DEFAULT 10;
ALTER TABLE taxonObservation ALTER COLUMN emb_taxonObservation SET DEFAULT 10;
ALTER TABLE taxonInterpretation ALTER COLUMN emb_taxonInterpretation SET DEFAULT 10;
ALTER TABLE taxonAlt ALTER COLUMN emb_taxonAlt SET DEFAULT 10;
ALTER TABLE taxonImportance ALTER COLUMN emb_taxonImportance SET DEFAULT 10;
ALTER TABLE stemCount ALTER COLUMN emb_stemCount SET DEFAULT 10;
ALTER TABLE stemLocation ALTER COLUMN emb_stemLocation SET DEFAULT 10;
ALTER TABLE commClass ALTER COLUMN emb_commClass SET DEFAULT 10;
ALTER TABLE commInterpretation ALTER COLUMN emb_commInterpretation SET DEFAULT 10;
ALTER TABLE classContributor ALTER COLUMN emb_classContributor SET DEFAULT 10;