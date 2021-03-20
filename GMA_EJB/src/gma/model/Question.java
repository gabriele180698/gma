package gma.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Question
 *
 */
@Entity
public class Question implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String text;
	@OneToMany(mappedBy = "question")
	private List<Answer> answers;
	@ManyToOne
	@JoinColumn(name = "questionnaire")
	private Questionnaire questionnaire;

	public Question() {
	}

	public Question(int id) {
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

	public List<Answer> getAnswers() {
		return this.answers;
	}

	public void addAnswer(Answer answer) {
		getAnswers().add(answer);
		answer.setQuestion(this);
	}

	public void removeAnswer(Answer answer) {
		getAnswers().remove(answer);
	}
	
	public void setQuestionnaire(Questionnaire questionnaire) {
		this.questionnaire = questionnaire;
	}
	
	public Questionnaire getQuestionnaire() {
		return this.questionnaire;
	}

}
