package de.tu_berlin.pjki_server.persistence;

import java.util.List;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import de.tu_berlin.pjki_server.game_engine.AbstractGame;

@Singleton
public class Controller {

	private final int MAXQUERY = 10;
	private static Controller INSTANCE;
	private final EntityManager entityManager;
        
	private Controller() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("$objectdb/db/game.odb");
		entityManager = emf.createEntityManager();
	}	
	
	public synchronized static Controller getController() {
		if (INSTANCE == null) {
			INSTANCE = new Controller();
		}
		return INSTANCE;
	}
	
	public EntityManager getEntityManager() {
		return entityManager;
	}
	
	public synchronized void persistGame(AbstractGame game) {
		entityManager.getTransaction().begin();
		entityManager.persist(game);
		entityManager.getTransaction().commit();
	}	
	
	public synchronized void deleteTable(Class<? extends AbstractGame> gameClass) {
		entityManager.getTransaction().begin();
		entityManager.createQuery("DELETE FROM " + gameClass.getSimpleName()).executeUpdate();
		entityManager.getTransaction().commit();
	}
	
	public synchronized List<? extends AbstractGame> query(Class<? extends AbstractGame> gameClass, int count){
		TypedQuery<? extends AbstractGame> query = getEntityManager()
        		.createQuery("SELECT p FROM %s p".formatted(gameClass.getSimpleName()), gameClass).setMaxResults(count);
        return query.getResultList();
	}
	
	public synchronized List<? extends AbstractGame> query(Class<? extends AbstractGame> gameClass){
		return query(gameClass, MAXQUERY);
	}
	
}
