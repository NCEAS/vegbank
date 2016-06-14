-- update commLevel list to reflect NVC v2
--update sort
update dba_fieldList set listValueSortOrder = 10*listValueSortOrder where fieldName='commLevel';

-- update descriptions
update dba_fieldList set listvalueDescription = '(obsolete) ' || listValueDescription  where fieldName='commLevel' and listValue<>'other' and listValue<>'association' and listValue<>'alliance';
update dba_fieldList set listvalueDescription = '(obsolete)' where fieldName='commLevel' and (listValueDescription is null) and listValue<>'other' and listValue<>'association' and listValue<>'alliance';

-- mark old values as old
update dba_fieldList set listvalue = 'formation (old)' where fieldName='commLevel' and  listValue='formation';

--add new values:
INSERT into dba_fieldList (tableName, fieldName, listValue, listValueDescription, listvaluesortorder) select 'commStatus', 'commLevel', 'class','(NVC v2) A vegetation classification unit of high rank (1st level) defined by broad combinations of dominant general growth forms adapted to basic moisture, temperature, and/or substrate or aquatic conditions.',1;
INSERT into dba_fieldList (tableName, fieldName, listValue, listValueDescription, listvaluesortorder) select 'commStatus', 'commLevel', 'subclass','(NVC v2) A vegetation classification unit of high rank (2nd level) defined by combinations of general dominant and diagnostic growth forms that reflect global macroclimatic factors driven primarily by latitude and continental position, or that reflect overriding substrate or aquatic conditions. (Whittaker 1975).',2;
INSERT into dba_fieldList (tableName, fieldName, listValue, listValueDescription, listvaluesortorder) select 'commStatus', 'commLevel', 'formation','(NVC v2) A vegetation classification unit of high rank (3rd level) defined by combinations of dominant and diagnostic growth forms that reflect global macroclimatic conditions as modified by altitude, seasonality of precipitation, substrates, and hydrologic conditions. (Whittaker 1975, Lincoln et al. 1998)',3;
INSERT into dba_fieldList (tableName, fieldName, listValue, listValueDescription, listvaluesortorder) select 'commStatus', 'commLevel', 'division','(NVC v2) A vegetation classification unit of intermediate rank (4th level) defined by combinations of dominant and diagnostic growth forms and a broad set of diagnostic plant taxa that reflect biogeographic differences in composition and continental differences in mesoclimate, geology, substrates, hydrology, and disturbance regimes. (Westhoff and van der Maarel 1973, pg. 664-665, Whittaker 1975)',4;
INSERT into dba_fieldList (tableName, fieldName, listValue, listValueDescription, listvaluesortorder) select 'commStatus', 'commLevel', 'macrogroup','(NVC v2) A vegetation classification unit of intermediate rank (5th level) defined by combinations of  moderate sets of diagnostic plant species and diagnostic growth forms that reflect biogeographic differences in composition and sub-continental to regional differences in mesoclimate, geology, substrates, hydrology, and disturbance regimes (cf. Pignatti et al. 1995).',5;
INSERT into dba_fieldList (tableName, fieldName, listValue, listValueDescription, listvaluesortorder) select 'commStatus', 'commLevel', 'group','(NVC v2) A vegetation classification unit of intermediate rank (6th level) defined by combinations of relatively narrow sets of diagnostic plant species (including dominants and co-dominants), broadly similar composition, and diagnostic growth forms that reflect biogeographic differences in mesoclimate, geology, substrates, hydrology, and disturbance regimes (cf. Pignatti et al. 1995, Specht and Specht 2001)',6;

--update sort order to alliance and association
update dba_fieldList set listValueSortOrder =7 where fieldName='commLevel' and listValue='alliance';
update dba_fieldList set listValueSortOrder =8 where fieldName='commLevel' and listValue='association';