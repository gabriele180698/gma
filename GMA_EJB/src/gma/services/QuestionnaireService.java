package gma.services;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import gma.entities.Questionnaire;
import gma.entities.Answer;
import gma.entities.Question;
import gma.exceptions.*;

@Stateless
public class QuestionnaireService {
	@PersistenceContext(unitName = "GMA")
	private EntityManager em;

	public QuestionnaireService() {
	}

	// retrieves all questions associated to the given questionnaire
	public List<Question> getQuestionsByQuestionnaire(Questionnaire questionnaire) throws QuestionnaireException {
		List<Question> questions;

		try {
			questions = questionnaire.getQuestions();
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw new QuestionnaireException("No questions!");
		}
		return questions;
	}

	// search for a questionnaire with the given date
	public Questionnaire getQuestionnaireByDate(Date date) throws QuestionnaireException {
		Questionnaire questionnaire;
		try {
			// use of the named query of the entity Questionnaire
			questionnaire = em.createNamedQuery("Questionnaire.findQuestionnaireByDate", Questionnaire.class)
					.setParameter(1, date).getResultStream().findFirst().orElse(null);
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw new QuestionnaireException("No questionnaire of the day found!");
		}
		return questionnaire;
	}

	// get questionnaire by id
	public Questionnaire getQuestionnaireById(int idQuestionnaire) throws QuestionnaireException {
		Questionnaire questionnaire = null;
		// find questionnaire
		try {
			questionnaire = em.find(Questionnaire.class, idQuestionnaire);
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw new QuestionnaireException("No questionnaire found!!");
		}
		return questionnaire;
	}

	// get all questionnaires
	public List<Questionnaire> getAllQuestionnaire() throws QuestionnaireException {
		List<Questionnaire> questionnaires = null;
		try {
			questionnaires = em.createNamedQuery("Questionnaire.findAll", Questionnaire.class).getResultList();
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw new QuestionnaireException("No questionnaires found!");
		}
		return questionnaires;
	}

	// delete questionnaire by id
	public void removeQuestionnaire(Questionnaire questionnaire) throws QuestionnaireException {
		// delete questionnaire
		try {
			// check if the entity is managed, if not manage it
			if (!em.contains(questionnaire)) {
				questionnaire = em.merge(questionnaire);
			}
			em.remove(questionnaire);
			// refresh persistence context
			em.flush();
			em.clear();
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw new QuestionnaireException("It is not possible to delete the questionnaire!");
		}
	}

}
