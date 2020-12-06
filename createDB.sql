CREATE DATABASE  IF NOT EXISTS `pictureorganizer` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */;
USE `pictureorganizer`;
-- MySQL dump 10.13  Distrib 8.0.15, for Win64 (x86_64)
--
-- Host: localhost    Database: picture
-- ------------------------------------------------------
-- Server version	8.0.15



--
-- Table structure for table `drive`
--

 SET character_set_client = utf8mb4 ;
CREATE TABLE `drive` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `volumeSN` varchar(20) DEFAULT NULL,
  `description` varchar(45) DEFAULT NULL,
  `backup` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `volumeSN_UNIQUE` (`volumeSN`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Table structure for table `folder`
--

 CREATE TABLE `folder` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `parent_id` int(11) unsigned DEFAULT NULL,
  `drive_id` int(11) unsigned NOT NULL,
  `path` varchar(512) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `credate` datetime DEFAULT NULL,
  `upddate` datetime DEFAULT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `updater` varchar(255) DEFAULT NULL,
  `actfold` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `path_UNIQUE` (`drive_id`,`path`),
  KEY `drive_id_idx` (`drive_id`),
  KEY `folder_folder_idx` (`parent_id`),
  CONSTRAINT `drive_id` FOREIGN KEY (`drive_id`) REFERENCES `drive` (`id`),
  CONSTRAINT `folder_folder` FOREIGN KEY (`parent_id`) REFERENCES `folder` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7248 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Table structure for table `image`
--

CREATE TABLE `image` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `odid` varchar(32) NOT NULL,
  `parent_id` int(11) unsigned DEFAULT NULL,
  `camera_make` VARCHAR(45) NULL DEFAULT NULL,
  `camera_model` VARCHAR(45) NULL DEFAULT NULL,
  `date_taken` varchar(32) DEFAULT NULL,
  `date_corrected` varchar(32) DEFAULT NULL,
  `original_filename` varchar(45) DEFAULT NULL,
  `original_file_hash` VARCHAR(32) NULL DEFAULT NULL,
  `bestimate_filename` varchar(45) DEFAULT NULL,
  `bestimate_file_hash` VARCHAR(32) NULL DEFAULT NULL,
  `type` varchar(10) DEFAULT NULL,
  `latitude` varchar(45) DEFAULT NULL,
  `longitude` varchar(45) DEFAULT NULL,
  `altitude` varchar(45) DEFAULT NULL,
  `orientation` BIT(1) NULL DEFAULT NULL,
  `title` VARCHAR(255) NULL DEFAULT NULL,
  `keyword` VARCHAR(255) NULL DEFAULT NULL,
  `rating` INT NULL DEFAULT NULL,
  `credate` datetime DEFAULT NULL,
  `upddate` datetime DEFAULT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `updater` varchar(255) DEFAULT NULL,
  `orig_exif` BLOB DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `ODID_UNIQUE` (`odid`,`type`),
  KEY `ParentImage_idx` (`parent_id`),
  CONSTRAINT `parent_image` FOREIGN KEY (`parent_id`) REFERENCES `image` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Table structure for table `media_file`
--

CREATE TABLE `media_file` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `image_id` int(11) unsigned DEFAULT NULL,
  `drive_id` int(11) unsigned NOT NULL,
  `folder_id` int(11) unsigned NOT NULL DEFAULT '0',
  `filename` varchar(256) NOT NULL,
  `original_filename` varchar(256) DEFAULT NULL,
  `date_from_filename` DATETIME NULL DEFAULT NULL,
  `name_version` int(2) unsigned DEFAULT 0,
  `XMPattached` bit(1) DEFAULT NULL,
  `parent` int(11) unsigned DEFAULT NULL,
  `filehash` varchar(32) DEFAULT NULL,
  `size` bigint(19) unsigned DEFAULT NULL,
  `date_mod` datetime DEFAULT NULL,
  `exifbackup` bit(1) unsigned DEFAULT NULL,
  `standalone` bit(1) unsigned DEFAULT NULL,
  `date_stored` varchar(32) DEFAULT NULL,
  `latitude` varchar(45) DEFAULT NULL,
  `longitude` varchar(45) DEFAULT NULL,
  `altitude` varchar(45) DEFAULT NULL,
  `orientation` BIT(1) NULL DEFAULT NULL,
  `keyword` VARCHAR(255) NULL DEFAULT NULL,
  `rating` INT NULL DEFAULT NULL,
  `title` VARCHAR(255) NULL DEFAULT NULL,
  `type` varchar(5) DEFAULT NULL,
  `credate` datetime DEFAULT NULL,
  `upddate` datetime DEFAULT NULL,
  `creator` varchar(255) DEFAULT NULL,
  `updater` varchar(255) DEFAULT NULL,
  `exif` BLOB DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `file_UNIQUE` (`drive_id`,`folder_id`,`filename`),
  KEY `mfile_drive_idx` (`drive_id`),
  KEY `mfile_image_idx` (`image_id`),
  KEY `mfile_folder_idx` (`folder_id`),
  KEY `mfile_parent_idx` (`parent`),
  CONSTRAINT `mfile_drive` FOREIGN KEY (`drive_id`) REFERENCES `drive` (`id`),
  CONSTRAINT `mfile_folder` FOREIGN KEY (`folder_id`) REFERENCES `folder` (`id`),
  CONSTRAINT `mfile_image` FOREIGN KEY (`image_id`) REFERENCES `image` (`id`),
  CONSTRAINT `mfile_parent` FOREIGN KEY (`parent`) REFERENCES `media_file` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1039693 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dump completed on 2020-01-26 17:01:18
