--create view of states to get them easily
drop view usStates;
create view usStates AS select * from namedplace where placeSystem='region|state|province' and placeCode like 'n-us-%' ;


---add all counties:
insert into namedPlaceCorrelation (childPlace_ID, parentPlace_ID , placeConvergence) 
 select namedplace.namedPlace_ID, usStates.namedPlace_ID, 'greater than' 
 from namedplace, usStates where namedplace.placeDescription=usStates.placeName and namedplace.placeSystem='county';
 
 --add almost all quads:
 insert into namedPlaceCorrelation (childPlace_ID, parentPlace_ID , placeConvergence)  
 select namedplace.namedPlace_ID, usStates.namedPlace_ID, 'greater than' 
 from namedplace, usStates where namedplace.placeDescription like usStates.placeName || ', %' and namedplace.placeSystem='quadrangle' ;
 
 --add rest of quads: Puerto Rico, Virgin Islands, Pacific Islands:
 insert into namedPlaceCorrelation (childPlace_ID, parentPlace_ID , placeConvergence)  
  select namedplace.namedPlace_ID, (select np_2.namedplace_id from namedplace as np_2 where np_2.accessionCode='VB.NP.348.PUERTORICO'), 'greater than' 
  from namedplace where namedplace.placeDescription like 'Puerto Rico, %' and namedplace.placeSystem='quadrangle' ;
 
 insert into namedPlaceCorrelation (childPlace_ID, parentPlace_ID , placeConvergence)  
   select namedplace.namedPlace_ID, (select np_2.namedplace_id from namedplace as np_2 where np_2.accessionCode='VB.NP.184.ATLANTICOCEAN'), 'greater than' 
   from namedplace where namedplace.placeDescription like 'Virgin Islands, %' and namedplace.placeSystem='quadrangle' ;
 
 insert into namedPlaceCorrelation (childPlace_ID, parentPlace_ID , placeConvergence)  
   select namedplace.namedPlace_ID, (select np_2.namedplace_id from namedplace as np_2 where np_2.accessionCode='VB.NP.430.PACIFICOCEAN'), 'greater than' 
   from namedplace where namedplace.placeDescription like 'Pacific Islands, %' and namedplace.placeSystem='quadrangle' ;
 
 
 
 
--done with quads

--get most states:
 INSERT INTO namedPlaceCorrelation (childPlace_ID, parentPlace_ID , placeConvergence)
 select namedplace.namedPlace_ID,  namedplace2.namedPlace_ID, 'greater than' 
 FROM namedplace, namedplace as namedplace2 WHERE namedplace.placeSystem='region|state|province' and substr(namedplace.placeCode,1,4)<>namedplace.placeCode 
 AND namedplace2.placeCode=substr(namedplace.placeCode,1,4) ;
 
 --get most countries/regions to continents:
  INSERT INTO namedPlaceCorrelation (childPlace_ID, parentPlace_ID , placeConvergence)
  select namedplace.namedPlace_ID,  namedplace2.namedPlace_ID,  'greater than' 
  FROM namedplace, namedplace as namedplace2 WHERE namedplace.placeSystem='area|country|territory' and substr(namedplace.placeCode,1,1)<>namedplace.placeCode 
  AND namedplace2.placeCode=substr(namedplace.placeCode,1,1) and namedplace2.placeSystem='continent';
  
 -- clean up misc places
 INSERT INTO namedPlaceCorrelation (childPlace_ID, parentPlace_ID , placeConvergence)
 select namedPlace_ID, (select np2.namedPlace_ID from namedPlace as np2 where np2.accessionCode='VB.NP.360.UNITEDSTATES'), 'greater than' 
 FROM namedplace where namedplace.accessionCode IN ('VB.NP.380.LAKESTATES','VB.NP.408.PACIFICNORTHWES','VB.NP.414.SOUTHEAST');


 INSERT INTO namedPlaceCorrelation (childPlace_ID, parentPlace_ID , placeConvergence)
 select namedPlace_ID, (select np2.namedPlace_ID from namedPlace as np2 where np2.accessionCode='VB.NP.265.RUSSIA'), 'greater than' 
 FROM namedplace where namedplace.accessionCode IN ('VB.NP.266.CAUCASUS','VB.NP.267.CAUCASUSNORTHER','VB.NP.268.CENTRALCHERNOZE','VB.NP.269.RUSSIANFAREAST','VB.NP.270.SIBERIA','VB.NP.271.SIBERIAEASTERN','VB.NP.272.SIBERIAWESTERN','VB.NP.273.URALMOUNTAINS','VB.NP.274.VOLGARIVER');

drop view usStates;


-- fix quadrangle overlaps with parent:
update namedplacecorrelation set placeconvergence='overlapping' where childplace_id in 
  (select namedplace_ID from namedplace where placeSystem='quadrangle' 
   AND  placeCode in (select placeCode from namedPlace where placeSystem='quadrangle' group by placecode having count(1) > 1));