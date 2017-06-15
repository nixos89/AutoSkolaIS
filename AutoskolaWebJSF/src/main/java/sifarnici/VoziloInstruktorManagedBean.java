package sifarnici;

import java.util.List;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import managers.JPAUtils;
import managers.VoziloManager;
import model.Vozilo;

@ManagedBean(name="voziloInstruktorManagedBean")
@ApplicationScoped
public class VoziloInstruktorManagedBean {
	
	private List<Vozilo> svaVozila = populateVozila();
	
	
	private List<Vozilo> populateVozila(){
		return new VoziloManager().getSvaVozila(JPAUtils.getEntityManager());
	}

	public List<Vozilo> getSvaVozila() {
		return svaVozila;
	}
	
}
