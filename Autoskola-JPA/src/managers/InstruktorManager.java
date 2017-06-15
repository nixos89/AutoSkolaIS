package managers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import customBeans.InstruktorBrojKandidataBean;
import model.Instruktor;
import model.Kandidat;
import model.Kategorija;
import model.Vozilo;

public class InstruktorManager {
	
	public Instruktor saveInstruktor2(String ime,String prezime,String brLK,Date datumR,String email,String jmbg,
			String lozinka,String telefon,String mesto,String ulica,String broj){
		try{
			EntityManager em = JPAUtils.getEntityManager();
			em.getTransaction().begin();
			Instruktor i = new Instruktor();
			i.setIme(ime);
			i.setPrezime(prezime);
			i.setBrLicnekarte(brLK);
			i.setDatumrodjenja(datumR);
			i.setEmail(email);
			i.setJmbg(jmbg);
			i.setLozinka(lozinka);
			i.setTelefon(telefon);
			i.setMesto(mesto);
			i.setUlica(ulica);
			i.setBroj(broj);
			em.persist(i);
			em.getTransaction().commit();
			em.close();
			System.out.println("sacuvan je");
			return i;			
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("nije sacuvan");
			return null;
		}
	}//saveInstruktor2
	
	public int saveInstruktor(Instruktor i){
		try{
			EntityManager em = JPAUtils.getEntityManager();
			em.getTransaction().begin();
			// ovo izmeni, jer ovde ucitava postojece kategorije 
			// i dodeljuje mu nove, umesto da mu dodeli SVE NOVE!
			for(Kategorija kat:i.getKategorijas()){
				if(!kat.getInstruktors().contains(i)){
					kat.getInstruktors().add(i);
				}
			}
			em.merge(i);
			em.getTransaction().commit();
			em.close();
			return i.getIdInstruktor();			
		}catch(Exception e){
			e.printStackTrace();
			return 0;
		}
	}//saveInstruktor
	
	public int vratiIdInstruktora(Instruktor inst){
		return inst.getIdInstruktor();
	}
	
	
	// metoda za proveru instruktora
	public Instruktor getInstruktorByUserAndPass(String user, String pass){
		EntityManager em = JPAUtils.getEntityManager();
		Query upit = em.createQuery("SELECT i FROM Instruktor i WHERE i.prezime LIKE :user AND i.lozinka LIKE :pass");
		upit.setParameter("user", user);
		upit.setParameter("pass", pass);
		Instruktor inst = (Instruktor) upit.getSingleResult();
		em.close();
		return inst;
	}
	
	public int updateInstruktor(Instruktor i){
		try{
			EntityManager em = JPAUtils.getEntityManager();
			em.getTransaction().begin();
			System.out.println("Pre merge");
		
			for(Kategorija kat:i.getKategorijas()){
				if(!kat.getInstruktors().contains(i))
					kat.getInstruktors().add(i);
			}
			em.merge(i);
			em.flush();
			System.out.println("Posle merge");
			em.getTransaction().commit();
			em.close();
			return i.getIdInstruktor();			
		}catch(Exception e){
			e.printStackTrace();
			return 0;
		}
	}//updateInstruktor
	
	public int updateInstruktor2(Instruktor i){
		try{
			EntityManager em = JPAUtils.getEntityManager();
			em.getTransaction().begin();
			System.out.println("Pre merge");
		
			for(Vozilo voz:i.getVozilos()){
				if(!voz.getInstruktors().contains(i)){
					voz.getInstruktors().add(i);
				}	
			}
			em.merge(i);
			em.flush();
			System.out.println("Posle merge");
			em.getTransaction().commit();
			em.close();
			return i.getIdInstruktor();			
		}catch(Exception e){
			e.printStackTrace();
			return 0;
		}
	}//updateInstruktor
	
	
	public Instruktor getInstruktorForId(int id){
		try {
			EntityManager em = JPAUtils.getEntityManager();
			em.getTransaction().begin();
			Instruktor i = em.find(Instruktor.class, id);
			em.getTransaction().commit();
			em.close();
			return i;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public Instruktor getInstruktorByPrezime(String prezime){
		try {
			EntityManager em = JPAUtils.getEntityManager();
			Query q  = em.createQuery("SELECT i FROM Instruktor i WHERE i.prezime LIKE :prezime");
			q.setParameter("prezime", prezime);
			Instruktor inst = (Instruktor)q.getSingleResult();
			em.close();
			return inst;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}//getInstruktorByPrezime

	public List<Instruktor> getSviInstruktori(){
		EntityManager em = JPAUtils.getEntityManager();
		TypedQuery<Instruktor> upit = em.createQuery("SELECT i FROM Instruktor i", Instruktor.class);
		List<Instruktor> sviInstruktori = upit.getResultList();
		em.close();
		return sviInstruktori;
	}
	
	
	public List<Kategorija>getSveKategorije(EntityManager em){
		TypedQuery<Kategorija> query = em.createQuery("select k from Kategorija k",Kategorija.class);
		List<Kategorija> result = query.getResultList();
		em.close();
		return result;
	}
	
	public List<Kategorija>getSveKategorijeNaziv(EntityManager em){
		TypedQuery<Kategorija> query = em.createQuery("select k.naziv from Kategorija k",Kategorija.class);
		List<Kategorija> result = query.getResultList();
		em.close();
		return result;
	}
	
	public List<Kategorija> getKatsZaInstruktora2(Instruktor i){
		try {
			EntityManager em = JPAUtils.getEntityManager();
			List<Kategorija> katZaInst = i.getKategorijas();
			em.close();
			return katZaInst;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	// BAD SMELL!!!
	public List<Kategorija> getKatsZaInstruktora3(Instruktor i){
		EntityManager em = JPAUtils.getEntityManager();
		TypedQuery<Kategorija> query = em.createQuery("select k from Kategorija k",Kategorija.class);
		List<Kategorija> sveKategorije = query.getResultList();
		List<Kategorija> katZaInst = i.getKategorijas();
		katZaInst.addAll(sveKategorije);
		em.close();
		return katZaInst;
	}
	
	// vraca SVE kategorije u vidu liste koje su dodeljene jednom instruktoru
	public List<Kategorija> getKatsZaInstruktora(int idInst){
		EntityManager em = JPAUtils.getEntityManager();
		Instruktor inst = em.find(Instruktor.class, idInst);
		List<Kategorija> katZaInst = inst.getKategorijas();
		em.close();
		return katZaInst;
	}
	
	// vraca sve Instruktore kojima je dodeljena prosledjena (u formalnom parametru) kategorija 
	public List<Instruktor> getInstruktorsByKategorija(Kategorija kat){
		EntityManager em = JPAUtils.getEntityManager();
		List<Instruktor> instZaKat = kat.getInstruktors();
		em.close();
		return instZaKat;
	}
	
	public List<Instruktor> getInstsZaKategoriju(int idKat){
		EntityManager em = JPAUtils.getEntityManager();
		Kategorija kat = em.find(Kategorija.class, idKat);
		List<Instruktor> instsZaKat = kat.getInstruktors();
		em.close();
		return instsZaKat;
	}
	
	public List<String> getInstsZaKategoriju2(int idKat){
		EntityManager em = JPAUtils.getEntityManager();
		Kategorija kat = em.find(Kategorija.class, idKat);
		List<Instruktor> instsZaKat = kat.getInstruktors();
		List<String> punoImeInstsZaKat = new ArrayList<String>();
		for(Instruktor i:instsZaKat){
			punoImeInstsZaKat.add(i.getIme()+" "+i.getPrezime());
		}
		em.close();
		return punoImeInstsZaKat;
	}
	
	public List<Vozilo>getSvaVozila(EntityManager em){
		TypedQuery<Vozilo> upit = em.createQuery("SELECT v FROM Vozilo v",Vozilo.class);
		List<Vozilo> vozila = upit.getResultList();
		em.close();
		return vozila;
	}
	
	
	public static Vozilo getVoziloForId(int id){
		try {
			EntityManager em = JPAUtils.getEntityManager();
			em.getTransaction().begin();
			Vozilo v = em.find(Vozilo.class, id);
			em.getTransaction().commit();
			em.close();
			return v;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	// vraca SVE instruktore koji vrse obuku nad ISTIM vozilom - DOVRSI!!!
	public List<Instruktor> getInstruktorsByVozilo(Vozilo v){
		try {
			EntityManager em = JPAUtils.getEntityManager();
			List<Instruktor> instruktori = v.getInstruktors();
			em.close();
			return instruktori;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	// pronalazi instruktore po JMBG-u, imenu i prezimenu
		public List<Instruktor> getInstruktorsByImePrezimeJmbg(String ime, String prezime,String jmbg){
			try {
				EntityManager em = JPAUtils.getEntityManager();
				TypedQuery<Instruktor> q  = em.createQuery("select i from Instruktor i where i.ime like :ime and i.prezime like :prezime and i.jmbg like :jmbg", Instruktor.class);
				q.setParameter("ime", "%"+ime+"%");
				q.setParameter("prezime", "%"+prezime+"%");
				q.setParameter("jmbg", "%"+jmbg+"%");
				List<Instruktor> insts = q.getResultList();
				em.close();
				return insts;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
	
	//pronalazi instruktora po JMBG-u
	public Instruktor getInstruktorByJMBG(String jmbg){
		try {
			EntityManager em = JPAUtils.getEntityManager();
			Query q  = em.createQuery("SELECT i FROM Instruktor i WHERE i.jmbg like :nekiJmbg");
			q.setParameter("nekiJmbg", jmbg);
			Instruktor inst = (Instruktor)q.getSingleResult();
			em.close();
			return inst;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	// pronalazi instruktore po JMBG-u
	public List<Instruktor> getInstruktorsByJMBG(String jmbg){
		try {
			EntityManager em = JPAUtils.getEntityManager();
			TypedQuery<Instruktor> q  = em.createQuery("select i from Instruktor i where i.jmbg like :jmbg", Instruktor.class);
			q.setParameter("jmbg", "%"+jmbg+"%");
			List<Instruktor> insts = q.getResultList();
			em.close();
			return insts;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	// pronalazi instruktora po broju licne karte 
	public Instruktor getInstruktorByBrLK(String brLK){
		EntityManager em = JPAUtils.getEntityManager();
		Query q  = em.createQuery("SELECT i FROM Instruktor i WHERE i.brLicnekarte = :brLK");
		q.setParameter("brLK", brLK);
		Instruktor i = (Instruktor)q.getSingleResult();
		em.close();
		return i;
	}
	
	public void promeniBrLK(String brLK, int idInstruktor){
		try {
			EntityManager em = JPAUtils.getEntityManager();
			em.getTransaction().begin();
			Instruktor i = em.find(Instruktor.class, idInstruktor);
			Query q = 
					em.createQuery("UPDATE Instruktor inst SET inst.brLicnekarte= :brLK WHERE inst.idInstruktor = :idInstruktor");
			q.setParameter("brLK", brLK);
			q.setParameter("idInstruktor", idInstruktor);
			int update = q.executeUpdate(); // MORA ici ova naredba!
			if(update>0)
				System.out.println("Izveseno AZURIRANJE!");
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}//promeniBrLK
	
	// moze i OVAJ - NAJBOLJI, tj. BOLJI od prethodnog!!!! =D - URADI to i za OSTALA polja
	public Instruktor promeniBrLK2(String brLK,int id){
			try {
				EntityManager em = JPAUtils.getEntityManager();
				em.getTransaction().begin();
				Instruktor i = em.find(Instruktor.class, id);
				i.setBrLicnekarte(brLK);
				em.merge(i);
				em.getTransaction().commit();
				em.close();
				return i;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}//promeniBrLK2
	
	public List<String> instruktorsByKatNaziv(String nazivKat){
		EntityManager em = JPAUtils.getEntityManager();
		Query upit=em.createQuery("SELECT k FROM Kategorija k WHERE k.naziv=:nazivKat");
		upit.setParameter("nazivKat", nazivKat);
		Kategorija kat = (Kategorija)upit.getSingleResult();
		List<Instruktor> instsZaKat = kat.getInstruktors();
		List<String> punoImeInstsZaKat = new ArrayList<String>();
		for(Instruktor i:instsZaKat){
			punoImeInstsZaKat.add(i.toString());
		}
		return punoImeInstsZaKat;
	}
	
	public List<String> instruktorsByKatNaziv2(String nazivKat){
		EntityManager em = JPAUtils.getEntityManager();
		TypedQuery<Instruktor> upit = em.createQuery("SELECT DISTINCT i FROM Instruktor i JOIN FETCH i.kategorijas k WHERE k.naziv LIKE :nazivKat)",Instruktor.class);
		upit.setParameter("nazivKat", nazivKat);
		List<Instruktor> instsZaKat = upit.getResultList();
		List<String> punoImeInstsZaKat = new ArrayList<String>();
		for(Instruktor i:instsZaKat){
			punoImeInstsZaKat.add(i.getPrezime());
		}
		return punoImeInstsZaKat;
	}
	
	
	public List<Kandidat> getKandidatsByInstruktor(Instruktor i){
		return i.getKandidats();
	}
	
	
	// brise instruktor iz baze za uneti ID - RADI!!!
	public boolean deleteInstruktor(Instruktor i){
		EntityManager em = JPAUtils.getEntityManager();
		EntityTransaction et = null;
		boolean retVal = false;
		try {
			et = em.getTransaction();
			et.begin();
			Instruktor ukloniOvog = em.merge(i);
			em.remove(ukloniOvog);
//			em.remove(i);
			em.flush();
			et.commit();
			em.close();
			retVal=true;
		}catch (Exception e) {
			e.printStackTrace();
			if(et!=null){
				et.rollback();
			}
		}finally{
			if(em!=null && em.isOpen()){
				em.close();
			}	
		}
		return retVal;
	}// TODO deleteInstruktor
	
	public boolean obrisiInstruktora(int idInstruktora){
		try {
			EntityManager em = JPAUtils.getEntityManager();
			em.getTransaction().begin();
			Instruktor i = em.find(Instruktor.class, idInstruktora);
			em.remove(i);
			em.getTransaction().commit();
			em.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	// uklanja SVE kategorije ODJEDNOM i TEK onda BRISE instruktora
	public boolean obrisiInstruktora2(int idInstruktora,List<Kategorija> kats){
		try {
			EntityManager em = JPAUtils.getEntityManager();
			em.getTransaction().begin();
			Instruktor i = em.find(Instruktor.class, idInstruktora);
			if(i.getKategorijas().size()>0){
				i.getKategorijas().removeAll(kats);
			}
			em.remove(i);
			em.getTransaction().commit();
			em.close();
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	// prolazi kroz jednu po jednu kategoriju, proverava da li ona sadrzi odredjenog instruktora
	// i ako ga sadrzi onda ga uklanja!
	public boolean obrisiInstruktora3(int idInstruktora){
		try {
			EntityManager em = JPAUtils.getEntityManager();
			em.getTransaction().begin();
			Instruktor i = em.find(Instruktor.class, idInstruktora);
			for(Kategorija kat:i.getKategorijas()){
				if(kat.getInstruktors().contains(i)){
					kat.getInstruktors().remove(i);
				}
			}
			for(Vozilo v:i.getVozilos()){
				if(v.getInstruktors().contains(i)){
					v.getInstruktors().remove(i);
				}
			}
			em.remove(i);
			em.getTransaction().commit();
			em.close();
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	public List<Vozilo> getVozilosByInstruktor(int idInst){
		EntityManager em = JPAUtils.getEntityManager();
		Instruktor i = em.find(Instruktor.class, idInst);
		List<Vozilo> listaVozilaInstruktora = i.getVozilos();
		return listaVozilaInstruktora;
	}
	
	public void poveziInstKat(Instruktor i, Kategorija k){
		EntityManager em = JPAUtils.getEntityManager();
		em.getTransaction().begin();
		if(i.getKategorijas()==null){
			i.setKategorijas(new ArrayList<Kategorija>());
		}
		if(k.getInstruktors()==null){
			k.setInstruktors(new ArrayList<Instruktor>());
		}
		i.getKategorijas().add(k);
		k.getInstruktors().add(i);
		em.merge(i);
		em.merge(k);
		em.getTransaction().commit();
		em.close();
	}
	
	public void poveziInstVoz(Instruktor i, Vozilo v){
		EntityManager em = JPAUtils.getEntityManager();
		em.getTransaction().begin();
		if(i.getVozilos()==null){
			i.setVozilos(new ArrayList<Vozilo>());
		}
		if(v.getInstruktors()==null){
			v.setInstruktors(new ArrayList<Instruktor>());
		}
		i.getVozilos().add(v);
		v.getInstruktors().add(i);
		em.merge(i);
		em.merge(v);
		em.getTransaction().commit();
		em.close();
	}
	
	// implementiraj ovo i kad se BRISE kandidat da se 
	// ukloni iz liste instruktora koji ga obucava!!! => NECE raditi, jer tim Kandidatima se PRVO MORA
	// dodeliti drugi instruktor
	public boolean removeKandidatsFromInstruktor(int idInstruktor){
		try {
			EntityManager em = JPAUtils.getEntityManager();
			em.getTransaction().begin();
			Instruktor i = em.find(Instruktor.class, idInstruktor);
//			List<Kandidat> listaKand = i.getKandidats();
//			i.removeKandidat(kandidat)
			List<Kandidat> listaKand = i.getKandidats();
			System.out.println("Ucitana lista kandidata...");
			for(Kandidat k: listaKand){
				i.removeKandidat(k);
				System.out.println("Kandidat je odstranjen...");
			}
			em.merge(i);
			em.getTransaction().commit();
			em.close();
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	/* Izveštaj koji generiše instruktore sa najviše kandidata za unetu kategoriju za odreðene datume */
	public List<InstruktorBrojKandidataBean> getInstruktoriSaMaxKandidataZaKat(String nazivKat, Date datumOd, Date datumDo){
		EntityManager em = JPAUtils.getEntityManager();
		Query upit = em.createQuery("SELECT i,count(kand) FROM Instruktor i JOIN FETCH i.kandidats kand WHERE ( (kand.kategorija.naziv LIKE :nazivKat) AND (kand.evidencija.datumupisa BETWEEN :datumOd AND :datumDo) ) GROUP BY i ORDER BY COUNT (kand) desc");
		upit.setParameter("nazivKat", nazivKat);
		upit.setParameter("datumOd", datumOd);
		upit.setParameter("datumDo", datumDo);
		List<Object[]> rez = upit.getResultList();
		List<InstruktorBrojKandidataBean> instruktoriSaMaxKandidata = new ArrayList<InstruktorBrojKandidataBean>();
		for(Object[] item:rez){
			int br = Math.toIntExact((Long)item[1]);
			InstruktorBrojKandidataBean ibkb = new InstruktorBrojKandidataBean((Instruktor)item[0],br);
			instruktoriSaMaxKandidata.add(ibkb);
		}
		em.close();
		return instruktoriSaMaxKandidata;
	}
	
	public static void main(String[] args) {
//		InstruktorManager im = new InstruktorManager();
//		Instruktor i = im.getInstruktorByPrezime("Stevanovic");
//		System.out.println("Instruktor "+i.getIme()+" "+i.getPrezime());
//		Instruktor i = im.getInstruktorByUserAndPass("Vukelja", "ljubiboj");
//		System.out.println("Instruktor "+i.getIme()+" "+i.getPrezime());
//		Instruktor i = im.getInstruktorForId(2);
//		List<Vozilo> vozilaInst = im.getVozilosByInstruktor(2);
//		System.out.println("Vozila instruktora "+i.getIme()+" "+i.getPrezime()+" su:");
//		for(Vozilo v:vozilaInst){
//			System.out.println("Vozilo marke: "+v.getMarka()+", model: "+v.getModel()+", gorivo tipa: "+v.getGorivo());
//		}
////		boolean ok = im.removeKandidatsFromInstruktor(i.getIdInstruktor());
//		if(ok){
//			System.out.println("Kandidati su uspesno odstranjeni");
//		}else{
//			System.out.println("Greska, kandidati NISU odstranjeni!");
//		}
//		List<Kandidat> listaKandidataInstruktora = im.getKandidatsByInstruktor(i);
//		System.out.println("Instruktor "+i.getIme()+" "+i.getPrezime()+" ima kandidate:");
//		for(Kandidat k: listaKandidataInstruktora){
//			System.out.println(k.getIme()+" "+k.getPrezime());
//		}
		
		
//		List<String> punaImenaInst = im.instruktorsByKatNaziv("B");
//		for(String punoIme:punaImenaInst){
//		System.out.println(punoIme);
//	}
//		Kategorija kat = new KategorijaManager().getKategorijaForId(2);
//		
//		List<Instruktor> instruktoriKategorizovani =  im.getInstsZaKategoriju(2);
//		System.out.println("Instruktori za kategoriju "+kat.getNaziv()+" :");
//		for(Instruktor i:instruktoriKategorizovani){
//			System.out.println("Instruktor "+i.getIme()+" "+i.getPrezime());
//		}
//		Instruktor inst = im.getInstruktorForId(10);
//		List<Kategorija> kategorijeInstruktora = im.getKatsZaInstruktora3(inst);
//		System.out.println("Kategorije instruktora "+inst.getIme()+" "+inst.getPrezime()+" su:");
//		for(Kategorija kat:kategorijeInstruktora){
//			System.out.println("Kategorija: "+kat.getNaziv());
//		}
//		Instruktor i = im.getInstruktorForId(7);
//		List<Kategorija> kategorije = im.getKatsZaInstruktora2(i);
//		for(Kategorija k:kategorije){
//			System.out.println("Kategorija: "+k.getNaziv());
//		}
//		Kategorija k = new KategorijaManager().getKategorijaForId(3);
//		im.poveziInstKat(i, k);
//		boolean ok = im.obrisiInstruktora(17);
//		if(ok){
//			System.out.println("Instruktor je obrisan!");
//		}else{
//			System.out.println("Instruktor NIJE obrisan!");
//		}
//		Instruktor i3 = im.getInstruktorByJMBG("1208620004321");
//		Instruktor i4 = im.getInstruktorForId(7);
//		System.out.println("Instruktor "+i3.getIme()+" "+i3.getPrezime()+" ima JMBG: "+i3.getJmbg());
//		List<Kategorija> katForInst = im.getKatsZaInstruktora(7);
//		System.out.println("Kategorije instruktora "+i4.getIme()+" "+i4.getPrezime()+": ");	
//		for(Kategorija kat:katForInst){
//			System.out.println("Kategorija "+kat.getNaziv());
//		}
//		List<Instruktor> instruktors = im.getInstruktorsByJMBG("1208620004321");
//		int duzina = instruktors.size();
//		System.out.println("Lista je duzine "+duzina);
//		for(Instruktor i:instruktors){
//			System.out.println("Instruktor "+i.getIme()+" "+i.getPrezime()+" ima JMBG: "+i.getJmbg());
//		}
//		Instruktor i2 = instruktors.get(0);
//		System.out.println("Instruktor "+i2.getIme()+" "+i2.getPrezime()+" ima JMBG: "+i2.getJmbg());
//		Instruktor i = im.getInstruktorByJMBG("1208620004321");
//		System.out.println("Instruktor "+i.getIme()+" "+i.getPrezime());
//		Date datumR=null;
//		try {
//			InstruktorManager im = new InstruktorManager();
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//			datumR = sdf.parse("1979-07-28");
//			Instruktor i = im.saveInstruktor("Dobrica", "Milovanovic", "006614796", datumR, "dobrimilo@gmail.com", "2807790007356", 
//					"vrem3S", "068173651", "Novi Sad", "Bulevar Oslobodjenja", "80a");
//			System.out.println("Sacuvan je instruktor "+i.getIme()+" "+i.getPrezime()+" pod brojem id-ja:"+i.getIdInstruktor()+".");
//			Instruktor i2 = new InstruktorManager().promeniBrLK("001514792", "001514791");
//			im.promeniBrLK("001514792", "001514791");
//			Instruktor i3 = new InstruktorManager().getInstruktorByBrLK("001514792");
//			System.out.println("Instruktor "+i3.getIme()+" "+i3.getPrezime()+" sa brojem licne karte: "+i3.getBrLicnekarte()+".");
//			Instruktor i4 = im.getInstruktorForId(6);
//			System.out.println("Instruktor "+i4.getIme()+" "+i4.getPrezime()+" sa brojem licne karte: "+i4.getBrLicnekarte()+".");
//			Instruktor i5 = im.promeniBrLK2("000407791", 5);
//			System.out.println("Instruktor "+i5.getIme()+" "+i5.getPrezime()+" sa brojem licne karte: "+i5.getBrLicnekarte()+".");
//			im.promeniBrLK("1515157",5); 
//			im.promeniBrLK2("1515157",5); 
//			im.deleteInstruktor(9); // - RADI!!! :)
//			Instruktor i6 = im.getInstruktorByBrLK("001514792");
//			System.out.println("Instruktor "+i6.getIme()+" "+i6.getPrezime()+
//					" sa brojem licne karte: "+i6.getBrLicnekarte()+".");						
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}//main
}