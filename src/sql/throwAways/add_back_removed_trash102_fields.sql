ALTER TABLE commClass add column commLevel   varchar(20);
ALTER TABLE commClass add column commFramework  varchar(20);
ALTER TABLE commClass add column commCode  varchar(20);
ALTER TABLE commClass add column commName  varchar(20);
ALTER TABLE commInterpretation add column commcode  varchar(20);
ALTER TABLE plantConcept add column plantCode  varchar(20);
ALTER TABLE plantStatus add column plantParentConcept_id  varchar(20);
ALTER TABLE plantStatus add column plantParentName  varchar(20);
ALTER TABLE plantUsage add column acceptedSynonym  varchar(20);
ALTER TABLE plot add column submitter_email  varchar(20);
ALTER TABLE plot add column submitter_givenname  varchar(20);
ALTER TABLE plot add column submitter_surname  varchar(20);
ALTER TABLE projectContributor add column cheatRole  varchar(20);
ALTER TABLE projectContributor add column surname  varchar(20);
ALTER TABLE stratum add column stratumDescription  varchar(20);

------------------------------------------------------------