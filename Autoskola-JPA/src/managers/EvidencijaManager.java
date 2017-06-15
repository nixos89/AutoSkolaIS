package managers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

import model.Evidencija;
import model.Instruktor;
import model.Kandidat;
import model.Kategorija;
import model.Polaganje;
import model.Prijava;

public class EvidencijaManager {
	
	public Evidencija saveEvidencija2(Kandidat kand, String brLekU,
			Date datumIzdavanjaLu,Date datumUpisa){
		try{
			EntityManager em = JPAUtils.getEntityManager();
			em.getTransaction().begin();
			Evidencija ev = new Evidencija();
			ev.setKandidat(kand);
			ev.setBrLekarUverenja(brLekU);
			ev.setDatumIzdavanjaLu(datumIzdavanjaLu);
			ev.setDatumupisa(datumUpisa);
			em.persist(ev);
			em.getTransaction().commit();
			em.close();
			return ev;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}//saveEvidencija2
	
	public int saveEvidencija(Evidencija ev){
		try{
			EntityManager em = JPAUtils.getEntityManager();
			em.getTransaction().begin();
			em.persist(ev);
			em.getTransaction().commit();
			em.close();
			return ev.getIdEvidencija();
		}catch(Exception e){
			e.printStackTrace();
			return 0;
		}
	}//saveEvidencija
	
	public Evidencija getEvidencijaForId(int id){
		EntityManager em = JPAUtils.getEntityManager();
		em.getTransaction().begin();
		Evidencija ev = em.find(Evidencija.class,id);
		em.getTransaction().commit();
		em.close();
		return ev;
	}
	
	public Evidencija getEvidencijaForIdKandidat(int idKand){
		try{
			EntityManager em = JPAUtils.getEntityManager();
			em.getTransaction().begin();
			Kandidat kand = em.find(Kandidat.class, idKand);
			Evidencija ev = kand.getEvidencija();
			em.close();
			return ev;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	/* Ovo je SUVISAN metod (tj. ovo je PARAZIT metod-BAD SMELL) koji nije UOPSTE potreban, jer 
	 * se evidencija azurira u metodi u KategorijaManager.java klasi !!! */	
	public boolean updateEvidencija(int idEvidencija,String brLekU, Date datumIzdLU, Date datumUpisa){
		EntityManager em = JPAUtils.getEntityManager();
		EntityTransaction et = null;
		boolean retVal = false;
		try{
			et = em.getTransaction();
			et.begin();
			Evidencija ev = em.find(Evidencija.class, idEvidencija);
//			Evidencija promeniEv=em.merge(ev);
			ev.setBrLekarUverenja(brLekU);
			ev.setDatumIzdavanjaLu(datumIzdLU);
			ev.setDatumupisa(datumUpisa);
			em.merge(ev);
			em.flush();
			et.commit();
			retVal=true;
		}catch(Exception e){
			if(et!=null)
				et.rollback();
		}finally{
			if(em!=null & em.isOpen())
				em.close();
		}
		return retVal;
	}
	
	// vraca kandidate upisane u intervalu odDatum-doDatum - PROBAJ sa JOIN naredbama
	// da regulises ovo da ti vraca tacno za Kandidate, a NE SAMO za idEvidencije!!! 
	public List<Evidencija> getKandidatsUpisani(Date odDatum, Date doDatum){
		try{
			EntityManager em = JPAUtils.getEntityManager();
			em.getTransaction().begin();
			TypedQuery<Evidencija> upit = 
					em.createQuery("SELECT e FROM Evidencija e WHERE e.datumupisa BETWEEN :odDatum AND :doDatum", Evidencija.class);
			upit.setParameter("odDatum", odDatum);
			upit.setParameter("doDatum", doDatum);
			List<Evidencija> evidencijas = (List<Evidencija>)upit.getResultList();
			return evidencijas;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}//getKandidatsUpisani
	
	public Prijava savePrijava2(Evidencija ev,Date datumPrijaveTI, Date datumPrijavePI){
		try{
			EntityManager em = JPAUtils.getEntityManager();
			em.getTransaction().begin();
			Prijava prijava = new Prijava();
			prijava.setEvidencija(ev);
			prijava.setDatumprijavetispita(datumPrijaveTI);
			prijava.setDatumprijavepispita(datumPrijavePI);
			em.persist(prijava);
			em.getTransaction().commit();
			em.close();	
			return prijava;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}//savePrijava2
	
	public int savePrijava(Prijava prijava,Evidencija ev){
		try{
			EntityManager em = JPAUtils.getEntityManager();
			em.getTransaction().begin();
			prijava.setEvidencija(ev);
			ev.addPrijava(prijava);				
			em.merge(ev);
			em.flush();
			em.getTransaction().commit();
			em.close();
			return prijava.getIdPrijava();			
		}catch(Exception e){
			e.printStackTrace();
			return 0;
		}
	}//savePrijava
	
	
	public void dodeliPrijavu(Evidencija ev, Prijava prijava){
		try{
			EntityManager em = JPAUtils.getEntityManager();
			em.getTransaction().begin();
//			prijava.setEvidencija(ev); // - Evidencija je VEC dodata pri kreiranju Prijave 
			ev.addPrijava(prijava);
			em.merge(ev); // da bi azuriralo objekat Evidencija
			em.getTransaction().commit();
			em.close();
						
		}catch(Exception e){
			e.printStackTrace();
			
		}
	}//dodeliPrijavu
	
	
	// nadji Evidencija ev i  onda pozovi metod getPrijavas() i to vrati!!!
	public List<Prijava> vratiSvePrijaveZaIdEvidencije(int idEvidencija){
		EntityManager em = JPAUtils.getEntityManager();
		em.getTransaction().begin();
		Evidencija ev = em.find(Evidencija.class, idEvidencija);
		List<Prijava> svePrijave = ev.getPrijavas();
		em.close();
		return svePrijave;
	}
	
	public List<Prijava> vratiSvePrijaveZaIdEvidencije2(int idEvidencija){
		EntityManager em = JPAUtils.getEntityManager();
		em.getTransaction().begin();
		TypedQuery<Prijava> upit = em.createQuery("SELECT p FROM Prijava p WHERE p.evidencija.idEvidencija=:idEvidencija", Prijava.class);
		upit.setParameter("idEvidencija", idEvidencija);
		List<Prijava> svePrijave = upit.getResultList();
		em.close();
		return svePrijave;
	}
	
	
	public List<Prijava> vratiSvePrijaveZaIdEvidencije3(int idEvidencija){
		EntityManager em = JPAUtils.getEntityManager();
		em.getTransaction().begin();
//		Evidencija ev = em.find(Evidencija.class, idEvidencija);
		TypedQuery<Prijava> upit = 
				em.createQuery("SELECT p FROM Prijava p WHERE p.evidencija.idEvidencija=:idEvidencija ORDER BY p.datumprijavetispita asc ORDER BY p.datumprijavepispita asc", Prijava.class);
		upit.setParameter("idEvidencija", idEvidencija);
		List<Prijava> svePrijave = upit.getResultList();
		em.close();
		return svePrijave;
	}
	
	/* ----------------------------------------------------------------------------------------------- */
	/* ----------------------------------------------------------------------------------------------- */
	
	/* METODE vezane za POLAGANJE*/
	
	public Polaganje savePolaganje2(Evidencija ev,Date datumPolaganjaTI, Date datumPolaganjaPI,
			int bodoviTI, int bodoviPI, boolean polozioTI, boolean polozioPI){
		try{
			EntityManager em = JPAUtils.getEntityManager();
			em.getTransaction().begin();
			Polaganje pol = new Polaganje();
			pol.setEvidencija(ev);
			pol.setDatumpolaganjati(datumPolaganjaTI);
			pol.setDatumpolaganjapi(datumPolaganjaPI);
			pol.setBodoviti(bodoviTI);
			pol.setBodovipi(bodoviPI);
			pol.setPolozenti(polozioTI);
			pol.setPolozenpi(polozioPI);
			em.persist(pol);
			em.getTransaction().commit();
			em.close();	
			return pol;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}//savePolaganje2
	
	public int savePolaganje(Polaganje pol,Evidencija ev){
		try{
			EntityManager em = JPAUtils.getEntityManager();
			em.getTransaction().begin();
			pol.setEvidencija(ev);
			if(ev.getPolaganjes().size()<=0){
				ev.setPolaganjes(new ArrayList<Polaganje>());
				ev.addPolaganje(pol);				
			}else{				
				ev.addPolaganje(pol);
			}
			em.merge(ev);
			em.flush();
			em.getTransaction().commit();
			em.close();
			return pol.getIdPolaganja();			
		}catch(Exception e){
			e.printStackTrace();
			return 0;
		}
	}//savePrijava
	
	public void dodeliPolaganje(Polaganje pol, Evidencija ev){
		try{
			EntityManager em = JPAUtils.getEntityManager();
			em.getTransaction().begin();
//			pol.setEvidencija(ev); // - Evidencija je VEC dodata pri kreiranju Polaganja 
			ev.addPolaganje(pol);
			em.merge(ev); // da bi azuriralo objekat Evidencija
			em.getTransaction().commit();
			em.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}//dodeliPolaganje
	
	// nadji Evidencija ev i  onda pozovi metod getPrijavas() i to vrati!!!
	public List<Polaganje> vratiSvaPolaganjaZaIdEvidencije(int idEvidencija){
		EntityManager em = JPAUtils.getEntityManager();
		em.getTransaction().begin();
		Evidencija ev = em.find(Evidencija.class, idEvidencija);
		List<Polaganje> svaPolaganja = ev.getPolaganjes();
		em.close();
		return svaPolaganja;
	}
	
	public List<Polaganje> vratiSvaPolaganjaZaIdEvidencije2(int idEvidencija){
		EntityManager em = JPAUtils.getEntityManager();
		em.getTransaction().begin();
		TypedQuery<Polaganje> upit = em.createQuery("SELECT p FROM Polaganje p WHERE p.evidencija.idEvidencija=:idEvidencija", Polaganje.class);
		upit.setParameter("idEvidencija", idEvidencija);
		List<Polaganje> svaPolaganja = upit.getResultList();
		em.close();
		return svaPolaganja;
	}
	
	
	
	
	public int updateEvidencija2(Evidencija ev){
		try{
			EntityManager em = JPAUtils.getEntityManager();
			em.getTransaction().begin();
			em.merge(ev);
			em.flush();
			em.getTransaction().commit();
			em.close();
			return ev.getIdEvidencija();
		}catch(Exception e){
			e.printStackTrace();
			return 0;
		}
	}// updateEvidencija2
	
	
	
	public static void main(String[] args) {		
//		EvidencijaManager evm = new EvidencijaManager();
		
//		List<Prijava> listaPrijavaKand = evm.vratiSvePrijaveZaIdEvidencije3(11);
//		Evidencija ev = new EvidencijaManager().getEvidencijaForId(11);
//		Kandidat k = new KandidatManager().getKandidatByEvidencija(ev);
//		System.out.println("Prijave kandidata "+k.getIme()+" "+k.getPrezime());
//		for(Prijava p:listaPrijavaKand){
//			System.out.println("Prijava testova: "+p.getDatumprijavetispita()+", prijava voznje: "+p.getDatumprijavepispita());
//		}
//		try{
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//			Date dateOd = sdf.parse("2016-01-05");
//			Date dateDo = sdf.parse("2016-10-10");
//			Instruktor i = new InstruktorManager().getInstruktorForId(1);
//			Kategorija kat = new KategorijaManager().getKategorijaForId2(2);
//			List<Kandidat> kandidatiPolagaliKodInstZaKat = evm.polaganjaVoznjeInstruktorKategorija(i,kat,dateOd,dateDo);
//			List<Kandidat> kandidatiPolagaliKodInstZaKat2 = evm.polaganjaVoznjeInstruktorKategorija2(1,2,dateOd,dateDo);
//			if(kandidatiPolagaliKodInstZaKat2==null)
//				System.out.println("Lista je prazna!");
//			for(Kandidat k:kandidatiPolagaliKodInstZaKat){
//				System.out.println("Kandidat "+k.getIme()+" "+k.getPrezime()+
//						", instruktor "+k.getInstruktor().getIme()+" "+k.getInstruktor().getPrezime());
//			}
//			Evidencija ev = evm.getEvidencijaForIdKandidat(1);
//			Kandidat k = new KandidatManager().getKandidatByEvidencija(ev);
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//			savePolaganje2(Evidencija ev,Date datumPolaganjaTI, Date datumPolaganjaPI,
//					int bodoviTI, int bodoviPI, boolean polozioTI, boolean polozioPI)

//			Date datumPolTI = sdf.parse("");
//			Date datumPrijaveTI = sdf.parse("2016-08-02");
//			Date datumPrijavePI = sdf.parse("2016-08-13");
//			Prijava prijava = evm.savePrijava2(ev, datumPrijaveTI, datumPrijavePI);
			/* MOZDA ce javiti GRESKU/Exception, jer pri kreiranju prijave ta prijava SME 
			 * da ima JEDNU i SAMO JEDNU Evidenciju, a ovde se ona OPET dodaje, sto NIJE moguce !*/			
//			evm.dodeliPrijavu(ev, prijava); 
//			List<Prijava> listaPrijava = evm.vratiSvePrijaveZaIdEvidencije(1);
//			System.out.println("Datumi prijava ispita kandidata "+k.getIme()+" "+k.getPrezime()+":");
//			for(Prijava p:listaPrijava){
//				System.out.println("ID prijave: "+p.getIdPrijava()+", datum prijave teorije: "+p.getDatumprijavetispita());
//			}
//			
//			
//			Date datumILU = sdf.parse("2016-06-08");
//			Date datumUpisa = sdf.parse("2016-08-27");
//			evm.updateEvidencija(6, "LU5519666B", datumILU, datumUpisa);
//			Date odDatum = sdf.parse("2016-08-27");
//			Date doDatum = sdf.parse("2016-08-28");	
//			List<Evidencija> evds = evm.getKandidatsUpisani(odDatum, doDatum);
//			for(Evidencija e:evds){
//				System.out.println("Evidencija za id: "+e.getIdEvidencija());
//			}
//		}catch(Exception e){
//			e.printStackTrace();
//		}
	}//main
}