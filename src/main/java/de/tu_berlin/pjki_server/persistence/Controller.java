package de.tu_berlin.pjki_server.persistence;

import javax.inject.Singleton;
import javax.persistence.Embeddable;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.sound.midi.VoiceStatus;

import de.tu_berlin.pjki_server.game_engine.AbstractGame;

@Singleton
public class Controller {

	private static Controller INSTANCE;
	private EntityManager entityManager;
        
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
	
	public synchronized void persistGame(AbstractGame game) {
		entityManager.getTransaction().begin();
		entityManager.persist(game);
		entityManager.getTransaction().commit();
	}
	
	
	
	
}
