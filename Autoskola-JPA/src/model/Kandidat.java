package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the kandidat database table.
 * 
 */
@Entity
@Table(name="Kandidat")
@NamedQuery(name="Kandidat.findAll", query="SELECT k FROM Kandidat k")
public class Kandidat implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID_KANDIDAT")
	private int idKandidat;

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

	//bi-directional many-to-one association to Evidencija
	@OneToMany(mappedBy="kandidat")
	private List<Evidencija> evidencijas;

	//bi-directional many-to-one association to Evidencija
	@ManyToOne(cascade={CascadeType.ALL},fetch=FetchType.EAGER) // sve je dodato (fetch malo kasnije)-Promenuto sa 'MERGE' na 'ALL' !!!
	@JoinColumn(name="ID_EVIDENCIJA")
	private Evidencija evidencija;

	//bi-directional many-to-one association to Instruktor
	@ManyToOne
	@JoinColumn(name="ID_INSTRUKTOR")
	private Instruktor instruktor;

	//bi-directional many-to-one association to Kategorija
	@ManyToOne
	@JoinColumn(name="ID_KATEGORIJA")
	private Kategorija kategorija;

	public Kandidat() {
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idKandidat;
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
		Kandidat other = (Kandidat) obj;
		if (idKandidat != other.idKandidat)
			return false;
		return true;
	}
	
	@Override
	public String toString(){
		return ime+" "+prezime+", JMBG: "+jmbg;
	}
	
	public int getIdKandidat() {
		return this.idKandidat;
	}

	public void setIdKandidat(int idKandidat) {
		this.idKandidat = idKandidat;
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

	public List<Evidencija> getEvidencijas() {
		return this.evidencijas;
	}

	public void setEvidencijas(List<Evidencija> evidencijas) {
		this.evidencijas = evidencijas;
	}

	public Evidencija addEvidencija(Evidencija evidencija) {
		getEvidencijas().add(evidencija);
		evidencija.setKandidat(this);

		return evidencija;
	}

	public Evidencija removeEvidencija(Evidencija evidencija) {
		getEvidencijas().remove(evidencija);
		evidencija.setKandidat(null);

		return evidencija;
	}

	public Evidencija getEvidencija() {
		return this.evidencija;
	}

	public void setEvidencija(Evidencija evidencija) {
		this.evidencija = evidencija;
	}

	public Instruktor getInstruktor() {
		return this.instruktor;
	}

	public void setInstruktor(Instruktor instruktor) {
		this.instruktor = instruktor;
	}

	public Kategorija getKategorija() {
		return this.kategorija;
	}

	public void setKategorija(Kategorija kategorija) {
		this.kategorija = kategorija;
	}

}