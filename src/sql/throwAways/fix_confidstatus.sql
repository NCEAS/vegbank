
--this updates any confidentiality status values that are out of range for it
update plot set confidentialitystatus=0 where confidentialitystatus>3;