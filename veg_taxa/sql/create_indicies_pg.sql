-- SQL SCRIPT TO CREATE PLANT TAXONOMY RELATED INDICIES USED 
-- PRIMARILY FOR INCREASING THE PERFOMANCE OF THE DATABASE 
-- DENORMALIZATION INTO THE QUERY TABLES(S), AND SHOULD BE RUN AFTER
-- 'FULL' LOAD OF THE DATABASE

create index plantparentname_idx on plantstatus (plantparentname);
create index plantconcept_id_idx on plantstatus (plantconcept_id);

--create index partyusagestatus_id_idx on plantusage (partyusagestatus_id);
--create index plantconcept_id_idx on plantusage (plantconcept_id);
--create index plantname_id_idx on plantusage (plantname_id);
--create index plantname_idx on plantusage (plantname);
--create index startdate_id_idx on plantusage (startdate);
--create index stopdate_id_idx on plantusage (stopdate);
