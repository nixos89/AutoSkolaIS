package sifarnici;

import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import managers.JPAUtils;
import managers.KandidatManager;
import model.Kategorija;

@ManagedBean(name="kategorijaKandidatManagedBean")
@ApplicationScoped
public class KategorijaKandidatManagedBean {
	
	private List<Kategorija> sveKategorije = populateKategorije();
	
	private List<Kategorija> populateKategorije(){
		return new KandidatManager().getSveKategorije(JPAUtils.getEntityManager());
	}
	
	public List<Kategorija> getSveKategorije(){
		return sveKategorije;
	}
	
}
