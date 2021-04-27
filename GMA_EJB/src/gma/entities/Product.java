package gma.entities;

import java.io.Serializable;
import java.util.Base64;
import java.util.List;

import javax.persistence.*;
import javax.servlet.annotation.MultipartConfig;

/**
 * Entity implementation class for Entity: Product
 *
 */
@Entity
@Table(name = "product", schema = "gma")
@NamedQuery(name = "Product.findProductByDate", query = "SELECT p FROM Product p JOIN p.questionnaires q WHERE q.date = ?1")
public class Product implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String name;
	@Basic(fetch = FetchType.LAZY)
	@Lob
	private byte[] img;
	@OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
	private List<Questionnaire> questionnaires;
	@OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
	private List<Review> reviews;

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

	public byte[] getImg() {
		return img;
	}

	public String getPhotoimageData() {
		return Base64.getMimeEncoder().encodeToString(img);
	}

	public void setImg(byte[] img) {
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

	public List<Review> getReviews() {
		return this.reviews;
	}

	public void addReview(Review review) {
		getReviews().add(review);
		review.setProduct(this);
	}

	public void removeReview(Review review) {
		getReviews().remove(review);
	}

}
