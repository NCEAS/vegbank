
---creating fields ----------------------

ALTER TABLE observation ADD COLUMN coverEstimationMethod varchar (80) ;

ALTER TABLE observation ADD COLUMN stratumIndividualDivision varchar (50) ;

---creating fields ----------------------

ALTER TABLE observation ADD COLUMN totalCover Float ;

----creating tables------------      

CREATE TABLE taxonImportance
(
taxonImportance_ID serial NOT NULL ,
taxonObservation_ID Integer NOT NULL ,
stratum_ID Integer ,
cover Float ,
basalArea Float ,
biomass Float ,
inferenceArea Float ,

PRIMARY KEY ( taxonImportance_ID )
);

ALTER TABLE taxonImportance   ADD CONSTRAINT R1taxonImportance_taxonObservation_ID FOREIGN KEY (taxonObservation_ID)    REFERENCES taxonObservation (taxonObservation_ID );

ALTER TABLE taxonImportance   ADD CONSTRAINT R2taxonImportance_stratum_ID FOREIGN KEY (stratum_ID)    REFERENCES stratum (STRATUM_ID );

---creating fields ----------------------

ALTER TABLE stemCount ADD COLUMN TAXONIMPORTANCE_ID Integer ;

-- Manual migration SQL------
--taxonObs part to taxonImportance --
INSERT INTO taxonImportance ( taxonObservation_ID, cover, basalArea )
SELECT taxonObservation.TAXONOBSERVATION_ID, taxonObservation.taxonCover, taxonObservation.taxonBasalArea
FROM taxonObservation;
--stemCount new key to taxonImp------
UPDATE stemCount INNER JOIN taxonImportance ON stemCount.taxonObservation_ID = taxonImportance.taxonObservation_ID SET stemCount.TAXONIMPORTANCE_ID = [taxonImportance].[taxonImportance_ID]
WHERE (((taxonImportance.stratum_ID) Is Null));
--manual fix required field on stemCount
ALTER TABLE stemCount ALTER COLUMN TAXONIMPORTANCE_ID Integer NOT NULL;

--now relate, after requiring field--
ALTER TABLE stemCount   ADD CONSTRAINT R1stemCount_TAXONIMPORTANCE_ID FOREIGN KEY (TAXONIMPORTANCE_ID)    REFERENCES taxonImportance (TAXONIMPORTANCE_ID );

-- stratumComp to taxonImportance --
INSERT INTO taxonImportance ( taxonObservation_ID, stratum_ID, cover )
SELECT stratumComposition.TAXONOBSERVATION_ID, stratumComposition.STRATUM_ID, stratumComposition.taxonStratumCover
FROM stratumComposition;

--populate authorPlantName on taxonObservation----
UPDATE taxonObservation INNER JOIN plantName ON taxonObservation.plantName_ID = plantName.PLANTNAME_ID SET taxonObservation.authorPlantName = [plantName].[plantName];


----creating tables------------      

CREATE TABLE embargo
(
embargo_ID serial NOT NULL ,
plot_ID Integer NOT NULL ,
embargoReason text NOT NULL ,
embargoStart Date NOT NULL ,
embargoStop Date NOT NULL ,
defaultStatus Integer NOT NULL ,

PRIMARY KEY ( embargo_ID )
);

ALTER TABLE embargo   ADD CONSTRAINT R1embargo_plot_ID FOREIGN KEY (plot_ID)    REFERENCES plot (PLOT_ID );

CREATE TABLE usr
(
usr_ID serial NOT NULL ,
party_ID Integer NOT NULL ,
password varchar (50) NOT NULL ,
permission_type Integer NOT NULL ,
begin_time Date ,
last_connect Date ,
ticket_count Integer ,
email_address varchar (100) NOT NULL ,
preferred_name varchar (100) ,
remote_address varchar (100) ,

PRIMARY KEY ( usr_ID )
);

ALTER TABLE usr   ADD CONSTRAINT R1user_party_ID FOREIGN KEY (party_ID)    REFERENCES party (PARTY_ID );

CREATE TABLE userCertification
(
userCertification_ID serial NOT NULL ,
usr_ID Integer NOT NULL ,
current_cert_level Integer NOT NULL ,
requested_cert_level Integer NOT NULL ,
highest_degree varchar (50) ,
degree_year varchar (50) ,
degree_institution varchar (50) ,
current_institution varchar (50) ,
current_position varchar (200) ,
esa_certified Boolean ,
prof_experience_doc text ,
relevant_pubs text ,
veg_sampling_doc text ,
veg_analysis_doc text ,
usnvc_experience_doc text ,
vegbank_experience_doc text ,
vegbank_expected_uses text ,
plotdb_experience_doc text ,
esa_sponsor_name_a varchar (120) ,
esa_sponsor_email_a varchar (120) ,
esa_sponsor_name_b varchar (120) ,
esa_sponsor_email_b varchar (120) ,
peer_review Boolean ,
additional_statements text ,
certificationStatus varchar (30) ,
certificationStatusComments text ,

PRIMARY KEY ( userCertification_ID )
);

ALTER TABLE userCertification   ADD CONSTRAINT R1userCertification_usr_ID FOREIGN KEY (usr_ID)    REFERENCES usr (usr_ID );

CREATE TABLE userRegionalExp
(
userRegionalExp_ID serial NOT NULL ,
userCertification_ID Integer NOT NULL ,
region varchar (50) NOT NULL ,
vegetation varchar (50) ,
floristics varchar (50) ,
nvc_ivc varchar (50) ,

PRIMARY KEY ( userRegionalExp_ID )
);

ALTER TABLE userRegionalExp   ADD CONSTRAINT R1userRegionalExp_userCertification_ID FOREIGN KEY (userCertification_ID)    REFERENCES userCertification (userCertification_ID );

CREATE TABLE userDataset
(
userDataset_ID serial NOT NULL ,
usr_ID Integer NOT NULL ,
datasetStart Date ,
datasetStop Date ,
AccessionCode varchar (100) ,
datasetName varchar (100) NOT NULL ,
datasetDescription text ,
datasetType varchar (50) ,
datasetSharing varchar (30) ,
datasetPassword varchar (50) ,

PRIMARY KEY ( userDataset_ID )
);

ALTER TABLE userDataset   ADD CONSTRAINT R1userDataset_usr_ID FOREIGN KEY (usr_ID)    REFERENCES usr (usr_ID );

CREATE TABLE userDatasetItem
(
userDatasetItem_ID serial NOT NULL ,
userDataset_ID Integer NOT NULL ,
ItemAccessionCode varchar (100) NOT NULL ,
itemType varchar (50) ,
itemStart Date NOT NULL ,
itemStop Date ,
notes text ,

PRIMARY KEY ( userDatasetItem_ID )
);

ALTER TABLE userDatasetItem   ADD CONSTRAINT R1userDatasetItem_userDataset_ID FOREIGN KEY (userDataset_ID)    REFERENCES userDataset (userDataset_ID );

CREATE TABLE userNotify
(
userNotify_ID serial NOT NULL ,
usr_ID Integer NOT NULL ,
notifyName varchar (100) ,
notifyDescription text ,
notifyStart Date ,
notifyStop Date ,
lastCheckDate Date ,
notifySQL text ,

PRIMARY KEY ( userNotify_ID )
);

ALTER TABLE userNotify   ADD CONSTRAINT R1userNotify_usr_ID FOREIGN KEY (usr_ID)    REFERENCES usr (usr_ID );

CREATE TABLE userPermission
(
userPermission_ID serial NOT NULL ,
embargo_ID Integer NOT NULL ,
usr_ID Integer NOT NULL ,
permissionStart Date NOT NULL ,
permissionStop Date ,
permissionStatus Integer NOT NULL ,
permissionNotes text ,

PRIMARY KEY ( userPermission_ID )
);

ALTER TABLE userPermission   ADD CONSTRAINT R1userPermission_embargo_ID FOREIGN KEY (embargo_ID)    REFERENCES embargo (embargo_ID );

ALTER TABLE userPermission   ADD CONSTRAINT R2userPermission_usr_ID FOREIGN KEY (usr_ID)    REFERENCES usr (usr_ID );

CREATE TABLE userQuery
(
userQuery_ID serial NOT NULL ,
usr_ID Integer NOT NULL ,
queryStart Date ,
queryStop Date ,
accessionCode varchar (100) ,
queryName varchar (100) ,
queryDescription text ,
querySQL text ,
queryType varchar (50) ,
querySharing varchar (30) ,
queryPassword varchar (50) ,

PRIMARY KEY ( userQuery_ID )
);

ALTER TABLE userQuery   ADD CONSTRAINT R1userQuery_usr_ID FOREIGN KEY (usr_ID)    REFERENCES usr (usr_ID );

CREATE TABLE userPreference
(
userPreference_ID serial NOT NULL ,
usr_ID Integer NOT NULL ,
preferenceName varchar (100) ,
preferenceValue text ,
preferencePriority Float ,
preferenceStart Date ,
preferenceStop Date ,

PRIMARY KEY ( userPreference_ID )
);

ALTER TABLE userPreference   ADD CONSTRAINT R1userPreference_usr_ID FOREIGN KEY (usr_ID)    REFERENCES usr (usr_ID );

---creating fields ----------------------

ALTER TABLE commStatus ADD COLUMN PARTY_ID Integer ;

ALTER TABLE commUsage ADD COLUMN PARTY_ID Integer ;

ALTER TABLE plantStatus ADD COLUMN PARTY_ID Integer ;

ALTER TABLE plantUsage ADD COLUMN PARTY_ID Integer ;

--- manual SQL to migrate extant data:----------

--plantParty to Party---------
INSERT INTO party ( salutation, givenName, middleName, surName, organizationName, contactInstructions )
SELECT plantParty.salutation, plantParty.givenName, plantParty.middleName, plantParty.surName, plantParty.organizationName, plantParty.contactInstructions
FROM plantParty;

--plantStatus update to party_ID--
UPDATE plantStatus SET plantStatus.PARTY_ID = 

(SELECT Min(party.PARTY_ID) 
FROM plantParty, party
   
WHERE 
 (
  (((plantParty.salutation)=(party.salutation)) or ((plantParty.salutation is null) and (party.salutation is null))) 
  AND 
  (((plantParty.givenName)=(party.givenName)) or ((plantParty.givenName is null) and (party.givenName is null))) 
  AND 
  (  ((plantParty.middleName)=(party.middleName)) or ((plantParty.middleName is null) and (party.middleName is null)) )
  AND 
  (  ((plantParty.surName)  =  (party.surName)) or ((plantParty.surName is null) and (party.surName is null)) )
  AND
  (  ((plantParty.organizationName)=(party.organizationName)) or ((plantParty.organizationName is null) and (party.organizationName is null)) )
  AND
  (  ((plantParty.contactInstructions)=(party.contactInstructions)) or ((plantParty.contactInstructions is null) and (party.contactInstructions is null)) )
  AND
  ( plantStatus.plantParty_ID =plantParty.plantParty_ID )
 )
);

--plantUsage update to party_ID ---
UPDATE plantUsage SET plantUsage.PARTY_ID = 

(SELECT Min(party.PARTY_ID) 
FROM plantParty, party
   
WHERE 
 (
  (((plantParty.salutation)=(party.salutation)) or ((plantParty.salutation is null) and (party.salutation is null))) 
  AND 
  (((plantParty.givenName)=(party.givenName)) or ((plantParty.givenName is null) and (party.givenName is null))) 
  AND 
  (  ((plantParty.middleName)=(party.middleName)) or ((plantParty.middleName is null) and (party.middleName is null)) )
  AND 
  (  ((plantParty.surName)  =  (party.surName)) or ((plantParty.surName is null) and (party.surName is null)) )
  AND
  (  ((plantParty.organizationName)=(party.organizationName)) or ((plantParty.organizationName is null) and (party.organizationName is null)) )
  AND
  (  ((plantParty.contactInstructions)=(party.contactInstructions)) or ((plantParty.contactInstructions is null) and (party.contactInstructions is null)) )
  AND
  ( plantUsage.plantParty_ID =plantParty.plantParty_ID )
 )
);


--commParty to Party---------
INSERT INTO party ( salutation, givenName, middleName, surName, organizationName, contactInstructions )
SELECT commParty.salutation, commParty.givenName, commParty.middleName, commParty.surName, commParty.organizationName, commParty.contactInstructions
FROM commParty;

--commStatus update to party_ID--
UPDATE commStatus SET commStatus.PARTY_ID = 

(SELECT Min(party.PARTY_ID) 
FROM commParty, party
   
WHERE 
 (
  (((commParty.salutation)=(party.salutation)) or ((commParty.salutation is null) and (party.salutation is null))) 
  AND 
  (((commParty.givenName)=(party.givenName)) or ((commParty.givenName is null) and (party.givenName is null))) 
  AND 
  (  ((commParty.middleName)=(party.middleName)) or ((commParty.middleName is null) and (party.middleName is null)) )
  AND 
  (  ((commParty.surName)  =  (party.surName)) or ((commParty.surName is null) and (party.surName is null)) )
  AND
  (  ((commParty.organizationName)=(party.organizationName)) or ((commParty.organizationName is null) and (party.organizationName is null)) )
  AND
  (  ((commParty.contactInstructions)=(party.contactInstructions)) or ((commParty.contactInstructions is null) and (party.contactInstructions is null)) )
  AND
  ( commStatus.commParty_ID =commParty.commParty_ID )
 )
);
--commUsage update to party_ID ---
UPDATE commUsage SET commUsage.PARTY_ID = 

(SELECT Min(party.PARTY_ID) 
FROM commParty, party
   
WHERE 
 (
  (((commParty.salutation)=(party.salutation)) or ((commParty.salutation is null) and (party.salutation is null))) 
  AND 
  (((commParty.givenName)=(party.givenName)) or ((commParty.givenName is null) and (party.givenName is null))) 
  AND 
  (  ((commParty.middleName)=(party.middleName)) or ((commParty.middleName is null) and (party.middleName is null)) )
  AND 
  (  ((commParty.surName)  =  (party.surName)) or ((commParty.surName is null) and (party.surName is null)) )
  AND
  (  ((commParty.organizationName)=(party.organizationName)) or ((commParty.organizationName is null) and (party.organizationName is null)) )
  AND
  (  ((commParty.contactInstructions)=(party.contactInstructions)) or ((commParty.contactInstructions is null) and (party.contactInstructions is null)) )
  AND
  ( commUsage.commParty_ID =commParty.commParty_ID )
 )
);

--manual require all party IDs on plant and commtables--
ALTER TABLE plantStatus ALTER COLUMN PARTY_ID Integer NOT NULL;
ALTER TABLE plantUsage ALTER COLUMN PARTY_ID Integer NOT NULL;
ALTER TABLE commStatus ALTER COLUMN PARTY_ID Integer NOT NULL;
ALTER TABLE commUsage ALTER COLUMN PARTY_ID Integer NOT NULL;

--manual now relate, after updating to not null---
ALTER TABLE commStatus   ADD CONSTRAINT R1commStatus_PARTY_ID FOREIGN KEY (PARTY_ID)    REFERENCES party (PARTY_ID );

ALTER TABLE commUsage   ADD CONSTRAINT R1commUsage_PARTY_ID FOREIGN KEY (PARTY_ID)    REFERENCES party (PARTY_ID );

ALTER TABLE plantStatus   ADD CONSTRAINT R1plantStatus_PARTY_ID FOREIGN KEY (PARTY_ID)    REFERENCES party (PARTY_ID );

ALTER TABLE plantUsage   ADD CONSTRAINT R1plantUsage_PARTY_ID FOREIGN KEY (PARTY_ID)    REFERENCES party (PARTY_ID );


---creating fields ----------------------

ALTER TABLE party ADD COLUMN partyType varchar (40) ;

----creating tables------------      

CREATE TABLE userRecordOwner
(
userRecordOwner_ID serial NOT NULL ,
usr_ID Integer NOT NULL ,
tableName varchar (50) NOT NULL ,
tableRecord Integer NOT NULL ,
RecordCreationDate Date NOT NULL ,
OwnerStart Date NOT NULL ,
OwnerStop Date ,
OwnerType varchar (30) NOT NULL ,

PRIMARY KEY ( userRecordOwner_ID )
);

ALTER TABLE userRecordOwner   ADD CONSTRAINT R1userRecordOwner_usr_ID FOREIGN KEY (usr_ID)    REFERENCES usr (usr_ID );
----creating tables------------      

CREATE TABLE partyMember
(
partyMember_ID serial NOT NULL ,
parentParty_ID Integer NOT NULL ,
childParty_ID Integer NOT NULL ,
role_ID Integer ,
memberStart Date NOT NULL ,
memberStop Date ,

PRIMARY KEY ( partyMember_ID )
);

ALTER TABLE partyMember   ADD CONSTRAINT R1partyMember_parentParty_ID FOREIGN KEY (parentParty_ID)    REFERENCES party (PARTY_ID );

ALTER TABLE partyMember   ADD CONSTRAINT R2partyMember_childParty_ID FOREIGN KEY (childParty_ID)    REFERENCES party (PARTY_ID );

ALTER TABLE partyMember   ADD CONSTRAINT R3partyMember_role_ID FOREIGN KEY (role_ID)    REFERENCES aux_role (role_ID );

---creating fields ----------------------

ALTER TABLE plot ADD COLUMN layoutNarrative text ;

--manual fix on plot table ---

UPDATE plot SET plot.layoutNarrative = plot.layoutNarative;


---altering fields ----------------------
--manual!-not long enough, this field---

ALTER TABLE soilObs ALTER COLUMN  soilTexture varchar (50);

--manual: dont require attribute in notelink:---
ALTER TABLE noteLink ALTER COLUMN attributeName varchar (50) DROP NOT NULL ;

---creating fields ----------------------

ALTER TABLE observation ADD COLUMN accessionCode varchar (100) ;

ALTER TABLE plot ADD COLUMN accessionCode varchar (100) ;

ALTER TABLE referenceJournal ADD COLUMN accessionCode varchar (100) ;

ALTER TABLE stratumMethod ADD COLUMN accessionCode varchar (100) ;

ALTER TABLE coverMethod ADD COLUMN accessionCode varchar (100) ;

ALTER TABLE taxonObservation ADD COLUMN accessionCode varchar (100) ;

ALTER TABLE project ADD COLUMN accessionCode varchar (100) ;

ALTER TABLE namedPlace ADD COLUMN accessionCode varchar (100) ;

ALTER TABLE soilTaxon ADD COLUMN accessionCode varchar (100) ;

ALTER TABLE commClass ADD COLUMN accessionCode varchar (100) ;

ALTER TABLE userDefined ADD COLUMN accessionCode varchar (100) ;

ALTER TABLE referenceParty ADD COLUMN accessionCode varchar (100) ;


---manual move of accessionCode fields on plot and obs ------
UPDATE observation SET accessionCode = obsaccessionnumber ;
UPDATE plot SET accessionCode = accession_number ;

---creating fields ----------------------

ALTER TABLE projectContributor ADD COLUMN cheatRole varchar (50) ;

ALTER TABLE plot ADD COLUMN stateProvince varchar (55) ;

ALTER TABLE definedValue ADD COLUMN definedValue text ;

--manual update of fields
UPDATE projectContributor SET cheatRole = role;
UPDATE plot SET stateProvince = state;
UPDATE definedValue SET definedValue = value ;

--manual require definedvalue.definedvalue--
ALTER TABLE definedValue ALTER COLUMN definedValue text NOT NULL;


--------------------------------------
----- DROP ALL TRIGGERS ---------------
--------------------------------------

--manual to address problems with dropping things: drop all relations:
DELETE FROM  pg_trigger WHERE tgisconstraint='t';




-------dropping fields -------------------

ALTER TABLE plot DROP COLUMN layoutNarative  ;

-------dropping fields -------------------

ALTER TABLE stemCount DROP COLUMN TAXONOBSERVATION_ID  ;

ALTER TABLE taxonObservation DROP COLUMN taxonCollection  ;

ALTER TABLE taxonObservation DROP COLUMN taxonCover  ;

ALTER TABLE taxonObservation DROP COLUMN taxonBasalArea  ;

ALTER TABLE taxonObservation DROP COLUMN cheatplantname  ;

ALTER TABLE taxonObservation DROP COLUMN PLANTNAME_ID  ;

-------dropping tables----------------

DROP TABLE stratumComposition ;

-------dropping fields -------------------

ALTER TABLE commStatus DROP COLUMN commPARTY_ID  ;

ALTER TABLE commUsage DROP COLUMN commPARTY_ID  ;

ALTER TABLE plantStatus DROP COLUMN plantPARTY_ID  ;

ALTER TABLE plantUsage DROP COLUMN plantPARTY_ID  ;

-------dropping tables----------------

DROP TABLE plantParty ;

DROP TABLE commParty ;

-------dropping fields -------------------

ALTER TABLE party DROP COLUMN owner_ID  ;

-------dropping fields -------------------

ALTER TABLE plot DROP COLUMN accession_number  ;

ALTER TABLE observation DROP COLUMN obsaccessionnumber  ;

-------dropping fields -------------------

ALTER TABLE projectContributor DROP COLUMN role  ;

ALTER TABLE plot DROP COLUMN state  ;

ALTER TABLE definedValue DROP COLUMN value  ;



----  manual, add back in relationships from db_model_vegbank.xml

--------------RELATIONSHIPS ----------------------------------------

ALTER TABLE commConcept   ADD CONSTRAINT R1commConcept_COMMNAME_ID FOREIGN KEY (COMMNAME_ID)    REFERENCES commName (COMMNAME_ID );

ALTER TABLE commConcept   ADD CONSTRAINT R2commConcept_reference_ID FOREIGN KEY (reference_ID)    REFERENCES reference (reference_ID );

ALTER TABLE commCorrelation   ADD CONSTRAINT R1commCorrelation_COMMSTATUS_ID FOREIGN KEY (COMMSTATUS_ID)    REFERENCES commStatus (COMMSTATUS_ID );

ALTER TABLE commCorrelation   ADD CONSTRAINT R2commCorrelation_COMMCONCEPT_ID FOREIGN KEY (COMMCONCEPT_ID)    REFERENCES commConcept (COMMCONCEPT_ID );

ALTER TABLE commLineage   ADD CONSTRAINT R1commLineage_parentCommStatus_ID FOREIGN KEY (parentCommStatus_ID)    REFERENCES commStatus (COMMSTATUS_ID );

ALTER TABLE commLineage   ADD CONSTRAINT R2commLineage_childCommStatus_ID FOREIGN KEY (childCommStatus_ID)    REFERENCES commStatus (COMMSTATUS_ID );

ALTER TABLE commName   ADD CONSTRAINT R1commName_reference_ID FOREIGN KEY (reference_ID)    REFERENCES reference (reference_ID );

ALTER TABLE commStatus   ADD CONSTRAINT R1commStatus_COMMCONCEPT_ID FOREIGN KEY (COMMCONCEPT_ID)    REFERENCES commConcept (COMMCONCEPT_ID );

ALTER TABLE commStatus   ADD CONSTRAINT R2commStatus_reference_ID FOREIGN KEY (reference_ID)    REFERENCES reference (reference_ID );

ALTER TABLE commStatus   ADD CONSTRAINT R3commStatus_commParent_ID FOREIGN KEY (commParent_ID)    REFERENCES commConcept (COMMCONCEPT_ID );

ALTER TABLE commStatus   ADD CONSTRAINT R4commStatus_PARTY_ID FOREIGN KEY (PARTY_ID)    REFERENCES party (PARTY_ID );

ALTER TABLE commUsage   ADD CONSTRAINT R1commUsage_COMMNAME_ID FOREIGN KEY (COMMNAME_ID)    REFERENCES commName (COMMNAME_ID );

ALTER TABLE commUsage   ADD CONSTRAINT R2commUsage_COMMCONCEPT_ID FOREIGN KEY (COMMCONCEPT_ID)    REFERENCES commConcept (COMMCONCEPT_ID );

ALTER TABLE commUsage   ADD CONSTRAINT R3commUsage_PARTY_ID FOREIGN KEY (PARTY_ID)    REFERENCES party (PARTY_ID );

ALTER TABLE plantConcept   ADD CONSTRAINT R1plantConcept_PLANTNAME_ID FOREIGN KEY (PLANTNAME_ID)    REFERENCES plantName (PLANTNAME_ID );

ALTER TABLE plantConcept   ADD CONSTRAINT R2plantConcept_reference_ID FOREIGN KEY (reference_ID)    REFERENCES reference (reference_ID );

ALTER TABLE plantCorrelation   ADD CONSTRAINT R1plantCorrelation_PLANTSTATUS_ID FOREIGN KEY (PLANTSTATUS_ID)    REFERENCES plantStatus (PLANTSTATUS_ID );

ALTER TABLE plantCorrelation   ADD CONSTRAINT R2plantCorrelation_PLANTCONCEPT_ID FOREIGN KEY (PLANTCONCEPT_ID)    REFERENCES plantConcept (PLANTCONCEPT_ID );

ALTER TABLE plantLineage   ADD CONSTRAINT R1plantLineage_childPlantStatus_ID FOREIGN KEY (childPlantStatus_ID)    REFERENCES plantStatus (PLANTSTATUS_ID );

ALTER TABLE plantLineage   ADD CONSTRAINT R2plantLineage_parentPlantStatus_ID FOREIGN KEY (parentPlantStatus_ID)    REFERENCES plantStatus (PLANTSTATUS_ID );

ALTER TABLE plantName   ADD CONSTRAINT R1plantName_reference_ID FOREIGN KEY (reference_ID)    REFERENCES reference (reference_ID );

ALTER TABLE plantStatus   ADD CONSTRAINT R1plantStatus_PLANTCONCEPT_ID FOREIGN KEY (PLANTCONCEPT_ID)    REFERENCES plantConcept (PLANTCONCEPT_ID );

ALTER TABLE plantStatus   ADD CONSTRAINT R2plantStatus_reference_ID FOREIGN KEY (reference_ID)    REFERENCES reference (reference_ID );

ALTER TABLE plantStatus   ADD CONSTRAINT R3plantStatus_plantParent_ID FOREIGN KEY (plantParent_ID)    REFERENCES plantConcept (PLANTCONCEPT_ID );

ALTER TABLE plantStatus   ADD CONSTRAINT R4plantStatus_PARTY_ID FOREIGN KEY (PARTY_ID)    REFERENCES party (PARTY_ID );

ALTER TABLE plantUsage   ADD CONSTRAINT R1plantUsage_PLANTNAME_ID FOREIGN KEY (PLANTNAME_ID)    REFERENCES plantName (PLANTNAME_ID );

ALTER TABLE plantUsage   ADD CONSTRAINT R2plantUsage_PLANTCONCEPT_ID FOREIGN KEY (PLANTCONCEPT_ID)    REFERENCES plantConcept (PLANTCONCEPT_ID );

ALTER TABLE plantUsage   ADD CONSTRAINT R3plantUsage_PARTY_ID FOREIGN KEY (PARTY_ID)    REFERENCES party (PARTY_ID );

ALTER TABLE address   ADD CONSTRAINT R1address_party_ID FOREIGN KEY (party_ID)    REFERENCES party (PARTY_ID );

ALTER TABLE address   ADD CONSTRAINT R2address_organization_ID FOREIGN KEY (organization_ID)    REFERENCES party (PARTY_ID );

ALTER TABLE reference   ADD CONSTRAINT R1reference_referenceJournal_ID FOREIGN KEY (referenceJournal_ID)    REFERENCES referenceJournal (referenceJournal_ID );

ALTER TABLE referenceAltIdent   ADD CONSTRAINT R1referenceAltIdent_reference_ID FOREIGN KEY (reference_ID)    REFERENCES reference (reference_ID );

ALTER TABLE referenceContributor   ADD CONSTRAINT R1referenceContributor_reference_ID FOREIGN KEY (reference_ID)    REFERENCES reference (reference_ID );

ALTER TABLE referenceContributor   ADD CONSTRAINT R2referenceContributor_referenceParty_ID FOREIGN KEY (referenceParty_ID)    REFERENCES referenceParty (referenceParty_ID );

ALTER TABLE referenceParty   ADD CONSTRAINT R1referenceParty_currentParty_ID FOREIGN KEY (currentParty_ID)    REFERENCES referenceParty (referenceParty_ID );

ALTER TABLE classContributor   ADD CONSTRAINT R1classContributor_COMMCLASS_ID FOREIGN KEY (COMMCLASS_ID)    REFERENCES commClass (COMMCLASS_ID );

ALTER TABLE classContributor   ADD CONSTRAINT R2classContributor_PARTY_ID FOREIGN KEY (PARTY_ID)    REFERENCES party (PARTY_ID );

ALTER TABLE classContributor   ADD CONSTRAINT R3classContributor_ROLE_ID FOREIGN KEY (ROLE_ID)    REFERENCES aux_Role (ROLE_ID );

ALTER TABLE commClass   ADD CONSTRAINT R1commClass_OBSERVATION_ID FOREIGN KEY (OBSERVATION_ID)    REFERENCES observation (OBSERVATION_ID );

ALTER TABLE commClass   ADD CONSTRAINT R2commClass_classPublication_ID FOREIGN KEY (classPublication_ID)    REFERENCES reference (reference_ID );

ALTER TABLE commInterpretation   ADD CONSTRAINT R1commInterpretation_COMMCLASS_ID FOREIGN KEY (COMMCLASS_ID)    REFERENCES commClass (COMMCLASS_ID );

ALTER TABLE commInterpretation   ADD CONSTRAINT R2commInterpretation_COMMCONCEPT_ID FOREIGN KEY (COMMCONCEPT_ID)    REFERENCES commConcept (COMMCONCEPT_ID );

ALTER TABLE commInterpretation   ADD CONSTRAINT R3commInterpretation_commAuthority_ID FOREIGN KEY (commAuthority_ID)    REFERENCES reference (reference_ID );

ALTER TABLE coverIndex   ADD CONSTRAINT R1coverIndex_COVERMETHOD_ID FOREIGN KEY (COVERMETHOD_ID)    REFERENCES coverMethod (COVERMETHOD_ID );

ALTER TABLE coverMethod   ADD CONSTRAINT R1coverMethod_reference_ID FOREIGN KEY (reference_ID)    REFERENCES reference (reference_ID );

ALTER TABLE definedValue   ADD CONSTRAINT R1definedValue_USERDEFINED_ID FOREIGN KEY (USERDEFINED_ID)    REFERENCES userDefined (USERDEFINED_ID );

ALTER TABLE disturbanceObs   ADD CONSTRAINT R1disturbanceObs_OBSERVATION_ID FOREIGN KEY (OBSERVATION_ID)    REFERENCES observation (OBSERVATION_ID );

ALTER TABLE graphic   ADD CONSTRAINT R1graphic_OBSERVATION_ID FOREIGN KEY (OBSERVATION_ID)    REFERENCES observation (OBSERVATION_ID );

ALTER TABLE namedPlace   ADD CONSTRAINT R1namedPlace_reference_ID FOREIGN KEY (reference_ID)    REFERENCES reference (reference_ID );

ALTER TABLE note   ADD CONSTRAINT R1note_NOTELINK_ID FOREIGN KEY (NOTELINK_ID)    REFERENCES noteLink (NOTELINK_ID );

ALTER TABLE note   ADD CONSTRAINT R2note_PARTY_ID FOREIGN KEY (PARTY_ID)    REFERENCES party (PARTY_ID );

ALTER TABLE note   ADD CONSTRAINT R3note_ROLE_ID FOREIGN KEY (ROLE_ID)    REFERENCES aux_Role (ROLE_ID );

ALTER TABLE observation   ADD CONSTRAINT R1observation_PREVIOUSOBS_ID FOREIGN KEY (PREVIOUSOBS_ID)    REFERENCES observation (OBSERVATION_ID );

ALTER TABLE observation   ADD CONSTRAINT R2observation_PLOT_ID FOREIGN KEY (PLOT_ID)    REFERENCES plot (PLOT_ID );

ALTER TABLE observation   ADD CONSTRAINT R3observation_PROJECT_ID FOREIGN KEY (PROJECT_ID)    REFERENCES project (PROJECT_ID );

ALTER TABLE observation   ADD CONSTRAINT R4observation_COVERMETHOD_ID FOREIGN KEY (COVERMETHOD_ID)    REFERENCES coverMethod (coverMethod_ID );

ALTER TABLE observation   ADD CONSTRAINT R5observation_STRATUMMETHOD_ID FOREIGN KEY (STRATUMMETHOD_ID)    REFERENCES stratumMethod (stratumMethod_ID );

ALTER TABLE observation   ADD CONSTRAINT R6observation_SOILTAXON_ID FOREIGN KEY (SOILTAXON_ID)    REFERENCES soilTaxon (soilTaxon_ID );

ALTER TABLE observationContributor   ADD CONSTRAINT R1observationContributor_OBSERVATION_ID FOREIGN KEY (OBSERVATION_ID)    REFERENCES observation (OBSERVATION_ID );

ALTER TABLE observationContributor   ADD CONSTRAINT R2observationContributor_PARTY_ID FOREIGN KEY (PARTY_ID)    REFERENCES party (PARTY_ID );

ALTER TABLE observationContributor   ADD CONSTRAINT R3observationContributor_ROLE_ID FOREIGN KEY (ROLE_ID)    REFERENCES aux_Role (ROLE_ID );

ALTER TABLE observationSynonym   ADD CONSTRAINT R1observationSynonym_synonymObservation_ID FOREIGN KEY (synonymObservation_ID)    REFERENCES observation (OBSERVATION_ID );

ALTER TABLE observationSynonym   ADD CONSTRAINT R2observationSynonym_primaryObservation_ID FOREIGN KEY (primaryObservation_ID)    REFERENCES observation (OBSERVATION_ID );

ALTER TABLE observationSynonym   ADD CONSTRAINT R3observationSynonym_PARTY_ID FOREIGN KEY (PARTY_ID)    REFERENCES party (PARTY_ID );

ALTER TABLE observationSynonym   ADD CONSTRAINT R4observationSynonym_ROLE_ID FOREIGN KEY (ROLE_ID)    REFERENCES aux_Role (ROLE_ID );

ALTER TABLE party   ADD CONSTRAINT R1party_currentName_ID FOREIGN KEY (currentName_ID)    REFERENCES party (PARTY_ID );

ALTER TABLE partyMember   ADD CONSTRAINT R1partyMember_parentParty_ID FOREIGN KEY (parentParty_ID)    REFERENCES party (PARTY_ID );

ALTER TABLE partyMember   ADD CONSTRAINT R2partyMember_childParty_ID FOREIGN KEY (childParty_ID)    REFERENCES party (PARTY_ID );

ALTER TABLE partyMember   ADD CONSTRAINT R3partyMember_role_ID FOREIGN KEY (role_ID)    REFERENCES aux_role (role_ID );

ALTER TABLE place   ADD CONSTRAINT R1place_PLOT_ID FOREIGN KEY (PLOT_ID)    REFERENCES plot (PLOT_ID );

ALTER TABLE place   ADD CONSTRAINT R2place_NAMEDPLACE_ID FOREIGN KEY (NAMEDPLACE_ID)    REFERENCES namedPlace (NAMEDPLACE_ID );

ALTER TABLE plot   ADD CONSTRAINT R1plot_reference_ID FOREIGN KEY (reference_ID)    REFERENCES reference (reference_ID );

ALTER TABLE plot   ADD CONSTRAINT R2plot_PARENT_ID FOREIGN KEY (PARENT_ID)    REFERENCES plot (PLOT_ID );

ALTER TABLE projectContributor   ADD CONSTRAINT R1projectContributor_PROJECT_ID FOREIGN KEY (PROJECT_ID)    REFERENCES project (PROJECT_ID );

ALTER TABLE projectContributor   ADD CONSTRAINT R2projectContributor_PARTY_ID FOREIGN KEY (PARTY_ID)    REFERENCES party (PARTY_ID );

ALTER TABLE projectContributor   ADD CONSTRAINT R3projectContributor_ROLE_ID FOREIGN KEY (ROLE_ID)    REFERENCES aux_Role (ROLE_ID );

ALTER TABLE revision   ADD CONSTRAINT R1revision_previousRevision_ID FOREIGN KEY (previousRevision_ID)    REFERENCES revision (REVISION_ID );

ALTER TABLE soilObs   ADD CONSTRAINT R1soilObs_OBSERVATION_ID FOREIGN KEY (OBSERVATION_ID)    REFERENCES observation (observation_ID );

ALTER TABLE soilTaxon   ADD CONSTRAINT R1soilTaxon_SOILPARENT_ID FOREIGN KEY (SOILPARENT_ID)    REFERENCES soilTaxon (SOILTAXON_ID );

ALTER TABLE stemCount   ADD CONSTRAINT R1stemCount_TAXONIMPORTANCE_ID FOREIGN KEY (TAXONIMPORTANCE_ID)    REFERENCES taxonImportance (TAXONIMPORTANCE_ID );

ALTER TABLE stemLocation   ADD CONSTRAINT R1stemLocation_STEMCOUNT_ID FOREIGN KEY (STEMCOUNT_ID)    REFERENCES stemCount (STEMCOUNT_ID );

ALTER TABLE stratum   ADD CONSTRAINT R1stratum_OBSERVATION_ID FOREIGN KEY (OBSERVATION_ID)    REFERENCES observation (observation_ID );

ALTER TABLE stratum   ADD CONSTRAINT R2stratum_STRATUMTYPE_ID FOREIGN KEY (STRATUMTYPE_ID)    REFERENCES stratumType (STRATUMTYPE_ID );

ALTER TABLE stratum   ADD CONSTRAINT R3stratum_STRATUMMETHOD_ID FOREIGN KEY (STRATUMMETHOD_ID)    REFERENCES stratumMethod (STRATUMMETHOD_ID );

ALTER TABLE stratumMethod   ADD CONSTRAINT R1stratumMethod_reference_ID FOREIGN KEY (reference_ID)    REFERENCES reference (reference_ID );

ALTER TABLE stratumType   ADD CONSTRAINT R1stratumType_STRATUMMETHOD_ID FOREIGN KEY (STRATUMMETHOD_ID)    REFERENCES stratumMethod (STRATUMMETHOD_ID );

ALTER TABLE taxonImportance   ADD CONSTRAINT R1taxonImportance_taxonObservation_ID FOREIGN KEY (taxonObservation_ID)    REFERENCES taxonObservation (taxonObservation_ID );

ALTER TABLE taxonImportance   ADD CONSTRAINT R2taxonImportance_stratum_ID FOREIGN KEY (stratum_ID)    REFERENCES stratum (STRATUM_ID );

ALTER TABLE taxonInterpretation   ADD CONSTRAINT R1taxonInterpretation_TAXONOBSERVATION_ID FOREIGN KEY (TAXONOBSERVATION_ID)    REFERENCES taxonObservation (TAXONOBSERVATION_ID );

ALTER TABLE taxonInterpretation   ADD CONSTRAINT R2taxonInterpretation_stemLocation_ID FOREIGN KEY (stemLocation_ID)    REFERENCES stemLocation (stemLocation_ID );

ALTER TABLE taxonInterpretation   ADD CONSTRAINT R3taxonInterpretation_PLANTCONCEPT_ID FOREIGN KEY (PLANTCONCEPT_ID)    REFERENCES plantConcept (PLANTCONCEPT_ID );

ALTER TABLE taxonInterpretation   ADD CONSTRAINT R4taxonInterpretation_PLANTNAME_ID FOREIGN KEY (PLANTNAME_ID)    REFERENCES plantName (PLANTNAME_ID );

ALTER TABLE taxonInterpretation   ADD CONSTRAINT R5taxonInterpretation_PARTY_ID FOREIGN KEY (PARTY_ID)    REFERENCES party (PARTY_ID );

ALTER TABLE taxonInterpretation   ADD CONSTRAINT R6taxonInterpretation_ROLE_ID FOREIGN KEY (ROLE_ID)    REFERENCES aux_Role (ROLE_ID );

ALTER TABLE taxonInterpretation   ADD CONSTRAINT R7taxonInterpretation_reference_ID FOREIGN KEY (reference_ID)    REFERENCES reference (reference_ID );

ALTER TABLE taxonInterpretation   ADD CONSTRAINT R8taxonInterpretation_collector_ID FOREIGN KEY (collector_ID)    REFERENCES party (party_ID );

ALTER TABLE taxonInterpretation   ADD CONSTRAINT R9taxonInterpretation_museum_ID FOREIGN KEY (museum_ID)    REFERENCES party (party_ID );

ALTER TABLE taxonObservation   ADD CONSTRAINT R1taxonObservation_OBSERVATION_ID FOREIGN KEY (OBSERVATION_ID)    REFERENCES observation (observation_ID );

ALTER TABLE taxonObservation   ADD CONSTRAINT R2taxonObservation_reference_ID FOREIGN KEY (reference_ID)    REFERENCES reference (reference_ID );

ALTER TABLE taxonAlt   ADD CONSTRAINT R1taxonAlt_taxonInterpretation_ID FOREIGN KEY (taxonInterpretation_ID)    REFERENCES taxonInterpretation (taxonInterpretation_ID );

ALTER TABLE taxonAlt   ADD CONSTRAINT R2taxonAlt_plantConcept_ID FOREIGN KEY (plantConcept_ID)    REFERENCES plantConcept (plantConcept_ID );

ALTER TABLE telephone   ADD CONSTRAINT R1telephone_PARTY_ID FOREIGN KEY (PARTY_ID)    REFERENCES party (PARTY_ID );

ALTER TABLE embargo   ADD CONSTRAINT R1embargo_plot_ID FOREIGN KEY (plot_ID)    REFERENCES plot (PLOT_ID );

ALTER TABLE usr   ADD CONSTRAINT R1usr_party_ID FOREIGN KEY (party_ID)    REFERENCES party (PARTY_ID );

ALTER TABLE userCertification   ADD CONSTRAINT R1userCertification_usr_ID FOREIGN KEY (usr_ID)    REFERENCES usr (usr_ID );

ALTER TABLE userRegionalExp   ADD CONSTRAINT R1userRegionalExp_userCertification_ID FOREIGN KEY (userCertification_ID)    REFERENCES userCertification (userCertification_ID );

ALTER TABLE userDataset   ADD CONSTRAINT R1userDataset_usr_ID FOREIGN KEY (usr_ID)    REFERENCES usr (usr_ID );

ALTER TABLE userDatasetItem   ADD CONSTRAINT R1userDatasetItem_userDataset_ID FOREIGN KEY (userDataset_ID)    REFERENCES userDataset (userDataset_ID );

ALTER TABLE userNotify   ADD CONSTRAINT R1userNotify_usr_ID FOREIGN KEY (usr_ID)    REFERENCES usr (usr_ID );

ALTER TABLE userPermission   ADD CONSTRAINT R1userPermission_embargo_ID FOREIGN KEY (embargo_ID)    REFERENCES embargo (embargo_ID );

ALTER TABLE userPermission   ADD CONSTRAINT R2userPermission_usr_ID FOREIGN KEY (usr_ID)    REFERENCES usr (usr_ID );

ALTER TABLE userQuery   ADD CONSTRAINT R1userQuery_usr_ID FOREIGN KEY (usr_ID)    REFERENCES usr (usr_ID );

ALTER TABLE userPreference   ADD CONSTRAINT R1userPreference_usr_ID FOREIGN KEY (usr_ID)    REFERENCES usr (usr_ID );

ALTER TABLE userRecordOwner   ADD CONSTRAINT R1userRecordOwner_usr_ID FOREIGN KEY (usr_ID)    REFERENCES usr (usr_ID );

