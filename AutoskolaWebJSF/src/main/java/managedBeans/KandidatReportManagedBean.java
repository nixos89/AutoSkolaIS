package managedBeans;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import managers.JPAUtils;
import managers.KandidatManager;
import managers.KategorijaManager;
import managers.PolaganjeManager;
import managers.PrijavaManager;
import model.Instruktor;
import model.Kandidat;
import model.Kategorija;
import model.Polaganje;
import model.Prijava;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@ManagedBean(name="kandidatReportManagedBean")
@SessionScoped
public class KandidatReportManagedBean {
	private HashMap<String, Object> params;
	private JREmptyDataSource emptyDataSource;
	private JRDataSource dataSource;
	private ServletContext context;
	private JasperPrint jasperPrint;
	private String reportsDirectory;
	private String jasperFile;
	private HttpServletResponse response;
	private KandidatManager kandidatManager;
	private PolaganjeManager polaganjeManager;
	private PrijavaManager prijavaManager;
	private KategorijaManager kategorijaManager;
	private Kategorija kategorija;
	private String naziv1;
	private String naziv2;
	private String naziv3;
	private int idKategorije;
	private Date datumOd1;
	private Date datumDo1;
	private Date datumOd2;
	private Date datumDo2;
	private Date datumOd3;
	private Date datumDo3;
	private Date datumOd4;
	private Date datumDo4;
	private Date datumOd5;
	private Date datumDo5;
	private List<Kategorija> listaKategorija;
	
	
	/* ==================================================================== */
	/* ======================= GET i SET metode =========================== */
	/* ==================================================================== */
	
	public int getIdKategorije() {
		return idKategorije;
	}

	public void setIdKategorije(int idKategorije) {
		this.idKategorije = idKategorije;
	}
	
	public Kategorija getKategorija() {
		return kategorija;
	}

	public void setKategorija(Kategorija kategorija) {
		this.kategorija = kategorija;
	}

	public String getNaziv1() {
		return naziv1;
	}

	public void setNaziv1(String naziv1) {
		this.naziv1 = naziv1;
	}
	
	public String getNaziv2() {
		return naziv2;
	}

	public void setNaziv2(String naziv2) {
		this.naziv2 = naziv2;
	}
	
	public String getNaziv3() {
		return naziv3;
	}

	public void setNaziv3(String naziv3) {
		this.naziv3 = naziv3;
	}

	public Date getDatumOd1() {
		return datumOd1;
	}

	public void setDatumOd1(Date datumOd1) {
		this.datumOd1 = datumOd1;
	}

	public Date getDatumDo1() {
		return datumDo1;
	}

	public void setDatumDo1(Date datumDo1) {
		this.datumDo1 = datumDo1;
	}
	
	public Date getDatumOd2() {
		return datumOd2;
	}

	public void setDatumOd2(Date datumOd2) {
		this.datumOd2 = datumOd2;
	}

	public Date getDatumDo2() {
		return datumDo2;
	}

	public void setDatumDo2(Date datumDo2) {
		this.datumDo2 = datumDo2;
	}

	public Date getDatumOd3() {
		return datumOd3;
	}

	public void setDatumOd3(Date datumOd3) {
		this.datumOd3 = datumOd3;
	}

	public Date getDatumDo3() {
		return datumDo3;
	}

	public void setDatumDo3(Date datumDo3) {
		this.datumDo3 = datumDo3;
	}

	public Date getDatumOd4() {
		return datumOd4;
	}

	public void setDatumOd4(Date datumOd4) {
		this.datumOd4 = datumOd4;
	}

	public Date getDatumDo4() {
		return datumDo4;
	}

	public void setDatumDo4(Date datumDo4) {
		this.datumDo4 = datumDo4;
	}

	public Date getDatumOd5() {
		return datumOd5;
	}

	public void setDatumOd5(Date datumOd5) {
		this.datumOd5 = datumOd5;
	}

	public Date getDatumDo5() {
		return datumDo5;
	}

	public void setDatumDo5(Date datumDo5) {
		this.datumDo5 = datumDo5;
	}

	
	public List<Kategorija> getListaKategorija() {
		return listaKategorija;
	}

	public void setListaKategorija(List<Kategorija> listaKategorija) {
		this.listaKategorija = listaKategorija;
	}
	/* ==================================================================== */
	
	
	// konstruktor
	public KandidatReportManagedBean(){
		params = new HashMap();
		kandidatManager = new KandidatManager();
		polaganjeManager = new PolaganjeManager();
		prijavaManager = new PrijavaManager();
		kategorijaManager = new KategorijaManager();
		listaKategorija = kategorijaManager.vratiSveKategorije();
	}
	
	/* Glavni metod za GENERISANJE izvestaja */
	public void generateReport(String reportType) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		try {
			ExternalContext externalContext = facesContext.getExternalContext();
			context = (ServletContext) externalContext.getContext();
			reportsDirectory = context.getRealPath("/")+"WEB-INF/classes/jasper/";
			response = (HttpServletResponse) externalContext.getResponse();
			
			if (reportType.equals("kandidatiPrijaviliTestoveZaDatum")) {
				EntityManager em = JPAUtils.getEntityManager();
				params.put("naziv", naziv1);
				params.put("datumOd", datumOd2);
				params.put("datumDo", datumDo2);
				List<Prijava> data = prijavaManager.
						getPrijaveKategorijeZaDatum(naziv1, datumOd2, datumDo2);
				jasperFile = reportsDirectory + "kandidatiPrijaviliTestove.jasper";
				if (data.isEmpty()) {
					jasperPrint = JasperFillManager.fillReport(jasperFile, params, emptyDataSource);
				} else {
					dataSource = new JRBeanCollectionDataSource(data);
					jasperPrint = JasperFillManager.fillReport(jasperFile, params, dataSource);
				}
			}
			if (reportType.equals("kandidatiZaKategoriju")) {
				EntityManager em = JPAUtils.getEntityManager();
				kategorija = em.find(Kategorija.class, idKategorije);
				params.put("kategorija", kategorija);
				params.put("idKategorije", idKategorije);
				List<Kandidat> data = kandidatManager.getKandidatiZaKategoriju2(idKategorije);
				jasperFile = reportsDirectory + "sviKandidatiKategorije.jasper";
				if (data.isEmpty()) {
					jasperPrint = JasperFillManager.fillReport(jasperFile, params, emptyDataSource);
				} else {
					dataSource = new JRBeanCollectionDataSource(data);
					jasperPrint = JasperFillManager.fillReport(jasperFile, params, dataSource);
				}
			}	
			if (reportType.equals("kandidatiUpisaniZaDatum")) {
				EntityManager em = JPAUtils.getEntityManager();
				kategorija = em.find(Kategorija.class, idKategorije);
				params.put("naziv", naziv1);
				params.put("datumOd", datumOd1);
				params.put("datumDo", datumDo1);
				List<Kandidat> data = kandidatManager.getKandidatiUpisaniZaDatum(datumOd1, datumDo1);
				jasperFile = reportsDirectory + "kandidatiUpisaniZaDatum.jasper";
				if (data.isEmpty()) {
					jasperPrint = JasperFillManager.fillReport(jasperFile, params, emptyDataSource);
				} else {
					dataSource = new JRBeanCollectionDataSource(data);
					jasperPrint = JasperFillManager.fillReport(jasperFile, params, dataSource);
				}
			}
			
			
			if (reportType.equals("kandidatiPrijaviliVoznjuZaDatum")) {
				EntityManager em = JPAUtils.getEntityManager();
				params.put("naziv", naziv3);
				params.put("datumOd", datumOd5);
				params.put("datumDo", datumDo5);
				List<Prijava> data = prijavaManager.getPrijaveVoznjeKategorijeZaDatum(naziv3, datumOd5, datumDo5);
				jasperFile = reportsDirectory + "kandidatiPrijaviliVoznju.jasper";
				if (data.isEmpty()) {
					jasperPrint = JasperFillManager.fillReport(jasperFile, params, emptyDataSource);
				} else {
					dataSource = new JRBeanCollectionDataSource(data);
					jasperPrint = JasperFillManager.fillReport(jasperFile, params, dataSource);
				}
			}
			
			
			
			if (reportType.equals("kandidatPoloziliTestoveZaDatum")) {
				EntityManager em = JPAUtils.getEntityManager();
				kategorija = em.find(Kategorija.class, idKategorije);
				params.put("datumOd", datumOd3);
				params.put("datumDo", datumDo3);
				List<Polaganje> data = polaganjeManager.getPolaganjaTestovaZaDatum(datumOd3,datumDo3);
				jasperFile = reportsDirectory + "kandidatiPoloziliTestoveZaDatum.jasper";
				if (data.isEmpty()) {
					jasperPrint = JasperFillManager.fillReport(jasperFile, params, emptyDataSource);
				} else {
					dataSource = new JRBeanCollectionDataSource(data);
					jasperPrint = JasperFillManager.fillReport(jasperFile, params, dataSource);
				}
			}
			if (reportType.equals("kandidatiPoloziliVoznjuZaDatum")) {
				EntityManager em = JPAUtils.getEntityManager();
				kategorija = em.find(Kategorija.class, idKategorije);
				params.put("naziv", naziv2);
				params.put("datumOd", datumOd4);
				params.put("datumDo", datumDo4);
				List<Kandidat> data = kandidatManager.polozenaVoznjaZaKategoriju(naziv2,datumOd4,datumDo4);
				jasperFile = reportsDirectory + "kandidatiPoloziliVoznjuZaDatum.jasper";
				if (data.isEmpty()) {
					jasperPrint = JasperFillManager.fillReport(jasperFile, params, emptyDataSource);
				} else {
					dataSource = new JRBeanCollectionDataSource(data);
					jasperPrint = JasperFillManager.fillReport(jasperFile, params, dataSource);
				}
			}
		
			ServletOutputStream servletOutputStream = response.getOutputStream();
			response.setContentType("application/pdf");
			JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
			servletOutputStream.flush();
			servletOutputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			facesContext.responseComplete();
		}
	}
	
	
}
