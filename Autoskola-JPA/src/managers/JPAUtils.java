package managers;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAUtils {
	private static EntityManagerFactory emFactory;
	
	static{
		emFactory = Persistence.createEntityManagerFactory("Autoskola-JPA");
	}
	
	public static EntityManager getEntityManager(){
		return emFactory.createEntityManager();
	}
}
