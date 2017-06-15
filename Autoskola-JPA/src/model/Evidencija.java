package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the evidencija database table.
 * 
 */
@Entity
@Table(name="Evidencija")
@NamedQuery(name="Evidencija.findAll", query="SELECT e FROM Evidencija e")
public class Evidencija implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID_EVIDENCIJA")
	private int idEvidencija;

	@Column(name="BR_LEKAR_UVERENJA")
	private String brLekarUverenja;

	@Temporal(TemporalType.DATE)
	@Column(name="DATUM_IZDAVANJA_LU")
	private Date datumIzdavanjaLu;

	@Temporal(TemporalType.DATE)
	private Date datumupisa;

	//bi-directional many-to-one association to Kandidat
	@ManyToOne(cascade={CascadeType.ALL},fetch=FetchType.EAGER) // SVE je NAKNADNO dodato! - Promenuto sa 'MERGE' na 'ALL' !!!
	@JoinColumn(name="ID_KANDIDAT")
	private Kandidat kandidat;

	//bi-directional many-to-one association to Kandidat
	@OneToMany(mappedBy="evidencija")
	private List<Kandidat> kandidats;

	//bi-directional many-to-one association to Polaganje
	@OneToMany(mappedBy="evidencija", cascade={CascadeType.MERGE}, fetch=FetchType.EAGER)
	private List<Polaganje> polaganjes;

	//bi-directional many-to-one association to Prijava
	@OneToMany(mappedBy="evidencija", cascade={CascadeType.MERGE}, fetch=FetchType.EAGER)
	private List<Prijava> prijavas;

	public Evidencija() {
	}

	public int getIdEvidencija() {
		return this.idEvidencija;
	}

	public void setIdEvidencija(int idEvidencija) {
		this.idEvidencija = idEvidencija;
	}

	public String getBrLekarUverenja() {
		return this.brLekarUverenja;
	}

	public void setBrLekarUverenja(String brLekarUverenja) {
		this.brLekarUverenja = brLekarUverenja;
	}

	public Date getDatumIzdavanjaLu() {
		return this.datumIzdavanjaLu;
	}

	public void setDatumIzdavanjaLu(Date datumIzdavanjaLu) {
		this.datumIzdavanjaLu = datumIzdavanjaLu;
	}

	public Date getDatumupisa() {
		return this.datumupisa;
	}

	public void setDatumupisa(Date datumupisa) {
		this.datumupisa = datumupisa;
	}

	public Kandidat getKandidat() {
		return this.kandidat;
	}

	public void setKandidat(Kandidat kandidat) {
		this.kandidat = kandidat;
	}

	public List<Kandidat> getKandidats() {
		return this.kandidats;
	}

	public void setKandidats(List<Kandidat> kandidats) {
		this.kandidats = kandidats;
	}

	public Kandidat addKandidat(Kandidat kandidat) {
		getKandidats().add(kandidat);
		kandidat.setEvidencija(this);

		return kandidat;
	}

	public Kandidat removeKandidat(Kandidat kandidat) {
		getKandidats().remove(kandidat);
		kandidat.setEvidencija(null);
		return kandidat;
	}

	public List<Polaganje> getPolaganjes() {
		return this.polaganjes;
	}

	public void setPolaganjes(List<Polaganje> polaganjes) {
		this.polaganjes = polaganjes;
	}

	public Polaganje addPolaganje(Polaganje polaganje) {
		getPolaganjes().add(polaganje);
		polaganje.setEvidencija(this);

		return polaganje;
	}

	public Polaganje removePolaganje(Polaganje polaganje) {
		getPolaganjes().remove(polaganje);
		polaganje.setEvidencija(null);

		return polaganje;
	}

	public List<Prijava> getPrijavas() {
		return this.prijavas;
	}

	public void setPrijavas(List<Prijava> prijavas) {
		this.prijavas = prijavas;
	}

	public Prijava addPrijava(Prijava prijava) {
		getPrijavas().add(prijava);
		prijava.setEvidencija(this);

		return prijava;
	}

	public Prijava removePrijava(Prijava prijava) {
		getPrijavas().remove(prijava);
		prijava.setEvidencija(null);

		return prijava;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idEvidencija;
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
		Evidencija other = (Evidencija) obj;
		if (idEvidencija != other.idEvidencija)
			return false;
		return true;
	}
	
	

}