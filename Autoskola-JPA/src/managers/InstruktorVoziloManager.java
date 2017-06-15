package managers;

import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;

import model.Instruktor;
import model.Vozilo;

public class InstruktorVoziloManager {
	
	public void povezi(Instruktor i, Vozilo v){
		try {
			EntityManager em = JPAUtils.getEntityManager();
			em.getTransaction().begin();
//			i.getVozilos().clear();
//			i.getVozilos().add(v);			
			//v.getInstruktors().clear();
			System.out.println("v.getInstruktors() "+v.getInstruktors());
			v.getInstruktors().add(i);
			
//			em.merge(i);
//			em.refresh(i);
			em.merge(v);
			em.flush();
//			em.refresh(v);
			System.out.println("Merge-vao iNSTRUKTORA i vOZILO...");
			em.getTransaction().commit();	System.out.println("Prosao commit-ovanje...");			
			em.close();						System.out.println("Konekcija 'em' zatvorena!");			
		}catch(EntityExistsException eee){
			System.out.println("...upao u EntityExistsException!");
			eee.printStackTrace();
		} catch (Exception e) {
			System.out.println("Upao u obican Exception!");
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		try {
			InstruktorVoziloManager ivm = new InstruktorVoziloManager();	
			InstruktorManager im = new InstruktorManager();
			VoziloManager vm = new VoziloManager();
			Instruktor i = im.getInstruktorForId(1);
			
			System.out.println("Instruktor "+i.getIme()+" "+i.getPrezime()+", id: "+i.getIdInstruktor());
			Vozilo v = vm.getVoziloForId(3);
			
			System.out.println("Vozilo marke: "+v.getMarka()+", model: "+v.getModel()+", njegov id: "+v.getIdVozila());
			ivm.povezi(i,v);
			System.out.println("Instruktor nakon povezivanja instruktora "+i.getIdInstruktor()+" i vozila id:"+v.getIdVozila());
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.exit(0);
	}
}