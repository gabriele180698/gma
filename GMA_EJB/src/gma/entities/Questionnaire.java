package gma.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Questionnaire
 *
 */
@Entity
@Table(name = "questionnaire", schema = "gma")
@NamedQueries({
//@NamedQuery(name = "Questionnaire.findQuestionnaireById", query = "SELECT q FROM Questionnaire q  WHERE q.id = ?1"),
@NamedQuery(name = "Questionnaire.findQuestionnaireByDate", query = "SELECT q FROM Questionnaire q  WHERE q.date = ?1"),
@NamedQuery(name = "Questionnaire.findAll", query = "SELECT q FROM Questionnaire q"),
@NamedQuery(name = "Questionnaire.findAnswerByUser", query = "SELECT a FROM Questionnaire q JOIN q.questions qu JOIN qu.answers a WHERE a.user = ?1")
//@NamedQuery(name = "Questionnaire.UserSubmitted", query = "SELECT s.user FROM Questionnaire q JOIN Statistics s WHERE s.status = 1 AND q.id = ?1"),
//@NamedQuery(name = "Questionnaire.UserCancelled", query = "SELECT s.user FROM Questionnaire q JOIN q.Statistics s WHERE s.status = 0 AND q.id = ?1")
//@NamedQuery(name = "Statistics.findQuestionnaireOtheUsers", query = "SELECT a,qu,u FROM Questionnaire q JOIN q.questions qu JOIN qu.answers a JOIN a.user u "
//		+ "WHERE q.date = ?1 AND u.id IN (SELECT us.id FROM Statistics s JOIN s.questionnaire q JOIN s.user us WHERE s.status = 0) and u.id <> ?2"),
})
public class Questionnaire implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Temporal(TemporalType.DATE)
	private Date date;
	@OneToMany(mappedBy = "questionnaire", fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
	private List<Question> questions;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idProduct")
	private Product product;
	@OneToMany(mappedBy = "questionnaire", fetch = FetchType.EAGER)
	private List<Statistics> statistics;

	public Questionnaire() {
	}

	public Questionnaire(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Product getProduct() {
		return this.product;
	}

	public List<Question> getQuestions() {
		return this.questions;
	}

	public void addQuestion(Question question) {
		getQuestions().add(question);
		question.setQuestionnaire(this);
	}

	public void removeQuestion(Question question) {
		getQuestions().remove(question);
	}

	public List<Statistics> getStatistics() {
		return this.statistics;
	}

	public void addStatistics(Statistics statistics) {
		getStatistics().add(statistics);
		statistics.setQuestionnaire(this);
	}

	public void removeStatistics(Statistics statistics) {
		getStatistics().remove(statistics);
	}

}
