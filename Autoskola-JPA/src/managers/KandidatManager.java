package managers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import model.Evidencija;
import model.Instruktor;
import model.Kandidat;
import model.Kategorija;
import model.Polaganje;
import model.Vozilo;

public class KandidatManager {
	
	public Kandidat saveKandidat2(String ime, String prezime,String jmbg,
			String brLK,Date datumRodjenja,String email,String lozinka,
			String mesto, String ulica, String broj,String telefon,
			Instruktor inst,Kategorija kat,Evidencija ev){
		try{
			EntityManager em = JPAUtils.getEntityManager();
			em.getTransaction().begin();
			Kandidat k = new Kandidat();
			k.setIme(ime);
			k.setPrezime(prezime);
			k.setJmbg(jmbg);
			k.setBrLicnekarte(brLK);
			k.setDatumrodjenja(datumRodjenja);
			k.setEmail(email);
			k.setLozinka(lozinka);
			k.setMesto(mesto);
			k.setUlica(ulica);
			k.setBroj(broj);
			k.setTelefon(telefon);
			k.setInstruktor(inst);
			k.setKategorija(kat);
			k.setEvidencija(ev);
			em.persist(k);
			em.getTransaction().commit();
			em.close();
			return k;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}	
	}//saveKandidat2	
	
	
	// verovatno treba kao i Instruktor-Kategorije da se dodaje 
	// posebno ako nema idEvidencije...a NEMA GA!
	public int saveKandidat(Kandidat k,Evidencija ev){
		try {
			EntityManager em = JPAUtils.getEntityManager();
			em.getTransaction().begin();
			ev.setKandidat(k);
			k.setEvidencija(ev);
			em.merge(ev); // Promenjeno sa 'merge' na 'persist' u 10h10min, 5.10.2016.
			em.flush(); // dodato 20:27 11.9.2016.
			em.getTransaction().commit();
			em.close();
			return k.getIdKandidat();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	
	public int saveKandidat2(Kandidat k,Evidencija ev){
		try {
			EntityManager em = JPAUtils.getEntityManager();
			em.getTransaction().begin();
			if(k.getIdKandidat()==0){
				em.persist(k);
			}else{
				em.merge(k);
			}
			ev.setKandidat(k); // probaj sa merge ako persist ne radi!
			saveEvidencija(ev);
			em.getTransaction().commit();
			em.close();
			return k.getIdKandidat();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public int saveKandidat3(Kandidat k){
		try {
			EntityManager em = JPAUtils.getEntityManager();
			em.getTransaction().begin();
			em.persist(k);
			em.getTransaction().commit();
			em.close();
			return k.getIdKandidat();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	
	// po Stackoverflow predlogu za Parent-Child 
	// (tema:How to avoid duplicates with JPA cascades?)
	public int saveEvidencija3(Evidencija ev, Kandidat k){
		try {
			EntityManager em = JPAUtils.getEntityManager();
			em.getTransaction().begin();
			if(ev.getIdEvidencija()==0){
				em.persist(ev);
			}else{
				em.merge(ev);
			}
			k.setEvidencija(ev);
			saveKandidat3(k);
			em.getTransaction().commit();
			em.close();
			return ev.getIdEvidencija();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		
	}
	
	// metoda za proveru instruktora
	public Kandidat getKandidatByUserAndPass(String user, String pass){
		EntityManager em = JPAUtils.getEntityManager();
		Query upit = em.createQuery("SELECT k FROM Kandidat k WHERE k.prezime LIKE :user AND k.lozinka LIKE :pass");
		upit.setParameter("user", user);
		upit.setParameter("pass", pass);
		Kandidat kand = (Kandidat) upit.getSingleResult();
		em.close();
		return kand;
	}
	
	public Kandidat getKandidatByEvidencija(Evidencija ev){
		try{
			EntityManager em = JPAUtils.getEntityManager();
			em.getTransaction().begin();
			Kandidat kand = ev.getKandidat();
			em.close();
			return kand;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	
	}
	
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
	
	//cuva evidenciju za prosledjenog Kandidata
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
	}////saveEvidencija2
	
	public List<Kandidat> getKandidatsByImePrezimeJmbg(String ime, String prezime,String jmbg){
		try {
			EntityManager em = JPAUtils.getEntityManager();
			TypedQuery<Kandidat> upit  = em.createQuery("select k from Kandidat k where k.ime like :ime and k.prezime like :prezime and k.jmbg like :jmbg", Kandidat.class);
			upit.setParameter("ime", "%"+ime+"%");
			upit.setParameter("prezime", "%"+prezime+"%");
			upit.setParameter("jmbg", "%"+jmbg+"%");
			List<Kandidat> kands = upit.getResultList();
			em.close();
			return kands;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	// vraca sve Kandidate za unetog instruktora!
	public List<Kandidat> getSviKandidatiInstruktora(int idInstruktora){
		EntityManager em = JPAUtils.getEntityManager();
		em.getTransaction().begin();
		TypedQuery<Kandidat> upit = em.createQuery("SELECT k FROM Kandidat k JOIN k.instruktor inst WHERE inst.idInstruktor=:idInstruktora ORDER BY k.kategorija.naziv asc", Kandidat.class);
		upit.setParameter("idInstruktora", idInstruktora);
		List<Kandidat> listaKandidataInst = upit.getResultList();
		em.close();
		return listaKandidataInst;
	}
	
	// Vraca SVE kandidate po njihovom imenu/prezimenu/jmbg-u i za UNET IdInstruktora!!!
	public List<Kandidat> getSviKandidatiInstruktora2(int idInstruktora,String ime,String prezime,String jmbg){
		EntityManager em = JPAUtils.getEntityManager();
		em.getTransaction().begin();
		TypedQuery<Kandidat> upit = em.createQuery("SELECT k FROM Kandidat k JOIN k.instruktor inst WHERE inst.idInstruktor=:idInstruktora AND k.ime LIKE :ime AND k.prezime LIKE :prezime AND k.jmbg LIKE :jmbg", Kandidat.class);
		upit.setParameter("idInstruktora", idInstruktora);
		upit.setParameter("ime", "%"+ime+"%");
		upit.setParameter("prezime", "%"+prezime+"%");
		upit.setParameter("jmbg", "%"+jmbg+"%");
		List<Kandidat> listaKandidataInst = upit.getResultList();
		em.close();
		return listaKandidataInst;
	}
		
	public List<Kandidat> getKandidatiZaKategoriju2(int idKat){
		EntityManager em = JPAUtils.getEntityManager();
		em.getTransaction().begin();
		TypedQuery<Kandidat> upit = em.createQuery("SELECT k FROM Kandidat k JOIN k.kategorija kat WHERE kat.idKategorija=:idKat",Kandidat.class); // ubaceno "SELECT k,kat.nazi FROM..."
		upit.setParameter("idKat", idKat);
		List<Kandidat> retVal = upit.getResultList();
		em.close();
		return retVal;
	}	
	
	
	public Kandidat getKandidatForId(int id){
		EntityManager em = JPAUtils.getEntityManager();
		em.getTransaction().begin();
		Kandidat k = em.find(Kandidat.class,id);
		em.getTransaction().commit();
		em.close();
		return k;
	}
	
	// sluzi da dodeli idEvidencije kandidatu za metod 'saveKandidat' - SUVISNA metoda, BAD SMELL!!!!!!!!!!!!!!!! 
	public Evidencija getEvidencijaForId2(int idKandidat){
		EntityManager em = JPAUtils.getEntityManager();
		Kandidat kand = em.find(Kandidat.class, idKandidat);
		Evidencija ev = kand.getEvidencija();
		em.close();
		return ev;
	}
	
	// sluzi da dodeli idEvidencije kandidatu za metod 'saveKandidat' 
		public Evidencija getEvidencijaForId(int idKand){
			EntityManager em = JPAUtils.getEntityManager();
			em.getTransaction().begin();
			Kandidat kand = em.find(Kandidat.class, idKand);
			Query upit = em.createQuery("SELECT ev FROM Evidencija ev JOIN ev.kandidat kand WHERE kand.idKandidat=:idKand");
			upit.setParameter("idKand", idKand);
			Evidencija evd = (Evidencija)upit.getSingleResult();
			em.close();
			return evd;
		}
	
	public List<Instruktor> getSviInstruktori(EntityManager em){
		TypedQuery<Instruktor> upit = em.createQuery("SELECT i FROM Instruktor i", Instruktor.class);
		List<Instruktor> sviInstruktori=  upit.getResultList();
		em.close();
		return sviInstruktori;
	}
	
	public List<Kandidat> getSviKandidati(){
		EntityManager em = JPAUtils.getEntityManager();
		TypedQuery<Kandidat> upit = em.createQuery("SELECT k FROM Kandidat k", Kandidat.class);
		List<Kandidat> sviKandidati = upit.getResultList();
		em.close();
		return sviKandidati;
	}
	
	// TESTIRAJ i dodaj u ovaj ISTI metod PROMENU evidencije!
	public Kandidat updateKandidat2(Kandidat k, String ime, String prezime,String jmbg,
			String brLK,Date datumRodjenja,String email,String lozinka,
			String mesto, String ulica, String broj,String telefon,
			Instruktor inst,Kategorija kat){
		
		try{
			EntityManager em = JPAUtils.getEntityManager();
			em.getTransaction().begin();
			k.setIme(ime);
			k.setPrezime(prezime);
			k.setJmbg(jmbg);
			k.setBrLicnekarte(brLK);
			k.setDatumrodjenja(datumRodjenja);
			k.setEmail(email);
			k.setLozinka(lozinka);
			k.setMesto(mesto);
			k.setUlica(ulica);
			k.setBroj(broj);
			k.setTelefon(telefon);
			k.setInstruktor(inst);
			k.setKategorija(kat);
			em.merge(k);
			em.getTransaction().commit();
			System.out.println("Kandidat sa id-jem: "+k.getIdKandidat()+" je azuriran!");
			return k;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}//updateKandidat2
	
	public int updateKandidat(Kandidat k,Evidencija ev){
		try {
			EntityManager em = JPAUtils.getEntityManager();
			em.getTransaction().begin();
			System.out.println("Pre 1. merge 'ev'");
			em.merge(ev);
			System.out.println("POSLE 1. merge 'ev'");
			em.merge(k);
			ev.setKandidat(k);
			k.setEvidencija(ev);
			em.merge(ev); // ova kurva ga duplira!!! - promenjeno sa 'merge' na 'persist'
//			em.persist(k); // promenjeno sa 'persist' na 'merge' - OVA kurva ga duplira!!!
			em.flush(); // dodato 20:27 11.9.2016.
			em.getTransaction().commit();
			em.close();
			return k.getIdKandidat();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}//
	
	public int updateKandidat3(Kandidat k){
		try {
			EntityManager em = JPAUtils.getEntityManager();
			em.getTransaction().begin();
			em.merge(k); 
			em.flush(); 
			em.getTransaction().commit();
			em.close();
			return k.getIdKandidat();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}//updateKandidat3
	
	public boolean obrisiKandidata(int idKandidat){
		boolean retVal = false;
		EntityManager em = JPAUtils.getEntityManager();
		EntityTransaction et = null;
		try{
			et = em.getTransaction();
			et.begin();
			Kandidat kand = em.find(Kandidat.class, idKandidat); 
			Kandidat ukloniKand = em.merge(kand);
			em.remove(ukloniKand);
			em.flush();
			et.commit();
			em.close();
			retVal=true;
		}catch(Exception e){
			if(et!=null)
				et.rollback();
			e.printStackTrace();
		}
		finally{
			if(em!=null && em.isOpen())
				em.close();
		}
		return retVal;
	}//obrisiKandidata
	
	
	public boolean obrisiKandidata2(int idKandidat){
		try{
			EntityManager em = JPAUtils.getEntityManager();
			em.getTransaction().begin();
			Kandidat kand = em.find(Kandidat.class, idKandidat); 
			Evidencija ev = kand.getEvidencija();
			// ovaj IF je dodat da brise i DUPLIKATE, tj. slogove Kandidata ciji ID slog Evidencija NE sadrzi!!!!
//			if(ev.getKandidat().getIdKandidat() != kand.getIdKandidat()){
//				em.remove(kand);
//			}				
			em.remove(kand);
			em.remove(ev);
//			em.flush(); // Ovo je MOZDA visak...
			em.getTransaction().commit();
			em.close();
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}//obrisiKandidata2
	
	// vraca kandidate upisane u intervalu odDatum-doDatum
	public List<Kandidat> getKandidatsUpisani(Date odDatum, Date doDatum){
		try{
			EntityManager em = JPAUtils.getEntityManager();
			em.getTransaction().begin();
			TypedQuery<Kandidat> upit = 
					em.createQuery("SELECT k FROM Kandidat k WHERE k.evidencija.datumupisa BETWEEN :odDatum AND :doDatum", Kandidat.class);
			upit.setParameter("odDatum", odDatum);
			upit.setParameter("doDatum", doDatum);
			List<Kandidat> kandidats = (List<Kandidat>)upit.getResultList();
			return kandidats;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public List<Kandidat> getKandidatiPaliVoznju(){
		try{
			EntityManager em = JPAUtils.getEntityManager();
			TypedQuery<Kandidat> upit = 
					em.createQuery("SELECT k FROM Kandidat k INNER JOIN k.evidencija ev INNER JOIN ev.polaganjes pol WHERE pol.polozenpi=false", Kandidat.class);
			List<Kandidat> paliKandidati = (List<Kandidat>)upit.getResultList();
			return paliKandidati;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	
	/* Napravi metod da za uneti parametar idKandidat npr. daje SVA njegova polaganja i sortira ih po datumima, tj.
	 * od najranijeg do najkasnijeg sto je ustvari ASC(ending)=RASTUCEM poretku => ovo je vec prikazano u 
	 * DataTable komponetni PrimeFaces-a na stranici 'polaganje.xhtml' */
	
	/* ========================================================================================================= */
	/* ========================================================================================================= */
	
	/* Napravi metod koji vraca Listu Kandidata za uneti idInstruktora i idKategorija u odredjenom vremenskom 
	 * intervalu => probaj PRVO sa objektima Instruktor i Kategorija kao parametrima */
	public List<Kandidat> polaganjaVoznjeInstruktorKategorija(Instruktor inst, Kategorija kat, Date datumOd, Date datumDo){
		try{
			EntityManager em = JPAUtils.getEntityManager();
			int idInst = inst.getIdInstruktor();
			int idKat = kat.getIdKategorija();
			TypedQuery<Kandidat> upit = em.createQuery("SELECT k FROM Kandidat k JOIN k.evidencija.polaganjes pol WHERE ( (pol.datumpolaganjapi BETWEEN :datumOd AND :datumDo) AND (k.instruktor.idInstruktor=:idInst AND k.kategorija.idKategorija=:idKat) )", Kandidat.class);
			upit.setParameter("idInst", idInst);
			upit.setParameter("idKat", idKat);
			upit.setParameter("datumOd", datumOd);
			upit.setParameter("datumDo", datumDo);
			List<Kandidat> listaKandPolagaliInstZaKat = upit.getResultList();
			em.close();
			return listaKandPolagaliInstZaKat;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Kandidat> polaganjaVoznjeInstruktorKategorija2(int idInst, int idKat, Date datumOd, Date datumDo){
		try{
			EntityManager em = JPAUtils.getEntityManager();
			TypedQuery<Kandidat> upit = em.createQuery("SELECT k FROM Kandidat k JOIN k.evidencija.polaganjes pol WHERE ( (pol.datumpolaganjapi BETWEEN :datumOd AND :datumDo) AND (k.instruktor.idInstruktor=:idInst AND k.kategorija.idKategorija=:idKat) )", Kandidat.class);
			upit.setParameter("idInst", idInst);
			upit.setParameter("idKat", idKat);
			upit.setParameter("datumOd", datumOd);
			upit.setParameter("datumDo", datumDo);
			List<Kandidat> listaKandPolagaliInstZaKat = upit.getResultList();
			em.close();
			return listaKandPolagaliInstZaKat;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	
	public List<Kandidat> polozenaVoznjaZaKategoriju(String nazivKategorije, Date datumOd, Date datumDo){
		try{
			EntityManager em = JPAUtils.getEntityManager();
			TypedQuery<Kandidat> upit = em.createQuery("SELECT DISTINCT k FROM Kandidat k JOIN k.evidencija.polaganjes pol WHERE ( (pol.datumpolaganjapi BETWEEN :datumOd AND :datumDo) AND k.kategorija.naziv LIKE :nazivKategorije) ORDER BY k.instruktor.ime", Kandidat.class);
			upit.setParameter("nazivKategorije", nazivKategorije);
			upit.setParameter("datumOd", datumOd);
			upit.setParameter("datumDo", datumDo);
			List<Kandidat> listaKandPolagaliInstZaKat = upit.getResultList();
			em.close();
			return listaKandPolagaliInstZaKat;
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
	
	public List<Kandidat> getKandidatiPoloziliTestove(Date datumOd, Date datumDo){
		try{
			EntityManager em = JPAUtils.getEntityManager();
			TypedQuery<Kandidat> upit = em.createQuery("SELECT k FROM Kandidat k JOIN k.evidencija ev INNER JOIN ev.polaganjes pol WHERE ((pol.datumpolaganjati BETWEEN :datumOd AND :datumDo) AND pol.polozenti=true)", Kandidat.class);
			upit.setParameter("datumOd", datumOd);
			upit.setParameter("datumDo", datumDo);
			List<Kandidat> kandidatiPaliTests = upit.getResultList();
			em.close();
			return kandidatiPaliTests;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	
	public List<Kandidat> getKandidatiPoloziliTestove2(Date datumOd, Date datumDo){
		try{
			EntityManager em = JPAUtils.getEntityManager();
			TypedQuery<Kandidat> upit = em.createQuery("SELECT k FROM Kandidat k JOIN k.evidencija ev INNER JOIN ev.polaganjes pol WHERE ((pol.datumpolaganjati BETWEEN :datumOd AND :datumDo) AND pol.polozenti=true) ORDER BY k.kategorija.naziv, pol.bodoviti desc", Kandidat.class);
			upit.setParameter("datumOd", datumOd);
			upit.setParameter("datumDo", datumDo);
			List<Kandidat> kandidatiPoloziliTests = upit.getResultList();
			em.close();
			return kandidatiPoloziliTests;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	
	public List<Kandidat> getKandidatiPaliTestove(Date datumOd, Date datumDo){
		try{
			EntityManager em = JPAUtils.getEntityManager();
			TypedQuery<Kandidat> upit = em.createQuery("SELECT k FROM Kandidat k JOIN k.evidencija ev INNER JOIN ev.polaganjes pol WHERE ((pol.datumpolaganjati BETWEEN :datumOd AND :datumDo) AND pol.polozenti=false)", Kandidat.class);
			upit.setParameter("datumOd", datumOd);
			upit.setParameter("datumDo", datumDo);
			List<Kandidat> kandidatiPaliTests = upit.getResultList();
			em.close();
			return kandidatiPaliTests;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	/* Vraca listu Kandidata koji su upisani u intervalu za prosledjene datume */
	public List<Kandidat> getKandidatiUpisaniZaDatum(Date datumOd, Date datumDo){
		try{
			EntityManager em  = JPAUtils.getEntityManager();
			TypedQuery<Kandidat> upit = em.createQuery("SELECT DISTINCT k FROM Kandidat k INNER JOIN k.evidencija ev WHERE ev.datumupisa BETWEEN :datumOd AND :datumDo ORDER BY k.instruktor.ime,k.kategorija.naziv", Kandidat.class);
			upit.setParameter("datumOd", datumOd);
			upit.setParameter("datumDo",datumDo);
			List<Kandidat> listaUpisanihKandidata = upit.getResultList();
			em.close();
			return listaUpisanihKandidata;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	public static void main(String[] args) {
		
//		KandidatManager km = new KandidatManager();
//		Date danasnjiDatum = new Date();
//		Calendar c = new GregorianCalendar();
//		c.add(Calendar., 30);
//		Date datumZaPrijavuTestova=c.getTime();
//		System.out.println("Danasnji datum je: "+danasnjiDatum+", a datum za prijavu testova je "
//				+datumZaPrijavuTestova);
//		PolaganjeManager polm = new PolaganjeManager();
//		List<Kandidat> listaKandInst = km.getSviKandidatiInstruktora(3);
//		for(Kandidat k:listaKandInst){
//			System.out.println("Kandidat "+k.getIme()+" "+k.getPrezime()+" obuèava se za kategoriju "
//					+k.getKategorija().getNaziv());
//		}
//		List<Kandidat> listaKandidataInstruktora =  km.getSviKandidatiInstruktora2(1, "", "", "");
//		for(Kandidat k:listaKandidataInstruktora){
//			System.out.println("Kandidat "+k.getIme()+" "+k.getPrezime());
//		}
//		for(Kandidat k:listaKandidataInstruktora){
//			System.out.println("Kandidat "+k.getIme()+" "+k.getPrezime());
//		}
//		Kandidat k = km.getKandidatByUserAndPass("Puškariæ", "puskara");
//		System.out.println("Kandidat "+k.getIme()+" "+k.getPrezime()+", id:"+k.getIdKandidat());
//		for(Kandidat k: kands){
//			System.out.println(k.getIme()+" "+k.getPrezime()+", JMBG:"+k.getJmbg());
//		}
////		km.obrisiKandidata(8);
//		try{
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//			Date dateOd = sdf.parse("2015-01-05");
//			Date dateDo = sdf.parse("2017-10-10");
//			List<Polaganje> listaPolozenihTestova = km.getPolaganjaTestovaZaDatum(dateOd,dateDo);
//			for(Polaganje p:listaPolozenihTestova){
//				System.out.println("Instruktor: "+p.getEvidencija().getKandidat().getInstruktor().toString()+", kandidat "+p.getEvidencija().getKandidat().getIme()+" "
//			        +p.getEvidencija().getKandidat().getPrezime()+" je polozio testove za kategoriju "+p.getEvidencija().getKandidat().getKategorija().getNaziv()+" sa "+p.getBodoviti()+" bodova.");
//			}
//			List<Kandidat> listaKandidataUpisanihDatum = km.getKandidatiUpisaniZaDatum(dateOd, dateDo);
//			List<Kandidat> listaKandPolTestove = km.getKandidatiPoloziliTestove(dateOd,dateDo);
//			List<Kandidat> listaKandPolTestove2 = km.getKandidatiPoloziliTestove2(dateOd,dateDo);
//			for(Kandidat k:listaKandPolTestove){
//				System.out.println("Kandidat "+k.getIme()+" "+k.getPrezime()+" instruktora "
//			         +k.getInstruktor().getIme()+" "+k.getInstruktor().getPrezime()+" za kategoriju "+k.getKategorija().getNaziv());
//			}
//			
//			System.out.println("Duzina liste je: "+listaKandidataUpisanihDatum.size());
//			for(Kandidat k:listaKandidataUpisanihDatum){
//				System.out.println("Kandidat "+k.getIme()+" "+k.getPrezime()+" upisan "
//						+k.getEvidencija().getDatumupisa()+" za kategoriju "+k.getKategorija().getNaziv()+
//						" kod instruktora "+k.getInstruktor().toString());
//			}
//			Instruktor i = new InstruktorManager().getInstruktorForId(1);
//			Kategorija kat = new KategorijaManager().getKategorijaForId2(2);
//			List<Kandidat> listaKandidataPoloziliVoznju = km.polaganjaVoznjeZaKategoriju("B",dateOd,dateDo);
//			for(Kandidat k:listaKandidataPoloziliVoznju){
//				System.out.println("Kandidat "+k.getIme()+" "+k.getPrezime()+" polagao za kategoriju "+k.getKategorija().getNaziv()+
//						" kod instruktora "+k.getInstruktor().getIme()+" "+k.getInstruktor().getPrezime());
//			}
//			List<Kandidat> kandidatiPolagaliKodInstZaKat = km.polaganjaVoznjeInstruktorKategorija(i,kat,dateOd,dateDo);
//			List<Kandidat> kandidatiPolagaliKodInstZaKat2 = km.polaganjaVoznjeInstruktorKategorija2(1,2,dateOd,dateDo);
//			List<Kandidat> kandidatiPolagaliZaKat = km.polaganjaVoznjeZaKategoriju(kat,dateOd,dateDo);
//			if(kandidatiPolagaliKodInstZaKat==null)
//				System.out.println("Lista je prazna!");
//			for(Kandidat k:kandidatiPolagaliZaKat){
//				System.out.println("Kandidat "+k.getIme()+" "+k.getPrezime()+" polagao za kategoriju "+k.getKategorija().getNaziv()+
//						" kod instruktora "+k.getInstruktor().getIme()+" "+k.getInstruktor().getPrezime());
//			}
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//			Date datumOd = sdf.parse("2016-05-14");
//			Date datumDo = sdf.parse("2016-09-24");
//			List<Kandidat> kands = km.getKandidatiPoloziliTestove(datumOd, datumDo);
//			for(Kandidat k:kands){
//				System.out.println("Kandidat "+k.getIme()+" "+k.getPrezime()+" je osvojio");
//			}
//			EntityManager em = JPAUtils.getEntityManager();
//			em.getTransaction().begin();
//			InstruktorManager im = new InstruktorManager();
//			Instruktor i = im.getInstruktorForId(6);
//			KategorijaManager katm = new KategorijaManager();
//			Kategorija kat = katm.getKategorijaForId(2);
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//			Date datumR = sdf.parse("1994-01-14");
//			Date datumIzdLU = sdf.parse("2016-08-24");			
//			// linija ispod se nece moci izvrsiti, jer objekat 'k' JOS
//			// nije kreiran!
//			/* Evidencija ev = new EvidencijaManager().saveEvidencija(k, "LU3519946B", datumIzdLU, new Date());*/
//			Evidencija ev = new Evidencija();
//			ev.setBrLekarUverenja("LU7599943B");
//			ev.setDatumIzdavanjaLu(datumIzdLU);
//			ev.setDatumupisa(new Date());
//
//			Kandidat k = km.saveKandidat2("Aleksandar", "Simeunic","1401940007641", "009558584", datumR, 
//					"aleksa94@gmail.com", "alJ3K!", "Novi Sad", "Bulevar Kralja Petra 1", "42c", "069756731", i, kat, ev);
//			ev.setKandidat(k);
//			em.merge(ev);
//			em.getTransaction().commit();
//			em.close();
//			/* ovaj CEO kod iznad naveden u try bloku je SAMO za pohranjivanje 
//			 * NOVOG kandidata u bazu i kreiranje njemu jedinstvene evidencije */
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//			Date odDatum = sdf.parse("2016-08-27");
//			Date doDatum = sdf.parse("2016-08-28");	
//			List<Kandidat> kands = km.getKandidatsUpisani(odDatum, doDatum);
//			for(Kandidat k:kands){
//				System.out.println("Kandidat upisan izmedju "+odDatum+" i "+doDatum+" :");
//			}
//			List<Kandidat> kands2 = km.getKandidatiPaliVoznju();
//			for(Kandidat k2:kands2){
//				System.out.println("Kandidat koji je pao voznju: "+k2.getIme()+" "+k2.getPrezime());
//			}
//		}catch(Exception e){
//			e.printStackTrace();
//		}
	}//main
}