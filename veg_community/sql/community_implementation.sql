
-----------------------------------------------------------------------------
-- commUsage
-----------------------------------------------------------------------------
drop sequence commUsage_COMMUSAGE_ID_seq;
drop table commUsage;

CREATE TABLE commUsage
(
    COMMUSAGE_ID serial,
    COMMNAME_ID integer NOT NULL,
    commName varchar (500) ,
    COMMCONCEPT_ID integer NOT NULL,
    ceglCode varchar (200),
    usageStart timestamp ,
    usageStop timestamp,
    commNameStatus varchar (20) ,
    COMMPARTY_ID integer ,
    classSystem varchar (50),
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
    commConceptStatus varchar (20) NOT NULL,
    commParent integer, -- the concept id for the parent
    commLevel varchar (50),
    startDate timestamp NOT NULL,
    stopDate timestamp,
    partyComments varchar (500),
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
    authors varchar (200),
    title varchar (200),
    pubDate timestamp,
    edition varchar (20),
    seriesName varchar (50),
    issueIdentification varchar (20),
    otherCitationDetails varchar (100),
    page varchar (20),
    tableCited varchar (20),
    ISBN varchar (20),
    ISSN varchar (20),
    commDescription text,
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
    commName varchar (500) NOT NULL,
    COMMREFERENCE_ID integer,
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
    commConvergence varchar (20),
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
    ceglCode varchar (20),
		conceptDescription text, 
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
    salutation varchar (20),
    givenName varchar (50),
    middleName varchar (50),
    surName varchar (50),
    organizationName varchar (100),
    currentName integer,
    contactInstructions varchar (1000),
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

ALTER TABLE commConcept
    ADD CONSTRAINT commParent FOREIGN KEY (commParent)
    REFERENCES commConcept (COMMCONCEPT_ID);


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
----------------------------------------------------------------------------
-- commConcept
----------------------------------------------------------------------------

ALTER TABLE commParty
    ADD CONSTRAINT currentName FOREIGN KEY (currentName)
    REFERENCES commParty (COMMPARTY_ID);

ALTER TABLE commParty
    ADD CONSTRAINT owner FOREIGN KEY (owner)
    REFERENCES commParty (COMMPARTY_ID);

