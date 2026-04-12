-- MySQL dump 10.13  Distrib 8.0.19, for Win64 (x86_64)
--
-- Host: localhost    Database: pictureorganizer
-- ------------------------------------------------------
-- Server version	8.0.19

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `folder`
--

DROP TABLE IF EXISTS `folder`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `folder` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `parent_id` int unsigned DEFAULT NULL,
  `drive_id` int unsigned NOT NULL,
  `path` varchar(512) COLLATE utf8mb4_0900_as_cs NOT NULL,
  `name` varchar(255) COLLATE utf8mb4_0900_as_cs DEFAULT NULL,
  `credate` datetime DEFAULT NULL,
  `upddate` datetime DEFAULT NULL,
  `creator` varchar(255) COLLATE utf8mb4_0900_as_cs DEFAULT NULL,
  `updater` varchar(255) COLLATE utf8mb4_0900_as_cs DEFAULT NULL,
  `actfold` varchar(255) COLLATE utf8mb4_0900_as_cs DEFAULT NULL,
  `media_directory_id` int unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `path_UNIQUE` (`drive_id`,`path`),
  KEY `drive_id_idx` (`drive_id`),
  KEY `folder_folder_idx` (`parent_id`),
  KEY `folder_path_idx` (`path`),
  KEY `folder_fk_media_directory_idx` (`media_directory_id`),
  CONSTRAINT `drive_id` FOREIGN KEY (`drive_id`) REFERENCES `drive` (`id`),
  CONSTRAINT `folder_fk_media_directory` FOREIGN KEY (`media_directory_id`) REFERENCES `media_directory` (`id`),
  CONSTRAINT `folder_folder` FOREIGN KEY (`parent_id`) REFERENCES `folder` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26169 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_as_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `image`
--

DROP TABLE IF EXISTS `image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `image` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `odid` varchar(32) COLLATE utf8mb4_0900_as_cs NOT NULL,
  `parent_id` int unsigned DEFAULT NULL,
  `camera_make` varchar(255) COLLATE utf8mb4_0900_as_cs DEFAULT NULL,
  `camera_model` varchar(255) COLLATE utf8mb4_0900_as_cs DEFAULT NULL,
  `orientation` int DEFAULT NULL,
  `date_taken` varchar(32) COLLATE utf8mb4_0900_as_cs DEFAULT NULL,
  `date_corrected` varchar(32) COLLATE utf8mb4_0900_as_cs DEFAULT NULL,
  `original_filename` varchar(45) COLLATE utf8mb4_0900_as_cs DEFAULT NULL,
  `original_file_hash` varchar(32) COLLATE utf8mb4_0900_as_cs DEFAULT NULL,
  `bestimate_filename` varchar(45) COLLATE utf8mb4_0900_as_cs DEFAULT NULL,
  `bestimate_file_hash` varchar(32) COLLATE utf8mb4_0900_as_cs DEFAULT NULL,
  `type` varchar(10) COLLATE utf8mb4_0900_as_cs DEFAULT NULL,
  `duration` int DEFAULT '0',
  `latitude` varchar(45) COLLATE utf8mb4_0900_as_cs DEFAULT NULL,
  `longitude` varchar(45) COLLATE utf8mb4_0900_as_cs DEFAULT NULL,
  `altitude` varchar(45) COLLATE utf8mb4_0900_as_cs DEFAULT NULL,
  `credate` datetime DEFAULT NULL,
  `upddate` datetime DEFAULT NULL,
  `creator` varchar(255) COLLATE utf8mb4_0900_as_cs DEFAULT NULL,
  `updater` varchar(255) COLLATE utf8mb4_0900_as_cs DEFAULT NULL,
  `valid` bit(1) NOT NULL DEFAULT b'0',
  `title` varchar(511) COLLATE utf8mb4_0900_as_cs DEFAULT NULL,
  `keyword` varchar(255) COLLATE utf8mb4_0900_as_cs DEFAULT NULL,
  `rating` int DEFAULT NULL,
  `orig_exif` blob,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `ODID_UNIQUE` (`odid`,`type`,`valid`),
  KEY `ParentImage_idx` (`parent_id`),
  KEY `Type_idx` (`type`),
  CONSTRAINT `parent_image` FOREIGN KEY (`parent_id`) REFERENCES `image` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=253577 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_as_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `media_directory`
--

DROP TABLE IF EXISTS `media_directory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `media_directory` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `from_date` date DEFAULT NULL,
  `to_date` date DEFAULT NULL,
  `title` varchar(231) COLLATE utf8mb4_0900_as_cs DEFAULT NULL,
  `folder_name` varchar(255) COLLATE utf8mb4_0900_as_cs DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=898 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_as_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `media_file`
--

DROP TABLE IF EXISTS `media_file`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `media_file` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `main_id` int unsigned DEFAULT NULL,
  `image_id` int unsigned DEFAULT NULL COMMENT 'temp for migration',
  `shotnumber` int unsigned DEFAULT NULL,
  `original_filename` varchar(256) COLLATE utf8mb4_0900_as_cs DEFAULT NULL,
  `title_in_filename` varchar(255) COLLATE utf8mb4_0900_as_cs DEFAULT NULL,
  `file_type` varchar(5) COLLATE utf8mb4_0900_as_cs DEFAULT NULL,
  `standalone` bit(1) DEFAULT NULL COMMENT 'main_id shouldn''t be null if >0',
  `original_version_id` int unsigned,
  `media_directory_id` int unsigned DEFAULT NULL,
  `credate` datetime DEFAULT NULL,
  `creator` varchar(255) COLLATE utf8mb4_0900_as_cs DEFAULT NULL,
  `upddate` datetime DEFAULT NULL,
  `updater` varchar(255) COLLATE utf8mb4_0900_as_cs DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `media_file_fk_image_idx` (`image_id`),
  KEY `media_file_fk_directory_idx` (`media_directory_id`),
  KEY `media_file_fk_main_idx` (`main_id`),
  KEY `media_file_fk_original_version_idx` (`original_version_id`),
  CONSTRAINT `media_file_fk_directory` FOREIGN KEY (`media_directory_id`) REFERENCES `media_directory` (`id`),
  CONSTRAINT `media_file_fk_image` FOREIGN KEY (`image_id`) REFERENCES `image` (`id`),
  CONSTRAINT `media_file_fk_main` FOREIGN KEY (`main_id`) REFERENCES `media_file` (`id`),
  CONSTRAINT `media_file_fk_media_directory` FOREIGN KEY (`media_directory_id`) REFERENCES `media_directory` (`id`),
  CONSTRAINT `media_file_fk_original_version` FOREIGN KEY (`original_version_id`) REFERENCES `media_file_version` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=266252 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_as_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `media_file_instance`
--

DROP TABLE IF EXISTS `media_file_instance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `media_file_instance` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `folder_id` int unsigned DEFAULT NULL,
  `media_file_old_id` int unsigned DEFAULT NULL,
  `filename` varchar(255) COLLATE utf8mb4_0900_as_cs NOT NULL,
  `name_version` int DEFAULT '0',
  `XMPattached` bit(1) DEFAULT NULL,
  `date_mod` datetime DEFAULT NULL,
  `media_file_version_id` int unsigned DEFAULT NULL,
  `media_file_id` int unsigned DEFAULT NULL,
  `file_type` varchar(5) COLLATE utf8mb4_0900_as_cs DEFAULT NULL,
  `credate` datetime DEFAULT NULL,
  `creator` varchar(255) COLLATE utf8mb4_0900_as_cs DEFAULT NULL,
  `upddate` datetime DEFAULT NULL,
  `updater` varchar(255) COLLATE utf8mb4_0900_as_cs DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `fk_media_file_idx` (`media_file_old_id`),
  KEY `fk_folder_idx` (`folder_id`),
  KEY `media_file_instance_fk_media_file_idx` (`media_file_id`),
  KEY `media_file_instance_fk_media_file_version_idx` (`media_file_version_id`),
  CONSTRAINT `fk_folder` FOREIGN KEY (`folder_id`) REFERENCES `folder` (`id`),
  CONSTRAINT `fk_media_file_old` FOREIGN KEY (`media_file_old_id`) REFERENCES `media_file_old` (`id`),
  CONSTRAINT `media_file_instance_fk_media_file` FOREIGN KEY (`media_file_id`) REFERENCES `media_file` (`id`),
  CONSTRAINT `media_file_instance_fk_media_file_version` FOREIGN KEY (`media_file_version_id`) REFERENCES `media_file_version` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2342187 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_as_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `media_file_version`
--

DROP TABLE IF EXISTS `media_file_version`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `media_file_version` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `filehash` varchar(32) COLLATE utf8mb4_0900_as_cs DEFAULT NULL,
  `file_type` varchar(5) COLLATE utf8mb4_0900_as_cs DEFAULT NULL,
  `commit` varchar(45) COLLATE utf8mb4_0900_as_cs DEFAULT NULL,
  `parent_id` int unsigned DEFAULT NULL,
  `size` bigint unsigned DEFAULT NULL,
  `media_file_id` int unsigned DEFAULT NULL,
  `standalone` bit(1) DEFAULT NULL,
  `date_stored` varchar(32) COLLATE utf8mb4_0900_as_cs DEFAULT NULL,
  `date_stored_local` datetime DEFAULT NULL,
  `date_stored_utc` datetime DEFAULT NULL,
  `date_stored_tz` varchar(45) COLLATE utf8mb4_0900_as_cs DEFAULT NULL,
  `invalid` tinyint unsigned DEFAULT NULL,
  `exifbackup` bit(1) DEFAULT NULL,
  `credate` datetime DEFAULT NULL,
  `creator` varchar(255) COLLATE utf8mb4_0900_as_cs DEFAULT NULL,
  `upddate` datetime DEFAULT NULL,
  `updater` varchar(255) COLLATE utf8mb4_0900_as_cs DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `filehash_unique` (`filehash`,`file_type`) /*!80000 INVISIBLE */,
  KEY `filehash_idx` (`filehash`,`file_type`),
  KEY `parent_fk_idx` (`parent_id`),
  KEY `media_file_version_fk_media_file_idx` (`media_file_id`),
  CONSTRAINT `media_file_version_fk_media_file` FOREIGN KEY (`media_file_id`) REFERENCES `media_file` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=375431 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_as_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `media_image`
--

DROP TABLE IF EXISTS `media_image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `media_image` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `media_file_version_id` int unsigned,
  `file_type` varchar(5) COLLATE utf8mb4_0900_as_cs DEFAULT NULL,
  `image_id` int unsigned DEFAULT NULL,
  `exifbackup` bit(1) DEFAULT NULL,
  `duration` int DEFAULT '0',
  `latitude` varchar(45) COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT 'empty',
  `longitude` varchar(45) COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT 'empty',
  `altitude` varchar(45) COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT 'empty',
  `orientation` int DEFAULT NULL,
  `keyword` varchar(255) COLLATE utf8mb4_0900_as_cs DEFAULT NULL,
  `rating` int DEFAULT NULL,
  `title` varchar(511) COLLATE utf8mb4_0900_as_cs DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `media_image_fk_media_file_version_idx` (`media_file_version_id`),
  KEY `media_image_fk_image_idx` (`image_id`),
  CONSTRAINT `media_image_fk_image` FOREIGN KEY (`image_id`) REFERENCES `image` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=393216 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_as_cs;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-12-17  8:22:23
