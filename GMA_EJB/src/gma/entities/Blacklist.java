package gma.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: Blacklist
 *
 */
@Entity
@NamedQueries({
//@NamedQuery(name = "Blacklist.search", query = "SELECT b FROM Blacklist b WHERE b.word = ?1")
@NamedQuery(name = "Blacklist.findOffensiveWord", query = "SELECT b FROM Blacklist b WHERE b.word IN ?1")
})

public class Blacklist implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String word;

	public Blacklist() {
	}

	public Blacklist(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

}
