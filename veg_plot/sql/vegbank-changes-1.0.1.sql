
ALTER TABLE taxonInterpretation   ADD COLUMN stemLocation_ID integer ;

ALTER TABLE taxonInterpretation
  ADD CONSTRAINT stemLocation_ID FOREIGN KEY (stemLocation_ID)
  REFERENCES stemLocation (stemLocation_ID );

ALTER TABLE stemLocation   ADD COLUMN stemHealth varchar (50) ;

ALTER TABLE observation   ADD COLUMN dateEntered Date ;

ALTER TABLE taxonInterpretation   ADD COLUMN collector_ID Integer ;

ALTER TABLE taxonInterpretation
  ADD CONSTRAINT collector_ID FOREIGN KEY (collector_ID)
  REFERENCES party (party_ID );

ALTER TABLE taxonInterpretation   ADD COLUMN collectionNumber varchar (100) ;

ALTER TABLE taxonInterpretation   ADD COLUMN collectionDate Date ;

ALTER TABLE taxonInterpretation   ADD COLUMN museum_ID Integer ;

ALTER TABLE taxonInterpretation
  ADD CONSTRAINT museum_ID FOREIGN KEY (museum_ID)
  REFERENCES party (party_ID );

ALTER TABLE taxonInterpretation   ADD COLUMN museumAccessionNumber varchar (100) ;

CREATE TABLE taxonAlt ( taxonAlt_ID serial ,  taxonInterpretation_ID Integer NOT NULL, plantConcept_ID Integer  NOT NULL, 
    PRIMARY KEY (taxonAlt_ID) );
    
  


ALTER TABLE taxonAlt
  ADD CONSTRAINT taxonInterpretation_ID FOREIGN KEY (taxonInterpretation_ID)
  REFERENCES taxonInterpretation (taxonInterpretation_ID );


ALTER TABLE taxonAlt
  ADD CONSTRAINT plantConcept_ID FOREIGN KEY (plantConcept_ID)
  REFERENCES plantConcept (plantConcept_ID );

ALTER TABLE taxonAlt   ADD COLUMN taxonAltFit varchar (50) ;

ALTER TABLE taxonAlt   ADD COLUMN taxonAltConfidence varchar (50) ;

ALTER TABLE taxonAlt   ADD COLUMN taxonAltNotes text ;

ALTER TABLE taxonObservation   ADD COLUMN plantName varchar (255) ;

ALTER TABLE taxonInterpretation   ADD COLUMN groupType varchar (20) ;

ALTER TABLE taxonInterpretation   ADD COLUMN taxonFit varchar (50) ;

ALTER TABLE taxonInterpretation   ADD COLUMN taxonConfidence varchar (50) ;

ALTER TABLE plantConcept   ADD COLUMN accessionCode varchar (100) ;

ALTER TABLE commConcept   ADD COLUMN accessionCode varchar (100) ;

ALTER TABLE party   ADD COLUMN accessionCode varchar (100) ;

ALTER TABLE reference   ADD COLUMN accessionCode varchar (100) ;

ALTER TABLE reference   ADD COLUMN fulltext text ;
