-----------------------------------------------------------------------------
-- aux_commInterpretation_classFit -- jhh 20020830
-----------------------------------------------------------------------------
drop table aux_commInterpretation_classFit;
CREATE TABLE aux_commInterpretation_classFit
(
    classFit varchar (200)
);

insert into aux_commInterpretation_classFit values ('Absolutely wrong');
insert into aux_commInterpretation_classFit values ('Understandable but wrong');
insert into aux_commInterpretation_classFit values ('Reasonable or acceptable answer');
insert into aux_commInterpretation_classFit values ('Good answer');
insert into aux_commInterpretation_classFit values ('Absolutely Right');


-----------------------------------------------------------------------------
-- address
-----------------------------------------------------------------------------
drop sequence address_ADDRESS_ID_seq;
drop table address;

CREATE TABLE address
(
    ADDRESS_ID serial,
    PARTY_ID integer NOT NULL,
    ORGANIZATION_ID integer,
    orgPosition varchar (50),
    email varchar (100),
    deliveryPoint varchar (200),
    city varchar (50),
    administrativeArea varchar (50),
    postalCode varchar (10),
    country varchar (50),
    currentFlag boolean,
    addressStartDate timestamp,
    PRIMARY KEY(ADDRESS_ID)
);

-----------------------------------------------------------------------------
-- aux_Role
-----------------------------------------------------------------------------
drop sequence aux_Role_ROLE_ID_seq;
drop table aux_Role;

CREATE TABLE aux_Role
(
    ROLE_ID serial,
    roleCode varchar (30) NOT NULL,
    roleDescription varchar (200),
    roleProject integer,
    roleObservation integer,
    roleTaxonInt integer,
    roleClassInt integer,
    PRIMARY KEY(ROLE_ID)
);

-----------------------------------------------------------------------------
-- citation
-----------------------------------------------------------------------------
drop sequence citation_CITATION_ID_seq;
drop table citation;

CREATE TABLE citation
(
    CITATION_ID serial,
    authors varchar (200),
    title varchar (200),
    alternateTitle varchar (50),
    pubDate timestamp,
    edition varchar (20),
    seriesName varchar (50),
    issueIdentification varchar (20),
    otherCitationDetails varchar (100),
    page varchar (20),
    tableCited varchar (20),
    plotCited varchar (20),
    ISBN varchar (20),
    ISSN varchar (20),
    PRIMARY KEY(CITATION_ID)
);

-----------------------------------------------------------------------------
-- classContributor
-----------------------------------------------------------------------------
drop sequence classContributor_CLASSCONTRIBUTOR_ID_seq;
drop table classContributor;

CREATE TABLE classContributor
(
    CLASSCONTRIBUTOR_ID serial,
    COMMCLASS_ID integer NOT NULL,
    PARTY_ID integer NOT NULL,
    ROLE_ID integer,
    PRIMARY KEY(CLASSCONTRIBUTOR_ID)
);

-----------------------------------------------------------------------------
-- commClass
-----------------------------------------------------------------------------
drop sequence commClass_COMMCLASS_ID_seq;
drop table commClass;

CREATE TABLE commClass
(
    COMMCLASS_ID serial,
    OBSERVATION_ID integer NOT NULL,
    classStartDate timestamp,
    classStopDate timestamp,
    inspection boolean,
    tableAnalysis boolean,
    multivariateAnalysis boolean,
    expertSystem boolean,
    classPublication_ID integer,
    classNotes  text,
		--below are denormalized by JHH
		commName varchar (200), -- the name as used in the plots db
		commCode varchar (200), -- the code if it exists
		commFramework varchar (200), --nvc, provisional, etc
		commLevel varchar (200), -- the level in the heiracry
    PRIMARY KEY(COMMCLASS_ID)
);

-----------------------------------------------------------------------------
-- commInterpretation
-----------------------------------------------------------------------------
drop sequence commInterpretation_COMMINTERPRETATION_ID_seq;
drop table commInterpretation;

CREATE TABLE commInterpretation
(
    COMMINTERPRETATION_ID serial,
    COMMCLASS_ID integer NOT NULL,
    COMMCONCEPT_ID integer,
		--next two added by JHH
		commcode varchar (34),
		commname varchar (200),
    classFit varchar (50),
    classConfidence varchar (15),
    commAuthority_ID integer,
    notes text,
    type boolean,
    PRIMARY KEY(COMMINTERPRETATION_ID)
);

-----------------------------------------------------------------------------
-- coverIndex
-----------------------------------------------------------------------------
drop sequence coverIndex_COVERINDEX_ID_seq;
drop table coverIndex;

CREATE TABLE coverIndex
(
    COVERINDEX_ID serial,
    COVERMETHOD_ID integer NOT NULL,
    coverCode varchar (10) NOT NULL,
    upperLimit float,
    lowerLimit float,
    coverPercent float NOT NULL,
    indexDescription text,
    PRIMARY KEY(COVERINDEX_ID)
);

-----------------------------------------------------------------------------
-- coverMethod
-----------------------------------------------------------------------------
drop sequence coverMethod_COVERMETHOD_ID_seq;
drop table coverMethod;

CREATE TABLE coverMethod
(
    COVERMETHOD_ID serial,
    CITATION_ID integer,
    coverType varchar (30) NOT NULL,
    PRIMARY KEY(COVERMETHOD_ID)
);

-----------------------------------------------------------------------------
-- definedValue
-----------------------------------------------------------------------------
drop sequence definedValue_DEFINEDVALUE_ID_seq;
drop table definedValue;

CREATE TABLE definedValue
(
    DEFINEDVALUE_ID serial,
    USERDEFINED_ID integer NOT NULL,
    value varchar (255) NOT NULL,
    tableRecord_ID integer NOT NULL,
    PRIMARY KEY(DEFINEDVALUE_ID)
);

-----------------------------------------------------------------------------
-- disturbanceObs
-----------------------------------------------------------------------------
drop sequence disturbanceObs_disturbanceObs_seq;
drop table disturbanceObs;

CREATE TABLE disturbanceObs
(
    disturbanceObs serial,
    OBSERVATION_ID integer NOT NULL,
    disturbanceType varchar (30) NOT NULL,
    disturbanceIntensity varchar (30),
    diosturbanceAge float,
    disturbanceExtent float,
    disturbanceComment text,
    PRIMARY KEY(disturbanceObs)
);

-----------------------------------------------------------------------------
-- graphic
-----------------------------------------------------------------------------
drop sequence graphic_GRAPHIC_ID_seq;
drop table graphic;

CREATE TABLE graphic
(
    GRAPHIC_ID serial,
    OBSERVATION_ID integer NOT NULL,
    graphicName varchar (30),
    graphicLocation text,
    graphicDescription text,
    graphicType varchar (20),
    graphicDate timestamp,
    graphicData oid,
    PRIMARY KEY(GRAPHIC_ID)
);

-----------------------------------------------------------------------------
-- namedPlace
-----------------------------------------------------------------------------
drop sequence namedPlace_NAMEDPLACE_ID_seq;
drop table namedPlace;

CREATE TABLE namedPlace
(
    NAMEDPLACE_ID serial,
    placeSystem varchar (50),
    placeName varchar (100) NOT NULL,
    placeDescription text,
    placeCode varchar (15),
    owner varchar (100),
    CITATION_ID integer,
    PRIMARY KEY(NAMEDPLACE_ID)
);

-----------------------------------------------------------------------------
-- noteLink
-----------------------------------------------------------------------------
drop sequence noteLink_NOTELINK_ID_seq;
drop table noteLink;

CREATE TABLE noteLink
(
    NOTELINK_ID serial,
    tableName varchar (50) NOT NULL,
    attributeName varchar (50) NOT NULL,
    tableRecord integer NOT NULL,
    PRIMARY KEY(NOTELINK_ID)
);

-----------------------------------------------------------------------------
-- Note
-----------------------------------------------------------------------------
drop sequence Note_NOTE_ID_seq;
drop table Note;

CREATE TABLE Note
(
    NOTE_ID serial,
    NOTELINK_ID integer NOT NULL,
    PARTY_ID integer NOT NULL,
    ROLE_ID integer NOT NULL,
    noteDate timestamp,
    noteType varchar (20) NOT NULL,
    noteText text NOT NULL,
    PRIMARY KEY(NOTE_ID)
);

-----------------------------------------------------------------------------
-- observationContributor
-----------------------------------------------------------------------------
drop sequence observationContributor_PLOTCONTRIBUTOR_ID_seq;
drop table observationContributor;

CREATE TABLE observationContributor
(
    PLOTCONTRIBUTOR_ID serial,
    OBSERVATION_ID integer NOT NULL,
    PARTY_ID integer NOT NULL,
    ROLE_ID integer NOT NULL,
    contributionDate timestamp,
    PRIMARY KEY(PLOTCONTRIBUTOR_ID)
);

-----------------------------------------------------------------------------
-- observationSynonym
-----------------------------------------------------------------------------
drop sequence observationSynonym_OBSERVATIONSYNONYM_ID_seq;
drop table observationSynonym;

CREATE TABLE observationSynonym
(
    OBSERVATIONSYNONYM_ID serial,
    OBSERVATION_ID integer NOT NULL,
    PRIMARYOBS_ID integer NOT NULL,
    PARTY_ID integer NOT NULL,
    ROLE_ID integer NOT NULL,
    classStartDate timestamp NOT NULL,
    classStopDate timestamp,
    SynonymComment text,
    PRIMARY KEY(OBSERVATIONSYNONYM_ID)
);

-----------------------------------------------------------------------------
-- observation
-----------------------------------------------------------------------------
drop sequence observation_OBSERVATION_ID_seq;
drop table observation;

CREATE TABLE observation
(
    OBSERVATION_ID serial,
    PREVIOUSOBS_ID integer,
    PLOT_ID integer NOT NULL,
    PROJECT_ID integer,
		ACCESSION_NUMBER varchar (200),
		VBSEQUENCE integer,
    authorObsCode varchar (30),
    obsStartDate timestamp,
    obsEndDate timestamp,
    dateAccuracy varchar (30),
    COVERMETHOD_ID integer NOT NULL,
    STRATUMMETHOD_ID integer NOT NULL,
    stemSizeLimit float,
    methodNarrative text,
    taxonObservationArea float NOT NULL,
    coverDispersion varchar (30) NOT NULL,
    autoTaxonCover boolean ,
    stemObservationArea float,
    stemSampleMethod varchar (30),
    originalData text,
    effortLevel varchar (30),
    plotValidationLevel varchar (60),
    floristicQuality varchar (30),
    bryophyteQuality varchar (30),
    lichenQuality varchar (30),
    observationNarrative text,
    landscapeNarrative text,
    homogeneity varchar (50),
    phenologicAspect varchar (30),
    representativeness varchar (100),
    basalArea float,
    hydrologicRegime varchar (30),
    soilMoistureRegime varchar (30),
    soilDrainage varchar (30),
    waterSalinity varchar (30),
    waterDepth float,
    shoreDistance float,
    soilDepth float,
    organicDepth float,
    percentBedRock float,
    percentRockGravel float,
    percentWood float,
    percentLitter float,
    percentBareSoil float,
    percentWater float,
    percentOther float,
    nameOther varchar (30),
    standMaturity varchar (50),
    successionalStatus text,
    treeHt float,
    shrubHt float,
    fieldHt float,
    nonvascularHt float,
    submergedHt float,
    treeCover float,
    shrubCover float,
    fieldCover float,
    nonvascularCover float,
    floatingCover float,
    submergedCover float,
    dominantStratum varchar (40),
    growthform1Type varchar (40),
    growthform2Type varchar (40),
    growthform3Type varchar (40),
    growthform1Cover float,
    growthform2Cover float,
    growthform3Cover float,
    SOILTAXON_ID integer,
    soilTaxonSrc varchar (200),
    notesPublic boolean,
    notesMgt boolean,
    revisions boolean,
    PRIMARY KEY(OBSERVATION_ID)
);

-----------------------------------------------------------------------------
-- party
-----------------------------------------------------------------------------
drop sequence party_PARTY_ID_seq;
drop table party;

CREATE TABLE party
(
    PARTY_ID serial,
    salutation varchar (20),
    givenName varchar (50),
    middleName varchar (50),
    surName varchar (50),
    organizationName varchar (100),
    currentName integer,
    contactInstructions varchar (1000),
    owner integer ,
		email varchar (120),
    PRIMARY KEY(PARTY_ID)
);

-----------------------------------------------------------------------------
-- place
-----------------------------------------------------------------------------
drop sequence place_PLOTPLACE_ID_seq;
drop table place;

CREATE TABLE place
(
    PLOTPLACE_ID serial,
    PLOT_ID integer NOT NULL,
    Calculated boolean,
    NAMEDPLACE_ID integer NOT NULL,
    PRIMARY KEY(PLOTPLACE_ID)
);

-----------------------------------------------------------------------------
-- plot
-----------------------------------------------------------------------------
drop sequence plot_PLOT_ID_seq;
drop table plot;

CREATE TABLE plot
(
    PLOT_ID serial,
    authorPlotCode varchar (30) NOT NULL,
		PROJECT_ID integer, -- denormalized this so that code will still work JHH 20020122
    CITATION_ID integer,
    PARENT_ID integer,
    -- Removed the 'not null' from all lats and longs JHH 20020122
		realLatitude float,
    realLongitude float,
    locationAccuracy float,
    confidentialityStatus integer NOT NULL,
    confidentialityReason varchar (200) NOT NULL,
    Latitude float,
    Longitude float,
    authorE varchar (20),
    authorN varchar (20),
    authorZone varchar (20),
    authorDatum varchar (20),
    authorLocation varchar (200),
    locationNarrative text,
    azimuth float,
    dsgpoly varchar (2000),
    shape varchar (50),
    area float NOT NULL,
    standSize varchar (50),
    placementMethod varchar (50),
    permanence boolean,
    layoutNarative text,
    elevation float,
    elevationAccuracy float,
    elevationRange float,
    slopeAspect float,
    minSlopeAspect float,
    maxSlopeAspect float,
    slopeGradient float,
    minSlopeGradient float,
    maxSlopeGradient float,
    topoPosition varchar (90),
    landform varchar (50),
    geology varchar (90),
    notesPublic boolean,
    notesMgt boolean,
    revisions boolean,
		-- These are added by JHH 20020122
		state varchar (55),
		country varchar (100),
		accession_number varchar (200),
		dateentered timestamp,
		submitter_surname varchar (100),
		submitter_givenname varchar (100),
		submitter_email varchar (100),
    PRIMARY KEY(PLOT_ID)
);

-----------------------------------------------------------------------------
-- projectContributor
-----------------------------------------------------------------------------
drop sequence projectContributor_PROJECTCONTRIBUTOR_ID_seq;
drop table projectContributor;

CREATE TABLE projectContributor
(
    PROJECTCONTRIBUTOR_ID serial,
    PROJECT_ID integer NOT NULL,
    PARTY_ID integer NOT NULL,
    ROLE_ID integer ,
		surname varchar (50),
		role varchar (50),
    PRIMARY KEY(PROJECTCONTRIBUTOR_ID)
);

-----------------------------------------------------------------------------
-- project
-----------------------------------------------------------------------------
drop sequence project_PROJECT_ID_seq;
drop table project;

CREATE TABLE project
(
    PROJECT_ID serial,
    projectName varchar (150) NOT NULL,
    projectDescription text,
    startDate timestamp,
    stopDate timestamp,
    PRIMARY KEY(PROJECT_ID)
);

-----------------------------------------------------------------------------
-- revision
-----------------------------------------------------------------------------
drop sequence revision_REVISION_ID_seq;
drop table revision;

CREATE TABLE revision
(
    REVISION_ID serial,
    tableName varchar (50) NOT NULL,
    tableAttribute varchar (50) NOT NULL,
    tableRecord integer NOT NULL,
    revisionDate timestamp NOT NULL,
    previousValueText varchar (500) NOT NULL,
    previousValueType varchar (20) NOT NULL,
    previousRevision_ID integer,
    PRIMARY KEY(REVISION_ID)
);

-----------------------------------------------------------------------------
-- soilObs
-----------------------------------------------------------------------------
drop sequence soilObs_SOILOBS_ID_seq;
drop table soilObs;

CREATE TABLE soilObs
(
    SOILOBS_ID serial,
    OBSERVATION_ID integer NOT NULL,
    soilHorizon varchar (15) NOT NULL,
    soilDepthTop float,
    soilDepthBottom float,
    soilColor varchar (30),
    soilOrganic float,
    soilTexture varchar (20),
    soilSand float,
    soilSilt float,
    soilClay float,
    soilCoarse float,
    soilPH float,
    exchanceCapacity float,
    baseSaturation float,
    soilDescription text,
    PRIMARY KEY(SOILOBS_ID)
);

-----------------------------------------------------------------------------
----this table added to SQL 6/18/02 by MTL
-- soilTaxon
-----------------------------------------------------------------------------
drop sequence soilTaxon_SOILTAXON_ID_seq;
drop table soilTaxon;
CREATE TABLE soilTaxon
(
    SOILTAXON_ID serial,
    soilCode varchar (15),
    soilName varchar (100),
    soilLevel Integer,
    SOILPARENT_ID Integer,
    soilFramework varchar (33),
    PRIMARY KEY(SOILTAXON_ID)
);

-----------------------------------------------------------------------------
-- stemCount
-----------------------------------------------------------------------------
drop sequence stemCount_STEMCOUNT_ID_seq;
drop table stemCount;

CREATE TABLE stemCount
(
    STEMCOUNT_ID serial,
    TAXONOBSERVATION_ID integer NOT NULL,
    stemDiameter float,
    stemDiameterAccuracy float,
    stemHeight float,
    stemHeightAccuracy float,
    stemCount integer NOT NULL,
--the following added by MTL 20020624    
    stemTaxonArea float,
    PRIMARY KEY(STEMCOUNT_ID)
);

-----------------------------------------------------------------------------
-- stemLocation
-----------------------------------------------------------------------------
drop sequence stemLocation_STEMLOCATION_ID_seq;
drop table stemLocation;

CREATE TABLE stemLocation
(
    STEMLOCATION_ID serial,
    STEMCOUNT_ID integer NOT NULL,
    stemCode varchar (20),
    stemXPosition float,
    stemYPosition float,
    PRIMARY KEY(STEMLOCATION_ID)
);

-----------------------------------------------------------------------------
-- stratumComposition
-----------------------------------------------------------------------------
drop sequence stratumComposition_STRATUMCOMPOSITION_ID_seq;
drop table stratumComposition;

CREATE TABLE stratumComposition
(
    STRATUMCOMPOSITION_ID serial,
    TAXONOBSERVATION_ID integer NOT NULL,
    STRATUM_ID integer NOT NULL,
    taxonStratumCover float NOT NULL,
			--added these next 2 to make the DB readable JHH 20020122
		cheatstratumname varchar (200),
		cheatplantname varchar (200),
		cheatplantcode varchar (200),
    PRIMARY KEY(STRATUMCOMPOSITION_ID)
);

-----------------------------------------------------------------------------
-- stratumMethod
-----------------------------------------------------------------------------
drop sequence stratumMethod_STRATUMMETHOD_ID_seq;
drop table stratumMethod;

CREATE TABLE stratumMethod
(
    STRATUMMETHOD_ID serial,
    CITATION_ID integer,
    stratumMethodName varchar (30) NOT NULL,
		stratumMethodDescription text,
    PRIMARY KEY(STRATUMMETHOD_ID)
);

-----------------------------------------------------------------------------
-- stratumType
-----------------------------------------------------------------------------
drop sequence stratum_STRATUMTYPE_ID_seq;
drop table stratumType;

CREATE TABLE stratumType
(
	STRATUMTYPE_ID serial,
	STRATUMMETHOD_ID integer NOT NULL,
	stratumIndex varchar (10),
	stratumName  varchar (30),
	stratumDescription text,
	PRIMARY KEY(STRATUMTYPE_ID)
);

-----------------------------------------------------------------------------
-- stratum
-----------------------------------------------------------------------------
drop sequence stratum_STRATUM_ID_seq;
drop table stratum;

CREATE TABLE stratum
(
    STRATUM_ID serial,
    OBSERVATION_ID integer NOT NULL,
    STRATUMTYPE_ID integer NOT NULL,
    STRATUMMETHOD_ID integer,
    stratumName varchar (30),
    stratumHeight float,
    stratumBase float,
    stratumCover float,
    stratumDescription varchar (200),
    PRIMARY KEY(STRATUM_ID)
);

-----------------------------------------------------------------------------
-- taxonInterpretation
-----------------------------------------------------------------------------
drop sequence taxonInterpretation_TAXONINTERPRETATION_ID_seq;
drop table taxonInterpretation;

CREATE TABLE taxonInterpretation
(
    TAXONINTERPRETATION_ID serial,
    TAXONOBSERVATION_ID integer NOT NULL,
    PLANTCONCEPT_ID integer NOT NULL,
    interpretationDate timestamp NOT NULL,
    PLANTNAME_ID integer,
    PARTY_ID integer NOT NULL,
    ROLE_ID integer NOT NULL,
    interpretationType varchar (30),
    CITATION_ID integer,
    originalInterpretation boolean not null,
    currentInterpretation boolean NOT NULL,
    notes text,
    notesPublic boolean,
    notesMgt boolean,
    revisions boolean,
    PRIMARY KEY(TAXONINTERPRETATION_ID)
);

-----------------------------------------------------------------------------
-- taxonObservation
-----------------------------------------------------------------------------
drop sequence taxonObservation_TAXONOBSERVATION_ID_seq;
drop table taxonObservation;

CREATE TABLE taxonObservation
(
    TAXONOBSERVATION_ID serial,
    OBSERVATION_ID integer NOT NULL,
		--remove the 'not null' because not implementing the plant taxa db JHH 20020122
    PLANTNAME_ID integer,
    PLANTREFERENCE_ID integer,
    taxonCollection varchar (100),
    taxonCover float,
    taxonBasalArea float,
    taxonInferenceArea float,
		--added this tom make the DB readable JHH 20020122
		cheatplantname varchar (200),
		cheatplantcode varchar (200),
    PRIMARY KEY(TAXONOBSERVATION_ID)
);

-----------------------------------------------------------------------------
-- telephone
-----------------------------------------------------------------------------
drop sequence telephone_TELEPHONE_ID_seq;
drop table telephone;

CREATE TABLE telephone
(
    TELEPHONE_ID serial,
    PARTY_ID integer NOT NULL,
    phoneNumber varchar (30) NOT NULL,
    phoneType varchar (20) NOT NULL,
    PRIMARY KEY(TELEPHONE_ID)
);

-----------------------------------------------------------------------------
-- userDefined
-----------------------------------------------------------------------------
drop sequence userDefined_USERDEFINED_ID_seq;
drop table userDefined;

CREATE TABLE userDefined
(
    USERDEFINED_ID serial,
    userDefinedName varchar (50),
    userDefinedMetadata text,
    userDefinedCategory varchar (30),
    userDefinedType varchar (20) NOT NULL,
    tableName varchar (50) NOT NULL,
    PRIMARY KEY(USERDEFINED_ID)
);
----------------------------------------------------------------------------
-- userDefined
----------------------------------------------------------------------------

ALTER TABLE address
    ADD CONSTRAINT PARTY_ID FOREIGN KEY (PARTY_ID)
    REFERENCES party (PARTY_ID);

ALTER TABLE address
    ADD CONSTRAINT ORGANIZATION_ID FOREIGN KEY (ORGANIZATION_ID)
    REFERENCES party (PARTY_ID);

----------------------------------------------------------------------------
-- address
----------------------------------------------------------------------------

----------------------------------------------------------------------------
-- aux_Role
----------------------------------------------------------------------------

----------------------------------------------------------------------------
-- citation
----------------------------------------------------------------------------

ALTER TABLE classContributor
    ADD CONSTRAINT COMMCLASS_ID FOREIGN KEY (COMMCLASS_ID)
    REFERENCES commClass (COMMCLASS_ID);

ALTER TABLE classContributor
    ADD CONSTRAINT PARTY_ID FOREIGN KEY (PARTY_ID)
    REFERENCES party (PARTY_ID);

ALTER TABLE classContributor
    ADD CONSTRAINT ROLE_ID FOREIGN KEY (ROLE_ID)
    REFERENCES aux_Role (ROLE_ID);

----------------------------------------------------------------------------
-- classContributor
----------------------------------------------------------------------------

ALTER TABLE commClass
    ADD CONSTRAINT OBSERVATION_ID FOREIGN KEY (OBSERVATION_ID)
    REFERENCES observation (OBSERVATION_ID);

ALTER TABLE commClass
    ADD CONSTRAINT classPublication_ID FOREIGN KEY (classPublication_ID)
    REFERENCES citation (CITATION_ID);

----------------------------------------------------------------------------
-- commClass
----------------------------------------------------------------------------

ALTER TABLE commInterpretation
    ADD CONSTRAINT COMMCLASS_ID FOREIGN KEY (COMMCLASS_ID)
    REFERENCES commClass (COMMCLASS_ID);

ALTER TABLE commInterpretation
    ADD CONSTRAINT COMMCONCEPT_ID FOREIGN KEY (COMMCONCEPT_ID)
    REFERENCES commConcept_COMMCONCEPT_ID (null);

ALTER TABLE commInterpretation
    ADD CONSTRAINT commAuthority_ID FOREIGN KEY (commAuthority_ID)
    REFERENCES citation (CITATION_ID);

----------------------------------------------------------------------------
-- commInterpretation
----------------------------------------------------------------------------

ALTER TABLE coverIndex
    ADD CONSTRAINT COVERMETHOD_ID FOREIGN KEY (COVERMETHOD_ID)
    REFERENCES coverMethod (COVERMETHOD_ID);

----------------------------------------------------------------------------
-- coverIndex
----------------------------------------------------------------------------

ALTER TABLE coverMethod
    ADD CONSTRAINT CITATION_ID FOREIGN KEY (CITATION_ID)
    REFERENCES citation (CITATION_ID);

----------------------------------------------------------------------------
-- coverMethod
----------------------------------------------------------------------------

ALTER TABLE definedValue
    ADD CONSTRAINT USERDEFINED_ID FOREIGN KEY (USERDEFINED_ID)
    REFERENCES userDefined (USERDEFINED_ID);

----------------------------------------------------------------------------
-- definedValue
----------------------------------------------------------------------------

ALTER TABLE disturbanceObs
    ADD CONSTRAINT OBSERVATION_ID FOREIGN KEY (OBSERVATION_ID)
    REFERENCES observation (OBSERVATION_ID);

----------------------------------------------------------------------------
-- disturbanceObs
----------------------------------------------------------------------------

ALTER TABLE graphic
    ADD CONSTRAINT OBSERVATION_ID FOREIGN KEY (OBSERVATION_ID)
    REFERENCES observation (OBSERVATION_ID);

----------------------------------------------------------------------------
-- graphic
----------------------------------------------------------------------------

ALTER TABLE namedPlace
    ADD CONSTRAINT CITATION_ID FOREIGN KEY (CITATION_ID)
    REFERENCES citation (CITATION_ID);

----------------------------------------------------------------------------
-- namedPlace
----------------------------------------------------------------------------

----------------------------------------------------------------------------
-- noteLink
----------------------------------------------------------------------------

ALTER TABLE Note
    ADD CONSTRAINT NOTELINK_ID FOREIGN KEY (NOTELINK_ID)
    REFERENCES noteLink (NOTELINK_ID);

ALTER TABLE Note
    ADD CONSTRAINT PARTY_ID FOREIGN KEY (PARTY_ID)
    REFERENCES party (PARTY_ID);

ALTER TABLE Note
    ADD CONSTRAINT ROLE_ID FOREIGN KEY (ROLE_ID)
    REFERENCES aux_Role (ROLE_ID);

----------------------------------------------------------------------------
-- Note
----------------------------------------------------------------------------

ALTER TABLE observationContributor
    ADD CONSTRAINT OBSERVATION_ID FOREIGN KEY (OBSERVATION_ID)
    REFERENCES Observation (OBSERVATION_ID);

ALTER TABLE observationContributor
    ADD CONSTRAINT PARTY_ID FOREIGN KEY (PARTY_ID)
    REFERENCES party (PARTY_ID);

ALTER TABLE observationContributor
    ADD CONSTRAINT ROLE_ID FOREIGN KEY (ROLE_ID)
    REFERENCES aux_Role (ROLE_ID);

----------------------------------------------------------------------------
-- observationContributor
----------------------------------------------------------------------------

ALTER TABLE observationSynonym
    ADD CONSTRAINT OBSERVATION_ID FOREIGN KEY (OBSERVATION_ID)
    REFERENCES observation (OBSERVATION_ID);

ALTER TABLE observationSynonym
    ADD CONSTRAINT PRIMARYOBS_ID FOREIGN KEY (PRIMARYOBS_ID)
    REFERENCES observation (OBSERVATION_ID);

ALTER TABLE observationSynonym
    ADD CONSTRAINT PARTY_ID FOREIGN KEY (PARTY_ID)
    REFERENCES party (PARTY_ID);

ALTER TABLE observationSynonym
    ADD CONSTRAINT ROLE_ID FOREIGN KEY (ROLE_ID)
    REFERENCES aux_Role (ROLE_ID);

----------------------------------------------------------------------------
-- observationSynonym
----------------------------------------------------------------------------

ALTER TABLE observation
    ADD CONSTRAINT PREVIOUSOBS_ID FOREIGN KEY (PREVIOUSOBS_ID)
    REFERENCES observation (OBSERVATION_ID);

ALTER TABLE observation
    ADD CONSTRAINT PLOT_ID FOREIGN KEY (PLOT_ID)
    REFERENCES plot (PLOT_ID);

ALTER TABLE observation
    ADD CONSTRAINT PROJECT_ID FOREIGN KEY (PROJECT_ID)
    REFERENCES project (PROJECT_ID);

ALTER TABLE observation
    ADD CONSTRAINT COVERMETHOD_ID FOREIGN KEY (COVERMETHOD_ID)
    REFERENCES coverMethod (COVERMETHOD_ID);

ALTER TABLE observation
    ADD CONSTRAINT STRATUMMETHOD_ID FOREIGN KEY (STRATUMMETHOD_ID)
    REFERENCES stratumMethod (STRATUMMETHOD_ID);

----this constraint added to SQL 6/18/02 by MTL
ALTER TABLE observation
    ADD CONSTRAINT SOILTAXON_ID FOREIGN KEY (SOILTAXON_ID)
    REFERENCES soilTaxon (SOILTAXON_ID);

----------------------------------------------------------------------------
-- observation
----------------------------------------------------------------------------

ALTER TABLE party
    ADD CONSTRAINT currentName FOREIGN KEY (currentName)
    REFERENCES party (PARTY_ID);

ALTER TABLE party
    ADD CONSTRAINT owner FOREIGN KEY (owner)
    REFERENCES party (PARTY_ID);

----------------------------------------------------------------------------
-- party
----------------------------------------------------------------------------

ALTER TABLE place
    ADD CONSTRAINT PLOT_ID FOREIGN KEY (PLOT_ID)
    REFERENCES plot (PLOT_ID);

ALTER TABLE place
    ADD CONSTRAINT NAMEDPLACE_ID FOREIGN KEY (NAMEDPLACE_ID)
    REFERENCES namedPlace (NAMEDPLACE_ID);

----------------------------------------------------------------------------
-- place
----------------------------------------------------------------------------

ALTER TABLE plot
    ADD CONSTRAINT CITATION_ID FOREIGN KEY (CITATION_ID)
    REFERENCES citation (CITATION_ID);

ALTER TABLE plot
    ADD CONSTRAINT PARENT_ID FOREIGN KEY (PARENT_ID)
    REFERENCES plot (PLOT_ID);

----------------------------------------------------------------------------
-- plot
----------------------------------------------------------------------------

ALTER TABLE projectContributor
    ADD CONSTRAINT PROJECT_ID FOREIGN KEY (PROJECT_ID)
    REFERENCES project (PROJECT_ID);

ALTER TABLE projectContributor
    ADD CONSTRAINT PARTY_ID FOREIGN KEY (PARTY_ID)
    REFERENCES party (PARTY_ID);

ALTER TABLE projectContributor
    ADD CONSTRAINT ROLE_ID FOREIGN KEY (ROLE_ID)
    REFERENCES aux_Role (ROLE_ID);

----------------------------------------------------------------------------
-- projectContributor
----------------------------------------------------------------------------

----------------------------------------------------------------------------
-- project
----------------------------------------------------------------------------

ALTER TABLE revision
    ADD CONSTRAINT previousRevision_ID FOREIGN KEY (previousRevision_ID)
    REFERENCES revision (REVISION_ID);

----------------------------------------------------------------------------
-- revision
----------------------------------------------------------------------------

ALTER TABLE soilObs
    ADD CONSTRAINT OBSERVATION_ID FOREIGN KEY (OBSERVATION_ID)
    REFERENCES observation (OBSERVATION_ID);

----------------------------------------------------------------------------
-- soilObs
----------------------------------------------------------------------------

----this constraint added to SQL 6/18/02 by MTL
ALTER TABLE soilTaxon
    ADD CONSTRAINT SOILPARENT_ID FOREIGN KEY (SOILPARENT_ID)
    REFERENCES soilTaxon (SOILTAXON_ID);

----------------------------------------------------------------------------
-- observation
----------------------------------------------------------------------------

ALTER TABLE stemCount
    ADD CONSTRAINT TAXONOBSERVATION_ID FOREIGN KEY (TAXONOBSERVATION_ID)
    REFERENCES taxonObservation (TAXONOBSERVATION_ID);

----------------------------------------------------------------------------
-- stemCount
----------------------------------------------------------------------------

ALTER TABLE stemLocation
    ADD CONSTRAINT STEMCOUNT_ID FOREIGN KEY (STEMCOUNT_ID)
    REFERENCES stemCount (STEMCOUNT_ID);

----------------------------------------------------------------------------
-- stemLocation
----------------------------------------------------------------------------

ALTER TABLE stratumComposition
    ADD CONSTRAINT TAXONOBSERVATION_ID FOREIGN KEY (TAXONOBSERVATION_ID)
    REFERENCES taxonObservation (TAXONOBSERVATION_ID);

ALTER TABLE stratumComposition
    ADD CONSTRAINT STRATUM_ID FOREIGN KEY (STRATUM_ID)
    REFERENCES stratum (STRATUM_ID);

----------------------------------------------------------------------------
-- stratumComposition
----------------------------------------------------------------------------

ALTER TABLE stratumMethod
    ADD CONSTRAINT CITATION_ID FOREIGN KEY (CITATION_ID)
    REFERENCES citation (CITATION_ID);

----------------------------------------------------------------------------
-- stratumMethod
----------------------------------------------------------------------------

ALTER TABLE stratumType
    ADD CONSTRAINT STRATUMMETHOD_ID FOREIGN KEY (STRATUMMETHOD_ID)
    REFERENCES stratumMethod (STRATUMMETHOD_ID);

----------------------------------------------------------------------------
-- stratumType
----------------------------------------------------------------------------

ALTER TABLE stratum
    ADD CONSTRAINT OBSERVATION_ID FOREIGN KEY (OBSERVATION_ID)
    REFERENCES observation (OBSERVATION_ID);

ALTER TABLE stratum
    ADD CONSTRAINT STRATUMMETHOD_ID FOREIGN KEY (STRATUMMETHOD_ID)
    REFERENCES stratumMethod (STRATUMMETHOD_ID);

ALTER TABLE stratum
    ADD CONSTRAINT STRATUMTYPE_ID FOREIGN KEY (STRATUMTYPE_ID)
    REFERENCES stratumType (STRATUMTYPE_ID);
    
----------------------------------------------------------------------------
-- stratum
----------------------------------------------------------------------------

ALTER TABLE taxonInterpretation
    ADD CONSTRAINT TAXONOBSERVATION_ID FOREIGN KEY (TAXONOBSERVATION_ID)
    REFERENCES taxonObservation (TAXONOBSERVATION_ID);

ALTER TABLE taxonInterpretation
    ADD CONSTRAINT PLANTCONCEPT_ID FOREIGN KEY (PLANTCONCEPT_ID)
    REFERENCES plantConcept (PLANTCONCEPT_ID);

ALTER TABLE taxonInterpretation
    ADD CONSTRAINT PLANTNAME_ID FOREIGN KEY (PLANTNAME_ID)
    REFERENCES plantName (PLANTNAME_ID);

ALTER TABLE taxonInterpretation
    ADD CONSTRAINT PARTY_ID FOREIGN KEY (PARTY_ID)
    REFERENCES party (PARTY_ID);

ALTER TABLE taxonInterpretation
    ADD CONSTRAINT ROLE_ID FOREIGN KEY (ROLE_ID)
    REFERENCES aux_Role (ROLE_ID);

ALTER TABLE taxonInterpretation
    ADD CONSTRAINT CITATION_ID FOREIGN KEY (CITATION_ID)
    REFERENCES Citation (CITATION_ID);

----------------------------------------------------------------------------
-- taxonInterpretation
----------------------------------------------------------------------------

ALTER TABLE taxonObservation
    ADD CONSTRAINT OBSERVATION_ID FOREIGN KEY (OBSERVATION_ID)
    REFERENCES observation (OBSERVATION_ID);

ALTER TABLE taxonObservation
    ADD CONSTRAINT PLANTNAME_ID FOREIGN KEY (PLANTNAME_ID)
    REFERENCES plantTaxa (plantName);

ALTER TABLE taxonObservation
    ADD CONSTRAINT PLANTREFERENCE_ID FOREIGN KEY (PLANTREFERENCE_ID)
    REFERENCES plantTaxa (plantReference);

----------------------------------------------------------------------------
-- taxonObservation
----------------------------------------------------------------------------

ALTER TABLE telephone
    ADD CONSTRAINT PARTY_ID FOREIGN KEY (PARTY_ID)
    REFERENCES party (PARTY_ID);

----------------------------------------------------------------------------
-- telephone
----------------------------------------------------------------------------

