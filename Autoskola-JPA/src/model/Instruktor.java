package model;

import java.io.Serializable;
import javax.persistence.*;
import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the instruktor database table.
 * 
 */
@Entity
@Table(name="Instruktor")
@NamedQuery(name="Instruktor.findAll", query="SELECT i FROM Instruktor i")
public class Instruktor implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID_INSTRUKTOR")
	private int idInstruktor;

	@Column(name="BR_LICNEKARTE")
	private String brLicnekarte;

	private String broj;

	@Temporal(TemporalType.DATE)
	private Date datumrodjenja;

	private String email;

	private String ime;

	private String jmbg;

	private String lozinka;

	private String mesto;

	private String prezime;

	private String telefon;

	private String ulica;
	
	private boolean admin; // NOVO dodato polje!!!

	// NEMOJ gubiti vreme da ovo isto radis i za Vozilo.java, jer je vec ova funkcionalnost implementirana
	//bi-directional many-to-many association to Kategorija
	@ManyToMany(mappedBy="instruktors", fetch=FetchType.EAGER, cascade={CascadeType.MERGE}) // dodat CascadeType.MERGE
	private List<Kategorija> kategorijas;

	//bi-directional many-to-many association to Vozilo
	@ManyToMany(mappedBy="instruktors", fetch=FetchType.EAGER,cascade={CascadeType.MERGE}) // dodat CascadeType.MERGE
	private List<Vozilo> vozilos;

	//bi-directional many-to-one association to Kandidat
	@OneToMany(mappedBy="instruktor", fetch=FetchType.EAGER, cascade={CascadeType.MERGE})
	private List<Kandidat> kandidats;

	/* konstruktor */
	public Instruktor() {
		kategorijas = new ArrayList<Kategorija>();
		vozilos = new ArrayList<Vozilo>();
//		kandidats = new ArrayList<Kandidat>(); // NEMOJ koristiti, nema presecne tabele
	}
	
	@Override
	public String toString(){
		return ime+" "+prezime+", JMBG: "+jmbg;
	}
	
	public int getIdInstruktor() {
		return this.idInstruktor;
	}

	public void setIdInstruktor(int idInstruktor) {
		this.idInstruktor = idInstruktor;
	}

	public String getBrLicnekarte() {
		return this.brLicnekarte;
	}
	
	
	public void setBrLicnekarte(String brLicnekarte) {
		this.brLicnekarte = brLicnekarte;
	}

	public String getBroj() {
		return this.broj;
	}

	public void setBroj(String broj) {
		this.broj = broj;
	}

	public Date getDatumrodjenja() {
		return this.datumrodjenja;
	}

	public void setDatumrodjenja(Date datumrodjenja) {
		this.datumrodjenja = datumrodjenja;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getIme() {
		return this.ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public String getJmbg() {
		return this.jmbg;
	}

	public void setJmbg(String jmbg) {
		this.jmbg = jmbg;
	}

	public String getLozinka() {
		return this.lozinka;
	}

	public void setLozinka(String lozinka) {
		this.lozinka = lozinka;
	}

	public String getMesto() {
		return this.mesto;
	}

	public void setMesto(String mesto) {
		this.mesto = mesto;
	}

	public String getPrezime() {
		return this.prezime;
	}

	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}

	public String getTelefon() {
		return this.telefon;
	}

	public void setTelefon(String telefon) {
		this.telefon = telefon;
	}

	public String getUlica() {
		return this.ulica;
	}

	public void setUlica(String ulica) {
		this.ulica = ulica;
	}
	
	
	public boolean getAdmin(){
		return this.admin;
	}
	
	public void setAdmin(boolean admin){
		this.admin = admin;
	}
	

	public List<Kategorija> getKategorijas() {
		return this.kategorijas;
	}

	public void setKategorijas(List<Kategorija> kategorijas) {
		this.kategorijas = kategorijas;
	}

	public List<Vozilo> getVozilos() {
		return this.vozilos;
	}

	public void setVozilos(List<Vozilo> vozilos) {
		this.vozilos = vozilos;
	}

	public List<Kandidat> getKandidats() {
		return this.kandidats;
	}

	public void setKandidats(List<Kandidat> kandidats) {
		this.kandidats = kandidats;
	}

	public Kandidat addKandidat(Kandidat kandidat) {
		getKandidats().add(kandidat);
		kandidat.setInstruktor(this);

		return kandidat;
	}

	public Kandidat removeKandidat(Kandidat kandidat) {
		getKandidats().remove(kandidat);
		kandidat.setInstruktor(null);

		return kandidat;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idInstruktor;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Instruktor other = (Instruktor) obj;
		if (idInstruktor != other.idInstruktor)
			return false;
		return true;
	}
	
}