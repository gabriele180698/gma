package gma.services;

import java.util.List;

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

	public StatisticsService() {
	}

	public void submitStatistics(int age, int expertise, Questionnaire questionnaire, int sex, User user, int score)
			throws StatisticsException {
		Statistics stat = new Statistics();

		try {
			// If age is not given, the points are not assigned
			if (age != 0) {
				score += 2;
			}
			stat.setAge(age);

			// Expertise level: 0 = no info; 1 = low; 2 = medium; 3 = high;
			if (expertise != 0) {
				score += 2;
				stat.setExpertise(expertise);
			}

			// Status = 0 = cancelled; 1 = submitted
			stat.setStatus(1);

			// Sex: 0 = no info; 1 = female; 2 = male; 3 = other;
			if (sex != 0) {
				score += 2;
				stat.setSex(sex);
			}

			stat.setScore(score);
			stat.setQuestionnaire(questionnaire);
			stat.setUser(user);
			em.persist(stat);
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw new StatisticsException("Something went wrong during the cancellation of the statistics");
		}
	}

	public void cancelStatistics(Questionnaire questionnaire, User user) throws StatisticsException {
		Statistics stat = new Statistics();

		try {
			// Status = 0 = cancelled; 1 = submitted
			stat.setStatus(0);
			stat.setQuestionnaire(questionnaire);
			stat.setUser(user);
			em.persist(stat);
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw new StatisticsException("Something went wrong during the cancellation of the statistics");
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
			throw new StatisticsException("Something went wrong during the cancellation of the statistics");
		}
		return statistics;
	}

}
