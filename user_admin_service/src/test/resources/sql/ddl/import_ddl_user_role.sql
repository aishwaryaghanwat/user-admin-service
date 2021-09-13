CREATE TABLE IF NOT EXISTS `user_role` (
  `id` bigint NOT NULL,
  `role_name` varchar(20) NOT NULL,
  `role_type` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;