
-- Create indexes for vegbank.
-- The db needs to be vacummed and analyzed before any impact .e.g. "vacuumdb -z -f vegbank" 
-- Those commented with a -- t mean that they are tested and work in our favor

-- plantusage
CREATE INDEX plantusage_plantname_x on plantusage ( plantname ); -- t
CREATE INDEX plantusage_plantname_id_x on plantusage ( plantname_id ); -- t
CREATE INDEX plantusage_plantconcept_id_x on plantusage ( plantconcept_id ); -- t
CREATE INDEX plantusage_classsystem_x on plantusage ( classsystem ); -- t
CREATE INDEX plantusage_party_id_x ON plantusage (party_id);
CREATE INDEX plantusage_plantstatus_id_x ON plantusage (plantstatus_id);

-- plantname
CREATE INDEX plantname_plantname_x on plantname ( plantname ); -- t
CREATE INDEX plantname_reference_id_x ON plantname (reference_id);

-- plantconcept
CREATE INDEX plantconcept_plantname_id_x on plantconcept ( plantname_id ); -- t
CREATE INDEX plantconcept_reference_id_x ON plantconcept (reference_id);

-- plantstatus
CREATE INDEX plantstatus_plantlevel_x ON plantstatus (plantlevel); -- t
CREATE INDEX plantstatus_plantconcept_id_x ON plantstatus (plantconcept_id); -- t
CREATE INDEX plantstatus_reference_id_x ON plantstatus (reference_id);
CREATE INDEX plantstatus_plantparent_id_x ON plantstatus (plantparent_id);
CREATE INDEX plantstatus_party_id_x ON plantstatus (party_id);

-- userregionalexp 
CREATE INDEX userregionalexp_usercertification_id_x ON userregionalexp (usercertification_id);

-- userdatasetitem 
CREATE INDEX userdatasetitem_userdataset_id_x ON userdatasetitem (userdataset_id);

-- userdataset 
CREATE INDEX userdataset_usr_id_x ON userdataset (usr_id);

-- usernotify 
CREATE INDEX usernotify_usr_id_x ON usernotify (usr_id);

-- embargo 
CREATE INDEX embargo_plot_id_x ON embargo (plot_id);

-- userpermission 
CREATE INDEX userpermission_embargo_id_x ON userpermission (embargo_id);
CREATE INDEX userpermission_usr_id_x ON userpermission (usr_id);

-- userquery 
CREATE INDEX userquery_usr_id_x ON userquery (usr_id);

-- userpreference 
CREATE INDEX userpreference_usr_id_x ON userpreference (usr_id);

-- userrecordowner 
CREATE INDEX userrecordowner_usr_id_x ON userrecordowner (usr_id);

-- usr 
CREATE INDEX usr_party_id_x ON usr (party_id);

-- aux_role 

-- covermethod 
CREATE INDEX covermethod_reference_id_x ON covermethod (reference_id);

-- stratummethod 
CREATE INDEX stratummethod_reference_id_x ON stratummethod (reference_id);

-- usercertification 
CREATE INDEX usercertification_usr_id_x ON usercertification (usr_id);

-- stratum 
CREATE INDEX stratum_observation_id_x ON stratum (observation_id);
CREATE INDEX stratum_stratumtype_id_x ON stratum (stratumtype_id);
CREATE INDEX stratum_stratummethod_id_x ON stratum (stratummethod_id);

-- stemlocation 
CREATE INDEX stemlocation_stemcount_id_x ON stemlocation (stemcount_id);


-- observation 
CREATE INDEX observation_previousobs_id_x ON observation (previousobs_id);
CREATE INDEX observation_previousobs_id_x ON observation (previousobs_id);
CREATE INDEX observation_previousobs_id_x ON observation (previousobs_id);
CREATE INDEX observation_plot_id_x ON observation (plot_id);
CREATE INDEX observation_project_id_x ON observation (project_id);
CREATE INDEX observation_covermethod_id_x ON observation (covermethod_id);
CREATE INDEX observation_stratummethod_id_x ON observation (stratummethod_id);
CREATE INDEX observation_soiltaxon_id_x ON observation (soiltaxon_id);

-- taxonobservation 
CREATE INDEX taxonobservation_observation_id_x ON taxonobservation (observation_id);
CREATE INDEX taxonobservation_reference_id_x ON taxonobservation (reference_id);

-- reference 
CREATE INDEX reference_referencejournal_id_x ON reference (referencejournal_id);

-- taxoninterpretation 
CREATE INDEX taxoninterpretation_taxonobservation_id_x ON taxoninterpretation (taxonobservation_id);
CREATE INDEX taxoninterpretation_stemlocation_id_x ON taxoninterpretation (stemlocation_id);
CREATE INDEX taxoninterpretation_plantconcept_id_x ON taxoninterpretation (plantconcept_id);
CREATE INDEX taxoninterpretation_plantname_id_x ON taxoninterpretation (plantname_id);
CREATE INDEX taxoninterpretation_party_id_x ON taxoninterpretation (party_id);
CREATE INDEX taxoninterpretation_role_id_x ON taxoninterpretation (role_id);
CREATE INDEX taxoninterpretation_reference_id_x ON taxoninterpretation (reference_id);
CREATE INDEX taxoninterpretation_collector_id_x ON taxoninterpretation (collector_id);
CREATE INDEX taxoninterpretation_museum_id_x ON taxoninterpretation (museum_id);

-- taxonalt 
CREATE INDEX taxonalt_taxoninterpretation_id_x ON taxonalt (taxoninterpretation_id);
CREATE INDEX taxonalt_plantconcept_id_x ON taxonalt (plantconcept_id);


-- telephone 
CREATE INDEX telephone_party_id_x ON telephone (party_id);

-- plot 
CREATE INDEX plot_reference_id_x ON plot (reference_id);
CREATE INDEX plot_parent_id_x ON plot (parent_id);
CREATE INDEX plot_parent_id_x ON plot (parent_id);
CREATE INDEX plot_parent_id_x ON plot (parent_id);

-- party 
CREATE INDEX party_currentname_id_x ON party (currentname_id);
CREATE INDEX party_currentname_id_x ON party (currentname_id);
CREATE INDEX party_currentname_id_x ON party (currentname_id);

-- place 
CREATE INDEX place_plot_id_x ON place (plot_id);
CREATE INDEX place_namedplace_id_x ON place (namedplace_id);

-- namedplace 
CREATE INDEX namedplace_reference_id_x ON namedplace (reference_id);

-- project 

-- projectcontributor 
CREATE INDEX projectcontributor_project_id_x ON projectcontributor (project_id);
CREATE INDEX projectcontributor_party_id_x ON projectcontributor (party_id);
CREATE INDEX projectcontributor_role_id_x ON projectcontributor (role_id);

-- revision 
CREATE INDEX revision_previousrevision_id_x ON revision (previousrevision_id);
CREATE INDEX revision_previousrevision_id_x ON revision (previousrevision_id);
CREATE INDEX revision_previousrevision_id_x ON revision (previousrevision_id);

-- soilobs 
CREATE INDEX soilobs_observation_id_x ON soilobs (observation_id);

-- soiltaxon 
CREATE INDEX soiltaxon_soilparent_id_x ON soiltaxon (soilparent_id);
CREATE INDEX soiltaxon_soilparent_id_x ON soiltaxon (soilparent_id);
CREATE INDEX soiltaxon_soilparent_id_x ON soiltaxon (soilparent_id);

-- stemcount 
CREATE INDEX stemcount_taxonimportance_id_x ON stemcount (taxonimportance_id);

-- stratumtype 
CREATE INDEX stratumtype_stratummethod_id_x ON stratumtype (stratummethod_id);

-- taxonimportance 
CREATE INDEX taxonimportance_taxonobservation_id_x ON taxonimportance (taxonobservation_id);
CREATE INDEX taxonimportance_stratum_id_x ON taxonimportance (stratum_id);

-- observationcontributor 
CREATE INDEX observationcontributor_observation_id_x ON observationcontributor (observation_id);
CREATE INDEX observationcontributor_party_id_x ON observationcontributor (party_id);
CREATE INDEX observationcontributor_role_id_x ON observationcontributor (role_id);

-- observationsynonym 
CREATE INDEX observationsynonym_synonymobservation_id_x ON observationsynonym (synonymobservation_id);
CREATE INDEX observationsynonym_primaryobservation_id_x ON observationsynonym (primaryobservation_id);
CREATE INDEX observationsynonym_party_id_x ON observationsynonym (party_id);
CREATE INDEX observationsynonym_role_id_x ON observationsynonym (role_id);

-- partymember 
CREATE INDEX partymember_parentparty_id_x ON partymember (parentparty_id);
CREATE INDEX partymember_childparty_id_x ON partymember (childparty_id);
CREATE INDEX partymember_role_id_x ON partymember (role_id);

-- referenceparty 
CREATE INDEX referenceparty_currentparty_id_x ON referenceparty (currentparty_id);
CREATE INDEX referenceparty_currentparty_id_x ON referenceparty (currentparty_id);
CREATE INDEX referenceparty_currentparty_id_x ON referenceparty (currentparty_id);

-- classcontributor 
CREATE INDEX classcontributor_commclass_id_x ON classcontributor (commclass_id);
CREATE INDEX classcontributor_party_id_x ON classcontributor (party_id);
CREATE INDEX classcontributor_role_id_x ON classcontributor (role_id);

-- commclass 
CREATE INDEX commclass_observation_id_x ON commclass (observation_id);
CREATE INDEX commclass_classpublication_id_x ON commclass (classpublication_id);

-- commconcept 
CREATE INDEX commconcept_commname_id_x ON commconcept (commname_id);
CREATE INDEX commconcept_reference_id_x ON commconcept (reference_id);

-- comminterpretation 
CREATE INDEX comminterpretation_commclass_id_x ON comminterpretation (commclass_id);
CREATE INDEX comminterpretation_commconcept_id_x ON comminterpretation (commconcept_id);
CREATE INDEX comminterpretation_commauthority_id_x ON comminterpretation (commauthority_id);

-- coverindex 
CREATE INDEX coverindex_covermethod_id_x ON coverindex (covermethod_id);

-- definedvalue 
CREATE INDEX definedvalue_userdefined_id_x ON definedvalue (userdefined_id);

-- userdefined 

-- disturbanceobs 
CREATE INDEX disturbanceobs_observation_id_x ON disturbanceobs (observation_id);

-- graphic 
CREATE INDEX graphic_observation_id_x ON graphic (observation_id);

-- notelink 

-- note 
CREATE INDEX note_notelink_id_x ON note (notelink_id);
CREATE INDEX note_party_id_x ON note (party_id);
CREATE INDEX note_role_id_x ON note (role_id);

-- plantlineage 
CREATE INDEX plantlineage_childplantstatus_id_x ON plantlineage (childplantstatus_id);
CREATE INDEX plantlineage_parentplantstatus_id_x ON plantlineage (parentplantstatus_id);



-- address 
CREATE INDEX address_party_id_x ON address (party_id);
CREATE INDEX address_organization_id_x ON address (organization_id);

-- referencejournal 

-- referencealtident 
CREATE INDEX referencealtident_reference_id_x ON referencealtident (reference_id);

-- referencecontributor 
CREATE INDEX referencecontributor_reference_id_x ON referencecontributor (reference_id);
CREATE INDEX referencecontributor_referenceparty_id_x ON referencecontributor (referenceparty_id);

-- commcorrelation 
CREATE INDEX commcorrelation_commstatus_id_x ON commcorrelation (commstatus_id);
CREATE INDEX commcorrelation_commconcept_id_x ON commcorrelation (commconcept_id);

-- commlineage 
CREATE INDEX commlineage_parentcommstatus_id_x ON commlineage (parentcommstatus_id);
CREATE INDEX commlineage_childcommstatus_id_x ON commlineage (childcommstatus_id);

-- commname 
CREATE INDEX commname_reference_id_x ON commname (reference_id);

-- commusage 
CREATE INDEX commusage_commname_id_x ON commusage (commname_id);
CREATE INDEX commusage_commconcept_id_x ON commusage (commconcept_id);
CREATE INDEX commusage_party_id_x ON commusage (party_id);
CREATE INDEX commusage_commstatus_id_x ON commusage (commstatus_id);
CREATE INDEX commusage_commname_x on commusage ( commname ); -- t
CREATE INDEX commusage_classsystem_x on commusage ( classsystem ); -- t

-- commstatus 
CREATE INDEX commstatus_commconcept_id_x ON commstatus (commconcept_id);
CREATE INDEX commstatus_reference_id_x ON commstatus (reference_id);
CREATE INDEX commstatus_commparent_id_x ON commstatus (commparent_id);
CREATE INDEX commstatus_party_id_x ON commstatus (party_id);
CREATE INDEX commstatus_commlevel_x ON commstatus (commlevel); -- t

-- plantcorrelation 
CREATE INDEX plantcorrelation_plantstatus_id_x ON plantcorrelation (plantstatus_id);
CREATE INDEX plantcorrelation_plantconcept_id_x ON plantcorrelation (plantconcept_id);

-- keywords
CREATE INDEX keywords_table_id_entity_key ON keywords (table_id,entity);

--embargo denorm fields
CREATE INDEX emb_classContributor_idx ON classContributor (emb_classContributor);
CREATE INDEX emb_commClass_idx ON commClass (emb_commClass);
CREATE INDEX emb_commInterpretation_idx ON commInterpretation (emb_commInterpretation);
CREATE INDEX emb_disturbanceObs_idx ON disturbanceObs (emb_disturbanceObs);
CREATE INDEX emb_observation_idx ON observation (emb_observation);
CREATE INDEX emb_plot_idx ON plot (emb_plot);
CREATE INDEX emb_soilObs_idx ON soilObs (emb_soilObs);
CREATE INDEX emb_stemCount_idx ON stemCount (emb_stemCount);
CREATE INDEX emb_stemLocation_idx ON stemLocation (emb_stemLocation);
CREATE INDEX emb_taxonAlt_idx ON taxonAlt (emb_taxonAlt);
CREATE INDEX emb_taxonImportance_idx ON taxonImportance (emb_taxonImportance);
CREATE INDEX emb_taxonInterpretation_idx ON taxonInterpretation (emb_taxonInterpretation);
CREATE INDEX emb_taxonObservation_idx ON taxonObservation (emb_taxonObservation);
