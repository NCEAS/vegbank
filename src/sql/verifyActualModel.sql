
select (select relname from pg_statio_all_tables where attrelid=relid) as mtl_tableName, attname, 

(select typname from pg_type where pg_type.oid=atttypid) || CASE WHEN atttypmod>4 THEN ' (' || atttypmod-4 || ') ' ELSE ' ' END as mtl_dataType, CASE WHEN attnotnull THEN 'no nulls' ELSE 'yes nulls' END as mtl_nulls, 

mtl_FTbl || '.' || mtl_fFldNm as MTL_references, 'dil' as unknownModel, 'if i''m lowercase, i''m from database, else from model xml' as unkn2 from pg_attribute LEFT JOIN (select conname, 
(select relname from pg_statio_all_tables where pg_constraint.conrelid=pg_statio_all_tables.relid) as mtl_tblNm, 

(select relname from pg_statio_all_tables where pg_constraint.confrelid=pg_statio_all_tables.relid) as mtl_FTbl, 
(select attname from pg_attribute as pg2 where attrelid=conrelid and attnum =conkey[1]) as mtl_fldNm, 
(select attname from pg_attribute as pg3 where attrelid=confrelid and attnum =confkey[1]) as mtl_fFldNm 

FROM pg_constraint WHERE conrelid>0 and confrelid>0) as mtl_cons 

ON (select relname from pg_statio_all_tables where attrelid=relid)=mtl_tblNm and pg_attribute.attname=mtl_fldNm where attstattarget=-1 and pg_attribute.attrelid in (select relid from pg_statio_all_tables where schemaname='public') 
GROUP BY pg_attribute.attrelid, attName, pg_attribute.atttypid, pg_attribute.atttypmod, pg_attribute.attnotnull, 
  mtl_cons.mtl_ftbl, mtl_cons.mtl_ffldnm
  HAVING (select relname from pg_statio_all_tables where attrelid=relid) NOT LIKE 'dba_%' AND 
         (select relname from pg_statio_all_tables where attrelid=relid) NOT LIKE 'temptbl_%'
ORDER BY (select relname from pg_statio_all_tables where attrelid=relid),  replace(attname,'_','a')
;
