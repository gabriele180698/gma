package gma.services;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import gma.entities.Questionnaire;
import gma.entities.Question;
import gma.exceptions.*;

@Stateless
public class QuestionnaireService {
	@PersistenceContext(unitName = "GMA")
	private EntityManager em;

	public QuestionnaireService() {
	}

	public List<Question> getQuestionsByQuestionnaire(int id) throws ProductException {
		List<Question> questions;
		Questionnaire questionnaire;
		
		try {
			questionnaire = em.createNamedQuery("Questionnaire.findQuestionnaireById", Questionnaire.class)
					.setParameter(1, id).getResultStream().findFirst().orElse(null);
			questions = questionnaire.getQuestions();
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw new ProductException("No product of the day found!");
		}
		return questions;
	}
}
