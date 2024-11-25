
SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';
-- -----------------------------------------------------
-- Schema home
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `home` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;

-- -----------------------------------------------------
-- Table `home`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `home`.`users` (
  `user_no` INT auto_increment primary key,
  `user_name` VARCHAR(50) NOT NULL,
  `user_password` VARCHAR(50) NOT NULL,
  `user_nickname` VARCHAR(50) NOT NULL,
  `user_email` VARCHAR(255) NOT NULL UNIQUE,
  `phone_number` VARCHAR(20) NULL DEFAULT NULL,
  `role` VARCHAR(30) NULL DEFAULT NULL,
  `register_time` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted` TINYINT(1) DEFAULT FALSE,
  `deleted_at` TIMESTAMP NULL DEFAULT NULL,
  `reusersfresh_token` VARCHAR(255) NULL DEFAULT NULL
);


-- -----------------------------------------------------
-- Table `home`.`board`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `home`.`board` (
  `board_no` INT AUTO_INCREMENT PRIMARY KEY,
  `user_no` INT,
  `subject` VARCHAR(100) DEFAULT '',
  `content` VARCHAR(2000) DEFAULT '',
  `hit` INT NULL DEFAULT '0',
  `register_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (`user_no`) REFERENCES users(`user_no`)
);

-- 첨부파일 테이블
CREATE TABLE IF NOT EXISTS `home`.`attachments` (
  `attachment_id` INT NOT NULL AUTO_INCREMENT,
  `board_no` INT NOT NULL,
  `file_name` VARCHAR(255) NOT NULL,
  `file_size` INT NOT NULL,
  `file_type` VARCHAR(100) NOT NULL,
  `file_path` VARCHAR(255) NOT NULL,
  `created_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`attachment_id`),
  CONSTRAINT `fk_board_attachments` 
  FOREIGN KEY (`board_no`) 
  REFERENCES `home`.`board` (`board_no`)
  ON DELETE CASCADE
);

-- 안되면
ALTER TABLE `home`.`attachments` 
DROP FOREIGN KEY `attachments_ibfk_1`;

-- CREATE TABLE과 동일한 설정으로 제약조건 다시 생성
ALTER TABLE `home`.`attachments`
ADD CONSTRAINT `fk_board_attachments`
FOREIGN KEY (`board_no`)
REFERENCES `home`.`board` (`board_no`)
ON DELETE CASCADE;

-- -----------------------------------------------------
-- Table `home`.`sidocode`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `home`.`sidocode` (
  `sidoCode` VARCHAR(10) NOT NULL,
  `sidoName` VARCHAR(30) NULL DEFAULT NULL,
  PRIMARY KEY (`sidoCode`),
  UNIQUE INDEX `sidoName` (`sidoName` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `home`.`dongcode`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `home`.`dongcode` (
  `dongCode` VARCHAR(10) NOT NULL,
  `sidoName` VARCHAR(30) NULL DEFAULT NULL,
  `gugunName` VARCHAR(30) NULL DEFAULT NULL,
  `dongName` VARCHAR(30) NULL DEFAULT NULL,
  PRIMARY KEY (`dongCode`),
  INDEX `sidoName` (`sidoName` ASC) VISIBLE,
  CONSTRAINT `dongcode_ibfk_1`
    FOREIGN KEY (`sidoName`)
    REFERENCES `home`.`sidocode` (`sidoName`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `home`.`houseinfos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `home`.`houseinfos` (
  `apt_seq` VARCHAR(20) NOT NULL,
  `sgg_cd` VARCHAR(5) NULL DEFAULT NULL,
  `umd_cd` VARCHAR(5) NULL DEFAULT NULL,
  `umd_nm` VARCHAR(20) NULL DEFAULT NULL,
  `jibun` VARCHAR(10) NULL DEFAULT NULL,
  `road_nm_sgg_cd` VARCHAR(5) NULL DEFAULT NULL,
  `road_nm` VARCHAR(20) NULL DEFAULT NULL,
  `road_nm_bonbun` VARCHAR(10) NULL DEFAULT NULL,
  `road_nm_bubun` VARCHAR(10) NULL DEFAULT NULL,
  `apt_nm` VARCHAR(40) NULL DEFAULT NULL,
  `build_year` INT NULL DEFAULT NULL,
  `latitude` VARCHAR(45) NULL DEFAULT NULL,
  `longitude` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`apt_seq`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

-- -----------------------------------------------------
-- Table `home`.`guguncode`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `home`.`guguncode` (
  `gugunCode` VARCHAR(10) NOT NULL,
  `gugunName` VARCHAR(30) NULL DEFAULT NULL,
  PRIMARY KEY (`gugunCode`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `home`.`housedeals`
-- -----------------------------------------------------
CREATE TABLE `houserecentdeals` (
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
  CONSTRAINT `houserecentdeals_ibfk_1` FOREIGN KEY (`apt_seq`) REFERENCES `houseinfos` (`apt_seq`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=524281 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE housedeals_jeonse (
  no int NOT NULL AUTO_INCREMENT,
  apt_seq varchar(20) DEFAULT NULL,
  apt_dong varchar(40) DEFAULT NULL,
  floor varchar(3) DEFAULT NULL,
  deal_year int DEFAULT NULL,
  deal_month int DEFAULT NULL,
  deal_day int DEFAULT NULL,
  exclu_use_ar decimal(7,2) DEFAULT NULL,
  deal_amount varchar(10) DEFAULT NULL,
  PRIMARY KEY (no)
) ENGINE=InnoDB AUTO_INCREMENT=7084512 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

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
  PRIMARY KEY (`no`)
) ENGINE=InnoDB AUTO_INCREMENT=7084512 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
    
    
    
INSERT INTO housedeals_month (
    apt_seq, apt_dong, floor, deal_year, deal_month, deal_day, exclu_use_ar, deal_amount, fee_amount
)
SELECT 
    apt_seq,
    apt_dong,
    floor,
    deal_year,
    deal_month,
    deal_day,
    exclu_use_ar,
    CONCAT(FLOOR((RAND() * (8000 - 200) + 200) / 100) * 100) AS deal_amount, -- 200 ~ 8000 범위의 100 단위 랜덤 값
    CONCAT(FLOOR((RAND() * (100 - 20)) + 20)) AS fee_amount -- 20 ~ 100 범위의 랜덤 값
FROM 
    housedeals;

INSERT INTO housedeals_jeonse (
    apt_seq, apt_dong, floor, deal_year, deal_month, deal_day, exclu_use_ar, deal_amount
)
SELECT 
    apt_seq,
    apt_dong,
    floor,
    deal_year,
    deal_month,
    deal_day,
    exclu_use_ar,
    ROUND(CAST(REPLACE(deal_amount, ',', '') AS UNSIGNED) * 0.8, 0) AS deal_amount -- deal_amount를 0.8배로 계산
FROM 
    housedeals;
    
    -- STEP: 최근 3회 거래 데이터를 추출하여 평균 거래 금액 및 거래 횟수를 집계
INSERT INTO houserecentdeals (apt_seq, deal_space, avg_deal_amount, deal_count, deal_category)
SELECT 
    ranked.apt_seq,
    CONCAT(ranked.deal_space) AS deal_space,
    ROUND(AVG(ranked.deal_amount), 2) AS avg_deal_amount,
    COUNT(*) AS deal_count, -- 거래 횟수 집계
    "매매"    
FROM (
    SELECT 
        apt_seq,
        FLOOR(exclu_use_ar * 0.3025) AS deal_space, -- 제곱미터 -> 평 단위 변환
        CAST(REPLACE(deal_amount, ',', '') AS UNSIGNED) AS deal_amount,
        ROW_NUMBER() OVER (
            PARTITION BY apt_seq, FLOOR(exclu_use_ar * 0.3025)
            ORDER BY deal_year DESC, deal_month DESC, deal_day DESC
        ) AS rn
    FROM 
        housedeals
) AS ranked
WHERE 
    ranked.rn <= 3 -- 각 아파트 및 평형별로 최근 3회 거래만 포함
GROUP BY 
    ranked.apt_seq, ranked.deal_space;      
    
-- STEP: 최근 3회 거래 데이터를 추출하여 평균 거래 금액 및 거래 횟수를 집계
INSERT INTO houserecentdeals (apt_seq, deal_space, avg_deal_amount, deal_count, deal_category)
SELECT 
    ranked.apt_seq,
    CONCAT(ranked.deal_space) AS deal_space,
    ROUND(AVG(ranked.deal_amount), 2) AS avg_deal_amount,
    COUNT(*) AS deal_count, -- 거래 횟수 집계
    "전세"    
FROM (
    SELECT 
        apt_seq,
        FLOOR(exclu_use_ar * 0.3025) AS deal_space, -- 제곱미터 -> 평 단위 변환
        CAST(REPLACE(deal_amount, ',', '') AS UNSIGNED) AS deal_amount,
        ROW_NUMBER() OVER (
            PARTITION BY apt_seq, FLOOR(exclu_use_ar * 0.3025)
            ORDER BY deal_year DESC, deal_month DESC, deal_day DESC
        ) AS rn
    FROM 
        housedeals_jeonse
) AS ranked
WHERE 
    ranked.rn <= 3 -- 각 아파트 및 평형별로 최근 3회 거래만 포함
GROUP BY 
    ranked.apt_seq, ranked.deal_space;   
    
    -- STEP: 최근 3회 거래 데이터를 추출하여 평균 거래 금액 및 거래 횟수를 집계
INSERT INTO houserecentdeals (apt_seq, deal_space, avg_deal_amount, deal_count, deal_category, avg_fee_amount)
SELECT 
    ranked.apt_seq,
    CONCAT(ranked.deal_space) AS deal_space,
    ROUND(AVG(ranked.deal_amount), 2) AS avg_deal_amount,
    COUNT(*) AS deal_count, -- 거래 횟수 집계
    "월세",
    ROUND(AVG(ranked.fee_amount), 2) AS avg_fee_amount
FROM (
    SELECT 
        apt_seq,
        FLOOR(exclu_use_ar * 0.3025) AS deal_space, -- 제곱미터 -> 평 단위 변환
        CAST(REPLACE(deal_amount, ',', '') AS UNSIGNED) AS deal_amount,
        ROW_NUMBER() OVER (
            PARTITION BY apt_seq, FLOOR(exclu_use_ar * 0.3025)
            ORDER BY deal_year DESC, deal_month DESC, deal_day DESC
        ) AS rn,
        fee_amount
    FROM 
        housedeals_month
) AS ranked
WHERE 
    ranked.rn <= 3 -- 각 아파트 및 평형별로 최근 3회 거래만 포함
GROUP BY 
    ranked.apt_seq, ranked.deal_space;


-- -----------------------------------------------------
-- Table `home`.`favorite`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `home`.`favorite` (
  `apt_seq` VARCHAR(20),
  `user_no` INT,
  `selected_at` timestamp not null,
  PRIMARY KEY (`apt_seq`, `user_no`),
  FOREIGN KEY (`apt_seq`) REFERENCES houseInfos (`apt_seq`),
  FOREIGN KEY (`user_no`) REFERENCES users (`user_no`)
);

use home;
select * from users;
INSERT INTO `home`.`users` (
  `user_name`, 
  `user_password`, 
  `user_nickname`, 
  `user_email`, 
  `phone_number`, 
  `role`
) VALUES (
  'ssafy',           -- 실제 사용자 이름을 적어주세요
  'example_password',       -- 실제 비밀번호를 적어주세요
  'ssafy',                  -- 닉네임
  'example@example.com',    -- 실제 이메일을 적어주세요
  '010-1234-5678',          -- 실제 전화번호를 적어주세요
  'user'                    -- 실제 역할을 적어주세요
);

select * from board;
use home;
INSERT INTO home.board (user_no, subject, content) VALUES
(1, 'Hello World', 'This is a test content for the board.'),
(1, 'Vue.js is awesome!', 'Vue.js is a progressive framework for building user interfaces.'),
(1, 'Tips for JavaScript', 'Here are some useful JavaScript tips and tricks.'),
(1, 'How to use CSS Grid', 'CSS Grid is a powerful layout system in CSS.'),
(1, 'Learning SQL', 'This is a simple introduction to SQL queries and their usage.'),
(1, 'Frontend Development', 'Frontend development is all about creating beautiful user interfaces.'),
(1, 'Backend with Node.js', 'Node.js is a great platform for building scalable backend applications.'),
(1, 'Web Accessibility', 'Web accessibility ensures that websites are usable by everyone, including people with disabilities.'),
(1, 'Responsive Web Design', 'Responsive web design is essential for creating mobile-friendly websites.'),
(1, 'Building APIs with Express', 'Express.js is a minimal and flexible Node.js web application framework that provides a robust set of features for building APIs.'),
(1, 'Understanding Promises in JavaScript', 'Promises in JavaScript are a way to handle asynchronous operations.'),
(1, 'The Power of React', 'React is a popular JavaScript library for building user interfaces.'),
(1, 'Introduction to MongoDB', 'MongoDB is a NoSQL database that is scalable and flexible for modern applications.'),
(1, 'CSS Flexbox Basics', 'Flexbox is a CSS layout module that allows you to design complex layouts with simple properties.'),
(1, 'Best Practices for Web Development', 'Web development best practices help ensure your applications are maintainable and scalable.'),
(1, 'Asynchronous Programming in JavaScript', 'Asynchronous programming is essential for non-blocking operations in JavaScript.'),
(1, 'Introduction to GraphQL', 'GraphQL is a query language for APIs that gives clients the ability to ask for specific data.'),
(1, 'Testing in JavaScript', 'Testing is crucial to ensure that your code is functioning correctly and as expected.'),
(1, 'State Management with Vuex', 'Vuex is a state management pattern and library for Vue.js applications.'),
(1, 'Building a To-Do App with Vue', 'In this tutorial, we will build a simple to-do app using Vue.js.');
desc attachments;
insert into board(user_no,subject,content,register_time) values (null,"dasdas","adsdsadsa",now());
select * from board;
select * from attachments;


