load data

INFILE '../data/ecoart/tables/FORMATION.txt'

append
into table FORMATION
fields terminated by '|' optionally enclosed by '"'
trailing nullcols
(
FormationKey,
FormationCode,
FormationName,
UpdateDate,
SubGroupKey,
Wetland,
Leaderesp,
Edition,
Edauthor,
AlliancesDef
)

