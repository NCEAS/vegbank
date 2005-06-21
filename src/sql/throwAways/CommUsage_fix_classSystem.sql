-- There are three solutions to this problem.
-- Problem ... all current classsystems not on system defined part of open picklist
--    We should eat our own dogfood after all
-- 1) Run this script to bring db into alignment with picklist
-- 2) Don't have party indicator in the picklist names, the db knows the party
-- 3) What Problem ?
UPDATE commusage SET classsystem  ='NVC-Code' WHERE classsystem = 'CEGL';
UPDATE commusage SET classsystem  ='NVC-Scientific' WHERE classsystem = 'NVC';
UPDATE commusage SET classsystem  ='NVC-English Common' WHERE classsystem = 'English Common';

update commusage set classsystem='Other' where classsystem is null;
update commusage set classsystem='NVC-Code' where classsystem = 'key';
update commusage set classsystem='NVC-Code' where classsystem = 'CODE';
update commusage set classsystem='NVC-Code' where classsystem = 'elcode';
update commusage set classsystem='NVC-Scientific' where classsystem = 'scientific name without author';
update commusage set classsystem='NVC-English Common' where classsystem = 'English common name';
update commusage set classsystem='NVC-Scientific translated' where classsystem = 'translated name';