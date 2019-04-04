/*
Navicat MySQL Data Transfer

Source Server         : user
Source Server Version : 50549
Source Host           : localhost:3306
Source Database       : Management

Target Server Type    : MYSQL
Target Server Version : 50549
File Encoding         : 65001

Date: 2017-08-29 18:37:02
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for bill
-- ----------------------------
DROP TABLE IF EXISTS `bill`;
CREATE TABLE `bill` (
  `bid` int(11) NOT NULL AUTO_INCREMENT,
  `bname` varchar(50) NOT NULL,
  `amount` int(11) DEFAULT NULL,
  `account` double DEFAULT NULL,
  `billType` varchar(50) DEFAULT NULL,
  `sid` int(11) DEFAULT NULL,
  `bdescription` varchar(50) DEFAULT NULL,
  `btime` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`bid`),
  KEY `fk_bill_sid` (`sid`),
  CONSTRAINT `fk_bill_sid` FOREIGN KEY (`sid`) REFERENCES `supplier` (`sid`)
) ENGINE=InnoDB AUTO_INCREMENT=88 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of bill
-- ----------------------------
INSERT INTO `bill` VALUES ('51', 'IPhone6', '100', '100000', '进货', '23', '', '2017-08-27');
INSERT INTO `bill` VALUES ('52', 'IPhone6s', '10', '40000', '进货', '23', '', '2017-08-27');
INSERT INTO `bill` VALUES ('53', '华为5', '10', '40000', '进货', '21', '', '2017-08-27');
INSERT INTO `bill` VALUES ('54', '华为4', '100', '400000', '进货', '21', '', '2017-08-27');
INSERT INTO `bill` VALUES ('55', '华为4', '100', '400000', '出货', '21', '', '2017-08-27');
INSERT INTO `bill` VALUES ('56', '联想1', '12', '1000', '进货', '29', '完全', '2017-08-28');
INSERT INTO `bill` VALUES ('58', '联想2', '122', '10000', '进货', '29', '完全', '2017-08-28');
INSERT INTO `bill` VALUES ('59', '联想3', '122', '10000', '进货', '29', '完全', '2017-08-28');
INSERT INTO `bill` VALUES ('60', '苹果1', '1', '6000', '出货', '23', '', '2017-08-28');
INSERT INTO `bill` VALUES ('61', '苹果2', '12', '60000', '出货', '23', '', '2017-08-28');
INSERT INTO `bill` VALUES ('62', '苹果3', '12', '60000', '出货', '23', '', '2017-08-28');
INSERT INTO `bill` VALUES ('63', '苹果4', '12', '60000', '出货', '23', '', '2017-08-28');
INSERT INTO `bill` VALUES ('64', '苹果5', '12', '60000', '出货', '23', '', '2017-08-28');
INSERT INTO `bill` VALUES ('65', '苹果6', '12', '60000', '出货', '23', '', '2017-08-28');
INSERT INTO `bill` VALUES ('67', '华为', '12', '60000', '进货', '21', '', '2017-08-28');
INSERT INTO `bill` VALUES ('68', '华为1', '12', '60000', '进货', '21', '', '2017-08-28');
INSERT INTO `bill` VALUES ('69', '华为2', '12', '60000', '出货', '21', '', '2017-08-28');
INSERT INTO `bill` VALUES ('70', '华为3', '12', '60000', '出货', '21', '', '2017-08-28');
INSERT INTO `bill` VALUES ('71', '华为4', '12', '60000', '进货', '21', '', '2017-08-28');
INSERT INTO `bill` VALUES ('72', '华为5', '12', '60000', '进货', '21', '', '2017-08-28');
INSERT INTO `bill` VALUES ('73', '华为5', '120', '600000', '出货', '21', '', '2017-08-28');
INSERT INTO `bill` VALUES ('74', '诺基亚', '120', '600000', '出货', '25', '', '2017-08-28');
INSERT INTO `bill` VALUES ('75', '诺基亚1', '120', '600000', '进货', '25', '', '2017-08-28');
INSERT INTO `bill` VALUES ('76', '诺基亚2', '120', '600000', '进货', '25', '', '2017-08-28');
INSERT INTO `bill` VALUES ('77', '诺基亚3', '120', '600000', '进货', '25', '', '2017-08-28');
INSERT INTO `bill` VALUES ('78', '诺基亚3', '120', '600000', '进货', '25', '', '2017-08-28');
INSERT INTO `bill` VALUES ('79', '农夫三泉', '12', '1444', '出货', '36', '		we', '2017-08-28');
INSERT INTO `bill` VALUES ('80', '农夫三泉', '12', '1444', '出货', '36', '		we', '2017-08-28');
INSERT INTO `bill` VALUES ('81', '农夫三泉', '12', '1444', '出货', '35', '		we', '2017-08-28');
INSERT INTO `bill` VALUES ('82', '农夫三泉', '12', '1444', '出货', '35', '		we', '2017-08-28');
INSERT INTO `bill` VALUES ('83', '苹果', '21', '232123', '进货', '23', '2', '2017-08-28');
INSERT INTO `bill` VALUES ('84', '农夫三泉', '543', '34232', '进货', '36', '453', '2017-08-28');
INSERT INTO `bill` VALUES ('85', '联想3', '122', '10000', '进货', '29', '完全', '2017-08-28');
INSERT INTO `bill` VALUES ('86', '联想4', '122', '10000', '进货', '29', '完全', '2017-08-28');

-- ----------------------------
-- Table structure for supplier
-- ----------------------------
DROP TABLE IF EXISTS `supplier`;
CREATE TABLE `supplier` (
  `sid` int(11) NOT NULL AUTO_INCREMENT,
  `sname` varchar(50) NOT NULL,
  `sdescription` varchar(50) DEFAULT NULL,
  `linkman` varchar(50) NOT NULL,
  `phoneno` varchar(50) DEFAULT NULL,
  `addr` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`sid`),
  UNIQUE KEY `sname` (`sname`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of supplier
-- ----------------------------
INSERT INTO `supplier` VALUES ('21', '华为', '		', '胡海丰', '18158939927', '南京');
INSERT INTO `supplier` VALUES ('23', '苹果公司', '我', '胡海丰', '18158939927', '南京');
INSERT INTO `supplier` VALUES ('25', '诺基亚', 'd', '胡海丰', '213214124', '南京');
INSERT INTO `supplier` VALUES ('26', 'VIVO', '智能手机', '胡海丰', '1318547548', '南京');
INSERT INTO `supplier` VALUES ('29', '联想', '智能手机', '胡海丰', '1318547548', '南京');
INSERT INTO `supplier` VALUES ('31', '戴尔1', '合肥', '胡海丰', '1721987217', '合肥');
INSERT INTO `supplier` VALUES ('32', '可口可乐', '合肥', '胡海丰', '1721987217', '合肥');
INSERT INTO `supplier` VALUES ('33', '芬达', '合肥', '胡海丰', '1721987217', '合肥');
INSERT INTO `supplier` VALUES ('34', '芬达1', '合肥', '胡海丰', '1721987217', '合肥');
INSERT INTO `supplier` VALUES ('35', '安踏', '合肥', '胡海丰', '1721987217', '合肥');
INSERT INTO `supplier` VALUES ('36', '农夫三泉', '合肥', '胡海丰', '1721987217', '合肥');
INSERT INTO `supplier` VALUES ('37', '娃哈哈', '23', '王香', '23423423324', '南京');
INSERT INTO `supplier` VALUES ('38', '华为9', '国产手机', '张三', '18118228381', '南京');
INSERT INTO `supplier` VALUES ('39', '金麦浪', '3', '胡', '213123214', '2312125');
INSERT INTO `supplier` VALUES ('40', 'IOS', '	', 'huhaifeng  ', '12423432', '234');
INSERT INTO `supplier` VALUES ('41', '  361', 'Ｗ', ' ＨＨＦ', '13122141243', '2ＥＥＷ');
INSERT INTO `supplier` VALUES ('42', '  360', 'Ｗ', ' ＨＨＦ', '13122141243', '2ＥＥＷ');
SET FOREIGN_KEY_CHECKS=1;
