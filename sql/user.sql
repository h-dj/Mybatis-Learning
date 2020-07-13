CREATE TABLE `user` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `age` int(11) NOT NULL,
  PRIMARY KEY (`id`)
);

insert into `user` (`id`,`name`,`age`) values (1, 'Jerry', 27);
insert into `user` (`id`,`name`,`age`) values (2, 'Angel', 25);