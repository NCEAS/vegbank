load data

INFILE '../data/ecoart/tables/SUBGROUP.txt'

append
into table SUBGROUP
fields terminated by '|' optionally enclosed by '"'
trailing nullcols
(
SubgroupKey,
SubgroupCode,
SubgroupName,
SubgroupDesc,
UpdateDate,
GroupKey
)

