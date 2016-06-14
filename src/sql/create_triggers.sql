drop trigger udi_commconcept_acccode_trig ON userdatasetitem;
drop function upd_commconceptAccCodeUDI();

CREATE FUNCTION upd_commconceptAccCodeUDI() RETURNS trigger AS $commconceptacccodeudi$
BEGIN
UPDATE userDatasetItem SET itemAccessionCode=(select accessionCode from commConcept where userDatasetItem.itemRecord=commConcept.commConcept_ID) where itemTable ilike 'commConcept' and itemRecord in (select commconcept_Id from commconcept where accessioncode is not null);
RETURN NULL;
END;
$commconceptacccodeudi$
LANGUAGE plpgsql;

create trigger udi_commconc_acccode_trig AFTER INSERT ON userdatasetitem
FOR EACH STATEMENT EXECUTE PROCEDURE upd_commConceptAccCodeUDI();

-- this tested and works successfully on manually added UDI 2016-05-11 by Michael Lee

drop trigger udi_observation_acccode_trig ON userdatasetitem;
drop function upd_observationAccCodeUDI();

CREATE FUNCTION upd_observationAccCodeUDI() RETURNS trigger AS $observationacccodeudi$
BEGIN
UPDATE userDatasetItem SET itemAccessionCode=(select accessionCode from observation where userDatasetItem.itemRecord=observation.observation_ID) where itemTable ilike 'observation' and itemRecord in (select observation_Id from observation where accessioncode is not null);
RETURN NULL;
END;
$observationacccodeudi$
LANGUAGE plpgsql;

create trigger udi_commconc_acccode_trig AFTER INSERT ON userdatasetitem
FOR EACH STATEMENT EXECUTE PROCEDURE upd_observationAccCodeUDI();




drop trigger udi_plot_acccode_trig ON userdatasetitem;
drop function upd_plotAccCodeUDI();

CREATE FUNCTION upd_plotAccCodeUDI() RETURNS trigger AS $plotacccodeudi$
BEGIN
UPDATE userDatasetItem SET itemAccessionCode=(select accessionCode from plot where userDatasetItem.itemRecord=plot.plot_ID) where itemTable ilike 'plot' and itemRecord in (select plot_Id from plot where accessioncode is not null);
RETURN NULL;
END;
$plotacccodeudi$
LANGUAGE plpgsql;

create trigger udi_commconc_acccode_trig AFTER INSERT ON userdatasetitem
FOR EACH STATEMENT EXECUTE PROCEDURE upd_plotAccCodeUDI();




drop trigger udi_plantconcept_acccode_trig ON userdatasetitem;
drop function upd_plantconceptAccCodeUDI();

CREATE FUNCTION upd_plantconceptAccCodeUDI() RETURNS trigger AS $plantconceptacccodeudi$
BEGIN
UPDATE userDatasetItem SET itemAccessionCode=(select accessionCode from plantconcept where userDatasetItem.itemRecord=plantconcept.plantconcept_ID) where itemTable ilike 'plantconcept' and itemRecord in (select plantconcept_Id from plantconcept where accessioncode is not null);
RETURN NULL;
END;
$plantconceptacccodeudi$
LANGUAGE plpgsql;

create trigger udi_commconc_acccode_trig AFTER INSERT ON userdatasetitem
FOR EACH STATEMENT EXECUTE PROCEDURE upd_plantconceptAccCodeUDI();




drop trigger udi_commconcept_acccode_trig ON userdatasetitem;
drop function upd_commconceptAccCodeUDI();

CREATE FUNCTION upd_commconceptAccCodeUDI() RETURNS trigger AS $commconceptacccodeudi$
BEGIN
UPDATE userDatasetItem SET itemAccessionCode=(select accessionCode from commconcept where userDatasetItem.itemRecord=commconcept.commconcept_ID) where itemTable ilike 'commconcept' and itemRecord in (select commconcept_Id from commconcept where accessioncode is not null);
RETURN NULL;
END;
$commconceptacccodeudi$
LANGUAGE plpgsql;

create trigger udi_commconc_acccode_trig AFTER INSERT ON userdatasetitem
FOR EACH STATEMENT EXECUTE PROCEDURE upd_commconceptAccCodeUDI();




drop trigger udi_party_acccode_trig ON userdatasetitem;
drop function upd_partyAccCodeUDI();

CREATE FUNCTION upd_partyAccCodeUDI() RETURNS trigger AS $partyacccodeudi$
BEGIN
UPDATE userDatasetItem SET itemAccessionCode=(select accessionCode from party where userDatasetItem.itemRecord=party.party_ID) where itemTable ilike 'party' and itemRecord in (select party_Id from party where accessioncode is not null);
RETURN NULL;
END;
$partyacccodeudi$
LANGUAGE plpgsql;

create trigger udi_commconc_acccode_trig AFTER INSERT ON userdatasetitem
FOR EACH STATEMENT EXECUTE PROCEDURE upd_partyAccCodeUDI();




drop trigger udi_reference_acccode_trig ON userdatasetitem;
drop function upd_referenceAccCodeUDI();

CREATE FUNCTION upd_referenceAccCodeUDI() RETURNS trigger AS $referenceacccodeudi$
BEGIN
UPDATE userDatasetItem SET itemAccessionCode=(select accessionCode from reference where userDatasetItem.itemRecord=reference.reference_ID) where itemTable ilike 'reference' and itemRecord in (select reference_Id from reference where accessioncode is not null);
RETURN NULL;
END;
$referenceacccodeudi$
LANGUAGE plpgsql;

create trigger udi_commconc_acccode_trig AFTER INSERT ON userdatasetitem
FOR EACH STATEMENT EXECUTE PROCEDURE upd_referenceAccCodeUDI();




drop trigger udi_referencejournal_acccode_trig ON userdatasetitem;
drop function upd_referencejournalAccCodeUDI();

CREATE FUNCTION upd_referencejournalAccCodeUDI() RETURNS trigger AS $referencejournalacccodeudi$
BEGIN
UPDATE userDatasetItem SET itemAccessionCode=(select accessionCode from referencejournal where userDatasetItem.itemRecord=referencejournal.referencejournal_ID) where itemTable ilike 'referencejournal' and itemRecord in (select referencejournal_Id from referencejournal where accessioncode is not null);
RETURN NULL;
END;
$referencejournalacccodeudi$
LANGUAGE plpgsql;

create trigger udi_commconc_acccode_trig AFTER INSERT ON userdatasetitem
FOR EACH STATEMENT EXECUTE PROCEDURE upd_referencejournalAccCodeUDI();




drop trigger udi_stratummethod_acccode_trig ON userdatasetitem;
drop function upd_stratummethodAccCodeUDI();

CREATE FUNCTION upd_stratummethodAccCodeUDI() RETURNS trigger AS $stratummethodacccodeudi$
BEGIN
UPDATE userDatasetItem SET itemAccessionCode=(select accessionCode from stratummethod where userDatasetItem.itemRecord=stratummethod.stratummethod_ID) where itemTable ilike 'stratummethod' and itemRecord in (select stratummethod_Id from stratummethod where accessioncode is not null);
RETURN NULL;
END;
$stratummethodacccodeudi$
LANGUAGE plpgsql;

create trigger udi_commconc_acccode_trig AFTER INSERT ON userdatasetitem
FOR EACH STATEMENT EXECUTE PROCEDURE upd_stratummethodAccCodeUDI();




drop trigger udi_covermethod_acccode_trig ON userdatasetitem;
drop function upd_covermethodAccCodeUDI();

CREATE FUNCTION upd_covermethodAccCodeUDI() RETURNS trigger AS $covermethodacccodeudi$
BEGIN
UPDATE userDatasetItem SET itemAccessionCode=(select accessionCode from covermethod where userDatasetItem.itemRecord=covermethod.covermethod_ID) where itemTable ilike 'covermethod' and itemRecord in (select covermethod_Id from covermethod where accessioncode is not null);
RETURN NULL;
END;
$covermethodacccodeudi$
LANGUAGE plpgsql;

create trigger udi_commconc_acccode_trig AFTER INSERT ON userdatasetitem
FOR EACH STATEMENT EXECUTE PROCEDURE upd_covermethodAccCodeUDI();




drop trigger udi_namedplace_acccode_trig ON userdatasetitem;
drop function upd_namedplaceAccCodeUDI();

CREATE FUNCTION upd_namedplaceAccCodeUDI() RETURNS trigger AS $namedplaceacccodeudi$
BEGIN
UPDATE userDatasetItem SET itemAccessionCode=(select accessionCode from namedplace where userDatasetItem.itemRecord=namedplace.namedplace_ID) where itemTable ilike 'namedplace' and itemRecord in (select namedplace_Id from namedplace where accessioncode is not null);
RETURN NULL;
END;
$namedplaceacccodeudi$
LANGUAGE plpgsql;

create trigger udi_commconc_acccode_trig AFTER INSERT ON userdatasetitem
FOR EACH STATEMENT EXECUTE PROCEDURE upd_namedplaceAccCodeUDI();




drop trigger udi_project_acccode_trig ON userdatasetitem;
drop function upd_projectAccCodeUDI();

CREATE FUNCTION upd_projectAccCodeUDI() RETURNS trigger AS $projectacccodeudi$
BEGIN
UPDATE userDatasetItem SET itemAccessionCode=(select accessionCode from project where userDatasetItem.itemRecord=project.project_ID) where itemTable ilike 'project' and itemRecord in (select project_Id from project where accessioncode is not null);
RETURN NULL;
END;
$projectacccodeudi$
LANGUAGE plpgsql;

create trigger udi_commconc_acccode_trig AFTER INSERT ON userdatasetitem
FOR EACH STATEMENT EXECUTE PROCEDURE upd_projectAccCodeUDI();




drop trigger udi_soiltaxon_acccode_trig ON userdatasetitem;
drop function upd_soiltaxonAccCodeUDI();

CREATE FUNCTION upd_soiltaxonAccCodeUDI() RETURNS trigger AS $soiltaxonacccodeudi$
BEGIN
UPDATE userDatasetItem SET itemAccessionCode=(select accessionCode from soiltaxon where userDatasetItem.itemRecord=soiltaxon.soiltaxon_ID) where itemTable ilike 'soiltaxon' and itemRecord in (select soiltaxon_Id from soiltaxon where accessioncode is not null);
RETURN NULL;
END;
$soiltaxonacccodeudi$
LANGUAGE plpgsql;

create trigger udi_commconc_acccode_trig AFTER INSERT ON userdatasetitem
FOR EACH STATEMENT EXECUTE PROCEDURE upd_soiltaxonAccCodeUDI();




drop trigger udi_userdefined_acccode_trig ON userdatasetitem;
drop function upd_userdefinedAccCodeUDI();

CREATE FUNCTION upd_userdefinedAccCodeUDI() RETURNS trigger AS $userdefinedacccodeudi$
BEGIN
UPDATE userDatasetItem SET itemAccessionCode=(select accessionCode from userdefined where userDatasetItem.itemRecord=userdefined.userdefined_ID) where itemTable ilike 'userdefined' and itemRecord in (select userdefined_Id from userdefined where accessioncode is not null);
RETURN NULL;
END;
$userdefinedacccodeudi$
LANGUAGE plpgsql;

create trigger udi_commconc_acccode_trig AFTER INSERT ON userdatasetitem
FOR EACH STATEMENT EXECUTE PROCEDURE upd_userdefinedAccCodeUDI();




drop trigger udi_taxonobservation_acccode_trig ON userdatasetitem;
drop function upd_taxonobservationAccCodeUDI();

CREATE FUNCTION upd_taxonobservationAccCodeUDI() RETURNS trigger AS $taxonobservationacccodeudi$
BEGIN
UPDATE userDatasetItem SET itemAccessionCode=(select accessionCode from taxonobservation where userDatasetItem.itemRecord=taxonobservation.taxonobservation_ID) where itemTable ilike 'taxonobservation' and itemRecord in (select taxonobservation_Id from taxonobservation where accessioncode is not null);
RETURN NULL;
END;
$taxonobservationacccodeudi$
LANGUAGE plpgsql;

create trigger udi_commconc_acccode_trig AFTER INSERT ON userdatasetitem
FOR EACH STATEMENT EXECUTE PROCEDURE upd_taxonobservationAccCodeUDI();




drop trigger udi_commclass_acccode_trig ON userdatasetitem;
drop function upd_commclassAccCodeUDI();

CREATE FUNCTION upd_commclassAccCodeUDI() RETURNS trigger AS $commclassacccodeudi$
BEGIN
UPDATE userDatasetItem SET itemAccessionCode=(select accessionCode from commclass where userDatasetItem.itemRecord=commclass.commclass_ID) where itemTable ilike 'commclass' and itemRecord in (select commclass_Id from commclass where accessioncode is not null);
RETURN NULL;
END;
$commclassacccodeudi$
LANGUAGE plpgsql;

create trigger udi_commconc_acccode_trig AFTER INSERT ON userdatasetitem
FOR EACH STATEMENT EXECUTE PROCEDURE upd_commclassAccCodeUDI();




drop trigger udi_referenceparty_acccode_trig ON userdatasetitem;
drop function upd_referencepartyAccCodeUDI();

CREATE FUNCTION upd_referencepartyAccCodeUDI() RETURNS trigger AS $referencepartyacccodeudi$
BEGIN
UPDATE userDatasetItem SET itemAccessionCode=(select accessionCode from referenceparty where userDatasetItem.itemRecord=referenceparty.referenceparty_ID) where itemTable ilike 'referenceparty' and itemRecord in (select referenceparty_Id from referenceparty where accessioncode is not null);
RETURN NULL;
END;
$referencepartyacccodeudi$
LANGUAGE plpgsql;

create trigger udi_commconc_acccode_trig AFTER INSERT ON userdatasetitem
FOR EACH STATEMENT EXECUTE PROCEDURE upd_referencepartyAccCodeUDI();




drop trigger udi_aux_role_acccode_trig ON userdatasetitem;
drop function upd_aux_roleAccCodeUDI();

CREATE FUNCTION upd_aux_roleAccCodeUDI() RETURNS trigger AS $aux_roleacccodeudi$
BEGIN
UPDATE userDatasetItem SET itemAccessionCode=(select accessionCode from aux_role where userDatasetItem.itemRecord=aux_role.role_ID) where itemTable ilike 'aux_role' and itemRecord in (select role_Id from aux_role where accessioncode is not null);
RETURN NULL;
END;
$aux_roleacccodeudi$
LANGUAGE plpgsql;

create trigger udi_commconc_acccode_trig AFTER INSERT ON userdatasetitem
FOR EACH STATEMENT EXECUTE PROCEDURE upd_aux_roleAccCodeUDI();




drop trigger udi_taxoninterpretation_acccode_trig ON userdatasetitem;
drop function upd_taxoninterpretationAccCodeUDI();

CREATE FUNCTION upd_taxoninterpretationAccCodeUDI() RETURNS trigger AS $taxoninterpretationacccodeudi$
BEGIN
UPDATE userDatasetItem SET itemAccessionCode=(select accessionCode from taxoninterpretation where userDatasetItem.itemRecord=taxoninterpretation.taxoninterpretation_ID) where itemTable ilike 'taxoninterpretation' and itemRecord in (select taxoninterpretation_Id from taxoninterpretation where accessioncode is not null);
RETURN NULL;
END;
$taxoninterpretationacccodeudi$
LANGUAGE plpgsql;

create trigger udi_commconc_acccode_trig AFTER INSERT ON userdatasetitem
FOR EACH STATEMENT EXECUTE PROCEDURE upd_taxoninterpretationAccCodeUDI();




drop trigger udi_plantconcept_acccode_trig ON userdatasetitem;
drop function upd_plantconceptAccCodeUDI();

CREATE FUNCTION upd_plantconceptAccCodeUDI() RETURNS trigger AS $plantconceptacccodeudi$
BEGIN
UPDATE userDatasetItem SET itemAccessionCode=(select accessionCode from plantconcept where userDatasetItem.itemRecord=plantconcept.plantconcept_ID) where itemTable ilike 'plantconcept' and itemRecord in (select plantconcept_Id from plantconcept where accessioncode is not null);
RETURN NULL;
END;
$plantconceptacccodeudi$
LANGUAGE plpgsql;

create trigger udi_commconc_acccode_trig AFTER INSERT ON userdatasetitem
FOR EACH STATEMENT EXECUTE PROCEDURE upd_plantconceptAccCodeUDI();




                