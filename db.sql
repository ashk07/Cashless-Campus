/*
SQLyog Community Edition- MySQL GUI v7.01 
MySQL - 5.0.27-community-nt : Database - cashlesscampus
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

CREATE DATABASE /*!32312 IF NOT EXISTS*/`cashlesscampus` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `cashlesscampus`;

/*Table structure for table `items` */

DROP TABLE IF EXISTS `items`;

CREATE TABLE `items` (
  `id` int(11) NOT NULL auto_increment,
  `filename` varchar(255) default NULL,
  `name` varchar(255) default NULL,
  `price` varchar(255) default NULL,
  `category` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `items` */

insert  into `items`(`id`,`filename`,`name`,`price`,`category`) values (2,'static/Library/image-20240327-174646.jpg','The kite runner','-','Library'),(3,'static/Stationary/image-20240311-162335.jpg','Roof of home','12','Stationary'),(4,'static/Canteen/image-20240311-125908.jpg','Pizza butter chicken','23','Canteen'),(6,'static/Canteen/image-20240327-182426.jpg','Harry potter','36','Canteen'),(7,'static/Canteen/image-20240327-170440.jpg','Leaffy salad','16','Canteen'),(8,'static/Canteen/image-20240327-170501.jpg','Mashroom kabab','23','Canteen'),(9,'static/Library/image-20240327-182503.jpg','Earpods','-','Library'),(10,'static/Stationary/image-20240327-170607.jpg','Dell laptop','30','Stationary'),(11,'static/Library/image-20240327-174755.jpg','Little women','-','Library'),(12,'static/Library/image-20240327-174829.jpg','comic','-','Library'),(13,'static/Library/image-20240327-181801.jpg','hhhh','-','Library'),(14,'static/Canteen/image-20240327-181845.jpg','Bag','36','Canteen'),(15,'static/Event/image-20240331-004723.jpg','Dance event','10','Event'),(16,'static/Event/image-20240331-013227.jpg','Coding event','10','Event');

/*Table structure for table `orders` */

DROP TABLE IF EXISTS `orders`;

CREATE TABLE `orders` (
  `orderid` int(255) NOT NULL auto_increment,
  `studid` varchar(255) default NULL,
  `studcat` varchar(255) default NULL,
  `pname` varchar(255) default NULL,
  `pcat` varchar(255) default NULL,
  `price` varchar(255) default NULL,
  `datetime` varchar(255) default NULL,
  `status` varchar(255) default '-',
  PRIMARY KEY  (`orderid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `orders` */

insert  into `orders`(`orderid`,`studid`,`studcat`,`pname`,`pcat`,`price`,`datetime`,`status`) values (1,'VU1F20210608','IT','Harry potter','Canteen','36','27-03-2024 23:48','-'),(2,'VU1F20210608','IT','Pizza butter chicken','Canteen','23','27-03-2024 23:49','-'),(3,'VU1F20210608','IT','The kite runner','Library','-','25-03-2024 23:49','20'),(4,'RYEj63749878','CS','Dell laptop','Stationary','30','27-03-2024 23:50','-'),(5,'RYEj63749878','CS','Pizza butter chicken','Canteen','23','28-03-2024 23:50','-'),(6,'RYEj63749878','CS','Little women','Library','-','25-03-2024 23:50','20'),(7,'RYEj63749878','CS','hhhh','Library','-','23-03-2024 23:51','40'),(8,'VU1F20210608','IT','Dell laptop','Stationary','30','28-03-2024 16:19','-'),(9,'VU1F20210608','IT','Coding event','Event','10','31-03-2024 01:58','-'),(10,'VU1F20210608','IT','Coding event','Event','10','31-03-2024 02:06','-'),(11,'VU1F20210608','IT','comic','Library','-','01-04-2024 10:39','-'),(12,'VU1F20210608','IT','Harry potter','Canteen','36','01-04-2024 15:15','-'),(13,'VU1F20210608','IT','hhhh','Library','-','01-04-2024 15:16','20'),(14,'VU1F20210608','IT','Dance event','Event','10','01-04-2024 15:17','-'),(15,'VU1F2021057','Computer','Bag','Canteen','36','01-04-2024 15:38','-'),(16,'VU1F20210608','IT','Dell laptop','Stationary','30','03-04-2024 22:24','-'),(17,'VU1F20210608','IT','Mashroom kabab','Canteen','23','04-04-2024 15:15','-');

/*Table structure for table `register` */

DROP TABLE IF EXISTS `register`;

CREATE TABLE `register` (
  `id` int(11) NOT NULL auto_increment,
  `fname` varchar(255) default NULL,
  `lname` varchar(255) default NULL,
  `cid` varchar(255) default NULL,
  `branch` varchar(255) default NULL,
  `year` varchar(255) default NULL,
  `email` varchar(255) default NULL,
  `mobile` varchar(255) default NULL,
  `password` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `register` */

insert  into `register`(`id`,`fname`,`lname`,`cid`,`branch`,`year`,`email`,`mobile`,`password`) values (1,'Yash','Salvi','VU1F20210608','IT','2025','yashsalvi1999@gmail.com','5859658458','yash'),(2,'Amol','Nerlekar','Vyke34569809','EXTC','2005','recordings.projects@gmail.com','3658965352','amol'),(3,'Pratham','Mane','Pratg3458903','AIDS','2010','yashsalvi1999@gmail.com','3652853652','pra'),(5,'Roshan','Mund','RYEj63749878','CS','2022','recordings.projects@gmail.com','9658569585','Roshan'),(6,'Aniket','Bhatkar','VU1F2021057','Computer','2024','aniketbhatkar2002@gmail.com','6398585235','aniket');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
