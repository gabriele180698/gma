DELIMITER $$
CREATE TRIGGER marketing_score
  BEFORE INSERT ON statistics
  FOR EACH ROW
BEGIN
    IF (NEW.status = 1) THEN
		IF (NEW.age != 0) THEN
			SET NEW.score = NEW.score + 2;
		END IF;
		
		IF (NEW.expertise != 0) THEN
			SET NEW.score = NEW.score + 2;
		END IF;
		
		IF (NEW.sex != 0) THEN
			SET NEW.score = NEW.score + 2;
		END IF;
    END IF;
END $$

CREATE TRIGGER insert_answer_score
  BEFORE INSERT ON statistics
  FOR EACH ROW
BEGIN
	IF (NEW.status = 1) THEN
		SET NEW.score = NEW.score + (SELECT count(*) FROM answer AS a, question AS q 
		WHERE q.id = a.idQuestion AND q.idQuestionnaire=NEW.idQuestionnaire AND a.idUser=NEW.idUser);
	END IF;
END $$

CREATE TRIGGER delete_answer_score
  AFTER DELETE ON answer
  FOR EACH ROW
BEGIN
	IF ((SELECT score FROM statistics  WHERE s.idQuestionnaire = (SELECT q.idQuestionnaire FROM question AS q WHERE q.id = OLD.idQuestion)) > 0 ) THEN
		UPDATE statistics AS s SET s.score = s.score - 1
		WHERE s.idQuestionnaire = (SELECT q.idQuestionnaire FROM question AS q WHERE q.id = OLD.idQuestion) AND OLD.idUser = s.idUser;
    END IF;
END $$
DELIMITER ;