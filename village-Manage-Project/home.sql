/*
SQLyog Community v12.4.3 (64 bit)
MySQL - 5.6.37-log : Database - home
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`home` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `home`;

/*Table structure for table `admin` */

DROP TABLE IF EXISTS `admin`;

CREATE TABLE `admin` (
  `id` int(4) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(20) NOT NULL COMMENT '用户名',
  `password` varchar(36) NOT NULL COMMENT '密码',
  `token` varchar(36) NOT NULL COMMENT '用户密码加密字符串',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Data for the table `admin` */

insert  into `admin`(`id`,`name`,`password`,`token`) values 
(1,'admin','123456','123456');

/*Table structure for table `alertinfo` */

DROP TABLE IF EXISTS `alertinfo`;

CREATE TABLE `alertinfo` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `alertName` int(2) NOT NULL COMMENT '警报事项',
  `time` datetime NOT NULL COMMENT '警报时间',
  `safeMin` int(5) NOT NULL DEFAULT '0' COMMENT '安全最小数值',
  `safeMax` int(5) NOT NULL DEFAULT '0' COMMENT '安全最大数值',
  `userId` int(5) NOT NULL COMMENT '用户关联id',
  `value` int(5) NOT NULL DEFAULT '0' COMMENT '警报时的值',
  PRIMARY KEY (`id`),
  KEY `fk_user` (`userId`),
  KEY `alertName` (`alertName`),
  CONSTRAINT `alertName` FOREIGN KEY (`alertName`) REFERENCES `alertselection` (`id`),
  CONSTRAINT `fk_user` FOREIGN KEY (`userId`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

/*Data for the table `alertinfo` */

insert  into `alertinfo`(`id`,`alertName`,`time`,`safeMin`,`safeMax`,`userId`,`value`) values 
(1,2,'2018-05-26 18:36:09',10,20,1,0),
(2,2,'2018-05-26 18:36:09',10,20,2,0),
(4,2,'2018-05-26 18:37:22',10,20,1,0),
(5,2,'2018-05-26 18:41:41',10,20,1,10),
(6,2,'2018-05-26 16:36:09',10,20,1,10),
(7,2,'2018-01-25 12:00:00',10,20,1,1),
(8,1,'2018-05-28 14:49:15',20,30,1,35),
(9,2,'2018-05-28 15:31:20',10,20,1,23);

/*Table structure for table `alertselection` */

DROP TABLE IF EXISTS `alertselection`;

CREATE TABLE `alertselection` (
  `id` int(2) NOT NULL AUTO_INCREMENT COMMENT '警报id',
  `alertName` varchar(10) NOT NULL COMMENT '警报名称',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNIQUE` (`alertName`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

/*Data for the table `alertselection` */

insert  into `alertselection`(`id`,`alertName`) values 
(2,'天然气'),
(3,'温度'),
(4,'湿度'),
(1,'煤气');

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` int(5) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(20) NOT NULL COMMENT '用户名',
  `pass` varchar(36) NOT NULL COMMENT '密码',
  `token` varchar(36) NOT NULL COMMENT '密码加密字符串',
  `build` int(5) NOT NULL COMMENT '栋数',
  `unit` int(5) DEFAULT NULL COMMENT '单元',
  `room` varchar(20) NOT NULL COMMENT '房间号 楼层+号数',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

/*Data for the table `user` */

insert  into `user`(`id`,`name`,`pass`,`token`,`build`,`unit`,`room`) values 
(1,'张三','123456','123456',0,1,'1101'),
(2,'李四','123456','123456',0,1,'1002'),
(3,'王五','123456','123456',0,2,'1001');

/*Table structure for table `warningconfig` */

DROP TABLE IF EXISTS `warningconfig`;

CREATE TABLE `warningconfig` (
  `id` int(5) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `alertName` int(2) NOT NULL COMMENT '警报类型',
  `safeMin` int(5) NOT NULL DEFAULT '0' COMMENT '安全最小数值',
  `safeMax` int(5) NOT NULL DEFAULT '0' COMMENT '安全最大数值',
  `userId` int(5) NOT NULL COMMENT '用户的关联id',
  PRIMARY KEY (`id`),
  KEY `user` (`userId`),
  KEY `fk_alertName` (`alertName`),
  CONSTRAINT `fk_alertName` FOREIGN KEY (`alertName`) REFERENCES `alertselection` (`id`),
  CONSTRAINT `user` FOREIGN KEY (`userId`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

/*Data for the table `warningconfig` */

insert  into `warningconfig`(`id`,`alertName`,`safeMin`,`safeMax`,`userId`) values 
(1,1,10,20,1),
(2,2,10,20,1),
(3,3,10,20,1),
(4,4,10,20,1),
(5,1,10,20,2),
(6,2,10,20,2),
(7,3,10,20,2),
(8,4,10,20,2);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
