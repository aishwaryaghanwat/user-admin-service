CREATE TABLE IF NOT EXISTS `country` (
  `id` bigint NOT NULL,
  `country_code` varchar(5) NOT NULL,
  `country_name` varchar(80) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;