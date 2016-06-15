-- first observation insertion started, then plot, then embargo, then observation finished, and related to observation tables.
-- however, it could go the other way later, with a new embargo added to existing framework
-- this will never do anything because as plot is added, there can't be an embargo record, so we have to attach the emb_plot to the embargo table itself


drop trigger emb_plot_trig ON plot;
drop function upd_plot_from_confidstatus();

CREATE FUNCTION upd_plot_from_confidstatus() RETURNS trigger AS $embplot$
BEGIN
update plot set emb_plot = NEW.confidentialityStatus where plot_id=NEW.plot_id;
RETURN NULL;
END;
$embplot$
LANGUAGE plpgsql;

create trigger emb_plot_trig AFTER INSERT ON plot
FOR EACH ROW EXECUTE PROCEDURE upd_plot_from_confidstatus();


drop trigger emb_embargo_trig ON embargo;
drop function upd_plot_from_embargo();

CREATE FUNCTION upd_plot_from_embargo() RETURNS trigger AS $embplot$
BEGIN
update plot set emb_plot = 
  (select max(currentEmb) from view_emb_embargo_complete where plot.plot_id = view_emb_embargo_complete.plot_ID) where plot_id=NEW.plot_id;
  
update observation set emb_observation = (select emb_plot from plot where 
plot.plot_ID=observation.plot_id) where observation.plot_Id = NEW.plot_id;

--disturbanceObs
update disturbanceObs set emb_disturbanceObs = (select emb_observation from 
observation where observation.observation_ID=disturbanceObs.observation_ID) 
where observation_id in (select observation_id from observation where plot_id=NEW.plot_id);

--soilObs
update soilObs set emb_soilObs = (select emb_observation from observation 
where observation.observation_ID=soilObs.observation_ID)  
where observation_id in (select observation_id from observation where plot_id=NEW.plot_id);

--commClass
UPDATE commClass set emb_commClass = (select emb_observation from observation 
where observation.observation_ID=commClass.observation_ID)  
where observation_id in (select observation_id from observation where plot_id=NEW.plot_id);

---classContributor
UPDATE classContributor SET emb_classContributor= (select emb_commClass from 
commClass where commClass.commClass_ID=classContributor.commClass_ID)  
where commclass_id in (select commClass_Id from commclass where observation_id in (select observation_id from observation where plot_id=NEW.plot_id));

---commInterpretation
UPDATE commInterpretation SET emb_commInterpretation= (select emb_commClass from 
commClass where commClass.commClass_ID=commInterpretation.commClass_ID) where 
commclass_id in (select commClass_Id from commclass where observation_id in (select observation_id from observation where plot_id=NEW.plot_id));

--taxonObservation

update taxonObservation set emb_taxonObservation = (select emb_observation from 
observation where observation.observation_ID=taxonObservation.observation_ID) 
where observation_id in (select observation_id from observation where plot_id=NEW.plot_id);

---taxonImportance
update taxonImportance set emb_taxonImportance = (select emb_taxonObservation 
FROM taxonObservation   where taxonImportance.taxonObservation_id =
taxonObservation.taxonObservation_ID) 
where taxonobservation_id in (select taxonObservation_ID from taxonObservation where observation_id IN (select observation_ID from observation where plot_id=NEW.plot_id));

----stemCount
update stemCount set emb_stemCount = (select emb_taxonImportance FROM 
taxonImportance   where stemCount.taxonImportance_id =taxonImportance.taxonImportance_ID) 
where taxonImportance_Id in (select taxonImportance_Id from taxonImportance where taxonobservation_id in (select taxonObservation_ID from taxonObservation where observation_id IN (select observation_ID from observation where plot_id=NEW.plot_id)));

-----stemLocation
update stemLocation set emb_stemLocation = (select emb_stemCount FROM stemCount   
where stemLocation.stemCount_id =stemCount.stemCount_ID) 
where stemCount_ID in (select stemCount_ID from stemCount where taxonImportance_Id in (select taxonImportance_Id from taxonImportance where taxonobservation_id in (select taxonObservation_ID from taxonObservation where observation_id IN (select observation_ID from observation where plot_id=NEW.plot_id))));

---taxonInterpretation
update taxonInterpretation set emb_taxonInterpretation = 
(select emb_taxonObservation FROM taxonObservation   where 
taxonInterpretation.taxonObservation_id =taxonObservation.taxonObservation_ID) 
where taxonobservation_id in (select taxonObservation_ID from taxonObservation where observation_id IN (select observation_ID from observation where plot_id=NEW.plot_id));

----taxonAlt
update taxonAlt set emb_taxonAlt = (select emb_taxonInterpretation FROM 
taxonInterpretation   where taxonAlt.taxonInterpretation_id =
taxonInterpretation.taxonInterpretation_ID) 
where taxonInterpretation_id in (select taxonInterpretation_ID from taxonInterpretation where taxonobservation_id in (select taxonObservation_ID from taxonObservation where observation_id IN (select observation_ID from observation where plot_id=NEW.plot_id)));
  
RETURN NULL;
END;
$embplot$
LANGUAGE plpgsql;

create trigger emb_embargo_trig AFTER INSERT ON embargo
FOR EACH ROW EXECUTE PROCEDURE upd_plot_from_embargo();

create trigger emb_embargo_trig2 AFTER UPDATE ON embargo
FOR EACH ROW EXECUTE PROCEDURE upd_plot_from_embargo();



--now create trigger for each table to read from "parent" table when it is created!!

drop trigger emb_observation_trig ON observation;
drop function upd_observation_emb();

CREATE FUNCTION upd_observation_emb() RETURNS trigger AS $embobservation$
BEGIN
update observation set emb_observation = (select emb_plot from plot where 
plot.plot_ID=observation.plot_id) where observation.observation_ID = NEW.observation_ID;
RETURN NULL;
END;
$embobservation$
LANGUAGE plpgsql;

create trigger emb_observation_trig AFTER INSERT ON observation
FOR EACH ROW EXECUTE PROCEDURE upd_observation_emb();


drop trigger emb_taxonobservation_trig ON taxonobservation;
drop function upd_taxonobservation_emb();

CREATE FUNCTION upd_taxonobservation_emb() RETURNS trigger AS $embtaxonobservation$
BEGIN
update taxonobservation set emb_taxonobservation = (select emb_observation from observation where 
observation.observation_ID=taxonobservation.observation_ID) where taxonobservation.taxonobservation_ID = NEW.taxonobservation_ID;
RETURN NULL;
END;
$embtaxonobservation$
LANGUAGE plpgsql;

create trigger emb_taxonobservation_trig AFTER INSERT ON taxonobservation
FOR EACH ROW EXECUTE PROCEDURE upd_taxonobservation_emb();


drop trigger emb_taxonImportance_trig ON taxonImportance;
drop function upd_taxonImportance_emb();

CREATE FUNCTION upd_taxonImportance_emb() RETURNS trigger AS $embtaxonImportance$
BEGIN
update taxonImportance set emb_taxonImportance = (select emb_taxonobservation from taxonobservation where 
taxonobservation.taxonobservation_ID=taxonImportance.taxonobservation_ID) where taxonImportance.taxonImportance_ID = NEW.taxonImportance_ID;
RETURN NULL;
END;
$embtaxonImportance$
LANGUAGE plpgsql;

create trigger emb_taxonImportance_trig AFTER INSERT ON taxonImportance
FOR EACH ROW EXECUTE PROCEDURE upd_taxonImportance_emb();


drop trigger emb_stemCount_trig ON stemCount;
drop function upd_stemCount_emb();

CREATE FUNCTION upd_stemCount_emb() RETURNS trigger AS $embstemCount$
BEGIN
update stemCount set emb_stemCount = (select emb_taxonImportance from taxonImportance where 
taxonImportance.taxonImportance_ID=stemCount.taxonImportance_ID) where stemCount.stemCount_ID = NEW.stemCount_ID;
RETURN NULL;
END;
$embstemCount$
LANGUAGE plpgsql;

create trigger emb_stemCount_trig AFTER INSERT ON stemCount
FOR EACH ROW EXECUTE PROCEDURE upd_stemCount_emb();



drop trigger emb_stemLocation_trig ON stemLocation;
drop function upd_stemLocation_emb();

CREATE FUNCTION upd_stemLocation_emb() RETURNS trigger AS $embstemLocation$
BEGIN
update stemLocation set emb_stemLocation = (select emb_stemCount from stemCount where 
stemCount.stemCount_ID=stemLocation.stemCount_ID) where stemLocation.stemLocation_ID = NEW.stemLocation_ID;
RETURN NULL;
END;
$embstemLocation$
LANGUAGE plpgsql;

create trigger emb_stemLocation_trig AFTER INSERT ON stemLocation
FOR EACH ROW EXECUTE PROCEDURE upd_stemLocation_emb();

drop trigger emb_taxonInterpretation_trig ON taxonInterpretation;
drop function upd_taxonInterpretation_emb();

CREATE FUNCTION upd_taxonInterpretation_emb() RETURNS trigger AS $embtaxonInterpretation$
BEGIN
update taxonInterpretation set emb_taxonInterpretation = (select emb_taxonobservation from taxonobservation where 
taxonobservation.taxonobservation_ID=taxonInterpretation.taxonobservation_ID) where taxonInterpretation.taxonInterpretation_ID = NEW.taxonInterpretation_ID;
RETURN NULL;
END;
$embtaxonInterpretation$
LANGUAGE plpgsql;

create trigger emb_taxonInterpretation_trig AFTER INSERT ON taxonInterpretation
FOR EACH ROW EXECUTE PROCEDURE upd_taxonInterpretation_emb();



drop trigger emb_taxonAlt_trig ON taxonAlt;
drop function upd_taxonAlt_emb();

CREATE FUNCTION upd_taxonAlt_emb() RETURNS trigger AS $embtaxonAlt$
BEGIN
update taxonAlt set emb_taxonAlt = (select emb_taxonInterpretation from taxonInterpretation where 
taxonInterpretation.taxonInterpretation_ID=taxonAlt.taxonInterpretation_ID) where taxonAlt.taxonAlt_ID = NEW.taxonAlt_ID;
RETURN NULL;
END;
$embtaxonAlt$
LANGUAGE plpgsql;

create trigger emb_taxonAlt_trig AFTER INSERT ON taxonAlt
FOR EACH ROW EXECUTE PROCEDURE upd_taxonAlt_emb();

drop trigger emb_soilObs_trig ON soilObs;
drop function upd_soilObs_emb();

CREATE FUNCTION upd_soilObs_emb() RETURNS trigger AS $embsoilObs$
BEGIN
update soilObs set emb_soilObs = (select emb_observation from observation where 
observation.observation_ID=soilObs.observation_ID) where soilObs.soilObs_ID = NEW.soilObs_ID;
RETURN NULL;
END;
$embsoilObs$
LANGUAGE plpgsql;

create trigger emb_soilObs_trig AFTER INSERT ON soilObs
FOR EACH ROW EXECUTE PROCEDURE upd_soilObs_emb();


drop trigger emb_disturbanceObs_trig ON disturbanceObs;
drop function upd_disturbanceObs_emb();

CREATE FUNCTION upd_disturbanceObs_emb() RETURNS trigger AS $embdisturbanceObs$
BEGIN
update disturbanceObs set emb_disturbanceObs = (select emb_observation from observation where 
observation.observation_ID=disturbanceObs.observation_ID) where disturbanceObs.disturbanceObs_ID = NEW.disturbanceObs_ID;
RETURN NULL;
END;
$embdisturbanceObs$
LANGUAGE plpgsql;

create trigger emb_disturbanceObs_trig AFTER INSERT ON disturbanceObs
FOR EACH ROW EXECUTE PROCEDURE upd_disturbanceObs_emb();


drop trigger emb_commClass_trig ON commClass;
drop function upd_commClass_emb();

CREATE FUNCTION upd_commClass_emb() RETURNS trigger AS $embcommClass$
BEGIN
update commClass set emb_commClass = (select emb_observation from observation where 
observation.observation_ID=commClass.observation_ID) where commClass.commClass_ID = NEW.commClass_ID;
RETURN NULL;
END;
$embcommClass$
LANGUAGE plpgsql;

create trigger emb_commClass_trig AFTER INSERT ON commClass
FOR EACH ROW EXECUTE PROCEDURE upd_commClass_emb();

drop trigger emb_commInterpretation_trig ON commInterpretation;
drop function upd_commInterpretation_emb();

CREATE FUNCTION upd_commInterpretation_emb() RETURNS trigger AS $embcommInterpretation$
BEGIN
update commInterpretation set emb_commInterpretation = (select emb_commClass from commclass where 
commclass.commclass_ID=commInterpretation.commclass_ID) where commInterpretation.commInterpretation_ID = NEW.commInterpretation_ID;
RETURN NULL;
END;
$embcommInterpretation$
LANGUAGE plpgsql;

create trigger emb_commInterpretation_trig AFTER INSERT ON commInterpretation
FOR EACH ROW EXECUTE PROCEDURE upd_commInterpretation_emb();


drop trigger emb_classContributor_trig ON classContributor;
drop function upd_classContributor_emb();

CREATE FUNCTION upd_classContributor_emb() RETURNS trigger AS $embclassContributor$
BEGIN
update classContributor set emb_classContributor = (select emb_commClass from commclass where 
commclass.commclass_ID=classContributor.commclass_ID) where classContributor.classContributor_ID = NEW.classContributor_ID;
RETURN NULL;
END;
$embclassContributor$
LANGUAGE plpgsql;

create trigger emb_classContributor_trig AFTER INSERT ON classContributor
FOR EACH ROW EXECUTE PROCEDURE upd_classContributor_emb();