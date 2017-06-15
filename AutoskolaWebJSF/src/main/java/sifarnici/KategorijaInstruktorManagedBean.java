package sifarnici;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import managers.InstruktorManager;
import managers.JPAUtils;
import model.Kategorija;

@ManagedBean(name="kategorijaInstruktorManagedBean")
@ApplicationScoped
public class KategorijaInstruktorManagedBean {

	private List<String> sveKategorije = populateKategorije();
	private List<Kategorija> basSveKategorije = popuniKategorije();

	
//	private List<String> naziviKategorija = (List<String>)new InstruktorManager().getSveKategorije(JPAUtils.getEntityManager());
	
	private List<String> populateKategorije(){
		// ovde umesto metoda getSveKategorije pozovi metod "getKatsZaInstruktora()"
		// samo NADJI nacin da ubacis i idInstruktor kao parametar!!!!
		List<Kategorija> kats = new InstruktorManager().getSveKategorije(JPAUtils.getEntityManager());
		sveKategorije = new ArrayList<String>();
		for(Kategorija nazivKat:kats){
			sveKategorije.add(nazivKat.getNaziv());
		}
		return sveKategorije;
	}
	
	private List<Kategorija> popuniKategorije(){
		// ovde umesto metoda getSveKategorije pozovi metod "getKatsZaInstruktora()"
		// samo NADJI nacin da ubacis i idInstruktor kao parametar!!!!
		List<Kategorija> kats = new InstruktorManager().getSveKategorije(JPAUtils.getEntityManager());
		return kats;
	}
	
	public List<String> getSveKategorije(){
		return sveKategorije;
	}

	public List<Kategorija> getBasSveKategorije() {
		return basSveKategorije;
	}
	
	
}