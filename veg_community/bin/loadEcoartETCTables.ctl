load data

INFILE '../data/ecoart/tables/ETC.txt'

append
into table ETC
fields terminated by '|' optionally enclosed by '"'
trailing nullcols
(
Elcode,
AccosStatus, 
ClassifResp,
ClassifKey, 
FormationCode,
AllianceNum,
Gname,
GnameTrans,
Gcomname,
AssocOriginDate,
ClassifUsed,
GconfLevel,
Level_,
System,
Nation,
SubformationName,
Acronym,
Author,
UsCrosswalked,
Gcrosswalked,
Grank,
Grankdate,
RoundedGrank,
Grankform,
Grankresp,
CcagResp,
Conspl,
Consplresp,
Stewab,
Stewabresp,
Jurisendem,
Gslide,
Gupdate
)

