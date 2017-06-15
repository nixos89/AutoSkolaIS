package customBeans;

import model.Instruktor;

public class InstruktorBrojKandidataBean {
	
	private Instruktor instruktor;
	private int brKandidata;
	
	public InstruktorBrojKandidataBean(){}
	
	
	public InstruktorBrojKandidataBean(Instruktor i, int brK){
		instruktor=i;
		brKandidata=brK;
	}


	public Instruktor getInstruktor() {
		return instruktor;
	}


	public void setInstruktor(Instruktor instruktor) {
		this.instruktor = instruktor;
	}


	public int getBrKandidata() {
		return brKandidata;
	}


	public void setBrKandidata(int brKandidata) {
		this.brKandidata = brKandidata;
	}
	
}
