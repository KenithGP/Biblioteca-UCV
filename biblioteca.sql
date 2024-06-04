-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1:3306
-- Tiempo de generación: 24-11-2023 a las 16:04:20
-- Versión del servidor: 8.2.0
-- Versión de PHP: 7.4.3-4ubuntu2.19

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `bibliotecaucv`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ADMINISTRADOR`
--

CREATE TABLE `ADMINISTRADOR` (
  `ID_ADMINISTRADOR` int NOT NULL,
  `NOMBRE_AD` varchar(40) NOT NULL,
  `APELLIDOS_AD` varchar(50) NOT NULL,
  `CORREO_AD` varchar(100) DEFAULT NULL,
  `DNI_AD` varchar(8) NOT NULL,
  `CELULAR_AD` varchar(9) DEFAULT NULL
) ;

--
-- Volcado de datos para la tabla `ADMINISTRADOR`
--

INSERT INTO `ADMINISTRADOR` (`ID_ADMINISTRADOR`, `NOMBRE_AD`, `APELLIDOS_AD`, `CORREO_AD`, `DNI_AD`, `CELULAR_AD`) VALUES
(1, 'Emilia Sara', 'Sarmiento Perez', 'perezemi@ucvvirtual.edu.pe', '74598630', '954866002');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ESTUDIANTE`
--

CREATE TABLE `ESTUDIANTE` (
  `ID_ESTUDIANTE` int NOT NULL,
  `CODIGO_ES` varchar(20) NOT NULL,
  `NOMBRE_ES` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `APELLIDO_ES` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `DNI` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `CORREO_ES` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `CELULAR_ES` varchar(9) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `ESTADO` int NOT NULL
) ;

--
-- Volcado de datos para la tabla `ESTUDIANTE`
--

INSERT INTO `ESTUDIANTE` (`ID_ESTUDIANTE`, `CODIGO_ES`, `NOMBRE_ES`, `APELLIDO_ES`, `DNI`, `CORREO_ES`, `CELULAR_ES`, `ESTADO`) VALUES
(1, '7002815830', 'Kenith', 'Guanilo Pizarro', '73171623', 'kenit@ucvvirtual.edu.pe', '926889461', 1),
(2, '7002765089', 'Cleber', 'Ramos Ramos', '75369145', 'cramosramosra@ucvvirtual.edu.pe', '934677332', 1),
(3, '7002779116', 'Luis Anthony', 'Atiro Vargas', '74717989', 'TonyMont@ucvvirtual.edu.pe', '922113246', 1),
(4, '7002850946', 'Ariana', 'Benítes Martinez', '74295315', 'Benitez@ucvvirtual.edu.pe', '902203672', 1),
(5, '7002765086', 'Solange', 'Arce Gómez', '71314202', 'arcego@ucvvirtual.edu.pe', '947297823', 1),
(6, '7002801363', 'Sandra', 'Vega Mejia ', '60284189', 'sandra@ucvvirtual.edu.pe', '985033282', 1),
(7, '7009808301', 'Emilce Marisol', 'Medina Dávila', '73447801', 'emilce@ucvvirtual.edu.pe', '953802173', 1),
(8, '7002747486', 'Solange Dessire', 'Torres Salhuana ', '76305642', 'solange@ucvvirtual.edu.pe', '962051413', 0),
(9, '7005186982', 'Jose', 'Garcia Pedrales', '75269854', 'JosG@ucvvirtual.edu.pe', '958477665', 0),
(10, '7009126910', 'Pedro ', 'Ramirez Ramos', '72188963', 'RamiPedro@ucvvirtual.edu.pe', '958660332', 0);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `REGISTRO`
--

CREATE TABLE `REGISTRO` (
  `ID_REGISTRO` int NOT NULL,
  `ID_ESTUDIANTE` int NOT NULL,
  `ID_SALA` int NOT NULL,
  `ID_ADMINISTRADOR` int NOT NULL,
  `TIEMPO_INICIO` datetime NOT NULL,
  `TIEMPO_FIN` timestamp NULL DEFAULT NULL,
  `OBSERVACION` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `SALA`
--

CREATE TABLE `SALA` (
  `ID_SALA` int NOT NULL,
  `CAPACIDAD` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `SALA`
--

INSERT INTO `SALA` (`ID_SALA`, `CAPACIDAD`) VALUES
(1, 7),
(2, 7),
(3, 7),
(4, 7),
(5, 11),
(6, 11),
(7, 11),
(8, 11),
(9, 11),
(10, 11),
(11, 11);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `USUARIO`
--

CREATE TABLE `USUARIO` (
  `ID_ADMINISTRADOR` int NOT NULL,
  `USUARIO` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `CONTRASEÑA` varbinary(69) NOT NULL,
  `ESTADO` int NOT NULL
) ;

--
-- Volcado de datos para la tabla `USUARIO`
--

INSERT INTO `USUARIO` (`ID_ADMINISTRADOR`, `USUARIO`, `CONTRASEÑA`, `ESTADO`) VALUES
(1, 'Administrador', 0x65383666373861386133636166306236306438653734653539343261613664383664633135306364336330333333386165663235623764326437653361636337, 1);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `ADMINISTRADOR`
--
ALTER TABLE `ADMINISTRADOR`
  ADD PRIMARY KEY (`ID_ADMINISTRADOR`),
  ADD UNIQUE KEY `DNI_AD` (`DNI_AD`),
  ADD UNIQUE KEY `CORREO_AD` (`CORREO_AD`);

--
-- Indices de la tabla `ESTUDIANTE`
--
ALTER TABLE `ESTUDIANTE`
  ADD PRIMARY KEY (`ID_ESTUDIANTE`),
  ADD UNIQUE KEY `CODIGO_ES` (`CODIGO_ES`),
  ADD UNIQUE KEY `DNI` (`DNI`),
  ADD UNIQUE KEY `CORREO_ES` (`CORREO_ES`);

--
-- Indices de la tabla `REGISTRO`
--
ALTER TABLE `REGISTRO`
  ADD PRIMARY KEY (`ID_REGISTRO`),
  ADD KEY `ID_ESTUDIANTE` (`ID_ESTUDIANTE`),
  ADD KEY `ID_SALA` (`ID_SALA`),
  ADD KEY `ID_ADMINISTRADOR` (`ID_ADMINISTRADOR`);

--
-- Indices de la tabla `SALA`
--
ALTER TABLE `SALA`
  ADD PRIMARY KEY (`ID_SALA`);

--
-- Indices de la tabla `USUARIO`
--
ALTER TABLE `USUARIO`
  ADD PRIMARY KEY (`ID_ADMINISTRADOR`),
  ADD UNIQUE KEY `USUARIO` (`USUARIO`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `ADMINISTRADOR`
--
ALTER TABLE `ADMINISTRADOR`
  MODIFY `ID_ADMINISTRADOR` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `ESTUDIANTE`
--
ALTER TABLE `ESTUDIANTE`
  MODIFY `ID_ESTUDIANTE` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `REGISTRO`
--
ALTER TABLE `REGISTRO`
  MODIFY `ID_REGISTRO` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `REGISTRO`
--
ALTER TABLE `REGISTRO`
  ADD CONSTRAINT `REGISTRO_ibfk_1` FOREIGN KEY (`ID_ESTUDIANTE`) REFERENCES `ESTUDIANTE` (`ID_ESTUDIANTE`),
  ADD CONSTRAINT `REGISTRO_ibfk_2` FOREIGN KEY (`ID_SALA`) REFERENCES `SALA` (`ID_SALA`),
  ADD CONSTRAINT `REGISTRO_ibfk_3` FOREIGN KEY (`ID_ADMINISTRADOR`) REFERENCES `USUARIO` (`ID_ADMINISTRADOR`);

--
-- Filtros para la tabla `USUARIO`
--
ALTER TABLE `USUARIO`
  ADD CONSTRAINT `USUARIO_ibfk_1` FOREIGN KEY (`ID_ADMINISTRADOR`) REFERENCES `ADMINISTRADOR` (`ID_ADMINISTRADOR`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
