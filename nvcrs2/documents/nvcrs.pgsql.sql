-- MySQL dump 10.4
--
-- Host: localhost    Database: test
-- ------------------------------------------------------
-- Server version	5.0.0-alpha-max-debug
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE=NO_AUTO_VALUE_ON_ZERO */;

--
-- Table structure for table `composition`
--

DROP TABLE  composition;
CREATE TABLE composition (
  
  COMPOSITION_ID serial8 PRIMARY KEY ,
  VBPlant_AccessionCode varchar(100) NOT NULL default '0',
  constancy decimal(10,0) default '0',
  compositionType varchar(20) NOT NULL default 'dominant',
  notes text,
  stratum varchar(50) default '0',
  TYPE_ID bigint NOT NULL default '0',
  VBMethod_AccessionCode varchar(100) default '0'

);

--
-- Dumping data for table `composition`
--




--
-- Table structure for table `correlation`
--

DROP TABLE  correlation;
CREATE TABLE correlation (

  CORRELATION_ID serial8 PRIMARY KEY ,
  VB_AccessionCode varchar(100) default '',
  convergence varchar(10) NOT NULL default '>',
  notes text,
  TYPE_ID bigint NOT NULL default '0',
  CORRELATEDTYPE_ID bigint NOT NULL default '0'
  
);

--
-- Dumping data for table `correlation`
--




--
-- Table structure for table `distribution`
--

DROP TABLE  distribution;
CREATE TABLE distribution (

  DISTRIBUTION_ID serial8 PRIMARY KEY ,
  placeName varchar(100) default '',
  VB_AccessionCode varchar(100) default '',
  placeType varchar(50) default '',
  placeNotes text,
  placeDistribution varchar(100) default '',
  placePattern varchar(100) default '',
  confidence varchar(20) default '',
  TYPE_ID bigint NOT NULL default '0'

);

--
-- Dumping data for table `distribution`
--




--
-- Table structure for table `event`
--

DROP TABLE  event;
CREATE TABLE event (

  EVENT_ID serial8 PRIMARY KEY ,
  PROPOSAL_ID bigint NOT NULL default '0',
  USR_ID bigint NOT NULL default '0',
  ROLE_ID bigint NOT NULL default '0',
  ACTION_ID varchar(30) NOT NULL default '0',
  SUBJECTUSR_ID bigint default '0',
  eventDate date ,
  privateComments varchar(255) default '',
  publicComments text,
  supporting_doc_URL varchar(100) default '',
  reviewText text,
  summary varchar(10) default ''

);

--
-- Dumping data for table `event`
--



--
-- Table structure for table `lookup_eventtype`
--

DROP TABLE  lookup_eventtype;
CREATE TABLE lookup_eventtype (

  TYPE_ID serial8 PRIMARY KEY ,
  type varchar(30) NOT NULL default 'submit'
);

--
-- Dumping data for table `lookup_eventtype`
--



--
-- Table structure for table `lookup_roletype`
--

DROP TABLE  lookup_roletype;
CREATE TABLE lookup_roletype (

  TYPE_ID serial8 PRIMARY KEY ,
  Type varchar(20) NOT NULL default 'Author'

);

--
-- Dumping data for table `lookup_roletype`
--




--
-- Table structure for table `plot`
--

DROP TABLE  plot;
CREATE TABLE plot (

  PLOT_ID serial8 PRIMARY KEY ,
  VB_AccessionCode varchar(100) default '',
  plotUse varchar(20) default '',
  TYPE_ID bigint NOT NULL default '0'
  
);

--
-- Dumping data for table `plot`
--




--
-- Table structure for table `project`
--

DROP TABLE  project;
CREATE TABLE project (

  PROJECT_ID serial8 PRIMARY KEY ,
  projectName varchar(100) NOT NULL default 'unnamed',
  projectDescription text,
  supporting_doc_URL varchar(100) default ''
 
);

--
-- Dumping data for table `project`
--




--
-- Table structure for table `proposal`
--

DROP TABLE  proposal;
CREATE TABLE proposal (

  PROPOSAL_ID serial8 PRIMARY KEY ,
  summary text,
  previousProposal_ID bigint default '0',
  supporting_doc_URL varchar(100) default '',
  current_status varchar(30) NOT NULL default 'unsubmitted',
  PROJECT_ID bigint default '0'
 
);

--
-- Dumping data for table `proposal`
--




--
-- Table structure for table `type`
--

DROP TABLE  type;
CREATE TABLE type (

  TYPE_ID serial8 PRIMARY KEY ,
  actionType varchar(20) NOT NULL default 'add',
  userTypeCode varchar(20) default '0',
  VB_AccessionCode varchar(100) default '',
  sausageAccessionCode varchar(100) NOT NULL default '0',
  level varchar(20) NOT NULL default 'Allience',
  primaryName varchar(100) NOT NULL default 'unnamed',
  confidence varchar(20) default '',
  typeSummary text,
  classifyComments text,
  GRank varchar(50) default '',
  GRankDate date ,
  GRankReasons text,
  wetlandIndicator varchar(20) default '',
  environmentSummary text,
  vegtationSummary text,
  succession varchar(50) default '',
  rational varchar(50) default '',
  additionalNotes text,
  PROPOSAL_ID bigint NOT NULL default '0'
  
);

--
-- Dumping data for table `type`
--



--
-- Table structure for table `typename`
--

DROP TABLE  typename;
CREATE TABLE typename (

  TYPENAME_ID serial8 PRIMARY KEY ,
  typeName varchar(100) NOT NULL default 'unnamed',
  typeNameSystem varchar(100) NOT NULL default 'unnamed',
  typeNameStatus varchar(20) NOT NULL default 'accepted',
  startDate date ,
  stopDate date ,
  TYPE_ID bigint NOT NULL default '0'
);

--
-- Dumping data for table `typename`
--




--
-- Table structure for table `typereference`
--

DROP TABLE  typereference;
CREATE TABLE typereference (

  REFERENCE_ID serial8 PRIMARY KEY ,
  VB_AccessionCode varchar(100) default '',
  detailText varchar(255) default '',
  authorReference varchar(10) default '0',
  scope varchar(100) default '',
  TYPE_ID bigint NOT NULL default '0'
  
);

--
-- Dumping data for table `typereference`
--



--
-- Table structure for table `usr`
--

DROP TABLE  usr;
CREATE TABLE usr (
  USR_ID serial8 PRIMARY KEY ,
  login_name varchar(15) NOT NULL default '',
  password varchar(20) NOT NULL default '',
  permission char(2) default '0',
  role varchar(20) default '',
  last_name varchar(20) default '',
  first_name varchar(20) default '',
  middle_initial char(2) default '',
  street varchar(100) default '',
  city varchar(30) default '',
  state varchar(20) default '',
  zip varchar(20) default '',
  phone varchar(20) default '',
  email varchar(100) default ''
  
);

--
-- Dumping data for table `usr`
--


