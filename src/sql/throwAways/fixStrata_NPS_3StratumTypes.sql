-- this file is to add 3 stratum Types to the extant NPS Stratum Method.  Note: running this multiple times will cause business rules errors!

INSERT INTO stratumType (stratumMethod_ID, stratumIndex, stratumName) select (select stratumMEthod_ID from stratumMethod where accessionCode='VB.SM.5.NATIONALPARKSER'), 'Unkn','Unknown stratum' ;
INSERT INTO stratumType (stratumMethod_ID, stratumIndex, stratumName) select (select stratumMEthod_ID from stratumMethod where accessionCode='VB.SM.5.NATIONALPARKSER'),'S','Shrub';
INSERT INTO stratumType (stratumMethod_ID, stratumIndex, stratumName) select (select stratumMEthod_ID from stratumMethod where accessionCode='VB.SM.5.NATIONALPARKSER'),'T','Tree';