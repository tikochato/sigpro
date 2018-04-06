-- MySQL dump 10.13  Distrib 5.7.12, for Win32 (AMD64)
--
-- Host: localhost    Database: sipro
-- ------------------------------------------------------
-- Server version	5.5.5-10.2.7-MariaDB

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
-- Table structure for table `actividad`
--

DROP TABLE IF EXISTS `actividad`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `actividad` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(1000) COLLATE utf8_bin NOT NULL,
  `descripcion` varchar(1000) COLLATE utf8_bin DEFAULT NULL,
  `fecha_inicio` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_fin` timestamp NOT NULL DEFAULT current_timestamp(),
  `porcentaje_avance` int(3) NOT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  `estado` int(2) NOT NULL,
  `actividad_tipoid` int(10) NOT NULL,
  `snip` bigint(10) DEFAULT NULL,
  `programa` int(4) DEFAULT NULL,
  `subprograma` int(4) DEFAULT NULL,
  `proyecto` int(4) DEFAULT NULL,
  `actividad` int(4) DEFAULT NULL,
  `obra` int(4) DEFAULT NULL,
  `objeto_id` int(11) NOT NULL,
  `objeto_tipo` int(2) NOT NULL,
  `duracion` int(10) NOT NULL DEFAULT 0,
  `duracion_dimension` varchar(1) COLLATE utf8_bin NOT NULL,
  `pred_objeto_id` int(11) DEFAULT NULL,
  `pred_objeto_tipo` int(2) DEFAULT NULL,
  `latitud` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `longitud` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `costo` decimal(15,2) DEFAULT 0.00,
  `acumulacion_costo` int(11) DEFAULT NULL,
  `renglon` int(4) DEFAULT NULL,
  `ubicacion_geografica` int(4) DEFAULT NULL,
  `orden` int(10) DEFAULT NULL,
  `treePath` varchar(1000) COLLATE utf8_bin DEFAULT NULL,
  `nivel` int(4) DEFAULT NULL,
  `proyecto_base` int(11) DEFAULT NULL,
  `componente_base` int(10) DEFAULT NULL,
  `producto_base` int(10) DEFAULT NULL,
  `fecha_inicio_real` timestamp NULL DEFAULT NULL,
  `fecha_fin_real` timestamp NULL DEFAULT NULL,
  `inversion_nueva` int(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `FKactividad818457` (`actividad_tipoid`),
  KEY `FKacumlacioncostoactividad` (`acumulacion_costo`),
  CONSTRAINT `FKactividad818457` FOREIGN KEY (`actividad_tipoid`) REFERENCES `actividad_tipo` (`id`),
  CONSTRAINT `FKacumlacioncostoactividad` FOREIGN KEY (`acumulacion_costo`) REFERENCES `acumulacion_costo` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `actividad`
--

LOCK TABLES `actividad` WRITE;
/*!40000 ALTER TABLE `actividad` DISABLE KEYS */;
/*!40000 ALTER TABLE `actividad` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER trigger_actividad_insert
AFTER INSERT ON sipro.actividad FOR EACH ROW
BEGIN
    DECLARE v_version int;
    SELECT max(a.version) INTO v_version FROM sipro_history.actividad a WHERE a.id=NEW.id;

    IF(v_version is null) THEN
        SET v_version=1;
    END IF;

    INSERT INTO sipro_history.actividad VALUE(NEW.id, NEW.nombre, NEW.descripcion, NEW.fecha_inicio, NEW.fecha_fin, NEW.porcentaje_avance, NEW.usuario_creo, NEW.usuario_actualizo, NEW.fecha_creacion, NEW.fecha_actualizacion, NEW.estado, NEW.actividad_tipoid, NEW.snip, NEW.programa, NEW.subprograma, NEW.proyecto, NEW.actividad, NEW.obra, NEW.objeto_id, NEW.objeto_tipo, NEW.duracion, NEW.duracion_dimension, pred_objeto_id,NEW.pred_objeto_tipo, NEW.latitud, NEW.longitud, NEW.costo, NEW.acumulacion_costo, NEW.renglon, NEW.ubicacion_geografica, NEW.orden, NEW.treePath, NEW.nivel, NEW.proyecto_base, NEW.componente_base, NEW.producto_base, NEW.fecha_inicio_real, NEW.fecha_fin_real, NEW.inversion_nueva, v_version, NULL, 1);

END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER trigger_actividad_update
AFTER UPDATE ON sipro.actividad FOR EACH ROW
BEGIN
        DECLARE v_version int;
        SELECT max(a.version) INTO v_version FROM sipro_history.actividad a WHERE a.id=OLD.id;

        IF(v_version is null) THEN
            UPDATE sipro_history.actividad SET actual=null WHERE id=OLD.id AND version is null;
            SET v_version=1;
        ELSE
            UPDATE sipro_history.actividad SET actual=null WHERE id=OLD.id AND version=v_version;
            SET v_version=v_version+1;
        END IF;

        INSERT INTO sipro_history.actividad VALUE(NEW.id, NEW.nombre, NEW.descripcion, NEW.fecha_inicio, NEW.fecha_fin, NEW.porcentaje_avance, NEW.usuario_creo, NEW.usuario_actualizo, NEW.fecha_creacion, NEW.fecha_actualizacion, NEW.estado, NEW.actividad_tipoid, NEW.snip, NEW.programa, NEW.subprograma, NEW.proyecto, NEW.actividad, NEW.obra, NEW.objeto_id, NEW.objeto_tipo, NEW.duracion, NEW.duracion_dimension, pred_objeto_id,NEW.pred_objeto_tipo, NEW.latitud, NEW.longitud, NEW.costo, NEW.acumulacion_costo, NEW.renglon, NEW.ubicacion_geografica, NEW.orden, NEW.treePath, NEW.nivel, NEW.proyecto_base, NEW.componente_base, NEW.producto_base, NEW.fecha_inicio_real, NEW.fecha_fin_real, NEW.inversion_nueva, v_version, NULL, 1);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER trigger_actividad_delete
BEFORE DELETE ON sipro.actividad FOR EACH ROW
BEGIN
    DECLARE v_version int;
    SELECT max(a.version) INTO v_version FROM sipro_history.actividad a WHERE a.id=OLD.id;

    IF(v_version is null) THEN
        SET v_version=1;
    ELSE
        UPDATE sipro_history.actividad SET actual=null WHERE id=OLD.id AND version=v_version;
        SET v_version=v_version+1;
    END IF;

    INSERT INTO sipro_history.actividad VALUE(OLD.id, OLD.nombre, OLD.descripcion, OLD.fecha_inicio, OLD.fecha_fin, OLD.porcentaje_avance, OLD.usuario_creo, OLD.usuario_actualizo, OLD.fecha_creacion, OLD.fecha_actualizacion, OLD.estado, OLD.actividad_tipoid, OLD.snip, OLD.programa, OLD.subprograma, OLD.proyecto, OLD.actividad, OLD.obra, OLD.objeto_id, OLD.objeto_tipo, OLD.duracion, OLD.duracion_dimension, pred_objeto_id,OLD.pred_objeto_tipo, OLD.latitud, OLD.longitud, OLD.costo, OLD.acumulacion_costo, OLD.renglon, OLD.ubicacion_geografica, OLD.orden, OLD.treePath, OLD.nivel, OLD.proyecto_base, OLD.componente_base, OLD.producto_base, OLD.fecha_inicio_real, OLD.fecha_fin_real, OLD.inversion_nueva, v_version, NULL, NULL);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `actividad_propiedad`
--

DROP TABLE IF EXISTS `actividad_propiedad`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `actividad_propiedad` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(200) COLLATE utf8_bin NOT NULL,
  `descripcion` varchar(1000) COLLATE utf8_bin DEFAULT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  `estado` int(2) NOT NULL,
  `dato_tipoid` int(10) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKactividad_557567` (`dato_tipoid`),
  CONSTRAINT `FKactividad_557567` FOREIGN KEY (`dato_tipoid`) REFERENCES `dato_tipo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `actividad_propiedad`
--

LOCK TABLES `actividad_propiedad` WRITE;
/*!40000 ALTER TABLE `actividad_propiedad` DISABLE KEYS */;
/*!40000 ALTER TABLE `actividad_propiedad` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `actividad_propiedad_valor`
--

DROP TABLE IF EXISTS `actividad_propiedad_valor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `actividad_propiedad_valor` (
  `actividadid` int(10) NOT NULL,
  `actividad_propiedadid` int(10) NOT NULL,
  `valor_entero` int(10) DEFAULT NULL,
  `valor_string` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `valor_decimal` decimal(15,2) DEFAULT NULL,
  `valor_tiempo` timestamp NULL DEFAULT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NULL DEFAULT NULL,
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  `estado` int(2) DEFAULT NULL,
  PRIMARY KEY (`actividadid`,`actividad_propiedadid`),
  KEY `FKactividad_3878` (`actividad_propiedadid`),
  KEY `FKactividad_548986` (`actividadid`),
  CONSTRAINT `FKactividad_3878` FOREIGN KEY (`actividad_propiedadid`) REFERENCES `actividad_propiedad` (`id`),
  CONSTRAINT `FKactividad_548986` FOREIGN KEY (`actividadid`) REFERENCES `actividad` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `actividad_propiedad_valor`
--

LOCK TABLES `actividad_propiedad_valor` WRITE;
/*!40000 ALTER TABLE `actividad_propiedad_valor` DISABLE KEYS */;
/*!40000 ALTER TABLE `actividad_propiedad_valor` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER trigger_act_prop_val_insert
AFTER INSERT ON sipro.actividad_propiedad_valor FOR EACH ROW
BEGIN
    DECLARE v_version int;
    SELECT max(a.version) INTO v_version FROM sipro_history.actividad_propiedad_valor a WHERE a.actividadid=NEW.actividadid AND a.actividad_propiedadid = NEW.actividad_propiedadid;

    IF(v_version is null) THEN
        SET v_version=1;
    END IF;

    INSERT INTO sipro_history.actividad_propiedad_valor
    VALUE(NEW.actividadid, NEW.actividad_propiedadid, NEW.valor_entero, NEW.valor_string, NEW.valor_decimal,
        NEW.valor_tiempo, NEW.usuario_creo, NEW.usuario_actualizo, NEW.fecha_creacion, NEW.fecha_actualizacion, NEW.estado,
         v_version, NULL, 1);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER trigger_act_prop_val_update
AFTER UPDATE ON sipro.actividad_propiedad_valor FOR EACH ROW
BEGIN
DECLARE v_version int;
SELECT max(a.version) INTO v_version FROM sipro_history.actividad_propiedad_valor a WHERE a.actividadid=OLD.actividadid AND a.actividad_propiedadid = OLD.actividad_propiedadid;

IF(v_version is null) THEN
    UPDATE sipro_history.actividad_propiedad_valor a SET actual=null WHERE a.actividadid=OLD.actividadid AND a.actividad_propiedadid = OLD.actividad_propiedadid AND version is null;
    SET v_version=1;
ELSE
    UPDATE sipro_history.actividad_propiedad_valor a SET actual=null WHERE a.actividadid=OLD.actividadid AND a.actividad_propiedadid = OLD.actividad_propiedadid AND version=v_version;
    SET v_version=v_version+1;
END IF;

INSERT INTO sipro_history.actividad_propiedad_valor
    VALUE(NEW.actividadid, NEW.actividad_propiedadid, NEW.valor_entero, NEW.valor_string, NEW.valor_decimal,
    NEW.valor_tiempo, NEW.usuario_creo, NEW.usuario_actualizo, NEW.fecha_creacion, NEW.fecha_actualizacion, NEW.estado,
   v_version, NULL, 1);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER trigger_act_prop_val_delete
BEFORE DELETE ON sipro.actividad_propiedad_valor FOR EACH ROW
BEGIN
    DECLARE v_version int;
    SELECT max(a.version) INTO v_version FROM sipro_history.actividad_propiedad_valor a WHERE a.actividadid=OLD.actividadid AND a.actividad_propiedadid = OLD.actividad_propiedadid;

    IF(v_version is null) THEN
        SET v_version=1;
    ELSE
        UPDATE sipro_history.actividad_propiedad_valor a SET actual=null WHERE a.actividadid=OLD.actividadid AND a.actividad_propiedadid = OLD.actividad_propiedadid AND version=v_version;
        SET v_version=v_version+1;
    END IF;

    INSERT INTO sipro_history.actividad_propiedad_valor
    VALUE(OLD.actividadid, OLD.actividad_propiedadid, OLD.valor_entero, OLD.valor_string, OLD.valor_decimal,
    OLD.valor_tiempo, OLD.usuario_creo, OLD.usuario_actualizo, OLD.fecha_creacion, OLD.fecha_actualizacion, OLD.estado,
      v_version, NULL, NULL);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `actividad_tipo`
--

DROP TABLE IF EXISTS `actividad_tipo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `actividad_tipo` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(200) COLLATE utf8_bin NOT NULL,
  `descripcion` varchar(1000) COLLATE utf8_bin DEFAULT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  `estado` int(2) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `actividad_tipo`
--

LOCK TABLES `actividad_tipo` WRITE;
/*!40000 ALTER TABLE `actividad_tipo` DISABLE KEYS */;
INSERT INTO `actividad_tipo` VALUES (1,'General',NULL,'admin',NULL,'2017-09-30 11:52:18',NULL,1);
/*!40000 ALTER TABLE `actividad_tipo` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER trigger_actividad_tipo_insert
AFTER INSERT ON sipro.actividad_tipo FOR EACH ROW
BEGIN
  DECLARE v_version int;
  SELECT max(a.version) INTO v_version FROM sipro_history.actividad_tipo a
  WHERE a.id=NEW.id;

  IF(v_version is null) THEN
      SET v_version=1;
  END IF;

  INSERT INTO sipro_history.actividad_tipo
  VALUE(NEW.id, NEW.nombre, NEW.descripcion,  NEW.usuario_creo, NEW.usuario_actualizo,
     NEW.fecha_creacion, NEW.fecha_actualizacion, NEW.estado,
     v_version, NULL, 1);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER trigger_actividad_tipo_update
AFTER UPDATE ON sipro.actividad_tipo FOR EACH ROW
BEGIN
        DECLARE v_version int;
        SELECT max(a.version) INTO v_version FROM sipro_history.actividad_tipo a WHERE a.id=OLD.id;

        IF(v_version is null) THEN
            UPDATE sipro_history.actividad_tipo SET actual=null WHERE id=OLD.id AND version is null;
            SET v_version=1;
        ELSE
            UPDATE sipro_history.actividad_tipo SET actual=null WHERE id=OLD.id AND version=v_version;
            SET v_version=v_version+1;
        END IF;

        INSERT INTO sipro_history.actividad_tipo VALUE(NEW.id, NEW.nombre, NEW.descripcion,  NEW.usuario_creo, NEW.usuario_actualizo,
           NEW.fecha_creacion, NEW.fecha_actualizacion, NEW.estado,
            v_version, NULL, 1);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER trigger_actividad_tipo_delete
BEFORE DELETE ON sipro.actividad_tipo FOR EACH ROW
BEGIN
    DECLARE v_version int;
    SELECT max(a.version) INTO v_version FROM sipro_history.actividad_tipo a WHERE a.id=OLD.id;

    IF(v_version is null) THEN
        SET v_version=1;
    ELSE
        UPDATE sipro_history.actividad_tipo SET actual=null WHERE id=OLD.id AND version=v_version;
        SET v_version=v_version+1;
    END IF;

    INSERT INTO sipro_history.actividad_tipo VALUE(OLD.id, OLD.nombre, OLD.descripcion,
      OLD.usuario_creo, OLD.usuario_actualizo,OLD.fecha_creacion, OLD.fecha_actualizacion, OLD.estado,
       v_version, NULL, NULL);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `actividad_usuario`
--

DROP TABLE IF EXISTS `actividad_usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `actividad_usuario` (
  `actividadid` int(10) NOT NULL,
  `usuario` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NULL DEFAULT NULL,
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`actividadid`,`usuario`),
  KEY `FKactividad_980179` (`actividadid`),
  CONSTRAINT `FKactividad_980179` FOREIGN KEY (`actividadid`) REFERENCES `actividad` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `actividad_usuario`
--

LOCK TABLES `actividad_usuario` WRITE;
/*!40000 ALTER TABLE `actividad_usuario` DISABLE KEYS */;
/*!40000 ALTER TABLE `actividad_usuario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `acumulacion_costo`
--

DROP TABLE IF EXISTS `acumulacion_costo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `acumulacion_costo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) COLLATE utf8_bin NOT NULL,
  `usuario_creo` varchar(45) COLLATE utf8_bin NOT NULL,
  `usuario_actualizo` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  `estado` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `acumulacion_costo`
--

LOCK TABLES `acumulacion_costo` WRITE;
/*!40000 ALTER TABLE `acumulacion_costo` DISABLE KEYS */;
INSERT INTO `acumulacion_costo` VALUES (1,'Inicio','admin',NULL,'2017-10-03 23:19:45',NULL,1),(2,'Prorrateo','admin',NULL,'2017-10-03 23:19:45',NULL,1),(3,'Fin','admin',NULL,'2017-10-03 23:19:45',NULL,1);
/*!40000 ALTER TABLE `acumulacion_costo` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER trigger_acum_costo_insert
AFTER INSERT ON sipro.acumulacion_costo FOR EACH ROW
BEGIN
  DECLARE v_version int;
  SELECT max(a.version) INTO v_version FROM sipro_history.acumulacion_costo a
  WHERE a.id=NEW.id;

  IF(v_version is null) THEN
      SET v_version=1;
  END IF;

  INSERT INTO sipro_history.acumulacion_costo
  VALUE(NEW.id, NEW.nombre,  NEW.usuario_creo, NEW.usuario_actualizo,
     NEW.fecha_creacion, NEW.fecha_actualizacion, NEW.estado,
     v_version, NULL, 1);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER trigger_acum_costo_update
AFTER UPDATE ON sipro.acumulacion_costo FOR EACH ROW
BEGIN
        DECLARE v_version int;
        SELECT max(a.version) INTO v_version FROM sipro_history.acumulacion_costo a WHERE a.id=OLD.id;

        IF(v_version is null) THEN
            UPDATE sipro_history.acumulacion_costo SET actual=null WHERE id=OLD.id AND version is null;
            SET v_version=1;
        ELSE
            UPDATE sipro_history.acumulacion_costo SET actual=null WHERE id=OLD.id AND version=v_version;
            SET v_version=v_version+1;
        END IF;

        INSERT INTO sipro_history.acumulacion_costo
        VALUE(NEW.id, NEW.nombre,  NEW.usuario_creo, NEW.usuario_actualizo,
           NEW.fecha_creacion, NEW.fecha_actualizacion, NEW.estado,
           v_version, NULL, 1);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER trigger_acum_costo_delete
BEFORE DELETE ON sipro.acumulacion_costo FOR EACH ROW
BEGIN
    DECLARE v_version int;
    SELECT max(a.version) INTO v_version FROM sipro_history.acumulacion_costo a WHERE a.id=OLD.id;

    IF(v_version is null) THEN
        SET v_version=1;
    ELSE
        UPDATE sipro_history.acumulacion_costo SET actual=null WHERE id=OLD.id AND version=v_version;
        SET v_version=v_version+1;
    END IF;

    INSERT INTO sipro_history.acumulacion_costo
    VALUE(OLD.id, OLD.nombre,  OLD.usuario_creo, OLD.usuario_actualizo,
       OLD.fecha_creacion, OLD.fecha_actualizacion, OLD.estado,
       v_version, NULL, NULL);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `asignacion_raci`
--

DROP TABLE IF EXISTS `asignacion_raci`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `asignacion_raci` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `colaboradorid` int(10) NOT NULL,
  `rol_raci` varchar(1) COLLATE utf8_bin NOT NULL,
  `objeto_id` int(11) NOT NULL,
  `objeto_tipo` int(11) NOT NULL,
  `estado` int(2) NOT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_asignacion_raci_colaborador_idx` (`colaboradorid`),
  KEY `fk_asignacion_raci_colaborador` (`colaboradorid`),
  CONSTRAINT `fk_asignacion_raci_colaborador` FOREIGN KEY (`colaboradorid`) REFERENCES `colaborador` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `asignacion_raci`
--

LOCK TABLES `asignacion_raci` WRITE;
/*!40000 ALTER TABLE `asignacion_raci` DISABLE KEYS */;
/*!40000 ALTER TABLE `asignacion_raci` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER trigger_asigna_raci_insert
AFTER INSERT ON sipro.asignacion_raci FOR EACH ROW
BEGIN
  DECLARE v_version int;
  SELECT max(a.version) INTO v_version FROM sipro_history.asignacion_raci a WHERE a.colaboradorid=NEW.colaboradorid AND a.rol_raci=NEW.rol_raci AND a.objeto_tipo=NEW.objeto_tipo AND a.objeto_id=NEW.objeto_id;

  IF(v_version is null) THEN
      SET v_version=1;
  END IF;

  INSERT INTO sipro_history.asignacion_raci VALUE(NEW.id, NEW.colaboradorid,NEW.rol_raci,NEW.objeto_id, NEW.objeto_tipo,  NEW.estado, NEW.usuario_creo, NEW.usuario_actualizo, NEW.fecha_creacion, NEW.fecha_actualizacion, v_version, NULL, 1);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER trigger_asigna_raci_update
AFTER UPDATE ON sipro.asignacion_raci FOR EACH ROW
BEGIN
        DECLARE v_version int;
        SELECT max(a.version) INTO v_version FROM sipro_history.asignacion_raci a WHERE a.colaboradorid=NEW.colaboradorid AND a.rol_raci=NEW.rol_raci AND a.objeto_tipo=NEW.objeto_tipo AND a.objeto_id=NEW.objeto_id;

        IF(v_version is null) THEN
            UPDATE sipro_history.asignacion_raci a SET actual=null WHERE a.colaboradorid=NEW.colaboradorid AND a.rol_raci=NEW.rol_raci AND a.objeto_tipo=NEW.objeto_tipo AND a.objeto_id=NEW.objeto_id AND version is null;
            SET v_version=1;
        ELSE
            UPDATE sipro_history.asignacion_raci a SET actual=null WHERE a.colaboradorid=NEW.colaboradorid AND a.rol_raci=NEW.rol_raci AND a.objeto_tipo=NEW.objeto_tipo AND a.objeto_id=NEW.objeto_id AND version=v_version;
            SET v_version=v_version+1;
        END IF;

        INSERT INTO sipro_history.asignacion_raci
        VALUE(NEW.id, NEW.colaboradorid,NEW.rol_raci,NEW.objeto_id,
          NEW.objeto_tipo,  NEW.estado, NEW.usuario_creo, NEW.usuario_actualizo,
          NEW.fecha_creacion, NEW.fecha_actualizacion,
           v_version, NULL, 1);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER trigger_asigna_raci_delete
BEFORE DELETE ON sipro.asignacion_raci FOR EACH ROW
BEGIN
    DECLARE v_version int;
    SELECT max(a.version) INTO v_version FROM sipro_history.asignacion_raci a WHERE a.colaboradorid=OLD.colaboradorid AND a.rol_raci=OLD.rol_raci AND a.objeto_tipo=OLD.objeto_tipo AND a.objeto_id=OLD.objeto_id;

    IF(v_version is null) THEN
        SET v_version=1;
    ELSE
        UPDATE sipro_history.asignacion_raci a SET actual=null WHERE a.colaboradorid=OLD.colaboradorid AND a.rol_raci=OLD.rol_raci AND a.objeto_tipo=OLD.objeto_tipo AND a.objeto_id=OLD.objeto_id AND version=v_version;
        SET v_version=v_version+1;
    END IF;

    INSERT INTO sipro_history.asignacion_raci
    VALUE(OLD.id, OLD.colaboradorid,OLD.rol_raci,OLD.objeto_id,
      OLD.objeto_tipo,  OLD.estado, OLD.usuario_creo, OLD.usuario_actualizo,
      OLD.fecha_creacion, OLD.fecha_actualizacion,
       v_version, NULL, NULL);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `atipo_propiedad`
--

DROP TABLE IF EXISTS `atipo_propiedad`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `atipo_propiedad` (
  `actividad_tipoid` int(10) NOT NULL,
  `actividad_propiedadid` int(10) NOT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`actividad_tipoid`,`actividad_propiedadid`),
  KEY `FKatipo_prop264327` (`actividad_propiedadid`),
  KEY `FKatipo_prop395442` (`actividad_tipoid`),
  CONSTRAINT `FKatipo_prop264327` FOREIGN KEY (`actividad_propiedadid`) REFERENCES `actividad_propiedad` (`id`),
  CONSTRAINT `FKatipo_prop395442` FOREIGN KEY (`actividad_tipoid`) REFERENCES `actividad_tipo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `atipo_propiedad`
--

LOCK TABLES `atipo_propiedad` WRITE;
/*!40000 ALTER TABLE `atipo_propiedad` DISABLE KEYS */;
/*!40000 ALTER TABLE `atipo_propiedad` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `autorizacion_tipo`
--

DROP TABLE IF EXISTS `autorizacion_tipo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `autorizacion_tipo` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) COLLATE utf8_bin NOT NULL,
  `descripcion` varchar(1000) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `autorizacion_tipo`
--

LOCK TABLES `autorizacion_tipo` WRITE;
/*!40000 ALTER TABLE `autorizacion_tipo` DISABLE KEYS */;
/*!40000 ALTER TABLE `autorizacion_tipo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categoria_adquisicion`
--

DROP TABLE IF EXISTS `categoria_adquisicion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `categoria_adquisicion` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) COLLATE utf8_bin NOT NULL,
  `descripcion` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  `estado` int(10) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categoria_adquisicion`
--

LOCK TABLES `categoria_adquisicion` WRITE;
/*!40000 ALTER TABLE `categoria_adquisicion` DISABLE KEYS */;
INSERT INTO `categoria_adquisicion` VALUES (1,'Automóviles','Automóviles','admin',NULL,'2017-10-02 14:17:58',NULL,1),(2,'Obras',NULL,'admin',NULL,'2017-10-03 03:50:35',NULL,1),(3,'Bienes',NULL,'admin',NULL,'2017-10-03 03:50:35',NULL,1),(4,'Firmas Consultoras',NULL,'admin',NULL,'2017-10-03 03:50:35',NULL,1),(5,'Servicios de No Consultoría',NULL,'admin',NULL,'2017-10-03 03:50:35',NULL,1),(6,'Consultores Individuales',NULL,'admin',NULL,'2017-10-03 03:50:35',NULL,1),(7,'Capacitaciones',NULL,'admin',NULL,'2017-10-03 03:50:35',NULL,1),(8,'Transferencias',NULL,'admin',NULL,'2017-10-03 03:50:35',NULL,1),(9,'Sub Proyectos',NULL,'admin',NULL,'2017-10-03 03:50:35',NULL,1),(10,'Gastos Operativos',NULL,'admin',NULL,'2017-10-03 03:50:35',NULL,1);
/*!40000 ALTER TABLE `categoria_adquisicion` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER trigger_cat_adqu_insert
AFTER INSERT ON sipro.categoria_adquisicion FOR EACH ROW
BEGIN
DECLARE v_version int;
SELECT max(a.version) INTO v_version FROM sipro_history.categoria_adquisicion a
WHERE a.id=NEW.id;

IF(v_version is null) THEN
    SET v_version=1;
END IF;

INSERT INTO sipro_history.categoria_adquisicion
VALUE(NEW.id, NEW.nombre, NEW.descripcion,
  NEW.usuario_creo, NEW.usuario_actualizo,
  NEW.fecha_creacion, NEW.fecha_actualizacion, NEW.estado,
  v_version, NULL, 1);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER trigger_cat_adqu_update
AFTER UPDATE ON sipro.categoria_adquisicion FOR EACH ROW
BEGIN
        DECLARE v_version int;
        SELECT max(a.version) INTO v_version FROM sipro_history.categoria_adquisicion a WHERE a.id=OLD.id;

        IF(v_version is null) THEN
            UPDATE sipro_history.categoria_adquisicion SET actual=null WHERE id=OLD.id AND version is null;
            SET v_version=1;
        ELSE
            UPDATE sipro_history.categoria_adquisicion SET actual=null WHERE id=OLD.id AND version=v_version;
            SET v_version=v_version+1;
        END IF;

        INSERT INTO sipro_history.categoria_adquisicion
        VALUE(NEW.id, NEW.nombre, NEW.descripcion,
          NEW.usuario_creo, NEW.usuario_actualizo,
          NEW.fecha_creacion, NEW.fecha_actualizacion, NEW.estado,
           v_version, NULL, 1);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER trigger_cat_adqu_delete
BEFORE DELETE ON sipro.categoria_adquisicion FOR EACH ROW
BEGIN
    DECLARE v_version int;
    SELECT max(a.version) INTO v_version FROM sipro_history.categoria_adquisicion a WHERE a.id=OLD.id;

    IF(v_version is null) THEN
        SET v_version=1;
    ELSE
        UPDATE sipro_history.categoria_adquisicion SET actual=null WHERE id=OLD.id AND version=v_version;
        SET v_version=v_version+1;
    END IF;

    INSERT INTO sipro_history.asignacion_raci
    VALUE(OLD.id, OLD.nombre, OLD.descripcion,
      OLD.usuario_creo, OLD.usuario_actualizo,
      OLD.fecha_creacion, OLD.fecha_actualizacion, OLD.estado,
       v_version, NULL, NULL);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `colaborador`
--

DROP TABLE IF EXISTS `colaborador`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `colaborador` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `pnombre` varchar(255) COLLATE utf8_bin NOT NULL,
  `snombre` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `papellido` varchar(255) COLLATE utf8_bin NOT NULL,
  `sapellido` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `cui` bigint(15) NOT NULL,
  `unidad_ejecutoraunidad_ejecutora` int(10) NOT NULL,
  `usuariousuario` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `estado` int(1) NOT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  `entidad` int(10) NOT NULL,
  `ejercicio` int(4) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_colaborador_15_idx` (`unidad_ejecutoraunidad_ejecutora`,`entidad`,`ejercicio`),
  KEY `fk_colaborador_15` (`unidad_ejecutoraunidad_ejecutora`,`entidad`,`ejercicio`),
  KEY `FKcolaborado544592` (`usuariousuario`),
  CONSTRAINT `FKcolaborado544592` FOREIGN KEY (`usuariousuario`) REFERENCES `usuario` (`usuario`),
  CONSTRAINT `fk_colaborador_15` FOREIGN KEY (`unidad_ejecutoraunidad_ejecutora`, `entidad`, `ejercicio`) REFERENCES `unidad_ejecutora` (`unidad_ejecutora`, `entidadentidad`, `ejercicio`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `colaborador`
--

LOCK TABLES `colaborador` WRITE;
/*!40000 ALTER TABLE `colaborador` DISABLE KEYS */;
/*!40000 ALTER TABLE `colaborador` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER trigger_colaborador_insert
AFTER INSERT ON sipro.colaborador FOR EACH ROW
BEGIN
    DECLARE v_version int;
    SELECT max(a.version) INTO v_version FROM sipro_history.colaborador a
    WHERE a.id=NEW.id;

    IF(v_version is null) THEN
        SET v_version=1;
    END IF;

    INSERT INTO sipro_history.colaborador
    VALUE(NEW.id, NEW.pnombre, NEW.snombre,NEW.papellido,NEW.sapellido,
      NEW.cui,NEW.unidad_ejecutoraunidad_ejecutora,NEW.usuariousuario,
      NEW.estado,NEW.usuario_creo, NEW.usuario_actualizo,
      NEW.fecha_creacion, NEW.fecha_actualizacion, NEW.entidad,NEW.ejercicio,
      v_version, NULL, 1);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER trigger_colaborador_update
AFTER UPDATE ON sipro.colaborador FOR EACH ROW
BEGIN
    DECLARE v_version int;
    SELECT max(a.version) INTO v_version FROM sipro_history.colaborador a WHERE a.id=OLD.id;

    IF(v_version is null) THEN
        UPDATE sipro_history.colaborador SET actual=null WHERE id=OLD.id AND version is null;
        SET v_version=1;
    ELSE
        UPDATE sipro_history.colaborador SET actual=null WHERE id=OLD.id AND version=v_version;
        SET v_version=v_version+1;
    END IF;

    INSERT INTO sipro_history.colaborador
    VALUE(NEW.id, NEW.pnombre, NEW.snombre,NEW.papellido,NEW.sapellido,
      NEW.cui,NEW.unidad_ejecutoraunidad_ejecutora,NEW.usuariousuario,
      NEW.estado,NEW.usuario_creo, NEW.usuario_actualizo,
      NEW.fecha_creacion, NEW.fecha_actualizacion, NEW.entidad,NEW.ejercicio,
       v_version, NULL, 1);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER trigger_colaborador_delete
BEFORE DELETE ON sipro.colaborador FOR EACH ROW
BEGIN
    DECLARE v_version int;
    SELECT max(a.version) INTO v_version FROM sipro_history.colaborador a WHERE a.id=OLD.id;

    IF(v_version is null) THEN
        SET v_version=1;
    ELSE
        UPDATE sipro_history.colaborador SET actual=null WHERE id=OLD.id AND version=v_version;
        SET v_version=v_version+1;
    END IF;

    INSERT INTO sipro_history.colaborador
    VALUE(OLD.id, OLD.pnombre, OLD.snombre,OLD.papellido,OLD.sapellido,
      OLD.cui,OLD.unidad_ejecutoraunidad_ejecutora,OLD.usuariousuario,
      OLD.estado,OLD.usuario_creo, OLD.usuario_actualizo,
      OLD.fecha_creacion, OLD.fecha_actualizacion, OLD.entidad,OLD.ejercicio,
       v_version, NULL, NULL);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `componente`
--

DROP TABLE IF EXISTS `componente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `componente` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(1000) COLLATE utf8_bin NOT NULL,
  `descripcion` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `proyectoid` int(10) NOT NULL,
  `componente_tipoid` int(10) NOT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  `estado` int(2) NOT NULL,
  `unidad_ejecutoraunidad_ejecutora` int(10) DEFAULT NULL,
  `snip` bigint(10) DEFAULT NULL,
  `programa` int(4) DEFAULT NULL,
  `subprograma` int(4) DEFAULT NULL,
  `proyecto` int(4) DEFAULT NULL,
  `actividad` int(4) DEFAULT NULL,
  `obra` int(4) DEFAULT NULL,
  `latitud` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `longitud` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `costo` decimal(15,2) DEFAULT NULL,
  `acumulacion_costoid` int(11) DEFAULT NULL,
  `renglon` int(4) DEFAULT NULL,
  `ubicacion_geografica` int(4) DEFAULT NULL,
  `fecha_inicio` timestamp NULL DEFAULT NULL,
  `fecha_fin` timestamp NULL DEFAULT NULL,
  `duracion` int(10) NOT NULL DEFAULT 0,
  `duracion_dimension` varchar(1) COLLATE utf8_bin DEFAULT NULL,
  `orden` int(10) DEFAULT NULL,
  `treePath` varchar(1000) COLLATE utf8_bin DEFAULT NULL,
  `nivel` int(4) DEFAULT NULL,
  `entidad` int(10) DEFAULT NULL,
  `ejercicio` int(4) DEFAULT NULL,
  `es_de_sigade` int(1) DEFAULT NULL,
  `fuente_prestamo` decimal(15,2) DEFAULT NULL,
  `fuente_donacion` decimal(15,2) DEFAULT NULL,
  `fuente_nacional` decimal(15,2) DEFAULT NULL,
  `componente_sigadeid` int(10) DEFAULT NULL,
  `fecha_inicio_real` timestamp NULL DEFAULT NULL,
  `fecha_fin_real` timestamp NULL DEFAULT NULL,
  `inversion_nueva` int(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `fkacumulacioncosto_idx` (`acumulacion_costoid`),
  KEY `fk_componente_15_idx` (`unidad_ejecutoraunidad_ejecutora`,`entidad`,`ejercicio`),
  KEY `fk_componente_15` (`unidad_ejecutoraunidad_ejecutora`,`entidad`,`ejercicio`),
  KEY `fkacumulacioncosto1` (`acumulacion_costoid`),
  KEY `FKcomponente380208` (`proyectoid`),
  KEY `FKcomponente53483` (`componente_tipoid`),
  KEY `fk_componente_sigade_idx` (`componente_sigadeid`) USING BTREE,
  CONSTRAINT `FKcomponente380208` FOREIGN KEY (`proyectoid`) REFERENCES `proyecto` (`id`),
  CONSTRAINT `FKcomponente53483` FOREIGN KEY (`componente_tipoid`) REFERENCES `componente_tipo` (`id`),
  CONSTRAINT `fk_componente_15` FOREIGN KEY (`unidad_ejecutoraunidad_ejecutora`, `entidad`, `ejercicio`) REFERENCES `unidad_ejecutora` (`unidad_ejecutora`, `entidadentidad`, `ejercicio`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_componente_sigade` FOREIGN KEY (`componente_sigadeid`) REFERENCES `componente_sigade` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fkacumulacioncosto1` FOREIGN KEY (`acumulacion_costoid`) REFERENCES `acumulacion_costo` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `componente`
--

LOCK TABLES `componente` WRITE;
/*!40000 ALTER TABLE `componente` DISABLE KEYS */;
/*!40000 ALTER TABLE `componente` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER trigger_componente_insert
AFTER INSERT ON sipro.componente FOR EACH ROW
BEGIN
    DECLARE v_version int;
    SELECT max(a.version) INTO v_version FROM sipro_history.componente a
    WHERE a.id=NEW.id;

    IF(v_version is null) THEN
        SET v_version=1;
    END IF;

    INSERT INTO sipro_history.componente
    VALUE(NEW.id, NEW.nombre, NEW.descripcion,NEW.proyectoid,
      NEW.componente_tipoid, NEW.usuario_creo, NEW.usuario_actualizo,
      NEW.fecha_creacion, NEW.fecha_actualizacion,NEW.estado,
      NEW.unidad_ejecutoraunidad_ejecutora, NEW.snip,
      NEW.programa,NEW.subprograma,NEW.proyecto,NEW.actividad,NEW.obra,
      NEW.latitud,NEW.longitud,NEW.costo,NEW.acumulacion_costoid,
      NEW.renglon,NEW.ubicacion_geografica,NEW.fecha_inicio,NEW.fecha_fin,
      NEW.duracion,NEW.duracion_dimension,NEW.orden,NEW.treePath,NEW.nivel,
      NEW.entidad,NEW.ejercicio,NEW.es_de_sigade,NEW.fuente_prestamo,
      NEW.fuente_donacion,NEW.fuente_nacional,NEW.componente_sigadeid,
      NEW.fecha_inicio_real,NEW.fecha_fin_real, NEW.inversion_nueva,
      v_version, NULL, 1);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER trigger_componente_update
AFTER UPDATE ON sipro.componente FOR EACH ROW
BEGIN
  DECLARE v_version int;
  SELECT max(a.version) INTO v_version FROM sipro_history.componente a WHERE a.id=OLD.id;

  IF(v_version is null) THEN
      UPDATE sipro_history.componente SET actual=null WHERE id=OLD.id AND version is null;
      SET v_version=1;
  ELSE
      UPDATE sipro_history.componente SET actual=null WHERE id=OLD.id AND version=v_version;
      SET v_version=v_version+1;
  END IF;

  INSERT INTO sipro_history.componente
  VALUE(NEW.id, NEW.nombre, NEW.descripcion,NEW.proyectoid,
    NEW.componente_tipoid, NEW.usuario_creo, NEW.usuario_actualizo,
    NEW.fecha_creacion, NEW.fecha_actualizacion,NEW.estado,
    NEW.unidad_ejecutoraunidad_ejecutora, NEW.snip,
    NEW.programa,NEW.subprograma,NEW.proyecto,NEW.actividad,NEW.obra,
    NEW.latitud,NEW.longitud,NEW.costo,NEW.acumulacion_costoid,
    NEW.renglon,NEW.ubicacion_geografica,NEW.fecha_inicio,NEW.fecha_fin,
    NEW.duracion,NEW.duracion_dimension,NEW.orden,NEW.treePath,NEW.nivel,
    NEW.entidad,NEW.ejercicio,NEW.es_de_sigade,NEW.fuente_prestamo,
    NEW.fuente_donacion,NEW.fuente_nacional,NEW.componente_sigadeid,
    NEW.fecha_inicio_real,NEW.fecha_fin_real, NEW.inversion_nueva,
     v_version, NULL, 1);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER trigger_componente_delete
BEFORE DELETE ON sipro.componente FOR EACH ROW
BEGIN
    DECLARE v_version int;
    SELECT max(a.version) INTO v_version FROM sipro_history.componente a WHERE a.id=OLD.id;

    IF(v_version is null) THEN
        SET v_version=1;
    ELSE
        UPDATE sipro_history.componente SET actual=null WHERE id=OLD.id AND version=v_version;
        SET v_version=v_version+1;
    END IF;

    INSERT INTO sipro_history.componente
    VALUE(OLD.id, OLD.nombre, OLD.descripcion,OLD.proyectoid,
      OLD.componente_tipoid, OLD.usuario_creo, OLD.usuario_actualizo,
      OLD.fecha_creacion, OLD.fecha_actualizacion,OLD.estado,
      OLD.unidad_ejecutoraunidad_ejecutora, OLD.snip,
      OLD.programa,OLD.subprograma,OLD.proyecto,OLD.actividad,OLD.obra,
      OLD.latitud,OLD.longitud,OLD.costo,OLD.acumulacion_costoid,
      OLD.renglon,OLD.ubicacion_geografica,OLD.fecha_inicio,OLD.fecha_fin,
      OLD.duracion,OLD.duracion_dimension,OLD.orden,OLD.treePath,OLD.nivel,
      OLD.entidad,OLD.ejercicio,OLD.es_de_sigade,OLD.fuente_prestamo,
      OLD.fuente_donacion,OLD.fuente_nacional,OLD.componente_sigadeid,
      OLD.fecha_inicio_real,OLD.fecha_fin_real, OLD.inversion_nueva,
       v_version, NULL, NULL);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `componente_propiedad`
--

DROP TABLE IF EXISTS `componente_propiedad`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `componente_propiedad` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(1000) COLLATE utf8_bin NOT NULL,
  `descripcion` varchar(2000) COLLATE utf8_bin DEFAULT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  `dato_tipoid` int(10) NOT NULL,
  `estado` int(2) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `FKcomponente26853` (`dato_tipoid`),
  CONSTRAINT `FKcomponente26853` FOREIGN KEY (`dato_tipoid`) REFERENCES `dato_tipo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `componente_propiedad`
--

LOCK TABLES `componente_propiedad` WRITE;
/*!40000 ALTER TABLE `componente_propiedad` DISABLE KEYS */;
/*!40000 ALTER TABLE `componente_propiedad` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `componente_propiedad_valor`
--

DROP TABLE IF EXISTS `componente_propiedad_valor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `componente_propiedad_valor` (
  `componenteid` int(10) NOT NULL,
  `componente_propiedadid` int(10) NOT NULL,
  `valor_string` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `valor_entero` int(10) DEFAULT NULL,
  `valor_decimal` decimal(15,2) DEFAULT NULL,
  `valor_tiempo` timestamp NULL DEFAULT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`componenteid`,`componente_propiedadid`),
  KEY `FKcomponente278188` (`componente_propiedadid`),
  KEY `FKcomponente747047` (`componenteid`),
  CONSTRAINT `FKcomponente278188` FOREIGN KEY (`componente_propiedadid`) REFERENCES `componente_propiedad` (`id`),
  CONSTRAINT `FKcomponente747047` FOREIGN KEY (`componenteid`) REFERENCES `componente` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `componente_propiedad_valor`
--

LOCK TABLES `componente_propiedad_valor` WRITE;
/*!40000 ALTER TABLE `componente_propiedad_valor` DISABLE KEYS */;
/*!40000 ALTER TABLE `componente_propiedad_valor` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER trigger_comp_prop_val_insert
AFTER INSERT ON sipro.componente_propiedad_valor FOR EACH ROW
BEGIN
    DECLARE v_version int;
    SELECT max(a.version) INTO v_version FROM sipro_history.componente_propiedad_valor a
    WHERE a.componenteid=NEW.componenteid AND a.componente_propiedadid = NEW.componente_propiedadid;

    IF(v_version is null) THEN
        SET v_version=1;
    END IF;

    INSERT INTO sipro_history.componente_propiedad_valor
    VALUE(NEW.componenteid,NEW.componente_propiedadid,NEW.valor_string,NEW.valor_entero,
      NEW.valor_decimal,NEW.valor_tiempo,NEW.usuario_creo,NEW.usuario_actualizo,
      NEW.fecha_creacion,NEW.fecha_actualizacion,
      v_version, NULL, 1);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER trigger_comp_prop_val_update
AFTER UPDATE ON sipro.componente_propiedad_valor FOR EACH ROW
BEGIN
    DECLARE v_version int;
    SELECT max(a.version) INTO v_version FROM sipro_history.componente_propiedad_valor a
    WHERE a.componenteid=OLD.componenteid AND a.componente_propiedadid = OLD.componente_propiedadid;

    IF(v_version is null) THEN
        UPDATE sipro_history.componente_propiedad_valor a SET actual=null
        WHERE a.componenteid=OLD.componenteid AND a.componente_propiedadid = OLD.componente_propiedadid AND version is null;
        SET v_version=1;
    ELSE
        UPDATE sipro_history.componente_propiedad_valor a SET actual=null
        WHERE a.componenteid=OLD.componenteid AND a.componente_propiedadid = OLD.componente_propiedadid AND version=v_version;
        SET v_version=v_version+1;
    END IF;

    INSERT INTO sipro_history.componente_propiedad_valor
    VALUE(NEW.componenteid,NEW.componente_propiedadid,NEW.valor_string,NEW.valor_entero,
      NEW.valor_decimal,NEW.valor_tiempo,NEW.usuario_creo,NEW.usuario_actualizo,
      NEW.fecha_creacion,NEW.fecha_actualizacion,
       v_version, NULL, 1);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER trigger_comp_prop_val_delete
BEFORE DELETE ON sipro.componente_propiedad_valor FOR EACH ROW
BEGIN
    DECLARE v_version int;
    SELECT max(a.version) INTO v_version FROM sipro_history.componente_propiedad_valor a WHERE a.componenteid=OLD.componenteid AND a.componente_propiedadid = OLD.componente_propiedadid;

    IF(v_version is null) THEN
        SET v_version=1;
    ELSE
        UPDATE sipro_history.componente_propiedad_valor a SET actual=null WHERE a.componenteid=OLD.componenteid AND a.componente_propiedadid = OLD.componente_propiedadid AND version=v_version;
        SET v_version=v_version+1;
    END IF;

    INSERT INTO sipro_history.componente_propiedad_valor
    VALUE(OLD.componenteid,OLD.componente_propiedadid,OLD.valor_string,OLD.valor_entero,
      OLD.valor_decimal,OLD.valor_tiempo,OLD.usuario_creo,OLD.usuario_actualizo,OLD.fecha_creacion,
      OLD.fecha_actualizacion,
       v_version, NULL, NULL);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `componente_sigade`
--

DROP TABLE IF EXISTS `componente_sigade`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `componente_sigade` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(2000) COLLATE utf8_bin NOT NULL,
  `codigo_presupuestario` varchar(45) COLLATE utf8_bin NOT NULL,
  `numero_componente` int(10) NOT NULL,
  `monto_componente` decimal(15,2) NOT NULL,
  `estado` int(2) NOT NULL,
  `usuaraio_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `componente_sigade`
--

LOCK TABLES `componente_sigade` WRITE;
/*!40000 ALTER TABLE `componente_sigade` DISABLE KEYS */;
/*!40000 ALTER TABLE `componente_sigade` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER trigger_comp_sigade_insert
AFTER INSERT ON sipro.componente_sigade FOR EACH ROW
BEGIN
    DECLARE v_version int;
    SELECT max(a.version) INTO v_version FROM sipro_history.componente_sigade a
    WHERE a.id=NEW.id;

    IF(v_version is null) THEN
        SET v_version=1;
    END IF;

    INSERT INTO sipro_history.componente_sigade
    VALUE(NEW.id,NEW.nombre,NEW.codigo_presupuestario,NEW.numero_componente,
      NEW.monto_componente,NEW.estado,NEW.usuaraio_creo,NEW.usuario_actualizo,
      NEW.fecha_creacion,NEW.fecha_actualizacion,
      v_version, NULL, 1);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER trigger_comp_sigade_update
AFTER UPDATE ON sipro.componente_sigade FOR EACH ROW
BEGIN
    DECLARE v_version int;
    SELECT max(a.version) INTO v_version FROM sipro_history.componente_sigade a WHERE a.id=OLD.id;

    IF(v_version is null) THEN
        UPDATE sipro_history.componente_sigade SET actual=null WHERE id=OLD.id AND version is null;
        SET v_version=1;
    ELSE
        UPDATE sipro_history.componente_sigade SET actual=null WHERE id=OLD.id AND version=v_version;
        SET v_version=v_version+1;
    END IF;

    INSERT INTO sipro_history.componente_sigade
    VALUE(NEW.id,NEW.nombre,NEW.codigo_presupuestario,NEW.numero_componente,
      NEW.monto_componente,NEW.estado,NEW.usuaraio_creo,NEW.usuario_actualizo,
      NEW.fecha_creacion,NEW.fecha_actualizacion,
       v_version, NULL, 1);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER trigger_comp_sigade_delete
BEFORE DELETE ON sipro.componente_sigade FOR EACH ROW
BEGIN
    DECLARE v_version int;
    SELECT max(a.version) INTO v_version FROM sipro_history.componente_sigade a WHERE a.id=OLD.id;

    IF(v_version is null) THEN
        SET v_version=1;
    ELSE
        UPDATE sipro_history.componente_sigade SET actual=null WHERE id=OLD.id AND version=v_version;
        SET v_version=v_version+1;
    END IF;

    INSERT INTO sipro_history.colaborador
    VALUE(OLD.id,OLD.nombre,OLD.codigo_presupuestario,OLD.numero_componente,
      OLD.monto_componente,OLD.estado,OLD.usuaraio_creo,OLD.usuario_actualizo,
      OLD.fecha_creacion,OLD.fecha_actualizacion,
       v_version, NULL, NULL);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `componente_tipo`
--

DROP TABLE IF EXISTS `componente_tipo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `componente_tipo` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(1000) COLLATE utf8_bin NOT NULL,
  `descripcion` varchar(2000) COLLATE utf8_bin DEFAULT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  `estado` int(2) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `componente_tipo`
--

LOCK TABLES `componente_tipo` WRITE;
/*!40000 ALTER TABLE `componente_tipo` DISABLE KEYS */;
INSERT INTO `componente_tipo` VALUES (1,'General',NULL,'admin',NULL,'2017-09-30 11:51:27',NULL,1);
/*!40000 ALTER TABLE `componente_tipo` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER trigger_componente_tipo_insert
AFTER INSERT ON sipro.componente_tipo FOR EACH ROW
BEGIN
    DECLARE v_version int;
    SELECT max(a.version) INTO v_version FROM sipro_history.componente_tipo a
    WHERE a.id=NEW.id;

    IF(v_version is null) THEN
        SET v_version=1;
    END IF;

    INSERT INTO sipro_history.componente_tipo
    VALUE(NEW.id,NEW.nombre,NEW.descripcion,NEW.usuario_creo,NEW.usuario_actualizo,
      NEW.fecha_creacion,NEW.fecha_actualizacion,NEW.estado,
      v_version, NULL, 1);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER trigger_componente_tipo_update
AFTER UPDATE ON sipro.componente_tipo FOR EACH ROW
BEGIN
    DECLARE v_version int;
    SELECT max(a.version) INTO v_version FROM sipro_history.componente_tipo a WHERE a.id=OLD.id;

    IF(v_version is null) THEN
        UPDATE sipro_history.componente_tipo SET actual=null WHERE id=OLD.id AND version is null;
        SET v_version=1;
    ELSE
        UPDATE sipro_history.componente_tipo SET actual=null WHERE id=OLD.id AND version=v_version;
        SET v_version=v_version+1;
    END IF;

    INSERT INTO sipro_history.componente_tipo
    VALUE(NEW.id,NEW.nombre,NEW.descripcion,NEW.usuario_creo,NEW.usuario_actualizo,
      NEW.fecha_creacion,NEW.fecha_actualizacion,NEW.estado,
       v_version, NULL, 1);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER trigger_componente_tipo_delete
BEFORE DELETE ON sipro.componente_tipo FOR EACH ROW
BEGIN
    DECLARE v_version int;
    SELECT max(a.version) INTO v_version FROM sipro_history.componente_tipo a WHERE a.id=OLD.id;

    IF(v_version is null) THEN
        SET v_version=1;
    ELSE
        UPDATE sipro_history.componente_tipo SET actual=null WHERE id=OLD.id AND version=v_version;
        SET v_version=v_version+1;
    END IF;

    INSERT INTO sipro_history.componente_tipo
    VALUE(OLD.id,OLD.nombre,OLD.descripcion,OLD.usuario_creo,OLD.usuario_actualizo,
      OLD.fecha_creacion,OLD.fecha_actualizacion,OLD.estado,
       v_version, NULL, NULL);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `componente_usuario`
--

DROP TABLE IF EXISTS `componente_usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `componente_usuario` (
  `componenteid` int(10) NOT NULL,
  `usuario` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NULL DEFAULT NULL,
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`componenteid`,`usuario`),
  KEY `FKusuario114298` (`usuario`),
  KEY `componente_usuario_ibfk_1` (`usuario`),
  KEY `FKcomponente114298` (`componenteid`),
  CONSTRAINT `FKcomponente114298` FOREIGN KEY (`componenteid`) REFERENCES `componente` (`id`),
  CONSTRAINT `componente_usuario_ibfk_1` FOREIGN KEY (`usuario`) REFERENCES `usuario` (`usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `componente_usuario`
--

LOCK TABLES `componente_usuario` WRITE;
/*!40000 ALTER TABLE `componente_usuario` DISABLE KEYS */;
/*!40000 ALTER TABLE `componente_usuario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cooperante`
--

DROP TABLE IF EXISTS `cooperante`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cooperante` (
  `codigo` int(5) NOT NULL,
  `siglas` varchar(45) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `nombre` varchar(1000) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `descripcion` varchar(4000) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `usuario_creo` varchar(30) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `usuario_actualizo` varchar(30) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  `estado` int(2) NOT NULL,
  `ejercicio` int(4) NOT NULL,
  PRIMARY KEY (`codigo`,`ejercicio`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cooperante`
--

LOCK TABLES `cooperante` WRITE;
/*!40000 ALTER TABLE `cooperante` DISABLE KEYS */;
/*!40000 ALTER TABLE `cooperante` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ctipo_propiedad`
--

DROP TABLE IF EXISTS `ctipo_propiedad`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ctipo_propiedad` (
  `componente_tipoid` int(10) NOT NULL,
  `componente_propiedadid` int(10) NOT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`componente_tipoid`,`componente_propiedadid`),
  KEY `FKctipo_prop358116` (`componente_tipoid`),
  KEY `FKctipo_prop74576` (`componente_propiedadid`),
  CONSTRAINT `FKctipo_prop358116` FOREIGN KEY (`componente_tipoid`) REFERENCES `componente_tipo` (`id`),
  CONSTRAINT `FKctipo_prop74576` FOREIGN KEY (`componente_propiedadid`) REFERENCES `componente_propiedad` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ctipo_propiedad`
--

LOCK TABLES `ctipo_propiedad` WRITE;
/*!40000 ALTER TABLE `ctipo_propiedad` DISABLE KEYS */;
/*!40000 ALTER TABLE `ctipo_propiedad` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dato_tipo`
--

DROP TABLE IF EXISTS `dato_tipo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dato_tipo` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) COLLATE utf8_bin NOT NULL,
  `descripcion` varchar(1000) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dato_tipo`
--

LOCK TABLES `dato_tipo` WRITE;
/*!40000 ALTER TABLE `dato_tipo` DISABLE KEYS */;
INSERT INTO `dato_tipo` VALUES (1,'texto',NULL),(2,'entero',NULL),(3,'decimal',NULL),(4,'booleano',NULL),(5,'fecha',NULL);
/*!40000 ALTER TABLE `dato_tipo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `desembolso`
--

DROP TABLE IF EXISTS `desembolso`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `desembolso` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `fecha` timestamp NOT NULL DEFAULT current_timestamp(),
  `estado` int(2) NOT NULL,
  `monto` decimal(15,2) NOT NULL,
  `tipo_cambio` decimal(15,4) NOT NULL,
  `monto_moneda_origen` int(11) DEFAULT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  `proyectoid` int(10) NOT NULL,
  `desembolso_tipoid` int(10) NOT NULL,
  `tipo_monedaid` int(10) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKdesembolso151185` (`proyectoid`),
  KEY `FKdesembolso941697` (`desembolso_tipoid`),
  KEY `FKdesembolso995959` (`tipo_monedaid`),
  CONSTRAINT `FKdesembolso151185` FOREIGN KEY (`proyectoid`) REFERENCES `proyecto` (`id`),
  CONSTRAINT `FKdesembolso941697` FOREIGN KEY (`desembolso_tipoid`) REFERENCES `desembolso_tipo` (`id`),
  CONSTRAINT `FKdesembolso995959` FOREIGN KEY (`tipo_monedaid`) REFERENCES `tipo_moneda` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `desembolso`
--

LOCK TABLES `desembolso` WRITE;
/*!40000 ALTER TABLE `desembolso` DISABLE KEYS */;
/*!40000 ALTER TABLE `desembolso` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER trigger_desembolso_insert
AFTER INSERT ON sipro.desembolso FOR EACH ROW
BEGIN
    DECLARE v_version int;
    SELECT max(a.version) INTO v_version FROM sipro_history.desembolso a
    WHERE a.id=NEW.id;

    IF(v_version is null) THEN
        SET v_version=1;
    END IF;

    INSERT INTO sipro_history.desembolso
    VALUE(NEW.id,NEW.fecha,NEW.estado,NEW.monto,NEW.tipo_cambio,NEW.monto_moneda_origen,
      NEW.usuario_creo,NEW.usuario_actualizo,NEW.fecha_creacion,NEW.fecha_actualizacion,
      NEW.proyectoid,NEW.desembolso_tipoid,NEW.tipo_monedaid,
      v_version, NULL, 1);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER trigger_desembolso_update
AFTER UPDATE ON sipro.desembolso FOR EACH ROW
BEGIN
    DECLARE v_version int;
    SELECT max(a.version) INTO v_version FROM sipro_history.desembolso a WHERE a.id=OLD.id;

    IF(v_version is null) THEN
        UPDATE sipro_history.desembolso SET actual=null WHERE id=OLD.id AND version is null;
        SET v_version=1;
    ELSE
        UPDATE sipro_history.desembolso SET actual=null WHERE id=OLD.id AND version=v_version;
        SET v_version=v_version+1;
    END IF;

    INSERT INTO sipro_history.desembolso
    VALUE(NEW.id,NEW.fecha,NEW.estado,NEW.monto,NEW.tipo_cambio,NEW.monto_moneda_origen,
      NEW.usuario_creo,NEW.usuario_actualizo,NEW.fecha_creacion,NEW.fecha_actualizacion,
      NEW.proyectoid,NEW.desembolso_tipoid,NEW.tipo_monedaid,
       v_version, NULL, 1);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER trigger_desembolso_delete
BEFORE DELETE ON sipro.desembolso FOR EACH ROW
BEGIN
    DECLARE v_version int;
    SELECT max(a.version) INTO v_version FROM sipro_history.desembolso a WHERE a.id=OLD.id;

    IF(v_version is null) THEN
        SET v_version=1;
    ELSE
        UPDATE sipro_history.desembolso SET actual=null WHERE id=OLD.id AND version=v_version;
        SET v_version=v_version+1;
    END IF;

    INSERT INTO sipro_history.desembolso
    VALUE(OLD.id,OLD.fecha,OLD.estado,OLD.monto,OLD.tipo_cambio,OLD.monto_moneda_origen,
      OLD.usuario_creo,OLD.usuario_actualizo,OLD.fecha_creacion,OLD.fecha_actualizacion,
      OLD.proyectoid,OLD.desembolso_tipoid,OLD.tipo_monedaid,
       v_version, NULL, NULL);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `desembolso_tipo`
--

DROP TABLE IF EXISTS `desembolso_tipo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `desembolso_tipo` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(1000) COLLATE utf8_bin DEFAULT NULL,
  `descripcion` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `estado` int(2) DEFAULT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `desembolso_tipo`
--

LOCK TABLES `desembolso_tipo` WRITE;
/*!40000 ALTER TABLE `desembolso_tipo` DISABLE KEYS */;
INSERT INTO `desembolso_tipo` VALUES (1,'Planificado','Desembolso',1,'admin',NULL,'2017-01-02 00:00:00',NULL);
/*!40000 ALTER TABLE `desembolso_tipo` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER trigger_desembolso_tipo_insert
AFTER INSERT ON sipro.desembolso_tipo FOR EACH ROW
BEGIN
    DECLARE v_version int;
    SELECT max(a.version) INTO v_version FROM sipro_history.desembolso_tipo a
    WHERE a.id=NEW.id;

    IF(v_version is null) THEN
        SET v_version=1;
    END IF;

    INSERT INTO sipro_history.desembolso_tipo
    VALUE(NEW.id,NEW.nombre,NEW.descripcion,NEW.estado,NEW.usuario_creo,
      NEW.usuario_actualizo,NEW.fecha_creacion,NEW.fecha_actualizacion,
      v_version, NULL, 1);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER trigger_desembolso_tipo_update
AFTER UPDATE ON sipro.desembolso_tipo FOR EACH ROW
BEGIN
    DECLARE v_version int;
    SELECT max(a.version) INTO v_version FROM sipro_history.desembolso_tipo a WHERE a.id=OLD.id;

    IF(v_version is null) THEN
        UPDATE sipro_history.desembolso_tipo SET actual=null WHERE id=OLD.id AND version is null;
        SET v_version=1;
    ELSE
        UPDATE sipro_history.desembolso_tipo SET actual=null WHERE id=OLD.id AND version=v_version;
        SET v_version=v_version+1;
    END IF;

    INSERT INTO sipro_history.desembolso_tipo
    VALUE(NEW.id,NEW.nombre,NEW.descripcion,NEW.estado,NEW.usuario_creo,
      NEW.usuario_actualizo,NEW.fecha_creacion,NEW.fecha_actualizacion,
       v_version, NULL, 1);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER trigger_desembolso_tipo_delete
BEFORE DELETE ON sipro.desembolso_tipo FOR EACH ROW
BEGIN
    DECLARE v_version int;
    SELECT max(a.version) INTO v_version FROM sipro_history.desembolso_tipo a WHERE a.id=OLD.id;

    IF(v_version is null) THEN
        SET v_version=1;
    ELSE
        UPDATE sipro_history.desembolso_tipo SET actual=null WHERE id=OLD.id AND version=v_version;
        SET v_version=v_version+1;
    END IF;

    INSERT INTO sipro_history.desembolso_tipo
    VALUE(OLD.id,OLD.nombre,OLD.descripcion,OLD.estado,OLD.usuario_creo,
      OLD.usuario_actualizo,OLD.fecha_creacion,OLD.fecha_actualizacion,
       v_version, NULL, NULL);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `documento`
--

DROP TABLE IF EXISTS `documento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `documento` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(1000) COLLATE utf8_bin NOT NULL,
  `extension` varchar(45) COLLATE utf8_bin NOT NULL,
  `id_tipo_objeto` int(11) NOT NULL,
  `id_objeto` int(11) NOT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NULL DEFAULT NULL,
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  `estado` int(2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `documento`
--

LOCK TABLES `documento` WRITE;
/*!40000 ALTER TABLE `documento` DISABLE KEYS */;
/*!40000 ALTER TABLE `documento` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER trigger_documento_insert
AFTER INSERT ON sipro.documento FOR EACH ROW
BEGIN
    DECLARE v_version int;
    SELECT max(a.version) INTO v_version FROM sipro_history.documento a
    WHERE a.id=NEW.id;

    IF(v_version is null) THEN
        SET v_version=1;
    END IF;

    INSERT INTO sipro_history.documento
    VALUE(NEW.id,NEW.nombre,NEW.extension,NEW.id_tipo_objeto,NEW.id_objeto,
      NEW.usuario_creo,NEW.usuario_actualizo,NEW.fecha_creacion,NEW.fecha_actualizacion,NEW.estado,
      v_version, NULL, 1);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER trigger_documento_update
AFTER UPDATE ON sipro.documento FOR EACH ROW
BEGIN
    DECLARE v_version int;
    SELECT max(a.version) INTO v_version FROM sipro_history.documento a WHERE a.id=OLD.id;

    IF(v_version is null) THEN
        UPDATE sipro_history.documento SET actual=null WHERE id=OLD.id AND version is null;
        SET v_version=1;
    ELSE
        UPDATE sipro_history.documento SET actual=null WHERE id=OLD.id AND version=v_version;
        SET v_version=v_version+1;
    END IF;

    INSERT INTO sipro_history.documento
    VALUE(NEW.id,NEW.nombre,NEW.extension,NEW.id_tipo_objeto,NEW.id_objeto,
      NEW.usuario_creo,NEW.usuario_actualizo,NEW.fecha_creacion,NEW.fecha_actualizacion,NEW.estado,
       v_version, NULL, 1);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER trigger_documento_delete
BEFORE DELETE ON sipro.documento FOR EACH ROW
BEGIN
    DECLARE v_version int;
    SELECT max(a.version) INTO v_version FROM sipro_history.documento a WHERE a.id=OLD.id;

    IF(v_version is null) THEN
        SET v_version=1;
    ELSE
        UPDATE sipro_history.documento SET actual=null WHERE id=OLD.id AND version=v_version;
        SET v_version=v_version+1;
    END IF;

    INSERT INTO sipro_history.documento
    VALUE(OLD.id,OLD.nombre,OLD.extension,OLD.id_tipo_objeto,OLD.id_objeto,OLD.usuario_creo,
      OLD.usuario_actualizo,OLD.fecha_creacion,OLD.fecha_actualizacion,OLD.estado,
       v_version, NULL, NULL);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `ejecucion_estado`
--

DROP TABLE IF EXISTS `ejecucion_estado`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ejecucion_estado` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(10) COLLATE utf8_bin NOT NULL,
  `descripcion` varchar(1000) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ejecucion_estado`
--

LOCK TABLES `ejecucion_estado` WRITE;
/*!40000 ALTER TABLE `ejecucion_estado` DISABLE KEYS */;
/*!40000 ALTER TABLE `ejecucion_estado` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `entidad`
--

DROP TABLE IF EXISTS `entidad`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `entidad` (
  `entidad` int(10) NOT NULL,
  `nombre` varchar(1000) COLLATE utf8_bin NOT NULL,
  `abreviatura` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `ejercicio` int(4) NOT NULL,
  PRIMARY KEY (`entidad`,`ejercicio`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `entidad`
--

LOCK TABLES `entidad` WRITE;
/*!40000 ALTER TABLE `entidad` DISABLE KEYS */;
INSERT INTO `entidad` VALUES (0,'SIN ENTIDAD','Sin Entidad',2017),(11110001,'CONGRESO DE LA REPUBLICA','',2017),(11120002,'ORGANISMO JUDICIAL','',2017),(11130003,'PRESIDENCIA DE LA REPÚBLICA','',2017),(11130004,'MINISTERIO DE RELACIONES   EXTERIORES','',2017),(11130005,'MINISTERIO DE GOBERNACIÓN','',2017),(11130006,'MINISTERIO DE LA DEFENSA NACIONAL','',2017),(11130007,'MINISTERIO DE FINANZAS PÚBLICAS','',2017),(11130008,'MINISTERIO DE EDUCACIÓN','',2017),(11130009,'MINISTERIO DE SALUD PÚBLICA Y ASISTENCIA SOCIAL','',2017),(11130010,'MINISTERIO DE TRABAJO Y PREVISIÓN SOCIAL','',2017),(11130011,'MINISTERIO DE ECONOMÍA','',2017),(11130012,'MINISTERIO DE AGRICULTURA, GANADERÍA Y ALIMENTACIÓN','',2017),(11130013,'MINISTERIO DE  COMUNICACIONES, INFRAESTRUCTURA Y VIVIENDA','',2017),(11130014,'MINISTERIO DE ENERGÍA Y MINAS','',2017),(11130015,'MINISTERIO DE CULTURA Y DEPORTES','',2017),(11130016,'SECRETARÍAS Y OTRAS DEPENDENCIAS DEL EJECUTIVO','',2017),(11130017,'MINISTERIO DE AMBIENTE Y RECURSOS NATURALES','',2017),(11130018,'OBLIGACIONES DEL ESTADO A CARGO DEL TESORO','',2017),(11130019,'SERVICIOS DE LA DEUDA PUBLICA','',2017),(11130020,'MINISTERIO DE DESARROLLO SOCIAL','',2017),(11140020,'CONTRALORÍA GENERAL DE CUENTAS','',2017),(11140021,'PROCURADURIA GENERAL DE LA NACIÓN','',2017),(11140022,'MINISTERIO PÚBLICO (MP)','',2017),(11140024,'CORTE DE CONSTITUCIONALIDAD.','',2017),(11140026,'REGISTRO GENERAL DE LA PROPIEDAD (RGP)','',2017),(11140027,'INSTITUTO DE LA DEFENSA PÚBLICA PENAL (IDPP)','',2017),(11140028,'SEGUNDO REGISTRO DE LA PROPIEDAD','',2017),(11140029,'COORDINADORA NACIONAL PARA LA REDUCCIÓN DE DESASTRES DE ORIGEN NATURAL O PROVOCADO (CONRED)','',2017),(11140065,'REGISTRO DE INFORMACION CATASTRAL DE GUATEMALA (RIC)','',2017),(11140066,'REGISTRO NACIONAL DE LAS PERSONAS (RENAP)','',2017),(11140067,'CONSEJO NACIONAL DE ADOPCIONES (CNA)','',2017),(11140068,'CONSEJO NACIONAL DE ATENCION AL MIGRANTE DE GUATEMALA (CONAMIGUA)','',2017),(11140069,'SECRETARIA EJECUTIVA DE LA INSTANCIA COORDINADORA DE LA MODERNIZACION DEL SECTOR JUSTICIA (SEICMSJ)','',2017),(11140070,'SECRETARIA NACIONAL DE ADMINISTRACION DE BIENES EN EXTINCION DE DOMINIO (SENABED)','',2017),(11140071,'CONSEJO NACIONAL DEL DEPORTE, LA EDUCACION FISICA Y LA RECREACION (CONADER)','',2017),(11140072,'CONSEJO ECONÓMICO Y SOCIAL DE GUATEMALA (CES)','',2017),(11140073,'COMISIÓN NACIONAL DE ENERGÍA ELÉCTRICA','',2017),(11140074,'OFICINA NACIONAL DE PREVENCION DE LA TORTURA Y OTROS TRATOS O PENAS CRUELES, INHUMANOS O DEGRADANTES','',2017),(11150023,'TRIBUNAL SUPREMO ELECTORAL (TSE)','',2017),(11150025,'PROCURADURIA DE LOS DERECHOS HUMANOS (PDH)','',2017),(11200030,'INSTITUTO NACIONAL DE ESTADÍSTICA  (INE)','',2017),(11200032,'INSTITUTO NACIONAL DE ADMINISTRACIÓN  PÚBLICA (INAP)','',2017),(11200034,'INSTITUTO TÉCNICO DE CAPACITACIÓN Y PRODUCTIVIDAD (INTECAP)','',2017),(11200035,'INSTITUTO DE RECREACIÓN DE LOS TRABAJADORES DE LA EMPRESA PRIVADA DE GUATEMALA (IRTRA)','',2017),(11200036,'CONSEJO NACIONAL PARA LA PROTECCIÓN DE LA ANTIGUA GUATEMALA (CNPAG)','',2017),(11200037,'BENEMÉRITO CUERPO VOLUNTARIO DE BOMBEROS DE GUATEMALA (CVB)','',2017),(11200039,'APORTE PARA LA DESCENTRALIZACIÓN CULTURAL (ADESCA)','',2017),(11200041,'INSTITUTO DE CIENCIA Y TECNOLOGÍA AGRÍCOLAS (ICTA)','',2017),(11200046,'INSTITUTO NACIONAL DE CIENCIAS FORENSES DE GUATEMALA (INACIF)','',2017),(11200050,'COMITÉ PERMANENTE DE EXPOSICIONES (COPEREX)','',2017),(11200051,'INSTITUTO NACIONAL DE COOPERATIVAS (INACOP)','',2017),(11200052,'INSPECCIÓN GENERAL DE COOPERATIVAS (INGECOP)','',2017),(11200053,'INSTITUTO GUATEMALTECO DE TURISMO (INGUAT)','',2017),(11200054,'INSTITUTO DE FOMENTO MUNICIPAL  (INFOM)','',2017),(11200055,'INSTITUTO NACIONAL DE BOSQUES (INAB)','',2017),(11200056,'SUPERINTENDENCIA DE ADMINISTRACIÓN TRIBUTARIA (SAT)','',2017),(11200057,'FONDO DE TIERRAS (FONTIERRAS)','',2017),(11200059,'COMITÉ NACIONAL DE ALFABETIZACIÓN (CONALFA)','',2017),(11200064,'ACADEMIA DE LAS LENGUAS MAYAS DE GUATEMALA (ALMG)','',2017),(11200067,'CONSEJO NACIONAL PARA LA ATENCIÓN DE LAS PERSONAS CON DISCAPACIDAD (CONADI)','',2017),(11200068,'AGENCIA NACIONAL DE ALIANZAS PARA EL DESARROLLO DE INFRAESTRUCTURA ECONÓMICA (ANADIE)','',2017),(11300060,'UNIVERSIDAD DE SAN CARLOS DE GUATEMALA (USAC)','',2017),(11300061,'CONFEDERACIÓN DEPORTIVA AUTÓNOMA DE GUATEMALA (CDAG)','',2017),(11300062,'COMITÉ OLÍMPICO GUATEMALTECO (COG)','',2017),(11300063,'ESCUELA NACIONAL CENTRAL DE AGRICULTURA (ENCA)','',2017),(11300064,'FEDERACIÓN NACIONAL DE TRIATLÓN','',2017),(11300065,'FEDERACIÓN NACIONAL DE BOLICHE','',2017),(11300066,'FEDERACIÓN NACIONAL DE VOLEIBOL','',2017),(11300067,'FEDERACIÓN NACIONAL DE NATACIÓN, CLAVADOS, POLO ACUÁTICO Y NADO SINCRONIZADO','',2017),(11300068,'FEDERACIÓN NACIONAL DE FÚTBOL','',2017),(11300069,'FEDERACIÓN NACIONAL DE LUCHAS DE GUATEMALA','',2017),(11300070,'FEDERACIÓN NACIONAL DE CICLISMO DE GUATEMALA','',2017),(11300071,'FEDERACIÓN NACIONAL DE TENIS DE CAMPO','',2017),(11300072,'FEDERACIÓN NACIONAL DE BOXEO','',2017),(11300073,'FEDERACIÓN NACIONAL DE TIRO','',2017),(11300074,'FEDERACIÓN NACIONAL DE BÁDMINTON DE GUATEMALA','',2017),(11300075,'FEDERACIÓN NACIONAL DE ESGRIMA','',2017),(11300076,'FEDERACIÓN NACIONAL DE BALONMANO','',2017),(11300077,'FEDERACIÓN NACIONAL DE LEVANTAMIENTO DE PESAS','',2017),(11300078,'FEDERACIÓN NACIONAL DE AJEDREZ DE GUATEMALA','',2017),(11300079,'FEDERACIÓN NACIONAL DE BEISBOL','',2017),(11300080,'FEDERACIÓN NACIONAL DE REMO Y CANOTAJE','',2017),(11300081,'FEDERACIÓN NACIONAL DE MOTOCICLISMO','',2017),(11300082,'TRIBUNAL ELECCIONARIO DEL DEPORTE FEDERADO','',2017),(11300083,'FEDERACIÓN NACIONAL DE ANDINISMO','',2017),(11300084,'FEDERACIÓN NACIONAL DE BALONCESTO','',2017),(11300085,'FEDERACIÓN NACIONAL DE ATLETISMO','',2017),(11300086,'FEDERACIÓN NACIONAL DE GIMNASIA','',2017),(11300087,'FEDERACIÓN NACIONAL DE FÍSICO CULTURISMO','',2017),(11300088,'FEDERACIÓN NACIONAL DE PATINAJE SOBRE RUEDAS DE GUATEMALA','',2017),(11300089,'FEDERACIÓN NACIONAL DE KARATE-DO','',2017),(11300090,'FEDERACIÓN NACIONAL DE LEVANTAMIENTO DE POTENCIA','',2017),(11300091,'FEDERACION NACIONAL DE TENIS DE MESA','',2017),(11300092,'FEDERACION NACIONAL DE TAEKWON-DO','',2017),(11300093,'FEDERACION NACIONAL DE JUDO','',2017),(11400068,'INSTITUTO GUATEMALTECO DE SEGURIDAD SOCIAL (IGSS)','',2017),(11400069,'INSTITUTO DE PREVISIÓN MILITAR (IPM)','',2017),(12100101,'MUNICIPALIDAD DE GUATEMALA','',2017),(12100102,'MUNICIPALIDAD DE SANTA CATARINA PINULA','',2017),(12100103,'MUNICIPALIDAD DE SAN JOSE PINULA','',2017),(12100104,'MUNICIPALIDAD DE SAN JOSE DEL GOLFO','',2017),(12100105,'MUNICIPALIDAD DE PALENCIA','',2017),(12100106,'MUNICIPALIDAD DE CHINAUTLA','',2017),(12100107,'MUNICIPALIDAD DE SAN PEDRO AYAMPUC','',2017),(12100108,'MUNICIPALIDAD DE MIXCO','',2017),(12100109,'MUNICIPALIDAD DE SAN PEDRO SACATEPEQUEZ','',2017),(12100110,'MUNICIPALIDAD DE SAN JUAN SACATEPEQUEZ','',2017),(12100111,'MUNICIPALIDAD DE SAN RAYMUNDO','',2017),(12100112,'MUNICIPALIDAD DE CHUARRANCHO','',2017),(12100113,'MUNICIPALIDAD DE FRAIJANES','',2017),(12100114,'MUNICIPALIDAD DE AMATITLAN','',2017),(12100115,'MUNICIPALIDAD DE VILLA NUEVA','',2017),(12100116,'MUNICIPALIDAD DE VILLA CANALES','',2017),(12100117,'MUNICIPALIDAD DE PETAPA','',2017),(12100201,'MUNICIPALIDAD DE GUASTATOYA','',2017),(12100202,'MUNICIPALIDAD DE MORAZAN','',2017),(12100203,'MUNICIPALIDAD DE SAN AGUSTIN ACASAGUASTLAN','',2017),(12100204,'MUNICIPALIDAD DE SAN CRISTOBAL ACASAGUASTLAN','',2017),(12100205,'MUNICIPALIDAD DE EL JICARO','',2017),(12100206,'MUNICIPALIDAD DE SANSARE','',2017),(12100207,'MUNICIPALIDAD DE SANARATE','',2017),(12100208,'MUNICIPALIDAD DE SAN ANTONIO LA PAZ','',2017),(12100301,'MUNICIPALIDAD DE ANTIGUA GUATEMALA','',2017),(12100302,'MUNICIPALIDAD DE JOCOTENANGO','',2017),(12100303,'MUNICIPALIDAD DE PASTORES','',2017),(12100304,'MUNICIPALIDAD DE SUMPANGO','',2017),(12100305,'MUNICIPALIDAD DE SANTO DOMINGO XENACOJ','',2017),(12100306,'MUNICIPALIDAD DE SANTIAGO SACATEPEQUEZ','',2017),(12100307,'MUNICIPALIDAD DE SAN BARTOLOME MILPAS ALTAS','',2017),(12100308,'MUNICIPALIDAD DE SAN LUCAS SACATEPEQUEZ','',2017),(12100309,'MUNICIPALIDAD DE SANTA LUCIA MILPAS ALTAS','',2017),(12100310,'MUNICIPALIDAD DE MAGDALENA MILPAS ALTAS','',2017),(12100311,'MUNICIPALIDAD DE SANTA MARIA DE JESUS','',2017),(12100312,'MUNICIPALIDAD DE CIUDAD VIEJA','',2017),(12100313,'MUNICIPALIDAD DE SAN MIGUEL DUEÑAS','',2017),(12100314,'MUNICIPALIDAD DE ALOTENANGO','',2017),(12100315,'MUNICIPALIDAD DE SAN ANTONIO AGUAS CALIENTES','',2017),(12100316,'MUNICIPALIDAD DE SANTA CATARINA BARAHONA','',2017),(12100401,'MUNICIPALIDAD DE CHIMALTENANGO','',2017),(12100402,'MUNICIPALIDAD DE SAN JOSE POAQUIL','',2017),(12100403,'MUNICIPALIDAD DE SAN MARTIN JILOTEPEQUE','',2017),(12100404,'MUNICIPALIDAD DE COMALAPA','',2017),(12100405,'MUNICIPALIDAD DE SANTA APOLONIA','',2017),(12100406,'MUNICIPALIDAD DE TECPAN GUATEMALA','',2017),(12100407,'MUNICIPALIDAD DE PATZUN','',2017),(12100408,'MUNICIPALIDAD DE POCHUTA','',2017),(12100409,'MUNICIPALIDAD DE PATZICIA','',2017),(12100410,'MUNICIPALIDAD DE SANTA CRUZ BALANYA','',2017),(12100411,'MUNICIPALIDAD DE ACATENANGO','',2017),(12100412,'MUNICIPALIDAD DE YEPOCAPA','',2017),(12100413,'MUNICIPALIDAD DE SAN ANDRES ITZAPA','',2017),(12100414,'MUNICIPALIDAD DE PARRAMOS','',2017),(12100415,'MUNICIPALIDAD DE ZARAGOZA','',2017),(12100416,'MUNICIPALIDAD DE EL TEJAR','',2017),(12100501,'MUNICIPALIDAD DE ESCUINTLA','',2017),(12100502,'MUNICIPALIDAD DE SANTA LUCIA COTZUMALGUAPA','',2017),(12100503,'MUNICIPALIDAD DE LA DEMOCRACIA','',2017),(12100504,'MUNICIPALIDAD DE SIQUINALA','',2017),(12100505,'MUNICIPALIDAD DE MASAGUA','',2017),(12100506,'MUNICIPALIDAD DE TIQUISATE','',2017),(12100507,'MUNICIPALIDAD DE LA GOMERA','',2017),(12100508,'MUNICIPALIDAD DE GUANAGAZAPA','',2017),(12100509,'MUNICIPALIDAD DE SAN JOSE','',2017),(12100510,'MUNICIPALIDAD DE IZTAPA','',2017),(12100511,'MUNICIPALIDAD DE PALIN','',2017),(12100512,'MUNICIPALIDAD DE SAN VICENTE PACAYA','',2017),(12100513,'MUNICIPALIDAD DE NUEVA CONCEPCION','',2017),(12100514,'MUNICIPALIDAD DE SIPACATE','',2017),(12100601,'MUNICIPALIDAD DE CUILAPA','',2017),(12100602,'MUNICIPALIDAD DE BARBERENA','',2017),(12100603,'MUNICIPALIDAD DE SANTA ROSA DE LIMA','',2017),(12100604,'MUNICIPALIDAD DE CASILLAS','',2017),(12100605,'MUNICIPALIDAD DE SAN RAFAEL LAS FLORES','',2017),(12100606,'MUNICIPALIDAD DE ORATORIO','',2017),(12100607,'MUNICIPALIDAD DE SAN JUAN TECUACO','',2017),(12100608,'MUNICIPALIDAD DE CHIQUIMULILLA','',2017),(12100609,'MUNICIPALIDAD DE TAXISCO','',2017),(12100610,'MUNICIPALIDAD DE SANTA MARIA IXHUATAN','',2017),(12100611,'MUNICIPALIDAD DE GUAZACAPAN','',2017),(12100612,'MUNICIPALIDAD DE SANTA CRUZ NARANJO','',2017),(12100613,'MUNICIPALIDAD DE PUEBLO NUEVO VIÑAS','',2017),(12100614,'MUNICIPALIDAD DE NUEVA SANTA ROSA','',2017),(12100701,'MUNICIPALIDAD DE SOLOLA','',2017),(12100702,'MUNICIPALIDAD DE SAN JOSE CHACAYA','',2017),(12100703,'MUNICIPALIDAD DE SANTA MARIA VISITACION','',2017),(12100704,'MUNICIPALIDAD DE SANTA LUCIA UTATLAN','',2017),(12100705,'MUNICIPALIDAD DE NAHUALA','',2017),(12100706,'MUNICIPALIDAD DE SANTA CATARINA IXTAHUACAN','',2017),(12100707,'MUNICIPALIDAD DE SANTA CLARA LA LAGUNA','',2017),(12100708,'MUNICIPALIDAD DE CONCEPCION','',2017),(12100709,'MUNICIPALIDAD DE SAN ANDRES SEMETABAJ','',2017),(12100710,'MUNICIPALIDAD DE PANAJACHEL','',2017),(12100711,'MUNICIPALIDAD DE SANTA CATARINA PALOPO','',2017),(12100712,'MUNICIPALIDAD DE SAN ANTONIO PALOPO','',2017),(12100713,'MUNICIPALIDAD DE SAN LUCAS TOLIMAN','',2017),(12100714,'MUNICIPALIDAD DE SANTA CRUZ LA LAGUNA','',2017),(12100715,'MUNICIPALIDAD DE SAN PABLO LA LAGUNA','',2017),(12100716,'MUNICIPALIDAD DE SAN MARCOS LA LAGUNA','',2017),(12100717,'MUNICIPALIDAD DE SAN JUAN LA LAGUNA','',2017),(12100718,'MUNICIPALIDAD DE SAN PEDRO LA LAGUNA','',2017),(12100719,'MUNICIPALIDAD DE SANTIAGO ATITLAN','',2017),(12100801,'MUNICIPALIDAD DE TOTONICAPAN','',2017),(12100802,'MUNICIPALIDAD DE SAN CRISTOBAL TOTONICAPAN','',2017),(12100803,'MUNICIPALIDAD DE SAN FRANCISCO EL ALTO','',2017),(12100804,'MUNICIPALIDAD DE SAN ANDRES XECUL','',2017),(12100805,'MUNICIPALIDAD DE MOMOSTENANGO','',2017),(12100806,'MUNICIPALIDAD DE SANTA MARIA CHIQUIMULA','',2017),(12100807,'MUNICIPALIDAD DE SANTA LUCIA LA REFORMA','',2017),(12100808,'MUNICIPALIDAD DE SAN BARTOLO AGUAS CALIENTES','',2017),(12100901,'MUNICIPALIDAD DE QUETZALTENANGO','',2017),(12100902,'MUNICIPALIDAD DE SALCAJA','',2017),(12100903,'MUNICIPALIDAD DE OLINTEPEQUE','',2017),(12100904,'MUNICIPALIDAD DE SAN CARLOS SIJA','',2017),(12100905,'MUNICIPALIDAD DE SIBILIA','',2017),(12100906,'MUNICIPALIDAD DE CABRICAN','',2017),(12100907,'MUNICIPALIDAD DE CAJOLA','',2017),(12100908,'MUNICIPALIDAD DE SAN MIGUEL SIGUILA','',2017),(12100909,'MUNICIPALIDAD DE SAN JUAN OSTUNCALCO','',2017),(12100910,'MUNICIPALIDAD DE SAN MATEO','',2017),(12100911,'MUNICIPALIDAD DE CONCEPCION CHIQUIRICHAPA','',2017),(12100912,'MUNICIPALIDAD DE SAN MARTIN SACATEPEQUEZ','',2017),(12100913,'MUNICIPALIDAD DE ALMOLONGA','',2017),(12100914,'MUNICIPALIDAD DE CANTEL','',2017),(12100915,'MUNICIPALIDAD DE HUITAN','',2017),(12100916,'MUNICIPALIDAD DE ZUNIL','',2017),(12100917,'MUNICIPALIDAD DE COLOMBA','',2017),(12100918,'MUNICIPALIDAD DE SAN FRANCISCO LA UNION','',2017),(12100919,'MUNICIPALIDAD DE EL PALMAR','',2017),(12100920,'MUNICIPALIDAD DE COATEPEQUE','',2017),(12100921,'MUNICIPALIDAD DE GENOVA','',2017),(12100922,'MUNICIPALIDAD DE FLORES COSTA CUCA','',2017),(12100923,'MUNICIPALIDAD DE LA ESPERANZA','',2017),(12100924,'MUNICIPALIDAD DE PALESTINA DE LOS ALTOS','',2017),(12101001,'MUNICIPALIDAD DE MAZATENANGO','',2017),(12101002,'MUNICIPALIDAD DE CUYOTENANGO','',2017),(12101003,'MUNICIPALIDAD DE SAN FRANCISCO ZAPOTITLAN','',2017),(12101004,'MUNICIPALIDAD DE SAN BERNARDINO','',2017),(12101005,'MUNICIPALIDAD DE SAN JOSE EL IDOLO','',2017),(12101006,'MUNICIPALIDAD DE SANTO DOMINGO SUCHITEPEQUEZ','',2017),(12101007,'MUNICIPALIDAD DE SAN LORENZO','',2017),(12101008,'MUNICIPALIDAD DE SAMAYAC','',2017),(12101009,'MUNICIPALIDAD DE SAN PABLO JOCOPILAS','',2017),(12101010,'MUNICIPALIDAD DE SAN ANTONIO SUCHITEPEQUEZ','',2017),(12101011,'MUNICIPALIDAD DE SAN MIGUEL PANAN','',2017),(12101012,'MUNICIPALIDAD DE SAN GABRIEL','',2017),(12101013,'MUNICIPALIDAD DE CHICACAO','',2017),(12101014,'MUNICIPALIDAD DE PATULUL','',2017),(12101015,'MUNICIPALIDAD DE SANTA BARBARA','',2017),(12101016,'MUNICIPALIDAD DE SAN JUAN BAUTISTA','',2017),(12101017,'MUNICIPALIDAD DE SANTO TOMAS LA UNION','',2017),(12101018,'MUNICIPALIDAD DE ZUNILITO','',2017),(12101019,'MUNICIPALIDAD DE PUEBLO NUEVO','',2017),(12101020,'MUNICIPALIDAD DE RIO BRAVO','',2017),(12101021,'MUNICIPALIDAD DE SAN JOSE LA MAQUINA','',2017),(12101101,'MUNICIPALIDAD DE RETALHULEU','',2017),(12101102,'MUNICIPALIDAD DE SAN SEBASTIAN','',2017),(12101103,'MUNICIPALIDAD DE SANTA CRUZ MULUA','',2017),(12101104,'MUNICIPALIDAD DE SAN MARTIN ZAPOTITLAN','',2017),(12101105,'MUNICIPALIDAD DE SAN FELIPE','',2017),(12101106,'MUNICIPALIDAD DE SAN ANDRES VILLA SECA','',2017),(12101107,'MUNICIPALIDAD DE CHAMPERICO','',2017),(12101108,'MUNICIPALIDAD DE NUEVO SAN CARLOS','',2017),(12101109,'MUNICIPALIDAD DE EL ASINTAL','',2017),(12101201,'MUNICIPALIDAD DE SAN MARCOS','',2017),(12101202,'MUNICIPALIDAD DE SAN PEDRO SACATEPEQUEZ','',2017),(12101203,'MUNICIPALIDAD DE SAN ANTONIO SACATEPEQUEZ','',2017),(12101204,'MUNICIPALIDAD DE COMITANCILLO','',2017),(12101205,'MUNICIPALIDAD DE SAN MIGUEL IXTAHUACAN','',2017),(12101206,'MUNICIPALIDAD DE CONCEPCION TUTUAPA','',2017),(12101207,'MUNICIPALIDAD DE TACANA','',2017),(12101208,'MUNICIPALIDAD DE SIBINAL','',2017),(12101209,'MUNICIPALIDAD DE TAJUMULCO','',2017),(12101210,'MUNICIPALIDAD DE TEJUTLA','',2017),(12101211,'MUNICIPALIDAD DE SAN RAFAEL PIE DE LA CUESTA','',2017),(12101212,'MUNICIPALIDAD DE NUEVO PROGRESO','',2017),(12101213,'MUNICIPALIDAD DE EL TUMBADOR','',2017),(12101214,'MUNICIPALIDAD DE EL RODEO','',2017),(12101215,'MUNICIPALIDAD DE MALACATAN','',2017),(12101216,'MUNICIPALIDAD DE CATARINA','',2017),(12101217,'MUNICIPALIDAD DE AYUTLA','',2017),(12101218,'MUNICIPALIDAD DE OCOS','',2017),(12101219,'MUNICIPALIDAD DE SAN PABLO','',2017),(12101220,'MUNICIPALIDAD DE EL QUETZAL','',2017),(12101221,'MUNICIPALIDAD DE LA REFORMA','',2017),(12101222,'MUNICIPALIDAD DE PAJAPITA','',2017),(12101223,'MUNICIPALIDAD DE IXCHIGUAN','',2017),(12101224,'MUNICIPALIDAD DE SAN JOSE OJETENAM','',2017),(12101225,'MUNICIPALIDAD DE SAN CRISTOBAL CUCHO','',2017),(12101226,'MUNICIPALIDAD DE SIPACAPA','',2017),(12101227,'MUNICIPALIDAD DE ESQUIPULAS PALO GORDO','',2017),(12101228,'MUNICIPALIDAD DE RIO BLANCO','',2017),(12101229,'MUNICIPALIDAD DE SAN LORENZO','',2017),(12101230,'MUNICIPALIDAD DE LA BLANCA','',2017),(12101301,'MUNICIPALIDAD DE HUEHUETENANGO','',2017),(12101302,'MUNICIPALIDAD DE CHIANTLA','',2017),(12101303,'MUNICIPALIDAD DE MALACATANCITO','',2017),(12101304,'MUNICIPALIDAD DE CUILCO','',2017),(12101305,'MUNICIPALIDAD DE NENTON','',2017),(12101306,'MUNICIPALIDAD DE SAN PEDRO NECTA','',2017),(12101307,'MUNICIPALIDAD DE JACALTENANGO','',2017),(12101308,'MUNICIPALIDAD DE SOLOMA','',2017),(12101309,'MUNICIPALIDAD DE SAN ILDEFONSO IXTAHUACAN','',2017),(12101310,'MUNICIPALIDAD DE SANTA BARBARA','',2017),(12101311,'MUNICIPALIDAD DE LA LIBERTAD','',2017),(12101312,'MUNICIPALIDAD DE LA DEMOCRACIA','',2017),(12101313,'MUNICIPALIDAD DE SAN MIGUEL ACATAN','',2017),(12101314,'MUNICIPALIDAD DE SAN RAFAEL LA INDEPENDENCIA','',2017),(12101315,'MUNICIPALIDAD DE TODOS SANTOS CUCHUMATAN','',2017),(12101316,'MUNICIPALIDAD DE SAN JUAN ATITAN','',2017),(12101317,'MUNICIPALIDAD DE SANTA EULALIA','',2017),(12101318,'MUNICIPALIDAD DE SAN MATEO IXTATAN','',2017),(12101319,'MUNICIPALIDAD DE COLOTENANGO','',2017),(12101320,'MUNICIPALIDAD DE SAN SEBASTIAN HUEHUETENANGO','',2017),(12101321,'MUNICIPALIDAD DE TECTITAN','',2017),(12101322,'MUNICIPALIDAD DE CONCEPCION HUISTA','',2017),(12101323,'MUNICIPALIDAD DE SAN JUAN IXCOY','',2017),(12101324,'MUNICIPALIDAD DE SAN ANTONIO HUISTA','',2017),(12101325,'MUNICIPALIDAD DE SAN SEBASTIAN COATAN','',2017),(12101326,'MUNICIPALIDAD DE BARILLAS','',2017),(12101327,'MUNICIPALIDAD DE AGUACATAN','',2017),(12101328,'MUNICIPALIDAD DE SAN RAFAEL PETZAL','',2017),(12101329,'MUNICIPALIDAD DE SAN GASPAR IXCHIL','',2017),(12101330,'MUNICIPALIDAD DE SANTIAGO CHIMALTENANGO','',2017),(12101331,'MUNICIPALIDAD DE SANTA ANA HUISTA','',2017),(12101332,'MUNICIPALIDAD DE UNION CANTINIL','',2017),(12101333,'MUNICIPALIDAD DE PETATÁN','',2017),(12101401,'MUNICIPALIDAD DE SANTA CRUZ DEL QUICHE','',2017),(12101402,'MUNICIPALIDAD DE CHICHE','',2017),(12101403,'MUNICIPALIDAD DE CHINIQUE','',2017),(12101404,'MUNICIPALIDAD DE ZACUALPA','',2017),(12101405,'MUNICIPALIDAD DE CHAJUL','',2017),(12101406,'MUNICIPALIDAD DE CHICHICASTENANGO','',2017),(12101407,'MUNICIPALIDAD DE PATZITE','',2017),(12101408,'MUNICIPALIDAD DE SAN ANTONIO ILOTENANGO','',2017),(12101409,'MUNICIPALIDAD DE SAN PEDRO JOCOPILAS','',2017),(12101410,'MUNICIPALIDAD DE CUNEN','',2017),(12101411,'MUNICIPALIDAD DE SAN JUAN COTZAL','',2017),(12101412,'MUNICIPALIDAD DE JOYABAJ','',2017),(12101413,'MUNICIPALIDAD DE NEBAJ','',2017),(12101414,'MUNICIPALIDAD DE SAN ANDRES SAJCABAJA','',2017),(12101415,'MUNICIPALIDAD DE USPANTAN','',2017),(12101416,'MUNICIPALIDAD DE SACAPULAS','',2017),(12101417,'MUNICIPALIDAD DE SAN BARTOLOME JOCOTENANGO','',2017),(12101418,'MUNICIPALIDAD DE CANILLA','',2017),(12101419,'MUNICIPALIDAD DE CHICAMAN','',2017),(12101420,'MUNICIPALIDAD DE IXCAN','',2017),(12101421,'MUNICIPALIDAD DE PACHALUM','',2017),(12101501,'MUNICIPALIDAD DE SALAMA','',2017),(12101502,'MUNICIPALIDAD DE SAN MIGUEL CHICAJ','',2017),(12101503,'MUNICIPALIDAD DE RABINAL','',2017),(12101504,'MUNICIPALIDAD DE CUBULCO','',2017),(12101505,'MUNICIPALIDAD DE GRANADOS','',2017),(12101506,'MUNICIPALIDAD DE EL CHOL','',2017),(12101507,'MUNICIPALIDAD DE SAN JERONIMO','',2017),(12101508,'MUNICIPALIDAD DE PURULHA','',2017),(12101601,'MUNICIPALIDAD DE COBAN','',2017),(12101602,'MUNICIPALIDAD DE SANTA CRUZ VERAPAZ','',2017),(12101603,'MUNICIPALIDAD DE SAN CRISTOBAL VERAPAZ','',2017),(12101604,'MUNICIPALIDAD DE TACTIC','',2017),(12101605,'MUNICIPALIDAD DE TAMAHU','',2017),(12101606,'MUNICIPALIDAD DE TUCURU','',2017),(12101607,'MUNICIPALIDAD DE PANZOS','',2017),(12101608,'MUNICIPALIDAD DE SENAHU','',2017),(12101609,'MUNICIPALIDAD DE SAN PEDRO CARCHA','',2017),(12101610,'MUNICIPALIDAD DE SAN JUAN CHAMELCO','',2017),(12101611,'MUNICIPALIDAD DE LANQUIN','',2017),(12101612,'MUNICIPALIDAD DE CAHABON','',2017),(12101613,'MUNICIPALIDAD DE CHISEC','',2017),(12101614,'MUNICIPALIDAD DE CHAHAL','',2017),(12101615,'MUNICIPALIDAD DE FRAY BARTOLOME DE LAS CASAS','',2017),(12101616,'MUNICIPALIDAD DE SANTA CATALINA LA TINTA','',2017),(12101617,'MUNICIPALIDAD DE RAXRUHA','',2017),(12101701,'MUNICIPALIDAD DE FLORES','',2017),(12101702,'MUNICIPALIDAD DE SAN JOSE','',2017),(12101703,'MUNICIPALIDAD DE SAN BENITO','',2017),(12101704,'MUNICIPALIDAD DE SAN ANDRES','',2017),(12101705,'MUNICIPALIDAD DE LA LIBERTAD','',2017),(12101706,'MUNICIPALIDAD DE SAN FRANCISCO','',2017),(12101707,'MUNICIPALIDAD DE SANTA ANA','',2017),(12101708,'MUNICIPALIDAD DE DOLORES','',2017),(12101709,'MUNICIPALIDAD DE SAN LUIS','',2017),(12101710,'MUNICIPALIDAD DE SAYAXCHE','',2017),(12101711,'MUNICIPALIDAD DE MELCHOR DE MENCOS','',2017),(12101712,'MUNICIPALIDAD DE POPTUN','',2017),(12101713,'MUNICIPALIDAD DE LAS CRUCES','',2017),(12101714,'MUNICIPALIDAD DE EL CHAL','',2017),(12101801,'MUNICIPALIDAD DE PUERTO BARRIOS','',2017),(12101802,'MUNICIPALIDAD DE LIVINGSTON','',2017),(12101803,'MUNICIPALIDAD DE EL ESTOR','',2017),(12101804,'MUNICIPALIDAD DE MORALES','',2017),(12101805,'MUNICIPALIDAD DE LOS AMATES','',2017),(12101901,'MUNICIPALIDAD DE ZACAPA','',2017),(12101902,'MUNICIPALIDAD DE ESTANZUELA','',2017),(12101903,'MUNICIPALIDAD DE RIO HONDO','',2017),(12101904,'MUNICIPALIDAD DE GUALAN','',2017),(12101905,'MUNICIPALIDAD DE TECULUTAN','',2017),(12101906,'MUNICIPALIDAD DE USUMATLAN','',2017),(12101907,'MUNICIPALIDAD DE CABAÑAS','',2017),(12101908,'MUNICIPALIDAD DE SAN DIEGO','',2017),(12101909,'MUNICIPALIDAD DE LA UNION','',2017),(12101910,'MUNICIPALIDAD DE HUITE','',2017),(12101911,'MUNICIPALIDAD DE SAN JORGE','',2017),(12102001,'MUNICIPALIDAD DE CHIQUIMULA','',2017),(12102002,'MUNICIPALIDAD DE SAN JOSE LA ARADA','',2017),(12102003,'MUNICIPALIDAD DE SAN JUAN ERMITA','',2017),(12102004,'MUNICIPALIDAD DE JOCOTAN','',2017),(12102005,'MUNICIPALIDAD DE CAMOTAN','',2017),(12102006,'MUNICIPALIDAD DE OLOPA','',2017),(12102007,'MUNICIPALIDAD DE ESQUIPULAS','',2017),(12102008,'MUNICIPALIDAD DE CONCEPCION LAS MINAS','',2017),(12102009,'MUNICIPALIDAD DE QUEZALTEPEQUE','',2017),(12102010,'MUNICIPALIDAD DE SAN JACINTO','',2017),(12102011,'MUNICIPALIDAD DE IPALA','',2017),(12102101,'MUNICIPALIDAD DE JALAPA','',2017),(12102102,'MUNICIPALIDAD DE SAN PEDRO PINULA','',2017),(12102103,'MUNICIPALIDAD DE SAN LUIS JILOTEPEQUE','',2017),(12102104,'MUNICIPALIDAD DE SAN MANUEL CHAPARRON','',2017),(12102105,'MUNICIPALIDAD DE SAN CARLOS ALZATATE','',2017),(12102106,'MUNICIPALIDAD DE MONJAS','',2017),(12102107,'MUNICIPALIDAD DE MATAQUESCUINTLA','',2017),(12102201,'MUNICIPALIDAD DE JUTIAPA','',2017),(12102202,'MUNICIPALIDAD DE EL PROGRESO','',2017),(12102203,'MUNICIPALIDAD DE SANTA CATARINA MITA','',2017),(12102204,'MUNICIPALIDAD DE AGUA BLANCA','',2017),(12102205,'MUNICIPALIDAD DE ASUNCION MITA','',2017),(12102206,'MUNICIPALIDAD DE YUPILTEPEQUE','',2017),(12102207,'MUNICIPALIDAD DE ATESCATEMPA','',2017),(12102208,'MUNICIPALIDAD DE JEREZ','',2017),(12102209,'MUNICIPALIDAD DE EL ADELANTO','',2017),(12102210,'MUNICIPALIDAD DE ZAPOTITLAN','',2017),(12102211,'MUNICIPALIDAD DE COMAPA','',2017),(12102212,'MUNICIPALIDAD DE JALPATAGUA','',2017),(12102213,'MUNICIPALIDAD DE CONGUACO','',2017),(12102214,'MUNICIPALIDAD DE MOYUTA','',2017),(12102215,'MUNICIPALIDAD DE PASACO','',2017),(12102216,'MUNICIPALIDAD DE SAN JOSE ACATEMPA','',2017),(12102217,'MUNICIPALIDAD DE QUESADA','',2017),(12102218,'MUNICIPALIDAD DE PETATÁN','',2017),(12200001,'ENTIDAD MET. REGUL. DE TRANSPORTE Y TRANSITO DEL MUN. DE GUAT. Y SUS AREAS DE INFLUENCIA U.','',2017),(12200597,'OFI. ASESORA DE R.R.H.H. DE LAS MUNI. -OARHM-','',2017),(12300599,'PLAN DE PRESTACIONES DEL EMPLEADO MUNICIPAL (PPEM)','',2017),(12400001,'MANCOMUNIDAD DE MUNICIPIOS DE DESARROLLO INTEGRAL DE LA CUENCA COPAN CH ORTI','',2017),(12400002,'MANCOMUNIDAD ENCUENTRO REGIONAL IXIL POR LA PAZ (ERIPAZ)','',2017),(12400003,'MANCOMUNIDAD LAGUNA GUIJA','',2017),(12400004,'MANCOMUNIDAD DE NOR-ORIENTE','',2017),(12400005,'MANCOMUNIDAD DE MUNICIPIOS DE LA CUENCA DEL RIO EL NARANJO (MANCUERNA)','',2017),(12400006,'MANCOMUNIDAD DE MUNICIPIOS METROPOLI DE LOS ALTOS','',2017),(12400007,'MANCOMUNIDAD DE MUNICIPIOS FRONTERA DEL NORTE','',2017),(12400008,'ASOCIACION DE MUNICIPIOS EN EL CORAZON DE LA ZONA PAZ (MUNICOPAZ)','',2017),(12400009,'ASOCIACION NACIONAL DE MUNICIPALIDADES (ANAM)','',2017),(12400010,'MANCOMUNIDAD DE MUNICIPIOS PARA EL DESARROLLO INTEGRAL DEL AREA POQOMCHI','',2017),(12400011,'MANCOMUNIDAD AREA MAM DE QUETZALTENANGO (MAMQ)','',2017),(12400012,'MANCOMUNIDAD DE MUNICIPIOS DE LA FRANJA TRANSVERSAL DEL NORTE','',2017),(12400013,'MANCOMUNIDAD TRINACIONAL FRONTERIZA RIO LEMPA','',2017),(12400014,'MANCOMUNIDAD DEL CONO SUR, JUTIAPA','',2017),(12400015,'MANCOMUNIDAD MONTAÑA EL GIGANTE','',2017),(12400017,'MANCOMUNIDAD DE MUNICIPIOS KAKCHIQUEL CHICHOY Y ATITLAN (MANKATITLAN)','',2017),(12400018,'MANCOMUNIDAD TZOLOJYA','',2017),(12400019,'MANCOMUNIDAD HUISTA','',2017),(12400020,'MANCOMUNIDAD DEL SUR ORIENTE','',2017),(12400021,'ASOCIACIÓN DE DESARROLLO INTEGRAL DE MUNICIPALIDADES DEL ALTIPLANO MÁRQUENSE (ADIMAM)','',2017),(12400022,'MANCOMUNIDAD GRAN CIUDAD DEL SUR DEL DEPARTAMENTO DE GUATEMALA','',2017),(12400023,'MANCOMUNIDAD JALAPA UNIDA POR LA SEGURIDAD ALIMENTARIA Y NUTRICIONAL','',2017),(12400024,'MANCOMUNIDAD DE MUNICIPIOS PARA EL DESARROLLO LOCAL SOSTENIBLE EL PACIFICO','',2017),(12400025,'MANCOMUNIDAD DE MUNICIPIOS DEL CORREDOR SECO DEL DEPARTAMENTO DE QUICHE (MANCOSEQ)','',2017),(21100072,'EMPRESA GUAT. DE TELECOM. -GUATEL-','',2017),(21100073,'Z. LIB. DE IND. Y COM. STO. T. DE CAST. ZOLIC','',2017),(21100074,'EMP. PORT. NAC. STO. T. DE CAST. EMPORNAC','',2017),(21100075,'EMPRESA PORTUARIA QUETZAL -EPQ-','',2017),(21100076,'EMP. PORT. NAC. DE CHAMPERICO -EPNCH-','',2017),(21100077,'EMPRESA FERROCARRILES DE GUATEMALA  -FEGUA-','',2017),(21100078,'INST. NAC. DE COMER. AGRICOLA -INDECA-','',2017),(21100080,'INST. NACIONAL DE ELECTRIFICACISN -INDE','',2017),(21200090,'EMPRESA MUNICIPAL DE AGUA -EMPAGUA-','',2017),(21200091,'EMP. ELECTRICA MUNI. DE HUEHUETENANGO','',2017),(21200092,'EMPRESA ELICTRICA MUNICIPAL DE JALAPA','',2017),(21200093,'EMP. ELECTRICA MUNI. DE SAN PEDRO PINULA','',2017),(21200094,'EMPRESA ELECTRICA MUNICIPAL DE ZACAPA','',2017),(21200095,'EMP. HIDROELICTRICA MUNI. DE EL PROGRESO','',2017),(21200096,'EMP. HIDROELICTRICA MUNI. DE RETALHULEU','',2017),(21200097,'EMPRESA MUNICIPAL DE TRANSPORTE DE LA CIUDAD DE GUATEMALA','',2017),(21200098,'EMPRESA MUNICIPAL RURAL DE ELECTRICIDAD IXCAN (EMRE)','',2017),(21200099,'EMPRESA MUNICIPAL DE AGUA POTABLE Y ALCANTARILLADO (EMAPET), FLORES-SAN BENITO, PETÉN','',2017),(21200100,'EMPRESA ELECTRICA DE GUALAN','',2017),(21200101,'EMPRESA ELECTRICA MUNICIPAL DE PUERTO BARRIOS','',2017),(21200102,'EMPRESA METROPOLITANA DE VIVIENDA Y DESARROLLO URBANO','',2017),(21200103,'EMPRESA PUBLICA MUNICIPAL AGROINDUSTRIAL DE ESTANZUELA, DEPARTAMENTO DE ZACAPA','',2017),(21200104,'EMPRESA MUNICIPAL DE AGUA JALAPAGUA','',2017),(21200105,'EMPRESA MUNICIPAL DE MANEJO INTEGRAL DE RESIDUOS SOLIDOS DE JALAPA DEPARTAMENTO DE JALAPA','',2017),(22110620,'INST. DE FOMENTO DE HIPOTECAS ASEGURADAS -FHA-','',2017),(22110621,'CORPORACION FINANCIERA NACIONAL  -CORFINA-','',2017),(22210033,'SUPERINTENDENCIA DE BANCOS -SIB-','',2017),(22210630,'EL CRÉDITO HIPOTECARIO NACIONAL DE GUATEMALA (CHN)','',2017),(22210633,'BANCO DE GUATEMALA','',2017);
/*!40000 ALTER TABLE `entidad` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `estado`
--

DROP TABLE IF EXISTS `estado`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `estado` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(30) COLLATE utf8_bin NOT NULL,
  `valor` int(2) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `estado`
--

LOCK TABLES `estado` WRITE;
/*!40000 ALTER TABLE `estado` DISABLE KEYS */;
INSERT INTO `estado` VALUES (1,'Activo',1),(2,'Congelado',2),(3,'Historico',3),(4,'Inactivo',0);
/*!40000 ALTER TABLE `estado` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `estado_informe`
--

DROP TABLE IF EXISTS `estado_informe`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `estado_informe` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) COLLATE utf8_bin NOT NULL,
  `tipo_informe` int(11) NOT NULL DEFAULT 0,
  `usuario_creo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NULL DEFAULT NULL,
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  `estado` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `estado_informe`
--

LOCK TABLES `estado_informe` WRITE;
/*!40000 ALTER TABLE `estado_informe` DISABLE KEYS */;
/*!40000 ALTER TABLE `estado_informe` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `estado_tabla`
--

DROP TABLE IF EXISTS `estado_tabla`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `estado_tabla` (
  `usuario` varchar(30) COLLATE utf8_bin NOT NULL,
  `tabla` varchar(50) COLLATE utf8_bin NOT NULL,
  `valores` varchar(1000) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`usuario`,`tabla`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `estado_tabla`
--

LOCK TABLES `estado_tabla` WRITE;
/*!40000 ALTER TABLE `estado_tabla` DISABLE KEYS */;
/*!40000 ALTER TABLE `estado_tabla` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `etiqueta`
--

DROP TABLE IF EXISTS `etiqueta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `etiqueta` (
  `id` int(2) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(1000) COLLATE utf8_bin NOT NULL,
  `descripcion` varchar(2000) COLLATE utf8_bin DEFAULT NULL,
  `proyecto` varchar(50) COLLATE utf8_bin NOT NULL,
  `color_principal` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  `estado` int(2) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `etiqueta`
--

LOCK TABLES `etiqueta` WRITE;
/*!40000 ALTER TABLE `etiqueta` DISABLE KEYS */;
INSERT INTO `etiqueta` VALUES (1,'Prestamo','Prestamo','PEP','#1f3b6a','admin','','2017-10-09 20:33:34',NULL,1),(2,'PAI','PAI','Proyecto','#204d74','admin','','2017-10-09 20:33:34',NULL,1);
/*!40000 ALTER TABLE `etiqueta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `formulario`
--

DROP TABLE IF EXISTS `formulario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `formulario` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `codigo` varchar(30) COLLATE utf8_bin NOT NULL,
  `descripcion` varchar(4000) COLLATE utf8_bin NOT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  `estado` int(2) NOT NULL,
  `formulario_tipoid` int(2) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKformulario341300` (`formulario_tipoid`),
  CONSTRAINT `FKformulario341300` FOREIGN KEY (`formulario_tipoid`) REFERENCES `formulario_tipo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `formulario`
--

LOCK TABLES `formulario` WRITE;
/*!40000 ALTER TABLE `formulario` DISABLE KEYS */;
/*!40000 ALTER TABLE `formulario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `formulario_item`
--

DROP TABLE IF EXISTS `formulario_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `formulario_item` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `texto` varchar(4000) COLLATE utf8_bin NOT NULL,
  `formularioid` int(10) NOT NULL,
  `orden` int(3) NOT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_actualizacion` int(10) DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  `estado` int(2) NOT NULL,
  `formulario_item_tipoid` int(10) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKformulario123316` (`formulario_item_tipoid`),
  KEY `FKformulario449783` (`formularioid`),
  CONSTRAINT `FKformulario123316` FOREIGN KEY (`formulario_item_tipoid`) REFERENCES `formulario_item_tipo` (`id`),
  CONSTRAINT `FKformulario449783` FOREIGN KEY (`formularioid`) REFERENCES `formulario` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `formulario_item`
--

LOCK TABLES `formulario_item` WRITE;
/*!40000 ALTER TABLE `formulario_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `formulario_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `formulario_item_opcion`
--

DROP TABLE IF EXISTS `formulario_item_opcion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `formulario_item_opcion` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `formulario_itemid` int(10) NOT NULL,
  `etiqueta` varchar(4000) COLLATE utf8_bin NOT NULL,
  `valor_entero` int(4) DEFAULT NULL,
  `valor_string` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `valor_tiempo` timestamp NULL DEFAULT NULL,
  `valor_decimal` decimal(15,2) DEFAULT NULL,
  `estado` int(2) NOT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_actualizacion` int(11) DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKformulario718877` (`formulario_itemid`),
  CONSTRAINT `FKformulario718877` FOREIGN KEY (`formulario_itemid`) REFERENCES `formulario_item` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `formulario_item_opcion`
--

LOCK TABLES `formulario_item_opcion` WRITE;
/*!40000 ALTER TABLE `formulario_item_opcion` DISABLE KEYS */;
/*!40000 ALTER TABLE `formulario_item_opcion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `formulario_item_tipo`
--

DROP TABLE IF EXISTS `formulario_item_tipo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `formulario_item_tipo` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(1000) COLLATE utf8_bin NOT NULL,
  `descripcion` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `estado` int(2) DEFAULT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `usuario_actualizacion` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  `dato_tipoid` int(10) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKformulario967906` (`dato_tipoid`),
  CONSTRAINT `FKformulario967906` FOREIGN KEY (`dato_tipoid`) REFERENCES `dato_tipo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `formulario_item_tipo`
--

LOCK TABLES `formulario_item_tipo` WRITE;
/*!40000 ALTER TABLE `formulario_item_tipo` DISABLE KEYS */;
/*!40000 ALTER TABLE `formulario_item_tipo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `formulario_item_valor`
--

DROP TABLE IF EXISTS `formulario_item_valor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `formulario_item_valor` (
  `formulario_itemid` int(10) NOT NULL,
  `objeto_formularioformularioid` int(10) NOT NULL,
  `objeto_formularioobjeto_tipoid` int(2) NOT NULL,
  `valor_entero` int(10) NOT NULL,
  `valor_string` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `valor_tiempo` timestamp NULL DEFAULT NULL,
  `valor_decimal` decimal(15,2) DEFAULT NULL,
  `proyectoid` int(10) NOT NULL,
  `componenteid` int(10) NOT NULL,
  `productoid` int(10) NOT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  `objeto_formularioobjeto_id` int(10) NOT NULL,
  PRIMARY KEY (`formulario_itemid`,`objeto_formularioformularioid`,`objeto_formularioobjeto_tipoid`,`objeto_formularioobjeto_id`),
  KEY `FKformulario508490` (`formulario_itemid`),
  KEY `FKformulario833430` (`objeto_formularioformularioid`,`objeto_formularioobjeto_tipoid`,`objeto_formularioobjeto_id`),
  CONSTRAINT `FKformulario508490` FOREIGN KEY (`formulario_itemid`) REFERENCES `formulario_item` (`id`),
  CONSTRAINT `FKformulario833430` FOREIGN KEY (`objeto_formularioformularioid`, `objeto_formularioobjeto_tipoid`, `objeto_formularioobjeto_id`) REFERENCES `objeto_formulario` (`formularioid`, `objeto_tipo`, `objeto_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `formulario_item_valor`
--

LOCK TABLES `formulario_item_valor` WRITE;
/*!40000 ALTER TABLE `formulario_item_valor` DISABLE KEYS */;
/*!40000 ALTER TABLE `formulario_item_valor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `formulario_tipo`
--

DROP TABLE IF EXISTS `formulario_tipo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `formulario_tipo` (
  `id` int(2) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(1000) COLLATE utf8_bin NOT NULL,
  `descripcion` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `usario_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  `estado` int(2) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `formulario_tipo`
--

LOCK TABLES `formulario_tipo` WRITE;
/*!40000 ALTER TABLE `formulario_tipo` DISABLE KEYS */;
/*!40000 ALTER TABLE `formulario_tipo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hito`
--

DROP TABLE IF EXISTS `hito`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hito` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(1000) COLLATE utf8_bin NOT NULL,
  `descripcion` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  `estado` int(2) NOT NULL,
  `fecha` timestamp NOT NULL DEFAULT current_timestamp(),
  `proyectoid` int(10) NOT NULL,
  `hito_tipoid` int(10) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKhito29799` (`proyectoid`),
  KEY `FKhito59625` (`hito_tipoid`),
  CONSTRAINT `FKhito29799` FOREIGN KEY (`proyectoid`) REFERENCES `proyecto` (`id`),
  CONSTRAINT `FKhito59625` FOREIGN KEY (`hito_tipoid`) REFERENCES `hito_tipo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hito`
--

LOCK TABLES `hito` WRITE;
/*!40000 ALTER TABLE `hito` DISABLE KEYS */;
/*!40000 ALTER TABLE `hito` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hito_resultado`
--

DROP TABLE IF EXISTS `hito_resultado`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hito_resultado` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `valor_entero` int(10) DEFAULT NULL,
  `valor_string` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `valor_decimal` decimal(15,2) DEFAULT NULL,
  `valor_tiempo` timestamp NULL DEFAULT NULL,
  `comentario` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `hitoid` int(10) NOT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  `estado` int(2) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKhito_resul737850` (`hitoid`),
  CONSTRAINT `FKhito_resul737850` FOREIGN KEY (`hitoid`) REFERENCES `hito` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hito_resultado`
--

LOCK TABLES `hito_resultado` WRITE;
/*!40000 ALTER TABLE `hito_resultado` DISABLE KEYS */;
/*!40000 ALTER TABLE `hito_resultado` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hito_tipo`
--

DROP TABLE IF EXISTS `hito_tipo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hito_tipo` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(1000) COLLATE utf8_bin DEFAULT NULL,
  `descripcion` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `estado` int(2) NOT NULL,
  `dato_tipoid` int(10) NOT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_actualizacion` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKhito_tipo850233` (`dato_tipoid`),
  CONSTRAINT `FKhito_tipo850233` FOREIGN KEY (`dato_tipoid`) REFERENCES `dato_tipo` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hito_tipo`
--

LOCK TABLES `hito_tipo` WRITE;
/*!40000 ALTER TABLE `hito_tipo` DISABLE KEYS */;
INSERT INTO `hito_tipo` VALUES (1,'General','General',1,4,'admin',NULL,'2017-10-02 14:13:51',NULL);
/*!40000 ALTER TABLE `hito_tipo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `informe_presupuesto`
--

DROP TABLE IF EXISTS `informe_presupuesto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `informe_presupuesto` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_prestamo` int(11) NOT NULL,
  `tipo_presupuesto` int(11) DEFAULT NULL,
  `objeto_tipo_id` int(11) NOT NULL,
  `objeto_tipo` int(11) NOT NULL,
  `posicion_arbol` int(11) DEFAULT NULL,
  `objeto_nombre` varchar(1000) COLLATE utf8_bin DEFAULT NULL,
  `id_predecesor` int(11) NOT NULL,
  `objeto_tipo_predecesor` int(11) NOT NULL,
  `mes1` decimal(11,2) DEFAULT 0.00,
  `mes2` decimal(11,2) DEFAULT 0.00,
  `mes3` decimal(11,2) DEFAULT 0.00,
  `mes4` decimal(11,2) DEFAULT 0.00,
  `mes5` decimal(11,2) DEFAULT 0.00,
  `mes6` decimal(11,2) DEFAULT 0.00,
  `mes7` decimal(11,2) DEFAULT 0.00,
  `mes8` decimal(11,2) DEFAULT 0.00,
  `mes9` decimal(11,2) DEFAULT 0.00,
  `mes10` decimal(11,2) DEFAULT 0.00,
  `mes11` decimal(11,2) DEFAULT 0.00,
  `mes12` decimal(11,2) DEFAULT 0.00,
  `total` decimal(11,2) DEFAULT 0.00,
  `anio` timestamp NULL DEFAULT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NULL DEFAULT NULL,
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  `estado` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `informe_presupuesto_estado_idx` (`tipo_presupuesto`),
  KEY `informe_presupuesto_estado` (`tipo_presupuesto`),
  CONSTRAINT `informe_presupuesto_estado` FOREIGN KEY (`tipo_presupuesto`) REFERENCES `estado_informe` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `informe_presupuesto`
--

LOCK TABLES `informe_presupuesto` WRITE;
/*!40000 ALTER TABLE `informe_presupuesto` DISABLE KEYS */;
/*!40000 ALTER TABLE `informe_presupuesto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `interes_tipo`
--

DROP TABLE IF EXISTS `interes_tipo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `interes_tipo` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) COLLATE utf8_bin NOT NULL,
  `descripcion` varchar(1000) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `interes_tipo`
--

LOCK TABLES `interes_tipo` WRITE;
/*!40000 ALTER TABLE `interes_tipo` DISABLE KEYS */;
/*!40000 ALTER TABLE `interes_tipo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `linea_base`
--

DROP TABLE IF EXISTS `linea_base`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `linea_base` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) COLLATE utf8_bin NOT NULL,
  `proyectoid` int(10) NOT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  `tipo_linea_base` int(2) NOT NULL COMMENT 'tipo_lina_base: 1\nliena_base, 2: congelado',
  `sobreescribir` int(2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_linea_base_1345_idx` (`proyectoid`),
  CONSTRAINT `fk_linea_base_1345` FOREIGN KEY (`proyectoid`) REFERENCES `proyecto` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `linea_base`
--

LOCK TABLES `linea_base` WRITE;
/*!40000 ALTER TABLE `linea_base` DISABLE KEYS */;
/*!40000 ALTER TABLE `linea_base` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `matriz_raci`
--

DROP TABLE IF EXISTS `matriz_raci`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `matriz_raci` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `proyectoid` int(10) NOT NULL,
  `estado` int(2) NOT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_matriz_raci_1_idx` (`proyectoid`),
  KEY `fk_matriz_raci_1` (`proyectoid`),
  CONSTRAINT `fk_matriz_raci_1` FOREIGN KEY (`proyectoid`) REFERENCES `proyecto` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `matriz_raci`
--

LOCK TABLES `matriz_raci` WRITE;
/*!40000 ALTER TABLE `matriz_raci` DISABLE KEYS */;
/*!40000 ALTER TABLE `matriz_raci` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `meta`
--

DROP TABLE IF EXISTS `meta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `meta` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(1000) COLLATE utf8_bin NOT NULL,
  `descripcion` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  `estado` int(2) DEFAULT NULL,
  `meta_unidad_medidaid` int(10) NOT NULL,
  `dato_tipoid` int(10) NOT NULL,
  `objeto_id` int(11) DEFAULT NULL,
  `objeto_tipo` int(11) DEFAULT NULL,
  `meta_final_entero` int(10) DEFAULT NULL,
  `meta_final_string` text COLLATE utf8_bin DEFAULT NULL,
  `meta_final_decimal` decimal(15,2) DEFAULT NULL,
  `meta_final_fecha` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKmeta336167` (`dato_tipoid`),
  KEY `FKmeta832527` (`meta_unidad_medidaid`),
  CONSTRAINT `FKmeta336167` FOREIGN KEY (`dato_tipoid`) REFERENCES `dato_tipo` (`id`),
  CONSTRAINT `FKmeta832527` FOREIGN KEY (`meta_unidad_medidaid`) REFERENCES `meta_unidad_medida` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `meta`
--

LOCK TABLES `meta` WRITE;
/*!40000 ALTER TABLE `meta` DISABLE KEYS */;
/*!40000 ALTER TABLE `meta` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER tr_meta_insert
AFTER INSERT ON sipro.meta FOR EACH ROW
BEGIN
	DECLARE v_version int;
	SELECT max(a.version) INTO v_version FROM sipro_history.meta a WHERE a.id=NEW.id;

    IF(v_version is null) THEN
		SET v_version=1;
	END IF;

    INSERT INTO sipro_history.meta VALUE(NEW.id,NEW.nombre,NEW.descripcion,NEW.usuario_creo,NEW.usuario_actualizo,NEW.fecha_creacion,NEW.fecha_actualizacion,NEW.estado,NEW.meta_unidad_medidaid,NEW.dato_tipoid,NEW.objeto_id,NEW.objeto_tipo,NEW.meta_final_entero,NEW.meta_final_string,NEW.meta_final_decimal,NEW.meta_final_fecha, v_version, NULL, 1);

END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER tr_meta_update
AFTER UPDATE ON sipro.meta FOR EACH ROW
BEGIN
		DECLARE v_version int;
		SELECT max(a.version) INTO v_version FROM sipro_history.meta a WHERE a.id=OLD.id;

		IF(v_version is null) THEN
            UPDATE sipro_history.meta SET actual=null WHERE id=OLD.id AND version is null;
            SET v_version=1;
		ELSE
            UPDATE sipro_history.meta SET actual=null WHERE id=OLD.id AND version=v_version;
            SET v_version=v_version+1;
        END IF;

        INSERT INTO sipro_history.meta VALUE(NEW.id,NEW.nombre,NEW.descripcion,NEW.usuario_creo,NEW.usuario_actualizo,NEW.fecha_creacion,NEW.fecha_actualizacion,NEW.estado,NEW.meta_unidad_medidaid,NEW.dato_tipoid,NEW.objeto_id,NEW.objeto_tipo,NEW.meta_final_entero,NEW.meta_final_string,NEW.meta_final_decimal,NEW.meta_final_fecha, v_version, NULL, 1);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER tr_meta_delete
BEFORE DELETE ON sipro.meta FOR EACH ROW
BEGIN
	DECLARE v_version int;
	SELECT max(a.version) INTO v_version FROM sipro_history.meta a WHERE a.id=OLD.id;

	IF(v_version is null) THEN
		SET v_version=1;
	ELSE
		UPDATE sipro_history.meta SET actual=null WHERE id=OLD.id AND version=v_version;
		SET v_version=v_version+1;
	END IF;

	INSERT INTO sipro_history.meta VALUE(OLD.id,OLD.nombre,OLD.descripcion,OLD.usuario_creo,OLD.usuario_actualizo,OLD.fecha_creacion,OLD.fecha_actualizacion,OLD.estado,OLD.meta_unidad_medidaid,OLD.dato_tipoid,OLD.objeto_id,OLD.objeto_tipo,OLD.meta_final_entero,OLD.meta_final_string,OLD.meta_final_decimal,OLD.meta_final_fecha, v_version, NULL, NULL);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `meta_avance`
--

DROP TABLE IF EXISTS `meta_avance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `meta_avance` (
  `metaid` int(10) NOT NULL,
  `fecha` timestamp NOT NULL DEFAULT current_timestamp(),
  `usuario` varchar(30) COLLATE utf8_bin NOT NULL,
  `valor_entero` int(10) DEFAULT NULL,
  `valor_string` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `valor_decimal` decimal(15,2) DEFAULT NULL,
  `valor_tiempo` timestamp NULL DEFAULT NULL,
  `estado` int(2) NOT NULL DEFAULT 1,
  `fecha_ingreso` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`metaid`,`fecha`),
  KEY `FKmeta_valor168661` (`metaid`),
  CONSTRAINT `FKmeta_valor168661` FOREIGN KEY (`metaid`) REFERENCES `meta` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `meta_avance`
--

LOCK TABLES `meta_avance` WRITE;
/*!40000 ALTER TABLE `meta_avance` DISABLE KEYS */;
/*!40000 ALTER TABLE `meta_avance` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER trigger_meta_avance_insert
AFTER INSERT ON sipro.meta_avance FOR EACH ROW
BEGIN
    DECLARE v_version int;
    SELECT max(a.version) INTO v_version FROM sipro_history.meta_avance a
    WHERE a.metaid=NEW.metaid AND a.fecha = NEW.fecha;

    IF(v_version is null) THEN
        SET v_version=1;
    END IF;

    INSERT INTO sipro_history.meta_avance
    VALUE(NEW.metaid,NEW.fecha,NEW.usuario,NEW.valor_entero,NEW.valor_string,
      NEW.valor_decimal,NEW.valor_tiempo,NEW.estado,NEW.fecha_ingreso,
      v_version, NULL, 1);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER trigger_meta_avance_update
AFTER UPDATE ON sipro.meta_avance FOR EACH ROW
BEGIN
      DECLARE v_version int;
      SELECT max(a.version) INTO v_version FROM sipro_history.meta_avance a
      WHERE a.metaid=OLD.metaid AND a.fecha = OLD.fecha;

      IF(v_version is null) THEN
          UPDATE sipro_history.meta_avance a SET actual=null
          WHERE a.metaid=OLD.metaid AND a.fecha = OLD.fecha;
          SET v_version=1;
      ELSE
          UPDATE sipro_history.meta_avance a SET actual=null
          WHERE a.metaid=OLD.metaid AND a.fecha = OLD.fecha AND version=v_version;
          SET v_version=v_version+1;
      END IF;

      INSERT INTO sipro_history.meta_avance
      VALUE(NEW.metaid,NEW.fecha,NEW.usuario,NEW.valor_entero,NEW.valor_string,
        NEW.valor_decimal,NEW.valor_tiempo,NEW.estado,NEW.fecha_ingreso,
         v_version, NULL, 1);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER trigger_meta_avance_delete
BEFORE DELETE ON sipro.meta_avance FOR EACH ROW
BEGIN
    DECLARE v_version int;
    SELECT max(a.version) INTO v_version FROM sipro_history.meta_avance a
    WHERE a.metaid=OLD.metaid AND a.fecha = OLD.fecha ;

    IF(v_version is null) THEN
        SET v_version=1;
    ELSE
        UPDATE sipro_history.meta_avance a SET actual=null
        WHERE a.metaid=OLD.metaid AND a.fecha = OLD.fecha  AND version=v_version;
        SET v_version=v_version+1;
    END IF;

    INSERT INTO sipro_history.meta_avance
    VALUE(OLD.metaid,OLD.fecha,OLD.usuario,OLD.valor_entero,OLD.valor_string,
      OLD.valor_decimal,OLD.valor_tiempo,OLD.estado,OLD.fecha_ingreso,
       v_version, NULL, NULL);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `meta_planificado`
--

DROP TABLE IF EXISTS `meta_planificado`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `meta_planificado` (
  `metaid` int(10) NOT NULL,
  `ejercicio` int(4) NOT NULL,
  `enero_entero` int(10) DEFAULT NULL,
  `enero_string` text COLLATE utf8_bin DEFAULT NULL,
  `enero_decimal` decimal(15,2) DEFAULT NULL,
  `enero_tiempo` timestamp NULL DEFAULT NULL,
  `febrero_entero` int(10) DEFAULT NULL,
  `febrero_string` text COLLATE utf8_bin DEFAULT NULL,
  `febrero_decimal` decimal(15,2) DEFAULT NULL,
  `febrero_tiempo` timestamp NULL DEFAULT NULL,
  `marzo_entero` int(10) DEFAULT NULL,
  `marzo_string` text COLLATE utf8_bin DEFAULT NULL,
  `marzo_decimal` decimal(15,2) DEFAULT NULL,
  `marzo_tiempo` timestamp NULL DEFAULT NULL,
  `abril_entero` int(10) DEFAULT NULL,
  `abril_string` text COLLATE utf8_bin DEFAULT NULL,
  `abril_decimal` decimal(15,2) DEFAULT NULL,
  `abril_tiempo` timestamp NULL DEFAULT NULL,
  `mayo_entero` int(10) DEFAULT NULL,
  `mayo_string` text COLLATE utf8_bin DEFAULT NULL,
  `mayo_decimal` decimal(15,2) DEFAULT NULL,
  `mayo_tiempo` timestamp NULL DEFAULT NULL,
  `junio_entero` int(10) DEFAULT NULL,
  `junio_string` text COLLATE utf8_bin DEFAULT NULL,
  `junio_decimal` decimal(15,2) DEFAULT NULL,
  `junio_tiempo` timestamp NULL DEFAULT NULL,
  `julio_entero` int(10) DEFAULT NULL,
  `julio_string` text COLLATE utf8_bin DEFAULT NULL,
  `julio_decimal` decimal(15,2) DEFAULT NULL,
  `julio_tiempo` timestamp NULL DEFAULT NULL,
  `agosto_entero` int(10) DEFAULT NULL,
  `agosto_string` text COLLATE utf8_bin DEFAULT NULL,
  `agosto_decimal` decimal(15,2) DEFAULT NULL,
  `agosto_tiempo` timestamp NULL DEFAULT NULL,
  `septiembre_entero` int(10) DEFAULT NULL,
  `septiembre_string` text COLLATE utf8_bin DEFAULT NULL,
  `septiembre_decimal` decimal(15,2) DEFAULT NULL,
  `septiembre_tiempo` timestamp NULL DEFAULT NULL,
  `octubre_entero` int(10) DEFAULT NULL,
  `octubre_string` text COLLATE utf8_bin DEFAULT NULL,
  `octubre_decimal` decimal(15,2) DEFAULT NULL,
  `octubre_tiempo` timestamp NULL DEFAULT NULL,
  `noviembre_entero` int(10) DEFAULT NULL,
  `noviembre_string` text COLLATE utf8_bin DEFAULT NULL,
  `noviembre_decimal` decimal(15,2) DEFAULT NULL,
  `noviembre_tiempo` timestamp NULL DEFAULT NULL,
  `diciembre_entero` int(10) DEFAULT NULL,
  `diciembre_string` text COLLATE utf8_bin DEFAULT NULL,
  `diciembre_decimal` decimal(15,2) DEFAULT NULL,
  `diciembre_tiempo` timestamp NULL DEFAULT NULL,
  `estado` int(2) NOT NULL DEFAULT 1,
  `usuario` varchar(30) COLLATE utf8_bin NOT NULL,
  `fecha_ingreso` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`metaid`,`ejercicio`),
  KEY `FKmeta_valorplan1` (`metaid`),
  KEY `FKmeta_valorplan2` (`metaid`),
  CONSTRAINT `FKmeta_valorplan2` FOREIGN KEY (`metaid`) REFERENCES `meta` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `meta_planificado`
--

LOCK TABLES `meta_planificado` WRITE;
/*!40000 ALTER TABLE `meta_planificado` DISABLE KEYS */;
/*!40000 ALTER TABLE `meta_planificado` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER trigger_meta_plan_insert
AFTER INSERT ON sipro.meta_planificado FOR EACH ROW
BEGIN
    DECLARE v_version int;
    SELECT max(a.version) INTO v_version FROM sipro_history.meta_planificado a
    WHERE a.metaid=NEW.metaid AND a.ejercicio = NEW.ejercicio;

    IF(v_version is null) THEN
        SET v_version=1;
    END IF;

    INSERT INTO sipro_history.meta_planificado
    VALUE(NEW.metaid,NEW.ejercicio,NEW.enero_entero,NEW.enero_string,NEW.enero_decimal,
      NEW.enero_tiempo,NEW.febrero_entero,NEW.febrero_string,NEW.febrero_decimal,
      NEW.febrero_tiempo,NEW.marzo_entero,NEW.marzo_string,NEW.marzo_decimal,NEW.marzo_tiempo,
      NEW.abril_entero,NEW.abril_string,NEW.abril_decimal,NEW.abril_tiempo,NEW.mayo_entero,
      NEW.mayo_string,NEW.mayo_decimal,NEW.mayo_tiempo,NEW.junio_entero,NEW.junio_string,
      NEW.junio_decimal,NEW.junio_tiempo,NEW.julio_entero,NEW.julio_string,NEW.julio_decimal,
      NEW.julio_tiempo,NEW.agosto_entero,NEW.agosto_string,NEW.agosto_decimal,NEW.agosto_tiempo,
      NEW.septiembre_entero,NEW.septiembre_string,NEW.septiembre_decimal,NEW.septiembre_tiempo,
      NEW.octubre_entero,NEW.octubre_string,NEW.octubre_decimal,NEW.octubre_tiempo,
      NEW.noviembre_entero,NEW.noviembre_string,NEW.noviembre_decimal,NEW.noviembre_tiempo,
      NEW.diciembre_entero,NEW.diciembre_string,NEW.diciembre_decimal,NEW.diciembre_tiempo,
      NEW.estado,NEW.usuario,NEW.fecha_ingreso,
      v_version, NULL, 1);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER trigger_meta_plan_update
AFTER UPDATE ON sipro.meta_planificado FOR EACH ROW
BEGIN
    DECLARE v_version int;
    SELECT max(a.version) INTO v_version FROM sipro_history.meta_planificado a
    WHERE a.metaid=OLD.metaid AND a.ejercicio = OLD.ejercicio;

    IF(v_version is null) THEN
        UPDATE sipro_history.meta_planificado a SET actual=null
        WHERE a.metaid=OLD.metaid AND a.ejercicio = OLD.ejercicio AND version is null;
        SET v_version=1;
    ELSE
        UPDATE sipro_history.meta_planificado a SET actual=null
        WHERE a.metaid=OLD.metaid AND a.ejercicio = OLD.ejercicio AND version=v_version;
        SET v_version=v_version+1;
    END IF;

    INSERT INTO sipro_history.meta_planificado
    VALUE(NEW.metaid,NEW.ejercicio,NEW.enero_entero,NEW.enero_string,NEW.enero_decimal,
      NEW.enero_tiempo,NEW.febrero_entero,NEW.febrero_string,NEW.febrero_decimal,
      NEW.febrero_tiempo,NEW.marzo_entero,NEW.marzo_string,NEW.marzo_decimal,NEW.marzo_tiempo,
      NEW.abril_entero,NEW.abril_string,NEW.abril_decimal,NEW.abril_tiempo,NEW.mayo_entero,
      NEW.mayo_string,NEW.mayo_decimal,NEW.mayo_tiempo,NEW.junio_entero,NEW.junio_string,
      NEW.junio_decimal,NEW.junio_tiempo,NEW.julio_entero,NEW.julio_string,NEW.julio_decimal,
      NEW.julio_tiempo,NEW.agosto_entero,NEW.agosto_string,NEW.agosto_decimal,NEW.agosto_tiempo,
      NEW.septiembre_entero,NEW.septiembre_string,NEW.septiembre_decimal,NEW.septiembre_tiempo,
      NEW.octubre_entero,NEW.octubre_string,NEW.octubre_decimal,NEW.octubre_tiempo,
      NEW.noviembre_entero,NEW.noviembre_string,NEW.noviembre_decimal,NEW.noviembre_tiempo,
      NEW.diciembre_entero,NEW.diciembre_string,NEW.diciembre_decimal,NEW.diciembre_tiempo,
      NEW.estado,NEW.usuario,NEW.fecha_ingreso,
       v_version, NULL, 1);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER trigger_meta_plan_delete
BEFORE DELETE ON sipro.meta_planificado FOR EACH ROW
BEGIN
    DECLARE v_version int;
    SELECT max(a.version) INTO v_version FROM sipro_history.meta_planificado a
    WHERE a.metaid=OLD.metaid AND a.ejercicio = OLD.ejercicio;

    IF(v_version is null) THEN
        SET v_version=1;
    ELSE
        UPDATE sipro_history.meta_planificado a SET actual=null
        WHERE a.metaid=OLD.metaid AND a.ejercicio = OLD.ejercicio AND version=v_version;
        SET v_version=v_version+1;
    END IF;

    INSERT INTO sipro_history.meta_planificado
    VALUE(OLD.metaid,OLD.ejercicio,OLD.enero_entero,OLD.enero_string,
      OLD.enero_decimal,OLD.enero_tiempo,OLD.febrero_entero,
      OLD.febrero_string,OLD.febrero_decimal,OLD.febrero_tiempo,
      OLD.marzo_entero,OLD.marzo_string,OLD.marzo_decimal,OLD.marzo_tiempo,
      OLD.abril_entero,OLD.abril_string,OLD.abril_decimal,OLD.abril_tiempo,
      OLD.mayo_entero,OLD.mayo_string,OLD.mayo_decimal,OLD.mayo_tiempo,
      OLD.junio_entero,OLD.junio_string,OLD.junio_decimal,
      OLD.junio_tiempo,OLD.julio_entero,OLD.julio_string,OLD.julio_decimal,
      OLD.julio_tiempo,OLD.agosto_entero,OLD.agosto_string,OLD.agosto_decimal,
      OLD.agosto_tiempo,OLD.septiembre_entero,OLD.septiembre_string,
      OLD.septiembre_decimal,OLD.septiembre_tiempo,OLD.octubre_entero,OLD.octubre_string,
      OLD.octubre_decimal,OLD.octubre_tiempo,OLD.noviembre_entero,OLD.noviembre_string,
      OLD.noviembre_decimal,OLD.noviembre_tiempo,OLD.diciembre_entero,
      OLD.diciembre_string,OLD.diciembre_decimal,OLD.diciembre_tiempo,OLD.estado,
      OLD.usuario,OLD.fecha_ingreso,
       v_version, NULL, NULL);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `meta_tipo`
--

DROP TABLE IF EXISTS `meta_tipo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `meta_tipo` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(1000) COLLATE utf8_bin NOT NULL,
  `descripcion` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  `estado` int(2) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `meta_tipo`
--

LOCK TABLES `meta_tipo` WRITE;
/*!40000 ALTER TABLE `meta_tipo` DISABLE KEYS */;
INSERT INTO `meta_tipo` VALUES (1,'General','General','admin',NULL,'2017-10-02 14:14:04',NULL,1);
/*!40000 ALTER TABLE `meta_tipo` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER tr_meta_tipo_insert
AFTER INSERT ON sipro.meta_tipo FOR EACH ROW
BEGIN
	DECLARE v_version int;
	SELECT max(a.version) INTO v_version FROM sipro_history.meta_tipo a WHERE a.id=NEW.id;

    IF(v_version is null) THEN
		SET v_version=1;
	END IF;

    INSERT INTO sipro_history.meta_tipo VALUE(NEW.id,NEW.nombre,NEW.descripcion,NEW.usuario_creo,NEW.usuario_actualizo,NEW.fecha_creacion,NEW.fecha_actualizacion,NEW.estado, v_version, NULL, 1);

END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER tr_meta_tipo_update
AFTER UPDATE ON sipro.meta_tipo FOR EACH ROW
BEGIN
		DECLARE v_version int;
		SELECT max(a.version) INTO v_version FROM sipro_history.meta_tipo a WHERE a.id=OLD.id;

		IF(v_version is null) THEN
            UPDATE sipro_history.meta_tipo SET actual=null WHERE id=OLD.id AND version is null;
            SET v_version=1;
		ELSE
            UPDATE sipro_history.meta_tipo SET actual=null WHERE id=OLD.id AND version=v_version;
            SET v_version=v_version+1;
        END IF;

        INSERT INTO sipro_history.meta_tipo VALUE(NEW.id,NEW.nombre,NEW.descripcion,NEW.usuario_creo,NEW.usuario_actualizo,NEW.fecha_creacion,NEW.fecha_actualizacion,NEW.estado, v_version, NULL, 1);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER tr_meta_tipo_delete
BEFORE DELETE ON sipro.meta_tipo FOR EACH ROW
BEGIN
	DECLARE v_version int;
	SELECT max(a.version) INTO v_version FROM sipro_history.meta_tipo a WHERE a.id=OLD.id;

	IF(v_version is null) THEN
		SET v_version=1;
	ELSE
		UPDATE sipro_history.meta_tipo SET actual=null WHERE id=OLD.id AND version=v_version;
		SET v_version=v_version+1;
	END IF;

	INSERT INTO sipro_history.meta_tipo VALUE(OLD.id,OLD.nombre,OLD.descripcion,OLD.usuario_creo,OLD.usuario_actualizo,OLD.fecha_creacion,OLD.fecha_actualizacion,OLD.estado, v_version, NULL, NULL);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `meta_unidad_medida`
--

DROP TABLE IF EXISTS `meta_unidad_medida`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `meta_unidad_medida` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(1000) COLLATE utf8_bin DEFAULT NULL,
  `descripcion` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `simbolo` varchar(10) COLLATE utf8_bin DEFAULT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NULL DEFAULT NULL,
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  `estado` int(2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `meta_unidad_medida`
--

LOCK TABLES `meta_unidad_medida` WRITE;
/*!40000 ALTER TABLE `meta_unidad_medida` DISABLE KEYS */;
INSERT INTO `meta_unidad_medida` VALUES (1,'Kilómetros','Kilómetros avanzados','Km','admin',NULL,'2017-10-02 14:14:51',NULL,1),(2,'Area Protegida',NULL,NULL,'admin',NULL,'2017-10-04 20:48:03',NULL,1),(3,'Kilómetros cuadrados',NULL,'Km2','admin',NULL,'2017-10-04 20:48:15',NULL,1),(4,'Sistema informatico',NULL,NULL,'admin',NULL,'2017-10-04 21:33:50',NULL,1),(5,'Lote',NULL,NULL,'admin',NULL,'2017-10-04 21:33:56',NULL,1),(6,'Eventos',NULL,NULL,'admin',NULL,'2017-10-04 21:34:02',NULL,1),(7,'Funcionarios',NULL,NULL,'admin',NULL,'2017-10-04 21:34:07',NULL,1),(8,'Propuestas',NULL,NULL,'admin',NULL,'2017-10-04 21:34:15',NULL,1),(9,'Estudios',NULL,NULL,'admin',NULL,'2017-10-04 21:34:24',NULL,1),(10,'pasteles','pasteles',NULL,'admin',NULL,'2017-10-04 23:13:37',NULL,1),(11,'Sitios arqueológicos',NULL,NULL,'eflores',NULL,'2017-10-10 04:29:20',NULL,1),(12,'Comunidades',NULL,NULL,'eflores',NULL,'2017-10-10 04:43:49',NULL,1),(13,'Dictámenes',NULL,NULL,'eflores',NULL,'2017-10-10 04:47:55',NULL,1),(14,'Unidad',NULL,'Unidad','eflores',NULL,'2017-10-11 21:41:14',NULL,1),(15,'Porcentaje','medida utilizada para representar un porcentaje','%','eflores','eflores','2017-10-18 02:57:26','2017-10-18 03:01:49',1),(16,'Centros',NULL,NULL,'eflores',NULL,'2017-10-18 21:56:45',NULL,1);
/*!40000 ALTER TABLE `meta_unidad_medida` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER tr_meta_unidad_medida_insert
AFTER INSERT ON sipro.meta_unidad_medida FOR EACH ROW
BEGIN
	DECLARE v_version int;
	SELECT max(a.version) INTO v_version FROM sipro_history.meta_unidad_medida a WHERE a.id=NEW.id;

    IF(v_version is null) THEN
		SET v_version=1;
	END IF;

    INSERT INTO sipro_history.meta_unidad_medida VALUE(NEW.id,NEW.nombre,NEW.descripcion,NEW.simbolo,NEW.usuario_creo,NEW.usuario_actualizo,NEW.fecha_creacion,NEW.fecha_actualizacion,NEW.estado, v_version, NULL, 1);

END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER tr_meta_unidad_medida_update
AFTER UPDATE ON sipro.meta_unidad_medida FOR EACH ROW
BEGIN
		DECLARE v_version int;
		SELECT max(a.version) INTO v_version FROM sipro_history.meta_unidad_medida a WHERE a.id=OLD.id;

		IF(v_version is null) THEN
            UPDATE sipro_history.meta_unidad_medida SET actual=null WHERE id=OLD.id AND version is null;
            SET v_version=1;
		ELSE
            UPDATE sipro_history.meta_unidad_medida SET actual=null WHERE id=OLD.id AND version=v_version;
            SET v_version=v_version+1;
        END IF;

        INSERT INTO sipro_history.meta_unidad_medida VALUE(NEW.id,NEW.nombre,NEW.descripcion,NEW.simbolo,NEW.usuario_creo,NEW.usuario_actualizo,NEW.fecha_creacion,NEW.fecha_actualizacion,NEW.estado, v_version, NULL, 1);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER tr_meta_unidad_medida_delete
BEFORE DELETE ON sipro.meta_unidad_medida FOR EACH ROW
BEGIN
	DECLARE v_version int;
	SELECT max(a.version) INTO v_version FROM sipro_history.meta_unidad_medida a WHERE a.id=OLD.id;

	IF(v_version is null) THEN
		SET v_version=1;
	ELSE
		UPDATE sipro_history.meta_unidad_medida SET actual=null WHERE id=OLD.id AND version=v_version;
		SET v_version=v_version+1;
	END IF;

	INSERT INTO sipro_history.meta_unidad_medida VALUE(OLD.id,OLD.nombre,OLD.descripcion,OLD.simbolo,OLD.usuario_creo,OLD.usuario_actualizo,OLD.fecha_creacion,OLD.fecha_actualizacion,OLD.estado, v_version, NULL, NULL);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `objeto_formulario`
--

DROP TABLE IF EXISTS `objeto_formulario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `objeto_formulario` (
  `formularioid` int(10) NOT NULL,
  `objeto_tipo` int(2) NOT NULL,
  `objeto_id` int(10) NOT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  `estado` int(2) NOT NULL,
  PRIMARY KEY (`formularioid`,`objeto_tipo`,`objeto_id`),
  KEY `FKobjeto_for155797` (`formularioid`),
  CONSTRAINT `FKobjeto_for155797` FOREIGN KEY (`formularioid`) REFERENCES `formulario` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `objeto_formulario`
--

LOCK TABLES `objeto_formulario` WRITE;
/*!40000 ALTER TABLE `objeto_formulario` DISABLE KEYS */;
/*!40000 ALTER TABLE `objeto_formulario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `objeto_prestamo`
--

DROP TABLE IF EXISTS `objeto_prestamo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `objeto_prestamo` (
  `prestamoid` int(10) NOT NULL,
  `objeto_id` int(11) NOT NULL,
  `objeto_tipo` int(11) NOT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `usuario_actualizo` int(11) DEFAULT NULL,
  `fecha_creacion` timestamp NULL DEFAULT NULL,
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  `estado` int(2) DEFAULT NULL,
  PRIMARY KEY (`prestamoid`,`objeto_id`,`objeto_tipo`),
  KEY `FKobjeto_pre588847` (`prestamoid`),
  CONSTRAINT `FKobjeto_pre588847` FOREIGN KEY (`prestamoid`) REFERENCES `prestamo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `objeto_prestamo`
--

LOCK TABLES `objeto_prestamo` WRITE;
/*!40000 ALTER TABLE `objeto_prestamo` DISABLE KEYS */;
/*!40000 ALTER TABLE `objeto_prestamo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `objeto_recurso`
--

DROP TABLE IF EXISTS `objeto_recurso`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `objeto_recurso` (
  `recursoid` int(10) NOT NULL,
  `objetoid` int(10) NOT NULL,
  `objeto_tipo` int(2) NOT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  `estado` int(2) NOT NULL,
  `valor_entero` int(10) DEFAULT NULL,
  `valor_string` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `valor_decimal` decimal(15,2) DEFAULT NULL,
  `valor_tiempo` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`recursoid`,`objetoid`,`objeto_tipo`),
  KEY `FKobjeto_rec137100` (`recursoid`),
  CONSTRAINT `FKobjeto_rec137100` FOREIGN KEY (`recursoid`) REFERENCES `recurso` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `objeto_recurso`
--

LOCK TABLES `objeto_recurso` WRITE;
/*!40000 ALTER TABLE `objeto_recurso` DISABLE KEYS */;
/*!40000 ALTER TABLE `objeto_recurso` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `objeto_riesgo`
--

DROP TABLE IF EXISTS `objeto_riesgo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `objeto_riesgo` (
  `riesgoid` int(10) NOT NULL,
  `objeto_id` int(11) NOT NULL,
  `objeto_tipo` int(11) NOT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NULL DEFAULT NULL,
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`riesgoid`,`objeto_id`,`objeto_tipo`),
  KEY `FKobjeto_rie619314` (`riesgoid`),
  CONSTRAINT `FKobjeto_rie619314` FOREIGN KEY (`riesgoid`) REFERENCES `riesgo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `objeto_riesgo`
--

LOCK TABLES `objeto_riesgo` WRITE;
/*!40000 ALTER TABLE `objeto_riesgo` DISABLE KEYS */;
/*!40000 ALTER TABLE `objeto_riesgo` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER trigger_obj_riesgo_insert
AFTER INSERT ON sipro.objeto_riesgo FOR EACH ROW
BEGIN
    DECLARE v_version int;
    SELECT max(a.version) INTO v_version FROM sipro_history.objeto_riesgo a WHERE a.riesgoid=NEW.riesgoid AND a.objeto_id=NEW.objeto_id AND a.objeto_tipo=NEW.objeto_tipo;

    IF(v_version is null) THEN
        SET v_version=1;
    END IF;

    INSERT INTO sipro_history.objeto_riesgo VALUE(NEW.riesgoid,NEW.objeto_id,NEW.objeto_tipo,NEW.usuario_creo,NEW.usuario_actualizo,NEW.fecha_creacion,NEW.fecha_actualizacion, v_version, NULL, 1);

END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER trigger_obj_riesgo_update
AFTER UPDATE ON sipro.objeto_riesgo FOR EACH ROW
BEGIN
        DECLARE v_version int;
        SELECT max(a.version) INTO v_version FROM sipro_history.objeto_riesgo a WHERE a.riesgoid=OLD.riesgoid AND a.objeto_id=OLD.objeto_id AND a.objeto_tipo=OLD.objeto_tipo;

        IF(v_version is null) THEN
            UPDATE sipro_history.objeto_riesgo SET actual=null WHERE a.riesgoid=OLD.riesgoid AND a.objeto_id=OLD.objeto_id AND a.objeto_tipo=OLD.objeto_tipo AND version is null;
            SET v_version=1;
        ELSE
            UPDATE sipro_history.objeto_riesgo SET actual=null WHERE a.riesgoid=OLD.riesgoid AND a.objeto_id=OLD.objeto_id AND a.objeto_tipo=OLD.objeto_tipo AND version=v_version;
            SET v_version=v_version+1;
        END IF;

        INSERT INTO sipro_history.objeto_riesgo VALUE(OLD.riesgoid,OLD.objeto_id,OLD.objeto_tipo,OLD.usuario_creo,OLD.usuario_actualizo,OLD.fecha_creacion,OLD.fecha_actualizacion, v_version, NULL, 1);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER trigger_obj_riesgo_delete
BEFORE DELETE ON sipro.objeto_riesgo FOR EACH ROW
BEGIN
    DECLARE v_version int;
    SELECT max(a.version) INTO v_version FROM sipro_history.objeto_riesgo a WHERE a.riesgoid=OLD.riesgoid AND a.objeto_id=OLD.objeto_id AND a.objeto_tipo=OLD.objeto_tipo;

    IF(v_version is null) THEN
        SET v_version=1;
    ELSE
        UPDATE sipro_history.objeto_riesgo a SET a.actual=null WHERE a.riesgoid=OLD.riesgoid AND a.objeto_id=OLD.objeto_id AND a.objeto_tipo=OLD.objeto_tipo AND version=v_version;
        SET v_version=v_version+1;
    END IF;

    INSERT INTO sipro_history.objeto_riesgo VALUE(OLD.riesgoid,OLD.objeto_id,OLD.objeto_tipo,OLD.usuario_creo,OLD.usuario_actualizo,OLD.fecha_creacion,OLD.fecha_actualizacion, v_version, NULL, 1);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `pago_planificado`
--

DROP TABLE IF EXISTS `pago_planificado`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pago_planificado` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `fecha_pago` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `pago` decimal(15,2) NOT NULL,
  `objeto_id` int(10) NOT NULL,
  `objeto_tipo` int(10) NOT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  `estado` int(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pago_planificado`
--

LOCK TABLES `pago_planificado` WRITE;
/*!40000 ALTER TABLE `pago_planificado` DISABLE KEYS */;
/*!40000 ALTER TABLE `pago_planificado` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER tr_pago_planificado_insert
AFTER INSERT ON sipro.pago_planificado FOR EACH ROW
BEGIN
	DECLARE v_version int;
	SELECT max(a.version) INTO v_version FROM sipro_history.pago_planificado a WHERE a.id=NEW.id;

    IF(v_version is null) THEN
		SET v_version=1;
	END IF;

    INSERT INTO sipro_history.pago_planificado VALUE(NEW.id,NEW.fecha_pago,NEW.pago,NEW.objeto_id,NEW.objeto_tipo,NEW.usuario_creo,NEW.usuario_actualizo,NEW.fecha_creacion,NEW.fecha_actualizacion,NEW.estado, v_version, NULL, 1);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER tr_pago_planificado_update
AFTER UPDATE ON sipro.pago_planificado FOR EACH ROW
BEGIN
		DECLARE v_version int;
		SELECT max(a.version) INTO v_version FROM sipro_history.pago_planificado a WHERE a.id=OLD.id;

		IF(v_version is null) THEN
            UPDATE sipro_history.pago_planificado SET actual=null WHERE id=OLD.id AND version is null;
            SET v_version=1;
		ELSE
            UPDATE sipro_history.pago_planificado SET actual=null WHERE id=OLD.id AND version=v_version;
            SET v_version=v_version+1;
        END IF;

        INSERT INTO sipro_history.pago_planificado VALUE(NEW.id,NEW.fecha_pago,NEW.pago,NEW.objeto_id,NEW.objeto_tipo,NEW.usuario_creo,NEW.usuario_actualizo,NEW.fecha_creacion,NEW.fecha_actualizacion,NEW.estado, v_version, NULL, 1);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER tr_pago_planificado_delete
BEFORE DELETE ON sipro.pago_planificado FOR EACH ROW
BEGIN
	DECLARE v_version int;
	SELECT max(a.version) INTO v_version FROM sipro_history.pago_planificado a WHERE a.id=OLD.id;

	IF(v_version is null) THEN
		SET v_version=1;
	ELSE
		UPDATE sipro_history.pago_planificado SET actual=null WHERE id=OLD.id AND version=v_version;
		SET v_version=v_version+1;
	END IF;

	INSERT INTO sipro_history.pago_planificado VALUE(OLD.id,OLD.fecha_pago,OLD.pago,OLD.objeto_id,OLD.objeto_tipo,OLD.usuario_creo,OLD.usuario_actualizo,OLD.fecha_creacion,OLD.fecha_actualizacion,OLD.estado, v_version, NULL, NULL);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `pep_detalle`
--

DROP TABLE IF EXISTS `pep_detalle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pep_detalle` (
  `proyectoid` int(11) NOT NULL,
  `observaciones` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `alertivos` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `elaborado` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `aprobado` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `autoridad` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  `estado` int(2) NOT NULL,
  PRIMARY KEY (`proyectoid`),
  CONSTRAINT `fk_pep_detalle_1` FOREIGN KEY (`proyectoid`) REFERENCES `proyecto` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pep_detalle`
--

LOCK TABLES `pep_detalle` WRITE;
/*!40000 ALTER TABLE `pep_detalle` DISABLE KEYS */;
/*!40000 ALTER TABLE `pep_detalle` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `permiso`
--

DROP TABLE IF EXISTS `permiso`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `permiso` (
  `id` int(11) NOT NULL,
  `nombre` varchar(255) COLLATE utf8_bin NOT NULL,
  `descripcion` varchar(4000) COLLATE utf8_bin NOT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  `estado` int(2) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `permiso`
--

LOCK TABLES `permiso` WRITE;
/*!40000 ALTER TABLE `permiso` DISABLE KEYS */;
INSERT INTO `permiso` VALUES (1010,'Actividades - Visualizar','Permite visualizar la actividad','admin','admin','2017-02-09 18:00:00','2017-02-16 06:30:14',1),(1020,'Actividades - Editar','Permite editar la actividad','admin',NULL,'2017-02-09 18:00:00',NULL,1),(1030,'Actividades - Eliminar','Permite eliminar actividades','admin',NULL,'2017-02-09 18:00:00',NULL,1),(1040,'Actividades - Crear','Permite crear nuevas actividades','admin',NULL,'2017-02-09 18:00:00',NULL,1),(2010,'Actividad Propiedades - Visualizar','Permite visualizar las propiedades de un tipo de actividad','admin',NULL,'2017-02-09 18:00:00',NULL,1),(2020,'Actividad Propiedades - Editar','Permite editar las propiedades de un tipo de actividad','admin',NULL,'2017-02-09 18:00:00',NULL,1),(2030,'Actividad Propiedades - Eliminar','Permite eliminar las propiedades de un tipo de actividad','admin',NULL,'2017-02-09 18:00:00',NULL,1),(2040,'Actividad Propiedades - Crear','Permite agregar nuevas propiedades a un tipo de actividad','admin',NULL,'2017-02-09 18:00:00',NULL,1),(3010,'Actividad Tipos - Visualizar','Permite visualizar los tipos de actividades','admin',NULL,'2017-02-09 18:00:00',NULL,1),(3020,'Actividad Tipos - Editar','Permite eidtar los tipos de actividades','admin',NULL,'2017-02-09 18:00:00',NULL,1),(3030,'Actividad Tipos - Eliminar','Permite eliminar los tipos de actividades','admin',NULL,'2017-02-09 18:00:00',NULL,1),(3040,'Actividad Tipos - Crear','Permite crear nuevos tipos de actividades','admin',NULL,'2017-02-09 18:00:00',NULL,1),(4010,'Colaboradores - Visualizar','Permite visualizar colaboradores','admin',NULL,'2017-02-09 18:00:00',NULL,1),(4020,'Colaboradores - Editar','Permite editar colaboradores','admin',NULL,'2017-02-09 18:00:00',NULL,1),(4030,'Colaboradores - Eliminar','Permite eliminar colaboradores','admin',NULL,'2017-02-09 18:00:00',NULL,1),(4040,'Colaboradores - Crear','Permite crear nuevos colaboradores','admin',NULL,'2017-02-09 18:00:00',NULL,1),(5010,'Componentes - Visualizar','Permite visualizar componentes','admin',NULL,'2017-02-09 18:00:00',NULL,1),(5020,'Componentes - Editar','Permite editar componentes','admin',NULL,'2017-02-09 18:00:00',NULL,1),(5030,'Componentes - Eliminar','Permite eliminar componentes','admin',NULL,'2017-02-09 18:00:00',NULL,1),(5040,'Componentes - Crear','Permite crear componentes','admin',NULL,'2017-02-09 18:00:00',NULL,1),(6010,'Componente Propiedades - Visualizar','Permite visualizar las propiedades de un tipo de componente','admin',NULL,'2017-02-09 18:00:00',NULL,1),(6020,'Componente Propiedades - Editar','Permite editar las propiedades de un tipo de componente','admin',NULL,'2017-02-09 18:00:00',NULL,1),(6030,'Componente Propiedades - Eliminar','Permite eliminar las propiedades de un tipo de componente','admin',NULL,'2017-02-09 18:00:00',NULL,1),(6040,'Componente Propiedades - Crear','Permite crear agregar nuevas propiedades a un tipo de componente','admin',NULL,'2017-02-09 18:00:00',NULL,1),(7010,'Componente Tipos - Visualizar','Permite visualizar los tipos de componentes','admin',NULL,'2017-02-09 18:00:00',NULL,1),(7020,'Componente Tipos - Editar','Permite editar los tipos de componentes','admin',NULL,'2017-02-09 18:00:00',NULL,1),(7030,'Componente Tipos - Eliminar','Permite eliminar los tipos de componentes','admin',NULL,'2017-02-09 18:00:00',NULL,1),(7040,'Componente Tipos - Crear','Permite crear nuevos tipos de componentes','admin',NULL,'2017-02-09 18:00:00',NULL,1),(8010,'Cooperantes - Visualizar','Permite visualizar cooperantes','admin',NULL,'2017-02-09 18:00:00',NULL,1),(8020,'Cooperantes - Editar','Permite edidtar cooperantes','admin',NULL,'2017-02-09 18:00:00',NULL,1),(8030,'Cooperantes - Eliminar','Permite eliminar cooperantes','admin',NULL,'2017-02-09 18:00:00',NULL,1),(8040,'Cooperantes - Crear','Permite crear nuevos cooperantes','admin',NULL,'2017-02-09 18:00:00',NULL,1),(9010,'Desembolsos - Visualizar','Permite visualizar los desembolsos de un préstamo o proyecto','admin',NULL,'2017-02-09 18:00:00',NULL,1),(9020,'Desembolsos - Editar','Permite editar los desembolsos de un préstamo o proyecto','admin',NULL,'2017-02-09 18:00:00',NULL,1),(9030,'Desembolsos - Eliminar','Permite eliminar los desembolsos de un préstamo o proyecto','admin',NULL,'2017-02-09 18:00:00',NULL,1),(9040,'Desembolsos - Crear','Permite agregar desembolsos a un préstamo o proyecto','admin',NULL,'2017-02-09 18:00:00',NULL,1),(10010,'Entidades - Visualizar','Permite visualizar las entidades','admin',NULL,'2017-02-09 18:00:00',NULL,1),(10020,'Entidades - Editar','Permite editar las entidades','admin',NULL,'2017-02-09 18:00:00',NULL,1),(10030,'Entidades - Eliminar','Permite eliminar las entidades','admin',NULL,'2017-02-09 18:00:00',NULL,1),(10040,'Entidades - Crear','Permite crear nuevas entidades','admin',NULL,'2017-02-09 18:00:00',NULL,1),(15010,'Hitos - Visualizar','Permite visualizar los hitos de un préstamo o proyecto','admin',NULL,'2017-02-09 18:00:00',NULL,1),(15020,'Hitos - Editar','Permite editar los hitos de un préstamo o proyecto','admin',NULL,'2017-02-09 18:00:00',NULL,1),(15030,'Hitos - Eliminar','Permite eliminar los hitos de un préstamo o proyecto','admin',NULL,'2017-02-09 18:00:00',NULL,1),(15040,'Hitos - Crear','Permite crear hitos nuevos a un préstamo o proyecto','admin',NULL,'2017-02-09 18:00:00',NULL,1),(16010,'Hito Tipos - Visualizar','Permite visualizar los tipos de hitos','admin',NULL,'2017-02-09 18:00:00',NULL,1),(16020,'Hito Tipos - Editar','Permite editar los tipos de hitos','admin',NULL,'2017-02-09 18:00:00',NULL,1),(16030,'Hito Tipos - Eliminar','Permite eliminar tipos de hitos','admin',NULL,'2017-02-09 18:00:00',NULL,1),(16040,'Hito Tipos - Crear','Permite crear nuevos tipos de hitos','admin',NULL,'2017-02-09 18:00:00',NULL,1),(17010,'Metas - Visualizar','Permite visualizar las metas de un producto','admin',NULL,'2017-02-09 18:00:00',NULL,1),(17020,'Metas - Editar','Permite editar las metas de un producto','admin',NULL,'2017-02-09 18:00:00',NULL,1),(17030,'Metas - Eliminar','Permite eliminar las metas de un producto','admin',NULL,'2017-02-09 18:00:00',NULL,1),(17040,'Metas - Crear','Permite crear nuevas metas para un producto','admin',NULL,'2017-02-09 18:00:00',NULL,1),(18010,'Meta Tipos - Visualizar','Permite visualizar los tipos de metas','admin',NULL,'2017-02-09 18:00:00',NULL,1),(18020,'Meta Tipos - Editar','Permite editar los tipos de metas','admin',NULL,'2017-02-09 18:00:00',NULL,1),(18030,'Meta Tipos - Eliminar','Permite eliminar los tipos de metas','admin',NULL,'2017-02-09 18:00:00',NULL,1),(18040,'Meta Tipos - Crear','Permite crear nuevos tipos de metas','admin',NULL,'2017-02-09 18:00:00',NULL,1),(19010,'Meta Unidades de Medida - Visualizar','Permite visualizar las unidades de medida de una meta','admin','admin','2017-02-09 18:00:00','2017-05-18 20:28:48',1),(19020,'Meta Unidades de Medida - Editar','Permite editar las unidades de medida de una meta','admin',NULL,'2017-02-09 18:00:00',NULL,1),(19030,'Meta Unidades de Medida - Eliminar','Permite eliminar las unidades de medida de una meta','admin',NULL,'2017-02-09 18:00:00',NULL,1),(19040,'Meta Unidades de Medida - Crear','Permite crear nuevas unidades de medida de una meta','admin',NULL,'2017-02-09 18:00:00',NULL,1),(20010,'Permisos - Visualizar','Permite visualizar los permisos del sistema','admin',NULL,'2017-02-09 18:00:00',NULL,1),(20020,'Permisos - Editar','Permite editar los permisos del sistema','admin',NULL,'2017-02-09 18:00:00',NULL,1),(20030,'Permisos - Eliminar','Permite eliminar los permisos del sistema','admin',NULL,'2017-02-09 18:00:00',NULL,1),(20040,'Permisos - Crear','Permite crear nuevos permisos del sistema','admin',NULL,'2017-02-09 18:00:00',NULL,1),(21010,'Productos - Visualizar','Permite visualizar los productos ','admin',NULL,'2017-02-09 18:00:00',NULL,1),(21020,'Productos - Editar','Permite editar los productos','admin',NULL,'2017-02-09 18:00:00',NULL,1),(21030,'Productos - Eliminar','Permite eliminar los productos','admin',NULL,'2017-02-09 18:00:00',NULL,1),(21040,'Productos - Crear','Permite crear los productos','admin',NULL,'2017-02-09 18:00:00',NULL,1),(22010,'Producto Propiedades - Visualizar','Permite visualizar las propiedades de un tipo de producto','admin',NULL,'2017-02-09 18:00:00',NULL,1),(22020,'Producto Propiedades - Editar','Permite editar las propiedades de un tipo de producto','admin',NULL,'2017-02-09 18:00:00',NULL,1),(22030,'Producto Propiedades - Eliminar','Permite eliminar las propiedades de un tipo de producto','admin',NULL,'2017-02-09 18:00:00',NULL,1),(22040,'Producto Propiedades - Crear','Permite agregar nuevas propiedades a un tipo de producto','admin',NULL,'2017-02-09 18:00:00',NULL,1),(23010,'Producto Tipos - Visualizar','Permite visualizar los tipos de producto','admin',NULL,'2017-02-09 18:00:00',NULL,1),(23020,'Producto Tipos - Editar','Permite editar los tipos de producto','admin',NULL,'2017-02-09 18:00:00',NULL,1),(23030,'Producto Tipos - Eliminar','Permite eliminar los tipos de producto','admin',NULL,'2017-02-09 18:00:00',NULL,1),(23040,'Producto Tipos - Crear','Permite crear nuevos tipos de producto','admin',NULL,'2017-02-09 18:00:00',NULL,1),(24010,'Préstamos o Proyectos - Visualizar','Permite visualizar los préstamos o proyectos','admin',NULL,'2017-02-09 18:00:00',NULL,1),(24020,'Préstamos o Proyectos - Editar','Permite editar los préstamos o proyectos','admin',NULL,'2017-02-09 18:00:00',NULL,1),(24030,'Préstamos o Proyectos - Eliminar','Permite eliminar los préstmos o proyectos','admin',NULL,'2017-02-09 18:00:00',NULL,1),(24040,'Préstamos o Proyectos - Crear','Permite crear nuevos préstmos o proyectos','admin',NULL,'2017-02-09 18:00:00',NULL,1),(25010,'Préstamo o Proyecto Propiedades - Visualizar','Permite visualizar las propiedades de un tipo de préstmos o proyecto','admin',NULL,'2017-02-09 18:00:00',NULL,1),(25020,'Préstamo o Proyecto Propiedades - Editar','Permite editar las propiedades de un tipo de préstmos o proyecto','admin',NULL,'2017-02-09 18:00:00',NULL,1),(25030,'Préstamo o Proyecto Propiedades - Eliminar','Permite eliminar las propiedades de un tipo de préstmos o proyecto','admin',NULL,'2017-02-09 18:00:00',NULL,1),(25040,'Préstamo o Proyecto Propiedades - Crear','Permite agregar nuevas propiedades a un tipo de préstmos o proyecto','admin',NULL,'2017-02-09 18:00:00',NULL,1),(30010,'Riesgos - Visualizar','Permite visualizar los riesgos de un préstmos o proyecto','admin',NULL,'2017-02-09 18:00:00',NULL,1),(30020,'Riesgos - Editar','Permite editar los riesgos de un préstmos o proyecto','admin',NULL,'2017-02-09 18:00:00',NULL,1),(30030,'Riesgos - Eliminar','Permite eliminar los riegos de un préstmos o proyecto','admin',NULL,'2017-02-09 18:00:00',NULL,1),(30040,'Riesgos - Crear','Permite crear nuevos riesgos para un préstmos o proyecto','admin',NULL,'2017-02-09 18:00:00',NULL,1),(31010,'Riesgo Propiedades - Visualizar','Permite visualizar las propiedades de un tipo de riesgo','admin',NULL,'2017-02-09 18:00:00',NULL,1),(31020,'Riesgo Propiedades - Editar','Permite editar las propiedades de un tipo de riesgo','admin',NULL,'2017-02-09 18:00:00',NULL,1),(31030,'Riesgo Propiedades - Eliminar','Permite eliminar las propiedades de un tipo de riesgo','admin',NULL,'2017-02-09 18:00:00',NULL,1),(31040,'Riesgo Propiedades - Crear','Permite agregar nuevas propiedades a un tipo de riesgo','admin',NULL,'2017-02-09 18:00:00',NULL,1),(32010,'Riesgo Tipos - Visualizar','Permite visualizar los tipos de riesgos','admin',NULL,'2017-02-09 18:00:00',NULL,1),(32020,'Riesgo Tipos - Editar','Permite editar los tipos de riesgos','admin',NULL,'2017-02-09 18:00:00',NULL,1),(32030,'Riesgo Tipos - Eliminar','Permite eliminar los tipos de riesgos','admin',NULL,'2017-02-09 18:00:00',NULL,1),(32040,'Riesgo Tipos - Crear','Permite crear nuevos tipos de riesgos','admin',NULL,'2017-02-09 18:00:00',NULL,1),(33010,'Unidades Ejecutoras - Visualizar','Permite visualizar las unidades ejecutoras ','admin',NULL,'2017-02-09 18:00:00',NULL,1),(33020,'Unidades Ejecutoras - Editar','Permite editar las unidades ejecutoras','admin',NULL,'2017-02-09 18:00:00',NULL,1),(33030,'Unidades Ejecutoras - Eliminar','Permite eliminar las unidades ejecutoras','admin',NULL,'2017-02-09 18:00:00',NULL,1),(33040,'Unidades Ejecutoras - Crear','Permite crear nuevas unidades ejecutoras','admin',NULL,'2017-02-09 18:00:00',NULL,1),(34010,'Usuarios - Visualizar','Permite visualizar a los usuarios del sistema','admin',NULL,'2017-02-09 18:00:00',NULL,1),(34020,'Usuarios - Editar','Permite editar a los usuarios del sistema','admin',NULL,'2017-02-09 18:00:00',NULL,1),(34030,'Usuarios - Eliminar','Permite eliminar usuarios del sistema','admin',NULL,'2017-02-09 18:00:00',NULL,1),(34040,'Usuarios - Crear','Permite crear nuevos usuarios del sistema','admin',NULL,'2017-02-09 18:00:00',NULL,1),(35010,'Desembolso Tipos - Visualizar','Permite visualizar los tips de desembolsos','admin',NULL,'2017-02-09 18:00:00',NULL,1),(35020,'Desembolso Tipos - Editar','Permite editar los tipos de desembolsos','admin',NULL,'2017-02-16 09:16:47',NULL,1),(35030,'Desembolso Tipos - Eliminar','Permite eliminar los tipos de desembolsos','admin',NULL,'2017-02-09 18:00:00',NULL,1),(35040,'Desembolso Tipos - Crear','Permite crear nuevos tipos de desembolsos','admin',NULL,'2017-02-09 18:00:00',NULL,1),(36010,'Préstamo o Proyecto Tipos - Visualizar','Permite visualizar los tipos de préstamos o proyectos','admin',NULL,'2017-02-09 18:00:00',NULL,1),(36020,'Préstamo o Proyecto Tipos - Editar','Permite editar los tipos de préstamos o proyectos','admin',NULL,'2017-02-09 18:00:00',NULL,1),(36030,'Préstamo o Proyecto Tipos - Eliminar','Permite eliminar los tipos de préstamos o proyectos','admin',NULL,'2017-02-09 18:00:00',NULL,1),(36040,'Préstamo o Proyecto Tipos - Crear','Permite crear nuevos tipos de préstamos o proyectos','admin',NULL,'2017-02-09 18:00:00',NULL,1),(37010,'Programas - Visualizar','Permite visualizar programas','admin',NULL,'2017-03-09 12:00:00',NULL,1),(37020,'Programas - Editar','Permite editar programas','admin',NULL,'2017-03-09 12:00:00',NULL,1),(37030,'Programas - Eliminar','Permite eliminar programas','admin',NULL,'2017-03-09 12:00:00',NULL,1),(37040,'Programas - Crear','Permite crear nuevos programas','admin',NULL,'2017-03-09 12:00:00',NULL,1),(38010,'Programa Propiedades - Visualizar','Permite visualizar las propiedades de un tipo de programa','admin',NULL,'2017-03-09 12:00:00',NULL,1),(38020,'Programa Propiedades - Editar','Permite editar las propiedades de un tipo de programa','admin',NULL,'2017-03-09 12:00:00',NULL,1),(38030,'Programa Propiedades - Eliminar','Permite eliminar las propiedades de un tipo de programa','admin',NULL,'2017-03-09 12:00:00',NULL,1),(38040,'Programa Propiedades - Crear','Permite agregar propiedades a un tipo de programa','admin',NULL,'2017-03-09 12:00:00',NULL,1),(39010,'Programa Tipos - Visualizar','Permite visualizar los tipos de programas','admin',NULL,'2017-02-09 12:00:00',NULL,1),(39020,'Programa Tipos - Editar','Permite editar los tipos de programa','admin',NULL,'2017-02-09 12:00:00',NULL,1),(39030,'Programa Tipos - Eliminar','Permite eliminar los tipos de programa','admin',NULL,'2017-02-09 12:00:00',NULL,1),(39040,'Programa Tipos - Crear','Permite crear nuevos tipos de programa','admin',NULL,'2017-02-09 12:00:00',NULL,1),(40010,'Subproductos - Visualizar','Permite visualizar los subproductos','admin',NULL,'2017-03-09 12:00:00',NULL,1),(40020,'Subproductos - Editar','Permite editar los subproductos','admin',NULL,'2017-03-09 12:00:00',NULL,1),(40030,'Subproductos - Eliminar','Permite eliminar los subproductos','admin',NULL,'2017-03-09 12:00:00',NULL,1),(40040,'Subproductos - Crear','Permite crear nuevos subproductos','admin',NULL,'2017-03-09 12:00:00',NULL,1),(41010,'Subproducto Propiedades - Visualizar','Permite visualizar las propiedades de un tipo de subproducto','admin',NULL,'2017-03-09 12:00:00',NULL,1),(41020,'Subproducto Propiedades - Editar','Permite editar las propiedades de un tipo de subproducto','admin',NULL,'2017-03-09 12:00:00',NULL,1),(41030,'Subproducto Propiedades - Eliminar','Permite eliminar las propiedades de un tipo de subproducto','admin',NULL,'2017-03-09 12:00:00',NULL,1),(41040,'Subproducto Propiedades - Crear','Permite agreagar nuevas propiedades a un tipo de subproducto','admin',NULL,'2017-03-09 12:00:00',NULL,1),(42010,'Subproducto Tipos - Visualizar','Permite visualizar los tipos de subproductos','admin',NULL,'2017-03-09 12:00:00',NULL,1),(42020,'Subproducto Tipos - Editar','Permite editar los tipos de subproductos','admin',NULL,'2017-03-09 12:00:00',NULL,1),(42030,'Subproducto Tipos - Eliminar','Permite eliminar los tipos de subproductos','admin',NULL,'2017-03-09 12:00:00',NULL,1),(42040,'Subproducto Tipos - Crear','Permite crear nuevos tipos de subproductos','admin',NULL,'2017-03-09 12:00:00',NULL,1),(43010,'Visualizar opciones adicionales del préstamo o proyecto','Permite visualizar las opciones extras de un préstamo o proyecto','admin',NULL,'2017-09-11 21:39:36',NULL,1),(44010,'Configuraciones - Visualizar','Permite ver el menu de configuraciones','admin',NULL,'2017-10-09 10:27:31',NULL,1),(45010,'Miembros Unidad Ejecutroa - Visualizar','Permite visualizar los miebros y roles de uniad ejecutora','admin',NULL,'2017-10-25 13:38:43',NULL,1),(45020,'Miembros Unidad Ejecutroa - Editar','Permite crear editar y roles de uniad ejecutora','admin',NULL,'2017-10-25 13:38:43',NULL,1),(45030,'Miembros Unidad Ejecutroa - Eliminar','Permite eliminar miebros y roles de uniad ejecutora','admin',NULL,'2017-10-25 13:38:43',NULL,1),(45040,'Miembros Unidad Ejecutroa - Crear','Permite crear miebros y roles de uniad ejecutora','admin',NULL,'2017-10-25 13:38:43',NULL,1),(46010,'Roles Unidad Ejecutora - Visualizar','Permite visualizar los roles de unidades ejecutoras','admin',NULL,'2017-10-25 13:38:43',NULL,1),(46020,'Roles Unidad Ejecutora - Editar','Permite editar los roles de unidades ejecutoras','admin',NULL,'2017-10-25 13:38:43',NULL,1),(46030,'Roles Unidad Ejecutora - Eliminar','Permite eliminar los roles de unidades ejecutoras','admin',NULL,'2017-10-25 13:38:43',NULL,1),(46040,'Roles Unidad Ejecutora - Crear','Permite crear los roles de unidades ejecutoras','admin',NULL,'2017-10-25 13:38:43',NULL,1),(46050,'Actividades - Visualizar','Permite visualizar la actividad','admin','admin','2017-02-09 18:00:00','2017-02-16 06:30:14',1),(46060,'Actividades - Editar','Permite editar la actividad','admin',NULL,'2017-02-09 18:00:00',NULL,1),(46070,'Actividades - Eliminar','Permite eliminar actividades','admin',NULL,'2017-02-09 18:00:00',NULL,1),(46080,'Actividades - Crear','Permite crear nuevas actividades','admin',NULL,'2017-02-09 18:00:00',NULL,1),(46090,'Actividad Propiedades - Visualizar','Permite visualizar las propiedades de un tipo de actividad','admin',NULL,'2017-02-09 18:00:00',NULL,1),(46100,'Actividad Propiedades - Editar','Permite editar las propiedades de un tipo de actividad','admin',NULL,'2017-02-09 18:00:00',NULL,1),(46110,'Actividad Propiedades - Eliminar','Permite eliminar las propiedades de un tipo de actividad','admin',NULL,'2017-02-09 18:00:00',NULL,1),(46120,'Actividad Propiedades - Crear','Permite agregar nuevas propiedades a un tipo de actividad','admin',NULL,'2017-02-09 18:00:00',NULL,1),(46130,'Actividad Tipos - Visualizar','Permite visualizar los tipos de actividades','admin',NULL,'2017-02-09 18:00:00',NULL,1),(46140,'Actividad Tipos - Editar','Permite eidtar los tipos de actividades','admin',NULL,'2017-02-09 18:00:00',NULL,1),(46150,'Actividad Tipos - Eliminar','Permite eliminar los tipos de actividades','admin',NULL,'2017-02-09 18:00:00',NULL,1),(46160,'Actividad Tipos - Crear','Permite crear nuevos tipos de actividades','admin',NULL,'2017-02-09 18:00:00',NULL,1),(99999,'prueba treeview','prueba treeview','admin',NULL,'2017-09-15 13:38:43',NULL,1);
/*!40000 ALTER TABLE `permiso` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `plan_adquisicion`
--

DROP TABLE IF EXISTS `plan_adquisicion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `plan_adquisicion` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tipo_adquisicion` int(11) NOT NULL,
  `categoria_adquisicion` int(11) DEFAULT NULL,
  `unidad_medida` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `cantidad` int(11) DEFAULT NULL,
  `total` decimal(15,2) DEFAULT NULL,
  `precio_unitario` decimal(15,2) DEFAULT NULL,
  `preparacion_doc_planificado` timestamp NULL DEFAULT NULL,
  `preparacion_doc_real` timestamp NULL DEFAULT NULL,
  `lanzamiento_evento_planificado` timestamp NULL DEFAULT NULL,
  `lanzamiento_evento_real` timestamp NULL DEFAULT NULL,
  `recepcion_ofertas_planificado` timestamp NULL DEFAULT NULL,
  `recepcion_ofertas_real` timestamp NULL DEFAULT NULL,
  `adjudicacion_planificado` timestamp NULL DEFAULT NULL,
  `adjudicacion_real` timestamp NULL DEFAULT NULL,
  `firma_contrato_planificado` timestamp NULL DEFAULT NULL,
  `firma_contrato_real` timestamp NULL DEFAULT NULL,
  `objeto_id` int(11) NOT NULL,
  `objeto_tipo` int(11) NOT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  `estado` int(11) NOT NULL,
  `bloqueado` int(11) DEFAULT NULL,
  `numero_contrato` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  `monto_contrato` decimal(15,2) NOT NULL DEFAULT 0.00,
  `nog` bigint(8) DEFAULT NULL,
  `tipo_revision` int(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fkcatergoriaadquisicion_idx` (`categoria_adquisicion`),
  KEY `fktipoadquisicion_idx` (`tipo_adquisicion`),
  KEY `fkunidadmedidaplan_idx` (`unidad_medida`),
  KEY `fkcatergoriaadquisicion` (`categoria_adquisicion`),
  CONSTRAINT `fkcatergoriaadquisicion` FOREIGN KEY (`categoria_adquisicion`) REFERENCES `categoria_adquisicion` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fktipoadquisicion` FOREIGN KEY (`tipo_adquisicion`) REFERENCES `tipo_adquisicion` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `plan_adquisicion`
--

LOCK TABLES `plan_adquisicion` WRITE;
/*!40000 ALTER TABLE `plan_adquisicion` DISABLE KEYS */;
/*!40000 ALTER TABLE `plan_adquisicion` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER tr_plan_adquisicion_insert
AFTER INSERT ON sipro.plan_adquisicion FOR EACH ROW
BEGIN
	DECLARE v_version int;
	SELECT max(a.version) INTO v_version FROM sipro_history.plan_adquisicion a WHERE a.id=NEW.id;

    IF(v_version is null) THEN
		SET v_version=1;
	END IF;

    INSERT INTO sipro_history.plan_adquisicion VALUE(NEW.id,NEW.tipo_adquisicion,NEW.categoria_adquisicion,NEW.unidad_medida,NEW.cantidad,NEW.total,NEW.precio_unitario,NEW.preparacion_doc_planificado,NEW.preparacion_doc_real,NEW.lanzamiento_evento_planificado,NEW.lanzamiento_evento_real,NEW.recepcion_ofertas_planificado,NEW.recepcion_ofertas_real,NEW.adjudicacion_planificado,NEW.adjudicacion_real,NEW.firma_contrato_planificado,NEW.firma_contrato_real,NEW.objeto_id,NEW.objeto_tipo,NEW.usuario_creo,NEW.usuario_actualizo,NEW.fecha_creacion,NEW.fecha_actualizacion,NEW.estado,NEW.bloqueado,NEW.numero_contrato,NEW.monto_contrato,NEW.nog,NEW.tipo_revision, v_version, NULL, 1);

END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER tr_plan_adquisicion_update
AFTER UPDATE ON sipro.plan_adquisicion FOR EACH ROW
BEGIN
		DECLARE v_version int;
		SELECT max(a.version) INTO v_version FROM sipro_history.plan_adquisicion a WHERE a.id=OLD.id;

		IF(v_version is null) THEN
            UPDATE sipro_history.plan_adquisicion SET actual=null WHERE id=OLD.id AND version is null;
            SET v_version=1;
		ELSE
            UPDATE sipro_history.plan_adquisicion SET actual=null WHERE id=OLD.id AND version=v_version;
            SET v_version=v_version+1;
        END IF;

        INSERT INTO sipro_history.plan_adquisicion VALUE(NEW.id,NEW.tipo_adquisicion,NEW.categoria_adquisicion,NEW.unidad_medida,NEW.cantidad,NEW.total,NEW.precio_unitario,NEW.preparacion_doc_planificado,NEW.preparacion_doc_real,NEW.lanzamiento_evento_planificado,NEW.lanzamiento_evento_real,NEW.recepcion_ofertas_planificado,NEW.recepcion_ofertas_real,NEW.adjudicacion_planificado,NEW.adjudicacion_real,NEW.firma_contrato_planificado,NEW.firma_contrato_real,NEW.objeto_id,NEW.objeto_tipo,NEW.usuario_creo,NEW.usuario_actualizo,NEW.fecha_creacion,NEW.fecha_actualizacion,NEW.estado,NEW.bloqueado,NEW.numero_contrato,NEW.monto_contrato,NEW.nog,NEW.tipo_revision, v_version, NULL, 1);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER tr_plan_adquisicion_delete
BEFORE DELETE ON sipro.plan_adquisicion FOR EACH ROW
BEGIN
	DECLARE v_version int;
	SELECT max(a.version) INTO v_version FROM sipro_history.plan_adquisicion a WHERE a.id=OLD.id;

	IF(v_version is null) THEN
		SET v_version=1;
	ELSE
		UPDATE sipro_history.plan_adquisicion SET actual=null WHERE id=OLD.id AND version=v_version;
		SET v_version=v_version+1;
	END IF;

	INSERT INTO sipro_history.plan_adquisicion VALUE(OLD.id,OLD.tipo_adquisicion,OLD.categoria_adquisicion,OLD.unidad_medida,OLD.cantidad,OLD.total,OLD.precio_unitario,OLD.preparacion_doc_planificado,OLD.preparacion_doc_real,OLD.lanzamiento_evento_planificado,OLD.lanzamiento_evento_real,OLD.recepcion_ofertas_planificado,OLD.recepcion_ofertas_real,OLD.adjudicacion_planificado,OLD.adjudicacion_real,OLD.firma_contrato_planificado,OLD.firma_contrato_real,OLD.objeto_id,OLD.objeto_tipo,OLD.usuario_creo,OLD.usuario_actualizo,OLD.fecha_creacion,OLD.fecha_actualizacion,OLD.estado,OLD.bloqueado,OLD.numero_contrato,OLD.monto_contrato,OLD.nog,OLD.tipo_revision, v_version, NULL, NULL);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `plan_adquisicion_pago`
--

DROP TABLE IF EXISTS `plan_adquisicion_pago`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `plan_adquisicion_pago` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `plan_adquisicionid` int(11) NOT NULL,
  `fecha_pago` timestamp NOT NULL DEFAULT current_timestamp(),
  `pago` decimal(15,2) DEFAULT NULL,
  `descripcion` varchar(100) COLLATE utf8_bin NOT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  `estado` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `plan_adquisicionid` (`plan_adquisicionid`),
  CONSTRAINT `fkplanadquisicionpago` FOREIGN KEY (`plan_adquisicionid`) REFERENCES `plan_adquisicion` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `plan_adquisicion_pago`
--

LOCK TABLES `plan_adquisicion_pago` WRITE;
/*!40000 ALTER TABLE `plan_adquisicion_pago` DISABLE KEYS */;
/*!40000 ALTER TABLE `plan_adquisicion_pago` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER tr_plan_adqui_pago_insert
AFTER INSERT ON sipro.plan_adquisicion_pago FOR EACH ROW
BEGIN
	DECLARE v_version int;
	SELECT max(a.version) INTO v_version FROM sipro_history.plan_adquisicion_pago a WHERE a.id=NEW.id;

    IF(v_version is null) THEN
		SET v_version=1;
	END IF;

    INSERT INTO sipro_history.plan_adquisicion_pago VALUE(NEW.id,NEW.plan_adquisicionid,NEW.fecha_pago,NEW.pago,NEW.descripcion,NEW.usuario_creo,NEW.usuario_actualizo,NEW.fecha_creacion,NEW.fecha_actualizacion,NEW.estado, v_version, NULL, 1);

END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER tr_plan_adqui_pago_update
AFTER UPDATE ON sipro.plan_adquisicion_pago FOR EACH ROW
BEGIN
		DECLARE v_version int;
		SELECT max(a.version) INTO v_version FROM sipro_history.plan_adquisicion_pago a WHERE a.id=OLD.id;

		IF(v_version is null) THEN
            UPDATE sipro_history.plan_adquisicion_pago SET actual=null WHERE id=OLD.id AND version is null;
            SET v_version=1;
		ELSE
            UPDATE sipro_history.plan_adquisicion_pago SET actual=null WHERE id=OLD.id AND version=v_version;
            SET v_version=v_version+1;
        END IF;

        INSERT INTO sipro_history.plan_adquisicion_pago VALUE(NEW.id,NEW.plan_adquisicionid,NEW.fecha_pago,NEW.pago,NEW.descripcion,NEW.usuario_creo,NEW.usuario_actualizo,NEW.fecha_creacion,NEW.fecha_actualizacion,NEW.estado, v_version, NULL, 1);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER tr_plan_adqui_pago_delete
BEFORE DELETE ON sipro.plan_adquisicion_pago FOR EACH ROW
BEGIN
	DECLARE v_version int;
	SELECT max(a.version) INTO v_version FROM sipro_history.plan_adquisicion_pago a WHERE a.id=OLD.id;

	IF(v_version is null) THEN
		SET v_version=1;
	ELSE
		UPDATE sipro_history.plan_adquisicion_pago SET actual=null WHERE id=OLD.id AND version=v_version;
		SET v_version=v_version+1;
	END IF;

	INSERT INTO sipro_history.plan_adquisicion_pago VALUE(OLD.id,OLD.plan_adquisicionid,OLD.fecha_pago,OLD.pago,OLD.descripcion,OLD.usuario_creo,OLD.usuario_actualizo,OLD.fecha_creacion,OLD.fecha_actualizacion,OLD.estado, v_version, NULL, NULL);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `prestamo`
--

DROP TABLE IF EXISTS `prestamo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `prestamo` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `fecha_corte` timestamp NULL DEFAULT NULL,
  `codigo_presupuestario` bigint(12) NOT NULL,
  `numero_prestamo` varchar(30) COLLATE utf8_bin NOT NULL,
  `destino` varchar(1000) COLLATE utf8_bin DEFAULT NULL,
  `sector_economico` varchar(1000) COLLATE utf8_bin DEFAULT NULL,
  `unidad_ejecutoraunidad_ejecutora` int(10) DEFAULT NULL,
  `fecha_firma` timestamp NULL DEFAULT NULL,
  `autorizacion_tipoid` int(10) DEFAULT NULL,
  `numero_autorizacion` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `fecha_autorizacion` timestamp NULL DEFAULT NULL,
  `anios_plazo` int(3) DEFAULT NULL,
  `anios_gracia` int(3) DEFAULT NULL,
  `fecha_fin_ejecucion` timestamp NULL DEFAULT NULL,
  `perido_ejecucion` int(3) DEFAULT NULL,
  `interes_tipoid` int(10) DEFAULT NULL,
  `porcentaje_interes` decimal(10,5) DEFAULT NULL,
  `porcentaje_comision_compra` decimal(10,5) DEFAULT NULL,
  `tipo_monedaid` int(10) NOT NULL,
  `monto_contratado` decimal(15,2) NOT NULL,
  `amortizado` decimal(15,2) DEFAULT NULL,
  `por_amortizar` decimal(15,2) DEFAULT NULL,
  `principal_anio` decimal(15,2) DEFAULT NULL,
  `intereses_anio` decimal(15,2) DEFAULT NULL,
  `comision_compromiso_anio` decimal(15,2) DEFAULT NULL,
  `otros_gastos` decimal(15,2) DEFAULT NULL,
  `principal_acumulado` decimal(15,2) DEFAULT NULL,
  `intereses_acumulados` decimal(15,2) DEFAULT NULL,
  `comision_compromiso_acumulado` decimal(15,2) DEFAULT NULL,
  `otros_cargos_acumulados` decimal(15,2) DEFAULT NULL,
  `presupuesto_asignado_funcionamiento` decimal(15,2) DEFAULT NULL,
  `prespupuesto_asignado_inversion` decimal(15,2) DEFAULT NULL,
  `presupuesto_modificado_funcionamiento` decimal(15,2) DEFAULT NULL,
  `presupuesto_modificado_inversion` decimal(15,2) DEFAULT NULL,
  `presupuesto_vigente_funcionamiento` decimal(15,2) DEFAULT NULL,
  `presupuesto_vigente_inversion` decimal(15,2) DEFAULT NULL,
  `prespupuesto_devengado_funcionamiento` decimal(15,2) DEFAULT NULL,
  `presupuesto_devengado_inversion` decimal(15,2) DEFAULT NULL,
  `presupuesto_pagado_funcionamiento` decimal(15,2) DEFAULT NULL,
  `presupuesto_pagado_inversion` decimal(15,2) DEFAULT NULL,
  `saldo_cuentas` decimal(15,2) DEFAULT NULL,
  `desembolsado_real` decimal(15,2) DEFAULT NULL,
  `ejecucion_estadoid` int(10) DEFAULT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NULL DEFAULT NULL,
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  `estado` int(2) NOT NULL,
  `proyecto_programa` varchar(100) COLLATE utf8_bin NOT NULL,
  `fecha_decreto` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_suscripcion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_elegibilidad_ue` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_cierre_origianl_ue` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_cierre_actual_ue` timestamp NOT NULL DEFAULT current_timestamp(),
  `meses_prorroga_ue` int(3) NOT NULL,
  `plazo_ejecucion_ue` int(3) DEFAULT NULL,
  `monto_asignado_ue` decimal(15,2) DEFAULT NULL,
  `desembolso_a_fecha_ue` decimal(15,2) DEFAULT NULL,
  `monto_por_desembolsar_ue` decimal(15,2) DEFAULT NULL,
  `fecha_vigencia` timestamp NOT NULL DEFAULT current_timestamp(),
  `monto_contratado_usd` decimal(15,2) NOT NULL,
  `monto_contratado_qtz` decimal(15,2) NOT NULL,
  `desembolso_a_fecha_usd` decimal(15,2) DEFAULT NULL,
  `monto_por_desembolsar_usd` decimal(15,2) NOT NULL,
  `monto_asignado_ue_usd` decimal(15,2) DEFAULT NULL,
  `monto_asignado_ue_qtz` decimal(15,2) DEFAULT NULL,
  `desembolso_a_fecha_ue_usd` decimal(15,2) DEFAULT NULL,
  `monto_por_desembolsar_ue_usd` decimal(15,2) DEFAULT NULL,
  `entidad` int(10) DEFAULT NULL,
  `ejercicio` int(4) DEFAULT NULL,
  `objetivo` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `objetivo_especifico` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `porcentaje_avance` int(3) DEFAULT NULL,
  `cooperantecodigo` int(5) NOT NULL,
  `cooperanteejercicio` int(4) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKprestamoue123_idx` (`unidad_ejecutoraunidad_ejecutora`,`entidad`,`ejercicio`),
  KEY `FKprestamo230739` (`tipo_monedaid`),
  KEY `FKprestamo248058` (`ejecucion_estadoid`),
  KEY `FKprestamo274437` (`interes_tipoid`),
  KEY `FKprestamo370308` (`autorizacion_tipoid`),
  KEY `FKprestamoue123` (`unidad_ejecutoraunidad_ejecutora`,`entidad`,`ejercicio`),
  KEY `fkprestamocooperante_idx` (`cooperanteejercicio`,`cooperantecodigo`),
  KEY `fkprestamocooperante` (`cooperantecodigo`,`cooperanteejercicio`),
  CONSTRAINT `FKprestamo230739` FOREIGN KEY (`tipo_monedaid`) REFERENCES `tipo_moneda` (`id`),
  CONSTRAINT `FKprestamo248058` FOREIGN KEY (`ejecucion_estadoid`) REFERENCES `ejecucion_estado` (`id`),
  CONSTRAINT `FKprestamo274437` FOREIGN KEY (`interes_tipoid`) REFERENCES `interes_tipo` (`id`),
  CONSTRAINT `FKprestamo370308` FOREIGN KEY (`autorizacion_tipoid`) REFERENCES `autorizacion_tipo` (`id`),
  CONSTRAINT `FKprestamoue123` FOREIGN KEY (`unidad_ejecutoraunidad_ejecutora`, `entidad`, `ejercicio`) REFERENCES `unidad_ejecutora` (`unidad_ejecutora`, `entidadentidad`, `ejercicio`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fkprestamocooperante` FOREIGN KEY (`cooperantecodigo`, `cooperanteejercicio`) REFERENCES `cooperante` (`codigo`, `ejercicio`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prestamo`
--

LOCK TABLES `prestamo` WRITE;
/*!40000 ALTER TABLE `prestamo` DISABLE KEYS */;
/*!40000 ALTER TABLE `prestamo` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER tr_prestamo_insert
AFTER INSERT ON sipro.prestamo FOR EACH ROW
BEGIN
	DECLARE v_version int;
	SELECT max(a.version) INTO v_version FROM sipro_history.prestamo a WHERE a.id=NEW.id;

    IF(v_version is null) THEN
		SET v_version=1;
	END IF;

    INSERT INTO sipro_history.prestamo VALUE(NEW.id,NEW.fecha_corte,NEW.codigo_presupuestario,NEW.numero_prestamo,NEW.destino,NEW.sector_economico,NEW.unidad_ejecutoraunidad_ejecutora,NEW.fecha_firma,NEW.autorizacion_tipoid,NEW.numero_autorizacion,NEW.fecha_autorizacion,NEW.anios_plazo,NEW.anios_gracia,NEW.fecha_fin_ejecucion,NEW.perido_ejecucion,NEW.interes_tipoid,NEW.porcentaje_interes,NEW.porcentaje_comision_compra,NEW.tipo_monedaid,NEW.monto_contratado,NEW.amortizado,NEW.por_amortizar,NEW.principal_anio,NEW.intereses_anio,NEW.comision_compromiso_anio,NEW.otros_gastos,NEW.principal_acumulado,NEW.intereses_acumulados,NEW.comision_compromiso_acumulado,NEW.otros_cargos_acumulados,NEW.presupuesto_asignado_funcionamiento,NEW.prespupuesto_asignado_inversion,NEW.presupuesto_modificado_funcionamiento,NEW.presupuesto_modificado_inversion,NEW.presupuesto_vigente_funcionamiento,NEW.presupuesto_vigente_inversion,NEW.prespupuesto_devengado_funcionamiento,NEW.presupuesto_devengado_inversion,NEW.presupuesto_pagado_funcionamiento,NEW.presupuesto_pagado_inversion,NEW.saldo_cuentas,NEW.desembolsado_real,NEW.ejecucion_estadoid,NEW.usuario_creo,NEW.usuario_actualizo,NEW.fecha_creacion,NEW.fecha_actualizacion,NEW.estado,NEW.proyecto_programa,NEW.fecha_decreto,NEW.fecha_suscripcion,NEW.fecha_elegibilidad_ue,NEW.fecha_cierre_origianl_ue,NEW.fecha_cierre_actual_ue,NEW.meses_prorroga_ue,NEW.plazo_ejecucion_ue,NEW.monto_asignado_ue,NEW.desembolso_a_fecha_ue,NEW.monto_por_desembolsar_ue,NEW.fecha_vigencia,NEW.monto_contratado_usd,NEW.monto_contratado_qtz,NEW.desembolso_a_fecha_usd,NEW.monto_por_desembolsar_usd,NEW.monto_asignado_ue_usd,NEW.monto_asignado_ue_qtz,NEW.desembolso_a_fecha_ue_usd,NEW.monto_por_desembolsar_ue_usd,NEW.entidad,NEW.ejercicio,NEW.objetivo,NEW.objetivo_especifico, NEW.porcentaje_avance, NEW.cooperantecodigo, NEW.cooperanteejercicio, v_version, NULL, 1);

END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER tr_prestamo_update
AFTER UPDATE ON sipro.prestamo FOR EACH ROW
BEGIN
		DECLARE v_version int;
		SELECT max(a.version) INTO v_version FROM sipro_history.prestamo a WHERE a.id=OLD.id;

		IF(v_version is null) THEN
            UPDATE sipro_history.prestamo SET actual=null WHERE id=OLD.id AND version is null;
            SET v_version=1;
		ELSE
            UPDATE sipro_history.prestamo SET actual=null WHERE id=OLD.id AND version=v_version;
            SET v_version=v_version+1;
        END IF;

        INSERT INTO sipro_history.prestamo VALUE(NEW.id,NEW.fecha_corte,NEW.codigo_presupuestario,NEW.numero_prestamo,NEW.destino,NEW.sector_economico,NEW.unidad_ejecutoraunidad_ejecutora,NEW.fecha_firma,NEW.autorizacion_tipoid,NEW.numero_autorizacion,NEW.fecha_autorizacion,NEW.anios_plazo,NEW.anios_gracia,NEW.fecha_fin_ejecucion,NEW.perido_ejecucion,NEW.interes_tipoid,NEW.porcentaje_interes,NEW.porcentaje_comision_compra,NEW.tipo_monedaid,NEW.monto_contratado,NEW.amortizado,NEW.por_amortizar,NEW.principal_anio,NEW.intereses_anio,NEW.comision_compromiso_anio,NEW.otros_gastos,NEW.principal_acumulado,NEW.intereses_acumulados,NEW.comision_compromiso_acumulado,NEW.otros_cargos_acumulados,NEW.presupuesto_asignado_funcionamiento,NEW.prespupuesto_asignado_inversion,NEW.presupuesto_modificado_funcionamiento,NEW.presupuesto_modificado_inversion,NEW.presupuesto_vigente_funcionamiento,NEW.presupuesto_vigente_inversion,NEW.prespupuesto_devengado_funcionamiento,NEW.presupuesto_devengado_inversion,NEW.presupuesto_pagado_funcionamiento,NEW.presupuesto_pagado_inversion,NEW.saldo_cuentas,NEW.desembolsado_real,NEW.ejecucion_estadoid,NEW.usuario_creo,NEW.usuario_actualizo,NEW.fecha_creacion,NEW.fecha_actualizacion,NEW.estado,NEW.proyecto_programa,NEW.fecha_decreto,NEW.fecha_suscripcion,NEW.fecha_elegibilidad_ue,NEW.fecha_cierre_origianl_ue,NEW.fecha_cierre_actual_ue,NEW.meses_prorroga_ue,NEW.plazo_ejecucion_ue,NEW.monto_asignado_ue,NEW.desembolso_a_fecha_ue,NEW.monto_por_desembolsar_ue,NEW.fecha_vigencia,NEW.monto_contratado_usd,NEW.monto_contratado_qtz,NEW.desembolso_a_fecha_usd,NEW.monto_por_desembolsar_usd,NEW.monto_asignado_ue_usd,NEW.monto_asignado_ue_qtz,NEW.desembolso_a_fecha_ue_usd,NEW.monto_por_desembolsar_ue_usd, NEW.entidad,NEW.ejercicio,NEW.objetivo,NEW.objetivo_especifico,NEW.porcentaje_avance, NEW.cooperantecodigo, NEW.cooperanteejercicio, v_version, NULL, 1);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER tr_prestamo_delete
BEFORE DELETE ON sipro.prestamo FOR EACH ROW
BEGIN
	DECLARE v_version int;
	SELECT max(a.version) INTO v_version FROM sipro_history.prestamo a WHERE a.id=OLD.id;

	IF(v_version is null) THEN
		SET v_version=1;
	ELSE
		UPDATE sipro_history.prestamo SET actual=null WHERE id=OLD.id AND version=v_version;
		SET v_version=v_version+1;
	END IF;

	INSERT INTO sipro_history.prestamo VALUE(OLD.id,OLD.fecha_corte,OLD.codigo_presupuestario,OLD.numero_prestamo,OLD.destino,OLD.sector_economico,OLD.unidad_ejecutoraunidad_ejecutora,OLD.fecha_firma,OLD.autorizacion_tipoid,OLD.numero_autorizacion,OLD.fecha_autorizacion,OLD.anios_plazo,OLD.anios_gracia,OLD.fecha_fin_ejecucion,OLD.perido_ejecucion,OLD.interes_tipoid,OLD.porcentaje_interes,OLD.porcentaje_comision_compra,OLD.tipo_monedaid,OLD.monto_contratado,OLD.amortizado,OLD.por_amortizar,OLD.principal_anio,OLD.intereses_anio,OLD.comision_compromiso_anio,OLD.otros_gastos,OLD.principal_acumulado,OLD.intereses_acumulados,OLD.comision_compromiso_acumulado,OLD.otros_cargos_acumulados,OLD.presupuesto_asignado_funcionamiento,OLD.prespupuesto_asignado_inversion,OLD.presupuesto_modificado_funcionamiento,OLD.presupuesto_modificado_inversion,OLD.presupuesto_vigente_funcionamiento,OLD.presupuesto_vigente_inversion,OLD.prespupuesto_devengado_funcionamiento,OLD.presupuesto_devengado_inversion,OLD.presupuesto_pagado_funcionamiento,OLD.presupuesto_pagado_inversion,OLD.saldo_cuentas,OLD.desembolsado_real,OLD.ejecucion_estadoid,OLD.usuario_creo,OLD.usuario_actualizo,OLD.fecha_creacion,OLD.fecha_actualizacion,OLD.estado,OLD.proyecto_programa,OLD.fecha_decreto,OLD.fecha_suscripcion,OLD.fecha_elegibilidad_ue,OLD.fecha_cierre_origianl_ue,OLD.fecha_cierre_actual_ue,OLD.meses_prorroga_ue,OLD.plazo_ejecucion_ue,OLD.monto_asignado_ue,OLD.desembolso_a_fecha_ue,OLD.monto_por_desembolsar_ue,OLD.fecha_vigencia,OLD.monto_contratado_usd,OLD.monto_contratado_qtz,OLD.desembolso_a_fecha_usd,OLD.monto_por_desembolsar_usd,OLD.monto_asignado_ue_usd,OLD.monto_asignado_ue_qtz,OLD.desembolso_a_fecha_ue_usd,OLD.monto_por_desembolsar_ue_usd,OLD.entidad,OLD.ejercicio,OLD.objetivo,OLD.objetivo_especifico, OLD.porcentaje_avance, OLD.cooperantecodigo, OLD.cooperanteejercicio, v_version, NULL, NULL);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `prestamo_tipo`
--

DROP TABLE IF EXISTS `prestamo_tipo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `prestamo_tipo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(1000) COLLATE utf8_bin NOT NULL,
  `descripcion` varchar(2000) COLLATE utf8_bin DEFAULT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  `estado` int(2) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prestamo_tipo`
--

LOCK TABLES `prestamo_tipo` WRITE;
/*!40000 ALTER TABLE `prestamo_tipo` DISABLE KEYS */;
INSERT INTO `prestamo_tipo` VALUES (1,'Inversión','Inversión','admin',NULL,'2017-10-28 00:44:08',NULL,1),(2,'Sectorial','Sectorial','admin',NULL,'2017-10-28 00:44:09',NULL,1),(3,'Apoyo presupuestario','Apoyo presupuestario','admin',NULL,'2017-10-28 00:44:09',NULL,1),(4,'Fomento a la exportación','Fomento a la exportación','admin',NULL,'2017-10-28 00:44:09',NULL,1);
/*!40000 ALTER TABLE `prestamo_tipo` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER tr_prestamo_tipo_insert
AFTER INSERT ON sipro.prestamo_tipo FOR EACH ROW
BEGIN
	DECLARE v_version int;
	SELECT max(a.version) INTO v_version FROM sipro_history.prestamo_tipo a WHERE a.id=NEW.id;

    IF(v_version is null) THEN
		SET v_version=1;
	END IF;

    INSERT INTO sipro_history.prestamo_tipo VALUE(NEW.id,NEW.nombre,NEW.descripcion,NEW.usuario_creo,NEW.usuario_actualizo,NEW.fecha_creacion,NEW.fecha_actualizacion,NEW.estado, v_version, NULL, 1);

END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER tr_prestamo_tipo_update
AFTER UPDATE ON sipro.prestamo_tipo FOR EACH ROW
BEGIN
		DECLARE v_version int;
		SELECT max(a.version) INTO v_version FROM sipro_history.prestamo_tipo a WHERE a.id=OLD.id;

		IF(v_version is null) THEN
            UPDATE sipro_history.prestamo_tipo SET actual=null WHERE id=OLD.id AND version is null;
            SET v_version=1;
		ELSE
            UPDATE sipro_history.prestamo_tipo SET actual=null WHERE id=OLD.id AND version=v_version;
            SET v_version=v_version+1;
        END IF;

        INSERT INTO sipro_history.prestamo_tipo VALUE(NEW.id,NEW.nombre,NEW.descripcion,NEW.usuario_creo,NEW.usuario_actualizo,NEW.fecha_creacion,NEW.fecha_actualizacion,NEW.estado, v_version, NULL, 1);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER tr_prestamo_tipo_delete
BEFORE DELETE ON sipro.prestamo_tipo FOR EACH ROW
BEGIN
	DECLARE v_version int;
	SELECT max(a.version) INTO v_version FROM sipro_history.prestamo_tipo a WHERE a.id=OLD.id;

	IF(v_version is null) THEN
		SET v_version=1;
	ELSE
		UPDATE sipro_history.prestamo_tipo SET actual=null WHERE id=OLD.id AND version=v_version;
		SET v_version=v_version+1;
	END IF;

	INSERT INTO sipro_history.prestamo_tipo VALUE(OLD.id,OLD.nombre,OLD.descripcion,OLD.usuario_creo,OLD.usuario_actualizo,OLD.fecha_creacion,OLD.fecha_actualizacion,OLD.estado, v_version, NULL, NULL);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `prestamo_tipo_prestamo`
--

DROP TABLE IF EXISTS `prestamo_tipo_prestamo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `prestamo_tipo_prestamo` (
  `prestamoId` int(11) NOT NULL,
  `tipoPrestamoId` int(11) NOT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  `estado` int(2) NOT NULL,
  PRIMARY KEY (`prestamoId`,`tipoPrestamoId`),
  KEY `fkprestamoid_idx` (`prestamoId`) USING BTREE,
  KEY `fktipoprestamo_idx` (`tipoPrestamoId`) USING BTREE,
  CONSTRAINT `fkprestamoid` FOREIGN KEY (`prestamoId`) REFERENCES `prestamo` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fktipoprestamo` FOREIGN KEY (`tipoPrestamoId`) REFERENCES `prestamo_tipo` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prestamo_tipo_prestamo`
--

LOCK TABLES `prestamo_tipo_prestamo` WRITE;
/*!40000 ALTER TABLE `prestamo_tipo_prestamo` DISABLE KEYS */;
/*!40000 ALTER TABLE `prestamo_tipo_prestamo` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER trigger_prest_t_prest_insert
AFTER INSERT ON sipro.prestamo_tipo_prestamo FOR EACH ROW
BEGIN
    DECLARE v_version int;
    SELECT max(a.version) INTO v_version FROM sipro_history.prestamo_tipo_prestamo a
    WHERE a.prestamoId=NEW.prestamoId and a.tipoPrestamoId = NEW.tipoPrestamoId;

    IF(v_version is null) THEN
        SET v_version=1;
    END IF;

    INSERT INTO sipro_history.prestamo_tipo_prestamo
    VALUE(NEW.prestamoId,NEW.tipoPrestamoId,NEW.usuario_creo,NEW.usuario_actualizo,
      NEW.fecha_creacion,NEW.fecha_actualizacion,NEW.estado,
      v_version, NULL, 1);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER trigger_prest_t_prest_update
AFTER UPDATE ON sipro.prestamo_tipo_prestamo FOR EACH ROW
BEGIN
    DECLARE v_version int;
    SELECT max(a.version) INTO v_version FROM sipro_history.prestamo_tipo_prestamo a
    WHERE a.prestamoId=OLD.prestamoId and a.tipoPrestamoId = OLD.tipoPrestamoId;

    IF(v_version is null) THEN
        UPDATE sipro_history.prestamo_tipo_prestamo a SET actual=null
        WHERE a.prestamoId=OLD.prestamoId and a.tipoPrestamoId = OLD.tipoPrestamoId AND version is null;
        SET v_version=1;
    ELSE
        UPDATE sipro_history.prestamo_tipo_prestamo a SET actual=null
        WHERE a.prestamoId=OLD.prestamoId and a.tipoPrestamoId = OLD.tipoPrestamoId AND version=v_version;
        SET v_version=v_version+1;
    END IF;

    INSERT INTO sipro_history.colaborador
    VALUE(NEW.prestamoId,NEW.tipoPrestamoId,NEW.usuario_creo,NEW.usuario_actualizo,
      NEW.fecha_creacion,NEW.fecha_actualizacion,NEW.estado,
       v_version, NULL, 1);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER trigger_prest_t_prest_delete
BEFORE DELETE ON sipro.prestamo_tipo_prestamo FOR EACH ROW
BEGIN
    DECLARE v_version int;
    SELECT max(a.version) INTO v_version FROM sipro_history.prestamo_tipo_prestamo a
     WHERE a.prestamoId=OLD.prestamoId and a.tipoPrestamoId = OLD.tipoPrestamoId;

    IF(v_version is null) THEN
        SET v_version=1;
    ELSE
        UPDATE sipro_history.prestamo_tipo_prestamo a SET actual=null
        WHERE a.prestamoId=OLD.prestamoId and a.tipoPrestamoId = OLD.tipoPrestamoId AND version=v_version;
        SET v_version=v_version+1;
    END IF;

    INSERT INTO sipro_history.prestamo_tipo_prestamo
    VALUE(OLD.prestamoId,OLD.tipoPrestamoId,OLD.usuario_creo,OLD.usuario_actualizo,
      OLD.fecha_creacion,OLD.fecha_actualizacion,OLD.estado,
       v_version, NULL, NULL);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `prestamo_usuario`
--

DROP TABLE IF EXISTS `prestamo_usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `prestamo_usuario` (
  `prestamoid` int(10) NOT NULL,
  `usuario` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NULL DEFAULT NULL,
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`prestamoid`,`usuario`),
  KEY `FKactividad_980179` (`prestamoid`) USING BTREE,
  CONSTRAINT `FKprestamo_980179` FOREIGN KEY (`prestamoid`) REFERENCES `prestamo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prestamo_usuario`
--

LOCK TABLES `prestamo_usuario` WRITE;
/*!40000 ALTER TABLE `prestamo_usuario` DISABLE KEYS */;
/*!40000 ALTER TABLE `prestamo_usuario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prodtipo_propiedad`
--

DROP TABLE IF EXISTS `prodtipo_propiedad`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `prodtipo_propiedad` (
  `producto_tipoid` int(10) NOT NULL,
  `producto_propiedadid` int(10) NOT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`producto_tipoid`,`producto_propiedadid`),
  KEY `FKprodtipo_p326363` (`producto_propiedadid`),
  KEY `FKprodtipo_p470266` (`producto_tipoid`),
  CONSTRAINT `FKprodtipo_p326363` FOREIGN KEY (`producto_propiedadid`) REFERENCES `producto_propiedad` (`id`),
  CONSTRAINT `FKprodtipo_p470266` FOREIGN KEY (`producto_tipoid`) REFERENCES `producto_tipo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prodtipo_propiedad`
--

LOCK TABLES `prodtipo_propiedad` WRITE;
/*!40000 ALTER TABLE `prodtipo_propiedad` DISABLE KEYS */;
/*!40000 ALTER TABLE `prodtipo_propiedad` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `producto`
--

DROP TABLE IF EXISTS `producto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `producto` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(1000) COLLATE utf8_bin NOT NULL,
  `descripcion` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `componenteid` int(10) DEFAULT NULL,
  `subcomponenteid` int(10) DEFAULT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  `producto_tipoid` int(10) NOT NULL,
  `estado` int(2) NOT NULL,
  `unidad_ejecutoraunidad_ejecutora` int(10) DEFAULT NULL,
  `snip` bigint(10) DEFAULT NULL,
  `programa` int(4) DEFAULT NULL,
  `subprograma` int(4) DEFAULT NULL,
  `proyecto` int(4) DEFAULT NULL,
  `actividad` int(4) DEFAULT NULL,
  `obra` int(4) DEFAULT NULL,
  `latitud` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `longitud` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `peso` int(3) DEFAULT NULL,
  `costo` decimal(15,2) DEFAULT NULL,
  `acumulacion_costoid` int(11) DEFAULT NULL,
  `renglon` int(4) DEFAULT NULL,
  `ubicacion_geografica` int(4) DEFAULT NULL,
  `fecha_inicio` timestamp NULL DEFAULT NULL,
  `fecha_fin` timestamp NULL DEFAULT NULL,
  `duracion` int(10) NOT NULL DEFAULT 0,
  `duracion_dimension` varchar(1) COLLATE utf8_bin DEFAULT NULL,
  `orden` int(10) DEFAULT NULL,
  `treePath` varchar(1000) COLLATE utf8_bin DEFAULT NULL,
  `nivel` int(4) DEFAULT NULL,
  `entidad` int(10) DEFAULT NULL,
  `ejercicio` int(4) DEFAULT NULL,
  `fecha_inicio_real` timestamp NULL DEFAULT NULL,
  `fecha_fin_real` timestamp NULL DEFAULT NULL,
  `inversion_nueva` int(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `fkacomulacioncostoproducto_idx` (`acumulacion_costoid`),
  KEY `fk_unidadejecutora124_idx` (`unidad_ejecutoraunidad_ejecutora`,`entidad`,`ejercicio`),
  KEY `fk_unidadejecutora124` (`unidad_ejecutoraunidad_ejecutora`,`entidad`,`ejercicio`),
  KEY `fkacomulacioncostoproducto` (`acumulacion_costoid`),
  KEY `FKproducto332920` (`componenteid`),
  KEY `FKproducto619237` (`producto_tipoid`),
  KEY `fk_subcomponente_1` (`subcomponenteid`) USING BTREE,
  CONSTRAINT `FKproducto332920` FOREIGN KEY (`componenteid`) REFERENCES `componente` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FKproducto619237` FOREIGN KEY (`producto_tipoid`) REFERENCES `producto_tipo` (`id`),
  CONSTRAINT `fk_subcomponente_1` FOREIGN KEY (`subcomponenteid`) REFERENCES `subcomponente` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_unidadejecutora124` FOREIGN KEY (`unidad_ejecutoraunidad_ejecutora`, `entidad`, `ejercicio`) REFERENCES `unidad_ejecutora` (`unidad_ejecutora`, `entidadentidad`, `ejercicio`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fkacomulacioncostoproducto` FOREIGN KEY (`acumulacion_costoid`) REFERENCES `acumulacion_costo` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `producto`
--

LOCK TABLES `producto` WRITE;
/*!40000 ALTER TABLE `producto` DISABLE KEYS */;
/*!40000 ALTER TABLE `producto` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER tr_producto_insert
AFTER INSERT ON sipro.producto FOR EACH ROW
BEGIN
	DECLARE v_version int;
	SELECT max(a.version) INTO v_version FROM sipro_history.producto a WHERE a.id=NEW.id;

    IF(v_version is null) THEN
		SET v_version=1;
	END IF;

    INSERT INTO sipro_history.producto VALUE(NEW.id,NEW.nombre,NEW.descripcion,NEW.componenteid,NEW.subcomponenteid,NEW.usuario_creo,NEW.usuario_actualizo,NEW.fecha_creacion,NEW.fecha_actualizacion,NEW.producto_tipoid,NEW.estado,NEW.unidad_ejecutoraunidad_ejecutora,NEW.snip,NEW.programa,NEW.subprograma,NEW.proyecto,NEW.actividad,NEW.obra,NEW.latitud,NEW.longitud,NEW.peso,NEW.costo,NEW.acumulacion_costoid,NEW.renglon,NEW.ubicacion_geografica,NEW.fecha_inicio,NEW.fecha_fin,NEW.duracion,NEW.duracion_dimension,NEW.orden,NEW.treePath,NEW.nivel,NEW.entidad,NEW.ejercicio,NEW.fecha_inicio_real,NEW.fecha_fin_real, NEW.inversion_nueva, v_version, NULL, 1);

END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER tr_producto_update
AFTER UPDATE ON sipro.producto FOR EACH ROW
BEGIN
		DECLARE v_version int;
		SELECT max(a.version) INTO v_version FROM sipro_history.producto a WHERE a.id=OLD.id;

		IF(v_version is null) THEN
            UPDATE sipro_history.producto SET actual=null WHERE id=OLD.id AND version is null;
            SET v_version=1;
		ELSE
            UPDATE sipro_history.producto SET actual=null WHERE id=OLD.id AND version=v_version;
            SET v_version=v_version+1;
        END IF;

        INSERT INTO sipro_history.producto VALUE(NEW.id,NEW.nombre,NEW.descripcion,NEW.componenteid,NEW.subcomponenteid,NEW.usuario_creo,NEW.usuario_actualizo,NEW.fecha_creacion,NEW.fecha_actualizacion,NEW.producto_tipoid,NEW.estado,NEW.unidad_ejecutoraunidad_ejecutora,NEW.snip,NEW.programa,NEW.subprograma,NEW.proyecto,NEW.actividad,NEW.obra,NEW.latitud,NEW.longitud,NEW.peso,NEW.costo,NEW.acumulacion_costoid,NEW.renglon,NEW.ubicacion_geografica,NEW.fecha_inicio,NEW.fecha_fin,NEW.duracion,NEW.duracion_dimension,NEW.orden,NEW.treePath,NEW.nivel,NEW.entidad,NEW.ejercicio,NEW.fecha_inicio_real,NEW.fecha_fin_real, NEW.inversion_nueva, v_version, NULL, 1);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER tr_producto_delete
BEFORE DELETE ON sipro.producto FOR EACH ROW
BEGIN
	DECLARE v_version int;
	SELECT max(a.version) INTO v_version FROM sipro_history.producto a WHERE a.id=OLD.id;

	IF(v_version is null) THEN
		SET v_version=1;
	ELSE
		UPDATE sipro_history.producto SET actual=null WHERE id=OLD.id AND version=v_version;
		SET v_version=v_version+1;
	END IF;

	INSERT INTO sipro_history.producto VALUE(OLD.id,OLD.nombre,OLD.descripcion,OLD.componenteid,OLD.subcomponenteid,OLD.usuario_creo,OLD.usuario_actualizo,OLD.fecha_creacion,OLD.fecha_actualizacion,OLD.producto_tipoid,OLD.estado,OLD.unidad_ejecutoraunidad_ejecutora,OLD.snip,OLD.programa,OLD.subprograma,OLD.proyecto,OLD.actividad,OLD.obra,OLD.latitud,OLD.longitud,OLD.peso,OLD.costo,OLD.acumulacion_costoid,OLD.renglon,OLD.ubicacion_geografica,OLD.fecha_inicio,OLD.fecha_fin,OLD.duracion,OLD.duracion_dimension,OLD.orden,OLD.treePath,OLD.nivel,OLD.entidad,OLD.ejercicio,OLD.fecha_inicio_real,OLD.fecha_fin_real, OLD.inversion_nueva, v_version, NULL, NULL);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `producto_propiedad`
--

DROP TABLE IF EXISTS `producto_propiedad`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `producto_propiedad` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(1000) COLLATE utf8_bin NOT NULL,
  `descripcion` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  `dato_tipoid` int(10) NOT NULL,
  `estado` int(2) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKproducto_p535852` (`dato_tipoid`),
  CONSTRAINT `FKproducto_p535852` FOREIGN KEY (`dato_tipoid`) REFERENCES `dato_tipo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `producto_propiedad`
--

LOCK TABLES `producto_propiedad` WRITE;
/*!40000 ALTER TABLE `producto_propiedad` DISABLE KEYS */;
/*!40000 ALTER TABLE `producto_propiedad` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `producto_propiedad_valor`
--

DROP TABLE IF EXISTS `producto_propiedad_valor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `producto_propiedad_valor` (
  `producto_propiedadid` int(10) NOT NULL,
  `productoid` int(10) NOT NULL,
  `valor_entero` int(10) DEFAULT NULL,
  `valor_string` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `valor_decimal` decimal(15,2) DEFAULT NULL,
  `valor_tiempo` timestamp NULL DEFAULT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  `estado` int(2) NOT NULL,
  PRIMARY KEY (`producto_propiedadid`,`productoid`),
  KEY `FKproducto_p148049` (`productoid`),
  KEY `FKproducto_p555801` (`producto_propiedadid`),
  CONSTRAINT `FKproducto_p148049` FOREIGN KEY (`productoid`) REFERENCES `producto` (`id`),
  CONSTRAINT `FKproducto_p555801` FOREIGN KEY (`producto_propiedadid`) REFERENCES `producto_propiedad` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `producto_propiedad_valor`
--

LOCK TABLES `producto_propiedad_valor` WRITE;
/*!40000 ALTER TABLE `producto_propiedad_valor` DISABLE KEYS */;
/*!40000 ALTER TABLE `producto_propiedad_valor` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER tr_prod_prop_val_insert
AFTER INSERT ON sipro.producto_propiedad_valor FOR EACH ROW
BEGIN
	DECLARE v_version int;
	SELECT max(a.version) INTO v_version FROM sipro_history.producto_propiedad_valor a WHERE a.producto_propiedadid=NEW.producto_propiedadid AND a.productoid=NEW.productoid;

    IF(v_version is null) THEN
		SET v_version=1;
	END IF;

    INSERT INTO sipro_history.producto_propiedad_valor VALUE(NEW.producto_propiedadid,NEW.productoid,NEW.valor_entero,NEW.valor_string,NEW.valor_decimal,NEW.valor_tiempo,NEW.usuario_creo,NEW.usuario_actualizo,NEW.fecha_creacion,NEW.fecha_actualizacion,NEW.estado, v_version, NULL, 1);

END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER tr_prod_prop_val_update
AFTER UPDATE ON sipro.producto_propiedad_valor FOR EACH ROW
BEGIN
		DECLARE v_version int;
		SELECT max(a.version) INTO v_version FROM sipro_history.producto_propiedad_valor a WHERE a.producto_propiedadid=NEW.producto_propiedadid AND a.productoid=NEW.productoid;

		IF(v_version is null) THEN
            UPDATE sipro_history.producto_propiedad_valor a SET actual=null WHERE a.producto_propiedadid=NEW.producto_propiedadid AND a.productoid=NEW.productoid AND version is null;
            SET v_version=1;
		ELSE
            UPDATE sipro_history.producto_propiedad_valor a SET actual=null WHERE a.producto_propiedadid=NEW.producto_propiedadid AND a.productoid=NEW.productoid AND version=v_version;
            SET v_version=v_version+1;
        END IF;

        INSERT INTO sipro_history.producto_propiedad_valor VALUE(NEW.producto_propiedadid,NEW.productoid,NEW.valor_entero,NEW.valor_string,NEW.valor_decimal,NEW.valor_tiempo,NEW.usuario_creo,NEW.usuario_actualizo,NEW.fecha_creacion,NEW.fecha_actualizacion,NEW.estado, v_version, NULL, 1);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER tr_prod_prop_val_delete
BEFORE DELETE ON sipro.producto_propiedad_valor FOR EACH ROW
BEGIN
	DECLARE v_version int;
	SELECT max(a.version) INTO v_version FROM sipro_history.producto_propiedad_valor a WHERE a.producto_propiedadid=OLD.producto_propiedadid AND a.productoid=OLD.productoid;

	IF(v_version is null) THEN
		SET v_version=1;
	ELSE
		UPDATE sipro_history.producto_propiedad_valor a SET actual=null WHERE a.producto_propiedadid=OLD.producto_propiedadid AND a.productoid=OLD.productoid AND version=v_version;
		SET v_version=v_version+1;
	END IF;

	INSERT INTO sipro_history.producto_propiedad_valor VALUE(OLD.producto_propiedadid,OLD.productoid,OLD.valor_entero,OLD.valor_string,OLD.valor_decimal,OLD.valor_tiempo,OLD.usuario_creo,OLD.usuario_actualizo,OLD.fecha_creacion,OLD.fecha_actualizacion,OLD.estado, v_version, NULL, NULL);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `producto_tipo`
--

DROP TABLE IF EXISTS `producto_tipo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `producto_tipo` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(1000) COLLATE utf8_bin NOT NULL,
  `descripcion` varchar(2000) COLLATE utf8_bin DEFAULT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  `estado` int(2) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `producto_tipo`
--

LOCK TABLES `producto_tipo` WRITE;
/*!40000 ALTER TABLE `producto_tipo` DISABLE KEYS */;
INSERT INTO `producto_tipo` VALUES (1,'General','General','admin',NULL,'2017-09-30 11:51:46',NULL,1);
/*!40000 ALTER TABLE `producto_tipo` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER tr_producto_tipo_insert
AFTER INSERT ON sipro.producto_tipo FOR EACH ROW
BEGIN
	DECLARE v_version int;
	SELECT max(a.version) INTO v_version FROM sipro_history.producto_tipo a WHERE a.id=NEW.id;

    IF(v_version is null) THEN
		SET v_version=1;
	END IF;

    INSERT INTO sipro_history.producto_tipo VALUE(NEW.id,NEW.nombre,NEW.descripcion,NEW.usuario_creo,NEW.usuario_actualizo,NEW.fecha_creacion,NEW.fecha_actualizacion,NEW.estado, v_version, NULL, 1);

END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER tr_producto_tipo_update
AFTER UPDATE ON sipro.producto_tipo FOR EACH ROW
BEGIN
		DECLARE v_version int;
		SELECT max(a.version) INTO v_version FROM sipro_history.producto_tipo a WHERE a.id=OLD.id;

		IF(v_version is null) THEN
            UPDATE sipro_history.producto_tipo SET actual=null WHERE id=OLD.id AND version is null;
            SET v_version=1;
		ELSE
            UPDATE sipro_history.producto_tipo SET actual=null WHERE id=OLD.id AND version=v_version;
            SET v_version=v_version+1;
        END IF;

        INSERT INTO sipro_history.producto_tipo VALUE(NEW.id,NEW.nombre,NEW.descripcion,NEW.usuario_creo,NEW.usuario_actualizo,NEW.fecha_creacion,NEW.fecha_actualizacion,NEW.estado, v_version, NULL, 1);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER tr_producto_tipo_delete
BEFORE DELETE ON sipro.producto_tipo FOR EACH ROW
BEGIN
	DECLARE v_version int;
	SELECT max(a.version) INTO v_version FROM sipro_history.producto_tipo a WHERE a.id=OLD.id;

	IF(v_version is null) THEN
		SET v_version=1;
	ELSE
		UPDATE sipro_history.producto_tipo SET actual=null WHERE id=OLD.id AND version=v_version;
		SET v_version=v_version+1;
	END IF;

	INSERT INTO sipro_history.producto_tipo VALUE(OLD.id,OLD.nombre,OLD.descripcion,OLD.usuario_creo,OLD.usuario_actualizo,OLD.fecha_creacion,OLD.fecha_actualizacion,OLD.estado, v_version, NULL, NULL);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `producto_usuario`
--

DROP TABLE IF EXISTS `producto_usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `producto_usuario` (
  `productoid` int(10) NOT NULL,
  `usuario` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NULL DEFAULT NULL,
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`productoid`,`usuario`),
  KEY `usuario` (`usuario`),
  KEY `FKproducto_u785635` (`productoid`),
  KEY `producto_usuario_ibfk_1` (`usuario`),
  CONSTRAINT `FKproducto_u785635` FOREIGN KEY (`productoid`) REFERENCES `producto` (`id`),
  CONSTRAINT `producto_usuario_ibfk_1` FOREIGN KEY (`usuario`) REFERENCES `usuario` (`usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `producto_usuario`
--

LOCK TABLES `producto_usuario` WRITE;
/*!40000 ALTER TABLE `producto_usuario` DISABLE KEYS */;
/*!40000 ALTER TABLE `producto_usuario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `programa`
--

DROP TABLE IF EXISTS `programa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `programa` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(2000) COLLATE utf8_bin DEFAULT NULL,
  `descripcion` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NULL DEFAULT NULL,
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  `estado` int(1) DEFAULT NULL,
  `programa_tipoid` int(10) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKprograma797850` (`programa_tipoid`),
  CONSTRAINT `FKprograma797850` FOREIGN KEY (`programa_tipoid`) REFERENCES `programa_tipo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `programa`
--

LOCK TABLES `programa` WRITE;
/*!40000 ALTER TABLE `programa` DISABLE KEYS */;
/*!40000 ALTER TABLE `programa` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `programa_propiedad`
--

DROP TABLE IF EXISTS `programa_propiedad`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `programa_propiedad` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(1000) COLLATE utf8_bin NOT NULL,
  `descripcion` varchar(2000) COLLATE utf8_bin DEFAULT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  `estado` int(2) NOT NULL,
  `dato_tipoid` int(10) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKprograma_p642904` (`dato_tipoid`),
  CONSTRAINT `FKprograma_p642904` FOREIGN KEY (`dato_tipoid`) REFERENCES `dato_tipo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `programa_propiedad`
--

LOCK TABLES `programa_propiedad` WRITE;
/*!40000 ALTER TABLE `programa_propiedad` DISABLE KEYS */;
/*!40000 ALTER TABLE `programa_propiedad` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `programa_propiedad_valor`
--

DROP TABLE IF EXISTS `programa_propiedad_valor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `programa_propiedad_valor` (
  `programa_propiedadid` int(10) NOT NULL,
  `programaid` int(10) NOT NULL,
  `valor_string` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `valor_entero` int(10) DEFAULT NULL,
  `valor_decimal` decimal(15,2) DEFAULT NULL,
  `valor_tiempo` timestamp NULL DEFAULT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NULL DEFAULT NULL,
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  `estado` int(2) DEFAULT NULL,
  PRIMARY KEY (`programa_propiedadid`,`programaid`),
  KEY `FKprograma_p313414` (`programa_propiedadid`),
  KEY `FKprograma_p662755` (`programaid`),
  CONSTRAINT `FKprograma_p313414` FOREIGN KEY (`programa_propiedadid`) REFERENCES `programa_propiedad` (`id`),
  CONSTRAINT `FKprograma_p662755` FOREIGN KEY (`programaid`) REFERENCES `programa` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `programa_propiedad_valor`
--

LOCK TABLES `programa_propiedad_valor` WRITE;
/*!40000 ALTER TABLE `programa_propiedad_valor` DISABLE KEYS */;
/*!40000 ALTER TABLE `programa_propiedad_valor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `programa_proyecto`
--

DROP TABLE IF EXISTS `programa_proyecto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `programa_proyecto` (
  `programaid` int(10) NOT NULL,
  `proyectoid` int(10) NOT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NULL DEFAULT NULL,
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  `estado` int(2) DEFAULT NULL,
  PRIMARY KEY (`programaid`,`proyectoid`),
  KEY `FKprograma_p53484` (`programaid`),
  KEY `FKprograma_p622682` (`proyectoid`),
  CONSTRAINT `FKprograma_p53484` FOREIGN KEY (`programaid`) REFERENCES `programa` (`id`),
  CONSTRAINT `FKprograma_p622682` FOREIGN KEY (`proyectoid`) REFERENCES `proyecto` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `programa_proyecto`
--

LOCK TABLES `programa_proyecto` WRITE;
/*!40000 ALTER TABLE `programa_proyecto` DISABLE KEYS */;
/*!40000 ALTER TABLE `programa_proyecto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `programa_tipo`
--

DROP TABLE IF EXISTS `programa_tipo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `programa_tipo` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `descripcion` varchar(2000) COLLATE utf8_bin DEFAULT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NULL DEFAULT NULL,
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  `estado` int(2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `programa_tipo`
--

LOCK TABLES `programa_tipo` WRITE;
/*!40000 ALTER TABLE `programa_tipo` DISABLE KEYS */;
INSERT INTO `programa_tipo` VALUES (1,'General','General','admin',NULL,'2017-10-02 14:13:19',NULL,1);
/*!40000 ALTER TABLE `programa_tipo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `progtipo_propiedad`
--

DROP TABLE IF EXISTS `progtipo_propiedad`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `progtipo_propiedad` (
  `programa_propiedadid` int(10) NOT NULL,
  `programa_tipoid` int(10) NOT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NULL DEFAULT NULL,
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  `estado` int(2) DEFAULT NULL,
  PRIMARY KEY (`programa_propiedadid`,`programa_tipoid`),
  KEY `FKprogtipo_p197701` (`programa_propiedadid`),
  KEY `FKprogtipo_p74917` (`programa_tipoid`),
  CONSTRAINT `FKprogtipo_p197701` FOREIGN KEY (`programa_propiedadid`) REFERENCES `programa_propiedad` (`id`),
  CONSTRAINT `FKprogtipo_p74917` FOREIGN KEY (`programa_tipoid`) REFERENCES `programa_tipo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `progtipo_propiedad`
--

LOCK TABLES `progtipo_propiedad` WRITE;
/*!40000 ALTER TABLE `progtipo_propiedad` DISABLE KEYS */;
/*!40000 ALTER TABLE `progtipo_propiedad` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `proyecto`
--

DROP TABLE IF EXISTS `proyecto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `proyecto` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(2000) COLLATE utf8_bin NOT NULL,
  `descripcion` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  `estado` int(2) NOT NULL,
  `proyecto_tipoid` int(10) NOT NULL,
  `unidad_ejecutoraunidad_ejecutora` int(10) DEFAULT NULL,
  `snip` bigint(10) DEFAULT NULL,
  `programa` int(4) DEFAULT NULL,
  `subprograma` int(4) DEFAULT NULL,
  `proyecto` int(4) DEFAULT NULL,
  `actividad` int(4) DEFAULT NULL,
  `obra` int(4) DEFAULT NULL,
  `latitud` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `longitud` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `objetivo` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `director_proyecto` int(10) DEFAULT NULL,
  `enunciado_alcance` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `costo` decimal(15,2) DEFAULT NULL,
  `acumulacion_costoid` int(11) DEFAULT NULL,
  `objetivo_especifico` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `vision_general` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  `renglon` int(4) DEFAULT NULL,
  `ubicacion_geografica` int(4) DEFAULT NULL,
  `fecha_inicio` timestamp NULL DEFAULT NULL,
  `fecha_fin` timestamp NULL DEFAULT NULL,
  `duracion` int(10) NOT NULL DEFAULT 0,
  `duracion_dimension` varchar(1) COLLATE utf8_bin DEFAULT NULL,
  `orden` int(10) DEFAULT NULL,
  `treePath` varchar(1000) COLLATE utf8_bin DEFAULT NULL,
  `nivel` int(4) DEFAULT NULL,
  `entidad` int(10) DEFAULT NULL,
  `ejercicio` int(4) DEFAULT NULL,
  `ejecucion_fisica_real` int(3) DEFAULT NULL,
  `proyecto_clase` int(2) NOT NULL DEFAULT 1,
  `project_cargado` int(1) DEFAULT NULL,
  `prestamoid` int(10) DEFAULT 0,
  `observaciones` varchar(2000) COLLATE utf8_bin DEFAULT NULL,
  `coordinador` int(1) DEFAULT NULL,
  `fecha_inicio_real` timestamp NULL DEFAULT NULL,
  `fecha_elegibilidad` timestamp NULL DEFAULT NULL,
  `fecha_fin_real` timestamp NULL DEFAULT NULL,
  `congelado` int(1) DEFAULT NULL,
  `fecha_cierre` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_unidadejecutoraproyecto123_idx` (`unidad_ejecutoraunidad_ejecutora`,`entidad`,`ejercicio`),
  KEY `fkacumulacioncostoproyecto_idx` (`acumulacion_costoid`),
  KEY `fk_unidadejecutoraproyecto123` (`unidad_ejecutoraunidad_ejecutora`,`entidad`,`ejercicio`),
  KEY `fkacumulacioncostoproyecto` (`acumulacion_costoid`),
  KEY `FKproyecto829715` (`proyecto_tipoid`),
  KEY `FKproyecto829716` (`director_proyecto`),
  KEY `fk_proyecto_clase` (`proyecto_clase`),
  KEY `fk_proyecto_prestamo4567_idx` (`prestamoid`) USING BTREE,
  CONSTRAINT `FKproyecto829715` FOREIGN KEY (`proyecto_tipoid`) REFERENCES `proyecto_tipo` (`id`),
  CONSTRAINT `FKproyecto829716` FOREIGN KEY (`director_proyecto`) REFERENCES `colaborador` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_proyecto_clase` FOREIGN KEY (`proyecto_clase`) REFERENCES `etiqueta` (`id`),
  CONSTRAINT `fk_proyecto_prestamo4567` FOREIGN KEY (`prestamoid`) REFERENCES `prestamo` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_unidadejecutoraproyecto123` FOREIGN KEY (`unidad_ejecutoraunidad_ejecutora`, `entidad`, `ejercicio`) REFERENCES `unidad_ejecutora` (`unidad_ejecutora`, `entidadentidad`, `ejercicio`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fkacumulacioncostoproyecto` FOREIGN KEY (`acumulacion_costoid`) REFERENCES `acumulacion_costo` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `proyecto`
--

LOCK TABLES `proyecto` WRITE;
/*!40000 ALTER TABLE `proyecto` DISABLE KEYS */;
/*!40000 ALTER TABLE `proyecto` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER tr_proyecto_insert
AFTER INSERT ON sipro.proyecto FOR EACH ROW
BEGIN
	DECLARE v_version int;
	SELECT max(a.version) INTO v_version FROM sipro_history.proyecto a WHERE a.id=NEW.id;

    IF(v_version is null) THEN
		SET v_version=1;
	END IF;

    INSERT INTO sipro_history.proyecto VALUE(NEW.id,NEW.nombre,NEW.descripcion,NEW.usuario_creo,NEW.usuario_actualizo,NEW.fecha_creacion,NEW.fecha_actualizacion,NEW.estado,NEW.proyecto_tipoid,NEW.unidad_ejecutoraunidad_ejecutora,NEW.snip,NEW.programa,NEW.subprograma,NEW.proyecto,NEW.actividad,NEW.obra,NEW.latitud,NEW.longitud,NEW.objetivo,NEW.director_proyecto,NEW.enunciado_alcance,NEW.costo,NEW.acumulacion_costoid,NEW.objetivo_especifico,NEW.vision_general,NEW.renglon,NEW.ubicacion_geografica,NEW.fecha_inicio,NEW.fecha_fin,NEW.duracion,NEW.duracion_dimension,NEW.orden,NEW.treePath,NEW.nivel,NEW.entidad,NEW.ejercicio,NEW.ejecucion_fisica_real,NEW.proyecto_clase,NEW.project_cargado,NEW.prestamoid,NEW.observaciones,NEW.coordinador,NEW.fecha_elegibilidad,NEW.fecha_cierre,NEW.fecha_inicio_real,NEW.fecha_fin_real, NEW.congelado, v_version, NULL, 1);

END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER tr_proyecto_update
AFTER UPDATE ON sipro.proyecto FOR EACH ROW
BEGIN
		DECLARE v_version int;
		SELECT max(a.version) INTO v_version FROM sipro_history.proyecto a WHERE a.id=OLD.id;

		IF(v_version is null) THEN
            UPDATE sipro_history.proyecto SET actual=null WHERE id=OLD.id AND version is null;
            SET v_version=1;
		ELSE
            UPDATE sipro_history.proyecto SET actual=null WHERE id=OLD.id AND version=v_version;
            SET v_version=v_version+1;
        END IF;

        INSERT INTO sipro_history.proyecto VALUE(NEW.id,NEW.nombre,NEW.descripcion,NEW.usuario_creo,NEW.usuario_actualizo,NEW.fecha_creacion,NEW.fecha_actualizacion,NEW.estado,NEW.proyecto_tipoid,NEW.unidad_ejecutoraunidad_ejecutora,NEW.snip,NEW.programa,NEW.subprograma,NEW.proyecto,NEW.actividad,NEW.obra,NEW.latitud,NEW.longitud,NEW.objetivo,NEW.director_proyecto,NEW.enunciado_alcance,NEW.costo,NEW.acumulacion_costoid,NEW.objetivo_especifico,NEW.vision_general,NEW.renglon,NEW.ubicacion_geografica,NEW.fecha_inicio,NEW.fecha_fin,NEW.duracion,NEW.duracion_dimension,NEW.orden,NEW.treePath,NEW.nivel,NEW.entidad,NEW.ejercicio,NEW.ejecucion_fisica_real,NEW.proyecto_clase,NEW.project_cargado,NEW.prestamoid,NEW.observaciones,NEW.coordinador,NEW.fecha_elegibilidad,NEW.fecha_cierre,NEW.fecha_inicio_real,NEW.fecha_fin_real, NEW.congelado, v_version, NULL, 1);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER tr_proyecto_delete
BEFORE DELETE ON sipro.proyecto FOR EACH ROW
BEGIN
	DECLARE v_version int;
	SELECT max(a.version) INTO v_version FROM sipro_history.proyecto a WHERE a.id=OLD.id;

	IF(v_version is null) THEN
		SET v_version=1;
	ELSE
		UPDATE sipro_history.proyecto SET actual=null WHERE id=OLD.id AND version=v_version;
		SET v_version=v_version+1;
	END IF;

	INSERT INTO sipro_history.proyecto VALUE(OLD.id,OLD.nombre,OLD.descripcion,OLD.usuario_creo,OLD.usuario_actualizo,OLD.fecha_creacion,OLD.fecha_actualizacion,OLD.estado,OLD.proyecto_tipoid,OLD.unidad_ejecutoraunidad_ejecutora,OLD.snip,OLD.programa,OLD.subprograma,OLD.proyecto,OLD.actividad,OLD.obra,OLD.latitud,OLD.longitud,OLD.objetivo,OLD.director_proyecto,OLD.enunciado_alcance,OLD.costo,OLD.acumulacion_costoid,OLD.objetivo_especifico,OLD.vision_general,OLD.renglon,OLD.ubicacion_geografica,OLD.fecha_inicio,OLD.fecha_fin,OLD.duracion,OLD.duracion_dimension,OLD.orden,OLD.treePath,OLD.nivel,OLD.entidad,OLD.ejercicio,OLD.ejecucion_fisica_real,OLD.proyecto_clase,OLD.project_cargado,OLD.prestamoid,OLD.observaciones,OLD.coordinador,OLD.fecha_elegibilidad,OLD.fecha_cierre,OLD.fecha_inicio_real,OLD.fecha_fin_real, OLD.congelado, v_version, NULL, NULL);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `proyecto_impacto`
--

DROP TABLE IF EXISTS `proyecto_impacto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `proyecto_impacto` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `proyectoid` int(10) NOT NULL,
  `entidadentidad` int(10) NOT NULL,
  `impacto` varchar(1000) COLLATE utf8_bin NOT NULL,
  `estado` int(2) NOT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  `ejercicio` int(4) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKentidad4567_idx` (`entidadentidad`,`ejercicio`),
  KEY `FKactividad138457_idx` (`proyectoid`),
  KEY `FKactividad138457` (`proyectoid`),
  KEY `FKentidad4567` (`entidadentidad`,`ejercicio`),
  CONSTRAINT `FKactividad138457` FOREIGN KEY (`proyectoid`) REFERENCES `proyecto` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FKentidad4567` FOREIGN KEY (`entidadentidad`, `ejercicio`) REFERENCES `entidad` (`entidad`, `ejercicio`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `proyecto_impacto`
--

LOCK TABLES `proyecto_impacto` WRITE;
/*!40000 ALTER TABLE `proyecto_impacto` DISABLE KEYS */;
/*!40000 ALTER TABLE `proyecto_impacto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `proyecto_miembro`
--

DROP TABLE IF EXISTS `proyecto_miembro`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `proyecto_miembro` (
  `proyectoid` int(10) NOT NULL,
  `colaboradorid` int(10) NOT NULL,
  `estado` int(1) NOT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`proyectoid`,`colaboradorid`),
  KEY `FKproyecto_557569_idx` (`colaboradorid`),
  KEY `FKproyecto_557568` (`proyectoid`),
  KEY `FKproyecto_557569` (`colaboradorid`),
  CONSTRAINT `FKproyecto_557568` FOREIGN KEY (`proyectoid`) REFERENCES `proyecto` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FKproyecto_557569` FOREIGN KEY (`colaboradorid`) REFERENCES `colaborador` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `proyecto_miembro`
--

LOCK TABLES `proyecto_miembro` WRITE;
/*!40000 ALTER TABLE `proyecto_miembro` DISABLE KEYS */;
/*!40000 ALTER TABLE `proyecto_miembro` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `proyecto_propiedad`
--

DROP TABLE IF EXISTS `proyecto_propiedad`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `proyecto_propiedad` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(1000) COLLATE utf8_bin NOT NULL,
  `descripcion` varchar(2000) COLLATE utf8_bin DEFAULT NULL,
  `dato_tipoid` int(10) NOT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  `estado` int(2) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKproyecto_p357270` (`dato_tipoid`),
  CONSTRAINT `FKproyecto_p357270` FOREIGN KEY (`dato_tipoid`) REFERENCES `dato_tipo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `proyecto_propiedad`
--

LOCK TABLES `proyecto_propiedad` WRITE;
/*!40000 ALTER TABLE `proyecto_propiedad` DISABLE KEYS */;
/*!40000 ALTER TABLE `proyecto_propiedad` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `proyecto_propiedad_valor`
--

DROP TABLE IF EXISTS `proyecto_propiedad_valor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `proyecto_propiedad_valor` (
  `proyectoid` int(10) NOT NULL,
  `proyecto_propiedadid` int(10) NOT NULL,
  `valor_string` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `valor_entero` int(10) DEFAULT NULL,
  `valor_decimal` decimal(15,2) DEFAULT NULL,
  `valor_tiempo` timestamp NULL DEFAULT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  `estado` int(2) NOT NULL,
  PRIMARY KEY (`proyectoid`,`proyecto_propiedadid`),
  KEY `FKproyecto_p219554` (`proyecto_propiedadid`),
  KEY `FKproyecto_p911551` (`proyectoid`),
  CONSTRAINT `FKproyecto_p219554` FOREIGN KEY (`proyecto_propiedadid`) REFERENCES `proyecto_propiedad` (`id`),
  CONSTRAINT `FKproyecto_p911551` FOREIGN KEY (`proyectoid`) REFERENCES `proyecto` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `proyecto_propiedad_valor`
--

LOCK TABLES `proyecto_propiedad_valor` WRITE;
/*!40000 ALTER TABLE `proyecto_propiedad_valor` DISABLE KEYS */;
/*!40000 ALTER TABLE `proyecto_propiedad_valor` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER tr_proy_prop_val_insert
AFTER INSERT ON sipro.proyecto_propiedad_valor FOR EACH ROW
BEGIN
	DECLARE v_version int;
	SELECT max(a.version) INTO v_version FROM sipro_history.proyecto_propiedad_valor a WHERE a.proyecto_propiedadid=NEW.proyecto_propiedadid AND a.proyectoid=NEW.proyectoid;

    IF(v_version is null) THEN
		SET v_version=1;
	END IF;

    INSERT INTO sipro_history.proyecto_propiedad_valor VALUE(NEW.proyectoid,NEW.proyecto_propiedadid,NEW.valor_string,NEW.valor_entero,NEW.valor_decimal,NEW.valor_tiempo,NEW.usuario_creo,NEW.usuario_actualizo,NEW.fecha_creacion,NEW.fecha_actualizacion,NEW.estado, v_version, NULL, 1);

END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER tr_proy_prop_val_update
AFTER UPDATE ON sipro.proyecto_propiedad_valor FOR EACH ROW
BEGIN
		DECLARE v_version int;
		SELECT max(a.version) INTO v_version FROM sipro_history.proyecto_propiedad_valor a WHERE a.proyecto_propiedadid=NEW.proyecto_propiedadid AND a.proyectoid=NEW.proyectoid;

		IF(v_version is null) THEN
            UPDATE sipro_history.proyecto_propiedad_valor a SET actual=null WHERE a.proyecto_propiedadid=NEW.proyecto_propiedadid AND a.proyectoid=NEW.proyectoid AND version is null;
            SET v_version=1;
		ELSE
            UPDATE sipro_history.proyecto_propiedad_valor a SET actual=null WHERE a.proyecto_propiedadid=NEW.proyecto_propiedadid AND a.proyectoid=NEW.proyectoid AND version=v_version;
            SET v_version=v_version+1;
        END IF;

        INSERT INTO sipro_history.proyecto_propiedad_valor VALUE(NEW.proyectoid,NEW.proyecto_propiedadid,NEW.valor_string,NEW.valor_entero,NEW.valor_decimal,NEW.valor_tiempo,NEW.usuario_creo,NEW.usuario_actualizo,NEW.fecha_creacion,NEW.fecha_actualizacion,NEW.estado, v_version, NULL, 1);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER tr_proy_prop_val_delete
BEFORE DELETE ON sipro.proyecto_propiedad_valor FOR EACH ROW
BEGIN
	DECLARE v_version int;
	SELECT max(a.version) INTO v_version FROM sipro_history.proyecto_propiedad_valor a WHERE a.proyecto_propiedadid=OLD.proyecto_propiedadid AND a.proyectoid=OLD.proyectoid;

	IF(v_version is null) THEN
		SET v_version=1;
	ELSE
		UPDATE sipro_history.proyecto_propiedad_valor a SET actual=null WHERE a.proyecto_propiedadid=OLD.proyecto_propiedadid AND a.proyectoid=OLD.proyectoid AND version=v_version;
		SET v_version=v_version+1;
	END IF;

	INSERT INTO sipro_history.proyecto_propiedad_valor VALUE(OLD.proyectoid,OLD.proyecto_propiedadid,OLD.valor_string,OLD.valor_entero,OLD.valor_decimal,OLD.valor_tiempo,OLD.usuario_creo,OLD.usuario_actualizo,OLD.fecha_creacion,OLD.fecha_actualizacion,OLD.estado, v_version, NULL, NULL);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `proyecto_rol_colaborador`
--

DROP TABLE IF EXISTS `proyecto_rol_colaborador`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `proyecto_rol_colaborador` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `colaboradorid` int(10) NOT NULL,
  `proyectoid` int(10) NOT NULL,
  `rol_unidad_ejecutoraid` int(10) NOT NULL,
  `estado` int(2) NOT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_proyecto_rol_colaborador_1_idx` (`colaboradorid`),
  KEY `fk_proyecto_rol_colaborador_2_idx` (`proyectoid`),
  KEY `fk_proyecto_rol_colaborador_3_idx` (`rol_unidad_ejecutoraid`),
  CONSTRAINT `fk_proyecto_rol_colaborador_1` FOREIGN KEY (`colaboradorid`) REFERENCES `colaborador` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_proyecto_rol_colaborador_2` FOREIGN KEY (`proyectoid`) REFERENCES `proyecto` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_proyecto_rol_colaborador_3` FOREIGN KEY (`rol_unidad_ejecutoraid`) REFERENCES `rol_unidad_ejecutora` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `proyecto_rol_colaborador`
--

LOCK TABLES `proyecto_rol_colaborador` WRITE;
/*!40000 ALTER TABLE `proyecto_rol_colaborador` DISABLE KEYS */;
/*!40000 ALTER TABLE `proyecto_rol_colaborador` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `proyecto_tipo`
--

DROP TABLE IF EXISTS `proyecto_tipo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `proyecto_tipo` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(1000) COLLATE utf8_bin NOT NULL,
  `descripcion` varchar(2000) COLLATE utf8_bin DEFAULT NULL,
  `usario_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  `estado` int(2) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `proyecto_tipo`
--

LOCK TABLES `proyecto_tipo` WRITE;
/*!40000 ALTER TABLE `proyecto_tipo` DISABLE KEYS */;
INSERT INTO `proyecto_tipo` VALUES (1,'General',NULL,'admin',NULL,'2017-09-30 11:49:37',NULL,1);
/*!40000 ALTER TABLE `proyecto_tipo` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER tr_proyecto_tipo_insert
AFTER INSERT ON sipro.proyecto_tipo FOR EACH ROW
BEGIN
	DECLARE v_version int;
	SELECT max(a.version) INTO v_version FROM sipro_history.proyecto_tipo a WHERE a.id=NEW.id;

    IF(v_version is null) THEN
		SET v_version=1;
	END IF;

    INSERT INTO sipro_history.proyecto_tipo VALUE(NEW.id,NEW.nombre,NEW.descripcion,NEW.usario_creo,NEW.usuario_actualizo,NEW.fecha_creacion,NEW.fecha_actualizacion,NEW.estado, v_version, NULL, 1);

END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER tr_proyecto_tipo_update
AFTER UPDATE ON sipro.proyecto_tipo FOR EACH ROW
BEGIN
		DECLARE v_version int;
		SELECT max(a.version) INTO v_version FROM sipro_history.proyecto_tipo a WHERE a.id=OLD.id;

		IF(v_version is null) THEN
            UPDATE sipro_history.proyecto_tipo SET actual=null WHERE id=OLD.id AND version is null;
            SET v_version=1;
		ELSE
            UPDATE sipro_history.proyecto_tipo SET actual=null WHERE id=OLD.id AND version=v_version;
            SET v_version=v_version+1;
        END IF;

        INSERT INTO sipro_history.proyecto_tipo VALUE(NEW.id,NEW.nombre,NEW.descripcion,NEW.usario_creo,NEW.usuario_actualizo,NEW.fecha_creacion,NEW.fecha_actualizacion,NEW.estado, v_version, NULL, 1);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER tr_proyecto_tipo_delete
BEFORE DELETE ON sipro.proyecto_tipo FOR EACH ROW
BEGIN
	DECLARE v_version int;
	SELECT max(a.version) INTO v_version FROM sipro_history.proyecto_tipo a WHERE a.id=OLD.id;

	IF(v_version is null) THEN
		SET v_version=1;
	ELSE
		UPDATE sipro_history.proyecto_tipo SET actual=null WHERE id=OLD.id AND version=v_version;
		SET v_version=v_version+1;
	END IF;

	INSERT INTO sipro_history.proyecto_tipo VALUE(OLD.id,OLD.nombre,OLD.descripcion,OLD.usario_creo,OLD.usuario_actualizo,OLD.fecha_creacion,OLD.fecha_actualizacion,OLD.estado, v_version, NULL, NULL);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `proyecto_usuario`
--

DROP TABLE IF EXISTS `proyecto_usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `proyecto_usuario` (
  `proyectoid` int(10) NOT NULL,
  `usuario` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NULL DEFAULT NULL,
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`proyectoid`,`usuario`),
  KEY `usuario` (`usuario`),
  KEY `FKproyecto_u77327` (`proyectoid`),
  KEY `proyecto_usuario_ibfk_1` (`usuario`),
  CONSTRAINT `FKproyecto_u77327` FOREIGN KEY (`proyectoid`) REFERENCES `proyecto` (`id`),
  CONSTRAINT `proyecto_usuario_ibfk_1` FOREIGN KEY (`usuario`) REFERENCES `usuario` (`usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `proyecto_usuario`
--

LOCK TABLES `proyecto_usuario` WRITE;
/*!40000 ALTER TABLE `proyecto_usuario` DISABLE KEYS */;
/*!40000 ALTER TABLE `proyecto_usuario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ptipo_propiedad`
--

DROP TABLE IF EXISTS `ptipo_propiedad`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ptipo_propiedad` (
  `proyecto_tipoid` int(10) NOT NULL,
  `proyecto_propiedadid` int(10) NOT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  `estado` int(2) DEFAULT NULL,
  PRIMARY KEY (`proyecto_tipoid`,`proyecto_propiedadid`),
  KEY `FKptipo_prop536620` (`proyecto_propiedadid`),
  KEY `FKptipo_prop684655` (`proyecto_tipoid`),
  CONSTRAINT `FKptipo_prop536620` FOREIGN KEY (`proyecto_propiedadid`) REFERENCES `proyecto_propiedad` (`id`),
  CONSTRAINT `FKptipo_prop684655` FOREIGN KEY (`proyecto_tipoid`) REFERENCES `proyecto_tipo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ptipo_propiedad`
--

LOCK TABLES `ptipo_propiedad` WRITE;
/*!40000 ALTER TABLE `ptipo_propiedad` DISABLE KEYS */;
/*!40000 ALTER TABLE `ptipo_propiedad` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER trigger_ptipo_prop_insert
AFTER INSERT ON sipro.ptipo_propiedad FOR EACH ROW
BEGIN
      DECLARE v_version int;
      SELECT max(a.version) INTO v_version FROM sipro_history.ptipo_propiedad a
      WHERE a.proyecto_tipoid=NEW.proyecto_tipoid AND a.proyecto_propiedadid = NEW.proyecto_propiedadid;

      IF(v_version is null) THEN
          SET v_version=1;
      END IF;

      INSERT INTO sipro_history.ptipo_propiedad
      VALUE(NEW.proyecto_tipoid,NEW.proyecto_propiedadid,NEW.usuario_creo,
        NEW.usuario_actualizo,NEW.fecha_creacion,NEW.fecha_actualizacion,NEW.estado,
        v_version, NULL, 1);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER trigger_ptipo_prop_update
AFTER UPDATE ON sipro.ptipo_propiedad FOR EACH ROW
BEGIN
DECLARE v_version int;
SELECT max(a.version) INTO v_version FROM sipro_history.ptipo_propiedad a
WHERE a.proyecto_tipoid=OLD.proyecto_tipoid AND a.proyecto_propiedadid = OLD.proyecto_propiedadid;

IF(v_version is null) THEN
    UPDATE sipro_history.ptipo_propiedad SET actual=null
    WHERE a.proyecto_tipoid=OLD.proyecto_tipoid AND a.proyecto_propiedadid = OLD.proyecto_propiedadid AND version is null;
    SET v_version=1;
ELSE
    UPDATE sipro_history.ptipo_propiedad SET actual=null
    WHERE a.proyecto_tipoid=OLD.proyecto_tipoid AND a.proyecto_propiedadid = OLD.proyecto_propiedadid AND version=v_version;
    SET v_version=v_version+1;
END IF;

INSERT INTO sipro_history.ptipo_propiedad
VALUE(NEW.proyecto_tipoid,NEW.proyecto_propiedadid,NEW.usuario_creo,
  NEW.usuario_actualizo,NEW.fecha_creacion,NEW.fecha_actualizacion,NEW.estado,
   v_version, NULL, 1);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER trigger_ptipo_prop_delete
BEFORE DELETE ON sipro.ptipo_propiedad FOR EACH ROW
BEGIN
    DECLARE v_version int;
    SELECT max(a.version) INTO v_version FROM sipro_history.ptipo_propiedad a
    WHERE a.proyecto_tipoid=OLD.proyecto_tipoid AND a.proyecto_propiedadid = OLD.proyecto_propiedadid;

    IF(v_version is null) THEN
        SET v_version=1;
    ELSE
        UPDATE sipro_history.ptipo_propiedad SET actual=null
        WHERE a.proyecto_tipoid=OLD.proyecto_tipoid AND a.proyecto_propiedadid = OLD.proyecto_propiedadid;
        SET v_version=v_version+1;
    END IF;

    INSERT INTO sipro_history.ptipo_propiedad
    VALUE(OLD.proyecto_tipoid,OLD.proyecto_propiedadid,OLD.usuario_creo,
      OLD.usuario_actualizo,OLD.fecha_creacion,OLD.fecha_actualizacion,OLD.estado,
       v_version, NULL, NULL);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `rectipo_propiedad`
--

DROP TABLE IF EXISTS `rectipo_propiedad`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rectipo_propiedad` (
  `recurso_propiedadid` int(10) NOT NULL,
  `recurso_tipoid` int(10) NOT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  `estado` int(2) NOT NULL,
  PRIMARY KEY (`recurso_propiedadid`,`recurso_tipoid`),
  KEY `FKrectipo_pr729557` (`recurso_tipoid`),
  KEY `FKrectipo_pr868901` (`recurso_propiedadid`),
  CONSTRAINT `FKrectipo_pr729557` FOREIGN KEY (`recurso_tipoid`) REFERENCES `recurso_tipo` (`id`),
  CONSTRAINT `FKrectipo_pr868901` FOREIGN KEY (`recurso_propiedadid`) REFERENCES `recurso_propiedad` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rectipo_propiedad`
--

LOCK TABLES `rectipo_propiedad` WRITE;
/*!40000 ALTER TABLE `rectipo_propiedad` DISABLE KEYS */;
/*!40000 ALTER TABLE `rectipo_propiedad` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `recurso`
--

DROP TABLE IF EXISTS `recurso`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `recurso` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(1000) COLLATE utf8_bin DEFAULT NULL,
  `descripcion` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  `estado` int(2) NOT NULL,
  `recurso_tipoid` int(10) NOT NULL,
  `recurso_unidad_medidaid` int(10) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKrecurso892046` (`recurso_unidad_medidaid`),
  KEY `FKrecurso944662` (`recurso_tipoid`),
  CONSTRAINT `FKrecurso892046` FOREIGN KEY (`recurso_unidad_medidaid`) REFERENCES `recurso_unidad_medida` (`id`),
  CONSTRAINT `FKrecurso944662` FOREIGN KEY (`recurso_tipoid`) REFERENCES `recurso_tipo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recurso`
--

LOCK TABLES `recurso` WRITE;
/*!40000 ALTER TABLE `recurso` DISABLE KEYS */;
/*!40000 ALTER TABLE `recurso` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `recurso_propiedad`
--

DROP TABLE IF EXISTS `recurso_propiedad`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `recurso_propiedad` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `dato_tipoid` int(10) NOT NULL,
  `nombre` varchar(1000) COLLATE utf8_bin NOT NULL,
  `descripcion` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  `estado` int(2) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKrecurso_pr849099` (`dato_tipoid`),
  CONSTRAINT `FKrecurso_pr849099` FOREIGN KEY (`dato_tipoid`) REFERENCES `dato_tipo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recurso_propiedad`
--

LOCK TABLES `recurso_propiedad` WRITE;
/*!40000 ALTER TABLE `recurso_propiedad` DISABLE KEYS */;
/*!40000 ALTER TABLE `recurso_propiedad` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `recurso_tipo`
--

DROP TABLE IF EXISTS `recurso_tipo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `recurso_tipo` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(1000) COLLATE utf8_bin NOT NULL,
  `descripcion` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_actualizacion` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  `estado` int(2) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recurso_tipo`
--

LOCK TABLES `recurso_tipo` WRITE;
/*!40000 ALTER TABLE `recurso_tipo` DISABLE KEYS */;
/*!40000 ALTER TABLE `recurso_tipo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `recurso_unidad_medida`
--

DROP TABLE IF EXISTS `recurso_unidad_medida`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `recurso_unidad_medida` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(1000) COLLATE utf8_bin NOT NULL,
  `descripcion` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `simbolo` varchar(10) COLLATE utf8_bin DEFAULT NULL,
  `estado` int(2) NOT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recurso_unidad_medida`
--

LOCK TABLES `recurso_unidad_medida` WRITE;
/*!40000 ALTER TABLE `recurso_unidad_medida` DISABLE KEYS */;
/*!40000 ALTER TABLE `recurso_unidad_medida` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `riesgo`
--

DROP TABLE IF EXISTS `riesgo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `riesgo` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(1000) COLLATE utf8_bin NOT NULL,
  `descripcion` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `riesgo_tipoid` int(10) NOT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  `estado` int(2) NOT NULL,
  `impacto` decimal(3,2) NOT NULL,
  `probabilidad` decimal(3,2) NOT NULL,
  `impacto_monto` decimal(15,2) DEFAULT NULL,
  `riesgos_segundarios` varchar(1000) COLLATE utf8_bin DEFAULT NULL,
  `ejecutado` int(1) NOT NULL DEFAULT 0,
  `fecha_ejecucion` timestamp NULL DEFAULT NULL,
  `resultado` varchar(1000) COLLATE utf8_bin DEFAULT NULL,
  `colaboradorid` int(10) DEFAULT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `solucion` varchar(1000) COLLATE utf8_bin DEFAULT NULL,
  `observaciones` varchar(1000) COLLATE utf8_bin DEFAULT NULL,
  `consecuencia` varchar(1000) COLLATE utf8_bin DEFAULT NULL,
  `impacto_tiempo` decimal(15,2) DEFAULT NULL,
  `gatillo` varchar(1000) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKriesgo148789` (`colaboradorid`),
  KEY `FKriesgo901387` (`riesgo_tipoid`),
  CONSTRAINT `FKriesgo148789` FOREIGN KEY (`colaboradorid`) REFERENCES `colaborador` (`id`),
  CONSTRAINT `FKriesgo901387` FOREIGN KEY (`riesgo_tipoid`) REFERENCES `riesgo_tipo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `riesgo`
--

LOCK TABLES `riesgo` WRITE;
/*!40000 ALTER TABLE `riesgo` DISABLE KEYS */;
/*!40000 ALTER TABLE `riesgo` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER tr_riesgo_insert
AFTER INSERT ON sipro.riesgo FOR EACH ROW
BEGIN
	DECLARE v_version int;
	SELECT max(a.version) INTO v_version FROM sipro_history.riesgo a WHERE a.id=NEW.id;

    IF(v_version is null) THEN
		SET v_version=1;
	END IF;

    INSERT INTO sipro_history.riesgo VALUE(NEW.id,NEW.nombre,NEW.descripcion,NEW.riesgo_tipoid,NEW.impacto,NEW.probabilidad,NEW.impacto_monto,NEW.impacto_tiempo,NEW.gatillo,NEW.consecuencia,NEW.solucion,NEW.riesgos_segundarios,NEW.ejecutado,NEW.fecha_ejecucion,NEW.resultado,NEW.observaciones,NEW.colaboradorid,NEW.usuario_creo,NEW.usuario_actualizo,NEW.fecha_creacion,NEW.fecha_actualizacion,NEW.estado, v_version, NULL, 1);

END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER tr_riesgo_update
AFTER UPDATE ON sipro.riesgo FOR EACH ROW
BEGIN
		DECLARE v_version int;
		SELECT max(a.version) INTO v_version FROM sipro_history.riesgo a WHERE a.id=OLD.id;

		IF(v_version is null) THEN
            UPDATE sipro_history.riesgo SET actual=null WHERE id=OLD.id AND version is null;
            SET v_version=1;
		ELSE
            UPDATE sipro_history.riesgo SET actual=null WHERE id=OLD.id AND version=v_version;
            SET v_version=v_version+1;
        END IF;

        INSERT INTO sipro_history.riesgo VALUE(NEW.id,NEW.nombre,NEW.descripcion,NEW.riesgo_tipoid,NEW.impacto,NEW.probabilidad,NEW.impacto_monto,NEW.impacto_tiempo,NEW.gatillo,NEW.consecuencia,NEW.solucion,NEW.riesgos_segundarios,NEW.ejecutado,NEW.fecha_ejecucion,NEW.resultado,NEW.observaciones,NEW.colaboradorid,NEW.usuario_creo,NEW.usuario_actualizo,NEW.fecha_creacion,NEW.fecha_actualizacion,NEW.estado, v_version, NULL, 1);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER tr_riesgo_delete
BEFORE DELETE ON sipro.riesgo FOR EACH ROW
BEGIN
	DECLARE v_version int;
	SELECT max(a.version) INTO v_version FROM sipro_history.riesgo a WHERE a.id=OLD.id;

	IF(v_version is null) THEN
		SET v_version=1;
	ELSE
		UPDATE sipro_history.riesgo SET actual=null WHERE id=OLD.id AND version=v_version;
		SET v_version=v_version+1;
	END IF;

	INSERT INTO sipro_history.riesgo VALUE(OLD.id,OLD.nombre,OLD.descripcion,OLD.riesgo_tipoid,OLD.impacto,OLD.probabilidad,OLD.impacto_monto,OLD.impacto_tiempo,OLD.gatillo,OLD.consecuencia,OLD.solucion,OLD.riesgos_segundarios,OLD.ejecutado,OLD.fecha_ejecucion,OLD.resultado,OLD.observaciones,OLD.colaboradorid,OLD.usuario_creo,OLD.usuario_actualizo,OLD.fecha_creacion,OLD.fecha_actualizacion,OLD.estado, v_version, NULL, NULL);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `riesgo_propiedad`
--

DROP TABLE IF EXISTS `riesgo_propiedad`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `riesgo_propiedad` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(1000) COLLATE utf8_bin NOT NULL,
  `descripcion` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  `estado` int(2) NOT NULL,
  `dato_tipoid` int(10) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKriesgo_pro642830` (`dato_tipoid`),
  CONSTRAINT `FKriesgo_pro642830` FOREIGN KEY (`dato_tipoid`) REFERENCES `dato_tipo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `riesgo_propiedad`
--

LOCK TABLES `riesgo_propiedad` WRITE;
/*!40000 ALTER TABLE `riesgo_propiedad` DISABLE KEYS */;
/*!40000 ALTER TABLE `riesgo_propiedad` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `riesgo_propiedad_valor`
--

DROP TABLE IF EXISTS `riesgo_propiedad_valor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `riesgo_propiedad_valor` (
  `riesgoid` int(10) NOT NULL,
  `riesgo_propiedadid` int(10) NOT NULL,
  `valor_entero` int(10) DEFAULT NULL,
  `valor_string` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `valor_decimal` decimal(15,2) DEFAULT NULL,
  `valor_tiempo` timestamp NULL DEFAULT NULL,
  `estado` int(2) NOT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`riesgoid`,`riesgo_propiedadid`),
  KEY `FKriesgo_pro389855` (`riesgoid`),
  KEY `FKriesgo_pro900192` (`riesgo_propiedadid`),
  CONSTRAINT `FKriesgo_pro389855` FOREIGN KEY (`riesgoid`) REFERENCES `riesgo` (`id`),
  CONSTRAINT `FKriesgo_pro900192` FOREIGN KEY (`riesgo_propiedadid`) REFERENCES `riesgo_propiedad` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `riesgo_propiedad_valor`
--

LOCK TABLES `riesgo_propiedad_valor` WRITE;
/*!40000 ALTER TABLE `riesgo_propiedad_valor` DISABLE KEYS */;
/*!40000 ALTER TABLE `riesgo_propiedad_valor` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER tr_riesgo_prop_val_insert
AFTER INSERT ON sipro.riesgo_propiedad_valor FOR EACH ROW
BEGIN
	DECLARE v_version int;
	SELECT max(a.version) INTO v_version FROM sipro_history.riesgo_propiedad_valor a WHERE a.riesgoid=NEW.riesgoid AND a.riesgo_propiedadid=NEW.riesgo_propiedadid;

    IF(v_version is null) THEN
		SET v_version=1;
	END IF;

    INSERT INTO sipro_history.riesgo_propiedad_valor VALUE(NEW.riesgoid,NEW.riesgo_propiedadid,NEW.valor_entero,NEW.valor_string,NEW.valor_decimal,NEW.valor_tiempo,NEW.estado,NEW.usuario_creo,NEW.usuario_actualizo,NEW.fecha_creacion,NEW.fecha_actualizacion, v_version, NULL, 1);

END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER tr_riesgo_prop_val_update
AFTER UPDATE ON sipro.riesgo_propiedad_valor FOR EACH ROW
BEGIN
		DECLARE v_version int;
		SELECT max(a.version) INTO v_version FROM sipro_history.riesgo_propiedad_valor a WHERE a.riesgoid=NEW.riesgoid AND a.riesgo_propiedadid=NEW.riesgo_propiedadid;

		IF(v_version is null) THEN
            UPDATE sipro_history.riesgo_propiedad_valor SET actual=null WHERE a.riesgoid=NEW.riesgoid AND a.riesgo_propiedadid=NEW.riesgo_propiedadid AND version is null;
            SET v_version=1;
		ELSE
            UPDATE sipro_history.riesgo_propiedad_valor SET actual=null WHERE a.riesgoid=NEW.riesgoid AND a.riesgo_propiedadid=NEW.riesgo_propiedadid AND version=v_version;
            SET v_version=v_version+1;
        END IF;

        INSERT INTO sipro_history.riesgo_propiedad_valor VALUE(NEW.riesgoid,NEW.riesgo_propiedadid,NEW.valor_entero,NEW.valor_string,NEW.valor_decimal,NEW.valor_tiempo,NEW.estado,NEW.usuario_creo,NEW.usuario_actualizo,NEW.fecha_creacion,NEW.fecha_actualizacion, v_version, NULL, 1);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER tr_riesgo_prop_val_delete
BEFORE DELETE ON sipro.riesgo_propiedad_valor FOR EACH ROW
BEGIN
	DECLARE v_version int;
	SELECT max(a.version) INTO v_version FROM sipro_history.riesgo_propiedad_valor a WHERE a.riesgoid=OLD.riesgoid AND a.riesgo_propiedadid=OLD.riesgo_propiedadid;

	IF(v_version is null) THEN
		SET v_version=1;
	ELSE
		UPDATE sipro_history.riesgo_propiedad_valor SET actual=null WHERE a.riesgoid=OLD.riesgoid AND a.riesgo_propiedadid=OLD.riesgo_propiedadid AND version=v_version;
		SET v_version=v_version+1;
	END IF;

	INSERT INTO sipro_history.riesgo_propiedad_valor VALUE(OLD.riesgoid,OLD.riesgo_propiedadid,OLD.valor_entero,OLD.valor_string,OLD.valor_decimal,OLD.valor_tiempo,OLD.estado,OLD.usuario_creo,OLD.usuario_actualizo,OLD.fecha_creacion,OLD.fecha_actualizacion, v_version, NULL, NULL);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `riesgo_tipo`
--

DROP TABLE IF EXISTS `riesgo_tipo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `riesgo_tipo` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(1000) COLLATE utf8_bin NOT NULL,
  `descripcion` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  `estado` int(2) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `riesgo_tipo`
--

LOCK TABLES `riesgo_tipo` WRITE;
/*!40000 ALTER TABLE `riesgo_tipo` DISABLE KEYS */;
INSERT INTO `riesgo_tipo` VALUES (1,'General','General','admin',NULL,'2017-10-02 14:14:23',NULL,1);
/*!40000 ALTER TABLE `riesgo_tipo` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER tr_riesgo_tipo_insert
AFTER INSERT ON sipro.riesgo_tipo FOR EACH ROW
BEGIN
	DECLARE v_version int;
	SELECT max(a.version) INTO v_version FROM sipro_history.riesgo_tipo a WHERE a.id=NEW.id;

    IF(v_version is null) THEN
		SET v_version=1;
	END IF;

    INSERT INTO sipro_history.riesgo_tipo VALUE(NEW.id,NEW.nombre,NEW.descripcion,NEW.usuario_creo,NEW.usuario_actualizo,NEW.fecha_creacion,NEW.fecha_actualizacion,NEW.estado, v_version, NULL, 1);

END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER tr_riesgo_tipo_update
AFTER UPDATE ON sipro.riesgo_tipo FOR EACH ROW
BEGIN
		DECLARE v_version int;
		SELECT max(a.version) INTO v_version FROM sipro_history.riesgo_tipo a WHERE a.id=OLD.id;

		IF(v_version is null) THEN
            UPDATE sipro_history.riesgo_tipo SET actual=null WHERE id=OLD.id AND version is null;
            SET v_version=1;
		ELSE
            UPDATE sipro_history.riesgo_tipo SET actual=null WHERE id=OLD.id AND version=v_version;
            SET v_version=v_version+1;
        END IF;

        INSERT INTO sipro_history.riesgo_tipo VALUE(NEW.id,NEW.nombre,NEW.descripcion,NEW.usuario_creo,NEW.usuario_actualizo,NEW.fecha_creacion,NEW.fecha_actualizacion,NEW.estado, v_version, NULL, 1);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER tr_riesgo_tipo_delete
BEFORE DELETE ON sipro.riesgo_tipo FOR EACH ROW
BEGIN
	DECLARE v_version int;
	SELECT max(a.version) INTO v_version FROM sipro_history.riesgo_tipo a WHERE a.id=OLD.id;

	IF(v_version is null) THEN
		SET v_version=1;
	ELSE
		UPDATE sipro_history.riesgo_tipo SET actual=null WHERE id=OLD.id AND version=v_version;
		SET v_version=v_version+1;
	END IF;

	INSERT INTO sipro_history.riesgo_tipo VALUE(OLD.id,OLD.nombre,OLD.descripcion,OLD.usuario_creo,OLD.usuario_actualizo,OLD.fecha_creacion,OLD.fecha_actualizacion,OLD.estado, v_version, NULL, NULL);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `rol`
--

DROP TABLE IF EXISTS `rol`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rol` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) COLLATE utf8_bin NOT NULL,
  `descripcion` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  `estado` int(2) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rol`
--

LOCK TABLES `rol` WRITE;
/*!40000 ALTER TABLE `rol` DISABLE KEYS */;
/*!40000 ALTER TABLE `rol` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rol_permiso`
--

DROP TABLE IF EXISTS `rol_permiso`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rol_permiso` (
  `rolid` int(10) NOT NULL,
  `permisoid` int(10) NOT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  `estado` int(2) NOT NULL,
  PRIMARY KEY (`rolid`,`permisoid`),
  KEY `FKrol_permis272334` (`permisoid`),
  KEY `FKrol_permis670833` (`rolid`),
  CONSTRAINT `FKrol_permis272334` FOREIGN KEY (`permisoid`) REFERENCES `permiso` (`id`),
  CONSTRAINT `FKrol_permis670833` FOREIGN KEY (`rolid`) REFERENCES `rol` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rol_permiso`
--

LOCK TABLES `rol_permiso` WRITE;
/*!40000 ALTER TABLE `rol_permiso` DISABLE KEYS */;
/*!40000 ALTER TABLE `rol_permiso` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rol_unidad_ejecutora`
--

DROP TABLE IF EXISTS `rol_unidad_ejecutora`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rol_unidad_ejecutora` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(500) COLLATE utf8_bin NOT NULL,
  `estado` int(2) NOT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  `rol_predeterminado` int(2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rol_unidad_ejecutora`
--

LOCK TABLES `rol_unidad_ejecutora` WRITE;
/*!40000 ALTER TABLE `rol_unidad_ejecutora` DISABLE KEYS */;
INSERT INTO `rol_unidad_ejecutora` VALUES (1,'Encargado Financiero',1,'admin',NULL,'2017-10-24 18:00:00',NULL,1),(2,'Encargado de Adquisiciones',1,'admin',NULL,'2017-10-24 18:00:00',NULL,1),(3,'Encargado de Monitoreo',1,'admin',NULL,'2017-10-24 18:00:00',NULL,1),(4,'Coordinador de la Unidad Ejecutora',1,'admin',NULL,'2017-10-24 18:00:00',NULL,1);
/*!40000 ALTER TABLE `rol_unidad_ejecutora` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rol_usuario_proyecto`
--

DROP TABLE IF EXISTS `rol_usuario_proyecto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rol_usuario_proyecto` (
  `rol` int(11) NOT NULL,
  `proyecto` int(11) NOT NULL,
  `usuario` varchar(30) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`rol`,`proyecto`,`usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rol_usuario_proyecto`
--

LOCK TABLES `rol_usuario_proyecto` WRITE;
/*!40000 ALTER TABLE `rol_usuario_proyecto` DISABLE KEYS */;
/*!40000 ALTER TABLE `rol_usuario_proyecto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rtipo_propiedad`
--

DROP TABLE IF EXISTS `rtipo_propiedad`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rtipo_propiedad` (
  `riesgo_tipoid` int(10) NOT NULL,
  `riesgo_propiedadid` int(10) NOT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  `estado` int(2) NOT NULL,
  PRIMARY KEY (`riesgo_tipoid`,`riesgo_propiedadid`),
  KEY `FKrtipo_prop489076` (`riesgo_tipoid`),
  KEY `FKrtipo_prop994938` (`riesgo_propiedadid`),
  CONSTRAINT `FKrtipo_prop489076` FOREIGN KEY (`riesgo_tipoid`) REFERENCES `riesgo_tipo` (`id`),
  CONSTRAINT `FKrtipo_prop994938` FOREIGN KEY (`riesgo_propiedadid`) REFERENCES `riesgo_propiedad` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rtipo_propiedad`
--

LOCK TABLES `rtipo_propiedad` WRITE;
/*!40000 ALTER TABLE `rtipo_propiedad` DISABLE KEYS */;
/*!40000 ALTER TABLE `rtipo_propiedad` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sctipo_propiedad`
--

DROP TABLE IF EXISTS `sctipo_propiedad`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sctipo_propiedad` (
  `subcomponente_tipoid` int(10) NOT NULL,
  `subcomponente_propiedadid` int(10) NOT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`subcomponente_tipoid`,`subcomponente_propiedadid`),
  KEY `FKsctipo_prop358116` (`subcomponente_tipoid`),
  KEY `FKsctipo_prop74576` (`subcomponente_propiedadid`),
  CONSTRAINT `FKsctipo_prop358116` FOREIGN KEY (`subcomponente_tipoid`) REFERENCES `subcomponente_tipo` (`id`),
  CONSTRAINT `FKsctipo_prop74576` FOREIGN KEY (`subcomponente_propiedadid`) REFERENCES `subcomponente_propiedad` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sctipo_propiedad`
--

LOCK TABLES `sctipo_propiedad` WRITE;
/*!40000 ALTER TABLE `sctipo_propiedad` DISABLE KEYS */;
/*!40000 ALTER TABLE `sctipo_propiedad` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `subcomponente`
--

DROP TABLE IF EXISTS `subcomponente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `subcomponente` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(1000) COLLATE utf8_bin NOT NULL,
  `descripcion` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `componenteid` int(10) NOT NULL,
  `subcomponente_tipoid` int(10) NOT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  `estado` int(2) NOT NULL,
  `unidad_ejecutoraunidad_ejecutora` int(10) DEFAULT NULL,
  `snip` bigint(10) DEFAULT NULL,
  `programa` int(4) DEFAULT NULL,
  `subprograma` int(4) DEFAULT NULL,
  `proyecto` int(4) DEFAULT NULL,
  `actividad` int(4) DEFAULT NULL,
  `obra` int(4) DEFAULT NULL,
  `latitud` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `longitud` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `costo` decimal(15,2) DEFAULT NULL,
  `acumulacion_costoid` int(11) DEFAULT NULL,
  `renglon` int(4) DEFAULT NULL,
  `ubicacion_geografica` int(4) DEFAULT NULL,
  `fecha_inicio` timestamp NULL DEFAULT NULL,
  `fecha_fin` timestamp NULL DEFAULT NULL,
  `duracion` int(10) NOT NULL DEFAULT 0,
  `duracion_dimension` varchar(1) COLLATE utf8_bin DEFAULT NULL,
  `orden` int(10) DEFAULT NULL,
  `treePath` varchar(1000) COLLATE utf8_bin DEFAULT NULL,
  `nivel` int(4) DEFAULT NULL,
  `entidad` int(10) DEFAULT NULL,
  `ejercicio` int(4) DEFAULT NULL,
  `fecha_inicio_real` timestamp NULL DEFAULT NULL,
  `fecha_fin_real` timestamp NULL DEFAULT NULL,
  `inversion_nueva` int(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `fkacumulacioncosto_idx1` (`acumulacion_costoid`),
  KEY `fk_subcomponente_15_idx` (`unidad_ejecutoraunidad_ejecutora`,`entidad`,`ejercicio`),
  KEY `FKsubcomponente380208` (`componenteid`),
  KEY `FKsubcomponente53483` (`subcomponente_tipoid`),
  CONSTRAINT `FKsubcomponente380208` FOREIGN KEY (`componenteid`) REFERENCES `componente` (`id`),
  CONSTRAINT `FKsubcomponente53483` FOREIGN KEY (`subcomponente_tipoid`) REFERENCES `subcomponente_tipo` (`id`),
  CONSTRAINT `fk_subcomponente_15` FOREIGN KEY (`unidad_ejecutoraunidad_ejecutora`, `entidad`, `ejercicio`) REFERENCES `unidad_ejecutora` (`unidad_ejecutora`, `entidadentidad`, `ejercicio`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fkacumulacioncosto2` FOREIGN KEY (`acumulacion_costoid`) REFERENCES `acumulacion_costo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subcomponente`
--

LOCK TABLES `subcomponente` WRITE;
/*!40000 ALTER TABLE `subcomponente` DISABLE KEYS */;
/*!40000 ALTER TABLE `subcomponente` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER tr_subcomponente_insert
AFTER INSERT ON sipro.subcomponente FOR EACH ROW
BEGIN
	DECLARE v_version int;
	SELECT max(a.version) INTO v_version FROM sipro_history.subcomponente a WHERE a.id=NEW.id;

    IF(v_version is null) THEN
		SET v_version=1;
	END IF;

    INSERT INTO sipro_history.subcomponente VALUE(NEW.id,NEW.nombre,NEW.descripcion,NEW.componenteid,NEW.subcomponente_tipoid,NEW.usuario_creo,NEW.usuario_actualizo,NEW.fecha_creacion,NEW.fecha_actualizacion,NEW.estado,NEW.unidad_ejecutoraunidad_ejecutora,NEW.snip,NEW.programa,NEW.subprograma,NEW.proyecto,NEW.actividad,NEW.obra,NEW.latitud,NEW.longitud,NEW.costo,NEW.acumulacion_costoid,NEW.renglon,NEW.ubicacion_geografica,NEW.fecha_inicio,NEW.fecha_fin,NEW.duracion,NEW.duracion_dimension,NEW.orden,NEW.treePath,NEW.nivel,NEW.entidad,NEW.ejercicio,NEW.fecha_inicio_real,NEW.fecha_fin_real, NEW.inversion_nueva, v_version, NULL, 1);

END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER tr_subcomponente_update
AFTER UPDATE ON sipro.subcomponente FOR EACH ROW
BEGIN
		DECLARE v_version int;
		SELECT max(a.version) INTO v_version FROM sipro_history.subcomponente a WHERE a.id=OLD.id;

		IF(v_version is null) THEN
            UPDATE sipro_history.subcomponente SET actual=null WHERE id=OLD.id AND version is null;
            SET v_version=1;
		ELSE
            UPDATE sipro_history.subcomponente SET actual=null WHERE id=OLD.id AND version=v_version;
            SET v_version=v_version+1;
        END IF;

        INSERT INTO sipro_history.subcomponente VALUE(NEW.id,NEW.nombre,NEW.descripcion,NEW.componenteid,NEW.subcomponente_tipoid,NEW.usuario_creo,NEW.usuario_actualizo,NEW.fecha_creacion,NEW.fecha_actualizacion,NEW.estado,NEW.unidad_ejecutoraunidad_ejecutora,NEW.snip,NEW.programa,NEW.subprograma,NEW.proyecto,NEW.actividad,NEW.obra,NEW.latitud,NEW.longitud,NEW.costo,NEW.acumulacion_costoid,NEW.renglon,NEW.ubicacion_geografica,NEW.fecha_inicio,NEW.fecha_fin,NEW.duracion,NEW.duracion_dimension,NEW.orden,NEW.treePath,NEW.nivel,NEW.entidad,NEW.ejercicio,NEW.fecha_inicio_real,NEW.fecha_fin_real, NEW.inversion_nueva, v_version, NULL, 1);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER tr_subcomponente_delete
BEFORE DELETE ON sipro.subcomponente FOR EACH ROW
BEGIN
	DECLARE v_version int;
	SELECT max(a.version) INTO v_version FROM sipro_history.subcomponente a WHERE a.id=OLD.id;

	IF(v_version is null) THEN
		SET v_version=1;
	ELSE
		UPDATE sipro_history.subcomponente SET actual=null WHERE id=OLD.id AND version=v_version;
		SET v_version=v_version+1;
	END IF;

	INSERT INTO sipro_history.subcomponente VALUE(OLD.id,OLD.nombre,OLD.descripcion,OLD.componenteid,OLD.subcomponente_tipoid,OLD.usuario_creo,OLD.usuario_actualizo,OLD.fecha_creacion,OLD.fecha_actualizacion,OLD.estado,OLD.unidad_ejecutoraunidad_ejecutora,OLD.snip,OLD.programa,OLD.subprograma,OLD.proyecto,OLD.actividad,OLD.obra,OLD.latitud,OLD.longitud,OLD.costo,OLD.acumulacion_costoid,OLD.renglon,OLD.ubicacion_geografica,OLD.fecha_inicio,OLD.fecha_fin,OLD.duracion,OLD.duracion_dimension,OLD.orden,OLD.treePath,OLD.nivel,OLD.entidad,OLD.ejercicio,OLD.fecha_inicio_real,OLD.fecha_fin_real, OLD.inversion_nueva, v_version, NULL, NULL);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `subcomponente_propiedad`
--

DROP TABLE IF EXISTS `subcomponente_propiedad`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `subcomponente_propiedad` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(1000) COLLATE utf8_bin NOT NULL,
  `descripcion` varchar(2000) COLLATE utf8_bin DEFAULT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  `dato_tipoid` int(10) NOT NULL,
  `estado` int(2) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `FKsubcomponente26853` (`dato_tipoid`),
  CONSTRAINT `FKsubcomponente26853` FOREIGN KEY (`dato_tipoid`) REFERENCES `dato_tipo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subcomponente_propiedad`
--

LOCK TABLES `subcomponente_propiedad` WRITE;
/*!40000 ALTER TABLE `subcomponente_propiedad` DISABLE KEYS */;
/*!40000 ALTER TABLE `subcomponente_propiedad` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `subcomponente_propiedad_valor`
--

DROP TABLE IF EXISTS `subcomponente_propiedad_valor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `subcomponente_propiedad_valor` (
  `subcomponenteid` int(10) NOT NULL,
  `subcomponente_propiedadid` int(10) NOT NULL,
  `valor_string` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `valor_entero` int(10) DEFAULT NULL,
  `valor_decimal` decimal(15,2) DEFAULT NULL,
  `valor_tiempo` timestamp NULL DEFAULT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`subcomponenteid`,`subcomponente_propiedadid`),
  KEY `FKsubcomponente278188` (`subcomponente_propiedadid`),
  KEY `FKsubcomponente747047` (`subcomponenteid`),
  CONSTRAINT `FKsubcomponente278188` FOREIGN KEY (`subcomponente_propiedadid`) REFERENCES `subcomponente_propiedad` (`id`),
  CONSTRAINT `FKsubcomponente747047` FOREIGN KEY (`subcomponenteid`) REFERENCES `subcomponente` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subcomponente_propiedad_valor`
--

LOCK TABLES `subcomponente_propiedad_valor` WRITE;
/*!40000 ALTER TABLE `subcomponente_propiedad_valor` DISABLE KEYS */;
/*!40000 ALTER TABLE `subcomponente_propiedad_valor` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER tr_subcomp_prop_val_insert
AFTER INSERT ON sipro.subcomponente_propiedad_valor FOR EACH ROW
BEGIN
    DECLARE v_version int;
    SELECT max(a.version) INTO v_version FROM sipro_history.subcomponente_propiedad_valor a WHERE a.subcomponenteid=NEW.subcomponenteid AND a.subcomponente_propiedadid=NEW.subcomponente_propiedadid;

    IF(v_version is null) THEN
        SET v_version=1;
    END IF;

    INSERT INTO sipro_history.subcomponente_propiedad_valor VALUE(NEW.subcomponenteid, NEW.subcomponente_propiedadid,NEW.valor_string,NEW.valor_entero, NEW.valor_decimal,NEW.valor_tiempo,NEW.usuario_creo,NEW.usuario_actualizo, NEW.fecha_creacion,NEW.fecha_actualizacion,v_version, NULL, 1);

END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER tr_subcomp_prop_val_update
AFTER UPDATE ON sipro.subcomponente_propiedad_valor FOR EACH ROW
BEGIN
        DECLARE v_version int;
        SELECT max(a.version) INTO v_version FROM sipro_history.subcomponente_propiedad_valor a WHERE a.subcomponenteid=NEW.subcomponenteid AND a.subcomponente_propiedadid=NEW.subcomponente_propiedadid;

        IF(v_version is null) THEN
            UPDATE sipro_history.subcomponente_propiedad_valor a SET actual=null WHERE a.subcomponenteid=NEW.subcomponenteid AND a.subcomponente_propiedadid=NEW.subcomponente_propiedadid AND version is null;
            SET v_version=1;
        ELSE
            UPDATE sipro_history.subcomponente_propiedad_valor a SET actual=null WHERE a.subcomponenteid=NEW.subcomponenteid AND a.subcomponente_propiedadid=NEW.subcomponente_propiedadid AND version=v_version;
            SET v_version=v_version+1;
        END IF;

        INSERT INTO sipro_history.subcomponente_propiedad_valor VALUE(NEW.subcomponenteid, NEW.subcomponente_propiedadid,NEW.valor_string,NEW.valor_entero,NEW.valor_decimal, NEW.valor_tiempo,NEW.usuario_creo,NEW.usuario_actualizo,NEW.fecha_creacion, NEW.fecha_actualizacion,v_version, NULL, 1);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER tr_subcomp_prop_val_delete
BEFORE DELETE ON sipro.subcomponente_propiedad_valor FOR EACH ROW
BEGIN
    DECLARE v_version int;
    SELECT max(a.version) INTO v_version FROM sipro_history.subcomponente_propiedad_valor a WHERE a.subcomponenteid=OLD.subcomponenteid AND a.subcomponente_propiedadid=OLD.subcomponente_propiedadid;

    IF(v_version is null) THEN
        SET v_version=1;
    ELSE
        UPDATE sipro_history.subcomponente_propiedad_valor a SET actual=null WHERE a.subcomponenteid=OLD.subcomponenteid AND a.subcomponente_propiedadid=OLD.subcomponente_propiedadid AND version=v_version;
        SET v_version=v_version+1;
    END IF;

    INSERT INTO sipro_history.subcomponente_propiedad_valor VALUE(OLD.subcomponenteid, OLD.subcomponente_propiedadid,OLD.valor_string,OLD.valor_entero,OLD.valor_decimal, OLD.valor_tiempo,OLD.usuario_creo,OLD.usuario_actualizo,OLD.fecha_creacion, OLD.fecha_actualizacion, v_version, NULL, NULL);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `subcomponente_tipo`
--

DROP TABLE IF EXISTS `subcomponente_tipo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `subcomponente_tipo` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(1000) COLLATE utf8_bin NOT NULL,
  `descripcion` varchar(2000) COLLATE utf8_bin DEFAULT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  `estado` int(2) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subcomponente_tipo`
--

LOCK TABLES `subcomponente_tipo` WRITE;
/*!40000 ALTER TABLE `subcomponente_tipo` DISABLE KEYS */;
INSERT INTO `subcomponente_tipo` VALUES (1,'General','Subcomponente general','admin',NULL,'2010-10-26 12:00:00',NULL,1);
/*!40000 ALTER TABLE `subcomponente_tipo` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER tr_subcomponente_tipo_insert
AFTER INSERT ON sipro.subcomponente_tipo FOR EACH ROW
BEGIN
	DECLARE v_version int;
	SELECT max(a.version) INTO v_version FROM sipro_history.subcomponente_tipo a WHERE a.id=NEW.id;

    IF(v_version is null) THEN
		SET v_version=1;
	END IF;

    INSERT INTO sipro_history.subcomponente_tipo VALUE(NEW.id,NEW.nombre,NEW.descripcion,NEW.usuario_creo,NEW.usuario_actualizo,NEW.fecha_creacion,NEW.fecha_actualizacion,NEW.estado, v_version, NULL, 1);

END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER tr_subcomponente_tipo_update
AFTER UPDATE ON sipro.subcomponente_tipo FOR EACH ROW
BEGIN
		DECLARE v_version int;
		SELECT max(a.version) INTO v_version FROM sipro_history.subcomponente_tipo a WHERE a.id=OLD.id;

		IF(v_version is null) THEN
            UPDATE sipro_history.subcomponente_tipo SET actual=null WHERE id=OLD.id AND version is null;
            SET v_version=1;
		ELSE
            UPDATE sipro_history.subcomponente_tipo SET actual=null WHERE id=OLD.id AND version=v_version;
            SET v_version=v_version+1;
        END IF;

        INSERT INTO sipro_history.subcomponente_tipo VALUE(NEW.id,NEW.nombre,NEW.descripcion,NEW.usuario_creo,NEW.usuario_actualizo,NEW.fecha_creacion,NEW.fecha_actualizacion,NEW.estado, v_version, NULL, 1);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER tr_subcomponente_tipo_delete
BEFORE DELETE ON sipro.subcomponente_tipo FOR EACH ROW
BEGIN
	DECLARE v_version int;
	SELECT max(a.version) INTO v_version FROM sipro_history.subcomponente_tipo a WHERE a.id=OLD.id;

	IF(v_version is null) THEN
		SET v_version=1;
	ELSE
		UPDATE sipro_history.subcomponente_tipo SET actual=null WHERE id=OLD.id AND version=v_version;
		SET v_version=v_version+1;
	END IF;

	INSERT INTO sipro_history.subcomponente_tipo VALUE(OLD.id,OLD.nombre,OLD.descripcion,OLD.usuario_creo,OLD.usuario_actualizo,OLD.fecha_creacion,OLD.fecha_actualizacion,OLD.estado, v_version, NULL, NULL);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `subcomponente_usuario`
--

DROP TABLE IF EXISTS `subcomponente_usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `subcomponente_usuario` (
  `subcomponenteid` int(10) NOT NULL,
  `usuario` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NULL DEFAULT NULL,
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`subcomponenteid`,`usuario`),
  KEY `fk_subcomponente_id2` (`subcomponenteid`),
  CONSTRAINT `fk_subcomponente_id2` FOREIGN KEY (`subcomponenteid`) REFERENCES `subcomponente` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subcomponente_usuario`
--

LOCK TABLES `subcomponente_usuario` WRITE;
/*!40000 ALTER TABLE `subcomponente_usuario` DISABLE KEYS */;
/*!40000 ALTER TABLE `subcomponente_usuario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `subprodtipo_propiedad`
--

DROP TABLE IF EXISTS `subprodtipo_propiedad`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `subprodtipo_propiedad` (
  `subproducto_tipoid` int(10) NOT NULL,
  `subproducto_propiedadid` int(10) NOT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`subproducto_tipoid`,`subproducto_propiedadid`),
  KEY `FKsubprodtip230372` (`subproducto_tipoid`),
  KEY `FKsubprodtip762195` (`subproducto_propiedadid`),
  CONSTRAINT `FKsubprodtip230372` FOREIGN KEY (`subproducto_tipoid`) REFERENCES `subproducto_tipo` (`id`),
  CONSTRAINT `FKsubprodtip762195` FOREIGN KEY (`subproducto_propiedadid`) REFERENCES `subproducto_propiedad` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subprodtipo_propiedad`
--

LOCK TABLES `subprodtipo_propiedad` WRITE;
/*!40000 ALTER TABLE `subprodtipo_propiedad` DISABLE KEYS */;
/*!40000 ALTER TABLE `subprodtipo_propiedad` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `subproducto`
--

DROP TABLE IF EXISTS `subproducto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `subproducto` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(1000) COLLATE utf8_bin NOT NULL,
  `descripcion` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  `estado` int(2) NOT NULL,
  `snip` bigint(10) DEFAULT NULL,
  `programa` int(4) DEFAULT NULL,
  `subprograma` int(4) DEFAULT NULL,
  `proyecto` int(4) DEFAULT NULL,
  `actividad` int(4) DEFAULT NULL,
  `obra` int(4) DEFAULT NULL,
  `productoid` int(10) NOT NULL,
  `subproducto_tipoid` int(10) NOT NULL,
  `unidad_ejecutoraunidad_ejecutora` int(10) DEFAULT NULL,
  `latitud` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `longitud` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `costo` decimal(15,2) DEFAULT NULL,
  `acumulacion_costoid` int(11) DEFAULT NULL,
  `renglon` int(4) DEFAULT NULL,
  `ubicacion_geografica` int(4) DEFAULT NULL,
  `fecha_inicio` timestamp NULL DEFAULT NULL,
  `fecha_fin` timestamp NULL DEFAULT NULL,
  `duracion` int(10) NOT NULL DEFAULT 0,
  `duracion_dimension` varchar(1) COLLATE utf8_bin DEFAULT NULL,
  `orden` int(10) DEFAULT NULL,
  `treePath` varchar(1000) COLLATE utf8_bin DEFAULT NULL,
  `nivel` int(4) DEFAULT NULL,
  `entidad` int(10) DEFAULT NULL,
  `ejercicio` int(4) DEFAULT NULL,
  `fecha_inicio_real` timestamp NULL DEFAULT NULL,
  `fecha_fin_real` timestamp NULL DEFAULT NULL,
  `inversion_nueva` int(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `fkacumulacioncostosubproducto_idx` (`acumulacion_costoid`),
  KEY `fk_unidadejecutorasubproducto12_idx` (`unidad_ejecutoraunidad_ejecutora`,`entidad`,`ejercicio`),
  KEY `fk_unidadejecutorasubproducto12` (`unidad_ejecutoraunidad_ejecutora`,`entidad`,`ejercicio`),
  KEY `fkacumulacioncostosubproducto` (`acumulacion_costoid`),
  KEY `FKsubproduct375225` (`productoid`),
  KEY `FKsubproduct661539` (`subproducto_tipoid`),
  CONSTRAINT `FKsubproduct375225` FOREIGN KEY (`productoid`) REFERENCES `producto` (`id`),
  CONSTRAINT `FKsubproduct661539` FOREIGN KEY (`subproducto_tipoid`) REFERENCES `subproducto_tipo` (`id`),
  CONSTRAINT `fk_unidadejecutorasubproducto12` FOREIGN KEY (`unidad_ejecutoraunidad_ejecutora`, `entidad`, `ejercicio`) REFERENCES `unidad_ejecutora` (`unidad_ejecutora`, `entidadentidad`, `ejercicio`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fkacumulacioncostosubproducto` FOREIGN KEY (`acumulacion_costoid`) REFERENCES `acumulacion_costo` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subproducto`
--

LOCK TABLES `subproducto` WRITE;
/*!40000 ALTER TABLE `subproducto` DISABLE KEYS */;
/*!40000 ALTER TABLE `subproducto` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER tr_subproducto_insert
AFTER INSERT ON sipro.subproducto FOR EACH ROW
BEGIN
	DECLARE v_version int;
	SELECT max(a.version) INTO v_version FROM sipro_history.subproducto a WHERE a.id=NEW.id;

    IF(v_version is null) THEN
		SET v_version=1;
	END IF;

    INSERT INTO sipro_history.subproducto VALUE(NEW.id,NEW.nombre,NEW.descripcion,NEW.usuario_creo,NEW.usuario_actualizo,NEW.fecha_creacion,NEW.fecha_actualizacion,NEW.estado,NEW.snip,NEW.programa,NEW.subprograma,NEW.proyecto,NEW.actividad,NEW.obra,NEW.productoid,NEW.subproducto_tipoid,NEW.unidad_ejecutoraunidad_ejecutora,NEW.latitud,NEW.longitud,NEW.costo,NEW.acumulacion_costoid,NEW.renglon,NEW.ubicacion_geografica,NEW.fecha_inicio,NEW.fecha_fin,NEW.duracion,NEW.duracion_dimension,NEW.orden,NEW.treePath,NEW.nivel,NEW.entidad,NEW.ejercicio,NEW.fecha_inicio_real,NEW.fecha_fin_real, NEW.inversion_nueva, v_version, NULL, 1);

END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER tr_subproducto_update
AFTER UPDATE ON sipro.subproducto FOR EACH ROW
BEGIN
		DECLARE v_version int;
		SELECT max(a.version) INTO v_version FROM sipro_history.subproducto a WHERE a.id=OLD.id;

		IF(v_version is null) THEN
            UPDATE sipro_history.subproducto SET actual=null WHERE id=OLD.id AND version is null;
            SET v_version=1;
		ELSE
            UPDATE sipro_history.subproducto SET actual=null WHERE id=OLD.id AND version=v_version;
            SET v_version=v_version+1;
        END IF;

        INSERT INTO sipro_history.subproducto VALUE(NEW.id,NEW.nombre,NEW.descripcion,NEW.usuario_creo,NEW.usuario_actualizo,NEW.fecha_creacion,NEW.fecha_actualizacion,NEW.estado,NEW.snip,NEW.programa,NEW.subprograma,NEW.proyecto,NEW.actividad,NEW.obra,NEW.productoid,NEW.subproducto_tipoid,NEW.unidad_ejecutoraunidad_ejecutora,NEW.latitud,NEW.longitud,NEW.costo,NEW.acumulacion_costoid,NEW.renglon,NEW.ubicacion_geografica,NEW.fecha_inicio,NEW.fecha_fin,NEW.duracion,NEW.duracion_dimension,NEW.orden,NEW.treePath,NEW.nivel,NEW.entidad,NEW.ejercicio,NEW.fecha_inicio_real,NEW.fecha_fin_real, NEW.inversion_nueva, v_version, NULL, 1);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER tr_subproducto_delete
BEFORE DELETE ON sipro.subproducto FOR EACH ROW
BEGIN
	DECLARE v_version int;
	SELECT max(a.version) INTO v_version FROM sipro_history.subproducto a WHERE a.id=OLD.id;

	IF(v_version is null) THEN
		SET v_version=1;
	ELSE
		UPDATE sipro_history.subproducto SET actual=null WHERE id=OLD.id AND version=v_version;
		SET v_version=v_version+1;
	END IF;

	INSERT INTO sipro_history.subproducto VALUE(OLD.id,OLD.nombre,OLD.descripcion,OLD.usuario_creo,OLD.usuario_actualizo,OLD.fecha_creacion,OLD.fecha_actualizacion,OLD.estado,OLD.snip,OLD.programa,OLD.subprograma,OLD.proyecto,OLD.actividad,OLD.obra,OLD.productoid,OLD.subproducto_tipoid,OLD.unidad_ejecutoraunidad_ejecutora,OLD.latitud,OLD.longitud,OLD.costo,OLD.acumulacion_costoid,OLD.renglon,OLD.ubicacion_geografica,OLD.fecha_inicio,OLD.fecha_fin,OLD.duracion,OLD.duracion_dimension,OLD.orden,OLD.treePath,OLD.nivel,OLD.entidad,OLD.ejercicio,OLD.fecha_inicio_real,OLD.fecha_fin_real, OLD.inversion_nueva, v_version, NULL, NULL);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `subproducto_propiedad`
--

DROP TABLE IF EXISTS `subproducto_propiedad`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `subproducto_propiedad` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(1000) COLLATE utf8_bin NOT NULL,
  `descripcion` varchar(2000) COLLATE utf8_bin DEFAULT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  `estado` int(2) NOT NULL,
  `dato_tipoid` int(10) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKsubproduct396653` (`dato_tipoid`),
  CONSTRAINT `FKsubproduct396653` FOREIGN KEY (`dato_tipoid`) REFERENCES `dato_tipo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subproducto_propiedad`
--

LOCK TABLES `subproducto_propiedad` WRITE;
/*!40000 ALTER TABLE `subproducto_propiedad` DISABLE KEYS */;
/*!40000 ALTER TABLE `subproducto_propiedad` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `subproducto_propiedad_valor`
--

DROP TABLE IF EXISTS `subproducto_propiedad_valor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `subproducto_propiedad_valor` (
  `subproductoid` int(10) NOT NULL,
  `subproducto_propiedadid` int(10) NOT NULL,
  `valor_entero` int(10) DEFAULT NULL,
  `valor_string` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `valor_decimal` decimal(15,2) DEFAULT NULL,
  `valor_tiempo` timestamp NULL DEFAULT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  `estado` int(2) NOT NULL,
  PRIMARY KEY (`subproductoid`,`subproducto_propiedadid`),
  KEY `FKsubproduct665739` (`subproducto_propiedadid`),
  KEY `FKsubproduct923614` (`subproductoid`),
  CONSTRAINT `FKsubproduct665739` FOREIGN KEY (`subproducto_propiedadid`) REFERENCES `subproducto_propiedad` (`id`),
  CONSTRAINT `FKsubproduct923614` FOREIGN KEY (`subproductoid`) REFERENCES `subproducto` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subproducto_propiedad_valor`
--

LOCK TABLES `subproducto_propiedad_valor` WRITE;
/*!40000 ALTER TABLE `subproducto_propiedad_valor` DISABLE KEYS */;
/*!40000 ALTER TABLE `subproducto_propiedad_valor` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER tr_subpr_propiedad_val_insert
AFTER INSERT ON sipro.subproducto_propiedad_valor FOR EACH ROW
BEGIN
    DECLARE v_version int;
    SELECT max(a.version) INTO v_version FROM sipro_history.subproducto_propiedad_valor a WHERE a.subproductoid=NEW.subproductoid AND a.subproducto_propiedadid=NEW.subproducto_propiedadid;

    IF(v_version is null) THEN
        SET v_version=1;
    END IF;

    INSERT INTO sipro_history.subproducto_propiedad_valor VALUE(NEW.subproductoid,NEW.subproducto_propiedadid,NEW.valor_entero,NEW.valor_string,

NEW.valor_decimal,NEW.valor_tiempo,NEW.usuario_creo,NEW.usuario_actualizo,
    NEW.fecha_creacion,NEW.fecha_actualizacion,NEW.estado,
    v_version,NULL, 1);

END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER tr_subpr_propiedad_val_update
AFTER UPDATE ON sipro.subproducto_propiedad_valor FOR EACH ROW
BEGIN
        DECLARE v_version int;
        SELECT max(a.version) INTO v_version FROM sipro_history.subproducto_propiedad_valor a WHERE a.subproductoid=OLD.subproductoid AND a.subproducto_propiedadid=OLD.subproducto_propiedadid;

        IF(v_version is null) THEN
            UPDATE sipro_history.subproducto_propiedad_valor a SET actual=null WHERE a.subproductoid=OLD.subproductoid AND a.subproducto_propiedadid=OLD.subproducto_propiedadid AND version is null;
            SET v_version=1;
        ELSE
            UPDATE sipro_history.subproducto_propiedad_valor a SET actual=null WHERE a.subproductoid=OLD.subproductoid AND a.subproducto_propiedadid=OLD.subproducto_propiedadid AND version=v_version;
            SET v_version=v_version+1;
        END IF;

        INSERT INTO sipro_history.subproducto_propiedad_valor VALUE(NEW.subproductoid,NEW.subproducto_propiedadid,NEW.valor_entero,NEW.valor_string, NEW.valor_decimal,NEW.valor_tiempo,NEW.usuario_creo,NEW.usuario_actualizo, NEW.fecha_creacion,NEW.fecha_actualizacion,NEW.estado, v_version, NULL, 1);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER tr_subpr_propiedad_val_delete
BEFORE DELETE ON sipro.subproducto_propiedad_valor FOR EACH ROW
BEGIN
    DECLARE v_version int;
    SELECT max(a.version) INTO v_version FROM sipro_history.subproducto_propiedad_valor a WHERE a.subproductoid=OLD.subproductoid AND a.subproducto_propiedadid=OLD.subproducto_propiedadid;

    IF(v_version is null) THEN
        SET v_version=1;
    ELSE
        UPDATE sipro_history.subproducto_propiedad_valor a SET actual=null WHERE a.subproductoid=OLD.subproductoid AND a.subproducto_propiedadid=OLD.subproducto_propiedadid AND version=v_version;
        SET v_version=v_version+1;
    END IF;

    INSERT INTO sipro_history.subproducto_propiedad_valor VALUE(OLD.subproductoid,OLD.subproducto_propiedadid,OLD.valor_entero,OLD.valor_string,OLD.valor_decimal,OLD.valor_tiempo,OLD.usuario_creo,OLD.usuario_actualizo,OLD.fecha_creacion,OLD.fecha_actualizacion,OLD.estado, v_version, NULL, NULL);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `subproducto_tipo`
--

DROP TABLE IF EXISTS `subproducto_tipo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `subproducto_tipo` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(1000) COLLATE utf8_bin NOT NULL,
  `descripcion` varchar(2000) COLLATE utf8_bin DEFAULT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  `estado` int(2) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subproducto_tipo`
--

LOCK TABLES `subproducto_tipo` WRITE;
/*!40000 ALTER TABLE `subproducto_tipo` DISABLE KEYS */;
INSERT INTO `subproducto_tipo` VALUES (1,'General','General','admin',NULL,'2017-09-30 11:52:02',NULL,1);
/*!40000 ALTER TABLE `subproducto_tipo` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER tr_subproducto_tipo_insert
AFTER INSERT ON sipro.subproducto_tipo FOR EACH ROW
BEGIN
	DECLARE v_version int;
	SELECT max(a.version) INTO v_version FROM sipro_history.subproducto_tipo a WHERE a.id=NEW.id;

    IF(v_version is null) THEN
		SET v_version=1;
	END IF;

    INSERT INTO sipro_history.subproducto_tipo VALUE(NEW.id,NEW.nombre,NEW.descripcion,NEW.usuario_creo,NEW.usuario_actualizo,NEW.fecha_creacion,NEW.fecha_actualizacion,NEW.estado, v_version, NULL, 1);

END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER tr_subproducto_tipo_update
AFTER UPDATE ON sipro.subproducto_tipo FOR EACH ROW
BEGIN
		DECLARE v_version int;
		SELECT max(a.version) INTO v_version FROM sipro_history.subproducto_tipo a WHERE a.id=OLD.id;

		IF(v_version is null) THEN
            UPDATE sipro_history.subproducto_tipo SET actual=null WHERE id=OLD.id AND version is null;
            SET v_version=1;
		ELSE
            UPDATE sipro_history.subproducto_tipo SET actual=null WHERE id=OLD.id AND version=v_version;
            SET v_version=v_version+1;
        END IF;

        INSERT INTO sipro_history.subproducto_tipo VALUE(NEW.id,NEW.nombre,NEW.descripcion,NEW.usuario_creo,NEW.usuario_actualizo,NEW.fecha_creacion,NEW.fecha_actualizacion,NEW.estado, v_version, NULL, 1);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER tr_subproducto_tipo_delete
BEFORE DELETE ON sipro.subproducto_tipo FOR EACH ROW
BEGIN
	DECLARE v_version int;
	SELECT max(a.version) INTO v_version FROM sipro_history.subproducto_tipo a WHERE a.id=OLD.id;

	IF(v_version is null) THEN
		SET v_version=1;
	ELSE
		UPDATE sipro_history.subproducto_tipo SET actual=null WHERE id=OLD.id AND version=v_version;
		SET v_version=v_version+1;
	END IF;

	INSERT INTO sipro_history.subproducto_tipo VALUE(OLD.id,OLD.nombre,OLD.descripcion,OLD.usuario_creo,OLD.usuario_actualizo,OLD.fecha_creacion,OLD.fecha_actualizacion,OLD.estado, v_version, NULL, NULL);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `subproducto_usuario`
--

DROP TABLE IF EXISTS `subproducto_usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `subproducto_usuario` (
  `subproductoid` int(10) NOT NULL,
  `usuario` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`subproductoid`,`usuario`),
  KEY `usuario` (`usuario`),
  KEY `FKsubproduct290099` (`subproductoid`),
  KEY `subproducto_usuario_ibfk_1` (`usuario`),
  CONSTRAINT `FKsubproduct290099` FOREIGN KEY (`subproductoid`) REFERENCES `subproducto` (`id`),
  CONSTRAINT `subproducto_usuario_ibfk_1` FOREIGN KEY (`usuario`) REFERENCES `usuario` (`usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subproducto_usuario`
--

LOCK TABLES `subproducto_usuario` WRITE;
/*!40000 ALTER TABLE `subproducto_usuario` DISABLE KEYS */;
/*!40000 ALTER TABLE `subproducto_usuario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tipo_adquisicion`
--

DROP TABLE IF EXISTS `tipo_adquisicion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tipo_adquisicion` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cooperantecodigo` int(10) NOT NULL,
  `nombre` varchar(1000) COLLATE utf8_bin NOT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  `estado` int(11) NOT NULL,
  `convenio_cdirecta` int(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tipo_adquisicion`
--

LOCK TABLES `tipo_adquisicion` WRITE;
/*!40000 ALTER TABLE `tipo_adquisicion` DISABLE KEYS */;
INSERT INTO `tipo_adquisicion` VALUES (1,402,'Licitación Pública Internacional','admin',NULL,'2017-10-04 06:43:57',NULL,1,NULL),(2,402,'Licitación Pública Internacional Limitada','admin',NULL,'2017-10-04 06:43:57',NULL,1,NULL),(3,402,'Licitación Pública Nacional','admin',NULL,'2017-10-04 06:43:57',NULL,1,NULL),(4,402,'Comparación de Precios','admin',NULL,'2017-10-04 06:43:57',NULL,1,NULL),(5,402,'Contratación Directa','admin',NULL,'2017-10-04 06:43:57',NULL,1,NULL),(6,402,'Administración Directa','admin',NULL,'2017-10-04 06:43:57',NULL,1,NULL),(7,402,'Compras Directas a Agenciass Especializadas','admin',NULL,'2017-10-04 06:43:57',NULL,1,NULL),(8,402,'Agencias de Contrataciones','admin',NULL,'2017-10-04 06:43:57',NULL,1,NULL),(9,402,'Agencias de Inspección','admin',NULL,'2017-10-04 06:43:57',NULL,1,NULL),(10,402,'Contrataciones en Prestamos a Intermediarios Financieros','admin',NULL,'2017-10-04 06:43:57',NULL,1,NULL),(11,401,'Licitaición o Concurso Público Internacional','admin',NULL,'2017-10-04 06:43:57',NULL,1,NULL),(12,401,'Licitación o Concurso Público Nacional','admin',NULL,'2017-10-04 06:43:57',NULL,1,NULL),(13,401,'Aplicación de Legislación Nacional','admin',NULL,'2017-10-04 06:43:57',NULL,1,NULL),(14,401,'Comparación de Precios o Calificaciones','admin',NULL,'2017-10-04 06:43:57',NULL,1,NULL),(15,403,'Licitación Internacional Limintada','admin',NULL,'2017-10-04 06:43:57',NULL,1,NULL),(16,403,'Licitación Pública Nacional','admin',NULL,'2017-10-04 06:43:57',NULL,1,NULL),(17,403,'Comparación de Precios','admin',NULL,'2017-10-04 06:43:57',NULL,1,NULL),(18,403,'Contratos Marco','admin',NULL,'2017-10-04 06:43:57',NULL,1,NULL),(19,403,'Contratación Directa','admin',NULL,'2017-10-04 06:43:57',NULL,1,NULL),(20,403,'Construcción por Administración','admin',NULL,'2017-10-04 06:44:19',NULL,1,NULL),(21,403,'Contrataciones a Través de Agencias de las Naciones Unidas','admin',NULL,'2017-10-04 06:44:19',NULL,1,NULL),(22,403,'Agentes de Contrataciones y Administración de Contratos de Construcción','admin',NULL,'2017-10-04 06:44:19',NULL,1,NULL),(23,403,'Servicios de Inspección','admin',NULL,'2017-10-04 06:44:19',NULL,1,NULL),(24,403,'Contrataciones en Préstamos a Intermediarios y Entidades Financieras','admin',NULL,'2017-10-04 06:44:19',NULL,1,NULL),(25,403,'Contrataciones por Acuerdos de Asociación Público-Privada','admin',NULL,'2017-10-04 06:44:19',NULL,1,NULL),(26,403,'Contratación Basada en Desempeño','admin',NULL,'2017-10-04 06:44:19',NULL,1,NULL),(27,403,'Contrataciones con Préstamos u Obligaciones de Pago Garantizados por el Banco','admin',NULL,'2017-10-04 06:44:19',NULL,1,NULL),(28,403,'Participación de la Comunidad en las Contrataciones','admin',NULL,'2017-10-04 06:44:19',NULL,1,NULL),(29,403,'Uso de Sistemas de País','admin',NULL,'2017-10-04 06:44:19',NULL,1,NULL),(30,505,'Licitación Pública Internacional','admin',NULL,'2017-10-04 06:44:19',NULL,1,NULL),(31,505,'Licitaciones locales','admin',NULL,'2017-10-04 06:44:19',NULL,1,NULL),(32,505,'Licitaciones limitadas','admin',NULL,'2017-10-04 06:44:19',NULL,1,NULL),(33,505,'Solicitud de Precios','admin',NULL,'2017-10-04 06:44:19',NULL,1,NULL),(34,505,'Procedimiento de varias etapas','admin',NULL,'2017-10-04 06:44:19',NULL,1,NULL),(35,505,'Adjudicación directa','admin',NULL,'2017-10-04 06:44:19',NULL,1,NULL),(36,505,'Obras por administración directa, medidas propias del grupo meta','admin',NULL,'2017-10-04 06:44:19',NULL,1,NULL),(37,509,'Licitación Pública Internacional','admin',NULL,'2017-10-04 06:44:19',NULL,1,NULL),(38,509,'Licitación Internacional Limitada','admin',NULL,'2017-10-04 06:44:19',NULL,1,NULL),(39,509,'Comparación Internacional de Precios','admin',NULL,'2017-10-04 06:44:19',NULL,1,NULL),(40,509,'Contratación Directa','admin',NULL,'2017-10-04 06:44:19',NULL,1,NULL);
/*!40000 ALTER TABLE `tipo_adquisicion` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER tr_tipo_adquisicion_insert
AFTER INSERT ON sipro.tipo_adquisicion FOR EACH ROW
BEGIN
	DECLARE v_version int;
	SELECT max(a.version) INTO v_version FROM sipro_history.tipo_adquisicion a WHERE a.id=NEW.id;

    IF(v_version is null) THEN
		SET v_version=1;
	END IF;

    INSERT INTO sipro_history.tipo_adquisicion VALUE(NEW.id,NEW.cooperantecodigo,NEW.nombre,NEW.usuario_creo,NEW.usuario_actualizo,NEW.fecha_creacion,NEW.fecha_actualizacion,NEW.estado, NEW.convenio_cdirecta, v_version, NULL, 1);

END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER tr_tipo_adquisicion_update
AFTER UPDATE ON sipro.tipo_adquisicion FOR EACH ROW
BEGIN
		DECLARE v_version int;
		SELECT max(a.version) INTO v_version FROM sipro_history.tipo_adquisicion a WHERE a.id=OLD.id;

		IF(v_version is null) THEN
            UPDATE sipro_history.tipo_adquisicion SET actual=null WHERE id=OLD.id AND version is null;
            SET v_version=1;
		ELSE
            UPDATE sipro_history.tipo_adquisicion SET actual=null WHERE id=OLD.id AND version=v_version;
            SET v_version=v_version+1;
        END IF;

        INSERT INTO sipro_history.tipo_adquisicion VALUE(NEW.id,NEW.cooperantecodigo,NEW.nombre,NEW.usuario_creo,NEW.usuario_actualizo,NEW.fecha_creacion,NEW.fecha_actualizacion,NEW.estado,  NEW.convenio_cdirecta, v_version, NULL, 1);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER tr_tipo_adquisicion_delete
BEFORE DELETE ON sipro.tipo_adquisicion FOR EACH ROW
BEGIN
	DECLARE v_version int;
	SELECT max(a.version) INTO v_version FROM sipro_history.tipo_adquisicion a WHERE a.id=OLD.id;

	IF(v_version is null) THEN
		SET v_version=1;
	ELSE
		UPDATE sipro_history.tipo_adquisicion SET actual=null WHERE id=OLD.id AND version=v_version;
		SET v_version=v_version+1;
	END IF;

	INSERT INTO sipro_history.tipo_adquisicion VALUE(OLD.id,OLD.cooperantecodigo,OLD.nombre,OLD.usuario_creo,OLD.usuario_actualizo,OLD.fecha_creacion,OLD.fecha_actualizacion,OLD.estado, OLD.convenio_cdirecta, v_version, NULL, NULL);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `tipo_moneda`
--

DROP TABLE IF EXISTS `tipo_moneda`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tipo_moneda` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(1000) COLLATE utf8_bin DEFAULT NULL,
  `simbolo` varchar(5) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tipo_moneda`
--

LOCK TABLES `tipo_moneda` WRITE;
/*!40000 ALTER TABLE `tipo_moneda` DISABLE KEYS */;
INSERT INTO `tipo_moneda` VALUES (1,'Quetzales','GTQ'),(2,'Dolares Estadounidenses','USD'),(3,'Euros','EUR'),(4,'Yen','JPY');
/*!40000 ALTER TABLE `tipo_moneda` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `unidad_ejecutora`
--

DROP TABLE IF EXISTS `unidad_ejecutora`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `unidad_ejecutora` (
  `unidad_ejecutora` int(10) NOT NULL,
  `nombre` varchar(1000) COLLATE utf8_bin NOT NULL,
  `entidadentidad` int(10) NOT NULL,
  `ejercicio` int(4) NOT NULL,
  PRIMARY KEY (`unidad_ejecutora`,`entidadentidad`,`ejercicio`),
  KEY `FKunidadentidad_idx` (`entidadentidad`,`ejercicio`),
  KEY `FKunidadentidad` (`entidadentidad`,`ejercicio`),
  CONSTRAINT `FKunidadentidad` FOREIGN KEY (`entidadentidad`, `ejercicio`) REFERENCES `entidad` (`entidad`, `ejercicio`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `unidad_ejecutora`
--

LOCK TABLES `unidad_ejecutora` WRITE;
/*!40000 ALTER TABLE `unidad_ejecutora` DISABLE KEYS */;
INSERT INTO `unidad_ejecutora` VALUES (0,'SIN UNIDAD EJECUTORA',0,2017),(0,'CONGRESO DE LA REPUBLICA',11110001,2017),(0,'ORGANISMO JUDICIAL',11120002,2017),(0,'PRESIDENCIA DE LA REPÚBLICA',11130003,2017),(0,'MINISTERIO DE RELACIONES   EXTERIORES',11130004,2017),(0,'MINISTERIO DE GOBERNACIÓN',11130005,2017),(0,'MINISTERIO DE LA DEFENSA NACIONAL',11130006,2017),(0,'MINISTERIO DE FINANZAS PÚBLICAS',11130007,2017),(0,'MINISTERIO DE EDUCACIÓN',11130008,2017),(0,'MINISTERIO DE SALUD PÚBLICA Y ASISTENCIA SOCIAL',11130009,2017),(0,'MINISTERIO DE TRABAJO Y PREVISIÓN SOCIAL',11130010,2017),(0,'MINISTERIO DE ECONOMÍA',11130011,2017),(0,'MINISTERIO DE AGRICULTURA, GANADERÍA Y ALIMENTACIÓN',11130012,2017),(0,'MINISTERIO DE  COMUNICACIONES, INFRAESTRUCTURA Y VIVIENDA',11130013,2017),(0,'MINISTERIO DE ENERGÍA Y MINAS',11130014,2017),(0,'MINISTERIO DE CULTURA Y DEPORTES',11130015,2017),(0,'SECRETARÍAS Y OTRAS DEPENDENCIAS DEL EJECUTIVO',11130016,2017),(0,'MINISTERIO DE AMBIENTE Y RECURSOS NATURALES',11130017,2017),(0,'OBLIGACIONES DEL ESTADO A CARGO DEL TESORO',11130018,2017),(0,'SERVICIOS DE LA DEUDA PUBLICA',11130019,2017),(0,'MINISTERIO DE DESARROLLO SOCIAL',11130020,2017),(0,'CONTRALORÍA GENERAL DE CUENTAS',11140020,2017),(0,'PROCURADURIA GENERAL DE LA NACIÓN',11140021,2017),(0,'MINISTERIO PÚBLICO (MP)',11140022,2017),(0,'CORTE DE CONSTITUCIONALIDAD.',11140024,2017),(0,'REGISTRO GENERAL DE LA PROPIEDAD (RGP)',11140026,2017),(0,'INSTITUTO DE LA DEFENSA PÚBLICA PENAL (IDPP)',11140027,2017),(0,'SEGUNDO REGISTRO DE LA PROPIEDAD',11140028,2017),(0,'COORDINADORA NACIONAL PARA LA REDUCCIÓN DE DESASTRES DE ORIGEN NATURAL O PROVOCADO (CONRED)',11140029,2017),(0,'REGISTRO DE INFORMACION CATASTRAL DE GUATEMALA (RIC)',11140065,2017),(0,'REGISTRO NACIONAL DE LAS PERSONAS (RENAP)',11140066,2017),(0,'CONSEJO NACIONAL DE ADOPCIONES (CNA)',11140067,2017),(0,'CONSEJO NACIONAL DE ATENCION AL MIGRANTE DE GUATEMALA (CONAMIGUA)',11140068,2017),(0,'SECRETARIA EJECUTIVA DE LA INSTANCIA COORDINADORA DE LA MODERNIZACION DEL SECTOR JUSTICIA (SEICMSJ)',11140069,2017),(0,'SECRETARIA NACIONAL DE ADMINISTRACION DE BIENES EN EXTINCION DE DOMINIO (SENABED)',11140070,2017),(0,'CONSEJO NACIONAL DEL DEPORTE, LA EDUCACION FISICA Y LA RECREACION (CONADER)',11140071,2017),(0,'CONSEJO ECONÓMICO Y SOCIAL DE GUATEMALA (CES)',11140072,2017),(0,'COMISIÓN NACIONAL DE ENERGÍA ELÉCTRICA',11140073,2017),(0,'OFICINA NACIONAL DE PREVENCION DE LA TORTURA Y OTROS TRATOS O PENAS CRUELES, INHUMANOS O DEGRADANTES',11140074,2017),(0,'TRIBUNAL SUPREMO ELECTORAL (TSE)',11150023,2017),(0,'PROCURADURIA DE LOS DERECHOS HUMANOS (PDH)',11150025,2017),(0,'INSTITUTO NACIONAL DE ESTADÍSTICA  (INE)',11200030,2017),(0,'INSTITUTO NACIONAL DE ADMINISTRACIÓN  PÚBLICA (INAP)',11200032,2017),(0,'INSTITUTO TÉCNICO DE CAPACITACIÓN Y PRODUCTIVIDAD (INTECAP)',11200034,2017),(0,'INSTITUTO DE RECREACIÓN DE LOS TRABAJADORES DE LA EMPRESA PRIVADA DE GUATEMALA (IRTRA)',11200035,2017),(0,'CONSEJO NACIONAL PARA LA PROTECCIÓN DE LA ANTIGUA GUATEMALA (CNPAG)',11200036,2017),(0,'BENEMÉRITO CUERPO VOLUNTARIO DE BOMBEROS DE GUATEMALA (CVB)',11200037,2017),(0,'APORTE PARA LA DESCENTRALIZACIÓN CULTURAL (ADESCA)',11200039,2017),(0,'INSTITUTO DE CIENCIA Y TECNOLOGÍA AGRÍCOLAS (ICTA)',11200041,2017),(0,'INSTITUTO NACIONAL DE CIENCIAS FORENSES DE GUATEMALA (INACIF)',11200046,2017),(0,'COMITÉ PERMANENTE DE EXPOSICIONES (COPEREX)',11200050,2017),(0,'INSTITUTO NACIONAL DE COOPERATIVAS (INACOP)',11200051,2017),(0,'INSPECCIÓN GENERAL DE COOPERATIVAS (INGECOP)',11200052,2017),(0,'INSTITUTO GUATEMALTECO DE TURISMO (INGUAT)',11200053,2017),(0,'INSTITUTO DE FOMENTO MUNICIPAL  (INFOM)',11200054,2017),(0,'INSTITUTO NACIONAL DE BOSQUES (INAB)',11200055,2017),(0,'SUPERINTENDENCIA DE ADMINISTRACIÓN TRIBUTARIA (SAT)',11200056,2017),(0,'FONDO DE TIERRAS (FONTIERRAS)',11200057,2017),(0,'COMITÉ NACIONAL DE ALFABETIZACIÓN (CONALFA)',11200059,2017),(0,'ACADEMIA DE LAS LENGUAS MAYAS DE GUATEMALA (ALMG)',11200064,2017),(0,'CONSEJO NACIONAL PARA LA ATENCIÓN DE LAS PERSONAS CON DISCAPACIDAD (CONADI)',11200067,2017),(0,'AGENCIA NACIONAL DE ALIANZAS PARA EL DESARROLLO DE INFRAESTRUCTURA ECONÓMICA (ANADIE)',11200068,2017),(0,'UNIVERSIDAD DE SAN CARLOS DE GUATEMALA (USAC)',11300060,2017),(0,'CONFEDERACIÓN DEPORTIVA AUTÓNOMA DE GUATEMALA (CDAG)',11300061,2017),(0,'COMITÉ OLÍMPICO GUATEMALTECO (COG)',11300062,2017),(0,'ESCUELA NACIONAL CENTRAL DE AGRICULTURA (ENCA)',11300063,2017),(0,'FEDERACIÓN NACIONAL DE TRIATLÓN',11300064,2017),(0,'FEDERACIÓN NACIONAL DE BOLICHE',11300065,2017),(0,'FEDERACIÓN NACIONAL DE VOLEIBOL',11300066,2017),(0,'FEDERACIÓN NACIONAL DE NATACIÓN, CLAVADOS, POLO ACUÁTICO Y NADO SINCRONIZADO',11300067,2017),(0,'FEDERACIÓN NACIONAL DE FÚTBOL',11300068,2017),(0,'FEDERACIÓN NACIONAL DE LUCHAS DE GUATEMALA',11300069,2017),(0,'FEDERACIÓN NACIONAL DE CICLISMO DE GUATEMALA',11300070,2017),(0,'FEDERACIÓN NACIONAL DE TENIS DE CAMPO',11300071,2017),(0,'FEDERACIÓN NACIONAL DE BOXEO',11300072,2017),(0,'FEDERACIÓN NACIONAL DE TIRO',11300073,2017),(0,'FEDERACIÓN NACIONAL DE BÁDMINTON DE GUATEMALA',11300074,2017),(0,'FEDERACIÓN NACIONAL DE ESGRIMA',11300075,2017),(0,'FEDERACIÓN NACIONAL DE BALONMANO',11300076,2017),(0,'FEDERACIÓN NACIONAL DE LEVANTAMIENTO DE PESAS',11300077,2017),(0,'FEDERACIÓN NACIONAL DE AJEDREZ DE GUATEMALA',11300078,2017),(0,'FEDERACIÓN NACIONAL DE BEISBOL',11300079,2017),(0,'FEDERACIÓN NACIONAL DE REMO Y CANOTAJE',11300080,2017),(0,'FEDERACIÓN NACIONAL DE MOTOCICLISMO',11300081,2017),(0,'TRIBUNAL ELECCIONARIO DEL DEPORTE FEDERADO',11300082,2017),(0,'FEDERACIÓN NACIONAL DE ANDINISMO',11300083,2017),(0,'FEDERACIÓN NACIONAL DE BALONCESTO',11300084,2017),(0,'FEDERACIÓN NACIONAL DE ATLETISMO',11300085,2017),(0,'FEDERACIÓN NACIONAL DE GIMNASIA',11300086,2017),(0,'FEDERACIÓN NACIONAL DE FÍSICO CULTURISMO',11300087,2017),(0,'FEDERACIÓN NACIONAL DE PATINAJE SOBRE RUEDAS DE GUATEMALA',11300088,2017),(0,'FEDERACIÓN NACIONAL DE KARATE-DO',11300089,2017),(0,'FEDERACIÓN NACIONAL DE LEVANTAMIENTO DE POTENCIA',11300090,2017),(0,'FEDERACION NACIONAL DE TENIS DE MESA',11300091,2017),(0,'FEDERACION NACIONAL DE TAEKWON-DO',11300092,2017),(0,'FEDERACION NACIONAL DE JUDO',11300093,2017),(0,'INSTITUTO GUATEMALTECO DE SEGURIDAD SOCIAL (IGSS)',11400068,2017),(0,'INSTITUTO DE PREVISIÓN MILITAR (IPM)',11400069,2017),(0,'MUNICIPALIDAD DE GUATEMALA',12100101,2017),(0,'MUNICIPALIDAD DE SANTA CATARINA PINULA',12100102,2017),(0,'MUNICIPALIDAD DE SAN JOSE PINULA',12100103,2017),(0,'MUNICIPALIDAD DE SAN JOSE DEL GOLFO',12100104,2017),(0,'MUNICIPALIDAD DE PALENCIA',12100105,2017),(0,'MUNICIPALIDAD DE CHINAUTLA',12100106,2017),(0,'MUNICIPALIDAD DE SAN PEDRO AYAMPUC',12100107,2017),(0,'MUNICIPALIDAD DE MIXCO',12100108,2017),(0,'MUNICIPALIDAD DE SAN PEDRO SACATEPEQUEZ',12100109,2017),(0,'MUNICIPALIDAD DE SAN JUAN SACATEPEQUEZ',12100110,2017),(0,'MUNICIPALIDAD DE SAN RAYMUNDO',12100111,2017),(0,'MUNICIPALIDAD DE CHUARRANCHO',12100112,2017),(0,'MUNICIPALIDAD DE FRAIJANES',12100113,2017),(0,'MUNICIPALIDAD DE AMATITLAN',12100114,2017),(0,'MUNICIPALIDAD DE VILLA NUEVA',12100115,2017),(0,'MUNICIPALIDAD DE VILLA CANALES',12100116,2017),(0,'MUNICIPALIDAD DE PETAPA',12100117,2017),(0,'MUNICIPALIDAD DE GUASTATOYA',12100201,2017),(0,'MUNICIPALIDAD DE MORAZAN',12100202,2017),(0,'MUNICIPALIDAD DE SAN AGUSTIN ACASAGUASTLAN',12100203,2017),(0,'MUNICIPALIDAD DE SAN CRISTOBAL ACASAGUASTLAN',12100204,2017),(0,'MUNICIPALIDAD DE EL JICARO',12100205,2017),(0,'MUNICIPALIDAD DE SANSARE',12100206,2017),(0,'MUNICIPALIDAD DE SANARATE',12100207,2017),(0,'MUNICIPALIDAD DE SAN ANTONIO LA PAZ',12100208,2017),(0,'MUNICIPALIDAD DE ANTIGUA GUATEMALA',12100301,2017),(0,'MUNICIPALIDAD DE JOCOTENANGO',12100302,2017),(0,'MUNICIPALIDAD DE PASTORES',12100303,2017),(0,'MUNICIPALIDAD DE SUMPANGO',12100304,2017),(0,'MUNICIPALIDAD DE SANTO DOMINGO XENACOJ',12100305,2017),(0,'MUNICIPALIDAD DE SANTIAGO SACATEPEQUEZ',12100306,2017),(0,'MUNICIPALIDAD DE SAN BARTOLOME MILPAS ALTAS',12100307,2017),(0,'MUNICIPALIDAD DE SAN LUCAS SACATEPEQUEZ',12100308,2017),(0,'MUNICIPALIDAD DE SANTA LUCIA MILPAS ALTAS',12100309,2017),(0,'MUNICIPALIDAD DE MAGDALENA MILPAS ALTAS',12100310,2017),(0,'MUNICIPALIDAD DE SANTA MARIA DE JESUS',12100311,2017),(0,'MUNICIPALIDAD DE CIUDAD VIEJA',12100312,2017),(0,'MUNICIPALIDAD DE SAN MIGUEL DUEÑAS',12100313,2017),(0,'MUNICIPALIDAD DE ALOTENANGO',12100314,2017),(0,'MUNICIPALIDAD DE SAN ANTONIO AGUAS CALIENTES',12100315,2017),(0,'MUNICIPALIDAD DE SANTA CATARINA BARAHONA',12100316,2017),(0,'MUNICIPALIDAD DE CHIMALTENANGO',12100401,2017),(0,'MUNICIPALIDAD DE SAN JOSE POAQUIL',12100402,2017),(0,'MUNICIPALIDAD DE SAN MARTIN JILOTEPEQUE',12100403,2017),(0,'MUNICIPALIDAD DE COMALAPA',12100404,2017),(0,'MUNICIPALIDAD DE SANTA APOLONIA',12100405,2017),(0,'MUNICIPALIDAD DE TECPAN GUATEMALA',12100406,2017),(0,'MUNICIPALIDAD DE PATZUN',12100407,2017),(0,'MUNICIPALIDAD DE POCHUTA',12100408,2017),(0,'MUNICIPALIDAD DE PATZICIA',12100409,2017),(0,'MUNICIPALIDAD DE SANTA CRUZ BALANYA',12100410,2017),(0,'MUNICIPALIDAD DE ACATENANGO',12100411,2017),(0,'MUNICIPALIDAD DE YEPOCAPA',12100412,2017),(0,'MUNICIPALIDAD DE SAN ANDRES ITZAPA',12100413,2017),(0,'MUNICIPALIDAD DE PARRAMOS',12100414,2017),(0,'MUNICIPALIDAD DE ZARAGOZA',12100415,2017),(0,'MUNICIPALIDAD DE EL TEJAR',12100416,2017),(0,'MUNICIPALIDAD DE ESCUINTLA',12100501,2017),(0,'MUNICIPALIDAD DE SANTA LUCIA COTZUMALGUAPA',12100502,2017),(0,'MUNICIPALIDAD DE LA DEMOCRACIA',12100503,2017),(0,'MUNICIPALIDAD DE SIQUINALA',12100504,2017),(0,'MUNICIPALIDAD DE MASAGUA',12100505,2017),(0,'MUNICIPALIDAD DE TIQUISATE',12100506,2017),(0,'MUNICIPALIDAD DE LA GOMERA',12100507,2017),(0,'MUNICIPALIDAD DE GUANAGAZAPA',12100508,2017),(0,'MUNICIPALIDAD DE SAN JOSE',12100509,2017),(0,'MUNICIPALIDAD DE IZTAPA',12100510,2017),(0,'MUNICIPALIDAD DE PALIN',12100511,2017),(0,'MUNICIPALIDAD DE SAN VICENTE PACAYA',12100512,2017),(0,'MUNICIPALIDAD DE NUEVA CONCEPCION',12100513,2017),(0,'MUNICIPALIDAD DE SIPACATE',12100514,2017),(0,'MUNICIPALIDAD DE CUILAPA',12100601,2017),(0,'MUNICIPALIDAD DE BARBERENA',12100602,2017),(0,'MUNICIPALIDAD DE SANTA ROSA DE LIMA',12100603,2017),(0,'MUNICIPALIDAD DE CASILLAS',12100604,2017),(0,'MUNICIPALIDAD DE SAN RAFAEL LAS FLORES',12100605,2017),(0,'MUNICIPALIDAD DE ORATORIO',12100606,2017),(0,'MUNICIPALIDAD DE SAN JUAN TECUACO',12100607,2017),(0,'MUNICIPALIDAD DE CHIQUIMULILLA',12100608,2017),(0,'MUNICIPALIDAD DE TAXISCO',12100609,2017),(0,'MUNICIPALIDAD DE SANTA MARIA IXHUATAN',12100610,2017),(0,'MUNICIPALIDAD DE GUAZACAPAN',12100611,2017),(0,'MUNICIPALIDAD DE SANTA CRUZ NARANJO',12100612,2017),(0,'MUNICIPALIDAD DE PUEBLO NUEVO VIÑAS',12100613,2017),(0,'MUNICIPALIDAD DE NUEVA SANTA ROSA',12100614,2017),(0,'MUNICIPALIDAD DE SOLOLA',12100701,2017),(0,'MUNICIPALIDAD DE SAN JOSE CHACAYA',12100702,2017),(0,'MUNICIPALIDAD DE SANTA MARIA VISITACION',12100703,2017),(0,'MUNICIPALIDAD DE SANTA LUCIA UTATLAN',12100704,2017),(0,'MUNICIPALIDAD DE NAHUALA',12100705,2017),(0,'MUNICIPALIDAD DE SANTA CATARINA IXTAHUACAN',12100706,2017),(0,'MUNICIPALIDAD DE SANTA CLARA LA LAGUNA',12100707,2017),(0,'MUNICIPALIDAD DE CONCEPCION',12100708,2017),(0,'MUNICIPALIDAD DE SAN ANDRES SEMETABAJ',12100709,2017),(0,'MUNICIPALIDAD DE PANAJACHEL',12100710,2017),(0,'MUNICIPALIDAD DE SANTA CATARINA PALOPO',12100711,2017),(0,'MUNICIPALIDAD DE SAN ANTONIO PALOPO',12100712,2017),(0,'MUNICIPALIDAD DE SAN LUCAS TOLIMAN',12100713,2017),(0,'MUNICIPALIDAD DE SANTA CRUZ LA LAGUNA',12100714,2017),(0,'MUNICIPALIDAD DE SAN PABLO LA LAGUNA',12100715,2017),(0,'MUNICIPALIDAD DE SAN MARCOS LA LAGUNA',12100716,2017),(0,'MUNICIPALIDAD DE SAN JUAN LA LAGUNA',12100717,2017),(0,'MUNICIPALIDAD DE SAN PEDRO LA LAGUNA',12100718,2017),(0,'MUNICIPALIDAD DE SANTIAGO ATITLAN',12100719,2017),(0,'MUNICIPALIDAD DE TOTONICAPAN',12100801,2017),(0,'MUNICIPALIDAD DE SAN CRISTOBAL TOTONICAPAN',12100802,2017),(0,'MUNICIPALIDAD DE SAN FRANCISCO EL ALTO',12100803,2017),(0,'MUNICIPALIDAD DE SAN ANDRES XECUL',12100804,2017),(0,'MUNICIPALIDAD DE MOMOSTENANGO',12100805,2017),(0,'MUNICIPALIDAD DE SANTA MARIA CHIQUIMULA',12100806,2017),(0,'MUNICIPALIDAD DE SANTA LUCIA LA REFORMA',12100807,2017),(0,'MUNICIPALIDAD DE SAN BARTOLO AGUAS CALIENTES',12100808,2017),(0,'MUNICIPALIDAD DE QUETZALTENANGO',12100901,2017),(0,'MUNICIPALIDAD DE SALCAJA',12100902,2017),(0,'MUNICIPALIDAD DE OLINTEPEQUE',12100903,2017),(0,'MUNICIPALIDAD DE SAN CARLOS SIJA',12100904,2017),(0,'MUNICIPALIDAD DE SIBILIA',12100905,2017),(0,'MUNICIPALIDAD DE CABRICAN',12100906,2017),(0,'MUNICIPALIDAD DE CAJOLA',12100907,2017),(0,'MUNICIPALIDAD DE SAN MIGUEL SIGUILA',12100908,2017),(0,'MUNICIPALIDAD DE SAN JUAN OSTUNCALCO',12100909,2017),(0,'MUNICIPALIDAD DE SAN MATEO',12100910,2017),(0,'MUNICIPALIDAD DE CONCEPCION CHIQUIRICHAPA',12100911,2017),(0,'MUNICIPALIDAD DE SAN MARTIN SACATEPEQUEZ',12100912,2017),(0,'MUNICIPALIDAD DE ALMOLONGA',12100913,2017),(0,'MUNICIPALIDAD DE CANTEL',12100914,2017),(0,'MUNICIPALIDAD DE HUITAN',12100915,2017),(0,'MUNICIPALIDAD DE ZUNIL',12100916,2017),(0,'MUNICIPALIDAD DE COLOMBA',12100917,2017),(0,'MUNICIPALIDAD DE SAN FRANCISCO LA UNION',12100918,2017),(0,'MUNICIPALIDAD DE EL PALMAR',12100919,2017),(0,'MUNICIPALIDAD DE COATEPEQUE',12100920,2017),(0,'MUNICIPALIDAD DE GENOVA',12100921,2017),(0,'MUNICIPALIDAD DE FLORES COSTA CUCA',12100922,2017),(0,'MUNICIPALIDAD DE LA ESPERANZA',12100923,2017),(0,'MUNICIPALIDAD DE PALESTINA DE LOS ALTOS',12100924,2017),(0,'MUNICIPALIDAD DE MAZATENANGO',12101001,2017),(0,'MUNICIPALIDAD DE CUYOTENANGO',12101002,2017),(0,'MUNICIPALIDAD DE SAN FRANCISCO ZAPOTITLAN',12101003,2017),(0,'MUNICIPALIDAD DE SAN BERNARDINO',12101004,2017),(0,'MUNICIPALIDAD DE SAN JOSE EL IDOLO',12101005,2017),(0,'MUNICIPALIDAD DE SANTO DOMINGO SUCHITEPEQUEZ',12101006,2017),(0,'MUNICIPALIDAD DE SAN LORENZO',12101007,2017),(0,'MUNICIPALIDAD DE SAMAYAC',12101008,2017),(0,'MUNICIPALIDAD DE SAN PABLO JOCOPILAS',12101009,2017),(0,'MUNICIPALIDAD DE SAN ANTONIO SUCHITEPEQUEZ',12101010,2017),(0,'MUNICIPALIDAD DE SAN MIGUEL PANAN',12101011,2017),(0,'MUNICIPALIDAD DE SAN GABRIEL',12101012,2017),(0,'MUNICIPALIDAD DE CHICACAO',12101013,2017),(0,'MUNICIPALIDAD DE PATULUL',12101014,2017),(0,'MUNICIPALIDAD DE SANTA BARBARA',12101015,2017),(0,'MUNICIPALIDAD DE SAN JUAN BAUTISTA',12101016,2017),(0,'MUNICIPALIDAD DE SANTO TOMAS LA UNION',12101017,2017),(0,'MUNICIPALIDAD DE ZUNILITO',12101018,2017),(0,'MUNICIPALIDAD DE PUEBLO NUEVO',12101019,2017),(0,'MUNICIPALIDAD DE RIO BRAVO',12101020,2017),(0,'MUNICIPALIDAD DE SAN JOSE LA MAQUINA',12101021,2017),(0,'MUNICIPALIDAD DE RETALHULEU',12101101,2017),(0,'MUNICIPALIDAD DE SAN SEBASTIAN',12101102,2017),(0,'MUNICIPALIDAD DE SANTA CRUZ MULUA',12101103,2017),(0,'MUNICIPALIDAD DE SAN MARTIN ZAPOTITLAN',12101104,2017),(0,'MUNICIPALIDAD DE SAN FELIPE',12101105,2017),(0,'MUNICIPALIDAD DE SAN ANDRES VILLA SECA',12101106,2017),(0,'MUNICIPALIDAD DE CHAMPERICO',12101107,2017),(0,'MUNICIPALIDAD DE NUEVO SAN CARLOS',12101108,2017),(0,'MUNICIPALIDAD DE EL ASINTAL',12101109,2017),(0,'MUNICIPALIDAD DE SAN MARCOS',12101201,2017),(0,'MUNICIPALIDAD DE SAN PEDRO SACATEPEQUEZ',12101202,2017),(0,'MUNICIPALIDAD DE SAN ANTONIO SACATEPEQUEZ',12101203,2017),(0,'MUNICIPALIDAD DE COMITANCILLO',12101204,2017),(0,'MUNICIPALIDAD DE SAN MIGUEL IXTAHUACAN',12101205,2017),(0,'MUNICIPALIDAD DE CONCEPCION TUTUAPA',12101206,2017),(0,'MUNICIPALIDAD DE TACANA',12101207,2017),(0,'MUNICIPALIDAD DE SIBINAL',12101208,2017),(0,'MUNICIPALIDAD DE TAJUMULCO',12101209,2017),(0,'MUNICIPALIDAD DE TEJUTLA',12101210,2017),(0,'MUNICIPALIDAD DE SAN RAFAEL PIE DE LA CUESTA',12101211,2017),(0,'MUNICIPALIDAD DE NUEVO PROGRESO',12101212,2017),(0,'MUNICIPALIDAD DE EL TUMBADOR',12101213,2017),(0,'MUNICIPALIDAD DE EL RODEO',12101214,2017),(0,'MUNICIPALIDAD DE MALACATAN',12101215,2017),(0,'MUNICIPALIDAD DE CATARINA',12101216,2017),(0,'MUNICIPALIDAD DE AYUTLA',12101217,2017),(0,'MUNICIPALIDAD DE OCOS',12101218,2017),(0,'MUNICIPALIDAD DE SAN PABLO',12101219,2017),(0,'MUNICIPALIDAD DE EL QUETZAL',12101220,2017),(0,'MUNICIPALIDAD DE LA REFORMA',12101221,2017),(0,'MUNICIPALIDAD DE PAJAPITA',12101222,2017),(0,'MUNICIPALIDAD DE IXCHIGUAN',12101223,2017),(0,'MUNICIPALIDAD DE SAN JOSE OJETENAM',12101224,2017),(0,'MUNICIPALIDAD DE SAN CRISTOBAL CUCHO',12101225,2017),(0,'MUNICIPALIDAD DE SIPACAPA',12101226,2017),(0,'MUNICIPALIDAD DE ESQUIPULAS PALO GORDO',12101227,2017),(0,'MUNICIPALIDAD DE RIO BLANCO',12101228,2017),(0,'MUNICIPALIDAD DE SAN LORENZO',12101229,2017),(0,'MUNICIPALIDAD DE LA BLANCA',12101230,2017),(0,'MUNICIPALIDAD DE HUEHUETENANGO',12101301,2017),(0,'MUNICIPALIDAD DE CHIANTLA',12101302,2017),(0,'MUNICIPALIDAD DE MALACATANCITO',12101303,2017),(0,'MUNICIPALIDAD DE CUILCO',12101304,2017),(0,'MUNICIPALIDAD DE NENTON',12101305,2017),(0,'MUNICIPALIDAD DE SAN PEDRO NECTA',12101306,2017),(0,'MUNICIPALIDAD DE JACALTENANGO',12101307,2017),(0,'MUNICIPALIDAD DE SOLOMA',12101308,2017),(0,'MUNICIPALIDAD DE SAN ILDEFONSO IXTAHUACAN',12101309,2017),(0,'MUNICIPALIDAD DE SANTA BARBARA',12101310,2017),(0,'MUNICIPALIDAD DE LA LIBERTAD',12101311,2017),(0,'MUNICIPALIDAD DE LA DEMOCRACIA',12101312,2017),(0,'MUNICIPALIDAD DE SAN MIGUEL ACATAN',12101313,2017),(0,'MUNICIPALIDAD DE SAN RAFAEL LA INDEPENDENCIA',12101314,2017),(0,'MUNICIPALIDAD DE TODOS SANTOS CUCHUMATAN',12101315,2017),(0,'MUNICIPALIDAD DE SAN JUAN ATITAN',12101316,2017),(0,'MUNICIPALIDAD DE SANTA EULALIA',12101317,2017),(0,'MUNICIPALIDAD DE SAN MATEO IXTATAN',12101318,2017),(0,'MUNICIPALIDAD DE COLOTENANGO',12101319,2017),(0,'MUNICIPALIDAD DE SAN SEBASTIAN HUEHUETENANGO',12101320,2017),(0,'MUNICIPALIDAD DE TECTITAN',12101321,2017),(0,'MUNICIPALIDAD DE CONCEPCION HUISTA',12101322,2017),(0,'MUNICIPALIDAD DE SAN JUAN IXCOY',12101323,2017),(0,'MUNICIPALIDAD DE SAN ANTONIO HUISTA',12101324,2017),(0,'MUNICIPALIDAD DE SAN SEBASTIAN COATAN',12101325,2017),(0,'MUNICIPALIDAD DE BARILLAS',12101326,2017),(0,'MUNICIPALIDAD DE AGUACATAN',12101327,2017),(0,'MUNICIPALIDAD DE SAN RAFAEL PETZAL',12101328,2017),(0,'MUNICIPALIDAD DE SAN GASPAR IXCHIL',12101329,2017),(0,'MUNICIPALIDAD DE SANTIAGO CHIMALTENANGO',12101330,2017),(0,'MUNICIPALIDAD DE SANTA ANA HUISTA',12101331,2017),(0,'MUNICIPALIDAD DE UNION CANTINIL',12101332,2017),(0,'MUNICIPALIDAD DE PETATÁN',12101333,2017),(0,'MUNICIPALIDAD DE SANTA CRUZ DEL QUICHE',12101401,2017),(0,'MUNICIPALIDAD DE CHICHE',12101402,2017),(0,'MUNICIPALIDAD DE CHINIQUE',12101403,2017),(0,'MUNICIPALIDAD DE ZACUALPA',12101404,2017),(0,'MUNICIPALIDAD DE CHAJUL',12101405,2017),(0,'MUNICIPALIDAD DE CHICHICASTENANGO',12101406,2017),(0,'MUNICIPALIDAD DE PATZITE',12101407,2017),(0,'MUNICIPALIDAD DE SAN ANTONIO ILOTENANGO',12101408,2017),(0,'MUNICIPALIDAD DE SAN PEDRO JOCOPILAS',12101409,2017),(0,'MUNICIPALIDAD DE CUNEN',12101410,2017),(0,'MUNICIPALIDAD DE SAN JUAN COTZAL',12101411,2017),(0,'MUNICIPALIDAD DE JOYABAJ',12101412,2017),(0,'MUNICIPALIDAD DE NEBAJ',12101413,2017),(0,'MUNICIPALIDAD DE SAN ANDRES SAJCABAJA',12101414,2017),(0,'MUNICIPALIDAD DE USPANTAN',12101415,2017),(0,'MUNICIPALIDAD DE SACAPULAS',12101416,2017),(0,'MUNICIPALIDAD DE SAN BARTOLOME JOCOTENANGO',12101417,2017),(0,'MUNICIPALIDAD DE CANILLA',12101418,2017),(0,'MUNICIPALIDAD DE CHICAMAN',12101419,2017),(0,'MUNICIPALIDAD DE IXCAN',12101420,2017),(0,'MUNICIPALIDAD DE PACHALUM',12101421,2017),(0,'MUNICIPALIDAD DE SALAMA',12101501,2017),(0,'MUNICIPALIDAD DE SAN MIGUEL CHICAJ',12101502,2017),(0,'MUNICIPALIDAD DE RABINAL',12101503,2017),(0,'MUNICIPALIDAD DE CUBULCO',12101504,2017),(0,'MUNICIPALIDAD DE GRANADOS',12101505,2017),(0,'MUNICIPALIDAD DE EL CHOL',12101506,2017),(0,'MUNICIPALIDAD DE SAN JERONIMO',12101507,2017),(0,'MUNICIPALIDAD DE PURULHA',12101508,2017),(0,'MUNICIPALIDAD DE COBAN',12101601,2017),(0,'MUNICIPALIDAD DE SANTA CRUZ VERAPAZ',12101602,2017),(0,'MUNICIPALIDAD DE SAN CRISTOBAL VERAPAZ',12101603,2017),(0,'MUNICIPALIDAD DE TACTIC',12101604,2017),(0,'MUNICIPALIDAD DE TAMAHU',12101605,2017),(0,'MUNICIPALIDAD DE TUCURU',12101606,2017),(0,'MUNICIPALIDAD DE PANZOS',12101607,2017),(0,'MUNICIPALIDAD DE SENAHU',12101608,2017),(0,'MUNICIPALIDAD DE SAN PEDRO CARCHA',12101609,2017),(0,'MUNICIPALIDAD DE SAN JUAN CHAMELCO',12101610,2017),(0,'MUNICIPALIDAD DE LANQUIN',12101611,2017),(0,'MUNICIPALIDAD DE CAHABON',12101612,2017),(0,'MUNICIPALIDAD DE CHISEC',12101613,2017),(0,'MUNICIPALIDAD DE CHAHAL',12101614,2017),(0,'MUNICIPALIDAD DE FRAY BARTOLOME DE LAS CASAS',12101615,2017),(0,'MUNICIPALIDAD DE SANTA CATALINA LA TINTA',12101616,2017),(0,'MUNICIPALIDAD DE RAXRUHA',12101617,2017),(0,'MUNICIPALIDAD DE FLORES',12101701,2017),(0,'MUNICIPALIDAD DE SAN JOSE',12101702,2017),(0,'MUNICIPALIDAD DE SAN BENITO',12101703,2017),(0,'MUNICIPALIDAD DE SAN ANDRES',12101704,2017),(0,'MUNICIPALIDAD DE LA LIBERTAD',12101705,2017),(0,'MUNICIPALIDAD DE SAN FRANCISCO',12101706,2017),(0,'MUNICIPALIDAD DE SANTA ANA',12101707,2017),(0,'MUNICIPALIDAD DE DOLORES',12101708,2017),(0,'MUNICIPALIDAD DE SAN LUIS',12101709,2017),(0,'MUNICIPALIDAD DE SAYAXCHE',12101710,2017),(0,'MUNICIPALIDAD DE MELCHOR DE MENCOS',12101711,2017),(0,'MUNICIPALIDAD DE POPTUN',12101712,2017),(0,'MUNICIPALIDAD DE LAS CRUCES',12101713,2017),(0,'MUNICIPALIDAD DE EL CHAL',12101714,2017),(0,'MUNICIPALIDAD DE PUERTO BARRIOS',12101801,2017),(0,'MUNICIPALIDAD DE LIVINGSTON',12101802,2017),(0,'MUNICIPALIDAD DE EL ESTOR',12101803,2017),(0,'MUNICIPALIDAD DE MORALES',12101804,2017),(0,'MUNICIPALIDAD DE LOS AMATES',12101805,2017),(0,'MUNICIPALIDAD DE ZACAPA',12101901,2017),(0,'MUNICIPALIDAD DE ESTANZUELA',12101902,2017),(0,'MUNICIPALIDAD DE RIO HONDO',12101903,2017),(0,'MUNICIPALIDAD DE GUALAN',12101904,2017),(0,'MUNICIPALIDAD DE TECULUTAN',12101905,2017),(0,'MUNICIPALIDAD DE USUMATLAN',12101906,2017),(0,'MUNICIPALIDAD DE CABAÑAS',12101907,2017),(0,'MUNICIPALIDAD DE SAN DIEGO',12101908,2017),(0,'MUNICIPALIDAD DE LA UNION',12101909,2017),(0,'MUNICIPALIDAD DE HUITE',12101910,2017),(0,'MUNICIPALIDAD DE SAN JORGE',12101911,2017),(0,'MUNICIPALIDAD DE CHIQUIMULA',12102001,2017),(0,'MUNICIPALIDAD DE SAN JOSE LA ARADA',12102002,2017),(0,'MUNICIPALIDAD DE SAN JUAN ERMITA',12102003,2017),(0,'MUNICIPALIDAD DE JOCOTAN',12102004,2017),(0,'MUNICIPALIDAD DE CAMOTAN',12102005,2017),(0,'MUNICIPALIDAD DE OLOPA',12102006,2017),(0,'MUNICIPALIDAD DE ESQUIPULAS',12102007,2017),(0,'MUNICIPALIDAD DE CONCEPCION LAS MINAS',12102008,2017),(0,'MUNICIPALIDAD DE QUEZALTEPEQUE',12102009,2017),(0,'MUNICIPALIDAD DE SAN JACINTO',12102010,2017),(0,'MUNICIPALIDAD DE IPALA',12102011,2017),(0,'MUNICIPALIDAD DE JALAPA',12102101,2017),(0,'MUNICIPALIDAD DE SAN PEDRO PINULA',12102102,2017),(0,'MUNICIPALIDAD DE SAN LUIS JILOTEPEQUE',12102103,2017),(0,'MUNICIPALIDAD DE SAN MANUEL CHAPARRON',12102104,2017),(0,'MUNICIPALIDAD DE SAN CARLOS ALZATATE',12102105,2017),(0,'MUNICIPALIDAD DE MONJAS',12102106,2017),(0,'MUNICIPALIDAD DE MATAQUESCUINTLA',12102107,2017),(0,'MUNICIPALIDAD DE JUTIAPA',12102201,2017),(0,'MUNICIPALIDAD DE EL PROGRESO',12102202,2017),(0,'MUNICIPALIDAD DE SANTA CATARINA MITA',12102203,2017),(0,'MUNICIPALIDAD DE AGUA BLANCA',12102204,2017),(0,'MUNICIPALIDAD DE ASUNCION MITA',12102205,2017),(0,'MUNICIPALIDAD DE YUPILTEPEQUE',12102206,2017),(0,'MUNICIPALIDAD DE ATESCATEMPA',12102207,2017),(0,'MUNICIPALIDAD DE JEREZ',12102208,2017),(0,'MUNICIPALIDAD DE EL ADELANTO',12102209,2017),(0,'MUNICIPALIDAD DE ZAPOTITLAN',12102210,2017),(0,'MUNICIPALIDAD DE COMAPA',12102211,2017),(0,'MUNICIPALIDAD DE JALPATAGUA',12102212,2017),(0,'MUNICIPALIDAD DE CONGUACO',12102213,2017),(0,'MUNICIPALIDAD DE MOYUTA',12102214,2017),(0,'MUNICIPALIDAD DE PASACO',12102215,2017),(0,'MUNICIPALIDAD DE SAN JOSE ACATEMPA',12102216,2017),(0,'MUNICIPALIDAD DE QUESADA',12102217,2017),(0,'MUNICIPALIDAD DE PETATÁN',12102218,2017),(0,'ENTIDAD MET. REGUL. DE TRANSPORTE Y TRANSITO DEL MUN. DE GUAT. Y SUS AREAS DE INFLUENCIA U.',12200001,2017),(0,'OFI. ASESORA DE R.R.H.H. DE LAS MUNI. -OARHM-',12200597,2017),(0,'PLAN DE PRESTACIONES DEL EMPLEADO MUNICIPAL (PPEM)',12300599,2017),(0,'MANCOMUNIDAD DE MUNICIPIOS DE DESARROLLO INTEGRAL DE LA CUENCA COPAN CH ORTI',12400001,2017),(0,'MANCOMUNIDAD ENCUENTRO REGIONAL IXIL POR LA PAZ (ERIPAZ)',12400002,2017),(0,'MANCOMUNIDAD LAGUNA GUIJA',12400003,2017),(0,'MANCOMUNIDAD DE NOR-ORIENTE',12400004,2017),(0,'MANCOMUNIDAD DE MUNICIPIOS DE LA CUENCA DEL RIO EL NARANJO (MANCUERNA)',12400005,2017),(0,'MANCOMUNIDAD DE MUNICIPIOS METROPOLI DE LOS ALTOS',12400006,2017),(0,'MANCOMUNIDAD DE MUNICIPIOS FRONTERA DEL NORTE',12400007,2017),(0,'ASOCIACION DE MUNICIPIOS EN EL CORAZON DE LA ZONA PAZ (MUNICOPAZ)',12400008,2017),(0,'ASOCIACION NACIONAL DE MUNICIPALIDADES (ANAM)',12400009,2017),(0,'MANCOMUNIDAD DE MUNICIPIOS PARA EL DESARROLLO INTEGRAL DEL AREA POQOMCHI',12400010,2017),(0,'MANCOMUNIDAD AREA MAM DE QUETZALTENANGO (MAMQ)',12400011,2017),(0,'MANCOMUNIDAD DE MUNICIPIOS DE LA FRANJA TRANSVERSAL DEL NORTE',12400012,2017),(0,'MANCOMUNIDAD TRINACIONAL FRONTERIZA RIO LEMPA',12400013,2017),(0,'MANCOMUNIDAD DEL CONO SUR, JUTIAPA',12400014,2017),(0,'MANCOMUNIDAD MONTAÑA EL GIGANTE',12400015,2017),(0,'MANCOMUNIDAD DE MUNICIPIOS KAKCHIQUEL CHICHOY Y ATITLAN (MANKATITLAN)',12400017,2017),(0,'MANCOMUNIDAD TZOLOJYA',12400018,2017),(0,'MANCOMUNIDAD HUISTA',12400019,2017),(0,'MANCOMUNIDAD DEL SUR ORIENTE',12400020,2017),(0,'ASOCIACIÓN DE DESARROLLO INTEGRAL DE MUNICIPALIDADES DEL ALTIPLANO MÁRQUENSE (ADIMAM)',12400021,2017),(0,'MANCOMUNIDAD GRAN CIUDAD DEL SUR DEL DEPARTAMENTO DE GUATEMALA',12400022,2017),(0,'MANCOMUNIDAD JALAPA UNIDA POR LA SEGURIDAD ALIMENTARIA Y NUTRICIONAL',12400023,2017),(0,'MANCOMUNIDAD DE MUNICIPIOS PARA EL DESARROLLO LOCAL SOSTENIBLE EL PACIFICO',12400024,2017),(0,'MANCOMUNIDAD DE MUNICIPIOS DEL CORREDOR SECO DEL DEPARTAMENTO DE QUICHE (MANCOSEQ)',12400025,2017),(0,'EMPRESA GUAT. DE TELECOM. -GUATEL-',21100072,2017),(0,'Z. LIB. DE IND. Y COM. STO. T. DE CAST. ZOLIC',21100073,2017),(0,'EMP. PORT. NAC. STO. T. DE CAST. EMPORNAC',21100074,2017),(0,'EMPRESA PORTUARIA QUETZAL -EPQ-',21100075,2017),(0,'EMP. PORT. NAC. DE CHAMPERICO -EPNCH-',21100076,2017),(0,'EMPRESA FERROCARRILES DE GUATEMALA  -FEGUA-',21100077,2017),(0,'INST. NAC. DE COMER. AGRICOLA -INDECA-',21100078,2017),(0,'INST. NACIONAL DE ELECTRIFICACISN -INDE',21100080,2017),(0,'EMPRESA MUNICIPAL DE AGUA -EMPAGUA-',21200090,2017),(0,'EMP. ELECTRICA MUNI. DE HUEHUETENANGO',21200091,2017),(0,'EMPRESA ELICTRICA MUNICIPAL DE JALAPA',21200092,2017),(0,'EMP. ELECTRICA MUNI. DE SAN PEDRO PINULA',21200093,2017),(0,'EMPRESA ELECTRICA MUNICIPAL DE ZACAPA',21200094,2017),(0,'EMP. HIDROELICTRICA MUNI. DE EL PROGRESO',21200095,2017),(0,'EMP. HIDROELICTRICA MUNI. DE RETALHULEU',21200096,2017),(0,'EMPRESA MUNICIPAL DE TRANSPORTE DE LA CIUDAD DE GUATEMALA',21200097,2017),(0,'EMPRESA MUNICIPAL RURAL DE ELECTRICIDAD IXCAN (EMRE)',21200098,2017),(0,'EMPRESA MUNICIPAL DE AGUA POTABLE Y ALCANTARILLADO (EMAPET), FLORES-SAN BENITO, PETÉN',21200099,2017),(0,'EMPRESA ELECTRICA DE GUALAN',21200100,2017),(0,'EMPRESA ELECTRICA MUNICIPAL DE PUERTO BARRIOS',21200101,2017),(0,'EMPRESA METROPOLITANA DE VIVIENDA Y DESARROLLO URBANO',21200102,2017),(0,'EMPRESA PUBLICA MUNICIPAL AGROINDUSTRIAL DE ESTANZUELA, DEPARTAMENTO DE ZACAPA',21200103,2017),(0,'EMPRESA MUNICIPAL DE AGUA JALAPAGUA',21200104,2017),(0,'EMPRESA MUNICIPAL DE MANEJO INTEGRAL DE RESIDUOS SOLIDOS DE JALAPA DEPARTAMENTO DE JALAPA',21200105,2017),(0,'INST. DE FOMENTO DE HIPOTECAS ASEGURADAS -FHA-',22110620,2017),(0,'CORPORACION FINANCIERA NACIONAL  -CORFINA-',22110621,2017),(0,'SUPERINTENDENCIA DE BANCOS -SIB-',22210033,2017),(0,'EL CRÉDITO HIPOTECARIO NACIONAL DE GUATEMALA (CHN)',22210630,2017),(0,'BANCO DE GUATEMALA',22210633,2017),(100,'COORDINACIÓN, ADMINISTRACIÓN Y NORMATIVIDAD',11200056,2017),(101,'PRESIDENCIA',11120002,2017),(101,'DIRECCIÓN GENERAL DE FINANZAS DEL MINISTERIO DE LA DEFENSA NACIONAL',11130006,2017),(101,'DIRECCION DE SERVICIOS ADMINISTRATIVOS',11130008,2017),(101,'DIRECCION SUPERIOR',11130011,2017),(101,'DIRECCION SUPERIOR',11130015,2017),(101,'REGISTRO GENERAL DE LA PROPIEDAD',11140026,2017),(101,'REGISTRO DE INFORMACION CATASTRAL',11140065,2017),(101,'GERENCIA GENERAL',11200035,2017),(101,'JUNTA DIRECTIVA',11400068,2017),(101,'MUNICIPALIDAD DE QUETZALTENANGO',12100901,2017),(101,'MUNICIPALIDAD DE SAN MARCOS',12101201,2017),(101,'MUNICIPALIDAD DE SAN PEDRO SACATEPEQUEZ',12101202,2017),(101,'EMPRESA DE GENERACIÓN DE ENERGÍA ELÉCTRICA DEL INDE',21100080,2017),(101,'ADMINISTRACION GENERAL',21200090,2017),(102,'GERENCIA GENERAL',11120002,2017),(102,'COMANDO REGIONAL CENTRAL',11130006,2017),(102,'DIRECCION DE INFORMATICA',11130008,2017),(102,'REGISTRO MERCANTIL GENERAL DE LA REPUBLICA',11130011,2017),(102,'DIRECCION GENERAL DE LAS ARTES',11130015,2017),(102,'OFICINA RECEPCION Y DEVOLUCION DE DOCTOS MONTUFAR',11140026,2017),(102,'PROYECTO DE ADMINISTRACION DE TIERRAS',11140065,2017),(102,'PARQUE AMATITLAN',11200035,2017),(102,'GERENCIA ',11400068,2017),(102,'EMPRESA ELECTRICA MUNICIPAL DE QUETZALTENANGO',12100901,2017),(102,'EMPRESA ELECTRICA MUNICIPAL DE SAN MARCOS',12101201,2017),(102,'EMPRESA ELECTRICA MUNICIPAL DE SAN PEDRO SACATEPEQUEZ',12101202,2017),(102,'EMPRESA DE TRANSPORTE Y CONTROL DE ENERGÍA ELÉCTRICA DEL INDE',21100080,2017),(102,'FUENTES DE PRODUCCION DE AGUA',21200090,2017),(103,'GERENCIA ADMINISTRATIVA',11120002,2017),(103,'COMANDO SUPERIOR DE EDUCACIÓN DEL EJÉRCITO DE GUATEMALA',11130006,2017),(103,'DIRECCION DE RECURSOS HUMANOS',11130008,2017),(103,'REGISTRO DE LA PROPIEDAD INTELECTUAL',11130011,2017),(103,'DIRECCION GENERAL DEL PATRIMONIO CULTURAL Y NATURAL',11130015,2017),(103,'OFICINA RECEPCION Y DEVOLUCION DOCTOS. ESCUINTLA',11140026,2017),(103,'PARQUE AGUA CALIENTE',11200035,2017),(103,'PRESTACIONES SOCIALES Y LABORALES (POLÍTICA LABORAL)',11400068,2017),(103,'EMPRESA MUNICIPAL DE AGUAS DE XELAJU',12100901,2017),(103,'EMPRESA MUNICIPAL DE AGUAS DE SAN MARCOS',12101201,2017),(103,'EMPRESA CONSTRUCTORA MUNICIPAL DE SAN PEDRO SACATEPEQUEZ',12101202,2017),(103,'EMPRESA DE COMERCIALIZACIÓN DE ENERGÍA ELÉCTRICA DEL INDE',21100080,2017),(103,'ADMINISTRACION DE LA RED',21200090,2017),(104,'CENTRO MÉDICO MILITAR',11130006,2017),(104,'DICADE',11130008,2017),(104,'DIRECCION DEL SISTEMA NACIONAL DE CALIDAD',11130011,2017),(104,'DIRECCION GENERAL DEL DEPORTE Y LA RECREACION',11130015,2017),(104,'OFICINA DE RECEPCION Y DEVOLUCION DE DOCTOS COBAN',11140026,2017),(104,'PARQUE URBANO PETAPA',11200035,2017),(104,'CONTRALORÍA GENERAL',11400068,2017),(104,'EMPRESA MUNICIPAL DE AGUA',12101202,2017),(104,'GERENCIA DE ELECTRIFICACIÓN RURAL Y OBRAS',21100080,2017),(104,'SISTEMA SANTA LUISA',21200090,2017),(105,'UNIDAD DE MODERNIZACIÓN',11120002,2017),(105,'DIRECCIÓN GENERAL ADMINISTRATIVA DEL ESTADO MAYOR DE LA DEFENSA NACIONAL',11130006,2017),(105,'DIRECCION DE PLANIFICACION EDUCATIVA',11130008,2017),(105,'DIRECCION DE SERVICIOS FINANCIEROS Y TECNICO EMPRESARIALES',11130011,2017),(105,'DIRECCION GENERAL DE DESARROLLO CULTURAL Y FORTALECIMIENTO DE LAS CULTURAS',11130015,2017),(105,'OFICINA DE RECEPCION Y DEVOLUCION DE DOCTOS. PETEN',11140026,2017),(105,'HOSTALES',11200035,2017),(105,'SUBGERENCIA DE INTEGRIDAD Y TRANSPARENCIA ADMINISTRATIVA ',11400068,2017),(105,'SISTEMA CAMBRAY',21200090,2017),(106,'APOYO AL SECTOR JUSTICIA',11120002,2017),(106,'DIRECCIÓN DE OPERACIONES DE PAZ DEL ESTADO MAYOR DE LA DEFENSA NACIONAL',11130006,2017),(106,'UNIDAD DE INNOVACION EDUCATIVA',11130008,2017),(106,'DIRECCION DE ATENCION Y ASISTENCIA AL CONSUMIDOR',11130011,2017),(106,'BIBLIOTECA NACIONAL DE GUATEMALA',11130015,2017),(106,'OFICINA DE RECEPCION Y DEVOLUCION DOCTOS. TECULUTA',11140026,2017),(106,'PARQUE ACUATICO XOCOMIL',11200035,2017),(106,'SUBGERENCIA DE PRESTACIONES EN SALUD',11400068,2017),(106,'SISTEMA ILUSIONES',21200090,2017),(107,'COMANDO DE APOYO LOGÍSTICO',11130006,2017),(107,'DIREC. GENERAL DE PROYECTOS DE APOYO',11130008,2017),(107,'UNIDAD EJECUTORA DEL PROGRAMA DE APOYO AL COMERCIO EXTERIOR',11130011,2017),(107,'ESCUELA REGISTRAL',11140026,2017),(107,'PARQUE DE DIVERSIONES XETULUL',11200035,2017),(107,'SUBGERENCIA ADMINISTRATIVA ',11400068,2017),(107,'SISTEMA LO DE COY',21200090,2017),(108,'COMANDANCIA DE LA FUERZA AÉREA GUATEMALTECA',11130006,2017),(108,'DIRECCION GENERAL DE EDUCACION BILINGUE INTERCULTURAL',11130008,2017),(108,'PROGRAMA NACIONAL DE COMPETITIVIDAD',11130011,2017),(108,'DEPARTAMENTO DE CONSTRUCCIONES',11200035,2017),(108,'DEPARTAMENTO DE SERVICIOS DE APOYO',11400068,2017),(108,'SISTEMA BRIGADA',21200090,2017),(109,'COMANDANCIA DE LA MARINA DE LA DEFENSA NACIONAL',11130006,2017),(109,'DIRECCION GENERAL DE EDUCACION FISICA',11130008,2017),(109,'PARQUE ECOLOGICO XEJUYUP',11200035,2017),(109,'DIVISIÓN DE TRANSPORTES  - ESTACIÓN CENTRAL',11400068,2017),(109,'SISTEMA DE BOMBEO OJO DE AGUA DIAMANTE',21200090,2017),(110,'CUERPO DE INGENIEROS DEL EJÉRCITO TENIENTE CORONEL DE INGENIEROS E INGENIERO FRANCISCO VELA ARANGO',11130006,2017),(110,'DIRECCION GENERAL DE EDUCACION EXTRAESCOLAR',11130008,2017),(110,'SUBGERENCIA DE PLANIFICACIÓN Y DESARROLLO',11400068,2017),(110,'SISTEMA DE POZOS',21200090,2017),(111,'DIRECCIÓN GENERAL DE CONTROL DE ARMAS Y MUNICIONES DEL MINISTERIO DE LA DEFENSA NACIONAL',11130006,2017),(111,'COMITE NACIONAL DE ALFABETIZACION',11130008,2017),(111,'SUBGERENCIA DE PRESTACIONES PECUNIARIAS',11400068,2017),(111,'SISTEMAS DE DRENAJES Y ALCANTARILLADO',21200090,2017),(112,'CENTRO DE ATENCIÓN A DISCAPACITADOS DEL EJÉRCITO DE GUATEMALA',11130006,2017),(112,'DIRECCION DE COOPERACION NACIONAL E INTERNACIONAL',11130008,2017),(112,'SUBGERENCIA FINANCIERA ',11400068,2017),(112,'SISTEMA DE POZOS EMERGENCIA I',21200090,2017),(113,'UNIDAD COORDINADORA DE PROYECTOS',11130008,2017),(113,'HOSPITAL GENERAL DE ENFERMEDADES',11400068,2017),(113,'SISTEMA DE BOMBEO OJO DE AGUA DIAMANTE',21200090,2017),(114,'PROG. NAC. DE AUTOGESTION PARA EL DESARROLLO EDUCATIVO',11130008,2017),(114,'HOSPITAL DOCTOR JUAN JOSÉ ARÉVALO BERMEJO',11400068,2017),(115,'DIRECCION DE AUDITORIA INTERNA',11130008,2017),(115,'POLICLÍNICA',11400068,2017),(115,'SISTEMA DE ALCANTARILLADO',21200090,2017),(116,'DIRECCION DE ASESORIA JURIDICA',11130008,2017),(116,'UNIDAD PERIFÉRICA ZONA 5',11400068,2017),(116,'DIRECCION DE INFRAESTRUCTURA Y OPERACIONES',21200090,2017),(117,'DIRECCION DE COMUNICACION SOCIAL',11130008,2017),(117,'UNIDAD PERIFÉRICA ZONA 11',11400068,2017),(117,'DIRECCION DE PRODUCCION DE APLICACIONES',21200090,2017),(118,'DIRECCION DE ADQUISICIONES Y CONTRATACIONES',11130008,2017),(118,'CONSULTORIO SAN JOSÉ PINULA, GUATEMALA',11400068,2017),(118,'SERVICIOS A USUARIOS SECTOR 1',21200090,2017),(119,'DIRECCION GENERAL DE COBERTURA E INFRAESTRUCTURA EDUCATIVA',11130008,2017),(119,'CONSULTORIO PALENCIA, GUATEMALA',11400068,2017),(119,'SERVICIOS A USUARIOS SECTOR 2',21200090,2017),(120,'DIRECCION GENERAL DE EVALUACION E INVESTIGACION EDUCATIVA',11130008,2017),(120,'SERVICIOS A USUARIOS SECTOR 3',21200090,2017),(121,'DIRECCIÓN GENERAL DE ACREDITACIÓN Y CERTIFICACIÓN',11130008,2017),(121,'UNIDAD ASISTENCIAL SAN JUAN SACATEPEQUEZ, GUATEMALA',11400068,2017),(121,'SERVICIOS A USUARIOS SECTOR 4',21200090,2017),(122,'DIRECCIÓN DE DESARROLLO Y FORTALECIMIENTO INSTITUCIONAL',11130008,2017),(122,'CONSULTORIO FRAIJANES, GUATEMALA',11400068,2017),(122,'SERVICIOS A USUARIOS SECTOR 5',21200090,2017),(123,'DIRECCIÓN GENERAL DE PARTICIPACION COMUNITARIA Y SERVICIOS DE APOYO',11130008,2017),(123,'UNIDAD ASISTENCIAL AMATITLÁN, GUATEMALA',11400068,2017),(123,'SERVICIOS A USUARIOS VENTANILLA UNICA',21200090,2017),(124,'DIRECCIÓN GENERAL DE GESTIÓN DE CALIDAD EDUCATIVA',11130008,2017),(124,'CONSULTORIO VILLA CANALES, GUATEMALA',11400068,2017),(124,'COORDINACION DE SISTEMAS DE DRENAJES Y ALCANTARILLADO',21200090,2017),(125,'DIRECCION GENERAL DE EDUCACION ESPECIAL',11130008,2017),(125,'CONSULTORIO FINCA SANTA LEONARDA, VILLA CANALES',11400068,2017),(125,'DIRECCION DE FUENTES DE PRODUCCION DE AGUA',21200090,2017),(126,'DIRECCION GENERAL DE CURRICULO',11130008,2017),(126,'CONSULTORIO VILLA NUEVA, GUATEMALA',11400068,2017),(126,'ADMINISTRACION DE LA RED',21200090,2017),(127,'DIRECCION GENERAL DE FORTALECIMIENTO A LA COMUNIDAD EDUCATIVA',11130008,2017),(127,'HOSPITAL DE REHABILITACIÓN',11400068,2017),(128,'DIRECCION GENERAL DE MONITOREO Y VERIFICACION DE LA CALIDAD',11130008,2017),(128,'HOSPITAL DE GINECO  OBSTETRICIA',11400068,2017),(129,'DIRECCION GENERAL DE COORDINACION DE DIRECCIONES DEPARTAMENTALES DE EDUCACION',11130008,2017),(129,'HOSPITAL GENERAL DE ACCIDENTES CEIBAL',11400068,2017),(130,'DIRECCION DE DESARROLLO MAGISTERIAL',11130008,2017),(130,'CENTRO DE ATENCIÓN INTEGRAL DE SALUD MENTAL ',11400068,2017),(131,'DIRECCION EJECUTORA DEL PROGRAMA MI FAMILIA PROGRESA',11130008,2017),(131,'CENTRO DE ATENCIÓN MÉDICA INTEGRAL PARA PENSIONADOS',11400068,2017),(132,'UNIDAD EJECUTORA ESPECIAL DEL PROGRAMA MI ESCUELA PROGRESA',11130008,2017),(132,'CONSULTORIO GUASTATOYA, EL PROGRESO',11400068,2017),(133,'CONSEJO NACIONAL DE EDUCACIÓN',11130008,2017),(133,'CONSULTORIO ANTIGUA GUATEMALA, SACATEPÉQUEZ',11400068,2017),(134,'JUNTA CALIFICADORA DE PERSONAL',11130008,2017),(134,'DIRECCIÓN DEPARTAMENTAL, CHIMALTENANGO',11400068,2017),(135,'JURADO NACIONAL DE OPOSICION',11130008,2017),(135,'HOSPITAL CHIMALTENANGO, CHIMALTENANGO',11400068,2017),(136,'UNIDAD EJECUTORA ESPECIAL DE FINANCIAMIENTO EXTERNO',11130008,2017),(136,'HOSPITAL CUILAPA, SANTA ROSA',11400068,2017),(137,'CONSULTORIO JALAPA, JALAPA',11400068,2017),(138,'DIRECCIÓN DEPARTAMENTAL, JUTIAPA, JUTIAPA',11400068,2017),(139,'CONSULTORIO JUTIAPA, JUTIAPA',11400068,2017),(140,'UNIDAD DE CONSULTA EXTERNA DE ENFERMEDADES',11400068,2017),(141,'CENTRO DE ATENCION MEDICA INTEGRAL PARA PENSIONADOS CAMIP 2 BARRANQUILLA',11400068,2017),(142,'DIVISION DE MANTENIMIENTO',11400068,2017),(143,'DEPARTAMENTO DE INFRAESTRUCTURA INSTITUCIONAL',11400068,2017),(144,'SUBGERENCIA DE RECURSOS HUMANOS',11400068,2017),(145,'CENTRO DE ATENCION MEDICA INTEGRAL PARA PENSIONADOS CAMIP 3 ZUNIL',11400068,2017),(146,'DEPARTAMENTO DE COMUNICACIÓN SOCIAL Y RELACIONES PÚBLICAS',11400068,2017),(147,'UNIDAD DE CONSULTA EXTERNA DE ESPECIALIDADES MÉDICO QUIRÚRGICAS \"GERONA\"',11400068,2017),(200,'GERENCIA REGIONAL CENTRAL - SAT',11200056,2017),(201,'UNIDAD DE ADMINISTRACION Y FINANZAS REGIONAL',11120002,2017),(201,'ESTADO MAYOR PRESIDENCIAL',11130003,2017),(201,'DIRECCION DE SERVICIOS ADMINISTRATIVOS Y FINANCIEROS',11130005,2017),(201,'DIRECCION FINANCIERA',11130007,2017),(201,'DIRECCION DE ADMINISTRACION FINANCIERA',11130008,2017),(201,'DEPARTAMENTO ADMINISTRATIVO',11130009,2017),(201,'ADMINISTRACIÓN FINANCIERA',11130012,2017),(201,'DIRECCION SUPERIOR',11130013,2017),(201,'ADMINISTRACIÓN FINANCIERA-UDAF',11130015,2017),(201,'SECRETARÍA GENERAL DE LA PRESIDENCIA DE LA REPÚBLICA',11130016,2017),(201,'UNIDAD DE ADMINISTRACIÓN FINANCIERA UDAF DEL MINISTERIO DE DESARROLLO SOCIAL',11130020,2017),(201,'ADMINISTRACION FINANCIERA-UDAF',11140022,2017),(201,'ADMINISTRACION FINANCIERA-UDAF',11140024,2017),(201,'ADMINISTRACION FINANCIERA-UDAF',11140026,2017),(201,'ADMINISTRACION FINANCIERA-UDAF',11140027,2017),(201,'ADMINISTRACION FINANCIERA-UDAF',11150023,2017),(201,'ADMINISTRACION FINANCIERA-UDAF',11150025,2017),(201,'UDAF - INFOM',11200054,2017),(201,'DIRECCIÓN DEPARTAMENTAL, ESCUINTLA',11400068,2017),(202,'REGIONAL QUETZALTENANGO',11120002,2017),(202,'VICEPRESIDENCIA DE LA REPUBLICA',11130003,2017),(202,'DIRECCION GENERAL DE INTELIGENCIA CIVIL',11130005,2017),(202,'DIRECCION DE RECURSOS HUMANOS',11130007,2017),(202,'DIRECCIÓN DE ÁREA DE SALUD GUATEMALA NOR ORIENTE',11130009,2017),(202,'INSTITUTO GEOGRÁFICO NACIONAL',11130012,2017),(202,'DIRECCION GENERAL DE CAMINOS',11130013,2017),(202,'DIRECCIÓN SUPERIOR',11130015,2017),(202,'COMISIÓN PRESIDENCIAL COORDINADORA DE LA POLÍTICA DEL EJECUTIVO EN MATERIA DE DERECHOS HUMANOS',11130016,2017),(202,'FONDO DE PROTECCIÓN SOCIAL',11130020,2017),(202,'PRESTAMO BIRF 7169',11200054,2017),(202,'HOSPITAL ESCUINTLA, ESCUINTLA',11400068,2017),(203,'SECRETARÍA DE ASUNTOS ADM.Y DE SEG.DE LA PRESIDENCIA',11130003,2017),(203,'DIRECCION GENERAL DE LA POLICIA NACIONAL CIVIL',11130005,2017),(203,'DIRECCION DE TECNOLOGIAS DE LA INFORMACION',11130007,2017),(203,'DIRECCIÓN DE ÁREA DE SALUD GUATEMALA NOR OCCIDENTE',11130009,2017),(203,'OFICINA DE CONTROL DE ÁREAS DE RESERVAS TERRITORIALES DEL ESTADO',11130012,2017),(203,'UNIDAD EJECUTORA DE CONSERVACION VIAL -COVIAL-',11130013,2017),(203,'CENTRO CULTURAL MIGUEL ANGEL ASTURIAS',11130015,2017),(203,'SECRETARÍA PRIVADA DE LA PRESIDENCIA',11130016,2017),(203,'FONDO DE DESARROLLO SOCIAL',11130020,2017),(203,'FOMENTO DEL SECTOR MUNICIPAL FSM I',11200054,2017),(203,'HOSPITAL SANTA LUCÍA COTZUMALGUAPA, ESCUINTLA',11400068,2017),(204,'DEPART.  DE ASUNTOS  ADMINISTR. DE LA PRESIDENCIA',11130003,2017),(204,'SECRETARIA DE ANALISIS E INFORMACION ANTINARCOTICA',11130005,2017),(204,'DIRECCION DE AUDITORIA INTERNA',11130007,2017),(204,'DIRECCIÓN DE ÁREA DE SALUD GUATEMALA SUR',11130009,2017),(204,'VICEMINISTERIO DE SEGURIDAD ALIMENTARIA Y NUTRICIONAL',11130012,2017),(204,'DIRECCION GENERAL DE TRANSPORTES',11130013,2017),(204,'EDITORIAL CULTURA',11130015,2017),(204,'SECRETARÍA DE COORDINACION  EJECUTIVA  DE LA PRESIDENCIA',11130016,2017),(204,'UNIDAD EJECUTORA DE PROGRAMAS Y PROYECTOS SOCIALES DEL MINISTERIO DE DESARROLLO SOCIAL',11130020,2017),(204,'PRESTAMO BIRF 4260',11200054,2017),(204,'HOSPITAL TIQUISATE, ESCUINTLA',11400068,2017),(205,'GUARDIA PRESIDENCIAL',11130003,2017),(205,'SUBDIRECCION GENERAL DE ESTUDIOS Y DOCTRINA DE LA POLICIA NACIONAL CIVIL',11130005,2017),(205,'DIRECCION DE ASUNTOS ADMINISTRATIVOS',11130007,2017),(205,'DIRECCIÓN DE ÁREA DE SALUD DE EL PROGRESO',11130009,2017),(205,'VICEMINISTERIO DE DESARROLLO ECONÓMICO Y RURAL',11130012,2017),(205,'DIRECCION GENERAL DE AERONAUTICA CIVIL',11130013,2017),(205,'ORQUESTA SINFONICA NACIONAL',11130015,2017),(205,'CONSEJOS DE DESARROLLO URBANO Y RURAL',11130016,2017),(205,'CONSULTORIO LA DEMOCRACIA, ESCUINTLA',11400068,2017),(206,'COMISION PRESIDENCIAL DE TRANSPARENCIA Y GOBIERNO ELECTRONICO',11130003,2017),(206,'SUBDIRECCION GENERAL DE SALUD POLICIAL',11130005,2017),(206,'DIRECCION DE ASESORIA JURIDICA',11130007,2017),(206,'DIRECCIÓN DE ÁREA DE SALUD DE SACATEPÉQUEZ',11130009,2017),(206,'UNIDAD DE CONSTRUCCION DE EDIFICIOS DEL ESTADO -UCEE-',11130013,2017),(206,'TEATRO DE BELLAS ARTES',11130015,2017),(206,'DESARROLLO INTEGRAL DE COMUNIDADES RURALES',11130016,2017),(206,'COORDINACION DONACION JAPONESA',11200054,2017),(206,'CONSULTORIO SIQUINALÁ, ESCUINTLA',11400068,2017),(207,'COMISIÓN PRESIDENCIAL DE DIÁLOGO',11130003,2017),(207,'SUBDIRECCION GENERAL DE INVESTIGACION CRIMINAL',11130005,2017),(207,'DIRECCION DE CATASTRO Y AVALUO DE BIENES INMUEBLES',11130007,2017),(207,'DIRECCION DE ÁREA DE SALUD DE CHIMALTENANGO',11130009,2017),(207,'PROGRAMA FIDA ORIENTE',11130012,2017),(207,'DIRECCION GENERAL DE RADIODIFUSION Y TELEVISION NACIONAL',11130013,2017),(207,'DIRECCIÓN GENERAL DE DEPORTE Y LA RECREACIÓN',11130015,2017),(207,'DIRECCION DE ASENTAMIENTOS HUMANOS Y VIVIENDA',11130016,2017),(207,'COORDINACION PROYECTO SAN BENITO',11200054,2017),(207,'CONSULTORIO DE MASAGUA ESCUINTLA',11400068,2017),(208,'SUBDIRECCION GENERAL DE PREVENCION DEL DELITO',11130005,2017),(208,'DIRECCION DE BIENES DEL ESTADO',11130007,2017),(208,'DIRECCIÓN DE ÁREA DE SALUD DE ESCUINTLA',11130009,2017),(208,'VICEMINISTERIO DE ASUNTOS DE PETÉN',11130012,2017),(208,'UNIDAD DE CONTROL Y SUPERVISION DE CABLE -UNCOSU-',11130013,2017),(208,'PARQUE NACIONAL TIKAL',11130015,2017),(208,'FONDO NACIONAL PARA LA PAZ',11130016,2017),(208,'DONACION REINO UNIDO ESPAÑOL',11200054,2017),(208,'CONSULTORIO LA GOMERA, ESCUINTLA',11400068,2017),(209,'DEPARTAMENTO DE TRANSITO',11130005,2017),(209,'DIRECCION TECNICA DEL PRESUPUESTO',11130007,2017),(209,'DIRECCIÓN DE ÁREA DE SALUD DE SANTA ROSA',11130009,2017),(209,'VICEMINISTERIO DE SANIDAD AGROPECUARIA Y REGULACIONES',11130012,2017),(209,'INSTITUTO NACIONAL DE SISMOLOGIA, VULCANOLOGIA, METEOROLOGIA E HIDROLOGIA -INSIVUMEH-',11130013,2017),(209,'SITIOS ARQUEOLOGICOS',11130015,2017),(209,'FONDO DE INVERSION SOCIAL',11130016,2017),(209,'PROGRAMA AGUA POTABLE Y SANEAMIENTO RURAL',11200054,2017),(209,'CONSULTORIO PUERTO DE SAN JOSÉ, ESCUINTLA',11400068,2017),(210,'DIRECCION GENERAL DEL SISTEMA PENITENCIARIO',11130005,2017),(210,'DIRECCION DE CONTABILIDAD DEL ESTADO',11130007,2017),(210,'DIRECCIÓN DE ÁREA DE SALUD DE SOLOLÁ',11130009,2017),(210,'DIRECCION GENERAL DE CORREOS Y TELEGRAFOS',11130013,2017),(210,'MUSEOS',11130015,2017),(210,'FONDO DE DESARROLLO INDIGENA GUATEMALTECO',11130016,2017),(210,'DONACION JAPONESA PARA REHABILITACIÓN DE PLANTAS DE TRATAMIENTO DE AGUA POTABLE',11200054,2017),(210,'CONSULTORIO EN PALIN, ESCUINTLA',11400068,2017),(211,'DIRECCION GENERAL DE MIGRACION',11130005,2017),(211,'TESORERIA NACIONAL',11130007,2017),(211,'DIRECCIÓN DE ÁREA DE SALUD DE TOTONICAPÁN',11130009,2017),(211,'SUPERINTENDENCIA DE TELECOMUNICACIONES -SIT-',11130013,2017),(211,'ARCHIVO GENERAL DE CENTROAMERICA',11130015,2017),(211,'SECRETARÍA DE COMUNICACIÓN SOCIAL DE LA PRESIDENCIA',11130016,2017),(211,'COORDINADORA PRESTAMO BID 1469 OC-GU',11200054,2017),(212,'DIRECCION GENERAL DEL DIARIO DE CENTRO AMERICA Y TIPOGRAFIA NACIONAL',11130005,2017),(212,'DIRECCION DE CREDITO PUBLICO',11130007,2017),(212,'DIRECCIÓN DE  ÁREA DE SALUD DE QUETZALTENANGO',11130009,2017),(212,'FONDO PARA EL DESARROLLO DE LA TELEFONIA -FONDETEL-',11130013,2017),(212,'BALLET NACIONAL DE GUATEMALA',11130015,2017),(212,'SECRETARÍA DE BIENESTAR SOCIAL DE LA PRESIDENCIA DE LA REP.',11130016,2017),(212,'COORDINADORA PRÉSTAMO JBIC GT-P5',11200054,2017),(213,'UNIDAD PARA LA PREVENCION COMUNITARIA DE LA VIOLENCIA',11130005,2017),(213,'DIRECCION NORMATIVA DE CONTRATACIONES Y ADQUISICIONES DEL ESTADO',11130007,2017),(213,'DIRECCIÓN DE ÁREA DE SALUD DE SUCHITEPÉQUEZ',11130009,2017),(213,'FONDO NACIONAL PARA LA REACTIVACION Y MODERNIZACION DE LA ACTIVIDAD AGROPECUARIA FONAGRO',11130012,2017),(213,'PALACIO NACIONAL DE LA CULTURA',11130015,2017),(213,'MEJORAMIENTO INSTITUCIONAL SECRETARÍAS',11130016,2017),(213,'PROYECTO UNION EUROPEA, DESARROLLO RURAL Y LOCAL',11200054,2017),(214,'REGISTRO DE PERSONAS JURIDICAS',11130005,2017),(214,'PROYECTO SIAF-SAG',11130007,2017),(214,'DIRECCIÓN DE ÁREA DE SALUD DE RETALHULEU',11130009,2017),(214,'UNIDAD PARA EL DESARROLLO DE VIVIENDA POPULAR -UDEVIPO-',11130013,2017),(214,'SECRETARÍA DE LA PAZ',11130016,2017),(214,'DONACION FORT. SOCIEDAD CIVIL EN GUATEMALA',11200054,2017),(215,'GOBERNACION DEPARTAMENTAL DE GUATEMALA',11130005,2017),(215,'TALLER NACIONAL DE GRABADOS EN ACERO',11130007,2017),(215,'DIRECCIÓN DE ÁREA DE SALUD DE SAN MARCOS',11130009,2017),(215,'INSTITUTO GEOGRAFICO NACIONAL -IGN-',11130013,2017),(215,'SECRETARÍA DE RECURSOS HIDRAULICOS',11130016,2017),(215,'DONACION EUROPEA DESCENTRALIZ. Y FORTAL. MUNICIPAL',11200054,2017),(216,'GOBERNACION DEPARTAMENTAL DE EL PROGRESO',11130005,2017),(216,'REGISTRO DE INGRESOS',11130007,2017),(216,'DIRECCIÓN DE ÁREA DE SALUD DE HUEHUETENANGO',11130009,2017),(216,'DIRECCION GENERAL DE PROTECCION Y SEGURIDAD VIAL -PROVIAL-',11130013,2017),(216,'OFICINA NACIONAL DE SERVICIO CIVIL',11130016,2017),(217,'GOBERNACION DEPARTAMENTAL DE SACATEPEQUEZ',11130005,2017),(217,'REGISTRO DE INGRESOS CREDITO PUBLICO',11130007,2017),(217,'DIRECCIÓN DE ÁREA DE SALUD DE QUICHÉ',11130009,2017),(217,'FONDO SOCIAL DE SOLIDARIDAD',11130013,2017),(217,'CONSEJO NACIONAL DE AREAS PROTEGIDAS',11130016,2017),(218,'GOBERNACION DEPARTAMENTAL DE CHIMALTENANGO',11130005,2017),(218,'DESPACHO MINISTERIAL Y VICEMINISTERIALES',11130007,2017),(218,'DIRECCIÓN DE ÁREA DE SALUD DE IXCÁN',11130009,2017),(218,'FONDO PARA LA VIVIENDA',11130013,2017),(218,'COMISION NACIONAL DEL MEDIO AMBIENTE',11130016,2017),(219,'GOBERNACION DEPARTAMENTAL DE ESCUINTLA',11130005,2017),(219,'COMUNICACIÓN SOCIAL',11130007,2017),(219,'DIRECCIÓN DE ÁREA DE SALUD DE BAJA VERAPAZ',11130009,2017),(219,'AUTORIDAD PARA EL MANEJO SUSTENTABLE DE LA CUENCA Y DEL LAGO DE AMATITLAN',11130016,2017),(220,'GOBERNACION DEPARTAMENTAL DE SANTA ROSA',11130005,2017),(220,'DIRECCION DE ANALISIS Y EVALUACION FISCAL',11130007,2017),(220,'DIRECCIÓN DE ÁREA DE SALUD DE ALTA VERAPAZ',11130009,2017),(220,'SECRETARÍA DE PLANIFICACION Y PROGRAMACION DE LA PRESIDENCIA',11130016,2017),(221,'GOBERNACION DEPARTAMENTAL DE SOLOLA',11130005,2017),(221,'DIRECCION DE TRANSPARENCIA FISCAL',11130007,2017),(221,'DIRECCIÓN DE ÁREA DE SALUD DE PETÉN NORTE',11130009,2017),(221,'CONSEJO NACIONAL DE LA JUVENTUD',11130016,2017),(222,'GOBERNACION DEPARTAMENTAL DE TOTONICAPAN',11130005,2017),(222,'DIRECCION DE FIDEICOMISOS',11130007,2017),(222,'DIRECCIÓN DE ÁREA DE SALUD DE IZABAL',11130009,2017),(222,'SECRETARÍA EJECUTIVA COMISION CONTRA LAS ADICCIONES Y EL TRAFICO ILICITO DE DROGAS',11130016,2017),(223,'GOBERNACION DEPARTAMENTAL DE QUETZALTENANGO',11130005,2017),(223,'DIR.DE ASISTENCIA A LA ADMON. FINANCIERA MUNICIPAL',11130007,2017),(223,'DIRECCIÓN DE ÁREA DE SALUD DE ZACAPA',11130009,2017),(223,'SECRETARÍA  NACIONAL DE CIENCIA Y TECNOLOGIA',11130016,2017),(224,'GOBERNACION DEPARTAMENTAL DE SUCHITEPEQUEZ',11130005,2017),(224,'SECRETARIA GENERAL',11130007,2017),(224,'DIRECCIÓN DE ÁREA DE SALUD DE CHIQUIMULA',11130009,2017),(224,'SECRETARÍA DE OBRAS SOCIALES DE LA ESPOSA DEL PRESIDENTE',11130016,2017),(225,'GOBERNACION DEPARTAMENTAL DE RETALHULEU',11130005,2017),(225,'DIRECCION DE EVALUACION FISCAL',11130007,2017),(225,'DIRECCIÓN DE ÁREA DE SALUD DE JALAPA',11130009,2017),(225,'SECRETARÍA DE ANALISIS ESTRATEGICO.',11130016,2017),(226,'GOBERNACION DEPARTAMENTAL DE SAN MARCOS',11130005,2017),(226,'DIRECCION DE ANALISIS Y POLITICA FISCAL',11130007,2017),(226,'DIRECCIÓN DE ÁREA DE SALUD DE JUTIAPA',11130009,2017),(226,'AUTORIDAD DEL LAGO DE IZABAL',11130016,2017),(227,'GOBERNACION DEPARTAMENTAL DE HUEHUETENANGO',11130005,2017),(227,'DIRECCIÓN DE PLANIFICACIÓN Y DESARROLLO INSTITUCIONAL',11130007,2017),(227,'HOSPITAL GENERAL SAN JUAN DE DIOS',11130009,2017),(227,'AUTORIDAD DEL LAGO DE AMATITLAN',11130016,2017),(228,'GOBERNACION DEPARTAMENTAL DE QUICHE',11130005,2017),(228,'HOSPITAL DE SALUD MENTAL DR. FEDERICO MORA',11130009,2017),(228,'SECRETARÍA DE DESARROLLO SOCIAL',11130016,2017),(229,'GOBERNACION DEPARTAMENTAL DE BAJA VERAPAZ',11130005,2017),(229,'HOSPITAL NACIONAL DE ORTOPEDIA Y REHABILITACION DR. JORGE VON AHN',11130009,2017),(229,'SECRETARÍA DEL MEDIO AMBIENTE RECURSOS NATURALES',11130016,2017),(230,'GOBERNACION DEPARTAMENTAL DE ALTA VERAPAZ',11130005,2017),(230,'HOSPITAL ROOSEVELT',11130009,2017),(230,'SECRETARÍA DE ASUNTOS PARTICULARES',11130016,2017),(231,'GOBERNACION DEPARTAMENTAL DE PETEN',11130005,2017),(231,'HOSPITAL INFANTIL DE INFECTOLOGÍA Y REHABILITACIÓN',11130009,2017),(231,'SECRETARÍA DE ASUNTOS ESPECIFICOS',11130016,2017),(232,'GOBERNACION DEPARTAMENTAL DE IZABAL',11130005,2017),(232,'HOSPITAL SAN VICENTE',11130009,2017),(232,'SECRETARÍA PRESIDENCIAL DE LA MUJER',11130016,2017),(233,'GOBERNACION DEPARTAMENTAL DE ZACAPA',11130005,2017),(233,'HOSPITAL NACIONAL DE AMATITLÁN',11130009,2017),(233,'SECRETARÍA DE ASUNTOS AGRARIOS DE LA PRESIDENCIA DE LA REP',11130016,2017),(234,'GOBERNACION DEPARTAMENTAL DE CHIQUIMULA',11130005,2017),(234,'HOSPITAL DE EL PROGRESO',11130009,2017),(234,'COMISION PRESIDENCIAL CONTRA LA DISCRIMINACION Y EL RACISMO CONTRA LOS PUEBLOS INDIGENAS',11130016,2017),(235,'GOBERNACION DEPARTAMENTAL DE JALAPA',11130005,2017),(235,'HOSPITAL PEDRO DE BETHANCOURT',11130009,2017),(235,'SECRETARÍA DE SEGURIDAD ALIMENTARIA Y NUTRICIONAL DE LA PRESIDENCIA DE LA REPUBLICA',11130016,2017),(236,'GOBERNACION DEPARTAMENTAL DE JUTIAPA',11130005,2017),(236,'HOGAR DE ANCIANOS FRAY RODRIGO DE LA CRUZ',11130009,2017),(236,'FONDO NACIONAL DE DESARROLLO',11130016,2017),(237,'HOSPITAL DE LA POLICIA NACIONAL CIVIL',11130005,2017),(237,'HOSPITAL NACIONAL DE CHIMALTENANGO',11130009,2017),(237,'AUTORIDAD PARA EL MANEJO SUSTENTABLE DE LA CUENCA DEL LAGO DE ATITLAN Y SU ENTORNO',11130016,2017),(238,'ACADEMIA DE LA POLICIA NACIONAL CIVIL',11130005,2017),(238,'HOSPITAL DE ESCUINTLA',11130009,2017),(238,'COMISIÓN PRESIDENCIAL PARA LA REFORMA, MODERNIZACIÓN Y FORTALECIMIENTO DEL ESTADO Y DE SUS ENTIDADES',11130016,2017),(239,'DIRECCIÓN GENERAL DE SERVICIOS DE SEGURIDAD PRIVADA',11130005,2017),(239,'HOSPITAL DE  TIQUISATE',11130009,2017),(239,'DEFENSORIA DE LA MUJER INDIGENA',11130016,2017),(240,'UNIDAD ESPECIAL ANTINARCOTICOS (UNESA)',11130005,2017),(240,'HOSPITAL REGIONAL DE CUILAPA SANTA ROSA',11130009,2017),(240,'FONDO NACIONAL DE DESARROLLO',11130016,2017),(241,'DIRECCION GENERAL DE INVESTIGACION CRIMINAL',11130005,2017),(241,'HOSPITAL DEPARTAMENTAL DE SOLOLÁ',11130009,2017),(241,'SECRETARIA DE INTELIGENCIA ESTRATÉGICA DEL ESTADO',11130016,2017),(242,'UNIDAD DEL NUEVO MODELO DE GESTION PENITENCIARIA - UNMGP -',11130005,2017),(242,'HOSPITAL DEPARTAMENTAL DE TOTONICAPAN',11130009,2017),(242,'SECRETARÍA TÉCNICA DEL CONSEJO NACIONAL DE SEGURIDAD',11130016,2017),(243,'HOSPITAL REGIONAL DE OCCIDENTE QUETZALTENANGO',11130009,2017),(243,'SECRETARIA CONTRA LA VIOLENCIA SEXUAL, EXPLOTACION Y TRATA DE PERSONAS',11130016,2017),(244,'HOSPITAL DE ESPECIALIDADES RODOLFO ROBLES',11130009,2017),(244,'SECRETARIA DE CONTROL Y TRANSPARENCIA',11130016,2017),(245,'HOSPITAL NACIONAL DE COATEPEQUE',11130009,2017),(246,'HOSPITAL DE MAZATENANGO',11130009,2017),(247,'HOSPITAL DE RETALHULEU',11130009,2017),(248,'HOSPITAL NACIONAL DE  SAN MARCOS DR. MOISES VILLAGRAN MAZARIEGOS',11130009,2017),(249,'HOSPITAL NACIONAL MALACATÁN SAN MARCOS',11130009,2017),(250,'HOSPITAL REGIONAL DE HUEHUETENANGO DR. JORGE VIDES MOLINA',11130009,2017),(251,'HOSPITAL NACIONAL DE SAN PEDRO NECTA',11130009,2017),(252,'HOSPITAL REGIONAL DE EL QUICHÉ',11130009,2017),(253,'HOSPITAL NACIONAL DE SALAMÁ',11130009,2017),(254,'HOSPITAL DE COBAN',11130009,2017),(255,'HOSPITAL DE SAN BENITO',11130009,2017),(256,'HOSPITAL DE  MELCHOR DE MENCOS',11130009,2017),(257,'HOSPITAL DISTRITAL SAYAXCHÉ PETÉN',11130009,2017),(258,'HOSPITAL DE POPTÚN',11130009,2017),(259,'HOSPITAL DE LA AMISTAD JAPÓN GUATEMALA',11130009,2017),(260,'HOSPITAL NACIONAL INFANTIL ELISA MARTÍNEZ, PUERTO BARRIOS, IZABAL',11130009,2017),(261,'HOSPITAL REGIONAL DE  ZACAPA',11130009,2017),(262,'HOSPITAL DE CHIQUIMULA',11130009,2017),(263,'HOSPITAL NACIONAL NICOLASA CRUZ JALAPA',11130009,2017),(264,'HOSPITAL NACIONAL ERNESTINA GARCIA VDA. DE RECINOS',11130009,2017),(265,'DIRECCIÓN DE ÁREA DE SALUD DE PETÉN SUR OCCIDENTE',11130009,2017),(266,'DIRECCIÓN DE ÁREA DE SALUD DE PETEN SURORIENTE',11130009,2017),(267,'HOSPITAL DE JOYABAJ',11130009,2017),(268,'HOSPITAL DE NEBAJ',11130009,2017),(269,'HOSPITAL DE USPANTAN',11130009,2017),(270,'HOSPITAL FRAY BARTOLOME DE LAS CASAS',11130009,2017),(271,'HOSPITAL DE LA TINTA',11130009,2017),(272,'ESCUELA NACIONAL DE ENFERMERAS',11130009,2017),(273,'ESC. NAC. DE ENFERMERIA DE COBAN E INST. DE ADIESTRAMIENTO PARA PERSONAL DE SALUD DE LAS VERAPACES',11130009,2017),(274,'ESCUELA NACIONAL DE ENFERMERÍA DE OCCIDENTE',11130009,2017),(275,'ESCUELA DE AUXILIARES DE ENFERMERIA DE SUR OCCIDENTE',11130009,2017),(276,'SCUELA PARA AUXILIARES DE ENFERMERIA DE ORIENTE',11130009,2017),(277,'INSTITUTO DE ADIESTRAMIENTO DE PERSONAL EN SALUD INDAPS',11130009,2017),(278,'DIRECCIÓN DE ÁREA DE SALUD GUATEMALA CENTRAL',11130009,2017),(279,'PROGRAMA DE ACCESIBILIDAD DE MEDICAMENTOS PROAM',11130009,2017),(280,'LABORATORIO NACIONAL DE SALUD',11130009,2017),(281,'CEMENTERIO NACIONAL',11130009,2017),(282,'UNIDAD EJECUTORA DE PROYECTOS DE INFRAESTRUCTURA DE SALUD',11130009,2017),(283,'DIRECCIÓN DE ÁREA DE SALUD IXIL',11130009,2017),(284,'HOSPITAL DE BARILLAS',11130009,2017),(285,'HOSPITAL GENERAL DE VILLA NUEVA',11130009,2017),(300,'GERENCIA REGIONAL SUR - SAT',11200056,2017),(301,'DIRECCION DEPARTAMENTAL DE EDUCACION DE GUATEMALA',11130008,2017),(301,'FONDO NACIONAL UNEPAR',11200054,2017),(301,'HOSPITAL POCHUTA, CHIMALTENANGO',11400068,2017),(302,'DIRECCION DEPARTAMENTAL DE EDUCACION DE EL PROGRESO',11130008,2017),(302,'COORDINACION SANEAMIENTO BID PRESTAMO BID 836',11200054,2017),(302,'CONSULTORIO SAN LUCAS TOLIMÁN,SOLOLÁ',11400068,2017),(303,'DIRECCION DEPARTAMENTAL DE EDUCACION DE SACATEPEQUEZ',11130008,2017),(303,'DONACION KFW IV',11200054,2017),(303,'HOSPITAL COATEPEQUE, QUETZALTENANGO',11400068,2017),(304,'DIRECCION DEPARTAMENTAL EDUCACION DE CHIMALTENANGO',11130008,2017),(304,'COORDINDORA AGUAS SUBTERRANEAS EN AREAS RURALES',11200054,2017),(304,'DIRECCIÓN DEPARTAMENTAL, SUCHITEPÉQUEZ',11400068,2017),(305,'DIRECCION DEPARTAMENTAL DE EDUCACION DE ESCUINTLA',11130008,2017),(305,'AGUA Y SANEAMIENTO PARA SEGURIDAD ALIMENTARIA',11200054,2017),(305,'HOSPITAL DE MAZATENANGO, SUCHITEPÉQUEZ',11400068,2017),(306,'DIRECCION DEPARTAMENTAL DE EDUCACION DE SANTA ROSA',11130008,2017),(306,'AGUA POTABLE Y SANEAMIENTO PARA EL DESARROLLO HUMANO',11200054,2017),(306,'HOSPITAL CHICACAO, SUCHITEPÉQUEZ',11400068,2017),(307,'DIRECCION DEPARTAMENTAL DE EDUCACION DE SOLOLA',11130008,2017),(307,'HOSPITAL PATULUL, SUCHITEPÉQUEZ',11400068,2017),(308,'DIRECCION DEPARTAMENTAL DE EDUCACION DE TOTONICAPAN',11130008,2017),(308,'DIRECCIÓN DEPARTAMENTAL, RETALHULEU',11400068,2017),(309,'DIRECCION DEPARTAMENTAL DE EDUCACION DE QUETZALTENANGO',11130008,2017),(309,'HOSPITAL RETALHULEU, RETALHULEU',11400068,2017),(310,'DIRECCION DEPARTAMENTAL DE EDUCACION DE SUCHITEPEQUEZ',11130008,2017),(310,'CONSULTORIO PUERTO CHAMPERICO, RETALHULEU',11400068,2017),(311,'DIRECCION DEPARTAMENTAL DE EDUCACION DE RETALHULEU',11130008,2017),(311,'PRESTAMO KFW I',11200054,2017),(311,'CONSULTORIO SAN FELIPE, RETALHULEU',11400068,2017),(312,'DIRECCION DEPARTAMENTAL DE EDUCACION DE SAN MARCOS',11130008,2017),(312,'DONACION KFW II',11200054,2017),(312,'HOSPITAL MALACATÁN, SAN MARCOS',11400068,2017),(313,'DIRECCION DEPARTAMENTAL DE EDUCACION DE HUEHUETENANGO',11130008,2017),(313,'HOSPITAL EL TUMBADOR, SAN MARCOS',11400068,2017),(314,'DIRECCION DEPARTAMENTAL DE EDUCACION DE EL QUICHE',11130008,2017),(314,'CONSULTORIO TECÚN UMÁN, SAN MARCOS',11400068,2017),(315,'DIRECCCION DEPARTAMENTAL DE EDUCACION DE BAJA VERAPAZ',11130008,2017),(316,'DIRECCION DEPARTAMENTAL DE EDUCACION DE ALTA VERAPAZ',11130008,2017),(317,'DIRECCION DEPARTAMENTAL DE EDUCACION DE PETEN',11130008,2017),(318,'DIRECCION DEPARTAMENTAL DE EDUCACION DE IZABAL',11130008,2017),(319,'DIRECCION DEPARTAMENTAL DE EDUCACION DE ZACAPA',11130008,2017),(320,'DIRECCION DEPARTAMENTAL DE EDUCACION DE CHIQUIMULA',11130008,2017),(321,'DIRECCION DEPARTAMENTAL DE EDUCACION DE JALAPA',11130008,2017),(322,'DIRECCION DEPARTAMENTAL DE EDUCACION DE JUTIAPA',11130008,2017),(323,'DIRECCION DEPARTAMENTAL DE EDUCACION GUATEMALA NORTE',11130008,2017),(324,'DIRECCION DEPARTAMENTAL DE EDUCACION GUATEMALA SUR',11130008,2017),(325,'DIRECCION DEPARTAMENTAL DE EDUCACION GUATEMALA ORIENTE',11130008,2017),(326,'DIRECCION DEPARTAMENTAL DE EDUCACION GUATEMALA OCCIDENTE',11130008,2017),(327,'DIRECCION DEPARTAMENTAL DE EDUCACION PETEN CENTRAL',11130008,2017),(328,'DIRECCION DEPARTAMENTAL DE EDUCACION PETEN ORIENTE',11130008,2017),(329,'DIRECCION DEPARTAMENTAL DE EDUCACION PETEN OCCIDENTE',11130008,2017),(330,'DIRECCION DEPARTAMENTAL DE EDUCACION QUICHE NORTE',11130008,2017),(400,'DONACION KFW III',11200054,2017),(400,'GERENCIA REGIONAL OCCIDENTE - SAT',11200056,2017),(401,'CONSULTORIO SOLOLÁ Y SALA ANEXA HOSPITAL NACIONAL',11400068,2017),(402,'CONSULTORIO TOTONICAPÁN, TOTONICAPÁN',11400068,2017),(403,'DIRECCIÓN DEPARTAMENTAL, QUETZALTENANGO',11400068,2017),(404,'HOSPITAL GENERAL DE QUETZALTENANGO',11400068,2017),(405,'HOSPITAL COLOMBA, QUETZALTENANGO',11400068,2017),(406,'DIRECCIÓN DEPARTAMENTAL, SAN MARCOS',11400068,2017),(407,'CONSULTORIO SAN MARCOS, SAN MARCOS',11400068,2017),(408,'DIRECCIÓN DEPARTAMENTAL, HUEHUETENANGO',11400068,2017),(409,'HOSPITAL HUEHUETENANGO, HUEHUETENANGO',11400068,2017),(410,'CONSULTORIO SANTA CRUZ DEL QUICHÉ, QUICHÉ',11400068,2017),(411,'CONSULTORIO SAN JUAN COTZAL, QUICHÉ',11400068,2017),(500,'GERENCIA REGIONAL NORORIENTE - SAT',11200056,2017),(501,'FONDOS PL 480-91',11200054,2017),(501,'CONSULTORIO SALAMA, BAJA VERAPAZ',11400068,2017),(502,'FONDOS PL 480-92',11200054,2017),(502,'DIRECCIÓN DEPARTAMENTAL, BAJA VERAPAZ',11400068,2017),(503,'PRÉSTAMO BCIE 36',11200054,2017),(503,'DIRECCIÓN DEPARTAMENTAL, ALTA VERAPAZ',11400068,2017),(504,'PRÉSTAMO BCIE 37',11200054,2017),(504,'HOSPITAL COBÁN, ALTA VERAPAZ',11400068,2017),(505,'PRÉSTAMO AID 520 L 017',11200054,2017),(505,'SALA ANEXA HOSPITAL NACIONAL SAN BENITO PETÉN, PETÉN',11400068,2017),(506,'DIRECCIÓN DEPARTAMENTAL,  IZABAL',11400068,2017),(507,'FONDO NACIONAL PAYSA',11200054,2017),(507,'HOSPITAL PUERTO BARRIOS, IZABAL',11400068,2017),(508,'DONACION AID 520 03-39',11200054,2017),(508,'CONSULTORIO EL ESTOR, IZABAL',11400068,2017),(509,'CONSULTORIO MORALES, IZABAL',11400068,2017),(510,'CONSULTORIO LOS AMATES, IZABAL',11400068,2017),(511,'DIRECCIÓN DEPARTAMENTAL, ZACAPA',11400068,2017),(512,'CONSULTORIO ZACAPA, ZACAPA',11400068,2017),(513,'CONSULTORIO GUALÁN, ZACAPA',11400068,2017),(514,'CONSULTORIO CHIQUIMULA, CHIQUIMULA',11400068,2017),(600,'CONTRIBUYENTES ESPECIALES GRANDES',11200056,2017),(700,'CONTRIBUYENTES ESPECIALES MEDIANOS',11200056,2017),(999,'PREVISION PAGO DE BONO Y REGULARIZACION NOMINA',11130016,2017);
/*!40000 ALTER TABLE `unidad_ejecutora` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `unidad_medida`
--

DROP TABLE IF EXISTS `unidad_medida`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `unidad_medida` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) COLLATE utf8_bin NOT NULL,
  `descripcion` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_actualizacion` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  `estado` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `unidad_medida`
--

LOCK TABLES `unidad_medida` WRITE;
/*!40000 ALTER TABLE `unidad_medida` DISABLE KEYS */;
INSERT INTO `unidad_medida` VALUES (1,'Casas',NULL,'admin',NULL,'2017-07-13 21:50:04',NULL,1),(2,'Kilómetros',NULL,'admin',NULL,'2017-07-13 21:50:04',NULL,1),(3,'Capacitaciones',NULL,'admin',NULL,'2017-07-13 21:50:04',NULL,1);
/*!40000 ALTER TABLE `unidad_medida` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER tr_unidad_medida_insert
AFTER INSERT ON sipro.unidad_medida FOR EACH ROW
BEGIN
	DECLARE v_version int;
	SELECT max(a.version) INTO v_version FROM sipro_history.unidad_medida a WHERE a.id=NEW.id;

    IF(v_version is null) THEN
		SET v_version=1;
	END IF;

    INSERT INTO sipro_history.unidad_medida VALUE(NEW.id,NEW.nombre,NEW.descripcion,NEW.usuario_creo,NEW.usuario_actualizacion,NEW.fecha_creacion,NEW.fecha_actualizacion,NEW.estado, v_version, NULL, 1);

END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER tr_unidad_medida_update
AFTER UPDATE ON sipro.unidad_medida FOR EACH ROW
BEGIN
		DECLARE v_version int;
		SELECT max(a.version) INTO v_version FROM sipro_history.unidad_medida a WHERE a.id=OLD.id;

		IF(v_version is null) THEN
            UPDATE sipro_history.unidad_medida SET actual=null WHERE id=OLD.id AND version is null;
            SET v_version=1;
		ELSE
            UPDATE sipro_history.unidad_medida SET actual=null WHERE id=OLD.id AND version=v_version;
            SET v_version=v_version+1;
        END IF;

        INSERT INTO sipro_history.unidad_medida VALUE(NEW.id,NEW.nombre,NEW.descripcion,NEW.usuario_creo,NEW.usuario_actualizacion,NEW.fecha_creacion,NEW.fecha_actualizacion,NEW.estado, v_version, NULL, 1);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER tr_unidad_medida_delete
BEFORE DELETE ON sipro.unidad_medida FOR EACH ROW
BEGIN
	DECLARE v_version int;
	SELECT max(a.version) INTO v_version FROM sipro_history.unidad_medida a WHERE a.id=OLD.id;

	IF(v_version is null) THEN
		SET v_version=1;
	ELSE
		UPDATE sipro_history.unidad_medida SET actual=null WHERE id=OLD.id AND version=v_version;
		SET v_version=v_version+1;
	END IF;

	INSERT INTO sipro_history.unidad_medida VALUE(OLD.id,OLD.nombre,OLD.descripcion,OLD.usuario_creo,OLD.usuario_actualizacion,OLD.fecha_creacion,OLD.fecha_actualizacion,OLD.estado, v_version, NULL, NULL);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `usuario` (
  `usuario` varchar(30) COLLATE utf8_bin NOT NULL,
  `password` varchar(255) COLLATE utf8_bin NOT NULL,
  `salt` varchar(255) COLLATE utf8_bin NOT NULL,
  `email` varchar(255) COLLATE utf8_bin NOT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  `estado` int(2) NOT NULL,
  `sistema_usuario` int(2) NOT NULL DEFAULT 3,
  PRIMARY KEY (`usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` VALUES ('admin','57kldOQR8YQxSpOwbNOIe7hlV6k2ewpEM++Fvy6YZHc=','J3iGT5W6I0uZtqu2vlfWHg==','admin@minfin.com','admin','admin','2017-01-20 09:25:39','2017-10-09 10:53:46',1,1),('dcp','h2fCydRVakd+/0ImYNd5hFtCZnjOJP6IWDL2B7jFEWI=','9vr5cDQ/cqXQTvGHBDbWTA==','dcp@minfin.gob.gt','eflores','admin','2017-10-06 03:02:22','2017-10-19 05:37:03',1,1),('djlopez','HR4Xs/hRvZ+q+WZBuKexdFWVyBGWa8DxFPwMjqQ8H+s=','eN4lMUTMoblFDwyJgJoi5g==','djlopez@minfin.gob.gt','eflores','admin','2017-10-06 02:40:23','2017-10-09 07:19:27',1,1),('eflores','Kpi9EgKbYEKtbNtOJrUJMCdjkAiaO8q59kc6tEH2cmk=','WZVDVmk5/Ne57GOqB3xr4A==','eflores@minfin.gob.gt','admin','admin','2017-10-02 20:33:34','2017-10-09 11:18:38',1,1),('eorozco','jTrz36Mu/CzTvkQwcyIox2kCUHZ/YAYGrQjhOERmn4g=','q0OAZXCnbEFAXfacPjHVVQ==','eorozco2@minfin.gob.gt','eflores',NULL,'2017-10-06 02:51:46',NULL,1,1),('hperez','vzUzYQT4zo5nEPcS20Vukd33Ig6KBUD7Jzk7/p+X8Nc=','w/2doRd45PHsZBTCNHyDrw==','hperez@minfin.gob.gt','eflores',NULL,'2017-10-06 02:53:23',NULL,1,1),('rossy','Xkd205rGF74rbBG/azy93T6rWpqMl87F9dqQZ/uEs1c=','0uk3CiQIUnTeaTV9ZYTELg==','rossy@minfin.gob.gt','admin','admin','2017-10-10 02:07:20','2017-10-10 06:06:57',1,2),('uejecutora','2bCC02UP7cYNMKvyW1PxTTlziXS/ySOA5+o5WKrs4b8=','kaXlxAIq7BdqR4IpWcM1PQ==','unidadejecutora@minfin.gob.gt','eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0,1);
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario_log`
--

DROP TABLE IF EXISTS `usuario_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `usuario_log` (
  `usuario` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario_log`
--

LOCK TABLES `usuario_log` WRITE;
/*!40000 ALTER TABLE `usuario_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `usuario_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario_permiso`
--

DROP TABLE IF EXISTS `usuario_permiso`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `usuario_permiso` (
  `usuariousuario` varchar(30) COLLATE utf8_bin NOT NULL,
  `permisoid` int(10) NOT NULL,
  `usuario_creo` varchar(30) COLLATE utf8_bin NOT NULL,
  `usuario_actualizo` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL,
  `estado` int(2) NOT NULL,
  PRIMARY KEY (`usuariousuario`,`permisoid`),
  KEY `FKusuario_pe101130` (`permisoid`),
  KEY `FKusuario_pe242169` (`usuariousuario`),
  CONSTRAINT `FKusuario_pe101130` FOREIGN KEY (`permisoid`) REFERENCES `permiso` (`id`),
  CONSTRAINT `FKusuario_pe242169` FOREIGN KEY (`usuariousuario`) REFERENCES `usuario` (`usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario_permiso`
--

LOCK TABLES `usuario_permiso` WRITE;
/*!40000 ALTER TABLE `usuario_permiso` DISABLE KEYS */;
INSERT INTO `usuario_permiso` VALUES ('admin',1010,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',1020,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',1030,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',1040,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',2010,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',2020,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',2030,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',2040,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',3010,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',3020,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',3030,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',3040,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',4010,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',4020,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',4030,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',4040,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',5010,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',5020,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',5030,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',5040,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',6010,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',6020,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',6030,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',6040,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',7010,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',7020,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',7030,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',7040,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',8010,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',8020,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',8030,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',8040,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',9010,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',9020,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',9030,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',9040,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',10010,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',10020,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',10030,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',10040,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',15010,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',15020,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',15030,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',15040,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',16010,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',16020,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',16030,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',16040,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',17010,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',17020,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',17030,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',17040,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',18010,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',18020,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',18030,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',18040,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',19010,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',19020,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',19030,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',19040,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',20010,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',20020,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',20030,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',20040,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',21010,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',21020,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',21030,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',21040,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',22010,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',22020,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',22030,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',22040,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',23010,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',23020,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',23030,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',23040,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',24010,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',24020,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',24030,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',24040,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',25010,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',25020,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',25030,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',25040,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',30010,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',30020,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',30030,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',30040,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',31010,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',31020,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',31030,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',31040,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',32010,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',32020,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',32030,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',32040,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',33010,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',33020,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',33030,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',33040,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',34010,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',34020,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',34030,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',34040,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',35010,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',35020,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',35030,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',35040,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',36010,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',36020,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',36030,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',36040,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',37010,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',37020,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',37030,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',37040,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',38010,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',38020,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',38030,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',38040,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',39010,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',39020,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',39030,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',39040,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',40010,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',40020,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',40030,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',40040,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',41010,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',41020,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',41030,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',41040,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',42010,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',42020,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',42030,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',42040,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',43010,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',44010,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('admin',45010,'admin',NULL,'2017-10-06 02:57:13',NULL,1),('admin',45020,'admin',NULL,'2017-10-06 02:57:13',NULL,1),('admin',45030,'admin',NULL,'2017-10-06 02:57:13',NULL,1),('admin',45040,'admin',NULL,'2017-10-06 02:57:13',NULL,1),('admin',46010,'admin',NULL,'2017-10-06 02:57:13',NULL,1),('admin',46020,'admin',NULL,'2017-10-06 02:57:13',NULL,1),('admin',46030,'admin',NULL,'2017-10-06 02:57:13',NULL,1),('admin',46040,'admin',NULL,'2017-10-06 02:57:13',NULL,1),('admin',99999,'admin',NULL,'2017-10-09 10:35:21',NULL,1),('dcp',1010,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',1020,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',1030,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',1040,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',2010,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',2020,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',2030,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',2040,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',3010,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',3020,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',3030,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',3040,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',4010,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',4020,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',4030,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',4040,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',5010,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',5020,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',5030,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',5040,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',6010,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',6020,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',6030,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',6040,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',7010,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',7020,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',7030,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',7040,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',8010,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',8020,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',8030,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',8040,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',9010,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',9020,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',9030,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',9040,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',10010,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',10020,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',10030,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',10040,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',15010,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',15020,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',15030,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',15040,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',16010,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',16020,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',16030,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',16040,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',17010,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',17020,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',17030,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',17040,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',18010,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',18020,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',18030,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',18040,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',19010,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',19020,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',19030,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',19040,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',20010,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',20020,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',20030,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',20040,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',21010,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',21020,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',21030,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',21040,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',22010,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',22020,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',22030,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',22040,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',23010,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',23020,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',23030,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',23040,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',24010,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',24020,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',24030,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',24040,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',25010,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',25020,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',25030,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',25040,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',30010,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',30020,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',30030,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',30040,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',31010,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',31020,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',31030,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',31040,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',32010,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',32020,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',32030,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',32040,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',33010,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',33020,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',33030,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',33040,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',35010,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',35020,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',35030,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',35040,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',36010,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',36020,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',36030,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',36040,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',37010,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',37020,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',37030,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',37040,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',38010,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',38020,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',38030,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',38040,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',39010,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',39020,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',39030,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',39040,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',40010,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',40020,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',40030,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',40040,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',41010,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',41020,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',41030,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',41040,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',42010,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',42020,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',42030,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',42040,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',43010,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('dcp',44010,'admin',NULL,'2017-10-19 05:37:03',NULL,1),('djlopez',1010,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',1020,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',1030,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',1040,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',2010,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',2020,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',2030,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',2040,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',3010,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',3020,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',3030,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',3040,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',4010,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',4020,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',4030,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',4040,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',5010,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',5020,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',5030,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',5040,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',6010,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',6020,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',6030,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',6040,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',7010,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',7020,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',7030,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',7040,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',8010,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',8020,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',8030,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',8040,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',9010,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',9020,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',9030,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',9040,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',10010,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',10020,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',10030,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',10040,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',15010,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',15020,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',15030,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',15040,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',16010,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',16020,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',16030,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',16040,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',17010,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',17020,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',17030,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',17040,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',18010,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',18020,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',18030,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',18040,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',19010,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',19020,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',19030,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',19040,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',20010,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',20020,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',20030,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',20040,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',21010,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',21020,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',21030,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',21040,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',22010,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',22020,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',22030,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',22040,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',23010,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',23020,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',23030,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',23040,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',24010,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',24020,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',24030,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',24040,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',25010,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',25020,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',25030,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',25040,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',30010,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',30020,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',30030,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',30040,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',31010,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',31020,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',31030,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',31040,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',32010,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',32020,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',32030,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',32040,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',33010,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',33020,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',33030,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',33040,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',35010,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',35020,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',35030,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',35040,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',36010,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',36020,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',36030,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',36040,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',37010,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',37020,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',37030,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',37040,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',38010,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',38020,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',38030,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',38040,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',39010,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',39020,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',39030,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',39040,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',40010,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',40020,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',40030,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',40040,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',41010,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',41020,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',41030,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',41040,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',42010,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',42020,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',42030,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',42040,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',43010,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('djlopez',99999,'admin',NULL,'2017-10-09 07:19:27',NULL,1),('eflores',1010,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',1020,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',1030,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',1040,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',2010,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',2020,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',2030,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',2040,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',3010,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',3020,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',3030,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',3040,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',4010,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',4020,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',4030,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',4040,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',5010,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',5020,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',5030,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',5040,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',6010,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',6020,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',6030,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',6040,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',7010,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',7020,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',7030,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',7040,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',8010,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',8020,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',8030,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',8040,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',9010,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',9020,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',9030,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',9040,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',10010,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',10020,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',10030,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',10040,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',15010,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',15020,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',15030,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',15040,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',16010,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',16020,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',16030,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',16040,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',17010,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',17020,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',17030,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',17040,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',18010,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',18020,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',18030,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',18040,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',19010,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',19020,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',19030,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',19040,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',20010,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',20020,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',20030,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',20040,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',21010,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',21020,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',21030,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',21040,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',22010,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',22020,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',22030,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',22040,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',23010,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',23020,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',23030,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',23040,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',24010,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',24020,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',24030,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',24040,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',25010,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',25020,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',25030,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',25040,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',30010,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',30020,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',30030,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',30040,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',31010,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',31020,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',31030,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',31040,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',32010,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',32020,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',32030,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',32040,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',33010,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',33020,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',33030,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',33040,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',35010,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',35020,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',35030,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',35040,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',36010,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',36020,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',36030,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',36040,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',37010,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',37020,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',37030,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',37040,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',38010,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',38020,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',38030,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',38040,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',39010,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',39020,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',39030,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',39040,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',40010,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',40020,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',40030,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',40040,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',41010,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',41020,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',41030,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',41040,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',42010,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',42020,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',42030,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',42040,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',43010,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',44010,'admin',NULL,'2017-10-09 11:18:38',NULL,1),('eflores',45010,'admin',NULL,'2017-10-06 02:57:13',NULL,1),('eflores',45020,'admin',NULL,'2017-10-06 02:57:13',NULL,1),('eflores',45030,'admin',NULL,'2017-10-06 02:57:13',NULL,1),('eflores',45040,'admin',NULL,'2017-10-06 02:57:13',NULL,1),('eflores',46010,'admin',NULL,'2017-10-06 02:57:13',NULL,1),('eflores',46020,'admin',NULL,'2017-10-06 02:57:13',NULL,1),('eflores',46030,'admin',NULL,'2017-10-06 02:57:13',NULL,1),('eflores',46040,'admin',NULL,'2017-10-06 02:57:13',NULL,1),('eorozco',1010,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',1020,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',1030,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',1040,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',2010,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',2020,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',2030,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',2040,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',3010,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',3020,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',3030,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',3040,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',4010,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',4020,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',4030,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',4040,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',5010,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',5020,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',5030,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',5040,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',6010,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',6020,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',6030,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',6040,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',7010,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',7020,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',7030,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',7040,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',8010,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',8020,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',8030,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',8040,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',9010,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',9020,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',9030,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',9040,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',10010,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',10020,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',10030,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',10040,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',15010,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',15020,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',15030,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',15040,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',16010,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',16020,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',16030,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',16040,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',17010,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',17020,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',17030,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',17040,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',18010,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',18020,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',18030,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',18040,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',19010,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',19020,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',19030,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',19040,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',21010,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',21020,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',21030,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',21040,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',22010,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',22020,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',22030,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',22040,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',23010,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',23020,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',23030,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',23040,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',24010,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',24020,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',24030,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',24040,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',25010,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',25020,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',25030,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',25040,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',30010,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',30020,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',30030,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',30040,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',31010,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',31020,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',31030,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',31040,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',32010,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',32020,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',32030,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',32040,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',33010,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',33020,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',33030,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',33040,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',35010,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',35020,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',35030,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',35040,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',36010,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',36020,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',36030,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',36040,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',37010,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',37020,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',37030,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',37040,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',38010,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',38020,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',38030,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',38040,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',39010,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',39020,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',39030,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',39040,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',40010,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',40020,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',40030,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',40040,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',41010,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',41020,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',41030,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',41040,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',42010,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',42020,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',42030,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('eorozco',42040,'eflores',NULL,'2017-10-06 02:51:46',NULL,1),('hperez',1010,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',1020,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',1030,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',1040,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',2010,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',2020,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',2030,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',2040,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',3010,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',3020,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',3030,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',3040,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',4010,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',4020,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',4030,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',4040,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',5010,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',5020,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',5030,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',5040,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',6010,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',6020,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',6030,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',6040,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',7010,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',7020,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',7030,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',7040,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',8010,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',8020,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',8030,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',8040,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',9010,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',9020,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',9030,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',9040,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',10010,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',10020,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',10030,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',10040,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',15010,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',15020,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',15030,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',15040,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',16010,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',16020,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',16030,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',16040,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',17010,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',17020,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',17030,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',17040,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',18010,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',18020,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',18030,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',18040,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',19010,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',19020,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',19030,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',19040,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',21010,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',21020,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',21030,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',21040,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',22010,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',22020,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',22030,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',22040,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',23010,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',23020,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',23030,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',23040,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',24010,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',24020,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',24030,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',24040,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',25010,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',25020,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',25030,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',25040,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',30010,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',30020,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',30030,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',30040,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',31010,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',31020,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',31030,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',31040,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',32010,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',32020,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',32030,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',32040,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',33010,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',33020,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',33030,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',33040,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',35010,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',35020,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',35030,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',35040,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',36010,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',36020,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',36030,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',36040,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',37010,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',37020,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',37030,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',37040,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',38010,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',38020,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',38030,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',38040,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',39010,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',39020,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',39030,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',39040,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',40010,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',40020,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',40030,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',40040,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',41010,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',41020,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',41030,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',41040,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',42010,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',42020,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',42030,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('hperez',42040,'eflores',NULL,'2017-10-06 02:53:23',NULL,1),('rossy',1010,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',1020,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',1030,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',1040,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',2010,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',2020,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',2030,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',2040,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',3010,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',3020,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',3030,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',3040,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',4010,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',4020,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',4030,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',4040,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',5010,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',5020,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',5030,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',5040,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',6010,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',6020,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',6030,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',6040,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',7010,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',7020,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',7030,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',7040,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',8010,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',8020,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',8030,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',8040,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',9010,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',9020,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',9030,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',9040,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',10010,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',10020,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',10030,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',10040,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',15010,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',15020,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',15030,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',15040,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',16010,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',16020,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',16030,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',16040,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',17010,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',17020,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',17030,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',17040,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',18010,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',18020,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',18030,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',18040,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',19010,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',19020,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',19030,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',19040,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',20010,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',20020,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',20030,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',20040,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',21010,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',21020,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',21030,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',21040,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',22010,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',22020,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',22030,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',22040,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',23010,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',23020,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',23030,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',23040,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',24010,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',24020,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',24030,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',24040,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',25010,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',25020,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',25030,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',25040,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',30010,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',30020,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',30030,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',30040,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',31010,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',31020,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',31030,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',31040,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',32010,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',32020,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',32030,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',32040,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',33010,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',33020,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',33030,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',33040,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',35010,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',35020,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',35030,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',35040,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',36010,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',36020,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',36030,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',36040,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',37010,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',37020,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',37030,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',37040,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',38010,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',38020,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',38030,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',38040,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',39010,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',39020,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',39030,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',39040,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',40010,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',40020,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',40030,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',40040,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',41010,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',41020,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',41030,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',41040,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',42010,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',42020,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',42030,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',42040,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('rossy',44010,'admin',NULL,'2017-10-10 06:06:57',NULL,1),('uejecutora',1010,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',1020,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',1030,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',1040,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',2010,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',2020,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',2030,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',2040,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',3010,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',3020,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',3030,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',3040,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',4010,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',4020,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',4030,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',4040,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',5010,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',5020,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',5030,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',5040,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',6010,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',6020,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',6030,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',6040,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',7010,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',7020,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',7030,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',7040,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',8010,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',8020,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',8030,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',8040,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',9010,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',9020,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',9030,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',9040,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',10010,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',10020,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',10030,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',10040,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',15010,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',15020,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',15030,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',15040,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',16010,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',16020,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',16030,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',16040,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',17010,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',17020,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',17030,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',17040,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',18010,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',18020,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',18030,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',18040,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',19010,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',19020,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',19030,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',19040,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',21010,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',21020,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',21030,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',21040,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',22010,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',22020,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',22030,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',22040,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',23010,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',23020,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',23030,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',23040,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',24010,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',24020,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',24030,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',24040,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',25010,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',25020,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',25030,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',25040,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',30010,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',30020,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',30030,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',30040,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',31010,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',31020,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',31030,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',31040,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',32010,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',32020,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',32030,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',32040,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',33010,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',33020,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',33030,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',33040,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',35010,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',35020,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',35030,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',35040,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',36010,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',36020,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',36030,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',36040,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',37010,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',37020,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',37030,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',37040,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',38010,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',38020,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',38030,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',38040,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',39010,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',39020,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',39030,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',39040,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',40010,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',40020,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',40030,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',40040,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',41010,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',41020,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',41030,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',41040,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',42010,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',42020,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',42030,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0),('uejecutora',42040,'eflores','eflores','2017-10-06 02:57:13','2017-10-06 03:00:40',0);
/*!40000 ALTER TABLE `usuario_permiso` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'sipro'
--

--
-- Dumping routines for database 'sipro'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-04-06 11:32:30
