package customBeans;

import model.Kandidat;

public class KandidatBrojPalihIspita {
	
	private Kandidat kandidat;
	private int brPalihIspita;
	
	public KandidatBrojPalihIspita(){}
	
	
	public KandidatBrojPalihIspita(Kandidat kandidat, int brPalihIspita){
		this.kandidat=kandidat;
		this.brPalihIspita=brPalihIspita;
	}
	
	public Kandidat getKandidat() {
		return kandidat;
	}

	public void setKandidat(Kandidat kandidat) {
		this.kandidat = kandidat;
	}

	public int getBrPalihIspita() {
		return brPalihIspita;
	}

	public void setBrPalihIspita(int brPalihIspita) {
		this.brPalihIspita = brPalihIspita;
	}
	
}
