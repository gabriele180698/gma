package gma.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: user
 *
 */
@Entity
@Table(name = "user", schema = "gma")
@NamedQueries({
@NamedQuery(name = "User.checkCredentials", query = "SELECT r FROM User r  WHERE r.username = ?1 and r.password = ?2"),
@NamedQuery(name = "User.findByQuestionnaire", query = "SELECT u FROM User u JOIN u.statistics s "
		+ "WHERE s.questionnaire = ?1")
//@NamedQuery(name = "User.findByQuestionnaireSub", query = "SELECT u FROM User u JOIN u.statistics s "
//		+ "WHERE s.questionnaire = ?1 and s.status = 0"),
//@NamedQuery(name = "User.findByQuestionnaireCanc", query = "SELECT u FROM User u JOIN u.statistics s "
//		+ "WHERE s.questionnaire = ?1 and s.status = 1")
})
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
	@OneToMany(mappedBy = "user")
	private List<Statistics> statistics;
	@OneToMany(mappedBy = "user")
	private List<Review> reviews;

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

	public List<Statistics> getStatistics() {
		return this.statistics;
	}

	public void addStatistics(Statistics statistics) {
		getStatistics().add(statistics);
		statistics.setUser(this);
	}

	public void removeStatistics(Statistics statistics) {
		getStatistics().remove(statistics);
	}
	
	public List<Review> getReviews() {
		return this.reviews;
	}

	public void addReview(Review review) {
		getReviews().add(review);
		review.setUser(this);
	}

	public void removeReview(Review review) {
		getReviews().remove(review);
	}

}
