package managers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import customBeans.VoziloBrojInstruktoraBean;
import model.Instruktor;
import model.Kategorija;
import model.Vozilo;

public class VoziloManager {
	
	public Vozilo saveVozilo2(String brRegistracije, Date godiste,String gorivo, 
			String marka, float mesecnaKm,float mesecnaPotr, String model,
			Date rokReg,float troskoviOdr, Kategorija kat){
		try{
			EntityManager em = JPAUtils.getEntityManager();
			em.getTransaction().begin();
			Vozilo v = new Vozilo();
			v.setBrRegistracije(brRegistracije);
			v.setGodiste(godiste);
			v.setGorivo(gorivo);
			v.setMarka(marka);
			v.setMesecnaKilometraza(mesecnaKm);
			v.setMesecnaPotrosnja(mesecnaPotr);
			v.setModel(model);
			v.setRokRegistracije(rokReg);
			v.setTroskoviOdrzavanja(troskoviOdr);
			v.setKategorija(kat);
			em.persist(v);
			em.getTransaction().commit();
			em.close();
			return v;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public Vozilo saveVozilo(Vozilo v){
		try {
			EntityManager em = JPAUtils.getEntityManager();
			em.getTransaction().begin();
			em.persist(v);
			em.getTransaction().commit();
			em.close();
			return v;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean updateVozilo(Vozilo v){
		try {
			EntityManager em = JPAUtils.getEntityManager();
			em.getTransaction().begin();
			System.out.println("Pre merge");
			
//			for(Instruktor i:v.getInstruktors()){
//				if(!i.getVozilos().contains(v)){
//					i.getVozilos().add(v);
//				}
//			}
			em.merge(v);
			em.flush();
			System.out.println("Posle merge-a");
			em.getTransaction().commit();
			em.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}// updateVozilo
	
	
	public int getIdForVozilo(Vozilo v){
		try{
			EntityManager em = JPAUtils.getEntityManager();
			int idVoz = v.getIdVozila();
			return idVoz;
		}catch(Exception e){
			e.printStackTrace();
			return 0;
		}
	}
	
	// vraca Listu Vozila za odredjenu kategoriju po opadajucem poretku
	public List<Vozilo> getVozilaKategorijeZaMaxKilometraza(int idKat){
		try{
			EntityManager em = JPAUtils.getEntityManager();
			TypedQuery<Vozilo> upit = em.createQuery("SELECT v FROM Vozilo v WHERE v.kategorija.idKategorija=:idKat ORDER BY v.mesecnaKilometraza desc", Vozilo.class);
			upit.setParameter("idKat", idKat);
			List<Vozilo> vozilaSaMaxKm = upit.getResultList();
			em.close();
			return vozilaSaMaxKm;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}// getVozilaKategorijeZaMaxKilometraza
	
	// vraca Listu Vozila za odredjenu kategoriju po opadajucem poretku
		public List<Vozilo> getVozilaKategorijeZaTroskoveOdrzavanja(String nazivKat){
			try{
				EntityManager em = JPAUtils.getEntityManager();
				TypedQuery<Vozilo> upit = em.createQuery("SELECT v FROM Vozilo v WHERE v.kategorija.naziv LIKE :nazivKat ORDER BY v.troskoviOdrzavanja asc", Vozilo.class);
				upit.setParameter("nazivKat", nazivKat);
				List<Vozilo> vozilaTroskoviOdrzavanja = upit.getResultList();
				em.close();
				return vozilaTroskoviOdrzavanja;
			}catch(Exception e){
				e.printStackTrace();
				return null;
			}
		}// getVozilaKategorijeZaMaxKilometraza
	
	
	public Vozilo getVoziloForId(int id){
		try {
			EntityManager em = JPAUtils.getEntityManager();
			em.getTransaction().begin();
			TypedQuery<Vozilo> q = em.createQuery("from Vozilo v join fetch v.instruktors where v.idVozila=:id", Vozilo.class);
			q.setParameter("id", id);
			Vozilo voz = q.getSingleResult();
			em.getTransaction().commit();
			em.close();
			return voz;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	// vraca instruktorE koji vrese obuku nas ISTIM vozilom
	public static List<Instruktor> getInstruktorsByVozilo(Vozilo v){
		try {
			EntityManager em = JPAUtils.getEntityManager();
			List<Instruktor> instruktors = v.getInstruktors();
			em.close();
			return instruktors;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	// vraca SVA vozila koja koristi odredjeni INSTRUKTOR!
	public List<Vozilo> getVozilosByInstruktor(Instruktor i){
		try {
			EntityManager em = JPAUtils.getEntityManager();
			List<Vozilo> vozila = i.getVozilos();
			em.close();
			return vozila;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	} 
	
	/* Vraca Listu instruktora za odabrano Vozilo na osnovu marke, modela i godista */
	public List<Instruktor> getInstrutkrosByVozilo(String marka, String model){
		try{
			EntityManager em = JPAUtils.getEntityManager();
			TypedQuery<Instruktor> upit = em.createQuery("SELECT i FROM Instruktor i JOIN FETCH i.vozilos v WHERE v.marka LIKE :marka AND v.model LIKE :model ORDER BY i.ime asc", Instruktor.class);
			upit.setParameter("marka", "%"+marka+"%");
			upit.setParameter("model", "%"+model+"%");
			
			List<Instruktor> instruktoriVozila = upit.getResultList();
			em.close();
			return instruktoriVozila;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	
	}
	
	
	public List<Kategorija>getSveKategorije(EntityManager em){
		TypedQuery<Kategorija> query = em.createQuery("select k from Kategorija k",Kategorija.class);
		List<Kategorija> result = query.getResultList();
		em.close();
		return result;
	}
	
	public List<Vozilo>getSvaVozila(EntityManager em){
		TypedQuery<Vozilo> upit = em.createQuery("SELECT v FROM Vozilo v",Vozilo.class);
		List<Vozilo> vozila = upit.getResultList();
		em.close();
		return vozila;
	}
	
	// vraca listu vozila po kriterijumima kao unetima formalnim parametrima
	public List<Vozilo> getVoziloByMarkaModelGorivo(String marka,String model,String gorivo,String brRegistracije){
		try{
			EntityManager em = JPAUtils.getEntityManager();
			TypedQuery<Vozilo> upit = em.createQuery("SELECT v FROM Vozilo v WHERE v.marka LIKE :marka and v.model LIKE :model and v.gorivo LIKE :gorivo and v.brRegistracije LIKE :brRegistracije", Vozilo.class);
			upit.setParameter("marka", "%"+marka+"%");
			upit.setParameter("model", "%"+model+"%");
			upit.setParameter("gorivo", "%"+gorivo+"%");
			upit.setParameter("brRegistracije", "%"+brRegistracije+"%");
			List<Vozilo> vozilaByMMG = upit.getResultList();
			em.close();
			return vozilaByMMG;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	
	public List<Vozilo> getVoziloInstruktoraByMarkaModelGorivo(int idInst,String marka,String model,String gorivo,String brRegistracije){
		try{
			EntityManager em = JPAUtils.getEntityManager();
			TypedQuery<Vozilo> upit = em.createQuery("SELECT v FROM Vozilo v JOIN v.instruktors i WHERE i.idInstruktor=:idInst AND v.marka LIKE :marka and v.model LIKE :model and v.gorivo LIKE :gorivo and v.brRegistracije LIKE :brRegistracije", Vozilo.class);
			upit.setParameter("idInst", idInst);
			upit.setParameter("marka", "%"+marka+"%");
			upit.setParameter("model", "%"+model+"%");
			upit.setParameter("gorivo", "%"+gorivo+"%");
			upit.setParameter("brRegistracije", "%"+brRegistracije+"%");
			List<Vozilo> vozilaInstByMMG = upit.getResultList();
			em.close();
			return vozilaInstByMMG;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	// prosledjuje idKategorija kao parametar
	public List<Vozilo> getVozilaByKategorijas(int idKat){
		try{
			EntityManager em = JPAUtils.getEntityManager();
			em.getTransaction().begin();
			TypedQuery<Vozilo> upit = em.createQuery("SELECT v FROM Vozilo v JOIN FETCH v.kategorija k WHERE k.idKategorija=:idKat", Vozilo.class);
			upit.setParameter("idKat", idKat);
			List<Vozilo> vozilaKategorije = upit.getResultList();
			em.getTransaction().commit();
			em.close();
			return vozilaKategorije;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Vozilo> getVozilaByKategorijas2(Kategorija kat){
		try{
			EntityManager em = JPAUtils.getEntityManager();
			em.getTransaction().begin();
			int idKat = kat.getIdKategorija();
			TypedQuery<Vozilo> upit = em.createQuery("SELECT v FROM Vozilo v JOIN FETCH v.kategorija k WHERE k.idKategorija=:idKat", Vozilo.class);
			upit.setParameter("idKat", idKat);			
			List<Vozilo> vozilaKategorije = upit.getResultList();
//			List<Vozilo> vozilaKategorije = kat.getVozilos();
			em.getTransaction().commit();
			em.close();
			return vozilaKategorije;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}//getVozilaByKategorijas2
	
	public List<Vozilo> getVozilaByKategorijas3(Kategorija kat){
		try{
			EntityManager em = JPAUtils.getEntityManager();
			List<Vozilo> vozilaKategorije = kat.getVozilos();
			em.close();
			return vozilaKategorije;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}//getVozilaByKategorijas3
	
	
	public Vozilo getVoziloByModel(String model){
		try{
			EntityManager em = JPAUtils.getEntityManager();
			Query upit=em.createQuery("SELECT v FROM Vozilo v WHERE v.model=:model");
			upit.setParameter("model", model);
			Vozilo voz = (Vozilo)upit.getSingleResult();
			em.close();
			return voz;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	
	// mozda ce biti problema  sa povratnom vrednoscu (true/false),
	// jer nema return-a u finally bloku... ISPRAVI da bude isto kao i Instruktor-Kategorija!!!
	public boolean deleteVozilo(int id){
		EntityManager em = JPAUtils.getEntityManager();
		EntityTransaction et = null;
		try{
			et = em.getTransaction();
			et.begin();
			Vozilo v = em.find(Vozilo.class, id);
			Vozilo ukloniV = em.merge(v);
			em.remove(ukloniV);
			em.flush();
			et.commit();
			return true;
		}catch(Exception e){
			System.out.println("Dogodio se izuzetak");
			e.printStackTrace();
			if(et!=null)
				et.rollback();
			return false;
		}finally{
			if(em!=null && em.isOpen())
				em.close();
		}
	}//deleteVozilo
	
	/* Suvisan metod - BAD SMELL!!! */
//	public List<VoziloBrojInstruktoraBean> vozilaSaMaxInstruktora2(int idKat){
//		EntityManager em = JPAUtils.getEntityManager();
//		Query upit = em.createQuery("SELECT v,count(inst) FROM Vozilo v JOIN FETCH v.instruktors inst JOIN FETCH inst.kategorijas kat WHERE kat.idKategorija:=idKat GROUP BY v COUNT (inst) desc");
//		upit.setParameter("idKat",idKat);
//		List<Object[]> rez = upit.getResultList();
//		List<VoziloBrojInstruktoraBean> vozilaMaxInst = new ArrayList<VoziloBrojInstruktoraBean>();
//		for(Object[] item:rez){
//			int br = Math.toIntExact((Long)item[1]);
//			VoziloBrojInstruktoraBean vbib = new VoziloBrojInstruktoraBean((Vozilo)item[0],br);
//			vozilaMaxInst.add(vbib);
//		}
//		em.close();
//		return vozilaMaxInst;
//	}
	
	// OVAJ je UPOTREBLJIV!!!
	public List<VoziloBrojInstruktoraBean> vozilaSaMaxInstruktora(){
		EntityManager em = JPAUtils.getEntityManager();
		Query upit = em.createQuery("SELECT v,count(inst) FROM Vozilo v JOIN FETCH v.instruktors inst GROUP BY v ORDER BY COUNT (inst) desc");
		List<Object[]> rez = upit.getResultList();
		List<VoziloBrojInstruktoraBean> vozilaMaxInst = new ArrayList<VoziloBrojInstruktoraBean>();
		for(Object[] item:rez){
			int br = Math.toIntExact((Long)item[1]);
			VoziloBrojInstruktoraBean vbib = new VoziloBrojInstruktoraBean((Vozilo)item[0],br);
			vozilaMaxInst.add(vbib);
		}
		em.close();
		return vozilaMaxInst;
	}
	
	public static void main(String[] args) {
//		VoziloManager vm = new VoziloManager();
//		List<Vozilo> listaVozilaTroskovi = vm.getVozilaKategorijeZaTroskoveOdrzavanja("B");
//		for(Vozilo v:listaVozilaTroskovi){
//			System.out.println("Vozilo "+v.getMarka()+" "+v.getModel()+", kategorije "+v.getKategorija().getNaziv());
//		}
//		List<Instruktor> listInstVozila = vm.getInstrutkrosByVozilo("Ford", "Focus");
//		for(Instruktor i:listInstVozila){
//			System.out.println("Instruktor "+i.getIme()+" "+i.getPrezime());
//		}
//		List<Vozilo> listaVozilaInst = vm.getVoziloInstruktoraByMarkaModelGorivo(2,"","","","");
//		Instruktor i = new InstruktorManager().getInstruktorForId(2);
//		System.out.println("Vozila instruktora "+i.getIme()+" "+i.getPrezime());
//		for(Vozilo v:listaVozilaInst){
//			System.out.println("Vozilo marke "+v.getMarka()+", model: "+v.getModel()+", kategorija: "+v.getKategorija().getNaziv());
//			
//		}
//		Kategorija kat = new KategorijaManager().getKategorijaByNaziv("B");
//		int idKat = kat.getIdKategorija();
//		List<Vozilo> vozZaKat = vm.getVozilaByKategorijas(idKat);
//		for(Vozilo v: vozZaKat){
//			System.out.println("Vozilo marke: "+v.getMarka()+", model: "+v.getModel());
//		}
//		vm.deleteVozilo(3);
//		try {
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//			Date godiste = sdf.parse("2009-01-01");
//			List<Instruktor> listInst = vm.getInstrutkrosByVozilo("Volvo", "", godiste);
//			for(Instruktor i:listInst){
//				System.out.println("Instruktor "+i.getIme()+" "+i.getPrezime());
//			}
//			Date rokReg = sdf.parse("2017-08-07");
//			Kategorija kat = new KategorijaManager().getKategorijaForId(2);
//			Vozilo v1 = vm.saveVozilo("NS764FC", godiste, "benzin", "Volkswagen", 320.0f, 20.0f, "Jetta", rokReg, 43000.0f, kat);
//			System.out.println("Sacuvano je vozilo marke: "+v1.getMarka()+", model: "+v1.getModel()+
//					" tipa "+v1.getGorivo()+" sa id-jem:"+v1.getIdVozila());
//			Vozilo v2 = vm.getVoziloForId(2);
//			List<Instruktor> insts = vm.getInstruktorsByVozilo(v2);
//			for(Instruktor i:insts){
//				System.out.println("Instruktor "+i.getIme()+" "+i.getPrezime()+" vozi "
//						+v2.getModel()+" marke "+v2.getMarka()+", kategorije "+v2.getKategorija().getNaziv()+".");
//			vm.deleteVozilo(3);
//			}
//		}catch (Exception e) {
//			e.printStackTrace();
//		}
//		System.exit(0);
	}//main
}