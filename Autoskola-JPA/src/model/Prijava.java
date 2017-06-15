package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the prijava database table.
 * 
 */
@Entity
@Table(name="Prijava")
@NamedQuery(name="Prijava.findAll", query="SELECT p FROM Prijava p")
public class Prijava implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID_PRIJAVA")
	private int idPrijava;

	@Temporal(TemporalType.DATE)
	private Date datumprijavepispita;

	@Temporal(TemporalType.DATE)
	private Date datumprijavetispita;

	//bi-directional many-to-one association to Evidencija
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="ID_EVIDENCIJA")
	private Evidencija evidencija;

	public Prijava() {
	}

	public int getIdPrijava() {
		return this.idPrijava;
	}

	public void setIdPrijava(int idPrijava) {
		this.idPrijava = idPrijava;
	}

	public Date getDatumprijavepispita() {
		return this.datumprijavepispita;
	}

	public void setDatumprijavepispita(Date datumprijavepispita) {
		this.datumprijavepispita = datumprijavepispita;
	}

	public Date getDatumprijavetispita() {
		return this.datumprijavetispita;
	}

	public void setDatumprijavetispita(Date datumprijavetispita) {
		this.datumprijavetispita = datumprijavetispita;
	}

	public Evidencija getEvidencija() {
		return this.evidencija;
	}

	public void setEvidencija(Evidencija evidencija) {
		this.evidencija = evidencija;
	}

}