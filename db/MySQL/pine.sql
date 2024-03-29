-- MySQL Script generated by MySQL Workbench
-- Sun Apr  8 20:08:42 2018
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

-- -----------------------------------------------------
-- Schema pine
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema pine
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `pine` DEFAULT CHARACTER SET utf8 ;
USE `pine` ;

-- -----------------------------------------------------
-- Table `pine`.`artist_type`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pine`.`artist_type` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `artistType` VARCHAR(2000) NULL DEFAULT '',
  PRIMARY KEY (`ID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `pine`.`artists`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pine`.`artists` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `firstName` VARCHAR(100) NOT NULL DEFAULT '',
  `lastName` VARCHAR(100) NULL DEFAULT '',
  `description` VARCHAR(15000) NULL,
  `birthdate` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `birthplace` VARCHAR(200) NULL DEFAULT '',
  `resourcePath` VARCHAR(2000) NULL DEFAULT '',
  PRIMARY KEY (`ID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `pine`.`media_rating`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pine`.`media_rating` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `mediaRating` VARCHAR(2000) NOT NULL DEFAULT '',
  PRIMARY KEY (`ID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `pine`.`media_type`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pine`.`media_type` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `mediaType` VARCHAR(2000) NOT NULL DEFAULT '',
  PRIMARY KEY (`ID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `pine`.`media`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pine`.`media` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `parentID` INT(11) NULL DEFAULT 0,
  `mediaType` INT(11) NOT NULL DEFAULT 0,
  `mediaName` VARCHAR(500) NULL DEFAULT '',
  `description` MEDIUMTEXT NULL,
  `releaseDateTheatre` DATE NULL DEFAULT "1000-01-01",
  `releaseDateDVD` DATE NULL DEFAULT "1000-01-01",
  `mediaRating` INT(11) NOT NULL DEFAULT 1,
  `runtime` INT(11) NULL DEFAULT 0,
  `boxOffice` INT(11) NULL DEFAULT 0,
  `mediaFrom` VARCHAR(500) NULL DEFAULT '',
  `audienceAvgScore` FLOAT NULL DEFAULT 0,
  `audienceMudemeter` FLOAT NULL DEFAULT 0,
  `criticAvgScore` FLOAT NULL DEFAULT 0,
  `criticMudemeter` FLOAT NULL DEFAULT 0,
  `resourcePath` VARCHAR(2000) NULL DEFAULT '',
  PRIMARY KEY (`ID`),
  INDEX `mediaType_idx` (`mediaType` ASC),
  INDEX `mediaRating_FK_idx` (`mediaRating` ASC),
  CONSTRAINT `media_mediaRating_FK`
    FOREIGN KEY (`mediaRating`)
    REFERENCES `pine`.`media_rating` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `media_mediaType_FK`
    FOREIGN KEY (`mediaType`)
    REFERENCES `pine`.`media_type` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `pine`.`awards`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pine`.`awards` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `mediaID` INT(11) NOT NULL DEFAULT 0,
  `awardName` VARCHAR(15000) NULL DEFAULT '',
  `awardYear` INT(11) NULL DEFAULT 0 COMMENT 'This is only year',
  PRIMARY KEY (`ID`),
  INDEX `mediaID_FK_idx` (`mediaID` ASC),
  CONSTRAINT `awards_mediaID_FK`
    FOREIGN KEY (`mediaID`)
    REFERENCES `pine`.`media` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `pine`.`member_type`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pine`.`member_type` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `memberType` VARCHAR(2000) NOT NULL DEFAULT '' COMMENT '\n1 = audience\n2 = critic\n3 = admin',
  PRIMARY KEY (`ID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `pine`.`members`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pine`.`members` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `memberType` INT(11) NOT NULL DEFAULT 1 COMMENT 'Every user account is audience until they are verified as critic\nSo by keeping default to 1 is okay since\n1 = audience in MemberType',
  `email` VARCHAR(200) NULL DEFAULT '',
  `emailValid` TINYINT(4) NULL DEFAULT 0 COMMENT '0 = false\n1 = true',
  `firstName` VARCHAR(200) NULL DEFAULT '',
  `lastName` VARCHAR(200) NULL DEFAULT '',
  `description` VARCHAR(15000) NULL,
  `password` BLOB NULL,
  `regDate` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `resourcePath` VARCHAR(2000) NULL DEFAULT '',
  PRIMARY KEY (`ID`),
  INDEX `Members_memberTypeID_FK_idx` (`memberType` ASC),
  CONSTRAINT `members_memberTypeID_FK`
    FOREIGN KEY (`memberType`)
    REFERENCES `pine`.`member_type` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `pine`.`publications`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pine`.`publications` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `publicationName` VARCHAR(2000) NOT NULL DEFAULT '',
  `description` VARCHAR(15000) NULL DEFAULT '',
  PRIMARY KEY (`ID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `pine`.`critic_requests`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pine`.`critic_requests` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `memberID` INT(11) NOT NULL DEFAULT 0,
  `publicationID` INT(11) NOT NULL DEFAULT 0,
  `description` VARCHAR(15000) NULL,
  `regDate` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`),
  INDEX `memberID_FK_idx` (`memberID` ASC),
  INDEX `publicationID_Fk_idx` (`publicationID` ASC),
  CONSTRAINT `critic_requests_memberID_FK`
    FOREIGN KEY (`memberID`)
    REFERENCES `pine`.`members` (`ID`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `critic_requests_publicationID_Fk`
    FOREIGN KEY (`publicationID`)
    REFERENCES `pine`.`publications` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `pine`.`filmography`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pine`.`filmography` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `mediaID` INT(11) NOT NULL DEFAULT 0,
  `artistID` INT(11) NOT NULL DEFAULT 0,
  `artistType` INT(11) NOT NULL DEFAULT 1,
  `artistRole` VARCHAR(2000) NULL DEFAULT '',
  PRIMARY KEY (`ID`),
  INDEX `mediaID_FK_idx` (`mediaID` ASC),
  INDEX `artistID_FK_idx` (`artistID` ASC),
  INDEX `artistTypeID_FK_idx` (`artistType` ASC),
  CONSTRAINT `filmography_artistID_FK`
    FOREIGN KEY (`artistID`)
    REFERENCES `pine`.`artists` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `filmography_artistTypeID_FK`
    FOREIGN KEY (`artistType`)
    REFERENCES `pine`.`artist_type` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `filmography_mediaID_FK`
    FOREIGN KEY (`mediaID`)
    REFERENCES `pine`.`media` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `pine`.`genre`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pine`.`genre` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `genreName` VARCHAR(2000) NULL DEFAULT '',
  PRIMARY KEY (`ID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `pine`.`media_genre`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pine`.`media_genre` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `mediaID` INT(11) NOT NULL DEFAULT 0,
  `genreID` INT(11) NOT NULL DEFAULT 0,
  PRIMARY KEY (`ID`),
  INDEX `mediaID_FK_idx` (`mediaID` ASC),
  INDEX `media_genre_genreID_FK_idx` (`genreID` ASC),
  CONSTRAINT `media_genre_mediaID_FK`
    FOREIGN KEY (`mediaID`)
    REFERENCES `pine`.`media` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `media_genre_genreID_FK`
    FOREIGN KEY (`genreID`)
    REFERENCES `pine`.`genre` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `pine`.`member_publication`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pine`.`member_publication` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `memberID` INT(11) NOT NULL DEFAULT 0,
  `publicationID` INT(11) NOT NULL DEFAULT 0,
  PRIMARY KEY (`ID`),
  INDEX `FKmemberID_idx` (`memberID` ASC),
  INDEX `FKpublicationID_idx` (`publicationID` ASC),
  CONSTRAINT `member_publication_memberID_FK`
    FOREIGN KEY (`memberID`)
    REFERENCES `pine`.`members` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `member_publication_publicationID_FK`
    FOREIGN KEY (`publicationID`)
    REFERENCES `pine`.`publications` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `pine`.`reviews`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pine`.`reviews` (
  `ID` BIGINT(20) NOT NULL,
  `mediaID` INT(11) NULL DEFAULT 0,
  `memberID` INT(11) NULL DEFAULT 0,
  `comment` VARCHAR(15000) NULL DEFAULT '',
  `score` FLOAT NULL DEFAULT 0,
  `ratingType` INT NOT NULL DEFAULT 1,
  `externalLink` VARCHAR(2000) NULL DEFAULT '',
  `regDate` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`),
  INDEX `reviews_mediaID_FK_idx` (`mediaID` ASC),
  INDEX `reviews_memeberID_FK_idx` (`memberID` ASC),
  INDEX `reviews_ratingType_FK_idx` (`ratingType` ASC))
ENGINE = MyISAM
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `pine`.`want_to_see`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pine`.`want_to_see` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `mediaID` INT(11) NOT NULL DEFAULT 0,
  `memberID` INT(11) NOT NULL DEFAULT 0,
  `regDate` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `isWantToSee` TINYINT(4) NOT NULL DEFAULT 1 COMMENT '0 = NOT INTERESTED\n1 = WANT TO SEE',
  PRIMARY KEY (`ID`),
  INDEX `mediaID_FK_idx` (`mediaID` ASC),
  INDEX `memberID_FK_idx` (`memberID` ASC),
  CONSTRAINT `WantToSee_mediaID_FK`
    FOREIGN KEY (`mediaID`)
    REFERENCES `pine`.`media` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `WantToSee_memberID_FK`
    FOREIGN KEY (`memberID`)
    REFERENCES `pine`.`members` (`ID`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
