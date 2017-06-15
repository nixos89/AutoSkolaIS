package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the polaganje database table.
 * 
 */
@Entity
@Table(name="Polaganje")
@NamedQuery(name="Polaganje.findAll", query="SELECT p FROM Polaganje p")
public class Polaganje implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID_POLAGANJA")
	private int idPolaganja;

	private int bodovipi;

	private int bodoviti;

	@Temporal(TemporalType.DATE)
	private Date datumpolaganjapi;

	@Temporal(TemporalType.DATE)
	private Date datumpolaganjati;

	private boolean polozenpi;

	private boolean polozenti;

	//bi-directional many-to-one association to Evidencija
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="ID_EVIDENCIJA")
	private Evidencija evidencija;

	public Polaganje() {
	}

	public int getIdPolaganja() {
		return this.idPolaganja;
	}

	public void setIdPolaganja(int idPolaganja) {
		this.idPolaganja = idPolaganja;
	}

	public int getBodovipi() {
		return this.bodovipi;
	}

	public void setBodovipi(int bodovipi) {
		this.bodovipi = bodovipi;
	}

	public int getBodoviti() {
		return this.bodoviti;
	}

	public void setBodoviti(int bodoviti) {
		this.bodoviti = bodoviti;
	}

	public Date getDatumpolaganjapi() {
		return this.datumpolaganjapi;
	}

	public void setDatumpolaganjapi(Date datumpolaganjapi) {
		this.datumpolaganjapi = datumpolaganjapi;
	}

	public Date getDatumpolaganjati() {
		return this.datumpolaganjati;
	}

	public void setDatumpolaganjati(Date datumpolaganjati) {
		this.datumpolaganjati = datumpolaganjati;
	}

	public boolean getPolozenpi() {
		return this.polozenpi;
	}

	public void setPolozenpi(boolean polozenpi) {
		this.polozenpi = polozenpi;
	}

	public boolean getPolozenti() {
		return this.polozenti;
	}

	public void setPolozenti(boolean polozenti) {
		this.polozenti = polozenti;
	}

	public Evidencija getEvidencija() {
		return this.evidencija;
	}

	public void setEvidencija(Evidencija evidencija) {
		this.evidencija = evidencija;
	}

}