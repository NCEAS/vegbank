INSERT INTO party (surname,givenName,email) values ('Osborne','Dianne','dianne_osborne@blm.gov');
INSERT INTO address (party_ID, email, deliveryPoint, city, administrativeArea, country, postalCode) values ((SELECT min(party_ID) from party where email='dianne_osborne@blm.gov'), 'dianne_osborne@blm.gov','Denver Federal Ctr,  Bldg. 50','Denver','CO','US','80225');
INSERT INTO telephone (party_ID, phoneNumber,phoneType) values ((SELECT min(party_ID) from party where email='dianne_osborne@blm.gov'),'303 236-5664','work');
UPDATE usr SET party_ID=(SELECT min(party_ID) from party where email='dianne_osborne@blm.gov') WHERE USR_ID=395;
INSERT INTO party (surname,givenName,email) values ('Fischer','Richard','fischer@wes.army.mil');
INSERT INTO address (party_ID, email, deliveryPoint, city, administrativeArea, country, postalCode) values ((SELECT min(party_ID) from party where email='fischer@wes.army.mil'), 'fischer@wes.army.mil','3909 Halls Ferry Rd.','Vicksburg','MS','US','39180');
INSERT INTO telephone (party_ID, phoneNumber,phoneType) values ((SELECT min(party_ID) from party where email='fischer@wes.army.mil'),'601 634-3983','work');
UPDATE usr SET party_ID=(SELECT min(party_ID) from party where email='fischer@wes.army.mil') WHERE USR_ID=396;
INSERT INTO party (surname,givenName,email) values ('Begley','Ralph','rjbegley@acm.org');
INSERT INTO address (party_ID, email, deliveryPoint, city, administrativeArea, country, postalCode) values ((SELECT min(party_ID) from party where email='rjbegley@acm.org'), 'rjbegley@acm.org','1072 Casitas Pass Rd #319','Carpinteria','CA','US','93013');
INSERT INTO telephone (party_ID, phoneNumber,phoneType) values ((SELECT min(party_ID) from party where email='rjbegley@acm.org'),'805 745 5266','work');
UPDATE usr SET party_ID=(SELECT min(party_ID) from party where email='rjbegley@acm.org') WHERE USR_ID=397;
INSERT INTO party (surname,givenName,email) values ('Teague','Judy','judy_teague@natureserve.org');
INSERT INTO address (party_ID, email, deliveryPoint, city, administrativeArea, country, postalCode) values ((SELECT min(party_ID) from party where email='judy_teague@natureserve.org'), 'judy_teague@natureserve.org','6114 Fayetteville Rd., 109','Durham','NC','US','27713');
INSERT INTO telephone (party_ID, phoneNumber,phoneType) values ((SELECT min(party_ID) from party where email='judy_teague@natureserve.org'),'919 4847857','work');
UPDATE usr SET party_ID=(SELECT min(party_ID) from party where email='judy_teague@natureserve.org') WHERE USR_ID=398;
INSERT INTO party (surname,givenName,email) values ('Xi','Weimin','weimin@email.unc.edu');
---
---
UPDATE usr SET party_ID=(SELECT min(party_ID) from party where email='weimin@email.unc.edu') WHERE USR_ID=399;
INSERT INTO party (surname,givenName,email) values ('White','Rickie','rickie_white@natureserve.org');
INSERT INTO address (party_ID, email, deliveryPoint, city, administrativeArea, country, postalCode) values ((SELECT min(party_ID) from party where email='rickie_white@natureserve.org'), 'rickie_white@natureserve.org','6114 Fayetteville Road Ste 109','Durham','NC','US','27713');
INSERT INTO telephone (party_ID, phoneNumber,phoneType) values ((SELECT min(party_ID) from party where email='rickie_white@natureserve.org'),'919 484-7857','work');
UPDATE usr SET party_ID=(SELECT min(party_ID) from party where email='rickie_white@natureserve.org') WHERE USR_ID=400;
INSERT INTO party (surname,givenName,email) values ('McCartney','Willard','bmccartney@mbakercorp.com');
INSERT INTO address (party_ID, email, deliveryPoint, city, administrativeArea, country, postalCode) values ((SELECT min(party_ID) from party where email='bmccartney@mbakercorp.com'), 'bmccartney@mbakercorp.com','770 Lynnhaven Pkwy','Virginia Beach','VA','US','23452');
INSERT INTO telephone (party_ID, phoneNumber,phoneType) values ((SELECT min(party_ID) from party where email='bmccartney@mbakercorp.com'),'757 6315466','work');
UPDATE usr SET party_ID=(SELECT min(party_ID) from party where email='bmccartney@mbakercorp.com') WHERE USR_ID=418;
INSERT INTO party (surname,givenName,email) values ('Decker','Karin','kdecker@lamar.colostate.edu');
---
---
UPDATE usr SET party_ID=(SELECT min(party_ID) from party where email='kdecker@lamar.colostate.edu') WHERE USR_ID=419;
INSERT INTO party (surname,givenName,email) values ('Johnston','Bill','bill.johnston22@comcast.net');
INSERT INTO address (party_ID, email, deliveryPoint, city, administrativeArea, country, postalCode) values ((SELECT min(party_ID) from party where email='bill.johnston22@comcast.net'), 'bill.johnston22@comcast.net','1101 Tuskegee Drive','Oak Ridge','TN','US','37830');
---
UPDATE usr SET party_ID=(SELECT min(party_ID) from party where email='bill.johnston22@comcast.net') WHERE USR_ID=420;
INSERT INTO party (surname,givenName,email) values ('Ryan','Liam','forficula@eircom.net');
---
---
UPDATE usr SET party_ID=(SELECT min(party_ID) from party where email='forficula@eircom.net') WHERE USR_ID=421;
INSERT INTO party (surname,givenName,email) values ('Bloye','Robert','Bloyerob@MSU.edu');
INSERT INTO address (party_ID, email, deliveryPoint, city, administrativeArea, country, postalCode) values ((SELECT min(party_ID) from party where email='Bloyerob@MSU.edu'), 'Bloyerob@MSU.edu','126 Natural Resources Bldg','East Lansing','MI','US','48824-1222');
INSERT INTO telephone (party_ID, phoneNumber,phoneType) values ((SELECT min(party_ID) from party where email='Bloyerob@MSU.edu'),'517 355 6218','work');
UPDATE usr SET party_ID=(SELECT min(party_ID) from party where email='Bloyerob@MSU.edu') WHERE USR_ID=436;
INSERT INTO party (surname,givenName,email) values ('Alonso','Maria','mariaralonso@aol.com');
INSERT INTO address (party_ID, email, deliveryPoint, city, administrativeArea, country, postalCode) values ((SELECT min(party_ID) from party where email='mariaralonso@aol.com'), 'mariaralonso@aol.com','1800 NW 10th Av','Miami','FL','US','33139');
INSERT INTO telephone (party_ID, phoneNumber,phoneType) values ((SELECT min(party_ID) from party where email='mariaralonso@aol.com'),'305 3223773','work');
UPDATE usr SET party_ID=(SELECT min(party_ID) from party where email='mariaralonso@aol.com') WHERE USR_ID=437;
INSERT INTO party (surname,givenName,email) values ('BR','Ramesh','ramesh.br@ifpindia.org');
INSERT INTO address (party_ID, email, deliveryPoint, city, administrativeArea, country, postalCode) values ((SELECT min(party_ID) from party where email='ramesh.br@ifpindia.org'), 'ramesh.br@ifpindia.org','11, St. Louid Street','Pondicherry',NULL,'IND','605 001');
INSERT INTO telephone (party_ID, phoneNumber,phoneType) values ((SELECT min(party_ID) from party where email='ramesh.br@ifpindia.org'),'91  2334168','work');
UPDATE usr SET party_ID=(SELECT min(party_ID) from party where email='ramesh.br@ifpindia.org') WHERE USR_ID=438;
INSERT INTO party (surname,givenName,email) values ('BR','Ramesh','ifpbota@satyam.net.in');
INSERT INTO address (party_ID, email, deliveryPoint, city, administrativeArea, country, postalCode) values ((SELECT min(party_ID) from party where email='ifpbota@satyam.net.in'), 'ifpbota@satyam.net.in','11, St. Louid Street','Pondicherry',NULL,'IND','605 001');
---
UPDATE usr SET party_ID=(SELECT min(party_ID) from party where email='ifpbota@satyam.net.in') WHERE USR_ID=439;
INSERT INTO party (surname,givenName,email) values ('Skeel','Virginia ','virginia.skeel@parsons.com');
INSERT INTO address (party_ID, email, deliveryPoint, city, administrativeArea, country, postalCode) values ((SELECT min(party_ID) from party where email='virginia.skeel@parsons.com'), 'virginia.skeel@parsons.com','400 Woods Mill Road, Suite 330','St. Louis','MO','US','63017');
INSERT INTO telephone (party_ID, phoneNumber,phoneType) values ((SELECT min(party_ID) from party where email='virginia.skeel@parsons.com'),'314 576-7330','work');
UPDATE usr SET party_ID=(SELECT min(party_ID) from party where email='virginia.skeel@parsons.com') WHERE USR_ID=440;
INSERT INTO party (surname,givenName,email) values ('Smith','Paul','wluser100@yahoo.com');
---
---
UPDATE usr SET party_ID=(SELECT min(party_ID) from party where email='wluser100@yahoo.com') WHERE USR_ID=441;
INSERT INTO party (surname,givenName,email) values ('Lander','Nicholas','nickl@calm.wa.gov.au');
---
---
UPDATE usr SET party_ID=(SELECT min(party_ID) from party where email='nickl@calm.wa.gov.au') WHERE USR_ID=442;
INSERT INTO party (surname,givenName,email) values ('Elizabeth','Crowe','ecrowe@state.mt.us');
---
---
UPDATE usr SET party_ID=(SELECT min(party_ID) from party where email='ecrowe@state.mt.us') WHERE USR_ID=443;
---
---
DELETE FROM party where party_id=226;
DELETE FROM usr where usr_id=96;


