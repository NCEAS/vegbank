--Views to remove:
drop view view_countcomms_perobs;
drop view view_countspecies_perobs ;
drop view view_countcomms_perobs_pre;
drop view view_countspecies_perobs_pre ;

drop view view_plantConcept_ordered ;
drop view view_project_countobs;

drop view view_browsenamedplace_bystate;

drop view view_temp_vbrcomms_get;

--fields to drop (denorm fields):
ALTER TABLE embargo DROP COLUMN usr_ID ;
