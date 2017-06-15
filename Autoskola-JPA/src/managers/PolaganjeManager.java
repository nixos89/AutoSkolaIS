package managers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import model.Evidencija;
import model.Polaganje;

public class PolaganjeManager {

	public Polaganje savePolaganje(int bodoviti,
			int bodovipi,Date datumPTI,Date datumPPI,
			boolean polozenpi,boolean polozenti,Evidencija ev){
		try{
			EntityManager em = JPAUtils.getEntityManager();
			em.getTransaction().begin();
			Polaganje p = new Polaganje();
			p.setBodoviti(bodoviti);
			p.setBodovipi(bodovipi);
			p.setDatumpolaganjati(datumPTI);
			p.setDatumpolaganjapi(datumPPI);
			p.setPolozenti(polozenti);
			p.setPolozenpi(polozenpi);
			p.setEvidencija(ev);
			em.persist(p);
			em.getTransaction().commit();
			em.close();
			return p;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean updatePolaganje(int id,int bodoviti,
			int bodovipi,Date datumPTI,Date datumPPI,
			boolean polozenpi,boolean polozenti){
		try{
			EntityManager em = JPAUtils.getEntityManager();
			Polaganje p = em.find(Polaganje.class, id);
			p.setBodoviti(bodoviti);
			p.setBodovipi(bodovipi);
			p.setDatumpolaganjapi(datumPPI);
			p.setDatumpolaganjati(datumPTI);
			p.setPolozenpi(polozenpi);
			p.setPolozenti(polozenti);
			em.merge(p);
			em.flush();
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	public List<Polaganje> getPolaganjaTestovaZaDatum(Date datumOd, Date datumDo){
		try{
			EntityManager em = JPAUtils.getEntityManager();
			TypedQuery<Polaganje> upit = em.createQuery("SELECT pol FROM Polaganje pol INNER JOIN pol.evidencija ev INNER JOIN ev.kandidat k WHERE ((pol.datumpolaganjati BETWEEN :datumOd AND :datumDo) AND pol.polozenti=true) ORDER BY k.kategorija.naziv,pol.bodoviti desc", Polaganje.class);
			upit.setParameter("datumOd", datumOd);
			upit.setParameter("datumDo", datumDo);
			List<Polaganje> polozeniTestovi = upit.getResultList();
			em.close();
			return polozeniTestovi;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public static void main(String[] args) {
//		EntityManager em = JPAUtils.getEntityManager();
//		try{
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//			Date dateOd = sdf.parse("2015-01-05");
//			Date dateDo = sdf.parse("2017-10-10");
//			List<Polaganje> listaPolozenihTestova = km.getPolaganjaTestovaZaDatum(dateOd,dateDo);
//			for(Polaganje p:listaPolozenihTestova){
//				System.out.println("Instruktor: "+p.getEvidencija().getKandidat().getInstruktor().toString()+", kandidat "+p.getEvidencija().getKandidat().getIme()+" "
//						+p.getEvidencija().getKandidat().getPrezime()+" je polozio testove za kategoriju "+p.getEvidencija().getKandidat().getKategorija().getNaziv()+" sa "+p.getBodoviti()+" bodova.");
//			}
//			Date datumPTI=sdf.parse("2016-06-22");
//			Date datumPPI=sdf.parse("2016-07-17");
//			boolean polozenti=true;
//			boolean polozenpi=false;
//			Evidencija ev = new EvidencijaManager().getEvidencijaForId(7);
//			Polaganje p = new PolaganjeManager().savePolaganje(56, 4, datumPTI, datumPPI, polozenpi, polozenti, ev);
//		}catch(Exception e){
//			e.printStackTrace();
//		}

	}//main
}