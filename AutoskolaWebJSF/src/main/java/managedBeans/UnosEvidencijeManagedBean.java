package managedBeans;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;

import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;

import managers.EvidencijaManager;
import managers.JPAUtils;
import managers.KandidatManager;
import managers.PolaganjeManager;
import managers.PrijavaManager;
import model.Evidencija;
import model.Kandidat;
import model.Polaganje;
import model.Prijava;
 

@ManagedBean(name="unosEvidencijeManagedBean")
@SessionScoped // promenjeno na ViewScoped sa SessionScoped
public class UnosEvidencijeManagedBean {
	
	/* INJECT-ovanje jednog ManagedBean-a u drugi */
	@ManagedProperty(value="#{korisniciManagedBean}")
	private KorisniciManagedBean korisniciManagedBean;
		
	public void setKorisniciManagedBean(KorisniciManagedBean korisniciManagedBean) {
		this.korisniciManagedBean = korisniciManagedBean;
	}
	
	private int idEvidencija;
	private int idKandidata;
	private Evidencija evidencija;
	private Kandidat kandidat;
	private Prijava prijava;
	private Polaganje polaganje;
	private EvidencijaManager evm;
	private KandidatManager kandidatManager;
	private PolaganjeManager polaganjeManager;
	private PrijavaManager prijavaManager;
	private List<Kandidat> kandidati;
	private List<Prijava> listaPrijava;
	private List<Polaganje> listaPolaganja;
	/*----------------------------------------*/
	private Date datumPrijaveTI;
	private Date datumPrijavePI;
	private Date datumPolaganjaTI;
	private Date datumPolaganjaPI;
	private Date datumIzdLu;
	private Date datumUpisa;
	private int poeniTI;
	private int poeniPI;
	private boolean polozioTI;
	private boolean polozioPI;
	/*----------------------------------------*/
	private String poruka;
	private String porukaSacuvanaPrijava;
	private String porukaObrisanaPrijava;
	private String porukaSacuvanoPolaganje;
	private String porukaObrisanoPolaganje;
	private String polozeniTestovi;
	private String polozenaVoznja;
	private String brLekUV;
	
	
	public UnosEvidencijeManagedBean(){
		evidencija= new Evidencija();
		kandidat = new Kandidat();
		prijava = new Prijava();
		polaganje = new Polaganje();
		kandidatManager = new KandidatManager();
		evm = new EvidencijaManager();
		idEvidencija = 0;
		poeniTI=0;
		poeniPI=0;
		polozioTI=false;
		polozioPI=false;
		kandidati=new ArrayList<Kandidat>();
		listaPrijava = new ArrayList<Prijava>();
		listaPolaganja = new ArrayList<Polaganje>();
		datumPrijaveTI=null;
		datumPrijavePI=null;
		datumPolaganjaTI=null;
		datumPolaganjaPI=null;
		poruka="";
		polozeniTestovi="";
		polozenaVoznja="";
		porukaSacuvanaPrijava="";
		porukaObrisanaPrijava="";
		porukaSacuvanoPolaganje="";
		porukaObrisanoPolaganje="";
	}
	
	/* Treba da odvede korisnika na stranicu vezanu za evidenciju (PRIJAVA/POLAGANJE)
	 * ODREDJENOG kandidata i zavodi datum prijave teorijskog/prakticnog ispita i
	 * i ostvarene bodove na ispitima i DA LI je polozio ILI nije!!! - ova metoda je ANALOGNA
	 * sa metodama "nadjiInstruktora()" i "nadjiKandidata()" */
	public String zavediEvidenciju(int idKand){
		EntityManager em = JPAUtils.getEntityManager();
		evidencija = kandidatManager.getEvidencijaForId(idKand);
		idEvidencija = evidencija.getIdEvidencija();
		evidencija = em.find(Evidencija.class, idEvidencija);
		kandidat = kandidatManager.getKandidatByEvidencija(evidencija);
		brLekUV = evidencija.getBrLekarUverenja();
		datumIzdLu=evidencija.getDatumIzdavanjaLu();
		datumUpisa=evidencija.getDatumupisa();
		return "/kandidat/azurirajEvidencijuKandidata.xhtml?faces-redirect=true";
	}
	
	
	public String azurirajEvidenciju(/*int idKand*/){
		EntityManager em = JPAUtils.getEntityManager();
		idEvidencija = evm.updateEvidencija2(evidencija); 
		return "/kandidat/pronadjiKandidata.xhtml?faces-redirect=true";
	}
	
	public String azurirajEvidenciju2(){
		EntityManager em = JPAUtils.getEntityManager();
		idEvidencija = evm.updateEvidencija2(evidencija);
		kandidat = evidencija.getKandidat();
		if( (korisniciManagedBean.getInstruktor()!=null) && (korisniciManagedBean.getInstruktor().getAdmin()) ){
			poruka ="Evidencija kandidata "+kandidat.getIme()+" "+kandidat.getPrezime()+" je uspešno ažurirana!";
			infoMessage(poruka);
			FacesContext context = FacesContext.getCurrentInstance();
			context.getExternalContext().getFlash().setKeepMessages(true);
			evidencija =new Evidencija();
			return "/kandidat/pronadjiKandidata.xhtml?faces-redirect=true";
		}else{
			poruka ="Evidencija kandidata "+kandidat.getIme()+" "+kandidat.getPrezime()+" je uspešno ažurirana!";
			infoMessage(poruka);
			FacesContext context = FacesContext.getCurrentInstance();
			context.getExternalContext().getFlash().setKeepMessages(true);
			evidencija =new Evidencija();
			return "/instruktor/kandidatiInstruktora.xhtml?faces-redirect=true";
		}
	}
	
	/* ---------------------------------------------------------------------------------*/
	/* ------------------------------- metode za PRIJAVA -------------------------------*/
	/* ---------------------------------------------------------------------------------*/
	
	public List<Prijava> findAllPrijavas(int idEvid){
		idEvidencija = idEvid;
		listaPrijava = evm.vratiSvePrijaveZaIdEvidencije2(idEvidencija);
		return listaPrijava;
	}
	
	public List<Prijava> prijaveZaKandidata(){
		List<Prijava> listaPrijavaKand = findAllPrijavas(idEvidencija);
		return listaPrijavaKand;
	}
	
	public String zavediPrijavu(int idKand){
		Evidencija ev = kandidatManager.getEvidencijaForId(idKand);
		idEvidencija = ev.getIdEvidencija();
		evidencija = evm.getEvidencijaForId(idEvidencija);
		kandidat = kandidatManager.getKandidatByEvidencija(evidencija);
		/* mozda treba ubaciti onaj metod dodeliPrijavu(Evidencija ev, Prijava prijava)
		 * iz EvidencijaManager klase... */
		prijaveZaKandidata();
		return "/kandidat/prijava.xhtml?faces-redirect=true";
	}
	

	
	
	public void sacuvajPrijavu(){
		prijava.setDatumprijavetispita(datumPrijaveTI);
		prijava.setDatumprijavepispita(datumPrijavePI);
		int idPrijava = evm.savePrijava(prijava,evidencija);
		if(idPrijava==0){
			porukaSacuvanaPrijava = "Prijava sa id: "+prijava.getIdPrijava()+" je uspesno sacuvana!";
			infoMessage(porukaSacuvanaPrijava);
		}else{
			porukaSacuvanaPrijava = "Greska, prijava NIJE sacuvana!";
			errMessage(porukaSacuvanaPrijava);
		}		
	}
	
	/* ----------------------------------------------------------------------------------- */
	/* ----------------------------------------------------------------------------------- */
	
	/* ---------------- METODE ZA Polaganje ---------------- */
	
	public List<Polaganje> findAllPolaganjas(int idEvid){
		idEvidencija = idEvid;
		listaPolaganja = evm.vratiSvaPolaganjaZaIdEvidencije2(idEvidencija);
		return listaPolaganja;
	}
	
	public List<Polaganje> polaganjaOdKandidata(){
		List<Polaganje> listaPolaganjaKand = findAllPolaganjas(idEvidencija);
		return listaPolaganjaKand;
	}
	
	public String zavediPolaganje(int idKand){
		Evidencija ev = kandidatManager.getEvidencijaForId(idKand);
		idEvidencija = ev.getIdEvidencija();
		evidencija = evm.getEvidencijaForId(idEvidencija);
		kandidat = kandidatManager.getKandidatByEvidencija(evidencija);
		polaganjaOdKandidata();
		return "/kandidat/polaganje.xhtml?faces-redirect=true";	
	}
	
	public String zavediPolaganje2(int idKand){
		Evidencija ev = kandidatManager.getEvidencijaForId(idKand);
		idEvidencija = ev.getIdEvidencija();
		evidencija = evm.getEvidencijaForId(idEvidencija);
		kandidat = kandidatManager.getKandidatByEvidencija(evidencija);
		polaganjaOdKandidata();
		return "/kandidat/polaganjaSamoKandidata.xhtml?faces-redirect=true";
			
	}
	
	// proverava da li je kandidat prijavio testove da se zna DA LI se SME OCENITI testovi!!!
	public boolean testoviKandidataSeSmejuPrijavitiOceniti(Kandidat kand){
		List<Prijava> prijavaKand = kand.getEvidencija().getPrijavas();
		Calendar c = new GregorianCalendar();
		c.add(Calendar.DATE, 30);
		Date datumZaPrijavuTestova=c.getTime();
		for(Prijava p:prijavaKand){
			if(p.getDatumprijavetispita().after(datumZaPrijavuTestova))
				return true;
		}
		return false;
	}
	
	
	// proverava da li odredjeni kandidat polozio testove
	public boolean kandidatPaoTestove(Kandidat kand){
		List <Polaganje> polaganjaKand = kand.getEvidencija().getPolaganjes();
		for(Polaganje p: polaganjaKand){
			if(p.getPolozenti())
				return false;
		}
		return true;
	}

	
	public void sacuvajPolaganje(){
		// set-uj SVE parametre za Prijavu (Date, boolean, int)
		polaganje.setDatumpolaganjati(datumPolaganjaTI);
		polaganje.setBodoviti(poeniTI);
		polaganje.setPolozenti(polozioTI);
		polaganje.setDatumpolaganjapi(datumPolaganjaPI);
		polaganje.setBodovipi(poeniPI);
		polaganje.setPolozenpi(polozioPI);		
		int idPolaganja = evm.savePolaganje(polaganje,evidencija);
		if(idPolaganja==0){
			porukaSacuvanoPolaganje = "Polaganje sa id: "+polaganje.getIdPolaganja()+" je uspešno saèuvano!";
			infoMessage(porukaSacuvanoPolaganje);
		}else{
			porukaSacuvanoPolaganje = "Greška, polaganje NIJE saèuvano!";
			errMessage(porukaSacuvanoPolaganje);
		}		
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
	
	/*-------------------------------------------------------*/
	/*------------------ EDIT Datatable metode --------------*/ 
	/*-------------------------------------------------------*/
	
	public void onRowEdit(RowEditEvent event) {
		FacesMessage msg = new FacesMessage("Prijava promenuta"/*, ((Prijava) event.getObject()).getIdPrijava()*/);
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}
	     
	 public void onRowCancel(RowEditEvent event) {
		 FacesMessage msg = new FacesMessage("Edit Cancelled"/*, ((Prijava) event.getObject()).getIdPrijava()*/);
		 FacesContext.getCurrentInstance().addMessage(null, msg);
	 }
	
	 public void onCellEdit(CellEditEvent event) {
		 Object oldValue = event.getOldValue();
		 Object newValue = event.getNewValue();

		 if(newValue != null && !newValue.equals(oldValue)) {
			 FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Vrednost æelije je promenuta", "Stara: " + oldValue + ", Nova:" + newValue);
			 FacesContext.getCurrentInstance().addMessage(null, msg);
		 }
	 }
	  /* ----------------------- Završetak EDIT Datatable metoda! ------------------- */
	 /* ---------------------------------------------------------------------------- */
	 
	
	 /*-------------------------------------------------------*/
	 /*-------------------- GET i SET metode -----------------*/ 
	 /*-------------------------------------------------------*/
	
	public Evidencija getEvidencija() {
		return evidencija;
	}

	public void setEvidencija(Evidencija evidencija) {
		this.evidencija = evidencija;
	}

	public int getIdEvidencija() {
		return idEvidencija;
	}

	public void setIdEvidencija(int idEvidencija) {
		this.idEvidencija = idEvidencija;
	}

	public int getIdKandidata() {
		return idKandidata;
	}

	public void setIdKandidata(int idKandidata) {
		this.idKandidata = idKandidata;
	}

	public EvidencijaManager getEvm() {
		return evm;
	}

	public void setEvm(EvidencijaManager evm) {
		this.evm = evm;
	}

	public Kandidat getKandidat() {
		return kandidat;
	}

	public void setKandidat(Kandidat kandidat) {
		this.kandidat = kandidat;
	}

	public Prijava getPrijava() {
		return prijava;
	}

	public void setPrijava(Prijava prijava) {
		this.prijava = prijava;
	}

	public Polaganje getPolaganje() {
		return polaganje;
	}

	public void setPolaganje(Polaganje polaganje) {
		this.polaganje = polaganje;
	}

	public PolaganjeManager getPolaganjeManager() {
		return polaganjeManager;
	}

	public void setPolaganjeManager(PolaganjeManager polaganjeManager) {
		this.polaganjeManager = polaganjeManager;
	}

	public PrijavaManager getPrijavaManager() {
		return prijavaManager;
	}

	public void setPrijavaManager(PrijavaManager prijavaManager) {
		this.prijavaManager = prijavaManager;
	}
	

	public List<Kandidat> getKandidati() {
		return kandidati;
	}

	public void setKandidati(List<Kandidat> kandidati) {
		this.kandidati = kandidati;
	}

	public List<Prijava> getListaPrijava() {
		return listaPrijava;
	}

	public void setListaPrijava(List<Prijava> listaPrijava) {
		this.listaPrijava = listaPrijava;
	}

	public List<Polaganje> getListaPolaganja() {
		return listaPolaganja;
	}

	public void setListaPolaganja(List<Polaganje> listaPolaganja) {
		this.listaPolaganja = listaPolaganja;
	}

	public KandidatManager getKandidatManager() {
		return kandidatManager;
	}

	public void setKandidatManager(KandidatManager kandidatManager) {
		this.kandidatManager = kandidatManager;
	}

	public Date getDatumPrijaveTI() {
		return datumPrijaveTI;
	}

	public void setDatumPrijaveTI(Date datumPrijaveTI) {
		this.datumPrijaveTI = datumPrijaveTI;
	}

	public Date getDatumPrijavePI() {
		return datumPrijavePI;
	}

	public void setDatumPrijavePI(Date datumPrijavePI) {
		this.datumPrijavePI = datumPrijavePI;
	}

	public Date getDatumPolaganjaTI() {
		return datumPolaganjaTI;
	}

	public void setDatumPolaganjaTI(Date datumPolaganjaTI) {
		this.datumPolaganjaTI = datumPolaganjaTI;
	}

	public Date getDatumPolaganjaPI() {
		return datumPolaganjaPI;
	}

	public void setDatumPolaganjaPI(Date datumPolaganjaPI) {
		this.datumPolaganjaPI = datumPolaganjaPI;
	}

	public int getPoeniTI() {
		return poeniTI;
	}

	public void setPoeniTI(int poeniTI) {
		this.poeniTI = poeniTI;
	}

	public int getPoeniPI() {
		return poeniPI;
	}

	public void setPoeniPI(int poeniPI) {
		this.poeniPI = poeniPI;
	}

	public boolean isPolozioTI() {
		return polozioTI;
	}

	public void setPolozioTI(boolean polozioTI) {
		this.polozioTI = polozioTI;
	}

	public boolean isPolozioPI() {
		return polozioPI;
	}

	public void setPolozioPI(boolean polozioPI) {
		this.polozioPI = polozioPI;
	}

	public String getPorukaSacuvanaPrijava() {
		return porukaSacuvanaPrijava;
	}

	public void setPorukaSacuvanaPrijava(String porukaSacuvanaPrijava) {
		this.porukaSacuvanaPrijava = porukaSacuvanaPrijava;
	}

	public String getPorukaObrisanaPrijava() {
		return porukaObrisanaPrijava;
	}

	public void setPorukaObrisanaPrijava(String porukaObrisanaPrijava) {
		this.porukaObrisanaPrijava = porukaObrisanaPrijava;
	}

	public String getPorukaSacuvanoPolaganje() {
		return porukaSacuvanoPolaganje;
	}

	public void setPorukaSacuvanoPolaganje(String porukaSacuvanoPolaganje) {
		this.porukaSacuvanoPolaganje = porukaSacuvanoPolaganje;
	}

	public String getPorukaObrisanoPolaganje() {
		return porukaObrisanoPolaganje;
	}

	public void setPorukaObrisanoPolaganje(String porukaObrisanoPolaganje) {
		this.porukaObrisanoPolaganje = porukaObrisanoPolaganje;
	}

	public String getPolozeniTestovi() {
		return polozeniTestovi;
	}

	public void setPolozeniTestovi(String polozeniTestovi) {
		this.polozeniTestovi = polozeniTestovi;
	}

	public String getPolozenaVoznja() {
		return polozenaVoznja;
	}

	public void setPolozenaVoznja(String polozenaVoznja) {
		this.polozenaVoznja = polozenaVoznja;
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

	public String getPoruka() {
		return poruka;
	}

	public void setPoruka(String poruka) {
		this.poruka = poruka;
	}

	public String getBrLekUV() {
		return brLekUV;
	}

	public void setBrLekUV(String brLekUV) {
		this.brLekUV = brLekUV;
	}
	
}
