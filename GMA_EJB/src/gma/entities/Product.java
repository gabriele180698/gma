package gma.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Product
 *
 */
@Entity
public class Product implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String name;
	private String img;
	@OneToMany(mappedBy = "product")
	private List<Questionnaire> questionnaires;

	public Product() {
	}

	public Product(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public List<Questionnaire> getQuestionnaires() {
		return this.questionnaires;
	}

	public void addQuestionnaire(Questionnaire questionnaire) {
		getQuestionnaires().add(questionnaire);
		questionnaire.setProduct(this);
	}

	public void removeMission(Questionnaire questionnaire) {
		getQuestionnaires().remove(questionnaire);
	}

}
