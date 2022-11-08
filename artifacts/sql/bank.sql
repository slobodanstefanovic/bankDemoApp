CREATE DATABASE `bank` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
CREATE TABLE `account` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `Name` tinytext,
  `Balance` double DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
CREATE TABLE `transaction` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `From_ID` int NOT NULL,
  `To_ID` int NOT NULL,
  `Amount` double NOT NULL,
  `Status` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_TransactionFrom` (`From_ID`),
  KEY `FK_TransactionTo` (`To_ID`),
  CONSTRAINT `FK_TransactionFrom` FOREIGN KEY (`From_ID`) REFERENCES `account` (`ID`),
  CONSTRAINT `FK_TransactionTo` FOREIGN KEY (`To_ID`) REFERENCES `account` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
INSERT INTO `bank`.`account` (`Name`, `Balance`) VALUES ('Aleksa', '100');
INSERT INTO `bank`.`account` (`Name`, `Balance`) VALUES ('Pera', '200');
INSERT INTO `bank`.`account` (`Name`, `Balance`) VALUES ('Zika', '150');

