package managers;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import model.Instruktor;
import model.Kategorija;

public class KategorijaManager {
	
	public int saveKategorija(Kategorija k){
		try {
			EntityManager em = JPAUtils.getEntityManager();
			em.getTransaction().begin();
			em.persist(k);
			em.getTransaction().commit();
			em.close();
			return k.getIdKategorija();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public List<Kategorija> vratiSveKategorije(){
		EntityManager em = JPAUtils.getEntityManager();
		em.getTransaction().begin();
		TypedQuery<Kategorija> upit = em.createQuery("SELECT k FROM Kategorija k", Kategorija.class);
		List<Kategorija> kategorijas = upit.getResultList();
		em.close();
		return kategorijas;
	}
	
	public List<Kategorija> vratiSveKategorijeNaziv(){
		EntityManager em = JPAUtils.getEntityManager();
		em.getTransaction().begin();
		TypedQuery<Kategorija> upit = em.createQuery("SELECT k.naziv FROM Kategorija k", Kategorija.class);
		List<Kategorija> kategorijas = upit.getResultList();
		em.close();
		return kategorijas;
	}
	
	// vraca ID kategorije po unetom nazivu kategorije
	public int getKategorijaIdByNaziv(String naziv){
		try{
			EntityManager em = JPAUtils.getEntityManager();
			Query upit=em.createQuery("SELECT k FROM Kategorija k WHERE k.naziv=:naziv");
			upit.setParameter("naziv", naziv);
			Kategorija kat = (Kategorija)upit.getSingleResult();
			em.close();
			return kat.getIdKategorija();
		}catch(Exception e){
			e.printStackTrace();
			return 0;
		}
	}
	
	public Kategorija getKategorijaByNaziv(String naziv){
		try{
			EntityManager em = JPAUtils.getEntityManager();
			Query upit=em.createQuery("SELECT k FROM Kategorija k WHERE k.naziv=:naziv");
			upit.setParameter("naziv", naziv);
			Kategorija kat = (Kategorija)upit.getSingleResult();
			em.close();
			return kat;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	
	public Kategorija getKategorijaForId(int id){
		try {
			EntityManager em = JPAUtils.getEntityManager();
			em.getTransaction().begin();
			TypedQuery<Kategorija> upit = em.createQuery("FROM Kategorija k JOIN FETCH k.instruktors WHERE k.idKategorija=:id",Kategorija.class);
			upit.setParameter("id", id);
			Kategorija kat = upit.getSingleResult();
			em.getTransaction().commit();
			em.close();
			return kat;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public int getIdForKategorija(Kategorija k){
		return k.getIdKategorija();
	}
	
	public Kategorija getKategorijaForId2(int idKat){
		try {
			EntityManager em = JPAUtils.getEntityManager();
			Kategorija kat = em.find(Kategorija.class, idKat);
			em.close();
			return kat;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Instruktor> getInstruktorsByKat(EntityManager em){
		TypedQuery<Instruktor> upit = em.createQuery("SELECT i FROM Instruktor i",Instruktor.class);
		List<Instruktor> sviInstruktoriKat = upit.getResultList();
		em.close();
		return sviInstruktoriKat;
	}
	
	public Kategorija updateKategorija(String naziv,String noviNaziv){
		try {
			EntityManager em = JPAUtils.getEntityManager();
			em.getTransaction().begin();
			// javlja Exception da sam upit, tj. String pod "" nije dobro konstruisan
			Query upit = em.createQuery("UPDATE Kategorija k SET k.naziv=:noviNaziv WHERE k.naziv=:stariNaziv");
			upit.setParameter("noviNaziv", noviNaziv);
			upit.setParameter("naziv", naziv); // mozda je problem i ovde
			
			Kategorija k = (Kategorija)upit.getSingleResult();
			em.getTransaction().commit();
			em.close();
			return k;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<String> vratiSveKategorijeNaziv2(){
		EntityManager em = JPAUtils.getEntityManager();
		em.getTransaction().begin();
		TypedQuery<Kategorija> upit = em.createQuery("SELECT k FROM Kategorija k", Kategorija.class);
		List<Kategorija> sveKategorije = upit.getResultList();
		List<String> naziviKategorija = new ArrayList<String>();
		for(Kategorija k:sveKategorije){
			naziviKategorija.add(k.getNaziv());
		}
		em.close();
		return naziviKategorija;
	}
	
	
	public boolean deleteKategorija(int idKat){
		try{
			EntityManager em = JPAUtils.getEntityManager();
			em.getTransaction().begin();
			Kategorija kat = em.find(Kategorija.class,idKat);
			for(Instruktor i:kat.getInstruktors()){
				if(i.getKategorijas().contains(kat)){
					i.getKategorijas().remove(kat);
				}
			}
			em.remove(kat);
			em.getTransaction().commit();
			em.close();
			return true;			
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public static void main(String[] args) {
//		KategorijaManager km = new KategorijaManager();
//		List<Instruktor> sviInst = km.getInstruktorsByKat(JPAUtils.getEntityManager());
//		for(Instruktor i:sviInst){
//			System.out.println(i.getIme()+" "+i.getPrezime());
//		}
//		int idKategorije = km.getKategorijaIdByNaziv("C1E");
//		System.out.println("Id kategorije C1E je: "+idKategorije);
//		Kategorija k = km.updateKategorija("E","F");
//		System.out.println("Sacuvana je kategorija "+k.getNaziv()+" pod id-jem: "+k.getIdKategorija());
	}
}
