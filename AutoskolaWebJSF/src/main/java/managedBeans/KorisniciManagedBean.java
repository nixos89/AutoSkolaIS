package managedBeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import managers.InstruktorManager;
import managers.KandidatManager;
import model.Instruktor;
import model.Kandidat;

@ManagedBean(name="korisniciManagedBean")
@SessionScoped // promenjeno sa @SessionScoped na @ViewScoped
public class KorisniciManagedBean implements Serializable{
	

	private String user;
	private String pass;
	private String tipKorisnika;
	private String porukaLogIn;
	private Instruktor instruktor;
	private Kandidat kandidat;
	private int idKandidat;
	private int idInstruktor;
	
	private boolean administrator;
	
	public KorisniciManagedBean(){
		kandidat = new Kandidat();
		instruktor = new Instruktor();
		idKandidat = 0;
		idInstruktor = 0;
		porukaLogIn="";
		administrator=false;
	}//KorisniciManagedBean
	
	
	/* Metoda za PRIJAVLJIVANJE na sistem! */
	public String logIn(){
		if(tipKorisnika.equals("instruktor")){
			instruktor = new InstruktorManager().getInstruktorByUserAndPass(user, pass);
			idInstruktor = instruktor.getIdInstruktor();
			if((instruktor!=null) && (instruktor.getAdmin())){
				return "admin.xhtml?faces-redirect=true";
			}else if((instruktor!=null) && (!instruktor.getAdmin())){
				return "/instruktor/samoInstruktor.xhtml?faces-redirect=true";
			}else{
				porukaLogIn="Greška, pogrešno korisnièko ime i/ili lozinka!";
				errMessage(porukaLogIn);
				return "index.xhtml?faces-redirect=true";
			}	
		}else if(tipKorisnika.equals("kandidat")){
			kandidat = new KandidatManager().getKandidatByUserAndPass(user, pass);
			idKandidat = kandidat.getIdKandidat();
			if(idKandidat!=0){ // uslov promenjeno sa "kandidat!=null" na "idKandidat!=0" 
				return "/kandidat/samoKandidat.xhtml?faces-redirect=true";
			}else
				porukaLogIn="Greška, pogrešno korisnièko ime i/ili lozinka!";
				errMessage(porukaLogIn);
				return "index.xhtml?faces-redirect=true";
		}else if(tipKorisnika.equals("")){
			porukaLogIn="Greška, molimo Vas odaberite koji ste tip korisnika!";
			errMessage(porukaLogIn);
			return porukaLogIn;
		}else{
			return "index.xhtml?faces-redirect=true";
		}	
	}//logIn
	
	
	/* Metoda za ODJAVLJIVANJE sa sistema! */
	public String logOut(){
		instruktor = new Instruktor(); //bilo je da je null sa desne strane znaka = (jednakosti)
		kandidat = new Kandidat(); //bilo je da je null sa desne strane znaka = (jednakosti)
		return "/index.xhtml?faces-redirect=true";
	}
	
	// metod za GROWL!!!
	public void saveMessage(){
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage("", porukaLogIn));
	}
	
	// 
	public void errMessage(String poruka){
		FacesContext context = FacesContext.getCurrentInstance();
        
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Greska",  poruka) );
		
	}
	
	/* ----------------------------------------------------- */
	/* ----------------- GET i SET metode ------------------ */
	/* ----------------------------------------------------- */

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getTipKorisnika() {
		return tipKorisnika;
	}


	public void setTipKorisnika(String tipKorisnika) {
		this.tipKorisnika = tipKorisnika;
	}

	public String getPorukaLogIn() {
		return porukaLogIn;
	}

	public void setPorukaLogIn(String porukaLogIn) {
		this.porukaLogIn = porukaLogIn;
	}

	public Instruktor getInstruktor() {
		return instruktor;
	}

	public void setInstruktor(Instruktor instruktor) {
		this.instruktor = instruktor;
	}

	public Kandidat getKandidat() {
		return kandidat;
	}

	public void setKandidat(Kandidat kandidat) {
		this.kandidat = kandidat;
	}


	public int getIdKandidat() {
		return idKandidat;
	}


	public void setIdKandidat(int idKandidat) {
		this.idKandidat = idKandidat;
	}


	public int getIdInstruktor() {
		return idInstruktor;
	}


	public void setIdInstruktor(int idInstruktor) {
		this.idInstruktor = idInstruktor;
	}

	// modifikovan GET metod kako bi ustanovili da li Instruktor-Korisnik ima ADMIN privilegiju
	public boolean isAdministrator() {
		if(instruktor.getAdmin())
			return true;
		else
			return false;
//		return administrator;
	}

	public void setAdministrator(boolean administrator) {
		this.administrator = administrator;
	}
	
	
	
}
