--FROM db_schema_changes.xml, changes for 1.0.1, Draft 1, Michael Lee
ALTER TABLE taxonInterpretation   ADD COLUMN stemLocation_ID integer ;

ALTER TABLE taxonInterpretation
  ADD CONSTRAINT stemLocation_ID FOREIGN KEY (stemLocation_ID)
  REFERENCES stemLocation (stemLocation_ID );

ALTER TABLE stemLocation   ADD COLUMN stemHealth varchar (50) ;

ALTER TABLE observation   ADD COLUMN dateEntered Date ;

ALTER TABLE taxonInterpretation   ADD COLUMN collector_ID integer ;

ALTER TABLE taxonInterpretation
  ADD CONSTRAINT collector_ID FOREIGN KEY (collector_ID)
  REFERENCES party (party_ID );

ALTER TABLE taxonInterpretation   ADD COLUMN collectionNumber varchar (100) ;

ALTER TABLE taxonInterpretation   ADD COLUMN collectionDate Date ;

ALTER TABLE taxonInterpretation   ADD COLUMN museum_ID integer ;

ALTER TABLE taxonInterpretation
  ADD CONSTRAINT museum_ID FOREIGN KEY (museum_ID)
  REFERENCES party (party_ID );

ALTER TABLE taxonInterpretation   ADD COLUMN museumAccessionNumber varchar (100) ;

CREATE TABLE taxonGroup ( taxonGroup_ID serial ,
    PRIMARY KEY (taxonGroup_ID) );
    
  
ALTER TABLE taxonGroup   ADD COLUMN taxonInterpretation_ID Integer ;

ALTER TABLE taxonGroup
  ADD CONSTRAINT taxonInterpretation_ID FOREIGN KEY (taxonInterpretation_ID)
  REFERENCES taxonInterpretation (taxonInterpretation_ID );

ALTER TABLE taxonGroup   ADD COLUMN plantConcept_ID Integer ;

ALTER TABLE taxonGroup
  ADD CONSTRAINT plantConcept_ID FOREIGN KEY (plantConcept_ID)
  REFERENCES plantConcept (plantConcept_ID );

ALTER TABLE taxonObservation   ADD COLUMN plantName varchar (255) ;

ALTER TABLE taxonInterpretation   ADD COLUMN groupType varchar (20) ;

ALTER TABLE taxonInterpretation   ADD COLUMN taxonFit varchar (50) ;

ALTER TABLE taxonInterpretation   ADD COLUMN taxonConfidence varchar (50) ;

ALTER TABLE plantConcept   ADD COLUMN accessionCode varchar (100) ;

ALTER TABLE commConcept   ADD COLUMN accessionCode varchar (100) ;

ALTER TABLE party   ADD COLUMN accessionCode varchar (100) ;

ALTER TABLE reference   ADD COLUMN accessionCode varchar (100) ;
