alter table plantUsage alter column plantConcept_ID drop not null;
alter table plantUsage alter column party_ID drop not null;
alter table commUsage alter column party_ID drop not null;
alter table commUsage alter column commConcept_ID drop not null;
