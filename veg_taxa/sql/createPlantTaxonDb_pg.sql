
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
    usageStart timestamp ,
    usageStop timestamp,
    plantNameStatus varchar (22) ,
    plantName varchar (220),
    PLANTPARTY_ID integer ,
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
    plantParentName varchar (200),
plantParentConcept_id integer,
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
    plantDescription varchar (200),
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
    plantName varchar (220) NOT NULL,
    plantNameWithAuthor varchar (222),
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
	plantname varchar(200),
	plantCode varchar(23),
	plantDescription varchar(600),
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
    salutation varchar (22),
    givenName varchar (22),
    middleName varchar (22),
    surName varchar (22),
    organizationName varchar (22),
    currentName integer,
    contactInstructions varchar (22),
    owner integer,
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

ALTER TABLE plantParty
    ADD CONSTRAINT currentName FOREIGN KEY (currentName)
    REFERENCES plantParty (PLANTPARTY_ID);

ALTER TABLE plantParty
    ADD CONSTRAINT owner FOREIGN KEY (owner)
    REFERENCES plantParty (PLANTPARTY_ID);

