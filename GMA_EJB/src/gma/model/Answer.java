package gma.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: Answer
 *
 */
@Entity
public class Answer implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String text;
	@ManyToOne
	@JoinColumn(name = "user")
	private User user;
	@ManyToOne
	@JoinColumn(name = "question")
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
	
	public User getUser(){
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}

}
