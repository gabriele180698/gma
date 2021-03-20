package gma.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Questionnaire
 *
 */
@Entity
public class Questionnaire implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Temporal(TemporalType.DATE)
	private Date date;
	@OneToMany(mappedBy = "questionnaire")
	private List<Question> questions;
	@ManyToOne
	@JoinColumn(name = "product")
	private Product product;
	@ManyToMany(mappedBy = "questionnaires")
	private List<User> users;

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

	public List<User> getUsers() {
		return this.users;
	}

	public void addUser(User user) {
		getUsers().add(user);
		user.getQuestionnaires().add(this);
	}

	public void removeUser(User user) {
		user.removeQuestionnaire(this);
		getUsers().remove(user);
	}

}
