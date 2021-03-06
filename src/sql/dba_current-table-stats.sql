INSERT INTO dba_dbstatstime ( statdate, stattable, countrecs, maxpk)  SELECT now(),  'address', count(1) as countRecs, max(address_ID) as maxPK from address ;
INSERT INTO dba_dbstatstime ( statdate, stattable, countrecs, maxpk)  SELECT now(),  'aux_role', count(1) as countRecs, max(role_ID) as maxPK from aux_role ;
INSERT INTO dba_dbstatstime ( statdate, stattable, countrecs, maxpk)  SELECT now(),  'classcontributor', count(1) as countRecs, max(classcontributor_ID) as maxPK from classcontributor ;
INSERT INTO dba_dbstatstime ( statdate, stattable, countrecs, maxpk)  SELECT now(),  'commclass', count(1) as countRecs, max(commclass_ID) as maxPK from commclass ;
INSERT INTO dba_dbstatstime ( statdate, stattable, countrecs, maxpk)  SELECT now(),  'commconcept', count(1) as countRecs, max(commconcept_ID) as maxPK from commconcept ;
INSERT INTO dba_dbstatstime ( statdate, stattable, countrecs, maxpk)  SELECT now(),  'commcorrelation', count(1) as countRecs, max(commcorrelation_ID) as maxPK from commcorrelation ;
INSERT INTO dba_dbstatstime ( statdate, stattable, countrecs, maxpk)  SELECT now(),  'comminterpretation', count(1) as countRecs, max(comminterpretation_ID) as maxPK from comminterpretation ;
INSERT INTO dba_dbstatstime ( statdate, stattable, countrecs, maxpk)  SELECT now(),  'commlineage', count(1) as countRecs, max(commlineage_ID) as maxPK from commlineage ;
INSERT INTO dba_dbstatstime ( statdate, stattable, countrecs, maxpk)  SELECT now(),  'commname', count(1) as countRecs, max(commname_ID) as maxPK from commname ;
INSERT INTO dba_dbstatstime ( statdate, stattable, countrecs, maxpk)  SELECT now(),  'commstatus', count(1) as countRecs, max(commstatus_ID) as maxPK from commstatus ;
INSERT INTO dba_dbstatstime ( statdate, stattable, countrecs, maxpk)  SELECT now(),  'commusage', count(1) as countRecs, max(commusage_ID) as maxPK from commusage ;
INSERT INTO dba_dbstatstime ( statdate, stattable, countrecs, maxpk)  SELECT now(),  'coverindex', count(1) as countRecs, max(coverindex_ID) as maxPK from coverindex ;
INSERT INTO dba_dbstatstime ( statdate, stattable, countrecs, maxpk)  SELECT now(),  'covermethod', count(1) as countRecs, max(covermethod_ID) as maxPK from covermethod ;
INSERT INTO dba_dbstatstime ( statdate, stattable, countrecs, maxpk)  SELECT now(),  'definedvalue', count(1) as countRecs, max(definedvalue_ID) as maxPK from definedvalue ;
INSERT INTO dba_dbstatstime ( statdate, stattable, countrecs, maxpk)  SELECT now(),  'disturbanceobs', count(1) as countRecs, max(disturbanceobs_ID) as maxPK from disturbanceobs ;
INSERT INTO dba_dbstatstime ( statdate, stattable, countrecs, maxpk)  SELECT now(),  'embargo', count(1) as countRecs, max(embargo_ID) as maxPK from embargo ;
INSERT INTO dba_dbstatstime ( statdate, stattable, countrecs, maxpk)  SELECT now(),  'graphic', count(1) as countRecs, max(graphic_ID) as maxPK from graphic ;
INSERT INTO dba_dbstatstime ( statdate, stattable, countrecs, maxpk)  SELECT now(),  'namedplace', count(1) as countRecs, max(namedplace_ID) as maxPK from namedplace ;
INSERT INTO dba_dbstatstime ( statdate, stattable, countrecs, maxpk)  SELECT now(),  'namedplaceCorrelation', count(1) as countRecs, max(namedplaceCorrelation_ID) as maxPK from namedplaceCorrelation ;
INSERT INTO dba_dbstatstime ( statdate, stattable, countrecs, maxpk)  SELECT now(),  'note', count(1) as countRecs, max(note_ID) as maxPK from note ;
INSERT INTO dba_dbstatstime ( statdate, stattable, countrecs, maxpk)  SELECT now(),  'notelink', count(1) as countRecs, max(notelink_ID) as maxPK from notelink ;
INSERT INTO dba_dbstatstime ( statdate, stattable, countrecs, maxpk)  SELECT now(),  'observation', count(1) as countRecs, max(observation_ID) as maxPK from observation ;
INSERT INTO dba_dbstatstime ( statdate, stattable, countrecs, maxpk)  SELECT now(),  'observationcontributor', count(1) as countRecs, max(observationcontributor_ID) as maxPK from observationcontributor ;
INSERT INTO dba_dbstatstime ( statdate, stattable, countrecs, maxpk)  SELECT now(),  'observationsynonym', count(1) as countRecs, max(observationsynonym_ID) as maxPK from observationsynonym ;
INSERT INTO dba_dbstatstime ( statdate, stattable, countrecs, maxpk)  SELECT now(),  'party', count(1) as countRecs, max(party_ID) as maxPK from party ;
INSERT INTO dba_dbstatstime ( statdate, stattable, countrecs, maxpk)  SELECT now(),  'partymember', count(1) as countRecs, max(partymember_ID) as maxPK from partymember ;
INSERT INTO dba_dbstatstime ( statdate, stattable, countrecs, maxpk)  SELECT now(),  'place', count(1) as countRecs, max(plotplace_ID) as maxPK from place ;
INSERT INTO dba_dbstatstime ( statdate, stattable, countrecs, maxpk)  SELECT now(),  'plantconcept', count(1) as countRecs, max(plantconcept_ID) as maxPK from plantconcept ;
INSERT INTO dba_dbstatstime ( statdate, stattable, countrecs, maxpk)  SELECT now(),  'plantcorrelation', count(1) as countRecs, max(plantcorrelation_ID) as maxPK from plantcorrelation ;
INSERT INTO dba_dbstatstime ( statdate, stattable, countrecs, maxpk)  SELECT now(),  'plantlineage', count(1) as countRecs, max(plantlineage_ID) as maxPK from plantlineage ;
INSERT INTO dba_dbstatstime ( statdate, stattable, countrecs, maxpk)  SELECT now(),  'plantname', count(1) as countRecs, max(plantname_ID) as maxPK from plantname ;
INSERT INTO dba_dbstatstime ( statdate, stattable, countrecs, maxpk)  SELECT now(),  'plantstatus', count(1) as countRecs, max(plantstatus_ID) as maxPK from plantstatus ;
INSERT INTO dba_dbstatstime ( statdate, stattable, countrecs, maxpk)  SELECT now(),  'plantusage', count(1) as countRecs, max(plantusage_ID) as maxPK from plantusage ;
INSERT INTO dba_dbstatstime ( statdate, stattable, countrecs, maxpk)  SELECT now(),  'plot', count(1) as countRecs, max(plot_ID) as maxPK from plot ;
INSERT INTO dba_dbstatstime ( statdate, stattable, countrecs, maxpk)  SELECT now(),  'project', count(1) as countRecs, max(project_ID) as maxPK from project ;
INSERT INTO dba_dbstatstime ( statdate, stattable, countrecs, maxpk)  SELECT now(),  'projectcontributor', count(1) as countRecs, max(projectcontributor_ID) as maxPK from projectcontributor ;
INSERT INTO dba_dbstatstime ( statdate, stattable, countrecs, maxpk)  SELECT now(),  'reference', count(1) as countRecs, max(reference_ID) as maxPK from reference ;
INSERT INTO dba_dbstatstime ( statdate, stattable, countrecs, maxpk)  SELECT now(),  'referencealtident', count(1) as countRecs, max(referencealtident_ID) as maxPK from referencealtident ;
INSERT INTO dba_dbstatstime ( statdate, stattable, countrecs, maxpk)  SELECT now(),  'referencecontributor', count(1) as countRecs, max(referencecontributor_ID) as maxPK from referencecontributor ;
INSERT INTO dba_dbstatstime ( statdate, stattable, countrecs, maxpk)  SELECT now(),  'referencejournal', count(1) as countRecs, max(referencejournal_ID) as maxPK from referencejournal ;
INSERT INTO dba_dbstatstime ( statdate, stattable, countrecs, maxpk)  SELECT now(),  'referenceparty', count(1) as countRecs, max(referenceparty_ID) as maxPK from referenceparty ;
INSERT INTO dba_dbstatstime ( statdate, stattable, countrecs, maxpk)  SELECT now(),  'revision', count(1) as countRecs, max(revision_ID) as maxPK from revision ;
INSERT INTO dba_dbstatstime ( statdate, stattable, countrecs, maxpk)  SELECT now(),  'soilobs', count(1) as countRecs, max(soilobs_ID) as maxPK from soilobs ;
INSERT INTO dba_dbstatstime ( statdate, stattable, countrecs, maxpk)  SELECT now(),  'soiltaxon', count(1) as countRecs, max(soiltaxon_ID) as maxPK from soiltaxon ;
INSERT INTO dba_dbstatstime ( statdate, stattable, countrecs, maxpk)  SELECT now(),  'stemcount', count(1) as countRecs, max(stemcount_ID) as maxPK from stemcount ;
INSERT INTO dba_dbstatstime ( statdate, stattable, countrecs, maxpk)  SELECT now(),  'stemlocation', count(1) as countRecs, max(stemlocation_ID) as maxPK from stemlocation ;
INSERT INTO dba_dbstatstime ( statdate, stattable, countrecs, maxpk)  SELECT now(),  'stratum', count(1) as countRecs, max(stratum_ID) as maxPK from stratum ;
INSERT INTO dba_dbstatstime ( statdate, stattable, countrecs, maxpk)  SELECT now(),  'stratummethod', count(1) as countRecs, max(stratummethod_ID) as maxPK from stratummethod ;
INSERT INTO dba_dbstatstime ( statdate, stattable, countrecs, maxpk)  SELECT now(),  'stratumtype', count(1) as countRecs, max(stratumtype_ID) as maxPK from stratumtype ;
INSERT INTO dba_dbstatstime ( statdate, stattable, countrecs, maxpk)  SELECT now(),  'taxonalt', count(1) as countRecs, max(taxonalt_ID) as maxPK from taxonalt ;
INSERT INTO dba_dbstatstime ( statdate, stattable, countrecs, maxpk)  SELECT now(),  'taxonimportance', count(1) as countRecs, max(taxonimportance_ID) as maxPK from taxonimportance ;
INSERT INTO dba_dbstatstime ( statdate, stattable, countrecs, maxpk)  SELECT now(),  'taxoninterpretation', count(1) as countRecs, max(taxoninterpretation_ID) as maxPK from taxoninterpretation ;
INSERT INTO dba_dbstatstime ( statdate, stattable, countrecs, maxpk)  SELECT now(),  'taxonobservation', count(1) as countRecs, max(taxonobservation_ID) as maxPK from taxonobservation ;
INSERT INTO dba_dbstatstime ( statdate, stattable, countrecs, maxpk)  SELECT now(),  'telephone', count(1) as countRecs, max(telephone_ID) as maxPK from telephone ;
INSERT INTO dba_dbstatstime ( statdate, stattable, countrecs, maxpk)  SELECT now(),  'usercertification', count(1) as countRecs, max(usercertification_ID) as maxPK from usercertification ;
INSERT INTO dba_dbstatstime ( statdate, stattable, countrecs, maxpk)  SELECT now(),  'userdataset', count(1) as countRecs, max(userdataset_ID) as maxPK from userdataset ;
INSERT INTO dba_dbstatstime ( statdate, stattable, countrecs, maxpk)  SELECT now(),  'userdatasetitem', count(1) as countRecs, max(userdatasetitem_ID) as maxPK from userdatasetitem ;
INSERT INTO dba_dbstatstime ( statdate, stattable, countrecs, maxpk)  SELECT now(),  'userdefined', count(1) as countRecs, max(userdefined_ID) as maxPK from userdefined ;
INSERT INTO dba_dbstatstime ( statdate, stattable, countrecs, maxpk)  SELECT now(),  'usernotify', count(1) as countRecs, max(usernotify_ID) as maxPK from usernotify ;
INSERT INTO dba_dbstatstime ( statdate, stattable, countrecs, maxpk)  SELECT now(),  'userpermission', count(1) as countRecs, max(userpermission_ID) as maxPK from userpermission ;
INSERT INTO dba_dbstatstime ( statdate, stattable, countrecs, maxpk)  SELECT now(),  'userpreference', count(1) as countRecs, max(userpreference_ID) as maxPK from userpreference ;
INSERT INTO dba_dbstatstime ( statdate, stattable, countrecs, maxpk)  SELECT now(),  'userquery', count(1) as countRecs, max(userquery_ID) as maxPK from userquery ;
INSERT INTO dba_dbstatstime ( statdate, stattable, countrecs, maxpk)  SELECT now(),  'userrecordowner', count(1) as countRecs, max(userrecordowner_ID) as maxPK from userrecordowner ;
INSERT INTO dba_dbstatstime ( statdate, stattable, countrecs, maxpk)  SELECT now(),  'userregionalexp', count(1) as countRecs, max(userregionalexp_ID) as maxPK from userregionalexp ;
INSERT INTO dba_dbstatstime ( statdate, stattable, countrecs, maxpk)  SELECT now(),  'usr', count(1) as countRecs, max(usr_ID) as maxPK from usr ;
