/*
SQLyog Community v12.4.3 (64 bit)
MySQL - 5.6.37-log : Database - mxsoha
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`mxsoha` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci */;

USE `mxsoha`;

/*Table structure for table `records` */

DROP TABLE IF EXISTS `records`;

CREATE TABLE `records` (
  `id` int(10) NOT NULL,
  `roomid` int(3) NOT NULL COMMENT '房间号',
  `date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '日期',
  `do` varchar(20) COLLATE utf8_unicode_ci NOT NULL COMMENT '操作',
  `score` int(10) NOT NULL COMMENT '分数变化',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `records` */

/*Table structure for table `users` */

DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `username` varchar(10) COLLATE utf8_unicode_ci NOT NULL COMMENT '用户名',
  `password` varchar(36) COLLATE utf8_unicode_ci NOT NULL COMMENT '用户密码',
  `token` varchar(36) COLLATE utf8_unicode_ci NOT NULL COMMENT '密码随机验证字符',
  `ip` varchar(15) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '上一次登录ip',
  `phone` varchar(11) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '手机号',
  `money` double(10,2) NOT NULL DEFAULT '0.00' COMMENT '当前余额',
  `status` int(1) NOT NULL DEFAULT '0' COMMENT '当前状态 0离线 1在线 2非法操作已冻结',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_name` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `users` */

insert  into `users`(`id`,`username`,`password`,`token`,`ip`,`phone`,`money`,`status`) values 
(1,'admin','YfI0ZOD8Cl6vbyQ0wCEW+A==','6b9f55ffcbdd4b07ba58475a903d18bd','127.0.0.1',NULL,0.00,1);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
