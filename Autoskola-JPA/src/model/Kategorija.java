package model;

import java.io.Serializable;
import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;


/**
 * The persistent class for the kategorija database table.
 * 
 */
@Entity
@Table(name="Kategorija")
@NamedQuery(name="Kategorija.findAll", query="SELECT k FROM Kategorija k")
public class Kategorija implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID_KATEGORIJA")
	private int idKategorija;

	private String naziv;

	//bi-directional many-to-many association to Instruktor
	@ManyToMany(fetch=FetchType.EAGER) // promenuto sa EAGER na LAZY !!!
	@JoinTable(
		name="instruktorkategorija"
		, joinColumns={
			@JoinColumn(name="ID_KATEGORIJA")
			}
		, inverseJoinColumns={
			@JoinColumn(name="ID_INSTRUKTOR")
			}
		)
	private List<Instruktor> instruktors;

	//bi-directional many-to-one association to Kandidat
	@OneToMany(mappedBy="kategorija")
	private List<Kandidat> kandidats;

	//bi-directional many-to-one association to Vozilo
	@OneToMany(mappedBy="kategorija")
	private List<Vozilo> vozilos;

	/* konstruktor */
	public Kategorija() {
		instruktors = new ArrayList<Instruktor>();
	}

	@Override
	public String toString(){
		return naziv;
	}
	
	//naknadno su dodati hashCode() i equals() (by Nikola)!!!
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idKategorija;
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
		Kategorija other = (Kategorija) obj;
		if (idKategorija != other.idKategorija)
			return false;
		return true;
	}

	public int getIdKategorija() {
		return this.idKategorija;
	}

	public void setIdKategorija(int idKategorija) {
		this.idKategorija = idKategorija;
	}

	public String getNaziv() {
		return this.naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public List<Instruktor> getInstruktors() {
		return this.instruktors;
	}

	public void setInstruktors(List<Instruktor> instruktors) {
		this.instruktors = instruktors;
	}

	public List<Kandidat> getKandidats() {
		return this.kandidats;
	}

	public void setKandidats(List<Kandidat> kandidats) {
		this.kandidats = kandidats;
	}

	public Kandidat addKandidat(Kandidat kandidat) {
		getKandidats().add(kandidat);
		kandidat.setKategorija(this);

		return kandidat;
	}

	public Kandidat removeKandidat(Kandidat kandidat) {
		getKandidats().remove(kandidat);
		kandidat.setKategorija(null);

		return kandidat;
	}

	public List<Vozilo> getVozilos() {
		return this.vozilos;
	}

	public void setVozilos(List<Vozilo> vozilos) {
		this.vozilos = vozilos;
	}

	public Vozilo addVozilo(Vozilo vozilo) {
		getVozilos().add(vozilo);
		vozilo.setKategorija(this);

		return vozilo;
	}

	public Vozilo removeVozilo(Vozilo vozilo) {
		getVozilos().remove(vozilo);
		vozilo.setKategorija(null);

		return vozilo;
	}

}