package managedBeans;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;

import org.primefaces.context.RequestContext;

import managers.InstruktorManager;
import managers.JPAUtils;
import managers.KategorijaManager;
import managers.VoziloManager;
import model.Instruktor;
import model.Kategorija;
import model.Vozilo;

@ManagedBean(name="unosVozilaManagedBean")
@SessionScoped
public class UnosVozilaManagedBean {
	
	@ManagedProperty(value="#{korisniciManagedBean}")
	private KorisniciManagedBean korisniciManagedBean;
		
	public void setKorisniciManagedBean(KorisniciManagedBean korisniciManagedBean) {
		this.korisniciManagedBean = korisniciManagedBean;
	}
		
	private EntityManager em;
	private VoziloManager voziloManager;
	private Vozilo vozilo;
	private KategorijaManager kategorijaManager;
	private Kategorija kategorija;
	private InstruktorManager instruktorManager;
	private Instruktor instruktor;
	private int idVozila;
	private int idInstruktora;
	private int idKategorije;
	private List<Vozilo> vozilos;
	private List<Vozilo> vozilaInstruktora;
	private List<Kategorija> kategorijas;
	private List<Kategorija> kategorijeInstruktora;
	private List<Instruktor> instruktors;
	private List<Instruktor> instruktoriZaKategoriju;
	private List<String> listaPunoImeInstruktora;
	/* -------------------------------------------- */
	private String markaV;
	private String modelV;
	private String gorivoV;
	private String registracijaV;
	private String porukaSnimljenoV;
	private String porukaUcitanoV;
	private String porukaObrisanoV;
	
	// konstruktor
	public UnosVozilaManagedBean(){
		vozilo = new Vozilo();
		kategorija = new Kategorija();
		instruktor = new Instruktor();
		voziloManager = new VoziloManager();
		kategorijaManager = new KategorijaManager();
		instruktorManager = new InstruktorManager();
		vozilos = new ArrayList<Vozilo>();
		vozilaInstruktora = new ArrayList<Vozilo>(); 
		kategorijas = new ArrayList<Kategorija>();
		kategorijeInstruktora = new ArrayList<Kategorija>();
		instruktors = new ArrayList<Instruktor>();
		instruktoriZaKategoriju = new ArrayList<Instruktor>();
		em = JPAUtils.getEntityManager();
		porukaSnimljenoV="";
		porukaObrisanoV="";
		markaV="";
		modelV="";
		gorivoV="";
		registracijaV="";
		idVozila = 0;
		idInstruktora=0;
		idKategorije=0;
		kategorijaManager = new KategorijaManager();
		kategorija = new Kategorija();
		kategorijas = new ArrayList<Kategorija>();
	}//konstruktor
	
	
	public String sacuvajVozilo(){
		Vozilo v = voziloManager.saveVozilo(vozilo);
		if(v==null){
			porukaSnimljenoV="Vozilo NIJE saèuvano!";
			errMessage(porukaSnimljenoV);
			FacesContext context = FacesContext.getCurrentInstance();
			context.getExternalContext().getFlash().setKeepMessages(true);
			vozilo = new Vozilo();
			return "/vozilo/unosVozila.xhtml?faces-redirect=true";
		}else{
			porukaSnimljenoV = "Vozilo "+v.getMarka()+" "+v.getModel()+" je uspešno saèuvano pod id-jem: "+v.getIdVozila();
			saveMessage(porukaSnimljenoV);
			FacesContext context = FacesContext.getCurrentInstance();
			context.getExternalContext().getFlash().setKeepMessages(true);
			vozilo = new Vozilo();
			return "/vozilo/unosVozila.xhtml?faces-redirect=true";
		}	
	}
	
	
	// Metoda za AZURIRANJE vozila - Implementiraj po uzoru na nadjiInstruktor() !!!
	public String nadjiVozilo(int idVoz){
		EntityManager em = JPAUtils.getEntityManager();
		vozilo = em.find(Vozilo.class, idVoz);
		kategorija = vozilo.getKategorija();
		return "/vozilo/azurirajVozilo.xhtml?faces-redirect=true";
	}//nadjiVozilo
	
	
	/* Ovaj metod se ne koristi - BAD SMELL!!! */
	public String azurirajVozilo(){
		voziloManager.updateVozilo(vozilo);
		findByMarkaModelGorivoRegistracija();
		vozilo = new Vozilo();
		return "/vozilo/pronadjiVozilo.xhtml?faces-redirect=true";
	}
	
	
	public String azurirajVozilo2(){
		voziloManager.updateVozilo(vozilo);
		if( (korisniciManagedBean.getInstruktor()!=null) && (korisniciManagedBean.getInstruktor().getAdmin()) ){
			porukaUcitanoV = "Vozilo "+vozilo.getMarka()+" "+vozilo.getModel()+" je uspešno ažurirano!";
			infoMessage(porukaUcitanoV);
			FacesContext context = FacesContext.getCurrentInstance();
			context.getExternalContext().getFlash().setKeepMessages(true);
			findByMarkaModelGorivoRegistracija();
			vozilo = new Vozilo();
			return "/vozilo/pronadjiVozilo.xhtml?faces-redirect=true";
		}else if(korisniciManagedBean.getInstruktor()!=null){
			porukaUcitanoV = "Vozilo "+vozilo.getMarka()+" "+vozilo.getModel()+" je uspešno ažurirano!";
			infoMessage(porukaUcitanoV);
			FacesContext context = FacesContext.getCurrentInstance();
			context.getExternalContext().getFlash().setKeepMessages(true);
			findVoziloInstruktoraByMarkaModelGorivoRegistracija(korisniciManagedBean.getInstruktor().getIdInstruktor());
			vozilo = new Vozilo();
			return "/instruktor/vozilaInstruktora.xhtml?faces-redirect=true"; 
		}else{
			porukaUcitanoV = "Vozilo "+vozilo.getMarka()+" "+vozilo.getModel()+" NIJE ažurirano!";
			errMessage(porukaUcitanoV);
			FacesContext context = FacesContext.getCurrentInstance();
			context.getExternalContext().getFlash().setKeepMessages(true);
			findVoziloInstruktoraByMarkaModelGorivoRegistracija(korisniciManagedBean.getInstruktor().getIdInstruktor());
			vozilo = new Vozilo();
			return "/instruktor/vozilaInstruktora.xhtml?faces-redirect=true"; 
		}	
	}
	
	
	public List<Instruktor> vratiInstruktoreZaKategoriju(){
		Kategorija kat = vozilo.getKategorija();
		int idKat = kat.getIdKategorija();
		instruktoriZaKategoriju = instruktorManager.getInstsZaKategoriju(idKat);
		return instruktoriZaKategoriju;
	}
	
	public List<Vozilo> findByMarkaModelGorivoRegistracija(){
		vozilos = voziloManager.getVoziloByMarkaModelGorivo(markaV, modelV, gorivoV, registracijaV);
		if(vozilos.size()<=0){
			porukaUcitanoV = "Vozilo nije ucitano, jer NE POSTOJI u bazi!";
			return null;
		}else{
			return vozilos;
		}
	}
	
	
	public List<Vozilo> findVoziloInstruktoraByMarkaModelGorivoRegistracija(int idInst){
		vozilaInstruktora = voziloManager.getVoziloInstruktoraByMarkaModelGorivo(idInst,markaV, modelV, gorivoV, registracijaV);
		if(vozilaInstruktora.size()<=0){
			porukaUcitanoV = "Vozilo nije ucitano, jer NE POSTOJI u bazi!";
			return null;
		}else{
			return vozilaInstruktora;
		}
	}
	
	// Ovaj metode bi pre BIO UPOTREBLJIV za prikaz vozila
	public List<Instruktor> vratiInstruktoreVozila(){
		// U InstruktorManager-u PROMENI 'idVozila' na 'vozilo'!
		instruktors = instruktorManager.getInstruktorsByVozilo(vozilo);
		return instruktors;
	}
	
	public List<Kategorija> ucitajKategorije(){
		kategorijas = kategorijaManager.vratiSveKategorije();
		return kategorijas;
	}
	
	public void reset() {
        RequestContext.getCurrentInstance().reset("form:voziloPanel");
    }
	
	
	
	public void obrisiVozilo(Vozilo v){
		idVozila = voziloManager.getIdForVozilo(v);
//		vozilo = voziloManager.getVoziloForId(idVozila);
		String marka = v.getMarka();
		String model = v.getModel();
		boolean ok = voziloManager.deleteVozilo(idVozila);
		if(ok){
			porukaObrisanoV="Vozilo marke:"+marka+", modela:"+model+" je uspešno obrisano!";
			infoMessage(porukaObrisanoV);
			FacesContext context = FacesContext.getCurrentInstance();
			context.getExternalContext().getFlash().setKeepMessages(true);
			findByMarkaModelGorivoRegistracija();
		}else{
			porukaObrisanoV="Vozilo NIJE obrisano!";
			errMessage(porukaObrisanoV);
			FacesContext context = FacesContext.getCurrentInstance();
			context.getExternalContext().getFlash().setKeepMessages(true);
			findByMarkaModelGorivoRegistracija();
		}
	}//obrisiVozilo
	
	// Parazit metod - BAD SMELL!!! -->> PROVERI!!!!!!!
	public List<Instruktor> getInstruktoriByKategorija(int idKat){
		instruktoriZaKategoriju = instruktorManager.getInstsZaKategoriju(idKat);
		return instruktoriZaKategoriju;
	}
	
	/* ------------------------------------------------------------------------ */
	/* -------- metode za izbacivanje poruka rezultujucih akcija CRUD-a ------- */
	/* ------------------------------------------------------------------------ */	
	
	public void saveMessage(String poruka) {
		FacesContext context = FacesContext.getCurrentInstance();

		context.addMessage(null, new FacesMessage("Obaveštenje", poruka));
	}

	public void infoMessage(String poruka) {
		FacesContext context = FacesContext.getCurrentInstance();

		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Obaveštenje", poruka));

	}

	public void errMessage(String poruka) {
		FacesContext context = FacesContext.getCurrentInstance();

		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Greška", poruka));

	}
	
	
	
	/* ------------------------------------------------------------------------ */
	/* ----------------- GET i SET metode za polja, tj. Properties ------------ */
	/* ------------------------------------------------------------------------ */	
	
	public EntityManager getEm() {
		return em;
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}

	public VoziloManager getVoziloManager() {
		return voziloManager;
	}

	public void setVoziloManager(VoziloManager voziloManager) {
		this.voziloManager = voziloManager;
	}

	public Vozilo getVozilo() {
		return vozilo;
	}

	public void setVozilo(Vozilo vozilo) {
		this.vozilo = vozilo;
	}

	public List<Vozilo> getVozilos() {
		return vozilos;
	}

	public void setVozilos(List<Vozilo> vozilos) {
		this.vozilos = vozilos;
	}

	public List<Vozilo> getVozilaInstruktora() {
		return vozilaInstruktora;
	}


	public void setVozilaInstruktora(List<Vozilo> vozilaInstruktora) {
		this.vozilaInstruktora = vozilaInstruktora;
	}


	public int getIdVozila() {
		return idVozila;
	}

	public void setIdVozila(int idVozila) {
		this.idVozila = idVozila;
	}

	public int getIdInstruktora() {
		return idInstruktora;
	}


	public void setIdInstruktora(int idInstruktora) {
		this.idInstruktora = idInstruktora;
	}


	public int getIdKategorije() {
		return idKategorije;
	}


	public void setIdKategorije(int idKategorije) {
		this.idKategorije = idKategorije;
	}


	public List<Kategorija> getKategorije() {
		return kategorijas;
	}

	public void setKategorije(List<Kategorija> kategorijas) {
		this.kategorijas = kategorijas;
	}

	public Kategorija getKategorija() {
		return kategorija;
	}

	public void setKategorija(Kategorija kategorija) {
		this.kategorija = kategorija;
	}

	public InstruktorManager getInstruktorManager() {
		return instruktorManager;
	}

	public void setInstruktorManager(InstruktorManager instruktorManager) {
		this.instruktorManager = instruktorManager;
	}

	public Instruktor getInstruktor() {
		return instruktor;
	}

	public void setInstruktor(Instruktor instruktor) {
		this.instruktor = instruktor;
	}

	public List<Kategorija> getKategorijas() {
		return kategorijas;
	}

	public void setKategorijas(List<Kategorija> kategorijas) {
		this.kategorijas = kategorijas;
	}

	public List<Kategorija> getKategorijeInstruktora() {
		return kategorijeInstruktora;
	}


	public void setKategorijeInstruktora(List<Kategorija> kategorijeInstruktora) {
		this.kategorijeInstruktora = kategorijeInstruktora;
	}


	public List<Instruktor> getInstruktors() {
		return instruktors;
	}

	public void setInstruktors(List<Instruktor> instruktors) {
		this.instruktors = instruktors;
	}

	public List<Instruktor> getInstruktoriZaKategoriju() {
		return instruktoriZaKategoriju;
	}


	public void setInstruktoriZaKategoriju(List<Instruktor> instruktoriZaKategoriju) {
		this.instruktoriZaKategoriju = instruktoriZaKategoriju;
	}


	public List<String> getListaPunoImeInstruktora() {
		return listaPunoImeInstruktora;
	}


	public void setListaPunoImeInstruktora(List<String> listaPunoImeInstruktora) {
		this.listaPunoImeInstruktora = listaPunoImeInstruktora;
	}


	public KategorijaManager getKategorijaManager() {
		return kategorijaManager;
	}

	public void setKategorijaManager(KategorijaManager kategorijaManager) {
		this.kategorijaManager = kategorijaManager;
	}
	
	public String getPorukaSnimljenoV() {
		return porukaSnimljenoV;
	}

	public void setPorukaSnimljenoV(String porukaSnimljenoV) {
		this.porukaSnimljenoV = porukaSnimljenoV;
	}
	
	public String getPorukaUcitanoV() {
		return porukaUcitanoV;
	}

	public void setPorukaUcitanoV(String porukaUcitanoV) {
		this.porukaUcitanoV = porukaUcitanoV;
	}

	public String getPorukaObrisanoV() {
		return porukaObrisanoV;
	}

	public void setPorukaObrisanoV(String porukaObrisanoV) {
		this.porukaObrisanoV = porukaObrisanoV;
	}

	public String getMarkaV() {
		return markaV;
	}

	public void setMarkaV(String markaV) {
		this.markaV = markaV;
	}

	public String getModelV() {
		return modelV;
	}

	public void setModelV(String modelV) {
		this.modelV = modelV;
	}

	public String getGorivoV() {
		return gorivoV;
	}

	public void setGorivoV(String gorivoV) {
		this.gorivoV = gorivoV;
	}

	public String getRegistracijaV() {
		return registracijaV;
	}

	public void setRegistracijaV(String registracijaV) {
		this.registracijaV = registracijaV;
	}

}
