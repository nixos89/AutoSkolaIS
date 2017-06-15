package model;

import java.io.Serializable;
import javax.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the vozilo database table.
 * 
 */
@Entity
@Table(name="Vozilo")
@NamedQuery(name="Vozilo.findAll", query="SELECT v FROM Vozilo v")
public class Vozilo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID_VOZILA")
	private int idVozila;

	@Column(name="BR_REGISTRACIJE")
	private String brRegistracije;

	@Temporal(TemporalType.DATE)
	private Date godiste;

	private String gorivo;

	private String marka;

	@Column(name="MESECNA_KILOMETRAZA")
	private float mesecnaKilometraza;

	@Column(name="MESECNA_POTROSNJA")
	private float mesecnaPotrosnja;

	private String model;

	@Temporal(TemporalType.DATE)
	@Column(name="ROK_REGISTRACIJE")
	private Date rokRegistracije;
	
	@Temporal(TemporalType.DATE)
	@Column(name="TEHNICKI_PREGLED")
	private Date tehnickiPregled;

	@Column(name="TROSKOVI_ODRZAVANJA")
	private float troskoviOdrzavanja;

	//bi-directional many-to-many association to Instruktor
	@ManyToMany(fetch=FetchType.EAGER) // promenjeno sa 'LAZY' na 'EAGER' i uklonjen cacadeType.MERGE !!!
	@JoinTable(
		name="instruktorvozilo"
		, joinColumns={
			@JoinColumn(name="ID_VOZILA")
			}
		, inverseJoinColumns={
			@JoinColumn(name="ID_INSTRUKTOR")
			}
		)
	private List<Instruktor> instruktors;

	//bi-directional many-to-one association to Kategorija
	@ManyToOne/*(fetch=FetchType.EAGER)*/ // nakndadno dodato fetch=FetchType.EAGER!!!
	@JoinColumn(name="ID_KATEGORIJA")
	private Kategorija kategorija;

	/* konstruktor */
	public Vozilo() {
		instruktors = new ArrayList<Instruktor>();
	}
	
	@Override
	public String toString(){
		return marka+" "+model; 
	}
	
	

	/* --------------------------------------------------- */
	/* ------------GET i SET metode ----------------- */
	/* --------------------------------------------------- */
	
	
	public int getIdVozila() {
		return this.idVozila;
	}

	public void setIdVozila(int idVozila) {
		this.idVozila = idVozila;
	}

	public String getBrRegistracije() {
		return this.brRegistracije;
	}

	public void setBrRegistracije(String brRegistracije) {
		this.brRegistracije = brRegistracije;
	}

	public Date getGodiste() {
		return this.godiste;
	}

	public void setGodiste(Date godiste) {
		this.godiste = godiste;
	}

	public String getGorivo() {
		return this.gorivo;
	}

	public void setGorivo(String gorivo) {
		this.gorivo = gorivo;
	}

	public String getMarka() {
		return this.marka;
	}

	public void setMarka(String marka) {
		this.marka = marka;
	}

	public float getMesecnaKilometraza() {
		return this.mesecnaKilometraza;
	}

	public void setMesecnaKilometraza(float mesecnaKilometraza) {
		this.mesecnaKilometraza = mesecnaKilometraza;
	}

	public float getMesecnaPotrosnja() {
		return this.mesecnaPotrosnja;
	}

	public void setMesecnaPotrosnja(float mesecnaPotrosnja) {
		this.mesecnaPotrosnja = mesecnaPotrosnja;
	}

	public String getModel() {
		return this.model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public Date getRokRegistracije() {
		return this.rokRegistracije;
	}

	public void setRokRegistracije(Date rokRegistracije) {
		this.rokRegistracije = rokRegistracije;
	}
	
	public Date getTehnickiPregled() {
		return tehnickiPregled;
	}

	public void setTehnickiPregled(Date tehnickiPregled) {
		this.tehnickiPregled = tehnickiPregled;
	}

	public float getTroskoviOdrzavanja() {
		return this.troskoviOdrzavanja;
	}

	public void setTroskoviOdrzavanja(float troskoviOdrzavanja) {
		this.troskoviOdrzavanja = troskoviOdrzavanja;
	}

	public List<Instruktor> getInstruktors() {
		return this.instruktors;
	}

	public void setInstruktors(List<Instruktor> instruktors) {
		this.instruktors = instruktors;
	}

	public Kategorija getKategorija() {
		return this.kategorija;
	}

	public void setKategorija(Kategorija kategorija) {
		this.kategorija = kategorija;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idVozila;
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
		Vozilo other = (Vozilo) obj;
		if (idVozila != other.idVozila)
			return false;
		return true;
	}
	
	

}