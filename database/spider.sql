# Host: localhost  (Version: 5.6.24)
# Date: 2015-11-24 09:00:00
# Generator: MySQL-Front 5.3  (Build 4.214)

/*!40101 SET NAMES utf8 */;

#
# Structure for table "comment"
#

DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
  `id` varchar(255) NOT NULL DEFAULT '',
  `topic_id` varchar(255) DEFAULT NULL,
  `author` varchar(255) DEFAULT NULL,
  `create_date` varchar(255) DEFAULT NULL,
  `content` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Data for table "comment"
#


#
# Structure for table "discussion"
#

DROP TABLE IF EXISTS `discussion`;
CREATE TABLE `discussion` (
  `id` varchar(255) NOT NULL DEFAULT '',
  `title` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Data for table "discussion"
#


#
# Structure for table "topic"
#

DROP TABLE IF EXISTS `topic`;
CREATE TABLE `topic` (
  `id` varchar(255) NOT NULL DEFAULT '',
  `discussion_id` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `author` varchar(255) DEFAULT NULL,
  `create_date` varchar(255) DEFAULT NULL,
  `comment_num` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Data for table "topic"
#

