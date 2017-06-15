package sifarnici;

import java.util.List;

import javax.faces.bean.ManagedBean;

import managers.JPAUtils;
import managers.KandidatManager;
import model.Instruktor;

@ManagedBean(name="kandidatInstruktorManagedBean")
public class KandidatInstruktorManagedBean {
	
	// Treba da iskoristim KategorijaInstruktorManagedBean
	// da bi se znalo kog instruktora SMEM/MOGU da selektujem na osnovu odabrane kategorije!!!
	private List<Instruktor> sviInstruktori = populateInstruktori();
	
	private List<Instruktor> populateInstruktori(){
		return new KandidatManager().getSviInstruktori(JPAUtils.getEntityManager());
	}

	public List<Instruktor> getSviInstruktori() {
		return sviInstruktori;
	}

}
