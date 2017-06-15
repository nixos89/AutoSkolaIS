package managers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import model.Evidencija;
import model.Prijava;

public class PrijavaManager {
	
	public Prijava savePrijava(Evidencija ev,Date datumPTI,Date datumPPI ){
		try{
			EntityManager em = JPAUtils.getEntityManager();
			em.getTransaction().begin();
			Prijava p = new Prijava();
			p.setEvidencija(ev);
			p.setDatumprijavepispita(datumPTI);
			p.setDatumprijavetispita(datumPPI);
			em.persist(p);
			em.getTransaction().commit();
			em.close();
			return p;
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public Prijava promeniDatumPPrakIspita(int idPrijava,Date datumPPI){
		try{
			EntityManager em = JPAUtils.getEntityManager();
			em.getTransaction().begin();
			Prijava pr = em.find(Prijava.class, idPrijava);
			pr.setDatumprijavepispita(datumPPI);
			em.merge(pr);
			em.flush();
			em.getTransaction().commit();
			return pr;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Prijava> getPrijaveKategorijeZaDatum(String nazivKat,Date datumOd, Date datumDo){
		try{
			EntityManager em = JPAUtils.getEntityManager();
			TypedQuery<Prijava> upit = em.createQuery("SELECT pr FROM Prijava pr INNER JOIN pr.evidencija ev INNER JOIN ev.kandidat k WHERE ( (pr.datumprijavetispita BETWEEN :datumOd AND :datumDo) AND k.kategorija.naziv LIKE :nazivKat ) ORDER BY pr.datumprijavetispita asc", Prijava.class);
			upit.setParameter("nazivKat",nazivKat);
			upit.setParameter("datumOd",datumOd);
			upit.setParameter("datumDo",datumDo);
			List<Prijava> listaPrijavaKandZaKatZaDatum = upit.getResultList();
			em.close();
			return listaPrijavaKandZaKatZaDatum;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	} 
	
	
	/* vraca listu Prijava onih kandidata koji su se prijavili za polaganje prakticnog ispita, tj. voznje
	 * i sortira ih po datumima od najranijeg ka najkasnijem datumu */
	public List<Prijava> getPrijaveVoznjeKategorijeZaDatum(String nazivKat,Date datumOd, Date datumDo){
		try{
			EntityManager em = JPAUtils.getEntityManager();
			TypedQuery<Prijava> upit = em.createQuery("SELECT pr FROM Prijava pr INNER JOIN pr.evidencija ev "
					+ "INNER JOIN ev.kandidat k WHERE ( (pr.datumprijavepispita BETWEEN :datumOd AND :datumDo) "
					+ "AND k.kategorija.naziv LIKE :nazivKat ) ORDER BY pr.datumprijavepispita asc", Prijava.class);
			upit.setParameter("nazivKat",nazivKat);
			upit.setParameter("datumOd",datumOd);
			upit.setParameter("datumDo",datumDo);
			List<Prijava> listaPrijavaVoznjeKandZaKatZaDatum = upit.getResultList();
			em.close();
			return listaPrijavaVoznjeKandZaKatZaDatum;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	} 
	
	public static void main(String[] args) {
//		PrijavaManager pm = new PrijavaManager();
//		try{
//			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
//			Date datumPTI = sdf.parse("2016-05-13");
//			Date datumPPI = sdf.parse("2016-06-07");
//			Date datumOd = sdf.parse("01-12-2015");
//			Date datumDo = sdf.parse("23-09-2016");
//			List<Prijava> listaPrijavaZaKatDatum = pm.getPrijaveKategorijeZaDatum("B", datumOd, datumDo);
//			List<Prijava> listaPrijavaVoznjeZaKatDatum = pm.getPrijaveVoznjeKategorijeZaDatum("B", datumOd, datumDo);
//			for(Prijava p:listaPrijavaVoznjeZaKatDatum){
//				System.out.println("Kandidat "+p.getEvidencija().getKandidat().getIme()+" "
//						+p.getEvidencija().getKandidat().getPrezime()+" je prijavio ispit datuma: "+p.getDatumprijavepispita());
//			}
//			Evidencija ev = new EvidencijaManager().getEvidencijaForId(7);
//			Prijava p = pm.savePrijava(ev, datumPTI, datumPPI);
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//			Date datumPPI2=sdf.parse("2016-07-02");
//			Prijava p2 = pm.promeniDatumPPrakIspita(1, datumPPI2);
//		}
//		catch(Exception e){
//			e.printStackTrace();
//		}
	}

}
