
---------------------CREATE TABLES ----------------------------

CREATE TABLE commConcept
(
COMMCONCEPT_ID serial ,
COMMNAME_ID Integer NOT NULL ,
reference_ID Integer ,
commDescription text ,
accessionCode varchar (100) ,

PRIMARY KEY ( COMMCONCEPT_ID )
);

CREATE TABLE commCorrelation
(
COMMCORRELATION_ID serial ,
COMMSTATUS_ID Integer NOT NULL ,
COMMCONCEPT_ID Integer NOT NULL ,
commConvergence varchar (20) NOT NULL ,
correlationStart Date NOT NULL ,
correlationStop Date ,

PRIMARY KEY ( COMMCORRELATION_ID )
);

CREATE TABLE commLineage
(
COMMLINEAGE_ID serial ,
parentCommStatus_ID Integer NOT NULL ,
childCommStatus_ID Integer NOT NULL ,

PRIMARY KEY ( COMMLINEAGE_ID )
);

CREATE TABLE commName
(
COMMNAME_ID serial ,
commName text NOT NULL ,
reference_ID Integer ,
dateEntered Date ,

PRIMARY KEY ( COMMNAME_ID )
);

CREATE TABLE commStatus
(
COMMSTATUS_ID serial ,
COMMCONCEPT_ID Integer NOT NULL ,
reference_ID Integer ,
commConceptStatus varchar (20) NOT NULL ,
commParent_ID Integer ,
commLevel varchar (80) ,
startDate Date NOT NULL ,
stopDate Date ,
commPartyComments text ,
PARTY_ID Integer NOT NULL ,

PRIMARY KEY ( COMMSTATUS_ID )
);

CREATE TABLE commUsage
(
COMMUSAGE_ID serial ,
COMMNAME_ID Integer NOT NULL ,
commName text ,
COMMCONCEPT_ID Integer NOT NULL ,
usageStart Date ,
usageStop Date ,
commNameStatus varchar (20) ,
classSystem varchar (50) ,
PARTY_ID Integer NOT NULL ,
COMMSTATUS_ID Integer ,

PRIMARY KEY ( COMMUSAGE_ID )
);

CREATE TABLE plantConcept
(
PLANTCONCEPT_ID serial ,
PLANTNAME_ID Integer NOT NULL ,
reference_ID Integer NOT NULL ,
plantname varchar (200) ,
plantCode varchar (23) ,
plantDescription text ,
accessionCode varchar (100) ,

PRIMARY KEY ( PLANTCONCEPT_ID )
);

CREATE TABLE plantCorrelation
(
PLANTCORRELATION_ID serial ,
PLANTSTATUS_ID Integer NOT NULL ,
PLANTCONCEPT_ID Integer NOT NULL ,
plantConvergence varchar (20) NOT NULL ,
correlationStart Date NOT NULL ,
correlationStop Date ,

PRIMARY KEY ( PLANTCORRELATION_ID )
);

CREATE TABLE plantLineage
(
PLANTLINEAGE_ID serial ,
childPlantStatus_ID Integer NOT NULL ,
parentPlantStatus_ID Integer NOT NULL ,

PRIMARY KEY ( PLANTLINEAGE_ID )
);

CREATE TABLE plantName
(
PLANTNAME_ID serial ,
plantName varchar (255) NOT NULL ,
reference_ID Integer ,
dateEntered Date ,

PRIMARY KEY ( PLANTNAME_ID )
);

CREATE TABLE plantStatus
(
PLANTSTATUS_ID serial ,
PLANTCONCEPT_ID Integer NOT NULL ,
reference_ID Integer ,
plantConceptStatus varchar (20) NOT NULL ,
startDate Date NOT NULL ,
stopDate Date ,
plantPartyComments text ,
plantParentName varchar (200) ,
plantParentConcept_id Integer ,
plantParent_ID Integer ,
plantLevel varchar (80) ,
PARTY_ID Integer NOT NULL ,

PRIMARY KEY ( PLANTSTATUS_ID )
);

CREATE TABLE plantUsage
(
PLANTUSAGE_ID serial ,
PLANTNAME_ID Integer NOT NULL ,
PLANTCONCEPT_ID Integer NOT NULL ,
usageStart Date ,
usageStop Date ,
plantNameStatus varchar (20) ,
plantName varchar (220) ,
classSystem varchar (50) ,
acceptedSynonym varchar (220) ,
PARTY_ID Integer NOT NULL ,
PLANTSTATUS_ID Integer ,

PRIMARY KEY ( PLANTUSAGE_ID )
);

CREATE TABLE address
(
ADDRESS_ID serial ,
party_ID Integer NOT NULL ,
organization_ID Integer ,
orgPosition varchar (50) ,
email varchar (100) ,
deliveryPoint varchar (200) ,
city varchar (50) ,
administrativeArea varchar (50) ,
postalCode varchar (10) ,
country varchar (50) ,
currentFlag Boolean ,
addressStartDate Date ,

PRIMARY KEY ( ADDRESS_ID )
);

CREATE TABLE aux_Role
(
ROLE_ID serial ,
roleCode varchar (30) NOT NULL ,
roleDescription varchar (200) ,
roleProject Integer ,
roleObservation Integer ,
roleTaxonInt Integer ,
roleClassInt Integer ,

PRIMARY KEY ( ROLE_ID )
);

CREATE TABLE reference
(
reference_ID serial NOT NULL ,
shortName varchar (250) ,
fulltext text ,
referenceType varchar (250) ,
title varchar (250) ,
titleSuperior varchar (250) ,
pubDate Date ,
accessDate Date ,
conferenceDate Date ,
referenceJournal_ID Integer ,
volume varchar (250) ,
issue varchar (250) ,
pageRange varchar (250) ,
totalPages Integer ,
publisher varchar (250) ,
publicationPlace varchar (250) ,
isbn varchar (250) ,
edition varchar (250) ,
numberOfVolumes Integer ,
chapterNumber Integer ,
reportNumber Integer ,
communicationType varchar (250) ,
degree varchar (250) ,
url text ,
doi text ,
additionalInfo text ,
accessionCode varchar (100) ,

PRIMARY KEY ( reference_ID )
);

CREATE TABLE referenceAltIdent
(
referenceAltIdent_ID serial NOT NULL ,
reference_ID Integer NOT NULL ,
system varchar (250) ,
identifier varchar (250) NOT NULL ,

PRIMARY KEY ( referenceAltIdent_ID )
);

CREATE TABLE referenceContributor
(
referenceContributor_ID serial NOT NULL ,
reference_ID Integer NOT NULL ,
referenceParty_ID Integer NOT NULL ,
roleType varchar (250) ,
position Integer ,

PRIMARY KEY ( referenceContributor_ID )
);

CREATE TABLE referenceParty
(
referenceParty_ID serial NOT NULL ,
type varchar (250) ,
positionName varchar (250) ,
salutation varchar (250) ,
givenName varchar (250) ,
surname varchar (250) ,
suffix varchar (250) ,
organizationName varchar (250) ,
currentParty_ID Integer ,
accessionCode varchar (100) ,

PRIMARY KEY ( referenceParty_ID )
);

CREATE TABLE referenceJournal
(
referenceJournal_ID serial NOT NULL ,
journal varchar (250) NOT NULL ,
issn varchar (250) ,
abbreviation varchar (250) ,
accessionCode varchar (100) ,

PRIMARY KEY ( referenceJournal_ID )
);

CREATE TABLE classContributor
(
CLASSCONTRIBUTOR_ID serial ,
COMMCLASS_ID Integer NOT NULL ,
PARTY_ID Integer NOT NULL ,
ROLE_ID Integer ,

PRIMARY KEY ( CLASSCONTRIBUTOR_ID )
);

CREATE TABLE commClass
(
COMMCLASS_ID serial ,
OBSERVATION_ID Integer NOT NULL ,
classStartDate Date ,
classStopDate Date ,
inspection Boolean ,
tableAnalysis Boolean ,
multivariateAnalysis Boolean ,
expertSystem Boolean ,
classPublication_ID Integer ,
classNotes text ,
commName varchar (200) ,
commCode varchar (200) ,
commFramework varchar (200) ,
commLevel varchar (200) ,
accessionCode varchar (100) ,

PRIMARY KEY ( COMMCLASS_ID )
);

CREATE TABLE commInterpretation
(
COMMINTERPRETATION_ID serial ,
COMMCLASS_ID Integer NOT NULL ,
COMMCONCEPT_ID Integer ,
commcode varchar (34) ,
commname varchar (200) ,
classFit varchar (50) ,
classConfidence varchar (15) ,
commAuthority_ID Integer ,
notes text ,
type Boolean ,
nomenclaturalType Boolean ,

PRIMARY KEY ( COMMINTERPRETATION_ID )
);

CREATE TABLE coverIndex
(
COVERINDEX_ID serial ,
COVERMETHOD_ID Integer NOT NULL ,
coverCode varchar (10) NOT NULL ,
upperLimit Float ,
lowerLimit Float ,
coverPercent Float NOT NULL ,
indexDescription text ,

PRIMARY KEY ( COVERINDEX_ID )
);

CREATE TABLE coverMethod
(
COVERMETHOD_ID serial ,
reference_ID Integer ,
coverType varchar (30) NOT NULL ,
accessionCode varchar (100) ,

PRIMARY KEY ( COVERMETHOD_ID )
);

CREATE TABLE definedValue
(
DEFINEDVALUE_ID serial ,
USERDEFINED_ID Integer NOT NULL ,
tableRecord_ID Integer NOT NULL ,
definedValue text NOT NULL ,

PRIMARY KEY ( DEFINEDVALUE_ID )
);

CREATE TABLE disturbanceObs
(
disturbanceObs_ID serial ,
OBSERVATION_ID Integer NOT NULL ,
disturbanceType varchar (30) NOT NULL ,
disturbanceIntensity varchar (30) ,
disturbanceAge Float ,
disturbanceExtent Float ,
disturbanceComment text ,

PRIMARY KEY ( disturbanceObs_ID )
);

CREATE TABLE graphic
(
GRAPHIC_ID serial ,
OBSERVATION_ID Integer NOT NULL ,
graphicName varchar (30) ,
graphicLocation text ,
graphicDescription text ,
graphicType varchar (20) ,
graphicDate Date ,
graphicData oid ,

PRIMARY KEY ( GRAPHIC_ID )
);

CREATE TABLE namedPlace
(
NAMEDPLACE_ID serial ,
placeSystem varchar (50) ,
placeName varchar (100) NOT NULL ,
placeDescription text ,
placeCode varchar (15) ,
owner varchar (100) ,
reference_ID Integer ,
accessionCode varchar (100) ,

PRIMARY KEY ( NAMEDPLACE_ID )
);

CREATE TABLE note
(
NOTE_ID serial ,
NOTELINK_ID Integer NOT NULL ,
PARTY_ID Integer NOT NULL ,
ROLE_ID Integer NOT NULL ,
noteDate Date ,
noteType varchar (20) NOT NULL ,
noteText text NOT NULL ,

PRIMARY KEY ( NOTE_ID )
);

CREATE TABLE noteLink
(
NOTELINK_ID serial ,
tableName varchar (50) NOT NULL ,
attributeName varchar (50) ,
tableRecord Integer NOT NULL ,

PRIMARY KEY ( NOTELINK_ID )
);

CREATE TABLE observation
(
OBSERVATION_ID serial ,
PREVIOUSOBS_ID Integer ,
PLOT_ID Integer NOT NULL ,
PROJECT_ID Integer ,
dateEntered Date ,
authorObsCode varchar (30) ,
obsStartDate Date ,
obsEndDate Date ,
dateAccuracy varchar (30) ,
COVERMETHOD_ID Integer ,
STRATUMMETHOD_ID Integer ,
stemSizeLimit Float ,
methodNarrative text ,
taxonObservationArea Float ,
coverDispersion varchar (30) ,
autoTaxonCover Boolean ,
stemObservationArea Float ,
stemSampleMethod varchar (30) ,
originalData text ,
effortLevel varchar (30) ,
plotValidationLevel Integer ,
floristicQuality varchar (30) ,
bryophyteQuality varchar (30) ,
lichenQuality varchar (30) ,
observationNarrative text ,
landscapeNarrative text ,
homogeneity varchar (50) ,
phenologicAspect varchar (30) ,
representativeness varchar (255) ,
basalArea Float ,
hydrologicRegime varchar (30) ,
soilMoistureRegime varchar (30) ,
soilDrainage varchar (30) ,
waterSalinity varchar (30) ,
waterDepth Float ,
shoreDistance Float ,
soilDepth Float ,
organicDepth Float ,
percentBedRock Float ,
percentRockGravel Float ,
percentWood Float ,
percentLitter Float ,
percentBareSoil Float ,
percentWater Float ,
percentOther Float ,
nameOther varchar (30) ,
standMaturity varchar (50) ,
successionalStatus text ,
treeHt Float ,
shrubHt Float ,
fieldHt Float ,
nonvascularHt Float ,
submergedHt Float ,
treeCover Float ,
shrubCover Float ,
fieldCover Float ,
nonvascularCover Float ,
floatingCover Float ,
submergedCover Float ,
dominantStratum varchar (40) ,
growthform1Type varchar (40) ,
growthform2Type varchar (40) ,
growthform3Type varchar (40) ,
growthform1Cover Float ,
growthform2Cover Float ,
growthform3Cover Float ,
SOILTAXON_ID Integer ,
soilTaxonSrc varchar (200) ,
coverEstimationMethod varchar (80) ,
stratumIndividualDivision varchar (50) ,
totalCover Float ,
accessionCode varchar (100) ,
notesPublic Boolean ,
notesMgt Boolean ,
revisions Boolean ,

PRIMARY KEY ( OBSERVATION_ID )
);

CREATE TABLE observationContributor
(
OBSERVATIONCONTRIBUTOR_ID serial ,
OBSERVATION_ID Integer NOT NULL ,
PARTY_ID Integer NOT NULL ,
ROLE_ID Integer NOT NULL ,
contributionDate Date ,

PRIMARY KEY ( OBSERVATIONCONTRIBUTOR_ID )
);

CREATE TABLE observationSynonym
(
OBSERVATIONSYNONYM_ID serial ,
synonymObservation_ID Integer NOT NULL ,
primaryObservation_ID Integer NOT NULL ,
PARTY_ID Integer NOT NULL ,
ROLE_ID Integer NOT NULL ,
classStartDate Date NOT NULL ,
classStopDate Date ,
synonymComment text ,

PRIMARY KEY ( OBSERVATIONSYNONYM_ID )
);

CREATE TABLE party
(
PARTY_ID serial ,
salutation varchar (20) ,
givenName varchar (50) ,
middleName varchar (50) ,
surName varchar (50) ,
organizationName varchar (100) ,
currentName_ID Integer ,
contactInstructions text ,
email varchar (120) ,
accessionCode varchar (100) ,
partyType varchar (40) ,

PRIMARY KEY ( PARTY_ID )
);

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

CREATE TABLE place
(
PLOTPLACE_ID serial ,
PLOT_ID Integer NOT NULL ,
calculated Boolean ,
NAMEDPLACE_ID Integer NOT NULL ,

PRIMARY KEY ( PLOTPLACE_ID )
);

CREATE TABLE plot
(
PLOT_ID serial ,
authorPlotCode varchar (30) NOT NULL ,
reference_ID Integer ,
PARENT_ID Integer ,
realLatitude Float ,
realLongitude Float ,
locationAccuracy Float ,
confidentialityStatus Integer NOT NULL ,
confidentialityReason varchar (200) ,
latitude Float ,
longitude Float ,
authorE varchar (20) ,
authorN varchar (20) ,
authorZone varchar (20) ,
authorDatum varchar (20) ,
authorLocation varchar (200) ,
locationNarrative text ,
azimuth Float ,
dsgpoly text ,
shape varchar (50) ,
area Float ,
standSize varchar (50) ,
placementMethod varchar (50) ,
permanence Boolean ,
layoutNarrative text ,
elevation Float ,
elevationAccuracy Float ,
elevationRange Float ,
slopeAspect Float ,
minSlopeAspect Float ,
maxSlopeAspect Float ,
slopeGradient Float ,
minSlopeGradient Float ,
maxSlopeGradient Float ,
topoPosition varchar (90) ,
landform varchar (50) ,
surficialDeposits varchar (90) ,
rockType varchar (90) ,
stateProvince varchar (55) ,
country varchar (100) ,
dateentered Date ,
submitter_surname varchar (100) ,
submitter_givenname varchar (100) ,
submitter_email varchar (100) ,
accessionCode varchar (100) ,
notesPublic Boolean ,
notesMgt Boolean ,
revisions Boolean ,

PRIMARY KEY ( PLOT_ID )
);

CREATE TABLE project
(
PROJECT_ID serial ,
projectName varchar (150) NOT NULL ,
projectDescription text ,
startDate Date ,
stopDate Date ,
accessionCode varchar (100) ,

PRIMARY KEY ( PROJECT_ID )
);

CREATE TABLE projectContributor
(
PROJECTCONTRIBUTOR_ID serial ,
PROJECT_ID Integer NOT NULL ,
PARTY_ID Integer NOT NULL ,
ROLE_ID Integer ,
surname varchar (50) ,
cheatRole varchar (50) ,

PRIMARY KEY ( PROJECTCONTRIBUTOR_ID )
);

CREATE TABLE revision
(
REVISION_ID serial ,
tableName varchar (50) NOT NULL ,
tableAttribute varchar (50) NOT NULL ,
tableRecord Integer NOT NULL ,
revisionDate Date NOT NULL ,
previousValueText text NOT NULL ,
previousValueType varchar (20) NOT NULL ,
previousRevision_ID Integer ,

PRIMARY KEY ( REVISION_ID )
);

CREATE TABLE soilObs
(
SOILOBS_ID serial ,
OBSERVATION_ID Integer NOT NULL ,
soilHorizon varchar (15) NOT NULL ,
soilDepthTop Float ,
soilDepthBottom Float ,
soilColor varchar (30) ,
soilOrganic Float ,
soilTexture varchar (50) ,
soilSand Float ,
soilSilt Float ,
soilClay Float ,
soilCoarse Float ,
soilPH Float ,
exchangeCapacity Float ,
baseSaturation Float ,
soilDescription text ,

PRIMARY KEY ( SOILOBS_ID )
);

CREATE TABLE soilTaxon
(
SOILTAXON_ID serial ,
soilCode varchar (15) ,
soilName varchar (100) ,
soilLevel Integer ,
SOILPARENT_ID Integer ,
soilFramework varchar (33) ,
accessionCode varchar (100) ,

PRIMARY KEY ( SOILTAXON_ID )
);

CREATE TABLE stemCount
(
STEMCOUNT_ID serial ,
TAXONIMPORTANCE_ID Integer NOT NULL ,
stemDiameter Float ,
stemDiameterAccuracy Float ,
stemHeight Float ,
stemHeightAccuracy Float ,
stemCount Integer NOT NULL ,
stemTaxonArea Float ,

PRIMARY KEY ( STEMCOUNT_ID )
);

CREATE TABLE stemLocation
(
STEMLOCATION_ID serial ,
STEMCOUNT_ID Integer NOT NULL ,
stemCode varchar (20) ,
stemXPosition Float ,
stemYPosition Float ,
stemHealth varchar (50) ,

PRIMARY KEY ( STEMLOCATION_ID )
);

CREATE TABLE stratum
(
STRATUM_ID serial ,
OBSERVATION_ID Integer NOT NULL ,
STRATUMTYPE_ID Integer NOT NULL ,
STRATUMMETHOD_ID Integer ,
stratumName varchar (30) ,
stratumHeight Float ,
stratumBase Float ,
stratumCover Float ,
stratumDescription varchar (200) ,

PRIMARY KEY ( STRATUM_ID )
);

CREATE TABLE stratumMethod
(
STRATUMMETHOD_ID serial ,
reference_ID Integer ,
stratumMethodName varchar (30) NOT NULL ,
stratumMethodDescription text ,
accessionCode varchar (100) ,

PRIMARY KEY ( STRATUMMETHOD_ID )
);

CREATE TABLE stratumType
(
STRATUMTYPE_ID serial ,
STRATUMMETHOD_ID Integer NOT NULL ,
stratumIndex varchar (10) ,
stratumName varchar (30) ,
stratumDescription text ,

PRIMARY KEY ( STRATUMTYPE_ID )
);

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

CREATE TABLE taxonInterpretation
(
TAXONINTERPRETATION_ID serial ,
TAXONOBSERVATION_ID Integer NOT NULL ,
stemLocation_ID Integer ,
PLANTCONCEPT_ID Integer NOT NULL ,
interpretationDate Date NOT NULL ,
PLANTNAME_ID Integer ,
PARTY_ID Integer NOT NULL ,
ROLE_ID Integer NOT NULL ,
interpretationType varchar (30) ,
reference_ID Integer ,
originalInterpretation Boolean NOT NULL ,
currentInterpretation Boolean NOT NULL ,
taxonFit varchar (50) ,
taxonConfidence varchar (50) ,
collector_ID Integer ,
collectionNumber varchar (100) ,
collectionDate Date ,
museum_ID Integer ,
museumAccessionNumber varchar (100) ,
groupType varchar (20) ,
notes text ,
notesPublic Boolean ,
notesMgt Boolean ,
revisions Boolean ,

PRIMARY KEY ( TAXONINTERPRETATION_ID )
);

CREATE TABLE taxonObservation
(
TAXONOBSERVATION_ID serial ,
OBSERVATION_ID Integer NOT NULL ,
authorPlantName varchar (255) ,
reference_ID Integer ,
taxonInferenceArea Float ,
accessionCode varchar (100) ,

PRIMARY KEY ( TAXONOBSERVATION_ID )
);

CREATE TABLE taxonAlt
(
taxonAlt_ID serial ,
taxonInterpretation_ID Integer NOT NULL ,
plantConcept_ID Integer NOT NULL ,
taxonAltFit varchar (50) ,
taxonAltConfidence varchar (50) ,
taxonAltNotes text ,

PRIMARY KEY ( taxonAlt_ID )
);

CREATE TABLE telephone
(
TELEPHONE_ID serial ,
PARTY_ID Integer NOT NULL ,
phoneNumber varchar (30) NOT NULL ,
phoneType varchar (20) NOT NULL ,

PRIMARY KEY ( TELEPHONE_ID )
);

CREATE TABLE userDefined
(
USERDEFINED_ID serial ,
userDefinedName varchar (50) ,
userDefinedMetadata text ,
userDefinedCategory varchar (30) ,
userDefinedType varchar (20) NOT NULL ,
tableName varchar (50) NOT NULL ,
accessionCode varchar (100) ,

PRIMARY KEY ( USERDEFINED_ID )
);

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

CREATE TABLE userDataset
(
userDataset_ID serial NOT NULL ,
usr_ID Integer NOT NULL ,
datasetStart Date ,
datasetStop Date ,
accessionCode varchar (100) ,
datasetName varchar (100) NOT NULL ,
datasetDescription text ,
datasetType varchar (50) ,
datasetSharing varchar (30) ,
datasetPassword varchar (50) ,

PRIMARY KEY ( userDataset_ID )
);

CREATE TABLE userDatasetItem
(
userDatasetItem_ID serial NOT NULL ,
userDataset_ID Integer NOT NULL ,
itemAccessionCode varchar (100) NOT NULL ,
itemType varchar (50) ,
itemStart Date NOT NULL ,
itemStop Date ,
notes text ,

PRIMARY KEY ( userDatasetItem_ID )
);

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

CREATE TABLE userRecordOwner
(
userRecordOwner_ID serial NOT NULL ,
usr_ID Integer NOT NULL ,
tableName varchar (50) NOT NULL ,
tableRecord Integer NOT NULL ,
recordCreationDate Date NOT NULL ,
ownerStart Date NOT NULL ,
ownerStop Date ,
ownerType varchar (30) NOT NULL ,

PRIMARY KEY ( userRecordOwner_ID )
);

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

ALTER TABLE commUsage   ADD CONSTRAINT R4commUsage_COMMSTATUS_ID FOREIGN KEY (COMMSTATUS_ID)    REFERENCES commStatus (COMMSTATUS_ID );

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

ALTER TABLE plantUsage   ADD CONSTRAINT R4plantUsage_PLANTSTATUS_ID FOREIGN KEY (PLANTSTATUS_ID)    REFERENCES plantStatus (PLANTSTATUS_ID );

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
