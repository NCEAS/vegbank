-- plantusage
CREATE INDEX plantusage_plantname_x on plantusage ( plantname );
CREATE INDEX plantusage_plantname_id_x on plantusage ( plantname_id );
CREATE INDEX plantusage_plantconcept_id_x on plantusage ( plantconcept_id );

-- plantname
CREATE INDEX plantname_plantname_x on plantname ( plantname );

-- plantconcept
CREATE INDEX plantconcept_plantname_id_x on plantconcept ( plantname_id );

-- plantstatus
CREATE INDEX plantstatus_plantlevel_x ON plantstatus (plantlevel);
CREATE INDEX plantstatus_plantconcept_id_x ON plantstatus (plantconcept_id);
