load data

INFILE '../data/ecoart/tables/GROUP.txt'

append
into table GROUP_
fields terminated by '|' optionally enclosed by '"'
trailing nullcols
(
GroupKey,
GroupCode,
GroupName,
GroupDesc,
UpdateDate,
SubClassKey
)

