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

-- temp dolpr views:

drop view temp_doplr_export_pre5;
drop view temp_doplr_export_pre4;
drop view temp_doplr_export_pre3;
drop view temp_doplr_export_pre2;
drop view temp_doplr_export_pre1;
drop view temp_doplr_county;
drop view temp_doplr_obs;
drop view temp_doplr_comm;
--temp rkp views:

drop view temp_rkp_allto_exp;
drop view temp_rkp_to_fullplotcvr;;
drop view temp_rkp_to_maxstrcvr;
drop view temp_rkp_allto;
