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
   
   
   drop view view_busRule_duplStratumType;
   CREATE VIEW view_busRule_duplStratumType AS 
    SELECT count(1), stratummethod_id, stratumindex FROM stratumType GROUP BY stratummethod_id, stratumindex HAVING count(1) > 1;
    
    drop view view_busRule_duplcovercode;
   CREATE VIEW view_busRule_duplcovercode AS 
    SELECT count(1), covermethod_id, covercode FROM coverIndex GROUP BY covermethod_id, covercode HAVING count(1) > 1;
    
drop view view_emb_embargo_currentfullonly;
CREATE VIEW view_emb_embargo_currentfullonly AS
  SELECT * FROM embargo WHERE (((defaultStatus)=6) AND ((embargoStart)<Now()) AND ((embargoStop)>Now()));
    
    
drop view view_emb_embargo_complete;
CREATE VIEW view_emb_embargo_complete AS
  SELECT Coalesce(emb.defaultStatus,0) AS currentEmb, plot.plot_id
  FROM plot LEFT JOIN view_emb_embargo_currentfullonly AS emb ON plot.PLOT_ID = emb.plot_ID;