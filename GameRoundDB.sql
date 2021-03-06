DROP DATABASE IF EXISTS GameRoundDB;
CREATE DATABASE GameRoundDB;

USE GameRoundDB;

CREATE TABLE Game(
GameId INT PRIMARY KEY AUTO_INCREMENT,
Answer INT NOT NULL,
`Status` VARCHAR(15) NOT NULL
);

CREATE TABLE `Round`(
RoundId INT PRIMARY KEY AUTO_INCREMENT,
GameId INT NOT NULL,
Guess INT NOT NULL,
`Time` DATETIME NOT NULL,
Result CHAR(7) NOT NULL,
FOREIGN KEY (GameId) REFERENCES Game(GameId)
);
