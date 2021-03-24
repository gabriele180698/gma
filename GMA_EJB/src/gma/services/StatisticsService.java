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

	public void submitStatistics(int score, int age, int expertise,Questionnaire questionnaire,int sex,User user,int status){
		Statistics stat = new Statistics();
		stat.setScore(score);
		stat.setAge(age);
		stat.setExpertise(expertise);
		stat.setQuestionnaire(questionnaire);
		stat.setSex(sex);
		stat.setUser(user);
		stat.setStatus(status);
	}
}

