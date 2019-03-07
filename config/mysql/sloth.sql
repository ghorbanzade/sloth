-- MySQL dump 10.13  Distrib 5.5.49, for Linux (x86_64)
--
-- Host: localhost    Database: sloth_main
-- ------------------------------------------------------
-- Server version	5.5.49-cll

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
-- Table structure for table `activities`
--

DROP TABLE IF EXISTS `activities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `activities` (
  `activity_id` int(6) NOT NULL AUTO_INCREMENT,
  `user_id` int(6) NOT NULL,
  `activity_name` varchar(16) NOT NULL,
  `recognition_accuracy` int(3) NOT NULL,
  `recognition_date` datetime NOT NULL,
  PRIMARY KEY (`activity_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `activities_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=59 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activities`
--

LOCK TABLES `activities` WRITE;
/*!40000 ALTER TABLE `activities` DISABLE KEYS */;
INSERT INTO `activities` (`activity_id`, `user_id`, `activity_name`, `recognition_accuracy`, `recognition_date`) VALUES (19,1,'Eating',1,'2016-05-04 02:48:56'),(20,1,'Reading',1,'2016-05-04 02:50:18'),(21,1,'Playing Piano',96,'2016-05-04 02:54:46'),(22,1,'Biking',95,'2016-05-04 02:58:54'),(23,1,'Standing Still',83,'2016-05-04 03:08:55'),(24,1,'Talking on Phone',93,'2016-05-04 03:15:59'),(25,1,'Walking',96,'2016-05-04 03:16:38'),(26,1,'Sitting',91,'2016-05-04 03:17:17'),(27,1,'Sleeping',88,'2016-05-04 03:20:07'),(30,1,'walking',98,'2016-05-05 14:45:08'),(31,1,'typing',99,'2016-05-05 14:46:50'),(32,1,'driving',96,'2016-05-05 14:47:25'),(33,1,'running',97,'2016-05-05 14:47:52'),(34,1,'typing',97,'2016-05-05 14:48:40'),(35,1,'waving',97,'2016-05-05 14:49:29'),(36,1,'walking',99,'2016-05-05 18:19:27'),(37,1,'typing',99,'2016-05-05 18:20:23'),(38,1,'walking',99,'2016-05-05 18:39:52'),(39,1,'typing',98,'2016-05-05 18:40:30'),(40,1,'jogging',98,'2016-05-05 18:40:58'),(41,1,'walking',97,'2016-05-05 19:01:46'),(42,1,'driving',95,'2016-05-05 19:08:52'),(43,1,'typing',95,'2016-05-11 15:10:04'),(44,1,'walking',98,'2016-05-11 15:47:54'),(45,1,'walking',99,'2016-05-11 15:47:39'),(46,1,'walking',98,'2016-05-11 15:48:09'),(47,1,'walking',98,'2016-05-11 15:49:34'),(48,1,'jogging',96,'2016-05-11 15:49:49'),(49,1,'driving',95,'2016-05-11 15:50:04'),(50,1,'waving',97,'2016-05-11 15:50:56'),(51,1,'driving',97,'2016-05-11 15:51:11'),(52,1,'eating',96,'2016-05-11 15:51:26'),(53,1,'walking',98,'2016-05-11 18:44:25'),(54,1,'walking',98,'2016-05-11 18:44:40'),(55,1,'typing',97,'2016-05-11 18:44:55'),(56,1,'jogging',96,'2016-05-11 18:45:56'),(57,1,'typing',96,'2016-05-11 18:46:11'),(58,1,'typing',96,'2016-05-11 18:46:26');
/*!40000 ALTER TABLE `activities` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `metrics`
--

DROP TABLE IF EXISTS `metrics`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `metrics` (
  `metric_id` int(11) NOT NULL AUTO_INCREMENT,
  `sensor_id` int(11) NOT NULL,
  `report_date` datetime NOT NULL,
  `status` int(2) NOT NULL,
  PRIMARY KEY (`metric_id`),
  KEY `user_id` (`sensor_id`),
  CONSTRAINT `metrics_ibfk_1` FOREIGN KEY (`sensor_id`) REFERENCES `sensors` (`sensor_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `metrics`
--

LOCK TABLES `metrics` WRITE;
/*!40000 ALTER TABLE `metrics` DISABLE KEYS */;
INSERT INTO `metrics` (`metric_id`, `sensor_id`, `report_date`, `status`) VALUES (1,1,'2016-05-01 04:30:10',1),(2,2,'2016-05-01 04:30:26',1),(3,1,'2016-05-01 04:30:41',1);
/*!40000 ALTER TABLE `metrics` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sensors`
--

DROP TABLE IF EXISTS `sensors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sensors` (
  `sensor_id` int(11) NOT NULL AUTO_INCREMENT,
  `sensor_key` varchar(8) NOT NULL,
  `sensor_name` varchar(32) NOT NULL,
  `user_id` int(6) NOT NULL,
  PRIMARY KEY (`sensor_id`),
  UNIQUE KEY `sensor_key` (`sensor_key`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `sensors_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sensors`
--

LOCK TABLES `sensors` WRITE;
/*!40000 ALTER TABLE `sensors` DISABLE KEYS */;
INSERT INTO `sensors` (`sensor_id`, `sensor_key`, `sensor_name`, `user_id`) VALUES (1,'ktmcgndr','right arm',1),(2,'ssmjxkxc','left arm',1);
/*!40000 ALTER TABLE `sensors` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `user_id` int(6) NOT NULL AUTO_INCREMENT,
  `firstname` varchar(16) NOT NULL,
  `lastname` varchar(16) NOT NULL,
  `username` varchar(16) NOT NULL,
  `password` varchar(32) NOT NULL,
  `avatar` varchar(16) DEFAULT NULL,
  `email_address` varchar(32) NOT NULL,
  `registration_date` datetime NOT NULL,
  `private_token` varchar(32) NOT NULL,
  `last_login` datetime DEFAULT NULL,
  `failed_logins` int(1) NOT NULL DEFAULT '0',
  `password_reset_time` datetime DEFAULT NULL,
  `temporary_password` varchar(16) DEFAULT NULL,
  `suspend` int(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `private_token` (`private_token`,`email_address`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` (`user_id`, `firstname`, `lastname`, `username`, `password`, `avatar`, `email_address`, `registration_date`, `private_token`, `last_login`, `failed_logins`, `password_reset_time`, `temporary_password`, `suspend`) VALUES (1,'Pejman','Ghorbanzade','ghorbanzade','e52178a61bc930894abf1881b195bfb6','','pejman@ghorbanzade.com','2016-05-01 04:21:00','NT20p6xPrdDLeBYs',NULL,0,NULL,'',0);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'sloth_main'
--

--
-- Dumping routines for database 'sloth_main'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-06-25  3:37:35
