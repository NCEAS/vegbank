load data
infile '/home/computer/harris/projects/vegclass/veg_plot/plotData/plantsDatabase.bin'

append
into table plantNames
fields terminated by ',' optionally enclosed by '"'
trailing nullcols
(
symbol,
scientificName,
commonName,
family
)
