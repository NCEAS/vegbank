
-----------------------------------------------------------------------------
-- plantUsage
-----------------------------------------------------------------------------
drop sequence plantUsage_PLANTUSAGE_ID_seq;
drop table plantUsage;

CREATE TABLE plantUsage
(
    PLANTUSAGE_ID serial,
    PLANTNAME_ID integer NOT NULL,
    PLANTCONCEPT_ID integer NOT NULL,
    initialUsage varchar (10) NOT NULL,
    usageStart timestamp NOT NULL,
    usageStop timestamp NOT NULL,
    plantNameStatus varchar (22) NOT NULL,
    PLANTPARTY_ID integer NOT NULL,
    classSystem varchar (22),
    PRIMARY KEY(PLANTUSAGE_ID)
);

-----------------------------------------------------------------------------
-- plantStatus
-----------------------------------------------------------------------------
drop sequence plantStatus_PLANTSTATUS_ID_seq;
drop table plantStatus;

CREATE TABLE plantStatus
(
    PLANTSTATUS_ID serial,
    PLANTCONCEPT_ID integer NOT NULL,
    PLANTREFERENCE_ID integer,
    plantConceptStatus varchar (22) NOT NULL,
    startDate timestamp NOT NULL,
    stopDate timestamp,
    plantPartyComments varchar (22),
    PLANTPARTY_ID integer NOT NULL,
    PRIMARY KEY(PLANTSTATUS_ID)
);

-----------------------------------------------------------------------------
-- plantReference
-----------------------------------------------------------------------------
drop sequence plantReference_PLANTREFERENCE_ID_seq;
drop table plantReference;

CREATE TABLE plantReference
(
    PLANTREFERENCE_ID serial,
    authors varchar (22),
    title varchar (22),
    pubDate timestamp,
    edition varchar (22),
    seriesName varchar (22),
    issueIdentification varchar (22),
    otherCitationDetails varchar (22),
    page varchar (22),
    tableCited varchar (22),
    ISBN varchar (22),
    ISSN varchar (22),
    PRIMARY KEY(PLANTREFERENCE_ID)
);

-----------------------------------------------------------------------------
-- plantName
-----------------------------------------------------------------------------
drop sequence plantName_PLANTNAME_ID_seq;
drop table plantName;

CREATE TABLE plantName
(
    PLANTNAME_ID serial,
    plantName varchar (22) NOT NULL,
    plantAuthorName varchar (22),
    PLANTREFERENCE_ID integer NOT NULL,
    dateEntered timestamp,
    PRIMARY KEY(PLANTNAME_ID)
);

-----------------------------------------------------------------------------
-- plantLineation
-----------------------------------------------------------------------------
drop sequence plantLineation_PLANTLINEAGE_ID_seq;
drop table plantLineation;

CREATE TABLE plantLineation
(
    PLANTLINEAGE_ID serial,
    PLANTSTATUS1_ID integer,
    PLANTSTATUS2_ID integer,
    PRIMARY KEY(PLANTLINEAGE_ID)
);

-----------------------------------------------------------------------------
-- plantDescription
-----------------------------------------------------------------------------
drop sequence plantDescription_DESCRIPTION_ID_seq;
drop table plantDescription;

CREATE TABLE plantDescription
(
    DESCRIPTION_ID serial,
    PLANTCONCEPT_ID integer NOT NULL,
    plantDescription varchar (200) NOT NULL,
    PRIMARY KEY(DESCRIPTION_ID)
);

-----------------------------------------------------------------------------
-- plantCorrelation
-----------------------------------------------------------------------------
drop sequence plantCorrelation_PLANTCORRELATION_ID_seq;
drop table plantCorrelation;

CREATE TABLE plantCorrelation
(
    PLANTCORRELATION_ID serial,
    PLANTSTATUS_ID integer NOT NULL,
    PLANTCONCEPT_ID integer NOT NULL,
    plantConvergence varchar (22) NOT NULL,
    correlationStart timestamp NOT NULL,
    correlationStop timestamp NOT NULL,
    PRIMARY KEY(PLANTCORRELATION_ID)
);

-----------------------------------------------------------------------------
-- plantConcept
-----------------------------------------------------------------------------
drop sequence plantConcept_PLANTCONCEPT_ID_seq;
drop table plantConcept;

CREATE TABLE plantConcept
(
    PLANTCONCEPT_ID serial,
    PLANTNAME_ID integer NOT NULL,
    PLANTREFERENCE_ID integer NOT NULL,
    plantLevel varchar (22),
    plantParent integer,
    PRIMARY KEY(PLANTCONCEPT_ID)
);

-----------------------------------------------------------------------------
-- plantParty
-----------------------------------------------------------------------------
drop sequence plantParty_PLANTPARTY_ID_seq;
drop table plantParty;

CREATE TABLE plantParty
(
    PLANTPARTY_ID serial,
    plantPartyName varchar (22) NOT NULL,
    plantPartyInfo varchar (22),
    PRIMARY KEY(PLANTPARTY_ID)
);
----------------------------------------------------------------------------
-- plantParty
----------------------------------------------------------------------------

ALTER TABLE plantUsage
    ADD CONSTRAINT PLANTNAME_ID FOREIGN KEY (PLANTNAME_ID)
    REFERENCES plantName (PLANTNAME_ID);

ALTER TABLE plantUsage
    ADD CONSTRAINT PLANTCONCEPT_ID FOREIGN KEY (PLANTCONCEPT_ID)
    REFERENCES plantConcept (PLANTCONCEPT_ID);

ALTER TABLE plantUsage
    ADD CONSTRAINT PLANTPARTY_ID FOREIGN KEY (PLANTPARTY_ID)
    REFERENCES plantParty (PLANTPARTY_ID);

----------------------------------------------------------------------------
-- plantUsage
----------------------------------------------------------------------------

ALTER TABLE plantStatus
    ADD CONSTRAINT PLANTCONCEPT_ID FOREIGN KEY (PLANTCONCEPT_ID)
    REFERENCES plantConcept (PLANTCONCEPT_ID);

ALTER TABLE plantStatus
    ADD CONSTRAINT PLANTREFERENCE_ID FOREIGN KEY (PLANTREFERENCE_ID)
    REFERENCES plantReference (PLANTREFERENCE_ID);

ALTER TABLE plantStatus
    ADD CONSTRAINT PLANTPARTY_ID FOREIGN KEY (PLANTPARTY_ID)
    REFERENCES plantParty (PLANTPARTY_ID);

----------------------------------------------------------------------------
-- plantStatus
----------------------------------------------------------------------------

----------------------------------------------------------------------------
-- plantReference
----------------------------------------------------------------------------

ALTER TABLE plantName
    ADD CONSTRAINT PLANTREFERENCE_ID FOREIGN KEY (PLANTREFERENCE_ID)
    REFERENCES plantReference (PLANTREFERENCE_ID);

----------------------------------------------------------------------------
-- plantName
----------------------------------------------------------------------------

ALTER TABLE plantLineation
    ADD CONSTRAINT PLANTSTATUS1_ID FOREIGN KEY (PLANTSTATUS1_ID)
    REFERENCES plantStatus (PLANTSTATUS_ID);

ALTER TABLE plantLineation
    ADD CONSTRAINT PLANTSTATUS2_ID FOREIGN KEY (PLANTSTATUS2_ID)
    REFERENCES plantStatus (PLANTSTATUS_ID);

----------------------------------------------------------------------------
-- plantLineation
----------------------------------------------------------------------------

ALTER TABLE plantDescription
    ADD CONSTRAINT PLANTCONCEPT_ID FOREIGN KEY (PLANTCONCEPT_ID)
    REFERENCES plantConcept (PLANTCONCEPT_ID);

----------------------------------------------------------------------------
-- plantDescription
----------------------------------------------------------------------------

ALTER TABLE plantCorrelation
    ADD CONSTRAINT PLANTSTATUS_ID FOREIGN KEY (PLANTSTATUS_ID)
    REFERENCES plantStatus (PLANTSTATUS_ID);

ALTER TABLE plantCorrelation
    ADD CONSTRAINT PLANTCONCEPT_ID FOREIGN KEY (PLANTCONCEPT_ID)
    REFERENCES plantConcept (PLANTCONCEPT_ID);

----------------------------------------------------------------------------
-- plantCorrelation
----------------------------------------------------------------------------

ALTER TABLE plantConcept
    ADD CONSTRAINT PLANTNAME_ID FOREIGN KEY (PLANTNAME_ID)
    REFERENCES plantName (PLANTNAME_ID);

ALTER TABLE plantConcept
    ADD CONSTRAINT PLANTREFERENCE_ID FOREIGN KEY (PLANTREFERENCE_ID)
    REFERENCES plantReference (PLANTREFERENCE_ID);

ALTER TABLE plantConcept
    ADD CONSTRAINT plantParent FOREIGN KEY (plantParent)
    REFERENCES plantConcept (PLANTCONCEPT_ID);

----------------------------------------------------------------------------
-- plantConcept
----------------------------------------------------------------------------

