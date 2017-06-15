package managedBeans;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.persistence.EntityManager;

import managers.InstruktorManager;
import managers.JPAUtils;
import managers.KategorijaManager;
import managers.VoziloManager;
import model.Instruktor;
import model.Kandidat;
import model.Kategorija;
import model.Vozilo;

@ManagedBean(name="unosInstruktoraManagedBean")
@SessionScoped // promenjeno sa SessionScoped na ViewScoped - ne radi! -> sad je RequestScoped
public class UnosInstruktoraManagedBean {
	
	@ManagedProperty(value="#{korisniciManagedBean}")
	private KorisniciManagedBean korisniciManagedBean;
		
	public void setKorisniciManagedBean(KorisniciManagedBean korisniciManagedBean) {
		this.korisniciManagedBean = korisniciManagedBean;
	}
	
	private Instruktor instruktor;
	private int idInstruktor;
	private String imeInst;
	private String prezimeInst;
	private String jmbg;
	private InstruktorManager instruktorManager;
	private Kategorija kategorija;
	private KategorijaManager kategorijaManager;
	private VoziloManager voziloManager;
	private List<Instruktor> instruktors;
	private List<Kandidat> kandidats;
	private List<Kategorija> kategorijas;
	private List<Kategorija> kategorijeInstruktora;
	private List<Vozilo> vozilos;
	private List<Vozilo> vozilaZaInstruktora;
	private List<Vozilo> vozilaZaInstruktora2;
	private List<String> listaPunoImeInstruktora;
	private List<String> listaNaziviKategorija;
	private List<String> listaModelVozila; 
	private EntityManager em;
	private String porukaInstruktor;
	private String porukaUcitanI;
	private String porukaSacuvanI;
	private String porukaObrisanI;
	private String naziv;
	
	public UnosInstruktoraManagedBean(){
		instruktor = new Instruktor();
		instruktorManager = new InstruktorManager();
		kategorijaManager = new KategorijaManager();
		voziloManager = new VoziloManager();
		kategorijas = new ArrayList<Kategorija>();
		kategorijeInstruktora=new ArrayList<Kategorija>();
		kandidats = new ArrayList<Kandidat>();
		vozilos = new ArrayList<Vozilo>();
		vozilaZaInstruktora = new ArrayList<Vozilo>();
		vozilaZaInstruktora2 = new ArrayList<Vozilo>();
		idInstruktor=0;
		instruktors = new ArrayList<Instruktor>(); // promenjeno sa null na new ArrayList<Instruktor>();		
		em = JPAUtils.getEntityManager();
		porukaInstruktor="";
		porukaUcitanI="";
		porukaSacuvanI="";
		porukaObrisanI="";
		jmbg="";
		imeInst="";
		prezimeInst="";
		naziv="";
	}
	
	// radi PROVERENO!
	public void saveInstruktor(){
		int idInstruktora = new InstruktorManager().saveInstruktor(instruktor);
		Instruktor i = new InstruktorManager().getInstruktorForId(idInstruktora);
		if(i.getIdInstruktor()==0){ // promenjeno sa "idInstruktora==0" na "i.getIdInstruktor==0" !!!
			porukaInstruktor="Instrukotr NIJE sacuvan!";
		}else{
			porukaInstruktor = "Instruktor "+i.getIme()+" "+i.getPrezime()+" JE sacuvan pod id: "+idInstruktora;
		}
	}
	
	
	// probaj da napravis metod "public void init(){}" i u njemu da inicijalizujes 
	// selekciju za odogvarajuce checkbox-eve!!! - RADI!!!
	public List<Kategorija> vratiKategorijeInstruktora(){
		// probaj sa "kategorijas" umesto sa "kategorijeInstruktora"
		kategorijas = instruktorManager.getKatsZaInstruktora2(instruktor);
		kategorijeInstruktora = kategorijas;
		return kategorijeInstruktora; // ...ili kategorijeInstruktora
	}
	
	// pronalazi instruktora za prosledjeni ID, pravi novu ArrayList-u
	// i puni je sa nazivima kategorija za pronadjenoj instrukgora, 
	// vracaju se sve kategorije (radi prikaza) i NA KRAJU vraca(prelazi) 
	// se na stranicu 'azurirajInstruktora.xhtml'
	public String nadjiInstruktora(int idInstruktora){
		EntityManager em = JPAUtils.getEntityManager();
		instruktor = em.find(Instruktor.class, idInstruktora);
		listaNaziviKategorija = new ArrayList<String>();
		for(Kategorija kat:instruktor.getKategorijas()){
			listaNaziviKategorija.add(kat.getNaziv());
		}
		vratiKategorijeInstruktora(); // Zakomentarisano radi TESTIRANJA, inace TREBA poziv ovog metoda!!!!
		porukaUcitanI="Instruktor "+instruktor.getIme()+" "+instruktor.getPrezime()+"je ucitan!";
		return "/instruktor/azurirajInstruktora.xhtml";
	}
	
	public List<Vozilo> vratiVozilaInstruktora(){
		vozilos = new VoziloManager().getVozilosByInstruktor(instruktor);
		vozilaZaInstruktora = vozilos;
		return vozilaZaInstruktora; 
	}
	
	
	// metoda namenjena za dodavanje Vozila Instruktoru na stranici "dodeliVoziloInstruktoru.xhtml"
	public String nadjiInstruktora2(int idInstruktora){
		EntityManager em = JPAUtils.getEntityManager();
		instruktor = em.find(Instruktor.class, idInstruktora);
		listaModelVozila = new ArrayList<String>();
		List<Kategorija> katsZaProsledjenogInst = instruktor.getKategorijas();
		List<Vozilo> tempListaZaVozilaInst = new ArrayList<Vozilo>();
		List<Vozilo> tempListaZaVozilaInst2 = new ArrayList<Vozilo>();
		tempListaZaVozilaInst2 = new VoziloManager().getVozilosByInstruktor(instruktor);
		for(Kategorija kat:katsZaProsledjenogInst){
			tempListaZaVozilaInst = new VoziloManager().getVozilaByKategorijas3(kat); // promenjeno sa 'getVozilaByKategorijas2' na 'getVozilaByKategorijas3'
			vozilaZaInstruktora2.addAll(tempListaZaVozilaInst);
		}
		for(Vozilo v: tempListaZaVozilaInst2){ // promenjeno sa 'vozilaZaInstruktora2' na 'tempListaZaVozilaInst2'
			listaModelVozila.add(v.getModel());
		}
		vratiVozilaInstruktora();
		return "/instruktor/dodeliVoziloInstruktoru.xhtml?faces-redirect=true";
	}//nadjiInstruktora2
	
	
	
	// RADI! - Nepotreban metod, koristio kad su se instruktori dodavali vozilu, a ne obrnuto!
	public List<Instruktor> findInstruktorsByImePrezimeJmbg(){
		instruktors = instruktorManager.getInstruktorsByImePrezimeJmbg(imeInst,prezimeInst,jmbg);
		if(instruktors.size()<=0){
			porukaUcitanI="Instruktor NE POSTOJI sa takvim podacima!";
			return null;
		}else{
			return instruktors;
		}
	}
	
	public List<Vozilo> selektovanaVozila(){
		List<Vozilo> selektovana = new ArrayList<Vozilo>();
		for(String model:listaModelVozila){
			selektovana.add(voziloManager.getVoziloByModel(model));			
		}
		return selektovana;
	}

	// sluzi SAMO da prikaze na poruci KOJE su kategorije selektovane
	public List<Kategorija> selektovaneKategorije(){
		List<Kategorija> selektovane = new ArrayList<Kategorija>();
		for(String naziv:listaNaziviKategorija){
			selektovane.add(kategorijaManager.getKategorijaByNaziv(naziv));		
		}
		return selektovane; 
	}
	
	public String updateInstruktor(){		
		instruktor.setKategorijas(selektovaneKategorije());
		System.out.println("selektovaneKategorije()"+selektovaneKategorije());
		instruktorManager.updateInstruktor(instruktor);
		System.out.println("Ispis posle instruktorManager.updateInstruktor(instruktor)");
		porukaInstruktor = "Instruktor "+instruktor.getIme()+" "+instruktor.getPrezime()+" je uspešno ažuriran!";
		infoMessage(porukaInstruktor);
		FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().getFlash().setKeepMessages(true);
		findInstruktorsByImePrezimeJmbg(); 
		instruktor = new Instruktor();
		listaNaziviKategorija = new ArrayList<String>();
		return "/instruktor/pronadjiInstruktora.xhtml?faces-redirect=true";
	}
	
	
	public String updateInstruktor2(){		
		instruktor.setVozilos(selektovanaVozila());
		System.out.println("selektovanaVozila()"+selektovanaVozila());
		instruktorManager.updateInstruktor2(instruktor);
		System.out.println("Ispis posle instruktorManager.updateInstruktor2(instruktor)");
		if(selektovanaVozila().size()==1){
			porukaUcitanI="Instruktoru "+instruktor.getIme()+" "+instruktor.getPrezime()+" je uspešno dodeljeno vozilo "+selektovanaVozila().get(0);
			infoMessage(porukaUcitanI);
			FacesContext context = FacesContext.getCurrentInstance();
			context.getExternalContext().getFlash().setKeepMessages(true);
			findInstruktorsByImePrezimeJmbg(); 
			instruktor = new Instruktor();
			listaModelVozila = new ArrayList<String>();
			return "/instruktor/pronadjiInstruktora.xhtml?faces-redirect=true";
		}	
		else if(selektovanaVozila().size()>1){
			porukaUcitanI="Instruktoru "+instruktor.getIme()+" "+instruktor.getPrezime()+" su uspešno dodeljena vozila: "+selektovanaVozila();
			infoMessage(porukaUcitanI);
			FacesContext context = FacesContext.getCurrentInstance();
			context.getExternalContext().getFlash().setKeepMessages(true);
			findInstruktorsByImePrezimeJmbg(); 
			instruktor = new Instruktor();
			listaModelVozila = new ArrayList<String>();
			return "/instruktor/pronadjiInstruktora.xhtml?faces-redirect=true";
		}
		else{
			porukaUcitanI="Instruktoru "+instruktor.getIme()+" "+instruktor.getPrezime()+" su NISU dodeljena vozila!";
			errMessage(porukaUcitanI);
			FacesContext context = FacesContext.getCurrentInstance();
			context.getExternalContext().getFlash().setKeepMessages(true);
			findInstruktorsByImePrezimeJmbg(); 
			instruktor = new Instruktor();
			listaModelVozila = new ArrayList<String>();
			return "/instruktor/pronadjiInstruktora.xhtml?faces-redirect=true";
		}
		
	}
	
	public String sacuvajInstruktora(List<Kategorija> kategorije){
		instruktor.setKategorijas(selektovaneKategorije());
		int idInstruktora = new InstruktorManager().saveInstruktor(instruktor);
		if(idInstruktora!=0){
			porukaInstruktor="Instrukotr NIJE sacuvan, a kategorije su: "+selektovaneKategorije();
			errMessage(porukaInstruktor);
			FacesContext context = FacesContext.getCurrentInstance();
			context.getExternalContext().getFlash().setKeepMessages(true);
			instruktor = new Instruktor();
			return "/instruktor/unosInstruktora.xhtml?faces-redirect=true";
		}else{			
			porukaInstruktor = "Instruktor "+instruktor.getIme()+" "+instruktor.getPrezime()+" JE sacuvan, kategorije: "+selektovaneKategorije();
			saveMessage(porukaInstruktor);
			FacesContext context = FacesContext.getCurrentInstance();
			context.getExternalContext().getFlash().setKeepMessages(true);
			instruktor = new Instruktor();
			return "/instruktor/unosInstruktora.xhtml?faces-redirect=true";
		}		
	}
	
	// RADI!
	public void obrisiInstruktora(Instruktor inst){
		idInstruktor = inst.getIdInstruktor();
		String imeI = inst.getIme();
		String prezimeI = inst.getPrezime();
		boolean ok = instruktorManager.obrisiInstruktora3(idInstruktor);
		if(ok){
			porukaObrisanI="Instruktor "+imeI+" "+prezimeI+" JE UKLONJEN";
			infoMessage(porukaObrisanI);
			findInstruktorsByImePrezimeJmbg();
		}else{
			porukaObrisanI="Instruktor "+imeI+" "+prezimeI+" NIJE UKLONJEN!";
			errMessage(porukaObrisanI);
			findInstruktorsByImePrezimeJmbg();
		}
	}//obrisiInstruktora
	
	public void nadjiKandidateInstruktora(int idInst){
		Instruktor i = em.find(Instruktor.class, idInst);
		kandidats = instruktorManager.getKandidatsByInstruktor(i);
	}
	
	public void nadjiVozilaInstruktora(int idInst){
		Instruktor i = em.find(Instruktor.class, idInst);
		vozilos = null; // Ispravi pod HITNO!!
	}
	
	public String vratiInstruktorPocetna(){
		return "/instruktor/instruktor.xhtml?faces-redirect=true";
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
	
	
	public Instruktor getInstruktor() {
		return instruktor;
	}

	public void setInstruktor(Instruktor instruktor) {
		this.instruktor = instruktor;
	}

	public InstruktorManager getInstruktorManager() {
		return instruktorManager;
	}

	public void setInstruktorManager(InstruktorManager instruktorManager) {
		this.instruktorManager = instruktorManager;
	}

	public EntityManager getEm() {
		return em;
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}

	public String getPorukaInstruktor() {
		return porukaInstruktor;
	}

	public void setPorukaInstruktor(String porukaInstruktor) {
		this.porukaInstruktor = porukaInstruktor;
	}

	public List<Kategorija> getKategorijas() {
		return kategorijas;
	}

	public void setKategorijas(List<Kategorija> kategorijas) {
		this.kategorijas = kategorijas;
	}

	public List<Vozilo> getVozilos() {
		return vozilos;
	}

	public void setVozilos(List<Vozilo> vozilos) {
		this.vozilos = vozilos;
	}

	public List<Vozilo> getVozilaZaInstruktora() {
		return vozilaZaInstruktora;
	}

	public void setVozilaZaInstruktora(List<Vozilo> vozilaZaInstruktora) {
		this.vozilaZaInstruktora = vozilaZaInstruktora;
	}

	public List<Vozilo> getVozilaZaInstruktora2() {
		return vozilaZaInstruktora2;
	}

	public void setVozilaZaInstruktora2(List<Vozilo> vozilaZaInstruktora2) {
		this.vozilaZaInstruktora2 = vozilaZaInstruktora2;
	}

	public List<String> getListaPunoImeInstruktora() {
		return listaPunoImeInstruktora;
	}

	public void setListaPunoImeInstruktora(List<String> listaPunoImeInstruktora) {
		this.listaPunoImeInstruktora = listaPunoImeInstruktora;
	}

	public List<String> getListaNaziviKategorija() {
		return listaNaziviKategorija;
	}

	public void setListaNaziviKategorija(List<String> listaNaziviKategorija) {
		this.listaNaziviKategorija = listaNaziviKategorija;
	}

	public List<String> getListaModelVozila() {
		return listaModelVozila;
	}

	public void setListaModelVozila(List<String> listaModelVozila) {
		this.listaModelVozila = listaModelVozila;
	}

	public String getJmbg() {
		return jmbg;
	}

	public void setJmbg(String jmbg) {
		this.jmbg = jmbg;
	}

	public KategorijaManager getKategorijaManager() {
		return kategorijaManager;
	}

	public void setKategorijaManager(KategorijaManager kategorijaManager) {
		this.kategorijaManager = kategorijaManager;
	}

	public VoziloManager getVoziloManager() {
		return voziloManager;
	}

	public void setVoziloManager(VoziloManager voziloManager) {
		this.voziloManager = voziloManager;
	}

	public String getPorukaUcitanI() {
		return porukaUcitanI;
	}

	public void setPorukaUcitanI(String porukaUcitanI) {
		this.porukaUcitanI = porukaUcitanI;
	}

	public String getPorukaSacuvanI() {
		return porukaSacuvanI;
	}

	public void setPorukaSacuvanI(String porukaSacuvanI) {
		this.porukaSacuvanI = porukaSacuvanI;
	}

	public String getPorukaObrisanI() {
		return porukaObrisanI;
	}

	public void setPorukaObrisanI(String porukaObrisanI) {
		this.porukaObrisanI = porukaObrisanI;
	}

	public List<Instruktor> getInstruktors() {
		return instruktors;
	}

	public void setInstruktors(List<Instruktor> instruktors) {
		this.instruktors = instruktors;
	}

	public List<Kandidat> getKandidats() {
		return kandidats;
	}

	public void setKandidats(List<Kandidat> kandidats) {
		this.kandidats = kandidats;
	}

	public Kategorija getKategorija() {
		return kategorija;
	}

	public void setKategorija(Kategorija kategorija) {
		this.kategorija = kategorija;
	}

	public String getImeInst() {
		return imeInst;
	}

	public void setImeInst(String imeInst) {
		this.imeInst = imeInst;
	}

	public String getPrezimeInst() {
		return prezimeInst;
	}

	public void setPrezimeInst(String prezimeInst) {
		this.prezimeInst = prezimeInst;
	}

	public int getIdInstruktor() {
		return idInstruktor;
	}

	public void setIdInstruktor(int idInstruktor) {
		this.idInstruktor = idInstruktor;
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public List<Kategorija> getKategorijeInstruktora() {
		return kategorijeInstruktora;
	}

	public void setKategorijeInstruktora(List<Kategorija> kategorijeInstruktora) {
		this.kategorijeInstruktora = kategorijeInstruktora;
	}
}