package managedBeans;

import java.io.IOException;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMessages;

import managers.JPAUtils;
import managers.KategorijaManager;
import model.Kategorija;

@ManagedBean(name="unosKategorijeManagedBean")
@ViewScoped
public class UnosKategorijeManagedBean {
	
	private KategorijaManager kategorijaManager;
	private Kategorija kategorija;
	private int idKategorija;
	private List<Kategorija> kategorijas;
	private EntityManager em;
	private String poruka;
	
	public UnosKategorijeManagedBean(){
		kategorijaManager = new KategorijaManager();
		kategorija = new Kategorija();
		em = JPAUtils.getEntityManager();
		poruka="";
		kategorijas=null;
		idKategorija=0;
	}
	
	public String sacuvajKategoriju() throws IOException{
		int idKategorija = new KategorijaManager().saveKategorija(kategorija);
		kategorija = kategorijaManager.getKategorijaForId2(idKategorija);
		if(idKategorija==0){
			poruka="Kategorija NIJE sacuvana!";	
			errMessage(poruka);
			FacesContext context = FacesContext.getCurrentInstance();
			context.getExternalContext().getFlash().setKeepMessages(true);
			findKategorijas();
			UIViewRoot view = FacesContext.getCurrentInstance().getViewRoot();
			kategorija.setNaziv(""); // naknadno dodato u 20:47, 11.10.2016.
			return view.getViewId() + "/kategorija/unosKategorije.xhtml?faces-redirect=true";
		}else{
			poruka="Kategorija "+kategorija.getNaziv()+" je sacuvana, njen id:"+kategorija.getIdKategorija();
			saveMessage(poruka);
			FacesContext context = FacesContext.getCurrentInstance();
			context.getExternalContext().getFlash().setKeepMessages(true);
			findKategorijas();
			UIViewRoot view = FacesContext.getCurrentInstance().getViewRoot();
			kategorija.setNaziv(""); 
			return view.getViewId() + "/kategorija/unosKategorije.xhtml?faces-redirect=true";
		}
		
	}
	
	public String obrisiKategoriju(Kategorija kat){
		idKategorija=kat.getIdKategorija();
		boolean ok = kategorijaManager.deleteKategorija(idKategorija);
		if(ok){
			poruka = "Kategorija je uspešno obrisana!";
			infoMessage(poruka);
			FacesContext context = FacesContext.getCurrentInstance();
			context.getExternalContext().getFlash().setKeepMessages(true);
			findKategorijas();
			UIViewRoot view = FacesContext.getCurrentInstance().getViewRoot();
			kategorija.setNaziv(""); 
			return view.getViewId() + "/kategorija/unosKategorije.xhtml?faces-redirect=true";
		}else{
			poruka = "Kategorija NIJE obrisana!";
			errMessage(poruka);
			FacesContext context = FacesContext.getCurrentInstance();
			context.getExternalContext().getFlash().setKeepMessages(true);
			findKategorijas();
			UIViewRoot view = FacesContext.getCurrentInstance().getViewRoot();
			kategorija.setNaziv(""); 
			return view.getViewId() + "/kategorija/unosKategorije.xhtml?faces-redirect=true";
		}		
	}
	
	
	public List<Kategorija> findKategorijas(){
		kategorijas = kategorijaManager.vratiSveKategorije();
		return kategorijas;
	}
	
	/* ------------------------------------------------------------------------ */
	/* -------- metode za izbacivanje poruka rezultujucih akcija CRUD-a ------- */
	/* ------------------------------------------------------------------------ */	
	
	public void saveMessage(String poruka) {
		FacesContext context = FacesContext.getCurrentInstance();

		context.addMessage(null, new FacesMessage("Obaveštenje", poruka));
	}

	public void infoMessage(String poruka) {
		FacesContext context = FacesContext.getCurrentInstance();

		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Obaveštenje", poruka));

	}

	public void errMessage(String poruka) {
		FacesContext context = FacesContext.getCurrentInstance();

		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Greška", poruka));

	}
	
	
	/* ------------------------------------------------------------ */
						/*    GET i SET metode  */
	/* ------------------------------------------------------------ */

	public KategorijaManager getKategorijaManager() {
		return kategorijaManager;
	}

	public void setKategorijaManager(KategorijaManager kategorijaManager) {
		this.kategorijaManager = kategorijaManager;
	}

	public Kategorija getKategorija() {
		return kategorija;
	}

	public void setKategorija(Kategorija kategorija) {
		this.kategorija = kategorija;
	}

	public int getIdKategorija() {
		return idKategorija;
	}

	public void setIdKategorija(int idKategorija) {
		this.idKategorija = idKategorija;
	}

	public EntityManager getEm() {
		return em;
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}

	public String getPoruka() {
		return poruka;
	}

	public void setPoruka(String poruka) {
		this.poruka = poruka;
	}

	public List<Kategorija> getKategorijas() {
		return kategorijas;
	}

	public void setKategorijas(List<Kategorija> kategorijas) {
		this.kategorijas = kategorijas;
	}
	
	
}
