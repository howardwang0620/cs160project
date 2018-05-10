-- phpMyAdmin SQL Dump
-- version 4.7.9
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: May 09, 2018 at 11:13 PM
-- Server version: 5.7.21
-- PHP Version: 7.1.16

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `asksjsu`
--
CREATE DATABASE IF NOT EXISTS `asksjsu` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `asksjsu`;

-- --------------------------------------------------------

--
-- Table structure for table `question`
--

CREATE TABLE `question` (
  `QuestionID` int(11) NOT NULL,
  `QuestionBody` varchar(500) NOT NULL,
  `QuestionCategory` varchar(50) NOT NULL DEFAULT 'Other',
  `QuestionType` enum('Polling','Rating') NOT NULL DEFAULT 'Polling',
  `DateCreated` date NOT NULL DEFAULT '2000-01-01',
  `ExpirationDate` date NOT NULL DEFAULT '2999-01-01',
  `UsefulCount` int(11) NOT NULL DEFAULT '0',
  `Visible` tinyint(1) NOT NULL DEFAULT '0',
  `UserID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Triggers `question`
--
DELIMITER $$
CREATE TRIGGER `BeforeUpdateSetQuestionVisible` BEFORE UPDATE ON `question` FOR EACH ROW BEGIN
if (NEW.questiontype = "Rating" AND NEW.usefulcount >= 10) Then SET NEW.visible = 1;
elseif (NEW.questiontype = "Rating" AND NEW.usefulcount < 10) Then SET NEW.visible = 0;
end if;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `questionoption`
--

CREATE TABLE `questionoption` (
  `OptionID` int(11) NOT NULL,
  `OptionName` varchar(100) NOT NULL,
  `VoteCount` int(11) DEFAULT '0',
  `QuestionID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `userdata`
--

CREATE TABLE `userdata` (
  `UserID` int(11) NOT NULL,
  `Username` varchar(50) NOT NULL,
  `UserPassword` varchar(50) NOT NULL,
  `UserEmail` varchar(100) NOT NULL,
  `Verified` tinyint(1) DEFAULT '0',
  `sjsuID` int(9) UNSIGNED ZEROFILL NOT NULL,
  `InviteCode` int(6) UNSIGNED ZEROFILL DEFAULT '123456'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `votehistory`
--

CREATE TABLE `votehistory` (
  `UserID` int(11) NOT NULL,
  `QuestionID` int(11) NOT NULL,
  `hasVoted` tinyint(1) NOT NULL DEFAULT '0',
  `hasUpvoted` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `question`
--
ALTER TABLE `question`
  ADD PRIMARY KEY (`QuestionID`),
  ADD KEY `UserID` (`UserID`);

--
-- Indexes for table `questionoption`
--
ALTER TABLE `questionoption`
  ADD PRIMARY KEY (`OptionID`),
  ADD KEY `QuestionID` (`QuestionID`);

--
-- Indexes for table `userdata`
--
ALTER TABLE `userdata`
  ADD PRIMARY KEY (`UserID`),
  ADD UNIQUE KEY `Username` (`Username`);

--
-- Indexes for table `votehistory`
--
ALTER TABLE `votehistory`
  ADD PRIMARY KEY (`UserID`,`QuestionID`),
  ADD KEY `QuestionID` (`QuestionID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `question`
--
ALTER TABLE `question`
  MODIFY `QuestionID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=94;

--
-- AUTO_INCREMENT for table `questionoption`
--
ALTER TABLE `questionoption`
  MODIFY `OptionID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=315;

--
-- AUTO_INCREMENT for table `userdata`
--
ALTER TABLE `userdata`
  MODIFY `UserID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=79;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `question`
--
ALTER TABLE `question`
  ADD CONSTRAINT `question_ibfk_1` FOREIGN KEY (`UserID`) REFERENCES `userdata` (`UserID`) ON DELETE CASCADE;

--
-- Constraints for table `questionoption`
--
ALTER TABLE `questionoption`
  ADD CONSTRAINT `questionoption_ibfk_1` FOREIGN KEY (`QuestionID`) REFERENCES `question` (`QuestionID`) ON DELETE CASCADE;

--
-- Constraints for table `votehistory`
--
ALTER TABLE `votehistory`
  ADD CONSTRAINT `votehistory_ibfk_1` FOREIGN KEY (`UserID`) REFERENCES `userdata` (`UserID`) ON DELETE CASCADE,
  ADD CONSTRAINT `votehistory_ibfk_2` FOREIGN KEY (`QuestionID`) REFERENCES `question` (`QuestionID`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
