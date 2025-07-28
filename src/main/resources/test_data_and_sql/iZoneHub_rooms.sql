CREATE DATABASE  IF NOT EXISTS `iZoneHub` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `iZoneHub`;
-- MySQL dump 10.13  Distrib 8.0.36, for Linux (x86_64)
--
-- Host: 127.0.0.1    Database: iZoneHub
-- ------------------------------------------------------
-- Server version	8.0.42-0ubuntu0.24.04.2

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
-- Table structure for table `rooms`
--

DROP TABLE IF EXISTS `rooms`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rooms` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `image_path` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `price` decimal(38,2) NOT NULL,
  `room_type` enum('KTV','normal','projection') DEFAULT NULL,
  `created_at` date DEFAULT NULL,
  `updated_at` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rooms`
--

LOCK TABLES `rooms` WRITE;
/*!40000 ALTER TABLE `rooms` DISABLE KEYS */;
INSERT INTO `rooms` VALUES (1,'深色木質 + 皮革沙發，適合小型會議＋娛樂','assets/img/restaurant/room1.png','商務雅集',120.00,'normal',NULL,NULL),(2,'溫馨風格，木紋＋柔色沙發，親子適用','assets/img/restaurant/room2.png','休閒家庭',120.00,'normal',NULL,NULL),(3,'燈光秀模式（變色LED燈帶）、派對用音響','assets/img/restaurant/room3.png','夜光派對',120.00,'normal',NULL,NULL),(4,'塌塌米區＋電動麻將桌，和風燈具','assets/img/restaurant/room4.png','日系和風',120.00,'normal',NULL,NULL),(5,'水泥牆、鐵件長桌，極簡風格，適合年輕人','assets/img/restaurant/room5.png','工業風',120.00,'normal',NULL,NULL),(6,'Switch/PS5遊戲機，適合桌遊與電玩聚會','assets/img/restaurant/room6.png','文青風',120.00,'normal',NULL,NULL),(7,'Switch/PS5遊戲機，適合桌遊與電玩聚會','assets/img/restaurant/room7.png','遊戲同樂',120.00,'normal',NULL,NULL),(8,'自帶吧台空間，開放式小型自助飲料區','assets/img/restaurant/room8.png','自在酒吧',120.00,'normal',NULL,NULL),(9,'KTV音響、氛圍燈、4人沙發，支援無人點歌','assets/img/restaurant/room9.png','K-Lounge極致娛樂',250.00,'KTV',NULL,NULL),(10,'牆面隔音強化','assets/img/restaurant/room10.png','K-Family歡唱',250.00,'KTV',NULL,NULL),(11,'150吋投影幕＋劇院沙發，Netflix/串流支援','assets/img/restaurant/room11.png','電影房',200.00,'projection',NULL,NULL),(12,'商務用途，白牆＋可書寫牆面，適合開會','assets/img/restaurant/room12.png','投影簡報房',200.00,'projection',NULL,NULL),(13,'錄影設備＋投影鏡像，供樂團練習與回放檢視','assets/img/restaurant/room13.png','投影練團房',200.00,'projection',NULL,NULL),(14,'支援筆電直連，適合小型講座或線上課程','assets/img/restaurant/room14.png','投影教學房',200.00,'projection',NULL,NULL);
/*!40000 ALTER TABLE `rooms` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-28  9:54:42
