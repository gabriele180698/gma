package gma.entities;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * Entity implementation class for Entity: Blacklist
 *
 */
@Entity
@NamedQueries({
@NamedQuery(name = "Statistics.findQuestionnaireByUser", query = "SELECT q FROM Statistics s JOIN s.user u JOIN s.questionnaire q WHERE u.id = ?1"),
@NamedQuery(name = "Statistics.findExistingStatistics", query = "SELECT s FROM Statistics s JOIN s.user u JOIN s.questionnaire q "
				+ "	WHERE u.id = ?1 AND q.id = ?2 AND s.status = 1"),
@NamedQuery(name = "Statistics.findByQuestionnaireId", query = "SELECT s FROM Statistics s JOIN s.questionnaire q WHERE q.id = ?1"),
})
public class Statistics implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private int score;
	private int status;
	private int age;
	private int sex;
	private int expertise;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idQuestionnaire")
	private Questionnaire questionnaire;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idUser")
	private User user;
	
	public Statistics() {
	}

	public Statistics(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public int getExpertise() {
		return expertise;
	}

	public void setExpertise(int expertise) {
		this.expertise = expertise;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Questionnaire getQuestionnaire() {
		return this.questionnaire;
	}

	public void setQuestionnaire(Questionnaire questionnaire) {
		this.questionnaire = questionnaire;
	}
}
