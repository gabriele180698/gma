package gma.services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import gma.entities.Product;
import gma.exceptions.*;

@Stateless
public class LeaderboardService {
	@PersistenceContext(unitName = "GMA")
	private EntityManager em;

	public LeaderboardService() {
	}

	public Product getLeaderboard() throws LeaderboardException {
		Product product = null;
		try {

		} catch (PersistenceException e) {
			e.printStackTrace();
			throw new LeaderboardException("It is not possible to retrive the leaderboard!");
		}
		return product;
	}
}
