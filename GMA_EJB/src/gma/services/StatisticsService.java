package gma.services;

import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import gma.entities.Questionnaire;
import gma.entities.User;
import gma.exceptions.AnswerException;
import gma.exceptions.StatisticsException;
import gma.entities.Statistics;

@Stateless
public class StatisticsService {
	@PersistenceContext(unitName = "GMA")
	private EntityManager em;
	@EJB(name = "gma.services/AnswerService")
	private AnswerService aService;

	public StatisticsService() {
	}

	public void submitStatistics(int age, int expertise, Questionnaire questionnaire, 
			int sex, User user, Map<Integer, String> map) throws StatisticsException, AnswerException {
		Statistics stat = new Statistics();

		try {
			// If age is not given, the points are not assigned
			stat.setAge(age);
			// Expertise level: 0 = no info; 1 = low; 2 = medium; 3 = high;
			stat.setExpertise(expertise);
			// Status: 0 = cancelled; 1 = submitted;
			stat.setStatus(1);
			// Sex: 0 = no info; 1 = female; 2 = male; 3 = other;
			stat.setSex(sex);
			stat.setQuestionnaire(questionnaire);
			stat.setUser(user);
			aService.submitAnswers(map, user); // submit the answers
			em.persist(stat);
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw new StatisticsException("Something went wrong during the submissions of the statistics!");
		}
	}

	public void cancelStatistics(Questionnaire questionnaire, User user) throws StatisticsException {
		Statistics stat = new Statistics();
		try {
			// Status: 0 = cancelled; 1 = submitted
			stat.setStatus(0);
			stat.setQuestionnaire(questionnaire);
			stat.setUser(user);
			em.persist(stat);
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw new StatisticsException("Something went wrong during the cancellation of the statistics!");
		}
	}

	public Statistics existingStatistics(int idUser, int idQuestionnaire) throws StatisticsException {
		Statistics statistics;

		try {
			// Check if a submitted statistic exists
			statistics = em.createNamedQuery("Statistics.findExistingStatistics", Statistics.class)
					.setParameter(1, idUser).setParameter(2, idQuestionnaire).getResultStream().findFirst()
					.orElse(null);
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw new StatisticsException("Something went wrong during the check of the existence of the statistics!");
		}
		return statistics;
	}

	public List<Statistics> getStatistics(Questionnaire questionnaire) throws StatisticsException {
		List<Statistics> statistics;

		try {
			// Check if a submitted statistic exists
			statistics = em.createNamedQuery("Statistics.findByQuestionnaireId", Statistics.class)
					.setParameter(1, questionnaire.getId()).getResultList();
			
			for(int i = 0; i < statistics.size(); i++){
				em.refresh(statistics.get(i));
			}
			
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw new StatisticsException("Something went wrong during the retriving statistics!");
		}
		return statistics;
	}

}
