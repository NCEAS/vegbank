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
    OR party.party_id IN (SELECT party_id FROM taxoninterpretation);
