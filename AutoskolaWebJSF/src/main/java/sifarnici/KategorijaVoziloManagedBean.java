package sifarnici;

import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import managers.JPAUtils;
import managers.VoziloManager;
import model.Kategorija;

@ManagedBean(name="kategorijaVoziloManagedBean")
@ApplicationScoped
public class KategorijaVoziloManagedBean {
	
	private List<Kategorija> sveKategorije = populateKategorije();
	
	private List<Kategorija> populateKategorije(){
		return new VoziloManager().getSveKategorije(JPAUtils.getEntityManager());
	}
	
	public List<Kategorija> getSveKategorije(){
		return sveKategorije;
	}
	
}