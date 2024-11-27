CREATE TABLE `attachments` (
  `attachment_id` int NOT NULL AUTO_INCREMENT,
  `board_no` int NOT NULL,
  `file_name` varchar(255) NOT NULL,
  `file_size` int NOT NULL,
  `file_type` varchar(100) NOT NULL,
  `file_path` varchar(255) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`attachment_id`),
  KEY `fk_board_attachments` (`board_no`),
  CONSTRAINT `fk_board_attachments` FOREIGN KEY (`board_no`) REFERENCES `board` (`board_no`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `board` (
  `board_no` int NOT NULL AUTO_INCREMENT,
  `user_no` int DEFAULT NULL,
  `subject` varchar(100) DEFAULT '',
  `content` varchar(2000) DEFAULT '',
  `hit` int DEFAULT '0',
  `register_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`board_no`),
  KEY `user_no` (`user_no`),
  CONSTRAINT `board_ibfk_1` FOREIGN KEY (`user_no`) REFERENCES `users` (`user_no`)
) ENGINE=InnoDB AUTO_INCREMENT=116 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `dongcodes` (
  `sido_name` varchar(30) DEFAULT NULL,
  `gugun_name` varchar(30) DEFAULT NULL,
  `dong1` varchar(30) DEFAULT NULL,
  `dong2` varchar(30) DEFAULT NULL,
  `latitude` varchar(45) DEFAULT NULL,
  `longitude` varchar(45) DEFAULT NULL,
  `dong_name` varchar(60) DEFAULT NULL,
  `dong_code` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `favorite_house` (
  `id` int NOT NULL AUTO_INCREMENT,
  `apt_seq` varchar(20) NOT NULL,
  `user_no` int NOT NULL,
  `house_type` varchar(20) NOT NULL,
  `selected_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_apt_user` (`apt_seq`,`user_no`,`house_type`),
  KEY `user_no` (`user_no`),
  CONSTRAINT `favorite_house_ibfk_1` FOREIGN KEY (`apt_seq`) REFERENCES `houseInfos` (`apt_seq`),
  CONSTRAINT `favorite_house_ibfk_2` FOREIGN KEY (`user_no`) REFERENCES `users` (`user_no`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `favorite_realestate` (
  `id` int NOT NULL AUTO_INCREMENT,
  `realestate_id` int NOT NULL,
  `user_no` int NOT NULL,
  `deal_category` varchar(20) NOT NULL,
  `house_type` varchar(20) NOT NULL,
  `selected_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `user_no` (`user_no`),
  KEY `favorite_realestate_ibfk_1` (`realestate_id`),
  CONSTRAINT `favorite_realestate_ibfk_1` FOREIGN KEY (`realestate_id`) REFERENCES `realestateinfos` (`id`),
  CONSTRAINT `favorite_realestate_ibfk_2` FOREIGN KEY (`user_no`) REFERENCES `users` (`user_no`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `guguncode` (
  `gugunCode` varchar(10) NOT NULL,
  `gugunName` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`gugunCode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `guide` (
  `id` int NOT NULL AUTO_INCREMENT,
  `category` varchar(100) NOT NULL,
  `content` text NOT NULL,
  `url` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `house_comments` (
  `comment_no` int NOT NULL AUTO_INCREMENT,
  `user_no` int DEFAULT NULL,
  `apt_seq` varchar(20) NOT NULL,
  `house_type` varchar(20) NOT NULL,
  `content` varchar(2000) DEFAULT '',
  `register_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`comment_no`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `housedeals` (
  `no` int NOT NULL AUTO_INCREMENT,
  `apt_seq` varchar(20) DEFAULT NULL,
  `apt_dong` varchar(40) DEFAULT NULL,
  `floor` varchar(3) DEFAULT NULL,
  `deal_year` int DEFAULT NULL,
  `deal_month` int DEFAULT NULL,
  `deal_day` int DEFAULT NULL,
  `exclu_use_ar` decimal(7,2) DEFAULT NULL,
  `deal_amount` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`no`),
  KEY `apt_seq_to_house_info_idx` (`apt_seq`),
  KEY `idx_apt_seq_exclu_use_ar` (`apt_seq`,`exclu_use_ar`),
  KEY `idx_deal_date` (`deal_year`,`deal_month`,`deal_day`),
  CONSTRAINT `apt_seq_to_house_info` FOREIGN KEY (`apt_seq`) REFERENCES `houseinfos` (`apt_seq`)
) ENGINE=InnoDB AUTO_INCREMENT=7084512 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `housedeals_jeonse` (
  `no` int NOT NULL AUTO_INCREMENT,
  `apt_seq` varchar(20) DEFAULT NULL,
  `apt_dong` varchar(40) DEFAULT NULL,
  `floor` varchar(3) DEFAULT NULL,
  `deal_year` int DEFAULT NULL,
  `deal_month` int DEFAULT NULL,
  `deal_day` int DEFAULT NULL,
  `exclu_use_ar` decimal(7,2) DEFAULT NULL,
  `deal_amount` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`no`),
  KEY `idx_apt_seq_exclu_use_ar` (`apt_seq`,`exclu_use_ar`),
  KEY `idx_deal_date` (`deal_year`,`deal_month`,`deal_day`)
) ENGINE=InnoDB AUTO_INCREMENT=13506942 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `housedeals_month` (
  `no` int NOT NULL AUTO_INCREMENT,
  `apt_seq` varchar(20) DEFAULT NULL,
  `apt_dong` varchar(40) DEFAULT NULL,
  `floor` varchar(3) DEFAULT NULL,
  `deal_year` int DEFAULT NULL,
  `deal_month` int DEFAULT NULL,
  `deal_day` int DEFAULT NULL,
  `exclu_use_ar` decimal(7,2) DEFAULT NULL,
  `deal_amount` varchar(10) DEFAULT NULL,
  `fee_amount` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`no`),
  KEY `idx_apt_seq_exclu_use_ar` (`apt_seq`,`exclu_use_ar`),
  KEY `idx_deal_date` (`deal_year`,`deal_month`,`deal_day`)
) ENGINE=InnoDB AUTO_INCREMENT=13506942 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `officeteldeals` (
  `no` int NOT NULL AUTO_INCREMENT,
  `apt_seq` varchar(20) DEFAULT NULL,
  `apt_dong` varchar(40) DEFAULT NULL,
  `floor` varchar(3) DEFAULT NULL,
  `deal_year` int DEFAULT NULL,
  `deal_month` int DEFAULT NULL,
  `deal_day` int DEFAULT NULL,
  `exclu_use_ar` decimal(7,2) DEFAULT NULL,
  `deal_amount` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`no`),
  KEY `apt_seq_to_house_info_idx` (`apt_seq`),
  KEY `idx_apt_seq_exclu_use_ar` (`apt_seq`,`exclu_use_ar`),
  KEY `idx_deal_date` (`deal_year`,`deal_month`,`deal_day`),
  CONSTRAINT `apt_seq_to_officetel_info` FOREIGN KEY (`apt_seq`) REFERENCES `officetelinfos` (`apt_seq`)
) ENGINE=InnoDB AUTO_INCREMENT=7084512 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `officeteldeals_jeonse` (
  `no` int NOT NULL AUTO_INCREMENT,
  `apt_seq` varchar(20) DEFAULT NULL,
  `apt_dong` varchar(40) DEFAULT NULL,
  `floor` varchar(3) DEFAULT NULL,
  `deal_year` int DEFAULT NULL,
  `deal_month` int DEFAULT NULL,
  `deal_day` int DEFAULT NULL,
  `exclu_use_ar` decimal(7,2) DEFAULT NULL,
  `deal_amount` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`no`),
  KEY `idx_apt_seq_exclu_use_ar` (`apt_seq`,`exclu_use_ar`),
  KEY `idx_deal_date` (`deal_year`,`deal_month`,`deal_day`)
) ENGINE=InnoDB AUTO_INCREMENT=13506942 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `officeteldeals_month` (
  `no` int NOT NULL AUTO_INCREMENT,
  `apt_seq` varchar(20) DEFAULT NULL,
  `apt_dong` varchar(40) DEFAULT NULL,
  `floor` varchar(3) DEFAULT NULL,
  `deal_year` int DEFAULT NULL,
  `deal_month` int DEFAULT NULL,
  `deal_day` int DEFAULT NULL,
  `exclu_use_ar` decimal(7,2) DEFAULT NULL,
  `deal_amount` varchar(10) DEFAULT NULL,
  `fee_amount` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`no`),
  KEY `idx_apt_seq_exclu_use_ar` (`apt_seq`,`exclu_use_ar`),
  KEY `idx_deal_date` (`deal_year`,`deal_month`,`deal_day`)
) ENGINE=InnoDB AUTO_INCREMENT=13506942 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `officetelinfos` (
  `apt_seq` varchar(20) NOT NULL,
  `sgg_cd` varchar(5) DEFAULT NULL,
  `umd_cd` varchar(5) DEFAULT NULL,
  `umd_nm` varchar(20) DEFAULT NULL,
  `jibun` varchar(10) DEFAULT NULL,
  `road_nm_sgg_cd` varchar(5) DEFAULT NULL,
  `road_nm` varchar(20) DEFAULT NULL,
  `road_nm_bonbun` varchar(10) DEFAULT NULL,
  `road_nm_bubun` varchar(10) DEFAULT NULL,
  `apt_nm` varchar(40) DEFAULT NULL,
  `build_year` int DEFAULT NULL,
  `latitude` varchar(45) DEFAULT NULL,
  `longitude` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`apt_seq`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `officetelrecentdeals` (
  `id` int NOT NULL AUTO_INCREMENT,
  `apt_seq` varchar(20) NOT NULL,
  `deal_space` int NOT NULL,
  `avg_deal_amount` decimal(10,2) DEFAULT NULL,
  `avg_fee_amount` decimal(10,2) DEFAULT NULL,
  `deal_count` int DEFAULT '0',
  `most_deal_type` varchar(10) DEFAULT NULL,
  `deal_category` varchar(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `apt_seq` (`apt_seq`),
  CONSTRAINT `officetelrecentdeals_ibfk_1` FOREIGN KEY (`apt_seq`) REFERENCES `officetelinfos` (`apt_seq`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=524281 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `realestateinfos` (
  `id` int NOT NULL AUTO_INCREMENT,
  `apt_seq` varchar(20) NOT NULL,
  `max_floor` varchar(3) DEFAULT NULL,
  `this_floor` varchar(3) DEFAULT NULL,
  `register_year` int DEFAULT NULL,
  `register_month` int DEFAULT NULL,
  `register_day` int DEFAULT NULL,
  `exclu_use_ar` decimal(7,2) DEFAULT NULL,
  `deal_amount` varchar(10) DEFAULT NULL,
  `fee_amount` varchar(10) DEFAULT NULL,
  `maintenance_cost` varchar(10) DEFAULT NULL,
  `deal_category` varchar(20) NOT NULL,
  `house_type` varchar(20) NOT NULL,
  `register_user_no` int NOT NULL COMMENT '등록한 유저 번호',
  PRIMARY KEY (`id`),
  KEY `idx_space_category_aptseq` (`exclu_use_ar`,`deal_category`,`apt_seq`),
  KEY `idx_register_date` (`register_year`,`register_month`,`register_day`),
  KEY `idx_register_user_no` (`register_user_no`)
) ENGINE=InnoDB AUTO_INCREMENT=22281903 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `sidocode` (
  `sidoCode` varchar(10) NOT NULL,
  `sidoName` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`sidoCode`),
  UNIQUE KEY `sidoName` (`sidoName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `users` (
  `user_no` int NOT NULL AUTO_INCREMENT,
  `user_name` varchar(50) NOT NULL,
  `user_password` varchar(50) NOT NULL,
  `user_nickname` varchar(50) NOT NULL,
  `user_email` varchar(255) NOT NULL,
  `phone_number` varchar(20) DEFAULT NULL,
  `role` varchar(30) DEFAULT NULL,
  `register_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted` tinyint(1) DEFAULT '0',
  `deleted_at` timestamp NULL DEFAULT NULL,
  `reusersfresh_token` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`user_no`),
  UNIQUE KEY `user_email` (`user_email`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `villadeals` (
  `no` int NOT NULL AUTO_INCREMENT,
  `apt_seq` varchar(20) DEFAULT NULL,
  `apt_dong` varchar(40) DEFAULT NULL,
  `floor` varchar(3) DEFAULT NULL,
  `deal_year` int DEFAULT NULL,
  `deal_month` int DEFAULT NULL,
  `deal_day` int DEFAULT NULL,
  `exclu_use_ar` decimal(7,2) DEFAULT NULL,
  `deal_amount` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`no`),
  KEY `apt_seq_to_house_info_idx` (`apt_seq`),
  KEY `idx_apt_seq_exclu_use_ar` (`apt_seq`,`exclu_use_ar`),
  KEY `idx_deal_date` (`deal_year`,`deal_month`,`deal_day`),
  CONSTRAINT `apt_seq_to_villa_info` FOREIGN KEY (`apt_seq`) REFERENCES `villainfos` (`apt_seq`)
) ENGINE=InnoDB AUTO_INCREMENT=7084512 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `villadeals_jeonse` (
  `no` int NOT NULL AUTO_INCREMENT,
  `apt_seq` varchar(20) DEFAULT NULL,
  `apt_dong` varchar(40) DEFAULT NULL,
  `floor` varchar(3) DEFAULT NULL,
  `deal_year` int DEFAULT NULL,
  `deal_month` int DEFAULT NULL,
  `deal_day` int DEFAULT NULL,
  `exclu_use_ar` decimal(7,2) DEFAULT NULL,
  `deal_amount` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`no`),
  KEY `idx_apt_seq_exclu_use_ar` (`apt_seq`,`exclu_use_ar`),
  KEY `idx_deal_date` (`deal_year`,`deal_month`,`deal_day`)
) ENGINE=InnoDB AUTO_INCREMENT=13506942 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `villadeals_month` (
  `no` int NOT NULL AUTO_INCREMENT,
  `apt_seq` varchar(20) DEFAULT NULL,
  `apt_dong` varchar(40) DEFAULT NULL,
  `floor` varchar(3) DEFAULT NULL,
  `deal_year` int DEFAULT NULL,
  `deal_month` int DEFAULT NULL,
  `deal_day` int DEFAULT NULL,
  `exclu_use_ar` decimal(7,2) DEFAULT NULL,
  `deal_amount` varchar(10) DEFAULT NULL,
  `fee_amount` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`no`),
  KEY `idx_apt_seq_exclu_use_ar` (`apt_seq`,`exclu_use_ar`),
  KEY `idx_deal_date` (`deal_year`,`deal_month`,`deal_day`)
) ENGINE=InnoDB AUTO_INCREMENT=13506942 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `villainfos` (
  `apt_seq` varchar(20) NOT NULL,
  `sgg_cd` varchar(5) DEFAULT NULL,
  `umd_cd` varchar(5) DEFAULT NULL,
  `umd_nm` varchar(20) DEFAULT NULL,
  `jibun` varchar(10) DEFAULT NULL,
  `road_nm_sgg_cd` varchar(5) DEFAULT NULL,
  `road_nm` varchar(20) DEFAULT NULL,
  `road_nm_bonbun` varchar(10) DEFAULT NULL,
  `road_nm_bubun` varchar(10) DEFAULT NULL,
  `apt_nm` varchar(40) DEFAULT NULL,
  `build_year` int DEFAULT NULL,
  `latitude` varchar(45) DEFAULT NULL,
  `longitude` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`apt_seq`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `villarecentdeals` (
  `id` int NOT NULL AUTO_INCREMENT,
  `apt_seq` varchar(20) NOT NULL,
  `deal_space` int NOT NULL,
  `avg_deal_amount` decimal(10,2) DEFAULT NULL,
  `avg_fee_amount` decimal(10,2) DEFAULT NULL,
  `deal_count` int DEFAULT '0',
  `most_deal_type` varchar(10) DEFAULT NULL,
  `deal_category` varchar(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `apt_seq` (`apt_seq`),
  CONSTRAINT `villarecentdeals_ibfk_1` FOREIGN KEY (`apt_seq`) REFERENCES `villainfos` (`apt_seq`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=524281 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;