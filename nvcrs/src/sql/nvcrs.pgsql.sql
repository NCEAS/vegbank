-- MySQL dump 10.4
--
-- Host: localhost    Database: test
-- ------------------------------------------------------

--
-- Table structure for table `composition`
--

CREATE TABLE "composition" (
  "COMPOSITION_ID" bigint NOT NULL,
  "VBPlant_AccessionCode" varchar(100) NOT NULL default '0',
  "constancy" decimal(10,0) default '0',
  "compositionType" varchar(20) NOT NULL default 'dominant',
  "notes" text,
  "stratum" varchar(50) default '0',
  "TYPE_ID" bigint NOT NULL default '0',
  "VBMethod_AccessionCode" varchar(100) default '0',
  PRIMARY KEY  ("COMPOSITION_ID")
);

--
-- Dumping data for table `composition`
--


--
-- Table structure for table `correlation`
--

CREATE TABLE "correlation" (
  "CORRELATION_ID" bigint NOT NULL default '0',
  "VB_AccessionCode" varchar(100) default '',
  "convergence" varchar(10) NOT NULL default '>',
  "notes" text,
  "TYPE_ID" bigint NOT NULL default '0',
  "CORRELATEDTYPE_ID" bigint NOT NULL default '0',
  PRIMARY KEY  ("CORRELATION_ID")
);

--
-- Dumping data for table `correlation`
--


--
-- Table structure for table `distribution`
--

CREATE TABLE "distribution" (
  "DISTRIBUTION_ID" bigint NOT NULL,
  "placeName" varchar(100) default '',
  "VB_AccessionCode" varchar(100) default '',
  "placeType" varchar(50) default '',
  "placeNotes" text,
  "placeDistribution" varchar(100) default '',
  "placePattern" varchar(100) default '',
  "confidence" varchar(20) default '',
  "TYPE_ID" bigint NOT NULL default '0',
  PRIMARY KEY  ("DISTRIBUTION_ID")
);

--
-- Dumping data for table `distribution`
--


--
-- Table structure for table `event`
--

CREATE TABLE "event" (
  "EVENT_ID" bigint NOT NULL,
  "PROPOSAL_ID" bigint NOT NULL default '0',
  "USR_ID" bigint NOT NULL default '0',
  "ROLE_ID" bigint NOT NULL default '0',
  "ACTION_ID" bigint NOT NULL default '0',
  "SUBJECTUSR_ID" bigint default '0',
  "eventDate" date default now(),
  "privateComments" varchar(255) default '',
  "publicComments" text,
  "supporting_doc_URL" varchar(100) default '',
  "reviewText" text,
  "summary" varchar(10) default '',
  PRIMARY KEY  ("EVENT_ID")
);

--
-- Dumping data for table `event`
--


INSERT INTO event VALUES (139,137,7,0,0,0,'2004-03-19','','','','','');
INSERT INTO event VALUES (138,136,7,0,0,0,'2004-03-19','','','','','');

--
-- Table structure for table `lookup_eventtype`
--

CREATE TABLE "lookup_eventtype" (
  "TYPE_ID" bigint NOT NULL,
  "type" varchar(30) NOT NULL default 'submit',
  PRIMARY KEY  ("TYPE_ID")
);

--
-- Dumping data for table `lookup_eventtype`
--


--
-- Table structure for table `lookup_roletype`
--

CREATE TABLE "lookup_roletype" (
  "TYPE_ID" bigint NOT NULL,
  "Type" varchar(20) NOT NULL default 'Author',
  PRIMARY KEY  ("TYPE_ID")
);

--
-- Dumping data for table `lookup_roletype`
--


--
-- Table structure for table `plot`
--

CREATE TABLE "plot" (
  "PLOT_ID" bigint NOT NULL,
  "VB_AccessionCode" varchar(100) default '',
  "plotUse" varchar(20) default '',
  "TYPE_ID" bigint NOT NULL default '0',
  PRIMARY KEY  ("PLOT_ID")
);

--
-- Dumping data for table `plot`
--


--
-- Table structure for table `project`
--

CREATE TABLE "project" (
  "PROJECT_ID" bigint NOT NULL,
  "projectName" varchar(100) NOT NULL default 'unnamed',
  "projectDescription" text,
  "supporting_doc_URL" varchar(100) default '',
  PRIMARY KEY  ("PROJECT_ID")
);

--
-- Dumping data for table `project`
--


INSERT INTO project VALUES (25,'asdas','asd','asda');

--
-- Table structure for table `proposal`
--

CREATE TABLE "proposal" (
  "PROPOSAL_ID" bigint NOT NULL,
  "summary" text,
  "previousProposal_ID" bigint default '0',
  "supporting_doc_URL" varchar(100) default '',
  "current_status" varchar(30) NOT NULL default 'unsubmitted',
  "PROJECT_ID" bigint default '0',
  PRIMARY KEY  ("PROPOSAL_ID")
);

--
-- Dumping data for table `proposal`
--


INSERT INTO proposal VALUES (136,'ytyu',0,' ghj','unsubmitted',0);
INSERT INTO proposal VALUES (137,'jugjg',0,' ghj','unsubmitted',0);

--
-- Table structure for table `type`
--

CREATE TABLE "type" (
  "TYPE_ID" bigint NOT NULL,
  "actionType" varchar(20) NOT NULL default 'add',
  "userTypeCode" varchar(20) default '0',
  "VB_AccessionCode" varchar(100) default '',
  "sausageAccessionCode" varchar(100) NOT NULL default '0',
  "level" varchar(20) NOT NULL default 'Allience',
  "primaryName" varchar(100) NOT NULL default 'unnamed',
  "confidence" varchar(20) default '',
  "typeSummary" text,
  "classifyComments" text,
  "GRank" varchar(50) default '',
  "GRankDate" date default now(),
  "GRankReasons" text,
  "wetlandIndicator" varchar(20) default '',
  "environmentSummary" text,
  "vegtationSummary" text,
  "succession" varchar(50) default '',
  "rational" varchar(50) default '',
  "additionalNotes" text,
  "PROPOSAL_ID" bigint NOT NULL default '0',
  PRIMARY KEY  ("TYPE_ID")
);

--
-- Dumping data for table `type`
--


--
-- Table structure for table `typename`
--

CREATE TABLE "typename" (
  "TYPENAME_ID" bigint NOT NULL,
  "typeName" varchar(100) NOT NULL default 'unnamed',
  "typeNameSystem" varchar(100) NOT NULL default 'unnamed',
  "typeNameStatus" varchar(20) NOT NULL default 'accepted',
  "startDate" date default NULL,
  "stopDate" date default NULL,
  "TYPE_ID" bigint NOT NULL default '0',
  PRIMARY KEY  ("TYPENAME_ID")
);

--
-- Dumping data for table `typename`
--


--
-- Table structure for table `typereference`
--

CREATE TABLE "typereference" (
  "REFERENCE_ID" bigint NOT NULL,
  "VB_AccessionCode" varchar(100) default '',
  "detailText" varchar(255) default '',
  "authorReference" varchar(10) default '0',
  "scope" varchar(100) default '',
  "TYPE_ID" bigint NOT NULL default '0',
  PRIMARY KEY  ("REFERENCE_ID")
);

--
-- Dumping data for table `typereference`
--


--
-- Table structure for table `usr`
--

CREATE TABLE "usr" (
  "USR_ID" bigint NOT NULL,
  "login_name" varchar(15) NOT NULL default '',
  "password" varchar(20) NOT NULL default '',
  "permission" char(2) default '0',
  "role" varchar(20) default '',
  "last_name" varchar(20) default '',
  "first_name" varchar(20) default '',
  "middle_initial" char(2) default '',
  "street" varchar(100) default '',
  "city" varchar(30) default '',
  "state" varchar(20) default '',
  "zip" varchar(20) default '',
  "phone" varchar(20) default '',
  "email" varchar(100) default '',
  PRIMARY KEY  ("USR_ID")
);

--
-- Dumping data for table `usr`
--


INSERT INTO usr VALUES (1,'xianhua','lxh72325','0','Author','Liu','Xianhua','c','106Q Shadowood Dr','Chapel Hill','NC','27514','919-929-5931','liuxianhua@unc.edu');
INSERT INTO usr VALUES (2,'nico','nico','0','Author','nico','nico','','trwert','ertwerte','wertwet','wertwe','ertwe','ertwer');
INSERT INTO usr VALUES (3,'robert','robert','0','','','robert','','','','','','','robert@yahoo.com');
INSERT INTO usr VALUES (4,'test','test','0','Author','test','test','t','test','test','test','test','test','test');
INSERT INTO usr VALUES (5,'anderson','pwd','0','Author','Anderson','Mark','','','','','','','anderson@nceas.ucsb.edu');
INSERT INTO usr VALUES (6,'lajacobs','lajsausage','0','Author','Jacobs','Lee Anne','','CB #3280 Coker Hall','Chapel Hill','NC','27510','919-962-6934','lajacobs@unc.edu');
INSERT INTO usr VALUES (7,'au','au','0','','','au','','','','','','','au@yahoo.com');
INSERT INTO usr VALUES (8,'pv','pv','1','','','pv','','','','','','','pv@yahoo.com');
INSERT INTO usr VALUES (9,'mg','mg','2','','','mg','','','','','','','mg@yahoo.com');
INSERT INTO usr VALUES (10,'au_pv','au_pv','3','','','au_pv','','','','','','','au_pv@yahoo.com');
INSERT INTO usr VALUES (11,'au_mg','au_mg','4','','','au_mg','','','','','','','au_mg@yahoo.com');
INSERT INTO usr VALUES (12,'pv_mg','pv_mg','5','','','pv_mg','','','','','','','pv_mg@yahoo.com');
INSERT INTO usr VALUES (13,'au_pv_mg','au_pv_mg','6','','','au_pv_mg','','','','','','','au_pv_mg@yahoo.com');
INSERT INTO usr VALUES (14,'aaa','aaa','0','Author','aaaa','aaaa','','','','','','','');
INSERT INTO usr VALUES (15,'qqqq','qqqq','0','Author','qqqq','qqqq','','','','','','','');
INSERT INTO usr VALUES (16,'sss','sss','0','Author','sss','sss','ss','','','','','','');
INSERT INTO usr VALUES (17,'123','123','0','Author','123','123','','','','','','','123');

