package customBeans;

import model.Kandidat;

public class KandidatKategorijaBean {
	
	private Kandidat kandidat;
	private String nazivKategorije;
	
	public KandidatKategorijaBean(){
		
	}
	
	public KandidatKategorijaBean(Kandidat kandidat,String nazivKategorije){
		super();
		this.kandidat=kandidat;
		this.nazivKategorije=nazivKategorije;
	}

	public Kandidat getKandidat() {
		return kandidat;
	}

	public void setKandidat(Kandidat kandidat) {
		this.kandidat = kandidat;
	}

	public String getNazivKategorije() {
		return nazivKategorije;
	}

	public void setNazivKategorije(String nazivKategorije) {
		this.nazivKategorije = nazivKategorije;
	}
	
	
}
