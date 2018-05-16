-- MySQL dump 10.13  Distrib 5.7.17, for macos10.12 (x86_64)
--
-- Host: mysql3.cs.stonybrook.edu    Database: pine
-- ------------------------------------------------------
-- Server version	5.7.20-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `artist_types`
--

DROP TABLE IF EXISTS `artist_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `artist_types` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `artistType` varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `artist_types`
--

LOCK TABLES `artist_types` WRITE;
/*!40000 ALTER TABLE `artist_types` DISABLE KEYS */;
INSERT INTO `artist_types` VALUES (1,'Other'),(2,'Cast'),(3,'Director'),(4,'Writer'),(5,'Producer'),(6,'Creator');
/*!40000 ALTER TABLE `artist_types` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `artists`
--

DROP TABLE IF EXISTS `artists`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `artists` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `firstName` varchar(100) NOT NULL,
  `lastName` varchar(100) DEFAULT NULL,
  `description` mediumtext,
  `birthdate` date DEFAULT NULL,
  `birthplace` varchar(200) DEFAULT NULL,
  `resourcePath` varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `artists`
--

LOCK TABLES `artists` WRITE;
/*!40000 ALTER TABLE `artists` DISABLE KEYS */;
INSERT INTO `artists` VALUES (1,'Jordan Peele',NULL,'Intended to be a puppeteer in college, but dropped out to study comedy, including a stint with Boom Chicago in Amsterdam; while on a cast swap with Second City in Chicago in 2002, met his future comedic partner Keegan-Michael Key. Beforethe premiere of Key& Peele on Comedy Central in 2012,overlapped with Keyfor several seasonsin the cast ofMADtv. Unsuccessfully auditioned to play Barack Obama on Saturday Night Live in 2008 (the role went to Fred Armisen), but wouldearn acclaimfor impersonating the president on Key & Peelein 2012. Was flattered to learn thatObama was a fan of his impression-and of Key\'s portrayal ofLuther, the president\'s anger translator-and in the spring of 2012 they had the opportunity to meet him in Los Angeles. Like Key, has a black father and a white mother, a biracial status that provides a uniquecultural sensitivityand informs much of their comedy.','1979-02-21',NULL,NULL),(2,'Daniel Kaluuya',NULL,NULL,NULL,NULL,NULL),(3,'Chadwick Boseman',NULL,'Was an athlete as a child; involved with Little League baseball but primarily played basketball. Studied acting at the British American Drama Academy in Oxford after graduating from Howard University in Washington, United States. Originally aspired to be a director. Made his TV debut in a 2003 episode of Third Watch. Trained for five months with baseball coaches to prepare for his role as Jackie Robinson in 42 (2013).',NULL,NULL,NULL),(4,'Ryan Coogler',NULL,NULL,NULL,NULL,NULL),(5,'Joe Robert Cole',NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `artists` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `awards`
--

DROP TABLE IF EXISTS `awards`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `awards` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `mediaID` int(11) NOT NULL,
  `awardName` varchar(21000) DEFAULT NULL,
  `awardYear` int(11) DEFAULT NULL COMMENT 'This is only year',
  PRIMARY KEY (`ID`),
  KEY `mediaID_FK_idx` (`mediaID`),
  CONSTRAINT `awards_mediaID_FK` FOREIGN KEY (`mediaID`) REFERENCES `media` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `awards`
--

LOCK TABLES `awards` WRITE;
/*!40000 ALTER TABLE `awards` DISABLE KEYS */;
/*!40000 ALTER TABLE `awards` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `critic_requests`
--

DROP TABLE IF EXISTS `critic_requests`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `critic_requests` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `memberID` int(11) NOT NULL,
  `publicationID` int(11) DEFAULT NULL,
  `description` mediumtext,
  `regDate` date DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `memberID_FK_idx` (`memberID`),
  KEY `publicationID_Fk_idx` (`publicationID`),
  CONSTRAINT `critic_requests_memberID_FK` FOREIGN KEY (`memberID`) REFERENCES `members` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `critic_requests_publicationID_Fk` FOREIGN KEY (`publicationID`) REFERENCES `publications` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `critic_requests`
--

LOCK TABLES `critic_requests` WRITE;
/*!40000 ALTER TABLE `critic_requests` DISABLE KEYS */;
/*!40000 ALTER TABLE `critic_requests` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `filmography`
--

DROP TABLE IF EXISTS `filmography`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `filmography` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `mediaID` int(11) NOT NULL,
  `artistID` int(11) NOT NULL,
  `artistTypeID` int(11) NOT NULL,
  `artistRole` varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `mediaID_FK_idx` (`mediaID`),
  KEY `artistID_FK_idx` (`artistID`),
  KEY `artistTypeID_FK_idx` (`artistTypeID`),
  CONSTRAINT `filmography_artistID_FK` FOREIGN KEY (`artistID`) REFERENCES `artists` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `filmography_artistTypeID_FK` FOREIGN KEY (`artistTypeID`) REFERENCES `artist_types` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `filmography_mediaID_FK` FOREIGN KEY (`mediaID`) REFERENCES `media` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `filmography`
--

LOCK TABLES `filmography` WRITE;
/*!40000 ALTER TABLE `filmography` DISABLE KEYS */;
INSERT INTO `filmography` VALUES (1,2,3,2,'T\'Challa/Black Panther'),(2,2,4,3,''),(3,2,3,4,''),(4,3,1,3,''),(5,3,1,4,''),(6,3,2,2,'Chris Washington');
/*!40000 ALTER TABLE `filmography` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `genre`
--

DROP TABLE IF EXISTS `genre`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `genre` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `genreName` varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `genre`
--

LOCK TABLES `genre` WRITE;
/*!40000 ALTER TABLE `genre` DISABLE KEYS */;
INSERT INTO `genre` VALUES (1,'Other'),(2,'Action'),(3,'Animation'),(4,'Art&Foreign'),(5,'Classic'),(6,'Comedy'),(7,'Documentary'),(8,'Drama'),(9,'Horror'),(10,'Kids&Family'),(11,'Mystery'),(12,'Romance'),(13,'Sci-Fi&Fantasy');
/*!40000 ALTER TABLE `genre` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `media`
--

DROP TABLE IF EXISTS `media`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `media` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `parentID` int(11) DEFAULT NULL,
  `mediaType` int(11) NOT NULL,
  `mediaName` varchar(500) DEFAULT NULL,
  `description` mediumtext,
  `releaseDateTheatre` date DEFAULT NULL,
  `releaseDateDVD` date DEFAULT NULL,
  `mediaRating` int(11) NOT NULL DEFAULT '1',
  `runtime` int(11) DEFAULT NULL,
  `boxOffice` int(11) DEFAULT NULL,
  `mediaFrom` varchar(500) DEFAULT NULL,
  `audienceAvgScore` float DEFAULT NULL,
  `audienceMudemeter` float DEFAULT NULL,
  `criticAvgScore` float DEFAULT NULL,
  `criticMudemeter` float DEFAULT NULL,
  `resourcePath` varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `mediaType_idx` (`mediaType`),
  KEY `mediaRating_FK_idx` (`mediaRating`),
  CONSTRAINT `media_mediaRating_FK` FOREIGN KEY (`mediaRating`) REFERENCES `media_ratings` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `media_mediaType_FK` FOREIGN KEY (`mediaType`) REFERENCES `media_types` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `media`
--

LOCK TABLES `media` WRITE;
/*!40000 ALTER TABLE `media` DISABLE KEYS */;
INSERT INTO `media` VALUES (1,0,1,'Marvel Cinematic Universe','From Iron Man to Captain America to Daredevil, Guardians of the Galaxy, Jessica Jones, and more, the MCU brings some of the most legendary comic heroes -- and some lesser known ones -- to vibrant, colorful, action-packed life.',NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2,1,2,'Black Panther','Black Panther follows T\'Challa who, after the events of Captain America: Civil War, returns home to the isolated, technologically advanced African nation of Wakanda to take his place as King. However, when an old enemy reappears on the radar, T\'Challa\'s mettle as King and Black Panther is tested when he is drawn into a conflict that puts the entire fate of Wakanda and the world at risk.','2018-02-16',NULL,4,135,501105037,'Marvel Studios',NULL,NULL,NULL,NULL,NULL),(3,0,2,'Get Out','Now that Chris and his girlfriend, Rose, have reached the meet-the-parents milestone of dating, she invites him for a weekend getaway upstate with Missy and Dean. At first, Chris reads the family\'s overly accommodating behavior as nervous attempts to deal with their daughter\'s interracial relationship, but as the weekend progresses, a series of increasingly disturbing discoveries lead him to a truth that he could have never imagined.','2017-02-24','2017-05-23',5,104,175428355,'Universal Pictures',NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `media` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `media_genre`
--

DROP TABLE IF EXISTS `media_genre`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `media_genre` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `mediaID` int(11) NOT NULL,
  `genreID` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `mediaID_FK_idx` (`mediaID`),
  KEY `genreID_FK_idx` (`genreID`),
  CONSTRAINT `media_genre_genreID_FK` FOREIGN KEY (`genreID`) REFERENCES `genre` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `media_genre_mediaID_FK` FOREIGN KEY (`mediaID`) REFERENCES `media` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `media_genre`
--

LOCK TABLES `media_genre` WRITE;
/*!40000 ALTER TABLE `media_genre` DISABLE KEYS */;
INSERT INTO `media_genre` VALUES (1,3,6),(2,3,9),(3,3,11),(4,2,2),(5,2,8),(6,2,13);
/*!40000 ALTER TABLE `media_genre` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `media_ratings`
--

DROP TABLE IF EXISTS `media_ratings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `media_ratings` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `mediaRating` varchar(2000) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `media_ratings`
--

LOCK TABLES `media_ratings` WRITE;
/*!40000 ALTER TABLE `media_ratings` DISABLE KEYS */;
INSERT INTO `media_ratings` VALUES (1,'NA'),(2,'G'),(3,'PG'),(4,'PG-13'),(5,'R'),(6,'NC-17');
/*!40000 ALTER TABLE `media_ratings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `media_types`
--

DROP TABLE IF EXISTS `media_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `media_types` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `mediaType` varchar(2000) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `media_types`
--

LOCK TABLES `media_types` WRITE;
/*!40000 ALTER TABLE `media_types` DISABLE KEYS */;
INSERT INTO `media_types` VALUES (1,'Franchise'),(2,'Movie'),(3,'TVSeries'),(4,'Season'),(5,'Episode');
/*!40000 ALTER TABLE `media_types` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `member_publications`
--

DROP TABLE IF EXISTS `member_publications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `member_publications` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `memberID` int(11) NOT NULL,
  `publicationID` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FKmemberID_idx` (`memberID`),
  KEY `FKpublicationID_idx` (`publicationID`),
  CONSTRAINT `member_publication_memberID_FK` FOREIGN KEY (`memberID`) REFERENCES `members` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `member_publication_publicationID_FK` FOREIGN KEY (`publicationID`) REFERENCES `publications` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `member_publications`
--

LOCK TABLES `member_publications` WRITE;
/*!40000 ALTER TABLE `member_publications` DISABLE KEYS */;
/*!40000 ALTER TABLE `member_publications` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `member_types`
--

DROP TABLE IF EXISTS `member_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `member_types` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `memberType` varchar(2000) NOT NULL COMMENT '\n1 = audience\n2 = critic\n3 = admin',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `member_types`
--

LOCK TABLES `member_types` WRITE;
/*!40000 ALTER TABLE `member_types` DISABLE KEYS */;
INSERT INTO `member_types` VALUES (1,'Audience'),(2,'Critic'),(3,'Admin');
/*!40000 ALTER TABLE `member_types` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `members`
--

DROP TABLE IF EXISTS `members`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `members` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `memberTypeID` int(11) NOT NULL DEFAULT '1' COMMENT 'Every user account is audience until they are verified as critic\nSo by keeping default to 1 is okay since\n1 = audience in MemberType',
  `email` varchar(200) DEFAULT NULL,
  `emailValid` tinyint(4) DEFAULT '0' COMMENT '0 = false\n1 = true',
  `firstName` varchar(200) DEFAULT NULL,
  `lastName` varchar(200) DEFAULT NULL,
  `username` varchar(200) DEFAULT NULL,
  `description` mediumtext,
  `password` blob,
  `regDate` date DEFAULT NULL,
  `resourcePath` varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `Members_memberTypeID_FK_idx` (`memberTypeID`),
  CONSTRAINT `members_memberTypeID_FK` FOREIGN KEY (`memberTypeID`) REFERENCES `member_types` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `members`
--

LOCK TABLES `members` WRITE;
/*!40000 ALTER TABLE `members` DISABLE KEYS */;
INSERT INTO `members` VALUES (1,1,'1',0,'a','aa','aaa','asdfsadf',NULL,NULL,NULL),(2,2,'2',0,'b','bb','bbb','asdfsadf',NULL,NULL,NULL),(3,3,'3',0,'c','cc','ccc','asdfsdf',NULL,NULL,NULL);
/*!40000 ALTER TABLE `members` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `publications`
--

DROP TABLE IF EXISTS `publications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `publications` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `publicationName` varchar(2000) NOT NULL,
  `description` mediumtext,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `publications`
--

LOCK TABLES `publications` WRITE;
/*!40000 ALTER TABLE `publications` DISABLE KEYS */;
/*!40000 ALTER TABLE `publications` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reviews`
--

DROP TABLE IF EXISTS `reviews`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `reviews` (
  `ID` bigint(20) NOT NULL,
  `mediaID` int(11) DEFAULT NULL,
  `memberID` int(11) DEFAULT NULL,
  `comment` varchar(20000) DEFAULT NULL,
  `score` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reviews`
--

LOCK TABLES `reviews` WRITE;
/*!40000 ALTER TABLE `reviews` DISABLE KEYS */;
INSERT INTO `reviews` VALUES (1,2,1,'111','1'),(2,2,2,'222','2'),(3,2,3,'333','4');
/*!40000 ALTER TABLE `reviews` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `want_to_see`
--

DROP TABLE IF EXISTS `want_to_see`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `want_to_see` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `mediaID` int(11) NOT NULL,
  `memberID` int(11) NOT NULL,
  `regDate` date DEFAULT NULL,
  `isWantToSee` tinyint(4) NOT NULL DEFAULT '1' COMMENT '0 = NOT INTERESTED\n1 = WANT TO SEE',
  PRIMARY KEY (`ID`),
  KEY `mediaID_FK_idx` (`mediaID`),
  KEY `memberID_FK_idx` (`memberID`),
  CONSTRAINT `WantToSee_mediaID_FK` FOREIGN KEY (`mediaID`) REFERENCES `media` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `WantToSee_memberID_FK` FOREIGN KEY (`memberID`) REFERENCES `members` (`ID`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `want_to_see`
--

LOCK TABLES `want_to_see` WRITE;
/*!40000 ALTER TABLE `want_to_see` DISABLE KEYS */;
/*!40000 ALTER TABLE `want_to_see` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'pine'
--

--
-- Dumping routines for database 'pine'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-04-07 19:11:52
