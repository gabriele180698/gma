package gma.services;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import gma.controllers.EJB;
import gma.entities.Product;
import gma.entities.Review;
import gma.exceptions.*;

@Stateless
public class ProductService {
	@PersistenceContext(unitName = "GMA")
	private EntityManager em;
	@EJB(name = "gma.services/QuestionnaireService.java")
	private QuestionnaireService qService;

	public ProductService() {
	}

	public Product getProductOfDay() throws ProductException {
		Product product = null;
		try {
			product = em.createNamedQuery("Product.findProductByDate", Product.class).setParameter(1, new Date())
					.getResultStream().findFirst().orElse(null);
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw new ProductException("Something went wrong during searching the product of the day!");
		}
		return product;
	}

	public List<Review> getAllReviews(Product product) {
		return product.getReviews();
	}
	
	
	// store in the database create product
	public Product createProductQuestionnaireAndQuestions(String pictureName, byte[] imgByteArray, Date date, List<String> questions) throws ProductException {
		Product product = new Product();
		try {
			product.setName(pictureName);
			product.setImg(imgByteArray);
			em.persist(product);
			qService.createQuestionnaireAndQuestions(date, product, questions);
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw new ProductException("Something went wrong during creation of the product!");
		}
		return product;
	}
}
