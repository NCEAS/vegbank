load data

INFILE '../data/ecoart/tables/SUBCLASS.txt'

append
into table SUBCLASS
fields terminated by '|' optionally enclosed by '"'
trailing nullcols
(
SubClassKey,
SubClassCode,
SubClassName,
SubClassDesc,
UpdateDate,
ClassKey
)

