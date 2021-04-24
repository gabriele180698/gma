package gma.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Question
 *
 */
@NamedQueries({
@NamedQuery(name = "Question.findQuestionById", query = "SELECT qu FROM Question qu  WHERE qu.id = ?1"),
@NamedQuery(name = "Question.findUserAnswersByIds", query = "SELECT a FROM Question qu JOIN qu.answers a WHERE qu.questionnaire.id = ?1 AND a.user.id = ?2")
})
@Entity
public class Question implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String text;
	@OneToMany(mappedBy = "question", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private List<Answer> answers;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idQuestionnaire")
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
