DROP VIEW view_csv_taxonimportance;
DROP VIEW view_csv_taxonimportance_pre;
DROP VIEW view_browseparty_all_count_combined;

DROP VIEW view_browseparty_classcontrib_count;
DROP VIEW view_browseparty_obscontrib_count;
DROP VIEW view_browseparty_projectcontrib_count;

DROP VIEW view_browseparty_all_count;
DROP VIEW view_browseparty_all;
DROP VIEW view_browseparty_obscontrib;
DROP VIEW view_browseparty_classcontrib;
DROP VIEW view_browseparty_projectcontrib;


DROP VIEW view_notemb_classContributor;
DROP VIEW view_notemb_commInterpretation;
DROP VIEW view_notemb_commClass;

DROP VIEW view_notemb_disturbanceObs;
DROP VIEW view_notemb_soilObs;
DROP VIEW view_notemb_stemCount;
DROP VIEW view_notemb_stemLocation;
DROP VIEW view_notemb_taxonAlt;
DROP VIEW view_notemb_taxonImportance;
DROP VIEW view_notemb_taxonInterpretation;
DROP VIEW view_notemb_taxonObservation;
DROP VIEW view_notemb_observation;
DROP VIEW view_notemb_plot;


drop view view_party_public;

CREATE VIEW view_party_public AS 
  SELECT party_id, accessioncode, salutation, surname, 
    givenname, middlename,organizationname, 
    contactinstructions, email 
    FROM party
   WHERE 
     party.party_id IN (SELECT note.party_id FROM note)
    OR party.party_id IN (SELECT party_id FROM observationcontributor)
    OR party.party_id IN (SELECT party_id FROM classcontributor)
    OR party.party_id IN (SELECT party_id FROM observationsynonym)
    OR party.party_id IN (SELECT party_id FROM projectcontributor)
    OR party.party_id IN (SELECT party_id FROM taxoninterpretation)
    OR party.party_id IN (SELECT party_id FROM commStatus)
    OR party.party_id IN (SELECT party_id FROM plantStatus)
    OR party.party_ID not IN (select party_ID from usr )
    ;


drop view view_busRule_plotsizeshape;
CREATE VIEW view_busRule_plotsizeshape AS 

  SELECT (project_id), (select projectName from project where project.project_ID=observation.projecT_ID) as projectName, 
   count(1) as plotcount
   from plot inner join observation on plot.plot_ID=observation.plot_ID 
   where ((area is null) and (shape is null or (((shape)<>'Plotless') and (upper(shape) not like 'RELEV%')))) group by project_ID;
   
   
DROP view view_busRule_duplStratumType;
   CREATE VIEW view_busRule_duplStratumType AS 
    SELECT count(1), stratummethod_id, stratumindex FROM stratumType GROUP BY stratummethod_id, stratumindex HAVING count(1) > 1;
    
DROP view view_busRule_duplcovercode;
   CREATE VIEW view_busRule_duplcovercode AS 
    SELECT count(1), covermethod_id, covercode FROM coverIndex GROUP BY covermethod_id, covercode HAVING count(1) > 1;

-- EMBARGO views    
drop view view_emb_embargo_complete;
drop view view_emb_embargo_currentfullonly;

CREATE VIEW view_emb_embargo_currentfullonly AS
  SELECT * FROM embargo WHERE (((defaultStatus)=6) AND ((embargoStart)<Now()) AND ((embargoStop)>Now()));
    
    

CREATE VIEW view_emb_embargo_complete AS
  SELECT Coalesce(emb.defaultStatus,0) AS currentEmb, plot.plot_id
  FROM plot LEFT JOIN view_emb_embargo_currentfullonly AS emb ON plot.PLOT_ID = emb.plot_ID;
  
  

 CREATE VIEW view_notEmb_classContributor AS SELECT * FROM classContributor where emb_classContributor<6;
 CREATE VIEW view_notEmb_commClass AS SELECT * FROM commClass where emb_commClass<6;
 CREATE VIEW view_notEmb_commInterpretation AS SELECT * FROM commInterpretation where emb_commInterpretation<6;
 CREATE VIEW view_notEmb_disturbanceObs AS SELECT * FROM disturbanceObs where emb_disturbanceObs<6;
 CREATE VIEW view_notEmb_observation AS SELECT * FROM observation where emb_observation<6;
 CREATE VIEW view_notEmb_plot AS SELECT * FROM plot where emb_plot<6;
 CREATE VIEW view_notEmb_soilObs AS SELECT * FROM soilObs where emb_soilObs<6;
 CREATE VIEW view_notEmb_stemCount AS SELECT * FROM stemCount where emb_stemCount<6;
 CREATE VIEW view_notEmb_stemLocation AS SELECT * FROM stemLocation where emb_stemLocation<6;
 CREATE VIEW view_notEmb_taxonAlt AS SELECT * FROM taxonAlt where emb_taxonAlt<6;
 CREATE VIEW view_notEmb_taxonImportance AS SELECT * FROM taxonImportance where emb_taxonImportance<6;
 CREATE VIEW view_notEmb_taxonInterpretation AS SELECT * FROM taxonInterpretation where emb_taxonInterpretation<6;
 CREATE VIEW view_notEmb_taxonObservation AS SELECT * FROM taxonObservation where emb_taxonObservation<6;

------------------
-- END EMBARGO VIEWS 
-------------------

DROP VIEW view_plantconcept_transl;
DROP VIEW  view_reference_transl ;
 CREATE VIEW view_reference_transl AS 
   SELECT CASE WHEN shortName IS NULL 
               THEN CASE WHEN title IS NULL 
                    THEN CASE WHEN fulltext IS NULL 
                         THEN '[poorly documented reference]' 
                         ELSE CASE WHEN length(fulltext)>35
                              THEN substr(fulltext,1,32) || '...'
                              ELSE fulltext
                              END
                         END 
                    ELSE title 
                    END 
               ELSE shortName 
               END 
             AS reference_id_transl, reference_id from reference;

DROP VIEW view_plantconcept_transl;
CREATE VIEW view_plantconcept_transl AS 
  SELECT plantconcept_id, plantname_id, plantname, reference_id, 
    plantname || ' [' || (select reference_id_transl 
                           FROM view_reference_transl 
                           where view_reference_transl.reference_id=plantconcept.reference_id) || ']' as plantconcept_id_transl 
    FROM plantconcept;



             
DROP VIEW view_party_transl ;
 CREATE VIEW view_party_transl AS 
   SELECT CASE WHEN surname is null 
               THEN CASE WHEN organizationname IS NULL
                    THEN '[poorly documented party]'
                    ELSE organizationname || ' (organization)'
                    END
               ELSE surname || CASE WHEN givenname IS NULL
                                    THEN ''
                                    ELSE ', ' || COALESCE(givenname,'') 
                                    END
               END 
             AS party_id_transl, party_id  
         FROM party ;
  
  
--- browsing by party views: -----

CREATE VIEW view_browseparty_obscontrib AS 
 SELECT observationContributor.PARTY_ID, observationContributor.OBSERVATION_ID
 FROM view_notemb_observation as observation INNER JOIN observationContributor ON observation.OBSERVATION_ID = observationContributor.OBSERVATION_ID
 GROUP BY observationContributor.PARTY_ID, observationContributor.OBSERVATION_ID;

CREATE VIEW view_browseparty_projectcontrib AS 
 SELECT observation_1.OBSERVATION_ID, projectContributor.PARTY_ID
 FROM (project INNER JOIN view_notemb_observation AS observation_1 ON project.PROJECT_ID = observation_1.PROJECT_ID) INNER JOIN projectContributor ON project.PROJECT_ID = projectContributor.PROJECT_ID
 GROUP BY observation_1.OBSERVATION_ID, projectContributor.PARTY_ID;

CREATE VIEW view_browseparty_classcontrib AS 
 SELECT observation_2.OBSERVATION_ID, classContributor.PARTY_ID
 FROM view_notemb_observation AS observation_2 INNER JOIN (commClass INNER JOIN classContributor ON commClass.COMMCLASS_ID = classContributor.COMMCLASS_ID) ON observation_2.OBSERVATION_ID = commClass.OBSERVATION_ID
 GROUP BY observation_2.OBSERVATION_ID, classContributor.PARTY_ID;



CREATE VIEW view_browseparty_all AS 
  SELECT OBSERVATION_ID, PARTY_ID
  FROM view_browseparty_classcontrib
 union SELECT OBSERVATION_ID, PARTY_ID
  FROM view_browseparty_projectcontrib
 UNION SELECT OBSERVATION_ID, PARTY_ID
  FROM view_browseparty_obscontrib;  


CREATE VIEW view_browseparty_all_count AS SELECT count(1) AS countallcontrib, party_ID
FROM view_browseparty_all
GROUP BY party_ID;


CREATE VIEW view_browseparty_classcontrib_count AS SELECT count(1) AS countclasscontrib, party_ID
FROM view_browseparty_classcontrib
GROUP BY party_ID;


CREATE VIEW view_browseparty_obscontrib_count AS SELECT count(1) AS countobscontrib, party_ID
FROM view_browseparty_obscontrib
GROUP BY party_ID;


CREATE VIEW view_browseparty_projectcontrib_count AS SELECT count(1) AS countprojectcontrib, party_ID
FROM view_browseparty_projectcontrib
GROUP BY party_ID;


CREATE VIEW view_browseparty_all_count_combined AS 
  SELECT view_browseparty_all_count.party_ID, view_browseparty_all_count.countallcontrib, view_browseparty_classcontrib_count.countclasscontrib, view_browseparty_obscontrib_count.countobscontrib, view_browseparty_projectcontrib_count.countprojectcontrib
  FROM ((view_browseparty_all_count LEFT JOIN view_browseparty_classcontrib_count ON view_browseparty_all_count.party_ID = view_browseparty_classcontrib_count.party_ID) LEFT JOIN view_browseparty_obscontrib_count ON view_browseparty_all_count.party_ID = view_browseparty_obscontrib_count.party_ID) LEFT JOIN view_browseparty_projectcontrib_count ON view_browseparty_all_count.party_ID = view_browseparty_projectcontrib_count.party_ID
  ORDER BY countallcontrib DESC;


CREATE view view_csv_taxonimportance_pre AS 
SELECT taxonobservation.observation_id AS observation_ID, 
    authorplantname  AS plant, 
    (CASE WHEN (taxonimportance.stratum_id is null) THEN '-all-' ELSE  (SELECT stratumtype.stratumname FROM stratumtype, stratum 
          WHERE stratum.stratumtype_id=stratumtype.stratumtype_id AND stratum.stratum_id=taxonimportance.stratum_id) END) AS stratum,
    cover, 
    (select min(coverIndex.coverCode) from coverIndex where coverPercent=taxonimportance.cover 
      AND coverIndex.coverMethod_ID=observation.coverMethod_ID and observation.observation_ID=taxonobservation.observation_ID) 
       as coverCode_exact, 
    (select min(coverIndex.coverCode) from coverIndex where upperlimit>=taxonimportance.cover and lowerlimit<=taxonimportance.cover
      AND coverIndex.coverMethod_ID=observation.coverMethod_ID and observation.observation_ID=taxonobservation.observation_ID) 
       as coverCode_byrange, 
       taxonimportance.basalarea, taxonobservation.accessioncode
 FROM taxonobservation, taxonimportance, view_notemb_observation AS observation 
 WHERE taxonobservation.taxonobservation_id=taxonimportance.taxonobservation_id AND 
   taxonobservation.observation_ID=observation.observation_ID ;
   
CREATE view view_csv_taxonimportance AS 
 SELECT observation_ID, plant, stratum, cover, CASE WHEN (covercode_exact is null) THEN covercode_byrange ELSE covercode_exact END as covercode, 
   basalarea, accessioncode
 FROM view_csv_taxonimportance_pre;
  