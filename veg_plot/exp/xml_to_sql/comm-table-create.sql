
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
    usageStart timestamp NOT NULL,
    usageStop timestamp NOT NULL,
    commNameStatus varchar (22) NOT NULL,
    party integer NOT NULL,
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
    otherCitationDetails varchar (22),
    page varchar (22),
    tableCited varchar (22),
    PrimaryKey integer,
    ISBN varchar (22),
    ISSN varchar (22),
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
    commName varchar (22) NOT NULL,
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
-- commDescription
-----------------------------------------------------------------------------
drop sequence commDescription_COMMDESCRIPTION_ID_seq;
drop table commDescription;

CREATE TABLE commDescription
(
    COMMDESCRIPTION_ID serial,
    COMMCONCEPT_ID integer NOT NULL,
    commDescription varchar (200) NOT NULL,
    supportingFields varchar (200),
    PRIMARY KEY(COMMDESCRIPTION_ID)
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
    CEGLcode varchar (22),
    commLevel varchar (22),
    commParent integer,
    party varchar (22),
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
    commPartyName varchar (22) NOT NULL,
    commPartyInfo varchar (22),
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
    ADD CONSTRAINT party FOREIGN KEY (party)
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

ALTER TABLE commReference
    ADD CONSTRAINT PrimaryKey FOREIGN KEY (PrimaryKey)
    REFERENCES commDescription (COMMDESCRIPTION_ID);

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

ALTER TABLE commDescription
    ADD CONSTRAINT COMMCONCEPT_ID FOREIGN KEY (COMMCONCEPT_ID)
    REFERENCES commConcept (COMMCONCEPT_ID);

----------------------------------------------------------------------------
-- commDescription
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

