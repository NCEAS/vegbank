
-----------------------------------------------------------------------------
-- revision
-----------------------------------------------------------------------------
drop sequence revision_REVISION_ID_seq;
drop table revision;

CREATE TABLE revision
(
    REVISION_ID serial,
    tableName varchar (22) NOT NULL,
    tableAttribute varchar (22) NOT NULL,
    tableRecord integer NOT NULL,
    revisionDate timestamp NOT NULL,
    previousValueText varchar (22) NOT NULL,
    previousValueType varchar (22) NOT NULL,
    previousRevision_ID integer,
    PRIMARY KEY(REVISION_ID)
);

-----------------------------------------------------------------------------
-- userDefined
-----------------------------------------------------------------------------
drop sequence userDefined_USERDEFINED_ID_seq;
drop table userDefined;

CREATE TABLE userDefined
(
    USERDEFINED_ID serial,
    userDefinedName varchar (22),
    userDefinedMetadata varchar (200),
    userDefinedCategory varchar (22),
    userDefinedType varchar (22) NOT NULL,
    tableName varchar (22) NOT NULL,
    PRIMARY KEY(USERDEFINED_ID)
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
    value varchar (10) NOT NULL,
    tableRecord_ID integer NOT NULL,
    PRIMARY KEY(DEFINEDVALUE_ID)
);

-----------------------------------------------------------------------------
-- project
-----------------------------------------------------------------------------
drop sequence project_PROJECT_ID_seq;
drop table project;

CREATE TABLE project
(
    PROJECT_ID serial,
    projectName varchar (22) NOT NULL,
    projectDescription varchar (200),
    startDate timestamp,
    stopDate timestamp,
    PRIMARY KEY(PROJECT_ID)
);

-----------------------------------------------------------------------------
-- party
-----------------------------------------------------------------------------
drop sequence party_PARTY_ID_seq;
drop table party;

CREATE TABLE party
(
    PARTY_ID serial,
    salutation varchar (22),
    givenName varchar (22),
    middleName varchar (22),
    surName varchar (22),
    organizationName varchar (22),
    currentName integer,
    email varchar (22),
    contactInstructions varchar (22),
    PRIMARY KEY(PARTY_ID)
);

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
    addressStartDate timestamp,
    orgPosition varchar (22),
    deliveryPoint varchar (22),
    city varchar (22),
    administrativeArea varchar (22),
    postalCode varchar (22),
    country varchar (22),
    currentFlag varchar (10),
    PRIMARY KEY(ADDRESS_ID)
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
    phoneNumber varchar (22) NOT NULL,
    phoneType varchar (22) NOT NULL,
    PRIMARY KEY(TELEPHONE_ID)
);

-----------------------------------------------------------------------------
-- aux_Soil
-----------------------------------------------------------------------------
drop sequence aux_Soil_SOIL_ID_seq;
drop table aux_Soil;

CREATE TABLE aux_Soil
(
    SOIL_ID serial,
    soilCode varchar (22),
    soilName varchar (22) NOT NULL,
    soilLevel integer NOT NULL,
    soilParent_ID integer,
    PRIMARY KEY(SOIL_ID)
);

-----------------------------------------------------------------------------
-- citation
-----------------------------------------------------------------------------
drop sequence citation_CITATION_ID_seq;
drop table citation;

CREATE TABLE citation
(
    CITATION_ID serial,
    authors varchar (22),
    title varchar (22),
    alternateTitle varchar (22),
    pubDate timestamp,
    edition varchar (22),
    seriesName varchar (22),
    issueIdentification varchar (22),
    otherCitationDetails varchar (22),
    page varchar (22),
    tableCited varchar (22),
    plotCited varchar (22),
    ISBN varchar (22),
    ISSN varchar (22),
    PRIMARY KEY(CITATION_ID)
);

-----------------------------------------------------------------------------
-- namedPlace
-----------------------------------------------------------------------------
drop sequence namedPlace_NAMEDPLACE_ID_seq;
drop table namedPlace;

CREATE TABLE namedPlace
(
    NAMEDPLACE_ID serial,
    placeName varchar (22) NOT NULL,
    placeDescription varchar (200),
    placeCode varchar (22),
    owner varchar (22),
    CITATION_ID integer,
    PRIMARY KEY(NAMEDPLACE_ID)
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
    coverType varchar (22) NOT NULL,
    PRIMARY KEY(COVERMETHOD_ID)
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
    coverCode varchar (22) NOT NULL,
    upperLimit varchar (10),
    lowerLimit varchar (10),
    coverPercent varchar (10) NOT NULL,
    indexDescription varchar (200),
    PRIMARY KEY(COVERINDEX_ID)
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
    stratumMethodName varchar (22) NOT NULL,
    stratumMethodDescription varchar (200),
    PRIMARY KEY(STRATUMMETHOD_ID)
);

-----------------------------------------------------------------------------
-- sampleMethod
-----------------------------------------------------------------------------
drop sequence sampleMethod_SAMPLEMETHOD_ID_seq;
drop table sampleMethod;

CREATE TABLE sampleMethod
(
    SAMPLEMETHOD_ID serial,
    sampleMethodName varchar (22) NOT NULL,
    sampleMethodDescription varchar (200),
    CITATION_ID integer,
    shape varchar (22),
    area varchar (10),
    areaAccuracy integer,
    dsgpoly varchar (200),
    permanent varchar (10),
    taxonInferenceArea varchar (10),
    subplotDispersion varchar (22),
    COVERMETHOD_ID integer,
    STRATUMMETHOD_ID integer,
    autoTaxonCover varchar (10) NOT NULL,
    taxonInferenceArea varchar (10),
    stemSampleMethod varchar (22),
    stemInferenceArea varchar (10),
    PRIMARY KEY(SAMPLEMETHOD_ID)
);

-----------------------------------------------------------------------------
-- plot
-----------------------------------------------------------------------------
drop sequence plot_PLOT_ID_seq;
drop table plot;

CREATE TABLE plot
(
    PLOT_ID serial,
    PROJECT_ID integer,
    authorPlotCode varchar (22) NOT NULL,
    CITATION_ID integer,
    PARENT_ID integer,
    SAMPLEMETHOD_ID integer,
    latitude varchar (10) NOT NULL,
    longitude varchar (10) NOT NULL,
    locationAccuracy varchar (10),
    authorE varchar (22),
    authorN varchar (22),
    authorZone varchar (22),
    authorDatum varchar (22),
    authorLocation varchar (22),
    locationNarrative varchar (200),
    GEOGRAPHY_ID integer,
    ecoregion varchar (22),
    USGSQUAD_ID integer,
    locationConfidentiality varchar (22) NOT NULL,
    ownerConfidentiality varchar (22) NOT NULL,
    azimuth varchar (10),
    dsgpoly varchar (22),
    shape varchar (22),
    area varchar (10) NOT NULL,
    areaAccuracy varchar (10),
    standSize varchar (22),
    placementMethod varchar (200),
    permanent varchar (10),
    layoutNarative varchar (200),
    elevation varchar (10),
    elevationAccuracy varchar (10),
    slopeAspect varchar (10),
    slopeGradient varchar (10),
    topoPosition varchar (22),
    landform varchar (22),
    geology varchar (22),
    SOIL_ID integer,
    soilTaxonSource varchar (22),
    soilTaxonAccuracy varchar (10),
    PRIMARY KEY(PLOT_ID)
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
    NAMEDPLACE_ID integer NOT NULL,
    PRIMARY KEY(PLOTPLACE_ID)
);

-----------------------------------------------------------------------------
-- observation
-----------------------------------------------------------------------------
drop sequence observation_OBSERVATION_ID_seq;
drop table observation;

CREATE TABLE observation
(
    OBSERVATION_ID serial,
    PLOT_ID integer NOT NULL,
    PREVIOUSOBS_ID integer,
    authorObsCode varchar (22),
    obsStartDate timestamp,
    obsEndDate timestamp,
    dateAccuracy varchar (22),
    SAMPLEMETHOD_ID integer,
    COVERMETHOD_ID integer NOT NULL,
    STRATUMMETHOD_ID integer NOT NULL,
    methodNarrative varchar (200),
    taxonInferenceArea varchar (10) NOT NULL,
    coverDispersion varchar (22) NOT NULL,
    coverAreaAccuracy varchar (10),
    autoTaxonCover varchar (10) NOT NULL,
    stemInferenceArea varchar (10),
    stemSampleMethod varchar (22),
    hardCopyLocation varchar (200),
    effortLevel varchar (22),
    plotQuality varchar (22),
    floristicQuality varchar (22),
    bryophyteQuality varchar (22),
    lichenQuality varchar (22),
    observationNarrative varchar (200),
    landscapeNarrative varchar (200),
    homogeneity varchar (22),
    phenologicAspect varchar (22),
    representativeness varchar (22),
    basalArea varchar (10),
    hydrologicRegime varchar (22),
    soilMoistureRegime varchar (22),
    soilDrainage varchar (22),
    soilSalinity varchar (22),
    waterDepth varchar (10),
    shoreDistance varchar (10),
    soilDepth varchar (10),
    soilDepthRange varchar (10),
    organicDepth varchar (10),
    humusType varchar (22),
    percentBedRock varchar (10),
    percentRockGravel varchar (10),
    percentWood varchar (10),
    percentLitter varchar (10),
    percentBareSoil varchar (10),
    percentWater varchar (10),
    percentOther varchar (10),
    nameOther varchar (22),
    standAge varchar (10),
    successionalStatus varchar (200),
    vegetationOrigin varchar (22),
    fireReturnInterval varchar (10),
    treeHt varchar (10),
    shrubHt varchar (10),
    herbHt varchar (10),
    nonvascularHt varchar (10),
    submergedHt varchar (10),
    treeCover varchar (10),
    shrubCover varchar (10),
    herbCover varchar (10),
    nonvascularCover varchar (10),
    floatingCover varchar (10),
    submergedCover varchar (10),
    dominantStratum varchar (22),
    growthform1Type varchar (22),
    growthform2Type varchar (22),
    growthform3Type varchar (22),
    growthform1Cover varchar (10),
    growthform2Cover varchar (10),
    growthform3Cover varchar (10),
    PRIMARY KEY(OBSERVATION_ID)
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
    graphicName varchar (22),
    graphicLocation varchar (200),
    graphicDescription varchar (200),
    graphicType varchar (22),
    graphicDate timestamp,
    graphicData varchar (10),
    PRIMARY KEY(GRAPHIC_ID)
);

-----------------------------------------------------------------------------
-- stratumType
-----------------------------------------------------------------------------
drop sequence stratumType_STRATUMTYPE_ID_seq;
drop table stratumType;

CREATE TABLE stratumType
(
    STRATUMTYPE_ID serial,
    STRATUMMETHOD_ID integer NOT NULL,
    stratumName varchar (22) NOT NULL,
    stratumDescription varchar (200),
    PRIMARY KEY(STRATUMTYPE_ID)
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
    taxonStratumCover varchar (10) NOT NULL,
    PRIMARY KEY(STRATUMCOMPOSITION_ID)
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
    PLANTNAME_ID integer NOT NULL,
    PLANTREFERENCE_ID integer NOT NULL,
    taxonCollection varchar (22),
    taxonCover varchar (10),
    taxonBasalArea varchar (10),
    taxonInferenceArea varchar (10),
    PRIMARY KEY(TAXONOBSERVATION_ID)
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
    interpretationType varchar (22),
    CITATION_ID integer,
    originalInterpretation varchar (10) NOT NULL,
    currentInterpretation varchar (10) NOT NULL,
    notes varchar (200),
    PRIMARY KEY(TAXONINTERPRETATION_ID)
);

-----------------------------------------------------------------------------
-- treeStem
-----------------------------------------------------------------------------
drop sequence treeStem_STEM_ID_seq;
drop table treeStem;

CREATE TABLE treeStem
(
    STEM_ID serial,
    TAXONOBSERVATION_ID integer NOT NULL,
    stemCode varchar (22),
    stemDiameter varchar (10),
    stemDiameterAccuracy varchar (10),
    stemXPosition varchar (10),
    stemYPosition varchar (10),
    stemHeight varchar (10),
    stemHeightAccuracy varchar (10),
    stemCount integer NOT NULL,
    PRIMARY KEY(STEM_ID)
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
    soilHorizon varchar (22) NOT NULL,
    soilDepthTop varchar (10),
    soilDepthBottom varchar (10),
    soilColor varchar (22),
    soilOrganic varchar (10),
    soilTexture varchar (22),
    soilSand varchar (10),
    soilSilt varchar (10),
    soilClay varchar (10),
    soilCoarse varchar (10),
    soilPH varchar (10),
    exchanceCapacity varchar (10),
    baseSaturation varchar (10),
    soilDescription varchar (200),
    PRIMARY KEY(SOILOBS_ID)
);

-----------------------------------------------------------------------------
-- aux_Geography
-----------------------------------------------------------------------------
drop sequence aux_Geography_GEOGRAPHY_ID_seq;
drop table aux_Geography;

CREATE TABLE aux_Geography
(
    GEOGRAPHY_ID serial,
    geographyCode varchar (22),
    geographyName varchar (22) NOT NULL,
    geographyLevel integer NOT NULL,
    geographyParent_ID integer,
    PRIMARY KEY(GEOGRAPHY_ID)
);

-----------------------------------------------------------------------------
-- aux_Role
-----------------------------------------------------------------------------
drop sequence aux_Role_ROLE_ID_seq;
drop table aux_Role;

CREATE TABLE aux_Role
(
    ROLE_ID serial,
    roleCode varchar (22) NOT NULL,
    roleDescription varchar (22),
    roleProject integer,
    roleObservation integer,
    roleTaxonInt integer,
    roleClassInt integer,
    PRIMARY KEY(ROLE_ID)
);

-----------------------------------------------------------------------------
-- noteLink
-----------------------------------------------------------------------------
drop sequence noteLink_NOTELINK_ID_seq;
drop table noteLink;

CREATE TABLE noteLink
(
    NOTELINK_ID serial,
    tableName varchar (22) NOT NULL,
    attributeName varchar (22) NOT NULL,
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
    noteDate timestamp,
    noteType varchar (22) NOT NULL,
    noteText varchar (200) NOT NULL,
    PRIMARY KEY(NOTE_ID)
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
    ROLE_ID integer NOT NULL,
    PRIMARY KEY(PROJECTCONTRIBUTOR_ID)
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
-- disturbanceObs
-----------------------------------------------------------------------------
drop sequence disturbanceObs_disturbanceObs_seq;
drop table disturbanceObs;

CREATE TABLE disturbanceObs
(
    disturbanceObs serial,
    OBSERVATION_ID integer NOT NULL,
    disturbanceType varchar (22) NOT NULL,
    disturbanceIntensity varchar (22),
    diosturbanceAge varchar (10),
    disturbanceExtent varchar (10),
    disturbanceComment varchar (200),
    PRIMARY KEY(disturbanceObs)
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
    stratumHeight varchar (10),
    stratumBase varchar (10),
    stratumCover varchar (10),
    PRIMARY KEY(STRATUM_ID)
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
    COMMCONCEPT_ID integer NOT NULL,
    classFit varchar (22),
    classConfidence varchar (22),
    commAuthority_ID integer,
    notes varchar (200),
    type varchar (10),
    PRIMARY KEY(COMMINTERPRETATION_ID)
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
    inspection varchar (10),
    tableAnalysis varchar (10),
    multivariateAnalysis varchar (10),
    expertSystem varchar (10),
    classPublication_ID integer,
    classNotes varchar (200),
    PRIMARY KEY(COMMCLASS_ID)
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
-- aux_usgsQuad
-----------------------------------------------------------------------------
drop sequence aux_usgsQuad_USGSQUAD_ID_seq;
drop table aux_usgsQuad;

CREATE TABLE aux_usgsQuad
(
    USGSQUAD_ID serial,
    MapName varchar (22) NOT NULL,
    State varchar (22) NOT NULL,
    Type varchar (22),
    Scale integer NOT NULL,
    Date timestamp NOT NULL,
    ISBN varchar (22) NOT NULL,
    NW_lat varchar (10) NOT NULL,
    NW_long varchar (10) NOT NULL,
    NE_lat varchar (10) NOT NULL,
    NE_lon varchar (10) NOT NULL,
    SE_lat varchar (10) NOT NULL,
    SE_lon varchar (10) NOT NULL,
    SW_lat varchar (10) NOT NULL,
    SW_lon varchar (10) NOT NULL,
    PRIMARY KEY(USGSQUAD_ID)
);
----------------------------------------------------------------------------
-- aux_usgsQuad
----------------------------------------------------------------------------

ALTER TABLE revision
    ADD CONSTRAINT previousRevision_ID FOREIGN KEY (previousRevision_ID)
    REFERENCES revision (REVISION_ID);

----------------------------------------------------------------------------
-- revision
----------------------------------------------------------------------------

----------------------------------------------------------------------------
-- userDefined
----------------------------------------------------------------------------

ALTER TABLE definedValue
    ADD CONSTRAINT USERDEFINED_ID FOREIGN KEY (USERDEFINED_ID)
    REFERENCES userDefined (USERDEFINED_ID);

----------------------------------------------------------------------------
-- definedValue
----------------------------------------------------------------------------

----------------------------------------------------------------------------
-- project
----------------------------------------------------------------------------

ALTER TABLE party
    ADD CONSTRAINT currentName FOREIGN KEY (currentName)
    REFERENCES party (PARTY_ID);

----------------------------------------------------------------------------
-- party
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

ALTER TABLE telephone
    ADD CONSTRAINT PARTY_ID FOREIGN KEY (PARTY_ID)
    REFERENCES party (PARTY_ID);

----------------------------------------------------------------------------
-- telephone
----------------------------------------------------------------------------

----------------------------------------------------------------------------
-- aux_Soil
----------------------------------------------------------------------------

----------------------------------------------------------------------------
-- citation
----------------------------------------------------------------------------

ALTER TABLE namedPlace
    ADD CONSTRAINT CITATION_ID FOREIGN KEY (CITATION_ID)
    REFERENCES citation (CITATION_ID);

----------------------------------------------------------------------------
-- namedPlace
----------------------------------------------------------------------------

ALTER TABLE coverMethod
    ADD CONSTRAINT CITATION_ID FOREIGN KEY (CITATION_ID)
    REFERENCES citation (CITATION_ID);

----------------------------------------------------------------------------
-- coverMethod
----------------------------------------------------------------------------

ALTER TABLE coverIndex
    ADD CONSTRAINT COVERMETHOD_ID FOREIGN KEY (COVERMETHOD_ID)
    REFERENCES coverMethod (COVERMETHOD_ID);

----------------------------------------------------------------------------
-- coverIndex
----------------------------------------------------------------------------

ALTER TABLE stratumMethod
    ADD CONSTRAINT CITATION_ID FOREIGN KEY (CITATION_ID)
    REFERENCES citation (CITATION_ID);

----------------------------------------------------------------------------
-- stratumMethod
----------------------------------------------------------------------------

ALTER TABLE sampleMethod
    ADD CONSTRAINT CITATION_ID FOREIGN KEY (CITATION_ID)
    REFERENCES citation (CITATION_ID);

ALTER TABLE sampleMethod
    ADD CONSTRAINT COVERMETHOD_ID FOREIGN KEY (COVERMETHOD_ID)
    REFERENCES coverMethod (COVERMETHOD_ID);

ALTER TABLE sampleMethod
    ADD CONSTRAINT STRATUMMETHOD_ID FOREIGN KEY (STRATUMMETHOD_ID)
    REFERENCES stratumMethod (STRATUMMETHOD_ID);

----------------------------------------------------------------------------
-- sampleMethod
----------------------------------------------------------------------------

ALTER TABLE plot
    ADD CONSTRAINT PROJECT_ID FOREIGN KEY (PROJECT_ID)
    REFERENCES project (project_ID);

ALTER TABLE plot
    ADD CONSTRAINT CITATION_ID FOREIGN KEY (CITATION_ID)
    REFERENCES citation (CITATION_ID);

ALTER TABLE plot
    ADD CONSTRAINT PARENT_ID FOREIGN KEY (PARENT_ID)
    REFERENCES plot (PLOT_ID);

ALTER TABLE plot
    ADD CONSTRAINT SAMPLEMETHOD_ID FOREIGN KEY (SAMPLEMETHOD_ID)
    REFERENCES sampleMethod (SAMPLEMETHOD_ID);

ALTER TABLE plot
    ADD CONSTRAINT GEOGRAPHY_ID FOREIGN KEY (GEOGRAPHY_ID)
    REFERENCES aux_Geography (GEOGRAPHY_ID);

ALTER TABLE plot
    ADD CONSTRAINT USGSQUAD_ID FOREIGN KEY (USGSQUAD_ID)
    REFERENCES aux_usgsQuad (USGSQUAD_ID);

ALTER TABLE plot
    ADD CONSTRAINT SOIL_ID FOREIGN KEY (SOIL_ID)
    REFERENCES aux_soil (SOIL_ID);

----------------------------------------------------------------------------
-- plot
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

ALTER TABLE observation
    ADD CONSTRAINT PLOT_ID FOREIGN KEY (PLOT_ID)
    REFERENCES plot (PLOT_ID);

ALTER TABLE observation
    ADD CONSTRAINT PREVIOUSOBS_ID FOREIGN KEY (PREVIOUSOBS_ID)
    REFERENCES observation (OBSERVATION_ID);

ALTER TABLE observation
    ADD CONSTRAINT SAMPLEMETHOD_ID FOREIGN KEY (SAMPLEMETHOD_ID)
    REFERENCES sampleMethod (SAMPLEMETHOD_ID);

ALTER TABLE observation
    ADD CONSTRAINT COVERMETHOD_ID FOREIGN KEY (COVERMETHOD_ID)
    REFERENCES coverMethod (COVERMETHOD_ID);

ALTER TABLE observation
    ADD CONSTRAINT STRATUMMETHOD_ID FOREIGN KEY (STRATUMMETHOD_ID)
    REFERENCES stratumMethod (STRATUMMETHOD_ID);

----------------------------------------------------------------------------
-- observation
----------------------------------------------------------------------------

ALTER TABLE graphic
    ADD CONSTRAINT OBSERVATION_ID FOREIGN KEY (OBSERVATION_ID)
    REFERENCES observation (OBSERVATION_ID);

----------------------------------------------------------------------------
-- graphic
----------------------------------------------------------------------------

ALTER TABLE stratumType
    ADD CONSTRAINT STRATUMMETHOD_ID FOREIGN KEY (STRATUMMETHOD_ID)
    REFERENCES stratumMethod (STRATUMMETHOD_ID);

----------------------------------------------------------------------------
-- stratumType
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

ALTER TABLE treeStem
    ADD CONSTRAINT TAXONOBSERVATION_ID FOREIGN KEY (TAXONOBSERVATION_ID)
    REFERENCES taxonObservation (TAXONOBSERVATION_ID);

----------------------------------------------------------------------------
-- treeStem
----------------------------------------------------------------------------

ALTER TABLE soilObs
    ADD CONSTRAINT OBSERVATION_ID FOREIGN KEY (OBSERVATION_ID)
    REFERENCES observation (OBSERVATION_ID);

----------------------------------------------------------------------------
-- soilObs
----------------------------------------------------------------------------

----------------------------------------------------------------------------
-- aux_Geography
----------------------------------------------------------------------------

----------------------------------------------------------------------------
-- aux_Role
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

----------------------------------------------------------------------------
-- Note
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

ALTER TABLE disturbanceObs
    ADD CONSTRAINT OBSERVATION_ID FOREIGN KEY (OBSERVATION_ID)
    REFERENCES observation (OBSERVATION_ID);

----------------------------------------------------------------------------
-- disturbanceObs
----------------------------------------------------------------------------

ALTER TABLE stratum
    ADD CONSTRAINT OBSERVATION_ID FOREIGN KEY (OBSERVATION_ID)
    REFERENCES observation (OBSEVATION_ID);

ALTER TABLE stratum
    ADD CONSTRAINT STRATUMTYPE_ID FOREIGN KEY (STRATUMTYPE_ID)
    REFERENCES stratumType (STRATUMTYPE_ID);

----------------------------------------------------------------------------
-- stratum
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

ALTER TABLE commClass
    ADD CONSTRAINT OBSERVATION_ID FOREIGN KEY (OBSERVATION_ID)
    REFERENCES observation (OBSERVATION_ID);

ALTER TABLE commClass
    ADD CONSTRAINT classPublication_ID FOREIGN KEY (classPublication_ID)
    REFERENCES citation (CITATION_ID);

----------------------------------------------------------------------------
-- commClass
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

