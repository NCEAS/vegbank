

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
UPDATE stemCount SET TAXONIMPORTANCE_ID = (SELECT min(taxonImportance.taxonImportance_ID)
 FROM  taxonImportance  WHERE stemCount.taxonObservation_ID = taxonImportance.taxonObservation_ID and ((taxonImportance.stratum_ID) Is Null));
--manual fix required field on stemCount
ALTER TABLE stemCount ALTER COLUMN TAXONIMPORTANCE_ID SET NOT NULL;

--now relate, after requiring field--
ALTER TABLE stemCount   ADD CONSTRAINT R1stemCount_TAXONIMPORTANCE_ID FOREIGN KEY (TAXONIMPORTANCE_ID)    REFERENCES taxonImportance (TAXONIMPORTANCE_ID );

-- stratumComp to taxonImportance --
INSERT INTO taxonImportance ( taxonObservation_ID, stratum_ID, cover )
SELECT stratumComposition.TAXONOBSERVATION_ID, stratumComposition.STRATUM_ID, stratumComposition.taxonStratumCover
FROM stratumComposition;

--populate authorPlantName on taxonObservation----
UPDATE taxonObservation SET authorPlantName = (SELECT (plantName.plantName) FROM plantName WHERE taxonObservation.plantName_ID = plantName.PLANTNAME_ID) ;


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
UPDATE plantStatus SET PARTY_ID = 

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
UPDATE plantUsage SET PARTY_ID = 

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
UPDATE commStatus SET PARTY_ID = 

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
UPDATE commUsage SET PARTY_ID = 

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
ALTER TABLE plantStatus ALTER COLUMN PARTY_ID SET NOT NULL;
ALTER TABLE plantUsage ALTER COLUMN PARTY_ID SET NOT NULL;
ALTER TABLE commStatus ALTER COLUMN PARTY_ID SET NOT NULL;
ALTER TABLE commUsage ALTER COLUMN PARTY_ID SET NOT NULL;

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

UPDATE plot SET layoutNarrative = layoutNarative;


---altering fields ----------------------
--manual!-not long enough, this field---

ALTER TABLE soilObs ADD COLUMN  soilTexture_temp varchar (50);
UPDATE soilObs SET soilTexture_temp=soilTexture;
ALTER TABLE soilObs DROP COLUMN soilTexture;
ALTER TABLE soilObs RENAME COLUMN soilTexture_temp TO soilTexture;


--manual: dont require attribute in notelink:---
ALTER TABLE noteLink ALTER COLUMN attributeName  DROP NOT NULL ;

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
ALTER TABLE definedValue ALTER COLUMN definedValue SET NOT NULL;

---creating fields ----------------------

ALTER TABLE commUsage ADD COLUMN COMMSTATUS_ID Integer ;

ALTER TABLE commUsage   ADD CONSTRAINT R1commUsage_COMMSTATUS_ID FOREIGN KEY (COMMSTATUS_ID)    REFERENCES commStatus (COMMSTATUS_ID );

ALTER TABLE plantUsage ADD COLUMN PLANTSTATUS_ID Integer ;

ALTER TABLE plantUsage   ADD CONSTRAINT R1plantUsage_PLANTSTATUS_ID FOREIGN KEY (PLANTSTATUS_ID)    REFERENCES plantStatus (PLANTSTATUS_ID );



