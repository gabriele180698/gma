package gma.services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import gma.entities.Questionnaire;
import gma.entities.User;
import gma.entities.Statistics;

@Stateless
public class StatisticsService {
	@PersistenceContext(unitName = "GMA")
	private EntityManager em;

	public StatisticsService() {
	}

	public void submitStatistics(int age, int expertise, Questionnaire questionnaire, int sex, User user) {
		Statistics stat = new Statistics();
		int score = 0;

		//If age is not given, the points are not assigned
		if(age!=0) {
			score += 2;
		}
		stat.setAge(age);

		// Expertise level: 0 = no info; 1 = low; 2 = medium; 3 = high;
		if(expertise!=0) {
			score += 2;
			stat.setExpertise(expertise);
		}
		
		// Status = 0 = cancelled; 1 = submitted
		stat.setStatus(1);
		
		// Sex: 0 = no info; 1 = female; 2 = male; 3 = other;
		if(sex!=0) {
			score += 2;
			stat.setSex(sex);
		}
		
		stat.setScore(score);
		stat.setQuestionnaire(questionnaire);
		stat.setUser(user);
		em.persist(stat);
	}
	
	
	public void cancelStatistics(Questionnaire questionnaire, User user) {
		Statistics stat = new Statistics();
		
		// Status = 0 = cancelled; 1 = submitted
		stat.setStatus(0);
		stat.setQuestionnaire(questionnaire);
		stat.setUser(user);
		em.persist(stat);
	}
}
