package gma.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import gma.entities.User;
import gma.exceptions.AnswerException;
import gma.exceptions.QuestionnaireException;
import gma.entities.Answer;
import gma.entities.Question;
import gma.entities.Questionnaire;

@Stateless
public class AnswerService {
	@PersistenceContext(unitName = "GMA")
	private EntityManager em;

	public AnswerService() {
	}

	// store in the database all the answers given in input
	public void submitAnswers(Map<Integer, String> map, User user) throws AnswerException {
		Question question;
		List<Answer> answers = new ArrayList<Answer>();

		try {
			// loop in the map table in input and containing the pairs (idQuestion,answer)
			for (Map.Entry<Integer, String> entry : map.entrySet()) {
				Answer answer = new Answer();
				// get the question associated to the Id
				question = em.find(Question.class, entry.getKey());
				answer.setQuestion(question);
				answer.setUser(user);
				answer.setText(entry.getValue());
				answers.add(answer);
			}
			for (int i = 0; i < answers.size(); i++) {
				em.persist(answers.get(i)); 
			}
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw new AnswerException("Something went wrong during the submission of the mandatory answers!");
		}
	}

	// delete the answers 
	public void deleteAnswers(List<Integer> idQuestion, int idUser) throws AnswerException{
		Answer answer;
		try {
			// loop to delete all answers
			for (int i = 0; i < idQuestion.size(); i++) {
				answer = em.createNamedQuery("Answer.findAnswerByIdAndUser", Answer.class)
						.setParameter(1, idQuestion.get(i)).setParameter(2, idUser).getResultStream().findFirst()
						.orElse(null);
				if (answer != null) {
					em.remove(answer);
				}
			}
			// update the persistence context for coherence
			em.flush();
			em.clear();
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw new AnswerException("Critical error!");
		}
		
	}

	// return all the answers associated to a given questionnaire and user
	public List<Answer> getAnswers(Questionnaire questionnaire, User user) throws AnswerException {
		List<Answer> answers = null;
		try {
			answers = em.createNamedQuery("Answer.findAnswersByQuestionnaireAndUser", Answer.class)
					.setParameter(1, questionnaire.getId()).setParameter(2, user.getId()).getResultList();
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw new AnswerException("Something went wrong during retrving the answers!");
		}
		return answers;

	}
}
