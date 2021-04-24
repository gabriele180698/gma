
package gma.services;

import java.util.Date;
import java.util.List;
import java.util.Stack;

import javax.ejb.Stateless;
import javax.management.j2ee.statistics.Statistic;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import gma.entities.Questionnaire;
import gma.entities.Statistics;
import gma.entities.User;
import gma.entities.Answer;
import gma.entities.Product;
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

	// retrieves users that have submitted the questionnaire
	public List<User> getUsersSubmitedQuestionnaire(Questionnaire questionnaire) throws QuestionnaireException {
		List<User> users = new Stack<User>();
		List<Statistics> statistics = null;
		try {
			statistics = questionnaire.getStatistics();
			for (int i = 0; i < statistics.size(); i++) {
				Statistics statistic = statistics.get(i);
				if (statistic.getStatus() == 1) {
					User u = statistic.getUser();
					users.add(u);
				}
			}
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw new QuestionnaireException("No questions!");
		}
		return users;

	}

	// retrieves users that have cancelled the questionnaire
	public List<User> getUsersCancelledQuestionnaire(Questionnaire questionnaire) throws QuestionnaireException {
		List<User> users = new Stack<User>();
		List<Statistics> statistics = null;
		try {
			statistics = questionnaire.getStatistics();
			for (int i = 0; i < statistics.size(); i++) {
				Statistics statistic = statistics.get(i);
				if (statistic.getStatus() == 0) {
					User u = statistic.getUser();
					users.add(u);
				}
			}

		} catch (PersistenceException e) {
			e.printStackTrace();
			throw new QuestionnaireException("No questions!");
		}
		return users;
	}
	/*
	 * retrieves users answers to a given questionnaire public List<Answer>
	 * getAnswers(Questionnaire questionnaire, User user) throws
	 * QuestionnaireException { List<Answer> answers = null; try { answers =
	 * em.createNamedQuery("Questionnaire.findAnswerByUser",
	 * Answer.class).getResultList(); return answers; } catch (PersistenceException
	 * e) { e.printStackTrace(); throw new QuestionnaireException("No questions!");
	 * }
	 * 
	 * }
	 */

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

	// Check if the questionnaire of the date exists
	public boolean questionnaireExist(Date date) throws QuestionnaireException {
		try {
			Questionnaire questionnaire;
			// use of the named query of the entity Questionnaire
			questionnaire = em.createNamedQuery("Questionnaire.findQuestionnaireByDate", Questionnaire.class)
					.setParameter(1, date).getResultStream().findFirst().orElse(null);
			if (questionnaire == null) {
				return false;
			} else {
				return true;
			}
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw new QuestionnaireException(
					"It is not possible to check if there exist an other questionnaire of the day");
		}

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

	// create Questionnaire and Questions
	public void createQuestionnaireAndQuestions(Date date, Product product, List<String> questions)
			throws QuestionnaireException {
		try {
			Questionnaire questionnaire = new Questionnaire();
			List<Question> qus = new Stack<Question>();
			questionnaire.setDate(date);
			questionnaire.setProduct(product);
			for (int i = 0; i < questions.size(); i++) {
				Question question = new Question();
				question.setText(questions.get(i));
				question.setQuestionnaire(questionnaire);
				qus.add(question);
			}
			questionnaire.setQuestions(qus);
			em.persist(questionnaire);
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw new QuestionnaireException("It is not possible to create the questionnaire and the answers");
		}

	}
}
