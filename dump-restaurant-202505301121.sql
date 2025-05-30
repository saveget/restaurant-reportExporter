-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: localhost    Database: restaurant
-- ------------------------------------------------------
-- Server version	5.5.5-10.3.39-MariaDB-1:10.3.39+maria~ubu2004

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

--
-- Table structure for table `kitchen_stations`
--

DROP TABLE IF EXISTS `kitchen_stations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `kitchen_stations` (
  `id` int(11) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `menu_item_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `menu_item_id` (`menu_item_id`),
  CONSTRAINT `kitchen_stations_ibfk_1` FOREIGN KEY (`menu_item_id`) REFERENCES `menu_items` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kitchen_stations`
--

LOCK TABLES `kitchen_stations` WRITE;
/*!40000 ALTER TABLE `kitchen_stations` DISABLE KEYS */;
INSERT INTO `kitchen_stations` VALUES (1,'Grill',NULL),(2,'Pasta',NULL),(3,'Salad',NULL),(4,'Beverage',NULL);
/*!40000 ALTER TABLE `kitchen_stations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `menu_items`
--

DROP TABLE IF EXISTS `menu_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `menu_items` (
  `id` int(11) NOT NULL,
  `name` varchar(50) NOT NULL,
  `category` varchar(30) DEFAULT NULL,
  `price` decimal(10,0) DEFAULT NULL,
  `is_available` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `menu_items`
--

LOCK TABLES `menu_items` WRITE;
/*!40000 ALTER TABLE `menu_items` DISABLE KEYS */;
INSERT INTO `menu_items` VALUES (101,'Grilled Chicken','grill',120,1),(102,'Pork Steak','grill',150,1),(103,'Grilled Salmon','grill',200,0),(201,'Spaghetti Carbonara','pasta',130,1),(202,'Spaghetti Bolognese','pasta',135,1),(301,'Caesar Salad','salad',95,1),(302,'Tuna Salad','salad',105,1),(401,'Lemon Soda','beverage',45,1),(402,'Iced Tea','beverage',40,1);
/*!40000 ALTER TABLE `menu_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_item`
--

DROP TABLE IF EXISTS `order_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_id` int(11) DEFAULT NULL,
  `menu_item_id` int(11) DEFAULT NULL,
  `quantity` int(11) NOT NULL,
  `status` enum('pending','preparing','done') DEFAULT 'pending',
  `station` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `menu_item_id` (`menu_item_id`),
  KEY `order_item_ibfk_1` (`order_id`),
  CONSTRAINT `order_item_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
  CONSTRAINT `order_item_ibfk_2` FOREIGN KEY (`menu_item_id`) REFERENCES `menu_items` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_item`
--

LOCK TABLES `order_item` WRITE;
/*!40000 ALTER TABLE `order_item` DISABLE KEYS */;
INSERT INTO `order_item` VALUES (41,28,102,1,'done','grill'),(42,28,301,1,'done','salad'),(43,28,402,1,'done','beverage'),(44,29,201,1,'done','pasta'),(45,29,401,1,'done','beverage'),(46,30,301,1,'done','salad'),(47,30,402,1,'done','beverage');
/*!40000 ALTER TABLE `order_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `table_id` int(11) DEFAULT NULL,
  `order_time` datetime DEFAULT NULL,
  `status` enum('pending','preparing','done') DEFAULT 'pending',
  `serve_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `table_id` (`table_id`),
  CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`table_id`) REFERENCES `restaurant_tables` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (28,1,'2025-05-29 16:52:09','done','2025-05-29 16:58:46'),(29,3,'2025-05-29 16:52:21','done','2025-05-29 16:59:28'),(30,4,'2025-05-29 16:52:35','done','2025-05-29 16:58:48');
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `restaurant_tables`
--

DROP TABLE IF EXISTS `restaurant_tables`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `restaurant_tables` (
  `id` int(11) NOT NULL,
  `table_num` varchar(10) NOT NULL,
  `status` enum('AVAILABLE','OCCUPIED') NOT NULL,
  `seats` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `restaurant_tables`
--

LOCK TABLES `restaurant_tables` WRITE;
/*!40000 ALTER TABLE `restaurant_tables` DISABLE KEYS */;
INSERT INTO `restaurant_tables` VALUES (1,'A1','AVAILABLE',4),(2,'A2','AVAILABLE',2),(3,'B1','AVAILABLE',4),(4,'B2','AVAILABLE',6),(5,'C1','AVAILABLE',2);
/*!40000 ALTER TABLE `restaurant_tables` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `role` varchar(30) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'customer_A1','customer','$2a$10$iMrTNpkuEfMVbJ1V0QE3guvexcRTqsYvVsR6lqkcBY3Ay305AZ64y'),(2,'customer_A2','customer','$2a$10$iMrTNpkuEfMVbJ1V0QE3guvexcRTqsYvVsR6lqkcBY3Ay305AZ64y'),(3,'customer_B1','customer','$2a$10$iMrTNpkuEfMVbJ1V0QE3guvexcRTqsYvVsR6lqkcBY3Ay305AZ64y'),(4,'customer_B2','customer','$2a$10$iMrTNpkuEfMVbJ1V0QE3guvexcRTqsYvVsR6lqkcBY3Ay305AZ64y'),(5,'customer_C1','customer','$2a$10$iMrTNpkuEfMVbJ1V0QE3guvexcRTqsYvVsR6lqkcBY3Ay305AZ64y'),(6,'Harry','waitress','$2a$10$iMrTNpkuEfMVbJ1V0QE3guvexcRTqsYvVsR6lqkcBY3Ay305AZ64y'),(7,'Lana','waitress','$2a$10$iMrTNpkuEfMVbJ1V0QE3guvexcRTqsYvVsR6lqkcBY3Ay305AZ64y'),(8,'Sabrina','waitress','$2a$10$iMrTNpkuEfMVbJ1V0QE3guvexcRTqsYvVsR6lqkcBY3Ay305AZ64y'),(9,'Taylor','chef_grill','$2a$10$iMrTNpkuEfMVbJ1V0QE3guvexcRTqsYvVsR6lqkcBY3Ay305AZ64y'),(10,'Shawn','chef_beverage','$2a$10$iMrTNpkuEfMVbJ1V0QE3guvexcRTqsYvVsR6lqkcBY3Ay305AZ64y'),(11,'Katy','chef_salad','$2a$10$iMrTNpkuEfMVbJ1V0QE3guvexcRTqsYvVsR6lqkcBY3Ay305AZ64y'),(12,'Giada','chef_pasta','$2a$10$iMrTNpkuEfMVbJ1V0QE3guvexcRTqsYvVsR6lqkcBY3Ay305AZ64y'),(13,'manager1','manager','$2a$10$iMrTNpkuEfMVbJ1V0QE3guvexcRTqsYvVsR6lqkcBY3Ay305AZ64y');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'restaurant'
--
