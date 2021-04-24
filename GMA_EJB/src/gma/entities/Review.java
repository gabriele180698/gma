package gma.entities;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Entity implementation class for Entity: Review
 *
 */
@Entity
@Table(name = "review", schema = "gma")
@NamedQuery(name = "Review.findReviewsByProduct", query = "SELECT r FROM Review r JOIN r.product p WHERE p.id = ?1")
public class Review implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String text;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idUser")
	private User user;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idProduct")
	private Product product;

	public Review() {
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
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
}
