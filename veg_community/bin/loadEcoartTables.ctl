load data
infile '../data/ecoart/tables/CLASS.txt'

append
into table CLASS
fields terminated by '|' optionally enclosed by '"'
trailing nullcols
(
ClassKey,
ClassCode,
ClassName,
ClassDesc,
UpdateDate,
System
)


