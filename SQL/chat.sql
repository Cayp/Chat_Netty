/*
Navicat MySQL Data Transfer

Source Server         : cayp
Source Server Version : 50723
Source Host           : localhost:3306
Source Database       : chat

Target Server Type    : MYSQL
Target Server Version : 50723
File Encoding         : 65001

Date: 2019-06-20 15:14:39
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for friend
-- ----------------------------
DROP TABLE IF EXISTS `friend`;
CREATE TABLE `friend` (
  `toid` int(11) NOT NULL,
  `friendid` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for groupchat
-- ----------------------------
DROP TABLE IF EXISTS `groupchat`;
CREATE TABLE `groupchat` (
  `userId` int(11) NOT NULL,
  PRIMARY KEY (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for noreadme
-- ----------------------------
DROP TABLE IF EXISTS `noreadme`;
CREATE TABLE `noreadme` (
  `toid` int(11) NOT NULL,
  `fromid` int(11) NOT NULL,
  `message` varchar(255) NOT NULL,
  `time` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for redpacket_detail
-- ----------------------------
DROP TABLE IF EXISTS `redpacket_detail`;
CREATE TABLE `redpacket_detail` (
  `redpacket_id` bigint(20) NOT NULL,
  `userid` int(11) NOT NULL,
  `money` varchar(10) NOT NULL,
  `time` varchar(13) NOT NULL,
  PRIMARY KEY (`redpacket_id`,`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for redpacket_table
-- ----------------------------
DROP TABLE IF EXISTS `redpacket_table`;
CREATE TABLE `redpacket_table` (
  `redpacket_id` bigint(20) NOT NULL,
  `userid_pub` int(11) NOT NULL,
  `total_money` varchar(7) NOT NULL,
  `redpacket_type` tinyint(1) NOT NULL,
  `size` int(11) NOT NULL,
  `time` varchar(13) NOT NULL,
  PRIMARY KEY (`redpacket_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `account` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`account`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for wallet
-- ----------------------------
DROP TABLE IF EXISTS `wallet`;
CREATE TABLE `wallet` (
  `userId` int(11) NOT NULL AUTO_INCREMENT,
  `money` decimal(10,2) NOT NULL,
  PRIMARY KEY (`userId`)
) ENGINE=InnoDB AUTO_INCREMENT=100001 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Procedure structure for iniwallet
-- ----------------------------
DROP PROCEDURE IF EXISTS `iniwallet`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `iniwallet`(IN `n` int)
BEGIN
	#Routine body goes here...
  WHILE n > 0 DO
  INSERT INTO wallet(money) VALUES(1000);
  set n = n - 1; 
  end WHILE;
  
END
;;
DELIMITER ;
