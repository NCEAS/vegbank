-- first have to drop views (see SQL files)

alter table observation alter column homogeneity set data type varchar(255);
alter table observation alter column phenologicAspect set data type varchar(255);
alter table observation alter column representativeness set data type varchar(255);
alter table observation alter column standMaturity set data type varchar(255);
alter table observation alter column hydrologicRegime set data type varchar(255);
alter table observation alter column soilMoistureRegime set data type varchar(255);
alter table observation alter column soilDrainage set data type varchar(255);
alter table observation alter column waterSalinity set data type varchar(255);

-- then create views back (see SQL files)
