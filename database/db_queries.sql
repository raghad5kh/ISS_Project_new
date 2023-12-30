DROP DATABASE IF EXISTS iss_project;
CREATE DATABASE iss_project;
USE iss_project;

CREATE TABLE list_data (
  id INT(15) PRIMARY KEY AUTO_INCREMENT,
  id_number INT(15),
  national_number VARCHAR(30),
  type VARCHAR(10)
);


CREATE TABLE students (
  id INT(15) PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(30) UNIQUE,
  password VARCHAR(8),
  address VARCHAR(30) DEFAULT  NULL,
  phone_number INT(15) DEFAULT  NULL,
  mobile_number INT(15) DEFAULT  NULL,
  publicKey VARCHAR(500),
  list_data_id INT UNIQUE,
  FOREIGN KEY (list_data_id) REFERENCES list_data(id)
);

CREATE TABLE professor (
  id INT(15) PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(30) UNIQUE,
  password VARCHAR(8),
  address VARCHAR(30) DEFAULT NULL,
  phone_number INT(15) DEFAULT NULL,
  mobile_number INT(15) DEFAULT NULL,
  publicKey VARCHAR(500),
  list_data_id INT UNIQUE,
   FOREIGN KEY (list_data_id) REFERENCES list_data(id)
);

INSERT INTO list_data(id_number,national_number,type) VALUES (53221,"0173456789287654","s");
INSERT INTO list_data(id_number,national_number,type) VALUES (53222,"0173542087576107","s");
INSERT INTO list_data(id_number,national_number,type) VALUES (53223,"0175432976476543","p");
INSERT INTO list_data(id_number,national_number,type) VALUES (53224,"0106523865397021","p");
INSERT INTO list_data(id_number,national_number,type) VALUES (53225,"0106523865387027","p");
INSERT INTO list_data(id_number,national_number,type) VALUES (53226,"0106523665387067","s");
INSERT INTO list_data(id_number,national_number,type) VALUES (53227,"0106523865387033","p");
INSERT INTO list_data(id_number,national_number,type) VALUES (53228,"0106523665387099","s");
INSERT INTO list_data(id_number,national_number,type) VALUES (53229,"0106523865387088","p");
INSERT INTO list_data(id_number,national_number,type) VALUES (54221,"0106523665387777","s");