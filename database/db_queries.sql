DROP DATABASE IF EXISTS iss_project;
CREATE DATABASE iss_project;
USE iss_project;
CREATE TABLE students (
  id INT(15) PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(30) UNIQUE,
  password VARCHAR(8),
  address VARCHAR(30) DEFAULT  NULL,
  phone_number INT(15) DEFAULT  NULL,
  mobile_number INT(15) DEFAULT  NULL,
  publicKey VARCHAR(30)

);
CREATE TABLE professor (
  id INT(15) PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(30) UNIQUE,
  password VARCHAR(8),
  address VARCHAR(30) DEFAULT NULL,
  phone_number INT(15) DEFAULT NULL,
  mobile_number INT(15) DEFAULT NULL,
  publicKey VARCHAR(30)


);
CREATE TABLE list_stus (
  id INT(15) PRIMARY KEY AUTO_INCREMENT,
  id_number INT(15),
  national_number VARCHAR(30)
);

CREATE TABLE list_pros (
  id INT(15) PRIMARY KEY AUTO_INCREMENT,
  id_number INT(15),
  national_number VARCHAR(30)
);

INSERT INTO list_stus(id_number,national_number) VALUES (53221,"0173456789287654");
INSERT INTO list_stus(id_number,national_number) VALUES (53222,"0173542087576107");
INSERT INTO list_pros(id_number,national_number) VALUES (53223,"0175432976476543");
INSERT INTO list_pros(id_number,national_number) VALUES (53224,"0106523865397021");