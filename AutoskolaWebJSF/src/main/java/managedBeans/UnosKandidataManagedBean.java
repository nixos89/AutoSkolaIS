package managedBeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.persistence.EntityManager;

import org.primefaces.context.RequestContext;

import managers.EvidencijaManager;
import managers.InstruktorManager;
import managers.JPAUtils;
import managers.KandidatManager;
import managers.KategorijaManager;
import model.Evidencija;
import model.Instruktor;
import model.Kandidat;
import model.Kategorija;
import sun.security.krb5.internal.util.KrbDataOutputStream;

@ManagedBean(name="unosKandidataManagedBean")
@SessionScoped 
public class UnosKandidataManagedBean/* implements Serializable*/{
	
	/* INJECT-ovanje jednog ManagedBean-a u drugi */
	@ManagedProperty(value="#{korisniciManagedBean}")
	private KorisniciManagedBean korisniciManagedBean;
		
	public void setKorisniciManagedBean(KorisniciManagedBean korisniciManagedBean) {
		this.korisniciManagedBean = korisniciManagedBean;
	}

	private EntityManager em;
	private Kandidat kandidat;
	private KandidatManager km;	
	private Evidencija evidencija;
	private EvidencijaManager evidencijaManager;
	private Instruktor instruktor;
	private InstruktorManager instruktorManager;
	private Kategorija kategorija;
	private KategorijaManager kategorijaManager;
	private int idKandidat;
	private int idEvidencija;
	private int idKategorija;
	private int idInstruktor;
	private List<Kandidat> kandidats;
	private List<Kandidat> kandidatsInstruktora;
	private List<Kategorija> kategorijas;
	private List<Instruktor> instruktors;
	private List<String> listaPunoImeInstruktora;
	private List<String> listaPunoImeInstruktora2;
	private List<String> listaNaziviKategorija;
	private List<String> listaNaziviKategorija2;
	/* --------------------------------------- */	
	private Date datumIzdLu;
	private Date datumUpisa;
	private String nazivKategorije;
	private String imeKand;
	private String prezimeKand;
	private String prezimeInstruktora;
	private String jmbg;
	private String brLekUV;
	private String porukaKandidat;
	private String porukaUcitanKand;
	private String porukaObrisanK;
	
	
	public UnosKandidataManagedBean(){
		kandidat = new Kandidat();
		km = new KandidatManager();
		evidencija = new Evidencija();
		kategorijaManager = new KategorijaManager();
		evidencijaManager = new EvidencijaManager();
		brLekUV="";
		imeKand="";
		prezimeKand="";
		jmbg="";
		datumIzdLu=null;
		datumUpisa=null;
		idKandidat=0;
		instruktorManager = new InstruktorManager();
		kandidats=null;
		kandidatsInstruktora=null;
		kategorijas = new ArrayList<Kategorija>();
		instruktors = new ArrayList<Instruktor>();
		em = JPAUtils.getEntityManager();
		porukaKandidat="";
		porukaObrisanK="";
		listaNaziviKategorija=kategorijaManager.vratiSveKategorijeNaziv2();
	}//UnosKandidataManagedBean
	
	
	public void localeChanged(ValueChangeEvent event){
		listaPunoImeInstruktora2 = instruktorManager.instruktorsByKatNaziv2(event.getNewValue().toString());
		// promenuto sa "instruktorsByKatNaziv" na "instruktorsByKatNaziv2"
		
	}
	
	public Kategorija selektovanaKategorija(){
		List<Kategorija> selektovaneKategorije = new ArrayList<Kategorija>();
		// u "for-u" bi trebalo da stoji "listaNaziviKategorija2", a NE "listaNaziviKategorija" !!!
		for(String naziv:listaNaziviKategorija){ 
			selektovaneKategorije.add(kategorijaManager.getKategorijaByNaziv(naziv));
		}
		Kategorija samo1Kategorija = selektovaneKategorije.get(0);
		return samo1Kategorija;
	}
	
	
	public Instruktor selektovanInstruktor(){
		List<Instruktor> selektovaniInstruktor = new ArrayList<Instruktor>();
		for(String prezime:listaPunoImeInstruktora2){
			selektovaniInstruktor.add(instruktorManager.getInstruktorByJMBG(prezime));
		}
		Instruktor samo1Instruktor = selektovaniInstruktor.get(0);
		return samo1Instruktor;
	}
	
	
	public void reset() {
        RequestContext.getCurrentInstance().reset("formaKandidata:unosKandidataPanel");
	}
	
	
	// RADI provereno!!!!
	public String sacuvajKandidata(){
		Evidencija ev = new Evidencija();
		int idEvid = new EvidencijaManager().saveEvidencija(evidencija);
		ev = new EvidencijaManager().getEvidencijaForId(idEvid);
		ev.setBrLekarUverenja(brLekUV); 
		ev.setDatumIzdavanjaLu(datumIzdLu);
		ev.setDatumupisa(datumUpisa);
		// ubaci da se nadje ID Kategorije!!! - PRE toga vrati na SessionScoped da vidis HOCE li raditi + procitaj Aselovo izlaganje!
		kategorija = kategorijaManager.getKategorijaByNaziv(nazivKategorije); // ako nece s ovim, probaj isto kao sto si uradio i za instruktora!
		kandidat.setKategorija(kategorija);
		Instruktor inst = instruktorManager.getInstruktorByPrezime(prezimeInstruktora);
		int idInst = inst.getIdInstruktor();
		instruktor = instruktorManager.getInstruktorForId(idInst);
//		instruktor = instruktorManager.getInstruktorByPrezime(prezimeInstruktora);
		kandidat.setInstruktor(instruktor);
		int idKandidat = km.saveKandidat(kandidat,ev); // menja se sa 'new KandidatManager()', na 'km'
		evidencija = ev; // NE DIRAJ!!! - dodato da bi NAVODNO proradila pretraga evidencije u "pronadjiKandidata.xhtml"
		if(idKandidat==0){
			porukaKandidat="Kandidatu "+kandidat.getIme()+" "+kandidat.getPrezime()+
					"  je dodeljen instruktor "+instruktor.getIme()+" "+instruktor.getPrezime()+
					" za kategoriju "+kandidat.getKategorija().getNaziv();
			saveMessage(porukaKandidat);
			FacesContext context = FacesContext.getCurrentInstance();
			context.getExternalContext().getFlash().setKeepMessages(true);
			brLekUV="";
			datumIzdLu=null;
			datumUpisa=null;
			kandidat = new Kandidat();
			evidencija = new Evidencija();
			nazivKategorije="";
			prezimeInstruktora = "";
			return "/kandidat/unosKandidata.xhtml?faces-redirect=true";
		}else{
			porukaKandidat="Kandidat NIJE sacuvan";
			errMessage(porukaKandidat);
			FacesContext context = FacesContext.getCurrentInstance();
			context.getExternalContext().getFlash().setKeepMessages(true);
			brLekUV="";
			datumIzdLu=null;
			datumUpisa=null;
			kandidat = new Kandidat();
			evidencija = new Evidencija();
			nazivKategorije="";
			prezimeInstruktora = "";	
			return "/kandidat/unosKandidata.xhtml?faces-redirect=true";
		}
	}// sacuvajKandidata()		
	
	
	public String nadjiKandidata(int idKand){
		EntityManager em = JPAUtils.getEntityManager();
		kandidat = em.find(Kandidat.class, idKand);
		return "/kandidat/azurirajKandidata.xhtml?faces-redirect=true";
	}
	
	
	// STARI metod !!! - azurira SAMO podatke Kandidata iz njegove tabele u bazi 
	public String azurirajKandidata(){
		idKandidat=km.updateKandidat3(kandidat);
		porukaUcitanKand="Kandidat "+kandidat.getIme()+" "+kandidat.getPrezime()+" je uspešno ažuriran!";
		System.out.println("Kandidat "+kandidat.getIme()+" "+kandidat.getPrezime()+" je azuriran.");
		
		infoMessage(porukaUcitanKand);
		FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().getFlash().setKeepMessages(true);
		findKandidatsByImePrezimeJmbg();
		kandidat=new Kandidat(); // ako nece ovde premesti ga drugde!!!
		
		return "/kandidat/pronadjiKandidata.xhtml?faces-redirect=true";
	}
	
	// azurira podatke iz tabele Evidencija za samo jednog Kandidata
	public String azurirajKandidata2(){
		idKandidat=km.updateKandidat3(kandidat);
		if( (korisniciManagedBean.getInstruktor()!=null) && (korisniciManagedBean.getInstruktor().getAdmin()) ){
			porukaUcitanKand="Kandidat "+kandidat.getIme()+" "+kandidat.getPrezime()+" je uspešno ažuriran!";
			infoMessage(porukaUcitanKand);
			FacesContext context = FacesContext.getCurrentInstance();
			context.getExternalContext().getFlash().setKeepMessages(true);
			findKandidatsByImePrezimeJmbg();
			kandidat=new Kandidat();
			nazivKategorije="";
			prezimeInstruktora="";
			return "/kandidat/pronadjiKandidata.xhtml?faces-redirect=true";
		}else{
			porukaUcitanKand="Kandidat "+kandidat.getIme()+" "+kandidat.getPrezime()+" je uspešno ažuriran!";
			infoMessage(porukaUcitanKand);
			FacesContext context = FacesContext.getCurrentInstance();
			context.getExternalContext().getFlash().setKeepMessages(true);
			findKandidatsInstruktoraByImePrezimeJmbg(korisniciManagedBean.getInstruktor().getIdInstruktor());
			kandidat=new Kandidat();
			nazivKategorije="";
			prezimeInstruktora="";
			return "/instruktor/kandidatiInstruktora.xhtml?faces-redirect=true";
		}	
	}
	
	// brise kandidata iz baze i SAMO NJEGOVU evidenciju i zajedno s tim i sva njegova Polaganja i Prijave!!!
	public void obrisiKandidata(Kandidat kand){
		idKandidat = kand.getIdKandidat();
		String imeK = kand.getIme();
		String prezimeK = kand.getPrezime();
		boolean ok = km.obrisiKandidata2(idKandidat);
		if(ok){
			porukaObrisanK="Kandidat "+imeK+" "+prezimeK+" je uspešno obrisan!";
			infoMessage(porukaObrisanK);
			findKandidatsByImePrezimeJmbg();
		}else{
			porukaObrisanK="Kandidat NIJE obrisan!";
			errMessage(porukaObrisanK);
			findKandidatsByImePrezimeJmbg();
		}
	}
			
	public List<Kategorija> vratiKategorijeInstruktora(){
		kategorijas = instruktorManager.getKatsZaInstruktora2(instruktor);
		return kategorijas;
	}
	
	
	public List<String> vratiPunaImenaInstruktoraZaKat2(List<Instruktor> listaInstruktora){
		List<String> listaImenaInstruktora = new ArrayList<String>();
		for(Instruktor i:listaInstruktora){
			listaImenaInstruktora.add(i.toString());
		}
		return listaImenaInstruktora;
	}
	
	
	public List<Kandidat> findKandidatsByImePrezimeJmbg(){
		kandidats = km.getKandidatsByImePrezimeJmbg(imeKand,prezimeKand,jmbg);
		if(kandidats.size()<=0){
			porukaUcitanKand="Greška, NE POSTOJI kandidat za unete parametre!";
			return null;
		}else{
			return kandidats;
		}
	}
	
	// metoda kad se (neAdmin-ski) Instruktor uloguje da pretrazi SAMO SVOJE kandidate
	public List<Kandidat> findKandidatsInstruktoraByImePrezimeJmbg(int idInst){
		kandidatsInstruktora = km.getSviKandidatiInstruktora2(idInst,imeKand,prezimeKand,jmbg);
		if(kandidatsInstruktora.size()<=0){
			porukaUcitanKand="Greška, NE POSTOJI kandidat za unete parametre!";
			return null;
		}else{
			return kandidatsInstruktora;
		}
	}
	
	/* Parazit metod- BAD SMELL!!! */
	public String vratiNaKandidatHome(){
		return "/kandidat/kandidat.xhtml?faces-redirect=true";
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
	
	public Kandidat getKandidat() {
		return kandidat;
	}

	public void setKandidat(Kandidat kandidat) {
		this.kandidat = kandidat;
	}

	public KandidatManager getKm() {
		return km;
	}

	public void setKm(KandidatManager km) {
		this.km = km;
	}

	public int getIdKandidat() {
		return idKandidat;
	}

	public void setIdKandidat(int idKandidat) {
		this.idKandidat = idKandidat;
	}

	public int getIdEvidencija() {
		return idEvidencija;
	}

	public void setIdEvidencija(int idEvidencija) {
		this.idEvidencija = idEvidencija;
	}

	public int getIdKategorija() {
		return idKategorija;
	}

	public void setIdKategorija(int idKategorija) {
		this.idKategorija = idKategorija;
	}

	public int getIdInstruktor() {
		return idInstruktor;
	}

	public void setIdInstruktor(int idInstruktor) {
		this.idInstruktor = idInstruktor;
	}

	public String getImeKand() {
		return imeKand;
	}

	public void setImeKand(String imeKand) {
		this.imeKand = imeKand;
	}

	public String getPrezimeKand() {
		return prezimeKand;
	}

	public void setPrezimeKand(String prezimeKand) {
		this.prezimeKand = prezimeKand;
	}

	public String getJmbg() {
		return jmbg;
	}

	public void setJmbg(String jmbg) {
		this.jmbg = jmbg;
	}

	public Evidencija getEvidencija() {
		return evidencija;
	}

	public void setEvidencija(Evidencija evidencija) {
		this.evidencija = evidencija;
	}

	public Instruktor getInstruktor() {
		return instruktor;
	}

	public void setInstruktor(Instruktor instruktor) {
		this.instruktor = instruktor;
	}

	public Kategorija getKategorija() {
		return kategorija;
	}

	public void setKategorija(Kategorija kategorija) {
		this.kategorija = kategorija;
	}

	public String getPorukaKandidat() {
		return porukaKandidat;
	}

	public void setPorukaKandidat(String porukaKandidat) {
		this.porukaKandidat = porukaKandidat;
	}

	public String getNazivKategorije() {
		return nazivKategorije;
	}


	public void setNazivKategorije(String nazivKategorije) {
		this.nazivKategorije = nazivKategorije;
	}


	public String getPorukaUcitanKand() {
		return porukaUcitanKand;
	}

	public void setPorukaUcitanKand(String porukaUcitanKand) {
		this.porukaUcitanKand = porukaUcitanKand;
	}

	public EntityManager getEm() {
		return em;
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}

	public EvidencijaManager getEvidencijaManager() {
		return evidencijaManager;
	}

	public void setEvidencijaManager(EvidencijaManager evidencijaManager) {
		this.evidencijaManager = evidencijaManager;
	}

	public InstruktorManager getInstruktorManager() {
		return instruktorManager;
	}

	public void setInstruktorManager(InstruktorManager instruktorManager) {
		this.instruktorManager = instruktorManager;
	}

	public KategorijaManager getKategorijaManager() {
		return kategorijaManager;
	}

	public void setKategorijaManager(KategorijaManager kategorijaManager) {
		this.kategorijaManager = kategorijaManager;
	}

	public List<Kandidat> getKandidats() {
		return kandidats;
	}

	public void setKandidats(List<Kandidat> kandidats) {
		this.kandidats = kandidats;
	}

	public List<Kandidat> getKandidatsInstruktora() {
		return kandidatsInstruktora;
	}


	public void setKandidatsInstruktora(List<Kandidat> kandidatsInstruktora) {
		this.kandidatsInstruktora = kandidatsInstruktora;
	}


	public List<Kategorija> getKategorijas() {
		return kategorijas;
	}

	public void setKategorijas(List<Kategorija> kategorijas) {
		this.kategorijas = kategorijas;
	}

	public List<Instruktor> getInstruktors() {
		return instruktors;
	}

	public void setInstruktors(List<Instruktor> instruktors) {
		this.instruktors = instruktors;
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
	
	
	public List<String> getListaPunoImeInstruktora2() {
		return listaPunoImeInstruktora2;
	}

	public void setListaPunoImeInstruktora2(List<String> listaPunoImeInstruktora2) {
		this.listaPunoImeInstruktora2 = listaPunoImeInstruktora2;
	}

	public List<String> getListaNaziviKategorija2() {
		return listaNaziviKategorija2;
	}

	public void setListaNaziviKategorija2(List<String> listaNaziviKategorija2) {
		this.listaNaziviKategorija2 = listaNaziviKategorija2;
	}

	public String getBrLekUV() {
		return brLekUV;
	}

	public void setBrLekUV(String brLekUV) {
		this.brLekUV = brLekUV;
	}

	public Date getDatumIzdLu() {
		return datumIzdLu;
	}

	public void setDatumIzdLu(Date datumIzdLu) {
		this.datumIzdLu = datumIzdLu;
	}

	public Date getDatumUpisa() {
		return datumUpisa;
	}

	public void setDatumUpisa(Date datumUpisa) {
		this.datumUpisa = datumUpisa;
	}

	public String getPorukaObrisanK() {
		return porukaObrisanK;
	}

	public void setPorukaObrisanK(String porukaObrisanK) {
		this.porukaObrisanK = porukaObrisanK;
	}


	public String getPrezimeInstruktora() {
		return prezimeInstruktora;
	}


	public void setPrezimeInstruktora(String prezimeInstruktora) {
		this.prezimeInstruktora = prezimeInstruktora;
	}
	
//	public static void main(String [] args){
//		UnosKandidataManagedBean ukmb = new UnosKandidataManagedBean();
//		List<Kandidat> listKandsInst = ukmb.findKandidatsInstruktoraByImePrezimeJmbg(korisniciManagedBean);
//		for(Kandidat k:listKandsInst){
//			System.out.println("Kandidat "+k.getIme()+" "+k.getPrezime());
//		}
//		
//	}
	
}
