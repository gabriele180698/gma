package gma.services;

import java.util.Arrays;
import java.util.Collection;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import gma.entities.Blacklist;
import gma.exceptions.*;

@Stateless
public class BlacklistService {
	@PersistenceContext(unitName = "GMA")
	private EntityManager em;

	public BlacklistService() {
	}

	public Blacklist searchOffensiveWord(String answers) throws BlacklistException {
		Blacklist words;
		Collection<String> answersSplit;
		try {
			// Split the input string in single words
			answersSplit = Arrays.asList(answers.split(" "));
			// use of the named query of the entity Blacklist
			words = em.createNamedQuery("Blacklist.findOffensiveWord", Blacklist.class).setParameter(1, answersSplit)
					.getResultStream().findFirst().orElse(null);

		} catch (PersistenceException e) {
			e.printStackTrace();
			throw new BlacklistException("No questionnaire of the day found!");
		}
		return words;
	}
}
