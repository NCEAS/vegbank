drop trigger udi_commconc_acccode_trig ON userdatasetitem;
drop function upd_commConcAccCodeUDI();

CREATE FUNCTION upd_commConcAccCodeUDI() RETURNS trigger AS $commconcacccodeudi$
BEGIN
UPDATE userDatasetItem SET itemAccessionCode=(select accessionCode from commConcept where userDatasetItem.itemRecord=commConcept.commConcept_ID) where itemTable ilike 'commConcept' and itemRecord in (select commconcept_Id from commconcept where accessioncode is not null);
RETURN NULL;
END;
$commconcacccodeudi$
LANGUAGE plpgsql;

create trigger udi_commconc_acccode_trig AFTER INSERT ON userdatasetitem
FOR EACH STATEMENT EXECUTE PROCEDURE upd_commConcAccCodeUDI();

-- this tested and works successfully on manually added UDI 2016-05-11 by Michael Lee
