package gma.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: Answer
 *
 */
@Entity
@NamedQuery(name = "Answer.findAnswerByIdAndUser", query = "SELECT a FROM Answer a JOIN a.user u JOIN a.question qu"
		+ " WHERE qu.id = ?1 AND u.id = ?2")
public class Answer implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String text;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idUser")
	private User user;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idQuestion")
	private Question question;

	public Answer() {
	}

	public Answer(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public Question getQuestion() {
		return this.question;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
