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

import customBeans.InstruktorBrojKandidataBean;
import managers.InstruktorManager;
import managers.JPAUtils;
import managers.KandidatManager;
import model.Instruktor;
import model.Kandidat;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@ManagedBean(name="instruktorReportManagedBean")
@SessionScoped
public class InstruktorReportManagedBean {
	private HashMap<String, Object> params;
	private JREmptyDataSource emptyDataSource;
	private JRDataSource dataSource;
	private ServletContext context;
	private JasperPrint jasperPrint;
	private String reportsDirectory;
	private String jasperFile;
	private HttpServletResponse response;
	private InstruktorManager im;
	private KandidatManager km;
	private int idInstruktora;
	private List<Instruktor> listaInstruktora;
	private String naziv;
	private Date datumOd;
	private Date datumDo;
	
	/* ++++++++++++++++++++++++++++++++++++++ */
	/* +++++++++ GET i SET metode ++++++++ */
	/* ++++++++++++++++++++++++++++++++++++++ */

	
	public int getIdInstruktora() {
		return idInstruktora;
	}

	public void setIdInstruktora(int idInstruktora) {
		this.idInstruktora = idInstruktora;
	}

	public List<Instruktor> getListaInstruktora() {
		return listaInstruktora;
	}
	
	public void setListaInstruktora(List<Instruktor> listaInstruktora) {
		this.listaInstruktora = listaInstruktora;
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public Date getDatumOd() {
		return datumOd;
	}

	public void setDatumOd(Date datumOd) {
		this.datumOd = datumOd;
	}

	public Date getDatumDo() {
		return datumDo;
	}

	public void setDatumDo(Date datumDo) {
		this.datumDo = datumDo;
	}

	// konstruktor
	public InstruktorReportManagedBean(){
		params = new HashMap();
		im = new InstruktorManager();
		km = new KandidatManager();
		listaInstruktora= im.getSviInstruktori();
	}
	
	// gl. metod za generisanje izvestaja
	public void generateReport(String reportType) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		try {
			ExternalContext externalContext = facesContext.getExternalContext();
			context = (ServletContext) externalContext.getContext();
			reportsDirectory = context.getRealPath("/")+"WEB-INF/classes/jasper/";
			response = (HttpServletResponse) externalContext.getResponse();
			
			if (reportType.equals("instruktorMaxKandKat")) {
				params.put("naziv", naziv);
				params.put("datumOd", datumOd);
				params.put("datumDo", datumDo);
				List<InstruktorBrojKandidataBean> data = im.getInstruktoriSaMaxKandidataZaKat(naziv,datumOd,datumDo);
				jasperFile = reportsDirectory + "instruktoriMaxKandidataKategorije.jasper"; // jasper fajl
				if (data.isEmpty()) {
					jasperPrint = JasperFillManager.fillReport(jasperFile, params, emptyDataSource);
				} else {
					dataSource = new JRBeanCollectionDataSource(data);
					jasperPrint = JasperFillManager.fillReport(jasperFile, params, dataSource);
				}
			}
			
			if (reportType.equals("kandidatiInstruktoraKategorijeZaDatum")) {
				List<Instruktor> data = im.getSviInstruktori();
				jasperFile = reportsDirectory + "sviInstruktori.jasper";
				if (data.isEmpty()) {
					jasperPrint = JasperFillManager.fillReport(jasperFile, params, emptyDataSource);
				} else {
					dataSource = new JRBeanCollectionDataSource(data);
					jasperPrint = JasperFillManager.fillReport(jasperFile, params, dataSource);
				}
			}
			
			if (reportType.equals("sviKandidatiInstruktora")) {
				params.put("idInstruktora", idInstruktora);
				List<Kandidat> data = km.getSviKandidatiInstruktora(idInstruktora);				
				jasperFile = reportsDirectory + "sviKandidatiInstruktora.jasper";
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