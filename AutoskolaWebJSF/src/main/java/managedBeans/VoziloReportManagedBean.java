package managedBeans;

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

import customBeans.VoziloBrojInstruktoraBean;
import managers.JPAUtils;
import managers.KandidatManager;
import managers.KategorijaManager;
import managers.VoziloManager;
import model.Instruktor;
import model.Kandidat;
import model.Kategorija;
import model.Vozilo;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@ManagedBean
@ApplicationScoped
public class VoziloReportManagedBean {
	private HashMap<String, Object> params;
	private JREmptyDataSource emptyDataSource;
	private JRDataSource dataSource;
	private ServletContext context;
	private JasperPrint jasperPrint;
	private String reportsDirectory;
	private String jasperFile;
	private HttpServletResponse response;
	private KandidatManager kandidatManager;
	private KategorijaManager kategorijaManager;
	private VoziloManager voziloManager;
	private Kategorija kategorija;
	private String naziv;
	private int idKategorije;
	private List<Kategorija> listaKategorija;
	
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

	public List<Kategorija> getListaKategorija() {
		return listaKategorija;
	}

	public void setListaKategorija(List<Kategorija> listaKategorija) {
		this.listaKategorija = listaKategorija;
	}
	

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public VoziloReportManagedBean(){
		params = new HashMap();
		kandidatManager = new KandidatManager();
		kategorijaManager = new KategorijaManager();
		voziloManager = new VoziloManager();
		listaKategorija = kategorijaManager.vratiSveKategorije();
	}
	
	
	public void generateReport(String reportType) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		try {
			ExternalContext externalContext = facesContext.getExternalContext();
			context = (ServletContext) externalContext.getContext();
			reportsDirectory = context.getRealPath("/")+"WEB-INF/classes/jasper/";
			response = (HttpServletResponse) externalContext.getResponse();
			if (reportType.equals("maxKmVozilaZaKat")) {
				params.put("idKategorije", idKategorije);
				List<Vozilo> data = voziloManager.getVozilaKategorijeZaMaxKilometraza(idKategorije);
				jasperFile = reportsDirectory + "maxKmVozilaKat.jasper";
				if (data.isEmpty()) {
					jasperPrint = JasperFillManager.fillReport(jasperFile, params, emptyDataSource);
				} else {
					dataSource = new JRBeanCollectionDataSource(data);
					jasperPrint = JasperFillManager.fillReport(jasperFile, params, dataSource);
				}
			}	
			if (reportType.equals("voziloSaMaxInstruktora")) {
				List<VoziloBrojInstruktoraBean> data = voziloManager.vozilaSaMaxInstruktora();
				jasperFile = reportsDirectory + "voziloSaMaxInstruktora.jasper";
				if (data.isEmpty()) {
					jasperPrint = JasperFillManager.fillReport(jasperFile, params, emptyDataSource);
				} else {
					dataSource = new JRBeanCollectionDataSource(data);
					jasperPrint = JasperFillManager.fillReport(jasperFile, params, dataSource);
				}
			}
			
			
			if (reportType.equals("vozilaTroskoviOdrzavanja")) {
				params.put("naziv", naziv);
				List<Vozilo> data = voziloManager.getVozilaKategorijeZaTroskoveOdrzavanja(naziv);
				jasperFile = reportsDirectory + "vozilaTroskoviOdzavanjaMinMax.jasper";
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
