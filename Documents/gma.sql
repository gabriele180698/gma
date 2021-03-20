-- phpMyAdmin SQL Dump
-- version 5.0.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Creato il: Mar 11, 2021 alle 15:36useruser
-- Versione del server: 10.4.17-MariaDB
-- Versione PHP: 8.0.0

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+01:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `clup2`
--

-- --------------------------------------------------------

--
-- Struttura della tabella `access`
--

CREATE TABLE `access` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `idUser` bigint(20) UNSIGNED NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Struttura della tabella `answer`
--

CREATE TABLE `answer` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `text` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `idUser` bigint(20) UNSIGNED NOT NULL,
  `idQuestion` bigint(20) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Struttura della tabella `blacklist`
--

CREATE TABLE `blacklist` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `word` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Struttura della tabella `product`
--

CREATE TABLE `product` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `img` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Struttura della tabella `question`
--

CREATE TABLE `question` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `text` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `idQuestionnaire` bigint(20) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Struttura della tabella `questionnaire`
--

CREATE TABLE `questionnaire` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `date` date NOT NULL,
  `idProduct` bigint(20) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Struttura della tabella `user`
--

CREATE TABLE `user` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `username` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `type` tinyint(3) UNSIGNED NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Struttura della tabella `user_questionnaire`
--

CREATE TABLE `user_questionnaire` (
  `idUser` bigint(20) UNSIGNED NOT NULL,
  `idQuestionnaire` bigint(20) UNSIGNED NOT NULL,
  `score` int(10) UNSIGNED NOT NULL DEFAULT 0,
  `status` tinyint(3) UNSIGNED NOT NULL DEFAULT 0,
  `age` tinyint(3) UNSIGNED NOT NULL DEFAULT 0,
  `sex` tinyint(3) UNSIGNED NOT NULL DEFAULT 0,
  `expertise` tinyint(4) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Indici per le tabelle scaricate
--

--
-- Indici per le tabelle `access`
--
ALTER TABLE `access`
  ADD PRIMARY KEY (`id`),
  ADD KEY `access_iduser_foreign` (`idUser`);

--
-- Indici per le tabelle `answer`
--
ALTER TABLE `answer`
  ADD PRIMARY KEY (`id`),
  ADD KEY `answer_iduser_foreign` (`idUser`),
  ADD KEY `answer_idquestion_foreign` (`idQuestion`);

--
-- Indici per le tabelle `blacklist`
--
ALTER TABLE `blacklist`
  ADD PRIMARY KEY (`id`);

--
-- Indici per le tabelle `product`
--
ALTER TABLE `product`
  ADD PRIMARY KEY (`id`);

--
-- Indici per le tabelle `question`
--
ALTER TABLE `question`
  ADD PRIMARY KEY (`id`),
  ADD KEY `question_idquestionnaire_foreign` (`idQuestionnaire`);

--
-- Indici per le tabelle `questionnaire`
--
ALTER TABLE `questionnaire`
  ADD PRIMARY KEY (`id`),
  ADD KEY `questionnaire_idproduct_foreign` (`idProduct`);

--
-- Indici per le tabelle `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `user_email_unique` (`email`);

--
-- Indici per le tabelle `user_questionnaire`
--
ALTER TABLE `user_questionnaire`
  ADD PRIMARY KEY (`idUser`,`idQuestionnaire`),
  ADD KEY `user_questionnaire_idquestionnaire_foreign` (`idQuestionnaire`);

--
-- AUTO_INCREMENT per le tabelle scaricate
--

--
-- AUTO_INCREMENT per la tabella `access`
--
ALTER TABLE `access`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT per la tabella `answer`
--
ALTER TABLE `answer`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT per la tabella `blacklist`
--
ALTER TABLE `blacklist`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT per la tabella `product`
--
ALTER TABLE `product`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT per la tabella `question`
--
ALTER TABLE `question`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT per la tabella `questionnaire`
--
ALTER TABLE `questionnaire`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT per la tabella `user`
--
ALTER TABLE `user`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- Limiti per le tabelle scaricate
--

--
-- Limiti per la tabella `access`
--
ALTER TABLE `access`
  ADD CONSTRAINT `access_iduser_foreign` FOREIGN KEY (`idUser`) REFERENCES `user` (`id`) ON DELETE CASCADE;

--
-- Limiti per la tabella `answer`
--
ALTER TABLE `answer`
  ADD CONSTRAINT `answer_idquestion_foreign` FOREIGN KEY (`idQuestion`) REFERENCES `question` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `answer_iduser_foreign` FOREIGN KEY (`idUser`) REFERENCES `user` (`id`) ON DELETE CASCADE;

--
-- Limiti per la tabella `question`
--
ALTER TABLE `question`
  ADD CONSTRAINT `question_idquestionnaire_foreign` FOREIGN KEY (`idQuestionnaire`) REFERENCES `questionnaire` (`id`) ON DELETE CASCADE;

--
-- Limiti per la tabella `questionnaire`
--
ALTER TABLE `questionnaire`
  ADD CONSTRAINT `questionnaire_idproduct_foreign` FOREIGN KEY (`idProduct`) REFERENCES `product` (`id`) ON DELETE CASCADE;

--
-- Limiti per la tabella `user_questionnaire`
--
ALTER TABLE `user_questionnaire`
  ADD CONSTRAINT `user_questionnaire_idquestionnaire_foreign` FOREIGN KEY (`idQuestionnaire`) REFERENCES `questionnaire` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `user_questionnaire_iduser_foreign` FOREIGN KEY (`idUser`) REFERENCES `user` (`id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
