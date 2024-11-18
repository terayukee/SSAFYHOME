
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
   FOREIGN KEY (`user_no`) REFERENCES users(`user_no`));

-- -----------------------------------------------------
-- Table `home`.`attachments`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `home`.`attachments` (
  `attachment_id` INT NOT NULL AUTO_INCREMENT,
  `board_no` INT NOT NULL,
  `file_name` VARCHAR(255) NOT NULL,
  `file_size` INT NOT NULL,
  `file_type` VARCHAR(100) NOT NULL,
  `file_path` VARCHAR(255) NOT NULL,
  `created_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`attachment_id`),
  FOREIGN KEY (`board_no`) REFERENCES `home`.`board` (`board_no`));


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
CREATE TABLE IF NOT EXISTS `home`.`housedeals` (
  `no` INT NOT NULL AUTO_INCREMENT,
  `apt_seq` VARCHAR(20) NULL DEFAULT NULL,
  `apt_dong` VARCHAR(40) NULL DEFAULT NULL,
  `floor` VARCHAR(3) NULL DEFAULT NULL,
  `deal_year` INT NULL DEFAULT NULL,
  `deal_month` INT NULL DEFAULT NULL,
  `deal_day` INT NULL DEFAULT NULL,
  `exclu_use_ar` DECIMAL(7,2) NULL DEFAULT NULL,
  `deal_amount` VARCHAR(10) NULL DEFAULT NULL,
  PRIMARY KEY (`no`),
  INDEX `apt_seq_to_house_info_idx` (`apt_seq` ASC) VISIBLE,
  CONSTRAINT `apt_seq_to_house_info`
    FOREIGN KEY (`apt_seq`)
    REFERENCES `home`.`houseinfos` (`apt_seq`))
ENGINE = InnoDB
AUTO_INCREMENT = 7084512
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

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
insert into board(user_no,subject,content) values (1,"aa","aaaa");
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


