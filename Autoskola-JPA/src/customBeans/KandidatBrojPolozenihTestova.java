package customBeans;

import model.Kandidat;

public class KandidatBrojPolozenihTestova {
	
	private Kandidat kandidat;
	private int brPolozenihTestova;
	
	public KandidatBrojPolozenihTestova(){}
	
	public KandidatBrojPolozenihTestova(Kandidat kandidat, int brPT){
		super();
		this.kandidat=kandidat;
		brPolozenihTestova=brPT;
	}

	public Kandidat getKandidat() {
		return kandidat;
	}

	public void setKandidat(Kandidat kandidat) {
		this.kandidat = kandidat;
	}

	public int getBrPolozenihTestova() {
		return brPolozenihTestova;
	}

	public void setBrPolozenihTestova(int brPolozenihTestova) {
		this.brPolozenihTestova = brPolozenihTestova;
	}

}
