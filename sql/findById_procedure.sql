---存储过程

DROP PROCEDURE IF EXISTS findById;

DELIMITER $$
CREATE PROCEDURE findById(IN sno VARCHAR(3))
BEGIN
	SELECT * FROM STUDENT S WHERE S.SNO = sno;
END $$
DELIMITER ;