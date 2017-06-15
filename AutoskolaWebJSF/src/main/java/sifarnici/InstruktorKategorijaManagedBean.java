package sifarnici;

import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import managers.JPAUtils;
import managers.KategorijaManager;
import model.Instruktor;

@ManagedBean(name="instruktorKategorijaManagedBean")
@ApplicationScoped
public class InstruktorKategorijaManagedBean {
	
	// vuci sve instruktore iz Kategorija!!!! tj. new KategorijaManager().getSviInstruktori();
	private List<Instruktor> sviInstruktori = populateInstruktori();
	
	private List<Instruktor> populateInstruktori(){
		return new KategorijaManager().getInstruktorsByKat(JPAUtils.getEntityManager());
	}

	public List<Instruktor> getSviInstruktori() {
		return sviInstruktori;
	}
}
