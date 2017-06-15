package converters;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import sifarnici.KategorijaKandidatManagedBean;
import model.Kategorija;

@FacesConverter("kategorijaKandidatConverter")
public class KategorijaKandidatConverter implements Converter{
	
	public KategorijaKandidatConverter(){}
	
	@Override
	public Object getAsObject(FacesContext context,UIComponent arg1,String arg2){
		KategorijaKandidatManagedBean katKandMB = context.getApplication().
				evaluateExpressionGet(context, "#{kategorijaKandidatManagedBean}", KategorijaKandidatManagedBean.class);
		for(Kategorija kat : katKandMB.getSveKategorije()){
			if(String.valueOf(kat.getIdKategorija()).equals(arg2)){
				return kat;
			}
		}
		throw new ConverterException(new FacesMessage(String.format("Can not convert "+arg2+" to kategorija")));
	}//getAsObject
	
	@Override
	public String getAsString(FacesContext arg0,UIComponent arg1,Object arg2){
		if(arg2==null){
			return null;
		}else{
			Kategorija kat = (Kategorija)arg2;
			return String.valueOf(kat.getIdKategorija());
		}
	}//getAsString
}
