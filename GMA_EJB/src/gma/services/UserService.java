package gma.services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.NonUniqueResultException;
import gma.entities.User;
import gma.exceptions.*;
import java.util.List;

@Stateless
public class UserService {
	@PersistenceContext(unitName = "GMA")
	private EntityManager em;

	public UserService() {
	}

	public User checkCredentials(String usrn, String pwd) throws CredentialsException{
		List<User> uList = null;
		try {
			uList = em.createNamedQuery("User.checkCredentials", User.class).setParameter(1, usrn).setParameter(2, pwd).getResultList();
		} catch (PersistenceException e) {
			throw new CredentialsException("Could not verify credentals");
		}
		if (uList.isEmpty())
			return null;
		else
			return uList.get(0);
	}
}
