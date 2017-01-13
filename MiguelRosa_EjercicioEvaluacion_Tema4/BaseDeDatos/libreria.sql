-- phpMyAdmin SQL Dump
-- version 4.0.4
-- http://www.phpmyadmin.net
--
-- Servidor: localhost
-- Tiempo de generación: 13-01-2017 a las 18:04:39
-- Versión del servidor: 5.6.12-log
-- Versión de PHP: 5.4.16

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de datos: `libreria`
--
CREATE DATABASE IF NOT EXISTS `libreria` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `libreria`;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `autores`
--

CREATE TABLE IF NOT EXISTS `autores` (
  `NOMBRE` varchar(50) NOT NULL,
  `NACIONALIDAD` varchar(20) DEFAULT NULL,
  `COMENTARIOS` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`NOMBRE`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `autores`
--

INSERT INTO `autores` (`NOMBRE`, `NACIONALIDAD`, `COMENTARIOS`) VALUES
('Fernando Fernández', 'España', 'Amigo de Francisco Rojas'),
('Francisco Rojas', 'España', 'Un comentario'),
('Jacinto Romero', 'Colombia', 'Otro amigo de Francisco');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `direcciones`
--

CREATE TABLE IF NOT EXISTS `direcciones` (
  `ID_DIRECCION` int(11) NOT NULL,
  `CALLE` varchar(50) NOT NULL,
  `NUMERO` int(11) DEFAULT NULL,
  `POBLACION` varchar(30) DEFAULT NULL,
  `CODIGO_POSTAL` int(11) DEFAULT NULL,
  `PROVINCIA` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`ID_DIRECCION`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `direcciones`
--

INSERT INTO `direcciones` (`ID_DIRECCION`, `CALLE`, `NUMERO`, `POBLACION`, `CODIGO_POSTAL`, `PROVINCIA`) VALUES
(1, 'Paraiso', 1, 'Córdoba', 11234, 'Córdoba'),
(2, 'Paraiso', 1, 'Córdoba', 11234, 'Córdoba'),
(3, 'Tarragona', 3, 'San Juan del Puerto', 23352, 'Huelva');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `editoriales`
--

CREATE TABLE IF NOT EXISTS `editoriales` (
  `NOMBRE` varchar(60) NOT NULL,
  `NIF` varchar(10) NOT NULL,
  `ID_DIRECCION` int(11) NOT NULL,
  PRIMARY KEY (`NOMBRE`),
  UNIQUE KEY `ID_DIRECCION` (`ID_DIRECCION`),
  KEY `FK28BFC095B2274404` (`ID_DIRECCION`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `editoriales`
--

INSERT INTO `editoriales` (`NOMBRE`, `NIF`, `ID_DIRECCION`) VALUES
('Atalaya', '28267262S', 3),
('Editorialsa', '12123123N', 2);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `libros`
--

CREATE TABLE IF NOT EXISTS `libros` (
  `ID_LIBRO` int(11) NOT NULL,
  `TITULO` varchar(60) NOT NULL,
  `ISBN` varchar(20) DEFAULT NULL,
  `PUBLICACION` int(11) DEFAULT NULL,
  `PRECIO` double DEFAULT NULL,
  `DESCRIPCION` varchar(255) DEFAULT NULL,
  `NOMBRE` varchar(60) NOT NULL,
  PRIMARY KEY (`ID_LIBRO`),
  UNIQUE KEY `TITULO` (`TITULO`),
  UNIQUE KEY `ISBN` (`ISBN`),
  KEY `FK87A63951802EB958` (`NOMBRE`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `libros`
--

INSERT INTO `libros` (`ID_LIBRO`, `TITULO`, `ISBN`, `PUBLICACION`, `PRECIO`, `DESCRIPCION`, `NOMBRE`) VALUES
(1, 'Mi primer libro', '123-123-123-123', 1960, 11.11, 'Una descripción', 'Editorialsa'),
(2, 'Mi segundo libro', '123-123-123-124', 1961, 11.12, 'Otro comentario', 'Editorialsa'),
(3, 'Mi tercer libro', '123-123-321-321', 1963, 0.23, 'Éste ya se hizo por la broma', 'Atalaya');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `libros_autores`
--

CREATE TABLE IF NOT EXISTS `libros_autores` (
  `ID_AUTOR` varchar(50) NOT NULL,
  `ID_LIBRO` int(11) NOT NULL,
  PRIMARY KEY (`ID_LIBRO`,`ID_AUTOR`),
  KEY `FK49C77B83AE3B62A8` (`ID_LIBRO`),
  KEY `FK49C77B83AD10CCEA` (`ID_AUTOR`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `libros_autores`
--

INSERT INTO `libros_autores` (`ID_AUTOR`, `ID_LIBRO`) VALUES
('Francisco Rojas', 1),
('Fernando Fernández', 2),
('Francisco Rojas', 2),
('Fernando Fernández', 3),
('Francisco Rojas', 3),
('Jacinto Romero', 3);

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `editoriales`
--
ALTER TABLE `editoriales`
  ADD CONSTRAINT `FK28BFC095B2274404` FOREIGN KEY (`ID_DIRECCION`) REFERENCES `direcciones` (`ID_DIRECCION`);

--
-- Filtros para la tabla `libros`
--
ALTER TABLE `libros`
  ADD CONSTRAINT `FK87A63951802EB958` FOREIGN KEY (`NOMBRE`) REFERENCES `editoriales` (`NOMBRE`);

--
-- Filtros para la tabla `libros_autores`
--
ALTER TABLE `libros_autores`
  ADD CONSTRAINT `FK49C77B83AD10CCEA` FOREIGN KEY (`ID_AUTOR`) REFERENCES `autores` (`NOMBRE`),
  ADD CONSTRAINT `FK49C77B83AE3B62A8` FOREIGN KEY (`ID_LIBRO`) REFERENCES `libros` (`ID_LIBRO`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
