-- MySQL dump 10.13  Distrib 8.0.35, for Win64 (x86_64)
--
-- ------------------------------------------------------
-- Server version	8.0.35

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
SET @MYSQLDUMP_TEMP_LOG_BIN = @@SESSION.SQL_LOG_BIN;
SET @@SESSION.SQL_LOG_BIN= 0;

--
-- GTID state at the beginning of the backup 
--

SET @@GLOBAL.GTID_PURGED=/*!80000 '+'*/ '';

--
-- Table structure for table `additional_info`
--

DROP TABLE IF EXISTS `additional_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `additional_info` (
  `additional_info_id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `animal` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `background_id` bigint DEFAULT NULL,
  `background_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ideal_animal` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `interest` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `introduce` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `mask_id` bigint DEFAULT NULL,
  `mask_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `personality` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `email` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`additional_info_id`),
  UNIQUE KEY `UK_kpal3g17fd3dpj27sluw3uxso` (`email`),
  CONSTRAINT `FK4fn4vhgfow13qfqkyh5bjxyas` FOREIGN KEY (`email`) REFERENCES `member` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `additional_info`
--

LOCK TABLES `additional_info` WRITE;
/*!40000 ALTER TABLE `additional_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `additional_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `animal_face`
--

DROP TABLE IF EXISTS `animal_face`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `animal_face` (
  `animal_face_id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `animal1` bigint DEFAULT NULL,
  `animal2` bigint DEFAULT NULL,
  `animal3` bigint DEFAULT NULL,
  `animal4` bigint DEFAULT NULL,
  `animal5` bigint DEFAULT NULL,
  `member_email` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`animal_face_id`),
  UNIQUE KEY `UK_2c15i1sq98kcs1ia4uunywtj7` (`member_email`),
  CONSTRAINT `FK4fmyi8bi55cp58ynufj0xg29w` FOREIGN KEY (`member_email`) REFERENCES `member` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `animal_face`
--

LOCK TABLES `animal_face` WRITE;
/*!40000 ALTER TABLE `animal_face` DISABLE KEYS */;
/*!40000 ALTER TABLE `animal_face` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `background`
--

DROP TABLE IF EXISTS `background`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `background` (
  `background_id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `price` bigint DEFAULT NULL,
  `file_id` bigint DEFAULT NULL,
  PRIMARY KEY (`background_id`),
  UNIQUE KEY `UK_684lbr4mnx3chdiouh5wfaty8` (`file_id`),
  CONSTRAINT `FKgrh6fm3pnhjty9qtusrt8g4lo` FOREIGN KEY (`file_id`) REFERENCES `file` (`file_id`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `background`
--

LOCK TABLES `background` WRITE;
/*!40000 ALTER TABLE `background` DISABLE KEYS */;
INSERT INTO `background` VALUES (18,'2024-02-11 01:39:37.000000','2024-02-11 01:39:37.000000',200,300),(19,'2024-02-11 01:39:37.000000','2024-02-11 01:39:37.000000',150,301),(20,'2024-02-11 01:39:37.000000','2024-02-11 01:39:37.000000',150,302),(21,'2024-02-11 01:39:37.000000','2024-02-11 01:39:37.000000',170,303),(22,'2024-02-11 01:39:37.000000','2024-02-11 01:39:37.000000',100,304),(23,'2024-02-11 01:39:37.000000','2024-02-11 01:39:37.000000',200,305),(24,'2024-02-11 01:39:37.000000','2024-02-11 01:39:37.000000',200,306),(25,'2024-02-11 01:39:37.000000','2024-02-11 01:39:37.000000',150,307),(26,'2024-02-11 01:39:37.000000','2024-02-11 01:39:37.000000',250,308),(27,'2024-02-11 01:39:37.000000','2024-02-11 01:39:37.000000',220,309),(28,'2024-02-11 01:39:37.000000','2024-02-11 01:39:37.000000',170,310),(29,'2024-02-11 01:39:37.000000','2024-02-11 01:39:37.000000',120,311),(30,'2024-02-11 01:39:37.000000','2024-02-11 01:39:37.000000',600,312),(31,'2024-02-11 01:39:37.000000','2024-02-11 01:39:37.000000',220,313),(32,'2024-02-11 01:39:37.000000','2024-02-11 01:39:37.000000',300,314),(33,'2024-02-11 01:39:37.000000','2024-02-11 01:39:37.000000',600,315),(34,'2024-02-11 01:39:37.000000','2024-02-11 01:39:37.000000',100,316);
/*!40000 ALTER TABLE `background` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `background_inventory`
--

DROP TABLE IF EXISTS `background_inventory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `background_inventory` (
  `inventory_id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `background_id` bigint DEFAULT NULL,
  `email` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`inventory_id`),
  KEY `FK5i6mtsiml5wd0tf96f550sbhs` (`background_id`),
  KEY `FKk2jsg8j7x1gohjkgoc28m6gse` (`email`),
  CONSTRAINT `FK5i6mtsiml5wd0tf96f550sbhs` FOREIGN KEY (`background_id`) REFERENCES `background` (`background_id`),
  CONSTRAINT `FKk2jsg8j7x1gohjkgoc28m6gse` FOREIGN KEY (`email`) REFERENCES `member` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `background_inventory`
--

LOCK TABLES `background_inventory` WRITE;
/*!40000 ALTER TABLE `background_inventory` DISABLE KEYS */;
/*!40000 ALTER TABLE `background_inventory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `balance_game`
--

DROP TABLE IF EXISTS `balance_game`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `balance_game` (
  `balancegame_id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `sentence1` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `sentence2` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`balancegame_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `balance_game`
--

LOCK TABLES `balance_game` WRITE;
/*!40000 ALTER TABLE `balance_game` DISABLE KEYS */;
/*!40000 ALTER TABLE `balance_game` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `block`
--

DROP TABLE IF EXISTS `block`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `block` (
  `block_id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `block_from` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `block_to` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`block_id`),
  KEY `FK9bhmka9fjxeyd54wf5vl71nl` (`block_from`),
  KEY `FKb4lvtps19eodp15tsuq3dkjoj` (`block_to`),
  CONSTRAINT `FK9bhmka9fjxeyd54wf5vl71nl` FOREIGN KEY (`block_from`) REFERENCES `member` (`email`),
  CONSTRAINT `FKb4lvtps19eodp15tsuq3dkjoj` FOREIGN KEY (`block_to`) REFERENCES `member` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `block`
--

LOCK TABLES `block` WRITE;
/*!40000 ALTER TABLE `block` DISABLE KEYS */;
/*!40000 ALTER TABLE `block` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `catch_mind`
--

DROP TABLE IF EXISTS `catch_mind`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `catch_mind` (
  `catchmind_id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `word` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`catchmind_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `catch_mind`
--

LOCK TABLES `catch_mind` WRITE;
/*!40000 ALTER TABLE `catch_mind` DISABLE KEYS */;
/*!40000 ALTER TABLE `catch_mind` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `disabled_member`
--

DROP TABLE IF EXISTS `disabled_member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `disabled_member` (
  `disabled_member_id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `end_date` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `start_date` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `email` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`disabled_member_id`),
  KEY `FKrrspgso44kn6blojn5dyrwufh` (`email`),
  CONSTRAINT `FKrrspgso44kn6blojn5dyrwufh` FOREIGN KEY (`email`) REFERENCES `member` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `disabled_member`
--

LOCK TABLES `disabled_member` WRITE;
/*!40000 ALTER TABLE `disabled_member` DISABLE KEYS */;
/*!40000 ALTER TABLE `disabled_member` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dm`
--

DROP TABLE IF EXISTS `dm`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dm` (
  `dm_id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `message` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `sender` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `dm_room_id` bigint DEFAULT NULL,
  PRIMARY KEY (`dm_id`),
  KEY `FKtqm6gv082dispcjqjo8qp58vk` (`dm_room_id`),
  CONSTRAINT `FKtqm6gv082dispcjqjo8qp58vk` FOREIGN KEY (`dm_room_id`) REFERENCES `dm_room` (`dm_room_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dm`
--

LOCK TABLES `dm` WRITE;
/*!40000 ALTER TABLE `dm` DISABLE KEYS */;
/*!40000 ALTER TABLE `dm` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dm_room`
--

DROP TABLE IF EXISTS `dm_room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dm_room` (
  `dm_room_id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `receiver_last_read_id` bigint DEFAULT NULL,
  `sender_last_read_id` bigint DEFAULT NULL,
  `receiver` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `sender` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`dm_room_id`),
  KEY `FKix3opdp2bfj4b3hadj3lxj06c` (`receiver`),
  KEY `FK6ydb5ri20c42hprogm1b4on8h` (`sender`),
  CONSTRAINT `FK6ydb5ri20c42hprogm1b4on8h` FOREIGN KEY (`sender`) REFERENCES `member` (`email`),
  CONSTRAINT `FKix3opdp2bfj4b3hadj3lxj06c` FOREIGN KEY (`receiver`) REFERENCES `member` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dm_room`
--

LOCK TABLES `dm_room` WRITE;
/*!40000 ALTER TABLE `dm_room` DISABLE KEYS */;
/*!40000 ALTER TABLE `dm_room` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `file`
--

DROP TABLE IF EXISTS `file`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `file` (
  `file_id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `file_dir` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `file_name` varchar(1000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `img_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `origin_file_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `s3id` binary(16) DEFAULT NULL,
  `thumbnail_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `dm_id` bigint DEFAULT NULL,
  PRIMARY KEY (`file_id`),
  UNIQUE KEY `UK_jvlu92snepolhyx3bkrcn5y6t` (`s3id`),
  KEY `FK351j3vonso2h6xud08kxvyn2a` (`dm_id`),
  CONSTRAINT `FK351j3vonso2h6xud08kxvyn2a` FOREIGN KEY (`dm_id`) REFERENCES `dm` (`dm_id`)
) ENGINE=InnoDB AUTO_INCREMENT=332 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `file`
--

LOCK TABLES `file` WRITE;
/*!40000 ALTER TABLE `file` DISABLE KEYS */;
INSERT INTO `file` VALUES (200,'2024-02-11 01:34:52.000000','2024-02-11 01:34:52.000000','Mask/','animal_group.png','https://zooting-s3-bucket.s3.ap-northeast-2.amazonaws.com/Mask/animal_group.png','animal_group.png',NULL,NULL,NULL),(201,'2024-02-11 01:34:52.000000','2024-02-11 01:34:52.000000','Mask/','bear_pixel_angry.jpg','https://zooting-s3-bucket.s3.ap-northeast-2.amazonaws.com/Mask/bear_pixel_angry.jpg','bear_pixel_angry.jpg',NULL,NULL,NULL),(202,'2024-02-11 01:34:52.000000','2024-02-11 01:34:52.000000','Mask/','bear_pixel_kind.jpg','https://zooting-s3-bucket.s3.ap-northeast-2.amazonaws.com/Mask/bear_pixel_kind.jpg','bear_pixel_kind.jpg',NULL,NULL,NULL),(203,'2024-02-11 01:34:52.000000','2024-02-11 01:34:52.000000','Mask/','bear_pixel_large.jpg','https://zooting-s3-bucket.s3.ap-northeast-2.amazonaws.com/Mask/bear_pixel_large.jpg','bear_pixel_large.jpg',NULL,NULL,NULL),(204,'2024-02-11 01:34:52.000000','2024-02-11 01:34:52.000000','Mask/','bearMask.png','https://zooting-s3-bucket.s3.ap-northeast-2.amazonaws.com/Mask/bearMask.png','bearMask.png',NULL,NULL,NULL),(205,'2024-02-11 01:34:52.000000','2024-02-11 01:34:52.000000','Mask/','cat_head.jpg','https://zooting-s3-bucket.s3.ap-northeast-2.amazonaws.com/Mask/cat_head.jpg','cat_head.jpg',NULL,NULL,NULL),(206,'2024-02-11 01:34:52.000000','2024-02-11 01:34:52.000000','Mask/','cat_pixel_cheeze.jpg','https://zooting-s3-bucket.s3.ap-northeast-2.amazonaws.com/Mask/cat_pixel_cheeze.jpg','cat_pixel_cheeze.jpg',NULL,NULL,NULL),(207,'2024-02-11 01:34:52.000000','2024-02-11 01:34:52.000000','Mask/','cat_pixel_dangerous.jpg','https://zooting-s3-bucket.s3.ap-northeast-2.amazonaws.com/Mask/cat_pixel_dangerous.jpg','cat_pixel_dangerous.jpg',NULL,NULL,NULL),(208,'2024-02-11 01:34:52.000000','2024-02-11 01:34:52.000000','Mask/','cat_pixel_frog.png','https://zooting-s3-bucket.s3.ap-northeast-2.amazonaws.com/Mask/cat_pixel_frog.png','cat_pixel_frog.png',NULL,NULL,NULL),(209,'2024-02-11 01:34:52.000000','2024-02-11 01:34:52.000000','Mask/','cat_pixel_tail.jpg','https://zooting-s3-bucket.s3.ap-northeast-2.amazonaws.com/Mask/cat_pixel_tail.jpg','cat_pixel_tail.jpg',NULL,NULL,NULL),(210,'2024-02-11 01:34:52.000000','2024-02-11 01:34:52.000000','Mask/','cat_pixel_white.gif','https://zooting-s3-bucket.s3.ap-northeast-2.amazonaws.com/Mask/cat_pixel_white.gif','cat_pixel_white.gif',NULL,NULL,NULL),(211,'2024-02-11 01:34:52.000000','2024-02-11 01:34:52.000000','Mask/','cat.png','https://zooting-s3-bucket.s3.ap-northeast-2.amazonaws.com/Mask/cat.png','cat.png',NULL,NULL,NULL),(212,'2024-02-11 01:34:52.000000','2024-02-11 01:34:52.000000','Mask/','deer_stand.jpg','https://zooting-s3-bucket.s3.ap-northeast-2.amazonaws.com/Mask/deer_stand.jpg','deer_stand.jpg',NULL,NULL,NULL),(213,'2024-02-11 01:34:52.000000','2024-02-11 01:34:52.000000','Mask/','deerMask.png','https://zooting-s3-bucket.s3.ap-northeast-2.amazonaws.com/Mask/deerMask.png','deerMask.png',NULL,NULL,NULL),(214,'2024-02-11 01:34:52.000000','2024-02-11 01:34:52.000000','Mask/','dinoMask.png','https://zooting-s3-bucket.s3.ap-northeast-2.amazonaws.com/Mask/dinoMask.png','dinoMask.png',NULL,NULL,NULL),(215,'2024-02-11 01:34:52.000000','2024-02-11 01:34:52.000000','Mask/','dinosoar_pixel_birthday.jpg','https://zooting-s3-bucket.s3.ap-northeast-2.amazonaws.com/Mask/dinosoar_pixel_birthday.jpg','dinosoar_pixel_birthday.jpg',NULL,NULL,NULL),(216,'2024-02-11 01:34:52.000000','2024-02-11 01:34:52.000000','Mask/','dog_cynical.jpg','https://zooting-s3-bucket.s3.ap-northeast-2.amazonaws.com/Mask/dog_cynical.jpg','dog_cynical.jpg',NULL,NULL,NULL),(217,'2024-02-11 01:34:52.000000','2024-02-11 01:34:52.000000','Mask/','dog_eyebrow.jpg','https://zooting-s3-bucket.s3.ap-northeast-2.amazonaws.com/Mask/dog_eyebrow.jpg','dog_eyebrow.jpg',NULL,NULL,NULL),(218,'2024-02-11 01:34:52.000000','2024-02-11 01:34:52.000000','Mask/','dog_pixel_running.gif','https://zooting-s3-bucket.s3.ap-northeast-2.amazonaws.com/Mask/dog_pixel_running.gif','dog_pixel_running.gif',NULL,NULL,NULL),(219,'2024-02-11 01:34:52.000000','2024-02-11 01:34:52.000000','Mask/','dog_pixel_watermelon.jpg','https://zooting-s3-bucket.s3.ap-northeast-2.amazonaws.com/Mask/dog_pixel_watermelon.jpg','dog_pixel_watermelon.jpg',NULL,NULL,NULL),(220,'2024-02-11 01:34:52.000000','2024-02-11 01:34:52.000000','Mask/','dog_sigh.jpg','https://zooting-s3-bucket.s3.ap-northeast-2.amazonaws.com/Mask/dog_sigh.jpg','dog_sigh.jpg',NULL,NULL,NULL),(221,'2024-02-11 01:34:52.000000','2024-02-11 01:34:52.000000','Mask/','dog_white.jpg','https://zooting-s3-bucket.s3.ap-northeast-2.amazonaws.com/Mask/dog_white.jpg','dog_white.jpg',NULL,NULL,NULL),(222,'2024-02-11 01:34:52.000000','2024-02-11 01:34:52.000000','Mask/','dogMask.png','https://zooting-s3-bucket.s3.ap-northeast-2.amazonaws.com/Mask/dogMask.png','dogMask.png',NULL,NULL,NULL),(223,'2024-02-11 01:34:52.000000','2024-02-11 01:34:52.000000','Mask/','penguin_pixel_blue.png','https://zooting-s3-bucket.s3.ap-northeast-2.amazonaws.com/Mask/penguin_pixel_blue.png','penguin_pixel_blue.png',NULL,NULL,NULL),(224,'2024-02-11 01:34:52.000000','2024-02-11 01:34:52.000000','Mask/','penguinMask.png','https://zooting-s3-bucket.s3.ap-northeast-2.amazonaws.com/Mask/penguinMask.png','penguinMask.png',NULL,NULL,NULL),(225,'2024-02-11 01:34:52.000000','2024-02-11 01:34:52.000000','Mask/','pupple_cat.png','https://zooting-s3-bucket.s3.ap-northeast-2.amazonaws.com/Mask/pupple_cat.png','pupple_cat.png',NULL,NULL,NULL),(226,'2024-02-11 01:34:52.000000','2024-02-11 01:34:52.000000','Mask/','pupple_dog.png','https://zooting-s3-bucket.s3.ap-northeast-2.amazonaws.com/Mask/pupple_dog.png','pupple_dog.png',NULL,NULL,NULL),(227,'2024-02-11 01:34:52.000000','2024-02-11 01:34:52.000000','Mask/','rabbit_hug.jpg','https://zooting-s3-bucket.s3.ap-northeast-2.amazonaws.com/Mask/rabbit_hug.jpg','rabbit_hug.jpg',NULL,NULL,NULL),(228,'2024-02-11 01:34:52.000000','2024-02-11 01:34:52.000000','Mask/','rabbit_pixel_pink_karate.jpg','https://zooting-s3-bucket.s3.ap-northeast-2.amazonaws.com/Mask/rabbit_pixel_pink_karate.jpg','rabbit_pixel_pink_karate.jpg',NULL,NULL,NULL),(229,'2024-02-11 01:34:52.000000','2024-02-11 01:34:52.000000','Mask/','rabbit_pixel_yellow_karate.jpg','https://zooting-s3-bucket.s3.ap-northeast-2.amazonaws.com/Mask/rabbit_pixel_yellow_karate.jpg','rabbit_pixel_yellow_karate.jpg',NULL,NULL,NULL),(230,'2024-02-11 01:34:52.000000','2024-02-11 01:34:52.000000','Mask/','rabbitMask.png','https://zooting-s3-bucket.s3.ap-northeast-2.amazonaws.com/Mask/rabbitMask.png','rabbitMask.png',NULL,NULL,NULL),(300,'2024-02-11 01:39:35.000000','2024-02-11 01:39:35.000000','Background/','cat_faces.jpg','https://zooting-s3-bucket.s3.ap-northeast-2.amazonaws.com/Background/cat_faces.jpg','cat_faces.jpg',NULL,NULL,NULL),(301,'2024-02-11 01:39:35.000000','2024-02-11 01:39:35.000000','Background/','cat_jealous.jpg','https://zooting-s3-bucket.s3.ap-northeast-2.amazonaws.com/Background/cat_jealous.jpg','cat_jealous.jpg',NULL,NULL,NULL),(302,'2024-02-11 01:39:35.000000','2024-02-11 01:39:35.000000','Background/','cat_mirror.jpg','https://zooting-s3-bucket.s3.ap-northeast-2.amazonaws.com/Background/cat_mirror.jpg','cat_mirror.jpg',NULL,NULL,NULL),(303,'2024-02-11 01:39:35.000000','2024-02-11 01:39:35.000000','Background/','deer_and_cat_love.jpg','https://zooting-s3-bucket.s3.ap-northeast-2.amazonaws.com/Background/deer_and_cat_love.jpg','deer_and_cat_love.jpg',NULL,NULL,NULL),(304,'2024-02-11 01:39:35.000000','2024-02-11 01:39:35.000000','Background/','dog_and_cat_bed.jpg','https://zooting-s3-bucket.s3.ap-northeast-2.amazonaws.com/Background/dog_and_cat_bed.jpg','dog_and_cat_bed.jpg',NULL,NULL,NULL),(305,'2024-02-11 01:39:35.000000','2024-02-11 01:39:35.000000','Background/','dog_and_cat_blanket.jpg','https://zooting-s3-bucket.s3.ap-northeast-2.amazonaws.com/Background/dog_and_cat_blanket.jpg','dog_and_cat_blanket.jpg',NULL,NULL,NULL),(306,'2024-02-11 01:39:35.000000','2024-02-11 01:39:35.000000','Background/','dog_and_cat_board.jpg','https://zooting-s3-bucket.s3.ap-northeast-2.amazonaws.com/Background/dog_and_cat_board.jpg','dog_and_cat_board.jpg',NULL,NULL,NULL),(307,'2024-02-11 01:39:35.000000','2024-02-11 01:39:35.000000','Background/','dog_jealous.jpg','https://zooting-s3-bucket.s3.ap-northeast-2.amazonaws.com/Background/dog_jealous.jpg','dog_jealous.jpg',NULL,NULL,NULL),(308,'2024-02-11 01:39:35.000000','2024-02-11 01:39:35.000000','Background/','pixel_apartment.jpg','https://zooting-s3-bucket.s3.ap-northeast-2.amazonaws.com/Background/pixel_apartment.jpg','pixel_apartment.jpg',NULL,NULL,NULL),(309,'2024-02-11 01:39:35.000000','2024-02-11 01:39:35.000000','Background/','pixel_convenience_store.jpg','https://zooting-s3-bucket.s3.ap-northeast-2.amazonaws.com/Background/pixel_convenience_store.jpg','pixel_convenience_store.jpg',NULL,NULL,NULL),(310,'2024-02-11 01:39:35.000000','2024-02-11 01:39:35.000000','Background/','pixel_dinosoar.gif','https://zooting-s3-bucket.s3.ap-northeast-2.amazonaws.com/Background/pixel_dinosoar.gif','pixel_dinosoar.gif',NULL,NULL,NULL),(311,'2024-02-11 01:39:35.000000','2024-02-11 01:39:35.000000','Background/','pixel_purple_castle.jpg','https://zooting-s3-bucket.s3.ap-northeast-2.amazonaws.com/Background/pixel_purple_castle.jpg','pixel_purple_castle.jpg',NULL,NULL,NULL),(312,'2024-02-11 01:39:35.000000','2024-02-11 01:39:35.000000','Background/','pixel_purple_night.gif','https://zooting-s3-bucket.s3.ap-northeast-2.amazonaws.com/Background/pixel_purple_night.gif','pixel_purple_night.gif',NULL,NULL,NULL),(313,'2024-02-11 01:39:35.000000','2024-02-11 01:39:35.000000','Background/','pixel_seveneleven.jpg','https://zooting-s3-bucket.s3.ap-northeast-2.amazonaws.com/Background/pixel_seveneleven.jpg','pixel_seveneleven.jpg',NULL,NULL,NULL),(314,'2024-02-11 01:39:35.000000','2024-02-11 01:39:35.000000','Background/','tired_dinosoar.jpg','https://zooting-s3-bucket.s3.ap-northeast-2.amazonaws.com/Background/tired_dinosoar.jpg','tired_dinosoar.jpg',NULL,NULL,NULL),(315,'2024-02-11 01:39:35.000000','2024-02-11 01:39:35.000000','Background/','travel_dog.gif','https://zooting-s3-bucket.s3.ap-northeast-2.amazonaws.com/Background/travel_dog.gif','travel_dog.gif',NULL,NULL,NULL),(316,'2024-02-11 01:39:35.000000','2024-02-11 01:39:35.000000','Background/','various_cat.jpg','https://zooting-s3-bucket.s3.ap-northeast-2.amazonaws.com/Background/various_cat.jpg','various_cat.jpg',NULL,NULL,NULL),(324,'2024-02-13 17:32:15.000000','2024-02-13 17:32:16.000000','Mask/','dinoMask-216.png','https://zooting-s3-bucket.s3.ap-northeast-2.amazonaws.com/Mask/dinoMask-216.png','dinoMask-216.png',NULL,'',NULL),(327,'2024-02-13 23:53:12.000000','2024-02-13 23:53:14.000000','Mask/','deerMask_purple.png','https://zooting-s3-bucket.s3.ap-northeast-2.amazonaws.com/Mask/deerMask_purple.png','deerMask_purple.png',NULL,NULL,NULL),(328,'2024-02-13 23:53:38.000000','2024-02-13 23:53:38.000000','Mask/','dogMask_blue.png','https://zooting-s3-bucket.s3.ap-northeast-2.amazonaws.com/Mask/dogMask_blue.png','dogMask_blue.png',NULL,NULL,NULL),(329,'2024-02-13 23:54:09.000000','2024-02-13 23:54:10.000000','Mask/','catMask_yellow.png','https://zooting-s3-bucket.s3.ap-northeast-2.amazonaws.com/Mask/catMask_yellow.png','catMask_yellow.png',NULL,NULL,NULL),(330,'2024-02-13 23:54:39.000000','2024-02-13 23:54:40.000000','Mask/','rabbitMask_green.png','https://zooting-s3-bucket.s3.ap-northeast-2.amazonaws.com/Mask/rabbitMask_green.png','rabbitMask_green.png',NULL,NULL,NULL),(331,'2024-02-13 23:55:08.000000','2024-02-13 23:55:09.000000','Mask/','bearMask_white.png','https://zooting-s3-bucket.s3.ap-northeast-2.amazonaws.com/Mask/bearMask_white.png','bearMask_white.png',NULL,NULL,NULL);
/*!40000 ALTER TABLE `file` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `friend`
--

DROP TABLE IF EXISTS `friend`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `friend` (
  `friend_id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `follower` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `following` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`friend_id`),
  KEY `FKt4v20j5tqq7j6ldykfo9nybol` (`follower`),
  KEY `FKrfqu3nf7ss2ph0oobk7nrolba` (`following`),
  CONSTRAINT `FKrfqu3nf7ss2ph0oobk7nrolba` FOREIGN KEY (`following`) REFERENCES `member` (`email`),
  CONSTRAINT `FKt4v20j5tqq7j6ldykfo9nybol` FOREIGN KEY (`follower`) REFERENCES `member` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `friend`
--

LOCK TABLES `friend` WRITE;
/*!40000 ALTER TABLE `friend` DISABLE KEYS */;
/*!40000 ALTER TABLE `friend` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `friend_request`
--

DROP TABLE IF EXISTS `friend_request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `friend_request` (
  `friend_request_id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `request_from` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `request_to` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`friend_request_id`),
  KEY `FKsp7qgwjrwtq2tr1sc1cco7jdc` (`request_from`),
  KEY `FKjkx1by5houk0fhggw4orrd1f6` (`request_to`),
  CONSTRAINT `FKjkx1by5houk0fhggw4orrd1f6` FOREIGN KEY (`request_to`) REFERENCES `member` (`email`),
  CONSTRAINT `FKsp7qgwjrwtq2tr1sc1cco7jdc` FOREIGN KEY (`request_from`) REFERENCES `member` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `friend_request`
--

LOCK TABLES `friend_request` WRITE;
/*!40000 ALTER TABLE `friend_request` DISABLE KEYS */;
/*!40000 ALTER TABLE `friend_request` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `liar_game`
--

DROP TABLE IF EXISTS `liar_game`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `liar_game` (
  `liargame_id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `topic` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `word` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`liargame_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `liar_game`
--

LOCK TABLES `liar_game` WRITE;
/*!40000 ALTER TABLE `liar_game` DISABLE KEYS */;
/*!40000 ALTER TABLE `liar_game` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mask`
--

DROP TABLE IF EXISTS `mask`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mask` (
  `mask_id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `animal` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `price` bigint DEFAULT NULL,
  `file_id` bigint DEFAULT NULL,
  PRIMARY KEY (`mask_id`),
  UNIQUE KEY `UK_s4c2g65qctmakc1hpj2c9u6ms` (`file_id`),
  CONSTRAINT `FKshhsjwj804ox24s8k7wyfph9a` FOREIGN KEY (`file_id`) REFERENCES `file` (`file_id`)
) ENGINE=InnoDB AUTO_INCREMENT=239 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mask`
--

LOCK TABLES `mask` WRITE;
/*!40000 ALTER TABLE `mask` DISABLE KEYS */;
INSERT INTO `mask` VALUES (100,'2024-02-11 01:34:56.000000','2024-02-11 01:34:56.000000','기본마스크','동물 그룹 마스크',200,200),(201,'2024-02-11 01:34:56.000000','2024-02-11 01:34:56.000000','곰','',200,201),(202,'2024-02-11 01:34:56.000000','2024-02-11 01:34:56.000000','곰','',200,202),(203,'2024-02-11 01:34:56.000000','2024-02-11 01:34:56.000000','곰','',200,203),(204,'2024-02-11 01:34:56.000000','2024-02-11 01:34:56.000000','곰','',200,204),(205,'2024-02-11 01:34:56.000000','2024-02-11 01:34:56.000000','고양이','',200,205),(206,'2024-02-11 01:34:56.000000','2024-02-11 01:34:56.000000','고양이','',200,206),(207,'2024-02-11 01:34:56.000000','2024-02-11 01:34:56.000000','고양이','',200,207),(208,'2024-02-11 01:34:56.000000','2024-02-11 01:34:56.000000','고양이','',200,208),(209,'2024-02-11 01:34:56.000000','2024-02-11 01:34:56.000000','고양이','',200,209),(210,'2024-02-11 01:34:56.000000','2024-02-11 01:34:56.000000','고양이','',200,210),(211,'2024-02-11 01:34:56.000000','2024-02-11 01:34:56.000000','고양이','',200,211),(212,'2024-02-11 01:34:56.000000','2024-02-11 01:34:56.000000','사슴','',200,212),(213,'2024-02-11 01:34:56.000000','2024-02-11 01:34:56.000000','사슴','',200,213),(214,'2024-02-11 01:34:56.000000','2024-02-11 01:34:56.000000','공룡','',200,214),(215,'2024-02-11 01:34:56.000000','2024-02-11 01:34:56.000000','공룡','',200,215),(216,'2024-02-11 01:34:56.000000','2024-02-11 01:34:56.000000','강아지','',200,216),(217,'2024-02-11 01:34:56.000000','2024-02-11 01:34:56.000000','강아지','',200,217),(218,'2024-02-11 01:34:56.000000','2024-02-11 01:34:56.000000','강아지','',200,218),(219,'2024-02-11 01:34:56.000000','2024-02-11 01:34:56.000000','강아지','',200,219),(220,'2024-02-11 01:34:56.000000','2024-02-11 01:34:56.000000','강아지','',200,220),(221,'2024-02-11 01:34:56.000000','2024-02-11 01:34:56.000000','강아지','',200,221),(222,'2024-02-11 01:34:56.000000','2024-02-11 01:34:56.000000','강아지','',200,222),(223,'2024-02-11 01:34:56.000000','2024-02-11 01:34:56.000000','펭귄','',200,223),(224,'2024-02-11 01:34:56.000000','2024-02-11 01:34:56.000000','펭귄','',200,224),(225,'2024-02-11 01:34:56.000000','2024-02-11 01:34:56.000000','고양이','',200,225),(226,'2024-02-11 01:34:56.000000','2024-02-11 01:34:56.000000','강아지','',200,226),(227,'2024-02-11 01:34:56.000000','2024-02-11 01:34:56.000000','토끼','',200,227),(228,'2024-02-11 01:34:56.000000','2024-02-11 01:34:56.000000','토끼','',200,228),(229,'2024-02-11 01:34:56.000000','2024-02-11 01:34:56.000000','토끼','',200,229),(230,'2024-02-11 01:34:56.000000','2024-02-11 01:34:56.000000','토끼','',200,230),(233,'2024-02-13 17:36:47.000000','2024-02-13 17:36:48.000000','공룡','',200,324),(234,'2024-02-13 23:55:54.000000','2024-02-13 23:55:55.000000','사슴','',200,327),(235,'2024-02-13 23:56:25.000000','2024-02-13 23:56:26.000000','강아지','',200,328),(236,'2024-02-13 23:56:52.000000','2024-02-13 23:56:52.000000','고양이','',200,329),(237,'2024-02-13 23:57:17.000000','2024-02-13 23:57:18.000000','토끼','',200,330),(238,'2024-02-13 23:57:28.000000','2024-02-13 23:57:29.000000','곰','',200,331);
/*!40000 ALTER TABLE `mask` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mask_inventory`
--

DROP TABLE IF EXISTS `mask_inventory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mask_inventory` (
  `mask_inventory_id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `mask_id` bigint DEFAULT NULL,
  `email` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`mask_inventory_id`),
  KEY `FKdfsfwwb1li581bxeqd7touwh2` (`mask_id`),
  KEY `FKff04po331oi8tpoehnfd76kme` (`email`),
  CONSTRAINT `FKdfsfwwb1li581bxeqd7touwh2` FOREIGN KEY (`mask_id`) REFERENCES `mask` (`mask_id`),
  CONSTRAINT `FKff04po331oi8tpoehnfd76kme` FOREIGN KEY (`email`) REFERENCES `member` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mask_inventory`
--

LOCK TABLES `mask_inventory` WRITE;
/*!40000 ALTER TABLE `mask_inventory` DISABLE KEYS */;
/*!40000 ALTER TABLE `mask_inventory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `meeting_log`
--

DROP TABLE IF EXISTS `meeting_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `meeting_log` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `meeting_room_id` binary(16) DEFAULT NULL,
  `email` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKjpi5poj2gjsn3cfhg68otji36` (`email`),
  CONSTRAINT `FKjpi5poj2gjsn3cfhg68otji36` FOREIGN KEY (`email`) REFERENCES `member` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `meeting_log`
--

LOCK TABLES `meeting_log` WRITE;
/*!40000 ALTER TABLE `meeting_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `meeting_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `member`
--

DROP TABLE IF EXISTS `member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `member` (
  `email` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `address` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `birth` datetime(6) DEFAULT NULL,
  `gender` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `nickname` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `point` bigint DEFAULT NULL,
  `status` bit(1) DEFAULT NULL,
  PRIMARY KEY (`email`),
  UNIQUE KEY `UK_hh9kg6jti4n1eoiertn2k6qsc` (`nickname`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `member`
--

LOCK TABLES `member` WRITE;
/*!40000 ALTER TABLE `member` DISABLE KEYS */;
/*!40000 ALTER TABLE `member` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `member_role`
--

DROP TABLE IF EXISTS `member_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `member_role` (
  `member_id` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `role` enum('ANONYMOUS','USER','MANAGER','ADMIN') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  KEY `FK34g7epqlcxqloewku3aoqhhmg` (`member_id`),
  CONSTRAINT `FK34g7epqlcxqloewku3aoqhhmg` FOREIGN KEY (`member_id`) REFERENCES `member` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `member_role`
--

LOCK TABLES `member_role` WRITE;
/*!40000 ALTER TABLE `member_role` DISABLE KEYS */;
/*!40000 ALTER TABLE `member_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notice`
--

DROP TABLE IF EXISTS `notice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notice` (
  `notice_id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `content` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `title` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`notice_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notice`
--

LOCK TABLES `notice` WRITE;
/*!40000 ALTER TABLE `notice` DISABLE KEYS */;
INSERT INTO `notice` VALUES (1,'2024-02-10 16:39:29.000000','2024-02-10 16:39:22.000000','익일(2024년 2월 11일) 2:00 ~ 4:00 간 서비스 이용이 불가하오니 양해부탁드립니다.','[서버점검 안내]'),(3,'2024-02-01 16:49:53.000000','2024-02-01 16:49:45.000000','불법 거래 권유가 있으면 신고 기능 또는 jeheon0717@nate.com로 신고하시고, 위급한 경우에는 경찰청(112), 안전Dream(117), 여성긴급전화(1336), 기타 연관 성폭력 보호센터(http://www.sexoffender.go.kr/)의 도움을 받을 수 있습니다.','[서비스 사용중 불법거래 방지]'),(4,'2024-02-02 16:53:01.000000','2024-02-02 16:52:54.000000','* 필수 접근 권한\n카메라 : 동물상 확인, 화상 채팅을 위해 필요\n마이크 : 영상 통화를 위해 필요\n','[접근 권한 안내]'),(5,'2024-01-14 16:56:37.000000','2024-01-14 16:56:48.000000','주팅 고객센터 : zyo0720@kakao.com\n고객센터 영업시간 월 ~ 금 오전 10: 00 ~ 오후 10:00','[고객센터 문의]'),(6,'2024-01-15 16:58:41.000000','2024-01-15 16:58:50.000000','버그를 좋아하는 사람은 아무도 없죠. 걱정마세요! 공지사항 조회 버그를 고쳤습니다.','[버그 치료 및 기타 성능 강화]'),(7,'2024-02-13 17:17:49.000000','2024-02-10 17:17:42.000000','익일(2024년 2월 14일) 2:00 ~ 4:00 간 서비스 이용이 불가하오니 양해부탁드립니다.','[서버점검 안내]'),(8,'2024-02-13 17:19:07.000000','2024-02-12 17:19:03.000000','동물상으로 사용할 수 있는 마스크가 추가되었어요! 마이페이지에서 확인해보세요','[마스크 추가 안내]'),(9,'2024-02-13 17:23:27.000000','2024-02-11 17:21:19.000000','■ 업데이트 일정 : 2024년 2월 14일(화) 오후 2시경\n','[주팅 비정기 업데이트 소식]'),(10,'2024-02-13 17:27:33.000000','2024-02-12 17:27:25.000000','버그를 좋아하는 사람은 아무도 없죠. 걱정마세요! 공지사항 조회 버그를 고쳤습니다.','[버그 치료 및 기타 성능 강화]');
/*!40000 ALTER TABLE `notice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `report_list`
--

DROP TABLE IF EXISTS `report_list`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `report_list` (
  `report_list_id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `date` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `reason` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `email` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`report_list_id`),
  KEY `FKfjr054r88npdgpr1w998c4idk` (`email`),
  CONSTRAINT `FKfjr054r88npdgpr1w998c4idk` FOREIGN KEY (`email`) REFERENCES `member` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `report_list`
--

LOCK TABLES `report_list` WRITE;
/*!40000 ALTER TABLE `report_list` DISABLE KEYS */;
/*!40000 ALTER TABLE `report_list` ENABLE KEYS */;
UNLOCK TABLES;
SET @@SESSION.SQL_LOG_BIN = @MYSQLDUMP_TEMP_LOG_BIN;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-02-15 13:55:22
