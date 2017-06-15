package managers;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.EntityManager;

import model.Instruktor;
import model.Kategorija;

public class InstruktorKategorijaManager {
	
	/* Iskoristi za UnosInstruktoraManagedBean pri 
	 * VIŠESTRUKOM SELEKTOVANJU Kategorija i Vozila!!! */
	public void povezi(Instruktor i, Kategorija k){
		EntityManager em = JPAUtils.getEntityManager();
		em.getTransaction().begin();
		i.getKategorijas().add(k);
		k.getInstruktors().add(i);
		em.merge(i);
		em.merge(k);
		em.getTransaction().commit();
		em.close();
	}
	
	public static void main(String[] args) {
//		try {
//			InstruktorKategorijaManager ikm = new InstruktorKategorijaManager(); 
//			InstruktorManager im = new InstruktorManager();
//			KategorijaManager km = new KategorijaManager();
//			Kategorija k = km.getKategorijaForId(3);
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//			Date datumR = sdf.parse("1973-07-04");
//			Instruktor i = im.saveInstruktor("Emilijen", "Vozac", "008943176", datumR, "emilijeeen@yahoo.fr", "2902730009234", 
//					"petrineS", "069415476", "Marselj", "Frerazaka", "6c");
//			Instruktor i = im.getInstruktorForId(1);
//			System.out.println("Sacuvan je instruktor "+i.getIme()+" "+i.getPrezime()+".");
//			ikm.povezi(i, k);
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		System.exit(0);
	}

}
