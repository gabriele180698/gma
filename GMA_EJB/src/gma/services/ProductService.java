package gma.services;

import java.util.Date;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import gma.entities.Product;
import gma.exceptions.*;

@Stateless
public class ProductService {
	@PersistenceContext(unitName = "GMA")
	private EntityManager em;

	public ProductService() {
	}

	public Product getProductOfDay() throws ProductException{
		Product product = null;
		try {
			//Get the product of the day
			product = em.createNamedQuery("Product.findProductByDate", Product.class).setParameter(1, new Date()).getResultStream().findFirst().orElse(null);
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw new ProductException("No product of the day found!");
		}
		return product;
	}
}
