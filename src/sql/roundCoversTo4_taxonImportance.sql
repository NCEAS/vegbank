-- rounds taxonImportance cover values to 1000's for numbers that have come in with too many significant digits.

update taxonimportance set cover= round(cast (cover as numeric),4)  where cover is not null and round(cast (cover as numeric),7) <> cover and cover> 0.1 ;