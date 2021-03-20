package gma.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: user
 *
 */
@Entity
public class User implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String username;
	private String email;
	private String password;
	private int type;
	@OneToMany(mappedBy = "user")
	private List<Access> accesses;
	@OneToMany(mappedBy = "user")
	private List<Answer> answers;
	@ManyToMany
	@JoinTable(name = "user_questionnaire", joinColumns = { @JoinColumn(name = "idUser") }, inverseJoinColumns = {
			@JoinColumn(name = "idQuestionnaire") })
	private List<Questionnaire> questionnaires;
	
	
	 /*@ElementCollection
	 @CollectionTable(name = "user_questionnaire", joinColumns = @JoinColumn(name = "idUser"))
	                  @MapKeyJoinColumn(name = "idQuestionnaire")
	 @Column(name = "score")
	 @Column(name = "status")
	 @Column(name = "age")
	 @Column(name = "sex")
	 @Column(name = "expertise")
	 private Map<Questionnaire, Integer> questionnaires;*/


	public User() {
	}

	public User(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public List<Questionnaire> getQuestionnaires() {
		return this.questionnaires;
	}

	public void addQuestionnaire(Questionnaire questionnaire) {
		getQuestionnaires().add(questionnaire);
		questionnaire.getUsers().add(this);
	}

	public void removeQuestionnaire(Questionnaire questionnaire) {
		questionnaire.removeUser(this);
		getQuestionnaires().remove(questionnaire);
	}

}
