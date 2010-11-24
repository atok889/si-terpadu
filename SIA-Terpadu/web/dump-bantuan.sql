/*
SQLyog Enterprise - MySQL GUI v8.1 
MySQL - 5.0.18-nt : Database - usd
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

CREATE DATABASE /*!32312 IF NOT EXISTS*/`usd` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `usd`;

/*Table structure for table `bantuan` */

CREATE TABLE `bantuan` (
  `id` int(6) NOT NULL auto_increment,
  `nama` varchar(200) NOT NULL,
  `jenis` varchar(100) NOT NULL,
  `pesan` text NOT NULL,
  `tanggal` date NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

/*Data for the table `bantuan` */

insert  into `bantuan`(id,nama,jenis,pesan,tanggal) values (1,'Hendro','Kritik','Testing','2010-11-24');
insert  into `bantuan`(id,nama,jenis,pesan,tanggal) values (2,'Hendro','Kritik','Uji Coba','2010-11-24');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;