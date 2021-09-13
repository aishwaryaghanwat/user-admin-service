CREATE TABLE IF NOT EXISTS `user_status` (
  `id` bigint NOT NULL,
  `user_status_code` varchar(20) NOT NULL,
  `user_status_description` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_euudwd450lv7fhfo7labyw2s7` (`user_status_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;