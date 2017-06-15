package customBeans;

import model.Vozilo;

public class VoziloBrojInstruktoraBean {
	private Vozilo vozilo;
	private int brInstruktora;
	
	public VoziloBrojInstruktoraBean(){
		
	}
	
	public VoziloBrojInstruktoraBean(Vozilo vozilo, int brInstruktora){
		super();
		this.vozilo=vozilo;
		this.brInstruktora=brInstruktora;
	}

	public Vozilo getVozilo() {
		return vozilo;
	}

	public void setVozilo(Vozilo vozilo) {
		this.vozilo = vozilo;
	}

	public int getBrInstruktora() {
		return brInstruktora;
	}

	public void setBrInstruktora(int brInstruktora) {
		this.brInstruktora = brInstruktora;
	}
	
}