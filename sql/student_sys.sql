DROP TABLE IF EXISTS `STUDENT`;

CREATE TABLE STUDENT(
    SNO VARCHAR(3) NOT NULL,
    SNAME VARCHAR(4) NOT NULL,
    SSEX VARCHAR(2) NOT NULL,
    SBIRTHDAY DATETIME,
    SCLASS VARCHAR(5),
     PRIMARY KEY (SNO)
);

DROP TABLE IF EXISTS `COURSE`;
CREATE TABLE COURSE (
	CNO VARCHAR(5) NOT NULL,
	CNAME VARCHAR(10) NOT NULL,
	TNO VARCHAR(10) NOT NULL,
	PRIMARY KEY (CNO)
);


DROP TABLE IF EXISTS `SCORE`;
CREATE TABLE SCORE
(SNO VARCHAR(3) NOT NULL,
CNO VARCHAR(5) NOT NULL,
DEGREE NUMERIC(10, 1) NOT NULL,
PRIMARY KEY (SNO,CNO));

DROP TABLE IF EXISTS `TEACHER`;
CREATE TABLE TEACHER(
    TNO VARCHAR(3) NOT NULL,
    TNAME VARCHAR(4) NOT NULL, TSEX VARCHAR(2) NOT NULL,
    TBIRTHDAY DATETIME NOT NULL, PROF VARCHAR(6),
    DEPART VARCHAR(10) NOT NULL,
    PRIMARY KEY (TNO)
);


INSERT INTO STUDENT (SNO,SNAME,SSEX,SBIRTHDAY,SCLASS) VALUES (108 ,'曾华'
,'男' ,'1977-09-01',95033);
INSERT INTO STUDENT (SNO,SNAME,SSEX,SBIRTHDAY,SCLASS) VALUES (105 ,'匡明'
,'男' ,'1975-10-02',95031);
INSERT INTO STUDENT (SNO,SNAME,SSEX,SBIRTHDAY,SCLASS) VALUES (107 ,'王丽'
,'女' ,'1976-01-23',95033);
INSERT INTO STUDENT (SNO,SNAME,SSEX,SBIRTHDAY,SCLASS) VALUES (101 ,'李军'
,'男' ,'1976-02-20',95033);
INSERT INTO STUDENT (SNO,SNAME,SSEX,SBIRTHDAY,SCLASS) VALUES (109 ,'王芳'
,'女' ,'1975-02-10',95031);
INSERT INTO STUDENT (SNO,SNAME,SSEX,SBIRTHDAY,SCLASS) VALUES (103 ,'陆君'
,'男' ,'1974-06-03',95031);

INSERT INTO COURSE(CNO,CNAME,TNO)VALUES ('3-105' ,'计算机导论',825);
INSERT INTO COURSE(CNO,CNAME,TNO)VALUES ('3-245' ,'操作系统' ,804);
INSERT INTO COURSE(CNO,CNAME,TNO)VALUES ('6-166' ,'数据电路' ,856);
INSERT INTO COURSE(CNO,CNAME,TNO)VALUES ('9-888' ,'高等数学' ,100);

INSERT INTO SCORE(SNO,CNO,DEGREE)VALUES (103,'3-245',86);
INSERT INTO SCORE(SNO,CNO,DEGREE)VALUES (105,'3-245',75);
INSERT INTO SCORE(SNO,CNO,DEGREE)VALUES (109,'3-245',68);
INSERT INTO SCORE(SNO,CNO,DEGREE)VALUES (103,'3-105',92);
INSERT INTO SCORE(SNO,CNO,DEGREE)VALUES (105,'3-105',88);
INSERT INTO SCORE(SNO,CNO,DEGREE)VALUES (109,'3-105',76);
INSERT INTO SCORE(SNO,CNO,DEGREE)VALUES (101,'3-105',64);
INSERT INTO SCORE(SNO,CNO,DEGREE)VALUES (107,'3-105',91);
INSERT INTO SCORE(SNO,CNO,DEGREE)VALUES (108,'3-105',78);
INSERT INTO SCORE(SNO,CNO,DEGREE)VALUES (101,'6-166',85);
INSERT INTO SCORE(SNO,CNO,DEGREE)VALUES (107,'6-106',79);
INSERT INTO SCORE(SNO,CNO,DEGREE)VALUES (108,'6-166',81);

INSERT INTO TEACHER(TNO,TNAME,TSEX,TBIRTHDAY,PROF,DEPART)
VALUES (804,'李诚','男','1958-12-02','副教授','计算机系');
INSERT INTO TEACHER(TNO,TNAME,TSEX,TBIRTHDAY,PROF,DEPART)
VALUES (856,'张旭','男','1969-03-12','讲师','电子工程系');
INSERT INTO TEACHER(TNO,TNAME,TSEX,TBIRTHDAY,PROF,DEPART)
VALUES (825,'王萍','女','1972-05-05','助教','计算机系');
INSERT INTO TEACHER(TNO,TNAME,TSEX,TBIRTHDAY,PROF,DEPART)
VALUES (831,'刘冰','女','1977-08-14','助教','电子工程系');

DROP TABLE IF EXISTS `grade`;
CREATE TABLE GRADE (
	LOW DECIMAL(3,0),
	UPP DECIMAL(3,0),
	"rank" CHAR(1)
);

-- ----------------------------
-- Records of grade
-- ----------------------------
INSERT INTO `grade` VALUES ('90', '100', 'A');
INSERT INTO `grade` VALUES ('80', '89', 'B');
INSERT INTO `grade` VALUES ('70', '79', 'C');
INSERT INTO `grade` VALUES ('60', '69', 'D');
INSERT INTO `grade` VALUES ('0', '59', 'E');