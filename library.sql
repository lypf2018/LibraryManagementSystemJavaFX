CREATE DATABASE /*!32312 IF NOT EXISTS*/ `library` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */;

USE `library`;

--
-- Table structure for table `book`
--

DROP TABLE IF EXISTS `book`;
CREATE TABLE `book`  (
  `Isbn` varchar(10) NOT NULL,
  `Title` varchar(1000) NOT NULL,
  PRIMARY KEY (`Isbn`) USING BTREE
);

--
-- Table structure for table `authors`
--

DROP TABLE IF EXISTS `authors`;
CREATE TABLE `authors`  (
  `Author_id` int(6) NOT NULL AUTO_INCREMENT,
  `Name` varchar(100) NOT NULL UNIQUE,
  PRIMARY KEY (`Author_id`) USING BTREE
);

--
-- Table structure for table `book_authors`
--

DROP TABLE IF EXISTS `book_authors`;
CREATE TABLE `book_authors`  (
  `Author_id` int(6) NOT NULL,
  `Isbn` varchar(10) NOT NULL,
  PRIMARY KEY (`Author_id`,`Isbn`),
  CONSTRAINT `fk_bookauthors_book` FOREIGN KEY (`Isbn`) REFERENCES `book` (`Isbn`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_bookauthors_authors` FOREIGN KEY (`Author_id`) REFERENCES `authors` (`Author_id`) ON DELETE CASCADE ON UPDATE CASCADE
);

--
-- Table structure for table `borrower`
--

DROP TABLE IF EXISTS `borrower`;
CREATE TABLE `borrower`  (
  `Card_id` int(6) ZEROFILL NOT NULL AUTO_INCREMENT,
  `Ssn` varchar(11) NOT NULL UNIQUE,
  `Bname` varchar(100) NOT NULL,
  `Address` varchar(100) NOT NULL,
  `Phone` varchar(100) NOT NULL,
  PRIMARY KEY (`Card_id`) USING BTREE
);

--
-- Table structure for table `book_loans`
--

DROP TABLE IF EXISTS `book_loans`;
CREATE TABLE `book_loans`  (
  `Loan_id` int(6) NOT NULL AUTO_INCREMENT,
  `Isbn` varchar(10) NOT NULL,
  `Card_id` int(6) ZEROFILL NOT NULL,
  `Date_out` date NOT NULL,
  `Due_date` date NOT NULL,
  `Date_in` date,
  PRIMARY KEY (`Loan_id`),
  CONSTRAINT `fk_bookloans_book` FOREIGN KEY (`Isbn`) REFERENCES `book` (`Isbn`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_bookloans_borrower` FOREIGN KEY (`Card_id`) REFERENCES `borrower` (`Card_id`) ON DELETE CASCADE ON UPDATE CASCADE
);

--
-- Table structure for table `fines`
--

DROP TABLE IF EXISTS `fines`;
CREATE TABLE `fines`  (
  `Loan_id` int(6) NOT NULL,
  `Fine_amt` decimal(6,2) DEFAULT 0.00,
  `Paid` BOOLEAN DEFAULT FALSE,
  PRIMARY KEY (`Loan_id`),
  CONSTRAINT `fk_fines_bookloans` FOREIGN KEY (`Loan_id`) REFERENCES `book_loans` (`Loan_id`) ON DELETE CASCADE ON UPDATE CASCADE
);
