/*
 * Creates the table for storing the usda plants data
 */

drop table plantNames;
 
CREATE TABLE plantNames (
symbol varchar(20),
scientificName varchar(150),
commonName varchar(150),
family varchar(45)
);
