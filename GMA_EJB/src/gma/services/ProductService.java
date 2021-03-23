package gma.services;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import gma.entities.Product;
import gma.entities.User;

@Stateless
public class ProductService {
	@PersistenceContext(unitName = "GMA")
	private EntityManager em;

	public ProductService() {
	}

	public Product getProductOfDay(){
		return null;
	}
}
