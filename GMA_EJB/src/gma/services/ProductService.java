package gma.services;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import gma.entities.Product;
import gma.entities.Review;
import gma.exceptions.*;

@Stateless
public class ProductService {
	@PersistenceContext(unitName = "GMA")
	private EntityManager em;

	public ProductService() {
	}

	public Product getProductOfDay() throws ProductException {
		Product product = null;
		try {
			product = em.createNamedQuery("Product.findProductByDate", Product.class).setParameter(1, new Date())
					.getResultStream().findFirst().orElse(null);
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw new ProductException("Can not get the product of the day!");
		}
		return product;
	}

	public List<Review> getAllReviews(int idProduct) throws ProductException {
		List<Review> reviews = null;
		try {
			reviews = em.createNamedQuery("Review.findReviewsByProduct", Review.class).setParameter(1, idProduct)
					.getResultList();
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw new ProductException("Can not get review!");
		}
		return reviews;
	}
	public void createProduct(String pictureName, String img) {
		Product p = new Product();
		p.setName(pictureName);
		p.setImg(img);
	}
}
