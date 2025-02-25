
CREATE TABLE IF NOT EXISTS `book` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name_book` varchar(240) NOT NULL,
  `name_author` varchar(120) NOT NULL,
  `description` varchar(120) NOT NULL,
  `gender` varchar(80) NOT NULL,
 
  PRIMARY KEY (`id`)
);


