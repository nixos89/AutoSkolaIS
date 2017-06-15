package controllers;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

@ManagedBean(name="navigacioniKontroler")
@RequestScoped
public class NavigacioniKontroler implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/* ManagedProperty: 1) polje koje je instanca drugog ManagedBean-a 
	 * ili predstavlja neku drugu promenljivu iz veb opsega
	 * 2) managed bean's property can be injected in another managed bean. */
	@ManagedProperty(value="#{param.idStranice}")
	private String idStranice;

	public String getIdStranice() {
		return idStranice;
	}

	public void setIdStranice(String idStranice) {
		this.idStranice = idStranice;
	}
	
	public String prikaziGlavneStranice(){
		if(idStranice==null){
			return "admin.xhtml?faces-redirect=true";
		}else if(idStranice.equals("Instruktor")){
			return "/instruktor/instruktor.xhtml?faces-redirect=true";
		}else if(idStranice.equals("Kandidat")){
			return "/kandidat/kandidat.xhtml?faces-redirect=true";				
		}else if(idStranice.equals("Kategorija")){
			return "/kategorija/unosKategorije.xhtml?faces-redirect=true";				
		}else if(idStranice.equals("Vozilo")){
			return "/vozilo/vozilo.xhtml?faces-redirect=true";	
		}else{
			return "admin.xhtml?faces-redirect=true";
		}
	}
	
	public String prikaziSekretaricineStranice(){
		if(idStranice==null){
			return "sekretarica.xhtml?faces-redirect=true";
		}else if(idStranice.equals("Instruktor")){
			return "/instruktor/instruktor.xhtml?faces-redirect=true";
		}else if(idStranice.equals("Kandidat")){
			return "/kandidat/kandidat.xhtml?faces-redirect=true";				
		}else if(idStranice.equals("Vozilo")){
			return "/vozilo/vozilo.xhtml?faces-redirect=true";	
		}else{
			return "sekretarica.xhtml?faces-redirect=true";
		}
	}
	
	// prikazi ovde stranice vezane za Instruktora
	public String prikaziInstruktorStranice(){
		if(idStranice==null){
			return "/instruktor/instruktor.xhtml?faces-redirect=true";
		}else if(idStranice.equals("dodajInstruktora")){
			return "/instruktor/unosInstruktora.xhtml?faces-redirect=true";
		}else if(idStranice.equals("Pronadji instruktora")){
			return "/instruktor/pronadjiInstruktora.xhtml?faces-redirect=true";	
		}else if(idStranice.equals("Izvestaji instruktora")){
			return "/izvestaji/izvestajInstruktora.xhtml?faces-redirect=true";
		}else{
			return "/instruktor/instruktor.xhtml?faces-redirect=true";
		}
	}
	
	// prikazi ovde stranice vezane za Kandidata
	public String prikaziKandidatStranice(){
		if(idStranice==null){
			return "/kandidat/kandidat.xhtml?faces-redirect=true";
		}else if(idStranice.equals("Dodaj kandidata")){
			return "/kandidat/unosKandidata.xhtml?faces-redirect=true";
		}else if(idStranice.equals("Pronadji kandidata")){
			return "/kandidat/pronadjiKandidata.xhtml?faces-redirect=true";
		}else if(idStranice.equals("Izvestaji kandidata")){
			return "/izvestaji/izvestajiKandidata.xhtml?faces-redirect=true";
		}else{
			return "/kandidat/kandidat.xhtml?faces-redirect=true";
		}
	}
	
	// prikazi ovde stranice vezane za Vozilo
	public String prikaziVoziloStranice(){
		if(idStranice==null){
			return "/vozilo/vozilo.xhtml?faces-redirect=true";
		}else if(idStranice.equals("Dodaj vozilo")){
			return "/vozilo/unosVozila.xhtml?faces-redirect=true";
		}else if(idStranice.equals("Nadji vozilo")){
			return "/vozilo/pronadjiVozilo.xhtml?faces-redirect=true";	
		}else{
			return "/vozilo/vozilo.xhtml?faces-redirect=true";
		}
	}
	
	
	
	
}
