BEGIN TRANSACTION;

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

END TRANSACTION;
