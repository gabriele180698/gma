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
		@NamedQuery(name = "Questionnaire.findQuestionnaireByDate", query = "SELECT q FROM Questionnaire q  WHERE q.date = ?1"),
		@NamedQuery(name = "Questionnaire.findAll", query = "SELECT q FROM Questionnaire q"),
		@NamedQuery(name = "Questionnaire.findAllBeforeToday", query = "SELECT q FROM Questionnaire q WHERE q.date < CURRENT_DATE")
})
public class Questionnaire implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Temporal(TemporalType.DATE)
	private Date date;
	@OneToMany(mappedBy = "questionnaire", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	private List<Question> questions;
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	@JoinColumn(name = "idProduct")
	private Product product;
	@OneToMany(mappedBy = "questionnaire", fetch = FetchType.LAZY)
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

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
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
