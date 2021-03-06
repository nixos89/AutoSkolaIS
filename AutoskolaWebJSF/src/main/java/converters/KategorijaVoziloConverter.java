package converters;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import sifarnici.KategorijaVoziloManagedBean;
import model.Kategorija;

@FacesConverter("kategorijaVoziloConverter")
public class KategorijaVoziloConverter implements Converter{
	
	public KategorijaVoziloConverter(){}
	
	@Override
	public Object getAsObject(FacesContext context,UIComponent arg1,String arg2){
		KategorijaVoziloManagedBean katVozMB = context.getApplication().
				evaluateExpressionGet(context, "#{kategorijaVoziloManagedBean}", KategorijaVoziloManagedBean.class);
		for(Kategorija kat : katVozMB.getSveKategorije()){
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
