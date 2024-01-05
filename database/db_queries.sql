DROP DATABASE IF EXISTS iss_project;
CREATE DATABASE iss_project;
USE iss_project;

CREATE TABLE list_data (
  id INT(15) PRIMARY KEY AUTO_INCREMENT,
  id_number INT(15),
  national_number VARCHAR(30),
  type VARCHAR(10),
  number_year INT(8)
);


CREATE TABLE students (
  id INT(15) PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(30) UNIQUE,
  password VARCHAR(500),
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
  password VARCHAR(500),
  address VARCHAR(30) DEFAULT NULL,
  phone_number INT(15) DEFAULT NULL,
  mobile_number INT(15) DEFAULT NULL,
  publicKey VARCHAR(500),
  list_data_id INT UNIQUE,
   FOREIGN KEY (list_data_id) REFERENCES list_data(id)
);

CREATE TABLE level4_data(
 id INT(15) PRIMARY KEY AUTO_INCREMENT,
 Server_response VARCHAR(500),
 signature VARCHAR(500),
 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE list_students_marks(
 id INT(15) PRIMARY KEY AUTO_INCREMENT,
 name VARCHAR(30),
 mark INT(15),
 level4_data_id INT ,
 FOREIGN KEY (level4_data_id) REFERENCES level4_data(id)
);
CREATE TABLE permission (
  id INT(15) PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(30) ,
  symbol VARCHAR(8),
  number_year INT(8),
  nameTable VARCHAR(30)
);

INSERT INTO permission(name,symbol,number_year,nameTable) VALUES ("view English_marks year1","(VEY1)",1,"English");
INSERT INTO permission(name,symbol,number_year,nameTable) VALUES ("view Programming_marks year2","(VPY2)",2,"Programming");
INSERT INTO permission(name,symbol,number_year,nameTable) VALUES ("view Database2_marks year3","(VDY3)",3,"Database2");
INSERT INTO permission(name,symbol,number_year,nameTable) VALUES ("view Software_engineering_marks year4","(VSEY4)",4,"Software_engineering");

INSERT INTO list_data(id_number,national_number,type,number_year) VALUES (53221,"0173456789287654","s",1);
INSERT INTO list_data(id_number,national_number,type,number_year) VALUES (53222,"0173542087576107","s",1);
INSERT INTO list_data(id_number,national_number,type,number_year) VALUES (53223,"0175432976476543","p",1);
INSERT INTO list_data(id_number,national_number,type,number_year) VALUES (53224,"0106523865397021","p",1);

INSERT INTO list_data(id_number,national_number,type,number_year) VALUES (53225,"0106523865387027","s",2);
INSERT INTO list_data(id_number,national_number,type,number_year) VALUES (53226,"0106523665387067","s",2);
INSERT INTO list_data(id_number,national_number,type,number_year) VALUES (53227,"0106523865387033","p",2);
INSERT INTO list_data(id_number,national_number,type,number_year) VALUES (53228,"0106523665387099","p",2);


INSERT INTO list_data(id_number,national_number,type,number_year) VALUES (53229,"0106593865387018","s",3);
INSERT INTO list_data(id_number,national_number,type,number_year) VALUES (54221,"0106523665387879","s",3);
INSERT INTO list_data(id_number,national_number,type,number_year) VALUES (54222,"0106523845387086","p",3);
INSERT INTO list_data(id_number,national_number,type,number_year) VALUES (54223,"0106528665387747","p",3);

INSERT INTO list_data(id_number,national_number,type,number_year) VALUES (54224,"0106523865387088","s",4);
INSERT INTO list_data(id_number,national_number,type,number_year) VALUES (54225,"0106523665387777","s",4);
INSERT INTO list_data(id_number,national_number,type,number_year) VALUES (54226,"0106523865387088","p",4);
INSERT INTO list_data(id_number,national_number,type,number_year) VALUES (54227,"0106523665387777","p",4);

CREATE TABLE English (
  id INT(15) PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(30) ,
  mark VARCHAR(8)
);

INSERT INTO English(name,mark) VALUES("raghad",100);
INSERT INTO English(name,mark) VALUES("saja",80);
INSERT INTO English(name,mark) VALUES("rama",90);
INSERT INTO English(name,mark) VALUES("alaa",89);
INSERT INTO English(name,mark) VALUES("touka",92);

CREATE TABLE Programming (
  id INT(15) PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(30) ,
  mark VARCHAR(8)
);
INSERT INTO Programming(name,mark) VALUES("raghad",90);
INSERT INTO Programming(name,mark) VALUES("saja",100);
INSERT INTO Programming(name,mark) VALUES("rama",80);
INSERT INTO Programming(name,mark) VALUES("alaa",92);
INSERT INTO Programming(name,mark) VALUES("touka",88);


CREATE TABLE Database2 (  id INT(15) PRIMARY KEY AUTO_INCREMENT,  name VARCHAR(30) ,  mark VARCHAR(8));
INSERT INTO Database2(name,mark) VALUES("raghad",88);
INSERT INTO Database2(name,mark) VALUES("saja",77);
INSERT INTO Database2(name,mark) VALUES("rama",100);
INSERT INTO Database2(name,mark) VALUES("alaa",96);
INSERT INTO Database2(name,mark) VALUES("touka",87);

CREATE TABLE Software_engineering (
  id INT(15) PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(30) ,
  mark VARCHAR(8)
);

INSERT INTO Software_engineering(name,mark) VALUES("raghad",79);
INSERT INTO Software_engineering(name,mark) VALUES("saja",70);
INSERT INTO Software_engineering(name,mark) VALUES("rama",80);
INSERT INTO Software_engineering(name,mark) VALUES("alaa",86);
INSERT INTO Software_engineering(name,mark) VALUES("touka",100);