-- MySQL dump 10.13  Distrib 8.0.46, for Win64 (x86_64)
--
-- Host: localhost    Database: acompanhamento_menstrual
-- ------------------------------------------------------
-- Server version	8.0.46

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
-- Table structure for table `auditoria_registro_sintoma`
--

DROP TABLE IF EXISTS `auditoria_registro_sintoma`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `auditoria_registro_sintoma` (
  `id_auditoria` bigint unsigned NOT NULL AUTO_INCREMENT,
  `operacao` varchar(10) NOT NULL,
  `id_registro_sintoma` int unsigned NOT NULL,
  `autor` varchar(288) NOT NULL,
  `data_hora` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `valores_antigos` json DEFAULT NULL,
  `valores_novos` json DEFAULT NULL,
  PRIMARY KEY (`id_auditoria`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auditoria_registro_sintoma`
--

LOCK TABLES `auditoria_registro_sintoma` WRITE;
/*!40000 ALTER TABLE `auditoria_registro_sintoma` DISABLE KEYS */;
INSERT INTO `auditoria_registro_sintoma` VALUES (1,'UPDATE',1,'root@localhost','2026-10-26 12:03:17','{\"intensidade\": 4, \"data_observacao\": \"2026-01-04\"}','{\"intensidade\": 5, \"data_observacao\": \"2026-01-04\"}'),(2,'INSERT',145,'root@localhost','2026-10-26 12:10:32',NULL,'{\"id_ciclo\": 1, \"id_sintoma\": 1, \"intensidade\": 3, \"data_observacao\": \"2026-10-26\"}');
/*!40000 ALTER TABLE `auditoria_registro_sintoma` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ciclo_menstrual`
--

DROP TABLE IF EXISTS `ciclo_menstrual`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ciclo_menstrual` (
  `id_ciclo` int unsigned NOT NULL AUTO_INCREMENT,
  `id_usuario` int unsigned NOT NULL,
  `data_inicio` date NOT NULL,
  `data_fim` date DEFAULT NULL,
  `duracao` smallint unsigned NOT NULL,
  `id_ciclo_anterior` int unsigned DEFAULT NULL,
  PRIMARY KEY (`id_ciclo`),
  UNIQUE KEY `uq_ciclo_usuario_data_inicio` (`id_usuario`,`data_inicio`),
  KEY `idx_ciclo_data_inicio` (`data_inicio`),
  KEY `idx_ciclo_usuario` (`id_usuario`),
  KEY `fk_ciclo_anterior` (`id_ciclo_anterior`),
  CONSTRAINT `fk_ciclo_anterior` FOREIGN KEY (`id_ciclo_anterior`) REFERENCES `ciclo_menstrual` (`id_ciclo`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_ciclo_usuario` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id_usuario`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `ck_ciclo_datas` CHECK (((`data_fim` is null) or (`data_fim` >= `data_inicio`))),
  CONSTRAINT `ck_ciclo_duracao` CHECK ((`duracao` > 0))
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ciclo_menstrual`
--

LOCK TABLES `ciclo_menstrual` WRITE;
/*!40000 ALTER TABLE `ciclo_menstrual` DISABLE KEYS */;
INSERT INTO `ciclo_menstrual` VALUES (1,1,'2026-01-03','2026-01-30',28,NULL),(2,1,'2026-01-31','2026-02-28',29,NULL),(3,1,'2026-03-01','2026-03-29',29,NULL),(4,2,'2026-02-02','2026-03-02',29,NULL),(5,2,'2026-03-03','2026-03-31',29,NULL),(6,2,'2026-04-01','2026-04-29',29,NULL),(7,3,'2026-08-20',NULL,30,NULL),(8,4,'2026-04-10','2026-05-07',28,NULL),(9,4,'2026-05-08','2026-06-04',28,NULL),(10,5,'2026-05-15','2026-06-13',30,NULL);
/*!40000 ALTER TABLE `ciclo_menstrual` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fase`
--

DROP TABLE IF EXISTS `fase`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fase` (
  `id_fase` int unsigned NOT NULL AUTO_INCREMENT,
  `nome` varchar(60) NOT NULL,
  `descricao` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_fase`),
  UNIQUE KEY `uq_fase_nome` (`nome`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fase`
--

LOCK TABLES `fase` WRITE;
/*!40000 ALTER TABLE `fase` DISABLE KEYS */;
INSERT INTO `fase` VALUES (1,'Menstrual','Fase de teste'),(2,'Folicular','Fase de teste'),(3,'Ovulatoria','Fase de teste'),(4,'Lutea','Fase de teste');
/*!40000 ALTER TABLE `fase` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fase_ciclo`
--

DROP TABLE IF EXISTS `fase_ciclo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fase_ciclo` (
  `id_ciclo` int unsigned NOT NULL,
  `id_fase` int unsigned NOT NULL,
  `data_inicio` date NOT NULL,
  `data_fim` date DEFAULT NULL,
  PRIMARY KEY (`id_ciclo`,`id_fase`),
  KEY `fk_fase_ciclo_fase` (`id_fase`),
  CONSTRAINT `fk_fase_ciclo_ciclo` FOREIGN KEY (`id_ciclo`) REFERENCES `ciclo_menstrual` (`id_ciclo`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_fase_ciclo_fase` FOREIGN KEY (`id_fase`) REFERENCES `fase` (`id_fase`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `ck_fase_ciclo_datas` CHECK (((`data_fim` is null) or (`data_fim` >= `data_inicio`)))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fase_ciclo`
--

LOCK TABLES `fase_ciclo` WRITE;
/*!40000 ALTER TABLE `fase_ciclo` DISABLE KEYS */;
INSERT INTO `fase_ciclo` VALUES (1,1,'2026-01-03','2026-01-07'),(1,2,'2026-01-08','2026-01-15'),(1,3,'2026-01-16','2026-01-18'),(1,4,'2026-01-19','2026-01-30'),(2,1,'2026-01-31','2026-02-04'),(2,2,'2026-02-05','2026-02-12'),(2,3,'2026-02-13','2026-02-15'),(2,4,'2026-02-16','2026-02-27'),(3,1,'2026-03-01','2026-03-05'),(3,2,'2026-03-06','2026-03-13'),(3,3,'2026-03-14','2026-03-16'),(3,4,'2026-03-17','2026-03-28'),(4,1,'2026-02-02','2026-02-06'),(4,2,'2026-02-07','2026-02-14'),(4,3,'2026-02-15','2026-02-17'),(4,4,'2026-02-18','2026-03-01'),(5,1,'2026-03-03','2026-03-07'),(5,2,'2026-03-08','2026-03-15'),(5,3,'2026-03-16','2026-03-18'),(5,4,'2026-03-19','2026-03-30'),(6,1,'2026-04-01','2026-04-05'),(6,2,'2026-04-06','2026-04-13'),(6,3,'2026-04-14','2026-04-16'),(6,4,'2026-04-17','2026-04-28'),(7,1,'2026-08-20','2026-08-24'),(7,2,'2026-08-25','2026-09-01'),(7,3,'2026-09-02','2026-09-04'),(7,4,'2026-09-05','2026-09-16'),(8,1,'2026-04-10','2026-04-14'),(8,2,'2026-04-15','2026-04-22'),(8,3,'2026-04-23','2026-04-25'),(8,4,'2026-04-26','2026-05-07'),(9,1,'2026-05-08','2026-05-12'),(9,2,'2026-05-13','2026-05-20'),(9,3,'2026-05-21','2026-05-23'),(9,4,'2026-05-24','2026-06-04'),(10,1,'2026-05-15','2026-05-19'),(10,2,'2026-05-20','2026-05-27'),(10,3,'2026-05-28','2026-05-30'),(10,4,'2026-05-31','2026-06-11');
/*!40000 ALTER TABLE `fase_ciclo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fluxo_menstrual`
--

DROP TABLE IF EXISTS `fluxo_menstrual`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fluxo_menstrual` (
  `id_ciclo` int unsigned NOT NULL,
  `data_registro` date NOT NULL,
  `intensidade` varchar(20) NOT NULL,
  `observacao` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_ciclo`,`data_registro`),
  CONSTRAINT `fk_fluxo_ciclo` FOREIGN KEY (`id_ciclo`) REFERENCES `ciclo_menstrual` (`id_ciclo`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `ck_fluxo_intensidade` CHECK ((`intensidade` in (_utf8mb4'Leve',_utf8mb4'Moderado',_utf8mb4'Intenso')))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fluxo_menstrual`
--

LOCK TABLES `fluxo_menstrual` WRITE;
/*!40000 ALTER TABLE `fluxo_menstrual` DISABLE KEYS */;
INSERT INTO `fluxo_menstrual` VALUES (1,'2026-01-03','Moderado','Registro fictício para teste'),(2,'2026-01-31','Intenso','Registro fictício para teste'),(3,'2026-03-01','Leve','Registro fictício para teste'),(4,'2026-02-02','Moderado','Registro fictício para teste'),(5,'2026-03-03','Intenso','Registro fictício para teste'),(6,'2026-04-01','Leve','Registro fictício para teste'),(7,'2026-08-20','Moderado','Registro fictício para teste'),(8,'2026-04-10','Intenso','Registro fictício para teste'),(9,'2026-05-08','Leve','Registro fictício para teste'),(10,'2026-05-15','Moderado','Registro fictício para teste');
/*!40000 ALTER TABLE `fluxo_menstrual` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `humor`
--

DROP TABLE IF EXISTS `humor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `humor` (
  `id_humor` int unsigned NOT NULL AUTO_INCREMENT,
  `id_usuario` int unsigned NOT NULL,
  `estado_humor` varchar(60) NOT NULL,
  `data_ocorrencia` date NOT NULL,
  PRIMARY KEY (`id_humor`),
  KEY `idx_humor_usuario` (`id_usuario`),
  KEY `idx_humor_data` (`data_ocorrencia`),
  CONSTRAINT `fk_humor_usuario` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id_usuario`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `humor`
--

LOCK TABLES `humor` WRITE;
/*!40000 ALTER TABLE `humor` DISABLE KEYS */;
INSERT INTO `humor` VALUES (1,1,'Estável','2026-01-10'),(2,1,'Irritada','2026-01-16'),(3,1,'Disposta','2026-02-10'),(4,2,'Ansiosa','2026-02-08'),(5,2,'Feliz','2026-02-20'),(6,2,'Cansada','2026-03-10'),(7,3,'Tranquila','2026-08-25'),(8,4,'Feliz','2026-04-18'),(9,4,'Irritada','2026-05-12'),(10,5,'Estável','2026-05-20');
/*!40000 ALTER TABLE `humor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `medicamento`
--

DROP TABLE IF EXISTS `medicamento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `medicamento` (
  `id_medicamento` int unsigned NOT NULL AUTO_INCREMENT,
  `nome` varchar(120) NOT NULL,
  `descricao` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_medicamento`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `medicamento`
--

LOCK TABLES `medicamento` WRITE;
/*!40000 ALTER TABLE `medicamento` DISABLE KEYS */;
INSERT INTO `medicamento` VALUES (1,'Medicamento A','Dado fictício para teste'),(2,'Medicamento B','Dado fictício para teste'),(3,'Medicamento C','Dado fictício para teste');
/*!40000 ALTER TABLE `medicamento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `registro_sintoma`
--

DROP TABLE IF EXISTS `registro_sintoma`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `registro_sintoma` (
  `id_registro_sintoma` int unsigned NOT NULL AUTO_INCREMENT,
  `id_ciclo` int unsigned NOT NULL,
  `id_sintoma` int unsigned NOT NULL,
  `intensidade` tinyint unsigned NOT NULL,
  `data_observacao` date NOT NULL,
  PRIMARY KEY (`id_registro_sintoma`),
  KEY `idx_registro_sintoma_ciclo` (`id_ciclo`),
  KEY `idx_registro_sintoma_sintoma` (`id_sintoma`),
  KEY `idx_registro_sintoma_data` (`data_observacao`),
  KEY `idx_registro_ciclo_data` (`id_ciclo`,`data_observacao`),
  CONSTRAINT `fk_registro_sintoma_ciclo` FOREIGN KEY (`id_ciclo`) REFERENCES `ciclo_menstrual` (`id_ciclo`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_registro_sintoma_sintoma` FOREIGN KEY (`id_sintoma`) REFERENCES `sintoma` (`id_sintoma`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `ck_registro_sintoma_intensidade` CHECK ((`intensidade` between 1 and 5))
) ENGINE=InnoDB AUTO_INCREMENT=147 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `registro_sintoma`
--

LOCK TABLES `registro_sintoma` WRITE;
/*!40000 ALTER TABLE `registro_sintoma` DISABLE KEYS */;
INSERT INTO `registro_sintoma` VALUES (1,1,1,5,'2026-01-04'),(2,1,2,2,'2026-01-05'),(3,1,4,3,'2026-01-08'),(4,2,1,3,'2026-02-01'),(5,2,5,2,'2026-02-03'),(6,3,1,5,'2026-03-02'),(7,3,7,3,'2026-03-04'),(8,4,2,3,'2026-02-03'),(9,4,4,2,'2026-02-05'),(10,5,1,4,'2026-03-04'),(11,5,3,2,'2026-03-05'),(12,6,5,3,'2026-04-03'),(13,7,1,4,'2026-08-21'),(14,7,6,2,'2026-08-22'),(15,8,7,2,'2026-04-11'),(16,9,1,3,'2026-05-09'),(17,10,4,3,'2026-05-16'),(18,1,1,3,'2026-01-03'),(19,1,1,4,'2026-01-04'),(20,1,2,4,'2026-01-03'),(21,1,2,5,'2026-01-04'),(22,1,3,5,'2026-01-03'),(23,1,3,1,'2026-01-04'),(24,1,4,1,'2026-01-03'),(25,1,4,2,'2026-01-04'),(26,1,5,2,'2026-01-03'),(27,1,5,3,'2026-01-04'),(28,1,6,3,'2026-01-03'),(29,1,6,4,'2026-01-04'),(30,1,7,4,'2026-01-03'),(31,1,7,5,'2026-01-04'),(32,2,1,4,'2026-01-31'),(33,2,1,5,'2026-02-01'),(34,2,2,5,'2026-01-31'),(35,2,2,1,'2026-02-01'),(36,2,3,1,'2026-01-31'),(37,2,3,2,'2026-02-01'),(38,2,4,2,'2026-01-31'),(39,2,4,3,'2026-02-01'),(40,2,5,3,'2026-01-31'),(41,2,5,4,'2026-02-01'),(42,2,6,4,'2026-01-31'),(43,2,6,5,'2026-02-01'),(44,2,7,5,'2026-01-31'),(45,2,7,1,'2026-02-01'),(46,3,1,5,'2026-03-01'),(47,3,1,1,'2026-03-02'),(48,3,2,1,'2026-03-01'),(49,3,2,2,'2026-03-02'),(50,3,3,2,'2026-03-01'),(51,3,3,3,'2026-03-02'),(52,3,4,3,'2026-03-01'),(53,3,4,4,'2026-03-02'),(54,3,5,4,'2026-03-01'),(55,3,5,5,'2026-03-02'),(56,3,6,5,'2026-03-01'),(57,3,6,1,'2026-03-02'),(58,3,7,1,'2026-03-01'),(59,3,7,2,'2026-03-02'),(60,4,1,1,'2026-02-02'),(61,4,1,2,'2026-02-03'),(62,4,2,2,'2026-02-02'),(63,4,2,3,'2026-02-03'),(64,4,3,3,'2026-02-02'),(65,4,3,4,'2026-02-03'),(66,4,4,4,'2026-02-02'),(67,4,4,5,'2026-02-03'),(68,4,5,5,'2026-02-02'),(69,4,5,1,'2026-02-03'),(70,4,6,1,'2026-02-02'),(71,4,6,2,'2026-02-03'),(72,4,7,2,'2026-02-02'),(73,4,7,3,'2026-02-03'),(74,5,1,2,'2026-03-03'),(75,5,1,3,'2026-03-04'),(76,5,2,3,'2026-03-03'),(77,5,2,4,'2026-03-04'),(78,5,3,4,'2026-03-03'),(79,5,3,5,'2026-03-04'),(80,5,4,5,'2026-03-03'),(81,5,4,1,'2026-03-04'),(82,5,5,1,'2026-03-03'),(83,5,5,2,'2026-03-04'),(84,5,6,2,'2026-03-03'),(85,5,6,3,'2026-03-04'),(86,5,7,3,'2026-03-03'),(87,5,7,4,'2026-03-04'),(88,6,1,3,'2026-04-01'),(89,6,1,4,'2026-04-02'),(90,6,2,4,'2026-04-01'),(91,6,2,5,'2026-04-02'),(92,6,3,5,'2026-04-01'),(93,6,3,1,'2026-04-02'),(94,6,4,1,'2026-04-01'),(95,6,4,2,'2026-04-02'),(96,6,5,2,'2026-04-01'),(97,6,5,3,'2026-04-02'),(98,6,6,3,'2026-04-01'),(99,6,6,4,'2026-04-02'),(100,6,7,4,'2026-04-01'),(145,1,1,3,'2026-10-26');
/*!40000 ALTER TABLE `registro_sintoma` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sintoma`
--

DROP TABLE IF EXISTS `sintoma`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sintoma` (
  `id_sintoma` int unsigned NOT NULL AUTO_INCREMENT,
  `nome` varchar(100) NOT NULL,
  `categoria` varchar(60) NOT NULL,
  PRIMARY KEY (`id_sintoma`),
  KEY `idx_sintoma_nome` (`nome`),
  KEY `idx_sintoma_categoria` (`categoria`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sintoma`
--

LOCK TABLES `sintoma` WRITE;
/*!40000 ALTER TABLE `sintoma` DISABLE KEYS */;
INSERT INTO `sintoma` VALUES (1,'Cólica','Dor'),(2,'Dor de cabeça','Dor'),(3,'Náusea','Gastrointestinal'),(4,'Cansaço','Físico'),(5,'Inchaço','Físico'),(6,'Sensibilidade nos seios','Físico'),(7,'Dor lombar','Dor');
/*!40000 ALTER TABLE `sintoma` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `uso_medicamento`
--

DROP TABLE IF EXISTS `uso_medicamento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `uso_medicamento` (
  `id_uso` int unsigned NOT NULL AUTO_INCREMENT,
  `id_usuario` int unsigned NOT NULL,
  `id_medicamento` int unsigned NOT NULL,
  `data_inicio` date NOT NULL,
  `data_fim` date DEFAULT NULL,
  `observacao` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_uso`),
  KEY `fk_uso_medicamento_usuario` (`id_usuario`),
  KEY `fk_uso_medicamento_medicamento` (`id_medicamento`),
  CONSTRAINT `fk_uso_medicamento_medicamento` FOREIGN KEY (`id_medicamento`) REFERENCES `medicamento` (`id_medicamento`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_uso_medicamento_usuario` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id_usuario`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `ck_uso_medicamento_datas` CHECK (((`data_fim` is null) or (`data_fim` >= `data_inicio`)))
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `uso_medicamento`
--

LOCK TABLES `uso_medicamento` WRITE;
/*!40000 ALTER TABLE `uso_medicamento` DISABLE KEYS */;
INSERT INTO `uso_medicamento` VALUES (1,1,1,'2026-06-01','2026-06-03','Registro fictício para teste'),(2,2,2,'2026-06-05',NULL,'Registro fictício para teste');
/*!40000 ALTER TABLE `uso_medicamento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuario` (
  `id_usuario` int unsigned NOT NULL AUTO_INCREMENT,
  `nome` varchar(120) NOT NULL,
  `email` varchar(254) NOT NULL,
  `data_cadastro` date NOT NULL,
  PRIMARY KEY (`id_usuario`),
  UNIQUE KEY `uq_usuario_email` (`email`),
  KEY `idx_usuario_nome` (`nome`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` VALUES (1,'Ana Oliveira','ana.oliveira@example.com','2026-01-10'),(2,'Mariana Costa','mariana.costa@example.com','2026-02-05'),(3,'Camila Souza','camila.souza@example.com','2026-02-20'),(4,'Juliana Santos','juliana.santos@example.com','2026-03-12'),(5,'Beatriz Lima','beatriz.lima@example.com','2026-04-01');
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `vw_ciclos_em_andamento`
--

DROP TABLE IF EXISTS `vw_ciclos_em_andamento`;
/*!50001 DROP VIEW IF EXISTS `vw_ciclos_em_andamento`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `vw_ciclos_em_andamento` AS SELECT 
 1 AS `nome_usuario`,
 1 AS `id_ciclo`,
 1 AS `data_inicio`,
 1 AS `duracao`*/;
SET character_set_client = @saved_cs_client;

--
-- Final view structure for view `vw_ciclos_em_andamento`
--

/*!50001 DROP VIEW IF EXISTS `vw_ciclos_em_andamento`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `vw_ciclos_em_andamento` AS select `u`.`nome` AS `nome_usuario`,`c`.`id_ciclo` AS `id_ciclo`,`c`.`data_inicio` AS `data_inicio`,`c`.`duracao` AS `duracao` from (`usuario` `u` join `ciclo_menstrual` `c` on((`c`.`id_usuario` = `u`.`id_usuario`))) where (`c`.`data_fim` is null) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-10-26 14:02:44
