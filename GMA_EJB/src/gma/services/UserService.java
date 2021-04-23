package gma.services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import gma.entities.Access;
import gma.entities.Answer;
import gma.entities.Questionnaire;
import gma.entities.User;
import gma.exceptions.*;

import java.sql.Timestamp;
import java.util.List;

@Stateless
public class UserService {
	@PersistenceContext(unitName = "GMA")
	private EntityManager em;

	public UserService() {
	}

	public User checkCredentials(String usrn, String pwd) throws CredentialsException {
		User user = null;
		try {
			user = em.createNamedQuery("User.checkCredentials", User.class).setParameter(1, usrn).setParameter(2, pwd)
					.getResultStream().findFirst().orElse(null);
		} catch (PersistenceException e) {
			throw new CredentialsException("Could not verify credentials");
		}
		return user;
	}
	public User getUserById(int id) throws CredentialsException {
		User user = null;
		try {
			user = em.createNamedQuery("User.findUserById", User.class).setParameter(1, id)
					.getResultStream().findFirst().orElse(null);
		} catch (PersistenceException e) {
			throw new CredentialsException("User not found correctly");
		}
		return user;
	}
	public void logAccess(User user) throws AccessException {
		try {
			// Log the access in the database
			Access access = new Access();
			access.setUser(user);
			access.setTimestamp(new Timestamp(System.currentTimeMillis()));
			em.persist(access);
		} catch (PersistenceException e) {
			throw new AccessException("Access not insert correctly!");
		}
	}

	public void banUser(User user) throws AccessException {
		try {
			user.setType(0);
			em.merge(user);
		} catch (PersistenceException e) {
			throw new AccessException("Error during the ban phase!");
		}
	}
}
