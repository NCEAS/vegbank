
-----------------------------------------------------------------------------
-- commUsage
-----------------------------------------------------------------------------
drop sequence commUsage_COMMUSAGE_ID_seq;
drop table commUsage;

CREATE TABLE commUsage
(
    COMMUSAGE_ID serial,
    COMMNAME_ID integer NOT NULL,
    COMMCONCEPT_ID integer NOT NULL,
    usageStart timestamp ,
    usageStop timestamp,
    commNameStatus varchar (22) ,
    COMMPARTY_ID integer ,
    classSystem varchar (22),
    PRIMARY KEY(COMMUSAGE_ID)
);

-----------------------------------------------------------------------------
-- commStatus
-----------------------------------------------------------------------------
drop sequence commStatus_COMMSTATUS_ID_seq;
drop table commStatus;

CREATE TABLE commStatus
(
    COMMSTATUS_ID serial,
    COMMCONCEPT_ID integer NOT NULL,
    COMMREFERENCE_ID integer,
    commConceptStatus varchar (22) NOT NULL,
    startDate timestamp NOT NULL,
    stopDate timestamp,
    partyComments varchar (22),
    COMMPARTY_ID integer NOT NULL,
    PRIMARY KEY(COMMSTATUS_ID)
);

-----------------------------------------------------------------------------
-- commReference
-----------------------------------------------------------------------------
drop sequence commReference_COMMREFERENCE_ID_seq;
drop table commReference;

CREATE TABLE commReference
(
    COMMREFERENCE_ID serial,
    authors varchar (22),
    title varchar (22),
    pubDate timestamp,
    edition varchar (22),
    seriesName varchar (22),
    issueIdentification varchar (22),
    otherCitationDetails varchar (300),
    page varchar (22),
    tableCited varchar (22),
    ISBN varchar (22),
    ISSN varchar (22),
    commDescription varchar (200),
    PRIMARY KEY(COMMREFERENCE_ID)
);

-----------------------------------------------------------------------------
-- commName
-----------------------------------------------------------------------------
drop sequence commName_COMMNAME_ID_seq;
drop table commName;

CREATE TABLE commName
(
    COMMNAME_ID serial,
    commName varchar (3000) NOT NULL,
    COMMREFERENCE_ID integer NOT NULL,
    dateEntered timestamp,
    PRIMARY KEY(COMMNAME_ID)
);

-----------------------------------------------------------------------------
-- commLineage
-----------------------------------------------------------------------------
drop sequence commLineage_COMMLINEAGE_ID_seq;
drop table commLineage;

CREATE TABLE commLineage
(
    COMMLINEAGE_ID serial,
    COMMSTATUS1_ID integer NOT NULL,
    COMMSTATUS2_ID integer NOT NULL,
    PRIMARY KEY(COMMLINEAGE_ID)
);

-----------------------------------------------------------------------------
-- commCorrelation
-----------------------------------------------------------------------------
drop sequence commCorrelation_COMMCORRELATION_ID_seq;
drop table commCorrelation;

CREATE TABLE commCorrelation
(
    COMMCORRELATION_ID serial,
    COMMSTATUS_ID integer,
    COMMCONCEPT_ID integer,
    commConvergence varchar (22),
    correlationStart timestamp,
    correlationStop timestamp,
    PRIMARY KEY(COMMCORRELATION_ID)
);

-----------------------------------------------------------------------------
-- commConcept
-----------------------------------------------------------------------------
drop sequence commConcept_COMMCONCEPT_ID_seq;
drop table commConcept;

CREATE TABLE commConcept
(
    COMMCONCEPT_ID serial,
    COMMNAME_ID integer NOT NULL,
    COMMREFERENCE_ID integer,
    ceglCode varchar (22),
    commLevel varchar (22),
    commParent integer,
		conceptDescription varchar (200),
    PRIMARY KEY(COMMCONCEPT_ID)
);

-----------------------------------------------------------------------------
-- commParty
-----------------------------------------------------------------------------
drop sequence commParty_COMMPARTY_ID_seq;
drop table commParty;

CREATE TABLE commParty
(
    COMMPARTY_ID serial,
    salutation varchar (22),
    givenName varchar (22),
    middleName varchar (22),
    surName varchar (22),
    organizationName varchar (22),
    currentName integer,
    contactInstructions varchar (22),
    owner integer,
    PRIMARY KEY(COMMPARTY_ID)
);
----------------------------------------------------------------------------
-- commParty
----------------------------------------------------------------------------

ALTER TABLE commUsage
    ADD CONSTRAINT COMMNAME_ID FOREIGN KEY (COMMNAME_ID)
    REFERENCES commName (COMMNAME_ID);

ALTER TABLE commUsage
    ADD CONSTRAINT COMMCONCEPT_ID FOREIGN KEY (COMMCONCEPT_ID)
    REFERENCES commConcept (COMMCONCEPT_ID);

ALTER TABLE commUsage
    ADD CONSTRAINT COMMPARTY_ID FOREIGN KEY (COMMPARTY_ID)
    REFERENCES commParty (COMMPARTY_ID);

----------------------------------------------------------------------------
-- commUsage
----------------------------------------------------------------------------

ALTER TABLE commStatus
    ADD CONSTRAINT COMMCONCEPT_ID FOREIGN KEY (COMMCONCEPT_ID)
    REFERENCES commConcept (COMMCONCEPT_ID);

ALTER TABLE commStatus
    ADD CONSTRAINT COMMREFERENCE_ID FOREIGN KEY (COMMREFERENCE_ID)
    REFERENCES commReference (COMMREFERENCE_ID);

ALTER TABLE commStatus
    ADD CONSTRAINT COMMPARTY_ID FOREIGN KEY (COMMPARTY_ID)
    REFERENCES commParty (COMMPARTY_ID);

----------------------------------------------------------------------------
-- commStatus
----------------------------------------------------------------------------

----------------------------------------------------------------------------
-- commReference
----------------------------------------------------------------------------

ALTER TABLE commName
    ADD CONSTRAINT COMMREFERENCE_ID FOREIGN KEY (COMMREFERENCE_ID)
    REFERENCES commReference (COMMREFERENCE_ID);

----------------------------------------------------------------------------
-- commName
----------------------------------------------------------------------------

ALTER TABLE commLineage
    ADD CONSTRAINT COMMSTATUS1_ID FOREIGN KEY (COMMSTATUS1_ID)
    REFERENCES commStatus (COMMSTATUS_ID);

ALTER TABLE commLineage
    ADD CONSTRAINT COMMSTATUS2_ID FOREIGN KEY (COMMSTATUS2_ID)
    REFERENCES commStatus (COMMSTATUS_ID);

----------------------------------------------------------------------------
-- commLineage
----------------------------------------------------------------------------

ALTER TABLE commCorrelation
    ADD CONSTRAINT COMMSTATUS_ID FOREIGN KEY (COMMSTATUS_ID)
    REFERENCES commStatus (COMMSTATUS_ID);

ALTER TABLE commCorrelation
    ADD CONSTRAINT COMMCONCEPT_ID FOREIGN KEY (COMMCONCEPT_ID)
    REFERENCES commConcept (COMMCONCEPT_ID);

----------------------------------------------------------------------------
-- commCorrelation
----------------------------------------------------------------------------

ALTER TABLE commConcept
    ADD CONSTRAINT COMMNAME_ID FOREIGN KEY (COMMNAME_ID)
    REFERENCES commName (COMMNAME_ID);

ALTER TABLE commConcept
    ADD CONSTRAINT COMMREFERENCE_ID FOREIGN KEY (COMMREFERENCE_ID)
    REFERENCES commReference (COMMREFERENCE_ID);

ALTER TABLE commConcept
    ADD CONSTRAINT commParent FOREIGN KEY (commParent)
    REFERENCES commConcept (COMMCONCEPT_ID);

----------------------------------------------------------------------------
-- commConcept
----------------------------------------------------------------------------

ALTER TABLE commParty
    ADD CONSTRAINT currentName FOREIGN KEY (currentName)
    REFERENCES commParty (COMMPARTY_ID);

ALTER TABLE commParty
    ADD CONSTRAINT owner FOREIGN KEY (owner)
    REFERENCES commParty (COMMPARTY_ID);

