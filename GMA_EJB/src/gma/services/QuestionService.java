package gma.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import gma.entities.Questionnaire;
import gma.entities.User;
import gma.entities.Answer;
import gma.entities.Question;
import gma.exceptions.*;

@Stateless
public class QuestionService {
	@PersistenceContext(unitName = "GMA")
	private EntityManager em;

	public QuestionService() {
	}

	// retrieves all questions associated to the given questionnaire
	public List<Integer> getQuestionsId(List<Question> questions) throws QuestionException {
		List<Integer> questionsId = new ArrayList<Integer>();

		try {
			// get the Ids of all questions in input
			for (int i = 0; i < questions.size(); i++) {
				questionsId.add(questions.get(i).getId());
			}
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw new QuestionException("No questions!");
		}
		return questionsId;
	}

	public void createQuestion(String text, Questionnaire questionnaire) {
		Question q = new Question();
		q.setText(text);
		q.setQuestionnaire(questionnaire);
		em.persist(q);
	}

	
}
