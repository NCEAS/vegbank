load data

INFILE '../data/ecoart/tables/ALLIANCE.txt'

append
into table ALLIANCE
fields terminated by '|' optionally enclosed by '"'
trailing nullcols
(
AllianceKey,
AllianceStatus,
Leaderesp,
FormationCode,
AllianceNum,
FormationKey,
AllianceName,
AllianceNameTrans,
AllianceDesc,
System,
AssocDef,
Edition,
Edauthor,
AllianceOriginDate,
UpdateDate
)

