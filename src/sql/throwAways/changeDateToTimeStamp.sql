
 
ALTER TABLE commCorrelation ADD COLUMN          correlationStart2     timestamp with time zone;
ALTER TABLE commCorrelation ADD COLUMN 		correlationStop2 timestamp with time zone;
ALTER TABLE commName ADD COLUMN 		dateEntered2 timestamp with time zone;
ALTER TABLE commStatus ADD COLUMN 		startDate2 timestamp with time zone;
ALTER TABLE commStatus ADD COLUMN 		stopDate2 timestamp with time zone;
ALTER TABLE commUsage ADD COLUMN 		usageStart2 timestamp with time zone;
ALTER TABLE commUsage ADD COLUMN 		usageStop2 timestamp with time zone;
ALTER TABLE plantCorrelation ADD COLUMN 	correlationStart2 timestamp with time zone;
ALTER TABLE plantCorrelation ADD COLUMN 	correlationStop2 timestamp with time zone;
ALTER TABLE plantName ADD COLUMN 		dateEntered2 timestamp with time zone;
ALTER TABLE plantStatus ADD COLUMN 		startDate2 timestamp with time zone;
ALTER TABLE plantStatus ADD COLUMN 		stopDate2 timestamp with time zone;
ALTER TABLE plantUsage ADD COLUMN 		usageStart2 timestamp with time zone;
ALTER TABLE plantUsage ADD COLUMN 		usageStop2 timestamp with time zone;
ALTER TABLE address ADD COLUMN 			addressStartDate2 timestamp with time zone;
ALTER TABLE reference ADD COLUMN 		pubDate2 timestamp with time zone;
ALTER TABLE reference ADD COLUMN 		accessDate2 timestamp with time zone;
ALTER TABLE reference ADD COLUMN 		conferenceDate2 timestamp with time zone;
ALTER TABLE commClass ADD COLUMN 		classStartDate2 timestamp with time zone;
ALTER TABLE commClass ADD COLUMN 		classStopDate2 timestamp with time zone;
ALTER TABLE graphic ADD COLUMN 			graphicDate2 timestamp with time zone;
ALTER TABLE note ADD COLUMN 			noteDate2 timestamp with time zone;
ALTER TABLE observation ADD COLUMN 		obsStartDate2 timestamp with time zone;
ALTER TABLE observation ADD COLUMN 		obsEndDate2 timestamp with time zone;
ALTER TABLE plot ADD COLUMN 			dateentered2 timestamp with time zone;
ALTER TABLE project ADD COLUMN 			startDate2 timestamp with time zone;
ALTER TABLE project ADD COLUMN 			stopDate2 timestamp with time zone;
ALTER TABLE revision ADD COLUMN 		revisionDate2 timestamp with time zone;
ALTER TABLE observationContributor ADD COLUMN 	contributionDate2 timestamp with time zone;
ALTER TABLE observationSynonym ADD COLUMN 	classStartDate2 timestamp with time zone;
ALTER TABLE observationSynonym ADD COLUMN 	classStopDate2 timestamp with time zone;
ALTER TABLE taxonInterpretation ADD COLUMN 	interpretationDate2 timestamp with time zone;
ALTER TABLE observation ADD COLUMN 		dateEntered2 timestamp with time zone;
ALTER TABLE taxonInterpretation ADD COLUMN 	collectionDate2 timestamp with time zone;
ALTER TABLE embargo ADD COLUMN 			embargoStart2 timestamp with time zone;
ALTER TABLE embargo ADD COLUMN 			embargoStop2 timestamp with time zone;
ALTER TABLE partyMember ADD COLUMN 		memberStart2 timestamp with time zone;
ALTER TABLE partyMember ADD COLUMN 		memberStop2 timestamp with time zone;





UPDATE commCorrelation SET          correlationStart2=correlationStart WHERE correlationStart IS NOT NULL;
UPDATE commCorrelation SET 	    correlationStop2=correlationStop WHERE correlationStop IS NOT NULL;
UPDATE commName SET 		    dateEntered2=dateEntered WHERE dateEntered IS NOT NULL;
UPDATE commStatus SET 		    startDate2=startDate WHERE startDate IS NOT NULL;
UPDATE commStatus SET 		    stopDate2=stopDate WHERE stopDate IS NOT NULL;
UPDATE commUsage SET 		    usageStart2=usageStart WHERE usageStart IS NOT NULL;
UPDATE commUsage SET 		    usageStop2=usageStop WHERE usageStop IS NOT NULL;
UPDATE plantCorrelation SET 	    correlationStart2=correlationStart WHERE correlationStart IS NOT NULL;
UPDATE plantCorrelation SET 	    correlationStop2=correlationStop WHERE correlationStop IS NOT NULL;
UPDATE plantName SET 		    dateEntered2=dateEntered WHERE dateEntered IS NOT NULL;
UPDATE plantStatus SET 		    startDate2=startDate WHERE startDate IS NOT NULL;
UPDATE plantStatus SET 		    stopDate2=stopDate WHERE stopDate IS NOT NULL;
UPDATE plantUsage SET 		    usageStart2=usageStart WHERE usageStart IS NOT NULL;
UPDATE plantUsage SET 		    usageStop2=usageStop WHERE usageStop IS NOT NULL;
UPDATE address SET 		    addressStartDate2=addressStartDate WHERE addressStartDate IS NOT NULL;
UPDATE reference SET 		    pubDate2=pubDate WHERE pubDate IS NOT NULL;
UPDATE reference SET 		    accessDate2=accessDate WHERE accessDate IS NOT NULL;
UPDATE reference SET 		    conferenceDate2=conferenceDate WHERE conferenceDate IS NOT NULL;
UPDATE commClass SET 		    classStartDate2=classStartDate WHERE classStartDate IS NOT NULL;
UPDATE commClass SET 		    classStopDate2=classStopDate WHERE classStopDate IS NOT NULL;
UPDATE graphic SET 		    graphicDate2=graphicDate WHERE graphicDate IS NOT NULL;
UPDATE note SET 		    noteDate2=noteDate WHERE noteDate IS NOT NULL;
UPDATE observation SET 		    obsStartDate2=obsStartDate WHERE obsStartDate IS NOT NULL;
UPDATE observation SET 		    obsEndDate2=obsEndDate WHERE obsEndDate IS NOT NULL;
UPDATE plot SET 		    dateentered2=dateentered WHERE dateentered IS NOT NULL;
UPDATE project SET 		    startDate2=startDate WHERE startDate IS NOT NULL;
UPDATE project SET 		    stopDate2=stopDate WHERE stopDate IS NOT NULL;
UPDATE revision SET 		    revisionDate2=revisionDate WHERE revisionDate IS NOT NULL;
UPDATE observationContributor SET   contributionDate2=contributionDate WHERE contributionDate IS NOT NULL;
UPDATE observationSynonym SET 	    classStartDate2=classStartDate WHERE classStartDate IS NOT NULL;
UPDATE observationSynonym SET 	    classStopDate2=classStopDate WHERE classStopDate IS NOT NULL;
UPDATE taxonInterpretation SET 	    interpretationDate2=interpretationDate WHERE interpretationDate IS NOT NULL;
UPDATE observation SET 		    dateEntered2=dateEntered WHERE dateEntered IS NOT NULL;
UPDATE taxonInterpretation SET 	    collectionDate2=collectionDate WHERE collectionDate IS NOT NULL;
UPDATE embargo SET 		    embargoStart2=embargoStart WHERE embargoStart IS NOT NULL;
UPDATE embargo SET 		    embargoStop2=embargoStop WHERE embargoStop IS NOT NULL;
UPDATE partyMember SET 		    memberStart2=memberStart WHERE memberStart IS NOT NULL;
UPDATE partyMember SET 		    memberStop2=memberStop WHERE memberStop IS NOT NULL;



ALTER TABLE commCorrelation DROP COLUMN            correlationStart;
ALTER TABLE commCorrelation DROP COLUMN 	   correlationStop;
ALTER TABLE commName DROP COLUMN 		   dateEntered;
ALTER TABLE commStatus DROP COLUMN 		   startDate;
ALTER TABLE commStatus DROP COLUMN 		   stopDate;
ALTER TABLE commUsage DROP COLUMN 		   usageStart;
ALTER TABLE commUsage DROP COLUMN 		   usageStop;
ALTER TABLE plantCorrelation DROP COLUMN 	   correlationStart;
ALTER TABLE plantCorrelation DROP COLUMN 	   correlationStop;
ALTER TABLE plantName DROP COLUMN 		   dateEntered;
ALTER TABLE plantStatus DROP COLUMN 		   startDate;
ALTER TABLE plantStatus DROP COLUMN 		   stopDate;
ALTER TABLE plantUsage DROP COLUMN 		   usageStart;
ALTER TABLE plantUsage DROP COLUMN 		   usageStop;
ALTER TABLE address DROP COLUMN 		   addressStartDate;
ALTER TABLE reference DROP COLUMN 		   pubDate;
ALTER TABLE reference DROP COLUMN 		   accessDate;
ALTER TABLE reference DROP COLUMN 		   conferenceDate;
ALTER TABLE commClass DROP COLUMN 		   classStartDate;
ALTER TABLE commClass DROP COLUMN 		   classStopDate;
ALTER TABLE graphic DROP COLUMN 		   graphicDate;
ALTER TABLE note DROP COLUMN 			   noteDate;
ALTER TABLE observation DROP COLUMN 		   obsStartDate;
ALTER TABLE observation DROP COLUMN 		   obsEndDate;
ALTER TABLE plot DROP COLUMN 			   dateentered;
ALTER TABLE project DROP COLUMN 		   startDate;
ALTER TABLE project DROP COLUMN 		   stopDate;
ALTER TABLE revision DROP COLUMN 		   revisionDate;
ALTER TABLE observationContributor DROP COLUMN 	   contributionDate;
ALTER TABLE observationSynonym DROP COLUMN 	   classStartDate;
ALTER TABLE observationSynonym DROP COLUMN 	   classStopDate;
ALTER TABLE taxonInterpretation DROP COLUMN 	   interpretationDate;
ALTER TABLE observation DROP COLUMN 		   dateEntered;
ALTER TABLE taxonInterpretation DROP COLUMN 	   collectionDate;
ALTER TABLE embargo DROP COLUMN 		   embargoStart;
ALTER TABLE embargo DROP COLUMN 		   embargoStop;
ALTER TABLE partyMember DROP COLUMN 		   memberStart;
ALTER TABLE partyMember DROP COLUMN 		   memberStop;


ALTER TABLE commCorrelation RENAME COLUMN            correlationStart2 TO  correlationStart;
ALTER TABLE commCorrelation RENAME COLUMN 	   correlationStop2 TO  correlationStop;
ALTER TABLE commName RENAME COLUMN 		   dateEntered2 TO  dateEntered;
ALTER TABLE commStatus RENAME COLUMN 		   startDate2 TO  startDate;
ALTER TABLE commStatus RENAME COLUMN 		   stopDate2 TO  stopDate;
ALTER TABLE commUsage RENAME COLUMN 		   usageStart2 TO  usageStart;
ALTER TABLE commUsage RENAME COLUMN 		   usageStop2 TO  usageStop;
ALTER TABLE plantCorrelation RENAME COLUMN 	   correlationStart2 TO  correlationStart;
ALTER TABLE plantCorrelation RENAME COLUMN 	   correlationStop2 TO  correlationStop;
ALTER TABLE plantName RENAME COLUMN 		   dateEntered2 TO  dateEntered;
ALTER TABLE plantStatus RENAME COLUMN 		   startDate2 TO  startDate;
ALTER TABLE plantStatus RENAME COLUMN 		   stopDate2 TO  stopDate;
ALTER TABLE plantUsage RENAME COLUMN 		   usageStart2 TO  usageStart;
ALTER TABLE plantUsage RENAME COLUMN 		   usageStop2 TO  usageStop;
ALTER TABLE address RENAME COLUMN 		   addressStartDate2 TO  addressStartDate;
ALTER TABLE reference RENAME COLUMN 		   pubDate2 TO  pubDate;
ALTER TABLE reference RENAME COLUMN 		   accessDate2 TO  accessDate;
ALTER TABLE reference RENAME COLUMN 		   conferenceDate2 TO  conferenceDate;
ALTER TABLE commClass RENAME COLUMN 		   classStartDate2 TO  classStartDate;
ALTER TABLE commClass RENAME COLUMN 		   classStopDate2 TO  classStopDate;
ALTER TABLE graphic RENAME COLUMN 		   graphicDate2 TO  graphicDate;
ALTER TABLE note RENAME COLUMN 			   noteDate2 TO  noteDate;
ALTER TABLE observation RENAME COLUMN 		   obsStartDate2 TO  obsStartDate;
ALTER TABLE observation RENAME COLUMN 		   obsEndDate2 TO  obsEndDate;
ALTER TABLE plot RENAME COLUMN 			   dateentered2 TO  dateentered;
ALTER TABLE project RENAME COLUMN 		   startDate2 TO  startDate;
ALTER TABLE project RENAME COLUMN 		   stopDate2 TO  stopDate;
ALTER TABLE revision RENAME COLUMN 		   revisionDate2 TO  revisionDate;
ALTER TABLE observationContributor RENAME COLUMN     contributionDate2 TO  contributionDate;
ALTER TABLE observationSynonym RENAME COLUMN 	   classStartDate2 TO  classStartDate;
ALTER TABLE observationSynonym RENAME COLUMN 	   classStopDate2 TO  classStopDate;
ALTER TABLE taxonInterpretation RENAME COLUMN 	   interpretationDate2 TO  interpretationDate;
ALTER TABLE observation RENAME COLUMN 		   dateEntered2 TO  dateEntered;
ALTER TABLE taxonInterpretation RENAME COLUMN 	   collectionDate2 TO  collectionDate;
ALTER TABLE embargo RENAME COLUMN 		   embargoStart2 TO  embargoStart;
ALTER TABLE embargo RENAME COLUMN 		   embargoStop2 TO  embargoStop;
ALTER TABLE partyMember RENAME COLUMN 		   memberStart2 TO  memberStart;
ALTER TABLE partyMember RENAME COLUMN 		   memberStop2 TO  memberStop;




-----------usr fields, too! -------------
---last_connect
---begin_time
---recordcreationdate
---ownerstop
---ownerstart
---querystop
---querystart
---preferencestop
---preferencestart
---permissionstop
---permissionstart
---notifystart
---notifystop
---lastcheckdate
---itemstart
---itemstop
---datasetstop
---datasetstart	
---
---
---usr
---usr
---userrecordowner
---userrecordowner
---userrecordowner
---userquery
---userquery
---userpreference
---userpreference
---userpermission
---userpermission
---usernotify
---usernotify
---usernotify
---userdatasetitem
---userdatasetitem
---userdataset
---userdataset


ALTER TABLE usr ADD COLUMN				last_connect2  timestamp with time zone;  UPDATE usr SET				last_connect2=last_connect WHERE			last_connect IS NOT NULL; ALTER TABLE usr DROP COLUMN				last_connect;  ALTER TABLE usr RENAME COLUMN				last_connect2 TO			last_connect; 
ALTER TABLE usr ADD COLUMN				begin_time2  timestamp with time zone;  UPDATE usr SET					begin_time2=begin_time WHERE			begin_time IS NOT NULL; ALTER TABLE usr DROP COLUMN				begin_time;  ALTER TABLE usr RENAME COLUMN				begin_time2 TO			begin_time; 
ALTER TABLE userrecordowner ADD COLUMN			recordcreationdate2  timestamp with time zone;  UPDATE userrecordowner SET		recordcreationdate2=recordcreationdate WHERE			recordcreationdate IS NOT NULL; ALTER TABLE userrecordowner DROP COLUMN			recordcreationdate;  ALTER TABLE userrecordowner RENAME COLUMN			recordcreationdate2 TO			recordcreationdate; 
ALTER TABLE userrecordowner ADD COLUMN			ownerstop2  timestamp with time zone;  UPDATE userrecordowner SET			ownerstop2=ownerstop WHERE			ownerstop IS NOT NULL; ALTER TABLE userrecordowner DROP COLUMN			ownerstop;  ALTER TABLE userrecordowner RENAME COLUMN			ownerstop2 TO			ownerstop; 
ALTER TABLE userrecordowner ADD COLUMN			ownerstart2  timestamp with time zone;  UPDATE userrecordowner SET			ownerstart2=ownerstart WHERE			ownerstart IS NOT NULL; ALTER TABLE userrecordowner DROP COLUMN			ownerstart;  ALTER TABLE userrecordowner RENAME COLUMN			ownerstart2 TO			ownerstart; 
ALTER TABLE userquery ADD COLUMN			querystop2  timestamp with time zone;  UPDATE userquery SET				querystop2=querystop WHERE			querystop IS NOT NULL; ALTER TABLE userquery DROP COLUMN				querystop;  ALTER TABLE userquery RENAME COLUMN				querystop2 TO			querystop; 
ALTER TABLE userquery ADD COLUMN			querystart2  timestamp with time zone;  UPDATE userquery SET				querystart2=querystart WHERE			querystart IS NOT NULL; ALTER TABLE userquery DROP COLUMN				querystart;  ALTER TABLE userquery RENAME COLUMN				querystart2 TO			querystart; 
ALTER TABLE userpreference ADD COLUMN			preferencestop2  timestamp with time zone;  UPDATE userpreference SET			preferencestop2=preferencestop WHERE			preferencestop IS NOT NULL; ALTER TABLE userpreference DROP COLUMN			preferencestop;  ALTER TABLE userpreference RENAME COLUMN			preferencestop2 TO			preferencestop; 
ALTER TABLE userpreference ADD COLUMN			preferencestart2  timestamp with time zone;  UPDATE userpreference SET			preferencestart2=preferencestart WHERE			preferencestart IS NOT NULL; ALTER TABLE userpreference DROP COLUMN			preferencestart;  ALTER TABLE userpreference RENAME COLUMN			preferencestart2 TO			preferencestart; 
ALTER TABLE userpermission ADD COLUMN			permissionstop2  timestamp with time zone;  UPDATE userpermission SET			permissionstop2=permissionstop WHERE			permissionstop IS NOT NULL; ALTER TABLE userpermission DROP COLUMN			permissionstop;  ALTER TABLE userpermission RENAME COLUMN			permissionstop2 TO			permissionstop; 
ALTER TABLE userpermission ADD COLUMN			permissionstart2  timestamp with time zone;  UPDATE userpermission SET			permissionstart2=permissionstart WHERE			permissionstart IS NOT NULL; ALTER TABLE userpermission DROP COLUMN			permissionstart;  ALTER TABLE userpermission RENAME COLUMN			permissionstart2 TO			permissionstart; 
ALTER TABLE usernotify ADD COLUMN			notifystart2  timestamp with time zone;  UPDATE usernotify SET				notifystart2=notifystart WHERE			notifystart IS NOT NULL; ALTER TABLE usernotify DROP COLUMN			notifystart;  ALTER TABLE usernotify RENAME COLUMN			notifystart2 TO			notifystart; 
ALTER TABLE usernotify ADD COLUMN			notifystop2  timestamp with time zone;  UPDATE usernotify SET				notifystop2=notifystop WHERE			notifystop IS NOT NULL; ALTER TABLE usernotify DROP COLUMN			notifystop;  ALTER TABLE usernotify RENAME COLUMN			notifystop2 TO			notifystop; 
ALTER TABLE usernotify ADD COLUMN			lastcheckdate2  timestamp with time zone;  UPDATE usernotify SET			lastcheckdate2=lastcheckdate WHERE			lastcheckdate IS NOT NULL; ALTER TABLE usernotify DROP COLUMN			lastcheckdate;  ALTER TABLE usernotify RENAME COLUMN			lastcheckdate2 TO			lastcheckdate; 
ALTER TABLE userdatasetitem ADD COLUMN			itemstart2  timestamp with time zone;  UPDATE userdatasetitem SET			itemstart2=itemstart WHERE			itemstart IS NOT NULL; ALTER TABLE userdatasetitem DROP COLUMN			itemstart;  ALTER TABLE userdatasetitem RENAME COLUMN			itemstart2 TO			itemstart; 
ALTER TABLE userdatasetitem ADD COLUMN			itemstop2  timestamp with time zone;  UPDATE userdatasetitem SET			itemstop2=itemstop WHERE			itemstop IS NOT NULL; ALTER TABLE userdatasetitem DROP COLUMN			itemstop;  ALTER TABLE userdatasetitem RENAME COLUMN			itemstop2 TO			itemstop; 
ALTER TABLE userdataset ADD COLUMN			datasetstop2  timestamp with time zone;  UPDATE userdataset SET				datasetstop2=datasetstop WHERE			datasetstop IS NOT NULL; ALTER TABLE userdataset DROP COLUMN			datasetstop;  ALTER TABLE userdataset RENAME COLUMN			datasetstop2 TO			datasetstop; 
ALTER TABLE userdataset ADD COLUMN			datasetstart2  timestamp with time zone;  UPDATE userdataset SET			datasetstart2=datasetstart	 WHERE			datasetstart	 IS NOT NULL; ALTER TABLE userdataset DROP COLUMN			datasetstart	;  ALTER TABLE userdataset RENAME COLUMN			datasetstart2 TO			datasetstart	; 

--add back non-null constraints:
ALTER TABLE userRecordOwner ALTER COLUMN recordCreationDate SET not null;
ALTER TABLE userRecordOwner ALTER COLUMN ownerStart SET not null;
ALTER TABLE userPermission ALTER COLUMN permissionStart SET not null;
ALTER TABLE userDatasetItem ALTER COLUMN itemStart SET not null;
ALTER TABLE taxonInterpretation ALTER COLUMN interpretationDate SET not null;
ALTER TABLE revision ALTER COLUMN revisionDate SET not null;
ALTER TABLE plantStatus ALTER COLUMN startDate SET not null;
ALTER TABLE plantCorrelation ALTER COLUMN correlationStart SET not null;
ALTER TABLE partyMember ALTER COLUMN memberStart SET not null;
ALTER TABLE observationSynonym ALTER COLUMN classStartDate SET not null;
ALTER TABLE embargo ALTER COLUMN embargoStart SET not null;
ALTER TABLE embargo ALTER COLUMN embargoStop SET not null;
ALTER TABLE commStatus ALTER COLUMN startDate SET not null;
ALTER TABLE commCorrelation ALTER COLUMN correlationStart SET not null;
